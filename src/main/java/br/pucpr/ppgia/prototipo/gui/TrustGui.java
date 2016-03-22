package br.pucpr.ppgia.prototipo.gui;

import java.util.Collection;
import java.util.Date;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import br.pucpr.ppgia.prototipo.core.Environment;
import br.pucpr.ppgia.prototipo.trustmodel.AbstractTrust;

public class TrustGui extends AbstractGUI {
	
	private static final long serialVersionUID = 1L;
	Environment environment;

	@Override
	protected XYSeriesCollection getAllSeries() {
		environment.init();
		XYSeriesCollection col = new XYSeriesCollection();
		Collection<XYSeries> values = AbstractTrust.getSeries().values();
		for (XYSeries series : values) {
			col.addSeries(series);
		}		
		return col;
	}	
	
	public void updateDate(Date day, long itoday){
		progress.setMaximum((int) environment.getNumDays());
		progress.setMinimum(1);
		progress.setValue((int)itoday);
		super.lbDate.setText(day.toString());
	}

	
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TrustGui().setVisible(true);
            }
        });
    }

	public void setEnvironment(Environment envi) {
		environment = envi;
	}

	public static String cenario;

	@Override
	protected void runSystem(String p_cenario) {
		cenario = p_cenario;
		Thread thread = new Thread(){
			public void run(){
				environment.runAllDays(cenario);	    		
			}
		};
		thread.start();
	}

}
