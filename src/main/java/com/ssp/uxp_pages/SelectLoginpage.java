/**
 * 
 */
package com.ssp.uxp_pages;

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
import com.ssp.utils.WaitUtils;

/**
 * @author jheelum.dutta
 *
 */
public class SelectLoginpage extends LoadableComponent<SelectLoginpage> {


  private final WebDriver driver;
  private ExtentTest extentedReport;
  private String sspURL;
  private boolean isPageLoaded;
  public String spinner = "div.spinning-on-load-bg-table-active";
  public final String ERROR_MSG_LOGIN =
      "We do not recognise your details. Please re-enter your credentials";
  /**********************************************************************************************
   ********************************* WebElements of Login Page **********************************
   **********************************************************************************************/

  @FindBy(css = "input#base_homePage_logon_user")
  WebElement txtUserName;

  @FindBy(css = "input#base_homePage_logon_pass")
  WebElement txtPassWord;

  @FindBy(css = "button#btn_login-ok-btn")
  WebElement btnSignIn;

  /**********************************************************************************************
   ********************************* WebElements of Login Page - Ends ***************************
   **********************************************************************************************/

  /**
   * 
   * Constructor class for Login page Here we initializing the driver for page factory objects. For
   * ajax element waiting time has added while initialization
   * 
   * @param driver : Webdriver
   * @param url : Login Page URL
   * @param extentedReport
   */
  public SelectLoginpage(WebDriver driver, String url, ExtentTest report) {

    this.driver = driver;
    this.extentedReport = report;
    sspURL = url;
    PageFactory.initElements(driver, this);
  }

  /**
   * 
   * Constructor class for Login page Here we initializing the driver for page factory objects. For
   * ajax element waiting time has added while initialization
   * 
   * @param driver : Webdriver
   */
  public SelectLoginpage(WebDriver driver, ExtentTest report) {

    this.driver = driver;
    this.extentedReport = report;
    PageFactory.initElements(driver, this);
  }

  @Override
  protected void isLoaded() {

    WaitUtils.waitForPageLoad(driver);

    if (!isPageLoaded) {
      Assert.fail();
    }

    if (isPageLoaded && !driver.getTitle().contains("Login")) {
      Log.fail("SSP Login Page did not open up. Site might be down.", driver, extentedReport);
    }
  }

  @Override
  protected void load() {

    isPageLoaded = true;
    driver.get(sspURL);
    WaitUtils.waitForPageLoad(driver);
    WaitUtils.waitForElement(driver, txtUserName);

  }

  /**
   * Login to SSP
   * 
   * @param username
   * @param password
   * @param screenShot
   * @param extentedReport
   * @return Home Page
   * @throws Exception
   * 
   */

  public SelectHomePage loginToSSP(String username, String password, boolean screenShot,
      ExtentTest extentedReport) throws Exception {
    try {

      if (sspURL != null)
        Log.event("Launched SSP site:: " + sspURL);

      Log.event("Login to the SSP");
      enterUserName(username, extentedReport);
      enterPassword(password, extentedReport);
      // rmbrUsername.click();
      clickBtnSignIn(extentedReport);
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForPageLoad(driver);
      if (isPageLoaded && driver.getTitle().toLowerCase().contains("login")) {
        throw new Exception("Invalid Login Details");
      }

      return new SelectHomePage(driver).get();
    } catch (Exception e) {
      throw new Exception("Error while login to application : " + e);
    }
  }

  /**
   * Verify User Name is displayed
   * 
   * @return boolean
   * @throws Exception
   * 
   */
  public boolean verifyUserNameField() throws Exception {
    try {
      return WaitUtils.waitForElement(driver, txtUserName);
    } catch (Exception e) {
      throw new Exception("Error while verifying username field : " + e);
    }
  }

  /**
   * Verify Password is displayed
   * 
   * @return boolean
   * @throws Exception
   * 
   */
  public boolean verifyPasswordField() throws Exception {
    try {
      return WaitUtils.waitForElement(driver, txtPassWord);
    } catch (Exception e) {
      throw new Exception("Error while verifying Password field : " + e);
    }
  }

  /**
   * To enter user name
   * 
   * @param userName
   * @param extentedReport
   * @throws Exception
   * 
   */
  public void enterUserName(String userName, ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForElement(driver, txtUserName);
      txtUserName.clear();
      txtUserName.sendKeys(userName);
      Log.message("Entered the UserName : " + userName, driver, extentedReport);
    } catch (Exception e) {
      throw new Exception("Error while entering Username : " + e);
    }
  }

  /**
   * To Enter password
   * 
   * @param pwd
   * @param extentedReport
   * @throws Exception
   * 
   */
  public void enterPassword(String pwd, ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForElement(driver, txtPassWord);
      txtPassWord.clear();
      txtPassWord.sendKeys(pwd);
      Log.message("Entered the Password: " + pwd, driver, extentedReport);
    } catch (Exception e) {
      throw new Exception("Error while entering password : " + e);
    }
  }

  /**
   * Click signIn button on login page
   * 
   * @param extentedReport
   * @throws Exception
   * 
   */
  public void clickBtnSignIn(ExtentTest extentedReport) throws Exception {
    try {
      final long startTime = StopWatch.startTime();
      WaitUtils.waitForElement(driver, btnSignIn);
      JavascriptExecutor executor = (JavascriptExecutor) driver;
      executor.executeScript("arguments[0].click();", btnSignIn);
      // btnSignIn.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked signIn button on login page ", driver, extentedReport);
      Log.event("Clicked signIn button on login page", StopWatch.elapsedTime(startTime));
    } catch (Exception e) {
      throw new Exception("Error while clicking signin button : " + e);
    }

  }



}
