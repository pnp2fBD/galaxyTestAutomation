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
import com.ssp.uxp_pages.GetQuotePage;
import com.ssp.uxp_pages.HomePage;
import com.ssp.uxp_pages.LoginPage;
import com.ssp.uxp_pages.NewQuotePage;
import com.ssp.uxp_pages.SearchPage;

@Listeners(EmailReport.class)
public class UXP_EC_1608 extends BaseTest {

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
        testCaseId + " - " + testDesc + " [" + browserwithos + "]", test, "");
  }

  public HashMap<String, String> getTestData(String testcaseId) {
    String env =
        Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("env");
    String className = "UXP_EC_1608_" + env;
    return DataUtils.testDatabyID(testcaseId, className);
  }

  @Test(
      description = "To check Finance Tab option is available in the Policy Detail section of Customer Dashboard",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_239(String browser) throws Exception {

    String tcId = "TC_239";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String tabName = testData.get("TabName");
    String firstName = testData.get("First Name");
    String lastName = testData.get("Last Name");
    String title = testData.get("Title");

    String CustomerName = title + " " + firstName + " " + lastName;
    String Corecover = testData.get("Cover");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("UXP SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);
      Log.softAssertThat(custdashboardpage.verifyContactName(CustomerName, extentedReport, true),
          "Verified Searched FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Details on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Verify Quote
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
      newquotepage.clickAddHomeEmergency(extentedReport);
      newquotepage.clickUpgradeNow(extentedReport);
      newquotepage.clickReCalculate(extentedReport);
      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      // Select card type
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      Log.pass("Selected VISA Card", driver, extentedReport, true);

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

      Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment was successful", "Payment was not successful", driver, extentedReport, true);

      // confirm payment
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // check for policy status
      String expectedPolicyStatus = "Accepted";
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat(
          (policyDetails.get("Status").toString().equalsIgnoreCase(expectedPolicyStatus)),
          "NB Policy Created Successfully in '" + expectedPolicyStatus + "' status",
          "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status");

      CustDashboardPage custDashboardPage = new CustDashboardPage(driver, extentedReport);

      // Check whether the Finance tab exists in Policy details page
      Log.softAssertThat(
          custDashboardPage.isThisTabAvailableInPolicyDetails(tabName, extentedReport, true),
          tabName + " Tab Presents", tabName + " Tab not available");

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(
      description = "To check below options will display under Finance Tab. 1. Finance Billing Details 2. Account",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_240(String browser) throws Exception {

    String tcId = "TC_240";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String tabName = testData.get("TabName");
    String[] sectionsLookingFor = testData.get("SectionsLookingFor").split(";");
    String firstName = testData.get("First Name");
    String lastName = testData.get("Last Name");
    String title = testData.get("Title");

    String CustomerName = title + " " + firstName + " " + lastName;
    String Corecover = testData.get("Cover");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is Verified", "Customer Dashboard page is not Verified", driver,
          extentedReport, true);
      Log.softAssertThat(custdashboardpage.verifyContactName(CustomerName, extentedReport, true),
          "Verified Searched FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Details on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Verify Quote
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
      newquotepage.clickAddHomeEmergency(extentedReport);
      newquotepage.clickUpgradeNow(extentedReport);
      newquotepage.clickReCalculate(extentedReport);
      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      // Select card type
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      Log.pass("Selected VISA Card", driver, extentedReport, true);

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

      Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment was successful", "Payment was not successful", driver, extentedReport, true);

      // confirm payment
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // check for policy status
      String expectedPolicyStatus = "Accepted";
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat(
          (policyDetails.get("Status").toString().equalsIgnoreCase(expectedPolicyStatus)),
          "NB Policy Created Successfully in '" + expectedPolicyStatus + "' status",
          "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status");
      CustDashboardPage custDashboardPage = new CustDashboardPage(driver, extentedReport);

      // Check if Finance tab available
      Log.softAssertThat(
          custDashboardPage.isThisTabAvailableInPolicyDetails(tabName, extentedReport, true),
          tabName + " Tab Presents", tabName + " Tab not available");
      // Click on Finance Tab
      custDashboardPage.clickTab(tabName, extentedReport, true);

      // Check the given sections are available in Finance tab and display
      // it
      Log.softAssertThat(
          custDashboardPage.showTabSectionsIfAvailable(sectionsLookingFor, extentedReport, true),
          "Tab sections available", "No tab sctions available");
      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(
      description = "To check below options will display under Finance Billing Details screen. Payer, Payment Method, Payment Plan",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_241(String browser) throws Exception {

    String tcId = "TC_241";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    // String policyNumber = testData.get("PolicyNumber");
    String tabName = testData.get("TabName");
    String[] sectionsLookingFor = testData.get("SectionsLookingFor").split(";");
    String firstName = testData.get("First Name");
    String lastName = testData.get("Last Name");
    String title = testData.get("Title");
    String[] financialBillingDetailsFields = testData.get("FieldsInSectionofTabs").split(";");
    String CustomerName = title + " " + firstName + " " + lastName;
    String Corecover = testData.get("Cover");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is Verified", "Customer Dashboard page is not Verified", driver,
          extentedReport, true);
      Log.softAssertThat(custdashboardpage.verifyContactName(CustomerName, extentedReport, true),
          "Verified Searched FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Details on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Verify Quote
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
      newquotepage.clickAddHomeEmergency(extentedReport);
      newquotepage.clickUpgradeNow(extentedReport);
      newquotepage.clickReCalculate(extentedReport);
      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      // Select card type
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      Log.pass("Selected VISA Card", driver, extentedReport, true);

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

      Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment was successful", "Payment was not successful", driver, extentedReport, true);

      // confirm payment
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is Verified", "Customer Dashboard page is not Verified", driver,
          extentedReport, true);

      // check for policy status
      String expectedPolicyStatus = "Accepted";
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat(
          (policyDetails.get("Status").toString().equalsIgnoreCase(expectedPolicyStatus)),
          "NB Policy Created Successfully in '" + expectedPolicyStatus + "' status",
          "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status");
      CustDashboardPage custDashboardPage = new CustDashboardPage(driver, extentedReport);

      // Check whether Finance tab available in Policy Details page
      Log.softAssertThat(
          custDashboardPage.isThisTabAvailableInPolicyDetails(tabName, extentedReport, true),
          tabName + " Tab Presents", tabName + " Tab not available");

      // Click on Finance Tab
      custDashboardPage.clickTab(tabName, extentedReport, true);

      // Check and display the section is available in Finance Tab
      Log.softAssertThat(
          custDashboardPage.showTabSectionsIfAvailable(sectionsLookingFor, extentedReport, true),
          "Tab sections available", "No tab sctions available");

      // Check the fields are available in Finance Billing Details Section
      Log.softAssertThat(custDashboardPage
          .checkFieldsOfFinanceBillingDetails(financialBillingDetailsFields, extentedReport, true),
          "Section Fields available", "Section fields missing");
      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(
      description = "To check below options will display under Account screen. Include Reveals checkbox,  Outstanding checking checkbox, Outstanding Amount, Financial account entry Grid, Take Payment button",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_242(String browser) throws Exception {

    String tcId = "TC_242";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String tabName = testData.get("TabName");
    String[] sectionsLookingFor = testData.get("SectionsLookingFor").split(";");
    String firstName = testData.get("First Name");
    String lastName = testData.get("Last Name");
    String title = testData.get("Title");
    String CustomerName = title + " " + firstName + " " + lastName;
    String Corecover = testData.get("Cover");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is Verified", "Customer Dashboard page is not Verified", driver,
          extentedReport, true);
      Log.softAssertThat(custdashboardpage.verifyContactName(CustomerName, extentedReport, true),
          "Verified Searched FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Details on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Verify Quote
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
      newquotepage.clickAddHomeEmergency(extentedReport);
      newquotepage.clickUpgradeNow(extentedReport);
      newquotepage.clickReCalculate(extentedReport);
      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      // Select card type
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      Log.pass("Selected VISA Card", driver, extentedReport, true);

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

      Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment was successful", "Payment was not successful", driver, extentedReport, true);

      // confirm payment
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is Verified", "Customer Dashboard page is not Verified", driver,
          extentedReport, true);

      // check for policy status
      String expectedPolicyStatus = "Accepted";
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat(
          (policyDetails.get("Status").toString().equalsIgnoreCase(expectedPolicyStatus)),
          "NB Policy Created Successfully in '" + expectedPolicyStatus + "' status",
          "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status");
      CustDashboardPage custDashboardPage = new CustDashboardPage(driver, extentedReport);

      Log.softAssertThat(
          custDashboardPage.isThisTabAvailableInPolicyDetails(tabName, extentedReport, true),
          tabName + " Tab Presents", tabName + " Tab not available");

      // Click on Finance Tab
      custDashboardPage.clickTab(tabName, extentedReport, true);

      // Check and display the section is available in Finance Tab
      Log.softAssertThat(
          custDashboardPage.showTabSectionsIfAvailable(sectionsLookingFor, extentedReport, true),
          "Tab sections available", "No tab sctions available");
      Thread.sleep(10000);
      // Check the fields are available in Finance Billing Details Section
      Log.softAssertThat(custDashboardPage.verifyAccountSectionFields(extentedReport, true),
          "Fields 'Include Reversal', 'Outstanding', 'Outstanding amount', 'Financial Table', 'Take Payment Button' are available in Account section of Finance Table",
          "Fields are missing in Account Section of Finance Table", driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(enabled = false,
      description = "To check below options will display under 'Financial account entry Grid' screen. 'Entry Date,Due Date,Type,Due,Paid,Balance,Receipts",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_243(String browser) throws Exception {

    String tcId = "TC_243";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String tabName = testData.get("TabName");
    String[] sectionsLookingFor = testData.get("SectionsLookingFor").split(";");
    String firstName = testData.get("First Name");
    String lastName = testData.get("Last Name");
    String title = testData.get("Title");
    String CustomerName = title + " " + firstName + " " + lastName;
    String Corecover = testData.get("Cover");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is Verified", "Customer Dashboard page is not Verified", driver,
          extentedReport, true);
      Log.softAssertThat(custdashboardpage.verifyContactName(CustomerName, extentedReport, true),
          "Verified Searched FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Details on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Verify Quote
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
      newquotepage.clickAddHomeEmergency(extentedReport);
      newquotepage.clickUpgradeNow(extentedReport);
      newquotepage.clickReCalculate(extentedReport);
      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      // Select card type
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      Log.pass("Selected VISA Card", driver, extentedReport, true);

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

      Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment was successful", "Payment was not successful", driver, extentedReport, true);

      // confirm payment
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is Verified", "Customer Dashboard page is not Verified", driver,
          extentedReport, true);

      // check for policy status
      String expectedPolicyStatus = "Accepted";
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat(
          (policyDetails.get("Status").toString().equalsIgnoreCase(expectedPolicyStatus)),
          "NB Policy Created Successfully in '" + expectedPolicyStatus + "' status",
          "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status");

      CustDashboardPage custDashboardPage = new CustDashboardPage(driver, extentedReport);
      Log.softAssertThat(
          custDashboardPage.isThisTabAvailableInPolicyDetails(tabName, extentedReport, true),
          tabName + " Tab Presents", tabName + " Tab not available");
      // Click on Finance Tab
      custDashboardPage.clickTab(tabName, extentedReport, true);

      // Check and display the section is available in Finance Tab
      Log.softAssertThat(
          custDashboardPage.showTabSectionsIfAvailable(sectionsLookingFor, extentedReport, true),
          "Tab sections available", "No tab sctions available");

      // Check the fields are available in Finance Billing Details Section
      Log.softAssertThat(
          custDashboardPage.uielement
              .verifyPageElements(
                  Arrays.asList("finTabAccountSecnIncludeReversalField",
                      "finTabAccountSecnOutstandingField", "finTabAccountSecnOutstandingAmtField",
                      "finTabAccountSecnFinanceTbl", "finTabAccountSecnTakePaymentBtn"),
                  custDashboardPage),
          "Fields 'Include Reversal', 'Outstanding', 'Outstanding amount', 'Financial Table', 'Take Payment Button' are available in Account section of Finance Table",
          "Fields are missing in Account Section of Finance Table", driver, extentedReport, true);
      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  /**
   * Creates a new customer using the testData given and navigates to Acceptance tab.
   * 
   * @param driver
   * @param testData
   * @param upgradePolicyFlag - true if need to upgrade policy to 5 star from 3 star; false
   *        otherwise
   * @param addCoverFlag - true if need to add cover to policy; false otherwise
   * @param extentedReport
   * @return the <code>HashMap</code> representation of testData, or null if no <code>HashMap</code>
   *         representation is applicable
   * @throws Exception
   */
  private HashMap<String, String> createACustomerAndNavigateToAcceptanceTab(WebDriver driver,
      HashMap<String, String> testData, boolean upgradePolicyFlag, boolean addCoverFlag,
      ExtentTest extentedReport) throws Exception {
    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

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

      // Create customer
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      testData = searchPage.enterCustomerDetails(testData, true, extentedReport, true);
      String title = testData.get("Title");
      String firstName = testData.get("First Name");
      String lastName = testData.get("Last Name");
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.message("Created the customer successfully", extentedReport);

      // Customer verification
      String customerName = title + " " + firstName + " " + lastName;
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);
      Log.softAssertThat(custdashboardpage.verifyContactName(customerName, extentedReport, true),
          "Verified the customer name on customer dashboard",
          "Not verified the customer details on dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Verify Quote
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
      if (upgradePolicyFlag) {
        newquotepage.clickUpgradeNow(extentedReport);
        Log.softAssertThat(newquotepage.verifyUpgradePolicyTo5Star(), "Policy upgraded to 5 star",
            "Policy not upgraded to 5 star", driver, extentedReport, true);
        newquotepage.clickReCalculate(extentedReport);
      }
      if (addCoverFlag) {
        String[] coversToAdd = testData.get("coverToSelect").toString().split(",");
        for (int i = 0; i < coversToAdd.length; i++) {
          if (coversToAdd[i].endsWith("_Remove")) {
            continue;
          }
          String ins_RowtoInteract =
              newquotepage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
          newquotepage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract, true,
              extentedReport);
        }
        newquotepage.clickReCalculate(extentedReport);
      }
      testData = newquotepage.grabPaymentDetailsFromCoverTab(testData, extentedReport, true);
      newquotepage.clickBuy(extentedReport);
    } catch (Exception e) {
      throw new Exception("Exception creating customer. " + e);
    }
    return testData;
  }

  /**
   * Creates a new customer and policy using the testData given and confirms that the policy is
   * created with 'Accepted' status.
   * 
   * @param driver
   * @param testData
   * @param upgradePolicyFlag - true if need to upgrade policy to 5 star from 3 star; false
   *        otherwise
   * @param addCoverFlag - true if need to add cover to policy; false otherwise
   * @param extentedReport
   * @return the <code>HashMap</code> representation of testData, or null if no <code>HashMap</code>
   *         representation is applicable
   * @throws Exception
   */
  private HashMap<String, String> createACustomerAndPolicy(WebDriver driver,
      HashMap<String, String> testData, boolean upgradePolicyFlag, boolean addCoverFlag,
      ExtentTest extentedReport) throws Exception {
    try {
      testData = createACustomerAndNavigateToAcceptanceTab(driver, testData, upgradePolicyFlag,
          addCoverFlag, extentedReport);
      NewQuotePage newquotepage = new NewQuotePage(driver, extentedReport);

      // Payment
      newquotepage.selectPayment(testData, true, extentedReport);

      // Acceptance tab - Payment details verification
      Log.softAssertThat(
          newquotepage.verifyPaymentDetailsAtAcceptanceTab(testData, extentedReport, true),
          "Verified that the payment details at Acceptance tab matches with the details at Cover tab",
          "The payment details at Acceptance tab does not match with the details at Cover tab",
          driver, extentedReport, true);

      CustDashboardPage custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
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
    } catch (Exception e) {
      throw new Exception("Exception creating customer(s). " + e);
    }
    return testData;
  }

  /**
   * Creates two new customers using the testData given.
   * 
   * @param driver
   * @param testData
   * @param extentedReport
   * @return the <code>HashMap</code> representation of testData, or null if no <code>HashMap</code>
   *         representation is applicable
   * @throws Exception
   */
  private HashMap<String, String> createTwoCustomers(WebDriver driver,
      HashMap<String, String> testData, ExtentTest extentedReport) throws Exception {
    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String brandname = testData.get("Brand Name");

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

      // Create first customer
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      testData = searchPage.enterCustomerDetails(testData, true, extentedReport, true);
      String title = testData.get("Title");
      String firstName = testData.get("First Name");
      String lastName = testData.get("Last Name");
      testData.put("Customer_1_First_Name", firstName);
      testData.put("Customer_1_Last_Name", lastName);
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.message("Created customer #1 successfully", extentedReport);

      // Customer #1 verification
      String customerName = title + " " + firstName + " " + lastName;
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);
      Log.softAssertThat(custdashboardpage.verifyContactName(customerName, extentedReport, true),
          "Verified first customer name on customer dashboard",
          "Not verified first customer details on dashboard", driver, extentedReport, true);

      // Create second customer
      custdashboardpage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      searchPage.clickCreateCustomer(true, extentedReport);
      testData = searchPage.enterCustomerDetails(testData, true, extentedReport, true);
      firstName = testData.get("First Name");
      lastName = testData.get("Last Name");
      testData.put("Customer_2_First_Name", firstName);
      testData.put("Customer_2_Last_Name", lastName);
      searchPage.confirmCreateCustomer(true, extentedReport);
      Log.message("Created customer #2 successfully", extentedReport);

      // Customer #2 verification
      customerName = title + " " + firstName + " " + lastName;
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);
      Log.softAssertThat(custdashboardpage.verifyContactName(customerName, extentedReport, true),
          "Verified first customer name on customer dashboard",
          "Not verified first customer details on dashboard", driver, extentedReport, true);
    } catch (Exception e) {
      throw new Exception("Exception creating customer(s). " + e);
    }
    return testData;
  }

  @Test(
      description = "Verify the checkbox selection in the label \"I have spoken directly to the Policy Payor and he/she has confirmed acceptance to making the payment\" when the policy holder is not the policy payor.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider",
      groups = {"Business_Scenario"})
  public void TC_664_665_671_881_883(String browser) throws Exception {
    String tcId = "TC_664_665_671_881_883";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String description = testData.get("Description");
    String Corecover = testData.get("Cover");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      testData = createTwoCustomers(driver, testData, extentedReport);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport).get();

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Verify Quote
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
      testData = newquotepage.grabPaymentDetailsFromCoverTab(testData, extentedReport, true);
      newquotepage.clickBuy(extentedReport);

      // Payment
      newquotepage.selectPaymentPlan("Monthly", extentedReport);
      newquotepage.selectPaymentMethod("Direct Debit", extentedReport);

      // No bank details message verification
      Log.softAssertThat(
          newquotepage.verifyNoAccountDetailsErrorMessage(newquotepage.ERROR_MSG_NO_ACCOUNT_DETAILS,
              extentedReport),
          "Verified that the expected error message is displayed when no bank account details are added",
          "The expected error message is not displayed when no bank account details are added",
          driver, extentedReport, true);

      // Includes fee verification
      String expectedFee = "0.00";
      Log.softAssertThat(newquotepage.verifyIncludesFee(expectedFee, extentedReport),
          "Verified that the 'Includes a fee of' field has expected data",
          "The 'Includes a fee of' field does have the data as expected", driver, extentedReport,
          true);

      // Acceptance tab - Payment details verification
      Log.softAssertThat(
          newquotepage.verifyPaymentDetailsAtAcceptanceTab(testData, extentedReport, true),
          "Verified that the payment details at Acceptance tab matches with the details at Cover tab",
          "The payment details at Acceptance tab does not match with the details at Cover tab",
          driver, extentedReport, true);

      // Payment Schedule table verification
      Log.softAssertThat(newquotepage.verifyPaymentScheduleTable(testData, extentedReport, true),
          "The 'Payment Schedule' table verification is successful",
          "The 'Payment Schedule' table verification is unsuccessful", driver, extentedReport,
          true);

      // Verification of change payor check box absence for default payor
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElementsDoNotExist(
              Arrays.asList("chkSpokenDirectlyNewPayor"), newquotepage),
          "Verified that the 'I have spoken to New Payor...' checkbox is absent for the default payor",
          "The 'I have spoken to New Payor...' checkbox is present unexpectly for the default payor",
          driver, extentedReport, true);

      // Change payor
      newquotepage.clickChangePayorButton(extentedReport, true);

      String payorname = newquotepage.enterChangePayorDetails(testData.get("Customer_1_Last_Name"),
          testData.get("Customer_1_First_Name"), testData.get("Post Code"), extentedReport, true);
      Log.softAssertThat(newquotepage.verifyPayorName(payorname),
          "The user is able to change the payor of the policy, once the quote reached Acceptance page : "
              + payorname,
          "The user is not able to change the payor of the policy, once the quote reached Acceptance page : "
              + payorname,
          driver, extentedReport, true);

      // Payment details verification after changing payor
      Log.softAssertThat(
          newquotepage.verifyPaymentDetailsAtAcceptanceTab(testData, extentedReport, true),
          "Verified that the payment details at Acceptance tab matches with the details at Cover tab",
          "The payment details at Acceptance tab does not match with the details at Cover tab",
          driver, extentedReport, true);

      // Payment Schedule table verification after changing payor
      Log.softAssertThat(newquotepage.verifyPaymentScheduleTable(testData, extentedReport, true),
          "The 'Payment Schedule' table verification is successful",
          "The 'Payment Schedule' table verification is unsuccessful", driver, extentedReport,
          true);

      newquotepage.selectPayment(testData, true, extentedReport);

      // Verification without checkbox selection
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElementsDoNotExist(Arrays.asList("btnAcceptPayment"),
              newquotepage),
          "Verified that the Payment 'Accept' button is absent as 'I have spoken to New Payor...' checkbox is unchecked",
          "The Payment 'Accept' button is present unexpectly though 'I have spoken to New Payor...' checkbox is unchecked",
          driver, extentedReport, true);

      // Verification with checkbox selection
      newquotepage.tickSpokenDirectlyNewPayorCheckbox(extentedReport);
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("btnAcceptPayment"),
              newquotepage),
          "Verified that the Payment 'Accept' button is present as 'I have spoken to New Payor...' checkbox is checked",
          "The Payment 'Accept' button is absent unexpectly though 'I have spoken to New Payor...' checkbox is checked",
          driver, extentedReport, true);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
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

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Verify the preferred payment day option by selecting the payment plan which is allowed to change the preferred payment day.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_667(String browser) throws Exception {
    String tcId = "TC_667";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      testData =
          createACustomerAndNavigateToAcceptanceTab(driver, testData, false, false, extentedReport);
      NewQuotePage newquotepage = new NewQuotePage(driver, extentedReport);

      // Payment Schedule columns verification with default date
      String paymentPlan = testData.get("Payment Plan");
      String paymentMethod = testData.get("Payment Method");
      newquotepage.selectPaymentPlan(paymentPlan, extentedReport);
      newquotepage.selectPaymentMethod(paymentMethod, extentedReport);

      // Acceptance tab - Payment details verification
      Log.softAssertThat(
          newquotepage.verifyPaymentDetailsAtAcceptanceTab(testData, extentedReport, true),
          "Verified that the payment details at Acceptance tab matches with the details at Cover tab",
          "The payment details at Acceptance tab does not match with the details at Cover tab",
          driver, extentedReport, true);

      String preferredPaymentDay = DateTimeUtility.getCurrentDate().split("/")[0];
      testData.put("Preferred Payment Day", preferredPaymentDay);
      Log.softAssertThat(newquotepage.verifyPaymentScheduleTable(testData, extentedReport, true),
          "The 'Payment Schedule' table verification is successful",
          "The 'Payment Schedule' table verification is unsuccessful", driver, extentedReport,
          true);

      // Payment - Error Verification of Preferred Payment Day without Bank account
      preferredPaymentDay = "" + GenericUtils.getRandomNumberBetween(1, 31);
      testData.put("Preferred Payment Day", preferredPaymentDay);
      newquotepage.selectPreferredPaymentDay(preferredPaymentDay, extentedReport, true);
      Log.softAssertThat(
          newquotepage.verifyAccountDetailsErrorMessage(newquotepage.ERROR_MSG_ACCOUNT_DETAILS,
              extentedReport),
          "Verified that the expected error message is displayed for the selection of 'Preferred Payment Day' without bank account details",
          "The expected error message is not displayed for the selection of 'Preferred Payment Day' without bank account details",
          driver, extentedReport, true);

      // Payment Schedule columns verification with 'Preferred Payment Day' selection
      newquotepage.selectPayment(testData, true, extentedReport);
      preferredPaymentDay = "" + GenericUtils.getRandomNumberBetween(1, 31);
      testData.put("Preferred Payment Day", preferredPaymentDay);
      newquotepage.selectPreferredPaymentDay(preferredPaymentDay, extentedReport, true);
      Log.softAssertThat(newquotepage.verifyPaymentScheduleTable(testData, extentedReport, true),
          "The 'Payment Schedule' table verification is successful",
          "The 'Payment Schedule' table verification is unsuccessful", driver, extentedReport,
          true);

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Verify 'Add Account Details' section with valid/invalid account number and sort code.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_677_679_680(String browser) throws Exception {
    String tcId = "TC_677_679_680";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      testData =
          createACustomerAndNavigateToAcceptanceTab(driver, testData, false, false, extentedReport);
      NewQuotePage newquotepage = new NewQuotePage(driver, extentedReport);

      // Invalid data verification
      newquotepage.selectPaymentPlan("Monthly", extentedReport);
      newquotepage.selectPaymentMethod("Direct Debit", extentedReport);

      // Acceptance tab - Payment details verification
      Log.softAssertThat(
          newquotepage.verifyPaymentDetailsAtAcceptanceTab(testData, extentedReport, true),
          "Verified that the payment details at Acceptance tab matches with the details at Cover tab",
          "The payment details at Acceptance tab does not match with the details at Cover tab",
          driver, extentedReport, true);

      newquotepage.clickAddAccountDetails(extentedReport);
      String validAccNo = testData.get("Account Number");
      String validSortCode = testData.get("Sort Code");
      String invalidAccNo = testData.get("Invalid Account Number");
      String invalidSortCode = testData.get("Invalid Sort Code");
      String expectedErrMsg = newquotepage.ERROR_MSG_ADD_ACCOUNT_DETAILS;

      // Invalid sort code
      newquotepage.checkBankAccount(validAccNo, invalidSortCode, extentedReport);
      Log.softAssertThat(
          newquotepage.verifyAddAccountDetailsErrorMessage(expectedErrMsg, extentedReport),
          "Verified that the error message is displayed for the invalid sort code given",
          "The error message is not displayed for the invalid sort code given", driver,
          extentedReport, true);

      // Invalid account number
      newquotepage.checkBankAccount(invalidAccNo, validSortCode, extentedReport);
      Log.softAssertThat(
          newquotepage.verifyAddAccountDetailsErrorMessage(expectedErrMsg, extentedReport),
          "Verified that the error message is displayed for the invalid account number given",
          "The error message is not displayed for the invalid account number given", driver,
          extentedReport, true);

      // Invalid sort code and account number
      newquotepage.checkBankAccount(invalidAccNo, invalidSortCode, extentedReport);
      Log.softAssertThat(
          newquotepage.verifyAddAccountDetailsErrorMessage(expectedErrMsg, extentedReport),
          "Verified that the error message is displayed for the invalid sort code and account number given",
          "The error message is not displayed for the invalid sort code and account number given",
          driver, extentedReport, true);
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElementsDoNotExist(Arrays.asList("btnAcceptPayment"),
              newquotepage),
          "Verified that the Payment 'Accept' button is absent",
          "The Payment 'Accept' button is present unexpectly", driver, extentedReport, true);

      // Valid data verification
      String expectedBranchName = testData.get("Bank Branch");
      newquotepage.checkBankAccount(validAccNo, validSortCode, extentedReport);
      Log.softAssertThat(newquotepage.verifyBranchName(expectedBranchName, extentedReport),
          "Verified that the branch name field has expected value and in edit-locked state",
          "The branch name field does not display expected branch name and/or the field is editable",
          driver, extentedReport, true);

      String accountName = testData.get("Last Name");
      newquotepage.enterAccountName(accountName, extentedReport);
      newquotepage.saveAccountDetails(extentedReport);
      newquotepage.selectAccount(validSortCode, expectedBranchName, accountName, validAccNo,
          extentedReport, true);

      // Account details verification
      Log.softAssertThat(
          newquotepage.verifyAccountSelection(validSortCode, expectedBranchName, accountName,
              validAccNo, extentedReport),
          "Verified that the account selection is successful",
          "The account selection is not successful", driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Verify the existing bank account details listing and the account selection by logging with the user who is having multiple bank accounts.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_681_685(String browser) throws Exception {
    String tcId = "TC_681_685";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      testData =
          createACustomerAndNavigateToAcceptanceTab(driver, testData, false, false, extentedReport);
      NewQuotePage newquotepage = new NewQuotePage(driver, extentedReport);

      // Add first account details and verifies the selection
      String accName1 = GenericUtils.getRandomCharacters("alpha", 5);
      testData.put("Account Name", accName1);
      newquotepage.selectPayment(testData, true, extentedReport);

      // Acceptance tab - Payment details verification
      Log.softAssertThat(
          newquotepage.verifyPaymentDetailsAtAcceptanceTab(testData, extentedReport, true),
          "Verified that the payment details at Acceptance tab matches with the details at Cover tab",
          "The payment details at Acceptance tab does not match with the details at Cover tab",
          driver, extentedReport, true);

      // Add second account details and verifies the selection
      String accName2 = GenericUtils.getRandomCharacters("alpha", 5);
      testData.put("Account Name", accName2);
      newquotepage.selectPayment(testData, true, extentedReport);

      CustDashboardPage custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
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

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Verify Quote
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

      // Add third account details and verifies the selection
      String accName3 = GenericUtils.getRandomCharacters("alpha", 5);
      testData.put("Account Name", accName3);
      newquotepage.selectPayment(testData, true, extentedReport);

      // Select and verify already created accounts
      String sortCode = testData.get("Sort Code");
      String branch = (testData.containsKey("Bank Branch")) ? testData.get("Bank Branch")
          : "Bootle Centre, Santander, Liverpool";
      String accNo = testData.get("Account Number");
      // Account 1
      String accName = accName1;
      newquotepage.selectAccount(sortCode, branch, accName, accNo, extentedReport, true);
      Log.softAssertThat(
          newquotepage.verifyAccountSelection(sortCode, branch, accName, accNo, extentedReport),
          "Verified that the account selection is successful",
          "The account selection is not successful", driver, extentedReport, true);
      // Account 2
      accName = accName2;
      newquotepage.selectAccount(sortCode, branch, accName, accNo, extentedReport, true);
      Log.softAssertThat(
          newquotepage.verifyAccountSelection(sortCode, branch, accName, accNo, extentedReport),
          "Verified that the account selection is successful",
          "The account selection is not successful", driver, extentedReport, true);
      // Account 3
      accName = accName3;
      newquotepage.selectAccount(sortCode, branch, accName, accNo, extentedReport, true);
      Log.softAssertThat(
          newquotepage.verifyAccountSelection(sortCode, branch, accName, accNo, extentedReport),
          "Verified that the account selection is successful",
          "The account selection is not successful", driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Verify the policy status, total paid and total balance in Customer Dashboard when the CCA checked the DD guarantee checkbox and clicked on ACCEPT button.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_688_884(String browser) throws Exception {
    String tcId = "TC_688_884";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      testData =
          createACustomerAndNavigateToAcceptanceTab(driver, testData, false, false, extentedReport);
      NewQuotePage newquotepage = new NewQuotePage(driver, extentedReport);

      // Payment - Add Account Details
      newquotepage.selectPaymentPlan("Monthly", extentedReport);
      newquotepage.selectPaymentMethod("Direct Debit", extentedReport);

      // Acceptance tab - Payment details verification
      Log.softAssertThat(
          newquotepage.verifyPaymentDetailsAtAcceptanceTab(testData, extentedReport, true),
          "Verified that the payment details at Acceptance tab matches with the details at Cover tab",
          "The payment details at Acceptance tab does not match with the details at Cover tab",
          driver, extentedReport, true);

      newquotepage.clickAddAccountDetails(extentedReport);
      String accNo = testData.get("Account Number");
      String sortCode = testData.get("Sort Code");
      String expectedBranchName = testData.get("Bank Branch");
      newquotepage.checkBankAccount(accNo, sortCode, extentedReport);
      Log.softAssertThat(newquotepage.verifyBranchName(expectedBranchName, extentedReport),
          "Verified that the branch name field has expected value and in edit-locked state",
          "The branch name field does not display expected branch name and/or the field is editable",
          driver, extentedReport, true);

      String accountName = testData.get("Last Name");
      newquotepage.enterAccountName(accountName, extentedReport);
      newquotepage.saveAccountDetails(extentedReport);

      // Account details verification
      newquotepage.selectAccount(sortCode, expectedBranchName, accountName, accNo, extentedReport,
          true);
      Log.softAssertThat(
          newquotepage.verifyAccountSelection(sortCode, expectedBranchName, accountName, accNo,
              extentedReport),
          "Verified that the account selection is successful",
          "The account selection is not successful", driver, extentedReport, true);

      newquotepage.tickCustomerAgreesCheckbox(extentedReport);
      CustDashboardPage custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
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

      // Premium summary details verification
      Log.softAssertThat(custdashboardpage.verifyPremiumSummary(testData, extentedReport, true),
          "Verified that the premium summary details are as expected",
          "The premium summary details are not as expected");

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Verify the policy status in Customer Dashboard when the CCA checked the DD guarantee checkbox and clicked on CANCEL button.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_689(String browser) throws Exception {
    String tcId = "TC_689";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      testData =
          createACustomerAndNavigateToAcceptanceTab(driver, testData, false, false, extentedReport);
      NewQuotePage newquotepage = new NewQuotePage(driver, extentedReport);

      // Payment
      newquotepage.selectPaymentPlan("Monthly", extentedReport);
      newquotepage.selectPaymentMethod("Direct Debit", extentedReport);

      // Acceptance tab - Payment details verification
      Log.softAssertThat(
          newquotepage.verifyPaymentDetailsAtAcceptanceTab(testData, extentedReport, true),
          "Verified that the payment details at Acceptance tab matches with the details at Cover tab",
          "The payment details at Acceptance tab does not match with the details at Cover tab",
          driver, extentedReport, true);

      newquotepage.tickCustomerAgreesCheckbox(extentedReport);

      // Cancel payment
      CustDashboardPage custdashboardpage = newquotepage.cancelPayment(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for policy status
      String expectedPolicyStatus = "Quoted";
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat(
          (policyDetails.get("Status").toString().equalsIgnoreCase(expectedPolicyStatus)),
          "NB Policy Created Successfully in '" + expectedPolicyStatus + "' status",
          "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status");

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Verify the policy status in Customer Dashboard when the CCA without checking the DD guarantee checkbox and clicked on CANCEL button.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_690(String browser) throws Exception {
    String tcId = "TC_690";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      testData =
          createACustomerAndNavigateToAcceptanceTab(driver, testData, false, false, extentedReport);
      NewQuotePage newquotepage = new NewQuotePage(driver, extentedReport);

      // Payment
      newquotepage.selectPaymentPlan("Monthly", extentedReport);
      newquotepage.selectPaymentMethod("Direct Debit", extentedReport);

      // Acceptance tab - Payment details verification
      Log.softAssertThat(
          newquotepage.verifyPaymentDetailsAtAcceptanceTab(testData, extentedReport, true),
          "Verified that the payment details at Acceptance tab matches with the details at Cover tab",
          "The payment details at Acceptance tab does not match with the details at Cover tab",
          driver, extentedReport, true);

      // Cancel payment
      CustDashboardPage custdashboardpage = newquotepage.cancelPayment(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for policy status
      String expectedPolicyStatus = "Quoted";
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat(
          (policyDetails.get("Status").toString().equalsIgnoreCase(expectedPolicyStatus)),
          "NB Policy Created Successfully in '" + expectedPolicyStatus + "' status",
          "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status");

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Verify the policy Status should be Inactive in Customer Dashboard when the CCA processing MTA and backout the transaction",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_692(String browser) throws Exception {
    String tcId = "TC_692";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      testData = createACustomerAndPolicy(driver, testData, false, false, extentedReport);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport);

      custdashboardpage.selectMTAfromManagePolicyTab(extentedReport, true);
      custdashboardpage.enterMTADetails(testData, extentedReport, true);
      GetQuotePage getQuotePage = custdashboardpage.verifyGetQuotePage(extentedReport);
      Log.message("Get quote page loaded successfully", driver, extentedReport, true);
      getQuotePage.clickBuy(extentedReport);
      getQuotePage.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
      getQuotePage.clickAddMTA(extentedReport, false);
      getQuotePage.clickAddAllMTANextButton(extentedReport, true);

      // Cancel payment
      custdashboardpage = getQuotePage.cancelPayment(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for MTA transaction status
      String expectedPosition = "MidTermAdjustment";
      String expectedTransactionStatus = "Inactive";
      Log.softAssertThat(
          custdashboardpage.verifyTransactionStatus(expectedPosition, expectedTransactionStatus,
              extentedReport, true),
          "Verified that the MTA transaction status is '" + expectedTransactionStatus
              + "' as expected",
          "The MTA transaction status is not as expected");

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Verify the policy Status should be Qouted Status in Customer Dashboard when the CCA processing MTA clicking on SAVE Quote button.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_693(String browser) throws Exception {
    String tcId = "TC_693";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      testData = createACustomerAndPolicy(driver, testData, false, false, extentedReport);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport);

      custdashboardpage.selectMTAfromManagePolicyTab(extentedReport, true);
      custdashboardpage.enterMTADetails(testData, extentedReport, true);
      GetQuotePage getQuotePage = custdashboardpage.verifyGetQuotePage(extentedReport);
      Log.message("Get quote page loaded successfully", driver, extentedReport, true);
      getQuotePage.clickBuy(extentedReport);
      getQuotePage.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
      getQuotePage.clickAddMTA(extentedReport, false);
      getQuotePage.clickAddAllMTANextButton(extentedReport, true);

      // Save Quote
      custdashboardpage = getQuotePage.saveQuote(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for MTA transaction status
      String expectedPosition = "MidTermAdjustment";
      String expectedTransactionStatus = "Inactive";
      Log.softAssertThat(
          custdashboardpage.verifyTransactionStatus(expectedPosition, expectedTransactionStatus,
              extentedReport, true),
          "Verified that the MTA transaction status is '" + expectedTransactionStatus
              + "' as expected",
          "The MTA transaction status is not as expected");

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Verify the ACCEPT button and MTA adjustment radio buttons presence and functionality during Monthly Direct Debit payment mode functionality without/after cliking 'Guarantee...' checkbox after MTA adjustment.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider",
      groups = {"Business_Scenario"})
  public void TC_699_700_707_708_713_716(String browser) throws Exception {
    String tcId = "TC_699_700_707_708_713_716";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      testData = createACustomerAndPolicy(driver, testData, false, false, extentedReport);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport);

      custdashboardpage.selectMTAfromManagePolicyTab(extentedReport, true);
      custdashboardpage.enterMTADetails(testData, extentedReport, true);
      GetQuotePage getQuotePage = custdashboardpage.verifyGetQuotePage(extentedReport);
      Log.message("Get quote page loaded successfully", driver, extentedReport, true);

      String[] coversToAdd = testData.get("coverToSelect").toString().split(",");
      for (int i = 0; i < coversToAdd.length; i++) {
        if (coversToAdd[i].endsWith("_Remove")) {
          continue;
        }
        String ins_RowtoInteract =
            getQuotePage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
        getQuotePage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract, true,
            extentedReport);
      }

      getQuotePage.clickReCalculate(extentedReport);
      testData =
          getQuotePage.grabPaymentDetailsFromAdjustmentCoverTab(testData, extentedReport, true);
      getQuotePage.clickBuy(extentedReport);
      getQuotePage.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
      getQuotePage.clickAddMTA(extentedReport, false);
      getQuotePage.clickAddAllMTANextButton(extentedReport, true);
      Log.softAssertThat(getQuotePage.verifyMTAAdjustmentRadioButtons(false, extentedReport, true),
          "Verified that radio buttons of 'MTA Adjustment' section are displayed as expected",
          "The radio buttons of 'MTA Adjustment' section are not displayed as expected", driver,
          extentedReport, true);

      Log.softAssertThat(getQuotePage.verifyChangePayorDisabled(),
          "Verified that the 'Change Payor' button is disabled",
          "The 'Change Payor' button is not disabled as expected", driver, extentedReport, true);
      Log.softAssertThat(getQuotePage.verifyPaymentPlanDisabled(),
          "Verified that the 'Payment Plan' drop-down is disabled",
          "The 'Payment Plan' drop-down is not disabled as expected", driver, extentedReport, true);
      Log.softAssertThat(getQuotePage.verifyPaymentMethodDisabled(),
          "Verified that the 'Payment Method' drop-down is disabled",
          "The 'Payment Method' drop-down is not disabled as expected", driver, extentedReport,
          true);

      String adjustmentOption = "Single Bill-Card";
      getQuotePage.selectSingleBillCardRadioButton(extentedReport);
      Log.softAssertThat(
          getQuotePage.verifyPaymentScheduleTable(adjustmentOption, testData, extentedReport, true),
          "The 'Payment Schedule' table verification is successful",
          "The 'Payment Schedule' table verification is unsuccessful", driver, extentedReport,
          true);

      adjustmentOption = "Spread over Instalments";
      getQuotePage.selectSpreadOverInstallmentsRadioButton(extentedReport);;
      Log.softAssertThat(
          getQuotePage.verifyPaymentScheduleTable(adjustmentOption, testData, extentedReport, true),
          "The 'Payment Schedule' table verification is successful",
          "The 'Payment Schedule' table verification is unsuccessful", driver, extentedReport,
          true);

      // Verification without checkbox selection
      Log.softAssertThat(
          getQuotePage.uielement.verifyPageElementsDoNotExist(Arrays.asList("btnAcceptPayment"),
              getQuotePage),
          "Verified that the Payment 'Accept' button is absent as 'Customer agrees...Direct Debit Guarantee' checkbox is unchecked",
          "The Payment 'Accept' button is present unexpectly though 'Customer agrees...Direct Debit Guarantee' checkbox is unchecked",
          driver, extentedReport, true);

      // Verification with checkbox selection
      getQuotePage.tickCustomerAgreesCheckbox(extentedReport);
      Log.softAssertThat(getQuotePage.verifyAcceptButtonPresence(extentedReport),
          "Verified that the Payment 'Accept' button is present as 'Customer agrees...Direct Debit Guarantee' checkbox is checked",
          "The Payment 'Accept' button is absent unexpectly though 'Customer agrees...Direct Debit Guarantee' checkbox is checked",
          driver, extentedReport, true);

      // Confirm payment
      custdashboardpage = getQuotePage.confirmPayment("MTA", testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for MTA transaction status
      String expectedPosition = "MidTermAdjustment";
      String expectedTransactionStatus = "Active: Debit And Issue Policy";
      Log.softAssertThat(
          custdashboardpage.verifyTransactionStatus(expectedPosition, expectedTransactionStatus,
              extentedReport, true),
          "Verified that the MTA transaction status is '" + expectedTransactionStatus
              + "' as expected",
          "The MTA transaction status is not as expected");

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Verify the payment schedule, adjustment radio buttons and save quote functionality while performing return or refund premium MTA transaction.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_709_712_724(String browser) throws Exception {
    String tcId = "TC_709_712_724";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      testData = createACustomerAndPolicy(driver, testData, false, true, extentedReport);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport);

      custdashboardpage.selectMTAfromManagePolicyTab(extentedReport, true);
      custdashboardpage.enterMTADetails(testData, extentedReport, true);
      GetQuotePage getQuotePage = custdashboardpage.verifyGetQuotePage(extentedReport);
      Log.message("Get quote page loaded successfully", driver, extentedReport, true);

      // Remove cover
      String[] coversToRemove = testData.get("coverToSelect").toString().split(",");
      for (int i = 0; i < coversToRemove.length; i++) {
        if (coversToRemove[i].endsWith("_Add")) {
          continue;
        }
        boolean isSuccessful = false;
        String ins_RowtoInteract =
            getQuotePage.SelectInsuranceItem(coversToRemove[i], true, extentedReport);
        isSuccessful = getQuotePage.enterInsCoverDetails(testData, coversToRemove[i],
            ins_RowtoInteract, true, extentedReport);
        String[] coverType = coversToRemove[i].split("_");
        String msg = isSuccessful ? coverType[2] + " " + coverType[0] + " cover done successfully"
            : "Failed to " + coverType[2] + " " + coverType[0] + " cover";
        Log.message(msg, driver, extentedReport, true);
      }

      // Recalculate and buy
      getQuotePage.clickReCalculate(extentedReport);
      testData =
          getQuotePage.grabPaymentDetailsFromAdjustmentCoverTab(testData, extentedReport, true);
      getQuotePage.clickBuy(extentedReport);
      getQuotePage.selectAvalilableAdjReasons("Remove Cover", extentedReport, false);
      getQuotePage.clickAddMTA(extentedReport, false);
      getQuotePage.clickAddAllMTANextButton(extentedReport, true);

      Log.softAssertThat(getQuotePage.verifyMTAAdjustmentRadioButtons(true, extentedReport, true),
          "Verified that radio buttons of 'MTA Adjustment' section are displayed as expected",
          "The radio buttons of 'MTA Adjustment' section are not displayed as expected", driver,
          extentedReport, true);

      // Acceptance tab - Payment details verification
      Log.softAssertThat(
          getQuotePage.verifyPaymentDetailsAtAdjustmentPaymentAcceptanceTab(testData,
              extentedReport, true),
          "Verified that the payment details at Acceptance tab matches with the details at Cover tab",
          "The payment details at Acceptance tab does not match with the details at Cover tab",
          driver, extentedReport, true);

      // Acceptance tab - Payment Schedule details verification
      String adjustmentOption = "Spread over Instalments";
      Log.softAssertThat(
          getQuotePage.verifyPaymentScheduleTable(adjustmentOption, testData, extentedReport, true),
          "The 'Payment Schedule' table verification is successful",
          "The 'Payment Schedule' table verification is unsuccessful", driver, extentedReport,
          true);

      // Save Quote
      custdashboardpage = getQuotePage.saveQuote(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for MTA transaction status
      String expectedPosition = "MidTermAdjustment";
      String expectedTransactionStatus = "Inactive";
      Log.softAssertThat(
          custdashboardpage.verifyTransactionStatus(expectedPosition, expectedTransactionStatus,
              extentedReport, true),
          "Verified that the MTA transaction status is '" + expectedTransactionStatus
              + "' as expected",
          "The MTA transaction status is not as expected");

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Verify the functionality clicking on TAKE PAYMENT with the 'Single Bill - Card' option",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_714_718_720(String browser) throws Exception {
    String tcId = "TC_714_718_720";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      testData = createACustomerAndPolicy(driver, testData, false, false, extentedReport);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport);

      custdashboardpage.selectMTAfromManagePolicyTab(extentedReport, true);
      custdashboardpage.enterMTADetails(testData, extentedReport, true);
      GetQuotePage getQuotePage = custdashboardpage.verifyGetQuotePage(extentedReport);
      Log.message("Get quote page loaded successfully", driver, extentedReport, true);

      // Add cover
      String[] coversToAdd = testData.get("coverToSelect").toString().split(",");
      for (int i = 0; i < coversToAdd.length; i++) {
        if (coversToAdd[i].endsWith("_Remove")) {
          continue;
        }
        String ins_RowtoInteract =
            getQuotePage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
        getQuotePage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract, true,
            extentedReport);
      }

      // Recalculate and buy
      getQuotePage.clickReCalculate(extentedReport);
      testData =
          getQuotePage.grabPaymentDetailsFromAdjustmentCoverTab(testData, extentedReport, true);
      getQuotePage.clickBuy(extentedReport);
      getQuotePage.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
      getQuotePage.clickAddMTA(extentedReport, false);
      getQuotePage.clickAddAllMTANextButton(extentedReport, true);

      Log.softAssertThat(getQuotePage.verifyMTAAdjustmentRadioButtons(false, extentedReport, true),
          "Verified that radio buttons of 'MTA Adjustment' section are displayed as expected",
          "The radio buttons of 'MTA Adjustment' section are not displayed as expected", driver,
          extentedReport, true);

      // Acceptance tab - Payment details verification
      getQuotePage.selectSingleBillCardRadioButton(extentedReport);
      Log.softAssertThat(
          getQuotePage.verifyPaymentDetailsAtAdjustmentPaymentAcceptanceTab(testData,
              extentedReport, true),
          "Verified that the payment details at Acceptance tab matches with the details at Cover tab",
          "The payment details at Acceptance tab does not match with the details at Cover tab",
          driver, extentedReport, true);

      // Acceptance tab - Payment Schedule details verification
      String adjustmentOption = "Single Bill-Card";
      Log.softAssertThat(
          getQuotePage.verifyPaymentScheduleTable(adjustmentOption, testData, extentedReport, true),
          "The 'Payment Schedule' table verification is successful",
          "The 'Payment Schedule' table verification is unsuccessful", driver, extentedReport,
          true);

      // Select Card
      getQuotePage.clickTakePaymentForMTA(extentedReport);
      CardDetailsPage carddetailspage = getQuotePage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);

      // Click continue button
      carddetailspage.clickContinueButton_getQuote(extentedReport);

      Log.softAssertThat(getQuotePage.verifyPaymentTrasaction(extentedReport),
          "Payment transaction is completed successfully", "Payment transaction is not completed",
          driver, extentedReport, true);

      // Return to customerdashboard page
      custdashboardpage = getQuotePage.clickReturnToCustomerDashboard(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for MTA transaction status
      String expectedPosition = "MidTermAdjustment";
      String expectedTransactionStatus = "Active: Debit And Issue Policy";
      Log.softAssertThat(
          custdashboardpage.verifyTransactionStatus(expectedPosition, expectedTransactionStatus,
              extentedReport, true),
          "Verified that the MTA transaction status is '" + expectedTransactionStatus
              + "' as expected",
          "The MTA transaction status is not as expected");

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Verify the policy Status should be Inactive in Customer Dashboard when the CCA processing MTA and backout the transaction from payment failure page and also verify that the MTA quote can be edited.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider",
      groups = {"Business_Scenario"})
  public void TC_725(String browser) throws Exception {
    String tcId = "TC_725";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      testData = createACustomerAndPolicy(driver, testData, false, true, extentedReport);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport);

      custdashboardpage.selectMTAfromManagePolicyTab(extentedReport, true);
      custdashboardpage.enterMTADetails(testData, extentedReport, true);
      GetQuotePage getQuotePage = custdashboardpage.verifyGetQuotePage(extentedReport);
      Log.message("Get quote page loaded successfully", driver, extentedReport, true);

      // Remove cover
      String[] coversToRemove = testData.get("coverToSelect").toString().split(",");
      for (int i = 0; i < coversToRemove.length; i++) {
        if (coversToRemove[i].endsWith("_Add")) {
          continue;
        }
        boolean isSuccessful = false;
        String ins_RowtoInteract =
            getQuotePage.SelectInsuranceItem(coversToRemove[i], true, extentedReport);
        isSuccessful = getQuotePage.enterInsCoverDetails(testData, coversToRemove[i],
            ins_RowtoInteract, true, extentedReport);
        String[] coverType = coversToRemove[i].split("_");
        String msg = isSuccessful ? coverType[2] + " " + coverType[0] + " cover done successfully"
            : "Failed to " + coverType[2] + " " + coverType[0] + " cover";
        Log.message(msg, driver, extentedReport, true);
      }

      // Recalculate and buy
      getQuotePage.clickReCalculate(extentedReport);
      getQuotePage.clickBuy(extentedReport);
      getQuotePage.selectAvalilableAdjReasons("Remove Cover", extentedReport, false);
      getQuotePage.clickAddMTA(extentedReport, false);
      getQuotePage.clickAddAllMTANextButton(extentedReport, true);

      Log.softAssertThat(getQuotePage.verifyMTAAdjustmentRadioButtons(true, extentedReport, true),
          "Verified that radio buttons of 'MTA Adjustment' section are displayed as expected",
          "The radio buttons of 'MTA Adjustment' section are not displayed as expected", driver,
          extentedReport, true);

      // Cancel payment
      custdashboardpage = getQuotePage.cancelPayment(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for MTA transaction status
      String expectedPosition = "MidTermAdjustment";
      String expectedTransactionStatus = "Inactive";
      Log.softAssertThat(
          custdashboardpage.verifyTransactionStatus(expectedPosition, expectedTransactionStatus,
              extentedReport, true),
          "Verified that the MTA transaction status is '" + expectedTransactionStatus
              + "' as expected",
          "The MTA transaction status is not as expected");

      // Edit 'Inactive' MTA transaction
      custdashboardpage.selectTransactionRow(extentedReport);
      custdashboardpage.clickEditTransactionButton(extentedReport, true);

      getQuotePage = custdashboardpage.verifyGetQuotePage(extentedReport);
      Log.message("Get quote page loaded successfully", driver, extentedReport, true);
      getQuotePage.clickBuy(extentedReport);
      getQuotePage.selectAvalilableAdjReasons("Remove Cover", extentedReport, false);
      getQuotePage.clickAddMTA(extentedReport, false);
      getQuotePage.clickAddAllMTANextButton(extentedReport, true);
      Log.softAssertThat(getQuotePage.verifyMTAAdjustmentRadioButtons(false, extentedReport, true),
          "Verified that radio buttons of 'MTA Adjustment' section are displayed as expected",
          "The radio buttons of 'MTA Adjustment' section are not displayed as expected", driver,
          extentedReport, true);

      String adjustmentOption = "Spread over Instalments";
      Log.softAssertThat(
          getQuotePage.verifyPaymentScheduleTable(adjustmentOption, testData, extentedReport, true),
          "The 'Payment Schedule' table verification is successful",
          "The 'Payment Schedule' table verification is unsuccessful", driver, extentedReport,
          true);

      // Verification without checkbox selection
      Log.softAssertThat(
          getQuotePage.uielement.verifyPageElementsDoNotExist(Arrays.asList("btnAcceptPayment"),
              getQuotePage),
          "Verified that the Payment 'Accept' button is absent as 'Customer agrees...Direct Debit Guarantee' checkbox is unchecked",
          "The Payment 'Accept' button is present unexpectly though 'Customer agrees...Direct Debit Guarantee' checkbox is unchecked",
          driver, extentedReport, true);

      // Verification with checkbox selection
      getQuotePage.tickCustomerAgreesCheckbox(extentedReport);
      Log.softAssertThat(getQuotePage.verifyAcceptButtonPresence(extentedReport),
          "Verified that the Payment 'Accept' button is present as 'Customer agrees...Direct Debit Guarantee' checkbox is checked",
          "The Payment 'Accept' button is absent unexpectly though 'Customer agrees...Direct Debit Guarantee' checkbox is checked",
          driver, extentedReport, true);

      // Confirm payment
      custdashboardpage = getQuotePage.confirmPayment("MTA", testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for MTA transaction status
      expectedTransactionStatus = "Active: Debit And Issue Policy";
      Log.softAssertThat(
          custdashboardpage.verifyTransactionStatus(expectedPosition, expectedTransactionStatus,
              extentedReport, true),
          "Verified that the MTA transaction status is '" + expectedTransactionStatus
              + "' as expected",
          "The MTA transaction status is not as expected");

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Verify the policy Status should be Qouted Status in Customer Dashboard when the CCA processing MTA clicking on SAVE Quote button from payment failure page.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_726(String browser) throws Exception {
    String tcId = "TC_726";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      testData = createACustomerAndPolicy(driver, testData, false, true, extentedReport);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport);

      custdashboardpage.selectMTAfromManagePolicyTab(extentedReport, true);
      custdashboardpage.enterMTADetails(testData, extentedReport, true);
      GetQuotePage getQuotePage = custdashboardpage.verifyGetQuotePage(extentedReport);
      Log.message("Get quote page loaded successfully", driver, extentedReport, true);

      // Remove cover
      String[] coversToRemove = testData.get("coverToSelect").toString().split(",");
      for (int i = 0; i < coversToRemove.length; i++) {
        if (coversToRemove[i].endsWith("_Add")) {
          continue;
        }
        boolean isSuccessful = false;
        String ins_RowtoInteract =
            getQuotePage.SelectInsuranceItem(coversToRemove[i], true, extentedReport);
        isSuccessful = getQuotePage.enterInsCoverDetails(testData, coversToRemove[i],
            ins_RowtoInteract, true, extentedReport);
        String[] coverType = coversToRemove[i].split("_");
        String msg = isSuccessful ? coverType[2] + " " + coverType[0] + " cover done successfully"
            : "Failed to " + coverType[2] + " " + coverType[0] + " cover";
        Log.message(msg, driver, extentedReport, true);
      }

      // Recalculate and buy
      getQuotePage.clickReCalculate(extentedReport);
      getQuotePage.clickBuy(extentedReport);
      getQuotePage.selectAvalilableAdjReasons("Remove Cover", extentedReport, false);
      getQuotePage.clickAddMTA(extentedReport, false);
      getQuotePage.clickAddAllMTANextButton(extentedReport, true);

      Log.softAssertThat(getQuotePage.verifyMTAAdjustmentRadioButtons(true, extentedReport, true),
          "Verified that radio buttons of 'MTA Adjustment' section are displayed as expected",
          "The radio buttons of 'MTA Adjustment' section are not displayed as expected", driver,
          extentedReport, true);

      // Save Quote
      custdashboardpage = getQuotePage.saveQuote(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for MTA transaction status
      String expectedPosition = "MidTermAdjustment";
      String expectedTransactionStatus = "Inactive";
      Log.softAssertThat(
          custdashboardpage.verifyTransactionStatus(expectedPosition, expectedTransactionStatus,
              extentedReport, true),
          "Verified that the MTA transaction status is '" + expectedTransactionStatus
              + "' as expected",
          "The MTA transaction status is not as expected");

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Verify the STA flow by using DD as payment method option selecting monthlt payment type.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_877(String browser) throws Exception {
    String tcId = "TC_877";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      testData = createACustomerAndPolicy(driver, testData, false, true, extentedReport);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport);

      // STA
      custdashboardpage.selectSTAfromManagePolicyTab(extentedReport, true);
      custdashboardpage.enterSTADetails(testData, extentedReport, true);
      GetQuotePage getQuotePage = custdashboardpage.verifyGetQuotePage(extentedReport);
      Log.message("Get quote page loaded successfully", driver, extentedReport, true);

      // Remove cover
      String[] coversToRemove = testData.get("coverToSelect").toString().split(",");
      for (int i = 0; i < coversToRemove.length; i++) {
        if (coversToRemove[i].endsWith("_Add")) {
          continue;
        }
        boolean isSuccessful = false;
        String ins_RowtoInteract =
            getQuotePage.SelectInsuranceItem(coversToRemove[i], true, extentedReport);
        isSuccessful = getQuotePage.enterInsCoverDetails(testData, coversToRemove[i],
            ins_RowtoInteract, true, extentedReport);
        String[] coverType = coversToRemove[i].split("_");
        String msg = isSuccessful ? coverType[2] + " " + coverType[0] + " cover done successfully"
            : "Failed to " + coverType[2] + " " + coverType[0] + " cover";
        Log.message(msg, driver, extentedReport, true);
      }

      // Recalculate and buy
      getQuotePage.clickReCalculate(extentedReport);
      getQuotePage.clickBuy(extentedReport);
      getQuotePage.selectAvalilableAdjReasons("Remove Cover", extentedReport, false);
      getQuotePage.clickAddMTA(extentedReport, false);
      getQuotePage.clickAddAllMTANextButton(extentedReport, true);

      Log.softAssertThat(getQuotePage.verifyMTAAdjustmentRadioButtons(true, extentedReport, true),
          "Verified that radio buttons of 'STA Adjustment' section are displayed as expected",
          "The radio buttons of 'STA Adjustment' section are not displayed as expected", driver,
          extentedReport, true);

      // Acceptance tab - Payment details verification
      Log.softAssertThat(
          getQuotePage.verifyPaymentDetailsAtAdjustmentPaymentAcceptanceTab(testData,
              extentedReport, true),
          "Verified that the payment details at Acceptance tab matches with the details at Cover tab",
          "The payment details at Acceptance tab does not match with the details at Cover tab",
          driver, extentedReport, true);

      // Acceptance tab - Payment Schedule details verification
      String adjustmentOption = "Spread over Instalments";
      Log.softAssertThat(
          getQuotePage.verifyPaymentScheduleTable(adjustmentOption, testData, extentedReport, true),
          "The 'Payment Schedule' table verification is successful",
          "The 'Payment Schedule' table verification is unsuccessful", driver, extentedReport,
          true);

      getQuotePage.tickCustomerAgreesCheckbox(extentedReport);
      Log.softAssertThat(getQuotePage.verifyAcceptButtonPresence(extentedReport),
          "Verified that the Payment 'Accept' button is present as 'Customer agrees...Direct Debit Guarantee' checkbox is checked",
          "The Payment 'Accept' button is absent unexpectly though 'Customer agrees...Direct Debit Guarantee' checkbox is checked",
          driver, extentedReport, true);

      // Accept Payment
      custdashboardpage = getQuotePage.confirmPayment("STA", testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for STA transaction status
      String expectedPosition = "ShortTermAdjustment";
      String expectedTransactionStatus = "Active: Debit And Issue Policy";
      Log.softAssertThat(
          custdashboardpage.verifyTransactionStatus(expectedPosition, expectedTransactionStatus,
              extentedReport, true),
          "Verified that the STA transaction status is '" + expectedTransactionStatus
              + "' as expected",
          "The STA transaction status is not as expected");

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(description = "Verify the DD payment option with 5 star Defacto rating policy for NB.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_878(String browser) throws Exception {
    String tcId = "TC_878";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Get upgraded 5 star policy
      testData =
          createACustomerAndNavigateToAcceptanceTab(driver, testData, true, false, extentedReport);
      NewQuotePage newquotepage = new NewQuotePage(driver, extentedReport);

      // Payment
      newquotepage.selectPayment(testData, true, extentedReport);

      // Acceptance tab - Payment details verification
      Log.softAssertThat(
          newquotepage.verifyPaymentDetailsAtAcceptanceTab(testData, extentedReport, true),
          "Verified that the payment details at Acceptance tab matches with the details at Cover tab",
          "The payment details at Acceptance tab does not match with the details at Cover tab",
          driver, extentedReport, true);

      // Payment Schedule table verification
      String preferredPaymentDay = DateTimeUtility.getCurrentDate().split("/")[0];
      testData.put("Preferred Payment Day", preferredPaymentDay);
      Log.softAssertThat(newquotepage.verifyPaymentScheduleTable(testData, extentedReport, true),
          "The 'Payment Schedule' table verification is successful",
          "The 'Payment Schedule' table verification is unsuccessful", driver, extentedReport,
          true);

      CustDashboardPage custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
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

      // Premium summary details verification
      Log.softAssertThat(custdashboardpage.verifyPremiumSummary(testData, extentedReport, true),
          "Verified that the premium summary details are as expected",
          "The premium summary details are not as expected");

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Verify the DD payment option with 5 star Defacto rating policy for MTA. - Remove cover with Spread over Installments",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_879_A(String browser) throws Exception {
    String tcId = "TC_879_A";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Get upgraded 5 star policy with added cover
      testData = createACustomerAndPolicy(driver, testData, true, true, extentedReport);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport);

      custdashboardpage.selectMTAfromManagePolicyTab(extentedReport, true);
      custdashboardpage.enterMTADetails(testData, extentedReport, true);
      GetQuotePage getQuotePage = custdashboardpage.verifyGetQuotePage(extentedReport);
      Log.message("Get quote page loaded successfully", driver, extentedReport, true);

      // Remove cover
      String[] coversToRemove = testData.get("coverToSelect").toString().split(",");
      for (int i = 0; i < coversToRemove.length; i++) {
        if (coversToRemove[i].endsWith("_Add")) {
          continue;
        }
        boolean isSuccessful = false;
        String ins_RowtoInteract =
            getQuotePage.SelectInsuranceItem(coversToRemove[i], true, extentedReport);
        isSuccessful = getQuotePage.enterInsCoverDetails(testData, coversToRemove[i],
            ins_RowtoInteract, true, extentedReport);
        String[] coverType = coversToRemove[i].split("_");
        String msg = isSuccessful ? coverType[2] + " " + coverType[0] + " cover done successfully"
            : "Failed to " + coverType[2] + " " + coverType[0] + " cover";
        Log.message(msg, driver, extentedReport, true);
      }

      // Recalculate and buy
      getQuotePage.clickReCalculate(extentedReport);
      testData =
          getQuotePage.grabPaymentDetailsFromAdjustmentCoverTab(testData, extentedReport, true);
      getQuotePage.clickBuy(extentedReport);
      getQuotePage.selectAvalilableAdjReasons("Remove Cover", extentedReport, false);
      getQuotePage.clickAddMTA(extentedReport, false);
      getQuotePage.clickAddAllMTANextButton(extentedReport, true);

      Log.softAssertThat(getQuotePage.verifyMTAAdjustmentRadioButtons(true, extentedReport, true),
          "Verified that radio buttons of 'MTA Adjustment' section are displayed as expected",
          "The radio buttons of 'MTA Adjustment' section are not displayed as expected", driver,
          extentedReport, true);

      // Acceptance tab - Payment details verification
      Log.softAssertThat(
          getQuotePage.verifyPaymentDetailsAtAdjustmentPaymentAcceptanceTab(testData,
              extentedReport, true),
          "Verified that the payment details at Acceptance tab matches with the details at Cover tab",
          "The payment details at Acceptance tab does not match with the details at Cover tab",
          driver, extentedReport, true);

      // Acceptance tab - Payment Schedule details verification
      String adjustmentOption = "Spread over Instalments";
      Log.softAssertThat(
          getQuotePage.verifyPaymentScheduleTable(adjustmentOption, testData, extentedReport, true),
          "The 'Payment Schedule' table verification is successful",
          "The 'Payment Schedule' table verification is unsuccessful", driver, extentedReport,
          true);

      getQuotePage.tickCustomerAgreesCheckbox(extentedReport);
      Log.softAssertThat(getQuotePage.verifyAcceptButtonPresence(extentedReport),
          "Verified that the Payment 'Accept' button is present as 'Customer agrees...Direct Debit Guarantee' checkbox is checked",
          "The Payment 'Accept' button is absent unexpectly though 'Customer agrees...Direct Debit Guarantee' checkbox is checked",
          driver, extentedReport, true);

      // Confirm payment with 'Spread over Instalments'
      custdashboardpage = getQuotePage.confirmPayment("MTA", testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for MTA transaction status
      String expectedPosition = "MidTermAdjustment";
      String expectedTransactionStatus = "Active: Debit And Issue Policy";
      Log.softAssertThat(
          custdashboardpage.verifyTransactionStatus(expectedPosition, expectedTransactionStatus,
              extentedReport, true),
          "Verified that the MTA transaction status is '" + expectedTransactionStatus
              + "' as expected",
          "The MTA transaction status is not as expected");

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Verify the DD payment option with 5 star Defacto rating policy for MTA - Add cover with Single Bill-Card.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_879_B(String browser) throws Exception {
    String tcId = "TC_879_B";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Get upgraded 5 star policy
      testData = createACustomerAndPolicy(driver, testData, true, false, extentedReport);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport);

      custdashboardpage.selectMTAfromManagePolicyTab(extentedReport, true);
      custdashboardpage.enterMTADetails(testData, extentedReport, true);
      GetQuotePage getQuotePage = custdashboardpage.verifyGetQuotePage(extentedReport);
      Log.message("Get quote page loaded successfully", driver, extentedReport, true);

      // Add cover
      String[] coversToAdd = testData.get("coverToSelect").toString().split(",");
      for (int i = 0; i < coversToAdd.length; i++) {
        if (coversToAdd[i].endsWith("_Remove")) {
          continue;
        }
        String ins_RowtoInteract =
            getQuotePage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
        getQuotePage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract, true,
            extentedReport);
      }

      // Recalculate and buy
      getQuotePage.clickReCalculate(extentedReport);
      testData =
          getQuotePage.grabPaymentDetailsFromAdjustmentCoverTab(testData, extentedReport, true);
      getQuotePage.clickBuy(extentedReport);
      getQuotePage.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
      getQuotePage.clickAddMTA(extentedReport, false);
      getQuotePage.clickAddAllMTANextButton(extentedReport, true);

      Log.softAssertThat(getQuotePage.verifyMTAAdjustmentRadioButtons(false, extentedReport, true),
          "Verified that radio buttons of 'MTA Adjustment' section are displayed as expected",
          "The radio buttons of 'MTA Adjustment' section are not displayed as expected", driver,
          extentedReport, true);

      // Acceptance tab - Payment details verification
      getQuotePage.selectSingleBillCardRadioButton(extentedReport);
      Log.softAssertThat(
          getQuotePage.verifyPaymentDetailsAtAdjustmentPaymentAcceptanceTab(testData,
              extentedReport, true),
          "Verified that the payment details at Acceptance tab matches with the details at Cover tab",
          "The payment details at Acceptance tab does not match with the details at Cover tab",
          driver, extentedReport, true);

      // Acceptance tab - Payment Schedule details verification
      String adjustmentOption = "Single Bill-Card";
      Log.softAssertThat(
          getQuotePage.verifyPaymentScheduleTable(adjustmentOption, testData, extentedReport, true),
          "The 'Payment Schedule' table verification is successful",
          "The 'Payment Schedule' table verification is unsuccessful", driver, extentedReport,
          true);

      // Select Card
      getQuotePage.clickTakePaymentForMTA(extentedReport);
      CardDetailsPage carddetailspage = getQuotePage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);

      // Click continue button
      carddetailspage.clickContinueButton_getQuote(extentedReport);

      Log.softAssertThat(getQuotePage.verifyPaymentTrasaction(extentedReport),
          "Payment transaction is completed successfully", "Payment transaction is not completed",
          driver, extentedReport, true);

      // Return to customerdashboard page
      getQuotePage.clickReturnToCustomerDashboard(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for MTA transaction status
      String expectedPosition = "MidTermAdjustment";
      String expectedTransactionStatus = "Active: Debit And Issue Policy";
      Log.softAssertThat(
          custdashboardpage.verifyTransactionStatus(expectedPosition, expectedTransactionStatus,
              extentedReport, true),
          "Verified that the MTA transaction status is '" + expectedTransactionStatus
              + "' as expected",
          "The MTA transaction status is not as expected");

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Verify the DD payment option with 5 star Defacto rating policy for STA. - Remove cover with Spread over Installments",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_880_A(String browser) throws Exception {
    String tcId = "TC_880_A";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Get upgraded 5 star policy with added cover
      testData = createACustomerAndPolicy(driver, testData, true, true, extentedReport);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport);

      custdashboardpage.selectSTAfromManagePolicyTab(extentedReport, true);
      custdashboardpage.enterSTADetails(testData, extentedReport, true);
      GetQuotePage getQuotePage = custdashboardpage.verifyGetQuotePage(extentedReport);
      Log.message("Get quote page loaded successfully", driver, extentedReport, true);

      // Remove cover
      String[] coversToRemove = testData.get("coverToSelect").toString().split(",");
      for (int i = 0; i < coversToRemove.length; i++) {
        if (coversToRemove[i].endsWith("_Add")) {
          continue;
        }
        boolean isSuccessful = false;
        String ins_RowtoInteract =
            getQuotePage.SelectInsuranceItem(coversToRemove[i], true, extentedReport);
        isSuccessful = getQuotePage.enterInsCoverDetails(testData, coversToRemove[i],
            ins_RowtoInteract, true, extentedReport);
        String[] coverType = coversToRemove[i].split("_");
        String msg = isSuccessful ? coverType[2] + " " + coverType[0] + " cover done successfully"
            : "Failed to " + coverType[2] + " " + coverType[0] + " cover";
        Log.message(msg, driver, extentedReport, true);
      }

      // Recalculate and buy
      getQuotePage.clickReCalculate(extentedReport);
      testData =
          getQuotePage.grabPaymentDetailsFromAdjustmentCoverTab(testData, extentedReport, true);
      getQuotePage.clickBuy(extentedReport);
      getQuotePage.selectAvalilableAdjReasons("Remove Cover", extentedReport, false);
      getQuotePage.clickAddMTA(extentedReport, false);
      getQuotePage.clickAddAllMTANextButton(extentedReport, true);

      Log.softAssertThat(getQuotePage.verifyMTAAdjustmentRadioButtons(true, extentedReport, true),
          "Verified that radio buttons of 'STA Adjustment' section are displayed as expected",
          "The radio buttons of 'STA Adjustment' section are not displayed as expected", driver,
          extentedReport, true);

      // Acceptance tab - Payment details verification
      Log.softAssertThat(
          getQuotePage.verifyPaymentDetailsAtAdjustmentPaymentAcceptanceTab(testData,
              extentedReport, true),
          "Verified that the payment details at Acceptance tab matches with the details at Cover tab",
          "The payment details at Acceptance tab does not match with the details at Cover tab",
          driver, extentedReport, true);

      // Acceptance tab - Payment Schedule details verification
      String adjustmentOption = "Spread over Instalments";
      Log.softAssertThat(
          getQuotePage.verifyPaymentScheduleTable(adjustmentOption, testData, extentedReport, true),
          "The 'Payment Schedule' table verification is successful",
          "The 'Payment Schedule' table verification is unsuccessful", driver, extentedReport,
          true);

      getQuotePage.tickCustomerAgreesCheckbox(extentedReport);
      Log.softAssertThat(getQuotePage.verifyAcceptButtonPresence(extentedReport),
          "Verified that the Payment 'Accept' button is present as 'Customer agrees...Direct Debit Guarantee' checkbox is checked",
          "The Payment 'Accept' button is absent unexpectly though 'Customer agrees...Direct Debit Guarantee' checkbox is checked",
          driver, extentedReport, true);

      // Confirm payment with 'Spread over Instalments'
      custdashboardpage = getQuotePage.confirmPayment("STA", testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for MTA transaction status
      String expectedPosition = "ShortTermAdjustment";
      String expectedTransactionStatus = "Active: Debit And Issue Policy";
      Log.softAssertThat(
          custdashboardpage.verifyTransactionStatus(expectedPosition, expectedTransactionStatus,
              extentedReport, true),
          "Verified that the STA transaction status is '" + expectedTransactionStatus
              + "' as expected",
          "The STA transaction status is not as expected");

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Verify the DD payment option with 5 star Defacto rating policy for STA - Add cover with Single Bill-Card.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_880_B(String browser) throws Exception {
    String tcId = "TC_880_B";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Get upgraded 5 star policy
      testData = createACustomerAndPolicy(driver, testData, true, false, extentedReport);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport);

      custdashboardpage.selectSTAfromManagePolicyTab(extentedReport, true);
      custdashboardpage.enterSTADetails(testData, extentedReport, true);
      GetQuotePage getQuotePage = custdashboardpage.verifyGetQuotePage(extentedReport);
      Log.message("Get quote page loaded successfully", driver, extentedReport, true);

      // Add cover
      String[] coversToAdd = testData.get("coverToSelect").toString().split(",");
      for (int i = 0; i < coversToAdd.length; i++) {
        if (coversToAdd[i].endsWith("_Remove")) {
          continue;
        }
        String ins_RowtoInteract =
            getQuotePage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
        getQuotePage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract, true,
            extentedReport);
      }

      // Recalculate and buy
      getQuotePage.clickReCalculate(extentedReport);
      testData =
          getQuotePage.grabPaymentDetailsFromAdjustmentCoverTab(testData, extentedReport, true);
      getQuotePage.clickBuy(extentedReport);
      getQuotePage.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
      getQuotePage.clickAddMTA(extentedReport, false);
      getQuotePage.clickAddAllMTANextButton(extentedReport, true);

      Log.softAssertThat(getQuotePage.verifyMTAAdjustmentRadioButtons(false, extentedReport, true),
          "Verified that radio buttons of 'STA Adjustment' section are displayed as expected",
          "The radio buttons of 'STA Adjustment' section are not displayed as expected", driver,
          extentedReport, true);

      // Acceptance tab - Payment details verification
      getQuotePage.selectSingleBillCardRadioButton(extentedReport);
      Log.softAssertThat(
          getQuotePage.verifyPaymentDetailsAtAdjustmentPaymentAcceptanceTab(testData,
              extentedReport, true),
          "Verified that the payment details at Acceptance tab matches with the details at Cover tab",
          "The payment details at Acceptance tab does not match with the details at Cover tab",
          driver, extentedReport, true);

      // Acceptance tab - Payment Schedule details verification
      String adjustmentOption = "Single Bill-Card";
      Log.softAssertThat(
          getQuotePage.verifyPaymentScheduleTable(adjustmentOption, testData, extentedReport, true),
          "The 'Payment Schedule' table verification is successful",
          "The 'Payment Schedule' table verification is unsuccessful", driver, extentedReport,
          true);

      // Select Card
      getQuotePage.clickTakePaymentForMTA(extentedReport);
      CardDetailsPage carddetailspage = getQuotePage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);

      // Click continue button
      carddetailspage.clickContinueButton_getQuote(extentedReport);

      Log.softAssertThat(getQuotePage.verifyPaymentTrasaction(extentedReport),
          "Payment transaction is completed successfully", "Payment transaction is not completed",
          driver, extentedReport, true);

      // Return to customerdashboard page
      getQuotePage.clickReturnToCustomerDashboard(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for MTA transaction status
      String expectedPosition = "ShortTermAdjustment";
      String expectedTransactionStatus = "Active: Debit And Issue Policy";
      Log.softAssertThat(
          custdashboardpage.verifyTransactionStatus(expectedPosition, expectedTransactionStatus,
              extentedReport, true),
          "Verified that the STA transaction status is '" + expectedTransactionStatus
              + "' as expected",
          "The STA transaction status is not as expected");

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Verify the display of payment details and installment details in the acceptance page while clicking on BUY button from Dashboard for the quoted policy",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_882(String browser) throws Exception {
    String tcId = "TC_882";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      testData =
          createACustomerAndNavigateToAcceptanceTab(driver, testData, false, false, extentedReport);
      NewQuotePage newquotepage = new NewQuotePage(driver, extentedReport);

      // Payment
      newquotepage.selectPaymentPlan("Monthly", extentedReport);
      newquotepage.selectPaymentMethod("Direct Debit", extentedReport);

      // Acceptance tab - Payment details verification
      Log.softAssertThat(
          newquotepage.verifyPaymentDetailsAtAcceptanceTab(testData, extentedReport, true),
          "Verified that the payment details at Acceptance tab matches with the details at Cover tab",
          "The payment details at Acceptance tab does not match with the details at Cover tab",
          driver, extentedReport, true);

      newquotepage.tickCustomerAgreesCheckbox(extentedReport);

      // Cancel payment
      CustDashboardPage custdashboardpage = newquotepage.cancelPayment(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for policy status
      String expectedPolicyStatus = "Quoted";
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat(
          (policyDetails.get("Status").toString().equalsIgnoreCase(expectedPolicyStatus)),
          "NB Policy Created Successfully in '" + expectedPolicyStatus + "' status",
          "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status");

      // Edit Quote
      GetQuotePage getQuotePage =
          custdashboardpage.clickEditfromManagePolicyDropdown(extentedReport, true);

      Log.softAssertThat(
          getQuotePage.uielement.verifyPageElements(Arrays.asList("btnBuyEditQuote"), getQuotePage),
          "Verified that the user is on GetQuote page",
          "The 'Buy' button in GetQuote page is not found. The user might not be on GetQuote page.",
          driver, extentedReport, true);
      getQuotePage.clickBuyEditQuote(extentedReport);
      getQuotePage.selectPaymentPlan("Monthly", extentedReport);
      getQuotePage.selectPaymentMethod("Direct Debit", extentedReport);

      // Acceptance tab - Payment details verification
      Log.softAssertThat(
          getQuotePage.verifyPaymentDetailsAtEditQuoteAcceptanceTab(testData, extentedReport, true),
          "Verified that the payment details at Acceptance tab matches with the details at Cover tab",
          "The payment details at Acceptance tab does not match with the details at Cover tab",
          driver, extentedReport, true);

      // Acceptance tab - Payment Schedule details verification
      String preferredPaymentDay = DateTimeUtility.getCurrentDate().split("/")[0];
      testData.put("Preferred Payment Day", preferredPaymentDay);
      Log.softAssertThat(
          getQuotePage.verifyPaymentScheduleTable("No Adjustment", testData, extentedReport, true),
          "The 'Payment Schedule' table verification is successful",
          "The 'Payment Schedule' table verification is unsuccessful", driver, extentedReport,
          true);

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(description = "Verify that it is possible to create a STA DD for additional cover",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider",
      groups = {"Business_Scenario"})
  public void BS_013(String browser) throws Exception {
    String tcId = "BS_013";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      testData = createACustomerAndPolicy(driver, testData, false, false, extentedReport);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport);

      custdashboardpage.selectSTAfromManagePolicyTab(extentedReport, true);
      custdashboardpage.enterSTADetails(testData, extentedReport, true);
      GetQuotePage getQuotePage = custdashboardpage.verifyGetQuotePage(extentedReport);
      Log.message("Get quote page loaded successfully", driver, extentedReport, true);

      // Add cover
      String[] coversToAdd = testData.get("coverToSelect").toString().split(",");
      for (int i = 0; i < coversToAdd.length; i++) {
        if (coversToAdd[i].endsWith("_Remove")) {
          continue;
        }
        String ins_RowtoInteract =
            getQuotePage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
        getQuotePage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract, true,
            extentedReport);
      }

      // Recalculate and buy
      getQuotePage.clickReCalculate(extentedReport);
      testData =
          getQuotePage.grabPaymentDetailsFromAdjustmentCoverTab(testData, extentedReport, true);
      getQuotePage.clickBuy(extentedReport);
      getQuotePage.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
      getQuotePage.clickAddMTA(extentedReport, false);
      getQuotePage.clickAddAllMTANextButton(extentedReport, true);

      Log.softAssertThat(getQuotePage.verifyMTAAdjustmentRadioButtons(false, extentedReport, true),
          "Verified that radio buttons of 'STA Adjustment' section are displayed as expected",
          "The radio buttons of 'STA Adjustment' section are not displayed as expected", driver,
          extentedReport, true);

      // Acceptance tab - Payment details verification
      Log.softAssertThat(
          getQuotePage.verifyPaymentDetailsAtAdjustmentPaymentAcceptanceTab(testData,
              extentedReport, true),
          "Verified that the payment details at Acceptance tab matches with the details at Cover tab",
          "The payment details at Acceptance tab does not match with the details at Cover tab",
          driver, extentedReport, true);

      // Acceptance tab - Payment Schedule details verification
      String adjustmentOption = "Spread over Instalments";
      Log.softAssertThat(
          getQuotePage.verifyPaymentScheduleTable(adjustmentOption, testData, extentedReport, true),
          "The 'Payment Schedule' table verification is successful",
          "The 'Payment Schedule' table verification is unsuccessful", driver, extentedReport,
          true);

      // Verification without checkbox selection
      Log.softAssertThat(
          getQuotePage.uielement.verifyPageElementsDoNotExist(Arrays.asList("btnAcceptPayment"),
              getQuotePage),
          "Verified that the Payment 'Accept' button is absent as 'Customer agrees...Direct Debit Guarantee' checkbox is unchecked",
          "The Payment 'Accept' button is present unexpectly though 'Customer agrees...Direct Debit Guarantee' checkbox is unchecked",
          driver, extentedReport, true);

      // Verification with checkbox selection
      getQuotePage.tickCustomerAgreesCheckbox(extentedReport);
      Log.softAssertThat(getQuotePage.verifyAcceptButtonPresence(extentedReport),
          "Verified that the Payment 'Accept' button is present as 'Customer agrees...Direct Debit Guarantee' checkbox is checked",
          "The Payment 'Accept' button is absent unexpectly though 'Customer agrees...Direct Debit Guarantee' checkbox is checked",
          driver, extentedReport, true);

      // Confirm payment
      custdashboardpage = getQuotePage.confirmPayment("STA", testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for MTA transaction status
      String expectedPosition = "ShortTermAdjustment";
      String expectedTransactionStatus = "Active: Debit And Issue Policy";
      Log.softAssertThat(
          custdashboardpage.verifyTransactionStatus(expectedPosition, expectedTransactionStatus,
              extentedReport, true),
          "Verified that the STA transaction status is '" + expectedTransactionStatus
              + "' as expected",
          "The STA transaction status is not as expected");

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Verify the policy Status should be Inactive in Customer Dashboard when the CCA processing STA and backout the transaction from payment failure page and also verify that the STA quote can be edited.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider",
      groups = {"Business_Scenario"})
  public void BS_014(String browser) throws Exception {
    String tcId = "BS_014";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      testData = createACustomerAndPolicy(driver, testData, false, true, extentedReport);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport);

      // STA
      custdashboardpage.selectSTAfromManagePolicyTab(extentedReport, true);
      custdashboardpage.enterSTADetails(testData, extentedReport, true);
      GetQuotePage getQuotePage = custdashboardpage.verifyGetQuotePage(extentedReport);
      Log.message("Get quote page loaded successfully", driver, extentedReport, true);

      // Remove cover
      String[] coversToRemove = testData.get("coverToSelect").toString().split(",");
      for (int i = 0; i < coversToRemove.length; i++) {
        if (coversToRemove[i].endsWith("_Add")) {
          continue;
        }
        boolean isSuccessful = false;
        String ins_RowtoInteract =
            getQuotePage.SelectInsuranceItem(coversToRemove[i], true, extentedReport);
        isSuccessful = getQuotePage.enterInsCoverDetails(testData, coversToRemove[i],
            ins_RowtoInteract, true, extentedReport);
        String[] coverType = coversToRemove[i].split("_");
        String msg = isSuccessful ? coverType[2] + " " + coverType[0] + " cover done successfully"
            : "Failed to " + coverType[2] + " " + coverType[0] + " cover";
        Log.message(msg, driver, extentedReport, true);
      }

      // Recalculate and buy
      getQuotePage.clickReCalculate(extentedReport);
      getQuotePage.clickBuy(extentedReport);
      getQuotePage.selectAvalilableAdjReasons("Remove Cover", extentedReport, false);
      getQuotePage.clickAddMTA(extentedReport, false);
      getQuotePage.clickAddAllMTANextButton(extentedReport, true);

      Log.softAssertThat(getQuotePage.verifyMTAAdjustmentRadioButtons(true, extentedReport, true),
          "Verified that radio buttons of 'STA Adjustment' section are displayed as expected",
          "The radio buttons of 'STA Adjustment' section are not displayed as expected", driver,
          extentedReport, true);

      // Cancel payment
      custdashboardpage = getQuotePage.cancelPayment(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for MTA transaction status
      String expectedPosition = "ShortTermAdjustment";
      String expectedTransactionStatus = "Inactive";
      Log.softAssertThat(
          custdashboardpage.verifyTransactionStatus(expectedPosition, expectedTransactionStatus,
              extentedReport, true),
          "Verified that the STA transaction status is '" + expectedTransactionStatus
              + "' as expected",
          "The STA transaction status is not as expected");

      // Edit 'Inactive' STA transaction
      custdashboardpage.selectTransactionRow(extentedReport);
      custdashboardpage.clickEditTransactionButton(extentedReport, true);

      getQuotePage = custdashboardpage.verifyGetQuotePage(extentedReport);
      Log.message("Get quote page loaded successfully", driver, extentedReport, true);
      getQuotePage.clickBuy(extentedReport);
      getQuotePage.selectAvalilableAdjReasons("Remove Cover", extentedReport, false);
      getQuotePage.clickAddMTA(extentedReport, false);
      getQuotePage.clickAddAllMTANextButton(extentedReport, true);
      Log.softAssertThat(getQuotePage.verifyMTAAdjustmentRadioButtons(false, extentedReport, true),
          "Verified that radio buttons of 'STA Adjustment' section are displayed as expected",
          "The radio buttons of 'STA Adjustment' section are not displayed as expected", driver,
          extentedReport, true);

      String adjustmentOption = "Spread over Instalments";
      Log.softAssertThat(
          getQuotePage.verifyPaymentScheduleTable(adjustmentOption, testData, extentedReport, true),
          "The 'Payment Schedule' table verification is successful",
          "The 'Payment Schedule' table verification is unsuccessful", driver, extentedReport,
          true);

      // Verification without checkbox selection
      Log.softAssertThat(
          getQuotePage.uielement.verifyPageElementsDoNotExist(Arrays.asList("btnAcceptPayment"),
              getQuotePage),
          "Verified that the Payment 'Accept' button is absent as 'Customer agrees...Direct Debit Guarantee' checkbox is unchecked",
          "The Payment 'Accept' button is present unexpectly though 'Customer agrees...Direct Debit Guarantee' checkbox is unchecked",
          driver, extentedReport, true);

      // Verification with checkbox selection
      getQuotePage.tickCustomerAgreesCheckbox(extentedReport);
      Log.softAssertThat(getQuotePage.verifyAcceptButtonPresence(extentedReport),
          "Verified that the Payment 'Accept' button is present as 'Customer agrees...Direct Debit Guarantee' checkbox is checked",
          "The Payment 'Accept' button is absent unexpectly though 'Customer agrees...Direct Debit Guarantee' checkbox is checked",
          driver, extentedReport, true);

      // Confirm payment
      custdashboardpage = getQuotePage.confirmPayment("STA", testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for MTA transaction status
      expectedTransactionStatus = "Active: Debit And Issue Policy";
      Log.softAssertThat(
          custdashboardpage.verifyTransactionStatus(expectedPosition, expectedTransactionStatus,
              extentedReport, true),
          "Verified that the STA transaction status is '" + expectedTransactionStatus
              + "' as expected",
          "The STA transaction status is not as expected");

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(description = "Verify that it is possible to create a MTA for reduction of cover",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider",
      groups = {"Business_Scenario"})
  public void BS_019(String browser) throws Exception {
    String tcId = "BS_019";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      testData = createACustomerAndPolicy(driver, testData, false, true, extentedReport);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport);

      custdashboardpage.selectMTAfromManagePolicyTab(extentedReport, true);
      custdashboardpage.enterMTADetails(testData, extentedReport, true);
      GetQuotePage getQuotePage = custdashboardpage.verifyGetQuotePage(extentedReport);
      Log.message("Get quote page loaded successfully", driver, extentedReport, true);

      // Remove cover
      String[] coversToRemove = testData.get("coverToSelect").toString().split(",");
      for (int i = 0; i < coversToRemove.length; i++) {
        if (coversToRemove[i].endsWith("_Add")) {
          continue;
        }
        boolean isSuccessful = false;
        String ins_RowtoInteract =
            getQuotePage.SelectInsuranceItem(coversToRemove[i], true, extentedReport);
        isSuccessful = getQuotePage.enterInsCoverDetails(testData, coversToRemove[i],
            ins_RowtoInteract, true, extentedReport);
        String[] coverType = coversToRemove[i].split("_");
        String msg = isSuccessful ? coverType[2] + " " + coverType[0] + " cover done successfully"
            : "Failed to " + coverType[2] + " " + coverType[0] + " cover";
        Log.message(msg, driver, extentedReport, true);
      }

      // Recalculate and buy
      getQuotePage.clickReCalculate(extentedReport);
      getQuotePage.clickBuy(extentedReport);
      getQuotePage.selectAvalilableAdjReasons("Remove Cover", extentedReport, false);
      getQuotePage.clickAddMTA(extentedReport, false);
      getQuotePage.clickAddAllMTANextButton(extentedReport, true);

      Log.softAssertThat(getQuotePage.verifyMTAAdjustmentRadioButtons(true, extentedReport, true),
          "Verified that radio buttons of 'MTA Adjustment' section are displayed as expected",
          "The radio buttons of 'MTA Adjustment' section are not displayed as expected", driver,
          extentedReport, true);

      // Acceptance tab - Payment details verification
      Log.softAssertThat(
          getQuotePage.verifyPaymentDetailsAtAdjustmentPaymentAcceptanceTab(testData,
              extentedReport, true),
          "Verified that the payment details at Acceptance tab matches with the details at Cover tab",
          "The payment details at Acceptance tab does not match with the details at Cover tab",
          driver, extentedReport, true);

      // Acceptance tab - Payment Schedule details verification
      String adjustmentOption = "Spread over Instalments";
      Log.softAssertThat(
          getQuotePage.verifyPaymentScheduleTable(adjustmentOption, testData, extentedReport, true),
          "The 'Payment Schedule' table verification is successful",
          "The 'Payment Schedule' table verification is unsuccessful", driver, extentedReport,
          true);

      getQuotePage.tickCustomerAgreesCheckbox(extentedReport);
      Log.softAssertThat(getQuotePage.verifyAcceptButtonPresence(extentedReport),
          "Verified that the Payment 'Accept' button is present as 'Customer agrees...Direct Debit Guarantee' checkbox is checked",
          "The Payment 'Accept' button is absent unexpectly though 'Customer agrees...Direct Debit Guarantee' checkbox is checked",
          driver, extentedReport, true);

      // Accept Payment
      custdashboardpage = getQuotePage.confirmPayment("MTA", testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // Check for STA transaction status
      String expectedPosition = "MidTermAdjustment";
      String expectedTransactionStatus = "Active: Debit And Issue Policy";
      Log.softAssertThat(
          custdashboardpage.verifyTransactionStatus(expectedPosition, expectedTransactionStatus,
              extentedReport, true),
          "Verified that the MTA transaction status is '" + expectedTransactionStatus
              + "' as expected",
          "The MTA transaction status is not as expected");

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(
      description = "Verify that it is possible to pay off outstanding DD instalments in one lump sum",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider",
      groups = {"Business_Scenario"})
  public void BS_023(String browser) throws Exception {
    String tcId = "BS_023";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      testData = createACustomerAndPolicy(driver, testData, false, false, extentedReport);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport);

      custdashboardpage.selectBAfromManagePolicyTab(extentedReport, true);
      BillingAdjustmentPage billingAdjustmentPage =
          custdashboardpage.enterBADetails(testData, extentedReport, true);

      // Selecting adjustment reason
      String adjustmentReason = testData.get("AdjustmentReason");
      String paymentPlan = testData.get("ChangePayment Plan");
      String paymentMethod = testData.get("ChangePayment Method");
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
      billingAdjustmentPage.selectVisacard(extentedReport);

      // Enter Card Details
      billingAdjustmentPage.enterCardNumber(testData, extentedReport, true);
      billingAdjustmentPage.selectExpiry(testData, extentedReport, true);
      billingAdjustmentPage.enterVerification(extentedReport, true);
      billingAdjustmentPage.enterName(testData, extentedReport, true);
      billingAdjustmentPage.clickbuy(extentedReport, true);

      // Click continue button
      billingAdjustmentPage.clickContinueButton(extentedReport);
      billingAdjustmentPage.clickConfirmBtn(extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);
      Log.softAssertThat(custdashboardpage.verifyPaymentMethod(paymentMethod),
          "Billing Adjustment is completed - Payment method is changed to - " + paymentMethod,
          "Billing Adjustment is not completed - Payment method is not changed to - "
              + paymentMethod,
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

}
