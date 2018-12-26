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
public class YourQuoteSelfService extends LoadableComponent<YourQuoteSelfService> {
  private final WebDriver driver;
  private ExtentTest extentedReport;
  private boolean isPageLoaded;
  public ElementLayer uielement;



  @FindBy(css = "[title='Your Details']")
  WebElement titleAcceptance;


  @FindBy(css = "#BUT_72C3D2CAFB89E17B3478")
  WebElement btnsummary;

  /**
   * 
   * Constructor class for Customer SelfService Page Here we initializing the driver for page
   * factory objects. For ajax element waiting time has added while initialization
   * 
   * @param driver : Webdriver
   */
  public YourQuoteSelfService(WebDriver driver, ExtentTest report) {

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

    if (isPageLoaded && !driver.getTitle().contains("Quote")) {
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


  /**
   * Click on Continue button
   * 
   * @return Void
   * @throws Exception
   * 
   */
  public void clickOnSummarybtn(ExtentTest extentedReport) throws Exception {
    try {
      final long startTime = StopWatch.startTime();
      WaitUtils.waitForElement(driver, btnsummary);
      JavascriptExecutor executor = (JavascriptExecutor) driver;
      executor.executeScript("arguments[0].click();", btnsummary);
      // btnSignIn.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on summary button on Your Quote page ", driver, extentedReport);
      Log.event("Clicked on button on Your Quote page", StopWatch.elapsedTime(startTime));
    } catch (Exception e) {
      throw new Exception("Error while clicking on Continue button : " + e);
    }



  }
}
