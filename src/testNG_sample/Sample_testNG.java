package testNG_sample;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class Sample_testNG {
	
	@BeforeSuite
	public void browserSetup()
	{
		System.out.println("Crome Browser Launched");
	}
	@BeforeTest
	public void openAPP()
	{
		System.out.println("App opened");
	}
	@BeforeClass
	public void enterURL()
	{
		System.out.println("Enter URL:");
	}
	@BeforeMethod
	public void launchResult()
	{
		System.out.println("Search result found.");
	}
	@Test
	public void testResult()
	{
		System.out.println("Logo Appeared.");
	}
	@AfterMethod
	public void closeWeb()
	{
		System.out.println("Search result is closed.");
	}
	@AfterClass
	public void closeApp()
	{
		System.out.println("Close App.");
	}
	@AfterTest
	public void deleteCookies()
	{
		System.out.println("Delete all cookies.");
	}
	

}
