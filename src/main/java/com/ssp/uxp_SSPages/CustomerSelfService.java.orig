/**
 * 
 */
package com.ssp.uxp_SSPages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.testng.Assert;
import com.relevantcodes.extentreports.ExtentTest;
import com.ssp.support.Log;
import com.ssp.support.StopWatch;
import com.ssp.utils.ElementLayer;
import com.ssp.utils.WaitUtils;

/**
 * @author jheelum.dutta
 *
 */
public class CustomerSelfService extends LoadableComponent<CustomerSelfService> {


  private final WebDriver driver;
  private ExtentTest extentedReport;
  private boolean isPageLoaded;
  public ElementLayer uielement;
  private String sspSelfServiceURL;

  @FindBy(css = "[title='Customer Self Service']")
  WebElement titleAcceptance;
  @FindBy(css = "#BUT_E14D31D21EBB8E7F24944")
  WebElement btnsigninlocator;



  /**
   * 
   * Constructor class for Customer SelfService Page Here we initializing the driver for page
   * factory objects. For ajax element waiting time has added while initialization
   * 
   * @param driver : Webdriver
   */
  public CustomerSelfService(WebDriver driver, String url, ExtentTest report) {

    this.driver = driver;
    this.extentedReport = report;
    sspSelfServiceURL = url;
    PageFactory.initElements(driver, this);
  }



  /**
   * 
   * Constructor class for Customer SelfService Page Here we initializing the driver for page
   * factory objects. For ajax element waiting time has added while initialization
   * 
   * @param driver : Webdriver
   */
  public CustomerSelfService(WebDriver driver, ExtentTest report) {

    this.driver = driver;
    this.extentedReport = report;
    PageFactory.initElements(driver, this);
  }

  @Override
  protected void isLoaded() {

    if (!isPageLoaded) {
      Assert.fail();
    }

    if (isPageLoaded && !driver.getTitle().contains("CustomerSelfService")) {
      Log.fail("SSP CustomerSelfService  Page did not open up. Site might be down.", driver,
          extentedReport);
    }
    uielement = new ElementLayer(driver);
  }

  @Override
  protected void load() {

    isPageLoaded = true;
    driver.get(sspSelfServiceURL);
    WaitUtils.waitForPageLoad(driver);
    // WaitUtils.waitForElement(driver, txtUserName);

  }



  public boolean verifysignin() throws Exception {
    if (!WaitUtils.waitForElement(driver, btnsigninlocator))
      throw new Exception("Sign in button is not visible!");
    return true;
  }


  /*
   * Method to click on Sign in button.
   * 
   * @locator - btnsigninlocator
   * 
   */
  /**
   * Click signIn button on login page
   * 
   * @param extentedReport
   * @throws Exception
   * 
   */
  public void clickBtnSignIn(ExtentTest extentedReport, WebDriver driver) throws Exception {
    try {
      final long startTime = StopWatch.startTime();
      verifysignin();
      JavascriptExecutor executor = (JavascriptExecutor) driver;
      executor.executeScript("arguments[0].click();", btnsigninlocator);
      // btnSignIn.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked signIn button on login page ", driver, extentedReport);
      Log.event("Clicked signIn button on login page", StopWatch.elapsedTime(startTime));
    } catch (Exception e) {
      throw new Exception("Error while clicking signin button : " + e);
    }

  }



}
