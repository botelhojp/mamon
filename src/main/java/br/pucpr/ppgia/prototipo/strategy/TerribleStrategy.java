package br.pucpr.ppgia.prototipo.strategy;

import java.util.Date;

import br.pucpr.ppgia.prototipo.agents.CronosAgent;
import br.pucpr.ppgia.prototipo.core.Environment;
import br.pucpr.ppgia.prototipo.vo.Acao;
import br.pucpr.ppgia.prototipo.vo.Cotacao;

public class TerribleStrategy extends AbstractStrategy {

	public TerribleStrategy(CronosAgent myAgent) {
		super(myAgent);
	}

	@Override
	public Action getRecomentation(Acao acao) {
		Date today = Environment.getInstance().getToday();
		Date tomorrow = Environment.getInstance().getAddDay(today, 5);
		
		if ( tomorrow == null)
			return Action.NOTHING;
		
		Cotacao cotacaoAtual = myAgent.getCotacao(acao, today);
		Cotacao cotacaoAmanha = myAgent.getCotacao(acao, tomorrow);
			
		
		if (cotacaoAmanha.getPreult() < cotacaoAtual.getPreult()){
			return Action.BUY;
		}else if (cotacaoAmanha.getPreult() > cotacaoAtual.getPreult()){
			return Action.SELL;
		}else{
			return Action.NOTHING;	
		}
	}
}
