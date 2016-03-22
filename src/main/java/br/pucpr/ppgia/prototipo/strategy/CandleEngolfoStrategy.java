package br.pucpr.ppgia.prototipo.strategy;

import java.util.Date;

import br.pucpr.ppgia.prototipo.agents.CronosAgent;
import br.pucpr.ppgia.prototipo.core.Environment;
import br.pucpr.ppgia.prototipo.strategy.vo.Candle;
import br.pucpr.ppgia.prototipo.vo.Acao;
import br.pucpr.ppgia.prototipo.vo.Cotacao;

/**
 * Método baseado em volume, quando o volume sobe
 * 
 * @author vanderson
 */
public class CandleEngolfoStrategy extends AbstractStrategy {

	
	
	private Candle cd = new Candle();
	private Cotacao cotacao;

	public CandleEngolfoStrategy(CronosAgent myAgent) {
		super(myAgent);
	}

	/**
	 * Compra o comportamento do volume com o preco
	 */
	@Override
	public Action getRecomentation(Acao acao) {
		Date diaAtual = Environment.getInstance().getToday();
		cotacao = myAgent.getCotacao(acao, diaAtual);
		cd.load(cotacao);
		//Verifica apenas o Engolfo de Alta
		if (cd.isEngolfoAlta()){			
			return Action.BUY;
		}		
		return Action.NOTHING;
	}
}