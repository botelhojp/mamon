package br.pucpr.ppgia.prototipo.strategy;

import java.util.Date;

import br.pucpr.ppgia.prototipo.agents.CronosAgent;
import br.pucpr.ppgia.prototipo.core.Environment;
import br.pucpr.ppgia.prototipo.vo.Acao;

public class MV20Strategy extends AbstractStrategy {
	
	public MV20Strategy(CronosAgent myAgent) {
		super(myAgent);
	}
	
	/**
	 * Analisa o cruzamento da média móvel 20 com o cruzamento da média
	 * movel 5
	 */
	@Override
	public Action getRecomentation(Acao acao) {
		Date hoje = Environment.getInstance().getToday();
		double hojeMV10 = myAgent.getCotacao(acao, hoje).getPreultmv10();
		double hojeMV20 = myAgent.getCotacao(acao, hoje).getPreultmv20();

		if (hojeMV20 == 0)
			return Action.NOTHING;
		
		Date ontem = Environment.getInstance().getAddDay(hoje, -1);				
		double ontemMV10 = myAgent.getCotacao(acao, ontem).getPreultmv10();
		double ontemMV20 = myAgent.getCotacao(acao, ontem).getPreultmv20();
		
		if (ontemMV20 == 0)
			return Action.NOTHING;

		//compra
		if (hojeMV10 > hojeMV20 && ontemMV10 <= ontemMV20){
			return Action.BUY;
		}
		//venda
		if (hojeMV10 < hojeMV20 && ontemMV10 >= ontemMV20){
			return Action.SELL;
		}		
		return Action.NOTHING;
	}
}