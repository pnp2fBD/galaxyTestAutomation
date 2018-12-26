package com.ssp.uxp_pages;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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



public class BillingAdjustmentPage extends LoadableComponent<BillingAdjustmentPage> {

  private final WebDriver driver;
  private ExtentTest extentedReport;
  private boolean isPageLoaded;
  public ElementLayer uielement;

  public String cssConfirmbutton = "#C2__C2__BUT_1F419D606979599B1231397";

  @FindBy(css = "[title='Acceptance']")
  WebElement titleAcceptance;

  @FindBy(css = "#C2__UNSEL_BillingAdjustmentReasons")
  WebElement selectAdjustmentReason;

  @FindBy(css = "#C2__B1_BillingAdjustmentReasons")
  WebElement BtnAddAdjReason;

  @FindBy(css = "#C2__BUT_FD051A198CB3861E1713417")
  WebElement btnNext;

  @FindBy(css = "select[name*='PAYMENTPLAN']")
  WebElement selectpaymentplan;

  @FindBy(css = "select[name*='PAYMENTMETHOD']")
  WebElement selectpaymentmethod;

  @FindBy(css = "#C2__C2__BUT_CE93A4FCF8469E1D5834607[title='Change Payor']")
  WebElement btnChangePayor;

  @FindBy(css = "input[name*='CHANGEPAYOR[1].SEARCH[1].FIRSTNAME']")
  WebElement fldPayorFirstName;

  @FindBy(css = "input[name*='CHANGEPAYOR[1].SEARCH[1].LASTNAME']")
  WebElement fldPayorLastName;

  @FindBy(css = "input[name*='CHANGEPAYOR[1].SEARCH[1].POSTCODE']")
  WebElement fldPayorPostCode;

  @FindBy(css = "button[id*='Payor_Search']")
  WebElement btnPayorSearch;

  @FindBy(css = "#C2__C2__p4_BUT_CE93A4FCF8469E1D5823094_R1")
  WebElement btnPayorSearchSelectRow1;

  @FindBy(css = "#C2__C2__p1_QUE_E0D434CA692481AD1202231>div>label>p")
  WebElement btnPayorGetName;

  @FindBy(css = "button[class*='payor-confirm']")
  WebElement btnPayorConfirm;

  @FindBy(css = "#C2__C2__QUE_CE93A4FCF8469E1D5834594[name*='PAYOR']")
  WebElement fldPayor;

  @FindBy(css = "#C2__C2__QUE_D44A4DCA8AF3342E1982539_0")
  WebElement chkPayorconfirmation;

  @FindBy(css = "[name*='PREFERREDPAYMENTDAY']")
  WebElement drpdwnPreferredPaymentDay;

  @FindBy(css = "button[id='C2__ID_DIRECT_DEBIT_SAVE']")
  WebElement but_AddAccSave;

  @FindBy(css = "#C2__BUT_EE2C7D9B8CC571FB2770393_R1")
  WebElement btn_acctSelect;

  @FindBy(css = "#C2__QUE_E2BF6E7D321C01D51456374_0")
  WebElement checkbox_CustAgree;

  @FindBy(css = "button[title='Add Account Details']")
  WebElement but_AddAccDetail;

  @FindBy(css = "input[id='C2__QUE_EE2C7D9B8CC571FB1355397']")
  WebElement txt_AccNumber;

  @FindBy(
      css = "input[name='C2__CONTACTCENTRE[1].BUYQUOTE[1].DIRECTDEBITS[1].ACCOUNTDETAILS[1].SORTCODE']")
  WebElement txt_SortCode;

  @FindBy(css = "button[title='Check Account']")
  WebElement but_CheckAccount;

  @FindBy(
      css = "input[name='C2__CONTACTCENTRE[1].BUYQUOTE[1].DIRECTDEBITS[1].ACCOUNTDETAILS[1].ACCOUNTNAME']")
  WebElement txt_AccName;

  @FindBy(css = "button[value='Accept']:not([disabled='disabled'])")
  WebElement btnAcceptPayment;

  @FindBy(css = "button[id*='DIRECT_TAKE_PAYMENT']")
  WebElement TakePayment;

  @FindBy(css = "iframe[id='wp-cl-custom-html-iframe']")
  WebElement frame_sagepay;

  @FindBy(css = "input[name='op-DPChoose-VISA^SSL'][alt='Visa']")
  WebElement lnkVisa;

  @FindBy(css = "input[alt='Continue']")
  WebElement btnContinue;

  @FindBy(css = "button[id*='CONFIRM']")
  WebElement btnConfirmPayment;

  @FindBy(css = "#C2__HEAD_9C281AC77BF4F72E2206644")
  WebElement titlePaymentSchedule;

  @FindBy(css = "#C2__HEAD_EE2C7D9B8CC571FB2596088")
  WebElement titleAccountDetail;

  @FindBy(css = "#C2__HEAD_9C281AC77BF4F72E3238826")
  WebElement titleDebitDirectGuarantee;

  @FindBy(css = "#C2__QUE_24D10CAD2CBD74854948763")
  WebElement txtAmountToPay;

  @FindBy(css = "#C2__FMT_2411741C919DF0935526399")
  WebElement panelCardDetailsection;

  @FindBy(css = ".main-brand")
  WebElement txtEngCenter;

  @FindBy(css = "#C2__TXT_2411741C919DF0934326893")
  WebElement msgCardDetail;

  @FindBy(css = "#C2__QUE_EE2C7D9B8CC571FB1612284")
  WebElement txtBranch;

  @FindBy(css = "#C2__BUT_7499E8C9DD09DCA41348358")
  WebElement btnCancel;

  @FindBy(css = "#C2__FMT_FD051A198CB3861E3809021")
  WebElement mdlReturnToDashboard;

  @FindBy(css = "#C2__BUT_FD051A198CB3861E3821012")
  WebElement btnNo;

  @FindBy(css = "#C2__HEAD_76604C1E84202971711304")
  WebElement titleCardDetail;

  @FindBy(css = "#C2__BUT_FD051A198CB3861E3809030")
  WebElement btnYes;

  @FindBy(css = "input[id*='submitButton']")
  WebElement btnBuy;

  @FindBy(css = "input#cardholderName")
  WebElement txtCardHoldName;

  @FindBy(css = "input[name='securityCode']")
  WebElement txtVerification;

  @FindBy(css = "input#cardNumber")
  WebElement txtCardNumber;
  
  
  @FindBy(css = "select[name='expiryDate.expiryYear']")
  WebElement selectExpiryYear;

  @FindBy(css = "select[name='expiryDate.expiryMonth']")
  WebElement selectExpiryMonth;

  @FindBy(css = "#address1")
  WebElement txtAddress1;

  @FindBy(css = "#address2")
  WebElement txtAddress2;

  @FindBy(css = "#town")
  WebElement txttown;

  @FindBy(css = "#postcode")
  WebElement txtPostCode;

  @FindBy(css = "#C2__BUT_925B6D15BDB617A8717552")
  WebElement btnConfirm;

  @FindBy(css = "#C2__FMT_F26218AB42DF738A818625")
  WebElement panelPremiumSummary;


  /**
   * 
   * Constructor class for Card Details Page Here we initializing the driver for page factory
   * objects. For ajax element waiting time has added while initialization
   * 
   * @param driver : Webdriver
   */
  public BillingAdjustmentPage(WebDriver driver, ExtentTest report) {
    this.driver = driver;
    this.extentedReport = report;
    PageFactory.initElements(driver, this);
  }

  @Override
  protected void isLoaded() {

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
  protected void load() {

    isPageLoaded = true;
    WaitUtils.waitForPageLoad(driver);

    (new WebDriverWait(driver, 20).pollingEvery(200, TimeUnit.MILLISECONDS)
        .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
        .withMessage("Buyquote page not loaded properly"))
            .until(ExpectedConditions.visibilityOf(titleAcceptance));
  }

  /**
   * To select option for adjustment reason
   * 
   * @param optionToClick - Reason name
   * @param extendReport
   * @param Screenshot - Capturing screens
   * @throws Exception
   * 
   */
  public void selectAdjustmentReason(String optionToClick, ExtentTest extendReport,
      boolean Screenshot) throws Exception {

    try {

      Select adjReasonBox = new Select(selectAdjustmentReason);
      adjReasonBox.selectByVisibleText(optionToClick);

      if (!WaitUtils.waitForElement(driver, BtnAddAdjReason)) {
        throw new Exception("Add button is not found to add adjustment reason");
      }

      BtnAddAdjReason.click();

      Log.message(optionToClick + " - Option is selected for adjustment reasoning ", driver,
          extendReport, Screenshot);

    } catch (Exception e) {
      throw new Exception("Error while selecting the adjustment option" + e);
    }

  }

  /**
   * To verify whether the option is selected
   * 
   * @param - extendReport
   * @throws Exception
   * 
   */
  public void clickNext(ExtentTest extendReport) throws Exception {

    try {
      if (WaitUtils.waitForElement(driver, btnNext)) {
        btnNext.click();
        Log.message("Next button is clicked after selecting adjustment reason", driver,
            extendReport);
      } else {
        throw new Exception("Next button is not found");
      }

    } catch (Exception e) {
      throw new Exception("Error while clicking next button" + e);
    }

  }

  /**
   * select payment plan
   *
   * @param paymentplan : String
   * @param extentedReport
   * @throws Exception
   * 
   */
  public void selectPaymentPlan(String paymentPlan, ExtentTest extentedReport) throws Exception {
    try {
      (new WebDriverWait(driver, 300).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Timed out to find 'Payment Plan' dropdown"))
              .until(ExpectedConditions.elementToBeClickable(selectpaymentplan));

      Select paymentdropdown = new Select(selectpaymentplan);
      paymentdropdown.selectByVisibleText(paymentPlan);
      Log.message("Payment Plan Selected : " + paymentPlan, extentedReport);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Select Payment Plan" + e);
    }
  }

  /**
   * select payment method
   *
   * @param paymentmethod : String extendReport Screenshot
   * @throws Exception
   */
  public void selectPaymentMethod(String paymentMethod, ExtentTest extendReport, boolean Screenshot)
      throws Exception {
    try {

      WaitUtils.waitForelementToBeClickable(driver, selectpaymentmethod,
          "Unable to find Payment method");
      selectpaymentmethod.sendKeys(paymentMethod);
      WaitUtils.waitForSpinner(driver);
      Log.message("Payment Method Selected: " + paymentMethod, driver, extendReport, Screenshot);
    } catch (Exception e) {
      throw new Exception("Unable to Select Payment Method" + e);
    }
  }

  /**
   * Click Change Payor button
   * 
   * @param extentedReport
   * @param Screenshot
   * @throws Exception : Custom Exception Message
   * 
   */
  public void clickChangePayorButton(ExtentTest extentedReport, boolean Screenshot)
      throws Exception {
    try {
      WaitUtils.waitForelementToBeClickable(driver, btnChangePayor,
          "Change Payor button not enabled");
      btnChangePayor.click();
      WaitUtils.waitForElement(driver, fldPayorFirstName);
      Log.message("Clicked on Change Payor button", driver, extentedReport, Screenshot);
    } catch (Exception e) {
      throw new Exception("Unable to click Change Payor button : " + e);
    }

  }

  /**
   * enter the change payor details
   * 
   * @param extentedReport
   * @param Screenshot
   * @param lastName
   * @param firstName
   * @param postcode
   * @return String
   * @throws Exception : Exception Message
   * 
   */
  public String enterChangePayorDetails(String lastName, String firstName, String postcode,
      ExtentTest extentedReport, boolean Screenshot) throws Exception {
    String[] name = null;
    String payorName = null;

    try {
      WaitUtils.waitForelementToBeClickable(driver, fldPayorLastName,
          "Last name field is not found");
      fldPayorLastName.clear();
      fldPayorLastName.sendKeys(lastName);
      Log.message("Entered Payor Last Name : " + lastName, driver, extentedReport);
      Thread.sleep(2000);
      fldPayorFirstName.clear();
      fldPayorFirstName.sendKeys(firstName);
      Log.message("Entered Payor First Name : " + firstName, driver, extentedReport);
      Thread.sleep(2000);
      fldPayorPostCode.clear();
      fldPayorPostCode.sendKeys(postcode);
      Log.message("Entered Payor Postcode : " + postcode, driver, extentedReport, Screenshot);
      btnPayorSearch.click();
      Log.message("Clicked on Payor Search button", driver, extentedReport);
      WaitUtils.waitForElement(driver, btnPayorSearchSelectRow1);
      btnPayorSearchSelectRow1.click();
      name = btnPayorGetName.getText().split("\\|");
      payorName = name[0].trim();
      Log.message("Selected the Payor : " + payorName, driver, extentedReport, Screenshot);

      btnPayorConfirm.click();
      Log.message("Clicked on Confirm Payor", driver, extentedReport);
      WaitUtils.waitForinvisiblityofElement(driver, 180, cssConfirmbutton,
          "Change payor search window did not close after 3min");
    } catch (Exception e) {
      throw new Exception("Unable to click Change Payor button : " + e);
    }
    return payorName;
  }

  /**
   * verify the payor name
   * 
   * @param payorName return status
   */
  public boolean verifyPayorName(String payorName) {
    boolean status = false;
    if (fldPayor.getAttribute("value").equals(payorName)) {
      status = true;
    }
    return status;
  }

  /**
   * click Payor ConfirmationCheckbox checkbox after changing payor
   * 
   * @param extentReport
   * @param screenshot
   * @throws Exception
   * 
   */
  public void clickPayorConfirmationCheckbox(ExtentTest extentReport, boolean screenshot)
      throws Exception {
    try {
      chkPayorconfirmation.click();
      Log.message("Checkbox to confirm the change payor is clicked", driver, extentReport,
          screenshot);

    } catch (Exception e) {
      throw new Exception("Error while selecting the preferred payment day : " + e);
    }
  }

  /**
   * To select preferred payment day
   * 
   * @param Day
   * @param extentReport
   * @param screenshot
   * @throws Exception : Exception Message
   * 
   */
  public void selectPreferredPaymentDay(String Day, ExtentTest extentReport, boolean screenshot)
      throws Exception {
    try {
      WaitUtils.waitForElement(driver, drpdwnPreferredPaymentDay);
      Select preferredPayementDay = new Select(drpdwnPreferredPaymentDay);
      preferredPayementDay.selectByVisibleText(Day);
      WaitUtils.waitForSpinner(driver);
      Log.message("Perferred Payment day is selected as - " + Day, driver, extentReport,
          screenshot);

    } catch (Exception e) {
      throw new Exception("Error while selecting the preferred payment day : " + e);
    }

  }

  /**
   * Add Account Detail
   * 
   * @param testdata
   * @param extentReport
   * @param screenshot
   * @throws Exception
   * 
   */
  public void addAccountDetail(HashMap<String, String> testdata, ExtentTest extentReport,
      boolean screenshot) throws Exception {
    try {
      clickAddAccountDetailButton(extentReport);
      enterAccountNoAndSortCode(testdata.get("Account Number").toString(),
          testdata.get("Sort Code").toString(), extentReport, false);

      String strAcctName = GenericUtils.getRandomCharacters("ALPHA", 5);
      enterAccountName(strAcctName, extentReport, screenshot);

      WaitUtils.waitForElementPresent(driver, but_AddAccSave,
          "Save button to add account is not found");
      but_AddAccSave.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on account save button", extentReport);

      WaitUtils.waitForElementPresent(driver, btn_acctSelect, "Select button not found");
      btn_acctSelect.click();
      Log.message("Clicked on account select button", extentReport);
      WaitUtils.waitForSpinner(driver);

      Actions actions = new Actions(driver);
      actions.moveToElement(checkbox_CustAgree).click().build().perform();
      Log.message("Clicked on customer agree checkbox in payment page", extentReport);

    } catch (Exception e) {
      throw new Exception("Error while Adding Account Details : " + e);
    }

  }

  /**
   * click add accountDeatil Button
   * 
   * @param extentReport
   * @throws Exception
   * 
   */
  public void clickAddAccountDetailButton(ExtentTest extentReport) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, but_AddAccDetail,
          "Add Account detail button is not found");
      but_AddAccDetail.click();
      Log.message("Add Account Detail button is clicked", driver, extentReport);
    } catch (Exception e) {
      throw new Exception("Error while clicking addaccountDeatil Button : " + e);
    }
  }

  /**
   * enter AccountNo And SortCode
   * 
   * @param AccountNumber
   * @param SortCode
   * @param extentReport
   * @param screenshot
   * @throws Exception
   * 
   */
  public void enterAccountNoAndSortCode(String AccountNumber, String SortCode,
      ExtentTest extentReport, boolean screenshot) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, txt_AccNumber,
          " Account number textbox is not found");

      txt_AccNumber.clear();
      txt_AccNumber.sendKeys(AccountNumber);
      Log.message("Account Number entered :" + AccountNumber, extentReport);

      txt_SortCode.clear();
      txt_SortCode.sendKeys(SortCode);
      Log.message("Sort code entered :" + SortCode, extentReport);

      WaitUtils.waitForElementPresent(driver, but_CheckAccount,
          "Check account button is not found");
      but_CheckAccount.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("CheckAccount button is clicked", driver, extentReport, screenshot);

    } catch (Exception e) {
      throw new Exception("Error while Entering Account Details : " + e);
    }
  }

  /**
   * enter AccountName
   * 
   * @param AccountName
   * @param extentReport
   * @param screenshot
   * @throws Exception
   * 
   */
  public void enterAccountName(String accountName, ExtentTest extentReport, boolean screenshot)
      throws Exception {
    try {

      WaitUtils.waitForElementPresent(driver, txt_AccName, "Account name textbox is not found");
      txt_AccName.clear();
      txt_AccName.sendKeys(accountName);
      Log.message("Account name entered :" + accountName, driver, extentReport, screenshot);

    } catch (Exception e) {
      throw new Exception("Error while Entering Account Name : " + e);
    }
  }

  /**
   * click accept button
   *
   * @param extentedReport
   * @throws Exception
   * 
   */
  public void clickAcceptBtn(ExtentTest extentedReport) throws Exception {
    try {

      WaitUtils.waitForelementToBeClickable(driver, btnAcceptPayment,
          "Unable to find Accept Button");
      btnAcceptPayment.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Accept button in page", extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to click Accpet button in page : " + e);
    }

  }

  /**
   * click Takepayment button
   *
   * @param extentedReport
   * @throws Exception
   * 
   */
  public void clickTakePaymentBtn(ExtentTest extentedReport) throws Exception {
    try {

      WaitUtils.waitForelementToBeClickable(driver, TakePayment,
          "Unable to find Takepayment Button");
      TakePayment.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Take payment button in BuyQuote page", extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to click Take Payment button in BuyQuote page : " + e);
    }

  }

  /**
   * To Click TakePayment button
   * 
   * @param extentedReport
   * @return CardDetailsPage
   * @throws Exception
   * 
   */
  public void selectVisacard(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForPageLoad(driver);
      Thread.sleep(3000);
      driver.switchTo().frame(frame_sagepay);
      /*
       * WaitUtils.waitForElement(driver, lnkVisa); lnkVisa.click();
       */
      Log.message("Selected the Visa card", driver, extentedReport, true);
    } catch (Exception e) {
      throw new Exception("Error while selecting VISA Card" + e);
    }
    (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
        .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
        .withMessage("Spinner is active")).until(
            ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.loading-image")));
  //  return new CardDetailsPage(driver, extentedReport).get();

  }

  /**
   * Click on Continue button
   * 
   * @param extentReport
   * @throws Exception
   * 
   */
  public void clickContinueButton(ExtentTest extentReport) throws Exception {
    try {
      btnContinue.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Continue Button in BuyQuote page", driver, extentReport);
    } catch (Exception e) {
      throw new Exception("Continue button not clicked :" + e);
    }
  }

  /**
   * Confirms the payment by clicking either 'Confirm' or 'Accept' button.
   * 
   * @param testdata
   * @param extentedReport
   * @return the <code>CustDashboardPage</code> object, or null if no <code>CustDashboardPage</code>
   *         representation is applicable
   * @throws Exception
   */
  public CustDashboardPage confirmPayment(HashMap<String, String> testdata,
      ExtentTest extentedReport) throws Exception {
    CustDashboardPage objCustDashboardPage = null;
    try {
      String paymentplan = testdata.get("Payment Plan");
      switch (paymentplan) {
        case "Annual":
          (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
              .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
              .withMessage("Unable to find Edit Details Link"))
                  .until(ExpectedConditions.elementToBeClickable(btnConfirmPayment));
          btnConfirmPayment.click();
          WaitUtils.waitForSpinner(driver);
          Log.message("Clicked on 'Confirm' payment button", extentedReport);
          break;
        case "Monthly":
          clickAcceptBtn(extentedReport);
          break;
      }
      objCustDashboardPage = new CustDashboardPage(driver, extentedReport).get();
    } catch (Exception e) {
      throw new Exception("Unable to Confirm Payment. " + e);
    }
    return objCustDashboardPage;
  }

  /**
   * To click the customer dashboard tab
   * 
   * @param extentedReport
   * @param Screenshot
   * @throws Exception
   * 
   */
  public String getAmountToPay(ExtentTest extentedReport, boolean Screenshot) throws Exception {
    try {
      String value = null;
      WaitUtils.waitForElement(driver, txtAmountToPay);
      value = txtAmountToPay.getText();
      Log.message(value + " - The Amount to pay is displayed", driver, extentedReport, Screenshot);
      return value;
    } catch (Exception e) {
      throw new Exception("Error while click CustomerDashboard Tab : " + e);
    }

  }

  /**
   * Verify Engagement Center Title
   * 
   * @param titleNameToVerify return boolean
   * @throws Exception
   * 
   */
  public boolean VerifyEngCenterTitle(String titleNameToVerify) throws Exception {
    try {
      WaitUtils.waitForPageLoad(driver);
      driver.switchTo().frame(frame_sagepay);

      return GenericUtils.verifyWebElementTextContains(txtEngCenter, titleNameToVerify);

    } catch (Exception e) {
      throw new Exception("Error while Verifying Engagement Center Title in buyQuote page: " + e);
    }
  }

  /**
   * Verify CardDetail Message
   * 
   * @param msgToVerfiy
   * @throws Exception return boolean
   * 
   */
  public boolean VerifyCardDetailMsg(String msgToVerfiy) throws Exception {
    try {
      GenericUtils.scrollIntoView(driver, msgCardDetail);
      return GenericUtils.verifyWebElementTextContains(msgCardDetail, msgToVerfiy);

    } catch (Exception e) {
      throw new Exception("Error while Verifying CardDetail Message : " + e);
    }
  }

  /**
   * Verify Branch Name
   * 
   * @param branchName
   * @throws Exception return boolean
   * 
   */
  public boolean VerifyBranchName(String branchName) throws Exception {
    try {
      boolean status = false;
      if (txtBranch.getAttribute("value").contains(branchName)) {
        status = true;
      }
      return status;

    } catch (Exception e) {
      throw new Exception("Error while Verifying Branch Name : " + e);
    }
  }

  /**
   * To verify Account name
   * 
   * @param AccountName
   * @throws Exception return boolean
   * 
   */
  public boolean VerifyAccountName(String AccountName, ExtentTest extentReport) throws Exception {
    try {
      boolean nameMatch = false;
      Log.message("Account name is displayed with the name - " + txt_AccName.getAttribute("value"),
          extentReport);

      if (txt_AccName.getAttribute("value").equals(AccountName)) {
        nameMatch = true;
      }

      return nameMatch;

    } catch (Exception e) {
      throw new Exception("Error while Verifying AccountNo And SortCode : " + e);
    }
  }

  /**
   * enter AccountNo And SortCode
   * 
   * @param AccountNumber
   * @param SortCode
   * @throws Exception return boolean
   * 
   */
  public boolean VerifyAccountNoAndSortCode(String AccountNumber, String SortCode)
      throws Exception {
    try {
      boolean status = false;
      String newSortCode = txt_SortCode.getAttribute("value").replaceAll("-", "");

      if (txt_AccNumber.getAttribute("value").equals(AccountNumber)
          && newSortCode.equals(SortCode)) {
        status = true;
      }

      return status;

    } catch (Exception e) {
      throw new Exception("Error while Verifying AccountNo And SortCode : " + e);
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
   * Click on No Button In WarningDialog
   * 
   * @param extentReport
   * @throws Exception
   * 
   */
  public void clickNoButtonInWarningDialog(ExtentTest extentReport) throws Exception {
    try {
      WaitUtils.waitForElement(driver, btnNo);
      btnNo.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on NO Button in Warning dialog ", driver, extentReport);
    } catch (Exception e) {
      throw new Exception("No button not clicked in Warning dialog : " + e);
    }
  }

  /**
   * verify the payment method
   * 
   * @param paymentMethod
   * 
   *        return boolean
   * 
   */
  public boolean verifyPaymentMethod(String paymentMethod) {
    boolean status = false;
    Select paymentMethodDropdown = new Select(selectpaymentmethod);
    if (paymentMethodDropdown.getFirstSelectedOption().getText().equals(paymentMethod)) {
      status = true;
    }
    return status;
  }

  /**
   * verify the payment plan
   * 
   * @param paymentPlan
   * 
   *        return boolean
   * 
   */
  public boolean verifyPaymentPlan(String paymentPlan) {
    boolean status = false;
    Select paymentPlandDropdown = new Select(selectpaymentplan);
    if (paymentPlandDropdown.getFirstSelectedOption().getText().equals(paymentPlan)) {
      status = true;
    }
    return status;
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
      return new CustDashboardPage(driver, extentedReport).get();

    } catch (Exception e) {
      throw new Exception("Yes button not clicked in Warning dialog : " + e);
    }
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
      txtVerification.sendKeys("123");
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
      txtCardHoldName.sendKeys(testdata.get("First Name"));
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
   * Change Address1
   * 
   * @param extentReport
   * @param Screenshot
   * @throws Exception
   * 
   */
  public void changeAddressOne(String Address1, ExtentTest extentReport, boolean Screenshot)
      throws Exception {
    try {
      GenericUtils.scrollIntoView(driver, txtAddress1);
      txtAddress1.clear();
      txtAddress1.sendKeys(Address1);
      Log.message("Address1 detail is changed in payment panel as - " + Address1, driver,
          extentReport, Screenshot);
    } catch (Exception e) {
      throw new Exception("Error while changing the Address1 : " + e);
    }

  }

  /**
   * verify customer address
   * 
   * @param address
   * @param extentReport
   * @param Screenshot
   * @throws Exception
   * 
   */
  public boolean verifyCustomerAddress(String[] address, ExtentTest extentReport,
      boolean Screenshot) throws Exception {
    try {

      boolean isfilled = false;
      Integer totalElement = address.length;
      Integer elementMatched = 0;

      String addressOne = txtAddress1.getAttribute("value");
      String address2 = txtAddress2.getAttribute("value");
      String town = txttown.getAttribute("value");
      String postCode = txtPostCode.getAttribute("value").replaceAll("\\s+", "");

      if (GenericUtils.verifyStringPresentInArray(address, addressOne)) {
        elementMatched++;
        Log.message(addressOne + " - is present in the textfield of Address1", extentReport);
      }

      if (GenericUtils.verifyStringPresentInArray(address, address2)) {
        elementMatched++;
        Log.message(address2 + " - is present in the textfield of Address2", extentReport);
      }

      if (GenericUtils.verifyStringPresentInArray(address, town)) {
        elementMatched++;
        Log.message(town + " - is present in the textfield of town", extentReport);
      }

      if (GenericUtils.verifyStringPresentInArray(address, postCode)) {
        elementMatched++;
        Log.message(town + " - is present in the textfield of postCode", extentReport);
      }

      if (elementMatched == totalElement) {
        isfilled = true;
      }

      return isfilled;

    } catch (Exception e) {
      throw new Exception("Error while verifying address detail in card detail page : " + e);
    }

  }

  /**
   * verify customer address
   * 
   * @param amount
   * @param extentReport
   * @param Screenshot
   * @throws Exception
   * 
   */
  public boolean verifyAmountToPay(String amount) throws Exception {
    try {
      return GenericUtils.verifyWebElementTextContains(txtAmountToPay, amount);

    } catch (Exception e) {
      throw new Exception("Error while verifying amount to pay in card detail page : " + e);
    }

  }

  /**
   * verify customer address
   * 
   * @param amount
   * @param extentReport
   * @param Screenshot
   * @throws Exception
   * 
   */
  public boolean verifyCardHolderName(String cardholderName) throws Exception {
    try {

      return GenericUtils.verifyWebElementTextContains(txtCardHoldName, cardholderName);

    } catch (Exception e) {
      throw new Exception("Error while verifying CardHolde rName in card detail page : " + e);
    }

  }

  /**
   * click accept button
   *
   * @param extentedReport
   * @throws Exception
   * 
   */
  public CustDashboardPage clickConfirmBtn(ExtentTest extentedReport) throws Exception {
    try {//#C2__BUT_925B6D15BDB617A8717552
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForelementToBeClickable(driver, btnConfirm, "Unable to find confirm Button");
      btnConfirm.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on confirm button in billing adjustment page", extentedReport);
      return new CustDashboardPage(driver, extentedReport).get();
    } catch (Exception e) {
      throw new Exception("Unable to click confirm button in page : " + e);
    }

  }


}
