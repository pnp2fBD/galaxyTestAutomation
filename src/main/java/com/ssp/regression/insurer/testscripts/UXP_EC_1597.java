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
import com.ssp.support.EmailReport;
import com.ssp.support.Log;
import com.ssp.utils.*;
import com.ssp.support.WebDriverFactory;
import com.ssp.utils.DataProviderUtils;
import com.ssp.utils.DataUtils;
import com.ssp.uxp_pages.CardDetailsPage;
import com.ssp.uxp_pages.CustDashboardPage;
import com.ssp.uxp_pages.HomePage;
import com.ssp.uxp_pages.LoginPage;
import com.ssp.uxp_pages.NewQuotePage;
import com.ssp.uxp_pages.SearchPage;

@Listeners(EmailReport.class)
public class UXP_EC_1597 extends BaseTest {

  private String webSite;
  public String PCLwebSite;

  @BeforeMethod(alwaysRun = true)
  public void init(ITestContext context) throws IOException {
    webSite = System.getProperty("webSite") != null ? System.getProperty("webSite")
        : context.getCurrentXmlTest().getParameter("webSite");
    PCLwebSite = System.getProperty("PCLwebSite") != null ? System.getProperty("PCLwebSite")
        : context.getCurrentXmlTest().getParameter("PCLwebSite");
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
        testCaseId + " - " + testDesc + " [" + browserwithos + "]", test);
  }

  public HashMap<String, String> getTestData(String testcaseId) {
    String env =
        Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("env");
    String className = "UXP_EC_1597_" + env;
    return DataUtils.testDatabyID(testcaseId, className);
  }

  @Test(
      description = "Create NB policy validate collapse / expand functionality of each tabs in customer dashboard page",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_02_04_05_06_07_08_11_12(String browser) throws Exception {

    String tcId = "TC_02_04_05_06_07_08_11_12";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = testData.get("First Name");
    String lastName = testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String title = testData.get("Title");
    String CustomerName = title + " " + firstName + " " + lastName;
    String Corecover = testData.get("Cover");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call link
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
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);
      Log.softAssertThat(custdashboardpage.verifyContactName(CustomerName, extentedReport, true),
          "Verified Searched FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Details on Dashboard", driver, extentedReport, true);

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
      // Step-8: Get and Buy Quote
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickAddHomeEmergency(extentedReport);
      newquotepage.clickUpgradeNow(extentedReport);
      newquotepage.clickReCalculate(extentedReport);
      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);

      // Newly added payment transaction to select Master/ Visa

      // Select card type
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      Log.pass("Selected VISA Card", driver, extentedReport, true);

      // Step-10: Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.pass("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      Log.message("Clicked on continue button in verification page", extentedReport);
      if (!newquotepage.verifyPaymentTrasaction(extentedReport)) {
        Log.fail("Payment was not successful", driver, extentedReport, true);
      } else {

        Log.pass("Payment was successful", driver, extentedReport, true);
      }


      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // Check for the customerdashboard page tabs
      custdashboardpage.verify_ExpandCollapse_Custdashboard(extentedReport, true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {

        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally

  }// TC_02_04_05_06_07_08_11_12

  @Test(description = "Check the policy bar on customer dashboard page",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_003(String browser) throws Exception {

    String tcId = "TC_003";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = testData.get("First Name");
    String lastName = testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String title = testData.get("Title");
    String CustomerName = title + " " + firstName + " " + lastName;
    String Corecover = testData.get("Cover");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call link
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
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);
      Log.softAssertThat(custdashboardpage.verifyContactName(CustomerName, extentedReport, true),
          "Verified Searched FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Details on Dashboard", driver, extentedReport, true);

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
      // Step-8: Get and Buy Quote
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

      // Step-10: Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.pass("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      Log.message("Clicked on continue button in verification page", extentedReport);


      if (!newquotepage.verifyPaymentTrasaction(extentedReport)) {
        Log.fail("Payment was not successful", driver, extentedReport, true);
      } else {

        Log.pass("Payment was successful", driver, extentedReport, true);
      }


      // confirm payment
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {

        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      custdashboardpage.verify_PolicyTab(policyDetails.get("PolicyNo").toString(), extentedReport,
          true);

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally

  }// TC_003

  @Test(
      description = "Check whether the first menu option in the 'policy information' section is Policy details when a policy is selected",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_018(String browser) throws Exception {

    String tcId = "TC_018";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = testData.get("First Name");
    String lastName = testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String title = testData.get("Title");
    String CustomerName = title + " " + firstName + " " + lastName;
    String Corecover = testData.get("Cover");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call link
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
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);
      Log.softAssertThat(custdashboardpage.verifyContactName(CustomerName, extentedReport, true),
          "Verified Searched FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Details on Dashboard", driver, extentedReport, true);

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
      // Step-8: Get and Buy Quote
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

      // Step-10: Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.pass("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      Log.message("Clicked on continue button in verification page", extentedReport);
      if (!newquotepage.verifyPaymentTrasaction(extentedReport)) {
        Log.fail("Payment was not successful", driver, extentedReport, true);
      } else {

        Log.pass("Payment was successful", driver, extentedReport, true);
      }


      // confirm payment
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {

        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }

      // To check whether the policy details tab has policy cover as the
      // default drop down value
      custdashboardpage.verifyPolDetailTab(extentedReport, true);

      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally

  }// TC_018

  @Test(
      description = "Check whether the navigation button reads 'Policy Details' and show as selected even when the 'policy cover' menu option is selected and check whether the user is able to navigate back to 'Policy Details' section by selecting the menu option 'policy details'",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_019_021_022(String browser) throws Exception {

    String tcId = "TC_019_021_022";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String title = testData.get("Title");
    // String CustomerName = title + " " + firstName + " " + lastName;
    String Corecover = testData.get("Cover");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call link
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
          "Customer Dashboardpage verified", "Customer Dashboardpage not verified", driver,
          extentedReport, true);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(
              title + " " + testData.get("First Name") + " " + testData.get("Last Name"),
              extentedReport, true),
          "Verified Searched FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Details on Dashboard", driver, extentedReport, true);

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
      // Step-8: Get and Buy Quote
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

      // Step-10: Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.pass("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      Log.message("Clicked on continue button in verification page", extentedReport);
      if (!newquotepage.verifyPaymentTrasaction(extentedReport)) {
        Log.fail("Payment was not successful", driver, extentedReport, true);
      } else {

        Log.pass("Payment was successful", driver, extentedReport, true);
      }


      // confirm payment
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {

        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }

      // To validate policy details and policy cover menu items
      // custdashboardpage.SelectMenu_from_policyDetails_tab("Policy Details", extentedReport,
      // true);

      policyDetails.remove("Position");

      if (GenericUtils.compareTwoHashMap(policyDetails,
          custdashboardpage.validate_PolicyDetailsInfo(extentedReport, true)))
        Log.pass("Both policy details tab and view policy quotes has the same policy references",
            driver, extentedReport, true);
      else
        Log.fail(
            "Policy details tab and view policy quotes failed to have the same policy references",
            driver, extentedReport, true);

      // To validate policy details and policy cover menu items
      custdashboardpage.selectMenu_from_policyDetails_tab("Policy cover", extentedReport, true);
      custdashboardpage.validate_PolicyDetailSection();

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally

  }// TC_019_021_022

  // @Test(description = "Create a quoted and NB and validate the cusotmer
  // dashboard page by clicking on the policies", dataProviderClass =
  // DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_025(String browser) throws Exception {

    String tcId = "TC_025";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = testData.get("First Name");
    String lastName = testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String title = testData.get("Title");
    String CustomerName = title + " " + firstName + " " + lastName;
    // String Corecover = testData.get("Cover");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);

      /*
       * // Click on Take Call link homePage.clickTakeCall(extentedReport);
       * homePage.clickMyBrands(brandname, extentedReport); SearchPage searchPage = new
       * SearchPage(driver, extentedReport).get();
       * Log.softAssertThat(searchPage.uielement.verifyPageElements(Arrays
       * .asList("txtPolicyNumber"), searchPage), "Search Page Verified",
       * "Search Page not Verified", driver, extentedReport, true);
       * 
       * // Enter Customer Details searchPage.clickCreateCustomer(true, extentedReport);
       * searchPage.enterCustomerDetails(testData, true, extentedReport);
       * 
       * // Confirm customer details and create customer CustDashboardPage custdashboardpage =
       * searchPage.confirmCreateCustomer(true, extentedReport); Log.softAssertThat(
       * custdashboardpage.uielement.verifyPageElements(Arrays.asList( "lnkEditDetails"),
       * custdashboardpage), "custdashboardpage Verified", "custdashboardpage not Verified", driver,
       * extentedReport, true); Log.softAssertThat(custdashboardpage.verifyContactName(
       * CustomerName, extentedReport, true),
       * "Verified Searched FirstName and LastName on Customer Dashboard",
       * "Not Verified Customer Details on Dashboard", driver, extentedReport, true);
       * 
       * // Create New Quote custdashboardpage.clickNewQuote(true, extentedReport);
       * custdashboardpage.enterQuoteDetails(testData, true, extentedReport); NewQuotePage
       * newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);
       * 
       * // Create New Quote Log.softAssertThat(newquotepage.verifyNewQuotePage(true,
       * extentedReport), "Verified NewQuotePage ", "Not verified NewQuotePage", driver,
       * extentedReport, true);
       * 
       * newquotepage.enterPolicyDetails(testData, true, extentedReport);
       * newquotepage.clickNextOne(extentedReport); newquotepage.enterCustomerDetails(testData,
       * true, Corecover, extentedReport); newquotepage.clickNextTwo(extentedReport);
       * newquotepage.clickView(extentedReport); newquotepage.clickAgree(extentedReport); // Step-8:
       * Get and Buy Quote newquotepage.clickGetQuote(extentedReport);
       * newquotepage.clickAddHomeEmergency(extentedReport);
       * newquotepage.clickUpgradeNow(extentedReport);
       * newquotepage.clickReCalculate(extentedReport); newquotepage.clickBuy(extentedReport);
       * newquotepage.selectPayment(testData, true, extentedReport);
       * 
       * // confirm payment custdashboardpage =
       * newquotepage.confirmPayment(testData,extentedReport); Log.softAssertThat(
       * custdashboardpage.uielement.verifyPageElements(Arrays.asList( "lnkEditDetails"),
       * custdashboardpage), "custdashboardpage Verified", "custdashboardpage not Verified", driver,
       * extentedReport, true);
       * 
       * // check for policy status HashMap<String, String> policyDetails =
       * custdashboardpage.getPolicyURN(extentedReport); if
       * (policyDetails.get("Status").toString().equalsIgnoreCase( "Accepted")) {
       * 
       * Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport,
       * true); } else { Log.fail("Failed - NB Policy Created in " +
       * policyDetails.get("Status").toString() + " status", driver, extentedReport, true); }
       * 
       * // Create a quoted policy
       * 
       * Log.message("Creating a Quoted policy", extentedReport);
       * custdashboardpage.clickNewQuote(true, extentedReport);
       * custdashboardpage.enterQuoteDetails(testData, true, extentedReport); newquotepage =
       * custdashboardpage.clickContinueQuote(true, extentedReport);
       * 
       * // Create New Quote Log.softAssertThat(newquotepage.verifyNewQuotePage(true,
       * extentedReport), "Verified NewQuotePage ", "Not verified NewQuotePage", driver,
       * extentedReport, true);
       * 
       * newquotepage.enterPolicyDetails(testData, true, extentedReport);
       * newquotepage.clickNextOne(extentedReport); newquotepage.enterCustomerDetails(testData,
       * true, Corecover, extentedReport); newquotepage.clickNextTwo(extentedReport);
       * newquotepage.clickView(extentedReport); newquotepage.clickAgree(extentedReport); // Step-8:
       * Get and Buy Quote newquotepage.clickGetQuote(extentedReport);
       * newquotepage.clickAddHomeEmergency(extentedReport);
       * newquotepage.clickUpgradeNow(extentedReport);
       * newquotepage.clickReCalculate(extentedReport); custdashboardpage =
       * newquotepage.clicksave(extentedReport); Log.softAssertThat(
       * custdashboardpage.uielement.verifyPageElements(Arrays.asList( "lnkEditDetails"),
       * custdashboardpage), "custdashboardpage Verified", "custdashboardpage not Verified", driver,
       * extentedReport, true);
       */
      /// Navigate to customer dashboard page and verify the nb and quoted
      /// policies

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Search with Valid Policy Number
      searchPage.searchValidPolicy("251", true, extentedReport);
      Log.message("Searching with Valid Policy Number", driver, extentedReport);
      CustDashboardPage custdashboardPage =
          searchPage.selectPolicy_from_SearchPage(true, extentedReport);

      Log.pass("Navigated to Cutomer Dashboard after selecting policy from search page", driver,
          extentedReport, true);

      // Verifying Customer Details
      custdashboardPage.clickPassVerification(extentedReport, true);
      custdashboardPage.verifyCustomerDashboardPage();
      Log.softAssertThat(custdashboardPage.verifyContactName(CustomerName, extentedReport, true),
          "Verified FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Details on Dashboard", driver, extentedReport, true);
      HashMap<String, String> policyDetails = custdashboardPage.getPolicyURN(extentedReport);

      custdashboardPage.click_PoliciesQuotesChkBox("Policies", "Select", extentedReport, true);

      // Validte the view more qutes section with policy information
      // section

      policyDetails.remove("Position");

      if (GenericUtils.compareTwoHashMap(policyDetails,
          custdashboardPage.validate_PolicyDetailsInfo(extentedReport, true)))
        Log.pass("Both policy details tab and view policy quotes has the same policy references",
            driver, extentedReport, true);
      else
        Log.fail(
            "Policy details tab and view policy quotes failed to have the same policy references",
            driver, extentedReport, true);
      custdashboardPage.click_PoliciesQuotesChkBox("Policies", "Deselect", extentedReport, true);

      custdashboardPage.click_PoliciesQuotesChkBox("Quotes", "Select", extentedReport, true);
      HashMap<String, String> quotedetails = custdashboardPage.getPolicyURN(extentedReport);
      quotedetails.remove("Position");

      if (GenericUtils.compareTwoHashMap(quotedetails,
          custdashboardPage.validate_PolicyDetailsInfo(extentedReport, true)))
        Log.pass(
            "Both policy details tab and view policy quotes has the same policy references for quoted policy",
            driver, extentedReport, true);
      else
        Log.fail(
            "Policy details tab and view policy quotes failed to have the same policy references for quoted policy",
            driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally

  }// TC_025

  // TC_24
  @Test(
      description = "Check whether the 'View Policy Notes' section does not have a heading 'Policy Notes' below the bar",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_024(String browser) throws Exception {

    String tcId = "TC_024";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = testData.get("First Name");
    String lastName = testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String title = testData.get("Title");
    String CustomerName = title + " " + firstName + " " + lastName;
    String Corecover = testData.get("Cover");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call link
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
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);
      Log.softAssertThat(custdashboardpage.verifyContactName(CustomerName, extentedReport, true),
          "Verified Searched FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Details on Dashboard", driver, extentedReport, true);

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
      // Step-8: Get and Buy Quote
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

      // Step-10: Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.pass("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      Log.message("Clicked on continue button in verification page", extentedReport);
      if (!newquotepage.verifyPaymentTrasaction(extentedReport)) {
        Log.fail("Payment was not successful", driver, extentedReport, true);
      } else {

        Log.pass("Payment was successful", driver, extentedReport, true);
      }


      // confirm payment
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {

        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }

      // Validate policy notes section
      custdashboardpage.validate_PolicyNotesSection(extentedReport, true);

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally

  }// TC_024

  @Test(
      description = "Check whether 'View more Quotes and Policies' tables are hoverable and able to select the row",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_015(String browser) throws Exception {

    String tcId = "TC_015";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = testData.get("First Name");
    String lastName = testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String title = testData.get("Title");
    String CustomerName = title + " " + firstName + " " + lastName;
    String Corecover = testData.get("Cover");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call link
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
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);
      Log.softAssertThat(custdashboardpage.verifyContactName(CustomerName, extentedReport, true),
          "Verified Searched FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Details on Dashboard", driver, extentedReport, true);

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
      // Step-8: Get and Buy Quote
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

      // Step-10: Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.pass("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      Log.message("Clicked on continue button in verification page", extentedReport);
      if (!newquotepage.verifyPaymentTrasaction(extentedReport)) {
        Log.fail("Payment was not successful", driver, extentedReport, true);
      } else {

        Log.pass("Payment was successful", driver, extentedReport, true);
      }


      // confirm payment
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {

        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      // to validate mouse hover on view more quotes and policies tab
      custdashboardpage.validate_mousehover_tabs(extentedReport, true);

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally

  }// TC_015

}
