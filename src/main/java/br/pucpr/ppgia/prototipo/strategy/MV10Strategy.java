package br.pucpr.ppgia.prototipo.strategy;

import java.util.Date;

import br.pucpr.ppgia.prototipo.agents.CronosAgent;
import br.pucpr.ppgia.prototipo.core.Environment;
import br.pucpr.ppgia.prototipo.vo.Acao;

public class MV10Strategy extends AbstractStrategy {
	
	public MV10Strategy(CronosAgent myAgent) {
		super(myAgent);
	}
	
	/**
	 * Analisa o cruzamento da média móvel 10 com o cruzamento da média
	 * movel 5
	 */
	@Override
	public Action getRecomentation(Acao acao) {
		Date hoje = Environment.getInstance().getToday();
		double hojeMV05 = myAgent.getCotacao(acao, hoje).getPreultmv5();
		double hojeMV10 = myAgent.getCotacao(acao, hoje).getPreultmv10();

		if (hojeMV10 == 0)
			return Action.NOTHING;
		
		Date ontem = Environment.getInstance().getAddDay(hoje, -1);				
		double ontemMV05 = myAgent.getCotacao(acao, ontem).getPreultmv5();
		double ontemMV10 = myAgent.getCotacao(acao, ontem).getPreultmv10();
		
		if (ontemMV10 == 0)
			return Action.NOTHING;
		//compra
		if (ontemMV05 <= ontemMV10 && hojeMV05 > hojeMV10){
			return Action.BUY;
		}
		//venda
		if (ontemMV05 >= ontemMV10 && hojeMV05 < hojeMV10){
			return Action.SELL;
		}		
		return Action.NOTHING;
	}
}