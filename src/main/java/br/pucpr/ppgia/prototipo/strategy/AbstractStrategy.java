package br.pucpr.ppgia.prototipo.strategy;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import br.pucpr.ppgia.prototipo.agents.CronosAgent;
import br.pucpr.ppgia.prototipo.agents.ServerAgent;
import br.pucpr.ppgia.prototipo.core.Environment;
import br.pucpr.ppgia.prototipo.vo.Acao;

public abstract class AbstractStrategy implements IStrategy {

	public static final int BUY = 1;
	public static final int SELL = 0;
	public static final int NOTHING = -1;


	protected ServerAgent myAgent;
	
	
	/**
	 * Calcula a função de utilidade do modelo baseado em cada instancia criada
	 * @param clazzModel Classe do modelo de confian�a
	 * @return Função de Utilidade em forma de rentabilidade
	 */
	@SuppressWarnings("unchecked")
	protected static Hashtable<Class, List<IStrategy>> instances = new Hashtable<Class, List<IStrategy>>();
	@SuppressWarnings("unchecked")
	public static Double getStrategyUtility(Class clazzModel){
		int cont = 0;
		double sum = 0d;		
		if (!instances.containsKey(clazzModel)){
			return 0d;
		}		
		List<IStrategy> trusts = instances.get(clazzModel);
		for (IStrategy trust : trusts) {			
			for (Acao acao : Environment.getInstance().getAcoes()) {
				Double ut = trust.getAgent().getUtility(trust.getAgent(), acao.getNomeres());
				if (ut != null){
					sum += ut;
					cont++;
				}
			}
		}
		Double tem = (cont > 0) ? sum/cont : 0d;
//		if (tem > 1.0){
//			tem = 1.0;
//		}
//		if (tem < -1.0){
//			tem = 1.0;
//		}
		return tem;
	}		
	
	public AbstractStrategy(CronosAgent myAgent) {
		super();
		this.myAgent = (ServerAgent) myAgent;
		if (instances.containsKey(this.getClass())){
			instances.get(this.getClass()).add(this);
		}else{
			List<IStrategy> trusts = new ArrayList<IStrategy>();
			trusts.add(this);
			instances.put(this.getClass(), trusts);
		}		
	}
	
	public abstract Action getRecomentation(Acao acao);
	
	public ServerAgent getAgent(){
		return myAgent;
	}

}
