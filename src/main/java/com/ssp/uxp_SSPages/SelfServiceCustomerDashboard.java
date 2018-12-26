/**
 * 
 */
package com.ssp.uxp_SSPages;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
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
 * @author purendra.agrawal
 *
 */
public class SelfServiceCustomerDashboard extends LoadableComponent<SelfServiceCustomerDashboard> {

  private WebDriver driver;
  private boolean isPageLoaded;
  public ElementLayer uielement;

  @FindBy(css = "[title='Customer Dashboard']")
  WebElement titleAcceptance;

  @FindBy(css = "#BUT_B3A80BADFCD28CFD90816_R1")
  WebElement btnConQuote;

  @FindBy(css = "button[id='IDManageContact']")
  WebElement btnEditContactDetails;

  public SelfServiceCustomerDashboard(WebDriver driver) {
    this.driver = driver;
    ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, WaitUtils.maxElementWait);
    PageFactory.initElements(finder, this);
  }

  @Override
  protected void isLoaded() {

    if (!isPageLoaded) {
      Assert.fail();
    }

    (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
        .ignoring(NoSuchElementException.class).withMessage("SIAAS Home page did not open up."))
            .until(ExpectedConditions.visibilityOf(titleAcceptance));

    uielement = new ElementLayer(driver);
  }

  @Override
  protected void load() {

    isPageLoaded = true;
    WaitUtils.waitForPageLoad(driver);

  }

  /**
   * verify Sign in page is displayed
   * 
   * @param extentedReport
   * @throws Exception
   * 
   */

  public boolean verifySelfServiceCustomerDashboardPage() throws Exception {
    WaitUtils.waitForSpinner(driver);
    if (!WaitUtils.waitForElement(driver, titleAcceptance))
      throw new Exception("Customer Dashboard is  not loaded");
    return true;
  }

  /**
   * Click on Continue button
   * 
   * @return Void
   * @throws Exception
   * 
   */
  public void clickonContinueQuotebtn(ExtentTest extentedReport) throws Exception {
    try {
      final long startTime = StopWatch.startTime();
      WaitUtils.waitForElement(driver, btnConQuote);
      JavascriptExecutor executor = (JavascriptExecutor) driver;
      executor.executeScript("arguments[0].click();", btnConQuote);
      // btnSignIn.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Continue button on customer Dashboard ", driver, extentedReport);
      Log.event("Clicked on Continue buttonon customer Dashboard ",
          StopWatch.elapsedTime(startTime));
    } catch (Exception e) {
      throw new Exception("Error while clicking on Continue button : " + e);
    }


  }
}
