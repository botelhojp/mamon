package br.pucpr.ppgia.prototipo.trustmodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.jfree.data.xy.XYSeries;

import br.pucpr.ppgia.prototipo.agents.ClientAgent;
import br.pucpr.ppgia.prototipo.agents.CronosAgent;
import br.pucpr.ppgia.prototipo.core.Environment;
import br.pucpr.ppgia.prototipo.core.Phase;
import br.pucpr.ppgia.prototipo.trustmodel.vo.Response;
import br.pucpr.ppgia.prototipo.vo.Acao;

public abstract class AbstractTrust implements ITrust {
	
	protected Phase phase;		
	protected ClientAgent myAgent;	
	protected Date today;
	protected Hashtable<Acao, ArrayList<Response>> responseList = new Hashtable<Acao, ArrayList<Response>>();
	
	protected static Logger logger = Logger.getLogger(AbstractTrust.class);
	
	@SuppressWarnings("unchecked")
	protected static Hashtable<Class, ArrayList<ITrust>> instances = new Hashtable<Class, ArrayList<ITrust>>();
	@SuppressWarnings("unchecked")
	protected static Hashtable<Class, XYSeries> series = new Hashtable<Class, XYSeries>();
	
	/**
	 * Calcula a função de utilidade do modelo baseado em cada instancia criada
	 * @param clazzModel Classe do modelo de confiança
	 * @return Função de Utilidade em forma de rentabilidade
	 */
	@SuppressWarnings("unchecked")
	public static Double getModelUtility(Class clazzModel){
		double sum = 0d;		
		if (!instances.containsKey(clazzModel)){
			return 0d;
		}		
		ArrayList<ITrust> trusts = instances.get(clazzModel);
		for (ITrust trust : trusts) {
			sum += ((ClientAgent)trust.getAgent()).getRendimentoTotal();
		}
		return ((trusts.size() > 0) ? sum/trusts.size() : null);
	}
	
	@SuppressWarnings("unchecked")
	public static XYSeries getModelSerie(Class clazzModel){				
		if (!series.containsKey(clazzModel)){
			return null;
		}		
		return series.get(clazzModel);
	}

	/**
	 * Retorna o agente propriet�rio do modelo de confian�a corrente
	 */
	public ClientAgent getAgent() {
		return myAgent;
	}

	/**
	 * Construtor padrão, onde a inst�ncia � cadastrada na classe para avaliação posterior
	 * do modelo
	 * @param agent Agente propriet�rio do modelo
	 */
	public AbstractTrust(ClientAgent agent){		
		this.myAgent = agent;
		if (instances.containsKey(this.getClass())){
			instances.get(this.getClass()).add(this);
		}else{
			ArrayList<ITrust> trusts = new ArrayList<ITrust>();
			trusts.add(this);
			instances.put(this.getClass(), trusts);
			series.put(this.getClass(), new XYSeries(this.getClass().getSimpleName()));
		}
	}
	
	
	
	
	/**
	 * Executa as ações referente ao dia corrente
	 * Chama ações para cada servidor da lista � conta corrente
	 */
	public final void runDay(Date today){
		beforeActionServer(today);
		phase = Environment.getInstance().getPhase();
		List<CronosAgent> servers = myAgent.getServers();
		for (CronosAgent serverAgent : servers) {
			actionServer(serverAgent);
		}
		try{
			afterActionServer(today);
		}catch (NullPointerException e) {
			logger.info("metodo: afterActionServer. " + e.getMessage());
		}
	}
	
	
	
	
	/**
	 * Método que será executado antes do runDay
	 * @param today
	 */
	public void beforeActionServer(Date today){
		responseList.clear();
	}

	
	/**
	 * Método sera chamado para todos os servidores
	 * @param server
	 */
	protected void actionServer(CronosAgent server){
		List<Response> respList = server.getService();		
		for (Response response : respList) {
			Acao acao = response.getAcao();
			if (responseList.containsKey(acao)){
				responseList.get(acao).add(response);
			}else{
				ArrayList<Response> lista = new ArrayList<Response>();
				lista.add(response);
				responseList.put(acao, lista);
			}
			myAgent.saveResponse(response);
			myAgent.sendUtility(server);			
		}
	}

	/**
	 * Ação que será executada depois do runDay
	 * @param today
	 */
	public abstract void afterActionServer(Date today);

	@SuppressWarnings("unchecked")
	public static Hashtable<Class, XYSeries> getSeries() {
		return series;
	}

}
