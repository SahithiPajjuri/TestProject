package com.cleartrip;

import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;


public class BookTicketsOneWay {

  private WebDriver driver;
  ExtentTest logger;
  ExtentHtmlReporter htmlReporter;
  ExtentReports extent;

  @BeforeClass
  public void startTest()
  {
	  htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir")+"\\CleartripSearchResults.html");
	  extent = new ExtentReports ();
	  extent.attachReporter(htmlReporter);
	  extent.setSystemInfo("Host Name", "cleartrip");
	  extent.setSystemInfo("Environment", "Automation Testing");
	  extent.setSystemInfo("User Name", "Sahithi");
		
	  htmlReporter.config().setDocumentTitle("Title of the Report Comes here");
	  htmlReporter.config().setReportName("Name of the Report Comes here");
	  htmlReporter.config().setTheme(Theme.STANDARD);
  }
  @BeforeMethod
  public void beforeClass() {
	  System.setProperty("webdriver.chrome.driver","D:\\ChromeDriver\\chromedriver.exe"); 
	  driver = new ChromeDriver();
  }
  @Test
  public void flightsFromHydToMumbai() throws Exception {
	  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

      driver.get("https://www.cleartrip.com/");
      WebDriverWait wait = new WebDriverWait(driver, 10);
      driver.manage().window().maximize();
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@title='Cleartrip ']")));
      String isChecked = driver.findElement(By.id("OneWay")).getAttribute("checked");
      if(!isChecked.equals("checked")) {
    	  driver.findElement(By.id("OneWay")).click();
    	  Thread.sleep(2000);
      }
      driver.findElement(By.id("FromTag")).sendKeys("Hyderabad");
      wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@id='ui-id-1']/li[1]"))).click();
      Thread.sleep(3000);
      driver.findElement(By.id("ToTag")).sendKeys("Mumbai");
      wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@id='ui-id-2']/li[1]"))).click();
      Thread.sleep(3000);
      
      LocalDate today = LocalDate.now();
      int currentMonth = today.getMonthValue();
      System.out.println("currentMonth"+currentMonth);
      LocalDate givenDt = LocalDate.parse("2020-08-25");
      int givenMonth = givenDt.getMonthValue();
      System.out.println("givenMonth"+givenMonth);
      if(givenMonth>currentMonth) {
    	  WebElement departure = driver.findElement(By.id("DepartDate"));
          ((JavascriptExecutor) driver).executeScript("arguments[0].click();", departure);
          int diff = givenMonth-currentMonth;
          if(diff>=2) {
        	  for(int i=1;i<=(diff-1);i++) {
            	  driver.findElement(By.xpath("//a[@class='nextMonth ']")).click();
	        	  Thread.sleep(2000);
        	  }
        	  String monthName = givenDt.getMonth().name();
        	  String year = String.valueOf(givenDt.getYear());
        	  String mnthDisplayed = driver.findElement(By.xpath("//div[@class='monthBlock last']/div/div/span[1]")).getText();
        	  Assert.assertTrue(monthName.equalsIgnoreCase(mnthDisplayed));
        	  String yrDisplayed = driver.findElement(By.xpath("//div[@class='monthBlock last']/div/div/span[2]")).getText();
        	  Assert.assertEquals(year, yrDisplayed);
        	  int dt = givenDt.getDayOfMonth();
  	          driver.findElement(By.xpath("//div[@class='monthBlock last']//a[text()='"+dt+"']")).click();
  	          Thread.sleep(2000); 
          }         
          Select se = new Select(driver.findElement(By.id("Adults")));
          se.selectByIndex(1);
          Select se1 = new Select(driver.findElement(By.id("Childrens")));
          se.selectByIndex(0);
          Select se2 = new Select(driver.findElement(By.id("Infants")));
          se.selectByIndex(0);         
          driver.findElement(By.id("SearchBtn")).click();
          Thread.sleep(10000);
          driver.findElement(By.xpath("//p[contains(text(),'Non-stop')]/../../../div[1]/input")).click();
          Thread.sleep(10000);
          for (int k=1;k<=10;k++) {
	          ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
	          Thread.sleep(3000);
          }
          ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, -document.body.scrollHeight);");
          List<WebElement> searchResults = driver.findElements(By.xpath("//div[contains(@class,'sticky__parent')]/../div[7]/div"));
    	  int totNoOfFlights = searchResults.size();
    	  System.out.println("totNoOfFlights"+totNoOfFlights);
    	  
    	  //create an excel and copy the search result into excel
    	  File file = new File(System.getProperty("user.dir")+"\\FlightSearchResult.xlsx");
    	  XSSFWorkbook wb = new XSSFWorkbook();
    	  XSSFSheet sh = wb.createSheet("HydToMumbai");
    	  sh.createRow(0).createCell(0).setCellValue("Flight No");
    	  sh.getRow(0).createCell(1).setCellValue("Flight Name");
    	  sh.getRow(0).createCell(2).setCellValue("Flight Duration");
    	  sh.getRow(0).createCell(3).setCellValue("Departure time");
    	  sh.getRow(0).createCell(4).setCellValue("Arrival time");
    	  sh.getRow(0).createCell(5).setCellValue("Price");
    	  
    	  for(int j=1;j<=totNoOfFlights;j++) {
    		  System.out.println("Entered for");
    		  String rsFlightDuration = driver.findElement(By.xpath("//div[contains(@class,'sticky__parent')]/../div[7]/div["+j+"]/div/div[2]/div[2]//div[3]/p")).getText();
    		  System.out.println("rsFlightDuration"+rsFlightDuration);
    		  sh.createRow(j).createCell(2).setCellValue(rsFlightDuration);
    		  
    		  String rsDepartureTime = driver.findElement(By.xpath("//div[contains(@class,'sticky__parent')]/../div[7]/div["+j+"]/div/div[2]/div[2]/div/div[1]/p")).getText();
    		  sh.getRow(j).createCell(3).setCellValue(rsDepartureTime);
    		  System.out.println("rsDepartureTime"+rsDepartureTime);
    		  
    		  String rsArrivalTime = driver.findElement(By.xpath("//div[contains(@class,'sticky__parent')]/../div[7]/div["+j+"]/div/div[2]/div[2]/div/div[2]/p")).getText();
    		  sh.getRow(j).createCell(4).setCellValue(rsArrivalTime);
    		  System.out.println("rsArrivalTime"+rsArrivalTime);
    		  
    		  String rsPrice = driver.findElement(By.xpath("//div[contains(@class,'sticky__parent')]/../div[7]/div["+j+"]/div/div[2]/div[3]/div[2]/p")).getText();
    		  System.out.println("rsPrice"+rsPrice);
    		  sh.getRow(j).createCell(5).setCellValue(rsPrice);
    		  driver.findElement(By.xpath("//div[contains(@class,'sticky__parent')]/../div[7]/div["+j+"]/div/div[2]/div/div/span")).click();
              Thread.sleep(1000);
              
              String rsFlightName = driver.findElement(By.xpath("//div[contains(@class,'sticky__parent')]/../div[7]/div["+j+"]/div/div[3]/div/div/div[2]//div/div/div/div/p")).getText();
    		  sh.getRow(j).createCell(1).setCellValue(rsFlightName);
    		  System.out.println("rsFlightName"+rsFlightName);
    		  
    		  String rsFlightNo = driver.findElement(By.xpath("//div[contains(@class,'sticky__parent')]/../div[7]/div["+j+"]/div/div[3]/div/div/div[2]//div/div/div/div/div/p[1]")).getText();
    		  sh.getRow(j).createCell(0).setCellValue(rsFlightNo);
    		  System.out.println("rsFlightNo"+rsFlightNo);
    		  System.out.println("--------------------------------------------------------------------------------------");
    		  
    		  int divisibleByFive = j%5;
    		  if(divisibleByFive == 0) {
    			  ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, "+(j*500)+")");
    		  }
    	  }
    	  
    	  try {
    		  FileOutputStream fos = new FileOutputStream(file);
        	  wb.write(fos);
    	  }catch(Exception e) {
    		  e.printStackTrace();
    	  }
    	  
      } 
  }
  
  @AfterMethod
  public void afterClass(ITestResult result) {
	  driver.quit();
	  System.out.println("result"+result.getStatus());
	  if(result.getStatus() == ITestResult.FAILURE){
		  System.out.println("Entered fail");
			//logger.log(Status.FAIL, "Test Case Failed is "+result.getName());
			//MarkupHelper is used to display the output in different colors
		  	logger = extent.createTest("BookTicketsOneWay");
			Assert.assertFalse(false);
			logger.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " - Test Case Failed", ExtentColor.RED));
		}else if(result.getStatus() == ITestResult.SKIP){
			System.out.println("Entered skip");
			//logger.log(Status.SKIP, "Test Case Skipped is "+result.getName());
			logger.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE));	
		}else if(result.getStatus() == ITestResult.SUCCESS) { 
			System.out.println("Entered success");
			logger = extent.createTest("BookTicketsOneWay");
			System.out.println("logger"+logger);
			Assert.assertTrue(true);
			logger.log(Status.PASS, MarkupHelper.createLabel(result.getName() + " - Test Case Passed", ExtentColor.GREEN));
		}  
  }
  
  @AfterClass
  public void endReport()
  {
	  extent.flush();
  }
}
