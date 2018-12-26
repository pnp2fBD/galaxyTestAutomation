package com.ssp.uxp_pages;

import java.util.HashMap;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import com.relevantcodes.extentreports.ExtentTest;
import com.ssp.support.Log;
import com.ssp.utils.WaitUtils;

/**
 * Card Details Page allows user to enter the Card Details for Making Annual Payment
 */
public class CardDetailsPage extends LoadableComponent<CardDetailsPage> {

  private final WebDriver driver;
  private ExtentTest extentedReport;
  private boolean isPageLoaded;

  @FindBy(css = "input#cardNumber")
  WebElement txtCardNumber;

  @FindBy(css = "input#cardholderName")
  WebElement txtCardHolderAnnual;

  @FindBy(css = "select[name='expiryDate.expiryMonth']")
  WebElement selectExpiryMonth;

  @FindBy(css = "select[name='expiryDate.expiryYear']")
  WebElement selectExpiryYear;

  @FindBy(css = "input[name='securityCode']")
  WebElement txtVerification;

  //@FindBy(css = "label[for='Continue'] span+input[alt='Continue']")
  @FindBy(css = "#C2__ID_DIRECT_DEBIT_CONFIRM")

  WebElement btnContinue;

  @FindBy(css = "input#cardholderName")
  WebElement txtCardHoldName;

  @FindBy(css = "input[id*='submitButton']")
  WebElement btnBuy;

  @FindBy(css = "#address1")
  WebElement txtAddress1;

  @FindBy(css = "#address2")
  WebElement txtAddress2;

  @FindBy(css = "#town")
  WebElement txttown;

  @FindBy(css = "#postcode")
  WebElement txtPostCode;

  @FindBy(xpath = "//table//tr[td[span[contains(text(), 'Amount')]]]/td[2]/span")
  WebElement txtAmountToPay;


  /**
   * 
   * Constructor class for Card Details Page Here we initializing the driver for page factory
   * objects. For ajax element waiting time has added while initialization
   * 
   * @param driver : Webdriver
   */
  public CardDetailsPage(WebDriver driver, ExtentTest report) {
    this.driver = driver;
    this.extentedReport = report;
    PageFactory.initElements(driver, this);
  }

  @Override
  protected void isLoaded() {

    if (!isPageLoaded) {
      Assert.fail();
    }

    if (isPageLoaded && !driver.getTitle().contains("Quote")) {
      Log.fail("SSP Card Details page did not open up. Site might be down.", driver,
          extentedReport);
    }
  }

  @Override
  protected void load() {

    isPageLoaded = true;
    WaitUtils.waitForPageLoad(driver);
    WaitUtils.waitForElement(driver, txtCardNumber);
  }

  /**
   * Enter Card number
   * 
   * @param testdata
   * @param extentReport
   * @param Screenshot
   * @throws Exception
   * 
   */
  public void enterCardNumber(HashMap<String, String> testdata, ExtentTest extentReport,
      boolean Screenshot) throws Exception {
    WaitUtils.waitForSpinner(driver);
    WaitUtils.waitForElement(driver, txtCardNumber);
    try {
      txtCardNumber.clear();
      txtCardNumber.sendKeys(testdata.get("Card Number"));
      Log.message("Entered Card Number : " + testdata.get("Card Number"), driver, extentReport,
          Screenshot);
    } catch (Exception e) {
      throw new Exception("Error while entering Card number :" + e);
    }
    WaitUtils.waitForSpinner(driver);
  }

  /**
   * Select Expiry date
   * 
   * @param testdata
   * @param extentReport
   * @param Screenshot
   * @throws Exception
   * 
   */
  public void selectExpiry(HashMap<String, String> testdata, ExtentTest extentReport,
      boolean Screenshot) throws Exception {
    try {
      selectMonth(extentReport);
      selectYear(testdata, extentReport);
    } catch (Exception e) {
      throw new Exception("Error while entering expiry details :" + e);
    }
    WaitUtils.waitForSpinner(driver);
  }

  /**
   * Select Month
   * 
   * @param extentReport
   * @throws Exception
   * 
   */
  public void selectMonth(ExtentTest extentReport) throws Exception {
    try {
      Select selectmonth = new Select(selectExpiryMonth);
      selectmonth.selectByValue("02");
      Log.message("Selected Month : 2", driver, extentReport);
    } catch (Exception e) {
      throw new Exception("Error while entering expiry month :" + e);
    }
  }

  /**
   * Select Year
   * 
   * @param extentReport
   * @param testdata
   * @throws Exception
   * 
   */
  public void selectYear(HashMap<String, String> testdata, ExtentTest extentReport)
      throws Exception {
    try {
      Select selectyear = new Select(selectExpiryYear);
      selectyear.selectByValue(testdata.get("Year"));
      Log.message("Selected Year : " + testdata.get("Year"), driver, extentReport);
    } catch (Exception e) {
      throw new Exception("Error while entering expiry year :" + e);
    }
  }

  /**
   * Enter CVV verification
   * 
   * @param extentReport
   * @param Screenshot
   * @throws Exception
   * 
   */
  public void enterVerification(ExtentTest extentReport, boolean Screenshot) throws Exception {
    try {
      txtVerification.clear();
      String security_code="123";
      txtVerification.sendKeys(security_code);
      Log.message("Entered CVV : 123", driver, extentReport, Screenshot);

    } catch (Exception e) {
      throw new Exception("Error while entering CVV number :" + e);
    }
  }

  /**
   * Enter Card Holder Name
   * 
   * @param extentReport
   * @param Screenshot
   * @throws Exception
   * 
   */
  public void enterName(HashMap<String, String> testdata, ExtentTest extentReport,
      boolean Screenshot) throws Exception {
    try {
      txtCardHoldName.clear();
      txtCardHoldName.sendKeys("First Name");
      Log.message("Entered Card Holder Name : " + testdata.get("First Name"), driver, extentReport,
          Screenshot);
    } catch (Exception e) {
      throw new Exception("Error occured while entering card holders name :" + e);
    }

  }

  /**
   * Click Buy
   * 
   * @param extentReport
   * @param Screenshot
   * @throws Exception
   * 
   */
  public void clickbuy(ExtentTest extentReport, boolean Screenshot) throws Exception {
    try {
      btnBuy.click();
      Log.message("Clicked on Buy button", driver, extentReport, true);
    } catch (Exception e) {
      throw new Exception("Failed to click buy button on card details page :" + e);
    }
  }

  /**
   * Click on Continue button
   * 
   * @param extentReport
   * @return GetQuotePage
   * @throws Exception
   * 
   */
  public GetQuotePage clickContinueButton_getQuote(ExtentTest extentReport) throws Exception {
    try {
      WaitUtils.waitForElement(driver, btnContinue);
      btnContinue.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Continue Button", driver, extentReport);
      driver.switchTo().defaultContent();
      return new GetQuotePage(driver, extentedReport).get();
    } catch (Exception e) {
      throw new Exception("Continue button not clicked :" + e);
    }
  }

  /**
   * Click on Continue button
   * 
   * @param extentReport
   * @return BuyQuotePage
   * @throws Exception
   * 
   */
  public BuyQuotePage clickContinueButton_buyQuote(ExtentTest extentReport) throws Exception {
    try {
      btnContinue.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Continue Button", driver, extentReport);
      driver.switchTo().defaultContent();
      return new BuyQuotePage(driver, extentReport).get();
    } catch (Exception e) {
      throw new Exception("Continue button not clicked :" + e);
    }
  }

  /**
   * Click on Continue button
   * 
   * @param extentReport
   * @return NewQuotePage
   * @throws Exception
   * 
   */
  public NewQuotePage clickContinueButton(ExtentTest extentReport) throws Exception {
    try {

      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForelementToBeClickable(driver, btnContinue, "Unable to claick continue");
      JavascriptExecutor executor = (JavascriptExecutor) driver;
      executor.executeScript("arguments[0].click();", btnContinue);

      // btnContinue.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Continue Button", driver, extentReport);
      driver.switchTo().defaultContent();

      return new NewQuotePage(driver, extentReport).get();
    } catch (Exception e) {
      throw new Exception("Continue button not clicked :" + e);
    }

  }

}
