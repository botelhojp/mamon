package br.pucpr.ppgia.prototipo.trustmodel.vo;

import java.util.Date;

import org.apache.log4j.Logger;

import br.pucpr.ppgia.prototipo.agents.CronosAgent;
import br.pucpr.ppgia.prototipo.core.Environment;
import br.pucpr.ppgia.prototipo.dao.AcaoDAO;
import br.pucpr.ppgia.prototipo.strategy.Action;
import br.pucpr.ppgia.prototipo.util.Config;
import br.pucpr.ppgia.prototipo.vo.Acao;
import br.pucpr.ppgia.prototipo.vo.Cotacao;

/**
 * Reprenta a responde de um agente servidor para um agente cliente
 * sobre sua indicação de compra ou venda de um papel para um 
 * determinado dia. Esta classe também possui a compacidade de 
 * calcular sua função de utilidade
 * @author Vanderson Botelho
 */
public class Response {

	private static final int DAY_COMPUTABLE_MIN = Config.getResponseValidMin();
	private long id;
		
	protected static Logger logger = Logger.getLogger(Response.class);

	private String term;
	private Action actionType;
	private Date date;
	private CronosAgent fromAgent;
	
	/**
	 * Construtor da classe com todos os campos
	 * @param id Identificar inico da resposta
	 * @param fromAgent Agente Emissor
	 * @param term Tipo de responta, neste caso será a ação
	 * @param value Valor da resposta
	 * @param date Data e hora que a resposta foi emitida
	 */
	public Response(long id, CronosAgent fromAgent, String term, Action value, Date date) {
		super();
		this.id = id;
		this.fromAgent = fromAgent;
		this.term = term;
		this.actionType = value;
		this.date = date;
	}
	
	/**
	 * Calcula a função de utilidade de resposta.
	 * O valor deve está entre -1 e 1. Zero é elemento neutro
	 * 
	 * @return
	 */
	public double getUtility(){
		Date today = Environment.getInstance().getToday();
		Cotacao cotacao_emissao = AcaoDAO.getInstance().getCotacao(new Acao(term), date);
		Cotacao cotacao_atual = AcaoDAO.getInstance().getCotacao(new Acao(term), today);
		int comp_atual_anterior = cotacao_atual.compareTo(cotacao_emissao);
		double sinal = 0d;		
		switch (actionType) {
			case BUY:
				sinal = ( comp_atual_anterior > 0 ) ? 1 : -1;
				break;
			case SELL:
				sinal = ( comp_atual_anterior < 0 ) ? 1 : -1;
				break;
		}
		return sinal;
	}	

	public long getId() {
		return id;
	}
	public CronosAgent getFromAgent() {
		return fromAgent;
	}

	public String getTerm() {
		return term;
	}
	public Action getValue() {
		return actionType;
	}
	
	/**
	 * Dia em que o response foi criado
	 * @return
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Método especifica para a aplicação da bolsa de valores onde o campo term represetna o tipo
	 * da ação que o agente requisitou. Acesso o objetco Enviroment para retorna a ação
	 * @return
	 */
	public Acao getAcao() {
		return Environment.getInstance().getAcao(this.term);
	}
	
	public boolean isComputable() {
		long days = Environment.getInstance().getNumDays(getDate());		
		return (days >= DAY_COMPUTABLE_MIN );
	}
	
	@Override
	protected void finalize() throws Throwable {
		//logger.debug("finalize id[" + getId() + "]");
		super.finalize();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Response other = (Response) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
