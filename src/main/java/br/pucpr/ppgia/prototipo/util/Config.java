package br.pucpr.ppgia.prototipo.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {
	
	private static Properties props = new Properties();
	private static Properties config = new Properties();
	
	static{
		try {
			String arquivo = Config.class.getResource("/application.properties").getFile();			
			props.load( new FileInputStream( arquivo ) );
			String configFile = Config.class.getResource("/" + getFileConfig()).getFile();
			config.load( new FileInputStream( configFile ) );			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	

	/**
	 * Retorna a lista de aquivos zip que serão tratados pelo sistema
	 * @return
	 */
	public static String[] getDataFiles(){
		String[] files = props.getProperty("data.files").split(",");
		return files;
	}
	
	private static String cenario = null;
	
	public static void loadCenario(String pcenario) {
		cenario = pcenario;
		String configFile = Config.class.getResource("/" + pcenario).getFile();
		try {
			config.load( new FileInputStream( configFile ) );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Arquivo com possui os parâmetros dos experimentos
	 * @return
	 */
	public static String getFileConfig() {
		if (cenario == null){
			return props.getProperty("file.config");	
		}else{
			return cenario;
		}
		
	}
	


	/**
	 * Configura o tipo de coleta, client (1) (coleta as informações dos cliente), server (2) (dos servidores, all (3) (ambos)
	 * @return Tipo de modelo
	 */
	public static long getColectModel(){
		return Long.valueOf(config.getProperty("colect.model").trim());  
	}
	
	/**
	 * Informa se os dados serão coletados para todas as fases. Padrão=false
	 * @return
	 */
	public static boolean isShowAllPhase(){
		return Boolean.valueOf(config.getProperty("show.allPhase"));
	}

	/**
	 * Define o valor inicial de cada carteira
	 * @return Valor da carteira
	 */
	public static long getAgentCountInitValue() {
		return Integer.valueOf(config.getProperty("agent.count.initvalue"));
	}
	
	/**
	 * Informa se o comportamento dos servidor mudará conforme o tempo
	 * @return true muda, false caso contrário
	 */
	public static boolean isServerChange(){
		return Boolean.valueOf(config.getProperty("system.serverChange"));
	}	

	/**
	 * Informa a lista de ativos que serão tratados pelo sistema
	 * @return Lista de ativos
	 */
	public static String[] getActives() {
		return config.getProperty("data.actives").trim().split(",");
	}
	
	/**
	 * Tempo mínimo para um Response se computável pelos agentes
	 * @return Tempo em dias
	 */
	public static int getResponseValidMin() {
		return Integer.valueOf(config.getProperty("response.valid.min").trim());
	}

	/**
	 * Tempo máximo para um Response ser computável pelos agentes
	 * @return Tempo em dias
	 */
	public static int getRatingValidMax() {
		return Integer.valueOf(config.getProperty("rating.valid.max").trim());
	}	
	
	/**
	 * Define a periodicidade de para o garbage collection ser acionado 
	 * @return Número de dias
	 */
	public static int getSystemGC() {
		return Integer.valueOf(config.getProperty("system.gc").trim());
	}
	
	/**
	 * Informa que quanto em quanto tepo as informações serão coletadas
	 * para o aquivo de saída
	 * @return
	 */
	public static int getTempoColeta() {
		return Integer.valueOf(config.getProperty("tempo.coleta").trim());
	}	
	
	/**
	 * Retorna o número de grupos de agentes
	 * @return Número de grupos
	 */
	public static int getGroupSize(){
		return Integer.valueOf(config.getProperty("agent.group.size").trim());		
	}		
	
	/**
	 * Periodicidade das mudanças de comportamento dos servidores
	 * @return
	 */
	public static long getChangeDay() {
		return Integer.valueOf(config.getProperty("server.change.days").trim());
	}
	
	/**
	 * Lista de agentes servidores que serão criados na inicialização do sistema
	 * @return Lista de agentes servidores
	 */
	public static String[] getServers(){
		String[] clazz = config.getProperty("create.servers").split(",");
		return clazz;
	}

	/**
	 * Lista de agentes clientes que serão criados na inicialização do sistema
	 * @return Lista de agentes clientes
	 */
	public static String[] getClients() {
		String[] clazz = config.getProperty("create.clients").split(",");
		return clazz;
	}
	
	/**
	 * Janela de tempo do método estocático
	 * @return Número de dias
	 */
	public static int getEstocaticoDays() {
		return Integer.valueOf(config.getProperty("estocatico.days").trim());
	}

	/**
	 * Valor limite
	 * @return Valor limite
	 */
	public static Float getEstocaticoMAX() {
		return Float.valueOf(config.getProperty("estocatico.max").trim());
	}

	/**
	 * Valor base
	 * @return Valor base
	 */
	public static Float getEstocaticoMIN() {
		return Float.valueOf(config.getProperty("estocatico.min").trim());
	}

	/**
	 * Janela de tempo do método Volume
	 * @return
	 */
	public static int getVolumeDays() {
		return Integer.valueOf(config.getProperty("volume.days").trim());
	}


}
