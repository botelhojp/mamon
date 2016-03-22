package br.pucpr.ppgia.prototipo.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.jfree.data.xy.XYSeries;

import br.pucpr.ppgia.prototipo.agents.AgentGroup;
import br.pucpr.ppgia.prototipo.agents.CronosAgent;
import br.pucpr.ppgia.prototipo.dao.AcaoDAO;
import br.pucpr.ppgia.prototipo.gui.TrustGui;
import br.pucpr.ppgia.prototipo.io.FileOutPut;
import br.pucpr.ppgia.prototipo.io.ImportFile;
import br.pucpr.ppgia.prototipo.strategy.AbstractStrategy;
import br.pucpr.ppgia.prototipo.strategy.IStrategy;
import br.pucpr.ppgia.prototipo.trustmodel.AbstractTrust;
import br.pucpr.ppgia.prototipo.trustmodel.ITrust;
import br.pucpr.ppgia.prototipo.util.Config;
import br.pucpr.ppgia.prototipo.util.Format;
import br.pucpr.ppgia.prototipo.util.Util;
import br.pucpr.ppgia.prototipo.vo.Acao;
import br.pucpr.ppgia.prototipo.vo.Cotacao;

/**
 * Classe que representa o ambiente dos agentes
 * @author Vanderson Botelho
 *
 */
public class Environment {	
	
	private static Environment environment; //instancia única do ambietne
	private static List<Acao> acaoList = new ArrayList<Acao>(); //lista de ação contidas no ambiente	 
	private List<AgentGroup> listAgentGroup; //grupo de agentes
	
	protected static Logger logger = Logger.getLogger(Environment.class);
	
	private long itoday;  	//contador de dias
	private int groupid;	//quarda o ultimo id od grupo solicitado
	private List<Date> time;//lista de dias do ambietne
	private Date today;		//dia corrente
	private FileOutPut result; //escritor dos resultados
	private Phase phase;		//fase corrente do ambiente
	private TrustGui gui;
	
	/**
	 * Construtor privado da classe. 
	 * Inicializa os arquivos de propriedade, se necessário importa algum
	 * arquivo de cotação.
	 */
	private Environment(){
 		ImportFile.importFiles();	
		itoday = 0;
		groupid = 0;
		//
		result = FileOutPut.getInstance();
		phase = new Phase();		
		listAgentGroup = new ArrayList<AgentGroup>();
	}
	
	/**
	 * Retorna um Environment de forma a manter uma �nica instância
	 * desta classe.
	 * @return Environment
	 */
	public static Environment getInstance(){
		if (environment == null)
			environment = new Environment();		
		return environment;
	}
	
	/**
	 * Inicializa a criação do agentes
	 */
	public void init(){
		if (listAgentGroup.isEmpty()){
			createGroups();
		}
	}
	
	/**
	 * Rota o ambiente, ou seja, diariamente manda todos os agentes clientes
	 * executar suas ação diária
	 */
	public void runAllDays(String cenario){
		long time_ini = 0;
		init();		
		time = getTimes();
		logger.info(">> BEGIN << cenario: " + Config.getFileConfig() );
		result.write(makeCabecalho());
		for (Date day : time) {	
			cleanMemory();
			phase.setPhase(day, time);
			setToday(day);
			itoday++;		
			gui.updateDate(day, itoday);
			logger.info("Day [" + itoday + "] " + day + " phase: " + phase + " time[" + Util.countTime(time_ini) + "]");
			time_ini = Calendar.getInstance().getTimeInMillis();
			for (AgentGroup group : listAgentGroup) {
				group.runDay(day);
			}
			writeDay(day);				
		}
		logger.info(">> END << setting: " + Config.getFileConfig() );	
	}
	
	/**
	 * Cria o grupo de agentes
	 */
	private void createGroups() {
		for (long groupId = 0; groupId < Config.getGroupSize(); groupId++) {
			listAgentGroup.add(new AgentGroup(groupId));						
		}
		for (AgentGroup group : listAgentGroup) {
			group.createAgents();			
		}
	}

	/**
	 * Limpa a memoria
	 */
	private void cleanMemory() {
		int dias = Config.getSystemGC();
		if (itoday % dias == 0){			
			logger.debug("freeMemory [" + Runtime.getRuntime().freeMemory() + "/" + Runtime.getRuntime().totalMemory() +"]");
			System.gc();	
			logger.debug("freeMemory [" + Runtime.getRuntime().freeMemory() + "/" + Runtime.getRuntime().totalMemory() +"]");
		}		
	}

	
	/**
	 * Retorna o grupo do agente. Este método é chamado no momento do criação dos agentes
	 * @return
	 */
	public int getGroup(){
		return (groupid++ % Config.getGroupSize());
	}

	/**
	 * Envia o cabeçao do arquivo de saída
	 * @return Cabeçalho
	 */
	private String makeCabecalho() {
		long model = Config.getColectModel();
		StringBuffer sb = new StringBuffer();
		sb.append("day");
		if (model == 1 || model == 3){ //cliente			
			for (String clazz : Config.getClients()) {				
				sb.append(";").append(clazz.split(":")[0]);
			}
		}
		if (model == 2 || model == 3){ //server
			for (String clazz : Config.getServers()) {				
				sb.append(";").append(clazz.split(":")[0]);			
			}	
		}
		return sb.toString();
	}



	/**
	 * Envia a arquivo de saída as informações referentes ao desempenho de cada
	 * modelo de reputação.
	 * @param day Dia
	 */
	private void writeDay(Date day){
		int tempoColeta = Config.getTempoColeta();
		if (itoday % tempoColeta == 0){
			long model = Config.getColectModel();			
			if (Config.isShowAllPhase() || phase.getFase() == 2){
				StringBuffer out = new StringBuffer();
				out.append(Format.getDate(day));
				if (model == 1 || model == 3){ //cliente					
					for (String clazz : Config.getClients()) {
						try {
							Class classe = Class.forName(ITrust.class.getPackage().getName() + "." + clazz.split(":")[0]);
							Double uti = AbstractTrust.getModelUtility(classe);
							XYSeries serie = AbstractTrust.getModelSerie(classe);
							serie.add(itoday, uti);
							out.append(";").append(uti);
						} catch (ClassNotFoundException e) {					
							e.printStackTrace();
						}
					}
				}
				if (model == 2 || model == 3){ //server
					for (String clazz : Config.getServers()) {
						try {
							Class classe = Class.forName(IStrategy.class.getPackage().getName() + "." + clazz.split(":")[0]);
							out.append(";").append(AbstractStrategy.getStrategyUtility(classe));
						} catch (ClassNotFoundException e) {					
							e.printStackTrace();
						}
					}
				}
				result.write(out);
			}
		}
	}
	
	/**
	 * Retorna um dia dado um dia de origem e a quantidade de dias consecutivos
	 * @param currentDay
	 * @param days
	 * @return
	 */
	public Date getAddDay(Date currentDay, int days) {
		int index = time.indexOf(currentDay);
		index = index + (days);
		if (index >= 0 && index < time.size()){
			return time.get(index);
		}else{	
			return null;
		}
	}
	
	/**
	 * Registra um agente em um tipo grupo
	 * @param agent Agente
	 * @param grupo Grupo
	 */
	public void registre(CronosAgent agent) {
		int id = getGroup();
		AgentGroup agentGroup = listAgentGroup.get(id);
		agentGroup.add(agent);
		agent.setGroup(agentGroup);
	}
	
	/**
	 * Retorna a lista de dias contidas no ambiente
	 * @return Coleção de datas
	 */
	private List<Date> getTimes() {
		AcaoDAO acao = AcaoDAO.getInstance();
		List<Cotacao> lista = acao.getCotacoes(getAcoes().get(0));
		List<Date> listaData = new ArrayList<Date>();
		for (Cotacao cotacao : lista) {
			listaData.add(cotacao.getDatapre());			
		}		
		return listaData;
	}
	
	

	
	/**
	 * Retora a lista da ações que serão tratadas pelo simulador
	 * @return Lista de ações
	 */
	public List<Acao> getAcoes(){
		if (acaoList.isEmpty()){		
			AcaoDAO acaoDao = AcaoDAO.getInstance();
			for (String acaoName : Config.getActives()){
				Acao acao = acaoDao.findByName(acaoName);
				if (acao == null){
					logger.info("Acao (" + acaoName + ") não instanciada no banco");					
				}else{
					acaoList.add(acao);
				}
			}
		}
		return acaoList;		
	}
	
	
	/**
	 * Facilitador para acessar um ação pelo nome
	 * @param acaoName Nome da ação
	 * @return Objeto ação
	 */
	private static Hashtable<String, Acao> acaoHash = new Hashtable<String, Acao>(); //Tabela de
	public Acao getAcao(String acaoName){		
		if (acaoHash.isEmpty()){
			for (Acao acao : getAcoes()) {
				acaoHash.put(acao.getNomeres(), acao);
			}
		}
		return acaoHash.get(acaoName);
	}
	
	/**
	 * Retorna o número de dias que o ambiente possui
	 * @return Número da dias
	 */
	public long getNumDays(){
		return time.size();
	}
	
	/**
	 * Retorna o número de dias passados entre duas datas
	 * @param d_begin Data inicial
	 * @param d_end Data final
	 * @return Número de dias
	 */
	public long getNumDays(Date d_begin, Date d_end){
		return time.indexOf(d_end) - time.indexOf(d_begin);
	}
	
	/**
	 * Retorna a data corrente do ambiente
	 * @return Data corrrente
	 */
	public Date getToday(){
		return today;
	}
	
	/**
	 * Altera a data corrente 
	 * @param today Nova data corrente
	 */
	public void setToday(Date today) {
		this.today = today;
	}

	/**
	 * Retorna o número de dias passados 
	 * @param date Data inicial a ser comparada com a data corrente
	 * @return Número de dias
	 */
	public long getNumDays(Date date) {
		return getNumDays(date, getToday());		
	}

	public Phase getPhase() {
		return phase;
	}

	/**
	 * Retorna o dia em valor inteiro. O dia em inteiro representa sua
	 * posição na lista de todos os dias do ambiente
	 * @param day Dia
	 * @return Dia em inteiro
	 */
	public long getDayToInt(Date day) {		
		return time.indexOf(day);
	}

	public void setGUI(TrustGui gui) {
		this.gui = gui;
		
	}
}