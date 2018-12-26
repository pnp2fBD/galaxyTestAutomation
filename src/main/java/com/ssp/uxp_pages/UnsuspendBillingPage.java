package com.ssp.uxp_pages;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
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

public class UnsuspendBillingPage extends LoadableComponent<UnsuspendBillingPage> {

  private final WebDriver driver;
  private ExtentTest extentedReport;
  private boolean isPageLoaded;
  public ElementLayer uielement;

  public String cssConfirmbutton = "#C2__C2__BUT_1F419D606979599B1231397";
  public String xpathAccSelectBtnOfAccDetails =
      "//table[@summary='BankDetails']//tr[.//span[text()='${SORT_CODE}']][.//span[text()='${BRANCH}']][.//span[text()='${ACC_NAME}']][.//span[text()='${ACC_NO}']]//button";

  @FindBy(css = "#C2__TXT_644A45EDEAA7BED1147104")
  WebElement titleUnsuspendBilling;

  @FindBy(css = "select[name*='PAYMENTPLAN']")
  WebElement selectPaymentPlan;

  @FindBy(css = "select[name*='PAYMENTMETHOD']")
  WebElement selectPaymentMethod;

  @FindBy(css = "#C2__BUT_EE2C7D9B8CC571FB2770393_R1")
  WebElement btnSelectBankDetailRow1;

  @FindBy(css = "button[title='Check Account']")
  WebElement but_CheckAccount;

  @FindBy(css = "#C2__QUE_E2BF6E7D321C01D51456374_0")
  WebElement checkbox_CustAgree;

  @FindBy(css = "[title='Accept']")
  WebElement btnAccept;

  @FindBy(css = "#C2__QUE_EE2C7D9B8CC571FB700927")
  WebElement drpdwnPreferredPaymentDay;

  @FindBy(css = "#C2__C2__BUT_CE93A4FCF8469E1D5834607[title='Change Payor']")
  WebElement btnChangePayor;

  @FindBy(css = "input[name*='CHANGEPAYOR[1].SEARCH[1].FIRSTNAME']")
  WebElement fldPayorFirstName;

  @FindBy(css = "input[name*='CHANGEPAYOR[1].SEARCH[1].LASTNAME']")
  WebElement fldPayorLastName;

  @FindBy(css = "#date-picker-C2__C2__QUE_CE93A4FCF8469E1D5823076")
  WebElement fldPayorDOB;

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

  @FindBy(css = "#C2__HEAD_24D10CAD2CBD74854948758")
  WebElement amountToPayInPremiumBanner;

  @FindBy(css = "#C2__p4_QUE_24D10CAD2CBD74854948763")
  WebElement totalPaymentInPremiumBanner;

  @FindBy(css = "#C2__p1_QUE_24D10CAD2CBD74854948769")
  WebElement firstPaymentOfInPremiumBanner;

  @FindBy(css = "#C2__p1_QUE_24D10CAD2CBD74854948782")
  WebElement preferredPaymentDayInPremiumBanner;

  @FindBy(css = "#C2__p1_QUE_24D10CAD2CBD74854948775")
  WebElement includesFeesOfInPremiumBanner;

  @FindBy(css = "#C2__p1_QUE_24D10CAD2CBD74854948771")
  WebElement followedByInstallementsInPremiumBanner;

  @FindBy(css = "button[id*='CONFIRM']")
  WebElement btnConfirmPayment;

  @FindBy(css = "button[value='Accept']:not([disabled='disabled'])")
  WebElement btnAcceptPayment;

  @FindBy(css = "button[title='Add Account Details']")
  WebElement btnAddAccDetail;

  @FindBy(css = "input[id='C2__QUE_EE2C7D9B8CC571FB1355397']")
  WebElement txtAccNumber;

  @FindBy(css = "input[name*='ACCOUNTDETAILS[1].SORTCODE']")
  WebElement txtAccSortCode;

  @FindBy(css = "button[title='Check Account']")
  WebElement btnCheckAccount;

  @FindBy(css = "input[name*='ACCOUNTDETAILS[1].ACCOUNTNAME']")
  WebElement txtAccName;

  @FindBy(css = "button[id='C2__ID_DIRECT_DEBIT_SAVE']")
  WebElement btnAddAccSave;

  @FindBy(css = "#C2__HEAD_EE2C7D9B8CC571FB2596088")
  WebElement h1AccDetailsTitle;

  @FindBy(css = "div#C2__p4_QUE_E2BF6E7D321C01D51456374>div>fieldset>div>input[type='checkbox']")
  WebElement chkCustAgree;

  @FindBy(css = "input[name*='ACCOUNTDETAILS[1].BRANCH_READONLY']")
  WebElement txtAccBranch;

  @FindBy(css = "#C2__ID_DIRECT_TAKE_PAYMENT")
  WebElement btnTakePayment;

  @FindBy(css = "input#cardNoInput")
  WebElement txtCardNumber;

  @FindBy(css = "input[name='cardCVV']")
  WebElement txtVerification;

  @FindBy(css = "input[alt='Continue']")
  WebElement btnContinue;

  @FindBy(css = "#name")
  WebElement txtCardHoldName;

  @FindBy(css = "#op-PMMakePayment[name='op-PMMakePayment']")
  WebElement btnBuy;

  @FindBy(css = "select[name='cardExp.month']")
  WebElement selectExpiryMonth;

  @FindBy(css = "select[name='cardExp.year']")
  WebElement selectExpiryYear;

  @FindBy(css = "iframe[name='sagepay']")
  WebElement frame_sagepay;

  @FindBy(css = "input[name='op-DPChoose-VISA^SSL'][alt='Visa']")
  WebElement lnkVisa;

  @FindBy(css = "#C2__BUT_925B6D15BDB617A8717552")
  WebElement btnConfirm;

  @FindBy(css = "input[id*='QUE_D44A4DCA8AF3342E1982539']")
  WebElement chkSpokenDirectlyNewPayor;

  @FindBy(css = "#C2__p1_GRP_9C281AC77BF4F72E3238818")
  WebElement panelDirectDebitGuarantee;

  // @FindBy(css = "#C2__QUE_E2BF6E7D321C01D51456374_0")
  // WebElement checkbox_CustAgree;

  /**
   * 
   * Constructor class for Card Details Page Here we initializing the driver for page factory
   * objects. For ajax element waiting time has added while initialization
   * 
   * @param driver : Webdriver
   * @param extentedReport : ExtentTest
   */
  public UnsuspendBillingPage(WebDriver driver, ExtentTest extentedReport) {
    this.driver = driver;
    this.extentedReport = extentedReport;
    PageFactory.initElements(driver, this);
  }

  @Override
  protected void isLoaded() {

    if (!isPageLoaded) {
    //  Assert.fail();
    }

    if (isPageLoaded && !driver.getTitle().contains("Unsuspend Billing")) {
      Log.fail("User is not navigated to Unsuspend Billing page", driver, extentedReport);
    }
    uielement = new ElementLayer(driver);
  }

  @Override
  protected void load() {

    isPageLoaded = true;
    WaitUtils.waitForPageLoad(driver);

    (new WebDriverWait(driver, 20).pollingEvery(200, TimeUnit.MILLISECONDS)
        .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
        .withMessage("Unsuspend Billing page not loaded properly"))
            .until(ExpectedConditions.visibilityOf(titleUnsuspendBilling));
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
              .until(ExpectedConditions.elementToBeClickable(selectPaymentPlan));

      Select paymentdropdown = new Select(selectPaymentPlan);
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

      WaitUtils.waitForelementToBeClickable(driver, selectPaymentMethod,
          "Unable to find Payment method");
      selectPaymentMethod.sendKeys(paymentMethod);
      WaitUtils.waitForSpinner(driver);
      Log.message("Payment Method Selected: " + paymentMethod, driver, extendReport, Screenshot);
    } catch (Exception e) {
      throw new Exception("Unable to Select Payment Method" + e);
    }
  }

  /**
   * To select First BankDetail
   * 
   * @param extentedReport
   * @param screenshot
   * @throws Exception
   * 
   */
  public void selectFirstBankDetail(ExtentTest extentedReport, boolean screenshot)
      throws Exception {
    try {
      if (WaitUtils.waitForElement(driver, btnSelectBankDetailRow1)) {
        btnSelectBankDetailRow1.click();
        WaitUtils.waitForElement(driver, but_CheckAccount);
        Log.message("Bank detail is selected", driver, extentedReport, screenshot);
      } else {
        throw new Exception("Select button to select bank detail is not found");
      }
    } catch (Exception e) {
      throw new Exception("Unable to select Bank detail : " + e);
    }
  }

  /**
   * To select First BankDetail
   * 
   * @param extentedReport
   * @param screenshot
   * @return CustDashboardPage
   * @throws Exception
   * 
   */
  public CustDashboardPage selectDirectDebitGuarantee(ExtentTest extentedReport, boolean screenshot)
      throws Exception {
    try {

      GenericUtils.scrollIntoView(driver, checkbox_CustAgree);
      checkbox_CustAgree.click();
      WaitUtils.waitForElement(driver, btnAccept);
      Log.message("Direct Debit Guarantee checkbox is checked", driver, extentedReport, screenshot);
      btnAccept.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Accept button is clicked after DriectDebit gaurantee checkbox is selected",
          driver, extentedReport, screenshot);
      return new CustDashboardPage(driver, extentedReport).get();

    } catch (Exception e) {
      throw new Exception("Unable to select Direct Debit Guarantee checkbox : " + e);
    }
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
    Select paymentPlandDropdown = new Select(selectPaymentPlan);
    if (paymentPlandDropdown.getFirstSelectedOption().getText().equals(paymentPlan)) {
      status = true;
    }
    return status;
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
    Select paymentMethodDropdown = new Select(selectPaymentMethod);
    if (paymentMethodDropdown.getFirstSelectedOption().getText().equals(paymentMethod)) {
      status = true;
    }
    return status;
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
   * To verify preferred payment day
   * 
   * @param Day
   * @throws Exception
   * 
   */
  public boolean verifyPreferredPaymentDay(String Day) throws Exception {
    try {
      boolean status = false;
      if (WaitUtils.waitForElement(driver, drpdwnPreferredPaymentDay)) {
        Select drpReason = new Select(drpdwnPreferredPaymentDay);
        if (drpReason.getFirstSelectedOption().getText().equals(Day)) {
          status = true;
        }
      } else {
        throw new Exception("Drop down to select reason for suspend billing is not found");
      }

      return status;

    } catch (Exception e) {
      throw new Exception("Error while selecting the preferred payment day : " + e);
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
      Thread.sleep(2000);
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
      WaitUtils.waitForElement(driver, btnPayorConfirm);
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
          clickAcceptbtn(extentedReport);
          break;
      }
      objCustDashboardPage = new CustDashboardPage(driver, extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to Confirm Payment. " + e);
    }
    return objCustDashboardPage;
  }

  public void clickAcceptbtn(ExtentTest extentedReport) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Edit Details Link"))
              .until(ExpectedConditions.elementToBeClickable(btnAcceptPayment));
      btnAcceptPayment.click();
      Thread.sleep(2000);
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on 'Accept' payment button", extentedReport);

    } catch (Exception e) {
      throw new Exception("Exception in clicking accept button during monthly payment :" + e);
    }
  }

  /**
   * Fills up Payment section input fields with the given test data and checks the 'Customer agrees
   * ...' checkbox.
   * 
   * @param testData
   * @param Screenshot
   * @param extentedReport
   * @throws Exception
   */
  public void selectPayment(HashMap<String, String> testData, boolean Screenshot,
      ExtentTest extentedReport) throws Exception {
    try {
      String paymentPlan = testData.get("Payment Plan");
      String paymentMethod = testData.get("Payment Method");

      selectPaymentPlan(paymentPlan, extentedReport);
      selectPaymentMethod(paymentMethod.toString(), extentedReport, false);

      if (paymentPlan.equals("Monthly")) {

        String accNo = testData.get("Account Number");
        String sortCode = testData.get("Sort Code");
        String accName = testData.containsKey("Account Name") ? testData.get("Account Name")
            : GenericUtils.getRandomCharacters("alpha", 5);
        String branch =
            (testData.containsKey("Bank Branch") && !testData.get("Bank Branch").equals(""))
                ? testData.get("Bank Branch") : "Bootle Centre, Santander, Liverpool";

        //clickAddAccountDetails(extentedReport);
      //  checkBankAccount(accNo, sortCode, extentedReport);
     //   enterAccountName(accName, extentedReport);
      //  saveAccountDetails(extentedReport);
       // selectAccount(sortCode, branch, accName, accNo, extentedReport, true);
      //  verifyAccountSelection(sortCode, branch, accName, accNo, extentedReport);
        tickCustomerAgreesCheckbox(extentedReport);
      }
    } catch (Exception e) {
      throw new Exception("Exception occured,Unable to Select Payment" + e);
    }
  }

  /**
   * Clicks on 'Add Account Details' button.
   * 
   * @param extentedReport
   */
  public void clickAddAccountDetails(ExtentTest extentedReport) {
    WaitUtils.waitForElementPresent(driver, btnAddAccDetail,
        "Add Account detail button is not found");
    btnAddAccDetail.click();
    Log.message("Clicked 'Add Account Details' buttion", extentedReport);
  }

  /**
   * Clicks on 'Check Account' button after writing the given account number and sort code in the
   * 'Account Number' and 'Sort Code' input fields respectively.
   * 
   * @param accountNumber
   * @param sortCode
   * @param extentedReport
   */
  public void checkBankAccount(String accountNumber, String sortCode, ExtentTest extentedReport) {
    WaitUtils.waitForElementPresent(driver, txtAccNumber, " Account number textbox is not found");
    txtAccNumber.clear();
    txtAccNumber.sendKeys(accountNumber);
    Log.message("Account number entered :" + accountNumber, extentedReport);

    txtAccSortCode.clear();
    txtAccSortCode.sendKeys(sortCode);
    Log.message("Sort code entered :" + sortCode, extentedReport);

    WaitUtils.waitForElementPresent(driver, btnCheckAccount, "Check account button is not found");
    btnCheckAccount.click();
    WaitUtils.waitForSpinner(driver);
  }

  /**
   * Writes the given account name in the 'Account Name' input field.
   * 
   * @param accountName
   * @param extentedReport
   */
  public void enterAccountName(String accountName, ExtentTest extentedReport) {
    txtAccName.clear();
    txtAccName.sendKeys(accountName);
    Log.message("Account name entered :" + accountName, extentedReport);
  }

  /**
   * Clicks on 'Save' button in 'Add Account Details' section.
   * 
   * @param extentedReport
   */
  public void saveAccountDetails(ExtentTest extentedReport) {
    WaitUtils.waitForElementPresent(driver, btnAddAccSave,
        "Save button to add account is not found");
    btnAddAccSave.click();
    WaitUtils.waitForSpinner(driver);
    Log.message("Clicked on account save button", extentedReport);
  }

  /**
   * Selects the expected account which is already added.
   * 
   * @param sortCode
   * @param branch
   * @param accName
   * @param accNo
   * @param extentedReport
   * @param screenshot
   */
  public void selectAccount(String sortCode, String branch, String accName, String accNo,
      ExtentTest extentedReport, boolean screenshot) throws Exception {
    sortCode = sortCode.matches("\\d{2}-\\d{2}-\\d{2}") ? sortCode
        : sortCode.substring(0, 2) + "-" + sortCode.substring(2, 4) + "-" + sortCode.substring(4);
    String xpathAccSelect = xpathAccSelectBtnOfAccDetails.replaceAll("\\$\\{SORT_CODE\\}", sortCode)
        .replaceAll("\\$\\{BRANCH\\}", branch).replaceAll("\\$\\{ACC_NAME\\}", accName)
        .replaceAll("\\$\\{ACC_NO\\}", accNo);
    GenericUtils.scrollIntoView(driver, h1AccDetailsTitle);
    WaitUtils.waitForElementPresent(driver, "xpath", xpathAccSelect, 30,
        "Timed out to find the 'select' button of account with following details {sort code: '"
            + sortCode + "', branch: '" + branch + "', account name: '" + accName
            + "', account number: '" + accNo + "'}");
    driver.findElement(By.xpath(xpathAccSelect)).click();
    WaitUtils.waitForSpinner(driver);
    WaitUtils.waitForElementAbsent(driver, "xpath", xpathAccSelect, 30,
        "Timed out waiting for the absence of the 'select' button of account with following details {sort code: '"
            + sortCode + "', branch: '" + branch + "', account name: '" + accName
            + "', account number: '" + accNo + "'}");
    Log.message("Clicked on the select button of account with account name '" + accName + "'",
        driver, extentedReport, true);
  }

  /**
   * Verifies that the 'Select' button of the selected account is hidden and the 'Account Details'
   * fields are populated with the selected account details.
   * 
   * @param sortCode
   * @param branch
   * @param accName
   * @param accNo
   * @param extentedReport
   * @return true if the 'Select' button of the selected account is hidden and the selected account
   *         details are successfully verified; false otherwise.
   */
  public boolean verifyAccountSelection(String sortCode, String branch, String accName,
      String accNo, ExtentTest extentedReport) {
    boolean isOk = false;

    sortCode = sortCode.matches("\\d{2}-\\d{2}-\\d{2}") ? sortCode
        : sortCode.substring(0, 2) + "-" + sortCode.substring(2, 4) + "-" + sortCode.substring(4);
    String xpathAccSelect = xpathAccSelectBtnOfAccDetails.replaceAll("\\$\\{SORT_CODE\\}", sortCode)
        .replaceAll("\\$\\{BRANCH\\}", branch).replaceAll("\\$\\{ACC_NAME\\}", accName)
        .replaceAll("\\$\\{ACC_NO\\}", accNo);
    WebElement btnAccSelect = driver.findElement(By.xpath(xpathAccSelect));

    Log.message("Verifying the account selection", driver, extentedReport, true);
    if (btnAccSelect.isDisplayed()) {
      Log.message("Account selection failed. 'Select' button of the selected account is visible.",
          extentedReport);
    } else if (!verifyAccountDetails(sortCode, branch, accName, accNo, extentedReport)) {
      Log.message(
          "Account selection failed. Selected account details are not populated in 'Account Details' section.",
          extentedReport);
    } else {
      isOk = true;
    }

    return isOk;
  }

  /**
   * Selects the 'Customer agrees...Direct Debit Guarantee' checkbox if it is not selected already.
   * 
   * @param extentedReport
   * @throws Exception
   */
  public void tickCustomerAgreesCheckbox(ExtentTest extentedReport) throws Exception {
    try {
      GenericUtils.scrollIntoView(driver, chkCustAgree);
      if (!chkCustAgree.isSelected()) {
        chkCustAgree.click();
        WaitUtils.waitForSpinner(driver);
        Log.message("Checked the 'Customer agrees...Direct Debit Guarantee' checkbox",
            extentedReport);
      } else {
        Log.message("The 'Customer agrees...Direct Debit Guarantee' checkbox is already checked",
            extentedReport);
      }
    } catch (Exception e) {
      throw new Exception(
          "Exception in checking the 'Customer agrees...Direct Debit Guarantee' checkbox." + e);
    }
  }

  /**
   * Verifies all the fields in the 'Account Details' section and returns the verification status.
   * 
   * @param sortCode
   * @param branch
   * @param accName
   * @param accNo
   * @param extentedReport
   * @return true if all the fields in the 'Account Details' section are successfully verified;
   *         false otherwise.
   */
  public boolean verifyAccountDetails(String sortCode, String branch, String accName, String accNo,
      ExtentTest extentedReport) {
    boolean isOk = false;

    if (verifySortCode(sortCode, extentedReport) && verifyBranchName(branch, extentedReport)
        && verifyAccountName(accName, extentedReport)
        && verifyAccountNumber(accNo, extentedReport)) {
      isOk = true;
    }

    return isOk;
  }

  /**
   * Verifies that the actual sort code matches the expected sort code given.
   * 
   * @param expectedSortCode
   * @param extentedReport
   * @return true if the actual sort code matches the expected sort code given; false otherwise.
   */
  public boolean verifySortCode(String expectedSortCode, ExtentTest extentedReport) {
    boolean isOk = false;
    String actualSortCode = txtAccSortCode.getAttribute("value").trim().replaceAll("-", "");
    Log.message("Actual Sort code: '" + actualSortCode + "'", extentedReport);
    Log.message("Expected Sort code: '" + expectedSortCode + "'", extentedReport);

    if (actualSortCode.equals(expectedSortCode)) {
      isOk = true;
    } else {
      Log.message("The actual sort code '" + actualSortCode + "' does not match the expected '"
          + expectedSortCode + "'", extentedReport);
    }

    return isOk;
  }

  /**
   * Verifies that the actual branch name matches the expected branch name given.<br/>
   * <br/>
   * Also verifies that the 'Branch Name' input field is locked to edit.
   * 
   * @param expectedBranchName
   * @param extentedReport
   * @return true if the actual branch name matches the expected branch name given and the field is
   *         not editable; false otherwise.
   */
  public boolean verifyBranchName(String expectedBranchName, ExtentTest extentedReport) {
    boolean isOk = false;
    String actualBranchName = txtAccBranch.getAttribute("value").trim();
    Log.message("Actual Branch: '" + actualBranchName + "'", extentedReport);
    Log.message("Expected Branch: '" + expectedBranchName + "'", extentedReport);

    if (actualBranchName.equals(expectedBranchName)) {
      if (!txtAccBranch.isEnabled()) {
        isOk = true;
        Log.message("'Branch Name' input field is not editable", extentedReport);
      } else {
        Log.message("'Branch Name' input field is editable", extentedReport);
      }
    } else {
      Log.message("The actual branch name '" + actualBranchName + "' does not match the expected '"
          + expectedBranchName + "'", extentedReport);
    }

    return isOk;
  }

  /**
   * Verifies that the actual account number matches the expected account number given.
   * 
   * @param expectedAccNo
   * @param extentedReport
   * @return true if the actual account number matches the expected account number given; false
   *         otherwise.
   */
  public boolean verifyAccountNumber(String expectedAccNo, ExtentTest extentedReport) {
    boolean isOk = false;
    String actualAccNo = txtAccNumber.getAttribute("value").trim();
    Log.message("Actual Acc No: '" + actualAccNo + "'", extentedReport);
    Log.message("Expected Acc No: '" + expectedAccNo + "'", extentedReport);

    if (actualAccNo.equals(expectedAccNo)) {
      isOk = true;
    } else {
      Log.message("The actual account number '" + actualAccNo + "' does not match the expected '"
          + expectedAccNo + "'", extentedReport);
    }

    return isOk;
  }

  /**
   * Verifies that the actual account name matches the expected account number given.
   * 
   * @param expectedAccName
   * @param extentedReport
   * @return true if the actual account name matches the expected account name given; false
   *         otherwise.
   */
  public boolean verifyAccountName(String expectedAccName, ExtentTest extentedReport) {
    boolean isOk = false;
    String actualAccName = txtAccName.getAttribute("value").trim();
    Log.message("Actual Acc Name: '" + actualAccName + "'", extentedReport);
    Log.message("Expected Acc Name: '" + expectedAccName + "'", extentedReport);

    if (actualAccName.equals(expectedAccName)) {
      isOk = true;
    } else {
      Log.message("The actual account name '" + actualAccName + "' does not match the expected '"
          + expectedAccName + "'", extentedReport);
    }

    return isOk;
  }

  /**
   * Click on 'Take Payment' button.
   * 
   * @param extentedReport
   */
  public void clickTakePaymentBtn(ExtentTest extentedReport) {
    WaitUtils.waitForElement(driver, btnTakePayment);
    WaitUtils.waitForElementPresent(driver, btnTakePayment,
        "Take Payment button is not found in Unsuspend billing page");
    btnTakePayment.click();
    WaitUtils.waitForSpinner(driver);
    Log.message("Clicked 'Take Payment' button in Unsuspend billing page", extentedReport);

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
  public void clickContinueButton(ExtentTest extentReport) throws Exception {
    try {
      btnContinue.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Continue Button", driver, extentReport);
      driver.switchTo().defaultContent();
    } catch (Exception e) {
      throw new Exception("Continue button not clicked :" + e);
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
      selectmonth.selectByValue("2");
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

  public void selectVisacard(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForPageLoad(driver);

      driver.switchTo().frame(frame_sagepay);
      WaitUtils.waitForElement(driver, lnkVisa);
      lnkVisa.click();
      Log.message("Selected the Visa card", driver, extentedReport, true);
    } catch (Exception e) {
      throw new Exception("Error while selecting VISA Card" + e);
    }
    (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
        .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
        .withMessage("Spinner is active")).until(
            ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.loading-image")));
  }

  /**
   * Click on 'Confirm' button.
   * 
   * @param extentedReport
   */
  public CustDashboardPage clickConfirmBtn(ExtentTest extentedReport) {
    WaitUtils.waitForElementPresent(driver, btnConfirm,
        "Confirm button is not found in Unsuspend billing page");
    btnConfirm.click();
    Log.message("Clicked 'Confirm' button in Unsuspend billing page", extentedReport);
    return new CustDashboardPage(driver, extentedReport).get();
  }

  /**
   * Selects the 'I have spoken to New Payor...' checkbox.
   * 
   * @param extentedReport
   * @throws Exception
   */
  public void tickSpokenDirectlyNewPayorCheckbox(ExtentTest extentedReport) throws Exception {
    try {
      GenericUtils.scrollIntoView(driver, chkSpokenDirectlyNewPayor);
      chkSpokenDirectlyNewPayor.click();
      Log.message("Checked the 'I have spoken to New Payor...' checkbox", extentedReport);
    } catch (Exception e) {
      throw new Exception(
          "Exception in checking the 'I have spoken to New Payor...' checkbox." + e);
    }
  }


}
