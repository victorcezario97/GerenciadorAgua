package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

public class Utils {
	
	public static String findGreaterDate(String venc1, String venc2) {
		String[] v1 = venc1.split("/");
		String[] v2 = venc2.split("/");
		
		int mes1 = Integer.valueOf(v1[1]);
		int mes2 = Integer.valueOf(v2[1]);
		int dia1 = Integer.valueOf(v1[0]);
		int dia2 = Integer.valueOf(v2[0]);
		
		if(mes1 > mes2) return venc1;
		if(mes1 < mes2) return venc2;
		
		if(dia1 > dia2) return venc1;
		return venc2;
	}

	public static String findDate() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		
		if(month < 10) return "0" + String.valueOf(month) + "/" + String.valueOf(year);
		return String.valueOf(month) + "/" + String.valueOf(year);
	}
	
	public static double round(double n) {
		BigDecimal bd = new BigDecimal(n);
	    bd = bd.setScale(2, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public static String getMonthString() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		int month = cal.get(Calendar.MONTH)+1;
		
		switch(month) {
		
		case 1:
			return "Janeiro";
		case 2:
			return "Fevereiro";
		case 3:
			return "Março";
		case 4:
			return "Abril";
		case 5:
			return "Maio";
		case 6:
			return "Junho";
		case 7:
			return "Julho";
		case 8:
			return "Agosto";
		case 9:
			return "Setembro";
		case 10:
			return "Outubro";
		case 11:
			return "Novembro";
		case 12:
			return "Dezembro";
		default:
			return "ERROR";
			
		}
	}
}
