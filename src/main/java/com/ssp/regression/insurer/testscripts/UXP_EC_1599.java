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
import com.ssp.uxp_pages.CustDashboardPage;
import com.ssp.uxp_pages.GetQuotePage;
import com.ssp.uxp_pages.HomePage;
import com.ssp.uxp_pages.LoginPage;
import com.ssp.uxp_pages.NewQuotePage;
import com.ssp.uxp_pages.SearchPage;
import com.ssp.uxp_pages.CardDetailsPage;

@Listeners(EmailReport.class)
public class UXP_EC_1599 extends BaseTest {

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
    String className = "UXP_EC_1599_" + env;
    return DataUtils.testDatabyID(testcaseId, className);
  }

  @Test(
      description = "Check for interested party pane - Validate the message \"No interested party have been added\" - Click interested party - Check for Add interesparty and search results -Check the fields of panes - Add existing Int party - Check the delete icon on newquote page and select Yes",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_57_59_60_126(String browser) throws Exception {

    String tcId = "TC_57_59_60_126";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String addInterestContactType = testData.get("AddInterestContactType");
    String brandname = testData.get("Brand Name");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP Contact Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);

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
      newquotepage.checkInterestedParties();
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("noInterestedpartiesMsg"),
              newquotepage),
          "No interested parties have been added--text visible",
          "No interested parties have been added-text has not visible", driver, extentedReport,
          true);
      newquotepage.clickAddParties(extentedReport, true);
      newquotepage.selectContactType(addInterestContactType, extentedReport);

      newquotepage.AIenterLastName(testData.get("Last Name"), extentedReport);

      newquotepage.AIenterFirstName(testData.get("First Name"), extentedReport);

      newquotepage.clicksearch(extentedReport);

      newquotepage.checkAddPartySearchResultsPane(extentedReport, true);

      newquotepage.clickselect(extentedReport, true);

      newquotepage.addInterestedPartyType(extentedReport, true);

      // newquotepage.addInterestedPartyTypeToDate(extentedReport, true);

      newquotepage.addInterestedPartyClickSave(extentedReport, true);

      newquotepage.click_Deleteicon(extentedReport, true);

      newquotepage.checkremoveDescription();

      newquotepage.yesnobuttonVisible(extentedReport, true);

      newquotepage.clickyesbutton(extentedReport, true);

      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally

  }// TC_57_59_60_126

  @Test(
      description = "Check for interested party pane - Validate the message \"No interested party have been added\" - Click interested party - Check for Add interesparty and search results -Check the fields of panes - Add existing Int party - Check the delete icon on newquote page and select No",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_73_74_75(String browser) throws Exception {

    String tcId = "TC_73_74_75";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String addInterestContactType = testData.get("AddInterestContactType");
    String brandname = testData.get("Brand Name");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP Contact Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);

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
      newquotepage.checkInterestedParties();
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("noInterestedpartiesMsg"),
              newquotepage),
          "No interested parties have been added--text visible",
          "No interested parties have been added-text has not visible", driver, extentedReport,
          true);
      newquotepage.clickAddParties(extentedReport, true);
      newquotepage.selectContactType(addInterestContactType, extentedReport);

      newquotepage.AIenterLastName(testData.get("Last Name"), extentedReport);

      newquotepage.AIenterFirstName(testData.get("First Name"), extentedReport);

      newquotepage.clicksearch(extentedReport);

      newquotepage.checkAddPartySearchResultsPane(extentedReport, true);

      newquotepage.clickselect(extentedReport, true);

      newquotepage.addInterestedPartyType(extentedReport, true);

      // newquotepage.addInterestedPartyTypeToDate(extentedReport, true);

      newquotepage.addInterestedPartyClickSave(extentedReport, true);

      newquotepage.click_Deleteicon(extentedReport, true);

      // newquotepage.verifyRemoveModal(extentedReport, true);

      newquotepage.checkremoveDescription();

      // newquotepage.yesnobuttonVisible(extentedReport, true);

      newquotepage.clicknobutton(extentedReport, true);

      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally

  }// TC_73_74_75

  @Test(
      description = "Check for interested party pane -Check the delete icon on newquote page and verify that page gets to new quote when clicking close(X) button",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_69_70_72(String browser) throws Exception {

    String tcId = "TC_69_70_72";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String addInterestContactType = testData.get("AddInterestContactType");
    String brandname = testData.get("Brand Name");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP Contact Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);

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
      newquotepage.checkInterestedParties();
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("noInterestedpartiesMsg"),
              newquotepage),
          "No interested parties have been added--text visible",
          "No interested parties have been added-text has not visible", driver, extentedReport,
          true);
      newquotepage.clickAddParties(extentedReport, true);
      newquotepage.selectContactType(addInterestContactType, extentedReport);

      newquotepage.AIenterLastName(testData.get("Last Name"), extentedReport);

      newquotepage.AIenterFirstName(testData.get("First Name"), extentedReport);

      newquotepage.clicksearch(extentedReport);

      newquotepage.checkAddPartySearchResultsPane(extentedReport, true);

      newquotepage.clickselect(extentedReport, true);

      newquotepage.addInterestedPartyType(extentedReport, true);

      // newquotepage.addInterestedPartyTypeToDate(extentedReport, true);

      newquotepage.addInterestedPartyClickSave(extentedReport, true);

      newquotepage.click_Deleteicon(extentedReport, true);

      // newquotepage.verifyRemoveModal(extentedReport, true);

      newquotepage.checkremoveDescription();

      // newquotepage.yesnobuttonVisible(extentedReport, true);

      newquotepage.clickClosebutton(extentedReport, true);

      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally

  }// TC_69_70_72

  @Test(description = "Verify that user able to add personal contact as interested party",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_97_104(String browser) throws Exception {

    String tcId = "TC_97_104";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = testData.get("First Name");
    String lastName = testData.get("Last Name");
    String CustomerName = testData.get("Title") + " " + firstName + " " + lastName;
    String addInterestContactType = testData.get("AddInterestContactType");
    String brandname = testData.get("Brand Name");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

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
      testData = searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardPage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardPage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardPage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);
      Log.softAssertThat(
          custdashboardPage.verifyContactName(testData.get("Title") + " "
              + testData.get("First Name") + " " + testData.get("Last Name"), extentedReport, true),
          "Verified Searched FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Details on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardPage.clickNewQuote(true, extentedReport);
      custdashboardPage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardPage.clickContinueQuote(true, extentedReport);

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
      newquotepage.checkInterestedParties();
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("noInterestedpartiesMsg"),
              newquotepage),
          "No interested parties have been added - text visible",
          "No interested parties have been added - text has not visible", driver, extentedReport,
          true);
      newquotepage.clickAddParties(extentedReport, true);
      newquotepage.selectContactType(addInterestContactType, extentedReport);

      newquotepage.AIenterLastName("Invalid", extentedReport);
      newquotepage.clicksearch(extentedReport);
      newquotepage.clickcreatenewAddparty(extentedReport, true);
      testData.put("First Name", "");
      testData.put("Last Name", "");


      HashMap<String, String> IPContact =
          newquotepage.enterCustomerDetails(testData, true, extentedReport);
      String mainCustFirstName = IPContact.get("First Name");
      String mainCustLastName = IPContact.get("Last Name");
      newquotepage.confirmCreateCustomer(true, extentedReport);

      newquotepage.clickselect(extentedReport, true);

      newquotepage.addInterestedPartyType(extentedReport, true);

      // newquotepage.addInterestedPartyTypeToDate(extentedReport, true);

      newquotepage.addInterestedPartyClickSave(extentedReport, true);
      String IpNameTochk = testData.get("Title") + " " + mainCustFirstName + " " + mainCustLastName;
      Log.message("Intersted party name:" + IpNameTochk);
      Log.softAssertThat(newquotepage.checkaddedInterestedParty(IpNameTochk),
          IpNameTochk + " Added party is visible", IpNameTochk + " Added party is not visible",
          driver, extentedReport, true);

      /// Add to buy NB policy
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
      newquotepage.verifyPaymentTrasaction(extentedReport);

      if (!newquotepage.verifyPaymentTrasaction(extentedReport)) {
        Log.fail("Payment was not successful", driver, extentedReport, true);
      } else {

        Log.pass("Payment was successful", driver, extentedReport, true);
      }

      // confirm payment
      custdashboardPage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardPage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardPage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardPage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {

        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);

        /// Add search for the existing IP contact
        custdashboardPage.clickComplete(extentedReport);
        Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
            "Login failed", driver, extentedReport, true);

        // Click on Take Call link
        homePage.clickTakeCall(extentedReport);
        homePage.clickMyBrands(brandname, extentedReport);
        Log.softAssertThat(
            searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
            "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

        searchPage.enterLastName(mainCustLastName, extentedReport);
        searchPage.clickSearch(extentedReport);

        custdashboardPage = searchPage.clickSearchedContactPolicy("policy",
            policyDetails.get("PolicyNo"), "", true, extentedReport);
        Log.pass("Policy details displayed for the added interested party", extentedReport);
        custdashboardPage.verifyCustomerDashboardPage();
        Log.pass("Navigated to Cutomer Dashboard", driver, extentedReport, true);

        // Verifying Customer Details
        CustomerName = testData.get("Title") + " " + mainCustFirstName + " " + mainCustLastName;
        Log.softAssertThat(custdashboardPage.verifyContactName(CustomerName, extentedReport, true),
            "Verified Interested party FirstName and LastName on Customer Dashboard",
            "Not Verified Interested party FirstName and LastName on Customer Dashboard", driver,
            extentedReport, true);

      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }

  }// TC_97_104

  @Test(
      description = "Add Existing Personal contact as interested party - validate the warning message, fields on Interesdparty window",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_61_67_68_76_77_78_100_101_102(String browser) throws Exception {

    String tcId = "TC_61_67_68_76_77_78_100_101_102";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String title = testData.get("Title");
    String brandname = testData.get("Brand Name");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("UXP Contact Centre Landing Page : " + webSite, driver, extentedReport, true);

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

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.checkInterestedParties();
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("noInterestedpartiesMsg"),
              newquotepage),
          "No interested parties have been added--text is visible",
          "No interested parties have been added-text has not visible", driver, extentedReport,
          true);
      newquotepage.clickAddParties(extentedReport, true);
      // newquotepage.checkAddPartyModalPanes(extentedReport, true);

      newquotepage.checkAddPartymodalPanefields(extentedReport, true);

      // to check the presence of interested party pop up - Manual Id
      // TC_69
      newquotepage.Select_InterstedParty(testData, extentedReport, true);

      newquotepage.validatemsgonIntPar(extentedReport, true);

      // to check whether pop up has createNewIntParty and cancel button -
      // Manual ID TC_75 & TC_84
      newquotepage.ValidateNewIPButon_cancelBut(extentedReport, true);

      // Add interested party values (second true paramter to true- to set
      // date
      newquotepage.EnterIntPartype(extentedReport, false, true);

      newquotepage.IntParty_butsave(extentedReport, true);

      // Validate error message when to date not set on inte party window
      // - Manual Id TC_605
      /*
       * newquotepage.Validate_errMsg_whenNoDateSet(extentedReport, true);
       * 
       * newquotepage.EnterIntPartype(extentedReport, true, true);
       * 
       * newquotepage.IntParty_butsave(extentedReport, true);
       */

      /// TODO
      /// Add method to verify the added interested party name displays
      /// the personal contact

      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally

  }// TC_61_67_68_76_77_78_100_101_102

  @Test(
      description = "Validate Search fields when personal contact type changed to corporate contact on search pane of interested party window",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_56(String browser) throws Exception {

    String tcId = "TC_56"; // Manual TC_609
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String title = testData.get("Title");

    String brandname = testData.get("Brand Name");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("UXP Contact Centre Landing Page : " + webSite, driver, extentedReport, true);

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

      newquotepage.clickGetQuote(extentedReport);

      newquotepage.checkInterestedParties();
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("noInterestedpartiesMsg"),
              newquotepage),
          "No interested parties have been added--text is visible",
          "No interested parties have been added-text has not visible", driver, extentedReport,
          true);
      newquotepage.clickAddParties(extentedReport, true);

      // to check the presence of interested party pop up
      newquotepage.Select_InterstedParty(testData, extentedReport, true);

      newquotepage.validate_corpContact_IntpartyFields(extentedReport, true);

      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally

  }// TC_56

  @Test(
      description = "Search NB policy and perform MTA with all covers added and Check that the 'Policyholders' column displays the personal contact name as per the below format while performing MTA '<Title, First Name, Last Name>' on getquote page",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_58(String browser) throws Exception {
    String tcId = "TC_58";
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
        custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport);
        Log.pass(" Clicked on MTA menu successfully", driver, extentedReport, true);
        // Fill up MTa data
        custdashboardpage.enterMTADetails(testData, extentedReport, true);

        Log.pass("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
        GetQuotePage getquotepage = new GetQuotePage(driver, extentedReport);
        getquotepage.verifyGetQuotePage(extentedReport);
        Log.pass("Get quote page loaded successfully", driver, extentedReport, true);
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

        // Validate the interested party section
        Log.softAssertThat(
            getquotepage.verifyPolicyHolderSection(
                title + " " + testData.get("First Name") + " " + testData.get("Last Name"),
                extentedReport),
            "Policy holder section displayed the customer name",
            "Policy holder section failed to display customer name in getquotepage");

      } else {
        Log.fail("NB Policy has failed to Create in Accepted status, unable to procced with MTA",
            driver, extentedReport, true);

      } // Perform MTA

      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();

    } // finally

  }// TC_58

  /// Functional test cases //////

  @Test(
      description = "check that  additional covers that are available to add to a policy are displayed under the 'Add Cover' section",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_F56(String browser) throws Exception {
    String tcId = "TC_F56";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
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

      // Verify the add covers section
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(
              Arrays.asList("txt_addCover", "txt_AddCoverLE", "txt_AddCoverHE"), newquotepage),
          "Verified Add cover section ", "Failed to verify add cover section in new quote page",
          driver, extentedReport, true);


      // Log.softAssertThat(newquotepage.verify_AddCoverSection(true, extentedReport), "Verified Add
      // cover section ",
      // "Failed to verify add cover section in new quote page", driver, extentedReport, true);

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
      description = "check that  ‘No interested parties have been added’ message is displayed in the 'Interested Parties' section if a policy does not have an interested party",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_F65(String browser) throws Exception {

    String tcId = "TC_F65";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP Contact Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);

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
      newquotepage.checkInterestedParties();
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("noInterestedpartiesMsg"),
              newquotepage),
          "No interested parties have been added--text visible",
          "No interested parties have been added-text has not visible", driver, extentedReport,
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

  }

  @Test(
      description = "check that  'Add Interested Party' modal is displayed on clicking 'Add Interested Party' button on the cover page",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_F66(String browser) throws Exception {

    String tcId = "TC_F66";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String addInterestContactType = testData.get("AddInterestContactType");
    String brandname = testData.get("Brand Name");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP Contact Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);

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
      newquotepage.checkInterestedParties();
      newquotepage.clickAddParties(extentedReport, true);
      newquotepage.selectContactType(addInterestContactType, extentedReport);
      Log.pass(
          "Add Interested Party modal is displayed on clicking 'Add Interested Party' button on the cover page",
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

  }// TC_F66

  @Test(
      description = "Check that the search button is active only after selecting the value from 'Contact Type' drop down in MTA policy",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_F70(String browser) throws Exception {
    String tcId = "TC_F70";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
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
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status and MTA
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);

        custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport);
        Log.pass(" Clicked on MTA menu successfully", driver, extentedReport, true);
        // Fill up MTa data
        custdashboardpage.enterMTADetails(testData, extentedReport, true);

        Log.pass("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
        GetQuotePage getquotepage = new GetQuotePage(driver, extentedReport);
        getquotepage.verifyGetQuotePage(extentedReport);
        Log.pass("Get quote page loaded successfully", driver, extentedReport, true);
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

        // Add interested party and verify the intersted aprty section
        getquotepage.clickAddParties(extentedReport, true);

        Log.softAssertThat(!getquotepage.verify_searchButton(extentedReport),
            "Search button on interested party modal was disabled when no data entered as expected",
            "Search button was enabled when no data entered in intersted party modal, which is not expected",
            driver, extentedReport, true);

        getquotepage.selectContactType(testData.get("AddInterestContactType"), extentedReport);

        Log.softAssertThat(getquotepage.verify_searchButton(extentedReport),
            "Search button is enabled when contact type selected",
            "Search button was disabled even after selecting contact type", driver, extentedReport,
            true);


      } else {
        Log.fail("NB Policy has failed to Create in Accepted status, unable to procced with MTA",
            driver, extentedReport, true);
      } // Perform MTA

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();

    } // finally

  }// TC_F70

  @Test(
      description = "Check that the user is able to search with 'LastName' and 'FirstName' when selecting 'Personal Contact' as 'Contact Type' drop down for MTA policy",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_F71(String browser) throws Exception {

    String tcId = "TC_F71";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String addInterestContactType = testData.get("AddInterestContactType");
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
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status and MTA
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);

        custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport);
        Log.pass(" Clicked on MTA menu successfully", driver, extentedReport, true);
        // Fill up MTa data
        custdashboardpage.enterMTADetails(testData, extentedReport, true);

        Log.pass("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
        GetQuotePage getquotepage = new GetQuotePage(driver, extentedReport);
        getquotepage.verifyGetQuotePage(extentedReport);
        Log.pass("Get quote page loaded successfully", driver, extentedReport, true);
        // Loop to add items to the existing ins cover
        // String[] coversToAdd = testData.get("coverToSelect").toString().split(",");
        //
        // for (int i = 0; i < coversToAdd.length; i++) {
        // String ins_RowtoInteract = getquotepage.SelectInsuranceItem(coversToAdd[i], true,
        // extentedReport);
        // getquotepage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract, true,
        // extentedReport);
        // }
        // Add interested party and verify the intersted aprty section

        getquotepage.clickAddParties(extentedReport, true);
        getquotepage.selectContactType(addInterestContactType, extentedReport);

        getquotepage.AIenterLastName(testData.get("Last Name"), extentedReport);

        getquotepage.AIenterFirstName(testData.get("First Name"), extentedReport);

        getquotepage.clicksearch(extentedReport);

        getquotepage.checkAddPartySearchResultsPane(extentedReport, true);

        getquotepage.clickselect(extentedReport, true);

        getquotepage.addInterestedPartyType(extentedReport, true);

        // getquotepage.addInterestedPartyTypeToDate(extentedReport, true);

        getquotepage.addInterestedPartyClickSave(extentedReport, true);

        getquotepage.checkaddedInterestedParty(extentedReport, testData.get("Last Name"), true);

      } else {
        Log.fail("NB Policy has failed to Create in Accepted status, unable to procced with MTA",
            driver, extentedReport, true);
      } // Perform MTA

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();

    } // finally

  }// TC_F71

  @Test(
      description = "Check that the existing 'Interested Parties' are displayed under 'Search results' pane when filling the required details and click on 'Search' button in the 'Add Interested Party' modal",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_F76(String browser) throws Exception {

    String tcId = "TC_F76";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");

    String addInterestContactType = testData.get("AddInterestContactType");
    String brandname = testData.get("Brand Name");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP Contact Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);

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
      newquotepage.checkInterestedParties();
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("noInterestedpartiesMsg"),
              newquotepage),
          "No interested parties have been added--text visible",
          "No interested parties have been added-text has not visible", driver, extentedReport,
          true);
      newquotepage.clickAddParties(extentedReport, true);
      newquotepage.selectContactType(addInterestContactType, extentedReport);

      newquotepage.AIenterLastName(testData.get("Last Name"), extentedReport);

      newquotepage.AIenterFirstName(testData.get("First Name"), extentedReport);

      newquotepage.clicksearch(extentedReport);

      newquotepage.checkAddPartySearchResultsPane(extentedReport, true);

      newquotepage.clickselect(extentedReport, true);
      Log.pass(
          "Interested party search results are displayed, user able to select the contact as interested party",
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

  }

  @Test(
      description = "Check that 'Remove Interested Party' modal is displayed when clicking on 'bin' icon in the cover page",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_F78(String browser) throws Exception {

    String tcId = "TC_F78";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String addInterestContactType = testData.get("AddInterestContactType");
    String brandname = testData.get("Brand Name");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP Contact Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);

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
      newquotepage.checkInterestedParties();
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("noInterestedpartiesMsg"),
              newquotepage),
          "No interested parties have been added--text visible",
          "No interested parties have been added-text has not visible", driver, extentedReport,
          true);
      newquotepage.clickAddParties(extentedReport, true);
      newquotepage.selectContactType(addInterestContactType, extentedReport);

      newquotepage.AIenterLastName(testData.get("Last Name"), extentedReport);

      newquotepage.AIenterFirstName(testData.get("First Name"), extentedReport);

      newquotepage.clicksearch(extentedReport);

      newquotepage.checkAddPartySearchResultsPane(extentedReport, true);

      newquotepage.clickselect(extentedReport, true);

      newquotepage.addInterestedPartyType(extentedReport, true);

      // newquotepage.addInterestedPartyTypeToDate(extentedReport, true);

      newquotepage.addInterestedPartyClickSave(extentedReport, true);

      newquotepage.click_Deleteicon(extentedReport, true);

      newquotepage.checkremoveDescription();
      Log.pass(
          "Remove Interested Party' modal is displayed when clicking on 'bin' icon in the cover page",
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

  }// TC_F78

  @Test(
      description = "Check that  clicking on 'Yes' option in Remove Interested Party' modal removes the Interested Party with policy but retains the Contact",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_F81(String browser) throws Exception {

    String tcId = "TC_F81";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");

    String addInterestContactType = testData.get("AddInterestContactType");
    String brandname = testData.get("Brand Name");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP Contact Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);

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
      newquotepage.checkInterestedParties();
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("noInterestedpartiesMsg"),
              newquotepage),
          "No interested parties have been added--text visible",
          "No interested parties have been added-text has not visible", driver, extentedReport,
          true);
      newquotepage.clickAddParties(extentedReport, true);
      newquotepage.selectContactType(addInterestContactType, extentedReport);

      newquotepage.AIenterLastName(testData.get("Last Name"), extentedReport);

      newquotepage.AIenterFirstName(testData.get("First Name"), extentedReport);

      newquotepage.clicksearch(extentedReport);

      newquotepage.checkAddPartySearchResultsPane(extentedReport, true);

      newquotepage.clickselect(extentedReport, true);

      newquotepage.addInterestedPartyType(extentedReport, true);

      // newquotepage.addInterestedPartyTypeToDate(extentedReport, true);

      newquotepage.addInterestedPartyClickSave(extentedReport, true);

      newquotepage.click_Deleteicon(extentedReport, true);

      newquotepage.checkremoveDescription();

      newquotepage.yesnobuttonVisible(extentedReport, true);

      newquotepage.clickyesbutton(extentedReport, true);
      newquotepage.checkInterestedParties();

      Log.softAssertThat(
          !newquotepage.verify_IntPartyName(testData.get("Title") + " " + testData.get("First Name")
              + " " + testData.get("Last Name"), extentedReport),
          "Intersted party has been removed after clicking yes button in remove modal",
          "Interested party was not removed after clicking yes button in remove modal", driver,
          extentedReport, true);

      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally

  }// TC_F81

  @Test(
      description = "Check that  clicking on 'No' option in Remove Interested Party' modal does NOT remove the Interested Party",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_F82(String browser) throws Exception {

    String tcId = "TC_F82";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String addInterestContactType = testData.get("AddInterestContactType");
    String brandname = testData.get("Brand Name");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP Contact Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);

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
      newquotepage.checkInterestedParties();
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("noInterestedpartiesMsg"),
              newquotepage),
          "No interested parties have been added--text visible",
          "No interested parties have been added-text has not visible", driver, extentedReport,
          true);
      newquotepage.clickAddParties(extentedReport, true);
      newquotepage.selectContactType(addInterestContactType, extentedReport);

      newquotepage.AIenterLastName(testData.get("Last Name"), extentedReport);

      newquotepage.AIenterFirstName(testData.get("First Name"), extentedReport);

      newquotepage.clicksearch(extentedReport);

      newquotepage.checkAddPartySearchResultsPane(extentedReport, true);

      newquotepage.clickselect(extentedReport, true);

      newquotepage.addInterestedPartyType(extentedReport, true);

      // newquotepage.addInterestedPartyTypeToDate(extentedReport, true);

      newquotepage.addInterestedPartyClickSave(extentedReport, true);

      newquotepage.click_Deleteicon(extentedReport, true);

      newquotepage.checkremoveDescription();

      newquotepage.clicknobutton(extentedReport, true);
      newquotepage.checkaddedInterestedParty(testData.get("Last Name"));

      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally

  }// TC_F82

  @Test(
      description = "Check that  'Remove Interested Party' modal is closed when clicking on 'X' icon",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_F83(String browser) throws Exception {

    String tcId = "TC_F83";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String addInterestContactType = testData.get("AddInterestContactType");
    String brandname = testData.get("Brand Name");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" UXP Contact Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);

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
      newquotepage.checkInterestedParties();
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("noInterestedpartiesMsg"),
              newquotepage),
          "No interested parties have been added--text visible",
          "No interested parties have been added-text has not visible", driver, extentedReport,
          true);
      newquotepage.clickAddParties(extentedReport, true);
      newquotepage.selectContactType(addInterestContactType, extentedReport);

      newquotepage.AIenterLastName(testData.get("Last Name"), extentedReport);

      newquotepage.AIenterFirstName(testData.get("First Name"), extentedReport);

      newquotepage.clicksearch(extentedReport);

      newquotepage.checkAddPartySearchResultsPane(extentedReport, true);

      newquotepage.clickselect(extentedReport, true);

      newquotepage.addInterestedPartyType(extentedReport, true);

      // newquotepage.addInterestedPartyTypeToDate(extentedReport, true);

      newquotepage.addInterestedPartyClickSave(extentedReport, true);

      newquotepage.click_Deleteicon(extentedReport, true);

      newquotepage.checkremoveDescription();

      newquotepage.clickClosebutton(extentedReport, true);

      Log.softAssertThat(
          !newquotepage.uielement.verifyPageElements(Arrays.asList("closebtn"), newquotepage),
          "Remove interested party modal was closed when clicked on X button",
          "Remove interested party modal was not closed when clicked on X button", driver,
          extentedReport, true);

      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally

  }// TC_F83

  @Test(
      description = "Add corporate contact as interested party and verify the name interested party section of new quote page",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_F73(String browser) throws Exception {

    String tcId = "TC_F73";
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
      Log.pass(" UXP Contact Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.createCorporateContact(testData, extentedReport, true);
      CustDashboardPage custdashboardpage =
          searchPage.confirmCreateCustomer_CC(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      custdashboardpage.click_cust_compButton(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      testData = searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
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
      newquotepage.checkInterestedParties();
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("noInterestedpartiesMsg"),
              newquotepage),
          "No interested parties have been added--text visible",
          "No interested parties have been added-text has not visible", driver, extentedReport,
          true);
      newquotepage.clickAddParties(extentedReport, true);

      newquotepage.Select_InterstedParty(testData, extentedReport, true);

      newquotepage.search_lName_coporateIP(testData, extentedReport, true);

      newquotepage.clickselect(extentedReport, true);

      newquotepage.EnterIntPartype(extentedReport, true, true);

      newquotepage.IntParty_butsave(extentedReport, true);

      Log.softAssertThat(
          newquotepage.verify_IntPartyName(testData.get("Trade Name"), extentedReport),
          "Selected corporate contact interested party was displayed in newquote page as expected",
          "Selected corporate contact interested party was not displayed in newquote page", driver,
          extentedReport, true);


      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally

  }// TC_F73

  @Test(
      description = "Check that 'Personal' tab is active when 'create new interested party' button is selected while keeping 'personal contact' as 'Contact Type' dropdown - MTA",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_F89(String browser) throws Exception {

    String tcId = "TC_F89";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String addInterestContactType = testData.get("AddInterestContactType");
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
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status and MTA
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);

        custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport);
        Log.pass(" Clicked on MTA menu successfully", driver, extentedReport, true);
        // Fill up MTa data
        custdashboardpage.enterMTADetails(testData, extentedReport, true);

        Log.pass("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
        GetQuotePage getquotepage = new GetQuotePage(driver, extentedReport);
        getquotepage.verifyGetQuotePage(extentedReport);
        Log.pass("Get quote page loaded successfully", driver, extentedReport, true);
        String[] coversToAdd = testData.get("coverToSelect").toString().split(",");
        // Loop to add items to the existing ins cover
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

        // Add interested party and verify the intersted party section

        getquotepage.clickAddParties(extentedReport, true);
        getquotepage.selectContactType(addInterestContactType, extentedReport);

        getquotepage.AIenterLastName(testData.get("Last Name"), extentedReport);

        getquotepage.AIenterFirstName(testData.get("First Name"), extentedReport);

        getquotepage.clicksearch(extentedReport);

        getquotepage.clickcreatenewAddparty(extentedReport, true);
        // Verify corporate contact and personal contact tab

        Log.softAssertThat(getquotepage.verify_defaultContactTab("Personal", extentedReport, true),
            "Personal contact tab was selected by default",
            "Personal contact tab was not selected as default tab, when interested party contact type set to personal",
            driver, extentedReport, true);

      } else {
        Log.fail("NB Policy has failed to Create in Accepted status, unable to procced with MTA",
            driver, extentedReport, true);
      } // Perform MTA

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();

    } // finally

  }// TC_F89

  @Test(
      description = "Check that  'Corporate' tab is active when 'create new interested party' button is selected while keeping 'Corporate contact' as 'Contact Type' dropdown - MTA",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_F90(String browser) throws Exception {

    String tcId = "TC_F90";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String addInterestContactType = testData.get("AddInterestContactType");
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
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status and MTA
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);

        custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport);
        Log.pass(" Clicked on MTA menu successfully", driver, extentedReport, true);
        // Fill up MTa data
        custdashboardpage.enterMTADetails(testData, extentedReport, true);

        Log.pass("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
        GetQuotePage getquotepage = new GetQuotePage(driver, extentedReport);
        getquotepage.verifyGetQuotePage(extentedReport);
        Log.pass("Get quote page loaded successfully", driver, extentedReport, true);
        String[] coversToAdd = testData.get("coverToSelect").toString().split(",");
        // Loop to add items to the existing ins cover
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

        // Add interested party and verify the intersted aprty section

        getquotepage.clickAddParties(extentedReport, true);
        getquotepage.selectContactType(addInterestContactType, extentedReport);

        // To add corporate contact
        getquotepage.search_lName_coporateIP(testData, extentedReport, true);

        getquotepage.clickcreatenewAddparty(extentedReport, true);
        // Verify corporate contact and personal contact tab

        Log.softAssertThat(getquotepage.verify_defaultContactTab("Corporate", extentedReport, true),
            "Corporate contact tab was set to default tab",
            "Corporate contact tab was not selected as default tab, when interested party contact type set to corporate",
            driver, extentedReport, true);

      } else {
        Log.fail("NB Policy has failed to Create in Accepted status, unable to procced with MTA",
            driver, extentedReport, true);
      } // Perform MTA

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();

    } // finally

  }// TC_F90

  @Test(
      description = "Check that user is able to toggle between 'Personal' and 'Corporate' tabs in the Create New Interested Party moda - MTA",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_F91(String browser) throws Exception {

    String tcId = "TC_F91";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String addInterestContactType = testData.get("AddInterestContactType");
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
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status and MTA
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);

        custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport);
        Log.pass(" Clicked on MTA menu successfully", driver, extentedReport, true);
        // Fill up MTa data
        custdashboardpage.enterMTADetails(testData, extentedReport, true);

        Log.pass("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
        GetQuotePage getquotepage = new GetQuotePage(driver, extentedReport);
        getquotepage.verifyGetQuotePage(extentedReport);
        Log.pass("Get quote page loaded successfully", driver, extentedReport, true);
        String[] coversToAdd = testData.get("coverToSelect").toString().split(",");
        // Loop to add items to the existing ins cover
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

        // Add interested party and verify the intersted aprty section

        getquotepage.clickAddParties(extentedReport, true);
        getquotepage.selectContactType(addInterestContactType, extentedReport);

        getquotepage.AIenterLastName(testData.get("Last Name"), extentedReport);

        getquotepage.AIenterFirstName(testData.get("First Name"), extentedReport);

        getquotepage.clicksearch(extentedReport);

        getquotepage.clickcreatenewAddparty(extentedReport, true);
        // Verify corporate contact and personal contact tab

        Log.softAssertThat(getquotepage.verify_defaultContactTab("Personal", extentedReport, true),
            "Personal contact tab was selected by default",
            "Personal contact tab was not selected as default tab, when interested party contact type set to personal",
            driver, extentedReport, true);

        getquotepage.Click_contactTabs("Corporate", extentedReport, true);

        Log.softAssertThat(getquotepage.verify_defaultContactTab("Corporate", extentedReport, true),
            "User able to switch between personal and corporate contact tabs",
            "User not able to switch between personal and corporate contact tabs", driver,
            extentedReport, true);


      } else {
        Log.fail("NB Policy has failed to Create in Accepted status, unable to procced with MTA",
            driver, extentedReport, true);
      } // Perform MTA

      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();

    } // finally

  }// TC_F91


}
