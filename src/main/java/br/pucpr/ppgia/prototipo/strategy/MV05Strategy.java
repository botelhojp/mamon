package br.pucpr.ppgia.prototipo.strategy;

import java.util.Date;

import br.pucpr.ppgia.prototipo.agents.CronosAgent;
import br.pucpr.ppgia.prototipo.core.Environment;
import br.pucpr.ppgia.prototipo.vo.Acao;

public class MV05Strategy extends AbstractStrategy {
	
	public MV05Strategy(CronosAgent myAgent) {
		super(myAgent);
	}

	/**
	 * Analisa o cruzamento do preço com a média móvel 5
	 */
	@Override
	public Action getRecomentation(Acao acao) {
		
		Date hoje = Environment.getInstance().getToday();
		double hojeMV00 = myAgent.getCotacao(acao, hoje).getPreult();
		double hojeMV05 = myAgent.getCotacao(acao, hoje).getPreultmv5();

		if (hojeMV05 == 0)
			return Action.NOTHING;
		
		Date ontem = Environment.getInstance().getAddDay(hoje, -1);				
		double ontemMV00 = myAgent.getCotacao(acao, ontem).getPreult();
		double ontemMV05 = myAgent.getCotacao(acao, ontem).getPreultmv5();
		
		if (ontemMV05 == 0)
			return Action.NOTHING;

		//compra
		if (hojeMV00 > hojeMV05 && ontemMV00 <= ontemMV05){
			return Action.BUY;
		}
		//venda
		if (hojeMV00 < hojeMV05 && ontemMV00 >= ontemMV05){
			return Action.SELL;
		}		
		return Action.NOTHING;
	}
}