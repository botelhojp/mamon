package br.pucpr.ppgia.prototipo.trustmodel;

import java.util.Date;

import br.pucpr.ppgia.prototipo.agents.ClientAgent;

public interface ITrust {	
	
	public void runDay(Date today);
	
	public ClientAgent getAgent();
}
