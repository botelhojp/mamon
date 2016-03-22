package br.pucpr.ppgia.prototipo.strategy.candle;

import br.pucpr.ppgia.prototipo.vo.Cotacao;


public class Candle {
	
	private Float abertura;
	private Float fechamento;
	private Float maxima;
	private Float minima;
	private Float corpo;
	private Float sombraSuperior;
	private Float sompraInferior;
	private Cotacao cotacao; 
	
	/**
	 * Construtor recebendo uma cotação
	 * @param cotacao
	 */
	public Candle(Cotacao cotacao){
		this.cotacao = cotacao;
		load(this.cotacao);
	}
	
	/**
	 * Construtor sem parâmetros
	 */
	public Candle() {
	}

	/**
	 * Verifica se há uma tendencia de baixa
	 * @return
	 */
	public boolean tendenciaBaixa(){
		Float preco1 = cotacao.getPreult();
		Float preco2 = cotacao.getAnterior(5).getPreult();
		Float preco3 = cotacao.getAnterior(10).getPreult();
		return (preco1 < preco2 && preco2 < preco3);		
	}

	/**
	 * Verifica se há uma tendência de alta
	 * @return
	 */
	public boolean tendenciaAlta(){
		Float preco1 = cotacao.getPreult();
		Float preco2 = cotacao.getAnterior(5).getPreult();
		Float preco3 = cotacao.getAnterior(10).getPreult();
		return (preco1 > preco2 && preco2 > preco3);		
	}

	
	/**
	 * Processa as informações primárias do Candle
	 */
	public void load(Cotacao cotacao) {
		this.cotacao = cotacao;
		this.abertura = cotacao.getPreabe();
		this.fechamento = cotacao.getPreofc();
		this.maxima = cotacao.getPremax();
		this.minima = cotacao.getPremin();
		makeBody();
	}

	/**
	 * Define o corpo do Candle
	 */
	private void makeBody() {
		this.corpo = Math.abs(abertura - fechamento);
		if (this.isBack()){
			this.sombraSuperior = maxima - abertura;
			this.sompraInferior = fechamento - minima;			
		}
		if (this.isWhite()){
			this.sombraSuperior = maxima - fechamento;
			this.sompraInferior = abertura - minima;
		}	
	}

	/**
	 * Informa se o candle é branco, ou seja, de alta
	 */
	public boolean isBack() {		
		return (abertura > fechamento);
	}

	/**
	 * Informa se o candle é preto, ou seja, de baixa
	 */
	public boolean isWhite() {		
		return (fechamento > abertura);
	}
	
	/**
	 * Verifica se o candle é um martelo
	 * @return
	 */
	public boolean isMarteloBranco(){
		//sompra inferiro deve ter duas vezes e meia o tamanho do corpo
		boolean b1 = (this.sompraInferior >= (this.corpo * 2.5));
		//sombra inferior deve ser igual a zero, pode ser um valor pequeno
		boolean b2 = (this.sombraSuperior < this.sompraInferior);
		//cerver ter corpo branco
		boolean b3 = isWhite(); 
		//deve haver aumento do volume		
		//boolean b4 = (cotacao.getVotot() > cotacao.getAnterior().getVotot());
		//tendencia de baixa
		//boolean b4 = (cotacao.getPreult() < cotacao.getAnterior(5).getPreult());
		
		return (b1 && b3 && b2);
	}
	
	public boolean isMarteloBrancoInvertido(){
		//sompra inferiro deve ter duas vezes e meia o tamanho do corpo
		boolean b1 = (this.sombraSuperior >= (this.corpo * 2.5));
		//sombra inferior deve ser igual a zero, pode ser um valor pequeno
		boolean b2 = (this.sompraInferior < this.sombraSuperior/2);
		//cerver ter corpo branco
		//boolean b3 = isWhite(); 
		//deve haver aumento do volume		
		//boolean b4 = (cotacao.getVotot() > cotacao.getAnterior().getVotot());
		//tendencia de baixa
		//boolean b4 = (cotacao.getPreult() < cotacao.getAnterior(5).getPreult());
		
		return (b1 && b2);
	}
	
	/**
	 * Detecta o padrão Enforcado
	 * @return se true é padrão, false caso contrário
	 */
	public boolean isEnforcado(){
		//sompra inferiro deve ter duas vezes e meia o tamanho do corpo
		boolean b1 = (this.sompraInferior >= (this.corpo * 2.5));
		//sombra inferior deve ser igual a zero, pode ser um valor pequeno
		boolean b2 = (this.sombraSuperior < this.sompraInferior/2);
		return (b1 && b2);		
	}
	
	/**
	 * Detecta o engolfo de alta
	 * @return
	 */
	public boolean isEngolfoAlta(){
		if(tendenciaBaixa()){ //Deve ocorrer durante uma tendêncai de baixa
			Candle cAnterior = new Candle(cotacao.getAnterior());
			if (cAnterior.corpo < this.corpo){ //Corpo do anterior de ser maior que o corpo do atual
				if (cAnterior.isBack() && this.isWhite()){ //O anterior deve ser preto e o atual deve ser branco
					if (this.abertura < cAnterior.fechamento){
						if (this.fechamento > cAnterior.abertura){
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
