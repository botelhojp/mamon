package br.pucpr.ppgia.prototipo.strategy;

import java.util.Date;

import br.pucpr.ppgia.prototipo.agents.CronosAgent;
import br.pucpr.ppgia.prototipo.core.Environment;
import br.pucpr.ppgia.prototipo.vo.Acao;

public class GoodStrategy extends AbstractStrategy {

	public GoodStrategy(CronosAgent myAgent) {
		super(myAgent);
	}

	@Override
	public Action getRecomentation(Acao acao) {
		Date hoje = Environment.getInstance().getToday();
		double hojeValor = myAgent.getCotacao(acao, hoje).getPreultmv5();
		double hojeMV10 = myAgent.getCotacao(acao, hoje).getPreultmv10();
		
		if (hojeMV10 == 0)
			return Action.NOTHING;

		Date ontem = Environment.getInstance().getAddDay(hoje, -1);				
		double ontemValor = myAgent.getCotacao(acao, ontem).getPreultmv5();
		double ontemMV10 = myAgent.getCotacao(acao, ontem).getPreultmv10();

		//compra
		if (hojeValor > hojeMV10 && ontemValor <= ontemMV10){
			return Action.BUY;
		}
		//venda
		if (hojeValor < hojeMV10 && ontemValor >= ontemMV10){
			return Action.SELL;
		}		
		return Action.NOTHING;
	}
}