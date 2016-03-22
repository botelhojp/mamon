package br.pucpr.ppgia.prototipo.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Transaction;
import org.omg.CORBA.portable.ApplicationException;

import br.pucpr.ppgia.prototipo.dao.AcaoDAO;
import br.pucpr.ppgia.prototipo.dao.FileDAO;
import br.pucpr.ppgia.prototipo.dao.HibernateUtil;
import br.pucpr.ppgia.prototipo.util.Config;
import br.pucpr.ppgia.prototipo.util.Util;
import br.pucpr.ppgia.prototipo.vo.Acao;
import br.pucpr.ppgia.prototipo.vo.Arquivo;
import br.pucpr.ppgia.prototipo.vo.Cotacao;

public class ImportFile {

	private AcaoDAO acaoDao = AcaoDAO.getInstance();
	private int cont;
	private Hashtable<String, List<Float>> colMediaMovel = new Hashtable<String, List<Float>>();
	private Hashtable<String, List<Float>> colEstocatico = new Hashtable<String, List<Float>>();
	
	/**
	 * Carrega o arquivo
	 * @param dataFile
	 * @param lines
	 * @throws ApplicationException
	 * @throws Exception
	 */
	public void loadFile(File dataFile, long lines)	throws ApplicationException, Exception {	
		BufferedReader file = new BufferedReader(new FileReader(dataFile));
		String sLine;			
		cont = 1;
		while ( (sLine = file.readLine()) != null && cont < lines){
			process(sLine);
        }		
	}	
	
	/**
	 * Carrega o arquivo
	 * @param file
	 * @throws ApplicationException
	 * @throws Exception
	 */
	public void loadFile(File file) throws ApplicationException, Exception {
		loadFile(file, Long.MAX_VALUE);
	}
	
	/**
	 * Processa uma linha da arquivo texto
	 * @param linha Valor da linha
	 * @throws ApplicationException
	 * @throws Exception
	 */
	private void process(String linha) throws ApplicationException, Exception {
		String nomeres = linha.substring(12, 24).trim();		
		if ( isCorpo(linha) && isBovespa(nomeres)  ){		
			cont++;
			System.out.print(".");
			if  (cont % 100 == 0) System.out.print("\n(" + cont +")" +  Util.makeData(linha.substring(02,10)));
							
			String nomefull = linha.substring(27, 39).trim() + " " + linha.substring(39, 49).trim();			
			
			Acao acao = acaoDao.findByName(nomeres);			
			if (acao == null){			
				acao = new Acao();
				acao.setNomeres(nomeres);
				acao.setNomefull(nomefull);
				acao.setIbovespa(true);
				acaoDao.insert(acao);
			}			
			Cotacao cotacao = new Cotacao();
			cotacao.setDatapre(Util.makeData(linha.substring(02,10)) );
			cotacao.setPreabe( Util.makeFloat(linha.substring(56, 69)) / 100 );
			cotacao.setPremax( Util.makeFloat(linha.substring(69, 82)) / 100 );
			cotacao.setPremin( Util.makeFloat(linha.substring(82, 95)) / 100 );
			cotacao.setPremed( Util.makeFloat(linha.substring(95, 108)) / 100 );
			cotacao.setPreult( Util.makeFloat(linha.substring(108, 121)) / 100 );
			//médias móveis
			saveMV(nomeres, cotacao.getPreult(), 20);
			cotacao.setPreultmv5(calcMV(nomeres, 5));
			cotacao.setPreultmv10(calcMV(nomeres, 10));
			cotacao.setPreultmv20(calcMV(nomeres, 20));
			//estocatico
			int DAYS = 30;
			saveES(nomeres, cotacao.getPreult(), DAYS);
			cotacao.setPreultESMax(calcESMax(nomeres, DAYS));
			cotacao.setPreultESMin(calcESMin(nomeres, DAYS));
			
			cotacao.setPreofc( Util.makeFloat(linha.substring(121, 134)) / 100 );
			cotacao.setPreofv( Util.makeFloat(linha.substring(134, 147)) / 100 );
			cotacao.setTotneg( Util.makeLong(linha.substring(147, 152))  );
			cotacao.setQuatot( Util.makeLong(linha.substring(152, 170))  );
			cotacao.setVotot( Util.makeLong(linha.substring(170, 188))  );
			cotacao.setAcao(acao);			
			acao.getCotacaos().add(cotacao);
			acaoDao.saveOrUpdate(acao);
		}
	}

	/**
	 * Calcula a média móvel para um espeço de tempo fixo
	 * @param count Espaço de tempo
	 * @return Valor da média móvel
	 */
	private Float calcMV(String key, int count) {
		List<Float> lista = colMediaMovel.get(key);			
		if (lista.size() < count) return 0f;		
		Float sum = 0f;
		for(int i = 0; i < count; i++){
			sum += lista.get(lista.size()-1-i);
		}
		return sum/count;
	}

	/**
	 * Garda x valores para o calculo da média móvel
	 * @param preult valor a ser quardado
	 * @param tam numero de valores a ser quardaddo
	 */
	private void saveMV(String key, Float preult, int tam) {
		if (colMediaMovel.containsKey(key)){
			List<Float> lista = colMediaMovel.get(key);			
			lista.add(preult);
			while (lista.size() > tam){
				lista.remove(0);
			}	
		}else{
			List<Float> lista = new ArrayList<Float>();
			lista.add(preult);
			colMediaMovel.put(key, lista);
		}		
	}
	
	
	/**
	 * Cálcula o valor máximo para uma lista de valores
	 * @param key Acao
	 * @param count Número de dias
	 * @return Maior valor
	 */
	private Float calcESMax(String key, int count) {
		Float max = -Float.MAX_VALUE;
		List<Float> lista = colEstocatico.get(key);			
		if (lista.size() < count) return 0f;
		for (Float value : lista) {
			if (value >= max)
				max = value;
		}		
		return max;
	}
	
	/**
	 * Cálcula o valor minimo para uma lista de valores
	 * @param key Acao
	 * @param count Número de dias
	 * @return Menor valor
	 */
	private Float calcESMin(String key, int count) {
		Float min = Float.MAX_VALUE;
		List<Float> lista = colEstocatico.get(key);			
		if (lista.size() < count) return 0f;
		for (Float value : lista) {
			if (value <= min)
				min = value;
		}		
		return min;
	}
	
	/**
	 * Garda x valores para o calculo do método estocástico
	 * @param preult valor a ser quardado
	 * @param tam numero de valores a ser quardaddo
	 */
	private void saveES(String key, Float preult, int tam) {
		if (colEstocatico.containsKey(key)){
			List<Float> lista = colEstocatico.get(key);			
			lista.add(preult);
			while (lista.size() > tam){
				lista.remove(0);
			}	
		}else{
			List<Float> lista = new ArrayList<Float>();
			lista.add(preult);
			colEstocatico.put(key, lista);
		}		
	}

	/**
	 * Indica se o papel pertence ao indice bovespa
	 * @param nomeres Nome do papel
	 * @return boolean
	 */
	private boolean isBovespa(String nomeres) {
		String[] papeis = Config.getActives();
		for (String papel : papeis)
			if (papel.equals(nomeres))
				return true;		
		return false;
	}
	
	/**
	 * Informa se a linhas faz parte do corpo
	 * @param linha
	 * @return
	 */
	private boolean isCorpo(String linha) {
		return (linha.substring(0, 2).equals("01"));
	}

	/**
	 * Carrega o arquivo com um número parametrizado de lihas
	 * @param file
	 * @param linhas
	 * @throws ApplicationException
	 * @throws Exception
	 */
	public void carregarFile(File file, long linhas) throws ApplicationException, Exception {	
		if (!file.exists()){
			throw new Exception("Arquivo nao encontrado (" + file.getAbsolutePath().toString() +")");
		}		
		loadFile(file, linhas);
	}

	/**
	 * Carrega arquivo com um número definido de linhas
	 * @param file
	 * @throws ApplicationException
	 * @throws Exception
	 */
	public void carregarFile(File file) throws ApplicationException, Exception {
		carregarFile(file, Long.MAX_VALUE);
	}

	/**
	 * Importa todos os arquivos informados no arquivo de configuração da aplicação
	 */
	public static void importFiles() {
		ImportFile imporFile = new ImportFile();
		FileDAO fileDAO = new FileDAO();		
		String[] files = Config.getDataFiles();
		for (String fileName : files) {
			try {					
				if (fileDAO.findByName(fileName)==null){ //se o arquivo nao foi importado
					File file = Util.descompacta(fileName);
					Transaction t = HibernateUtil.getCurrentSession().beginTransaction();
					fileDAO.insert(new Arquivo(fileName, Calendar.getInstance().getTime()));
					imporFile.carregarFile(file);
					file.delete();
					t.commit();						
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
	
	/**
	 * Realiza a importação 
	 * @param args
	 */
	public static void main(String[] args){
		ImportFile.importFiles();	
	}

}
