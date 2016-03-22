package br.pucpr.ppgia.prototipo.strategy;

import java.util.Date;

import br.pucpr.ppgia.prototipo.agents.CronosAgent;
import br.pucpr.ppgia.prototipo.core.Environment;
import br.pucpr.ppgia.prototipo.util.Config;
import br.pucpr.ppgia.prototipo.vo.Acao;

/**
 * Método baseado em volume, quando o volume sobe
 * 
 * @author vanderson
 */
public class VolumeStrategy extends AbstractStrategy {

	public static int DAYS = Config.getVolumeDays();

	public VolumeStrategy(CronosAgent myAgent) {
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

		Float precoAtual = myAgent.getCotacao(acao, diaAtual).getPreultmv5();
		Long volumeAtual = myAgent.getCotacao(acao, diaAtual).getVotot();

		Float precoAnterior = myAgent.getCotacao(acao, diaAnterior).getPreultmv5();
		Long volumeAnterior = myAgent.getCotacao(acao, diaAnterior).getVotot();

		Boolean precoSubiu = (precoAtual > precoAnterior);
		Boolean precoDesceu = (precoAtual < precoAnterior);

		Boolean volumeSubiu = (volumeAtual > volumeAnterior);
		Boolean volumeDesceu = (volumeAtual < volumeAnterior);

		if (volumeSubiu && precoSubiu) {
			return Action.BUY;
		}

		if (volumeDesceu && precoDesceu) {
			return Action.SELL;
		}
		return Action.NOTHING;
	}
}