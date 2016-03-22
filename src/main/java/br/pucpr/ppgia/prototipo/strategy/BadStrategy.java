package br.pucpr.ppgia.prototipo.strategy;

import br.pucpr.ppgia.prototipo.agents.CronosAgent;
import br.pucpr.ppgia.prototipo.vo.Acao;

public class BadStrategy extends GoodStrategy {


	public BadStrategy(CronosAgent myAgent) {
		super(myAgent);
	}

	@Override
	public Action getRecomentation(Acao acao) {
		Action opinion = super.getRecomentation(acao);
		switch (opinion) {
			case BUY:	return Action.SELL;
			case SELL:	return Action.BUY;
			default: 			return Action.NOTHING;
		}
	}	
}