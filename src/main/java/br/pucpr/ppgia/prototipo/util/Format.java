package br.pucpr.ppgia.prototipo.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Format {
	
	static private DecimalFormat forma0 = new DecimalFormat("###");	
	static private DecimalFormat forma2 = new DecimalFormat("###.##");	
	static private DecimalFormat forma3 = new DecimalFormat("###.###");	
	static private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	public static double getDouble0(double valor) {
		try {
			return forma0.parse(forma0.format(valor)).doubleValue();
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Retora uma data no formato String na forma dd/mm/aaaa
	 * @param data
	 * @return
	 */
	public static String getDate(Date data) {
		return dateFormat.format(data);
	}


	public static double getDouble2(double valor) {
		try {
			return forma2.parse(forma2.format(valor)).doubleValue();
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static double getDouble3(double valor) {
		try {
			return forma3.parse(forma3.format(valor)).doubleValue();
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
}
