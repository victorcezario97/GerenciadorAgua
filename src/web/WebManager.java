package web;

import javax.swing.JProgressBar;

import utils.ProgressBarFiller;
import utils.Utils;

public class WebManager{
	
	JProgressBar pb;
	public int progress;
	private ContaGetter cg1, cg2;
	private String[] s1 = null, s2 = null;
	
	public WebManager(JProgressBar pb) {
		this.pb = pb;
		progress = 0;
	}
	
	public int getProgress() {
		return progress;
	}
	
	private void startProgressBars() {
		cg1 = new ContaGetter("18449", "79", s1);
		cg2 = new ContaGetter("58292", "16", s2);
		ProgressBarFiller pbf1 = new ProgressBarFiller(cg1);
		ProgressBarFiller pbf2 = new ProgressBarFiller(cg2);
		pbf1.start();
		pbf2.start();
	}
	
	public String[] start() {
		startProgressBars();
		
		return getTotals();
	}
	

	public String[] getTotals() {
		
		boolean done1 = false, done2 = false;
		
		/*ContaGetter cg1 = new ContaGetter("18449", "79", s1);
		ContaGetter cg2 = new ContaGetter("58292", "16", s2);
		ProgressBarFiller pbf1 = new ProgressBarFiller(cg1);
		ProgressBarFiller pbf2 = new ProgressBarFiller(cg2);
		pbf1.start();
		pbf2.start();*/
		
		
		cg1.start();
		

		cg2.start();
		
		
		
		while(!done1 & !done2) {
			done1 = cg1.getDone();
			done2 = cg2.getDone();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(done1 + " " + done2);
		}
		
		//s1 = cg1.getInfo();
		//s2 = cg2.getInfo();
		
		//cg1.setStop(true);
		//cg2.setStop(true);
		
		progress = 100;
		System.out.println(s1[0] + " " + s2[0]);
		
		String[] totals = new String[5];/*
		totals[0] = String.valueOf(Utils.round(Double.valueOf(s1[0].replace(",", ".")) + Double.valueOf(s2[0].replace(",", ".")))).replace(".", ",");
		totals[1] = String.valueOf(Utils.round(Double.valueOf(s1[1].replace(",", ".")) + Double.valueOf(s2[1].replace(",", ".")))).replace(".", ",");
		totals[2] = String.valueOf(Utils.round(Double.valueOf(s1[2].replace(",", ".")) + Double.valueOf(s2[2].replace(",", ".")))).replace(".", ",");
		totals[3] = String.valueOf(Utils.round(Double.valueOf(totals[1].replace(",", ".")) + Double.valueOf(totals[2].replace(",", ".")))).replace(".", ",");
		//totals[4] = findVencimento(s1[3], s2[3]);
		totals[4] = "testemeu";
		System.out.println(totals[0]);
		System.out.println(totals[1]);
		System.out.println(totals[2]);
		System.out.println(totals[3]);
		System.out.println(totals[4]);
		*/
		return totals;
	}
}
