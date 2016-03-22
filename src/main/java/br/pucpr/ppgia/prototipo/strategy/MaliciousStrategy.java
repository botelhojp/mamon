package br.pucpr.ppgia.prototipo.strategy;

import br.pucpr.ppgia.prototipo.agents.CronosAgent;
import br.pucpr.ppgia.prototipo.vo.Acao;

public class MaliciousStrategy extends PerfectStrategy  {

	public MaliciousStrategy(CronosAgent myAgent) {
		super(myAgent);
	}
	
	
	private long cont = 0;
	@Override
	public Action getRecomentation(Acao acao) {
		Action opinion = super.getRecomentation(acao);
		if (cont++ % 15 == 0){ // Permite que ele acerte e ganhe alguns ratings positivos
			return opinion;
		}else{
			switch (opinion) {
				case BUY:	return Action.SELL; 
				case SELL:	return Action.BUY;
				default:	return Action.NOTHING;
			}
		}
	}
}
