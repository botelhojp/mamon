package br.pucpr.ppgia.prototipo.core;

import java.util.Date;
import java.util.List;

public class Phase {

	private static final int LIMITE = 2;
	public static final int INIT = 1;
	public static final int TRUST = 2; //fase para entrar os modelos de confian�a
	public static final int CHANCE = 3; //fase para que os agentes mudem seu comportamentos

	
	private int currentPhase;
	private boolean isChanged = false;

	public Phase() {
		super();	
		currentPhase = 1;
	}

	public int getFase() {
		return currentPhase;
	}	
	
	private int getNumFase() {
		return LIMITE;	
	}

	public void setPhase(Date day, List<Date> time) {
		isChanged = false;
		int hoje = time.indexOf(day);
		int dias = time.size();		
		int fatia =  (dias / getNumFase()) + 1;		
		int newFase = 1 + (hoje / fatia);	
		if (newFase != currentPhase){
			isChanged = (newFase != currentPhase);
		}
		isChanged = (newFase != currentPhase);		
		currentPhase = (newFase > getNumFase() ? getNumFase() : newFase);
	}
	
	/**
	 * Informa se a fase foi alterada
	 * @return
	 */
	public boolean isChanged(){
		return false;
		//TODO Verificar se vamos trabalhar com mais uma fase, acho que nao uma ta bom
		//return isChanged;
	}
	
	public String toString(){
		return "" + currentPhase; 
	}	
}
