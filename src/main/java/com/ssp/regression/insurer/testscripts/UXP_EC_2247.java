package com.ssp.regression.insurer.testscripts;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.ssp.support.BaseTest;
import com.ssp.support.Log;
import com.ssp.support.WebDriverFactory;
import com.ssp.utils.DataProviderUtils;
import com.ssp.utils.DataUtils;
import com.ssp.utils.GenericUtils;
import com.ssp.uxp_pages.BillingAdjustmentPage;
import com.ssp.uxp_pages.CardDetailsPage;
import com.ssp.uxp_pages.CustDashboardPage;
// import com.ssp.uxp_pages.GetQuotePage;
import com.ssp.uxp_pages.HomePage;
import com.ssp.uxp_pages.LoginPage;
import com.ssp.uxp_pages.NewQuotePage;
import com.ssp.uxp_pages.SearchPage;

public class UXP_EC_2247 extends BaseTest {

  private String webSite;
  // private HashMap<String, String> customerDetails1 = new HashMap<String,
  // String>();
  private HashMap<String, String> customerDetails2 = new HashMap<String, String>();
  String mainCustFirstName = null;
  String mainCustLastName = null;
  String adjustmentAmount = null;

  @BeforeMethod(alwaysRun = true)
  public void init(ITestContext context) throws IOException {
    webSite = System.getProperty("webSite") != null ? System.getProperty("webSite")
        : context.getCurrentXmlTest().getParameter("webSite");
  }

  // @BeforeClass

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

    return Log.testCaseInfo("EC _ " + testCaseId + " [" + test + "]",
        "EC_" + testCaseId + " - " + testDesc + " [" + browserwithos + "]", test, "Regression");
  }

  public HashMap<String, String> getTestData(String testcaseId) {
    String env =
        Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("env");
    String className = "UXP_EC_2247_" + env;
    return DataUtils.testDatabyID(testcaseId, className);
  }

  @Test(description = "Data Setup for 2247, Creating 2 new contacts",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 0)
  public void Datasetup_2247(String browser) throws Exception {

    String tcId = "Datasetup_2247";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(
          homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page UserName : "
              + userName + ", Password : " + password,
          "Not logged in to Home page", driver, extentedReport, true);

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      testData = searchPage.enterCustomerDetails(testData, true, extentedReport);
      mainCustFirstName = testData.get("First Name");
      mainCustLastName = testData.get("Last Name");
      Log.message("Contact 1 First Name------>" + mainCustFirstName, driver, extentedReport);
      Log.message("Contact 1 Last Name------>" + mainCustLastName, driver, extentedReport);

      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      custdashboardpage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      searchPage.clickCreateCustomer(true, extentedReport);
      customerDetails2 = searchPage.enterCustomerDetails(testData, true, extentedReport, true);
      customerDetails2.get("First Name");
      customerDetails2.get("Last Name");
      Log.message("Contact 2 First Name------>" + customerDetails2.get("First Name"), driver,
          extentedReport);
      Log.message("Contact 2 Last Name------>" + customerDetails2.get("Last Name"), driver,
          extentedReport);
      searchPage.confirmCreateCustomer(false, extentedReport);
      Log.message("*******End of DataSetup***********", driver, extentedReport);
      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Check that the user is able to change the payor of the policy, once the quote reaches the summary page",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 1)
  public void TC_480(String browser) throws Exception {

    String tcId = "TC_480";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(
          homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page UserName : "
              + userName + ", Password : " + password,
          "Not logged in to Home page", driver, extentedReport, true);

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.enterLastName(mainCustLastName, extentedReport);
      searchPage.enterFirstName(mainCustFirstName, extentedReport);
      searchPage.clickSearch(extentedReport);
      CustDashboardPage custdashboardpage =
          searchPage.selectContactFromSearchResult(mainCustFirstName, true, extentedReport);
      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(false, extentedReport);
      // Create New Quote
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
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);
      // Payor Change
      newquotepage.clickChangePayorButton(extentedReport, true);
      String payorname = newquotepage.selectChangePayor(customerDetails2.get("Last Name"),
          customerDetails2.get("First Name"), "GU111PZ", extentedReport, true);
      Log.softAssertThat(newquotepage.verifyPayorName(payorname),
          "The user is able to change the payor of the policy, once the quote reached Acceptance page : "
              + payorname,
          "The user is not able to change the payor of the policy, once the quote reached Acceptance page : "
              + payorname,
          driver, extentedReport, true);
      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Check that the user is able to change the payor of the policy via Billing adjustment function",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 2)
  public void TC_481(String browser) throws Exception {

    String tcId = "TC_481";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(
          homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page UserName : "
              + userName + ", Password : " + password,
          "Not logged in to Home page", driver, extentedReport, true);

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.enterLastName(mainCustLastName, extentedReport);
      searchPage.enterFirstName(mainCustFirstName, extentedReport);
      searchPage.clickSearch(extentedReport);
      CustDashboardPage custdashboardpage = searchPage.clickSearchedContactPolicy("contact", "",
          testData.get("Title"), true, extentedReport);
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(false, extentedReport);
      // Create New Quote
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
      newquotepage.selectPayment(testData, true, extentedReport);
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);
      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.pass("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      newquotepage.verifyPaymentTrasaction(extentedReport);

      Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment was successful", "Payment was not successful", driver, extentedReport, true);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      custdashboardpage.selectBAfromManagePolicyTab(extentedReport, true);
      BillingAdjustmentPage billingAdjustmentPage =
          custdashboardpage.enterBADetails(testData, extentedReport, true);
      billingAdjustmentPage.selectAdjustmentReason("Change Payment Plan", extentedReport, true);
      billingAdjustmentPage.clickNext(extentedReport);
      billingAdjustmentPage.selectPaymentPlan("Monthly", extentedReport);
      billingAdjustmentPage.selectPaymentMethod("Direct Debit", extentedReport, true);
      billingAdjustmentPage.clickChangePayorButton(extentedReport, true);
      String postcode = customerDetails2.get("Post Code");
      billingAdjustmentPage.enterChangePayorDetails(customerDetails2.get("Last Name"),
          customerDetails2.get("First Name"), postcode, extentedReport, true);
      String payorname = customerDetails2.get("Title") + " " + customerDetails2.get("First Name")
          + " " + customerDetails2.get("Last Name");
      Log.softAssertThat(billingAdjustmentPage.verifyPayorName(payorname),
          "The user is able to change the payor by selecting the Change Payor button from the payment panel : "
              + payorname,
          "The user is unable to change the payor by selecting the Change Payor button from the payment panel : "
              + payorname,
          driver, extentedReport, true);
      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Check that by default the policyholder is set as payor of the policy at the time of New Business",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 3)
  public void TC_482(String browser) throws Exception {

    String tcId = "TC_482";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
    String payorName = null;
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(
          homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page UserName : "
              + userName + ", Password : " + password,
          "Not logged in to Home page", driver, extentedReport, true);

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      String firstName = customerDetails2.get("First Name");
      String lastName = customerDetails2.get("Last Name");
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);
      CustDashboardPage custdashboardpage =
          searchPage.selectContactFromSearchResult(firstName, true, extentedReport);
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(false, extentedReport);
      // Create New Quote
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
      payorName = testData.get("Title") + " " + firstName + " " + lastName;
      Log.softAssertThat(newquotepage.verifyPayorName(payorName),
          "By default the policyholder name is set as payor of the policy at the time of New Business : "
              + payorName,
          "By default the policyholder name is not set as payor of the policy at the time of New Business : "
              + payorName,
          driver, extentedReport, true);
      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(
      description = "Check that the payor of renewal is default/set to the payor of policy before renewal",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 4)
  public void TC_484(String browser) throws Exception {

    String tcId = "TC_484";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(
          homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page UserName : "
              + userName + ", Password : " + password,
          "Not logged in to Home page", driver, extentedReport, true);

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);
      testData.get("First Name");
      testData.get("Last Name");
      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true);
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(false, extentedReport);
      // Create New Quote
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
      newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.pass("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      newquotepage.verifyPaymentTrasaction(extentedReport);

      Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment was successful", "Payment was not successful", driver, extentedReport, true);

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      custdashboardpage.performRenewals_LapseRenewal("Invite Renewal", extentedReport, true);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickRenewalQuoteBuy(extentedReport);
      newquotepage.clickPaymentOptionBar(extentedReport, true);
      String payorName = testData.get("Title") + " " + testData.get("First Name") + " "
          + testData.get("Last Name");
      Log.softAssertThat(newquotepage.verifyPayorName(payorName),
          "The payor of renewal is default/set to the payor of policy before renewal : "
              + payorName,
          "The payor of renewal is not default/set to the payor of policy before renewal : "
              + payorName,
          driver, extentedReport, true);
      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(
      description = "Check that the user is NOT able to select 'Payment Method' without selecting the 'Payment Plan'",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 5)
  public void TC_488(String browser) throws Exception {

    String tcId = "TC_488";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
    String paymentMethod = testData.get("Payment Method");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(
          homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page UserName : "
              + userName + ", Password : " + password,
          "Not logged in to Home page", driver, extentedReport, true);

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);
      testData.get("First Name");
      testData.get("Last Name");
      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(+5, -1, extentedReport, true);
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(false, extentedReport);
      // Create New Quote
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
      newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPaymentMethod(paymentMethod, extentedReport);
      Log.softAssertThat(newquotepage.getPaymentMethod().equals("- Please select -"),
          "The user is not able to select 'Payment Method' without selecting the 'Payment Plan'",
          "The user is able to select 'Payment Method' without selecting the 'Payment Plan'",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(
      description = "Check that the user is navigated to 'Acceptance page' and the Card details panel contains the automatic payment reference when the payment is made successfully",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 6)
  public void TC_497(String browser) throws Exception {

    String tcId = "TC_497";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
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

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);
      testData.get("First Name");
      testData.get("Last Name");
      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(+5, -1, extentedReport, true);
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(false, extentedReport);
      // Create New Quote
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
      newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.pass("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      newquotepage.verifyPaymentTrasaction(extentedReport);

      Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
          "The user is navigated to 'Acceptance page' and the Card details panel should contain the automatic payment reference when the payment is made successfully via payment hub",
          "The user is not navigated to 'Acceptance page' and the Card details panel should contain the automatic payment reference when the payment is made successfully via payment hub",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(
      description = "Check that the stored 'Bank Details' are listed when the payment method is selected as 'Direct Debit' when the  policyholder has existing details and Check that 'Change Payor' modal gets closed on clicking the 'Cancel' & 'Close' button",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 7)
  public void TC_499_504_505(String browser) throws Exception {

    String tcId = "TC_499_504_505";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
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

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);
      testData.get("First Name");
      testData.get("Last Name");
      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(+5, -1, extentedReport, true);
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(false, extentedReport);
      // Create New Quote
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
      newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);
      newquotepage.clickChangePayorButton(extentedReport, true);
      newquotepage.clickCancelChangePayorModal(extentedReport, true);
      Log.softAssertThat(newquotepage.verifyPaymodalIsClosed(),
          "'Change Payor' modal is closed on clicking the 'Cancel' button ",
          "'Change Payor' modal is not closed on clicking the 'Cancel' button ", driver,
          extentedReport);
      newquotepage.clickChangePayorButton(extentedReport, true);
      newquotepage.clickCloseChangePayorModal(extentedReport, true);
      Log.softAssertThat(newquotepage.verifyPaymodalIsClosed(),
          "'Change Payor' modal is closed on clicking the 'Cancel' button ",
          "'Change Payor' modal is not closed on clicking the 'Cancel' button ", driver,
          extentedReport);
      Log.softAssertThat(
          newquotepage.verifyStoredBankDetailsListed("Bootle Centre, Santander, Liverpool",
              testData, true, extentedReport),
          "The stored 'Bank Details' is listed in Bank details panel when the payment method is selected as 'Direct Debit' when the policyholder has existing details",
          "The stored 'Bank Details' is not listed in Bank details panel when the payment method is selected as 'Direct Debit' when the  policyholder has existing details",
          driver, extentedReport, true);
      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(
      description = "Check error message for 'First Name','Last Name' , 'Date of Birth' / 'Postcode' fields are mandatory in the 'Change Payor' modal",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 8)
  public void TC_507_508_509(String browser) throws Exception {

    String tcId = "TC_507_508_509";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
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

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);
      testData.get("First Name");
      testData.get("Last Name");
      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(+5, -1, extentedReport, true);
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(false, extentedReport);
      // Create New Quote
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
      newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);
      newquotepage.clickChangePayorButton(extentedReport, true);
      Log.softAssertThat(
          newquotepage.verifyPayorFieldsErrorMsg("All Fields", "", "", "",
              newquotepage.ERROR_MSG_PAYOR_ALLFIELDS, extentedReport),
          "'First Name' , 'Last Name' , 'Date of Birth' / 'Postcode' fields are mandatory in the 'Change Payor' modal: "
              + newquotepage.ERROR_MSG_PAYOR_ALLFIELDS,
          "'First Name' , 'Last Name' , 'Date of Birth' / 'Postcode' fields are not mandatory in the 'Change Payor' modal",
          driver, extentedReport, true);
      Log.softAssertThat(
          newquotepage.verifyPayorFieldsErrorMsg("PostCode", testData.get("Last Name"),
              testData.get("First Name"), "", newquotepage.ERROR_MSG_PAYOR_POSTCODE_OR_DOB,
              extentedReport),
          "A prompt message is displayed when Postcode or Date of Birth field is not been entered in the 'Change Payor' modal : "
              + newquotepage.ERROR_MSG_PAYOR_POSTCODE_OR_DOB,
          "A prompt message is not displayed when Postcode or Date of Birth field is not been entered in the 'Change Payor' modal",
          driver, extentedReport, true);
      Log.softAssertThat(
          newquotepage.verifyPayorFieldsErrorMsg("LastName", "", testData.get("First Name"),
              testData.get("Post Code"), newquotepage.ERROR_MSG_PAYOR_LASTNAME, extentedReport),
          "A prompt message is displayed when Last Name field is not been entered in the 'Change Payor' modal : "
              + newquotepage.ERROR_MSG_PAYOR_LASTNAME,
          "A prompt message is not displayed when Last Name field is not been entered in the 'Change Payor' modal",
          driver, extentedReport, true);
      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(
      description = "Check that the data entered in search fields are removed when 'Clear All' button is clicked before the search is performed",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_513(String browser) throws Exception {

    String tcId = "TC_513";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
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

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String firstName = custDetails.get("First Name");
      String lastName = custDetails.get("Last Name");

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);

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

      // Get and Buy Quote
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickBuy(extentedReport);
      newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      newquotepage.enterChangePayorDetail(lastName, firstName, testData.get("Post Code"),
          extentedReport, true);

      // click AllClear button
      newquotepage.clickAllClearButton(extentedReport, true);

      Log.softAssertThat(newquotepage.verifySearchFieldCleared(extentedReport),
          "The data entered in search fields is removed when 'Clear All' button is clicked before the search is performed",
          "The data entered in search fields is not removed when 'Clear All' button is clicked before the search is performed",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(
      description = "Check that the search results are removed and the modal is refreshed when 'Clear All' button is clicked after the search is performed",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 8)
  public void TC_514(String browser) throws Exception {

    String tcId = "TC_514";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
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

      // Create customer1
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> payorDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String payorFirstName = payorDetails.get("First Name");
      String payorLastName = payorDetails.get("Last Name");
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      searchPage.clickComplete(extentedReport);

      // Create customer2
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport, false);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(false, extentedReport);
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
      newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      newquotepage.enterChangePayorDetail(payorLastName, payorFirstName, testData.get("Post Code"),
          extentedReport, true);
      newquotepage.clickSearchBtn_ChangePayor(extentedReport);

      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("btnPayorSearchSelectRow1"),
              newquotepage),
          "Search result is displayed after clicking Search button",
          "Search result is not displayed after clicking Search button", driver, extentedReport,
          true);

      // click AllClear button
      newquotepage.clickAllClearButton(extentedReport, true);

      Log.softAssertThat(
          !newquotepage.uielement.verifyPageElements(Arrays.asList("btnPayorSearchSelectRow1"),
              newquotepage),
          "The search results is removed and the modal is refreshed when 'Clear All' button is clicked after the search is performed",
          "The search results is not removed and the modal is not refreshed when 'Clear All' button is clicked after the search is performed",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(description = "Check that the message 'No match found' is displayed when the search fails",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 8)
  public void TC_517(String browser) throws Exception {

    String tcId = "TC_517";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
    String errorMessage = testData.get("Message");

    String payorFirstName =
        GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name") + "Payor";
    String payorLastName =
        GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name") + "Payor";

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

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);

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

      // Get and Buy Quote
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickBuy(extentedReport);
      newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      newquotepage.enterChangePayorDetail(payorLastName, payorFirstName, testData.get("Post Code"),
          extentedReport, true);
      newquotepage.clickSearchBtn_ChangePayor(extentedReport);

      Log.softAssertThat(newquotepage.verifyChangePayorErrorMsg(errorMessage, extentedReport),
          "The message 'No match found' is displayed when the search fails",
          "The message 'No match found' is not displayed when the search fails", driver,
          extentedReport, true);

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(description = "Check that 'Create New Payor' button is displayed below the search results",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 8)
  public void TC_518(String browser) throws Exception {

    String tcId = "TC_518";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
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

      // Create customer1
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> payorDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String payorFirstName = payorDetails.get("First Name");
      String payorLastName = payorDetails.get("Last Name");
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      searchPage.clickComplete(extentedReport);

      // Create customer2
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport, true);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(false, extentedReport);
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
      newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      newquotepage.enterChangePayorDetail(payorLastName, payorFirstName, testData.get("Post Code"),
          extentedReport, true);
      newquotepage.clickSearchBtn_ChangePayor(extentedReport);

      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("btnCreateNewPayor"),
              newquotepage),
          "'Create New Payor' button is displayed below the search results",
          "'Create New Payor' button is not displayed below the search results", driver,
          extentedReport, true);

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(
      description = "Check that the contact(s) are displayed below the search fields when search is successful",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 8)
  public void TC_515(String browser) throws Exception {

    String tcId = "TC_515";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
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

      // Create customer1
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> payorDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String payorFirstName = payorDetails.get("First Name");
      String payorLastName = payorDetails.get("Last Name");
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      searchPage.clickComplete(extentedReport);

      // Create customer2
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport, true);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(false, extentedReport);
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
      newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      newquotepage.enterChangePayorDetail(payorLastName, payorFirstName, testData.get("Post Code"),
          extentedReport, true);
      newquotepage.clickSearchBtn_ChangePayor(extentedReport);

      // Verify search result
      String changePayorName = payorFirstName + " " + payorLastName;
      Log.softAssertThat(
          newquotepage.verifyChangePayorSearchResult(changePayorName, false, extentedReport),
          changePayorName
              + " - contact is displayed below the search fields when search is successful ",
          changePayorName
              + " - contact is not displayed below the search fields when search is successful ",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(
      description = "Check that the confirmation of the contact selection is displayed beneath the grid when 'Select' button is clicked against a particular contact",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_522(String browser) throws Exception {

    String tcId = "TC_522";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
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

      // Create customer1
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> payorDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String payorFirstName = payorDetails.get("First Name");
      String payorLastName = payorDetails.get("Last Name");
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      searchPage.clickComplete(extentedReport);

      // Create customer2
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport, true);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(false, extentedReport);
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
      newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      newquotepage.searchAndSelectChangePayor(payorLastName, payorFirstName,
          testData.get("Post Code"), extentedReport, true);

      // Verify confirmation
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("msgYouHaveSelect"),
              newquotepage),
          "The confirmation of the contact selection is displayed beneath the grid ",
          "The confirmation of the contact selection is not displayed beneath the grid ", driver,
          extentedReport, true);

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(
      description = "Check that the user is navigated to the Payment Panel with the original policyholder intact when 'Cancel' button is selected ",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_525(String browser) throws Exception {

    String tcId = "TC_525";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
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

      // Create customer1
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> payorDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String payorFirstName = payorDetails.get("First Name");
      String payorLastName = payorDetails.get("Last Name");
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      searchPage.clickComplete(extentedReport);

      // Create customer2
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport, true);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(false, extentedReport);
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
      newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      newquotepage.searchAndSelectChangePayor(payorLastName, payorFirstName,
          testData.get("Post Code"), extentedReport, true);

      // click Cancel button
      newquotepage.clickCancelbtn_ChangePayor(extentedReport);

      // Verify confirmation
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("barPaymentOption"),
              newquotepage),
          "The user is navigated to the Payment Panel with the original policyholder intact when 'Cancel' button is selected",
          "The user is navigated to the Payment Panel with the original policyholder intact when 'Cancel' button is selected",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(
      description = "Check that the data entered into the search fields of 'Change payor' modal is auto-populated into the 'Create New Payor' modal on selecting 'Create new payor' button",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_529(String browser) throws Exception {

    String tcId = "TC_529";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
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

      // Create customer1
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> payorDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String payorFirstName = payorDetails.get("First Name");
      String payorLastName = payorDetails.get("Last Name");
      String postCode = testData.get("Post Code");
      String dob = testData.get("Date of Birth");

      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      searchPage.clickComplete(extentedReport);

      // Create customer2
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport, true);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(false, extentedReport);
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
      newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      newquotepage.enterChangePayorDetail(payorLastName, payorFirstName, postCode, extentedReport,
          true);
      newquotepage.enterChangePayorDOB(dob, extentedReport);
      newquotepage.clickSearchBtn_ChangePayor(extentedReport);

      newquotepage.clickCreateNewPayor(extentedReport);

      Log.softAssertThat(newquotepage.verifyLastNameTxtBoxValue(payorLastName),
          payorLastName
              + " - Details of Last name is auto-populated in the 'Create New Payor' modal",
          payorLastName
              + " - Details of Last name is not auto-populated in the 'Create New Payor' modal",
          driver, extentedReport, true);

      Log.softAssertThat(newquotepage.verifyFirstNameTxtBoxValue(payorFirstName),
          payorFirstName
              + " - Details of First name is auto-populated in the 'Create New Payor' modal",
          payorFirstName
              + " - Details of First name is not auto-populated in the 'Create New Payor' modal",
          driver, extentedReport, true);

      Log.softAssertThat(newquotepage.verifyDOBTxtBoxValue(dob),
          dob + " - Details of dob is auto-populated in the 'Create New Payor' modal",
          dob + " - Details of dob  is not auto-populated in the 'Create New Payor' modal", driver,
          extentedReport, true);

      Log.softAssertThat(newquotepage.verifyPostCodeTxtBoxValue(postCode),
          postCode + " - Details of postCode is auto-populated in the 'Create New Payor' modal",
          postCode + " - Details of postCode is not auto-populated in the 'Create New Payor' modal",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(
      description = "Check that a 'New Contact' is created and payor added to the Payment Panel screen when click on 'Save' button from Create New Payor screen",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_535(String browser) throws Exception {

    String tcId = "TC_535";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");

    String title = testData.get("Title");
    String dob = testData.get("Date of Birth");
    String postCode = testData.get("Post Code");

    String newPayorFirstName = GenericUtils.getRandomCharacters("alpha", 5);
    String newPayorLastName = GenericUtils.getRandomCharacters("alpha", 5);

    String newPayorName = title + " " + newPayorFirstName + " " + newPayorLastName;

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

      // Create customer1
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> payorDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String payorFirstName = payorDetails.get("First Name");
      String payorLastName = payorDetails.get("Last Name");

      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      searchPage.clickComplete(extentedReport);

      // Create customer2
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport, true);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(false, extentedReport);
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
      newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      newquotepage.enterChangePayorDetail(payorLastName, payorFirstName, postCode, extentedReport,
          true);
      newquotepage.clickSearchBtn_ChangePayor(extentedReport);

      newquotepage.clickCreateNewPayor(extentedReport);

      // Create New Payor
      newquotepage.selectTitle(title);
      newquotepage.entercn_firstName(newPayorFirstName);
      newquotepage.entercn_LastName(newPayorLastName);
      newquotepage.entercn_DateOfBirth(dob);
      newquotepage.entercn_PostCode(postCode);
      newquotepage.selectAddress(extentedReport);

      newquotepage.clickSaveBtn_CreateNewPayor(extentedReport);

      Log.softAssertThat(newquotepage.verifyPayorName(newPayorName),
          newPayorName
              + "New Contact is created and payor is added to the Payment Panel screen when click on 'Save' button from Create New Payor screen",
          newPayorName
              + "New Contact is not created and payor is not added to the Payment Panel screen when click on 'Save' button from Create New Payor screen",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(
      description = "Check that the 'Bank Details' of the payor (if stored already) is displayed in the Payment Panel page when that contact already exists",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_536(String browser) throws Exception {

    String tcId = "TC_536";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");

    /*
     * String branch = testData.get("Bank Branch"); String accNo = testData.get("Account Number");
     * String sortCode = testData.get("Sort Code");
     */

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

      // Create customer1
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> payorDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String payorFirstName = payorDetails.get("First Name");
      String payorLastName = payorDetails.get("Last Name");
      String postCode = testData.get("Post Code");

      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);

      // Confirm customer details and create customer
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
      Log.softAssertThat(newquotepage.verifyNewQuotePage(true, extentedReport),
          "Verified NewQuotePage ", "Not verified NewQuotePage", driver, extentedReport, true);
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      // Payment
      newquotepage.clickBuy(extentedReport);

      String accName = GenericUtils.getRandomCharacters("alpha", 5);
      testData.put("Account Name", accName);

      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to custdashboardpage", "Could not Navigate to custdashboardpage", driver,
          extentedReport, true);

      searchPage.clickComplete(extentedReport);

      // Create customer2
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport, true);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(false, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
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
      newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      newquotepage.selectChangePayor(payorLastName, payorFirstName, postCode, extentedReport, true);

      Log.softAssertThat(newquotepage.verifyAccountNameDisplayed(accName),
          "'Bank Details' of the payor (if stored already) is displayed in the Payment Panel page",
          "'Bank Details' of the payor (if stored already) is not displayed in the Payment Panel page",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(
      description = "Check that the user is able to complete a New Business after changing the payor ",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_540(String browser) throws Exception {

    String tcId = "TC_540";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");

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

      // Create customer1
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> payorDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String payorFirstName = payorDetails.get("First Name");
      String payorLastName = payorDetails.get("Last Name");
      String postCode = testData.get("Post Code");

      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);

      // Confirm customer details and create customer
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
      Log.softAssertThat(newquotepage.verifyNewQuotePage(true, extentedReport),
          "Verified NewQuotePage ", "Not verified NewQuotePage", driver, extentedReport, true);
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      // Payment
      newquotepage.clickBuy(extentedReport);

      String accName = GenericUtils.getRandomCharacters("alpha", 5);
      testData.put("Account Name", accName);
      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to custdashboardpage", "Could not Navigate to custdashboardpage", driver,
          extentedReport, true);

      searchPage.clickComplete(extentedReport);

      // Create customer2
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport, true);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(false, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
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
      newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      String payorname = newquotepage.selectChangePayor(payorLastName, payorFirstName, postCode,
          extentedReport, true);

      Log.softAssertThat(newquotepage.verifyPayorName(payorname),
          payorname + " - payor name is changed successfully",
          payorname + " - payor name is not changed", driver, extentedReport, true);
      newquotepage.tickSpokenDirectlyNewPayorCheckbox(extentedReport);

      newquotepage.selectAccount(sortCode, branch, accName, accNo, extentedReport, true);

      newquotepage.tickCustomerAgreesCheckbox(extentedReport);
      newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for policy status
      String expectedPolicyStatus = "Accepted";
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat(
          (policyDetails.get("Status").toString().equalsIgnoreCase(expectedPolicyStatus)),
          "NB Policy Created Successfully in '" + expectedPolicyStatus + "' status",
          "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status");

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(
      description = "Verify user is navigated to 'Payment hub' when the payment method is selected as 'Card (via payment hub)'",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_496(String browser) throws Exception {

    String tcId = "TC_496";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
    String paymentPlan = testData.get("Payment Plan");
    String paymentMethod = testData.get("Payment Method");

    // String payorName = null;
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(
          homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page UserName : "
              + userName + ", Password : " + password,
          "Not logged in to Home page", driver, extentedReport, true);

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(false, extentedReport);
      // Create New Quote
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
      newquotepage.selectPaymentPlan(paymentPlan, extentedReport);
      newquotepage.selectPaymentMethod(paymentMethod.toString(), extentedReport);
      newquotepage.takePayment(extentedReport);

      Log.softAssertThat(newquotepage.verifyPaymentHub(driver),
          "User Navigated to Payment Hub Page : ", "User not Navigated to Payment Hub Page", driver,
          extentedReport, true);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(description = "Verify 'Enter Payment Reference' field on selecting 'confirm' button",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 3)
  public void TC_495(String browser) throws Exception {

    String tcId = "TC_495";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
    String paymentPlan = testData.get("Payment Plan");
    String paymentMethod = testData.get("Payment Method");

    // String payorName = null;
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(
          homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page UserName : "
              + userName + ", Password : " + password,
          "Not logged in to Home page", driver, extentedReport, true);

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(false, extentedReport);
      // Create New Quote
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
      newquotepage.selectPaymentPlan(paymentPlan, extentedReport);
      newquotepage.selectPaymentMethod(paymentMethod.toString(), extentedReport);
      // newquotepage.selectPayment(testData, true, extentedReport);
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

      custdashboardpage = newquotepage.confirmPayment(extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "User able to navigate to the customer dashboard page after clicking confirm payment",
          "User not navigated to the customer dashboard page after clicking confirm payment",
          driver, extentedReport, true);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(description = "Verify the 'Create New Payor' modal",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 3)
  public void TC_534(String browser) throws Exception {

    String tcId = "TC_534";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(
          homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page UserName : "
              + userName + ", Password : " + password,
          "Not logged in to Home page", driver, extentedReport, true);

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> policyDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      String changePayorFN = policyDetails.get("First Name");
      String changePayorLN = policyDetails.get("Last Name");
      String changePayorPC = policyDetails.get("Post Code");

      custdashboardpage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchpage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchpage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      searchpage.clickCreateCustomer(true, extentedReport);

      searchpage.enterCustomerDetails(testData, true, extentedReport);
      CustDashboardPage custdashboardPage = searchpage.confirmCreateCustomer(false, extentedReport);
      custdashboardPage.clickNewQuote(true, extentedReport);
      custdashboardPage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(false, extentedReport);
      // Create New Quote
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
      newquotepage.selectPayment(testData, true, extentedReport);

      newquotepage.clickChangePayorButton(extentedReport, true);
      newquotepage.changePayorDetails(changePayorLN, changePayorFN, changePayorPC, extentedReport);
      newquotepage.clickPayorSearchButton(extentedReport, true);
      newquotepage.createNewPayorDetails(extentedReport);

      newquotepage.clickCrossIcon(extentedReport);

      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("searchScreen"), newquotepage),
          "Search Screen Verified", "Search Screen not Verified", driver, extentedReport, true);
      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }


  @Test(description = "Verify the 'Create New Payor' modal",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 3)
  public void TC_530(String browser) throws Exception {

    String tcId = "TC_530";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");

    // String payorName = null;
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(
          homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page UserName : "
              + userName + ", Password : " + password,
          "Not logged in to Home page", driver, extentedReport, true);

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> policyDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String changePayorFN = policyDetails.get("First Name");
      String changePayorLN = policyDetails.get("Last Name");
      String changePayorPC = policyDetails.get("Post Code");
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      custdashboardpage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage1 = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage1.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage1),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      searchPage1.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);
      CustDashboardPage custdashboardpage1 =
          searchPage.confirmCreateCustomer(false, extentedReport);
      Log.softAssertThat(
          custdashboardpage1.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage1),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);
      custdashboardpage1.clickNewQuote(true, extentedReport);
      custdashboardpage1.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage1.clickContinueQuote(false, extentedReport);
      // Create New Quote
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
      newquotepage.selectPayment(testData, true, extentedReport);

      newquotepage.clickChangePayorButton(extentedReport, true);
      newquotepage.changePayorDetails(changePayorLN, changePayorFN, changePayorPC, extentedReport);
      newquotepage.clickPayorSearchButton(extentedReport, true);

      newquotepage.createNewPayorDetails(extentedReport);
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("titleFieldInNewPayor"),
              newquotepage),
          "Title Field Verified in New payor details",
          "Title Field not Verified in New payor details", driver, extentedReport, true);
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("firstNameFieldInNewPayor"),
              newquotepage),
          "First Name Field Verified in New payor details",
          "First Name Field not Verified in New payor details", driver, extentedReport, true);
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("lastNameFieldInNewPayor"),
              newquotepage),
          "Last Name Field Verified in New payor details",
          "Last Name Field not Verified in New payor details", driver, extentedReport, true);
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("addressFieldInNewPayor"),
              newquotepage),
          "Address Field Verified in New payor details",
          "Address Field not Verified in New payor details", driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(description = "Verify the 'Search' button on 'Change Payor' modal ",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 3)
  public void TC_510(String browser) throws Exception {

    String tcId = "TC_510";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    // String lastName=testData.get("Last Name");
    // String firstName=testData.get("First Name");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");

    // String payorName = null;
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(
          homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page UserName : "
              + userName + ", Password : " + password,
          "Not logged in to Home page", driver, extentedReport, true);

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      searchPage.clickCreateCustomer(true, extentedReport);

      HashMap<String, String> policyDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String changePayorFN = policyDetails.get("First Name");
      String changePayorLN = policyDetails.get("Last Name");
      String changePayorPC = policyDetails.get("Post Code");
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      custdashboardpage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage1 = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage1.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage1),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);
      searchPage1.clickCreateCustomer(true, extentedReport);
      searchPage1.enterCustomerDetails(testData, true, extentedReport);
      CustDashboardPage custdashboardpage1 =
          searchPage1.confirmCreateCustomer(false, extentedReport);
      custdashboardpage1.clickNewQuote(true, extentedReport);
      custdashboardpage1.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage1.clickContinueQuote(false, extentedReport);
      // Create New Quote
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
      newquotepage.selectPayment(testData, true, extentedReport);

      newquotepage.clickChangePayorButton(extentedReport, true);
      newquotepage.changePayorDetails(changePayorLN, changePayorFN, changePayorPC, extentedReport);
      newquotepage.clickPayorSearchButton(extentedReport, true);
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("searchResults"), newquotepage),
          "Search Results Verified", "Search Results not Verified", driver, extentedReport, true);
      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(description = "Verify the search results grid", dataProviderClass = DataProviderUtils.class,
      dataProvider = "ssBVTDataProvider", priority = 3)
  public void TC_519(String browser) throws Exception {

    String tcId = "TC_519";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
    String Firstletter = "XY";
    String LNFirstLetter = "ZZ";

    String firstName = Firstletter + GenericUtils.getRandomCharacters("alpha", 4);
    String LastName = LNFirstLetter + GenericUtils.getRandomCharacters("alpha", 4);
    testData.put("First Name", firstName);
    testData.put("Last Name", LastName);

    HashMap<String, String> testData1 = getTestData(tcId);

    String firstName1 = Firstletter + GenericUtils.getRandomCharacters("alpha", 4);
    String LastName1 = LNFirstLetter + GenericUtils.getRandomCharacters("alpha", 4);
    testData1.put("First Name", firstName1);
    testData1.put("Last Name", LastName1);

    HashMap<String, String> testData2 = getTestData(tcId);
    String firstName11 = Firstletter + GenericUtils.getRandomCharacters("alpha", 4);
    String LastName11 = LNFirstLetter + GenericUtils.getRandomCharacters("alpha", 4);
    testData2.put("First Name", firstName11);
    testData2.put("Last Name", LastName11);
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(
          homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page UserName : "
              + userName + ", Password : " + password,
          "Not logged in to Home page", driver, extentedReport, true);

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> policyDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);

      // HashMap<String, String> policyDetails =
      // searchPage.enterCustomerDetails(testData,true,extentedReport);
      String changePayorFN = policyDetails.get("First Name");
      String changePayorLN = policyDetails.get("Last Name");
      // String trimLN=changePayorLN.substring(changePayorLN.length() - 4);
      String changePayorPC = policyDetails.get("Post Code");
      custdashboardpage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage1 = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage1.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage1),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      searchPage1.clickCreateCustomer(true, extentedReport);
      searchPage1.enterCustomerDetails(testData1, true, extentedReport);
      CustDashboardPage custdashboardpage1 =
          searchPage1.confirmCreateCustomer(false, extentedReport);
      custdashboardpage1.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchpage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchpage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);
      searchpage.clickCreateCustomer(true, extentedReport);
      searchpage.enterCustomerDetails(testData2, true, extentedReport);
      CustDashboardPage custdashboardPage = searchpage.confirmCreateCustomer(false, extentedReport);
      custdashboardPage.clickNewQuote(true, extentedReport);
      custdashboardPage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(false, extentedReport);
      // Create New Quote
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
      newquotepage.selectPayment(testData, true, extentedReport);
      newquotepage.clickChangePayorButton(extentedReport, true);
      newquotepage.changePayorDetails(changePayorLN.substring(0, 2), changePayorFN.substring(0, 2),
          changePayorPC, extentedReport);
      newquotepage.clickPayorSearchButton(extentedReport, true);
      newquotepage.lastName_Alphabeticalorder(extentedReport);
      Log.message("Verified last Name");

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }


  }

  @Test(groups = "Business_Scenario",
      description = "Change the payor in respect of a New Business Quote- Contact (Policyholder) is a new contact- Payor is a new contact and doesn't already exist- Payment is Annual Card- No card details therefore exist",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_001(String browser) throws Exception {

    String tcId = "BS_001";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
    String postCode = testData.get("Post Code");
    String message = testData.get("Message");

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
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String contactFirstName = contactDetails.get("First Name");
      String contactLastName = contactDetails.get("Last Name");

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
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

      // Select Payment Plan/method
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      newquotepage.enterChangePayorDetail(contactLastName, contactFirstName, postCode,
          extentedReport, true);
      newquotepage.clickSearchBtn_ChangePayor(extentedReport);

      // Create New change payor
      newquotepage.clickCreateNewPayor(extentedReport);
      HashMap<String, String> payorDetails =
          newquotepage.enterCustomerDetails(testData, true, extentedReport, true);
      String payorFirstName = payorDetails.get("First Name");
      String payorLastName = payorDetails.get("Last Name");
      String payorname = payorFirstName + " " + payorLastName;
      newquotepage.clickSaveBtn_CreateNewPayor(extentedReport);

      Log.softAssertThat(newquotepage.verifyPayorName(payorname),
          payorname + " - Payor name is updated in change payor",
          payorname + " - Payor name is not updated in change payor", driver, extentedReport, true);

      Log.softAssertThat(newquotepage.verifyNoCardDetailMsg(message), "Card details is not stored",
          "Card details is stored", driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(groups = "Business_Scenario",
      description = "Change the payor in respect of a New Business Quote- Contact (Policyholder) is a new contact- Payor is a new contact and doesn't already exist- Payment is Monthly Direct Debit- No Bank Details therefore exist",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_002(String browser) throws Exception {

    String tcId = "BS_002";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
    String postCode = testData.get("Post Code");
    String message = testData.get("Message");

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
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String contactFirstName = contactDetails.get("First Name");
      String contactLastName = contactDetails.get("Last Name");

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
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

      // Select Payment Plan/method
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      newquotepage.enterChangePayorDetail(contactLastName, contactFirstName, postCode,
          extentedReport, true);
      newquotepage.clickSearchBtn_ChangePayor(extentedReport);

      // Create New change payor
      newquotepage.clickCreateNewPayor(extentedReport);
      HashMap<String, String> payorDetails =
          newquotepage.enterCustomerDetails(testData, true, extentedReport, true);
      String payorFirstName = payorDetails.get("First Name");
      String payorLastName = payorDetails.get("Last Name");
      String payorname = payorFirstName + " " + payorLastName;
      newquotepage.clickSaveBtn_CreateNewPayor(extentedReport);

      Log.softAssertThat(newquotepage.verifyPayorName(payorname),
          payorname + " - Payor name is updated in change payor",
          payorname + " - Payor name is not updated in change payor", driver, extentedReport, true);

      Log.softAssertThat(newquotepage.verifyNoAccountDetailMsg(message),
          "Account details does not exists", "Account details does exists", driver, extentedReport,
          true);

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(groups = "Business_Scenario",
      description = "Change the payor in respect of a New Business Quote- Contact (Policyholder) is a new contact- Payor is an existing contact and already exists- Payment is Annual Card- Card details are not stored",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_003(String browser) throws Exception {

    String tcId = "BS_003";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
    String postCode = testData.get("Post Code");
    String message = testData.get("Message");

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

      // Create customer1 - payor
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> payorDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String payorFirstName = payorDetails.get("First Name");
      String payorLastName = payorDetails.get("Last Name");

      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      searchPage.clickComplete(extentedReport);

      // Create customer2
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport, true);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(false, extentedReport);
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
      newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      String payorname = newquotepage.selectChangePayor(payorLastName, payorFirstName, postCode,
          extentedReport, true);
      Log.softAssertThat(newquotepage.verifyPayorName(payorname),
          payorname + " - Payor name is updated in change payor",
          payorname + " - Payor name is not updated in change payor", driver, extentedReport, true);

      Log.softAssertThat(newquotepage.verifyNoCardDetailMsg(message), "Card details is not stored",
          "Card details is stored", driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(groups = "Business_Scenario",
      description = "Change the payor in respect of a New Business Quote- Contact (Policyholder) is a new contact- Payor is an existing contact and already exists- Payment is Monthly Direct Debit- Use existing Bank Account",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_004(String browser) throws Exception {

    String tcId = "BS_004";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");

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

      // Create customer1-payor
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> payorDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String payorFirstName = payorDetails.get("First Name");
      String payorLastName = payorDetails.get("Last Name");
      String postCode = testData.get("Post Code");

      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);

      // Confirm customer details and create customer
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
      Log.softAssertThat(newquotepage.verifyNewQuotePage(true, extentedReport),
          "Verified NewQuotePage ", "Not verified NewQuotePage", driver, extentedReport, true);
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      // Payment
      newquotepage.clickBuy(extentedReport);

      String accName = GenericUtils.getRandomCharacters("alpha", 5);
      testData.put("Account Name", accName);

      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to custdashboardpage", "Could not Navigate to custdashboardpage", driver,
          extentedReport, true);

      searchPage.clickComplete(extentedReport);

      // Create customer2
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport, true);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(false, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
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
      // newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      newquotepage.selectChangePayor(payorLastName, payorFirstName, postCode, extentedReport, true);
      newquotepage.tickSpokenDirectlyNewPayorCheckbox(extentedReport);

      newquotepage.selectAccount(sortCode, branch, accName, accNo, extentedReport, true);
      Log.softAssertThat(
          newquotepage.verifyAccountSelection(sortCode, branch, accName, accNo, extentedReport),
          "Existing Bank account is selected", "Existing Bank account is not selected", driver,
          extentedReport, true);

      newquotepage.tickCustomerAgreesCheckbox(extentedReport);
      newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for policy status
      String expectedPolicyStatus = "Accepted";
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat(
          (policyDetails.get("Status").toString().equalsIgnoreCase(expectedPolicyStatus)),
          "NB Policy Created Successfully in '" + expectedPolicyStatus + "' status",
          "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status");

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(groups = "Business_Scenario",
      description = "Change the payor in respect of a New Business Quote- Contact (Policyholder) is a new contact- Payor is an existing contact and already exists- Payment is Monthly Direct Debit- Add new Bank account and select",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_005(String browser) throws Exception {

    String tcId = "BS_005";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");

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

      // Create customer1 - payor
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> payorDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String payorFirstName = payorDetails.get("First Name");
      String payorLastName = payorDetails.get("Last Name");
      String postCode = testData.get("Post Code");

      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);

      // Confirm customer details and create customer
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
      Log.softAssertThat(newquotepage.verifyNewQuotePage(true, extentedReport),
          "Verified NewQuotePage ", "Not verified NewQuotePage", driver, extentedReport, true);
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      // Payment
      newquotepage.clickBuy(extentedReport);

      String accName = GenericUtils.getRandomCharacters("alpha", 5);
      testData.put("Account Name", accName);

      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to custdashboardpage", "Could not Navigate to custdashboardpage", driver,
          extentedReport, true);
      searchPage.clickComplete(extentedReport);

      // Create customer2
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport, true);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(false, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
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
      // newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      newquotepage.selectChangePayor(payorLastName, payorFirstName, postCode, extentedReport, true);
      newquotepage.tickSpokenDirectlyNewPayorCheckbox(extentedReport);

      // Add new Account
      newquotepage.clickAddAccountDetails(extentedReport);
      newquotepage.checkBankAccount(accNo, sortCode, extentedReport);

      String newAccName = GenericUtils.getRandomCharacters("alpha", 5);
      newquotepage.enterAccountName(newAccName, extentedReport);
      newquotepage.saveAccountDetails(extentedReport);
      newquotepage.selectAccount(sortCode, branch, newAccName, accNo, extentedReport, true);

      Log.softAssertThat(
          newquotepage.verifyAccountSelection(sortCode, branch, newAccName, accNo, extentedReport),
          "New Account detail is added and selected",
          "New Account detail is not added and not selected", driver, extentedReport, true);

      newquotepage.tickCustomerAgreesCheckbox(extentedReport);
      newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for policy status
      String expectedPolicyStatus = "Accepted";
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat(
          (policyDetails.get("Status").toString().equalsIgnoreCase(expectedPolicyStatus)),
          "NB Policy Created Successfully in '" + expectedPolicyStatus + "' status",
          "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status");

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(groups = "Business_Scenario",
      description = "Change the payor in respect of a New Business Quote- Contact (Policyholder) is an existing contact- Payor is a new contact and doesn't already exist- Payment is Annual Card- No card details are stored",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_006(String browser) throws Exception {

    String tcId = "BS_006";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");

    String message = testData.get("Message");

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

      // Create customer1 - payor
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String custFirstName = custDetails.get("First Name");
      String custLastName = custDetails.get("Last Name");
      String postCode = testData.get("Post Code");

      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);

      // Confirm customer details and create customer
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);
      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(custLastName, extentedReport);
      searchPage.enterFirstName(custFirstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(custLastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      Log.softAssertThat(newquotepage.verifyNewQuotePage(true, extentedReport),
          "Verified NewQuotePage ", "Not verified NewQuotePage", driver, extentedReport, true);
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      // Payment
      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      newquotepage.enterChangePayorDetail(custLastName, custFirstName, postCode, extentedReport,
          true);
      newquotepage.clickSearchBtn_ChangePayor(extentedReport);

      // Create New change payor
      newquotepage.clickCreateNewPayor(extentedReport);
      HashMap<String, String> payorDetails =
          newquotepage.enterCustomerDetails(testData, true, extentedReport, true);
      String payorFirstName = payorDetails.get("First Name");
      String payorLastName = payorDetails.get("Last Name");
      String payorname = payorFirstName + " " + payorLastName;
      newquotepage.clickSaveBtn_CreateNewPayor(extentedReport);

      Log.softAssertThat(newquotepage.verifyPayorName(payorname),
          payorname + " - Payor name is updated in change payor",
          payorname + " - Payor name is not updated in change payor", driver, extentedReport, true);

      Log.softAssertThat(newquotepage.verifyNoCardDetailMsg(message), "Card details is not stored",
          "Card details is stored", driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(groups = "Business_Scenario",
      description = "Change the payor in respect of a New Business Quote- Contact (Policyholder) is an existing contact- Payor is a new contact and doesn't already exist- Payment is Monthly Direct Debit- Add new Bank account and select",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_007(String browser) throws Exception {

    String tcId = "BS_007";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");

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

      // Create customer1 - payor
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String custFirstName = custDetails.get("First Name");
      String custLastName = custDetails.get("Last Name");
      String postCode = testData.get("Post Code");

      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);

      // Confirm customer details and create customer
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);
      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(custLastName, extentedReport);
      searchPage.enterFirstName(custFirstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(custLastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      Log.softAssertThat(newquotepage.verifyNewQuotePage(true, extentedReport),
          "Verified NewQuotePage ", "Not verified NewQuotePage", driver, extentedReport, true);
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      // Payment
      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      newquotepage.enterChangePayorDetail(custLastName, custFirstName, postCode, extentedReport,
          true);
      newquotepage.clickSearchBtn_ChangePayor(extentedReport);

      // Create New change payor
      newquotepage.clickCreateNewPayor(extentedReport);
      HashMap<String, String> payorDetails =
          newquotepage.enterCustomerDetails(testData, true, extentedReport, true);
      String payorFirstName = payorDetails.get("First Name");
      String payorLastName = payorDetails.get("Last Name");
      String payorname = payorFirstName + " " + payorLastName;
      newquotepage.clickSaveBtn_CreateNewPayor(extentedReport);

      Log.softAssertThat(newquotepage.verifyPayorName(payorname),
          payorname + " - Payor name is updated in change payor",
          payorname + " - Payor name is not updated in change payor", driver, extentedReport, true);
      newquotepage.tickSpokenDirectlyNewPayorCheckbox(extentedReport);

      // Add new Account
      newquotepage.clickAddAccountDetails(extentedReport);
      newquotepage.checkBankAccount(accNo, sortCode, extentedReport);

      String newAccName = GenericUtils.getRandomCharacters("alpha", 5);
      newquotepage.enterAccountName(newAccName, extentedReport);
      newquotepage.saveAccountDetails(extentedReport);
      newquotepage.selectAccount(sortCode, branch, newAccName, accNo, extentedReport, true);

      Log.softAssertThat(
          newquotepage.verifyAccountSelection(sortCode, branch, newAccName, accNo, extentedReport),
          "New Account detail is added and selected",
          "New Account detail is not added and not selected", driver, extentedReport, true);

      newquotepage.tickCustomerAgreesCheckbox(extentedReport);
      newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for policy status
      String expectedPolicyStatus = "Accepted";
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat(
          (policyDetails.get("Status").toString().equalsIgnoreCase(expectedPolicyStatus)),
          "NB Policy Created Successfully in '" + expectedPolicyStatus + "' status",
          "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status");

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(groups = "Business_Scenario",
      description = "Change the payor in respect of a New Business Quote- Contact (Policyholder) is an existing contact- Payor is an existing contact and already exists- Payment is Annual Card- No card details are stored",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_008(String browser) throws Exception {

    String tcId = "BS_008";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
    String postCode = testData.get("Post Code");
    String message = testData.get("Message");

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

      // Create customer1 - payor
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> payorDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String payorFirstName = payorDetails.get("First Name");
      String payorLastName = payorDetails.get("Last Name");

      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      searchPage.clickComplete(extentedReport);

      // Create customer2 - customer
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> contactDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport, true);
      String custFirstName = contactDetails.get("First Name");
      String custLastName = contactDetails.get("Last Name");

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(false, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(custLastName, extentedReport);
      searchPage.enterFirstName(custFirstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(custLastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      Log.softAssertThat(newquotepage.verifyNewQuotePage(true, extentedReport),
          "Verified NewQuotePage ", "Not verified NewQuotePage", driver, extentedReport, true);
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      // Payment
      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      String payorname = newquotepage.selectChangePayor(payorLastName, payorFirstName, postCode,
          extentedReport, true);
      Log.softAssertThat(newquotepage.verifyPayorName(payorname),
          payorname + " - Payor name is updated in change payor",
          payorname + " - Payor name is not updated in change payor", driver, extentedReport, true);

      Log.softAssertThat(newquotepage.verifyNoCardDetailMsg(message), "Card details is not stored",
          "Card details is stored", driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(groups = "Business_Scenario",
      description = "Change the payor in respect of a New Business Quote- Contact (Policyholder) is an existing contact- Payor is an existing contact and already exists- Payment is Monthly Direct Debit- Add new Bank account and select",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_009(String browser) throws Exception {

    String tcId = "BS_009";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
    String postCode = testData.get("Post Code");

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

      // Create customer1 - payor
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> payorDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String payorFirstName = payorDetails.get("First Name");
      String payorLastName = payorDetails.get("Last Name");

      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      searchPage.clickComplete(extentedReport);

      // Create customer2 - customer
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> contactDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport, true);
      String custFirstName = contactDetails.get("First Name");
      String custLastName = contactDetails.get("Last Name");

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(false, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(custLastName, extentedReport);
      searchPage.enterFirstName(custFirstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(custLastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      Log.softAssertThat(newquotepage.verifyNewQuotePage(true, extentedReport),
          "Verified NewQuotePage ", "Not verified NewQuotePage", driver, extentedReport, true);
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      // Payment
      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      String payorname = newquotepage.selectChangePayor(payorLastName, payorFirstName, postCode,
          extentedReport, true);
      Log.softAssertThat(newquotepage.verifyPayorName(payorname),
          payorname + " - Payor name is updated in change payor",
          payorname + " - Payor name is not updated in change payor", driver, extentedReport, true);
      newquotepage.tickSpokenDirectlyNewPayorCheckbox(extentedReport);

      // Add new Account
      newquotepage.clickAddAccountDetails(extentedReport);
      newquotepage.checkBankAccount(accNo, sortCode, extentedReport);

      String newAccName = GenericUtils.getRandomCharacters("alpha", 5);
      newquotepage.enterAccountName(newAccName, extentedReport);
      newquotepage.saveAccountDetails(extentedReport);
      newquotepage.selectAccount(sortCode, branch, newAccName, accNo, extentedReport, true);

      Log.softAssertThat(
          newquotepage.verifyAccountSelection(sortCode, branch, newAccName, accNo, extentedReport),
          "New Account detail is added and selected successfully",
          "New Account detail is not added and not selected", driver, extentedReport, true);

      newquotepage.tickCustomerAgreesCheckbox(extentedReport);
      newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for policy status
      String expectedPolicyStatus = "Accepted";
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat(
          (policyDetails.get("Status").toString().equalsIgnoreCase(expectedPolicyStatus)),
          "NB Policy Created Successfully in '" + expectedPolicyStatus + "' status",
          "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status");


      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(groups = "Business_Scenario",
      description = "Change the payor in respect of a New Business Quote- Contact (Policyholder) is an existing contact- Payor is an existing contact and already exists- Use existing Bank Account",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_010(String browser) throws Exception {

    String tcId = "BS_010";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
    String postCode = testData.get("Post Code");

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

      // Create customer1 - payor
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> payorDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String payorFirstName = payorDetails.get("First Name");
      String payorLastName = payorDetails.get("Last Name");

      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      Log.softAssertThat(newquotepage.verifyNewQuotePage(true, extentedReport),
          "Verified NewQuotePage ", "Not verified NewQuotePage", driver, extentedReport, true);
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);
      newquotepage.clickGetQuote(extentedReport);

      // Payment
      newquotepage.clickBuy(extentedReport);

      String accName = GenericUtils.getRandomCharacters("alpha", 5);
      testData.put("Account Name", accName);

      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to custdashboardpage", "Could not Navigate to custdashboardpage", driver,
          extentedReport, true);

      searchPage.clickComplete(extentedReport);

      // Create customer2 - customer
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> contactDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport, true);
      String custFirstName = contactDetails.get("First Name");
      String custLastName = contactDetails.get("Last Name");

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(false, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(custLastName, extentedReport);
      searchPage.enterFirstName(custFirstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(custLastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      Log.softAssertThat(newquotepage.verifyNewQuotePage(true, extentedReport),
          "Verified NewQuotePage ", "Not verified NewQuotePage", driver, extentedReport, true);
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      // Payment
      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      String payorname = newquotepage.selectChangePayor(payorLastName, payorFirstName, postCode,
          extentedReport, true);
      Log.softAssertThat(newquotepage.verifyPayorName(payorname),
          payorname + " - Payor name is updated in change payor",
          payorname + " - Payor name is not updated in change payor", driver, extentedReport, true);
      newquotepage.tickSpokenDirectlyNewPayorCheckbox(extentedReport);

      newquotepage.selectAccount(sortCode, branch, accName, accNo, extentedReport, true);
      Log.softAssertThat(
          newquotepage.verifyAccountSelection(sortCode, branch, accName, accNo, extentedReport),
          "Existing Bank account is selected and used successfully",
          "Existing Bank account is unselected and unused", driver, extentedReport, true);

      newquotepage.tickCustomerAgreesCheckbox(extentedReport);
      newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for policy status
      String expectedPolicyStatus = "Accepted";
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat(
          (policyDetails.get("Status").toString().equalsIgnoreCase(expectedPolicyStatus)),
          "NB Policy Created Successfully in '" + expectedPolicyStatus + "' status",
          "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status");

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(groups = "Business_Scenario",
      description = "Change the payor in respect of a Renewal Quote- Contact (Policyholder) is an existing contact- Payor is a new contact and doesn't already exist- Payment is Annual Card- No card details are stored",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_012(String browser) throws Exception {

    String tcId = "BS_012";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
    String postCode = testData.get("Post Code");
    String message = testData.get("Message");

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

      // Select card type
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment was successful", "Payment was not successful", driver, extentedReport, true);

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

      newquotepage.clickNextOne(extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickRenewalQuoteBuy(extentedReport);

      // Payment
      newquotepage.selectPaymentPlan(testData.get("Payment Plan"), extentedReport);
      newquotepage.selectPaymentMethod(testData.get("Payment Method"), extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      newquotepage.enterChangePayorDetail(custLastName, custFirstName, postCode, extentedReport,
          true);
      newquotepage.clickSearchBtn_ChangePayor(extentedReport);

      // Create New change payor
      newquotepage.clickCreateNewPayor(extentedReport);

      testData.put("Email", "");
      testData.put("Email", "ann");
      HashMap<String, String> payorDetails =
          newquotepage.enterCustomerDetails(testData, true, extentedReport, true);
      String payorFirstName = payorDetails.get("First Name");
      String payorLastName = payorDetails.get("Last Name");


      String payorname = payorFirstName + " " + payorLastName;
      newquotepage.clickSaveBtn_CreateNewPayor(extentedReport);

      Log.softAssertThat(newquotepage.verifyPayorName(payorname),
          payorname + " - Payor name is updated in change payor",
          payorname + " - Payor name is not updated in change payor", driver, extentedReport, true);

      Log.softAssertThat(newquotepage.verifyNoCardDetailMsg(message), "Card details is not stored",
          "Card details is stored", driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(groups = "Business_Scenario",
      description = "Change the payor in respect of a Renewal Quote- Contact (Policyholder) is an existing contact- Payor is a new contact and doesn't already exist- Payment is Monthly Direct Debit- Add new Bank account and select",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_013(String browser) throws Exception {

    String tcId = "BS_013";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
    String postCode = testData.get("Post Code");

    String branch = testData.get("Bank Branch");
    String accNo = testData.get("Account Number");
    String sortCode = testData.get("Sort Code");

    String ChangePaymentPlan = testData.get("ChangePayment Plan");
    String ChangePaymentMethod = testData.get("ChangePayment Method");

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

      // Select card type
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment was successful", "Payment was not successful", driver, extentedReport, true);

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

      newquotepage.clickNextOne(extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickRenewalQuoteBuy(extentedReport);

      // Payment
      newquotepage.selectPaymentPlan(ChangePaymentPlan, extentedReport);
      newquotepage.selectPaymentMethod(ChangePaymentMethod, extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      newquotepage.enterChangePayorDetail(custLastName, custFirstName, postCode, extentedReport,
          true);
      newquotepage.clickSearchBtn_ChangePayor(extentedReport);

      // Create New change payor
      newquotepage.clickCreateNewPayor(extentedReport);
      testData.put("Email", "");
      testData.put("Email", "ann");

      HashMap<String, String> payorDetails =
          newquotepage.enterCustomerDetails(testData, true, extentedReport, true);
      String payorFirstName = payorDetails.get("First Name");
      String payorLastName = payorDetails.get("Last Name");
      String payorname = payorFirstName + " " + payorLastName;
      newquotepage.clickSaveBtn_CreateNewPayor(extentedReport);

      Log.softAssertThat(newquotepage.verifyPayorName(payorname),
          payorname + " - Payor name is updated in change payor",
          payorname + " - Payor name is not updated in change payor", driver, extentedReport, true);
      newquotepage.tickSpokenDirectlyNewPayorCheckbox(extentedReport);

      // Add new Account
      newquotepage.clickAddAccountDetails(extentedReport);
      newquotepage.checkBankAccount(accNo, sortCode, extentedReport);

      String newAccName = GenericUtils.getRandomCharacters("alpha", 5);
      newquotepage.enterAccountName(newAccName, extentedReport);
      newquotepage.saveAccountDetails(extentedReport);
      newquotepage.selectAccount(sortCode, branch, newAccName, accNo, extentedReport, true);

      Log.softAssertThat(
          newquotepage.verifyAccountSelection(sortCode, branch, newAccName, accNo, extentedReport),
          "New Account detail is added and selected",
          "New Account detail is not added and not selected", driver, extentedReport, true);

      newquotepage.tickCustomerAgreesCheckbox(extentedReport);
      newquotepage.clickAcceptbtn(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for policy status
      String expectedPolicyStatus = "Accepted";
      HashMap<String, String> policyDetail = custdashboardpage.getPolicyURN(extentedReport);

      Log.softAssertThat(policyDetail.get("Position").toString().equalsIgnoreCase("Renewal"),
          "Policy position displaying as Renewal", "Latest position is not displaying");

      Log.softAssertThat(
          (policyDetail.get("Status").toString().equalsIgnoreCase(expectedPolicyStatus)),
          "Policy renewed Successfully in '" + expectedPolicyStatus + "' status",
          "Failed - Policy renewed in " + policyDetail.get("Status").toString() + " status");

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(groups = "Business_Scenario",
      description = "Change the payor in respect of a Renewal Quote- Contact (Policyholder) is an existing contact- Payor is an existing contact and already exists- Payment is Monthly Direct Debit- Add new Bank account and select",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_014(String browser) throws Exception {

    String tcId = "BS_014";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
    String postCode = testData.get("Post Code");

    String branch = testData.get("Bank Branch");
    String accNo = testData.get("Account Number");
    String sortCode = testData.get("Sort Code");

    String ChangePaymentPlan = testData.get("ChangePayment Plan");
    String ChangePaymentMethod = testData.get("ChangePayment Method");


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

      // Create customer1 - payor
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> payorDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String payorFirstName = payorDetails.get("First Name");
      String payorLastName = payorDetails.get("Last Name");

      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      searchPage.clickComplete(extentedReport);

      // Create customer - customer
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> contactDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport, true);
      String custFirstName = contactDetails.get("First Name");
      String custLastName = contactDetails.get("Last Name");

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(false, extentedReport);
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

      // Select card type
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment was successful", "Payment was not successful", driver, extentedReport, true);

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

      newquotepage.clickNextOne(extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickRenewalQuoteBuy(extentedReport);

      // Payment
      newquotepage.selectPaymentPlan(ChangePaymentPlan, extentedReport);
      newquotepage.selectPaymentMethod(ChangePaymentMethod, extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      newquotepage.enterChangePayorDetail(custLastName, custFirstName, postCode, extentedReport,
          true);
      newquotepage.clickSearchBtn_ChangePayor(extentedReport);

      // Create New change payor
      String payorname = newquotepage.selectChangePayor(payorLastName, payorFirstName, postCode,
          extentedReport, true);
      Log.softAssertThat(newquotepage.verifyPayorName(payorname),
          payorname + " - Payor name is updated in change payor",
          payorname + " - Payor name is not updated in change payor", driver, extentedReport, true);
      newquotepage.tickSpokenDirectlyNewPayorCheckbox(extentedReport);

      // Add new Account
      newquotepage.clickAddAccountDetails(extentedReport);
      newquotepage.checkBankAccount(accNo, sortCode, extentedReport);

      String newAccName = GenericUtils.getRandomCharacters("alpha", 5);
      newquotepage.enterAccountName(newAccName, extentedReport);
      newquotepage.saveAccountDetails(extentedReport);
      newquotepage.selectAccount(sortCode, branch, newAccName, accNo, extentedReport, true);

      Log.softAssertThat(
          newquotepage.verifyAccountSelection(sortCode, branch, newAccName, accNo, extentedReport),
          "New Bank Account detail is added and selected",
          "New Bank Account detail is not added and not selected", driver, extentedReport, true);

      newquotepage.tickCustomerAgreesCheckbox(extentedReport);
      newquotepage.clickAcceptbtn(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for policy status
      String expectedPolicyStatus = "Accepted";
      HashMap<String, String> policyDetail = custdashboardpage.getPolicyURN(extentedReport);

      Log.softAssertThat(policyDetail.get("Position").toString().equalsIgnoreCase("Renewal"),
          "Policy position displaying as Renewal", "Latest position is not displaying");

      Log.softAssertThat(
          (policyDetail.get("Status").toString().equalsIgnoreCase(expectedPolicyStatus)),
          "Policy renewed Successfully in '" + expectedPolicyStatus + "' status",
          "Failed - Policy renewed in " + policyDetail.get("Status").toString() + " status");


      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }

  @Test(groups = "Business_Scenario",
      description = "Change the payor in respect of a Renewal Quote- Contact (Policyholder) is an existing contact- Payor is an existing contact and already exists -Payment is Monthly Direct Debit- Use existing Bank Account",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_015(String browser) throws Exception {

    String tcId = "BS_015";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String Corecover = testData.get("Cover");
    String postCode = testData.get("Post Code");

    String branch = testData.get("Bank Branch");
    String accNo = testData.get("Account Number");
    String sortCode = testData.get("Sort Code");

    String ChangePaymentPlan = testData.get("ChangePayment Plan");
    String ChangePaymentMethod = testData.get("ChangePayment Method");


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

      // Create customer1 - payor
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> payorDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      String payorFirstName = payorDetails.get("First Name");
      String payorLastName = payorDetails.get("Last Name");

      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);

      // Confirm customer details and create customer
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
      Log.softAssertThat(newquotepage.verifyNewQuotePage(true, extentedReport),
          "Verified NewQuotePage ", "Not verified NewQuotePage", driver, extentedReport, true);
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      // Payment
      newquotepage.clickBuy(extentedReport);

      String accName = GenericUtils.getRandomCharacters("alpha", 5);

      newquotepage.selectPaymentPlan(ChangePaymentPlan, extentedReport);
      newquotepage.selectPaymentMethod(ChangePaymentMethod.toString(), extentedReport);

      newquotepage.clickAddAccountDetails(extentedReport);
      newquotepage.checkBankAccount(accNo, sortCode, extentedReport);
      newquotepage.enterAccountName(accName, extentedReport);
      newquotepage.saveAccountDetails(extentedReport);
      newquotepage.selectAccount(sortCode, branch, accName, accNo, extentedReport, true);
      newquotepage.verifyAccountSelection(sortCode, branch, accName, accNo, extentedReport);
      newquotepage.tickCustomerAgreesCheckbox(extentedReport);

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to custdashboardpage", "Could not Navigate to custdashboardpage", driver,
          extentedReport, true);

      searchPage.clickComplete(extentedReport);

      // Create customer - customer
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> contactDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport, true);
      String custFirstName = contactDetails.get("First Name");
      String custLastName = contactDetails.get("Last Name");

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(false, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // for renewal quote


      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
      custdashboardpage.clickContinueQuote(true, extentedReport);

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

      // Select card type
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment was successful", "Payment was not successful", driver, extentedReport, true);

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

      newquotepage.clickNextOne(extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickRenewalQuoteBuy(extentedReport);

      // Payment
      newquotepage.selectPaymentPlan(ChangePaymentPlan, extentedReport);
      newquotepage.selectPaymentMethod(ChangePaymentMethod, extentedReport);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, false);
      newquotepage.enterChangePayorDetail(custLastName, custFirstName, postCode, extentedReport,
          true);
      newquotepage.clickSearchBtn_ChangePayor(extentedReport);

      // Create New change payor
      String payorname = newquotepage.selectChangePayor(payorLastName, payorFirstName, postCode,
          extentedReport, true);
      Log.softAssertThat(newquotepage.verifyPayorName(payorname),
          payorname + " - Payor name is updated in change payor",
          payorname + " - Payor name is not updated in change payor", driver, extentedReport, true);

      newquotepage.selectAccount(sortCode, branch, accName, accNo, extentedReport, true);
      Log.softAssertThat(
          newquotepage.verifyAccountSelection(sortCode, branch, accName, accNo, extentedReport),
          "Existing Bank account is selected and used successfully",
          "Existing Bank account is unselected and unused", driver, extentedReport, true);

      newquotepage.tickCustomerAgreesCheckbox(extentedReport);
      newquotepage.clickAcceptbtn(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for policy status
      String expectedPolicyStatus = "Accepted";
      HashMap<String, String> policyDetail = custdashboardpage.getPolicyURN(extentedReport);

      Log.softAssertThat(policyDetail.get("Position").toString().equalsIgnoreCase("Renewal"),
          "Policy position displaying as Renewal", "Latest position is not displaying");

      Log.softAssertThat(
          (policyDetail.get("Status").toString().equalsIgnoreCase(expectedPolicyStatus)),
          "Policy renewed Successfully in '" + expectedPolicyStatus + "' status",
          "Failed - Policy renewed in " + policyDetail.get("Status").toString() + " status");

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally
  }
}
