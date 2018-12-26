package com.ssp.uxp_WJpages;

import java.util.HashMap;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.relevantcodes.extentreports.ExtentTest;
import com.ssp.support.Log;
import com.ssp.utils.ElementLayer;
import com.ssp.utils.GenericUtils;
import com.ssp.utils.WaitUtils;

public class PaymentOptionsPage {
  private final WebDriver driver;
  private ExtentTest extentedReport;
  public ElementLayer uielement;
  public String emailAddress;

  public PaymentOptionsPage(WebDriver driver, ExtentTest report) {
    this.driver = driver;
    this.extentedReport = report;
    PageFactory.initElements(driver, this);
    WaitUtils.waitForSpinner(driver);
  }

  @FindBy(css = "input#QUE_4513E281EC0720232245")
  WebElement txtEmailAddress;

  @FindBy(css = "input#QUE_4513E281EC0720232247")
  WebElement txtConfirmEmailAddress;

  @FindBy(css = "input#QUE_4513E281EC0720232249")
  WebElement txtTelephoneNumber;

  @FindBy(css = "a#BUT_72C3D2CAFB89E17B3478")
  WebElement btnConfirmation;

  @FindBy(css = "input#QUE_93F4D8F2D63B4990121421")
  WebElement txtBankAccountHolderName;

  @FindBy(css = "input#QUE_93F4D8F2D63B4990121422")
  WebElement txtBankSortCode;

  @FindBy(css = "input#QUE_93F4D8F2D63B4990121423")
  WebElement txtBankAccount;

  @FindBy(css = "label[for*='QUE_7B73D9DC642171EC40046']>span")
  WebElement chkBoxIagree;



  By radioBtnHowWouldYouLikeToPay =
      By.cssSelector("label[for*=QUE_FE03B8B4E69C4F4615536]>span:nth-child(2)");

  By radioBtnDifferentBillingAddress =
      By.cssSelector("label[for*='QUE_16066C729BDC294C8881']>span:nth-child(2)");

  By radioBtnBankDetails =
      By.cssSelector("label[for*='QUE_FE03B8B4E69C4F4616230']>span:nth-child(2)");

  By radioBtnAgeOver18Years =
      By.cssSelector("label[for*='QUE_7B73D9DC642171EC40339']>span:nth-child(2)");

  By radioBtnStateOfBankruptcy =
      By.cssSelector("label[for*='QUE_7B73D9DC642171EC40258']>span:nth-child(2)");


  public void enterEmailAddress(String emailAddress) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, txtEmailAddress,
          "E-mail address textbox is not present on the page");
      if (emailAddress.trim().equals(""))
        emailAddress = GenericUtils.getEmail();
      this.emailAddress = emailAddress;
      txtEmailAddress.sendKeys(emailAddress);
      Log.message("Enter the email address on the payment page " + emailAddress, extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the text box of email address " + e);
    }
  }

  public void enterCnfrmEmailAddress() throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, txtConfirmEmailAddress,
          "Confirm E-mail address textbox is not present on the page");
      txtConfirmEmailAddress.clear();
      txtConfirmEmailAddress.sendKeys(emailAddress);
      Log.message("Enter the confirm email address on the payment page " + emailAddress,
          extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the text box of confrim email address " + e);
    }
  }

  public void enterTelephoneNumber(String telephoneNumber) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, txtTelephoneNumber,
          "Telephone number textbox is not present on the page");
      txtTelephoneNumber.clear();
      txtTelephoneNumber.sendKeys(telephoneNumber);
      Log.message("Enter the telephone number on the payment page " + telephoneNumber,
          extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the text box of Telephone number " + e);
    }
  }

  public void click_RadioHowWouldYouLikeToPay(String valueToClick) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, radioBtnHowWouldYouLikeToPay,
          "How would you like to pay? radio buttons are not present on the page");
      GenericUtils.selectFromRadioButtons(driver, radioBtnHowWouldYouLikeToPay, valueToClick);
      Log.message("Select the radio button " + valueToClick + " in type of cover", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the radio button of How would you like to pay? " + e);
    }
  }

  public void click_RadioBillingAddress(String valueToClick) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, radioBtnDifferentBillingAddress,
          "Would you like to add a different billing address? radio buttons are not present on the page");
      GenericUtils.selectFromRadioButtons(driver, radioBtnDifferentBillingAddress, valueToClick);
      Log.message("Select the radio button " + valueToClick + " in type of cover", extentedReport);
    } catch (Exception e) {
      throw new Exception(
          "Issue with the radio button of Would you like to add a different billing address? " + e);
    }
  }

  public void click_ConfirmationBtn() throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, btnConfirmation,
          "Confirmation button is not present on the page");
      btnConfirmation.click();
      Log.message("Click on confirmation button", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the button of confirmation " + e);
    }
  }

  public void click_RadioBankDetails(String valueToClick) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, radioBtnBankDetails,
          "Is the bank account from which the Direct Debit will be collected a personal (not business) account in your name and one which only requires you to authorise transactions? radio buttons are not present on the page");
      GenericUtils.selectFromRadioButtons(driver, radioBtnBankDetails, valueToClick);
      Log.message("Select the radio button " + valueToClick + " in Bank Details", extentedReport);
    } catch (Exception e) {
      throw new Exception(
          "Issue with the radio button of Is the bank account from which the Direct Debit will be collected a personal (not business) account in your name and one which only requires you to authorise transactions? "
              + e);
    }
  }

  public void enterBankAccountHolderName(String bankAccountHolderName) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, txtBankAccountHolderName,
          "Bank Account holder name textbox is not present on the page");
      txtBankAccountHolderName.clear();
      txtBankAccountHolderName.sendKeys(bankAccountHolderName);
      Log.message("Enter the bank account holder name on the payment page " + bankAccountHolderName,
          extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the text box of Bank account holder name " + e);
    }
  }

  public void enterBankAccountSortCode(String bankSortCode) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, txtBankSortCode,
          "Sort code textbox is not present on the page");
      txtBankSortCode.clear();
      txtBankSortCode.sendKeys(bankSortCode);
      Log.message("Enter the Sort code on the payment page " + bankSortCode, extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the text box of Sort code " + e);
    }
  }

  public void enterBankAccount(String bankAccount) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, txtBankAccount,
          "Bank account textbox is not present on the page");
      txtBankAccount.clear();
      txtBankAccount.sendKeys(bankAccount);
      Log.message("Enter the Bank account on the payment page " + bankAccount, extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the text box of Bank account " + e);
    }
  }

  public void click_RadioOver18Years(String valueToClick) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, radioBtnAgeOver18Years,
          "Are you over 18 years old and a UK resident? radio buttons are not present on the page");
      GenericUtils.selectFromRadioButtons(driver, radioBtnAgeOver18Years, valueToClick);
      Log.message("Select the radio button " + valueToClick
          + " in Are you over 18 years old and a UK resident?", extentedReport);
    } catch (Exception e) {
      throw new Exception(
          "Issue with the radio button of Are you over 18 years old and a UK resident? " + e);
    }
  }

  public void click_RadioStateOfBankruptcy(String valueToClick) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, radioBtnStateOfBankruptcy,
          "Are you in a state of bankruptcy? radio buttons are not present on the page");
      GenericUtils.selectFromRadioButtons(driver, radioBtnStateOfBankruptcy, valueToClick);
      Log.message(
          "Select the radio button " + valueToClick + " in Are you in a state of bankruptcy?",
          extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the radio button of Are you in a state of bankruptcy? " + e);
    }
  }

  public void clickCheckBoxIagree() throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, chkBoxIagree,
          "I agree with the statement above and would like to proceed with my payment checkbox is not present on the page");
      chkBoxIagree.click();
      Log.message(
          "Select the check box of I agree with the statement above and would like to proceed with my payment",
          extentedReport);
    } catch (Exception e) {
      throw new Exception(
          "Issue with the check box of I agree with the statement above and would like to proceed with my payment"
              + e);
    }
  }

  public PaymentConfirmationPage runFlowOfPaymentOptionsPage(HashMap<String, String> testData)
      throws Exception {
    String emailAddress = testData.get("Email");
    String telephoneNumber = testData.get("Phone Number");
    String howWouldYouLikeToPayRadio = testData.get("Payment Method");
    String billingAddressRadio = testData.get("Billing Address");
    String bankSortCode = testData.get("Sort Code");
    String bankAccount = testData.get("Bank Account");
    String over18Years = testData.get("UK Resident");
    String bankruptcy = testData.get("Bankruptcy");
    this.enterEmailAddress(emailAddress);
    this.enterCnfrmEmailAddress();
    this.enterTelephoneNumber(telephoneNumber);
    this.click_RadioHowWouldYouLikeToPay(howWouldYouLikeToPayRadio);
    WaitUtils.waitForSpinner(driver);
    if (howWouldYouLikeToPayRadio.equalsIgnoreCase("Monthly")) {
      this.click_RadioBankDetails("Yes");
      WaitUtils.waitForSpinner(driver);
      this.enterBankAccountHolderName("Test");
      this.enterBankAccountSortCode(bankSortCode);
      this.enterBankAccount(bankAccount);
      this.click_RadioOver18Years(over18Years);
      this.click_RadioStateOfBankruptcy(bankruptcy);
      this.clickCheckBoxIagree();
      this.click_ConfirmationBtn();
    } else {
      this.click_RadioBillingAddress(billingAddressRadio);
      this.click_ConfirmationBtn();
    }
    return new PaymentConfirmationPage(driver, extentedReport);
  }

}
