package br.pucpr.ppgia.prototipo.agents;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import br.pucpr.ppgia.prototipo.dao.AcaoDAO;
import br.pucpr.ppgia.prototipo.trustmodel.vo.Rating;
import br.pucpr.ppgia.prototipo.trustmodel.vo.Response;
import br.pucpr.ppgia.prototipo.util.Util;
import br.pucpr.ppgia.prototipo.vo.Acao;
import br.pucpr.ppgia.prototipo.vo.Cotacao;

/**
 * Classe que represta um agente
 * @author Vanderson Botêlho
 */
public abstract class CronosAgent {
	
	protected AgentGroup group; //Grupo onde o agente atuará. Servidores e consumidores deverão está no mesmo grupo
	protected String name; 		//Nome do agente
	
	protected Hashtable<String, List<Rating>> ratings; //Guarda um grupo de ratings para um terminado termo
	
	public abstract String showState();
	
	/**
	 * Controi um agente recebendo seu nome
	 * @param name Nome do agente
	 */
	public CronosAgent(String name) {
		this.name = name;	
	}

	/**
	 * Retorna o nome do agente
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Executa as ações para um determinado dia
	 * @param today Dia corrente
	 */
	public abstract void runDay(Date today);

	/**
	 * Retorna uma lista de resposta para um serviço
	 * @return
	 */
	public abstract List<Response> getService();
	
	/**
	 * Retorna a cotação de uma ação para um determinado dia
	 * @param acao
	 * @param hoje
	 * @return
	 */
	public Cotacao getCotacao(Acao acao, Date hoje) {		
		return AcaoDAO.getInstance().getCotacao(acao, hoje);
	}
	
	/**
	 * Retorna uma chave baseada na nome do agente e no termo
	 * @param agent
	 * @param term
	 * @return
	 */
	public String getKey(CronosAgent agent, String term){
		return agent.getName() + "_" + term;
	}
	
	/**
	 * Mofidifica o grupo do agente
	 * @param agentGroup
	 */
	public void setGroup(AgentGroup agentGroup) {
		this.group = agentGroup;		
	}
	
	/**
	 * Calcula e retorna a função de utilidade de um agente.
	 * A função representa a média das função de utilidade de cada Rating
	 * Os valores vão de -1 a 1. O 0 representa o valor neutro da utilidade
	 * @param agent Agente
	 * @return Valor média da função de utilidade
	 */	
	public Double getUtility(CronosAgent server, String term){
		int count_rating = 0; //número de ratings que serão comtabilizados
		Double utility_value = 0d;		
		List<Rating> listAll = ratings.get(getKey(server, term));
		List<Rating> listToRemove = new ArrayList<Rating>();
		if (listAll != null){		
			for (Rating rating : listAll) {			
				if (rating.isExpired()){
					listToRemove.add(rating);			
				}else{
					count_rating++;			
					utility_value += rating.getValor();					
				}			
			}			
		}
		utility_value = (count_rating > 0 ? utility_value / count_rating : null);
		Util.remove(listAll, listToRemove);
		return utility_value;		
	}
	
	/**
	 * Retorna o grupo do agente
	 * @return
	 */
	public AgentGroup getGroup() {
		return group;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CronosAgent other = (CronosAgent) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}