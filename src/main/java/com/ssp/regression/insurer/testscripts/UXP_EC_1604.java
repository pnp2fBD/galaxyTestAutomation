package com.ssp.regression.insurer.testscripts;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.ssp.support.BaseTest;
import com.ssp.support.DateTimeUtility;
import com.ssp.support.EmailReport;
import com.ssp.support.Log;
import com.ssp.support.WebDriverFactory;
import com.ssp.utils.DataProviderUtils;
import com.ssp.utils.DataUtils;
import com.ssp.utils.GenericUtils;
import com.ssp.uxp_pages.BillingAdjustmentPage;
import com.ssp.uxp_pages.CardDetailsPage;
import com.ssp.uxp_pages.CustDashboardPage;
import com.ssp.uxp_pages.HomePage;
import com.ssp.uxp_pages.LoginPage;
import com.ssp.uxp_pages.NewQuotePage;
import com.ssp.uxp_pages.SearchPage;
import com.ssp.uxp_pages.SuspendBillingPage;
import com.ssp.uxp_pages.UnsuspendBillingPage;

@Listeners(EmailReport.class)
public class UXP_EC_1604 extends BaseTest {
  private String webSite;

  @BeforeMethod(alwaysRun = true)
  public void init(ITestContext context) throws IOException {
    webSite = System.getProperty("webSite") != null ? System.getProperty("webSite")
        : context.getCurrentXmlTest().getParameter("webSite");
  }

  public ExtentTest addTestInfo(String testCaseId, String testDesc) {
    String browserwithos = null;
    String test = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getName();

    String browsername = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
        .getParameter("browser");
    String browserversion = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
        .getParameter("browser_version");
    String os = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
        .getParameter("os").substring(0, 1);
    String osversion = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
        .getParameter("os_version");
    browserwithos = os + osversion + "_" + browsername + browserversion;

    return Log.testCaseInfo(testCaseId + " [" + test + "]",
        testCaseId + " - " + testDesc + " [" + browserwithos + "]", test, "UXP_NFR Team");
  }

  public HashMap<String, String> getTestData(String testcaseId) {
    String env =
        Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("env");
    String className = "UXP_EC_1604_" + env;
    return DataUtils.testDatabyID(testcaseId, className);
  }


  @Test(
      description = "Check that the user is able to change the payor by selecting the Change Payor button from the payment panel",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_765(String browser) throws Exception {

    String tcId = "TC_765";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String paymentPlan = testData.get("ChangePayment Plan");
    String paymentMethod = testData.get("ChangePayment Method");

    String adjustmentReason = testData.get("AdjustmentReason");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String changePayorfirstName =
        GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String changePayorlastName =
        GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");

    testData2.put("First Name", changePayorfirstName);
    testData2.put("Last Name", changePayorlastName);
    String postCode = testData2.get("Post Code");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Create Customer1 for change payor
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Customer2 Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      custdashboardpage = newquotepage.confirmPayment(extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Selecting billing adjustment
      custdashboardpage.selectBAfromManagePolicyTab(extentedReport, true);
      BillingAdjustmentPage billingAdjustmentPage =
          custdashboardpage.enterBADetails(testData, extentedReport, true);

      // Selecting adjustment reason
      billingAdjustmentPage.selectAdjustmentReason(adjustmentReason, extentedReport, true);
      billingAdjustmentPage.clickNext(extentedReport);

      // Selecting Payment method/Plan
      billingAdjustmentPage.selectPaymentPlan(paymentPlan, extentedReport);
      billingAdjustmentPage.selectPaymentMethod(paymentMethod, extentedReport, true);
      billingAdjustmentPage.clickChangePayorButton(extentedReport, true);

      String payorName = billingAdjustmentPage.enterChangePayorDetails(changePayorlastName,
          changePayorfirstName, postCode, extentedReport, true);

      Log.softAssertThat(billingAdjustmentPage.verifyPayorName(payorName),
          "User is able to change payor by selecting 'Change Payor' button from the payment panel",
          "User is unable to change payor by selecting 'Change Payor' button from the payment panel",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "Check that the following processes that are modified are recorded in system audit history, if adjustment is accepted successfully- Change of Payment Plan, Change of Payment Method, Change of Payor, Change of Preferred Payment Day, Change of Account Details",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_772(String browser) throws Exception {

    String tcId = "TC_772";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);


    String postCode = testData2.get("Post Code");
    String paymentPlan = testData.get("ChangePayment Plan");
    String paymentMethod = testData.get("ChangePayment Method");

    String preferredPaymentDay = testData.get("Preferred PaymentDay");

    String adjustmentReason = testData.get("AdjustmentReason");

    String detailToCheck = testData.get("SystemAuditDetail");
    String[] arrSystemAudit = detailToCheck.split(",");

    String ChangePayorfirstName =
        GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String ChangePayorlastName =
        GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");

    testData2.put("First Name", ChangePayorfirstName);
    testData2.put("Last Name", ChangePayorlastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Create Customer1 for change payor
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);


      searchPage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Customer2 Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      custdashboardpage = newquotepage.confirmPayment(extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // Selecting billing adjustment
      custdashboardpage.selectBAfromManagePolicyTab(extentedReport, true);
      BillingAdjustmentPage billingAdjustmentPage =
          custdashboardpage.enterBADetails(testData, extentedReport, true);

      // Selecting adjustment reason
      billingAdjustmentPage.selectAdjustmentReason(adjustmentReason, extentedReport, true);
      billingAdjustmentPage.clickNext(extentedReport);

      // Selecting Payment method/Plan
      billingAdjustmentPage.selectPaymentPlan(paymentPlan, extentedReport);
      billingAdjustmentPage.selectPaymentMethod(paymentMethod, extentedReport, true);

      // changing Payor
      billingAdjustmentPage.clickChangePayorButton(extentedReport, true);
      billingAdjustmentPage.enterChangePayorDetails(ChangePayorlastName, ChangePayorfirstName,
          postCode, extentedReport, true);
      billingAdjustmentPage.clickPayorConfirmationCheckbox(extentedReport, true);



      // Select preferred paymentday
      billingAdjustmentPage.selectPreferredPaymentDay(preferredPaymentDay, extentedReport, true);

      // Add account detail
      billingAdjustmentPage.addAccountDetail(testData, extentedReport, true);

      billingAdjustmentPage.clickAcceptBtn(extentedReport);

      Log.softAssertThat(custdashboardpage.verifySystemAuditHistory(extentedReport, arrSystemAudit),
          "System audit contains the following details - " + detailToCheck,
          "System audit does not contains the following details - " + detailToCheck, driver,
          extentedReport, true);

      Log.testCaseResult(extentedReport);


    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(
      description = "Check that the reference is accepted only after entering the Payment reference and click on 'Confirm' button (when payments are NOT managed via payment hub)",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_787(String browser) throws Exception {

    String tcId = "TC_787";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String adjustmentReason = testData.get("AdjustmentReason");
    String paymentPlan = testData.get("ChangePayment Plan");
    String paymentMethod = testData.get("ChangePayment Method");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      testData = searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);

      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to custdashboardpage", "Could not Navigate to custdashboardpage", driver,
          extentedReport, true);

      custdashboardpage.selectBAfromManagePolicyTab(extentedReport, true);
      BillingAdjustmentPage billingAdjustmentPage =
          custdashboardpage.enterBADetails(testData, extentedReport, true);

      // Selecting adjustment reason
      billingAdjustmentPage.selectAdjustmentReason(adjustmentReason, extentedReport, true);
      billingAdjustmentPage.clickNext(extentedReport);

      // Selecting Payment method/Plan
      billingAdjustmentPage.selectPaymentPlan(paymentPlan, extentedReport);
      billingAdjustmentPage.selectPaymentMethod(paymentMethod, extentedReport, true);

      billingAdjustmentPage.clickTakePaymentBtn(extentedReport);
      billingAdjustmentPage.selectVisacard(extentedReport);

      // Enter Card Details
      billingAdjustmentPage.enterCardNumber(testData, extentedReport, true);
      billingAdjustmentPage.selectExpiry(testData, extentedReport, true);
      billingAdjustmentPage.enterVerification(extentedReport, true);
      billingAdjustmentPage.enterName(testData, extentedReport, true);
      billingAdjustmentPage.clickbuy(extentedReport, true);

      // Click continue button
      billingAdjustmentPage.clickContinueButton(extentedReport);
      billingAdjustmentPage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Reference is accepted after clicking on 'Confirm' button",
          "Reference is not accepted after clicking on 'Confirm' button", driver, extentedReport,
          true);

      Log.testCaseResult(extentedReport);


    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "Check that when the customer address is modified in the PSP iFrame, it is NOT getting reflected within the Engagement Cente application",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_790(String browser) throws Exception {

    String tcId = "TC_790";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String adjustmentReason = testData.get("AdjustmentReason");
    String paymentPlan = testData.get("ChangePayment Plan");
    String paymentMethod = testData.get("ChangePayment Method");
    String addressOne = testData.get("AddressOne");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to custdashboardpage", "Could not Navigate to custdashboardpage", driver,
          extentedReport, true);

      custdashboardpage.selectBAfromManagePolicyTab(extentedReport, true);
      BillingAdjustmentPage billingAdjustmentPage =
          custdashboardpage.enterBADetails(testData, extentedReport, true);

      // Selecting adjustment reason
      billingAdjustmentPage.selectAdjustmentReason(adjustmentReason, extentedReport, true);
      billingAdjustmentPage.clickNext(extentedReport);

      // Selecting Payment method/Plan
      billingAdjustmentPage.selectPaymentPlan(paymentPlan, extentedReport);
      billingAdjustmentPage.selectPaymentMethod(paymentMethod, extentedReport, true);

      billingAdjustmentPage.clickTakePaymentBtn(extentedReport);
      billingAdjustmentPage.selectVisacard(extentedReport);

      // Enter Card Details
      billingAdjustmentPage.enterCardNumber(testData, extentedReport, true);
      billingAdjustmentPage.selectExpiry(testData, extentedReport, true);
      billingAdjustmentPage.enterVerification(extentedReport, true);
      billingAdjustmentPage.enterName(testData, extentedReport, true);

      billingAdjustmentPage.changeAddressOne(addressOne, extentedReport, true);
      billingAdjustmentPage.clickbuy(extentedReport, true);

      // Click continue button
      billingAdjustmentPage.clickContinueButton(extentedReport);
      billingAdjustmentPage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to customer dashboard page",
          "could not able to navigated to customer dashboard page", driver, extentedReport, true);

      Log.softAssertThat(!custdashboardpage.verifyContactAddressDetails(addressOne),
          "The customer address modified in the PSP iFrame is not reflected within the Engagement Cente application",
          "The customer address modified in the PSP iFrame is reflected within the Engagement Cente application",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "Check that the user is able to complete the suspension by selecting Accept button",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_813(String browser) throws Exception {

    String tcId = "TC_813";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String suspendBillingReason = testData.get("SuspendBilling Reason");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      SuspendBillingPage suspendBillingPage =
          custdashboardpage.selectSuspendBillingfromManagePolicy(extentedReport, true);

      suspendBillingPage.completeSuspendBilling(suspendBillingReason, extentedReport, true);

      Log.softAssertThat(custdashboardpage.verifySuspendBilling_Accepted(),
          "User is able to complete the Suspend Billing after clicking Accept button",
          "User is unable to complete the Suspend Billing after clicking Accept button", driver,
          extentedReport, true);
      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "Check that the user is able to complete the unsuspend process by clicking on the 'Accept' button",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_825(String browser) throws Exception {

    String tcId = "TC_825";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String paymentPlan = testData.get("Payment Plan");
    String paymentMethod = testData.get("Payment Method");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String suspendBillingReason = testData.get("SuspendBilling Reason");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      SuspendBillingPage suspendBillingPage =
          custdashboardpage.selectSuspendBillingfromManagePolicy(extentedReport, true);

      suspendBillingPage.completeSuspendBilling(suspendBillingReason, extentedReport, true);

      Log.softAssertThat(custdashboardpage.verifySuspendBilling_Accepted(),
          "Suspend Billing is accepted", "Suspend Billing is not accepted", driver, extentedReport,
          true);


      UnsuspendBillingPage unsuspendBillingPage =
          custdashboardpage.clickUnsuspendBillingfromManagePolicy(extentedReport, true);

      // Select Payment Plan & method
      unsuspendBillingPage.selectPaymentPlan(paymentPlan, extentedReport);
      unsuspendBillingPage.selectPaymentMethod(paymentMethod, extentedReport, true);

      // unsuspendBillingPage.selectFirstBankDetail(extentedReport, true);
      unsuspendBillingPage.selectDirectDebitGuarantee(extentedReport, true);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Custdashboard page", "Could not navigate to custdashboardpage", driver,
          extentedReport, true);

      Log.softAssertThat(custdashboardpage.verifyUnsuspendBilling_Accepted(),
          "User is able to complete the Unsuspend Billing",
          "User is unable to complete the Unsuspend Billing", driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "Check that based on the selection of 'Payment method (Card)' type, the information in the payment panel is displayed",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_764(String browser) throws Exception {

    String tcId = "TC_764";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String adjustmentReason = testData.get("AdjustmentReason");
    String paymentPlan = testData.get("ChangePayment Plan");
    String paymentMethod = testData.get("ChangePayment Method");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);

      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to custdashboardpage", "Could not Navigate to custdashboardpage", driver,
          extentedReport, true);

      custdashboardpage.selectBAfromManagePolicyTab(extentedReport, true);
      BillingAdjustmentPage billingAdjustmentPage =
          custdashboardpage.enterBADetails(testData, extentedReport, true);

      // Selecting adjustment reason
      billingAdjustmentPage.selectAdjustmentReason(adjustmentReason, extentedReport, true);
      billingAdjustmentPage.clickNext(extentedReport);

      // Selecting Payment method/Plan
      billingAdjustmentPage.selectPaymentPlan(paymentPlan, extentedReport);
      billingAdjustmentPage.selectPaymentMethod(paymentMethod, extentedReport, true);

      Log.softAssertThat(
          billingAdjustmentPage.uielement.verifyPageElements(Arrays.asList("titlePaymentSchedule"),
              billingAdjustmentPage),
          "Payment Schedule Detail is displayed in payment panel",
          "Payment Schedule Detail is not displayed in payment panel", driver, extentedReport,
          true);

      Log.softAssertThat(
          billingAdjustmentPage.uielement.verifyPageElements(Arrays.asList("titleAccountDetail"),
              billingAdjustmentPage),
          "Account Detail is displayed in payment panel",
          "Account Detail is not displayed in payment panel", driver, extentedReport, true);

      Log.softAssertThat(
          billingAdjustmentPage.uielement.verifyPageElements(
              Arrays.asList("titleDebitDirectGuarantee"), billingAdjustmentPage),
          "DebitDirect Guarantee is displayed in payment panel",
          "DebitDirect Guarantee is not displayed in payment panel", driver, extentedReport, true);

      Log.softAssertThat(
          billingAdjustmentPage.uielement.verifyPageElements(
              Arrays.asList("drpdwnPreferredPaymentDay"), billingAdjustmentPage),
          "Preferred PaymentDay is displayed in payment panel",
          "Preferred PaymentDay is not displayed in payment panel", driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "Check that based on the selection of 'Payment method (Direct Debit)' type, the information in the payment panel is displayed",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_766(String browser) throws Exception {

    String tcId = "TC_766";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String adjustmentReason = testData.get("AdjustmentReason");
    String paymentPlan = testData.get("ChangePayment Plan");
    String paymentMethod = testData.get("ChangePayment Method");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      custdashboardpage.selectBAfromManagePolicyTab(extentedReport, true);
      BillingAdjustmentPage billingAdjustmentPage =
          custdashboardpage.enterBADetails(testData, extentedReport, true);

      // Selecting adjustment reason
      billingAdjustmentPage.selectAdjustmentReason(adjustmentReason, extentedReport, true);
      billingAdjustmentPage.clickNext(extentedReport);

      // Selecting Payment method/Plan
      billingAdjustmentPage.selectPaymentPlan(paymentPlan, extentedReport);
      billingAdjustmentPage.selectPaymentMethod(paymentMethod, extentedReport, true);

      Log.softAssertThat(
          billingAdjustmentPage.uielement
              .verifyPageElements(Arrays.asList("panelCardDetailsection"), billingAdjustmentPage),
          "CardDetail section is displayed in payment panel",
          "CardDetail section is not displayed in payment panel", driver, extentedReport, true);

      Log.softAssertThat(
          billingAdjustmentPage.uielement.verifyPageElements(Arrays.asList("panelPremiumSummary"),
              billingAdjustmentPage),
          "Premium summary section is displayed in payment panel",
          "Premium summary section is not displayed in payment panel", driver, extentedReport,
          true);


      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "Check that a standard modal 'You are about to return to Customer Dashboard. Any changes you have made will not be saved. Do you wish to continue?' is displayed  when click on 'Cancel' button in the payment modal",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_769(String browser) throws Exception {

    String tcId = "TC_769";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String adjustmentReason = testData.get("AdjustmentReason");
    String paymentPlan = testData.get("ChangePayment Plan");
    String paymentMethod = testData.get("ChangePayment Method");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);

      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      custdashboardpage.selectBAfromManagePolicyTab(extentedReport, true);
      BillingAdjustmentPage billingAdjustmentPage =
          custdashboardpage.enterBADetails(testData, extentedReport, true);

      // Selecting adjustment reason
      billingAdjustmentPage.selectAdjustmentReason(adjustmentReason, extentedReport, true);
      billingAdjustmentPage.clickNext(extentedReport);

      // Selecting Payment method/Plan
      billingAdjustmentPage.selectPaymentPlan(paymentPlan, extentedReport);
      billingAdjustmentPage.selectPaymentMethod(paymentMethod, extentedReport, true);

      billingAdjustmentPage.clickCancelButton(extentedReport);

      Log.softAssertThat(
          billingAdjustmentPage.uielement.verifyPageElements(Arrays.asList("mdlReturnToDashboard"),
              billingAdjustmentPage),
          "'You are about to return to Customer Dashboard.Any changes you have made will not be saved. Do you wish to continue?' modal is displayed after clicking cancel button",
          "'You are about to return to Customer Dashboard.Any changes you have made will not be saved. Do you wish to continue?' modal is not displayed after clicking cancel button",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(
      description = "Check that the user returns to the Customer Dashboard and changes are aborted when 'Yes' option is selected from the standard modal",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_770(String browser) throws Exception {

    String tcId = "TC_770";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String adjustmentReason = testData.get("AdjustmentReason");
    String changePaymentPlan = testData.get("ChangePayment Plan");
    String changePaymentMethod = testData.get("ChangePayment Method");
    String paymentMethod = testData.get("Payment Method");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);

      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      custdashboardpage.selectBAfromManagePolicyTab(extentedReport, true);
      BillingAdjustmentPage billingAdjustmentPage =
          custdashboardpage.enterBADetails(testData, extentedReport, true);

      // Selecting adjustment reason
      billingAdjustmentPage.selectAdjustmentReason(adjustmentReason, extentedReport, true);
      billingAdjustmentPage.clickNext(extentedReport);

      // Selecting Payment method/Plan
      billingAdjustmentPage.selectPaymentPlan(changePaymentPlan, extentedReport);
      billingAdjustmentPage.selectPaymentMethod(changePaymentMethod, extentedReport, true);

      billingAdjustmentPage.clickCancelButton(extentedReport);

      billingAdjustmentPage.clickYesButtonInWarningDialog(extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer Dashboard after clicking Yes in warning dialog",
          "Could not navigated to Customer Dashboard after clicking Yes in warning dialog", driver,
          extentedReport, true);

      Log.softAssertThat(custdashboardpage.verifyPaymentMethod(paymentMethod),
          "changes are aborted when 'Yes' option is selected from the standard modal",
          "changes are aborted when 'Yes' option is selected from the standard modal", driver,
          extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "Check that the user returns to the Customer Dashboard and changes are aborted when 'NO' option is selected from the standard modal",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_771(String browser) throws Exception {

    String tcId = "TC_771";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String adjustmentReason = testData.get("AdjustmentReason");
    String changePaymentPlan = testData.get("ChangePayment Plan");
    String changePaymentMethod = testData.get("ChangePayment Method");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);

      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);


      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      custdashboardpage.selectBAfromManagePolicyTab(extentedReport, true);
      BillingAdjustmentPage billingAdjustmentPage =
          custdashboardpage.enterBADetails(testData, extentedReport, true);

      // Selecting adjustment reason
      billingAdjustmentPage.selectAdjustmentReason(adjustmentReason, extentedReport, true);
      billingAdjustmentPage.clickNext(extentedReport);

      // Selecting Payment method/Plan
      billingAdjustmentPage.selectPaymentPlan(changePaymentPlan, extentedReport);
      billingAdjustmentPage.selectPaymentMethod(changePaymentMethod, extentedReport, true);

      billingAdjustmentPage.clickCancelButton(extentedReport);

      billingAdjustmentPage.clickNoButtonInWarningDialog(extentedReport);

      Log.softAssertThat(
          !custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Not navigated to Customer Dashboard after clicking No in warning dialog",
          "Navigated to Customer Dashboard after clicking No in warning dialog", driver,
          extentedReport, true);

      Log.softAssertThat(billingAdjustmentPage.verifyPaymentMethod(changePaymentMethod),
          changePaymentMethod + " - Payment method is retained after clicking NO in warning dialog",
          changePaymentMethod
              + " - Payment method is not retained after clicking NO in warning dialog",
          driver, extentedReport, true);

      Log.softAssertThat(billingAdjustmentPage.verifyPaymentPlan(changePaymentPlan),
          changePaymentPlan + " - Payment plan is retained after clicking NO in warning dialog",
          changePaymentPlan + " - Payment plan is not retained after clicking NO in warning dialog",
          driver, extentedReport, true);


      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "To verify that the premium summary box is updated based on the selection of payment day",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_778(String browser) throws Exception {
    String tcId = "TC_778";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String paymentPlan = testData.get("ChangePayment Plan");
    String paymentMethod = testData.get("ChangePayment Method");
    String adjustmentReason = testData.get("AdjustmentReason");
    String preferredPaymentDay = testData.get("Preferred PaymentDay").toString();

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      newquotepage.clickAcceptbtn(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Custdashboard Page", "Could not navigate to custdashboard Page", driver,
          extentedReport, true);

      // Selecting billing adjustment
      custdashboardpage.selectBAfromManagePolicyTab(extentedReport, true);
      BillingAdjustmentPage billingAdjPage =
          custdashboardpage.enterBADetails(testData, extentedReport, true);

      // Selecting adjustment reason
      billingAdjPage.selectAdjustmentReason(adjustmentReason, extentedReport, true);
      billingAdjPage.clickNext(extentedReport);

      // Selecting Payment method/Plan
      billingAdjPage.selectPaymentPlan(paymentPlan, extentedReport);
      billingAdjPage.selectPaymentMethod(paymentMethod, extentedReport, true);
      billingAdjPage.addAccountDetail(testData, extentedReport, true);
      billingAdjPage.selectPreferredPaymentDay(preferredPaymentDay, extentedReport, true);

      String preferredPaymentDay1 = DateTimeUtility.getCurrentDate().split("/")[0];
      testData.put("Preferred Payment Day", preferredPaymentDay1);
      String[] columnsToVerify = testData.get("Payment Schedule Columns To Verify").split(",");
      for (int i = 0; i < columnsToVerify.length; i++) {
        String columnName = columnsToVerify[i];
        Log.softAssertThat(
            newquotepage.verifyPaymentScheduleColumn(columnName, testData, extentedReport, true),
            "Verified that column '" + columnName
                + "' of 'Payment Schedule' table has data as expected",
            "The column '" + columnName
                + "' of 'Payment Schedule' table does not have the expected data",
            driver, extentedReport, true);
      }

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(
      description = "Check that the payment day is disabled / read only mode when it cannot be changed",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_779(String browser) throws Exception {

    String tcId = "TC_779";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      SuspendBillingPage suspendBillingPage =
          custdashboardpage.selectSuspendBillingfromManagePolicy(extentedReport, true);

      Log.softAssertThat(!suspendBillingPage.isPaymentDayEditable(extentedReport),
          "The payment day is in disabled/read only mode",
          "The payment day is not in disabled/read only mode", driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "Check that the user is able to update the 'Bank Details' when the payment method is selected as 'Direct Debit'",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_780(String browser) throws Exception {

    String tcId = "TC_780";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String paymentPlan = testData.get("ChangePayment Plan");
    String paymentMethod = testData.get("ChangePayment Method");

    String adjustmentReason = testData.get("AdjustmentReason");

    String accountNumber = testData.get("Account Number").toString();
    String sortCode = testData.get("Sort Code").toString();

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      custdashboardpage = newquotepage.confirmPayment(extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Selecting billing adjustment
      custdashboardpage.selectBAfromManagePolicyTab(extentedReport, true);
      BillingAdjustmentPage billingAdjustmentPage =
          custdashboardpage.enterBADetails(testData, extentedReport, true);

      // Selecting adjustment reason
      billingAdjustmentPage.selectAdjustmentReason(adjustmentReason, extentedReport, true);
      billingAdjustmentPage.clickNext(extentedReport);

      // Selecting Payment method/Plan
      billingAdjustmentPage.selectPaymentPlan(paymentPlan, extentedReport);
      billingAdjustmentPage.selectPaymentMethod(paymentMethod, extentedReport, true);

      // Enter Account detail
      billingAdjustmentPage.clickAddAccountDetailButton(extentedReport);
      billingAdjustmentPage.enterAccountNoAndSortCode(accountNumber, sortCode, extentedReport,
          true);

      Log.softAssertThat(billingAdjustmentPage.VerifyAccountNoAndSortCode(accountNumber, sortCode),
          "The user is able to update the 'Bank Details' when the payment method is selected as 'Direct Debit'",
          "The user is not able to update the 'Bank Details' when the payment method is selected as 'Direct Debit'",
          driver, extentedReport, true);
      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "Check that the 'Account name' field in the Account Details section (during Direct Debit) is pre-populated with payor's name as <Title> <First Name> <Last Name>",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_781(String browser) throws Exception {

    String tcId = "TC_781";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String title = testData.get("Title");
    String firstName = testData.get("First Name");
    String lastName = testData.get("Last Name");

    String accountName = title + " " + firstName + " " + lastName;

    String paymentPlan = testData.get("ChangePayment Plan");
    String paymentMethod = testData.get("ChangePayment Method");

    String adjustmentReason = testData.get("AdjustmentReason");

    String accountNumber = testData.get("Account Number").toString();
    String sortCode = testData.get("Sort Code").toString();

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      custdashboardpage = newquotepage.confirmPayment(extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Selecting billing adjustment
      custdashboardpage.selectBAfromManagePolicyTab(extentedReport, true);
      BillingAdjustmentPage billingAdjustmentPage =
          custdashboardpage.enterBADetails(testData, extentedReport, true);

      // Selecting adjustment reason
      billingAdjustmentPage.selectAdjustmentReason(adjustmentReason, extentedReport, true);
      billingAdjustmentPage.clickNext(extentedReport);

      // Selecting Payment method/Plan
      billingAdjustmentPage.selectPaymentPlan(paymentPlan, extentedReport);
      billingAdjustmentPage.selectPaymentMethod(paymentMethod, extentedReport, true);

      // Enter Account detail
      billingAdjustmentPage.clickAddAccountDetailButton(extentedReport);
      billingAdjustmentPage.enterAccountNoAndSortCode(accountNumber, sortCode, extentedReport,
          true);

      Log.softAssertThat(billingAdjustmentPage.VerifyAccountName(accountName, extentedReport),
          "Account name field in the Account Details section during Direct Debit is pre-populated with payor's name as name as Title First Name Last Name : "
              + accountName,
          "Account name field in the Account Details section during Direct Debit is not pre-populated with payor's name as name as Title First Name Last Name : "
              + accountName,
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    }

    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "Check that the 'Account name' field  in the Account Details section (during Direct Debit) is editable",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_782(String browser) throws Exception {

    String tcId = "TC_782";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String paymentPlan = testData.get("ChangePayment Plan");
    String paymentMethod = testData.get("ChangePayment Method");

    String adjustmentReason = testData.get("AdjustmentReason");

    String accountNumber = testData.get("Account Number").toString();
    String sortCode = testData.get("Sort Code").toString();

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      custdashboardpage = newquotepage.confirmPayment(extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Selecting billing adjustment
      custdashboardpage.selectBAfromManagePolicyTab(extentedReport, true);
      BillingAdjustmentPage billingAdjustmentPage =
          custdashboardpage.enterBADetails(testData, extentedReport, true);

      // Selecting adjustment reason
      billingAdjustmentPage.selectAdjustmentReason(adjustmentReason, extentedReport, true);
      billingAdjustmentPage.clickNext(extentedReport);

      // Selecting Payment method/Plan
      billingAdjustmentPage.selectPaymentPlan(paymentPlan, extentedReport);
      billingAdjustmentPage.selectPaymentMethod(paymentMethod, extentedReport, true);

      // Enter Account detail
      billingAdjustmentPage.clickAddAccountDetailButton(extentedReport);
      billingAdjustmentPage.enterAccountNoAndSortCode(accountNumber, sortCode, extentedReport,
          true);

      // entering Account name
      String strAcctName = GenericUtils.getRandomCharacters("ALPHA", 5);
      billingAdjustmentPage.enterAccountName(strAcctName, extentedReport, true);

      Log.softAssertThat(billingAdjustmentPage.VerifyAccountName(strAcctName, extentedReport),
          "Account name field  in the Account Details section (during Direct Debit) is editable",
          "Account name field  in the Account Details section (during Direct Debit) is not editable",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "Check that the validation occurs when 'Account Number' and 'Sort Code' fields are entered and click on 'Check Account' button",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_783(String browser) throws Exception {

    String tcId = "TC_783";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String paymentPlan = testData.get("ChangePayment Plan");
    String paymentMethod = testData.get("ChangePayment Method");

    String adjustmentReason = testData.get("AdjustmentReason");

    String branchName = testData.get("Branch Name").toString();

    String accountNumber = testData.get("Account Number").toString();
    String sortCode = testData.get("Sort Code").toString();

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      custdashboardpage = newquotepage.confirmPayment(extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Custdashboard Page", "Could not navigate to custdashboard Page", driver,
          extentedReport, true);

      // Selecting billing adjustment
      custdashboardpage.selectBAfromManagePolicyTab(extentedReport, true);
      BillingAdjustmentPage billingAdjustmentPage =
          custdashboardpage.enterBADetails(testData, extentedReport, true);

      // Selecting adjustment reason
      billingAdjustmentPage.selectAdjustmentReason(adjustmentReason, extentedReport, true);
      billingAdjustmentPage.clickNext(extentedReport);

      // Selecting Payment method/Plan
      billingAdjustmentPage.selectPaymentPlan(paymentPlan, extentedReport);
      billingAdjustmentPage.selectPaymentMethod(paymentMethod, extentedReport, true);

      // Enter Account detail
      billingAdjustmentPage.clickAddAccountDetailButton(extentedReport);
      billingAdjustmentPage.enterAccountNoAndSortCode(accountNumber, sortCode, extentedReport,
          true);

      // Verify BranchName

      Log.softAssertThat(billingAdjustmentPage.VerifyBranchName(branchName),
          "Branch field is auto-populated when 'Account Number' and 'Sort Code' fields are entered and clicked on 'Check Account' button",
          "Branch field is not auto-populated when 'Account Number' and 'Sort Code' fields are entered and clicked on 'Check Account' button",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "Check that a message 'No card details are held for this Contact' is displayed on the Card details panel when 'Card' is selected as payment method",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_784(String browser) throws Exception {

    String tcId = "TC_784";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String adjustmentReason = testData.get("AdjustmentReason");
    String paymentPlan = testData.get("ChangePayment Plan");
    String paymentMethod = testData.get("ChangePayment Method");

    String CardDetailMsg = testData.get("CardDetail Message");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickBuy(extentedReport);

      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to custdashboardpage", "Could not Navigate to custdashboardpage", driver,
          extentedReport, true);

      custdashboardpage.selectBAfromManagePolicyTab(extentedReport, true);
      BillingAdjustmentPage billingAdjustmentPage =
          custdashboardpage.enterBADetails(testData, extentedReport, true);

      // Selecting adjustment reason
      billingAdjustmentPage.selectAdjustmentReason(adjustmentReason, extentedReport, true);
      billingAdjustmentPage.clickNext(extentedReport);

      // Selecting Payment method/Plan
      billingAdjustmentPage.selectPaymentPlan(paymentPlan, extentedReport);
      billingAdjustmentPage.selectPaymentMethod(paymentMethod, extentedReport, true);

      Log.softAssertThat(
          billingAdjustmentPage.uielement
              .verifyPageElements(Arrays.asList("panelCardDetailsection"), billingAdjustmentPage),
          "Card Details panel is displayed", "Card Details panel is not displayed", driver,
          extentedReport, true);


      Log.softAssertThat(billingAdjustmentPage.VerifyCardDetailMsg(CardDetailMsg),
          "A message 'No card details are held for this Contact' is displayed on the Card details panel",
          "A message 'No card details are held for this Contact' is not displayed on the Card details panel",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);


    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "Check that the user is able to select the 'Take Payment' button in the Card details panel in order to start the payment process",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_785(String browser) throws Exception {

    String tcId = "TC_785";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String adjustmentReason = testData.get("AdjustmentReason");
    String paymentPlan = testData.get("ChangePayment Plan");
    String paymentMethod = testData.get("ChangePayment Method");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickBuy(extentedReport);

      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to custdashboardpage", "Could not Navigate to custdashboardpage", driver,
          extentedReport, true);

      custdashboardpage.selectBAfromManagePolicyTab(extentedReport, true);
      BillingAdjustmentPage billingAdjustmentPage =
          custdashboardpage.enterBADetails(testData, extentedReport, true);

      // Selecting adjustment reason
      billingAdjustmentPage.selectAdjustmentReason(adjustmentReason, extentedReport, true);
      billingAdjustmentPage.clickNext(extentedReport);

      // Selecting Payment method/Plan
      billingAdjustmentPage.selectPaymentPlan(paymentPlan, extentedReport);
      billingAdjustmentPage.selectPaymentMethod(paymentMethod, extentedReport, true);

      Log.softAssertThat(
          billingAdjustmentPage.uielement
              .verifyPageElements(Arrays.asList("panelCardDetailsection"), billingAdjustmentPage),
          "Card Details panel is displayed", "Card Details panel is not displayed", driver,
          extentedReport, true);


      // Click TakePayment button
      billingAdjustmentPage.clickTakePaymentBtn(extentedReport);

      Log.softAssertThat(
          billingAdjustmentPage.uielement.verifyPageElements(Arrays.asList("frame_sagepay"),
              billingAdjustmentPage),
          "The user is able to select the 'Take Payment' button in the Card details panel in order to start the payment process",
          "The user is unable to select the 'Take Payment' button in the Card details panel in order to start the payment process",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "Check that the user is redirected to a separate web page that contains the Engagement Centre top banner (when payments are managed via payment hub)",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_788(String browser) throws Exception {

    String tcId = "TC_788";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String adjustmentReason = testData.get("AdjustmentReason");
    String paymentPlan = testData.get("ChangePayment Plan");
    String paymentMethod = testData.get("ChangePayment Method");

    String bannerTitle = testData.get("Banner Title");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickBuy(extentedReport);

      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to custdashboardpage", "Could not Navigate to custdashboardpage", driver,
          extentedReport, true);

      custdashboardpage.selectBAfromManagePolicyTab(extentedReport, true);
      BillingAdjustmentPage billingAdjustmentPage =
          custdashboardpage.enterBADetails(testData, extentedReport, true);

      // Selecting adjustment reason
      billingAdjustmentPage.selectAdjustmentReason(adjustmentReason, extentedReport, true);
      billingAdjustmentPage.clickNext(extentedReport);

      Log.softAssertThat(
          billingAdjustmentPage.uielement
              .verifyPageElements(Arrays.asList("panelCardDetailsection"), billingAdjustmentPage),
          "Card Details panel is displayed", "Card Details panel is not displayed", driver,
          extentedReport, true);


      // Selecting Payment method/Plan
      billingAdjustmentPage.selectPaymentPlan(paymentPlan, extentedReport);
      billingAdjustmentPage.selectPaymentMethod(paymentMethod, extentedReport, true);

      // Click TakePayment button
      billingAdjustmentPage.clickTakePaymentBtn(extentedReport);

      Log.softAssertThat(billingAdjustmentPage.VerifyEngCenterTitle(bannerTitle),
          "The user is redirected to a separate web page that contains the Engagement Centre top banner (when payments are managed via payment hub)",
          "The user is not redirected to a separate web page that contains the Engagement Centre top banner (when payments are managed via payment hub)",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "Check that the user is redirected to Acceptance page when 'Suspend Billing' option is selected from the Manage Policy drop-down",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_805(String browser) throws Exception {

    String tcId = "TC_805";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickBuy(extentedReport);

      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to custdashboardpage", "Could not Navigate to custdashboardpage", driver,
          extentedReport, true);

      // Select Suspend billing
      SuspendBillingPage suspendBillingPage =
          custdashboardpage.selectSuspendBillingfromManagePolicy(extentedReport, true);

      Log.softAssertThat(suspendBillingPage.isSuspendBillingSelected(),
          "Navigated to Acceptance Page", "Could not navigated to Acceptance Page", driver,
          extentedReport, true);

      Log.softAssertThat(
          suspendBillingPage.uielement.verifyPageElements(Arrays.asList("titleAcceptance"),
              suspendBillingPage),
          "The user is redirected to Acceptance page when 'Suspend Billing' option is selected from the Manage Policy drop-down",
          "The user is not redirected to Acceptance page when 'Suspend Billing' option is selected from the Manage Policy drop-down",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "Check that the user is NOT able to select any fields other than 'Reason to Suspend Billing' drop-down in the acceptance page when navigated via suspend billing option",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_810(String browser) throws Exception {

    String tcId = "TC_810";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String suspendBillingReason = testData.get("SuspendBilling Reason");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickBuy(extentedReport);

      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to custdashboardpage", "Could not Navigate to custdashboardpage", driver,
          extentedReport, true);

      // Select Suspend billing
      SuspendBillingPage suspendBillingPage =
          custdashboardpage.selectSuspendBillingfromManagePolicy(extentedReport, true);
      suspendBillingPage.selectSuspendBillingReason(suspendBillingReason, extentedReport, true);

      Log.softAssertThat(suspendBillingPage.verifySuspendBillingReason(suspendBillingReason),
          "The user is able to select Reason to Suspend Billing drop-down - "
              + suspendBillingReason,
          "The user is not able to select Reason to Suspend Billing drop-down - "
              + suspendBillingReason,
          driver, extentedReport, true);


      Log.softAssertThat(!suspendBillingPage.isPaymentDayEditable(extentedReport),
          "The user is not able to select any fields other than Reason to Suspend Billing drop-down in the acceptance page when navigated via suspend billing option",
          "The user is able to select any fields other than Reason to Suspend Billing drop-down in the acceptance page when navigated via suspend billing option",
          driver, extentedReport, true);


      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "Check that the suspend billing process is aborted when click on 'Cancel' button on the Acceptance page",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_814(String browser) throws Exception {

    String tcId = "TC_814";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickBuy(extentedReport);

      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to custdashboardpage", "Could not Navigate to custdashboardpage", driver,
          extentedReport, true);

      // Select Suspend billing
      SuspendBillingPage suspendBillingPage =
          custdashboardpage.selectSuspendBillingfromManagePolicy(extentedReport, true);

      // Click cancel button
      suspendBillingPage.clickCancelButton(extentedReport);
      suspendBillingPage.clickYesButtonInWarningDialog(extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer Dashboard after clicking Yes in warning dialog",
          "Could not navigated to Customer Dashboard after clicking Yes in warning dialog", driver,
          extentedReport, true);

      Log.softAssertThat(!custdashboardpage.verifySuspendBilling_Accepted(),
          "The suspend billing process is aborted when click on 'Cancel' button on the Acceptance page",
          "The suspend billing process is not aborted when click on 'Cancel' button on the Acceptance page",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "Check that the 'Payment Schedule Table' is displayed on the Acceptance page when Payment method is selected as 'Direct Debit'",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_824(String browser) throws Exception {

    String tcId = "TC_824";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String suspendBillingReason = testData.get("SuspendBilling Reason");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      SuspendBillingPage suspendBillingPage =
          custdashboardpage.selectSuspendBillingfromManagePolicy(extentedReport, true);

      suspendBillingPage.completeSuspendBilling(suspendBillingReason, extentedReport, true);

      Log.softAssertThat(custdashboardpage.verifySuspendBilling_Accepted(),
          "Suspend Billing is accepted", "Suspend Billing is not accepted", driver, extentedReport,
          true);

      // Select unsuspend billing
      UnsuspendBillingPage unsuspendBillingPage =
          custdashboardpage.clickUnsuspendBillingfromManagePolicy(extentedReport, true);

      /*
       * //Select Payment Plan & method buyQuotePage.selectPaymentPlan(paymentPlan, extentedReport);
       * buyQuotePage.selectPaymentMethod(paymentMethod, extentedReport, true);
       */
      Log.softAssertThat(
          unsuspendBillingPage.uielement.verifyPageElements(Arrays.asList("panelPaymentSchedule"),
              unsuspendBillingPage),
          "The 'Payment Schedule Table' is displayed on the Acceptance page when Payment method is selected as 'Direct Debit'",
          "The 'Payment Schedule Table' is not displayed on the Acceptance page when Payment method is selected as 'Direct Debit'",
          driver, extentedReport, true);


      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "Check that the 'Direct Debit Guarantee' section and its checkbox is displayed on the Acceptance page when Payment method is selected as 'Direct Debit'",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_823(String browser) throws Exception {

    String tcId = "TC_823";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String suspendBillingReason = testData.get("SuspendBilling Reason");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      SuspendBillingPage suspendBillingPage =
          custdashboardpage.selectSuspendBillingfromManagePolicy(extentedReport, true);

      suspendBillingPage.completeSuspendBilling(suspendBillingReason, extentedReport, true);

      Log.softAssertThat(custdashboardpage.verifySuspendBilling_Accepted(),
          "Suspend Billing is accepted", "Suspend Billing is not accepted", driver, extentedReport,
          true);

      // Select unsuspend billing
      UnsuspendBillingPage unsuspendBillingPage =
          custdashboardpage.clickUnsuspendBillingfromManagePolicy(extentedReport, true);

      /*
       * //Select Payment Plan & method buyQuotePage.selectPaymentPlan(paymentPlan, extentedReport);
       * buyQuotePage.selectPaymentMethod(paymentMethod, extentedReport, true);
       */

      Log.softAssertThat(
          unsuspendBillingPage.uielement
              .verifyPageElements(Arrays.asList("panelDirectDebitGuarantee"), unsuspendBillingPage),
          "The 'Direct Debit Guarantee' section is displayed on the Acceptance page when Payment method is selected as 'Direct Debit'",
          "The 'Direct Debit Guarantee' section is not displayed on the Acceptance page when Payment method is selected as 'Direct Debit'",
          driver, extentedReport, true);

      Log.softAssertThat(
          unsuspendBillingPage.uielement.verifyPageElements(Arrays.asList("checkbox_CustAgree"),
              unsuspendBillingPage),
          "The 'Direct Debit Guarantee' checkbox is displayed on the Acceptance page when Payment method is selected as 'Direct Debit'",
          "The 'Direct Debit Guarantee' checkbox is not displayed on the Acceptance page when Payment method is selected as 'Direct Debit'",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(
      description = "Check that the following fields are editable in the Acceptance page when the user navigates via Unsuspend billing - Payment Plan,Payment Method,Payor,Preferred Payment Day (when it is via Direct Debit)",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_821(String browser) throws Exception {

    String tcId = "TC_821";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String paymentPlan = testData.get("Payment Plan");
    String paymentMethod = testData.get("Payment Method");
    String preferredDay = testData.get("Preferred PaymentDay");

    String suspendBillingReason = testData.get("SuspendBilling Reason");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String changePayorfirstName =
        GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String changePayorlastName =
        GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");

    testData2.put("First Name", changePayorfirstName);
    testData2.put("Last Name", changePayorlastName);
    String postCode = testData2.get("Post Code");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Create Customer1 for change payor
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      SuspendBillingPage suspendBillingPage =
          custdashboardpage.selectSuspendBillingfromManagePolicy(extentedReport, true);

      suspendBillingPage.completeSuspendBilling(suspendBillingReason, extentedReport, true);

      Log.softAssertThat(custdashboardpage.verifySuspendBilling_Accepted(),
          "Suspend Billing is accepted", "Suspend Billing is not accepted", driver, extentedReport,
          true);

      // Select unsuspend billing
      UnsuspendBillingPage unsuspendBillingPage =
          custdashboardpage.clickUnsuspendBillingfromManagePolicy(extentedReport, true);

      // Verify Payment Plan
      unsuspendBillingPage.selectPaymentPlan(paymentPlan, extentedReport);

      Log.softAssertThat(unsuspendBillingPage.verifyPaymentPlan(paymentPlan),
          "In the Acceptance page when the user navigates via Unsuspend billing Payment Plan is editable",
          "In the Acceptance page when the user navigates via Unsuspend billing Payment Plan is not editable",
          driver, extentedReport, true);

      // Verify Payment method
      unsuspendBillingPage.selectPaymentMethod(paymentMethod, extentedReport, false);

      Log.softAssertThat(unsuspendBillingPage.verifyPaymentMethod(paymentMethod),
          "In the Acceptance page when the user navigates via Unsuspend billing Payment method is editable",
          "In the Acceptance page when the user navigates via Unsuspend billing Payment method is not editable",
          driver, extentedReport, true);


      // Verify Preferredpayment day
      unsuspendBillingPage.selectPreferredPaymentDay(preferredDay, extentedReport, false);
      Log.softAssertThat(unsuspendBillingPage.verifyPreferredPaymentDay(preferredDay),
          "In the Acceptance page when the user navigates via Unsuspend billing Prefered payment day is editable",
          "In the Acceptance page when the user navigates via Unsuspend billing Prefered payment day is not editable",
          driver, extentedReport, true);


      unsuspendBillingPage.clickChangePayorButton(extentedReport, false);
      unsuspendBillingPage.enterChangePayorDetails(changePayorlastName, changePayorfirstName,
          postCode, extentedReport, true);
      String payorName = changePayorfirstName + " " + changePayorlastName;

      Log.softAssertThat(unsuspendBillingPage.verifyPayorName(payorName),
          "In the Acceptance page when the user navigates via Unsuspend billing Payor is editable",
          "In the Acceptance page when the user navigates via Unsuspend billing Payor is not editable",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "To verify the user should navigate to acceptance page after performing unsuspended",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_818(String browser) throws Exception {

    String tcId = "TC_818";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String suspendBillingReason = testData.get("SuspendBilling Reason");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      SuspendBillingPage suspendBillingPage =
          custdashboardpage.selectSuspendBillingfromManagePolicy(extentedReport, true);

      suspendBillingPage.completeSuspendBilling(suspendBillingReason, extentedReport, true);

      Log.softAssertThat(custdashboardpage.verifySuspendBilling_Accepted(),
          "User is able to complete the Suspend Billing after clicking Accept button",
          "User is unable to complete the Suspend Billing after clicking Accept button", driver,
          extentedReport, true);

      UnsuspendBillingPage unsuspendBillingPage =
          custdashboardpage.clickUnsuspendBillingfromManagePolicy(extentedReport, true);

      Log.softAssertThat(
          unsuspendBillingPage.uielement.verifyPageElements(Arrays.asList("titleAcceptance"),
              unsuspendBillingPage),
          "Acceptance Page Verified", "Acceptance Page not Verified", driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(description = "To Verify the premium banner on the Acceptance page via suspend billing",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_809(String browser) throws Exception {

    String tcId = "TC_809";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();


      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      SuspendBillingPage suspendBillingPage =
          custdashboardpage.selectSuspendBillingfromManagePolicy(extentedReport, true);
      Log.softAssertThat(
          suspendBillingPage.uielement
              .verifyPageElements(Arrays.asList("totalPaymentInPremiumBanner"), suspendBillingPage),
          "Total Payment Element is available in Premium Banner",
          "Total Payment Element is not available in Premium Banner", driver, extentedReport, true);
      Log.softAssertThat(
          suspendBillingPage.uielement
              .verifyPageElements(Arrays.asList("amountToPayInPremiumBanner"), suspendBillingPage),
          "Amount to Pay Element is available in Premium Banner",
          "Amount to Pay Element is not available in Premium Banner", driver, extentedReport, true);
      Log.softAssertThat(
          suspendBillingPage.uielement.verifyPageElements(
              Arrays.asList("firstPaymentOfInPremiumBanner"), suspendBillingPage),
          "1st Payment of Element is available in Premium Banner",
          "1st Payment of Element is not available in Premium Banner", driver, extentedReport,
          true);
      Log.softAssertThat(
          suspendBillingPage.uielement.verifyPageElements(
              Arrays.asList("preferredPaymentDayInPremiumBanner"), suspendBillingPage),
          "Preferred Payment Day Element is available in Premium Banner",
          "Preferred Payment Day Element is not available in Premium Banner", driver,
          extentedReport, true);
      Log.softAssertThat(
          suspendBillingPage.uielement.verifyPageElements(
              Arrays.asList("includesFeesOfInPremiumBanner"), suspendBillingPage),
          "Include Fees Of Element is available in Premium Banner",
          "Include Fees Of Element is not available in Premium Banner", driver, extentedReport,
          true);
      Log.softAssertThat(
          suspendBillingPage.uielement.verifyPageElements(
              Arrays.asList("followedByInstallementsInPremiumBanner"), suspendBillingPage),
          "Followed by 11 Installments of Element is available in Premium Banner",
          "Followed by 11 Installments of Element is not available in Premium Banner", driver,
          extentedReport, true);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } //
  }

  @Test(
      description = "Check that the user is redirected to the Customer Dashboard on clicking the 'Customer Dashboard' navigation stepper from the Acceptance page",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_795(String browser) throws Exception {

    String tcId = "TC_795";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      // Select suspend billing
      SuspendBillingPage suspendBillingPage =
          custdashboardpage.selectSuspendBillingfromManagePolicy(extentedReport, true);
      suspendBillingPage.clickCustomerDashboardTab(extentedReport, true);

      suspendBillingPage.clickYesButtonInWarningDialog(extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Redirected to the Customer Dashboard on clicking the 'Customer Dashboard' navigation stepper from the Acceptance page",
          "Could not redirected to the Customer Dashboard on clicking the 'Customer Dashboard' navigation stepper from the Acceptance paged",
          driver, extentedReport, true);


      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "Check that the following details of the customer are pre-populated within the PSP form (when payments are managed via payment hub) - Amount to Pay, Cardholder Name-Title,First Name and Last Name, Address-Contact�s address",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_789(String browser) throws Exception {

    String tcId = "TC_789";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    HashMap<String, String> customerDetails = new HashMap<String, String>();

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String adjustmentReason = testData.get("AdjustmentReason");
    String paymentPlan = testData.get("ChangePayment Plan");
    String paymentMethod = testData.get("ChangePayment Method");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Create Customer
      searchPage.clickCreateCustomer(true, extentedReport);
      customerDetails = searchPage.enterCustomerDetails(testData, true, extentedReport);

      String firstName = customerDetails.get("First Name");
      String lastName = customerDetails.get("Last Name");

      String customerName = firstName + " " + lastName;

      String[] customerAddress = {customerDetails.get("Address1"), customerDetails.get("Address2"),
          customerDetails.get("Address3"), customerDetails.get("Postcode")};

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      // Selecting billing adjustment
      custdashboardpage.selectBAfromManagePolicyTab(extentedReport, true);
      BillingAdjustmentPage billingAdjustmentPage =
          custdashboardpage.enterBADetails(testData, extentedReport, true);

      // Selecting adjustment reason
      billingAdjustmentPage.selectAdjustmentReason(adjustmentReason, extentedReport, true);
      billingAdjustmentPage.clickNext(extentedReport);

      // Selecting Payment method/Plan
      billingAdjustmentPage.selectPaymentPlan(paymentPlan, extentedReport);
      billingAdjustmentPage.selectPaymentMethod(paymentMethod, extentedReport, true);

      String amountToPay = billingAdjustmentPage.getAmountToPay(extentedReport, true);

      billingAdjustmentPage.clickTakePaymentBtn(extentedReport);

      // Verify Customer Address
      Log.softAssertThat(
          billingAdjustmentPage.verifyCustomerAddress(customerAddress, extentedReport, true),
          "Address - Contact�s address is pre-populated within the PSP form",
          "Address - Contact�s address is not pre-populated within the PSP form", driver,
          extentedReport, true);

      // Verify amount to pay
      Log.softAssertThat(billingAdjustmentPage.verifyAmountToPay(amountToPay),
          "Amount to Pay is pre-populated within the PSP form",
          "Amount to Pay is not pre-populated within the PSP form", driver, extentedReport, true);

      // Verify Cardholder Name name
      Log.softAssertThat(billingAdjustmentPage.verifyCardHolderName(customerName),
          "Cardholder Name is pre-populated within the PSP form",
          "Cardholder Name is not pre-populated within the PSP form", driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "Check that 'Premium Summary Banner' is displayed  when the acceptance page is via unsuspend billing",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_820(String browser) throws Exception {

    String tcId = "TC_820";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String suspendBillingReason = testData.get("SuspendBilling Reason");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      SuspendBillingPage suspendBillingPage =
          custdashboardpage.selectSuspendBillingfromManagePolicy(extentedReport, true);

      suspendBillingPage.completeSuspendBilling(suspendBillingReason, extentedReport, true);

      Log.softAssertThat(custdashboardpage.verifySuspendBilling_Accepted(),
          "User is able to complete the Suspend Billing after clicking Accept button",
          "User is unable to complete the Suspend Billing after clicking Accept button", driver,
          extentedReport, true);

      UnsuspendBillingPage unsuspendBillingPage =
          custdashboardpage.clickUnsuspendBillingfromManagePolicy(extentedReport, true);

      Log.softAssertThat(
          unsuspendBillingPage.uielement.verifyPageElements(Arrays.asList("titleAcceptance"),
              unsuspendBillingPage),
          "User is navigated to Acceptance Page", "User is not navigated to Acceptance Page",
          driver, extentedReport, true);

      Log.softAssertThat(
          unsuspendBillingPage.uielement.verifyPageElements(
              Arrays.asList("totalPaymentInPremiumBanner"), unsuspendBillingPage),
          "Total Payment Element is available in Premium Banner",
          "Total Payment Element is not available in Premium Banner", driver, extentedReport, true);

      Log.softAssertThat(
          unsuspendBillingPage.uielement.verifyPageElements(
              Arrays.asList("amountToPayInPremiumBanner"), unsuspendBillingPage),
          "Amount to Pay Element is available in Premium Banner",
          "Amount to Pay Element is not available in Premium Banner", driver, extentedReport, true);

      Log.softAssertThat(
          unsuspendBillingPage.uielement.verifyPageElements(
              Arrays.asList("firstPaymentOfInPremiumBanner"), unsuspendBillingPage),
          "1st Payment of Element is available in Premium Banner",
          "1st Payment of Element is not available in Premium Banner", driver, extentedReport,
          true);

      Log.softAssertThat(
          unsuspendBillingPage.uielement.verifyPageElements(
              Arrays.asList("preferredPaymentDayInPremiumBanner"), unsuspendBillingPage),
          "Preferred Payment Day Element is available in Premium Banner",
          "Preferred Payment Day Element is not available in Premium Banner", driver,
          extentedReport, true);

      Log.softAssertThat(
          unsuspendBillingPage.uielement.verifyPageElements(
              Arrays.asList("includesFeesOfInPremiumBanner"), unsuspendBillingPage),
          "Include Fees Of Element is available in Premium Banner",
          "Include Fees Of Element is not available in Premium Banner", driver, extentedReport,
          true);

      Log.softAssertThat(
          unsuspendBillingPage.uielement.verifyPageElements(
              Arrays.asList("followedByInstallementsInPremiumBanner"), unsuspendBillingPage),
          "Followed by 11 Installments of Element is available in Premium Banner",
          "Followed by 11 Installments of Element is not available in Premium Banner", driver,
          extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "Check that based on the selection of 'Payment method (Annual card)' type, the information in the payment panel is displayed",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_768(String browser) throws Exception {

    String tcId = "TC_768";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String paymentPlan = testData.get("Payment Plan");
    String paymentMethod = testData.get("Payment Method");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);

      // Select Payment plan & method
      newquotepage.selectPaymentPlan(paymentPlan, extentedReport);
      newquotepage.selectPaymentMethod(paymentMethod.toString(), extentedReport);

      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("panelCardDetailSection"),
              newquotepage),
          "Card Details section is displayed in the payment panel",
          "Card Details section is not displayed in the payment panel", driver, extentedReport,
          true);

      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("btnTakePayment"), newquotepage),
          "TakePayment button is displayed on selection of Payment method (Annual card) in the payment panel",
          "TakePayment button is not displayed on selection of Payment method (Annual card) in the payment panel",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(groups = "Business_Scenario",
      description = "Unsuspend Billing on a Policy and change Payment Plan and Payment Method to Annual Card and Change Payor",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_018(String browser) throws Exception {

    String tcId = "BS_018";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");

    String changePaymentPlan = testData.get("ChangePayment Plan");
    String changePaymentMethod = testData.get("ChangePayment Method");

    String suspendBillingReason = testData.get("SuspendBilling Reason");

    String changePayorfirstName =
        GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String changePayorlastName =
        GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    testData2.put("First Name", changePayorfirstName);
    testData2.put("Last Name", changePayorlastName);
    String postCode = testData2.get("Post Code");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Create Customer1 for change payor
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      SuspendBillingPage suspendBillingPage =
          custdashboardpage.selectSuspendBillingfromManagePolicy(extentedReport, true);

      suspendBillingPage.completeSuspendBilling(suspendBillingReason, extentedReport, true);

      Log.softAssertThat(custdashboardpage.verifySuspendBilling_Accepted(),
          "Suspend Billing is accepted", "Suspend Billing is not accepted", driver, extentedReport,
          true);


      UnsuspendBillingPage unsuspendBillingPage =
          custdashboardpage.clickUnsuspendBillingfromManagePolicy(extentedReport, true);

      // Select Payment Plan & method
      unsuspendBillingPage.selectPaymentPlan(changePaymentPlan, extentedReport);
      unsuspendBillingPage.selectPaymentMethod(changePaymentMethod, extentedReport, true);

      unsuspendBillingPage.clickChangePayorButton(extentedReport, true);

      String payorName = unsuspendBillingPage.enterChangePayorDetails(changePayorlastName,
          changePayorfirstName, postCode, extentedReport, true);

      Log.softAssertThat(unsuspendBillingPage.verifyPayorName(payorName),
          "User is able to change payor by selecting 'Change Payor' button from the payment panel",
          "User is unable to change payor by selecting 'Change Payor' button from the payment panel",
          driver, extentedReport, true);
      unsuspendBillingPage.tickSpokenDirectlyNewPayorCheckbox(extentedReport);

      unsuspendBillingPage.clickTakePaymentBtn(extentedReport);
      unsuspendBillingPage.selectVisacard(extentedReport);

      // Enter Card Details
      unsuspendBillingPage.enterCardNumber(testData, extentedReport, true);
      unsuspendBillingPage.selectExpiry(testData, extentedReport, true);
      unsuspendBillingPage.enterVerification(extentedReport, true);
      unsuspendBillingPage.enterName(testData, extentedReport, true);
      unsuspendBillingPage.clickbuy(extentedReport, true);

      // Click continue button
      unsuspendBillingPage.clickContinueButton(extentedReport);

      // Click Confirm button
      unsuspendBillingPage.clickConfirmBtn(extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Unsuspend billing process is completed after changing payment plan & method and change Payor",
          "Unsuspend billing process is not completed after changing payment plan & method and change Payor",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(groups = "Business_Scenario",
      description = "Unsuspend Billing on a Policy and change Payment Plan and Payment Method to Annual Card and take payment",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_017(String browser) throws Exception {

    String tcId = "BS_017";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String changePaymentPlan = testData.get("ChangePayment Plan");
    String changePaymentMethod = testData.get("ChangePayment Method");

    String suspendBillingReason = testData.get("SuspendBilling Reason");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      SuspendBillingPage suspendBillingPage =
          custdashboardpage.selectSuspendBillingfromManagePolicy(extentedReport, true);

      suspendBillingPage.completeSuspendBilling(suspendBillingReason, extentedReport, true);

      Log.softAssertThat(custdashboardpage.verifySuspendBilling_Accepted(),
          "Suspend Billing is accepted", "Suspend Billing is not accepted", driver, extentedReport,
          true);


      UnsuspendBillingPage unsuspendBillingPage =
          custdashboardpage.clickUnsuspendBillingfromManagePolicy(extentedReport, true);

      // Select Payment Plan & method
      unsuspendBillingPage.selectPaymentPlan(changePaymentPlan, extentedReport);
      unsuspendBillingPage.selectPaymentMethod(changePaymentMethod, extentedReport, true);

      unsuspendBillingPage.clickTakePaymentBtn(extentedReport);
      unsuspendBillingPage.selectVisacard(extentedReport);

      // Enter Card Details
      unsuspendBillingPage.enterCardNumber(testData, extentedReport, true);
      unsuspendBillingPage.selectExpiry(testData, extentedReport, true);
      unsuspendBillingPage.enterVerification(extentedReport, true);
      unsuspendBillingPage.enterName(testData, extentedReport, true);
      unsuspendBillingPage.clickbuy(extentedReport, true);

      // Click continue button
      unsuspendBillingPage.clickContinueButton(extentedReport);

      // Click Confirm button
      unsuspendBillingPage.clickConfirmBtn(extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Unsuspend billing process is completed after changing payment plan & method ",
          "Unsuspend billing process is not completed after changing payment plan & method ",
          driver, extentedReport, true);


      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(groups = "Business_Scenario",
      description = "Unsuspend Billing on a Policy and reinstate Direct Debit payments but change the Payor",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_016(String browser) throws Exception {

    String tcId = "BS_016";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");

    String suspendBillingReason = testData.get("SuspendBilling Reason");

    String changePayorfirstName =
        GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String changePayorlastName =
        GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    testData2.put("First Name", changePayorfirstName);
    testData2.put("Last Name", changePayorlastName);
    String postCode = testData2.get("Post Code");

    String branch = testData.get("Bank Branch");
    String accNo = testData.get("Account Number");
    String sortCode = testData.get("Sort Code");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Create Customer1 for change payor
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      SuspendBillingPage suspendBillingPage =
          custdashboardpage.selectSuspendBillingfromManagePolicy(extentedReport, true);

      suspendBillingPage.completeSuspendBilling(suspendBillingReason, extentedReport, true);

      Log.softAssertThat(custdashboardpage.verifySuspendBilling_Accepted(),
          "Suspend Billing is accepted", "Suspend Billing is not accepted", driver, extentedReport,
          true);


      UnsuspendBillingPage unsuspendBillingPage =
          custdashboardpage.clickUnsuspendBillingfromManagePolicy(extentedReport, true);

      // Change payor
      unsuspendBillingPage.clickChangePayorButton(extentedReport, true);
      String payorName = unsuspendBillingPage.enterChangePayorDetails(changePayorlastName,
          changePayorfirstName, postCode, extentedReport, true);
      Log.softAssertThat(unsuspendBillingPage.verifyPayorName(payorName),
          "User is able to change payor by selecting 'Change Payor' button from the payment panel",
          "User is unable to change payor by selecting 'Change Payor' button from the payment panel",
          driver, extentedReport, true);
      unsuspendBillingPage.tickSpokenDirectlyNewPayorCheckbox(extentedReport);

      // Add new Account
      unsuspendBillingPage.clickAddAccountDetails(extentedReport);
      unsuspendBillingPage.checkBankAccount(accNo, sortCode, extentedReport);

      String newAccName = GenericUtils.getRandomCharacters("alpha", 5);
      unsuspendBillingPage.enterAccountName(newAccName, extentedReport);
      unsuspendBillingPage.saveAccountDetails(extentedReport);
      unsuspendBillingPage.selectAccount(sortCode, branch, newAccName, accNo, extentedReport, true);

      Log.softAssertThat(
          unsuspendBillingPage.verifyAccountSelection(sortCode, branch, newAccName, accNo,
              extentedReport),
          "New Account detail is added and selected",
          "New Account detail is not added and not selected", driver, extentedReport, true);

      unsuspendBillingPage.tickCustomerAgreesCheckbox(extentedReport);

      custdashboardpage = unsuspendBillingPage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Unsuspend Billing on a Policy is completed after reinstated Direct Debit payments but change the Payor",
          "Unsuspend Billing on a Policy is completed after reinstated Direct Debit payments but change the Payor",
          driver, extentedReport, true);


      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(groups = "Business_Scenario",
      description = "Unsuspend Billing on a Policy and reinstate Direct Debit payments",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_015(String browser) throws Exception {


    String tcId = "BS_015";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");

    String suspendBillingReason = testData.get("SuspendBilling Reason");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Create Customer1 for change payor
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      SuspendBillingPage suspendBillingPage =
          custdashboardpage.selectSuspendBillingfromManagePolicy(extentedReport, true);

      suspendBillingPage.completeSuspendBilling(suspendBillingReason, extentedReport, true);

      Log.softAssertThat(custdashboardpage.verifySuspendBilling_Accepted(),
          "Suspend Billing is accepted", "Suspend Billing is not accepted", driver, extentedReport,
          true);


      UnsuspendBillingPage unsuspendBillingPage =
          custdashboardpage.clickUnsuspendBillingfromManagePolicy(extentedReport, true);

      unsuspendBillingPage.tickCustomerAgreesCheckbox(extentedReport);

      custdashboardpage = unsuspendBillingPage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to custdashboardpage", "Could not Navigate to custdashboardpage", driver,
          extentedReport, true);

      Log.softAssertThat(custdashboardpage.verifyUnsuspendBilling_Accepted(),
          "Unsuspend Billing on a Policy is completed with reinstate Direct Debit payments",
          "Unsuspend Billing on a Policy is not completed with reinstate Direct Debit payments",
          driver, extentedReport, true);


      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(groups = "Business_Scenario",
      description = "Change the Preferred Payment Day for a Direct Debit at Renewal once in the Renewal Cycle",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_010(String browser) throws Exception {

    String tcId = "BS_010";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");
    String preferredPaymentDay = testData.get("Preferred PaymentDay");

    String branch = testData.get("Bank Branch");
    String accNo = testData.get("Account Number");
    String sortCode = testData.get("Sort Code");


    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(
          homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page UserName : "
              + userName + ", Password : " + password,
          "Not logged in to Home page", driver, extentedReport, true);

      // Create customer
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> contactDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport, true);
      String custFirstName = contactDetails.get("First Name");
      String custLastName = contactDetails.get("Last Name");

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // for renewal quote


      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      Log.softAssertThat(newquotepage.verifyNewQuotePage(true, extentedReport),
          "Verified NewQuotePage ", "Not verified NewQuotePage", driver, extentedReport, true);
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      // Get and Buy Quote
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickBuy(extentedReport);

      // Select Payment
      String accName = GenericUtils.getRandomCharacters("alpha", 5);
      testData.put("Account Name", accName);
      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to customer dashboard page", "Not navigated to customer dashboard page",
          driver, extentedReport, true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.message("NB Policy Created Successfully in accepted status", driver, extentedReport,
            true);
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
            driver, extentedReport, true);
      }

      // Add search for the existing IP contact
      custdashboardpage.clickComplete(extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Search with Valid user names
      searchPage.enterLastName(custLastName, extentedReport);
      searchPage.enterFirstName(custFirstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(custLastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

      // Verifying Customer Details
      custdashboardpage.clickPassVerification(extentedReport, true);
      custdashboardpage.verifyCustomerDashboardPage();

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("drpManageRenewals"),
              custdashboardpage),
          "Manage renewal tab is dispalyed on customer dashaboard page",
          "Manage renewal tab is not dispalying on customer dashboard page");

      custdashboardpage.performRenewals(extentedReport, true);
      custdashboardpage.clickContinueWarningMsg(extentedReport, true);

      newquotepage.clickNextOne(extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickRenewalQuoteBuy(extentedReport);

      newquotepage.selectPreferredPaymentDay(preferredPaymentDay, extentedReport, true);

      newquotepage.selectAccount(sortCode, branch, accName, accNo, extentedReport, true);
      newquotepage.verifyAccountSelection(sortCode, branch, accName, accNo, extentedReport);
      newquotepage.tickCustomerAgreesCheckbox(extentedReport);

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      // check for policy status
      HashMap<String, String> policyDetail = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetail.get("Status").toString().equalsIgnoreCase("Accepted")
          && policyDetails.get("Position").toString().equalsIgnoreCase("Renewal")) {
        Log.message("User able to change the preferred payment day in renewal quote variation ",
            driver, extentedReport, true);
      } else {
        Log.fail(
            "Failed - User unable to change the preferred payment day in renewal quote variation",
            driver, extentedReport, true);
      }

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      // driver.close();
    } // finally
  }

  @Test(groups = "Business_Scenario",
      description = "Change an existing Payment Plan and Payment Method from Direct Debit to Annual Card at Renewal once in the Renewal Cycle",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_003(String browser) throws Exception {

    String tcId = "BS_003";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String changePaymentPlan = testData.get("ChangePayment Plan");
    String changePaymentMethod = testData.get("ChangePayment Method");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(
          homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page UserName : "
              + userName + ", Password : " + password,
          "Not logged in to Home page", driver, extentedReport, true);

      // Create customer
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> contactDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport, true);
      String custFirstName = contactDetails.get("First Name");
      String custLastName = contactDetails.get("Last Name");

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // for renewal quote


      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      Log.softAssertThat(newquotepage.verifyNewQuotePage(true, extentedReport),
          "Verified NewQuotePage ", "Not verified NewQuotePage", driver, extentedReport, true);
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      // Get and Buy Quote
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickBuy(extentedReport);

      // Select Payment
      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to customer dashboard page", "Not navigated to customer dashboard page",
          driver, extentedReport, true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.message("NB Policy Created Successfully in accepted status", driver, extentedReport,
            true);
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
            driver, extentedReport, true);
      }

      // Add search for the existing IP contact
      custdashboardpage.clickComplete(extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Search with Valid user names
      searchPage.enterLastName(custLastName, extentedReport);
      searchPage.enterFirstName(custFirstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(custLastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

      // Verifying Customer Details
      custdashboardpage.clickPassVerification(extentedReport, true);
      custdashboardpage.verifyCustomerDashboardPage();

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("drpManageRenewals"),
              custdashboardpage),
          "Manage renewal tab is dispalyed on customer dashaboard page",
          "Manage renewal tab is not dispalying on customer dashboard page");

      custdashboardpage.performRenewals(extentedReport, true);
      custdashboardpage.clickContinueWarningMsg(extentedReport, true);

      newquotepage.clickNextOne(extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickRenewalQuoteBuy(extentedReport);

      // Payment
      newquotepage.selectPaymentPlan(changePaymentPlan, extentedReport);
      newquotepage.selectPaymentMethod(changePaymentMethod, extentedReport);

      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("paymentReferrence"),
              newquotepage),
          "Payment Referrence Field verified", "Payment Referrence Field Not Verified", driver,
          extentedReport, true);

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      // check for policy status
      HashMap<String, String> policyDetail = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetail.get("Status").toString().equalsIgnoreCase("Accepted")
          && policyDetail.get("Position").toString().equalsIgnoreCase("Renewal")) {
        Log.message("User able to change the preferred payment day in renewal quote variation ",
            driver, extentedReport, true);
      } else {
        Log.fail(
            "Failed - User unable to change the preferred payment day in renewal quote variation",
            driver, extentedReport, true);
      }

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      // driver.close();
    } // finally
  }



}
