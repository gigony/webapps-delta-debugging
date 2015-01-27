package edu.unl.webtesting.sample;
import com.thoughtworks.selenium.Selenium;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.WebDriver;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.Pattern;
import static org.apache.commons.lang3.StringUtils.join;

public class SeleniumFight {
	private Selenium selenium;

	@Before
	public void setUp() throws Exception {
		WebDriver driver = new FirefoxDriver();
		String baseUrl = "http://129.93.164.127:4647/";
		selenium = new WebDriverBackedSelenium(driver, baseUrl);
	}

	@Test
	public void testSeleniumFight() throws Exception {
		selenium.open("/wong/");
		selenium.type("name=email", "mouna@cse.unl.edu");
		selenium.type("name=password", "Maths2069");
		selenium.click("name=cookie");
		selenium.click("css=input.button");
		selenium.waitForPageToLoad("30000");
		selenium.type("name=source", "atl");
		selenium.type("name=destination", "clt");
		selenium.click("id=departure");
		selenium.click("link=30");
		selenium.type("name=seats", "1");
		selenium.type("name=seats", "2");
		selenium.click("name=Search");
		selenium.waitForPageToLoad("30000");
		selenium.click("//input[@value='$126.00\nContinue ▸']");
		selenium.waitForPageToLoad("30000");
		selenium.click("css=input.button");
		selenium.click("//input[@value='Check ▸\nout']");
		selenium.waitForPageToLoad("30000");
		selenium.click("//input[@value='Check ▸\nout']");
		selenium.waitForPageToLoad("30000");
		selenium.type("name=AccountHolder", "Mouna Hammoudi");
		selenium.type("name=AccountNumber", "17002");
		selenium.type("name=RoutingNumber", "1234567910");
		selenium.click("css=input.button");
		selenium.type("name=AccountHolder", "Mouna Hammoudi");
		selenium.type("name=AccountNumber", "17002");
		selenium.type("name=RoutingNumber", "123456790");
		selenium.click("css=input.button");
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
