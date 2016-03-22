package br.pucpr.ppgia.prototipo.core;

import org.apache.log4j.Logger;

import br.pucpr.ppgia.prototipo.gui.TrustGui;

/**
 * Inicializa os agentes conforme a configuração do árquivo de propriedades e
 * arquivo de dados xml
 * 
 * @author vanderson
 */
public class TrustSystem {
	protected static Logger logger = Logger.getLogger(TrustSystem.class);

	public static void main(String[] args) {
		Environment envi = Environment.getInstance();		
		TrustGui gui = new TrustGui();
		envi.setGUI(gui);		
		gui.setEnvironment(envi);
		gui.setVisible(true);
		
	
		//Environment.getInstance().runAllDays();

		/*
		 * Maker.Run(Config.getSizeAgent(), Config.getSizeData()); String
		 * parametros = (Config.isGuiJade()?"-gui ":"") + "-host localhost
		 * -detect-main false " + Maker.getAgentList(); logger.debug("Starting
		 * Jade: " + parametros); new Boot(parametros.split(" "));
		 */
	}
}
