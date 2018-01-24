package web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebManager {
	
	public WebManager() {
		
	}
	

	public String[] getTotals() {
		
		String[] s1 = getConta("18449", "79");
		String[] s2 = getConta("58292", "16");
		System.out.println(s1[0] + " " + s2[0]);
		
		String[] totals = new String[3];
		totals[0] = String.valueOf(round(Double.valueOf(s1[0].replace(",", ".")) + Double.valueOf(s2[0].replace(",", ".")))).replace(".", ",");
		totals[1] = String.valueOf(round(Double.valueOf(s1[1].replace(",", ".")) + Double.valueOf(s2[1].replace(",", ".")))).replace(".", ",");
		totals[2] = String.valueOf(round(Double.valueOf(s1[2].replace(",", ".")) + Double.valueOf(s2[2].replace(",", ".")))).replace(".", ",");
		System.out.println(totals[0]);
		System.out.println(totals[1]);
		System.out.println(totals[2]);
		
		return totals;
	}
	
	String[] getConta(String cdc, String dv){
		WebDriver driver = getFirefoxDriver();
		
		driver.get("http://aguaconta.cebinet.com.br/aguasc/");
		WebElement tCdc = driver.findElement(By.id("tCDC"));
		WebElement tDv = driver.findElement(By.id("tDV"));
		
		tCdc.sendKeys(cdc);
		tDv.sendKeys(dv);
		
		driver.findElement(By.id("tIr")).click();
		
		@SuppressWarnings("unused")
		WebElement myDynamicElement = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("hlConta")));
		driver.findElement(By.id("hlConta")).click();
		
		myDynamicElement = (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(By.linkText(findDate())));
		driver.findElement(By.linkText(findDate())).click();
		
		String winHandleBefore = driver.getWindowHandle();
		
		for(String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}
	   
		myDynamicElement = (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/form/p[1]/table[11]/tbody/tr/td[4]/div")));
	    String total = driver.findElement(By.xpath("/html/body/form/p[1]/table[11]/tbody/tr/td[4]/div")).getText();
	    String multaText = "Multa por atraso";
	    String multaValue = null;
	    
	    if(driver.getPageSource().contains(multaText)) {
	    		multaValue = driver.findElement(By.xpath("//table[@id='Table6']/tbody/tr[4]/td[6]")).getText();
	    }
	    
	    System.out.println(multaValue);
		
		driver.close();
		driver.switchTo().window(winHandleBefore).close();
		
		String[] values = new String[3];
		values[0] = total;
		values[1] = multaValue;
		values[2] = calculateShare(total, 15, multaValue);
		
		return values;
	}
	
	String findDate() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		int month = cal.get(Calendar.MONTH)+1;
		int year = cal.get(Calendar.YEAR);
		
		if(month < 10) return "0" + String.valueOf(month) + "/" + String.valueOf(year);
		return String.valueOf(month) + "/" + String.valueOf(year);
	}
	
	double round(double n) {
		BigDecimal bd = new BigDecimal(n);
	    bd = bd.setScale(2, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	String calculateShare(String total, int n, String multa) {
		total = total.replace(",", ".");
		multa = multa.replace(",", ".");
		double tot = Double.valueOf(total) - Double.valueOf(multa);

	    return String.valueOf(round(tot/(n*1.0))).replace(".", ",");
	}
	
	WebDriver getFirefoxDriver() {
		FirefoxBinary binary = new FirefoxBinary();
		binary.addCommandLineOptions("--headless");
		FirefoxOptions options = new FirefoxOptions();
		options.setBinary(binary);
		WebDriver driver = new FirefoxDriver(options);
		
		return driver;
	}

}
