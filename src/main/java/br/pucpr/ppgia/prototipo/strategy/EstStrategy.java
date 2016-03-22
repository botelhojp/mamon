package br.pucpr.ppgia.prototipo.strategy;

import java.util.Date;

import br.pucpr.ppgia.prototipo.agents.CronosAgent;
import br.pucpr.ppgia.prototipo.core.Environment;
import br.pucpr.ppgia.prototipo.util.Config;
import br.pucpr.ppgia.prototipo.vo.Acao;

/**
 * Método estocástio
 * @author vanderson
 */
public class EstStrategy extends AbstractStrategy {
	
	public static final int DAYS = Config.getEstocaticoDays();
	public static final Float MAX = Config.getEstocaticoMAX();
	public static final Float MIN = Config.getEstocaticoMIN();
	
	Action lastRec;
	
	public EstStrategy(CronosAgent myAgent) {
		super(myAgent);
	}

	@Override
	public Action getRecomentation(Acao acao) {
	
		Date hoje = Environment.getInstance().getToday();
		Float maior = myAgent.getCotacao(acao, hoje).getPreultESMax();
		Float menor = myAgent.getCotacao(acao, hoje).getPreultESMin();
		
		if (maior == 0.0) return Action.NOTHING;

		Float corrente = myAgent.getCotacao(acao, hoje).getPreult();
		//Normalizando entre [0 e 1]
		Float normal = 1 - ((maior - corrente) / (maior - menor));
		
		if (normal >= MAX){
			lastRec = Action.BUY;
		}else if (normal <= MIN){
			lastRec = Action.SELL;
		}else{
			return Action.NOTHING;
		}
		return lastRec;
	}
}