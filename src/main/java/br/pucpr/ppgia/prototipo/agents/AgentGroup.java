package br.pucpr.ppgia.prototipo.agents;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import br.pucpr.ppgia.prototipo.core.Environment;
import br.pucpr.ppgia.prototipo.util.Config;
import br.pucpr.ppgia.prototipo.util.Util;

public class AgentGroup {
	
	private List<CronosAgent> clientAgentList;	
	private List<CronosAgent> serverAgentList;
	private long id;
	protected static Logger logger = Logger.getLogger(AgentGroup.class);
	
	/**
	 * Construtor passando o id do grupo
	 * @param id
	 */
	public AgentGroup(long id){
		this.id = id;
		clientAgentList = new ArrayList<CronosAgent>();
		serverAgentList = new ArrayList<CronosAgent>();
	}
	
	public long getId() {
		return id;
	}

	/**
	 * Adicionar um agent no grupo
	 * @param agent Agente que será adicionado
	 */
	public void add(CronosAgent agent){
		if (agent instanceof ClientAgent){
			clientAgentList.add(agent);
		}else if (agent instanceof ServerAgent){
			serverAgentList.add(agent);
		}else{
			throw new RuntimeException("Objeto () nao he um agente");
		}
	}
	
	/**
	 * Retorna lista de agentes clientes
	 * @return Lista de agentes
	 */
	public List<CronosAgent> getClients(){
		return clientAgentList;
	}
		
	/**
	 * Retorna os agentes do tipo servidores
	 * @return
	 */
	public List<CronosAgent> getServers(){
		return serverAgentList;
	}

	public void runDay(Date day) {
		for (CronosAgent agent : serverAgentList) {
			agent.runDay(day);
			debug(agent);
		}
		for (CronosAgent agent : clientAgentList) {
			agent.runDay(day);
			debug(agent);
		}	
	}	
	

	/**
	 * Se o sistema estiver no modo debug, mostra o estado do agente
	 * @param agent Agente a ser debugado
	 */
	private void debug(CronosAgent agent) {
		if (logger.isDebugEnabled()){
			logger.debug(agent.showState());
		}		
	}
	
	/**
	 * Popula o ambiente com agentes
	 */
	public void createAgents() {
		//SERVIDORES			
		for (String clazz : Config.getServers()) {
			String className = clazz.split(":")[0];
			long num = Long.valueOf(clazz.split(":")[1]);
			for(int i = 0; i < num; i++){
				CronosAgent a = Util.createServer(i, className);
				Environment.getInstance().registre(a);				
			}
		}
		
		//CLIENTES			
		for (String clazz : Config.getClients()) {
			String className = clazz.split(":")[0];
			long num = Long.valueOf(clazz.split(":")[1]);
			for(int i = 0; i < num; i++){
				CronosAgent a = Util.createClient(i, className);
				Environment.getInstance().registre(a);				
			}
		}
	}
}
