package com.ssp.uxp_pages;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.relevantcodes.extentreports.ExtentTest;
import com.ssp.support.Log;
import com.ssp.utils.ElementLayer;
import com.ssp.utils.GenericUtils;
import com.ssp.utils.WaitUtils;



public class SuspendBillingPage extends LoadableComponent<SuspendBillingPage> {
  private final WebDriver driver;
  private ExtentTest extentedReport;
  private boolean isPageLoaded;
  public ElementLayer uielement;

  @FindBy(css = ".breadcrumb.alert-danger")
  WebElement titleSuspendBilling;

  @FindBy(css = "[title='Accept']")
  WebElement btnAccept;

  @FindBy(css = "#C2__QUE_A32E2E21DB4003071328047")
  WebElement drpoptSuspendBillingReason;

  @FindBy(css = "#C2__QUE_24D10CAD2CBD74854948782")
  WebElement drpdwnPreferredPaymentDay;

  @FindBy(css = ".breadcrumb.alert-danger")
  WebElement panelSuspendBilling;

  @FindBy(css = "#C2__BUT_7499E8C9DD09DCA41348358")
  WebElement btnCancel;

  @FindBy(css = "#C2__FMT_FD051A198CB3861E3809021")
  WebElement mdlReturnToDashboard;

  @FindBy(css = "#C2__BUT_FD051A198CB3861E3809030")
  WebElement btnYes;

  @FindBy(css = "#C2__BUT_B13A0396B2DE40F42605037")
  WebElement tabCustomerDashboard;



  /**
   * 
   * Constructor class for Card Details Page Here we initializing the driver for page factory
   * objects. For ajax element waiting time has added while initialization
   * 
   * @param driver : Webdriver
   * @param extentedReport : ExtentTest
   */
  public SuspendBillingPage(WebDriver driver, ExtentTest extentedReport) {
    this.driver = driver;
    this.extentedReport = extentedReport;
    PageFactory.initElements(driver, this);
  }

  @Override
  protected void isLoaded() {

    if (!isPageLoaded) {
      Assert.fail();
    }

    if (isPageLoaded && !driver.getTitle().contains("Suspend Billing")) {
      Log.fail("User is not navigated to Suspended billing page", driver, extentedReport);
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
            .until(ExpectedConditions.visibilityOf(titleSuspendBilling));
  }

  /**
   * To complete SuspendBilling
   * 
   * @param reason
   * @param extentedReport
   * @param screenshot
   * @return CustDashboardPage
   * @throws Exception
   * 
   */
  public CustDashboardPage completeSuspendBilling(String reason, ExtentTest extentedReport,
      boolean screenshot) throws Exception {
    try {
      selectSuspendBillingReason(reason, extentedReport, screenshot);
      btnAccept.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Accept button is clicked after Reason for suspeded billing is selected", driver,
          extentedReport, screenshot);
      return new CustDashboardPage(driver, extentedReport).get();
    } catch (Exception e) {
      throw new Exception("Unable to select reason for suspend billing : " + e);
    }
  }

  /**
   * To select reason for SuspendBilling
   * 
   * @param reason
   * @param extentedReport
   * @param screenshot
   * @throws Exception
   * 
   */
  public void selectSuspendBillingReason(String reason, ExtentTest extentedReport,
      boolean screenshot) throws Exception {
    try {
      if (WaitUtils.waitForElement(driver, drpoptSuspendBillingReason)) {
        Select drpReason = new Select(drpoptSuspendBillingReason);
        drpReason.selectByVisibleText(reason);
        Log.message("Reason for suspeded billing is selected - " + reason, driver, extentedReport,
            screenshot);
      } else {
        throw new Exception("Drop down to select reason for suspend billing is not found");
      }
    } catch (Exception e) {
      throw new Exception("Unable to select reason for suspend billing : " + e);
    }
  }

  /**
   * To check whether Payment Day is enable
   * 
   * @throws Exception return status
   */
  public boolean isPaymentDayEditable(ExtentTest extentReport) throws Exception {
    boolean status = false;
    try {
      Log.message("Trying to select the preferred Payment day", extentReport);
      drpdwnPreferredPaymentDay.sendKeys("ABC");
      if (drpdwnPreferredPaymentDay.getAttribute("value").equals("ABC")) {
        status = true;
      }
      return status;
    } catch (Exception e) {
      return status;
      // throw new Exception("Error while checking whether PaymentDay
      // dropdown is enable : " + e);
    }

  }

  /**
   * Verify Engagement Center Title
   * 
   * @param titleNameToVerify return boolean
   * @throws Exception
   * 
   */
  public boolean isSuspendBillingSelected() throws Exception {
    try {
      return GenericUtils.verifyWebElementTextContains(panelSuspendBilling, "Suspend Billing");

    } catch (Exception e) {
      throw new Exception(
          "Error while Verifying suspend billing is selected in buyQuote page: " + e);
    }
  }

  /**
   * To verify selected reason for SuspendBilling
   * 
   * @param reason
   * @param extentedReport
   * @param screenshot
   * @throws Exception
   * 
   */
  public boolean verifySuspendBillingReason(String reason) throws Exception {
    try {
      boolean status = false;
      if (WaitUtils.waitForElement(driver, drpoptSuspendBillingReason)) {
        Select drpReason = new Select(drpoptSuspendBillingReason);
        if (drpReason.getFirstSelectedOption().getText().equals(reason)) {
          status = true;
        }
      } else {
        throw new Exception("Drop down to select reason for suspend billing is not found");
      }

      return status;
    } catch (Exception e) {
      throw new Exception("Error while verifying the suspend billing option : " + e);
    }
  }

  /**
   * Click on Cancel button
   * 
   * @param extentReport
   * @throws Exception
   * 
   */
  public void clickCancelButton(ExtentTest extentReport) throws Exception {
    try {
      btnCancel.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Cancel Button in BuyQuote page ", driver, extentReport);
      WaitUtils.waitForElement(driver, mdlReturnToDashboard);
    } catch (Exception e) {
      throw new Exception("Cancel button not clicked in BuyQuote page : " + e);
    }
  }

  /**
   * Click on Yes Button In WarningDialog
   * 
   * @param extentReport
   * @return CustDashboardPage
   * @throws Exception
   * 
   */
  public CustDashboardPage clickYesButtonInWarningDialog(ExtentTest extentReport) throws Exception {
    try {
      WaitUtils.waitForElement(driver, btnYes);
      btnYes.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Yes Button in Warning dialog ", driver, extentReport);
      return new CustDashboardPage(driver, extentReport).get();

    } catch (Exception e) {
      throw new Exception("Yes button not clicked in Warning dialog : " + e);
    }
  }

  /**
   * To click the customer dashboard tab
   * 
   * @param extentedReport
   * @param Screenshot
   * @throws Exception
   * 
   */
  public void clickCustomerDashboardTab(ExtentTest extentedReport, boolean Screenshot)
      throws Exception {
    try {
      WaitUtils.waitForElement(driver, tabCustomerDashboard);
      tabCustomerDashboard.click();
      WaitUtils.waitForElement(driver, mdlReturnToDashboard);
      Log.message("CustomerDashboard tab is clicked to navigate to customer dashboard", driver,
          extentedReport, Screenshot);
    } catch (Exception e) {
      throw new Exception("Error while click CustomerDashboard Tab : " + e);
    }
  }


}
