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

import utils.Utils;

public class ContaGetter implements Runnable{

	private Thread t;
	private String cdc, dv;
	private String[] info;
	private boolean done;
	private boolean stop;
	private WebManager w;
	private int n;
	
	public boolean getDone() {
		return done;
	}
	
	public String[] getInfo() {
		return info;
	}
	
	public void setStop(boolean stop) {
		this.stop = stop;
	}
	
	public ContaGetter(String cdc, String dv, String[] info, WebManager w, int n) {
		this.cdc = cdc;
		this.dv = dv;
		this.info = info;
		done = false;
		stop = false;
		this.w = w;
		this.n = n;
		//pbf = new ProgressBarFiller(this);
		//pbf.start();
	}
	
	@Override
	public void run() throws TimeoutException{
		try {
			info = getConta(cdc, dv);	
		}catch(TimeoutException ex) {
			System.out.println("Timeout in run() from ContaGetter");
			throw ex;
		}
		System.out.println(info[0]);
		done = true;
		w.d[n] = true;
		while(!stop) {
			try {
				Thread.sleep(3000);
				Thread.yield();
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
				System.out.println("Timeout in start() from ContaGetter");
				throw ex;
			}
		}
	}
	
	String[] getConta(String cdc, String dv) throws TimeoutException{
		System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
		WebDriver driver = getFirefoxDriver();
	
		
		driver.get("http://aguaconta.cebinet.com.br/aguasc/");
		WebElement tCdc = driver.findElement(By.id("tCDC"));
		WebElement tDv = driver.findElement(By.id("tDV"));
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tCdc.sendKeys(cdc);
		tDv.sendKeys(dv);
		
		driver.findElement(By.id("tIr")).click();
		
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
		
		try {
			System.out.println(Utils.findDate());
			myDynamicElement = (new WebDriverWait(driver, 15)).until(ExpectedConditions.presenceOfElementLocated(By.linkText(Utils.findDate())));
		}catch(TimeoutException ex) {
			System.out.println("Timeout waiting to find the date link");
			throw ex;
		}
		driver.findElement(By.linkText(Utils.findDate())).click();
		
		String winHandleBefore = driver.getWindowHandle();
		
		for(String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}
	   
		myDynamicElement = (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/form/p[1]/table[11]/tbody/tr/td[4]/div")));
	    String total = driver.findElement(By.xpath("/html/body/form/p[1]/table[11]/tbody/tr/td[4]/div")).getText();
	    String multaText = "Multa";
	    String multaValue = null;
	    
	    String vencimento = driver.findElement(By.xpath("/html/body/form/p[1]/table[11]/tbody/tr/td[2]/div")).getText();
		
		String[] values = new String[4];
		values[0] = total;
		values[3] = vencimento;
		
		String multa = findMulta(driver.getPageSource());
		System.out.println("Multa -> " + multa);
		values[1] = multa;
		values[2] = calculateShare(total, 15, multa);
		
		driver.close();
		driver.switchTo().window(winHandleBefore).close();
		
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
	
	String findMulta(String content) {
		int fromIndex = 0, valueIndex;
		String str = "Multa", valueStr;
		double multa = 0;
		
		fromIndex = content.indexOf(str, fromIndex);
		while(fromIndex != -1) {
			valueIndex = content.indexOf(",", fromIndex)-2;
			valueStr = content.substring(valueIndex, valueIndex+5);
			valueStr = valueStr.replace(",", ".");
			try {
				System.out.println("valueStr = " + valueStr);
				Double.parseDouble(valueStr);
			}catch(NumberFormatException e) {
				System.out.println("In catch");
				valueStr = valueStr.substring(1);
			}
//			if(valueStr.charAt(0) < 48 || content.charAt(0) > 57) {
//				System.out.println("valueStr = " + valueStr);
//				valueStr = valueStr.substring(1);
//			}
			multa += Double.valueOf(valueStr);
			fromIndex = content.indexOf(str, fromIndex+1);
		}
		
		return String.valueOf(multa);
	}

}
