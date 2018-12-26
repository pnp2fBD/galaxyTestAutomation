package com.ssp.regression.insurer.testscripts;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
// import org.testng.annotations.BeforeTest;
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
import com.ssp.uxp_pages.CardDetailsPage;
import com.ssp.uxp_pages.CustDashboardPage;
import com.ssp.uxp_pages.GetQuotePage;
import com.ssp.uxp_pages.HomePage;
import com.ssp.uxp_pages.LoginPage;
import com.ssp.uxp_pages.NewQuotePage;
import com.ssp.uxp_pages.SearchPage;

@Listeners(EmailReport.class)
public class UXP_EC_1610 extends BaseTest {

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
    String className = "UXP_EC_1610_" + env;
    return DataUtils.testDatabyID(testcaseId, className);
  }

  @Test(
      description = "To check that the Terms and Conditions can be viewed from 'Manage Policy --> MTA' for an existing contact",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_271(String browser) throws Exception {

    String tcId = "TC_271";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String title = testData.get("Title");

    String CustomerName = title + " " + firstName + " " + lastName;
    String Corecover = testData.get("Cover");
    String policyToSelect = testData.get("Policy Name");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

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
      // newquotepage.verifyPaymentTrasaction(extentedReport);

      Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment was successful", "Payment was not successful", driver, extentedReport, true);

      // confirm payment
      custdashboardpage = newquotepage.confirmPayment(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);


      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat((policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")),
          "NB Policy Created Successfully in accepted status",
          "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status");

      // CustDashboardPage custDashboardPage = new CustDashboardPage(driver);
      // custdashboardpage.clickCompleteBtn(extentedReport);
      // custdashboardpage.clickComplete();
      loginPage = new LoginPage(driver, webSite, extentedReport).get();
      homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      // Click on Take Call
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      /*
       * // Search for existing policy searchPage.enterPolicyNumber(policyDetails.get("policyNo"));
       * createdPolicyNo = policyDetails.get("policyNo"); searchPage.clickSearch(extentedReport);
       */

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);


      // Move to Customer Dashboard Page
      custdashboardpage.clickPassVerification(extentedReport, true);
      Log.softAssertThat(custdashboardpage.verifyContactName(CustomerName, extentedReport, true),
          "Verified Searched FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Details on Dashboard", driver, extentedReport, true);

      // Click on Manage Policy Tab
      custdashboardpage.selectMTAfromManagePolicyTab(extentedReport, true);
      custdashboardpage.enterMTADetails(testData, extentedReport, true);

      GetQuotePage getquotepage = custdashboardpage.verifyGetQuotePage(extentedReport);
      getquotepage.clickAcceptanceLink(extentedReport, true);

      // add T&C Policy
      newquotepage.clickTermsAndConditionSection(extentedReport);
      newquotepage.clickAttachTermAndConditionButton(extentedReport);

      newquotepage.selectTandCPolicy(policyToSelect, extentedReport, true);

      // Verify T&C is added
      Log.softAssertThat(newquotepage.verifyTandCPolicyadded(policyToSelect, extentedReport, false),
          "User is able to view the Terms and Conditions after the Acceptance/Payment page is completed in MTA",
          "User is unable to view the Terms and Conditions after the Acceptance/Payment page is completed in MTA",
          driver, extentedReport, true);

      /*
       * // Verify able to read and edit the terms and conditions
       * Log.softAssertThat(getquotepage.verifyReadTermsAndCondition(extentedReport),
       * "Verified Edit Terms and Conditions", "Not able to verify Edit Terms and Conditions",
       * driver, extentedReport, true);
       */

      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally

  }// tcEC271

  @Test(
      description = "To check that the Terms and Conditions can be viewed from New Quote for an existing contact",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_267(String browser) throws Exception {

    String tcId = "TC_267";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String title = testData.get("Title");

    String CustomerName = title + " " + firstName + " " + lastName;
    String Corecover = testData.get("Cover");
    String policyToSelect = testData.get("Policy Name");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

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

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

      custdashboardpage.clickPassVerification(extentedReport, true);
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

      /*
       * // Verify able to read the terms and conditions
       * Log.softAssertThat(newquotepage.verifyReadTermsAndCondition(extentedReport),
       * "Verified Read Terms and Conditions", "Not able to verify Read Terms and Conditions",
       * driver, extentedReport, true);
       */

      // add T&C Policy
      newquotepage.clickTermsAndConditionSection(extentedReport);
      newquotepage.clickAttachTermAndConditionButton(extentedReport);

      newquotepage.selectTandCPolicy(policyToSelect, extentedReport, true);

      // Verify T&C is added
      Log.softAssertThat(newquotepage.verifyTandCPolicyadded(policyToSelect, extentedReport, false),
          "User is able to view the Terms and Conditions after the Acceptance/Payment page is completed",
          "User is unable to view the Terms and Conditions after the Acceptance/Payment page is completed",
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

  }// tcEC267

  @Test(
      description = "To check that the Terms and Conditions can be edited from New Quote for an existing contact",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_268(String browser) throws Exception {

    String tcId = "TC_268";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String title = testData.get("Title");

    String CustomerName = title + " " + firstName + " " + lastName;
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

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

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

      // Move to Customer Dashboard Page
      custdashboardpage.clickPassVerification(extentedReport, true);

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

      // Verify able to read and edit the terms and conditions
      Log.softAssertThat(newquotepage.verifyEditTermAndCondition(extentedReport),
          "Terms and Conditions can be edited from New Quote for an existing contact",
          "Terms and Conditions can not be edited from New Quote for an existing contact", driver,
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

  }// tcEC268

  @Test(
      description = "To check that the Terms and Conditions can be viewed from New Quote for a New contact",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_269(String browser) throws Exception {

    String tcId = "TC_269";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String title = testData.get("Title");

    String CustomerName = title + " " + firstName + " " + lastName;
    String Corecover = testData.get("Cover");
    String policyToSelect = testData.get("Policy Name");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP SIAAS Landing Page : " + webSite, driver, extentedReport, true);
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

      // add T&C Policy
      newquotepage.clickTermsAndConditionSection(extentedReport);
      newquotepage.clickAttachTermAndConditionButton(extentedReport);

      newquotepage.selectTandCPolicy(policyToSelect, extentedReport, true);

      // Verify T&C is added
      Log.softAssertThat(newquotepage.verifyTandCPolicyadded(policyToSelect, extentedReport, false),
          "User is able to view the Terms and Conditions from New Quote for a New contact",
          "User is unable to view the Terms and Conditions from New Quote for a New contact",
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

  }// tcEC269

  @Test(
      description = "To check that the Terms and Conditions can be edited from New Quote for a New contact",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_270(String browser) throws Exception {

    String tcId = "TC_270";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String title = testData.get("Title");

    String CustomerName = title + " " + firstName + " " + lastName;
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP SIAAS Landing Page : " + webSite, driver, extentedReport, true);
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

      // Verify able to read and edit the terms and conditions
      Log.softAssertThat(newquotepage.verifyEditTermAndCondition(extentedReport),
          "Terms and Conditions can be edited from New Quote for a New contact",
          "Terms and Conditions can not edited from New Quote for a New contact", driver,
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

  }// tcEC270

  @Test(
      description = "To check that the Terms and Conditions can be viewed from 'Manage Policy --> STA' for an existing contact",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_272(String browser) throws Exception {

    String tcId = "TC_272";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String title = testData.get("Title");
    String Corecover = testData.get("Cover");
    String policyToSelect = testData.get("Policy Name");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String CustomerName = title + " " + firstName + " " + lastName;

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP SIAAS Landing Page : " + webSite, driver, extentedReport, true);

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
      // newquotepage.verifyPaymentTrasaction(extentedReport);

      Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment was successful", "Payment was not successful", driver, extentedReport, true);

      // confirm payment
      custdashboardpage = newquotepage.confirmPayment(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is Verified", "Customer Dashboard page is not Verified", driver,
          extentedReport, true);


      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat((policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")),
          "NB Policy Created Successfully in accepted status",
          "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status");

      // Click Complete button
      searchPage.clickComplete(extentedReport);

      // Click on Take Call
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

      custdashboardpage.clickPassVerification(extentedReport, true);
      Log.softAssertThat(custdashboardpage.verifyContactName(CustomerName, extentedReport, true),
          "Verified Searched FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Details on Dashboard", driver, extentedReport, true);

      // Click on Manage Policy Tab
      custdashboardpage.selectSTAfromManagePolicyTab(extentedReport, true);
      custdashboardpage.enterSTADetails(testData, extentedReport, true);

      GetQuotePage getquotepage = custdashboardpage.verifyGetQuotePage(extentedReport);
      getquotepage.clickAcceptanceLink(extentedReport, true);

      // add T&C Policy
      newquotepage.clickTermsAndConditionSection(extentedReport);
      newquotepage.clickAttachTermAndConditionButton(extentedReport);

      newquotepage.selectTandCPolicy(policyToSelect, extentedReport, true);

      // Verify T&C is added
      Log.softAssertThat(newquotepage.verifyTandCPolicyadded(policyToSelect, extentedReport, false),
          "User is able to view the Terms and Conditions after the Acceptance/Payment page is completed in STA",
          "User is unable to view the Terms and Conditions after the Acceptance/Payment page is completed in STA",
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

  }// tcEC272

  @Test(
      description = "To check that the Terms and Conditions can be edited from \"Manage Policy --> MTA \" for an existing contact",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_273(String browser) throws Exception {

    String tcId = "TC_273";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String title = testData.get("Title");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);
    String CustomerName = title + " " + firstName + " " + lastName;

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
      // newquotepage.verifyPaymentTrasaction(extentedReport);

      Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment was successful", "Payment was not successful", driver, extentedReport, true);

      // confirm payment
      custdashboardpage = newquotepage.confirmPayment(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat((policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")),
          "NB Policy Created Successfully in accepted status",
          "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status");

      loginPage = new LoginPage(driver, webSite, extentedReport).get();
      homePage = loginPage.loginToSSP(userName, password, false, extentedReport);

      // Click on Take Call
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);


      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

      // Move to Customer Dashboard Page
      custdashboardpage.clickPassVerification(extentedReport, true);
      Log.softAssertThat(custdashboardpage.verifyContactName(CustomerName, extentedReport, true),
          "Verified Searched FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Details on Dashboard", driver, extentedReport, true);

      // Click on Manage Policy Tab
      custdashboardpage.selectMTAfromManagePolicyTab(extentedReport, true);
      custdashboardpage.enterMTADetails(testData, extentedReport, true);

      GetQuotePage getquotepage = custdashboardpage.verifyGetQuotePage(extentedReport);
      getquotepage.clickAcceptanceLink(extentedReport, true);

      // Verify able to read and edit the terms and conditions
      Log.softAssertThat(newquotepage.verifyEditTermAndCondition(extentedReport),
          "Terms and Conditions can be edited from MTA  for an existing contact",
          "Terms and Conditions can not be edited from MTA  for an existing contact", driver,
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

  }// tcEC273

  @Test(
      description = "To check that the Terms and Conditions can be edited from 'Manage Policy --> STA' for an existing contact",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_274(String browser) throws Exception {

    String tcId = "TC_274";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String title = testData.get("Title");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String CustomerName = title + " " + firstName + " " + lastName;

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP SIAAS Landing Page : " + webSite, driver, extentedReport, true);

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

      // confirm payment
      custdashboardpage = newquotepage.confirmPayment(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is Verified", "Customer Dashboard page is not Verified", driver,
          extentedReport, true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat((policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")),
          "NB Policy Created Successfully in accepted status",
          "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status");

      // Click Complete button
      searchPage.clickComplete(extentedReport);

      // Click on Take Call
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

      custdashboardpage.clickPassVerification(extentedReport, true);
      Log.softAssertThat(custdashboardpage.verifyContactName(CustomerName, extentedReport, true),
          "Verified Searched FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Details on Dashboard", driver, extentedReport, true);

      // Click on Manage Policy Tab
      custdashboardpage.selectSTAfromManagePolicyTab(extentedReport, true);
      custdashboardpage.enterSTADetails(testData, extentedReport, true);

      GetQuotePage getquotepage = custdashboardpage.verifyGetQuotePage(extentedReport);
      getquotepage.clickAcceptanceLink(extentedReport, true);

      // Verify able to read and edit the terms and conditions
      Log.softAssertThat(newquotepage.verifyEditTermAndCondition(extentedReport),
          "Terms and Conditions can be edited from STA for an existing contact",
          "Terms and Conditions can not be edited from STA for an existing contact", driver,
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

  }// tcEC274

  @Test(
      description = "To check that the Terms and Conditions can be viewed from 'Manage Policy --> Invite Renewal' for an existing contact",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_275(String browser) throws Exception {

    String tcId = "TC_275";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String Corecover = testData.get("Cover");
    String title = testData.get("Title");
    String CustomerName = title + " " + firstName + " " + lastName;
    String policyToSelect = testData.get("Policy Name");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

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
      searchPage.enterCustomerDetails(testData, true, extentedReport, true);

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

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

      custdashboardpage.clickPassVerification(extentedReport, true);
      Log.softAssertThat(custdashboardpage.verifyContactName(CustomerName, extentedReport, true),
          "Verified Searched FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Details on Dashboard", driver, extentedReport, true);

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

      // add T&C Policy
      newquotepage.clickTermsAndConditionSection(extentedReport);
      newquotepage.clickAttachTermAndConditionButton(extentedReport);

      newquotepage.selectTandCPolicy(policyToSelect, extentedReport, true);

      // Verify T&C is added
      Log.softAssertThat(newquotepage.verifyTandCPolicyadded(policyToSelect, extentedReport, false),
          "User is able to view the Terms and Conditions under Invite Renewal' for an existing contact",
          "User is unable to view the Terms and Conditions under Invite Renewal' for an existing contact",
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

  }// tcEC275

  @Test(
      description = "To check that the Terms and Conditions can be edited from 'Manage Policy --> Invite Renewal' for an existing contact and check that the modified T&C is displayed in the customer dashboard",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_276(String browser) throws Exception {

    String tcId = "TC_276";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String title = testData.get("Title");
    String Corecover = testData.get("Cover");
    String policyToSelect = testData.get("Policy Name");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String branch = testData.get("Bank Branch");
    String accNo = testData.get("Account Number");
    String sortCode = testData.get("Sort Code");

    String CustomerName = title + " " + firstName + " " + lastName;

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
      searchPage.enterCustomerDetails(testData, true, extentedReport);

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

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

      custdashboardpage.clickPassVerification(extentedReport, true);
      Log.softAssertThat(custdashboardpage.verifyContactName(CustomerName, extentedReport, true),
          "Verified Searched FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Details on Dashboard", driver, extentedReport, true);

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

      // add T&C Policy
      newquotepage.clickTermsAndConditionSection(extentedReport);
      newquotepage.clickAttachTermAndConditionButton(extentedReport);

      newquotepage.selectTandCPolicy(policyToSelect, extentedReport, true);

      String randomValue = "Testing";
      Log.softAssertThat(
          newquotepage.verifyTandC_Edited(policyToSelect, randomValue, extentedReport, true),
          "User is able to edit the text when click on 'Edit' button with correct skills profile",
          "User is unable to edit the text when click on 'Edit' button with correct skills profile",
          driver, extentedReport, true);

      newquotepage.selectAccount(sortCode, branch, accName, accNo, extentedReport, true);
      newquotepage.tickCustomerAgreesCheckbox(extentedReport);

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

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

      custdashboardpage.clickViewTandCfromManagePolicyDropdown(extentedReport, false);


      Log.softAssertThat(
          custdashboardpage.verifyEditedValueExitsInTermAndCond(policyToSelect, randomValue,
              extentedReport, true),
          "User is able to edit the Terms and Conditions during renewal process and is able to view the edited T&C from the customer dashboard",
          "User is unable to edit the Terms and Conditions during renewal process and is unable to view the edited T&C from the customer dashboard",
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

  }// tcEC276

  @Test(
      description = "To check that the user is NOT able to edit the Terms and Conditions that are mandatory",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_278(String browser) throws Exception {

    String tcId = "TC_278";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String title = testData.get("Title");
    String Corecover = testData.get("Cover");

    String policyToSelect = testData.get("Policy Name");
    String mandatoryMessage = testData.get("Mandatory message");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String CustomerName = title + " " + firstName + " " + lastName;

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call
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
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      Log.softAssertThat(custdashboardpage.verifyContactName(CustomerName, extentedReport, true),
          "Verified Searched FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Details on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Create Quote
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      // Get and Buy Quote
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickBuy(extentedReport);

      // add T&C Policy
      newquotepage.clickTermsAndConditionSection(extentedReport);
      newquotepage.clickAttachTermAndConditionButton(extentedReport);

      newquotepage.selectTandCPolicy(policyToSelect, extentedReport, true);

      // Verify Quote
      Log.softAssertThat(newquotepage.verifyTandCPolicyadded(policyToSelect, extentedReport, false),
          policyToSelect + " - T&C is added under terms & condition section",
          policyToSelect + " - T&C is not added under terms & condition section", driver,
          extentedReport, true);

      Log.softAssertThat(
          newquotepage.verifyMandatoryTandCMessage(mandatoryMessage, extentedReport, false),
          mandatoryMessage + " - is displayed under terms & condition section",
          mandatoryMessage + " - is not displayed under terms & condition section", driver,
          extentedReport, true);

      // Click & verify mandatroy policy
      Log.softAssertThat(!newquotepage.verifyMandatoryTandCEditable(extentedReport, false),
          "Mandatory Policy is not editable since edit button is not available",
          "Mandatory Policy is editable since edit button is available", driver, extentedReport,
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

  }// TC_278

  @Test(
      description = "To check that a Star icon is added against the new Coverage Name of one or more T&C",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_282(String browser) throws Exception {

    String tcId = "TC_282";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String title = testData.get("Title");
    String Corecover = testData.get("Cover");

    String policyToSelect = testData.get("Policy Name");
    String CustomerName = title + " " + firstName + " " + lastName;

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call
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
      newquotepage.clickBuy(extentedReport);

      // add T&C Policy
      newquotepage.clickTermsAndConditionSection(extentedReport);

      newquotepage.clickAttachTermAndConditionButton(extentedReport);
      newquotepage.selectTandCPolicy(policyToSelect, extentedReport, true);

      // Verify T&C is added
      Log.softAssertThat(newquotepage.verifyTandCPolicyadded(policyToSelect, extentedReport, false),
          policyToSelect + " - T&C is added under terms & condition section",
          policyToSelect + " - T&C is not added under terms & condition section", driver,
          extentedReport, true);

      // Verify star icon is added
      Log.softAssertThat(newquotepage.isStarIconDispalyed(policyToSelect, extentedReport, false),
          "Star icon is added against the newly added T&C",
          "Star icon is not added against the newly added T&C", driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally

  }// TC_282

  @Test(
      description = "To check that a Star icon is added against the new Coverage Name of one or more T&C",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_283(String browser) throws Exception {

    String tcId = "TC_283";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String title = testData.get("Title");
    String Corecover = testData.get("Cover");

    String policyToSelect = testData.get("Policy Name");
    String CustomerName = title + " " + firstName + " " + lastName;
    String[] arrPolicy = policyToSelect.split(",");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call
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
      newquotepage.clickBuy(extentedReport);

      // add T&C Policy
      newquotepage.clickTermsAndConditionSection(extentedReport);

      for (int i = 0; i < arrPolicy.length; i++) {

        newquotepage.clickAttachTermAndConditionButton(extentedReport);
        newquotepage.selectTandCPolicy(arrPolicy[i], extentedReport, true);

        // Verify T&C is added
        Log.softAssertThat(newquotepage.verifyTandCPolicyadded(arrPolicy[i], extentedReport, false),
            arrPolicy[i] + " - T&C is added under terms & condition section",
            arrPolicy[i] + " - T&C is not added under terms & condition section", driver,
            extentedReport, true);

        // Verify star icon is added
        if (i == 0) {
          Log.softAssertThat(newquotepage.isStarIconDispalyed(arrPolicy[i], extentedReport, false),
              "Star icon is added against the newly added T&C",
              "Star icon is not added against the newly added T&C", driver, extentedReport, true);
        } else {
          Log.softAssertThat(newquotepage.isStarIconDispalyed(arrPolicy[i], extentedReport, false),
              "Star icon is added against the new Coverage Name of one or more T&C",
              "Star icon is not added against the new Coverage Name of one or more T&C", driver,
              extentedReport, true);
        }
      }

      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally

  }// TC_283

  @Test(description = "To check that the policy wording is collapsed if click on Close button",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_302(String browser) throws Exception {

    String tcId = "TC_302";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String title = testData.get("Title");
    String Corecover = testData.get("Cover");

    String policyToSelect = testData.get("Policy Name");
    String CustomerName = title + " " + firstName + " " + lastName;

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call
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
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      Log.softAssertThat(custdashboardpage.verifyContactName(CustomerName, extentedReport, true),
          "Verified Searched FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Details on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      // Get and Buy Quote
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickBuy(extentedReport);

      // add T&C Policy
      newquotepage.clickTermsAndConditionSection(extentedReport);
      newquotepage.clickAttachTermAndConditionButton(extentedReport);

      newquotepage.selectTandCPolicy(policyToSelect, extentedReport, true);

      // Verify T&C is added
      Log.softAssertThat(newquotepage.verifyTandCPolicyadded(policyToSelect, extentedReport, false),
          policyToSelect + " - T&C is added under terms & condition section",
          policyToSelect + " - T&C is not added under terms & condition section", driver,
          extentedReport, true);

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

      custdashboardpage.clickViewTandCfromManagePolicyDropdown(extentedReport, true);

      Log.softAssertThat(
          custdashboardpage.verifyAddedTandC_Collapsed(policyToSelect, extentedReport, true),
          "The policy wording is collapsed after clicking the close button",
          "The policy wording is not collapsed after clicking the close button", driver,
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

  }// TC_283

  @Test(
      description = "To check that Editor controls consist of the following buttons 'Edit and De-select' depending on the type of T&C",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_308(String browser) throws Exception {

    String tcId = "TC_308";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String title = testData.get("Title");
    String Corecover = testData.get("Cover");
    String policyToSelect = testData.get("Policy Name");
    String CustomerName = title + " " + firstName + " " + lastName;

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call
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
      newquotepage.clickBuy(extentedReport);

      // add T&C Policy
      newquotepage.clickTermsAndConditionSection(extentedReport);
      newquotepage.clickAttachTermAndConditionButton(extentedReport);

      newquotepage.selectTandCPolicy(policyToSelect, extentedReport, true);

      // Verify T&C is added
      Log.softAssertThat(newquotepage.verifyTandCPolicyadded(policyToSelect, extentedReport, false),
          policyToSelect + " - T&C is added under terms & condition section",
          policyToSelect + " - T&C is not added under terms & condition section", driver,
          extentedReport, true);

      Log.softAssertThat(newquotepage.verifyButtonForTandC(policyToSelect, extentedReport, false),
          policyToSelect
              + " - policy T&C that are not mandatory is having buttons Edit & De-select",
          policyToSelect
              + "  - policy T&C that are not mandatory is not having buttons Edit & De-select",
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

  }// TC_282

  @Test(
      description = "To check that user is able to edit the text when click on 'Edit' button with correct skills profile",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_321(String browser) throws Exception {

    String tcId = "TC_321";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String title = testData.get("Title");
    String Corecover = testData.get("Cover");
    String policyToSelect = testData.get("Policy Name");
    String CustomerName = title + " " + firstName + " " + lastName;

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call
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
      newquotepage.clickBuy(extentedReport);

      // add T&C Policy
      newquotepage.clickTermsAndConditionSection(extentedReport);
      newquotepage.clickAttachTermAndConditionButton(extentedReport);

      newquotepage.selectTandCPolicy(policyToSelect, extentedReport, true);

      // Verify T&C is added
      Log.softAssertThat(newquotepage.verifyTandCPolicyadded(policyToSelect, extentedReport, false),
          policyToSelect + " - T&C is added under terms & condition section",
          policyToSelect + " - T&C is not added under terms & condition section", driver,
          extentedReport, true);

      Log.softAssertThat(
          newquotepage.verifyTandC_Editable(policyToSelect, extentedReport, true, "ABC"),
          "User is able to edit the text when click on 'Edit' button with correct skills profile",
          "User is unable to edit the text when click on 'Edit' button with correct skills profile",
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

  }// TC_282

  @Test(
      description = "To check that user is able to apply the edited terms and conditions when click on 'save' button",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_322(String browser) throws Exception {

    String tcId = "TC_322";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String title = testData.get("Title");
    String Corecover = testData.get("Cover");
    String policyToSelect = testData.get("Policy Name");
    String CustomerName = title + " " + firstName + " " + lastName;

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call
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
      newquotepage.clickBuy(extentedReport);

      // add T&C Policy
      newquotepage.clickTermsAndConditionSection(extentedReport);
      newquotepage.clickAttachTermAndConditionButton(extentedReport);

      newquotepage.selectTandCPolicy(policyToSelect, extentedReport, true);

      // Verify T&C is added
      Log.softAssertThat(newquotepage.verifyTandCPolicyadded(policyToSelect, extentedReport, false),
          policyToSelect + " - T&C is added under terms & condition section",
          policyToSelect + " - T&C is not added under terms & condition section", driver,
          extentedReport, true);

      String randomValue = "Testing";
      Log.softAssertThat(
          newquotepage.verifyTandC_Edited(policyToSelect, randomValue, extentedReport, true),
          "User is able to apply the edited terms and conditions when click on 'save' button",
          "User is unable to apply the edited terms and conditions when click on 'save' button",
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

  }// TC_322

  @Test(
      description = "To check that the user is able to add a T & C to a policy when click on 'Select' button",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_331(String browser) throws Exception {

    String tcId = "TC_331";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String title = testData.get("Title");
    String Corecover = testData.get("Cover");
    String policyToSelect = testData.get("Policy Name");
    String CustomerName = title + " " + firstName + " " + lastName;

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call
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
      newquotepage.clickBuy(extentedReport);

      // add T&C Policy
      newquotepage.clickTermsAndConditionSection(extentedReport);
      newquotepage.clickAttachTermAndConditionButton(extentedReport);

      newquotepage.selectTandCPolicy(policyToSelect, extentedReport, true);

      // Verify T&C is added
      Log.softAssertThat(newquotepage.verifyTandCPolicyadded(policyToSelect, extentedReport, false),
          "User is able to add a T & C to a policy when click on 'Select' button",
          "User is unable to add a T & C to a policy when click on 'Select' button", driver,
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

  }// TC_331

  @Test(
      description = "To check that 'Any changes you have made to this Term & Condition will be lost if you do not Save.Do you wish to Save?' is prompted when 'Cancel' button is clicked",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_327(String browser) throws Exception {

    String tcId = "TC_327";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String title = testData.get("Title");
    String Corecover = testData.get("Cover");
    String policyToSelect = testData.get("Policy Name");
    String CustomerName = title + " " + firstName + " " + lastName;

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call
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
      newquotepage.clickBuy(extentedReport);

      // add T&C Policy
      newquotepage.clickTermsAndConditionSection(extentedReport);
      newquotepage.clickAttachTermAndConditionButton(extentedReport);

      newquotepage.selectTandCPolicy(policyToSelect, extentedReport, true);

      // Verify T&C is added
      Log.softAssertThat(newquotepage.verifyTandCPolicyadded(policyToSelect, extentedReport, false),
          policyToSelect + " - T&C is added under terms & condition section",
          policyToSelect + " - T&C is not added under terms & condition section", driver,
          extentedReport, true);

      // Edit T&C
      newquotepage.verifyTandC_Editable(policyToSelect, extentedReport, false, "ABC");

      // Click cancel button
      newquotepage.clickCancelBtnOfTandC(policyToSelect, extentedReport, true);

      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("mdlConfirmationWnd"),
              newquotepage),
          "'Any changes you have made to this Term & Condition will be lost if you do not Save.  Do you wish to Save?' is prompted when 'Cancel' button is clicked",
          "'Any changes you have made to this Term & Condition will be lost if you do not Save.  Do you wish to Save?' is not prompted when 'Cancel' button is clicked",
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

  }// TC_331

  @Test(
      description = "To check that 'Any changes you have made to this Term & Condition will be lost if you do not Save. Do you wish to Save?' modal has 2 buttons 'Yes' and 'No'",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_328(String browser) throws Exception {

    String tcId = "TC_328";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String title = testData.get("Title");
    String Corecover = testData.get("Cover");
    String policyToSelect = testData.get("Policy Name");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String CustomerName = title + " " + firstName + " " + lastName;

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call
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
      newquotepage.clickBuy(extentedReport);

      // add T&C Policy
      newquotepage.clickTermsAndConditionSection(extentedReport);
      newquotepage.clickAttachTermAndConditionButton(extentedReport);

      newquotepage.selectTandCPolicy(policyToSelect, extentedReport, true);

      // Verify T&C is added
      Log.softAssertThat(newquotepage.verifyTandCPolicyadded(policyToSelect, extentedReport, false),
          policyToSelect + " - T&C is added under terms & condition section",
          policyToSelect + " - T&C is not added under terms & condition section", driver,
          extentedReport, true);

      // Edit T&C
      newquotepage.verifyTandC_Editable(policyToSelect, extentedReport, false, "ABC");

      // Click cancel button
      newquotepage.clickCancelBtnOfTandC(policyToSelect, extentedReport, true);

      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("btnYesTandC"), newquotepage),
          "Yes button is available in the cofirmation dialog",
          "Yes button is not available in the cofirmation dialog", driver, extentedReport, true);

      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("btnNoTandC"), newquotepage),
          "No button is available in the cofirmation dialog",
          "No button is not available in the cofirmation dialog", driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    } // finally

  }// TC_331

  @Test(
      description = "To check that selecting 'Yes' saves the edits made by the user on a particular T&C",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_329(String browser) throws Exception {

    String tcId = "TC_329";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String title = testData.get("Title");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String policyToSelect = testData.get("Policy Name");

    String CustomerName = title + " " + firstName + " " + lastName;

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call
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
      newquotepage.clickBuy(extentedReport);

      // add T&C Policy
      newquotepage.clickTermsAndConditionSection(extentedReport);
      newquotepage.clickAttachTermAndConditionButton(extentedReport);

      newquotepage.selectTandCPolicy(policyToSelect, extentedReport, true);

      // Verify T&C is added
      Log.softAssertThat(newquotepage.verifyTandCPolicyadded(policyToSelect, extentedReport, false),
          policyToSelect + " - T&C is added under terms & condition section of accpetance page",
          policyToSelect + " - T&C is not added under terms & condition section of accpetance page",
          driver, extentedReport, true);

      // Edit T&C
      String ValueToEdit = "ABC";
      Log.softAssertThat(
          newquotepage.verifyTandC_Editable(policyToSelect, extentedReport, false, ValueToEdit),
          policyToSelect + " - T&C is edited under terms & condition section",
          policyToSelect + " - T&C is not edited under terms & condition section", driver,
          extentedReport, true);

      // Click cancel button
      newquotepage.clickCancelBtnOfTandC(policyToSelect, extentedReport, true);

      // click yes button
      newquotepage.clickBtnOfTandCConfirmationWnd("yes", extentedReport, true);

      Log.softAssertThat(
          newquotepage.verifyTandC_Editable(policyToSelect, extentedReport, false, ValueToEdit),
          policyToSelect + " - T&C contain the edited value - " + ValueToEdit
              + " - After clicking YES button",
          policyToSelect + " - T&C does not contain the edited value - " + ValueToEdit
              + " - After clicking YES button",
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

  }// TC_331

}
