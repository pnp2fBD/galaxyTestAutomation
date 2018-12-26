package com.ssp.regression.insurer.testscripts;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import com.ssp.support.WebDriverFactory;
import com.ssp.utils.DataProviderUtils;
import com.ssp.utils.DataUtils;
import com.ssp.utils.GenericUtils;
import com.ssp.uxp_pages.BuyQuotePage;
import com.ssp.uxp_pages.CardDetailsPage;
import com.ssp.uxp_pages.CustDashboardPage;
import com.ssp.uxp_pages.GetQuotePage;
import com.ssp.uxp_pages.HomePage;
import com.ssp.uxp_pages.LoginPage;
import com.ssp.uxp_pages.ManageContactDetailsPage;
import com.ssp.uxp_pages.NewQuotePage;
import com.ssp.uxp_pages.SearchPage;
import com.ssp.uxp_pages.SuspendBillingPage;

@Listeners(EmailReport.class)
public class UXP_EC_1613 extends BaseTest {
  private String webSite;
  public String monthlywebSite;

  String firstName = "";
  String lastName = "";
  String quoteDescription1 = "TestDescription 1";
  String quoteDescription2 = "TestDescription 2";
  String quoteDescription3 = "TestDescription 3";

  @BeforeMethod(alwaysRun = true)
  public void init(ITestContext context) throws IOException {
    webSite = System.getProperty("webSite") != null ? System.getProperty("webSite")
        : context.getCurrentXmlTest().getParameter("webSite");
    monthlywebSite =
        System.getProperty("monthlywebSite") != null ? System.getProperty("monthlywebSite")
            : context.getCurrentXmlTest().getParameter("monthlywebSite");
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
    String className = "UXP_EC_1613_" + env;
    return DataUtils.testDatabyID(testcaseId, className);
  }

  @Test(
      description = "To check that the latest renewal position of the policy is only displayed, if renewal is not blocked for that policy. To check that the 'Manage Renewals' tab is displayed, if the User has the permission to perform 'invite renewal'. To check that the 'Manage Renewals' tab is displayed, if the policy is within the date tolerances for renewal as defined on the product / scheme",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_337_340_342_374_375(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_337_340_342_374_375";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");
    String brandname = testData.get("Brand Name");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.message("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard" + custDetails.get("First Name")
              + " " + custDetails.get("Last Name"),
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal

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

        /// Add search for the existing IP contact
        custdashboardpage.clickComplete(extentedReport);
        Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
            "Login failed", driver, extentedReport, true);

        // Click on Take Call link
        homePage.clickTakeCall(extentedReport);
        homePage.clickMyBrands(brandname, extentedReport);
        Log.softAssertThat(
            searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
            "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

        // Search for valid contact
        searchPage.enterLastName(custDetails.get("Last Name"), extentedReport);
        searchPage.clickSearch(extentedReport);
        custdashboardpage = searchPage.selectPolicy_from_SearchPage(true, extentedReport);

        // Verifying Customer Details
        custdashboardpage.clickPassVerification(extentedReport, true);
        custdashboardpage.verifyCustomerDashboardPage();
        Log.softAssertThat(
            custdashboardpage.verifyContactName(testData.get("Title") + " "
                + custDetails.get("First Name") + " " + custDetails.get("Last Name"),
                extentedReport, true),
            "Verified FirstName and LastName on Customer Dashboard",
            "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

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
        // Select Payment
        newquotepage.selectPayment(testData, true, extentedReport);

        // Select card type
        newquotepage.takePayment(extentedReport);
        newquotepage.selectVisacard(extentedReport);

        // Enter Card Details
        carddetailspage.enterCardNumber(testData, extentedReport, true);
        carddetailspage.selectExpiry(testData, extentedReport, true);
        carddetailspage.enterVerification(extentedReport, true);
        carddetailspage.enterName(testData, extentedReport, true);
        carddetailspage.clickbuy(extentedReport, true);
        Log.message("Entered Card Details and Navigated to Verificaion page", driver,
            extentedReport, true);

        // Click continue button
        newquotepage = carddetailspage.clickContinueButton(extentedReport);
        Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
            "Payment was successful", "Payment was not successful", driver, extentedReport, true);

        custdashboardpage = newquotepage.rnlconfirmPayment(testData, extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
            true);
        HashMap<String, String> policyDetailsrnl = custdashboardpage.getPolicyURN(extentedReport);
        Log.softAssertThat(policyDetailsrnl.get("Position").toString().equalsIgnoreCase("Renewal"),
            "Policy position displaying as Renewal", "Latest position is not displaying");

        Log.softAssertThat(policyDetails.get("Status").toString().equalsIgnoreCase("Accepted"),
            "Policy status displaying as accepted", "Policy status not displaying as accepted");

        // check for policy status

      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
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
  }

  @Test(
      description = "To check that the Manage Renewals tab is NOT displayed, if the User does NOT have the permission to perform invite renewal, To check that the 'Manage Renewals' tab is NOT displayed, if the policy is NOT within the date tolerances for renewal as defined on the product / scheme ",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_341_343(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_341_343";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");
    String brandname = testData.get("Brand Name");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.message("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard" + custDetails.get("First Name")
              + " " + custDetails.get("Last Name"),
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      /*
       * custdashboardpage.enterNewQuotePastDate(5,1,extentedReport,true); //Past date for renewal
       * custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
       */
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

        custdashboardpage.clickComplete(extentedReport);
        Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
            "Login failed", driver, extentedReport, true);

        // Click on Take Call link
        homePage.clickTakeCall(extentedReport);
        homePage.clickMyBrands(brandname, extentedReport);
        Log.softAssertThat(
            searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
            "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

        // Search for valid contact
        searchPage.enterLastName(custDetails.get("Last Name"), extentedReport);
        searchPage.clickSearch(extentedReport);
        custdashboardpage = searchPage.selectPolicy_from_SearchPage(true, extentedReport);

        // Verifying Customer Details
        custdashboardpage.clickPassVerification(extentedReport, true);
        custdashboardpage.verifyCustomerDashboardPage();
        Log.softAssertThat(
            custdashboardpage.verifyContactName(testData.get("Title") + " "
                + custDetails.get("First Name") + " " + custDetails.get("Last Name"),
                extentedReport, true),
            "Verified FirstName and LastName on Customer Dashboard",
            "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElementsDoNotExist(
                Arrays.asList("drpManageRenewals"), custdashboardpage),
            "Manage renewal tab is not dispalyed on customer dashaboard page",
            "Manage renewal tab is dispalying on customer dashboard page");

      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
            driver, extentedReport, true);
        Log.testCaseResult(extentedReport);

      }
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally

  }

  @Test(
      description = "To check that the 'Manage Renewals' tab is NOT displayed, if the policy is in a 'Cancelled' status,  To check that the 'Manage Renewals' tab is displayed, if the cancelled policy is re-instatement ",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 4)
  public void TC_344_346(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_344_346";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");
    String brandname = testData.get("Brand Name");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.message("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard" + custDetails.get("First Name")
              + " " + custDetails.get("Last Name"),
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      /*
       * custdashboardpage.enterNewQuotePastDate(5,1,extentedReport,true); //Past date for renewal
       * custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
       */
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

        custdashboardpage.clickComplete(extentedReport);
        Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
            "Login failed", driver, extentedReport, true);

        // Click on Take Call link
        homePage.clickTakeCall(extentedReport);
        homePage.clickMyBrands(brandname, extentedReport);
        Log.softAssertThat(
            searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
            "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

        // Search for valid contact
        searchPage.enterLastName(custDetails.get("Last Name"), extentedReport);
        searchPage.clickSearch(extentedReport);
        custdashboardpage = searchPage.selectPolicy_from_SearchPage(true, extentedReport);

        // Verifying Customer Details
        custdashboardpage.clickPassVerification(extentedReport, true);
        custdashboardpage.verifyCustomerDashboardPage();
        Log.softAssertThat(
            custdashboardpage.verifyContactName(testData.get("Title") + " "
                + custDetails.get("First Name") + " " + custDetails.get("Last Name"),
                extentedReport, true),
            "Verified FirstName and LastName on Customer Dashboard",
            "Not Verified Customer Name on Dashboard", driver, extentedReport, true);
        // Cancel the policy
        custdashboardpage.clickCancelPolicy(extentedReport);
        custdashboardpage.clickStandardCancelletion(extentedReport);
        custdashboardpage.enterRequestCancellationDetails("No Refund", "Better Quote",
            extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElementsDoNotExist(
                Arrays.asList("drpManageRenewals"), custdashboardpage),
            "Manage renewal tab is not dispalyed on customer dashaboard page",
            "Manage renewal tab is dispalyed on customer dashboard page");
        // Reinstate the policy
        custdashboardpage.clickReinstatePolicy(true, extentedReport);
        custdashboardpage.selectReinstateReason(testData, true, extentedReport);
        custdashboardpage.clickCalculateForReinstate(false, extentedReport);
        custdashboardpage.clickConfirmForReinstate(true, extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElementsDoNotExist(
                Arrays.asList("drpManageRenewals"), custdashboardpage),
            "Manage renewal tab is not dispalyed on customer dashaboard page",
            "Manage renewal tab is dispalyed on customer dashboard page");

        Log.testCaseResult(extentedReport);
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
            driver, extentedReport, true);
        Log.testCaseResult(extentedReport);
      }
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
      description = "To check that the user landed on the 'data capture' screens,if the user click on 'Continue'option, To check that the existing policy details is displayed by default in the 'Data capture' screen, To check that the new cover start date is displayed by default and it is protected , To check that the 'Invite Renewal' option is available for selection, if the policy is eligible for renewal ",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_354_355_357_349_358(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_354_355_357_358";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");
    String brandname = testData.get("Brand Name");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.message("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard" + custDetails.get("First Name")
              + " " + custDetails.get("Last Name"),
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal

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

        /// Add search for the existing IP contact
        custdashboardpage.clickComplete(extentedReport);
        Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
            "Login failed", driver, extentedReport, true);

        // Click on Take Call link
        homePage.clickTakeCall(extentedReport);
        homePage.clickMyBrands(brandname, extentedReport);
        Log.softAssertThat(
            searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
            "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

        // Search for valid contact
        searchPage.enterLastName(custDetails.get("Last Name"), extentedReport);
        searchPage.clickSearch(extentedReport);
        custdashboardpage = searchPage.selectPolicy_from_SearchPage(true, extentedReport);

        // Verifying Customer Details
        custdashboardpage.clickPassVerification(extentedReport, true);
        custdashboardpage.verifyCustomerDashboardPage();
        Log.softAssertThat(
            custdashboardpage.verifyContactName(testData.get("Title") + " "
                + custDetails.get("First Name") + " " + custDetails.get("Last Name"),
                extentedReport, true),
            "Verified FirstName and LastName on Customer Dashboard",
            "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("drpManageRenewals"),
                custdashboardpage),
            "Manage renewal tab is dispalyed on customer dashaboard page",
            "Manage renewal tab is not dispalying on customer dashboard page");

        custdashboardpage.performRenewals(extentedReport, true);

        // Verify Customer Dashboard page
        Log.softAssertThat(newquotepage.verifyTabTitles("datacapture", true, extentedReport),
            "Naviagted to policy data capture page with title as Data capture",
            "Not navigated to policy data capture page");
        Log.softAssertThat(
            newquotepage.verifyExistedData(testData, policyDetails.get("ExceptionDate"), true,
                extentedReport),
            "Existed data displaying on policy data capture page",
            " Existed data missing on policy data capture page");
        Log.softAssertThat(
            newquotepage.rnlIncpt(policyDetails.get("InceptionDate"), true, extentedReport),
            "New cover start date displayed and protected on date capture page",
            " NB start date displayed on date capture page");
        newquotepage.clickNextOne(extentedReport);
        newquotepage.clickNextTwo(extentedReport);
        newquotepage.clickView(extentedReport);
        newquotepage.clickAgree(extentedReport);
        newquotepage.clickGetQuote(extentedReport);
        newquotepage.clickQuoteSave("Renew", "Renewal Quote", extentedReport);

        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
            true);

      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
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
  }

  @Test(
      description = "To check that the 'Save' and 'Get Quote' options are available, if the declaration has been confirmed ",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")

  public void TC_359_361(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_359_361";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");
    String brandname = testData.get("Brand Name");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.message("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard" + custDetails.get("First Name")
              + " " + custDetails.get("Last Name"),
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal

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
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(
              Arrays.asList("btn_SaveQuoteAfterTerms", "btnGetQuote"), newquotepage),
          "Save and getquote buttons are displayed on customer dashboard page",
          "Save and Getquote buttons are not dispalying on customer dashboard page", driver,
          extentedReport, true);
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

        /// Add search for the existing IP contact
        custdashboardpage.clickComplete(extentedReport);
        Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
            "Login failed", driver, extentedReport, true);

        // Click on Take Call link
        homePage.clickTakeCall(extentedReport);
        homePage.clickMyBrands(brandname, extentedReport);
        Log.softAssertThat(
            searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
            "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

        // Search for valid contact
        searchPage.enterLastName(custDetails.get("Last Name"), extentedReport);
        searchPage.clickSearch(extentedReport);
        custdashboardpage = searchPage.selectPolicy_from_SearchPage(true, extentedReport);

        // Verifying Customer Details
        custdashboardpage.clickPassVerification(extentedReport, true);
        custdashboardpage.verifyCustomerDashboardPage();
        Log.softAssertThat(
            custdashboardpage.verifyContactName(testData.get("Title") + " "
                + custDetails.get("First Name") + " " + custDetails.get("Last Name"),
                extentedReport, true),
            "Verified FirstName and LastName on Customer Dashboard",
            "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

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
        Log.softAssertThat(
            newquotepage.uielement.verifyPageElements(Arrays.asList("btnSaveonRNL", "btnGetQuote"),
                newquotepage),
            "Save and getquote buttons are displayed on customer dashboard page",
            "Save and Getquote buttons are not dispalying on customer dashboard page", driver,
            extentedReport, true);
        newquotepage.clickGetQuote(extentedReport);
        Log.softAssertThat(newquotepage.verifyTabTitles("pricepresentation", true, extentedReport),
            "Navigated to Price presentation page", "Not navigated to price prsentation page",
            driver, extentedReport, true);
        newquotepage.clickQuoteSave("Renew", "Renewal Quote", extentedReport);

        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
            true);

      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
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
  }

  @Test(
      description = "To check that the 'Save' and 'Get Quote' options are available, if the declaration has been confirmed ",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")

  public void TC_349_351(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_349_351";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");
    String brandname = testData.get("Brand Name");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.message("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard" + custDetails.get("First Name")
              + " " + custDetails.get("Last Name"),
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal

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
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(
              Arrays.asList("btn_SaveQuoteAfterTerms", "btnGetQuote"), newquotepage),
          "Save and getquote buttons are displayed on customer dashboard page",
          "Save and Getquote buttons are not dispalying on customer dashboard page", driver,
          extentedReport, true);
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

        /// Add search for the existing IP contact
        custdashboardpage.clickComplete(extentedReport);
        Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
            "Login failed", driver, extentedReport, true);

        // Click on Take Call link
        homePage.clickTakeCall(extentedReport);
        homePage.clickMyBrands(brandname, extentedReport);
        Log.softAssertThat(
            searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
            "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

        // Search for valid contact
        searchPage.enterLastName(custDetails.get("Last Name"), extentedReport);
        searchPage.clickSearch(extentedReport);
        custdashboardpage = searchPage.selectPolicy_from_SearchPage(true, extentedReport);

        // Verifying Customer Details
        custdashboardpage.clickPassVerification(extentedReport, true);
        custdashboardpage.verifyCustomerDashboardPage();
        Log.softAssertThat(
            custdashboardpage.verifyContactName(testData.get("Title") + " "
                + custDetails.get("First Name") + " " + custDetails.get("Last Name"),
                extentedReport, true),
            "Verified FirstName and LastName on Customer Dashboard",
            "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("drpManageRenewals"),
                custdashboardpage),
            "Manage renewal tab is dispalyed on customer dashaboard page",
            "Manage renewal tab is not dispalying on customer dashboard page");
        custdashboardpage.clickManageRenewal(extentedReport, true);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("drpoptInviteRenewals"),
                custdashboardpage),
            "Invite renewal tab dispalyed on customer dashaboard page",
            "Invite renewal tab not dispalying on customer dashboard page");

      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
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
  }

  @Test(
      description = "To check that the following buttons are available in the 'Price presentation' screen Back to 'Data Capture' 'Save' 'Buy' and To check that the renewal of selected policy is saved as ‘Review Required’ and the User returned to the Customer Dashboard, if the User selects 'Save' option in the 'Price presentation' page",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")

  public void TC_362_364(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_362_364";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");
    String brandname = testData.get("Brand Name");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.message("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard" + custDetails.get("First Name")
              + " " + custDetails.get("Last Name"),
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal

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
      Log.softAssertThat(newquotepage.verifyTabTitles("acceptance", true, extentedReport),
          "Naviagted to Acceptance page ", "Not navigated to Acceptance page");

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
        custdashboardpage.performRenewals(extentedReport, true);
        newquotepage.clickNextOne(extentedReport);
        newquotepage.clickNextTwo(extentedReport);
        newquotepage.clickView(extentedReport);
        newquotepage.clickAgree(extentedReport);
        newquotepage.clickGetQuote(extentedReport);
        Log.softAssertThat(
            newquotepage.uielement.verifyPageElements(
                Arrays.asList("btn_renewQuoteSave", "btnRenewalQuoteBuy", "btnRnlBacktoDashboard"),
                newquotepage),
            "Save, Buy and Back to Dashoboard buttons are displaying on price presentation page",
            "Save, Buy and Back to Dashoboard buttons are not displaying on price presentation page",
            driver, extentedReport, true);
        newquotepage.clickQuoteSave("Renew", "Renewal Quote", extentedReport);
        // TC_361
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Navigated to customer dashboard page", "Not navigated to customer dashboard page",
            driver, extentedReport, true);
        HashMap<String, String> policyDetailsrnl = custdashboardpage.getPolicyURN(extentedReport);

        // TC_362
        Log.softAssertThat(
            policyDetailsrnl.get("notification_plcy").toString().equalsIgnoreCase("R"),
            "Policy position displaying as Renewal", "Latest position is not displaying");
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
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
  }

  @Test(
      description = "To check that the user is return back to 'Data capture' screen, if the User selects 'Back to Data Capture' option",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")

  public void TC_363(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_363";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");
    String brandname = testData.get("Brand Name");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.message("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard" + custDetails.get("First Name")
              + " " + custDetails.get("Last Name"),
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal

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
        custdashboardpage.performRenewals(extentedReport, true);
        newquotepage.clickNextOne(extentedReport);
        newquotepage.clickNextTwo(extentedReport);
        newquotepage.clickView(extentedReport);
        newquotepage.clickAgree(extentedReport);
        newquotepage.clickGetQuote(extentedReport);
        newquotepage.clickbacktoDatacapture(extentedReport);
        Log.softAssertThat(newquotepage.verifyTabTitles("datacapture", true, extentedReport),
            "Naviagted to policy data capture page with title as Data capture",
            "Not navigated to policy data capture page");

      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
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
  }

  @Test(
      description = "To check that the user is allowed to modify the 'Change Payment Plan / Method' details in the 'Acceptance' screen. To check that the user is allowed to make payment in accordance with payment method chosen in the 'Acceptance' screen",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")

  public void TC_366_369_371_367(String browser) throws Exception {
    // Get the web driver instance
    String tcId = "TC_366_369_371_367";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");
    String brandname = testData.get("Brand Name");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.message("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard" + custDetails.get("First Name")
              + " " + custDetails.get("Last Name"),
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal

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
        custdashboardpage.performRenewals(extentedReport, true);
        newquotepage.clickNextOne(extentedReport);
        newquotepage.clickNextTwo(extentedReport);
        newquotepage.clickView(extentedReport);
        newquotepage.clickAgree(extentedReport);
        newquotepage.clickGetQuote(extentedReport);
        newquotepage.clickRenewalQuoteBuy(extentedReport);

        // TC_366
        Log.softAssertThat(
            newquotepage.verifyPayorName(testData.get("Title") + " " + custDetails.get("First Name")
                + " " + custDetails.get("Last Name")),
            "Change payor and customer name dispaying as same",
            "Change payor and customer name not dispaying as same");
        newquotepage.selectPaymentPlan("Monthly", extentedReport);
        newquotepage.selectPaymentMethod("Direct Debit", extentedReport);
        Log.softAssertThat(newquotepage.selectPayment("Monthly", testData, true, extentedReport),
            "Customer able to modify the payment details and selected the Monthly policy agreement check box",
            "Customer not able to modify the payment details and Monthly policy agreement check box not displayed");
        custdashboardpage = newquotepage.confirmPaymentRnl("Monthly", extentedReport);
        Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
            "Customer able to pay the amount as per payment plan",
            " Customer not able to pay as per the payment plan");

      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
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
  }

  @Test(
      description = "To check that the user is allowed to modify the 'Change Payor' details in the Acceptance screen",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 1)
  public void TC_370(String browser) throws Exception {

    String tcId = "TC_370";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
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
      // HashMap<String, String> custDetails1 =
      // searchPage.enterJointCustomerDetails(testData, true,
      // extentedReport);
      List<String> jointPolicyholderdetails =
          searchPage.enterJointCustomerDetails(testData, true, extentedReport);
      CustDashboardPage custdashboardpage1 = searchPage.confirmCreateCustomer(true, extentedReport);
      System.out.println("customer first name" + jointPolicyholderdetails.get(0)
          + "Customer last name" + jointPolicyholderdetails.get(1));

      custdashboardpage1.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport, true);
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);

      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard" + custDetails.get("First Name")
              + " " + custDetails.get("Last Name"),
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);
      System.out.println("customer first name" + jointPolicyholderdetails.get(0)
          + "Customer last name" + jointPolicyholderdetails.get(1));
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
      String payorname = newquotepage.enterChangePayorDetails(jointPolicyholderdetails.get(1),
          jointPolicyholderdetails.get(0), "AB101AH", extentedReport, true);
      Log.softAssertThat(newquotepage.verifyPayorName(payorname),
          "The user is able to change the payor of the policy, once the quote reached Acceptance page : "
              + payorname,
          "The user is not able to change the payor of the policy, once the quote reached Acceptance page : "
              + payorname,
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
      description = "To check that the user is allowed to modify the 'User Preferences' details in the 'Acceptance' screen",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_367(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_367";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");
    String brandname = testData.get("Brand Name");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.message("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard" + custDetails.get("First Name")
              + " " + custDetails.get("Last Name"),
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal

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

        /// Add search for the existing IP contact
        custdashboardpage.clickComplete(extentedReport);
        Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
            "Login failed", driver, extentedReport, true);

        // Click on Take Call link
        homePage.clickTakeCall(extentedReport);
        homePage.clickMyBrands(brandname, extentedReport);
        Log.softAssertThat(
            searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
            "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

        // Search for valid contact
        searchPage.enterLastName(custDetails.get("Last Name"), extentedReport);
        searchPage.clickSearch(extentedReport);
        custdashboardpage = searchPage.selectPolicy_from_SearchPage(true, extentedReport);

        // Verifying Customer Details
        custdashboardpage.clickPassVerification(extentedReport, true);
        custdashboardpage.verifyCustomerDashboardPage();
        Log.softAssertThat(
            custdashboardpage.verifyContactName(testData.get("Title") + " "
                + custDetails.get("First Name") + " " + custDetails.get("Last Name"),
                extentedReport, true),
            "Verified FirstName and LastName on Customer Dashboard",
            "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

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
        Log.softAssertThat(newquotepage.selectRadioButton("Post"),
            "Preference details able to modify on acceptance page",
            "Preference details not able to modify on accpetance page");
        // Select Payment
        newquotepage.selectPayment(testData, true, extentedReport);

        // Select card type
        newquotepage.takePayment(extentedReport);
        newquotepage.selectVisacard(extentedReport);

        // Enter Card Details
        carddetailspage.enterCardNumber(testData, extentedReport, true);
        carddetailspage.selectExpiry(testData, extentedReport, true);
        carddetailspage.enterVerification(extentedReport, true);
        carddetailspage.enterName(testData, extentedReport, true);
        carddetailspage.clickbuy(extentedReport, true);

        Log.message("Entered Card Details and Navigated to Verificaion page", driver,
            extentedReport, true);

        // Click continue button
        newquotepage = carddetailspage.clickContinueButton(extentedReport);
        Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
            "Payment was successful", "Payment was not successful", driver, extentedReport, true);

        custdashboardpage = newquotepage.rnlconfirmPayment(testData, extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
            true);
        HashMap<String, String> policyDetailsrnl = custdashboardpage.getPolicyURN(extentedReport);
        Log.softAssertThat(policyDetailsrnl.get("Position").toString().equalsIgnoreCase("Renewal"),
            "Policy position displaying as Renewal", "Latest position is not displaying");

        Log.softAssertThat(policyDetails.get("Status").toString().equalsIgnoreCase("Accepted"),
            "Policy status displaying as accepted", "Policy status not displaying as accepted");

        // check for policy status

      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
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
  }

  @Test(description = "Check that the user is allowed to Review/Amend T’s and C’s details",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_368(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_368";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    String brandname = testData.get("Brand Name");
    String policyToSelect = testData.get("Policy Name");
    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
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
      // Select Payment
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

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
        custdashboardpage.performRenewals_LapseRenewal("Invite Renewal", extentedReport, true);
        newquotepage.clickNextOne(extentedReport);
        newquotepage.clickNextTwo(extentedReport);
        newquotepage.clickView(extentedReport);
        newquotepage.clickAgree(extentedReport);
        newquotepage.clickGetQuote(extentedReport);
        newquotepage.clickRenewalQuoteBuy(extentedReport);
        BuyQuotePage buyQuotePage = new BuyQuotePage(driver, extentedReport);
        buyQuotePage.clickTermsAndConditionSection(extentedReport);
        buyQuotePage.clickAttachTermAndConditionButton(extentedReport);
        buyQuotePage.selectTandCPolicy(policyToSelect, extentedReport, true);

        // Verify T&C is added
        Log.softAssertThat(
            buyQuotePage.verifyTandCPolicyadded(policyToSelect, extentedReport, false),
            policyToSelect + " - T&C is added under terms & condition section",
            policyToSelect + " - T&C is not added under terms & condition section", driver,
            extentedReport, true);

        Log.softAssertThat(buyQuotePage.verifyTandC_Edited(policyToSelect, extentedReport, true),
            "User is able to edit the text when click on 'Edit' button with correct skills profile",
            "User is unable to edit the text when click on 'Edit' button with correct skills profile",
            driver, extentedReport, true);

      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
            driver, extentedReport, true);
      }

      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      // driver.quit();
    } // finally

  }

  @Test(
      description = "To check that the Notification column is displayed with 'L' flag for lapsed renewal",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_376_378(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_376_378";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    String brandname = testData.get("Brand Name");
    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
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
      // Select Payment
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

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
        custdashboardpage.performRenewals_LapseRenewal("Invite Renewal", extentedReport, true);
        newquotepage.clickNextOne(extentedReport);
        newquotepage.clickNextTwo(extentedReport);
        newquotepage.clickView(extentedReport);
        newquotepage.clickAgree(extentedReport);
        newquotepage.clickGetQuote(extentedReport);
        newquotepage.clickRenewalQuoteBuy(extentedReport);
        BuyQuotePage buyQuotePage = new BuyQuotePage(driver, extentedReport);
        buyQuotePage.clicksave_LapsebuttoninAcceptancepage("", "", "Lapse", extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Navigated to custdashboardpage successfully after quote lapse",
            "Failed to Navigate to custdashboardpage after quote lapse", driver, extentedReport,
            true);
        // Verify the lapsed status of all the quotes
        HashMap<String, String> policyDetailsrnl = custdashboardpage.getPolicyURN(extentedReport);
        Log.softAssertThat(
            policyDetailsrnl.get("notification_plcy").toString().equalsIgnoreCase("L"),
            "Policy position displaying as Lapsed", "Latest position is not displaying");

      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
            driver, extentedReport, true);
      }

      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      // driver.quit();
    } // finally

  }

  @Test(
      description = "The Notification column should be displayed with 'R' flag (red olior) for review required selected policy. The position and Status of the policy which reqiure review for renewal the policy, should be displayed as 'Renewal' and status as 'Review Required'",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_379_380_381(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_379_380_381";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    String brandname = testData.get("Brand Name");
    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
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
      // Select Payment
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

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
        custdashboardpage.performRenewals_LapseRenewal("Invite Renewal", extentedReport, true);
        newquotepage.clickNextOne(extentedReport);
        newquotepage.clickNextTwo(extentedReport);
        newquotepage.clickView(extentedReport);
        newquotepage.clickAgree(extentedReport);
        newquotepage.clickGetQuote(extentedReport);
        newquotepage.clickRenewalQuoteBuy(extentedReport);
        BuyQuotePage buyQuotePage = new BuyQuotePage(driver, extentedReport);
        buyQuotePage.clicksave_LapsebuttoninAcceptancepage("Review Required", "QuoteDescription",
            "Save", extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Navigated to custdashboardpage successfully after quote lapse",
            "Failed to Navigate to custdashboardpage after quote lapse", driver, extentedReport,
            true);

        HashMap<String, String> policyDetailsrnl = custdashboardpage.getPolicyURN(extentedReport);
        Log.softAssertThat(
            policyDetailsrnl.get("notification_plcy").toString().equalsIgnoreCase("R"),
            "Policy position displaying as Renewal", "Latest position is not displaying");

        Log.softAssertThat(
            custdashboardpage.verify_reviewStatus("Review Required", extentedReport, true),
            "Review Required status was displayed after saving a renewal quote",
            "Review Required status was not displayed after saving a renewal quote");
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
            driver, extentedReport, true);
      }

      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      // driver.quit();
    } // finally

  }

  @Test(
      description = "The Notification column should be displayed with 'R' flag (Green Color) for required selected policy. The position and Status of the policy which is reviewed for renewal the policy, should be displayed as 'Renewal' and status as 'Reviewed'",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_382_383_384(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_382_383_384";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    String brandname = testData.get("Brand Name");
    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
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
      // Select Payment
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

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
        custdashboardpage.performRenewals_LapseRenewal("Invite Renewal", extentedReport, true);
        newquotepage.clickNextOne(extentedReport);
        newquotepage.clickNextTwo(extentedReport);
        newquotepage.clickView(extentedReport);
        newquotepage.clickAgree(extentedReport);
        newquotepage.clickGetQuote(extentedReport);
        newquotepage.clickRenewalQuoteBuy(extentedReport);
        BuyQuotePage buyQuotePage = new BuyQuotePage(driver, extentedReport);
        buyQuotePage.clicksave_LapsebuttoninAcceptancepage("Reviewed", "QuoteDescription", "Save",
            extentedReport);
        // buyQuotePage.clicksave_ReviewedbuttoninAcceptancepage("Save",
        // extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Navigated to custdashboardpage successfully after quote lapse",
            "Failed to Navigate to custdashboardpage after quote lapse", driver, extentedReport,
            true);

        HashMap<String, String> policyDetailsrnl = custdashboardpage.getPolicyURN(extentedReport);
        Log.softAssertThat(
            policyDetailsrnl.get("notification_plcy").toString().equalsIgnoreCase("R"),
            "Policy position displaying as Renewal", "Latest position is not displaying");

        Log.softAssertThat(custdashboardpage.verify_reviewStatus("Reviewed", extentedReport, true),
            "Reviewed status was displayed after saving a renewal quote",
            "Reviewed status was not displayed after saving a renewal quote");
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
            driver, extentedReport, true);
      }

      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      // driver.quit();
    } // finally

  }

  @Test(groups = "Business_Scenario",
      description = "Invite a policy, save as renewal reviewed, green R, then do an MTA, complete the MTA and check that the renewal has gone back to a red R",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_653_692(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_653_692";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    String brandname = testData.get("Brand Name");
    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");
    String MTAcreationReason = testData.get("MTAdjReason");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
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
      // Select Payment
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

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
        custdashboardpage.performRenewals_LapseRenewal("Invite Renewal", extentedReport, true);
        newquotepage.clickNextOne(extentedReport);
        newquotepage.clickNextTwo(extentedReport);
        newquotepage.clickView(extentedReport);
        newquotepage.clickAgree(extentedReport);
        newquotepage.clickGetQuote(extentedReport);
        newquotepage.clickRenewalQuoteBuy(extentedReport);
        BuyQuotePage buyQuotePage = new BuyQuotePage(driver, extentedReport);
        // buyQuotePage.clicksave_ReviewedbuttoninAcceptancepage("Save",
        // extentedReport);
        buyQuotePage.clicksave_LapsebuttoninAcceptancepage("Reviewed", "QuoteDescription", "Save",
            extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Navigated to custdashboardpage successfully after quote Reviewed",
            "Failed to Navigate to custdashboardpage after quote Reviewed", driver, extentedReport,
            true);

        HashMap<String, String> policyDetailsrnl = custdashboardpage.getPolicyURN(extentedReport);
        Log.softAssertThat(
            policyDetailsrnl.get("notification_plcy").toString().equalsIgnoreCase("R"),
            "Policy position displaying as Renewal", "Latest position is not displaying");

        Log.softAssertThat(custdashboardpage.verify_reviewStatus("Reviewed", extentedReport, true),
            "Reviewed status was displayed after saving a renewal quote",
            "Reviewed status was not displayed after saving a renewal quote");

        // Perform MTA
        custdashboardpage.clickManagePolicy(extentedReport);
        custdashboardpage.clickMidTermAdjustment(extentedReport);
        custdashboardpage.enterDateForMTA(extentedReport);
        custdashboardpage.selectMidTermAdjReason(MTAcreationReason, extentedReport);
        custdashboardpage.clickMidTermContinue(extentedReport);
        GetQuotePage getquotepage = new GetQuotePage(driver, extentedReport);
        String[] coversToAdd = testData.get("coverToSelect").toString().split(",");

        for (int i = 0; i < coversToAdd.length; i++) {
          boolean BoolVal = false;
          String ins_RowtoInteract =
              getquotepage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
          BoolVal = getquotepage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract,
              true, extentedReport);
          String[] coverType = coversToAdd[i].split("_");
          if (BoolVal != false) {
            Log.pass(coverType[2] + " " + coverType[0] + " cover done successfully", driver,
                extentedReport, true);
          } else {
            Log.fail("Failed to " + coverType[2] + " " + coverType[0] + " cover", driver,
                extentedReport, true);
          }

        }
        // Click on recalculate
        getquotepage.clickReCalculate(extentedReport);

        // Click on buy button
        getquotepage.clickBuy(extentedReport);
        getquotepage.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
        getquotepage.clickAddMTA(extentedReport, false);
        getquotepage.clickAddAllMTANextButton(extentedReport, true);

        // Select card type
        getquotepage.takePayment(extentedReport);
        carddetailspage = getquotepage.selectVisacard(extentedReport);

        // Enter Card Details
        carddetailspage.enterCardNumber(testData, extentedReport, true);
        carddetailspage.selectExpiry(testData, extentedReport, true);
        carddetailspage.enterVerification(extentedReport, true);
        carddetailspage.enterName(testData, extentedReport, true);
        carddetailspage.clickbuy(extentedReport, true);
        Log.message("Entered Card Details and Navigated to Verificaion page", driver,
            extentedReport, true);

        // Click continue button
        getquotepage = carddetailspage.clickContinueButton_getQuote(extentedReport);
        Log.softAssertThat(getquotepage.verifyPaymentTrasaction(extentedReport),
            "Payment is successful", "Payment is not successful", driver, extentedReport, true);

        newquotepage = getquotepage.confirmPaymentOnMTA("MTA", testData, extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Navigated to Customer dashboard page", "Not navigated to Customer dashboard page",
            driver, extentedReport, true);

        custdashboardpage = newquotepage.clickbacktoDashboard(extentedReport);
        policyDetails = custdashboardpage.getPolicyURN(extentedReport);
        if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {

          Log.pass("MTA Policy Created Successfully in accepted status", driver, extentedReport,
              true);
        } else {
          Log.fail("Failed - MTA Policy Created in " + policyDetails.get("Status").toString()
              + " status", driver, extentedReport, true);
        }

      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
            driver, extentedReport, true);
      }

      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      // driver.quit();
    } // finally

  }

  @Test(groups = "Business_Scenario",
      description = "To check that the user is able to reverse the completed renewal policy",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_652_683(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_652_683";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");
    String brandname = testData.get("Brand Name");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.message("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard" + custDetails.get("First Name")
              + " " + custDetails.get("Last Name"),
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal

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

        /// Add search for the existing IP contact
        custdashboardpage.clickComplete(extentedReport);
        Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
            "Login failed", driver, extentedReport, true);

        // Click on Take Call link
        homePage.clickTakeCall(extentedReport);
        homePage.clickMyBrands(brandname, extentedReport);
        Log.softAssertThat(
            searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
            "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

        // Search for valid contact
        searchPage.enterLastName(custDetails.get("Last Name"), extentedReport);
        searchPage.clickSearch(extentedReport);
        custdashboardpage = searchPage.selectPolicy_from_SearchPage(true, extentedReport);

        // Verifying Customer Details
        custdashboardpage.clickPassVerification(extentedReport, true);
        custdashboardpage.verifyCustomerDashboardPage();
        Log.softAssertThat(
            custdashboardpage.verifyContactName(testData.get("Title") + " "
                + custDetails.get("First Name") + " " + custDetails.get("Last Name"),
                extentedReport, true),
            "Verified FirstName and LastName on Customer Dashboard",
            "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

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
        // Select Payment
        newquotepage.selectPayment(testData, true, extentedReport);

        // Select card type
        newquotepage.takePayment(extentedReport);
        newquotepage.selectVisacard(extentedReport);

        // Enter Card Details
        carddetailspage.enterCardNumber(testData, extentedReport, true);
        carddetailspage.selectExpiry(testData, extentedReport, true);
        carddetailspage.enterVerification(extentedReport, true);
        carddetailspage.enterName(testData, extentedReport, true);
        carddetailspage.clickbuy(extentedReport, true);
        Log.message("Entered Card Details and Navigated to Verificaion page", driver,
            extentedReport, true);

        // Click continue button
        newquotepage = carddetailspage.clickContinueButton(extentedReport);
        Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
            "Payment was successful", "Payment was not successful", driver, extentedReport, true);

        custdashboardpage = newquotepage.rnlconfirmPayment(testData, extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
            true);
        HashMap<String, String> policyDetailsrnl = custdashboardpage.getPolicyURN(extentedReport);
        Log.softAssertThat(policyDetailsrnl.get("Position").toString().equalsIgnoreCase("Renewal"),
            "Policy position displaying as Renewal", "Latest position is not displaying");

        Log.softAssertThat(policyDetails.get("Status").toString().equalsIgnoreCase("Accepted"),
            "Policy status displaying as accepted", "Policy status not displaying as accepted");
        custdashboardpage.selectTabDropdown("Manage Policy", extentedReport, false);
        custdashboardpage.clickReverseTransaction(extentedReport);
        custdashboardpage.selectReverseTransType("Client Request", extentedReport, true);
        custdashboardpage.clickConfirmReversalButton(extentedReport);
        custdashboardpage.getPolicyURN(extentedReport);
        Log.softAssertThat(
            policyDetailsrnl.get("Position").toString().equalsIgnoreCase("NewBusiness"),
            "Policy position displaying as NewBusiness", "Latest position is not displaying");

        // check for policy status

      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
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
  }

  @Test(groups = "Business_Scenario",
      description = " Reinstate a cancelled direct debit renewal policy, then attempt an MTA",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")

  public void BS_656_657(String browser) throws Exception {
    // Get the web driver instance
    String tcId = "BS_656_657";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");
    String brandname = testData.get("Brand Name");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    String MTAcreationReason = testData.get("MTAdjReason");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.message("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard" + custDetails.get("First Name")
              + " " + custDetails.get("Last Name"),
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal

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
        custdashboardpage.performRenewals(extentedReport, true);
        newquotepage.clickNextOne(extentedReport);
        newquotepage.clickNextTwo(extentedReport);
        newquotepage.clickView(extentedReport);
        newquotepage.clickAgree(extentedReport);
        newquotepage.clickGetQuote(extentedReport);
        newquotepage.clickRenewalQuoteBuy(extentedReport);

        // TC_366
        Log.softAssertThat(
            newquotepage.verifyPayorName(testData.get("Title") + " " + custDetails.get("First Name")
                + " " + custDetails.get("Last Name")),
            "Change payor and customer name dispaying as same",
            "Change payor and customer name not dispaying as same");
        newquotepage.selectPaymentPlan("Monthly", extentedReport);
        newquotepage.selectPaymentMethod("Direct Debit", extentedReport);
        Log.softAssertThat(newquotepage.selectPayment("Monthly", testData, true, extentedReport),
            "Customer able to modify the payment details and selected Monthly payment aggreement check box",
            "Customer not able to modify the payment details and Monthly payment aggreement check box is not dispaying");
        custdashboardpage = newquotepage.confirmPaymentRnl("Monthly", extentedReport);
        Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
            "Customer able to pay the amount as per payment plan",
            " Customer not able to pay as per the payment plan");

        // Cancel the policy
        custdashboardpage.clickCancelPolicy(extentedReport);
        custdashboardpage.clickStandardCancelletion(extentedReport);
        custdashboardpage.enterRequestCancellationDetails("No Refund", "Better Quote",
            extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElementsDoNotExist(
                Arrays.asList("drpManageRenewals"), custdashboardpage),
            "Manage renewal tab is not dispalyed on customer dashaboard page",
            "Manage renewal tab is dispalyed on customer dashboard page");
        // Reinstate the policy
        custdashboardpage.clickReinstatePolicy(true, extentedReport);
        custdashboardpage.selectReinstateReason(testData, true, extentedReport);
        custdashboardpage.clickCalculateForReinstate(false, extentedReport);
        custdashboardpage.clickConfirmForReinstate(true, extentedReport);
        custdashboardpage.clickManagePolicy(extentedReport);
        custdashboardpage.clickMidTermAdjustment(extentedReport);
        custdashboardpage.enterDateForMTA(extentedReport);
        custdashboardpage.selectMidTermAdjReason(MTAcreationReason, extentedReport);
        custdashboardpage.clickMidTermContinue(extentedReport);
        GetQuotePage getquotepage = new GetQuotePage(driver, extentedReport);
        String[] coversToAdd = testData.get("coverToSelect").toString().split(",");

        for (int i = 0; i < coversToAdd.length; i++) {
          boolean BoolVal = false;
          String ins_RowtoInteract =
              getquotepage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
          BoolVal = getquotepage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract,
              true, extentedReport);
          String[] coverType = coversToAdd[i].split("_");
          if (BoolVal != false) {
            Log.pass(coverType[2] + " " + coverType[0] + " cover done successfully", driver,
                extentedReport, true);
          } else {
            Log.fail("Failed to " + coverType[2] + " " + coverType[0] + " cover", driver,
                extentedReport, true);
          }

        }
        // Click on recalculate
        getquotepage.clickReCalculate(extentedReport);

        // Click on buy button
        getquotepage.clickBuy(extentedReport);
        getquotepage.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
        getquotepage.clickAddMTA(extentedReport, false);
        getquotepage.clickAddAllMTANextButton(extentedReport, true);

        // Select card type
        getquotepage.takePayment(extentedReport);
        carddetailspage = getquotepage.selectVisacard(extentedReport);

        // Enter Card Details
        carddetailspage.enterCardNumber(testData, extentedReport, true);
        carddetailspage.selectExpiry(testData, extentedReport, true);
        carddetailspage.enterVerification(extentedReport, true);
        carddetailspage.enterName(testData, extentedReport, true);
        carddetailspage.clickbuy(extentedReport, true);
        Log.message("Entered Card Details and Navigated to Verificaion page", driver,
            extentedReport, true);

        // Click continue button
        getquotepage = carddetailspage.clickContinueButton_getQuote(extentedReport);
        Log.softAssertThat(getquotepage.verifyPaymentTrasaction(extentedReport),
            "Payment is successful", "Payment is not successful", driver, extentedReport, true);

        newquotepage = getquotepage.confirmPaymentOnMTA("MTA", testData, extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Navigated to Customer dashboard page", "Not navigated to Customer dashboard page",
            driver, extentedReport, true);

        custdashboardpage = newquotepage.clickbacktoDashboard(extentedReport);
        policyDetails = custdashboardpage.getPolicyURN(extentedReport);
        if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {

          Log.pass("MTA Policy Created Successfully in accepted status", driver, extentedReport,
              true);
        } else {
          Log.fail("Failed - MTA Policy Created in " + policyDetails.get("Status").toString()
              + " status", driver, extentedReport, true);
        }

      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
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
  }

  @Test(groups = "Business_Scenario",
      description = "On a lapsed quote, can I get it back to reinvite",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_661_684(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_661_684";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    String brandname = testData.get("Brand Name");
    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
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
      // Select Payment
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

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
        custdashboardpage.performRenewals_LapseRenewal("Invite Renewal", extentedReport, true);
        newquotepage.clickNextOne(extentedReport);
        newquotepage.clickNextTwo(extentedReport);
        newquotepage.clickView(extentedReport);
        newquotepage.clickAgree(extentedReport);
        newquotepage.clickGetQuote(extentedReport);
        newquotepage.clickRenewalQuoteBuy(extentedReport);
        BuyQuotePage buyQuotePage = new BuyQuotePage(driver, extentedReport);
        buyQuotePage.clicksave_LapsebuttoninAcceptancepage("", "", "Lapse", extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Navigated to custdashboardpage successfully after quote lapse",
            "Failed to Navigate to custdashboardpage after quote lapse", driver, extentedReport,
            true);
        // Verify the lapsed status of all the quotes
        HashMap<String, String> policyDetailsrnl = custdashboardpage.getPolicyURN(extentedReport);
        Log.softAssertThat(
            policyDetailsrnl.get("notification_plcy").toString().equalsIgnoreCase("L"),
            "Policy position displaying as Lapsed", "Latest position is not displaying");

        buyQuotePage = custdashboardpage.performRenewals_QuoteVariation(extentedReport, true);

        buyQuotePage.selectPaymentPlan("Annual", extentedReport);
        buyQuotePage.selectPaymentMethod("Card", extentedReport, true);
        buyQuotePage.takePayment(extentedReport);

        carddetailspage = buyQuotePage.selectVisacard(extentedReport);
        Log.pass("Selected VISA Card", driver, extentedReport, true);

        // Enter Card Details
        carddetailspage.enterCardNumber(testData, extentedReport, true);
        carddetailspage.selectExpiry(testData, extentedReport, true);
        carddetailspage.enterVerification(extentedReport, true);
        carddetailspage.enterName(testData, extentedReport, true);
        carddetailspage.clickbuy(extentedReport, true);
        Log.message("Entered Card Details and Navigated to Verificaion page", driver,
            extentedReport, true);

        // Click continue button
        buyQuotePage = carddetailspage.clickContinueButton_buyQuote(extentedReport);
        Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
            "Payment was successful", "Payment was not successful", driver, extentedReport, true);

        custdashboardpage = newquotepage.rnlconfirmPayment(testData, extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
            true);
        HashMap<String, String> policyDetailsrnl2 = custdashboardpage.getPolicyURN(extentedReport);
        Log.softAssertThat(policyDetailsrnl2.get("Position").toString().equalsIgnoreCase("Renewal"),
            "Policy position displaying as Renewal", "Latest position is not displaying");

        Log.softAssertThat(policyDetailsrnl2.get("Status").toString().equalsIgnoreCase("Accepted"),
            "Policy status displaying as accepted", "Policy status not displaying as accepted");

        // check for policy status

      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
            driver, extentedReport, true);
      }

      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      // driver.quit();
    } // finally

  }

  @Test(groups = "Business_Scenario",
      description = "To check that the latest renewal position of the policy is only displayed, if renewal is not blocked for that policy. To check that the 'Manage Renewals' tab is displayed, if the User has the permission to perform 'invite renewal'. To check that the 'Manage Renewals' tab is displayed, if the policy is within the date tolerances for renewal as defined on the product / scheme, Check that it is clear during renewal cycle that a policy is in the renewal cycle",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_660_659(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_660_659";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");
    String brandname = testData.get("Brand Name");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.message("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard" + custDetails.get("First Name")
              + " " + custDetails.get("Last Name"),
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal

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

        /// Add search for the existing IP contact
        custdashboardpage.clickComplete(extentedReport);
        Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
            "Login failed", driver, extentedReport, true);

        // Click on Take Call link
        homePage.clickTakeCall(extentedReport);
        homePage.clickMyBrands(brandname, extentedReport);
        Log.softAssertThat(
            searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
            "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

        // Search for valid contact
        searchPage.enterLastName(custDetails.get("Last Name"), extentedReport);
        searchPage.clickSearch(extentedReport);
        custdashboardpage = searchPage.selectPolicy_from_SearchPage(true, extentedReport);

        // Verifying Customer Details
        custdashboardpage.clickPassVerification(extentedReport, true);
        custdashboardpage.verifyCustomerDashboardPage();
        Log.softAssertThat(
            custdashboardpage.verifyContactName(testData.get("Title") + " "
                + custDetails.get("First Name") + " " + custDetails.get("Last Name"),
                extentedReport, true),
            "Verified FirstName and LastName on Customer Dashboard",
            "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

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
        // Select Payment
        newquotepage.selectPayment(testData, true, extentedReport);

        // Select card type
        newquotepage.takePayment(extentedReport);
        newquotepage.selectVisacard(extentedReport);

        // Enter Card Details
        carddetailspage.enterCardNumber(testData, extentedReport, true);
        carddetailspage.selectExpiry(testData, extentedReport, true);
        carddetailspage.enterVerification(extentedReport, true);
        carddetailspage.enterName(testData, extentedReport, true);
        carddetailspage.clickbuy(extentedReport, true);
        Log.message("Entered Card Details and Navigated to Verificaion page", driver,
            extentedReport, true);

        // Click continue button
        newquotepage = carddetailspage.clickContinueButton(extentedReport);
        Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
            "Payment was successful", "Payment was not successful", driver, extentedReport, true);

        custdashboardpage = newquotepage.rnlconfirmPayment(testData, extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
            true);
        HashMap<String, String> policyDetailsrnl = custdashboardpage.getPolicyURN(extentedReport);
        Log.softAssertThat(policyDetailsrnl.get("Position").toString().equalsIgnoreCase("Renewal"),
            "Policy position displaying as Renewal", "Latest position is not displaying");

        Log.softAssertThat(policyDetails.get("Status").toString().equalsIgnoreCase("Accepted"),
            "Policy status displaying as accepted", "Policy status not displaying as accepted");

        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElementsDoNotExist(
                Arrays.asList("drpManageRenewals"), custdashboardpage),
            "Manage renewal tab is not dispalyed on renewd policy on customer dashaboard page",
            "Manage renewal tab is dispalying on renewd policy on customer dashboard page");

        // check for policy status

      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
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
  }

  @Test(groups = "Business_Scenario",
      description = "To Check that a reinstated policy is accessible if dates are relevant",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_716(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_716";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");
    String brandname = testData.get("Brand Name");

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
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      Log.message("<b>Setup for TC_716</b>", extentedReport);
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
      custdashboardpage.enterNewQuotePastDate(+5, 1, extentedReport, false); // Past
                                                                             // date
                                                                             // for
                                                                             // renewal
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
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
      // Select Payment
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

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Verification for TC24
      Log.message("<b> Verification for TC_716</b>", extentedReport);
      custdashboardpage.clickCancelPolicy(testData, true, extentedReport);
      custdashboardpage.enterEffectiveDate();
      custdashboardpage.enterReason("Better Quote", extentedReport);
      custdashboardpage.enterPremiumType(testData, extentedReport);
      custdashboardpage.clickCalculate(true, extentedReport);
      custdashboardpage.clickConfirm(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.verifyTabUnderPolicyHolder("Reinstate Policy", extentedReport, true),
          "Reinstate policy tab is displayed", "Reinstate policy tab is not displayed");
      custdashboardpage.clickReinstatePolicy(true, extentedReport);
      custdashboardpage.selectReinstateReason(testData, true, extentedReport);
      custdashboardpage.clickCalculateForReinstate(true, extentedReport);
      custdashboardpage.clickConfirmForReinstate(true, extentedReport);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }

  }

  @Test(groups = "Business_Scenario",
      description = "In Renewals variations, check that you can do multiple variations and that each previous one is set to lapsed",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_714_715_719_718(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_714_715_719_718";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");

    String brandname = testData.get("Brand Name");
    String quoteDescription = testData.get("Quote_Description");
    String[] quoteDescriptionList = quoteDescription.split("\\|");
    quoteDescription1 = quoteDescriptionList[0];
    quoteDescription2 = quoteDescriptionList[1];
    quoteDescription3 = quoteDescriptionList[2];

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
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      Log.message("<b>Setup for BS_714_715</b>", extentedReport);
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      firstName = custDetails.get("First Name");
      lastName = custDetails.get("Last Name");

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);
      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(+5, 1, extentedReport, false); // Past
                                                                             // date
                                                                             // for
                                                                             // renewal
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
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
      // Select Payment
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

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);

        custdashboardpage.performRenewals_LapseRenewal("Invite Renewal", extentedReport, true);
        newquotepage.clickNextOne(extentedReport);
        newquotepage.clickNextTwo(extentedReport);
        newquotepage.clickView(extentedReport);
        newquotepage.clickAgree(extentedReport);
        newquotepage.clickGetQuote(extentedReport);
        newquotepage.clickQuoteSave("Renew", quoteDescription1, extentedReport);

        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
            true);

        // Editing first quote variation
        custdashboardpage.clickRenewalQuoteVariations(extentedReport, true);
        custdashboardpage.ClickOptionInRenewalQuoteVariations(quoteDescription1, extentedReport,
            true);
        BuyQuotePage buyQuotePage = new BuyQuotePage(driver, extentedReport).get();
        buyQuotePage.clicksavebuttoninAcceptancepage(quoteDescription2, extentedReport);
        custdashboardpage.clickRenewalQuoteVariations(extentedReport, false);
        Log.softAssertThat(
            custdashboardpage.verifyOptionInRenewalQuoteVariations(quoteDescription2,
                extentedReport, true),
            "Quote variation " + quoteDescription2 + " is created",
            "Quote variation " + quoteDescription2 + "is not created", driver, extentedReport);
        // Editing second quote variation
        custdashboardpage.clickRenewalQuoteVariations(extentedReport, true);
        custdashboardpage.ClickOptionInRenewalQuoteVariations(quoteDescription2, extentedReport,
            true);
        buyQuotePage.clicksavebuttoninAcceptancepage(quoteDescription3, extentedReport);
        custdashboardpage.clickRenewalQuoteVariations(extentedReport, true);
        Log.softAssertThat(
            custdashboardpage.verifyOptionInRenewalQuoteVariations(quoteDescription3,
                extentedReport, true),
            "Quote variation " + quoteDescription3 + " is created",
            "Quote variation " + quoteDescription3 + " is not created", driver, extentedReport);
        Log.message("<b>Verification for BS_714_715<b>", extentedReport);
        Log.softAssertThat(
            (custdashboardpage.verifyStatusForQuoteVariations(quoteDescription3, "R",
                extentedReport, true)
                && custdashboardpage.verifyStatusForQuoteVariations(quoteDescription2, "L",
                    extentedReport, true)
                && custdashboardpage.verifyStatusForQuoteVariations(quoteDescription1, "L",
                    extentedReport, true)),
            "Recent quote " + quoteDescription3 + " is active and remaining Quotes "
                + quoteDescription1 + ", " + quoteDescription2 + " are lapsed",
            "Recent quote " + quoteDescription3 + " is not active and remaining Quotes "
                + quoteDescription1 + ", " + quoteDescription2 + " are not lapsed",
            driver, extentedReport);
      }

      Log.message("<b>Setup for BS_719_718<b>", extentedReport);
      custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport);
      // Fill up MTa data
      custdashboardpage.enterMTADetails(testData, extentedReport, true);

      Log.pass("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
      GetQuotePage getquotepage = new GetQuotePage(driver, extentedReport);

      // Loop to add items to the existing ins cover
      String[] coversToAdd = testData.get("coverToSelect").toString().split(",");

      for (int i = 0; i < coversToAdd.length; i++) {
        boolean BoolVal = false;
        String ins_RowtoInteract =
            getquotepage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
        BoolVal = getquotepage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract,
            true, extentedReport);
        String[] coverType = coversToAdd[i].split("_");
        if (BoolVal != false) {
          Log.pass(coverType[2] + " " + coverType[0] + "_" + coverType[1]
              + " cover type done successfully", driver, extentedReport, true);
        } else {
          Log.fail("Failed to " + coverType[2] + " " + coverType[0] + "_" + coverType[1] + " cover",
              driver, extentedReport, true);
        }

      }
      // Click on recalculate
      getquotepage.clickReCalculate(extentedReport);

      // Click on buy button
      getquotepage.clickBuy(extentedReport);
      getquotepage.selectMTAReasonPayment(extentedReport);

      // Select card type
      getquotepage.takePayment(extentedReport);
      carddetailspage = getquotepage.selectVisacard(extentedReport);
      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.pass("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);

      // Click continue button
      getquotepage = carddetailspage.clickContinueButton_getQuote(extentedReport);
      Log.softAssertThat(getquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment was successful", "Payment was not successful", driver, extentedReport, true);

      Log.softAssertThat(getquotepage.ECbannertitleCheck(extentedReport),
          "Payment Page has the banner title as Engagement Centre",
          "Payment page failed to have banner title as Engagement Centre", driver, extentedReport,
          true);

      getquotepage.clickConfirmMTA(extentedReport);

      //
      newquotepage.clickNextOne(extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickQuoteSave("Renew", quoteDescription2, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // Reverse Transaction
      custdashboardpage.clickManagePolicy(extentedReport);
      custdashboardpage.clickReverseTransaction(extentedReport);
      custdashboardpage.selectReverseTransType("Client Request", extentedReport, true);
      custdashboardpage.clickConfirmReversalButton(extentedReport);
      Log.softAssertThat(custdashboardpage.verifyMTATStatusAndDate("Inactive: Reversed"),
          "'Reverse Transaction' is made for the latest date when multiple MTAs are performed in EC application",
          "'Reverse Transaction' is not made for the latest date when multiple MTAs are performed in EC application",
          driver, extentedReport, true);
      //
      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }

  }

  @Test(groups = "Business_Scenario",
      description = "To Check that you cannot change inception dates on renewal invitation and Check that on renewal invite in EC I am forced to view the terms and conditions",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_710_711_712_717(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_710_711_712_717";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");
    String address = testData.get("ContactAddress");
    String brandname = testData.get("Brand Name");

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
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      Log.message("<b>Setup for BS_710</b>", extentedReport);
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      firstName = custDetails.get("First Name");
      lastName = custDetails.get("Last Name");

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);
      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(+5, 1, extentedReport, false); // Past
                                                                             // date
                                                                             // for
                                                                             // renewal
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
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
      // Select Payment
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

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
        custdashboardpage.performRenewals_LapseRenewal("Invite Renewal", extentedReport, true);

        Log.message("<b>Verification for BS_710</b>", extentedReport);
        Log.softAssertThat(
            !newquotepage.uielement.verifyPageElements(Arrays.asList("mdlNewQuote", "datePicker"),
                newquotepage),
            "Popup and inception date are not displayed to change the date.",
            "Popup and inception date are not displayed to change the date.", driver,
            extentedReport, true);

        newquotepage.clickNextOne(extentedReport);
        newquotepage.clickNextTwo(extentedReport);
        newquotepage.clickView(extentedReport);
        newquotepage.clickAgree(extentedReport);
        newquotepage.clickGetQuote(extentedReport);
        newquotepage.clickRenewalQuoteBuy(extentedReport);
        Log.message("<b>Verification for BS_711</b>", extentedReport);
        newquotepage.clickTermsAndConditionSection(extentedReport);
        Log.softAssertThat(
            newquotepage.uielement.verifyPageElements(Arrays.asList("btnAttachTermAndCondition"),
                newquotepage),
            "Terms & Conditions is verified.", "Terms & Conditions is not verified.", driver,
            extentedReport, true);

        Log.message("<b>Verification for BS_717</b>", extentedReport);
        // Select Payment
        newquotepage.selectPayment(testData, true, extentedReport);
        // Select card type
        newquotepage.takePayment(extentedReport);
        carddetailspage = newquotepage.selectVisacard(extentedReport);
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
        newquotepage.clickAcceptbtn(extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
            true);
        custdashboardpage.clickCancelPolicy(testData, true, extentedReport);
        custdashboardpage.clickCalculate(true, extentedReport);
        custdashboardpage.clickConfirm(true, extentedReport);
        // Verifying that policy is cancelled
        Log.softAssertThat(custdashboardpage.verifyCancellation(true, extentedReport),
            "Policy cancelled successfully", "Policy is not cancelled", driver, extentedReport,
            true);

        Log.message("<b>Verification for BS_712</b>", extentedReport);
        ManageContactDetailsPage manageContactDetailsPg =
            custdashboardpage.verifyndClickManageCntDetails(extentedReport, false);
        Log.softAssertThat(manageContactDetailsPg.verifyManageContactDetailsFields(),
            "Manage Contact Details screen is displayed",
            "Manage Contact Details screen is not displayed", driver, extentedReport, true);
        manageContactDetailsPg.clickAddNewAddressButton(extentedReport, true);
        manageContactDetailsPg.enterNewAddressDetails("AB10 1AH", "Home", extentedReport, true);
        Log.softAssertThat(custdashboardpage.verifyContactAddressDetails(address),
            "System displayed newly mailing address under Contact Detail section : " + address,
            "System not displayed newly mailing address under Contact Detail section : " + address,
            driver, extentedReport, true);
      }
      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }

  }

  @Test(groups = "Business_Scenario",
      description = "Check that during manual invite on EC, it is possible to change payor to search for an existing payor and select",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_704(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_704";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);
    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");

    String ChangePayorfirstName = GenericUtils.getRandomCharacters("alpha", 4) + "test";
    String ChangePayorlastName = GenericUtils.getRandomCharacters("alpha", 4) + "test";

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
      custdashboardpage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      // custdashboardpage.clickComplete(extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      Log.softAssertThat(newquotepage.verifyNewQuotePage(true, extentedReport),
          "Verified NewQuotePage ", "Not verified NewQuotePage", driver, extentedReport, true);
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, testData.get("Cover"), extentedReport);
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

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
        custdashboardpage.performRenewals_LapseRenewal("Invite Renewal", extentedReport, true);
        newquotepage.clickNextOne(extentedReport);
        newquotepage.clickNextTwo(extentedReport);
        newquotepage.clickView(extentedReport);
        newquotepage.clickAgree(extentedReport);
        newquotepage.clickGetQuote(extentedReport);
        newquotepage.clickQuoteSave("Renew", "Renewal Quote", extentedReport);

        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
            true);

        Log.softAssertThat(
            custdashboardpage.verify_reviewStatus("Review Required", extentedReport, true),
            "Review Required status was displayed after saving a renewal quote",
            "Review Required status was not displayed after saving a renewal quote");

        BuyQuotePage buyQuotePage =
            custdashboardpage.performRenewals_QuoteVariation(extentedReport, true);

        // Verify Acceptance page is landed
        Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
            "Acceptance tab was selected by default when clicked on quote variations",
            "Acceptance tab was not selected as default tab when clicked on quote variations",
            driver, extentedReport, true);
        // update payment method
        buyQuotePage.selectPaymentPlan("Annual", extentedReport);
        buyQuotePage.selectPaymentMethod("Card", extentedReport, true);
        Log.softAssertThat(
            !(buyQuotePage.uielement.verifyPageElements(Arrays.asList("chkBx_toMakePayment"),
                buyQuotePage)),
            "Confirm checkbox to take payment Button is not present before change new payer ",
            "Confirm checkbox to take payment Button is present before change new payer ", driver,
            extentedReport, true);

        buyQuotePage.clickChangePayorButton(extentedReport, true);
        // buyQuotePage.clickCheckboxinPayment(extentedReport, true);

        String payorname = newquotepage.enterChangePayorDetails(ChangePayorlastName,
            ChangePayorfirstName, testData.get("Post Code"), extentedReport, true);
        Log.softAssertThat(newquotepage.verifyPayorName(payorname),
            "The user is able to change the payor of the policy, once the quote reached Acceptance page : "
                + payorname,
            "The user is not able to change the payor of the policy, once the quote reached Acceptance page : "
                + payorname,
            driver, extentedReport, true);
        Log.softAssertThat(
            buyQuotePage.uielement.verifyPageElements(Arrays.asList("chkBx_toMakePayment"),
                buyQuotePage),
            "Confirm checkbox to take payment Button is present before change new payer ",
            "Confirm checkbox to take payment Button is not present before change new payer ",
            driver, extentedReport, true);

      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
            driver, extentedReport, true);
      }

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(groups = "Business_Scenario",
      description = "Check that able Renew the policy and able to pay in the original payment method",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_690(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_690";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");
    String brandname = testData.get("Brand Name");

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
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      Log.message("<b>Setup for TC_024</b>", extentedReport);
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      firstName = custDetails.get("First Name");
      lastName = custDetails.get("Last Name");
      // String contactName = firstName + " " + lastName;

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);
      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(+5, 1, extentedReport, false); // Past
                                                                             // date
                                                                             // for
                                                                             // renewal
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
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
      // Select Payment
      newquotepage.selectPayment(testData, true, extentedReport);
      Thread.sleep(4000);

      // Select card type
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
      Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment was successful", "Payment was not successful", driver, extentedReport, true);

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);
      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);

        custdashboardpage.performRenewals_LapseRenewal("Invite Renewal", extentedReport, true);
        newquotepage.clickNextOne(extentedReport);
        newquotepage.clickNextTwo(extentedReport);
        newquotepage.clickView(extentedReport);
        newquotepage.clickAgree(extentedReport);
        newquotepage.clickGetQuote(extentedReport);
        newquotepage.clickQuoteSave("Renew", "Renewal Quote", extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
            true);

        BuyQuotePage buyQuotePage =
            custdashboardpage.performRenewals_QuoteVariation(extentedReport, true);

        // Select card type
        buyQuotePage.takePayment(extentedReport);
        carddetailspage = buyQuotePage.selectVisacard(extentedReport);
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
        buyQuotePage = carddetailspage.clickContinueButton_buyQuote(extentedReport);
        Log.softAssertThat(buyQuotePage.verifyPaymentTrasaction(extentedReport),
            "Payment was successful", "Payment was not successful", driver, extentedReport, true);

        buyQuotePage.clickAcceptbtn(extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
            true);
        Log.softAssertThat(
            !(custdashboardpage.uielement.verifyPageElements(Arrays.asList("drpManageRenewals"),
                custdashboardpage)),
            "Manage renewal tab is not present so cannot be invited again",
            "Manage renewal tab is present", driver, extentedReport, true);

      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
            driver, extentedReport, true);
      }

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(groups = "Business_Scenario",
      description = "Verify that after renewal do MTA and pay in alternative payment method",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_693(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_693";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    String brandname = testData.get("Brand Name");
    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");
    String MTAcreationReason = testData.get("MTAdjReason");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
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
      // Select Payment
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

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
        custdashboardpage.performRenewals_LapseRenewal("Invite Renewal", extentedReport, true);
        newquotepage.clickNextOne(extentedReport);
        newquotepage.clickNextTwo(extentedReport);
        newquotepage.clickView(extentedReport);
        newquotepage.clickAgree(extentedReport);
        newquotepage.clickGetQuote(extentedReport);
        newquotepage.clickRenewalQuoteBuy(extentedReport);
        BuyQuotePage buyQuotePage = new BuyQuotePage(driver, extentedReport);
        // buyQuotePage.clicksave_ReviewedbuttoninAcceptancepage("Save",
        // extentedReport);
        buyQuotePage.clicksave_LapsebuttoninAcceptancepage("Reviewed", "QuoteDescription", "Save",
            extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Navigated to custdashboardpage successfully after quote Reviewed",
            "Failed to Navigate to custdashboardpage after quote Reviewed", driver, extentedReport,
            true);

        HashMap<String, String> policyDetailsrnl = custdashboardpage.getPolicyURN(extentedReport);
        Log.softAssertThat(
            policyDetailsrnl.get("notification_plcy").toString().equalsIgnoreCase("R"),
            "Policy position displaying as Renewal", "Latest position is not displaying");

        Log.softAssertThat(custdashboardpage.verify_reviewStatus("Reviewed", extentedReport, true),
            "Reviewed status was displayed after saving a renewal quote",
            "Reviewed status was not displayed after saving a renewal quote");

        // Perform MTA
        custdashboardpage.clickManagePolicy(extentedReport);
        custdashboardpage.clickMidTermAdjustment(extentedReport);
        custdashboardpage.enterDateForMTA(extentedReport);
        custdashboardpage.selectMidTermAdjReason(MTAcreationReason, extentedReport);
        custdashboardpage.clickMidTermContinue(extentedReport);
        GetQuotePage getquotepage = new GetQuotePage(driver, extentedReport);
        String[] coversToAdd = testData.get("coverToSelect").toString().split(",");

        for (int i = 0; i < coversToAdd.length; i++) {
          boolean BoolVal = false;
          String ins_RowtoInteract =
              getquotepage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
          BoolVal = getquotepage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract,
              true, extentedReport);
          String[] coverType = coversToAdd[i].split("_");
          if (BoolVal != false) {
            Log.pass(coverType[2] + " " + coverType[0] + " cover done successfully", driver,
                extentedReport, true);
          } else {
            Log.fail("Failed to " + coverType[2] + " " + coverType[0] + " cover", driver,
                extentedReport, true);
          }

        }
        // Click on recalculate
        getquotepage.clickReCalculate(extentedReport);

        // Click on buy button
        getquotepage.clickBuy(extentedReport);
        getquotepage.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
        getquotepage.clickAddMTA(extentedReport, false);
        getquotepage.clickAddAllMTANextButton(extentedReport, true);

        getquotepage.clickSaveQuoteMta(extentedReport);
        buyQuotePage = custdashboardpage.performRenewals_QuoteVariation(extentedReport, true);
        // Verify Acceptance page is landed
        Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
            "Acceptance tab was selected by default when clicked on quote variations",
            "Acceptance tab was not selected as default tab when clicked on quote variations",
            driver, extentedReport, true);
        // update payment method
        buyQuotePage.selectPaymentPlan("Monthly", extentedReport);
        buyQuotePage.selectPaymentMethod("Direct Debit", extentedReport, true);
        buyQuotePage.addAccountDetail(testData, extentedReport, true);
        buyQuotePage.clickCutomerAgreeButton(extentedReport);
        buyQuotePage.clickAcceptBtn(extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
            true);
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
            driver, extentedReport, true);
      }

      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      // driver.quit();
    } // finally

  }

  @Test(groups = "Business_Scenario",
      description = "Check that it is possible during renewal to create a brand new payor, check that button indicating they were spoken to is enabled, Check that where payor is not changed on a renewal the button indicating they are spoken to is not there",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_665_666(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_665_666";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);
    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");

    String ChangePayorfirstName = GenericUtils.getRandomCharacters("alpha", 4) + "test";
    String ChangePayorlastName = GenericUtils.getRandomCharacters("alpha", 4) + "test";

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
      custdashboardpage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      // custdashboardpage.clickComplete(extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      Log.softAssertThat(newquotepage.verifyNewQuotePage(true, extentedReport),
          "Verified NewQuotePage ", "Not verified NewQuotePage", driver, extentedReport, true);
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, testData.get("Cover"), extentedReport);
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

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
        custdashboardpage.performRenewals_LapseRenewal("Invite Renewal", extentedReport, true);
        newquotepage.clickNextOne(extentedReport);
        newquotepage.clickNextTwo(extentedReport);
        newquotepage.clickView(extentedReport);
        newquotepage.clickAgree(extentedReport);
        newquotepage.clickGetQuote(extentedReport);
        newquotepage.clickQuoteSave("Renew", "Renewal Quote", extentedReport);

        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
            true);

        Log.softAssertThat(
            custdashboardpage.verify_reviewStatus("Review Required", extentedReport, true),
            "Review Required status was displayed after saving a renewal quote",
            "Review Required status was not displayed after saving a renewal quote");

        BuyQuotePage buyQuotePage =
            custdashboardpage.performRenewals_QuoteVariation(extentedReport, true);

        // Verify Acceptance page is landed
        Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
            "Acceptance tab was selected by default when clicked on quote variations",
            "Acceptance tab was not selected as default tab when clicked on quote variations",
            driver, extentedReport, true);
        // update payment method
        buyQuotePage.selectPaymentPlan("Annual", extentedReport);
        buyQuotePage.selectPaymentMethod("Card", extentedReport, true);
        Log.softAssertThat(
            !(buyQuotePage.uielement.verifyPageElements(Arrays.asList("chkBx_toMakePayment"),
                buyQuotePage)),
            "Confirm checkbox to take payment Button is not present before change new payer ",
            "Confirm checkbox to take payment Button is present before change new payer ", driver,
            extentedReport, true);

        buyQuotePage.clickChangePayorButton(extentedReport, true);
        // buyQuotePage.clickCheckboxinPayment(extentedReport, true);

        newquotepage.enterChangePayorDetail(ChangePayorfirstName, ChangePayorlastName,
            testData.get("Post Code"), extentedReport, true);
        newquotepage.clickSearchBtn_ChangePayor(extentedReport);

        // Create New change payor
        newquotepage.clickCreateNewPayor(extentedReport);
        testData.put("Email", "");
        HashMap<String, String> payorDetails =
            newquotepage.enterCustomerDetails(testData, true, extentedReport, true);
        String payorFirstName = payorDetails.get("First Name");
        String payorLastName = payorDetails.get("Last Name");
        String payorname = payorFirstName + " " + payorLastName;
        newquotepage.clickSaveBtn_CreateNewPayor(extentedReport);

        Log.softAssertThat(newquotepage.verifyPayorName(payorname),
            payorname + " - Payor name is updated in change payor",
            payorname + " - Payor name is not updated in change payor", driver, extentedReport,
            true);
        Log.softAssertThat(
            buyQuotePage.uielement.verifyPageElements(Arrays.asList("chkBx_toMakePayment"),
                buyQuotePage),
            "Confirm checkbox to take payment Button is present before change new payer ",
            "Confirm checkbox to take payment Button is not present before change new payer ",
            driver, extentedReport, true);

      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
            driver, extentedReport, true);
      }

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(groups = "Business_Scenario",
      description = "On renewal invite check that direct debit agreement is enabled and can be ticked ",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_707(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_707";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    String brandname = testData.get("Brand Name");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(
          homePage.verifyHomePage(), "Successfully logged into UXP Home Page with User id:"
              + userName + "& Password:" + password + "",
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
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
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
      Log.pass("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment was successful", "Payment was not successful", driver, extentedReport, true);

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
        custdashboardpage.performRenewals_LapseRenewal("Invite Renewal", extentedReport, true);
        newquotepage.clickNextOne(extentedReport);
        newquotepage.clickNextTwo(extentedReport);
        newquotepage.clickView(extentedReport);
        newquotepage.clickAgree(extentedReport);
        newquotepage.clickGetQuote(extentedReport);
        newquotepage.clickQuoteSave("Renew", "Renewal Quote", extentedReport);

        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
            true);

        BuyQuotePage buyQuotePage =
            custdashboardpage.performRenewals_QuoteVariation(extentedReport, true);
        // Verify Acceptance page is landed
        Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
            "Acceptance tab was selected by default when clicked on quote variations",
            "Acceptance tab was not selected as default tab when clicked on quote variations",
            driver, extentedReport, true);
        // update payment method
        buyQuotePage.selectPaymentPlan("Monthly", extentedReport);
        buyQuotePage.selectPaymentMethod("Direct Debit", extentedReport, true);
        buyQuotePage.addAccountDetail(testData, extentedReport, true);
        Log.softAssertThat(
            !(buyQuotePage.uielement.verifyPageElementsDisabled(Arrays.asList("checkbox_CustAgree"),
                buyQuotePage)),
            "Customer agreement is enabled", "Customer agreement is not enabled", driver,
            extentedReport, true);
        buyQuotePage.clickCutomerAgreeButton(extentedReport);
        Log.softAssertThat(buyQuotePage.verifyCustomerAgreementChecked(extentedReport, true),
            "Able to tick the cuctomer agreement check box",
            "Not ble to tick the cuctomer agreement check box", driver, extentedReport, true);
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
            driver, extentedReport, true);
      }

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(groups = "Business_Scenario",
      description = " Verify that able carry out an MTA, accept and pay for it whilst the policy is in the renewal cycle and automatically have the renewal re-invited based on the amended policy details",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_687(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_687";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");
    String brandname = testData.get("Brand Name");
    String MTAcreationReason = testData.get("MTAdjReason");

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
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      Log.message("<b>Setup for TC_024</b>", extentedReport);
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      firstName = custDetails.get("First Name");
      lastName = custDetails.get("Last Name");
      // String contactName = firstName + " " + lastName;

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);
      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(+5, 1, extentedReport, false); // Past
                                                                             // date
                                                                             // for
                                                                             // renewal
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
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
      Log.pass("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment was successful", "Payment was not successful", driver, extentedReport, true);

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);
      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);

        custdashboardpage.performRenewals_LapseRenewal("Invite Renewal", extentedReport, true);
        newquotepage.clickNextOne(extentedReport);
        newquotepage.clickNextTwo(extentedReport);
        newquotepage.clickView(extentedReport);
        newquotepage.clickAgree(extentedReport);
        newquotepage.clickGetQuote(extentedReport);
        newquotepage.clickQuoteSave("Renew", "Renewal Quote", extentedReport);

        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
            true);
        // Perform MTA
        custdashboardpage.clickManagePolicy(extentedReport);
        custdashboardpage.clickMidTermAdjustment(extentedReport);
        custdashboardpage.enterDateForMTA(extentedReport);
        custdashboardpage.selectMidTermAdjReason(MTAcreationReason, extentedReport);
        custdashboardpage.clickMidTermContinue(extentedReport);
        GetQuotePage getquotepage = new GetQuotePage(driver, extentedReport);
        String[] coversToAdd = testData.get("coverToSelect").toString().split(",");

        for (int i = 0; i < coversToAdd.length; i++) {
          boolean BoolVal = false;
          String ins_RowtoInteract =
              getquotepage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
          BoolVal = getquotepage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract,
              true, extentedReport);
          String[] coverType = coversToAdd[i].split("_");
          if (BoolVal != false) {
            Log.pass(coverType[2] + " " + coverType[0] + " cover done successfully", driver,
                extentedReport, true);
          } else {
            Log.fail("Failed to " + coverType[2] + " " + coverType[0] + " cover", driver,
                extentedReport, true);
          }

        }
        // Click on recalculate
        getquotepage.clickReCalculate(extentedReport);

        // Click on buy button
        getquotepage.clickBuy(extentedReport);
        getquotepage.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
        getquotepage.clickAddMTA(extentedReport, false);
        getquotepage.clickAddAllMTANextButton(extentedReport, true);
        getquotepage.clickConfirmMTA(extentedReport);
        Log.softAssertThat(getquotepage.verifyTabTitles("datacapture", true, extentedReport),
            "Naviagted to Acceptance page ", "Not navigated to Acceptance page");

      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
            driver, extentedReport, true);
      }

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      // driver.quit();
    }
  }

  @Test(groups = "Business_Scenario",
      description = "Check that we can add or remove cover during renewal invite, including add-ons",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_713(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_713";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");
    String brandname = testData.get("Brand Name");
    String MTAcreationReason = testData.get("MTAdjReason");

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
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      Log.message("<b>Setup for TC_024</b>", extentedReport);
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      firstName = custDetails.get("First Name");
      lastName = custDetails.get("Last Name");
      // String contactName = firstName + " " + lastName;

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);
      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(+5, 1, extentedReport, false); // Past
                                                                             // date
                                                                             // for
                                                                             // renewal
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
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
      String[] coversToAdd = testData.get("coverToSelect").toString().split(",");

      boolean BoolVal = false;
      String ins_RowtoInteract =
          newquotepage.SelectInsuranceItem(coversToAdd[0], true, extentedReport);
      BoolVal = newquotepage.enterInsCoverDetails(testData, coversToAdd[0], ins_RowtoInteract, true,
          extentedReport);
      String[] coverType = coversToAdd[0].split("_");
      if (BoolVal != false) {
        Log.pass(coverType[2] + " " + coverType[0] + " cover done successfully", driver,
            extentedReport, true);
      } else {
        Log.fail("Failed to " + coverType[2] + " " + coverType[0] + " cover", driver,
            extentedReport, true);
      }

      // Click on recalculate
      newquotepage.clickReCalculate(extentedReport);
      newquotepage.clickBuy(extentedReport);
      newquotepage.clickPaymentNext(extentedReport);

      // Select Payment Method and Confirm Payment
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
          "Payment is successful", "Payment is not successful", driver, extentedReport, true);

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer dashboard page", "Not navigated to Customer dashboard page",
          driver, extentedReport, true);

      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);

        custdashboardpage.performRenewals_LapseRenewal("Invite Renewal", extentedReport, true);
        newquotepage.clickNextOne(extentedReport);
        newquotepage.clickNextTwo(extentedReport);
        newquotepage.clickView(extentedReport);
        newquotepage.clickAgree(extentedReport);
        newquotepage.clickGetQuote(extentedReport);
        newquotepage.clickRenewalQuoteBuy(extentedReport);
        BuyQuotePage buyQuotePage = new BuyQuotePage(driver, extentedReport);
        // buyQuotePage.clicksave_ReviewedbuttoninAcceptancepage("Save",
        // extentedReport);
        buyQuotePage.clicksave_LapsebuttoninAcceptancepage("Reviewed", "QuoteDescription", "Save",
            extentedReport);

        HashMap<String, String> policyDetailsrnl = custdashboardpage.getPolicyURN(extentedReport);
        Log.softAssertThat(
            policyDetailsrnl.get("notification_plcy").toString().equalsIgnoreCase("R"),
            "Policy position displaying as Renewal", "Latest position is not displaying");

        Log.softAssertThat(custdashboardpage.verify_reviewStatus("Reviewed", extentedReport, true),
            "Reviewed status was displayed after saving a renewal quote",
            "Reviewed status was not displayed after saving a renewal quote");

        // Perform MTA
        custdashboardpage.clickManagePolicy(extentedReport);
        custdashboardpage.clickMidTermAdjustment(extentedReport);
        custdashboardpage.enterDateForMTA(extentedReport);
        custdashboardpage.selectMidTermAdjReason(MTAcreationReason, extentedReport);
        custdashboardpage.clickMidTermContinue(extentedReport);
        GetQuotePage getquotepage = new GetQuotePage(driver, extentedReport);

        for (int i = 1; i < coversToAdd.length; i++) {
          BoolVal = false;
          ins_RowtoInteract =
              getquotepage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
          BoolVal = getquotepage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract,
              true, extentedReport);
          coverType = coversToAdd[i].split("_");
          if (BoolVal != false) {
            Log.pass(coverType[2] + " " + coverType[0] + " cover done successfully", driver,
                extentedReport, true);
          } else {
            Log.fail("Failed to " + coverType[2] + " " + coverType[0] + " cover", driver,
                extentedReport, true);
          }

        }
        // Click on recalculate
        getquotepage.clickReCalculate(extentedReport);

        // Click on buy button
        getquotepage.clickBuy(extentedReport);
        getquotepage.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
        getquotepage.clickAddMTA(extentedReport, false);
        getquotepage.clickAddAllMTANextButton(extentedReport, true);

        getquotepage.clickSaveQuoteMta(extentedReport);
        buyQuotePage = custdashboardpage.performRenewals_QuoteVariation(extentedReport, true);
        // Verify Acceptance page is landed
        Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
            "Acceptance tab was selected by default when clicked on quote variations",
            "Acceptance tab was not selected as default tab when clicked on quote variations",
            driver, extentedReport, true);
        // update payment method
        buyQuotePage.selectPaymentPlan("Monthly", extentedReport);
        buyQuotePage.selectPaymentMethod("Direct Debit", extentedReport, true);
        buyQuotePage.addAccountDetail(testData, extentedReport, true);
        buyQuotePage.clickCutomerAgreeButton(extentedReport);
        buyQuotePage.clickAcceptBtn(extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
            true);

      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
            driver, extentedReport, true);
      }

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      // driver.quit();
    }
  }

  @Test(groups = "Business_Scenario",
      description = "Check that a policy which is previously flagged with suspend billing during billing adjustment is forced out of renewal cycle (need to check that the product is set up with the referral rules that say this is a referral reason",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_708(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_708";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    String brandname = testData.get("Brand Name");
    String suspendBillingReason = testData.get("SuspendBilling Reason");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(
          homePage.verifyHomePage(), "Successfully logged into UXP Home Page with User id:"
              + userName + "& Password:" + password + "",
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
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
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
      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);

        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("drpManageRenewals"),
                custdashboardpage),
            "Manage renwal tab is present before suspend billing",
            "Manage renwal tab is not present before suspend billing", driver, extentedReport,
            true);
        SuspendBillingPage suspendBillingPage =
            custdashboardpage.selectSuspendBillingfromManagePolicy(extentedReport, true);

        suspendBillingPage.completeSuspendBilling(suspendBillingReason, extentedReport, true);
        Log.softAssertThat(
            !(custdashboardpage.uielement.verifyPageElements(Arrays.asList("drpManageRenewals"),
                custdashboardpage)),
            "Manage renwal tab is not present after suspend billing",
            "Manage renwal tab is present after suspend billing", driver, extentedReport, true);
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
            driver, extentedReport, true);
      }

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(groups = "Business_Scenario",
      description = "Invite Renewal for a policy with outstanding debt",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_720(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_720";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    String brandname = testData.get("Brand Name");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(
          homePage.verifyHomePage(), "Successfully logged into UXP Home Page with User id:"
              + userName + "& Password:" + password + "",
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
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
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
      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);

        custdashboardpage.clickInviteRenewalfromManagePolicyDropdown(extentedReport, true);
        Log.softAssertThat(custdashboardpage.verifyOutstandingWarningPopup(extentedReport),

            "Outstanding dept warning message is present",
            "Outstanding dept warning message is not present", driver, extentedReport, true);


      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
            driver, extentedReport, true);
      }

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(groups = "Business_Scenario",
      description = "Check preferred payment date is still in place on renewal ",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_709(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_709";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    String brandname = testData.get("Brand Name");
    String preferredDay = testData.get("Preferred Payment Day");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(
          homePage.verifyHomePage(), "Successfully logged into UXP Home Page with User id:"
              + userName + "& Password:" + password + "",
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
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
                                                                           // date
                                                                           // for
                                                                           // renewal
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
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
      newquotepage.selectPayment(testData, true, extentedReport);
      newquotepage.selectPreferredPaymentDay(preferredDay, extentedReport, true);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
        custdashboardpage.clickInviteRenewalfromManagePolicyDropdown(extentedReport, true);
        custdashboardpage.clickWarningContinue(extentedReport);
        newquotepage.clickNextOne(extentedReport);
        newquotepage.clickNextTwo(extentedReport);
        newquotepage.clickView(extentedReport);
        newquotepage.clickAgree(extentedReport);
        newquotepage.clickGetQuote(extentedReport);
        newquotepage.clickQuoteSave("Renew", "Renewal Quote", extentedReport);

        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
            true);

        BuyQuotePage buyQuotePage =
            custdashboardpage.performRenewals_QuoteVariation(extentedReport, true);

        Log.softAssertThat(buyQuotePage.verifyPreferredPaymentDay(preferredDay),
            "Preferred payment day is not changed during renewal",
            "Preferred payment day is changed during renewal", driver, extentedReport, true);

      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
            driver, extentedReport, true);
      }

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(groups = "Business_Scenario",
      description = "Payment details should be defaulted to what is displayed on policy,Check that during invite from within EC, the original payment methods are not overwritten and that they default in, i.e if direct debit then it still shows as direct debit",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_721_663(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_721_663";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");
    String brandname = testData.get("Brand Name");
    String paymentPlan = testData.get("Payment Plan");


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
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Enter Customer Details
      Log.message("<b>Setup for TC_024</b>", extentedReport);
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      firstName = custDetails.get("First Name");
      lastName = custDetails.get("Last Name");
      // String contactName = firstName + " " + lastName;

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);
      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // Past
      // date
      // for
      // renewal
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
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
      Log.pass("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);

      // Click continue button
      newquotepage = carddetailspage.clickContinueButton(extentedReport);
      Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment was successful", "Payment was not successful", driver, extentedReport, true);

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status

      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
        custdashboardpage.performRenewals_LapseRenewal("Invite Renewal", extentedReport, true);
        newquotepage.clickNextOne(extentedReport);
        newquotepage.clickNextTwo(extentedReport);
        newquotepage.clickView(extentedReport);
        newquotepage.clickAgree(extentedReport);
        newquotepage.clickGetQuote(extentedReport);
        newquotepage.clickQuoteSave("Renew", "Renewal Quote", extentedReport);

        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
            true);

        BuyQuotePage buyQuotePage =
            custdashboardpage.performRenewals_QuoteVariation(extentedReport, true);
        Log.softAssertThat(buyQuotePage.verifyPaymentPlan(paymentPlan),
            "Payment plan is not changed during renewal", "Payment plan is changed during renewal",
            driver, extentedReport, true);



      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
            driver, extentedReport, true);
      }

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(groups = "Business_Scenario",
      description = "Check a policy that has had do not invite flag unticked during MTA goes to red ‘R’",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_662(String browser) throws Exception {
    String tcId = "BS_662";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String title = testData.get("Title");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");

    try {
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in with User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

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

      Log.softAssertThat(
          custdashboardpage.verifyContactName(
              title + " " + testData.get("First Name") + " " + testData.get("Last Name"),
              extentedReport, true),
          "Verified Searched FirstName and LastName on Customer Dashboard",
          "Not Verified Customer name on Dashboard", driver, extentedReport, true);
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

      // Select Payment
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
      newquotepage.verifyPaymentTrasaction(extentedReport);

      Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment was successful", "Payment was not successful", driver, extentedReport, true);

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status and MTA
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {

        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);

        // Verify the flags set in STA remains same in MTA cycle
        custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport);
        Log.pass("Clicked on MTA menu successfully", driver, extentedReport, true);
        // Fill up MTa data
        custdashboardpage.enterMTADetails(testData, extentedReport, true);

        Log.pass("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
        // getquotepage = new GetQuotePage(driver, extentedReport);
        GetQuotePage getquotepage = custdashboardpage.verifyGetQuotePage(extentedReport);
        Log.pass("Get quote page loaded successfully", driver, extentedReport, true);
        // UnCheck block renewal and verify all the policy flag in MTA
        getquotepage.clickPolicyFlags("Automatic Review", extentedReport, true);

        if (!getquotepage.validateReviewRenewalFields("Automatic Review", "checked", extentedReport,
            true)) {

          Log.pass("Automatic Review unchecked successfully", extentedReport);

          // Loop to add items to the existing ins cover
          String[] coversToAdd = testData.get("coverToSelect").toString().split(",");

          for (int i = 0; i < coversToAdd.length; i++) {
            boolean BoolVal = false;
            String ins_RowtoInteract =
                getquotepage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
            BoolVal = getquotepage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract,
                true, extentedReport);
            String[] coverType = coversToAdd[i].split("_");
            if (BoolVal != false) {
              Log.pass(coverType[2] + " " + coverType[0] + "_" + coverType[1]
                  + " cover type done successfully", driver, extentedReport, true);
            } else {
              Log.fail(
                  "Failed to " + coverType[2] + " " + coverType[0] + "_" + coverType[1] + " cover",
                  driver, extentedReport, true);
            }

          }

          // Click on recalculate
          getquotepage.clickReCalculate(extentedReport);
          Log.message("Clicked on Re-Calculate", driver, extentedReport);

          // Click on buy button
          getquotepage.clickBuy(extentedReport);
          Log.message("Clicked on Buy Quote", driver, extentedReport);

          getquotepage.selectMTAReasonPayment(extentedReport);

          // Select card type
          getquotepage.takePayment(extentedReport);
          carddetailspage = getquotepage.selectVisacard(extentedReport);
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
          getquotepage = carddetailspage.clickContinueButton_getQuote(extentedReport);
          Log.softAssertThat(getquotepage.verifyPaymentTrasaction(extentedReport),
              "Payment was successful", "Payment was not successful", driver, extentedReport, true);

          custdashboardpage = getquotepage.confirmPayment("MTA", testData, extentedReport);
          Log.softAssertThat(
              custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                  custdashboardpage),
              "custdashboardpage Verified", "custdashboardpage not Verified", driver,
              extentedReport, true);

          // check for policy status
          policyDetails = custdashboardpage.getPolicyURN(extentedReport);

          if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {

            Log.pass("MTA Policy Created Successfully in accepted status", driver, extentedReport,
                true);

          } else {
            Log.fail(
                "Failed to create MTA policy in accepted status,it stands in following status:"
                    + policyDetails.get("Status").toString() + " status",
                driver, extentedReport, true);
          }

          Log.softAssertThat(
              custdashboardpage.verify_reviewStatus("Review Required", extentedReport, true),
              "Displays Review required status in customer dashboard page",
              "Failed to display review required status in customer dashboard page", driver,
              extentedReport, true);

        } else
          Log.fail(
              "Failed to uncheck Automatic Review checkbox, unable to proceed with the test case",
              extentedReport);

      }

      else {
        Log.fail("NB Policy has failed to Create in Accepted status, unable to procced with STA",
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

  }

}
