/**
 * 
 */
package com.ssp.uxp_SSPages;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
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
public class PaymentOptionsPage extends LoadableComponent<PaymentOptionsPage> {

  private final WebDriver driver;
  private ExtentTest extentedReport;
  private boolean isPageLoaded;
  public ElementLayer uielement;

  private String radio_button_value = null;

  public String radSelectedNo =
      "//*[@id='p4_QUE_16066C729BDC294C8881']/div/div[2]/label[2]/span[1]";

  @FindBy(css = "[title='Payment Options']")
  WebElement titleAcceptance;
  @FindBy(css = "#BUT_72C3D2CAFB89E17B3478")
  WebElement btnconfirmation;

  @FindBy(
      css = "#p4_QUE_16066C729BDC294C8881 > div > div.col-xs-12.col-sm-5.col-md-6 > label:nth-child(2) > span.radio")
  WebElement radNo_Ele;



  public PaymentOptionsPage(WebDriver driver, ExtentTest report) {


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

  public void clickOnConfirmationbtn(ExtentTest extentedReport) throws Exception {
    try {
      final long startTime = StopWatch.startTime();
      WaitUtils.waitForElement(driver, btnconfirmation);
      JavascriptExecutor executor = (JavascriptExecutor) driver;
      executor.executeScript("arguments[0].click();", btnconfirmation);
      // btnSignIn.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Confirmation button on Your Quote page ", driver, extentedReport);
      Log.event("Clicked on button on Payment option page", StopWatch.elapsedTime(startTime));
    } catch (Exception e) {
      throw new Exception("Error while clicking on Confirmation button : " + e);
    }

  }



  /**
   * select radio button -generic
   *
   * @throws Exception
   * @param locator : string
   * @param option : string
   * 
   */
  private void selectRadioButton(String locator, String option) throws Exception {
    try {
      List<WebElement> buttonOptions = driver.findElements(By.cssSelector(locator));

      for (int i = 0; i < buttonOptions.size(); i++) {
        radio_button_value = buttonOptions.get(i).getText();
        if (radio_button_value.contains(option)) {

          WaitUtils.waitForElement(driver, buttonOptions.get(i), 1);
          buttonOptions.get(i).click();
          Thread.sleep(1000);
          radio_button_value = null;
          break;
        }
      }
    } catch (Exception e) {
      throw new Exception("Error in Selecting radio button : " + option + e);
    }
  }

  /**
   * select Agree
   *
   * @param agree : String
   * @param extentReport
   * @throws Exception
   * 
   */
  public void selectAgree(String noSelected, ExtentTest extentReport) throws Exception {
    try {

      WaitUtils.waitForElement(driver, radNo_Ele);
      selectRadioButton(radSelectedNo, noSelected);
      Log.message("Billing Address Radiobutton selected as no :" + noSelected, extentReport);

    } catch (Exception e) {
      throw new Exception("Unable to select Agreement Statement" + e);
    }
  }

}
