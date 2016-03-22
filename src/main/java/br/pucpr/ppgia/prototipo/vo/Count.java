package br.pucpr.ppgia.prototipo.vo;

import br.pucpr.ppgia.prototipo.core.Environment;
import br.pucpr.ppgia.prototipo.dao.AcaoDAO;
import br.pucpr.ppgia.prototipo.util.Format;
/**
 * Classe que abstrae um conta de um agente. A conta � adminstrada por um
 * agente cliente, porém ela manipulada por conta das dicas de um �nico
 * servidor, ou seja, a conta está relacionada a um cliente e a um �nico 
 * servidor.
 * @author Vanderson Botêlho
 */
public class Count {	

	private double initCash;		//valor inicial da conta
	private double currentCash;		//valor corrente em dinheiro
	private Acao acao;				//acao que a conta gerencia
	private Integer quant_acao;		//quantidade de papeis desta conta

	/**
	 * Construtor que recebe uma ação e cash 
	 * @param acao
	 * @param cash
	 */
	public Count(Acao acao, double cash) {
		initCash = currentCash = cash;
		this.acao = acao;
		quant_acao = 0;
	}

	/**
	 * Executa uma ordem de Venda. Vende todos os pap�ia de uma ação
	 * @param acao Papel a ser negociada
	 */
	public boolean sell() {
		double valor = papelToCash();			
		int papel = (int) (valor / getCotacao().getPreult());
		currentCash += papel * getCotacao().getPreult();
		quant_acao -= papel;
		return (papel > 0);
	}

	/**
	 * Executa uma operação de compra
	 * @param acao Papel a ser comprado
	 */
	public boolean buy() {
		int papel = (int) (currentCash / getCotacao().getPreult());
		if (papel > 0){
			currentCash -= papel * getCotacao().getPreult();
			quant_acao += papel;			
		}
		return (papel > 0);
	}
	
	/**
	 * Retorna o valor do papel em dinheiro
	 * @param acao Ação
	 * @return Valor em dinheiro
	 */
	private double papelToCash() {		
		return quant_acao * getCotacao().getPreult();		
	}
	
	/**
	 * Retorna o valor em dinheiro que a conta possui no momento
	 * @return Valor em Dinheiro.
	 */
	public double getCash() {
		return currentCash;
	}
	
	/**
	 * Retorna o valor atual da conta, ou seja, o valor em dinheiro
	 * e o valor em papéis.
	 * @return
	 */
	public double getTotal(){
		return getCash() + getAcaoCash();
	}
	
	/**
	 * Retorna o valor em dinheiro dos papeis comprados
	 * @return Valor em dinheiro dos papéis
	 */
	public double getAcaoCash(){
		return quant_acao * getCotacao().getPreult();		 
	}
	
	/**
	 * Calcula e retorna o valor do rendimento atual da conta
	 * @return
	 */
	public double getRendimento() {
		double total = getTotal();
		return Format.getDouble2(((total-getInitCash())/getInitCash())) * 100; 
	}

	/**
	 * Retorna o valor inicial depositado na conta
	 * @return
	 */
	public double getInitCash() {
		return initCash;
	}
	
	/**
	 * Retorna o valor da cotação corrente
	 * @return
	 */
	public Cotacao getCotacao(){
		return AcaoDAO.getInstance().getCotacao(acao, Environment.getInstance().getToday());
	}
}
