package br.pucpr.ppgia.prototipo.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileOutPut {
	
	private File outFile;	
	private static FileOutPut output;
	
	public static FileOutPut getInstance(){
		if (output == null){
			output = new FileOutPut();
		}
		return output;
	}
	
	private FileOutPut(){
		try {
			String sPath = FileOutPut.class.getResource("/").getFile();
			outFile = new File(sPath + "output.csv");
			if (outFile.exists())
				outFile.delete();
			if (!outFile.exists())
				outFile.createNewFile();			
		} catch (IOException e) {				
			e.printStackTrace();
		}
	}

	public void write(Object obj) {
		try {
			FileWriter writer = new FileWriter(outFile, true);
			PrintWriter saida = new PrintWriter(writer,true);		
			saida.println(obj.toString().replace('.', ','));
			saida.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
