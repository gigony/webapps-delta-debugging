package edu.unl.webtesting.sample;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SeleniumFlightWD {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://129.93.164.127:4647/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testSeleniumFlightWD() throws Exception {
    driver.get(baseUrl + "/wong/");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("mouna@cse.unl.edu");
    driver.findElement(By.name("password")).clear();
    driver.findElement(By.name("password")).sendKeys("Maths2069");
    driver.findElement(By.name("cookie")).click();
    driver.findElement(By.cssSelector("input.button")).click();
    driver.findElement(By.name("source")).clear();
    driver.findElement(By.name("source")).sendKeys("atl");
    driver.findElement(By.name("destination")).clear();
    driver.findElement(By.name("destination")).sendKeys("clt");
    driver.findElement(By.id("departure")).click();
    driver.findElement(By.linkText("30")).click();
    driver.findElement(By.name("seats")).clear();
    driver.findElement(By.name("seats")).sendKeys("1");
    driver.findElement(By.name("seats")).clear();
    driver.findElement(By.name("seats")).sendKeys("2");
    driver.findElement(By.name("Search")).click();
    driver.findElement(By.xpath("//input[@value='$126.00\nContinue ▸']")).click();
    driver.findElement(By.cssSelector("input.button")).click();
    driver.findElement(By.xpath("//input[@value='Check ▸\nout']")).click();
    driver.findElement(By.xpath("//input[@value='Check ▸\nout']")).click();
    driver.findElement(By.name("AccountHolder")).clear();
    driver.findElement(By.name("AccountHolder")).sendKeys("Mouna Hammoudi");
    driver.findElement(By.name("AccountNumber")).clear();
    driver.findElement(By.name("AccountNumber")).sendKeys("17002");
    driver.findElement(By.name("RoutingNumber")).clear();
    driver.findElement(By.name("RoutingNumber")).sendKeys("1234567910");
    driver.findElement(By.cssSelector("input.button")).click();
    driver.findElement(By.name("AccountHolder")).clear();
    driver.findElement(By.name("AccountHolder")).sendKeys("Mouna Hammoudi");
    driver.findElement(By.name("AccountNumber")).clear();
    driver.findElement(By.name("AccountNumber")).sendKeys("17002");
    driver.findElement(By.name("RoutingNumber")).clear();
    driver.findElement(By.name("RoutingNumber")).sendKeys("123456790");
    driver.findElement(By.cssSelector("input.button")).click();
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
