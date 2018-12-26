/**
 * 
 */
package com.ssp.uxp_SSPages;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;
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
public class CustomerSigninPage extends LoadableComponent<CustomerSigninPage> {

  private final WebDriver driver;
  private ExtentTest extentedReport;
  private boolean isPageLoaded;
  public ElementLayer uielement;
  // Customer Sign in

  @FindBy(css = "[title='Customer Sign in']")
  WebElement titleAcceptance;
  @FindBy(css = "#email_address")
  WebElement txtUserName;

  @FindBy(css = "#password")
  WebElement txtPassWord;


  @FindBy(css = "#sign_in")
  WebElement btnSignIn;

  public CustomerSigninPage(WebDriver driver) {
    this.driver = driver;
    ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, WaitUtils.maxElementWait);
    PageFactory.initElements(finder, this);
  }



  @Override
  protected void isLoaded() {

    if (!isPageLoaded) {
      Assert.fail();
    }

    if (isPageLoaded && !driver.getTitle().contains("Customer Sign in")) {
      Log.fail("Customer Sign in  Page of Self Service did not open up. Site might be down.",
          driver, extentedReport);
    }
    uielement = new ElementLayer(driver);
  }

  @Override
  protected void load() {

    isPageLoaded = true;
    WaitUtils.waitForPageLoad(driver);

    (new WebDriverWait(driver, 20).pollingEvery(200, TimeUnit.MILLISECONDS)
        .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
        .withMessage("Buyquote page not loaded properly"))
            .until(ExpectedConditions.visibilityOf(titleAcceptance));
  }

  /**
   * Verify User Name is displayed
   * 
   * @return boolean
   * @throws Exception
   * 
   */
  public boolean verifyUserEmailField() throws Exception {
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
  public void enterUserEmail(String userName, ExtentTest extentedReport) throws Exception {
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

  /**
   * verify Sign in page is displayed
   * 
   * @param extentedReport
   * @throws Exception
   * 
   */

  public boolean verifyCustomersiginPage() throws Exception {
    WaitUtils.waitForSpinner(driver);
    if (!WaitUtils.waitForElement(driver, titleAcceptance))
      throw new Exception("Customer sigin Page is not loaded");
    return true;
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

  public SelfServiceCustomerDashboard loginToSSPCustomerSelfService(String username,
      String password, boolean screenShot, ExtentTest extentedReport) throws Exception {
    try {
      /*
       * Enter userEmail,UserPassword
       */
      enterUserEmail(username, extentedReport);
      enterPassword(password, extentedReport);
      // rmbrUsername.click();
      clickBtnSignIn(extentedReport);
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForPageLoad(driver);
      System.out.println(driver.getTitle());
      if (isPageLoaded && driver.getTitle().toLowerCase().contains("Customer Dashboard")) {
        throw new Exception("Invalid Login Details");
      }

      return new SelfServiceCustomerDashboard(driver);
    } catch (Exception e) {
      throw new Exception("Error while login to application : " + e);
    }
  }

}
