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
public class PaymentConfirmationPage extends LoadableComponent<PaymentConfirmationPage> {



  private final WebDriver driver;
  private ExtentTest extentedReport;
  private boolean isPageLoaded;
  public ElementLayer uielement;

  @FindBy(css = "[title='Payment Confirmation']")
  WebElement titleAcceptance;

  @FindBy(css = "#main > ol > li:nth-child(1) > form > button > span.payment-method__name")
  WebElement btnVisa;
  @FindBy(css = "#form-card_details\2e field-pan")
  WebElement txtCardNumber;
  @FindBy(
      css = "#main > div > form > div > div:nth-child(3) > div > div.layout__item.\37 \2f 10.lap-and-up-1\2f 1.form-group__controls-container > div > div")
  WebElement txtExpiry;

  @FindBy(css = "#form-card_details\2e field-cvc")
  WebElement txtCSV;
  @FindBy(
      css = "#main > div > form > div > div:nth-child(5) > div > div:nth-child(1) > div > button")
  WebElement btnConfirmcard;



  public PaymentConfirmationPage(WebDriver driver, ExtentTest report) {


    this.driver = driver;
    this.extentedReport = report;
    ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, WaitUtils.maxElementWait);
    PageFactory.initElements(driver, this);
  }

  @Override
  protected void load() {
    // TODO Auto-generated method stub
    if (!isPageLoaded) {
      Assert.fail();
    }

    if (isPageLoaded && !driver.getTitle().contains("Billing Adjustment")) {
      Log.fail("SSP Billing Adjustment Page did not open up. Site might be down.", driver,
          extentedReport);
    }
    uielement = new ElementLayer(driver);
  }



  @Override
  protected void isLoaded() throws Error {
    // TODO Auto-generated method stub

    isPageLoaded = true;
    WaitUtils.waitForPageLoad(driver);

    (new WebDriverWait(driver, 20).pollingEvery(200, TimeUnit.MILLISECONDS)
        .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
        .withMessage("Buyquote page not loaded properly"))
            .until(ExpectedConditions.visibilityOf(titleAcceptance));
  }

  // click on visa button
  public void clickOnVisabtn(ExtentTest extentedReport) throws Exception {
    try {
      final long startTime = StopWatch.startTime();
      WaitUtils.waitForElement(driver, btnVisa);
      JavascriptExecutor executor = (JavascriptExecutor) driver;
      executor.executeScript("arguments[0].click();", btnVisa);
      // btnSignIn.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Visa button on PaymentConfirmationPage ", driver, extentedReport);
      Log.event("Clicked on button on PaymentConfirmationPage", StopWatch.elapsedTime(startTime));
    } catch (Exception e) {
      throw new Exception("Error while clicking on Confirmation button : " + e);
    }

  }



  /**
   * To enter card Number
   * 
   * @param txtCardNo
   * @param extentedReport
   * @throws Exception
   * 
   */
  public void enterCardNumber(String txtCardNo, ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForElement(driver, txtCardNumber);
      txtCardNumber.clear();
      txtCardNumber.sendKeys(txtCardNo);
      Log.message("Entered the UserName : " + txtCardNo, driver, extentedReport);
    } catch (Exception e) {
      throw new Exception("Error while entering Username : " + e);
    }
  }

  /**
   * To enter card Expiry
   * 
   * @param txtExpiry
   * @param extentedReport
   * @throws Exception
   * 
   */
  public void enterExpiry(String txtExpirydata, ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForElement(driver, txtExpiry);
      txtExpiry.clear();
      txtExpiry.sendKeys(txtExpirydata);
      Log.message("Entered the UserName : " + txtExpiry, driver, extentedReport);
    } catch (Exception e) {
      throw new Exception("Error while entering Username : " + e);
    }
  }


  /**
   * To enter card CSV
   * 
   * @param txtCSV
   * @param extentedReport
   * @throws Exception
   * 
   */
  public void enterCSV(String txtCSVdata, ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForElement(driver, txtExpiry);
      // txtCSV.clear();
      // txtCSV.sendKeys(txtCSVdata);
      Log.message("Entered the UserName : " + txtExpiry, driver, extentedReport);
    } catch (Exception e) {
      throw new Exception("Error while entering Username : " + e);
    }
  }

  // click on visa button
  public void clickOnConfirmCardbtn(ExtentTest extentedReport) throws Exception {
    try {
      final long startTime = StopWatch.startTime();
      WaitUtils.waitForElement(driver, btnConfirmcard);
      JavascriptExecutor executor = (JavascriptExecutor) driver;
      executor.executeScript("arguments[0].click();", btnConfirmcard);
      // btnSignIn.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Confirm card button on PaymentConfirmationPage ", driver,
          extentedReport);
      Log.event("Clicked on button on PaymentConfirmationPage", StopWatch.elapsedTime(startTime));
    } catch (Exception e) {
      throw new Exception("Error while clicking on Card Details  button : " + e);
    }

  }

  // click on PayNow btn
  public void clickOnPayNowbtn(ExtentTest extentedReport) throws Exception {
    try {
      final long startTime = StopWatch.startTime();
      WaitUtils.waitForElement(driver, btnConfirmcard);
      JavascriptExecutor executor = (JavascriptExecutor) driver;
      executor.executeScript("arguments[0].click();", btnConfirmcard);
      // btnSignIn.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Pay Now button on PaymentConfirmationPage ", driver, extentedReport);
      Log.event("Clicked on button on PaymentConfirmationPage", StopWatch.elapsedTime(startTime));
    } catch (Exception e) {
      throw new Exception("Error while clicking on Pay Now button : " + e);
    }

  }

}
