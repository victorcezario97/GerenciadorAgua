package web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.Utils;

public class ContaGetter implements Runnable{

	private Thread t;
	private String cdc, dv;
	private String[] info;
	private boolean done;
	private boolean stop;
	private int progress;
	
	public int getProgress() {
		return progress;
	}
	
	public boolean getDone() {
		return done;
	}
	
	public String[] getInfo() {
		return info;
	}
	
	public void setStop(boolean stop) {
		this.stop = stop;
	}
	
	public ContaGetter(String cdc, String dv, String[] info) {
		this.cdc = cdc;
		this.dv = dv;
		this.info = info;
		done = false;
		stop = false;
	}
	
	@Override
	public void run() {
		info = getConta(cdc, dv);	
		done = true;
		while(!stop) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(dv + " waiting");
		}
		System.out.println(dv + " done");
		return;
	}
	
	public void start() {
		if(t == null) {
			t = new Thread(this, "Conta getter thread");
			t.start();
		}
	}
	
	String[] getConta(String cdc, String dv){
		WebDriver driver = getFirefoxDriver();
		
		driver.get("http://aguaconta.cebinet.com.br/aguasc/");
		WebElement tCdc = driver.findElement(By.id("tCDC"));
		WebElement tDv = driver.findElement(By.id("tDV"));
		
		tCdc.sendKeys(cdc);
		tDv.sendKeys(dv);
		
		driver.findElement(By.id("tIr")).click();
		progress = 10;
		
		@SuppressWarnings("unused")
		WebElement myDynamicElement = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("hlConta")));
		driver.findElement(By.id("hlConta")).click();
		progress = 30;
		
		myDynamicElement = (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(By.linkText(Utils.findDate())));
		driver.findElement(By.linkText(Utils.findDate())).click();
		progress = 50;
		
		String winHandleBefore = driver.getWindowHandle();
		
		for(String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}
	   
		myDynamicElement = (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/form/p[1]/table[11]/tbody/tr/td[4]/div")));
	    String total = driver.findElement(By.xpath("/html/body/form/p[1]/table[11]/tbody/tr/td[4]/div")).getText();
	    String multaText = "Multa por atraso";
	    String multaValue = null;
	    progress = 70;
	    
	    if(driver.getPageSource().contains(multaText)) {
	    		multaValue = driver.findElement(By.xpath("//table[@id='Table6']/tbody/tr[4]/td[6]")).getText();
	    }
	    
	    String vencimento = driver.findElement(By.xpath("/html/body/form/p[1]/table[11]/tbody/tr/td[2]/div")).getText();
	    progress = 90;
	    System.out.println(multaValue);
		
		driver.close();
		driver.switchTo().window(winHandleBefore).close();
		
		String[] values = new String[4];
		values[0] = total;
		values[1] = multaValue;
		values[2] = calculateShare(total, 15, multaValue);
		values[3] = vencimento;
		progress = 100;
		return values;
	}
	
	String calculateShare(String total, int n, String multa) {
		total = total.replace(",", ".");
		multa = multa.replace(",", ".");
		double tot = Double.valueOf(total) - Double.valueOf(multa);

	    return String.valueOf(Utils.round(tot/(n*1.0))).replace(".", ",");
	}
	
	WebDriver getFirefoxDriver() {
		FirefoxBinary binary = new FirefoxBinary();
		binary.addCommandLineOptions("--headless");
		FirefoxOptions options = new FirefoxOptions();
		options.setBinary(binary);
		WebDriver driver = new FirefoxDriver(options);
		
		return driver;
	}
	
	String findVencimento(String venc1, String venc2) {
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

}
