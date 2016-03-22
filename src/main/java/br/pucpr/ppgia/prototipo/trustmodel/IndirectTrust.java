package br.pucpr.ppgia.prototipo.trustmodel;

import java.util.Date;
import java.util.List;

import br.pucpr.ppgia.prototipo.agents.ClientAgent;
import br.pucpr.ppgia.prototipo.agents.CronosAgent;
import br.pucpr.ppgia.prototipo.agents.ServerAgent;
import br.pucpr.ppgia.prototipo.trustmodel.vo.Rating;
import br.pucpr.ppgia.prototipo.trustmodel.vo.Response;
import br.pucpr.ppgia.prototipo.vo.Acao;

public class IndirectTrust extends AbstractTrust {

	
	public IndirectTrust(ClientAgent agent) {
		super(agent);
	}
	
	@Override
	public void beforeActionServer(Date today) {	
		super.responseList.clear();
	}
	
	

	@Override
	public void afterActionServer(Date today) {	
		double buy = 0d, sell = 0d, nothing = 0d;
		for (Acao acao : myAgent.getAcoes()) { //Analisa a resposta para cada ação	
			List<Response> responses = responseList.get(acao);
			for (Response resp : responses) {
				double ratingV = getRatingValue(resp.getFromAgent(), resp.getTerm());
				switch (resp.getValue()) 
				{
					case BUY:
						buy += ratingV;						
						break;				
					case SELL:
						sell += ratingV;
						break;
					case NOTHING:
						nothing += ratingV;
						break;				
					default:
						break;
				}							
			}
			//if (buy > sell && buy > nothing){
			if (buy > sell){
				myAgent.getContas().get(acao).buy();
			}			
			//if (sell > buy && buy > nothing){
			if (sell > buy){
				myAgent.getContas().get(acao).sell();
			}
		}
	}	

	/**
	 * Retorna o valor médio do Rating para um certo agente em um determinado termo
	 * @param server Agente
	 * @param term Termo a ser avaliado
	 * @return Média do ratings
	 */
	private final double getRatingValue(CronosAgent server, String term) {
		List<Rating> ratings = getRatings((ServerAgent)server, term);
		double contador = 0d;
		double soma = 0d;
		if (ratings != null){
			for (Rating rating : ratings) {
				contador++;
				soma += (Double) rating.getValor();
			}		
		}
		return (contador > 0d ? soma / contador : 0);
	}

	/**
	 * Retorna uma lista de ratings de um agente para um certo termo
	 * Sobrescrito pelo Cryp_trust
	 * @param server Agente que possui seu rating
	 * @param term termo a ser avaliado
	 * @return lista de ratings
	 */
	protected List<Rating> getRatings(ServerAgent server, String term){
		return server.getRatings(term);
	}
}
