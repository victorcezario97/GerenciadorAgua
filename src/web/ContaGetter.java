package web;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.ProgressBarFiller;
import utils.Utils;

public class ContaGetter implements Runnable{

	private Thread t;
	private String cdc, dv;
	private String[] info;
	private boolean done;
	private boolean stop;
	private int progress;
	private ProgressBarFiller pbf;
	
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
		//pbf = new ProgressBarFiller(this);
		//pbf.start();
	}
	
	@Override
	public void run() throws TimeoutException{
		try {
			info = getConta(cdc, dv);	
		}catch(TimeoutException ex) {
			System.out.println("OWWWWWWWWWWWWUWUWUWBUEBUE");
			throw ex;
		}
		System.out.println(info[0]);
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
	
	public void start() throws TimeoutException{
		if(t == null) {
			t = new Thread(this, "Conta getter thread");
			try {
				t.run();
			}catch(TimeoutException ex) {
				System.out.println("UAUAUAUAUAUAUAUAUAUuuuuuuuu");
				throw ex;
			}
		}
	}
	
	String[] getConta(String cdc, String dv) throws TimeoutException{
		WebDriver driver = getFirefoxDriver();
		
		driver.get("http://aguaconta.cebinet.com.br/aguasc/");
		WebElement tCdc = driver.findElement(By.id("tCDC"));
		WebElement tDv = driver.findElement(By.id("tDV"));
		
		tCdc.sendKeys(cdc);
		tDv.sendKeys(dv);
		
		driver.findElement(By.id("tIr")).click();
		progress = 10;
		
		@SuppressWarnings("unused")
		WebDriverWait wait = new WebDriverWait(driver, 30) {
			@Override
			protected java.lang.RuntimeException timeoutException(java.lang.String message,
                    java.lang.Throwable lastException){
				
				TimeoutException ex = new TimeoutException();
				
				throw ex;
			}
		};
		WebElement myDynamicElement;
		try {
			myDynamicElement = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("hlConta")));
		}catch(TimeoutException ex) {
			throw ex;
		}
		driver.findElement(By.id("hlConta")).click();
		progress = 30;
		
		try {
			myDynamicElement = (new WebDriverWait(driver, 5)).until(ExpectedConditions.presenceOfElementLocated(By.linkText(Utils.findDate())));
		}catch(TimeoutException ex) {
			System.out.println("OWWWWWWWWW");
			throw ex;
		}
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

}
