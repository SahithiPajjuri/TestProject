

import org.testng.annotations.Test;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.testng.Assert;


public class googleSearch {
	
  private WebDriver driver;
	
  @BeforeMethod
  public void beforeClass() {
	  System.setProperty("webdriver.chrome.driver","D:\\ChromeDriver\\chromedriver.exe"); 
	  driver = new ChromeDriver();
  }

  @Test
  public void serchFunctionality(){
	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

    driver.get("http://www.google.com");

    String search_text = "Google Search";
    WebElement search_button = driver.findElement(By.name("btnK"));

    String text = search_button.getAttribute("value");

    Assert.assertEquals(text, search_text, "Text not found!");
  }
  
	@AfterMethod
	public void afterClass() {
		  driver.quit();
	}
}
