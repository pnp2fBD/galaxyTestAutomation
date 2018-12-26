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
import org.openqa.selenium.interactions.Actions;
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
import com.ssp.utils.GenericUtils;
import com.ssp.utils.WaitUtils;

/**
 * @author jheelum.dutta
 *
 */
public class SummaryPage extends LoadableComponent<SummaryPage> {
  private final WebDriver driver;
  private ExtentTest extentedReport;
  private boolean isPageLoaded;
  public ElementLayer uielement;

  @FindBy(css = "[title='Summary']")
  WebElement titleAcceptance;

  @FindBy(css = "#SingleSelectCheckbox_imp > div:nth-child(1) > label > span")
  WebElement checkbox_CustAgree;
  @FindBy(css = "#BUT_72C3D2CAFB89E17B3478")
  WebElement btnPayment;

  public SummaryPage(WebDriver driver, ExtentTest report) {


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

    if (isPageLoaded && !driver.getTitle().contains("Summary")) {
      Log.fail("Summary Page did not open up. Site might be down.", driver, extentedReport);
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



  public void tickCustomerAgreesCheckbox(ExtentTest extentedReport) throws Exception {
    try {
      GenericUtils.scrollIntoView(driver, checkbox_CustAgree);
      if (!checkbox_CustAgree.isSelected()) {
        checkbox_CustAgree.click();
        WaitUtils.waitForSpinner(driver);
        Log.message("Checked the Customer agrees... checkbox", extentedReport);
      } else {
        Log.message("The Customer agrees checkbox is already checked", extentedReport);
      }
    } catch (Exception e) {
      throw new Exception("Exception in checking the Customer Agree checkbox" + e);
    }
  }



  public void clickOnPaymentbtn(ExtentTest extentedReport) throws Exception {
    try {
      final long startTime = StopWatch.startTime();
      WaitUtils.waitForElement(driver, btnPayment);
      JavascriptExecutor executor = (JavascriptExecutor) driver;
      executor.executeScript("arguments[0].click();", btnPayment);
      // btnSignIn.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Confirmation button on Your Quote page ", driver, extentedReport);
      Log.event("Clicked on button on Payment option page", StopWatch.elapsedTime(startTime));
    } catch (Exception e) {
      throw new Exception("Error while clicking on Confirmation button : " + e);
    }
  }

}
