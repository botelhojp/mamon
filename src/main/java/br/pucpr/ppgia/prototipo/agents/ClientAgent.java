package br.pucpr.ppgia.prototipo.agents;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import br.pucpr.ppgia.prototipo.core.Environment;
import br.pucpr.ppgia.prototipo.trustmodel.ITrust;
import br.pucpr.ppgia.prototipo.trustmodel.vo.Rating;
import br.pucpr.ppgia.prototipo.trustmodel.vo.Response;
import br.pucpr.ppgia.prototipo.util.Config;
import br.pucpr.ppgia.prototipo.util.Format;
import br.pucpr.ppgia.prototipo.util.Util;
import br.pucpr.ppgia.prototipo.vo.Acao;
import br.pucpr.ppgia.prototipo.vo.Count;


/**
 * Classe de agentes clientes, consumidores de servicos
 * @author vanderson
 */
public class ClientAgent extends CronosAgent{
	
	private static final long COUNT_INIT_VALUE = Config.getAgentCountInitValue();
	private Hashtable<Acao, Count> actives;	
	private ITrust trust;
	protected Hashtable<CronosAgent, List<Response>>  listResponse = new Hashtable<CronosAgent, List<Response>>();
	private Date today;
	
	protected static Logger logger = Logger.getLogger(ClientAgent.class);
	
		
	/**
	 * Construtor
	 * @param name
	 */
	public ClientAgent(String name) {				
		super(name);
		actives = new Hashtable<Acao, Count>();
		ratings = new Hashtable<String, List<Rating>>();
		initCounts();
	}


	/**
	 * Inicializa as contas em função das ações que serão negociadas
	 */
	public void initCounts() {
		actives.clear();
		for (Acao acao: getAcoes()){			
			actives.put(acao, new Count(acao, COUNT_INIT_VALUE));
		}
	}

	/**
	 * Retorna a coleção de contas
	 * @return
	 */
	public Hashtable<Acao, Count> getContas() {		
		return actives;
	}

	/**
	 * Método para acionar o agente.
	 * � chamado todos os dias pelo ambiente
	 */
	@Override
	public void runDay(Date today) {	
		this.today = today;
		if (Environment.getInstance().getPhase().isChanged()){
			initCounts(); 
		}
		trust.runDay(today);				
	}

	/**
	 * Apresenta a situação financeira do agente
	 */
	public void show() {
		System.out.println("--------------------------------------------");
		for (Acao acao : getAcoes()) {			
			double init = actives.get(acao).getInitCash();
			double cash = actives.get(acao).getCash();
			double acaoCash = actives.get(acao).getAcaoCash();
			double total = cash + acaoCash;
			System.out.println(getName() + "/" + acao.getNomeres() + ": Inicial: " + init + " Dinheiro (" + Format.getDouble2(cash) + ") Acoes (" + Format.getDouble2(acaoCash) + ") Total ("+ Format.getDouble2(total) +") Rendimento (" + actives.get(acao).getRendimento()+ "%) Rendimento Total: " + getRendimentoTotal() + "%");			
		}
	}	
	
	/**
	 * Calcula o rendimento total do agente para o dia corrente
	 * @return Rendimento
	 */
	public double getRendimentoTotal(){
		double dinicial = 0;
		double dfinal = 0;
		for (Acao acao : getAcoes()) {	
			dinicial += actives.get(acao).getInitCash();
			dfinal += actives.get(acao).getTotal();			
		}
		return Format.getDouble2(((dfinal-dinicial)/dinicial)) * 100;		
	}
	
	/**
	 * Este método não tratado pelo cliente pois o cliente não
	 * responde serviço
	 */
	@Override
	public List<Response> getService() {
		throw new RuntimeException("Agente Cliente não responde serviço");		
	}


	/**
	 * Retoran a lista de servidores que o agente cliente está interagindo
	 * @return Lista de agentes servidores
	 */
	public List<CronosAgent> getServers(){
		return group.getServers();
	}
	
	/**
	 * Salva uma resposta enviada pelo servidor
	 * @param resp
	 */
	public void saveResponse(Response resp) {
		CronosAgent server = resp.getFromAgent();
		if (listResponse.containsKey(server)){
			listResponse.get(server).add(resp);
		}else{
			List<Response> lista = new ArrayList<Response>();
			lista.add(resp);
			listResponse.put(server, lista);
		}
	}	
	

	/**
	 * Procurar o proximo response que pode servir com rating.
	 * Quanto este response é encontra o respons é eliminado.
	 * @param server
	 * @return
	 */
	public List<Rating> getNextRating(CronosAgent server){		
		List<Response> listAll = listResponse.get(server);		
		List<Response> listToRemove = new ArrayList<Response>();
		List<Rating> listReturn = new ArrayList<Rating>();
		if (listAll != null){
			for (Response resp : listAll) {			
				if (resp.isComputable()){
					listToRemove.add(resp);	 //remove response tratado	
					try{
						if (resp.getUtility() != 0){
							listReturn.add(new Rating(this, resp.getUtility(), today, resp.getTerm(), resp.getDate()));						
						}
					}catch (NullPointerException e) {
						logger.info("NullPointerException");
					}
				}
			}			
		}
		Util.remove(listAll, listToRemove);
		return listReturn;		
	}
	
	
	
	/**
	 * Envia a utilidade do servidor.
	 * As utilidades neutras não serão enviadas.
	 * @param serverAgent
	 */
	public void sendUtility(CronosAgent serverAgent){
		List<Rating> ratingList = getNextRating(serverAgent);
		for (Rating rating : ratingList) {
			if (ratingList != null){
				((ServerAgent)serverAgent).receiveUtility(rating);
				saveRating(serverAgent, rating.getTerm(), rating);
			}
		}
	}
	
	/**
	 * Salva um rating
	 * @param server 
	 * @param rating
	 */
	private void saveRating(CronosAgent server, String term, Rating rating) {
		if (rating != null ){			
			if (ratings.containsKey(getKey(server, term))){
				ratings.get(getKey(server, term)).add(rating);
			}else{
				ArrayList<Rating> lista = new ArrayList<Rating>();
				lista.add(rating);
				ratings.put(getKey(server, term), lista);
			}	
		}
	}


	/**
	 * Apresenta o estado do agente cliente
	 */
	@Override
	public String showState() {		
		int response = 0;
		int ratings = 0;
		Enumeration<List<Response>> listaResp = listResponse.elements();
		while (listaResp.hasMoreElements()) {
			List<Response> elem = (List<Response>) listaResp.nextElement();
			response += elem.size();			
		}
		Enumeration<List<Rating>> listaRating = super.ratings.elements();
		while (listaResp.hasMoreElements()) {
			List<Rating> elem = (List<Rating>) listaRating.nextElement();
			ratings += elem.size();			
		}	
		return this.getName()+ " - servers[" + getServers().size() + "] contas[" + actives.size() + "] responses[" + response + "] rating[" + ratings + "]";
	}

	/**
	 * Retorna a lista de ações que são tratadas pelo agente
	 * @return
	 */
	public List<Acao> getAcoes() {		
		return Environment.getInstance().getAcoes();
	}


	public void setTrust(ITrust trust2) {
		this.trust = trust2;
	}	
}
