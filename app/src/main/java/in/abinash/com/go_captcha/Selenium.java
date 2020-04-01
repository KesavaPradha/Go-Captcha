package in.abinash.com.go_captcha;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class Selenium {

    public static void main(String[] args) throws Exception {



        System.setProperty("webdriver.chrome.driver", "C:\\Users\\KESAVA PRADHA\\Downloads\\chromedriver_win32\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("http://www.google.com");

    }



}