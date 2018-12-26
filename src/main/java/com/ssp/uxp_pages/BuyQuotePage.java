package com.ssp.uxp_pages;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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

public class BuyQuotePage extends LoadableComponent<BuyQuotePage> {
  private final WebDriver driver;
  private ExtentTest extentedReport;
  private boolean isPageLoaded;
  public ElementLayer uielement;
  private String radio_button_value = null;

  public String cssConfirmbutton = "#C2__C2__BUT_1F419D606979599B1231397";
  public String lstQuotevariations = ".clearfix.renewalQuoteVariations";
  public String cssComponentDropDown = "select[name*='COMPONENT']";
  public String radAgree =
      "#C2__p4_QUE_921BCE84FE9FC3B2469614 div:nth-child(1) > label[for='C2__QUE_921BCE84FE9FC3B2469614_0']";
  public String xpathAccSelectBtnOfAccDetails =
      "//table[@summary='BankDetails']//tr[.//span[text()='${SORT_CODE}']][.//span[text()='${BRANCH}']][.//span[text()='${ACC_NAME}']][.//span[text()='${ACC_NO}']]//button";

  /**********************************************************************************************
   ********************************* WebElements of BuyQuote Page ***************************
   **********************************************************************************************/

  @FindBy(css = "[title='Acceptance']")
  WebElement titleAcceptance;

  @FindBy(css = "#C2__SEL_BillingAdjustmentReasons")
  WebElement selectedAdjustmentReason;

  @FindBy(css = "select[name*='PAYMENTPLAN']")
  WebElement selectpaymentplan;

  @FindBy(css = "select[name*='PAYMENTMETHOD']")
  WebElement selectpaymentmethod;

  @FindBy(css = "#C2__BUT_EE2C7D9B8CC571FB2770393_R1")
  WebElement btnAcctSelect;

  @FindBy(css = "button[title='Add Account Details']")
  WebElement btnAddAccDetail;

  @FindBy(css = "input[id='C2__QUE_EE2C7D9B8CC571FB1355397']")
  WebElement txtAccNumber;

  @FindBy(css = "input[name*='ACCOUNTDETAILS[1].SORTCODE']")
  WebElement txtAccSortCode;

  @FindBy(css = "button[title='Check Account']")
  WebElement btnCheckAccount;

  @FindBy(css = "input[name*='ACCOUNTDETAILS[1].BRANCH_READONLY']")
  WebElement txtAccBranch;

  @FindBy(css = "input[name*='ACCOUNTDETAILS[1].ACCOUNTNAME']")
  WebElement txtAccName;

  @FindBy(css = "button[id='C2__ID_DIRECT_DEBIT_SAVE']")
  WebElement btnAddAccSave;

  @FindBy(css = "div#C2__p4_QUE_E2BF6E7D321C01D51456374>div>fieldset>div>input[type='checkbox']")
  WebElement chkCustAgree;

  @FindBy(css = "#C2__C2__BUT_CE93A4FCF8469E1D5834607[title='Change Payor']")
  WebElement btnChangePayor;

  @FindBy(css = "#C2__C2__QUE_CE93A4FCF8469E1D5834594[name*='PAYOR']")
  WebElement fldPayor;

  @FindBy(css = "input[name*='CHANGEPAYOR[1].SEARCH[1].LASTNAME']")
  WebElement fldPayorLastName;

  @FindBy(css = "input[name*='CHANGEPAYOR[1].SEARCH[1].FIRSTNAME']")
  WebElement fldPayorFirstName;

  @FindBy(css = "#C2__C2__QUE_CE93A4FCF8469E1D5823076")
  WebElement fldPayorDOB;

  @FindBy(css = "input[name*='CHANGEPAYOR[1].SEARCH[1].POSTCODE']")
  WebElement fldPayorPostCode;

  @FindBy(css = "button[id*='Payor_Search']")
  WebElement btnPayorSearch;

  @FindBy(css = "#C2__C2__p1_QUE_E0D434CA692481AD1202231>div>label>p")
  WebElement btnPayorGetName;

  @FindBy(css = "#C2__C2__p4_BUT_CE93A4FCF8469E1D5823094_R1")
  WebElement btnPayorSearchSelectRow1;

  @FindBy(css = "button[class*='payor-confirm']")
  WebElement btnPayorConfirm;

  @FindBy(
      css = "input[name='C2__CONTACTCENTRE[1].BUYQUOTE[1].DIRECTDEBITS[1].ACCOUNTDETAILS[1].SORTCODE']")
  WebElement txt_SortCode;

  @FindBy(css = "button[title='Check Account']")
  WebElement but_CheckAccount;

  @FindBy(
      css = "input[name='C2__CONTACTCENTRE[1].BUYQUOTE[1].DIRECTDEBITS[1].ACCOUNTDETAILS[1].ACCOUNTNAME']")
  WebElement txt_AccName;

  @FindBy(css = "button[id='C2__ID_DIRECT_DEBIT_SAVE']")
  WebElement but_AddAccSave;

  @FindBy(css = "#C2__QUE_E2BF6E7D321C01D51456374_0")
  WebElement checkbox_CustAgree;

  @FindBy(css = "#C2__BUT_EE2C7D9B8CC571FB2770393_R1")
  WebElement btn_acctSelect;

  @FindBy(css = "input[id='C2__QUE_EE2C7D9B8CC571FB1355397']")
  WebElement txt_AccNumber;

  @FindBy(css = "button[title='Add Account Details']")
  WebElement but_AddAccDetail;

  @FindBy(css = "#C2__QUE_A32E2E21DB4003071328047")
  WebElement drpoptSuspendBillingReason;

  @FindBy(css = "[title='Accept']")
  WebElement btnAccept;

  @FindBy(css = "#C2__BUT_EE2C7D9B8CC571FB2770393_R1")
  WebElement btnSelectBankDetailRow1;

  @FindBy(css = "button[id*='DIRECT_TAKE_PAYMENT']")
  WebElement TakePayment;

  @FindBy(css = "input[name*='PAYMENTREF']")
  WebElement paymentRef;

  @FindBy(css = "iframe[name='sagepay']")
  WebElement frame_sagepay;

  @FindBy(css = "input[name='op-DPChoose-VISA^SSL'][alt='Visa']")
  WebElement lnkVisa;

  @FindBy(css = "#C2__BUT_925B6D15BDB617A8717552")
  WebElement btnConfirm;

  // @FindBy(css = "button[title='Accept']")
  @FindBy(css = "button[value='Accept']:not([disabled='disabled'])")
  WebElement btnAcceptPayment;

  @FindBy(css = "input[alt='Continue']")
  WebElement btnContinue;

  @FindBy(css = "#C2__BUT_7499E8C9DD09DCA41348358")
  WebElement btnCancel;
  @FindBy(css = "#C2__BUT_FD051A198CB3861E3809030")
  WebElement btnYes;

  @FindBy(css = "#C2__C2__QUE_D44A4DCA8AF3342E1982539_0")
  WebElement chkPayorconfirmation;

  @FindBy(css = "button[title='Acceptance']")
  WebElement tab_acceptance;

  @FindBy(css = "button[title='Cover/Price Presentation']")
  WebElement tab_pricePresentation;

  @FindBy(css = "button[title='Data Capture']")
  WebElement tab_dataCapture;

  @FindBy(css = "button.btn.btn-default.pull-right.btn_home_emergency.page-spinner")
  WebElement btnAddHomeEmergency;

  @FindBy(css = "input[name*='YEAROFBUILD']")
  WebElement txtYearofBuild;

  @FindBy(css = "div[id*='_BUT_B13A0396B2DE40F42605055'")
  WebElement tab_SelectedTab;

  @FindBy(css = "button[id*=BUT_8A17B7AD077319CD4115330]")
  WebElement btnAttachTermAndCondition;

  @FindBy(css = "input[type='checkbox'][name*='TERMSANDCONDITIONS']")
  WebElement termsconditions;// C1__CONTACTCENTRE[1].BUYQUOTE[1].PAYMENTPLAN

  @FindBy(css = "a[id='termsandconditions']")
  WebElement termsAndConditionsSection;

  @FindBy(css = "div[id*='FMT_3A056143913CF4954126757_R2']")
  WebElement firstTermAndConditionInList;

  @FindBy(css = "div[id*='FMT_3A056143913CF4954126757_R3']")
  WebElement secondTermAndConditionInList;

  @FindBy(css = "button[title='Edit']")
  WebElement btnEditTermAndCondition;

  @FindBy(css = "span[id*='QUE_3A056143913CF4954126787_R2'][class='editTAC']")
  WebElement selectTAC;

  @FindBy(css = "span[id*='QUE_3A056143913CF4954126787_R3'][class='editTAC']")
  WebElement selectSecondTAC;

  @FindBy(css = "select[name*='COMPONENT']")
  WebElement dropdownComponent;

  @FindBy(css = "[id*='C1__TXT_0FF02AFB3DFE5A53595462']")
  WebElement msgMandatoryTandC;

  @FindBy(css = "[id*='C2__C1__QUE_13B992799BD80BAE1217217_R1_1']")
  WebElement MandatoryTandC;

  @FindBy(css = "button[title='Back to Data Capture']")
  WebElement btn_bcktoDatacapture;

  @FindBy(css = "#C2__QUE_2411741C919DF0934660562")
  WebElement txt_paymntRef;

  @FindBy(css = "button[id*='CONFIRM']")
  WebElement btnConfirmPayment;

  @FindBy(css = "button.btn.btn-default.page-spinner[title='Upgrade now']")
  WebElement btnUpgradeNow;

  @FindBy(css = "button[id*='BUT_6F266DB023A12C1F523339']")
  WebElement button3StarProduct;

  @FindBy(css = "button#C2__BUT-RE-CALCULATE")
  WebElement btnReCalculate;

  @FindBy(css = "#C2__BUT_41DC0DB7791BD869934726")
  WebElement btnSaveprcpresentPage;

  @FindBy(css = "#C2__BUT_41DC0DB7791BD869934726[title='Save']")
  WebElement btn_renewQuoteSave;

  @FindBy(css = "#C2__QUE_83B5913BCD115CC1794679")
  WebElement txtFldquoteDesc_acceptance;

  @FindBy(css = "#C2__BUT_D6A69F84D226E30B1415464[title='SaveAcceptance']")
  WebElement btnsaveQuoteDesc_acceptance;

  @FindBy(css = "button#C2__BUT_D6A69F84D226E30B1415468[title='Cancel']")
  WebElement btn_qutCancel;

  @FindBy(css = "button#C2__BUT_6A3EEB799C31413C1462463")
  WebElement btnLapse_acceptance;

  @FindBy(css = "button#C2__BUT_D6A69F84D226E30B688855")
  WebElement btnSaveinAcptancePage;

  @FindBy(css = "h4#C2__HEAD_4F68B5B85CFB26FF1057492")
  WebElement msglapseWarningmsg;

  @FindBy(css = "button#C2__BUT_4F68B5B85CFB26FF1057498")
  WebElement btnContinuewarning;

  @FindBy(css = "button#C2__BUT_4F68B5B85CFB26FF1057504")
  WebElement btncancelWarning;

  @FindBy(css = "#C2__QUE_D6A69F84D226E30B1415462_0[value='Reviewed']")
  WebElement radiobtnReviewed_acceptance;

  @FindBy(css = "#C2__QUE_D6A69F84D226E30B1415462_1[value='Review Required']")
  WebElement radiobtnReviewreq_acceptance;

  @FindBy(css = "#C2__FMT_9C281AC77BF4F72E2893468")
  WebElement panelPaymentSchedule;

  @FindBy(css = "#C2__p1_GRP_9C281AC77BF4F72E3238818")
  WebElement panelDirectDebitGuarantee;

  @FindBy(css = "button[id*='BUT_15D8DD38C9D9D4FD403269']")
  WebElement btnNextOne;

  @FindBy(css = "button[id*='BUT_21E887F47BE45ECE11009']")
  WebElement btnNextTwo;

  @FindBy(css = "button[id*='BUT_921BCE84FE9FC3B2409611']")
  WebElement btnView;

  @FindBy(css = "button[id*='save-declaration']")
  WebElement btnSaveDeclaration;

  @FindBy(
      css = "#C2__p4_QUE_921BCE84FE9FC3B2469614 div:nth-child(1) > label[for='C2__QUE_921BCE84FE9FC3B2469614_0']")
  WebElement radAgree_Ele;

  @FindBy(css = "button[id*='C2__BUT_41DC0DB7791BD869949612']")
  WebElement btnSaveDatacapture;
  // Data capture page
  @FindBy(
      css = "input[name='C2__CONTACTCENTRE[1].COMMON[1].VARIATIONDESCRIPTION[1].QUOTEDESCRIPTION']")
  WebElement txtQuoteVariDesc;

  @FindBy(css = "#C2__p4_QUE_24D10CAD2CBD74854948763")
  WebElement totalPaymentInPremiumBanner;

  @FindBy(css = "button#C2__BUT_FF640219DCA82CE34011225")
  WebElement btnQuoteSave;

  @FindBy(css = "button#C2__BUT_FF640219DCA82CE34011231[title='Cancel']")
  WebElement btnQuoteCancel;

  @FindBy(css = "button[id*='DIRECT_TAKE_PAYMEN']")
  WebElement but_takPayment;

  @FindBy(css = ".checkbox.checkbox-info.checkbox-inline.payor-confirm-check-box")
  WebElement chkBx_toMakePayment;

  @FindBy(css = "C2__QUE_C1022B81F9580E243978260")
  WebElement txtFld_quoteDescppPage;

  @FindBy(css = "button[title='SavePricePres']")
  WebElement btnsaveQuoteDescppPage;

  @FindBy(css = "button#C2__BUT_C1022B81F9580E243978254")
  WebElement btnCancelQuoteDescppPage;

  @FindBy(css = "div#C2__FMT_C1022B81F9580E244006857 .close")
  WebElement btnCloseQuoteDescppPage;

  @FindBy(css = "button#C2__BUT_728A190A046539422616673[title='Buy']")
  WebElement btnBuy;

  @FindBy(css = "#C2__QUE_3661A386F2BF3E14683219_0")
  WebElement radEmail;

  @FindBy(css = "#C2__QUE_3661A386F2BF3E14683219_1")
  WebElement radPost;

  @FindBy(css = "#C2__QUE_644A45EDEAA7BED1142855_0")
  WebElement radOptinMarketYes;

  @FindBy(css = "#C2__QUE_644A45EDEAA7BED1142855_0") ////////
  WebElement radOptinMarketNo;

  @FindBy(css = "#C2__FMT_FD051A198CB3861E3809021")
  WebElement mdlReturnToDashboard;

  @FindBy(css = "#C2__QUE_24D10CAD2CBD74854948782")
  WebElement drpdwnPreferredPaymentDay;

  @FindBy(css = "#C2__BUT_FD051A198CB3861E3821012")
  WebElement btnNo;

  @FindBy(css = "div[id=\"C2__TXT_644A45EDEAA7BED1147104\"] >div>h2")
  WebElement txtTitle;

  @FindBy(css = "#C2__HEAD_EE2C7D9B8CC571FB2596088")
  WebElement h1AccDetailsTitle;

  /**********************************************************************************************
   ********************************* WebElements of BuyQuote Page- Ends *************************
   **********************************************************************************************/

  /**
   * 
   * Constructor class for Card Details Page Here we initializing the driver for page factory
   * objects. For ajax element waiting time has added while initialization
   * 
   * @param driver : Webdriver
   */
  public BuyQuotePage(WebDriver driver, ExtentTest report) {
    this.driver = driver;
    this.extentedReport = report;
    PageFactory.initElements(driver, this);
  }

  @Override
  protected void isLoaded() {

    if (!isPageLoaded) {
      Assert.fail();
    }

    if (isPageLoaded && !driver.getTitle().contains("BuyQuote")) {
      Log.fail("SSP Buyquote page did not open up. Site might be down.", driver, extentedReport);
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
   * To verify whether the option is selected
   * 
   * @param optionToverify - Reason name to verify extendReport Screenshot
   * @throws Exception
   * @return boolean
   * 
   */
  public boolean verify_AdjReason_Selected(String optionToverify, ExtentTest extendReport,
      boolean Screenshot) throws Exception {

    try {

      boolean valueSelected = false;

      Select selectedAdjReasonBox = new Select(selectedAdjustmentReason);
      List<WebElement> allSelectedOptions = selectedAdjReasonBox.getAllSelectedOptions();

      for (WebElement webElement : allSelectedOptions) {
        if (webElement.getText().equals(optionToverify)) {
          valueSelected = true;
          break;

        }

      }

      return valueSelected;

    } catch (Exception e) {
      throw new Exception("Error while verifying whether adjustment reason option is selected" + e);
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
      selectPaymentMethod(paymentMethod.toString(), extentedReport);

      if (paymentPlan.equals("Monthly")) {

        String accNo = testData.get("Account Number");
        String sortCode = testData.get("Sort Code");
        String accName = testData.containsKey("Account Name") ? testData.get("Account Name")
            : GenericUtils.getRandomCharacters("alpha", 5);
        String branch =
            (testData.containsKey("Bank Branch") && !testData.get("Bank Branch").equals(""))
                ? testData.get("Bank Branch") : "Bootle Centre, Santander, Liverpool";

       clickAddAccountDetails(extentedReport);
        checkBankAccount(accNo, sortCode, extentedReport);
        enterAccountName(accName, extentedReport);
        saveAccountDetails(extentedReport);
    selectAccount(sortCode, branch, accName, accNo, extentedReport, true);
        verifyAccountSelection(sortCode, branch, accName, accNo, extentedReport);
   
        tickCustomerAgreesCheckbox(extentedReport);
      }
    } catch (Exception e) {
      throw new Exception("Exception occured,Unable to Select Payment" + e);
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
   * @param paymentmethod : String
   * @param extentedReport
   * @throws Exception
   * 
   */
  public void selectPaymentMethod(String paymentMethod, ExtentTest extentedReport)
      throws Exception {
    try {
      Thread.sleep(2000);
      (new WebDriverWait(driver, 300).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Timed out to find 'Payment Method' dropdown"))
              .until(ExpectedConditions.elementToBeClickable(selectpaymentmethod));
      selectpaymentmethod.sendKeys(paymentMethod);
      Log.message("Payment Method Selected : " + paymentMethod, extentedReport);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Select Payment Method" + e);
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
   *
   * To verify Payment Transaction
   * 
   * @param extentedReport
   * @return boolean
   * @throws Exception
   * 
   */

  public boolean verifyPaymentTrasaction(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElementPresent(driver, txt_paymntRef,
          "Failed to get payment reference id after payment");

      Log.message("Payment reference number :" + txt_paymntRef.getText(), extentedReport);
      return (Integer.parseInt(txt_paymntRef.getText()) > 0);

    } catch (Exception e) {
      throw new Exception("Trasaction was not successful, failed to fetch the transaction id " + e);
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
          clickAcceptbtn(extentedReport);
          break;
      }
      objCustDashboardPage = new CustDashboardPage(driver, extentedReport).get();
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
   * To Click TakePayment button
   * 
   * @param extentedReport
   * @return CardDetailsPage
   * @throws Exception
   * 
   */
  public CardDetailsPage selectVisacard(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForPageLoad(driver);
      driver.switchTo().frame(frame_sagepay);
      WaitUtils.waitForElement(driver, lnkVisa);
      lnkVisa.click();
      Log.message("Selected the Visa card", driver, extentedReport, true);
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Spinner is active")).until(
              ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.loading-image")));
      return new CardDetailsPage(driver, extentedReport).get();

    } catch (Exception e) {
      throw new Exception("Error while selecting VISA Card : " + e);
    }

  }

  /**
   * To click the navigation stepper on top of screen (to move across different tab)
   * 
   * @param tabToClick as String
   * @param extentedReport
   * @throws Exception
   * 
   */

  public void checkQuotePage_traverse(String tabToClick, ExtentTest extentedReport,
      boolean Screenshot) throws Exception {
    try {

      switch (tabToClick) {

        case "dataCapture":
          tab_dataCapture.click();
          WaitUtils.waitForSpinner(driver);
          WaitUtils.waitForElementPresent(driver, txtYearofBuild,
              "Failed to navigate to data capture page");
          Log.message("Navigated to data capture page", extentedReport);
          break;

        case "pricePresentation":
          tab_pricePresentation.click();
          WaitUtils.waitForSpinner(driver);
          WaitUtils.waitForElementPresent(driver, btnAddHomeEmergency,
              "Failed to navigate to price presentation page");
          Log.message("Navigated to price presentation page", extentedReport);
          break;

        case "acceptance":
          tab_acceptance.click();
          WaitUtils.waitForSpinner(driver);
          WaitUtils.waitForElementPresent(driver, selectpaymentmethod,
              "Failed to navigate to acceptance page");
          Log.message("Navigated to Acceptance page", extentedReport);
          break;
      }

    } catch (Exception e) {
      throw new Exception("Unable to traverse to the tabs present on buy quote page" + e);
    }
  }

  /**
   * To verify the default tab name
   * 
   * @param tabName as String
   * @param extentedReport
   * @param Screenshot
   * @throws Exception
   * @return boolean
   * 
   */

  public boolean verifyDefaultTab(String tabName, ExtentTest extentedReport, boolean Screenshot)
      throws Exception {
    try {
      return GenericUtils.verifyWebElementTextEquals(tab_SelectedTab, tabName);

    }

    catch (Exception e) {
      throw new Exception("Failed to get the default selected tab, throws exception:" + e);
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
   * Description : Edit Terms & Conditions section
   * 
   * Return : boolean
   * 
   * @param extentedReport
   * 
   * @throws Exception : Custom Exception Message
   */
  public boolean verifyEditTermAndCondition(ExtentTest extentedReport) throws Exception {
    boolean toReturn = false;
    try {

      clickTermsAndConditionSection(extentedReport);

      if (WaitUtils.waitForElement(driver, btnAttachTermAndCondition, 2)) {
        btnAttachTermAndCondition.click();
        WaitUtils.waitForSpinner(driver);
        Log.message("Clicked on button - Attach Term and Condition", driver, extentedReport);
        if (WaitUtils.waitForElement(driver, firstTermAndConditionInList, 2)) {
          firstTermAndConditionInList.click();
          WaitUtils.waitForSpinner(driver);
          if (!selectTAC.isDisplayed() && !btnEditTermAndCondition.isDisplayed()) {
            firstTermAndConditionInList.click();
            WaitUtils.waitForSpinner(driver);
          }
          WaitUtils.waitForSpinner(driver);
          Log.message("Clicked on first Term and Condition", driver, extentedReport);
        } else {
          throw new Exception("First Term and condition not visible");
        }
      } else {
        throw new Exception("Button Attach Term and Condition not visible");
      }
      boolean isTACreadable = false;
      if (selectTAC.getText().length() > 0) {
        Log.message("Can read the terms and condition", extentedReport);
        Log.message("Terms and Conditions : " + selectTAC.getText(), extentedReport);
        isTACreadable = true;
      } else {
        Log.message("Cannot read the terms and condition", extentedReport);
        Log.message("Terms and Conditions : " + selectTAC.getText(), extentedReport);
      }
      if (isTACreadable == true) {
        String actualContent = selectTAC.getText();
        if (btnEditTermAndCondition.isDisplayed()) {
          btnEditTermAndCondition.click();
          WaitUtils.waitForSpinner(driver);
          Log.message("Clicked on Edit button", extentedReport);
          Thread.sleep(2000);
          JavascriptExecutor js = (JavascriptExecutor) driver;
          js.executeScript(
              "document.getElementById('C2__C1__QUE_3A056143913CF4954126787_R2').innerHTML = 'Modified TAC content'");
          toReturn = (selectTAC.getText().contains("Modified TAC content")) ? true : false;
          Log.message("Actual Content : " + actualContent, extentedReport);
          Log.message("Modified Content : " + selectTAC.getText(), extentedReport);
          Log.message("Able to Edit the Term and Condition", extentedReport);
        } else {
          throw new Exception("Edit button not visible");
        }
      }
      return toReturn;
    } catch (Exception e) {
      throw new Exception("Unable to edit the Terms and Conditions" + e);
    }
  }

  /**
   * Description : click TermsAndCondition Section in acceptance page
   * 
   * @param extentedReport
   * 
   * @throws Exception : Custom Exception Message
   */
  public void clickTermsAndConditionSection(ExtentTest extentedReport) throws Exception {
    try {

      WaitUtils.waitForElementPresent(driver, termsAndConditionsSection,
          "Failed to locate Terms and condition check box");
      termsAndConditionsSection.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Terms and Condition Section", extentedReport);
    } catch (Exception e) {
      throw new Exception("Error while clicking termsAndConditions Section" + e);
    }

  }

  /**
   * Description : click TermsAndCondition button of TermsAndCondition section in acceptance page
   * 
   * @param extentedReport
   * 
   * @throws Exception : Custom Exception Message
   */
  public void clickAttachTermAndConditionButton(ExtentTest extentedReport) throws Exception {
    try {

      WaitUtils.waitForElementPresent(driver, btnAttachTermAndCondition,
          "Failed to locate Attaceh Termsandcondition button");
      btnAttachTermAndCondition.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on button - Attach Term and Condition", extentedReport);
    } catch (Exception e) {
      throw new Exception("Error while clicking Attach TermandCondition button" + e);
    }

  }

  /**
   * Description : Read Terms & Conditions section
   * 
   * Return : boolean
   * 
   * @param extentedReport
   * 
   * @param policyToSelect - Policy name
   * 
   * @throws Exception : Custom Exception Message
   */
  public void selectTandCPolicy(String policyToSelect, ExtentTest extentedReport,
      boolean Screenshot) throws Exception {
    try {

      String cssPoilcyRow = "[id*='C2__C1__FMT_3A056143913CF4954126757_R']";

      String cssSelect = "id*='C2__C1__BUT_3A056143913CF4954126804_R";

      WaitUtils.waitForElement(driver, dropdownComponent);

      boolean policySelected = false;
      List<WebElement> PolicyOptions = driver.findElements(By.cssSelector(cssPoilcyRow));

      for (int i = 0; i < PolicyOptions.size(); i++) {
        String displayedPolicyName = PolicyOptions.get(i)
            .findElement(By.cssSelector("[id*='C1__QUE_3A056143913CF4954126760_R" + (i + 1) + "']"))
            .getText();
        if (displayedPolicyName.equals(policyToSelect)) {

          WebElement rowToselect = PolicyOptions.get(i).findElement(
              By.cssSelector("[id*='C1__QUE_3A056143913CF4954126760_R" + (i + 1) + "']"));

          JavascriptExecutor executor = (JavascriptExecutor) driver;
          executor.executeScript("arguments[0].click();", rowToselect);

          policySelected = true;
          Log.message(policyToSelect + " - T&C is clicked", driver, extentedReport, Screenshot);
          WaitUtils.waitForElement(driver,
              driver.findElement(By.cssSelector("[" + cssSelect + (i + 1) + "']")));
          driver.findElement(By.cssSelector("[" + cssSelect + (i + 1) + "']")).click();

          break;
        }
      }

      if (!policySelected) {
        throw new Exception(policyToSelect + " - T&C is not available to select");
      }

      WaitUtils.waitForinvisiblityofElement(driver, 240, cssComponentDropDown,
          "Policy window did not close");
      Log.message(policyToSelect + " - T&C is selected", driver, extentedReport);

    } catch (Exception e) {
      throw new Exception("Unable to add T&C policy " + e);
    }
  }

  /**
   * Description : To verify whether the terms and condition is added under terms & condition
   * section
   * 
   * Return : boolean
   * 
   * @param extentedReport
   * 
   * @param policyToSelect - Policy name
   * 
   * @throws Exception : Custom Exception Message
   */
  public boolean verifyTandCPolicyadded(String policyToVerify, ExtentTest extentedReport,
      boolean Screenshot) throws Exception {
    try {
      WaitUtils.waitForElement(driver, MandatoryTandC);
      String cssPolicy = "[id*='C1__QUE_304899FEFEF1DDEB353990_R1_']";
      boolean policyAdded = false;
      List<WebElement> PolicyOptions = driver.findElements(By.cssSelector(cssPolicy));

      for (int i = 0; i < PolicyOptions.size(); i++) {
        String displayedPolicyName = PolicyOptions.get(i).getText();
        if (displayedPolicyName.equals(policyToVerify)) {
          policyAdded = true;
          Log.message(policyToVerify + " - T&C is available under terms & condition section",
              driver, extentedReport, Screenshot);
          break;
        }
      }

      return policyAdded;

    } catch (Exception e) {
      throw new Exception("Error while veriyfying whether the T&C is added" + e);
    }
  }

  /**
   * Description : To verify edit and save terms&condition under terms & condition section
   * 
   * @param extentedReport
   * 
   * @param policyToEdit - Policy name
   * 
   * @throws Exception : Custom Exception Message
   */
  public boolean verifyTandC_Edited(String policyToEdit, ExtentTest extentedReport,
      boolean Screenshot) throws Exception {
    try {
      boolean isedited = false;
      boolean isClicked = false;
      WebElement rowToClick = null;
      Integer rowValue = null;

      WaitUtils.waitForElement(driver, MandatoryTandC);

      String cssPolicy = "[id*='C1__FMT_304899FEFEF1DDEB821953_R1_']";
      List<WebElement> PolicyOptions = driver.findElements(By.cssSelector(cssPolicy));

      for (int i = 0; i < PolicyOptions.size(); i++) {
        rowToClick = PolicyOptions.get(i);
        String displayedPolicyName = rowToClick
            .findElement(
                By.cssSelector("[id*='C1__QUE_304899FEFEF1DDEB353990_R1_" + (i + 1) + "']"))
            .getText();
        if (displayedPolicyName.equals(policyToEdit)) {
          rowToClick.click();
          Log.message(policyToEdit + " - added T&C is clicked under terms&condition", driver,
              extentedReport, Screenshot);
          isClicked = true;
          rowValue = i;
          break;
        }
      }

      if (!isClicked) {
        throw new Exception(policyToEdit + " is not availed to click");
      }

      String cssEditButton = "[id*='C1__BUT_304899FEFEF1DDEB941705_R1_" + (rowValue + 1) + "']";
      if (WaitUtils.waitForElement(driver, rowToClick.findElement(By.cssSelector(cssEditButton)))) {
        rowToClick.findElement(By.cssSelector(cssEditButton)).click();
        Log.message("Edit button is clicked to edit the T&C", driver, extentedReport);
      } else {
        throw new Exception("Edit button is not found to click");
      }

      String cssSaveButton = "[id*='C1__BUT_304899FEFEF1DDEB1181295_R1_" + (rowValue + 1) + "']";
      String cssTandCTextArea = "[id*='C1__QUE_A048EC24ED762AD77362602_R1_" + (rowValue + 1) + "']";

      if (WaitUtils.waitForElement(driver,
          rowToClick.findElement(By.cssSelector(cssTandCTextArea)))) {

        WaitUtils.waitForElement(driver, rowToClick.findElement(By.cssSelector(cssSaveButton)));
        WebElement txtAreaTandC = rowToClick.findElement(By.cssSelector(cssTandCTextArea));

        txtAreaTandC.click();

        // Edit the text
        ((JavascriptExecutor) driver).executeScript("arguments[0].innerText ='Testing'",
            txtAreaTandC);

        WebElement btnSave = rowToClick.findElement(By.cssSelector(cssSaveButton));
        btnSave.click();
        Log.message("Save button is clicked after editing the T&C", driver, extentedReport);

        WaitUtils.waitForinvisiblityofElement(driver, 180, cssSaveButton,
            "Save button did not close after waiting for 3 mins");

        cssPolicy = "[id*='C1__FMT_304899FEFEF1DDEB821953_R1_']";
        PolicyOptions = driver.findElements(By.cssSelector(cssPolicy));

        for (int i = 0; i < PolicyOptions.size(); i++) {
          rowToClick = PolicyOptions.get(i);
          String displayedPolicyName = rowToClick
              .findElement(
                  By.cssSelector("[id*='C1__QUE_304899FEFEF1DDEB353990_R1_" + (i + 1) + "']"))
              .getText();
          if (displayedPolicyName.equals(policyToEdit)) {
            rowToClick.click();
            Log.message(policyToEdit + " - added T&C is clicked under terms&condition", driver,
                extentedReport);
            isClicked = true;
            rowValue = i;
            break;
          }
        }

        if (!isClicked) {
          throw new Exception(policyToEdit + " is not availed to click to check edited T&C");
        }

        Log.message(policyToEdit + " - T&C is clicked again after values are edited", driver,
            extentedReport);

        String cssNewTxtArea =
            "[id*='C2__C1__QUE_A048EC24ED762AD77362602_R1_" + (rowValue + 1) + "']";
        txtAreaTandC = rowToClick.findElement(By.cssSelector(cssNewTxtArea));

        String EditedText = txtAreaTandC.getText();
        if (EditedText.contains("Testing")) {
          isedited = true;
          Log.message("Edited strings are saved in T&C after clicking save button", driver,
              extentedReport, Screenshot);
        }

      } else {
        throw new Exception("T&C text area is not found to edit");
      }

      return isedited;

    } catch (Exception e) {
      throw new Exception("Error while saving the edited T&C" + e);
    }
  }

  /**
   * Description : To click back to data capture button and verify section
   * 
   * @param extentedReport
   * 
   * @throws Exception : Custom Exception Message
   */
  public boolean clickDataCaptureandVerify(ExtentTest extentedReport, boolean Screenshot)
      throws Exception {
    try {
      if (WaitUtils.waitForElement(driver, btn_bcktoDatacapture, 2)) {
        btn_bcktoDatacapture.click();
        WaitUtils.waitForSpinner(driver);
        Log.message("Clicked on back to data capture button in price presentation page",
            extentedReport);

      }

      return WaitUtils.waitForElement(driver, txtYearofBuild, 10);
    } catch (Exception e) {
      throw new Exception(
          "Failed to navigate to data capture from price presentation page, throws exception:" + e);
    }
  }

  /**
   * click upgradenow (upgrading policy from 3* to 5*)
   *
   * @throws Exception : Custom Exception Message
   * @param extentedReport
   */

  public void clickUpgradeNow(ExtentTest extentedReport) throws Exception {

    try {
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElement(driver, btnUpgradeNow);
      btnUpgradeNow.click();
      Log.message("Clicked on Upgrade Now button", driver, extentedReport);
      WaitUtils.waitForSpinner(driver);

    } catch (Exception e) {
      throw new Exception("Unable to click on Upgrade Now Button :" + e);
    }
  }

  /**
   * verify policy upgrade
   * 
   * @param extentedReport
   */
  public void verifyUpgradePolicy(ExtentTest extentedReport) {
    try {

      if (GenericUtils.verifyWebElementTextContains(button3StarProduct, "3 Star Product")) {
        Log.pass("Policy upgraded successfully", driver, extentedReport, true);
      } else {
        Log.fail("Failed to upgrade policy", driver, extentedReport, true);
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  /**
   * click recalculate (recalculate premium)
   *
   * @throws Exception : Custom Exception Message
   * @param extentedReport
   */
  public void clickReCalculate(ExtentTest extentedReport) throws Exception {

    try {
      WaitUtils.waitForElement(driver, btnReCalculate);
      btnReCalculate.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Re-Calculate after upgrading policy", driver, extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to click on Re-Calculate Button :" + e);
    }
  }

  /**
   * Description : To save the quote in price presentation page
   * 
   * @param :txtToenter as String
   * @throws Exception : Custom Exception Message
   * @param extentedReport
   */

  public void clickSavebuttonForRenewalQuoteinPPpage(String txtToenter, ExtentTest extentedReport)
      throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElementPresent(driver, btn_renewQuoteSave,
          "Save button not found,failed to save renewal quote");
      btn_renewQuoteSave.click();
      Log.message("Clicked on save button in price presentation page", extentedReport);
      WaitUtils.waitForElementPresent(driver, txtFld_quoteDescppPage,
          "Quote variation modal failed to open /unable to locate quote variation description field from price presentation page");
      Actions actions = new Actions(driver);
      actions.moveToElement(txtFld_quoteDescppPage).click();
      actions.sendKeys(txtToenter);
      Thread.sleep(2000);
      actions.sendKeys(Keys.RETURN).build().perform();

      Log.message("Entered quote description :" + txtToenter, extentedReport);
      if (WaitUtils.waitForElement(driver, btn_qutCancel, 3))
        Log.message("Cancel button present in quote description modal", extentedReport);

      WaitUtils.waitForElementPresent(driver, btnsaveQuoteDescppPage,
          "Failed to locate quote variation save button on quote description modal");
      btnsaveQuoteDescppPage.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on save button on quote description modal", extentedReport);

    } catch (Exception e) {
      throw new Exception("unable to click on Save quote Button, throws exception:" + e);
    }
  }

  /**
   * Description : To save / Lapse the quote in acceptance page
   * 
   * @param :btntoClickinAcptancePage - enter the button user wanted to interact (Save / Lapse) as
   *        String
   * @throws Exception : Custom Exception Message
   * @param extentedReport
   */

  public boolean clicksave_LapsebuttoninAcceptancepage(String btnToClick, String quoteDesc,
      String btntoClickinAcptancePage, ExtentTest extentedReport) throws Exception {
    try {
      boolean boolReturn = false;
      switch (btntoClickinAcptancePage.toLowerCase()) {

        case "lapse":
          WaitUtils.waitForElementPresent(driver, btnLapse_acceptance,
              "Lapse button not present in acceptance page");
          btnLapse_acceptance.click();
          WaitUtils.waitForSpinner(driver);
          WaitUtils.waitForElementPresent(driver, msglapseWarningmsg,
              "Warning pop up did not open after clicking Lapse button in acceptance page");
          Log.message("Warning pop up opened after clicking lapse button", extentedReport);
          // verify button and text
          boolean boolmsg = GenericUtils.verifyWebElementTextContains(msglapseWarningmsg,
              "You are about to lapse this renewal quote variation leaving no active variations against this policy. Do you wish to continue?");
          if (WaitUtils.waitForElement(driver, btnContinuewarning)
              && WaitUtils.waitForElement(driver, btncancelWarning) && boolmsg) {
            Log.message("Warning message, continue and cancel button were present in warning modal",
                driver, true);
            btnContinuewarning.click();
            WaitUtils.waitForSpinner(driver);
            Log.message("Clicked on continue button to lapse the policy", extentedReport);
            boolReturn = true;
          }
          break;

        case "save":
          WaitUtils.waitForElementPresent(driver, btnSaveinAcptancePage,
              "Save button not present in acceptance page");
          btnSaveinAcptancePage.click();
          WaitUtils.waitForSpinner(driver);
          Log.message("Clicked on Save button in acceptance page", extentedReport);
          // Verify two radio button on save page
          // if(WaitUtils.waitForElement(driver, radiobtnReviewed_acceptance) &&
          // WaitUtils.waitForElement(driver, radiobtnReviewreq_acceptance))
          // Log.message("Both Reviewed and Review required button are visible on quote save modal",
          // extentedReport);
          if (btnToClick.equalsIgnoreCase("Reviewed"))
            radiobtnReviewed_acceptance.click();
          else
            radiobtnReviewreq_acceptance.click();
          Log.message("clicked on " + btnToClick + " button", extentedReport);
          WaitUtils.waitForElementPresent(driver, txtFldquoteDesc_acceptance,
              "Quote variation modal failed to open /unable to locate quote variation description field");
          Actions actions = new Actions(driver);
          actions.moveToElement(txtFldquoteDesc_acceptance).click();
          actions.sendKeys(quoteDesc);
          Log.message("Entered Quote description:" + quoteDesc, driver, extentedReport, true);
          Thread.sleep(2000);
          actions.sendKeys(Keys.RETURN).build().perform();

          WaitUtils.waitForElementPresent(driver, btnsaveQuoteDesc_acceptance,
              "Failed to locate quote variation save button on quote description modal");
          btnsaveQuoteDesc_acceptance.click();
          WaitUtils.waitForSpinner(driver);
          boolReturn = true;
          break;

        case "cancel":

          WaitUtils.waitForElementPresent(driver, btnSaveinAcptancePage,
              "Save button not present in acceptance page");
          btnSaveinAcptancePage.click();
          WaitUtils.waitForSpinner(driver);
          Log.message("Clicked on Save button in acceptance page", extentedReport);

          radiobtnReviewed_acceptance.click();
          WaitUtils.waitForElementPresent(driver, txtFldquoteDesc_acceptance,
              "Quote variation modal failed to open /unable to locate quote variation description field");
          Actions actions1 = new Actions(driver);
          actions1.moveToElement(txtFldquoteDesc_acceptance).click();
          actions1.sendKeys(quoteDesc);
          Log.message("Entered Quote description:" + quoteDesc, extentedReport);
          Thread.sleep(2000);
          actions1.sendKeys(Keys.RETURN).build().perform();

          WaitUtils.waitForElementPresent(driver, btn_qutCancel,
              "Cancel button not found in the quote description modal");
          Log.message("Cancel button present in quote description modal", extentedReport);
          btn_qutCancel.click();
          return (WaitUtils.waitForElement(driver, btnSaveinAcptancePage, 2)
              && !WaitUtils.waitForElement(driver, btn_qutCancel, 2));

      }

      return boolReturn;
    } catch (Exception e) {
      throw new Exception("Throws exception , unable to click " + btntoClickinAcptancePage
          + " button in acceptance page:" + e);
    }
  }

  public boolean clicksave_ReviewedbuttoninAcceptancepage(String btntoClickinAcptancePage,
      ExtentTest extentedReport) throws Exception {
    try {
      boolean boolReturn = false;
      switch (btntoClickinAcptancePage.toLowerCase()) {

        case "lapse":
          WaitUtils.waitForElementPresent(driver, btnLapse_acceptance,
              "Lapse button not present in acceptance page");
          btnLapse_acceptance.click();
          WaitUtils.waitForSpinner(driver);
          WaitUtils.waitForElementPresent(driver, msglapseWarningmsg,
              "Warning pop up did not open after clicking Lapse button in acceptance page");
          Log.message("Warning pop up opened after clicking lapse button", extentedReport);
          // verify button and text
          boolean boolmsg = GenericUtils.verifyWebElementTextContains(msglapseWarningmsg,
              "You are about to lapse this renewal quote variation leaving no active variations against this policy. Do you wish to continue?");
          if (WaitUtils.waitForElement(driver, btnContinuewarning, 2)
              && WaitUtils.waitForElement(driver, btncancelWarning, 2) && boolmsg) {
            Log.message("Warning message, continue and cancel button were present in warning modal",
                driver, true);
            btnContinuewarning.click();
            WaitUtils.waitForSpinner(driver);
            Log.message("Clicked on continue button to lapse the policy", extentedReport);
            boolReturn = true;
          }
          break;

        case "save":
          WaitUtils.waitForElementPresent(driver, btnSaveinAcptancePage,
              "Save button not present in acceptance page");
          btnSaveinAcptancePage.click();
          WaitUtils.waitForSpinner(driver);
          Log.message("Clicked on Save button in accepatance page", extentedReport);
          WaitUtils.waitForSpinner(driver);
          // Verify two radio button on save page

          radiobtnReviewed_acceptance.click();
          WaitUtils.waitForElementPresent(driver, txtFldquoteDesc_acceptance,
              "Quote variation modal failed to open /unable to locate quote variation description field");
          Actions actions = new Actions(driver);
          actions.moveToElement(txtFldquoteDesc_acceptance).click();
          actions.sendKeys("QuoteVariation");
          Thread.sleep(2000);
          actions.sendKeys(Keys.RETURN).build().perform();

          WaitUtils.waitForElementPresent(driver, btnsaveQuoteDesc_acceptance,
              "Failed to locate quote variation save button on quote description modal");
          btnsaveQuoteDesc_acceptance.click();
          WaitUtils.waitForSpinner(driver);
          boolReturn = true;
          break;

        case "cancel":

          WaitUtils.waitForElementPresent(driver, btnSaveinAcptancePage,
              "Save button not present in acceptance page");
          btnSaveinAcptancePage.click();
          WaitUtils.waitForSpinner(driver);
          Log.message("Clicked on Save button in accepatance page", extentedReport);

          radiobtnReviewed_acceptance.click();
          WaitUtils.waitForElementPresent(driver, txtFldquoteDesc_acceptance,
              "Quote variation modal failed to open /unable to locate quote variation description field");
          Actions actions1 = new Actions(driver);
          actions1.moveToElement(txtFldquoteDesc_acceptance).click();
          actions1.sendKeys("QuoteVariationCancel");
          Thread.sleep(2000);
          actions1.sendKeys(Keys.RETURN).build().perform();

          WaitUtils.waitForElementPresent(driver, btn_qutCancel,
              "Cancel button not found in the quote description modal");
          Log.message("Cancel button present in quote description modal", extentedReport);
          btn_qutCancel.click();
          return (WaitUtils.waitForElement(driver, btnSaveinAcptancePage, 2)
              && !WaitUtils.waitForElement(driver, btn_qutCancel, 2));

      }

      return boolReturn;
    } catch (Exception e) {
      throw new Exception("Throws exception , unable to click " + btntoClickinAcptancePage
          + " button in acceptance page:" + e);
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
   * click NextTwo button
   * 
   * @param extentReport
   * @throws Exception : Custom Exception Message
   */
  public void clickNextTwo(ExtentTest extentReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      btnNextTwo.click();
      Log.message("Clicked on Next button", extentReport);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Click on Next" + e);
    }
  }

  /**
   * click View button
   * 
   * @param extentReport
   * @throws Exception : Custom Exception Message
   */
  public void clickView(ExtentTest extentReport) throws Exception {
    try {

      WaitUtils.waitForSpinner(driver);
      (new WebDriverWait(driver, 4000).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find view button"))
              .until(ExpectedConditions.elementToBeClickable(btnView));
      btnView.click();
      WaitUtils.waitForPageLoad(driver);
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on View Button", driver, extentReport);

    } catch (Exception e) {
      throw new Exception("Unable to Click on View Button :" + e);
    }
  }

  /**
   * click Agree
   * 
   * @throws Exception : Custom Exception Message
   * @param extentReport
   */
  public void clickAgree(ExtentTest extentReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElement(driver, btnSaveDeclaration);
      selectAgree("Customer agrees", extentReport);
      btnSaveDeclaration.click();

      Thread.sleep(3000);
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Save Button", extentReport);

    } catch (Exception e) {
      throw new Exception("Unable to Click on Agreement Statement : " + e);
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
  public void selectAgree(String agree, ExtentTest extentReport) throws Exception {
    try {

      WaitUtils.waitForElement(driver, radAgree_Ele);
      selectRadioButton(radAgree, agree);
      Log.message("Selected Agreement Statement :" + agree, extentReport);

    } catch (Exception e) {
      throw new Exception("Unable to select Agreement Statement" + e);
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
   * Description : click save button in data capture page and verify the quote description dialog
   * box
   *
   * @throws Exception
   * @throws Exception
   * 
   */

  public void clickSaveDataCapture(ExtentTest extentReport, boolean Screenshot) throws Exception {
    try {
      WaitUtils.waitForElement(driver, btnSaveDatacapture, 2);
      btnSaveDatacapture.click();
      Log.message("clicked on save button in data capture page", driver, extentReport);

    } catch (Exception e) {
      throw new Exception("Error in clicking save button in data capture page : " + e);
    }
  }

  /**
   * Description :Enter quote description and verify the buttons
   *
   * @throws Exception
   * @throws Exception
   * 
   */
  public boolean enterQuoteDescVerifybuttons(String buttonToClick, ExtentTest extentReport,
      boolean Screenshot) throws Exception {
    try {
      boolean boolval = false;
      WaitUtils.waitForElementPresent(driver, txtQuoteVariDesc,
          "Vairation description dailog box was not opened");
      txtQuoteVariDesc.sendKeys("Quote Variation");
      Log.message(
          "Variation description text box opened and variation description was entered as Quote Variation",
          extentReport);
      if (WaitUtils.waitForElement(driver, btnQuoteSave, 2)
          && WaitUtils.waitForElement(driver, btnQuoteCancel, 2))
        Log.message("Button save and cancel present in the quote description dialog", driver,
            extentReport, true);
      else
        throw new Exception("Button save and cancel not present in the quote description dialog");

      if (buttonToClick.equalsIgnoreCase("cancel")) {
        btnQuoteCancel.click();
        WaitUtils.waitForSpinner(driver);
        Log.message("Clicked on cancel button in quote description dialog", extentReport);
        boolval = WaitUtils.waitForElement(driver, btnSaveDatacapture, 10);

      } else {
        btnQuoteSave.click();
        Log.message("Clicked on Save button in quote description dialog", extentReport);

      }
      return boolval;
    }

    catch (Exception e) {
      throw new Exception("Exception occured while validating quote variation dialog : " + e);
    }
  }

  /**
   *
   * Description: To click the Take Payment option
   * 
   * @param extentedReport
   * @throws Exception
   * 
   */

  public void takePayment(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElement(driver, but_takPayment, 60);
      but_takPayment.click();
      Log.message("Clicked on Take Payment", driver, extentedReport);
      WaitUtils.waitForSpinner(driver);

    } catch (Exception e) {
      throw new Exception("Unable to click on take payment Button, exception occured :" + e);
    }

  }

  /**
   *
   * Description: To click check box in payment
   * 
   * @param extentedReport
   * @throws Exception
   * 
   */

  public void clickCheckboxinPayment(ExtentTest extentedReport, boolean Screenshot)
      throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElement(driver, chkBx_toMakePayment, 60);
      if (chkBx_toMakePayment.getAttribute("checked").equalsIgnoreCase("checked"))
        chkBx_toMakePayment.click();
      Log.message(
          "Clicked on checkbox 'I have spoken directly to the new Payor and he/she has confirmed acceptance to making the payment'",
          driver, extentedReport);
      WaitUtils.waitForSpinner(driver);

    } catch (Exception e) {
      throw new Exception("Unable to click on take payment Button, exception occured :" + e);
    }

  }

  /**
   *
   * Description: To click check box in payment
   * 
   * @param extentedReport
   * @throws Exception
   * 
   */

  public boolean clickCancelandClosebuttonppPageandVerify(String btnToClick,
      ExtentTest extentedReport, boolean Screenshot) throws Exception {
    try {
      boolean boolval = false;
      WaitUtils.waitForElementPresent(driver, btn_renewQuoteSave,
          "Save button was not found in price presentaion page");
      btn_renewQuoteSave.click();
      Log.message("Clicked on save button in price presentation page", extentedReport);
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElement(driver, btnCancelQuoteDescppPage, 60);
      if (btnToClick.equalsIgnoreCase("cancel"))
        btnCancelQuoteDescppPage.click();
      else
        btnCloseQuoteDescppPage.click();
      Log.message("Clicked on " + btnToClick + " button successfully", driver, extentedReport);
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElementPresent(driver, btnAddHomeEmergency,
          "Failed to navigate to price presentation page");
      boolval = true;
      Log.message("Navigated to price presentation page", extentedReport);
      return boolval;

    } catch (Exception e) {
      throw new Exception("Unable to click on " + btnToClick + " button, exception occured :" + e);
    }

  }

  /**
   * click Buy button
   * 
   * @param extentedReport
   * @throws Exception : Custom Exception Message
   */

  public void clickBuy(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForElement(driver, btnBuy);
      btnBuy.click();
      Log.message("Clicked on Buy Quote button", driver, extentedReport, true);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Click on Buy Button - Premium values could be zero :" + e);
    }
  }

  /**
   * To click preferences
   * 
   * @param extentedReport
   * @throws Exception : Custom Exception Message
   */

  public void clickEditPreferences(ExtentTest extentedReport, boolean Screenshot) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, radOptinMarketYes,
          "Customer Preferernces Opt in marketing field failed to load");
      radOptinMarketYes.click();
      Log.message("Sected yes radio button of Opt in Marketing", extentedReport);
      radPost.click();
      Log.message("Selected Post as correspondance preference", driver, extentedReport, true);

    } catch (Exception e) {
      throw new Exception("Unable to Click on Buy Button - Premium values could be zero :" + e);
    }
  }

  /**
   * click Next One button
   * 
   * @param extentReport
   *
   * @throws Exception : Custom Exception Message
   */
  public void clickNextOne(ExtentTest extentReport) throws Exception {
    try {
      btnNextOne.click();
      Log.message("Clicked on Next button", extentReport);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Click on Next button : " + e);
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
      Thread.sleep(1000);
      Log.message("Clicked on account select button", extentReport);
      WaitUtils.waitForSpinner(driver);

    } catch (Exception e) {
      throw new Exception("Error while Adding Account Details : " + e);
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
   * Description : To save = the quote in acceptance page
   * 
   * @param :description
   * @throws Exception : Custom Exception Message
   * @param extentedReport
   * @return boolean
   */

  public boolean clicksavebuttoninAcceptancepage(String description, ExtentTest extentedReport)
      throws Exception {
    boolean boolReturn = false;
    try {

      WaitUtils.waitForElementPresent(driver, btnSaveinAcptancePage,
          "Save button not present in acceptance page");
      btnSaveinAcptancePage.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Save button in accepatance page", extentedReport);
      WaitUtils.waitForSpinner(driver);
      // Verify two radio button on save page
      radiobtnReviewreq_acceptance.click();
      WaitUtils.waitForElementPresent(driver, txtFldquoteDesc_acceptance,
          "Quote variation modal failed to open /unable to locate quote variation description field");
      Actions actions = new Actions(driver);
      actions.moveToElement(txtFldquoteDesc_acceptance).click();
      actions.sendKeys(description);
      Thread.sleep(2000);
      actions.sendKeys(Keys.RETURN).build().perform();

      WaitUtils.waitForElementPresent(driver, btnsaveQuoteDesc_acceptance,
          "Failed to locate quote variation save button on quote description modal");
      btnsaveQuoteDesc_acceptance.click();
      boolReturn = true;

    } catch (Exception e) {
      throw new Exception("Throws exception , unable to click save button in acceptance page:" + e);
    }
    return boolReturn;
  }


  /**
   * To click the Save or Lapse button
   * 
   * @param btnToClick
   * @param extentedReport
   * @param Screenshot
   * @throws Exception
   * 
   */
  public void clickSaveOrLapse(String btnToClick, ExtentTest extentedReport, boolean Screenshot)
      throws Exception {
    try {
      if (btnToClick.equalsIgnoreCase("Lapse")) {
        WaitUtils.waitForElement(driver, btnLapse_acceptance);
        btnLapse_acceptance.click();
        WaitUtils.waitForSpinner(driver);
        Log.message("Lapse button is clicked", driver, extentedReport, Screenshot);
      } else if (btnToClick.equalsIgnoreCase("Save")) {
        WaitUtils.waitForElement(driver, btnSaveinAcptancePage);
        btnSaveinAcptancePage.click();
        WaitUtils.waitForSpinner(driver);
        Log.message("Save button is clicked", extentedReport);
      }
    } catch (Exception e) {
      throw new Exception("Error while click Lapse button : " + e);
    }
  }

  /**
   * To verify the warning pop up
   * 
   * @param extentedReport
   * @param warningMsg
   * 
   */

  public boolean verifyWarningMsg(ExtentTest extentedReport, String warningMsg) {
    boolean boolMsg = false;
    WaitUtils.waitForElementPresent(driver, msglapseWarningmsg,
        "Warning pop up did not open after clicking Lapse button in acceptance page");
    Log.message("Warning pop up opened after clicking lapse button", extentedReport);
    // verify button and text
    if (GenericUtils.verifyWebElementTextContains(msglapseWarningmsg, warningMsg)) {
      boolMsg = true;
    }
    return boolMsg;
  }

  /**
   * To get Total amount to pay
   * 
   * @param extentReport
   * @return String
   * @throws Exception
   * 
   */

  public String getPolicyPremiumAmount(ExtentTest extentedReport) throws Exception {
    WaitUtils.waitForElement(driver, totalPaymentInPremiumBanner, 20);
    String textAmount = totalPaymentInPremiumBanner.getText();
    Log.message("Value of Payment textbox " + textAmount + "", driver, extentedReport);
    return textAmount;
  }

  /**
   * To click customer agreement button
   * 
   * @param extentReport
   * @throws Exception
   * 
   */

  public void clickCutomerAgreeButton(ExtentTest extentedReport) throws Exception {
    Actions actions = new Actions(driver);
    actions.moveToElement(checkbox_CustAgree).click().build().perform();
    Thread.sleep(3000);
    Log.message("Clicked on customer agree checkbox in payment page", extentedReport);
  }

  /**
   * To Verify cuctomer agreement is selected
   * 
   * @param extentReport
   * @param Screenshot
   * 
   */

  public boolean verifyCustomerAgreementChecked(ExtentTest extentedReport, boolean Screenshot) {
    boolean checked = false;
    WaitUtils.waitForElement(driver, checkbox_CustAgree, 20);
    if (checkbox_CustAgree.isSelected()) {
      checked = true;
      Log.message("Customer Agreement check box is checked", driver, extentedReport, Screenshot);
    }
    return checked;
  }

 
}
