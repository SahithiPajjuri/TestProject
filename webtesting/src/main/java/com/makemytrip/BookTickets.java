package com.makemytrip;

import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.support.ui.WebDriverWait;
//import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;


public class BookTickets {

  private WebDriver driver;
  static ExtentTest test;
  static ExtentReports report;

  @BeforeClass
  public static void startTest()
  {
  report = new ExtentReports(System.getProperty("user.dir")+"\\ExtentReportResults.html");
  test = report.startTest("BookTickets");
  }
  @BeforeMethod
  public void beforeClass() {
	  System.setProperty("webdriver.chrome.driver","D:\\ChromeDriver\\chromedriver.exe"); 
	  driver = new ChromeDriver();
  }
  @Test
  public void flightsFromHydToMumbai() throws Exception {
	  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

      driver.get("https://www.makemytrip.com/");
      WebDriverWait wait = new WebDriverWait(driver, 10);
      driver.manage().window().maximize();
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//img[@alt='Make My Trip']")));
      String isSelected = driver.findElement(By.xpath("//li[@data-cy='oneWayTrip']")).getAttribute("class");
      if(!isSelected.equals("selected")) {
    	  driver.findElement(By.xpath("//li[@data-cy='oneWayTrip']")).click();
    	  Thread.sleep(2000);
      }
      driver.findElement(By.id("fromCity")).click();
      driver.findElement(By.xpath("//input[@placeholder='From']")).sendKeys("Hyderabad");
      driver.findElement(By.xpath("//p[contains(text(),'SUGGESTIONS')]/../../ul/li")).click();
      Thread.sleep(3000);
      WebElement toCity = driver.findElement(By.id("toCity"));
      ((JavascriptExecutor) driver).executeScript("arguments[0].click();", toCity);
      driver.findElement(By.xpath("//input[@placeholder='To']")).sendKeys("Mumbai");
      Thread.sleep(2000);
      driver.findElement(By.xpath("//p[contains(text(),'SUGGESTIONS')]/../../ul/li")).click();
      Thread.sleep(3000);
      
      LocalDate today = LocalDate.now();
      int currentMonth = today.getMonthValue();
      System.out.println("currentMonth"+currentMonth);
      LocalDate givenDt = LocalDate.parse("2020-08-25");
      int givenMonth = givenDt.getMonthValue();
      System.out.println("givenMonth"+givenMonth);
      if(givenMonth>currentMonth) {
    	  WebElement departure = driver.findElement(By.xpath("//label[@for='departure']"));
          ((JavascriptExecutor) driver).executeScript("arguments[0].click();", departure);
          int diff = givenMonth-currentMonth;
          if(diff>=2) {
        	  int nxtMnth = diff/2;
        	  for(int i=1;i<=nxtMnth;i++) {
            	  driver.findElement(By.xpath("//span[@aria-label='Next Month']")).click();
	        	  Thread.sleep(2000);
	        	  String monthName = givenDt.getMonth().name();
	        	  String year = String.valueOf(givenDt.getYear());
	        	  List<WebElement> mnthYr = driver.findElements(By.xpath("//div[@class='DayPicker-Months']/div"));
	        	  int mnthYrDisplayed = mnthYr.size();
	        	  for (int j=1;j<=mnthYrDisplayed;j++) {
	        		  String mnthYearDisplayed = driver.findElement(By.xpath("//div[@class='DayPicker-Months']/div["+j+"]/div[@class='DayPicker-Caption']/div")).getText();
	        		  if(mnthYearDisplayed.equalsIgnoreCase(monthName+" "+year)) {
	        			  String mnthNdDt = monthName.substring(0,1)+monthName.substring(1,3).toLowerCase()+" "+givenDt.getDayOfMonth();
	        			  driver.findElement(By.xpath("//div[contains(@aria-label,'"+mnthNdDt+" "+year+"')]//p")).click();
	        	          Thread.sleep(2000); 
	        		  }
	        	  }
        	  }
          }
          driver.findElement(By.xpath("//a[text()='Search']")).click();
          Thread.sleep(5000);
          for (int k=1;k<=10;k++) {
	          ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
	          Thread.sleep(6000);
          }
          List<WebElement> searchResults = driver.findElements(By.xpath("//div[@id='left-side--wrapper']/div[3]/div/div"));
    	  int totNoOfFlights = searchResults.size();
    	  System.out.println("totNoOfFlights"+totNoOfFlights);
      } 
  }
  
  @AfterMethod
  public void afterClass() {
	  driver.quit();
  }
  
  @AfterClass
  public static void endTest()
  {
  report.endTest(test);
  report.flush();
  }
}
