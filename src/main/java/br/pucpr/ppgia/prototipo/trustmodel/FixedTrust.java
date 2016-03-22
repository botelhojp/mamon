package br.pucpr.ppgia.prototipo.trustmodel;

import java.util.Date;

import br.pucpr.ppgia.prototipo.agents.ClientAgent;
import br.pucpr.ppgia.prototipo.agents.CronosAgent;
import br.pucpr.ppgia.prototipo.core.Environment;
import br.pucpr.ppgia.prototipo.vo.Acao;

public class FixedTrust extends AbstractTrust{
	
	private boolean stop = false;
	
	public FixedTrust(ClientAgent agent) {
		super(agent);
	}
	
	@Override
	public void beforeActionServer(Date today){		
	}
	
	@Override
	public void actionServer(CronosAgent server) {		
	}

	@Override
	public void afterActionServer(Date today) {
		if (Environment.getInstance().getPhase().isChanged()){
			stop = false;
			myAgent.initCounts();
		}		
		if (stop == false){
			for (Acao acao : myAgent.getAcoes()) { //Analisa a resposta para cada ação				
				if (!myAgent.getContas().get(acao).buy()){
					stop = true;
				}			
			}
		}
	}
}
