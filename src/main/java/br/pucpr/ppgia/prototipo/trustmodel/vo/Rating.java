package br.pucpr.ppgia.prototipo.trustmodel.vo;

import java.util.Date;

import org.apache.log4j.Logger;

import br.pucpr.ppgia.prototipo.agents.CronosAgent;
import br.pucpr.ppgia.prototipo.core.Environment;
import br.pucpr.ppgia.prototipo.util.Config;

public class Rating implements Comparable<Object> {
	
	private static final int DAY_COMPUTABLE_MAX = Config.getRatingValidMax();
	private CronosAgent agent; 
	private Double valor;
	private Date time;
	private Date responseDate;


	private String term;
	
	protected static Logger logger = Logger.getLogger(Rating.class);
	
	/**
	 * 
	 * @param agent Agente que enviou a avaliação
	 * @param valor Valor da avaliação
	 * @param time Dia em que foi realizada a avaliação
	 * @param responseDate Dia que o response foi gerado
	 */
	public Rating(CronosAgent agent, Double valor, Date time, String term, Date responseDate) {
		super();
		this.agent = agent;
		this.valor = valor;
		this.time = time;
		this.term = term;	
		this.responseDate = responseDate;
	}

	/**
	 * Retorna o agente emissor do Rating
	 * @return
	 */
	public CronosAgent getAgent() {
		return agent;
	}
	
	/**
	 * Termo que o rating foi gerado
	 * @return Nome do termo
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * Retorna o valor do rating em função do valor original do rating e o tempo.
	 * O tempo é calculado a partir da data de emissão do rating com a data atual do sistema
	 * @return Valor do rating
	 */
	public Double getValor() {
		//long dias = 1 + Environment.getInstance().getNumDays(getDate());
		//return valor / dias;
		//TODO REMOVER COMENTÁRIO
		return valor;
	}

	/**
	 * Retorna a data em que o rating foi gerado
	 * @return
	 */
	public Date getDate() {
		return time;
	}
	
	public String toString(){
		return getAgent().getName() + "|" +  getValor().toString() + "|" + time.toString() + "|" + getTerm();
	}

	public int compareTo(Object obj) {
		Rating other = (Rating) obj;
		return getValor().compareTo(other.getValor());
	}

	/**
	 * Informa se a resposta está expirada por tempo
	 * @return
	 */
	public boolean isExpired(){
		long num = Environment.getInstance().getNumDays(getDate());		
		return (num > DAY_COMPUTABLE_MAX);		
	}
	
	@Override
	protected void finalize() throws Throwable {
		//logger.debug("finalize");
		super.finalize();
	}
	
	public Date getResponseDate() {
		return responseDate;
	}
	
}
