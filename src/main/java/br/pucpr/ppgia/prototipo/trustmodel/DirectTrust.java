package br.pucpr.ppgia.prototipo.trustmodel;

import java.util.Date;
import java.util.List;

import br.pucpr.ppgia.prototipo.agents.ClientAgent;
import br.pucpr.ppgia.prototipo.trustmodel.vo.Response;
import br.pucpr.ppgia.prototipo.vo.Acao;

public class DirectTrust extends AbstractTrust{
	
	public DirectTrust(ClientAgent agent) {
		super(agent);		
	}
	
	/**
	 * Ação executada ao término do dia
	 * Neste momento o agente analisar a indicações dos servidores
	 * e decidir o que será feito.
	 */
	@Override
	public void afterActionServer(Date today) {
		double buy = 0d, sell = 0d, nothing = 0d;
		for (Acao acao : myAgent.getAcoes()) { //Analisa a resposta para cada ação	
			List<Response> responses = responseList.get(acao);
			for (Response resp : responses) {
				double ratingV = myAgent.getUtility(resp.getFromAgent(), resp.getTerm());
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
			//if (sell > buy && sell > nothing){
			if (sell > buy){
				myAgent.getContas().get(acao).sell();
			}
		}
	}
}
