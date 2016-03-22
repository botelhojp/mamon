package br.pucpr.ppgia.prototipo.trustmodel;

import java.util.Date;
import java.util.List;

import br.pucpr.ppgia.prototipo.agents.ClientAgent;
import br.pucpr.ppgia.prototipo.trustmodel.vo.Response;
import br.pucpr.ppgia.prototipo.vo.Acao;

public class NoTrust extends AbstractTrust {
	
	public NoTrust(ClientAgent agent) {
		super(agent);
	}
	
	/**
	 * Ação executada ao término do dia
	 * Neste momento o agente analisar a indicações dos servidores
	 * e decidir o que será feito.
	 */
	@Override
	public void afterActionServer(Date today) {
		double buy = 0d, sell = 0d;
		for (Acao acao : myAgent.getAcoes()) { //Analisa a resposta para cada ação sem calcular os ratings	
			List<Response> responses = responseList.get(acao);
			for (Response resp : responses) {
				switch (resp.getValue()) 
				{
					case BUY:
						buy += 1.0;				
						break;				
					case SELL:
						sell += 1.0;
						break;				
					default:
						break;
				}							
			}
			if (buy > sell){
				myAgent.getContas().get(acao).buy();
			}			
			if (sell > buy){
				myAgent.getContas().get(acao).sell();
			}
		}
	}
}
