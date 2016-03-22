package br.pucpr.ppgia.prototipo.strategy;

import java.util.ArrayList;
import java.util.Date;

import br.pucpr.ppgia.prototipo.agents.CronosAgent;
import br.pucpr.ppgia.prototipo.core.Environment;
import br.pucpr.ppgia.prototipo.vo.Acao;
import br.pucpr.ppgia.prototipo.vo.Cotacao;

public class PerfectStrategy extends AbstractStrategy {

	ArrayList<Cotacao> cotacoesAnteriores = new ArrayList<Cotacao>();
	
	public PerfectStrategy(CronosAgent myAgent) {
		super(myAgent);
	}
	
	@Override
	public Action getRecomentation(Acao acao) {		
		Date today = Environment.getInstance().getToday();
		Date nextDay = Environment.getInstance().getAddDay(today, 5);
		
		if ( nextDay == null)
			return Action.NOTHING;
		
		Cotacao cotacaoAtual = myAgent.getCotacao(acao, today);
		Cotacao cotacaoAmanha = myAgent.getCotacao(acao, nextDay);
			
		if (cotacaoAmanha.getPreult() < cotacaoAtual.getPreult()){
			return Action.SELL;
		}else if (cotacaoAmanha.getPreult() > cotacaoAtual.getPreult()){
			return Action.BUY;
		}else{
			return Action.NOTHING;	
		}
	}
}