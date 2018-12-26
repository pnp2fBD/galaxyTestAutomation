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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.relevantcodes.extentreports.ExtentTest;
import com.ssp.support.Log;
import com.ssp.utils.GenericUtils;
import com.ssp.utils.WaitUtils;

/**
 * Card Details Page allows user to enter the Card Details for Making Annual Payment *
 */
public class ViewTransactionPage extends LoadableComponent<ViewTransactionPage> {

  private final WebDriver driver;
  private ExtentTest extentedReport;
  private boolean isPageLoaded;

  /**********************************************************************************************
   ********************************* WebElements of ViewTransaction Page ***************************
   **********************************************************************************************/

  @FindBy(css = "[title='Back to Dashboard']")
  WebElement lnkBacktoDashboard;

  @FindBy(css = "#C2__TAB_A08DB4BEE3F2E475481704")
  WebElement tabPricePresentation;

  @FindBy(css = "#C2__TAB_F45AF3D482E592C71339718")
  WebElement tabDeclaration;

  @FindBy(css = "C2__TAB_A08DB4BEE3F2E475481595")
  WebElement tabDataCapture;

  @FindBy(css = "[class*='ViewPolicyHolder']")
  WebElement btnView;


  /**********************************************************************************************
   ********************************* WebElements of ViewTransaction Page- Ends *************************
   **********************************************************************************************/

  /**
   * 
   * Constructor class for Card Details Page Here we initializing the driver for page factory
   * objects. For ajax element waiting time has added while initialization
   * 
   * @param driver : Webdriver
   * @param extentedReport : ExtentTest
   */
  public ViewTransactionPage(WebDriver driver, ExtentTest extentedReport) {
    this.driver = driver;
    this.extentedReport = extentedReport;
    PageFactory.initElements(driver, this);
  }

  @Override
  protected void isLoaded() {


    if (!isPageLoaded) {
      Assert.fail();
    }

    if (isPageLoaded && !driver.getTitle().contains("ViewTransaction")) {
      Log.fail("User is not navigated to ", driver, extentedReport);
    }
  }

  @Override
  protected void load() {

    isPageLoaded = true;
    WaitUtils.waitForPageLoad(driver);

    (new WebDriverWait(driver, 20).pollingEvery(200, TimeUnit.MILLISECONDS)
        .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
        .withMessage("View Transaction page not loaded properly"))
            .until(ExpectedConditions.visibilityOf(lnkBacktoDashboard));
  }

  /**
   * To select Tab in view transaction page
   * 
   * @param tabToClick- Data Caputure/Price Presentation/Declaration
   * @param extendReport
   * @param Screenshot
   * @throws Exception
   * 
   */
  public void selectTab(String tabToClick, ExtentTest extendReport, boolean Screenshot)
      throws Exception {
    try {
      switch (tabToClick) {

        case "Data Caputure":
          WaitUtils.waitForElement(driver, tabPricePresentation);
          tabDataCapture.click();
          Log.message("Data Caputure tab is clicked in view transaction page", driver, extendReport,
              Screenshot);
          break;

        case "Price Presentation":
          WaitUtils.waitForElement(driver, tabPricePresentation);
          tabPricePresentation.click();
          Log.message("Price Presentation tab is clicked in view transaction page", driver,
              extendReport, Screenshot);
          break;

        case "Declaration":
          WaitUtils.waitForElement(driver, tabPricePresentation);
          tabDeclaration.click();
          Log.message("Declaration tab is clicked in view transaction page", driver, extendReport,
              Screenshot);
          break;
      }

    } catch (Exception e) {
      throw new Exception("Error while selecting tab in view transaction page : " + e);
    }
  }

  /**
   * To check whether view button is enabled
   * 
   * @return boolean
   * @throws Exception
   * 
   */
  public boolean isViewBtnEnabled() throws Exception {
    try {
      boolean isEnabled = false;
      GenericUtils.scrollIntoView(driver, btnView);
      if (btnView.isEnabled()) {
        isEnabled = true;
      }
      return isEnabled;

    } catch (Exception e) {
      throw new Exception(
          "Error while checking whether view button is enabled in view transaction page : " + e);
    }

  }

}
