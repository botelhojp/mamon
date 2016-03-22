package br.pucpr.ppgia.prototipo.strategy;

import br.pucpr.ppgia.prototipo.agents.ServerAgent;
import br.pucpr.ppgia.prototipo.vo.Acao;

public interface IStrategy {
	
	public Action getRecomentation(Acao acao);
	
	public ServerAgent getAgent();
	
}
