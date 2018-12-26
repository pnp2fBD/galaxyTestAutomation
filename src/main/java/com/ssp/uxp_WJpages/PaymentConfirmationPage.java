package com.ssp.uxp_WJpages;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.relevantcodes.extentreports.ExtentTest;
import com.ssp.support.Log;
import com.ssp.utils.ElementLayer;
import com.ssp.utils.WaitUtils;

public class PaymentConfirmationPage {
  private final WebDriver driver;
  private ExtentTest extentedReport;
  public ElementLayer uielement;
  public String emailAddress;

  public PaymentConfirmationPage(WebDriver driver, ExtentTest report) {
    this.driver = driver;
    this.extentedReport = report;
    PageFactory.initElements(driver, this);
    WaitUtils.waitForSpinner(driver);
  }

  @FindBy(css = "button.payment-method[value='VISA']>span[class='payment-method__name']")
  WebElement btnPaymentMethodVisaLocator;

  @FindBy(css = "input#form-card_details\\2e field-pan")
  WebElement cardNumberLocator;

  @FindBy(css = "input#form-card_details\\2e field-expiry_mm")
  WebElement expiryMonthLocator;

  @FindBy(css = "input#form-card_details\\2e field-expiry_yy")
  WebElement expiryYearLocator;

  @FindBy(css = "input#form-card_details\\2e field-cvc")
  WebElement cvcLocator;

  @FindBy(css = "iframe.embed-responsive-item")
  WebElement frameOfSagePay;

  @FindBy(css = "button[type=submit][value='proceed']")
  WebElement btnConfirmCardDetails;

  @FindBy(css = "label[for=QUE_BAE601C919E3AD945563_0]>span")
  WebElement checkBoxIconfirm;

  @FindBy(css = "a#BUT_72C3D2CAFB89E17B3478")
  WebElement btnGetCovered;

  public void click_PaymentMethodsBtn() throws Exception {
    try {
      driver.switchTo().frame(frameOfSagePay);
      WaitUtils.waitForElementPresent(driver, btnPaymentMethodVisaLocator,
          "VISA Payment method is not present on the page");
      btnPaymentMethodVisaLocator.click();
      Log.message("Click on the VISA button", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the VISA button " + e);
    }
  }

  public void click_CheckBoxIConfirm() throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, checkBoxIconfirm,
          "Issue with the check box of I confirm have read and understood the above documents");
      checkBoxIconfirm.click();
      Log.message("Click on the checkbox  button", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the checkbox button " + e);
    }
  }

  public void enter_CardNumber(String cardNumber) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, cardNumberLocator,
          "Card number element is not present on the page");
      cardNumberLocator.clear();
      cardNumberLocator.sendKeys(cardNumber);
      Log.message("Enter the card number " + cardNumber, extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the VISA card number " + e);
    }
  }

  public void enter_ExpiryMonth() throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, expiryMonthLocator,
          "Expiry month element is not present on the page");
      expiryMonthLocator.clear();
      expiryMonthLocator.sendKeys("04");
      Log.message("Enter the card expiry month 04", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the VISA expiry month " + e);
    }
  }

  public void enter_ExpiryYear() throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, expiryYearLocator,
          "Expiry year element is not present on the page");
      expiryYearLocator.clear();
      Calendar cal = new GregorianCalendar();
      int year = cal.get(Calendar.YEAR); // 2013
      year = year + 1;
      String expiryYear = Integer.toString(year).substring(2);
      expiryYearLocator.sendKeys(expiryYear);
      Log.message("Enter the card expiry year " + expiryYear, extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the VISA expiry year " + e);
    }
  }

  public void enter_CardCVC(String cvc) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, cvcLocator,
          "Card CVC element is not present on the page");
      cvcLocator.clear();
      cvcLocator.sendKeys(cvc);
      Log.message("Enter the Card CVC " + cvc, extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the VISA Card CVC " + e);
    }
  }

  public void click_ConfirmCardDetails() throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, btnConfirmCardDetails,
          "Confirm card details is not present on the page");
      btnConfirmCardDetails.click();
      Log.message("Click on the confirm card details button", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the confirm card details button " + e);
    }
  }

  public void click_OnPay() throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, btnConfirmCardDetails,
          "Pay button is not present on the page");
      btnConfirmCardDetails.click();
      Log.message("Click on the pay button", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the pay button" + e);
    }
  }

  public void click_onGetCovererd() throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, btnGetCovered,
          "Get covered button is not present on the page");
      btnGetCovered.click();
      Log.message("Click on Get covered button", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the get covered button" + e);
    }
  }

  public PaymentCompletePage runFlowOfMonthlyPaymentConfirmationPage() throws Exception {
    this.click_CheckBoxIConfirm();
    this.click_onGetCovererd();
    return new PaymentCompletePage(driver, extentedReport);
  }

  public PaymentCompletePage runFlowOfAnnualPaymentConfirmationPage(
      HashMap<String, String> testData) throws Exception {
    this.click_PaymentMethodsBtn();
    String cardNumber = testData.get("Card Number");
    this.enter_CardNumber(cardNumber);
    this.enter_ExpiryMonth();
    this.enter_ExpiryYear();
    String cvc = testData.get("Security Code");
    this.enter_CardCVC(cvc);
    this.click_ConfirmCardDetails();
    WaitUtils.waitForSpinner(driver);
    this.click_OnPay();
    return new PaymentCompletePage(driver, extentedReport);
  }

}
