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

import java.util.concurrent.Callable;

@SuppressWarnings("rawtypes")
public class ContaGetterCallable implements Callable {

    private String cdc, dv;
    /*
     * [0] = total
     * [1] = multa
     * [2] = dividido
     * [3] = vencimento
     */
    private String[] info;
    private boolean done;
    private WebManager w;
    private int n;

    public boolean getDone() {
        return done;
    }

    public String[] getInfo() {
        return info;
    }

    public ContaGetterCallable(String cdc, String dv, String[] info, WebManager w, int n) {
        this.cdc = cdc;
        this.dv = dv;
        this.info = info;
        done = false;
        this.w = w;
        this.n = n;
    }

    @Override
    public Integer call() throws TimeoutException{
        try {
            info = getConta(cdc, dv);
        }catch(TimeoutException ex) {
            System.out.println("Timeout in run() from ContaGetter");
            throw ex;
        }
        System.out.println(info[0]);
        done = true;
        w.d[n] = true;
        System.out.println(dv + " done");
        return 0;
    }

    String[] getConta(String cdc, String dv) throws TimeoutException{
        System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
        WebDriver driver = getFirefoxDriver();


        driver.get("http://aguaconta.cebinet.com.br/aguasc/");
        
        //Page 1
        //Insert CDC and DV and click button Ir
        WebElement tCdc = driver.findElement(By.id("tCDC"));
        WebElement tDv = driver.findElement(By.id("tDV"));
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tCdc.sendKeys(cdc);
        tDv.sendKeys(dv);

        driver.findElement(By.id("tIr")).click();
        
        
        //Page 2
        //Click 'Segunda via'
        @SuppressWarnings("unused")
		WebElement myDynamicElement;
        try {
            myDynamicElement = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("hlConta")));
        }catch(TimeoutException ex) {
            driver.close();
            throw ex;
        }
        driver.findElement(By.id("hlConta")).click();

        
        //Page 3
        //Find the date corresponding to the current month and click on it
        try {
            //System.out.println(Utils.findDate());
            myDynamicElement = (new WebDriverWait(driver, 15)).until(ExpectedConditions.presenceOfElementLocated(By.linkText(Utils.findDate())));
        }catch(TimeoutException ex) {
            System.out.println("Timeout waiting to find the date link");
            driver.close();
            throw ex;
        }
        driver.findElement(By.linkText(Utils.findDate())).click();

        
        //Page 4
        //Switch handle to the bill page that opened and get all the values
        String winHandleBefore = driver.getWindowHandle();

        for(String winHandle : driver.getWindowHandles()) {
            if(!winHandle.equalsIgnoreCase(winHandleBefore)) driver.switchTo().window(winHandle);
        }

        //Finding total
        myDynamicElement = (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/form/p[1]/table[11]/tbody/tr/td[4]/div")));
        String total = driver.findElement(By.xpath("/html/body/form/p[1]/table[11]/tbody/tr/td[4]/div")).getText();

        //Finding due date
        String vencimento = driver.findElement(By.xpath("/html/body/form/p[1]/table[11]/tbody/tr/td[2]/div")).getText();

        //Finding late fee
        String multa = findMulta(driver.getPageSource());
        //System.out.println("Multa -> " + multa);
        
        //Calculating shared value
        String share = calculateShare(total, 15, multa);
        
        String[] values = new String[4];
        values[0] = total;
        values[1] = multa;
        values[2] = share;
        values[3] = vencimento;

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
            
            multa += Double.valueOf(valueStr);
            fromIndex = content.indexOf(str, fromIndex+1);
        }

        return String.valueOf(multa);
    }

}
