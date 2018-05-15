package web;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import utils.Utils;

public class WebManager{

	public int progress;
	private ContaGetterCallable cg1, cg2;
	private String[] s1 = null, s2 = null;
	public boolean[] d;

	public String[] start() {
		return getTotals();
	}


	public String[] getTotals() {

		d = new boolean[2];
		d[0] = false; d[1] = false;

		cg1 = new ContaGetterCallable("18449", "79", s1, this, 0);
		cg2 = new ContaGetterCallable("58292", "16", s2, this, 1);
		ExecutorService pool = Executors.newFixedThreadPool(2);
		@SuppressWarnings("unchecked")
		Future<Integer> future = pool.submit(cg1);
		@SuppressWarnings("unchecked")
		Future<Integer> future2 = pool.submit(cg2);

		try {
			future.get();
			future2.get();
		}catch(InterruptedException e){
			System.out.println("interrupted");
			return null;
		}catch(ExecutionException e){
			System.out.println("execution");
			return null;
		}
		System.out.println("No exceptions in ContaGetterCallable");
		
		s1 = cg1.getInfo();
		s2 = cg2.getInfo();
		
		//System.out.println("Valor total 79: " + s1[0] + "\nValor total 16: " + s2[0]);
		
		String[] totals = new String[5];
		try {
			totals[0] = String.valueOf(Utils.round(Double.valueOf(s1[0].replace(",", ".")) + Double.valueOf(s2[0].replace(",", ".")))).replace(".", ",");
			totals[1] = String.valueOf(Utils.round(Double.valueOf(s1[1].replace(",", ".")) + Double.valueOf(s2[1].replace(",", ".")))).replace(".", ",");
			totals[2] = String.valueOf(Utils.round(Double.valueOf(s1[2].replace(",", ".")) + Double.valueOf(s2[2].replace(",", ".")))).replace(".", ",");
			totals[3] = String.valueOf(Utils.round(Double.valueOf(totals[1].replace(",", ".")) + Double.valueOf(totals[2].replace(",", ".")))).replace(".", ",");
			totals[4] = Utils.findGreaterDate(s1[3], s2[3]);
		}catch(Exception e) {
			System.out.println("Error formatting the strings.");
			return null;
		}
		
		/*System.out.println("Printing the string array:");
		System.out.println("Total: " + totals[0]);
		System.out.println("Multa: " + totals[1]);
		System.out.println("Dividido: " + totals[2]);
		System.out.println("Multado: " + totals[3]);
		System.out.println("Vencimento: " + totals[4]);
		System.out.println();*/
		
		return totals;
	}
}
