import com.thoughtworks.selenium.Selenium;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.By;
import java.util.concurrent.TimeUnit;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

driver = new FirefoxDriver();
baseUrl = "http://www.google.com";
if (isWebDriver) {
	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
}
else
	selenium = new WebDriverBackedSelenium(driver, baseUrl);




