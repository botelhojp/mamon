package br.pucpr.ppgia.prototipo.strategy;

import java.util.Date;

import br.pucpr.ppgia.prototipo.agents.CronosAgent;
import br.pucpr.ppgia.prototipo.core.Environment;
import br.pucpr.ppgia.prototipo.strategy.vo.Candle;
import br.pucpr.ppgia.prototipo.util.Config;
import br.pucpr.ppgia.prototipo.vo.Acao;
import br.pucpr.ppgia.prototipo.vo.Cotacao;

/**
 * Método baseado em volume, quando o volume sobe
 * 
 * @author vanderson
 */
public class CandlesStrategy extends AbstractStrategy {

	public static int DAYS = Config.getVolumeDays();
	
	private Candle cd = new Candle();
	private Cotacao cotacao;

	public CandlesStrategy(CronosAgent myAgent) {
		super(myAgent);
	}

	/**
	 * Compra o comportamento do volume com o preco
	 */
	@Override
	public Action getRecomentation(Acao acao) {

		Date diaAtual = Environment.getInstance().getToday();
		Date diaAnterior = Environment.getInstance().getAddDay(diaAtual, -DAYS);
		if (diaAnterior == null)
			return Action.NOTHING;
		
		cotacao = myAgent.getCotacao(acao, diaAtual);
		cd.load(cotacao);
		//Verifica o martelo branco
		if (cd.isMarteloBrancoInvertido()){			
			return Action.BUY;
		}
		if (cd.isEnforcado()){
			return Action.SELL;
		}
		return Action.NOTHING;
	}
}