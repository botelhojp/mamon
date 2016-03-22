package br.pucpr.ppgia.prototipo.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import br.pucpr.ppgia.prototipo.agents.ClientAgent;
import br.pucpr.ppgia.prototipo.agents.CronosAgent;
import br.pucpr.ppgia.prototipo.agents.ServerAgent;
import br.pucpr.ppgia.prototipo.strategy.IStrategy;
import br.pucpr.ppgia.prototipo.trustmodel.ITrust;
import br.pucpr.ppgia.prototipo.trustmodel.vo.Rating;

/**
 * Classe utilitária
 * @author Vanderson Botêlho
 */
public class Util {

	/**
	 * Retorna um dada para uma string de origem
	 * 
	 * @param sData
	 * @return
	 */
	public static Date makeData(String sData) {
		SimpleDateFormat formato = new SimpleDateFormat("yyyyMMdd");		
		try {
			return formato.parse(sData);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	/**
	 * Converte string em Float
	 * 
	 * @param sFloat
	 * @return
	 */
	public static Float makeFloat(String sFloat) {
		return new Float(sFloat).floatValue();
	}

	/**
	 * Converte Long em Float
	 * 
	 * @param string
	 * @return
	 */
	public static long makeLong(String string) {
		return new Long(string).longValue();
	}

	/**
	 * Remove todos os elemento de uma lista que estão contidos em outro
	 * 
	 * @param listInit
	 *            Lista original que terá seus elementos removidos
	 * @param listTodoRemove
	 *            Lista com os elementos a serem removidos
	 */
	public static void remove(List<?> listInit, List<?> listTodoRemove) {
		for (Object object : listTodoRemove) {
			listInit.remove(object);
		}
	}

	/**
	 * Descompata um arquivo de importação
	 * 
	 * @param fileName
	 * @return
	 */
	public static File descompacta(String fileName) {
		try {
			File file2 = new File(Util.class.getResource("/" + fileName).getFile());
			System.out.println(file2.exists());
			
			ZipFile zipFile = new ZipFile(file2);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				if (entry.isDirectory()) {
					System.err.println("Descompactando diretório: "
							+ entry.getName());
					(new File(entry.getName())).mkdir();
					continue;
				}
				System.out.println("Descompactando arquivo:" + entry.getName());
				copyInputStream(zipFile.getInputStream(entry),
						new BufferedOutputStream(new FileOutputStream(entry
								.getName()
								+ ".tmp")));
				File file = new File(entry.getName() + ".tmp");
				zipFile.close();
				return file;
			}
			zipFile.close();
		} catch (IOException ioe) {
			System.err.println("Erro ao descompactar:" + ioe.getMessage());
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(Util.class.getResource("/"));
	}

	public static void ordenaLista(List<Rating> testemunhos) {
		Collections.sort(testemunhos, new Comparator<Rating>() {
			public int compare(Rating o1, Rating o2) {
				return o1.compareTo(o2);
			}
		});
	}

	private static final void copyInputStream(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len;
		while ((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);
		in.close();
		out.close();
	}

	/**
	 * Concatena duas lista
	 * 
	 * @param lista1 - Lista final concatenada
	 * @param lista2
	 */
	public static void concatenaList(List<Rating> lista1,
			ArrayList<Rating> lista2) {
		for (Rating rating : lista2) {
			lista1.add(rating);
		}
	}
	
	
	/**
	 * Cria um agente cliente a partir da nome da classe de seu modelo de confiança
	 * @param id Número id do agente
	 * @param trustClazzName Nome da classe do tipo ITrust
	 * @return Agente Cliente
	 */
	@SuppressWarnings("unchecked")
	public static CronosAgent createClient(int id, String trustClazzName){
		try {
			ClientAgent client = new ClientAgent("server_" + id + "_" + trustClazzName);			
			Class classe = Class.forName(ITrust.class.getPackage().getName() + "." + trustClazzName);			
			Constructor contrutor = classe.getConstructor(new Class[]{ClientAgent.class});			
			ITrust trust = (ITrust) contrutor.newInstance(new Object[]{client});			
			client.setTrust(trust);			
			return client;			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Cria um agente servidor a partir da nome da classe de sua estratégia
	 * @param id Número id do agente
	 * @param stratetyClazzName Nome da classe do tipo IStrategy
	 * @return Agente Servidor
	 */
	@SuppressWarnings("unchecked")
	public static CronosAgent createServer(int id, String stratetyClazzName){
		try {
			ServerAgent server = new ServerAgent("server_" + id + "_" + stratetyClazzName);
			Class classe = Class.forName(IStrategy.class.getPackage().getName()+ "." + stratetyClazzName);			
			Constructor contrutor = classe.getConstructor(new Class[]{CronosAgent.class});			
			IStrategy strategia = (IStrategy) contrutor.newInstance(new Object[]{server});			
			server.setStratety(strategia);			
			return server;			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Retorna o tempo transcorrido a partir de um tempo inicial
	 * @param time_ini Tempo inicial
	 * @return tempo trancorrido
	 */
	public static String countTime(long time_ini) {
		long time_fim = Calendar.getInstance().getTimeInMillis();
		long tempol = (time_ini == 0 ? 0 : time_fim - time_ini);
		return (tempol / 1000 == 0 ? tempol + "m" : "" + tempol / 1000 + "s," + tempol % 1000 + "m");
	}

}
