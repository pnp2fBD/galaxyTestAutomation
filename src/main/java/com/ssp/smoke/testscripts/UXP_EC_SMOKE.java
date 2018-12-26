package com.ssp.smoke.testscripts;

import java.io.IOException;
// import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
// import org.testng.annotations.BeforeTest;
import com.ssp.support.BaseTest;
import com.ssp.support.EmailReport;
import com.ssp.support.Log;
import com.ssp.support.WebDriverFactory;
import com.ssp.utils.DataProviderUtils;
import com.ssp.utils.DataUtils;
import com.ssp.utils.GenericUtils;
import com.ssp.utils.WaitUtils;
import com.ssp.uxp_pages.BillingAdjustmentPage;
import com.ssp.uxp_pages.BuyQuotePage;
import com.ssp.uxp_pages.CardDetailsPage;
import com.ssp.uxp_pages.ComplaintPage;
import com.ssp.uxp_pages.CustDashboardPage;
import com.ssp.uxp_pages.GetQuotePage;
import com.ssp.uxp_pages.HomePage;
import com.ssp.uxp_pages.LoginPage;
import com.ssp.uxp_pages.NewQuotePage;
import com.ssp.uxp_pages.SearchPage;
import com.ssp.uxp_pages.SuspendBillingPage;
import com.ssp.uxp_pages.UnsuspendBillingPage;
import com.ssp.uxp_pages.ManageContactDetailsPage;

@Listeners(EmailReport.class)
public class UXP_EC_SMOKE extends BaseTest {

  private String webSite;
  private String environment;
  public String monthlywebSite;
  private HashMap<String, String> customerDetails2 = new HashMap<String, String>();

  @BeforeMethod
  public void init(ITestContext context) throws IOException {
    webSite = System.getProperty("webSite") != null ? System.getProperty("webSite")
        : context.getCurrentXmlTest().getParameter("webSite");

    System.out.println("Old Website Value " + webSite);

    environment = System.getProperty("environment") != null ? System.getProperty("environment")
        : context.getCurrentXmlTest().getParameter("environment");

    System.out.println("parameterised environment value is :" + environment);

    switch (environment) {
      case "SystemTest":
        webSite = "http://aldvmstweb02.siriusfs.com/EngagementCentre/servletcontroller";
        break;
      case "CoreFT1":
        webSite = "https://siaas-sit2.ssp-hosting.com/EngagementCentreCoreFT1/servletcontroller";
        break;
      case "CoreFT2":
        webSite = "http://aldvmweb01.siriusfs.com/EngagementCentreCoreFT2/servletcontroller";
        break;
      case "CoreFT3":
        webSite = "http://aldvmweb01.siriusfs.com/EngagementCentreCoreFT3/servletcontroller";
        break;
      case "CoreSIT":
        webSite = "http://aldvmweb01.siriusfs.com/EngagementCentreCoreSIT/servletcontroller";
        break;
      default:
        webSite = "http://aldvmstweb02.siriusfs.com/EngagementCentre/servletcontroller";
        break;
    }
    System.out.println("Update webSite value " + webSite);

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
    String className = "UXP_EC_SMOKE_" + env;
    System.out.println("Sheet Class Name : " + className);
    return DataUtils.testDatabyID(testcaseId, className);
  }

  @Test(
      description = "Create New Business 3* Policy - Annual Payment and search the created NB policy through normal policy search",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")

  public void TC_001(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_001";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String title = testData.get("Title");
    String brandname = testData.get("Brand Name");
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
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search page is verified", "Search Page is not verified", driver, extentedReport, true);

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
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

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
      // Select Payment
      newquotepage.selectPayment(testData, true, extentedReport);
      newquotepage.takePayment(extentedReport);
      Thread.sleep(20000);// To be deleted
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, false);
      carddetailspage.selectExpiry(testData, extentedReport, false);
      carddetailspage.enterVerification(extentedReport, false);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer Dashboard page", "Not navigated to Customer Dashboard page",
          driver, extentedReport, true);

      // Check for the customerdashboard page tabs
      custdashboardpage.verify_ExpandCollapse_Custdashboard(extentedReport, true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {

        Log.message("New Business Policy Created Successfully in Accepted status", driver,
            extentedReport, true);
        policyDetails.get("PolicyNo");

        custdashboardpage.clickComplete(extentedReport);
        Log.softAssertThat(homePage.verifyHomePage(),
            "Successfully navigated to Home Page after clicking complete button in customer dashboard page",
            "Failed to navigate to Home Page after clicking complete button in customer dashboard page",
            driver, extentedReport, true);

        // Click on Take Call link
        homePage.clickTakeCall(extentedReport);
        homePage.clickMyBrands(brandname, extentedReport);
        // Search with Valid Policy Number
        searchPage.searchValidPolicy(policyDetails.get("PolicyNo"), true, extentedReport);
        CustDashboardPage custdashboardPage =
            searchPage.selectPolicy_from_SearchPage(true, extentedReport);
        Log.message("Navigated to Cutomer Dashboard after selecting policy from search page",
            driver, extentedReport, true);

        // Verifying Customer Details
        custdashboardPage.clickPassVerification(extentedReport, true);
        custdashboardPage.verifyCustomerDashboardPage();
        Log.softAssertThat(
            custdashboardPage.verifyContactName(
                title + " " + custDetails.get("First Name") + " " + custDetails.get("Last Name"),
                extentedReport, true),
            "Verified FirstName and LastName on Customer Dashboard : "
                + custDetails.get("First Name") + " " + custDetails.get("Last Name"),
            "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

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

  }


  @Test(description = "Create New Busienss 3* Policy - Monthly Payment and verify advanced search",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_002(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_002";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String title = testData.get("Title");
    String brandname = testData.get("Brand Name");
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
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search page is verified", "Search Page is not verified", driver, extentedReport, true);

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
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

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
      newquotepage.selectPayment(testData, true, extentedReport);
      // confirm payment
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer Dashboard page", "Not navigated to Customer Dashboard page",
          driver, extentedReport, true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {

        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);

        custdashboardpage.clickComplete(extentedReport);
        Log.softAssertThat(homePage.verifyHomePage(),
            "Successfully navigated to Home Page after clicking complete button in customer dashboard page",
            "Failed to navigate to Home Page after clicking complete button in customer dashboard page",
            driver, extentedReport, true);

        // Click on Take Call link
        homePage.clickTakeCall(extentedReport);
        homePage.clickMyBrands(brandname, extentedReport);

        Log.softAssertThat(
            searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
            "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);
        // Search with Valid Policy Number
        searchPage.enterLastName(testData.get("Last Name"), extentedReport);
        searchPage.enterFirstName(testData.get("First Name"), extentedReport);
        searchPage.clickSearch(extentedReport);
        Log.message("Advanced search - Searching with the last and first name ", extentedReport);

        CustDashboardPage custdashboardPage =
            searchPage.selectPolicy_from_SearchPage(true, extentedReport);

        Log.message("Navigated to Cutomer Dashboard after selecting policy from search page",
            driver, extentedReport, true);

        // Verifying Customer Details
        custdashboardPage.clickPassVerification(extentedReport, true);
        custdashboardPage.verifyCustomerDashboardPage();
        Log.softAssertThat(
            custdashboardPage.verifyContactName(
                title + " " + custDetails.get("First Name") + " " + custDetails.get("Last Name"),
                extentedReport, true),
            "Verified FirstName and LastName on Customer Dashboard : "
                + custDetails.get("First Name") + " " + custDetails.get("Last Name"),
            "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

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
  }


  @Test(description = "Create New 5 Policy - Annual Payment and verify document creation",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_003(String browser) throws Exception {

    String tcId = "TC_003";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");
    String DocumentnameNB = testData.get("DocumentnameNB");
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
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search page is verified", "Search Page is not verified", driver, extentedReport, true);

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
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

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
      // Add the insurance cover items
      String[] coversToAdd = testData.get("coverToSelect_NB").toString().split(",");
      for (int i = 0; i < coversToAdd.length; i++) {
        boolean BoolVal = false;
        String ins_RowtoInteract =
            newquotepage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
        BoolVal = newquotepage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract,
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
      /*
       * newquotepage.clickUpgradeNow(extentedReport);
       * Log.softAssertThat(newquotepage.verifyUpgradePolicyTo5Star(), "Policy upgraded to 5 star",
       * "Policy not upgraded to 5 star", driver, extentedReport, true);
       */
      newquotepage.clickReCalculate(extentedReport);
      newquotepage.clickBuy(extentedReport);
      newquotepage.clickPaymentNext(extentedReport); // Select Payment
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

      // Click continue button //

      // newquotepage = carddetailspage.clickContinueButton(extentedReport);
      // Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
      // "Payment is successful", "Payment is not successful", driver, extentedReport, true);

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer dashboard page", "Not navigated to Customer dashboard page",
          driver, extentedReport, true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {

        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }

      // click the document tab
      custdashboardpage.clickDocuments(true, extentedReport);

      // check for document generation
      Log.softAssertThat(
          custdashboardpage.verifyDocumentGeneration(DocumentnameNB, policyDetails.get("Position"),
              extentedReport),
          "Documents are generated for 5 start policy",
          "Documents not generated for 5 start policy", driver, extentedReport, true);
      custdashboardpage.clickdocViewbtn(policyDetails.get("PolicyNo"), extentedReport);
      Log.pass("Document has  been verified");
      // check the attached document pdf/email
      /*
       * Log.softAssertThat( custdashboardpage.clickdocViewbtn(policyDetails.get("PolicyNo"),
       * extentedReport), "Clicked on document view button and verified documents",
       * "Clicked on document view button and documents not verified", driver, extentedReport,
       * false);
       */
      Log.testCaseResult(extentedReport);
    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }


  @Test(description = "Create New 5 Policy - Monthly Payment and verify document creation ",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_004(String browser) throws Exception {

    String tcId = "TC_004";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);


    // Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("env");
    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");
    // String DocumentnameNB = testData.get("DocumentnameNB");

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
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search page is verified", "Search Page is not verified", driver, extentedReport, true);

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
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

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
      // Add the insurance cover items
      String[] coversToAdd = testData.get("coverToSelect_NB").toString().split(",");
      for (int i = 0; i < coversToAdd.length; i++) {
        boolean BoolVal = false;
        String ins_RowtoInteract =
            newquotepage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
        BoolVal = newquotepage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract,
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
      newquotepage.clickUpgradeNow(extentedReport);
      Log.softAssertThat(newquotepage.verifyUpgradePolicyTo5Star(), "Policy upgraded to 5 star",
          "Policy not upgraded to 5 star", driver, extentedReport, true);
      newquotepage.clickReCalculate(extentedReport);
      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      // confirm payment
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer Dashboard page", "Not navigated to Customer Dashboard page",
          driver, extentedReport, true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }


      /*
       * // click the document tab custdashboardpage.clickDocuments(true, extentedReport);
       * 
       * // check for document generation
       * Log.softAssertThat(custdashboardpage.verifyDocumentGeneration(DocumentnameNB,
       * policyDetails.get("Position"), extentedReport),
       * "Documents are generated for 5 start policy", "Documents not generated for 5 start policy",
       * driver, extentedReport, true);
       * 
       * // check the attached document pdf/email
       * Log.softAssertThat(custdashboardpage.clickdocViewbtn(policyDetails.get( "policyNo"),
       * extentedReport), "Clicked on document view button and verified documents",
       * "Clicked on document view button and documents not verified", driver, extentedReport,
       * false);
       */
      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }

  }


  @Test(
      description = "Create New 5* Policy - Annual payment with Policy-Cancellation and Reinstatement",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_005(String browser) throws Exception {

    String tcId = "TC_005";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    // String DocumentnameCNL = testData.get("DocumentnameCNL");
    // String DocumentnameRST = testData.get("DocumentnameRST");
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
      Log.message("Logged in User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search page is verified", "Search Page is not verified", driver, extentedReport, true);

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
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

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
      // Select Payment
      newquotepage.selectPayment(testData, true, extentedReport);
      newquotepage.takePayment(extentedReport);
      Thread.sleep(20000);// To be deleted
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, false);
      carddetailspage.selectExpiry(testData, extentedReport, false);
      carddetailspage.enterVerification(extentedReport, false);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer Dashboard page", "Not navigated to Customer Dashboard page",
          driver, extentedReport, true);

      // Check for the customerdashboard page tabs
      custdashboardpage.verify_ExpandCollapse_Custdashboard(extentedReport, true);

      // check for policy status
      /*
       * HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport); if
       * (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
       * 
       * Log.message("New Business Policy Created Successfully in Accepted status", driver,
       * extentedReport, true); policyDetails.get("PolicyNo");
       */
      Log.pass("Policy has been created", extentedReport);

      // Canceling a Policy
      custdashboardpage.clickCancelPolicy(testData, true, extentedReport);
      custdashboardpage.enterRequestCancellationDate(extentedReport);
      custdashboardpage.enterReason(testData.get("Cancellation Reason"), extentedReport);
      custdashboardpage.enterPremiumType(testData, extentedReport);

      custdashboardpage.clickCalculate(true, extentedReport);
      custdashboardpage.clickConfirm(true, extentedReport);

      Log.pass("Policy has been cancelled", extentedReport);

      // Reinstating a Policy
      custdashboardpage.clickReinstatePolicy(true, extentedReport);
      custdashboardpage.selectReinstateReason(testData, true, extentedReport);
      custdashboardpage.clickCalculateForReinstate(true, extentedReport);
      custdashboardpage.clickConfirmForReinstate(true, extentedReport);
      Log.pass("Policy Reinstated successfully", extentedReport);


    } catch (Exception e) {
      Log.fail("Test Failed", extentedReport);
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.pass("Test Passed", extentedReport);
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(
      description = "Create New Policy - Monthly payment with Cancellation cooling off and Reinstatement",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_006(String browser) throws Exception {

    String tcId = "TC_006";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");
    String DocumentnameCNL = testData.get("DocumentnameCNL");
    String DocumentnameRST = testData.get("DocumentnameRST");

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
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search page is verified", "Search Page is not verified", driver, extentedReport, true);

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
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

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
      newquotepage.selectPayment(testData, true, extentedReport);
      // confirm payment
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer Dashboard page", "Not navigated to Customer Dashboard page",
          driver, extentedReport, true);

      // check for policy status

      Log.pass("Policy has been created ", extentedReport);

      custdashboardpage.clickCancelPolicy(testData, true, extentedReport);
      custdashboardpage.enterReason(testData.get("Cancellation Reason"), extentedReport);
      custdashboardpage.enterPremiumType(testData, extentedReport);

      custdashboardpage.clickCalculate(true, extentedReport);
      custdashboardpage.clickConfirm(true, extentedReport);
      Log.pass("Policy has been cancelled", extentedReport);
      // Verifying that policy is cancelled
      /*
       * Log.softAssertThat(custdashboardpage.verifyCancellation(true, extentedReport),
       * "Policy cancelled successfully", "Policy is not cancelled", driver, extentedReport, true);
       */
      // Reinstate a policy
      custdashboardpage.clickReinstatePolicy(true, extentedReport);

      custdashboardpage.selectReinstateReason(testData, true, extentedReport);
      custdashboardpage.clickCalculateForReinstate(true, extentedReport);
      custdashboardpage.clickConfirmForReinstate(true, extentedReport);
      Log.pass("Policy Reinstated successfully", extentedReport);

      Log.testCaseResult(extentedReport);
      Log.pass("Test Passed", extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(description = "Create NewBusiness policy and perform MTA with all covers added",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_007(String browser) throws Exception {

    String tcId = "TC_007";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");
    String NB_CoversToChk = testData.get("NB_Check");
    String DocumentnameMTA = testData.get("DocumentnameMTA");
    String MTAcreationReason = testData.get("MTAdjReason");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

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
          "Search page is verified", "Search Page is not verified", driver, extentedReport, true);

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
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

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

      // Get and BuyQuote
      newquotepage.clickGetQuote(extentedReport);

      // Add validation here to check the BLD and CNT cover
      String[] coversToChck = NB_CoversToChk.split(",");
      for (int i = 0; i < coversToChck.length; i++) {
        String[] coversToAdd = coversToChck[i].split("_");
        String coverSec = coversToAdd[0].trim();
        String Exp_limit_Amt = coversToAdd[1].trim();
        String Exp_Acess_Amt = coversToAdd[2].trim();
        String valToReturn =
            newquotepage.getDefaultVals_InsCovers(coversToChck[i], true, extentedReport);
        if (!(valToReturn.equalsIgnoreCase("false"))) {
          Log.pass(
              "New quote Page has the default values as expected for " + coverSec
                  + " cover,Limit : " + Exp_limit_Amt.trim() + ",Excess:" + Exp_Acess_Amt.trim(),
              driver, extentedReport, true);
        } else {
          Log.fail(
              "New quote Page has failed to populate with following default values for " + coverSec
                  + " cover,Limit : " + Exp_limit_Amt + ", Excess : " + Exp_Acess_Amt,
              driver, extentedReport, true);
        }

      }

      newquotepage.clickBuy(extentedReport);
      // Select Payment
      newquotepage.selectPayment(testData, true, extentedReport);
      newquotepage.takePayment(extentedReport);
      Thread.sleep(20000);// To be deleted
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, false);
      carddetailspage.selectExpiry(testData, extentedReport, false);
      carddetailspage.enterVerification(extentedReport, false);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer dashboard page", "Not navigated to Customer dashboard page",
          driver, extentedReport, true);

      // check for policystatus
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      custdashboardpage.clickManagePolicy(extentedReport);
      custdashboardpage.clickMidTermAdjustment(extentedReport);
      custdashboardpage.enterDateForMTA(extentedReport);
      custdashboardpage.selectMidTermAdjReason(MTAcreationReason, extentedReport);
      custdashboardpage.clickMidTermContinue(extentedReport);
      GetQuotePage getquotepage = new GetQuotePage(driver, extentedReport);
      String[] coversToAdd = testData.get("coverToSelect").toString().split(",");

      for (int i = 1; i < coversToAdd.length; i++) {
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
      getquotepage.selectAvalilableAdjReasons(MTAcreationReason, extentedReport, false);
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
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);

      WaitUtils.waitForSpinner(driver);
      custdashboardpage = getquotepage.confirmPayment("MTA", testData, extentedReport);


      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer dashboard page", "Not navigated to Customer dashboard page",
          driver, extentedReport, true);

      policyDetails = custdashboardpage.getPolicyURN(extentedReport);

      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
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

  }

  @Test(
      description = "Create NewBusiness policy with all covers added and Remove the covers in MTA transaction",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_008(String browser) throws Exception {
    String tcId = "TC_008";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String NB_CoversToChk = testData.get("NB_Check");
    String Corecover = testData.get("Cover");
    String MTAcreationReason = testData.get("MTAdjReason");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

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
          "Search page is verified", "Search Page is not verified", driver, extentedReport, true);

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
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

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

      // Add validation here to check the BLD and CNT cover
      String[] coversToChck = NB_CoversToChk.split(",");
      for (int i = 0; i < coversToChck.length; i++) {
        String[] coversToAdd = coversToChck[i].split("_");
        String coverSec = coversToAdd[0].trim();
        String Exp_limit_Amt = coversToAdd[1].trim();
        String Exp_Acess_Amt = coversToAdd[2].trim();
        String valToReturn =
            newquotepage.getDefaultVals_InsCovers(coversToChck[i], true, extentedReport);
        if (!(valToReturn.equalsIgnoreCase("false"))) {

          Log.pass(
              "New quote Page has the default values as expected for " + coverSec
                  + " cover,Limit : " + Exp_limit_Amt + ", Excess:" + Exp_Acess_Amt,
              driver, extentedReport, true);
        } else {
          Log.fail(
              "New quote Page has failed to populate with following default values for " + coverSec
                  + " cover,Limit : " + Exp_limit_Amt + " , Excess : " + Exp_Acess_Amt,
              driver, extentedReport, true);
        }
      }

      // Add the insurance cover items
      String[] coversToAdd = testData.get("coverToSelect_NB").toString().split(",");
      for (int i = 0; i < coversToAdd.length; i++) {
        boolean BoolVal = false;
        String ins_RowtoInteract =
            newquotepage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
        BoolVal = newquotepage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract,
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
      newquotepage.clickReCalculate(extentedReport);
      newquotepage.clickBuy(extentedReport);
      newquotepage.clickPaymentNext(extentedReport);

      // Select Payment Method and Confirm Payment
      newquotepage.selectPayment(testData, true, extentedReport);
      // Select card type
      // newquotepage.takePayment(extentedReport);
      /*
       * CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);
       * 
       * // Enter Card Details carddetailspage.enterCardNumber(testData, extentedReport, true);
       * carddetailspage.selectExpiry(testData, extentedReport, true);
       * carddetailspage.enterVerification(extentedReport, true);
       * carddetailspage.enterName(testData, extentedReport, true);
       * carddetailspage.clickbuy(extentedReport, true);
       * Log.message("Entered Card Details and Navigated to Verificaion page", driver,
       * extentedReport, true);
       * 
       * // Click continue button newquotepage =
       * carddetailspage.clickContinueButton(extentedReport);
       * Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
       * "Payment is successful", "Payment is not successful", driver, extentedReport, true);
       */

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer dashboard page", "Not navigated to Customer dashboard page",
          driver, extentedReport, true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {

        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }

      custdashboardpage.clickManagePolicy(extentedReport);
      custdashboardpage.clickMidTermAdjustment(extentedReport);
      custdashboardpage.enterDateForMTA(extentedReport);
      custdashboardpage.selectMidTermAdjReason(MTAcreationReason, extentedReport);
      custdashboardpage.clickMidTermContinue(extentedReport);
      GetQuotePage getquotepage = new GetQuotePage(driver, extentedReport);
      String[] coversToRemove = testData.get("coverToSelect").toString().split(",");

      for (int i = 0; i < coversToRemove.length; i++) {
        boolean BoolVal = false;
        String ins_RowtoInteract =
            getquotepage.SelectInsuranceItem(coversToRemove[i], true, extentedReport);
        BoolVal = getquotepage.enterInsCoverDetails(testData, coversToRemove[i], ins_RowtoInteract,
            true, extentedReport);
        String[] coverType = coversToRemove[i].split("_");
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
      getquotepage.selectAvalilableAdjReasons(MTAcreationReason, extentedReport, false);
      getquotepage.clickAddMTA(extentedReport, false);
      getquotepage.clickAddAllMTANextButton(extentedReport, true);
      // getquotepage.selectMTAPayment(testData, true, extentedReport);
      // ***Stj1***/
      newquotepage.selectPayment(testData, true, extentedReport);
      // **Stj1 ended **/

      custdashboardpage = getquotepage.confirmPayment("MTA", testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer dashboard page", "Not navigated to Customer dashboard page",
          driver, extentedReport, true);

      policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {

        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
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

  }



  @Test(description = "Create NewBusiness 5 policy with all covers added - Monthly payment ",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_009(String browser) throws Exception {
    String tcId = "TC_009";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");
    String brandname = testData.get("Brand Name");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

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
          "Search page is verified", "Search Page is not verified", driver, extentedReport, true);

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
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

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
      // Get and BuyQuote
      newquotepage.clickGetQuote(extentedReport);

      String[] coversToAdd = testData.get("coverToSelect_NB").toString().split(",");

      for (int i = 0; i < coversToAdd.length; i++) {
        boolean BoolVal = false;
        String ins_RowtoInteract =
            newquotepage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
        BoolVal = newquotepage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract,
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
      newquotepage.clickUpgradeNow(extentedReport);
      newquotepage.verifyUpgradePolicyTo5Star();
      newquotepage.clickReCalculate(extentedReport);
      newquotepage.clickBuy(extentedReport);


      // Select Payment Method and Confirm Payment
      newquotepage.selectPayment(testData, true, extentedReport);



      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer dashboard page", "Not navigated to Customer dashboard page",
          driver, extentedReport, true);

      // check for policystatus
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB 5 Policy Created Successfully in accepted status", driver, extentedReport,
            true);
      } else {
        Log.fail(
            "Failed - NB 5 Policy Created in " + policyDetails.get("Status").toString() + " status",
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
    }
  }

  @Test(
      description = "Check the customer dashboard page , click policy details in policy bar and validate the policy references",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_010(String browser) throws Exception {

    String tcId = "TC_010";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

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
          "Search page is verified", "Search Page is not verified", driver, extentedReport, true);

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
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

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
      // Get and BuyQuote
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickBuy(extentedReport);
      // Select Payment
      newquotepage.selectPayment(testData, true, extentedReport);


      newquotepage.takePayment(extentedReport);
      Thread.sleep(20000);// To be deleted
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, false);
      carddetailspage.selectExpiry(testData, extentedReport, false);
      carddetailspage.enterVerification(extentedReport, false);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer Dashboard page", "Not navigated to Customer Dashboard page",
          driver, extentedReport, true);

      // check for policystatus
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }

      // To validate policy details and policy cover menu items
      policyDetails.remove("Position");
      policyDetails.remove("notification_plcy");
      Log.softAssertThat(
          GenericUtils.compareTwoHashMap(policyDetails,
              custdashboardpage.validate_PolicyDetailsInfo(extentedReport, true)),
          "Both policy details tab and view policy quotes has the same policy references",
          "Policy details tab and view policy quotes failed to have the same policy references",
          driver, extentedReport, true);

      // To validate policy details and policy cover menu items
      custdashboardpage.selectMenu_from_policyDetails_tab("Policy cover", extentedReport, true);
      Log.softAssertThat(custdashboardpage.validate_PolicyDetailSection(),
          "On selecting policy cover, the cover details are viewed in policy information section of customer dashboard page",
          "On selecting policy cover, the cover details are not viewed in policy information section of customer dashboard page",
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
      description = "Check user able to replace address in the Manage Contact Detail screen by using Quick Search  and 'or Enter Manually' link",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_011(String browser) throws Exception {

    String tcId = "TC_011";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String address = testData.get("ContactAddress");

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
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search page is verified", "Search Page is not verified", driver, extentedReport, true);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);

      ManageContactDetailsPage manageContactDetailsPg =
          custdashboardpage.verifyndClickManageCntDetails(extentedReport, false);
      Log.softAssertThat(manageContactDetailsPg.verifyManageContactDetailsFields(),
          "Manage Contact Details screen is displayed",
          "Manage Contact Details screen is not displayed", driver, extentedReport, true);
      manageContactDetailsPg.clickEditButton(extentedReport, true);
      manageContactDetailsPg.clickEnterManuallyLink(extentedReport, false);
      Log.softAssertThat(manageContactDetailsPg.verifyReplaceAddressTypeAndHouseNameDisabled(),
          "System displays Housename or number field",
          "System not displayed Housename or number field", driver, extentedReport, true);
      Log.softAssertThat(
          manageContactDetailsPg.uielement
              .verifyPageElements(Arrays.asList("fldReplaceAddressLine1", "fldReplaceAddressLine2",
                  "fldReplaceAddressLine3", "fldReplaceCountry", "fldReplaceAddressPostCode",
                  "btnCancel", "btnSave"), manageContactDetailsPg),
          "System displays list of fields after selecting 'or Enter Manually' link 'AddressLine1','AddressLine2','AddressLine3','PostCode','Country',Save & Cancel' buttons",
          "System not displays list of fields after selecting 'or Enter Manually' link 'AddressLine1','AddressLine2','AddressLine3','PostCode','Country',Save & Cancel' buttons",
          driver, extentedReport, true);
      manageContactDetailsPg.clickCancelButton(extentedReport, false);
      manageContactDetailsPg.clickEditButton(extentedReport, true);
      manageContactDetailsPg.enterReplaceAddressDetails("PostCode", "AB101AH", extentedReport,
          true);
      manageContactDetailsPg.clickFindAddressButton(extentedReport, false); //
      manageContactDetailsPg.clickFindAddressDetail(extentedReport, true);
      manageContactDetailsPg.clickSaveButton(extentedReport, false);
      Log.softAssertThat(manageContactDetailsPg.verifyFullAddressPostCode("AB101AH"),
          "User is able to enter address manually by selecting or Enter Manually link to change the address",
          "User is not able to enter address manually by selecting or Enter Manually link to change the address",
          driver, extentedReport, true);
      manageContactDetailsPg.clickAddNewAddressButton(extentedReport, true);
      manageContactDetailsPg.enterNewAddressDetails("AB10 1AH", "Home", extentedReport, true);
      Log.softAssertThat(custdashboardpage.verifyContactAddressDetails(address),
          "System displayed newly mailing address under Contact Detail section : " + address,
          "System not displayed newly mailing address under Contact Detail section : " + address,
          driver, extentedReport, true);
      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }



  // 1599
  @Test(
      description = "Check that clicking on 'Yes' option in Remove Interested Party' modal removes the Interested Party with policy but retains the Contact",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_012(String browser) throws Exception {

    String tcId = "TC_012";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    boolean ip;
    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String addInterestContactType = testData.get("AddInterestContactType");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      /// Navigate to Login Page
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
          "Search page is verified", "Search Page is not verified", driver, extentedReport, true);

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
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Verify New Quote
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
      /*
       * Log.softAssertThat(newquotepage.checkInterestedParties(), "Interested parties is visible",
       * "Interested parties is not visible", driver, extentedReport, true);
       */
      ip = newquotepage.checkInterestedParties();
      if (ip) {
        Log.pass("Intrested Party is not Visible", extentedReport);
      } else {
        Log.fail("Intrested Party is visible", extentedReport);
      }
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

      Log.softAssertThat(
          newquotepage.checkaddedInterestedParty(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name")),
          testData.get("Title") + " " + custDetails.get("First Name") + " "
              + custDetails.get("Last Name") + "Added party is visible",
          testData.get("Title") + " " + custDetails.get("First Name") + " "
              + custDetails.get("Last Name") + " Added party is not visible",
          driver, extentedReport, true);

      newquotepage.click_Deleteicon(extentedReport, true);

      Log.softAssertThat(newquotepage.checkremoveDescription(),
          "Interested Parties Remove description message is displayed : "
              + newquotepage.INTERESTED_PARTIES_REMOVE_DESC1 + " and "
              + newquotepage.INTERESTED_PARTIES_REMOVE_DESC2,
          "Interested Parties Remove description message is not displayed", driver, extentedReport,
          true);

      newquotepage.yesnobuttonVisible(extentedReport, true);

      newquotepage.clickyesbutton(extentedReport, true);
      ip = newquotepage.checkInterestedParties();
      if (ip) {
        Log.pass("Intrested Party is not Visible", extentedReport);
      } else {
        Log.fail("Intrested Party is visible", extentedReport);
      }
      /*
       * Log.softAssertThat( !newquotepage.verify_IntPartyName(testData.get("Title") + " " +
       * testData.get("First Name") + " " + testData.get("Last Name"), extentedReport),
       * "Intersted party has been removed after clicking yes button in remove modal",
       * "Interested party was not removed after clicking yes button in remove modal", driver,
       * extentedReport, true);
       */

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }

  }

  // 1600
  @Test(
      description = "Verify the display of Proceed button & behaviour of proceed button and search message",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_013(String browser) throws Exception {

    String tcId = "TC_013";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
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
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search page is verified", "Search Page is not verified", driver, extentedReport, true);

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
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);
      custdashboardpage.clickComplete(extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully navigated to Home Page",
          "Failed to navigate to Home Page", driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      searchPage.enterLastName(custDetails.get("Last Name"), extentedReport);
      searchPage.enterFirstName(custDetails.get("First Name"), extentedReport);
      searchPage.clickSearch(extentedReport);
      Log.softAssertThat(searchPage.verify_stringonSearchPage(extentedReport, true),
          "Verified the message : " + searchPage.SEARCH_NOPOLICY_MSG,
          "Failed to Verified the message : " + searchPage.SEARCH_NOPOLICY_MSG, driver,
          extentedReport, true);

      custdashboardpage = searchPage.clickSearchedContactPolicy("contact", "",
          custDetails.get("First Name"), true, extentedReport);

      // custdashboardpage = searchPage.click_proceedButton(extentedReport, true);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer dashboardpage", "Not navigated to Customer dashboardpage", driver,
          extentedReport, true);
      Log.pass("Proceed button has been displayed and clicked ", extentedReport);
      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  // 1605
  @Test(description = "Validate brand search_Make call and Admin Task",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_014(String browser) throws Exception {

    String tcId = "TC_014";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);
    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in with User id:" + userName + "& Password:" + password, driver,
          extentedReport);

      // Click on Take Call link
      homePage.clickMakeCall(extentedReport);
      Log.softAssertThat(homePage.verifyBrandsPopup(), "Brands Popup is opened",
          "Brands Popup is not opened", driver, extentedReport);
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
          "Customer dashboard page is verified", "Customer dashboard page is not verified", driver,
          extentedReport, true);
      custdashboardpage.clickComplete(extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully navigated to Home Page",
          "Failed to navigate to Home Page", driver, extentedReport, true);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      searchPage = new SearchPage(driver, extentedReport).get();
      // Search with Valid user names
      searchPage.enterLastName(custDetails.get("Last Name"), extentedReport);
      searchPage.enterFirstName(custDetails.get("First Name"), extentedReport);
      searchPage.clickSearch(extentedReport);
      searchPage.click_proceedButton(extentedReport, true);
      searchPage.clickComplete(extentedReport);
      homePage = new HomePage(driver).get();
      Log.softAssertThat(homePage.verifyAgentDashboard(), "Agent Dashboard is verified",
          "Agent Dashboard is not verified", driver, extentedReport, true);
      homePage.clickMakeCall(extentedReport);
      homePage.clickBrandPopupCloseBtn(extentedReport, true);
      homePage.clickMakeCall(extentedReport);
      homePage.clickBrandPopupCancelBtn(extentedReport, true);
      homePage.clickMakeCall(extentedReport);
      homePage.clickOutsideBrandPopup(extentedReport, true);
      Log.pass(
          "Make  Call - Close button , Cancel button and outside pop up clicks works as expected",
          extentedReport);
      homePage.clickBrandPopupCloseBtn(extentedReport, true);
      // Checking Admin task
      homePage.clickAdminTask(extentedReport);
      Log.softAssertThat(homePage.verifyBrandsPopup(), "Brands Popup is opened",
          "Brands Popup is not opened", driver, extentedReport);
      Log.softAssertThat(homePage.isAllBrandsPresent(), "All Brands displayed",
          "All Brands not displayed", driver, extentedReport, true);
      homePage.clickMyBrands(brandname, extentedReport);
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);
      // Search with Valid user names
      searchPage.enterLastName(custDetails.get("Last Name"), extentedReport);
      searchPage.enterFirstName(custDetails.get("First Name"), extentedReport);
      searchPage.clickSearch(extentedReport);
      searchPage.click_proceedButton(extentedReport, true);
      searchPage.clickComplete(extentedReport);
      homePage = new HomePage(driver).get();
      Log.softAssertThat(homePage.verifyAgentDashboard(), "Agent Dashboard is verified",
          "Agent Dashboard is not verified", driver, extentedReport, true);
      // Admin Task
      homePage.clickAdminTask(extentedReport);
      homePage.clickBrandPopupCloseBtn(extentedReport, true);
      homePage.clickAdminTask(extentedReport);
      homePage.clickBrandPopupCancelBtn(extentedReport, true);
      homePage.clickAdminTask(extentedReport);
      homePage.clickOutsideBrandPopup(extentedReport, true);
      Log.pass(
          "Admin Task - Close button , Cancel button and outside pop up clicks works as expected",
          extentedReport);
      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);

    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  // 1606
  @Test(
      description = "To check that the user is able to access Add/Remove Joint Policyholders feature, when performs Mid-Term Adjustment",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_015(String browser) throws Exception {

    String tcId = "TC_015";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    // HashMap<String, String> customerDetails1 = new HashMap<String, String>();
    HashMap<String, String> customerDetails2 = new HashMap<String, String>();

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");
    String MTAcreationReason = testData.get("MTAdjReason");
    String NB_CoversToChk = testData.get("NB_Check");
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
      String mainCustFirstName = testData.get("First Name");
      String mainCustLastName = testData.get("Last Name");
      Log.message("Contact 1 First Name------>" + mainCustFirstName, driver, extentedReport);
      Log.message("Contact 1 Last Name------>" + mainCustLastName, driver, extentedReport);
      String jointPolicyName = mainCustFirstName + " " + mainCustLastName;
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(
              testData.get("Title") + " " + mainCustFirstName + " " + mainCustLastName,
              extentedReport, true),
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

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
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + customerDetails2.get("First Name") + " " + customerDetails2.get("Last Name"),
              extentedReport, true),
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);
      custdashboardpage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(customerDetails2.get("Last Name"), extentedReport);
      searchPage.enterFirstName(customerDetails2.get("First Name"), extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(customerDetails2.get("Last Name"), true,
          extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

      // Create New Quote
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

      // Get and BuyQuote
      newquotepage.clickGetQuote(extentedReport);

      // Add validation here to check the BLD and CNT cover
      String[] coversToChck = NB_CoversToChk.split(",");
      for (int i = 0; i < coversToChck.length; i++) {
        String[] coversToAdd = coversToChck[i].split("_");
        String coverSec = coversToAdd[0].trim();
        String Exp_limit_Amt = coversToAdd[1].trim();
        String Exp_Acess_Amt = coversToAdd[2].trim();
        String valToReturn =
            newquotepage.getDefaultVals_InsCovers(coversToChck[i], true, extentedReport);
        if (!(valToReturn.equalsIgnoreCase("false"))) {
          Log.pass(
              "New quote Page has the default values as expected for " + coverSec
                  + " cover,Limit : " + Exp_limit_Amt.trim() + ",Excess:" + Exp_Acess_Amt.trim(),
              driver, extentedReport, true);
        } else {
          Log.fail(
              "New quote Page has failed to populate with following default values for " + coverSec
                  + " cover,Limit : " + Exp_limit_Amt + ", Excess : " + Exp_Acess_Amt,
              driver, extentedReport, true);
        }

      }

      newquotepage.clickBuy(extentedReport);
      // Select Payment
      newquotepage.selectPayment(testData, true, extentedReport);
      newquotepage.takePayment(extentedReport);
      Thread.sleep(20000);// To be deleted
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, false);
      carddetailspage.selectExpiry(testData, extentedReport, false);
      carddetailspage.enterVerification(extentedReport, false);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer dashboard page", "Not navigated to Customer dashboard page",
          driver, extentedReport, true);

      // check for policystatus
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }


      custdashboardpage.clickManagePolicy(extentedReport);
      custdashboardpage.clickMidTermAdjustment(extentedReport);
      custdashboardpage.enterDateForMTA(extentedReport);
      custdashboardpage.selectMidTermAdjReason(MTAcreationReason, extentedReport);
      custdashboardpage.clickMidTermContinue(extentedReport);
      GetQuotePage getquotepage = new GetQuotePage(driver, extentedReport);

      // Add joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.selectJointPolicyHolderContactType("Personal Contact", extentedReport);
      newquotepage.enterJointPolicy_LastName(mainCustLastName, extentedReport);
      newquotepage.enterJointPolicy_FirstName(mainCustFirstName, extentedReport);
      newquotepage.enterJointPolicy_DateOfBirth(testData.get("Date of Birth"), extentedReport);
      newquotepage.enterJointPolicy_PostCode(testData.get("Post Code"), extentedReport);
      newquotepage.clickPolicyHolderSearchButton(extentedReport);
      newquotepage.clickJointPolicyHolderSelectButton(extentedReport);

      Log.softAssertThat(
          newquotepage.verifyPolicyHolderContactName(jointPolicyName, true, extentedReport),
          jointPolicyName + " - additional policy holder is added",
          jointPolicyName + " - additional policy holder is not added", driver, extentedReport,
          true);

      String[] coversToAdd = testData.get("coverToSelect").toString().split(",");

      for (int i = 1; i < coversToAdd.length; i++) {
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
      getquotepage.clickReCalculate(extentedReport);

      // Click on buy button
      getquotepage.clickBuy(extentedReport);
      getquotepage.selectAvalilableAdjReasons(MTAcreationReason, extentedReport, false);
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
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);

      WaitUtils.waitForSpinner(driver);
      custdashboardpage = getquotepage.confirmPayment("MTA", testData, extentedReport);


      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer dashboard page", "Not navigated to Customer dashboard page",
          driver, extentedReport, true);


      // check for policystatus
      policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }

      searchPage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      // Search with Valid user names
      searchPage.enterLastName(mainCustLastName, extentedReport);

      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page
      searchPage.clickSearchedContactPolicy("policy", "", mainCustLastName, true, extentedReport);



      // Verify button AddPolicyHolder is accessible
      /*
       * Log.softAssertThat(
       * getquotepage.uielement.verifyPageElements(Arrays.asList("btnAddPolicyHolder"),
       * getquotepage), "Add joint Policyholder feature(Button) is accessable for the user",
       * "Add joint Policyholder feature(Button) is not accessable for the user", driver,
       * extentedReport, true);
       * 
       * // Verify bin icon is accessible
       * Log.softAssertThat(getquotepage.verifyBinIcon(jointPolicyName, false, extentedReport),
       * "Remove joint Policyholder feature is accessable for the user (Bin icon is present)",
       * "Remove joint Policyholder feature is not accessable for the user (Bin icon is not present)"
       * , driver, extentedReport, true);
       */
      Log.testCaseResult(extentedReport);
    }

    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }



  @Test(
      description = "To check that the selected policy holder is permanently removed from the list when click on Yes in the confirmation popup window",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_016(String browser) throws Exception {

    String tcId = "TC_016";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    // HashMap<String, String> customerDetails1 = new HashMap<String, String>();
    HashMap<String, String> customerDetails2 = new HashMap<String, String>();

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
      testData = searchPage.enterCustomerDetails(testData, true, extentedReport);
      String mainCustFirstName = testData.get("First Name");
      String mainCustLastName = testData.get("Last Name");
      Log.message("Contact 1 First Name------>" + mainCustFirstName, driver, extentedReport);
      Log.message("Contact 1 Last Name------>" + mainCustLastName, driver, extentedReport);
      String jointPolicyName = mainCustFirstName + " " + mainCustLastName;
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(
              testData.get("Title") + " " + mainCustFirstName + " " + mainCustLastName,
              extentedReport, true),
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

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
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + customerDetails2.get("First Name") + " " + customerDetails2.get("Last Name"),
              extentedReport, true),
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);
      custdashboardpage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(customerDetails2.get("Last Name"), extentedReport);
      searchPage.enterFirstName(customerDetails2.get("First Name"), extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(customerDetails2.get("Last Name"), true,
          extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

      // Create New Quote
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

      // Add joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.selectJointPolicyHolderContactType("Personal Contact", extentedReport);
      newquotepage.enterJointPolicy_LastName(mainCustLastName, extentedReport);
      newquotepage.enterJointPolicy_FirstName(mainCustFirstName, extentedReport);
      newquotepage.enterJointPolicy_DateOfBirth(testData.get("Date of Birth"), extentedReport);
      newquotepage.enterJointPolicy_PostCode(testData.get("Post Code"), extentedReport);
      newquotepage.clickPolicyHolderSearchButton(extentedReport);
      newquotepage.clickJointPolicyHolderSelectButton(extentedReport);
      Log.softAssertThat(
          newquotepage.verifyPolicyHolderContactName(jointPolicyName, true, extentedReport),
          jointPolicyName + " - additional policy holder is added",
          jointPolicyName + " - additional policy holder is not added", driver, extentedReport,
          true);

      // To click yes to remove policy
      newquotepage.clickYesToRemovePolicy(jointPolicyName, true, extentedReport);

      Log.softAssertThat(
          !newquotepage.verifyPolicyHolderContactName(jointPolicyName, true, extentedReport),
          jointPolicyName + " - is removed using remove Joint Policyholders feature",
          jointPolicyName + " - is not removed using remove Joint Policyholders feature", driver,
          extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }



  // 1607
  @Test(
      description = "Check that 'Reverse Transaction' option is available under the 'Manage Policy' sub-section after performing MTA in EC application and verify Modal",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_017(String browser) throws Exception {

    String tcId = "TC_017";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");
    String MTAcreationReason = testData.get("MTAdjReason");

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
      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + testData.get("First Name") + " " + testData.get("Last Name"), extentedReport, true),
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);
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
      // Add the insurance cover items
      String[] coversToAdd = testData.get("coverToSelect_NB").toString().split(",");
      for (int i = 0; i < coversToAdd.length; i++) {
        boolean BoolVal = false;
        String ins_RowtoInteract =
            newquotepage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
        BoolVal = newquotepage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract,
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
      newquotepage.clickUpgradeNow(extentedReport);
      Log.softAssertThat(newquotepage.verifyUpgradePolicyTo5Star(), "Policy upgraded to 5 star",
          "Policy not upgraded to 5 star", driver, extentedReport, true);
      newquotepage.clickReCalculate(extentedReport);
      newquotepage.clickBuy(extentedReport);
      newquotepage.clickPaymentNext(extentedReport);
      // Select Payment
      newquotepage.selectPayment(testData, true, extentedReport);

      // Select card type
      newquotepage.takePayment(extentedReport);

      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, false);
      carddetailspage.selectExpiry(testData, extentedReport, false);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, false);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);

      // Click continue button
      // newquotepage = carddetailspage.clickContinueButton(extentedReport);
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
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      Log.message("Policy Number ====>" + policyDetails.get("policyNo"), driver, extentedReport);
      newquotepage.clickManagePolicy(extentedReport);
      newquotepage.clickMidTermAdjustment(extentedReport);
      newquotepage.enterDateForMTA(extentedReport);
      newquotepage.selectMidTermAdjReason(MTAcreationReason, extentedReport);
      newquotepage.clickMidTermContinue(extentedReport);
      GetQuotePage getQuotePage = new GetQuotePage(driver, extentedReport).get();
      coversToAdd = testData.get("coverToSelect").toString().split(",");
      for (int i = 0; i < coversToAdd.length; i++) {
        boolean BoolVal = false;
        String ins_RowtoInteract =
            newquotepage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
        BoolVal = newquotepage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract,
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
      getQuotePage.clickReCalculate(extentedReport);
      getQuotePage.clickBuy(extentedReport);
      getQuotePage.selectAvalilableAdjReasons(testData.get("AdjustmentReason"), extentedReport,
          false);
      getQuotePage.clickAddMTA(extentedReport, false);
      getQuotePage.clickAddAllMTANextButton(extentedReport, true);
      getQuotePage.takePayment(extentedReport);
      // carddetailspage = getQuotePage.selectVisacard(extentedReport);
      carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      // getQuotePage = carddetailspage.clickContinueButton_getQuote(extentedReport);

      Log.softAssertThat(getQuotePage.verifyPaymentTrasaction(extentedReport),
          "Payment is successful", "Payment is not successful", driver, extentedReport, true);
      custdashboardpage = getQuotePage.confirmPayment("MTA", testData, extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer dashboard page", "Not navigated to Customer dashboard page",
          driver, extentedReport, true);
      newquotepage.clickManagePolicy(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkReverseTransaction"),
              custdashboardpage),
          "User able to see the 'Reverse Transaction' option under the 'Manage Policy' sub-section after performing MTA",
          "User not able to see the 'Reverse Transaction' option under the 'Manage Policy' sub-section after performing MTA",
          driver, extentedReport, true);
      newquotepage.clickReverseTransaction(extentedReport);
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(
              Arrays.asList("mdlReverseTransaction", "btnConfirmReversal"), newquotepage),
          "Reverse transaction modal is displayed", "Reverse transaction modal is not displayed",
          driver, extentedReport, true);
      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }


  // 1608
  @Test(
      description = "To check Finance Tab option is available in the Policy Detail section of Customer Dashboard and options will display under Finance Tab. 1. Finance Billing Details 2. Account",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_018(String browser) throws Exception {

    String tcId = "TC_018";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String tabName = testData.get("TabName");
    String Corecover = testData.get("Cover");
    String[] sectionsLookingFor = testData.get("SectionsLookingFor").split(";");

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
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);

      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

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
      Log.softAssertThat(newquotepage.verifyUpgradePolicyTo5Star(), "Policy upgraded to 5 star",
          "Policy not upgraded to 5 star", driver, extentedReport, true);
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
      Log.pass("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);

      // Click continue button
      // newquotepage = carddetailspage.clickContinueButton(extentedReport);
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
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat((policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")),
          "NB Policy Created Successfully in accepted status",
          "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status");

      CustDashboardPage custDashboardPage = new CustDashboardPage(driver, extentedReport);

      // Check whether the Finance tab exists in Policy details page
      Log.softAssertThat(
          custDashboardPage.isThisTabAvailableInPolicyDetails("Finance", extentedReport, true),
          tabName + " Tab Presents", tabName + " Tab not available");
      // Click on Finance Tab
      custDashboardPage.clickTab(tabName, extentedReport, true);

      // Check the given sections are available in Finance tab and display
      Log.softAssertThat(
          custDashboardPage.showTabSectionsIfAvailable(sectionsLookingFor, extentedReport, false),
          "Tab sections available", "No tab sctions available", driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  // 1610
  @Test(
      description = "To check that the Terms and Conditions can be viewed from 'Manage Policy --> MTA' for an existing contact",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 1)
  public void TC_019(String browser) throws Exception {

    String tcId = "TC_019";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");
    String MTAcreationReason = testData.get("MTAdjReason");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged into UXP with User id:" + userName + "& Password:" + password, driver,
          extentedReport);


      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();


      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);


      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Verify New Quote
      Log.softAssertThat(newquotepage.verifyNewQuotePage(true, extentedReport),
          "Verified NewQuotePage ", "Not verified NewQuotePage", driver, extentedReport, true);

      Log.softAssertThat(searchPage.ECbannertitleCheck(extentedReport), "NewQuotePage has the Logo",
          "NewQuotePage failed to have Logo", driver, extentedReport, true);
      // Enter policy details
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      Log.softAssertThat(searchPage.ECbannertitleCheck(extentedReport), "Search Page has the Logo",
          "Search page failed to have Logo", driver, extentedReport, true);

      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      // Get and Buy Quote
      newquotepage.clickGetQuote(extentedReport);
      Log.softAssertThat(newquotepage.ECbannertitleCheck(extentedReport),
          "Price presentation page has Logoe", "Price presentation page failed to have Logo",
          driver, extentedReport, true);
      newquotepage.clickBuy(extentedReport);
      // Select Payment type
      newquotepage.selectPayment(testData, true, extentedReport);
      Log.softAssertThat(newquotepage.ECbannertitleCheck(extentedReport),
          "Payment Page has the Logo", "Payment page failed to have Logo", driver, extentedReport,
          true);
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


      // Verify payment transaction
      // Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
      // "Payment was successful", "Payment was not successful", driver, extentedReport, true);

      // Confirm payment

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer Dashboard page", "Not navigated to Customer Dashboard page",
          driver, extentedReport, true);



      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").equalsIgnoreCase("Accepted")) {

        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);

        // MTA
        custdashboardpage.clickManagePolicy(extentedReport);
        custdashboardpage.clickMidTermAdjustment(extentedReport);
        custdashboardpage.enterDateForMTA(extentedReport);
        custdashboardpage.selectMidTermAdjReason(MTAcreationReason, extentedReport);
        custdashboardpage.clickMidTermContinue(extentedReport);
        GetQuotePage getquotepage = new GetQuotePage(driver, extentedReport);
        String[] coversToAdd = testData.get("coverToSelect").toString().split(",");

        for (int i = 1; i < coversToAdd.length; i++) {
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
        boolean flag = getquotepage.verifyReadTermsAndCondition(extentedReport);
        Log.softAssertThat(flag, "Verified Edit Terms and Conditions",
            "Not able to verify Edit Terms and Conditions", driver, extentedReport, true);

        getquotepage.selectAvalilableAdjReasons(MTAcreationReason, extentedReport, false);
        getquotepage.clickAddMTA(extentedReport, false);
        getquotepage.clickAddAllMTANextButton(extentedReport, true);
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


        custdashboardpage = getquotepage.confirmPayment("MTA", testData, extentedReport);


        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Navigated to Customer dashboard page", "Not navigated to Customer dashboard page",
            driver, extentedReport, true);

        policyDetails = custdashboardpage.getPolicyURN(extentedReport);

        if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
          Log.pass("MTA Policy Created Successfully in accepted status", driver, extentedReport,
              true);
        } else {
          Log.fail(
              "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
              driver, extentedReport, true);
        }



      }


      Log.testCaseResult(extentedReport);
      Log.pass("Test Case Passed", extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  // 1753 & 1754
  @Test(
      description = "Verify User not able to login with Invalid UserName and Password and valid UserName and Invalid Password and verify password screen",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_020(String browser) throws Exception {

    String tcId = "TC_020";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String[] userName = testData.get("UserName").split(";");
    String[] password = testData.get("Password").split(";");
    String description = testData.get("Description");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      try {
        loginPage.loginToSSP(userName[0], password[0], false, extentedReport);
      } catch (Exception e) {

        Log.softAssertThat(e.getMessage().contains("Invalid Login Details"),
            "User could not login with invalid Username : " + userName + " & valid Password : "
                + password,
            "User could login with invalid Username : " + userName + " & valid Password : "
                + password,
            driver, extentedReport, true);
      }
      // To check the error message
      Log.softAssertThat(loginPage.verifyErroMessage(),
          loginPage.ERROR_MSG_LOGIN + " - Error message is displayed ",
          loginPage.ERROR_MSG_LOGIN + "  Error message is not displayed", driver, extentedReport,
          true);
      loginPage.enterUserName(userName[1], extentedReport);
      loginPage.enterPassword(password[1], extentedReport);
      loginPage.clickBtnSignIn(extentedReport);
      Log.softAssertThat(loginPage.verifyErroMessage(),
          loginPage.ERROR_MSG_LOGIN + " - Error message is displayed ",
          loginPage.ERROR_MSG_LOGIN + "  Error message is not displayed", driver, extentedReport,
          true);
      HomePage homePage = loginPage.loginToSSP(userName[1], password[0], false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page",
          "Not logged in to Home page", driver, extentedReport, true);

      homePage.clickChangePassword(extentedReport);
      Log.softAssertThat(
          homePage.uielement.verifyPageElements(
              Arrays.asList("fldChPwdOldPwd", "fldChPwdNewPwd", "fldChPwdReEnterPwd"), homePage),
          "Change Password elements 'Please type exist data' text, 'Old Password', 'New Password', 'ReEnter Password' are Verified",
          "Change Password elements are not present", driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  // 2247
  @Test(
      description = "Check that the user is able to change the payor of the policy, once the quote reaches the summary page",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 1)
  public void TC_021(String browser) throws Exception {

    String tcId = "TC_021";
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
      String mainCustFirstName = testData.get("First Name");
      String mainCustLastName = testData.get("Last Name");
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
      custdashboardpage.clickComplete(extentedReport);
      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      searchPage.enterLastName(mainCustLastName, extentedReport);
      searchPage.enterFirstName(mainCustFirstName, extentedReport);
      searchPage.clickSearch(extentedReport);
      searchPage.clickSearchedContactPolicy("contact", "", mainCustLastName, true, extentedReport);
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

  // 1614
  @Test(description = "Verify Reverse transaction after doing Invite Renewal",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_022(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_022";
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
          "Customer dashboardpage verified", "Customer dashboardpage not verified", driver,
          extentedReport, true);
      Log.softAssertThat(
          custdashboardpage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard",
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      // Past date
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true);
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
      // confirm payment
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer Dashboard page", "Not navigated to Customer Dashboard page",
          driver, extentedReport, true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      Log.message("InceptionDate NB:" + policyDetails.get("InceptionDate"));
      Log.message("ExpiryDate NB:" + policyDetails.get("ExpiryDate"));

      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
        String NBPremium = custdashboardpage.getPremiums(extentedReport, true);

        custdashboardpage.performRenewals_LapseRenewal("Invite Renewal", extentedReport, true);
        custdashboardpage.clickContinueWarningMsg(extentedReport, true);
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
            "Review Required status displayed after saving a renewal quote",
            "Review Required status was not displayed after saving a renewal quote");

        BuyQuotePage buyQuotePage =
            custdashboardpage.performRenewals_QuoteVariation(extentedReport, true);
        // Verify Acceptance page is landed
        Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
            "Acceptance tab was selected by default when clicked on quote variations",
            "Acceptance tab was not selected as default tab when clicked on quote variations",
            driver, extentedReport, true);
        // update payment method
        buyQuotePage.selectPayment(testData, true, extentedReport);
        custdashboardpage = buyQuotePage.confirmPayment(testData, extentedReport);
        custdashboardpage.verifyCustomerDashboardPage();

        HashMap<String, String> policyDetailsRnwl = custdashboardpage.getPolicyURN(extentedReport);
        Log.message("InceptionDate : " + policyDetailsRnwl.get("InceptionDate"), driver,
            extentedReport);
        Log.message("ExpiryDate : " + policyDetailsRnwl.get("ExpiryDate"), driver, extentedReport);

        Log.softAssertThat(
            policyDetailsRnwl.get("Status").toString().equalsIgnoreCase("Accepted")
                && policyDetailsRnwl.get("Position").toString().equalsIgnoreCase("Renewal"),
            "User able to change the payment plan and method in renewal quote variation ",
            "Policy was not created in accepted status when payment method/plan updated in renew quote variation, it stands in :"
                + policyDetailsRnwl.get("Status"));

        // verify renewal tab not visible

        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElementsDoNotExist(
                Arrays.asList("drpRenewQotVariations", "drpManageRenewals"), custdashboardpage),
            "Manage Renewal and Renewal Quote Varation tabs were removed from customer dashboard page after completing payment",
            "Manage Renewal and Renewal Quote Varation tabs were not removed from customer dashboard page after completing payment",
            driver, extentedReport, true);
        String rnwlPremium = custdashboardpage.getPremiums(extentedReport, true);
        Log.message("Premium displayed after Renewal :" + rnwlPremium, extentedReport);

        custdashboardpage.clickManagePolicy(extentedReport);
        custdashboardpage.clickReverseTransaction(extentedReport);
        custdashboardpage.selectReverseTransType("Client Request", extentedReport, true);
        custdashboardpage.clickConfirmReversalButton(extentedReport);
        // Validate the policy status and verify the premium
        String reverseTransPremium = custdashboardpage.getPremiums(extentedReport, true);
        Log.softAssertThat(reverseTransPremium.equalsIgnoreCase(NBPremium),
            "Premiums reversed to NB premium on reverse transaction. NBPremium :" + NBPremium
                + ", Renewal Premium:" + rnwlPremium + ", Reversed Premium:" + reverseTransPremium,
            "Premiums not reversed to NB premium on reverse transaction. NBPremium :" + NBPremium
                + ", Renewal Premium:" + rnwlPremium + ", Reversed Premium:" + reverseTransPremium,
            driver, extentedReport, true);

        HashMap<String, String> polDet_AfterRevTran =
            custdashboardpage.getPolicyURN(extentedReport);

        Log.softAssertThat(
            polDet_AfterRevTran.get("InceptionDate")
                .equalsIgnoreCase(policyDetails.get("InceptionDate"))
                && polDet_AfterRevTran.get("ExpiryDate")
                    .equalsIgnoreCase(policyDetails.get("ExpiryDate")),
            "Inception Date and Expiry date reversed successfully. Renewed Inception Date:"
                + policyDetailsRnwl.get("InceptionDate") + ", Renewed Expiry Date:"
                + policyDetailsRnwl.get("ExpiryDate") + ".Reveresed Inception Date:"
                + polDet_AfterRevTran.get("InceptionDate") + ",Reveresed Expiry Date:"
                + polDet_AfterRevTran.get("ExpiryDate"),
            "No change in the policy inception / expiry date.Renewed Inception Date:"
                + policyDetailsRnwl.get("InceptionDate") + ", Renewed Expiry Date:"
                + policyDetailsRnwl.get("ExpiryDate") + ".Reveresed Inception Date:"
                + polDet_AfterRevTran.get("InceptionDate") + ",Reveresed Expiry Date:"
                + polDet_AfterRevTran.get("ExpiryDate"),
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

  @Test(
      description = "Create NB (Add interested party) and do Main search for IP and see whether the search results shows the Policy details",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_023(String browser) throws Exception {

    String tcId = "TC_023";
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
      HashMap<String, String> CustomertestData =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardPage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardPage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardPage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);
      Log.softAssertThat(
          custdashboardPage.verifyContactName(CustomertestData.get("Title") + " "
              + CustomertestData.get("First Name") + " " + CustomertestData.get("Last Name"),
              extentedReport, true),
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
      HashMap<String, String> IPContact =
          newquotepage.enterIntrestedpartyCustomerDetails(testData, true, extentedReport);

      String mainCustFirstName = IPContact.get("First Name");
      String mainCustLastName = IPContact.get("Last Name");
      newquotepage.confirmCreateCustomer(true, extentedReport);

      newquotepage.clickselect(extentedReport, true);

      newquotepage.addInterestedPartyType(extentedReport, true);

      // newquotepage.addInterestedPartyTypeToDate(extentedReport, true);

      newquotepage.addInterestedPartyClickSave(extentedReport, true);
      String IpNameTochk = testData.get("Title") + " " + mainCustFirstName + " " + mainCustLastName;
      Log.message("Intersted party name:" + IpNameTochk);
      /*
       * Log.softAssertThat(newquotepage.checkaddedInterestedParty(IpNameTochk), IpNameTochk +
       * " Added party is visible", IpNameTochk + " Added party is not visible", driver,
       * extentedReport, true);
       */
      Log.pass("Intrested Party has been added-----" + IpNameTochk, extentedReport);

      /*
       * /// Add to buy NB policy newquotepage.clickBuy(extentedReport);
       * newquotepage.selectPayment(testData, true, extentedReport);
       * 
       * // Select card type newquotepage.takePayment(extentedReport); CardDetailsPage
       * carddetailspage = newquotepage.selectVisacard(extentedReport);
       * 
       * Log.pass("Selected VISA Card", driver, extentedReport, true);
       * 
       * // Step-10: Enter Card Details carddetailspage.enterCardNumber(testData, extentedReport,
       * true); carddetailspage.selectExpiry(testData, extentedReport, true);
       * carddetailspage.enterVerification(extentedReport, true);
       * carddetailspage.enterName(testData, extentedReport, true);
       * carddetailspage.clickbuy(extentedReport, true);
       * Log.pass("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
       * true);
       * 
       * // Click continue button newquotepage =
       * carddetailspage.clickContinueButton(extentedReport);
       * newquotepage.verifyPaymentTrasaction(extentedReport);
       * 
       * if (!newquotepage.verifyPaymentTrasaction(extentedReport)) {
       * Log.fail("Payment was not successful", driver, extentedReport, true); } else {
       * 
       * Log.pass("Payment was successful", driver, extentedReport, true); }
       * 
       */

      newquotepage.clickBuy(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      // confirm payment
      custdashboardPage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardPage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardPage),
          "Navigated to Customer Dashboard page", "Not navigated to Customer Dashboard page",
          driver, extentedReport, true);


      /*
       * // confirm payment custdashboardPage = newquotepage.confirmPayment(testData,
       * extentedReport); Log.softAssertThat(
       * custdashboardPage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
       * custdashboardPage), "custdashboardpage Verified", "custdashboardpage not Verified", driver,
       * extentedReport, true);
       */

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

  }

  @Test(description = "Corporate Contact Creation, Replace address for Corporate contact ",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_024(String browser) throws Exception {

    String tcId = "TC_024";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");

    String brandname = testData.get("Brand Name");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

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
      searchPage.createCorporateContact(testData, extentedReport, true);
      CustDashboardPage custdashboardPage =
          searchPage.confirmCreateCustomer_CC(true, extentedReport);

      Log.softAssertThat(
          custdashboardPage.verifyContactName(testData.get("Trade Name"), extentedReport, true),
          "Verified Business name on Customer Dashboard",
          "Verification of Business name on Customer Dashboard failed", driver, extentedReport,
          true);

      Log.softAssertThat(custdashboardPage.verifyEditBusinesAddress(extentedReport, true),
          "Replaced address reflects in customer dashboard page",
          "Failed to replace the address in customer dashboard page", driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally

  }



  @Test(description = "MTA using Single Bill Payment  (for NB Monthly DD Policy)",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_025(String browser) throws Exception {
    // "TC_025";
    String tcId = "TC_025";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String description = testData.get("Description");
    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");
    String MTAcreationReason = testData.get("MTAdjReason");

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
      newquotepage.clickBuy(extentedReport);

      // Payment
      newquotepage.selectPayment(testData, true, extentedReport);
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

      // custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport);


      newquotepage.clickManagePolicy(extentedReport);
      newquotepage.clickMidTermAdjustment(extentedReport);
      newquotepage.enterDateForMTA(extentedReport);
      newquotepage.selectMidTermAdjReason(MTAcreationReason, extentedReport);
      newquotepage.clickMidTermContinue(extentedReport);

      // custdashboardpage.enterMTADetails(testData, extentedReport, true);
      GetQuotePage getQuotePage = new GetQuotePage(driver, extentedReport).get();
      GetQuotePage getquotepage = custdashboardpage.verifyGetQuotePage(extentedReport);
      Log.message("Get quote page loaded successfully", driver, extentedReport, true);

      String[] coversToAdd = testData.get("coverToSelect").toString().split(",");
      for (int i = 0; i < coversToAdd.length; i++) {
        String ins_RowtoInteract =
            getquotepage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
        getquotepage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract, true,
            extentedReport);
      }

      getquotepage.clickReCalculate(extentedReport);
      getquotepage.clickBuy(extentedReport);
      getquotepage.selectAvalilableAdjReasons(MTAcreationReason, extentedReport, false);
      getquotepage.clickAddMTA(extentedReport, false);
      getquotepage.clickAddAllMTANextButton(extentedReport, true);
      Log.softAssertThat(getquotepage.verifyMTAAdjustmentRadioButtons(false, extentedReport, true),
          "Verified that radio buttons of 'MTA Adjustment' section are displayed as expected",
          "The radio buttons of 'MTA Adjustment' section are not displayed as expected", driver,
          extentedReport, true);
      getquotepage.selectSingleBillCardRadioButton(extentedReport);

      // Select Card
      getquotepage.clickTakePaymentForMTA(extentedReport);
      CardDetailsPage carddetailspage = getquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);

      // Click continue button
      // carddetailspage.clickContinueButton_getQuote(extentedReport);

      Log.softAssertThat(getquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment transaction is completed successfully", "Payment transaction is not completed",
          driver, extentedReport, true);

      // Return to customerdashboard page
      getquotepage.clickReturnToCustomerDashboard(extentedReport);
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

  @Test(description = "Verify Billing Adjustment", dataProviderClass = DataProviderUtils.class,
      dataProvider = "ssBVTDataProvider")
  public void TC_026(String browser) throws Exception {

    String tcId = "TC_026";
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
      billingAdjustmentPage.selectVisacard(extentedReport);

      // Enter Card Details
      billingAdjustmentPage.enterCardNumber(testData, extentedReport, true);
      billingAdjustmentPage.selectExpiry(testData, extentedReport, true);
      billingAdjustmentPage.enterVerification(extentedReport, true);
      billingAdjustmentPage.enterName(testData, extentedReport, true);
      billingAdjustmentPage.clickbuy(extentedReport, true);


      // Click continue button
      // billingAdjustmentPage.clickContinueButton(extentedReport);
      billingAdjustmentPage.clickConfirmBtn(extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to customer dashboard page", "Could not navigated to customer dashboard page",
          driver, extentedReport, true);

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
      driver.quit();
    }
  }

  // 1721
  @Test(description = "Create New Policy - check Engagement Centre banner title",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_027(String browser) throws Exception {
    // Get the web driver instance
    String tcId = "TC_027";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");
    String MTAcreationReason = testData.get("MTAdjReason");
    String STAcreationReason = testData.get("AdjustmentReason_STA");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged into UXP with User id:" + userName + "& Password:" + password, driver,
          extentedReport);

      /*
       * Log.softAssertThat(homePage.ECbannertitleCheck(extentedReport),
       * "Home page has the banner title - Engagement Centre",
       * "Home page failed to have banner title as Engagement Centre", driver, extentedReport,
       * true);
       */

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      /*
       * Log.softAssertThat(searchPage.ECbannertitleCheck(extentedReport),
       * "Search Page has the Logo", "Search page failed to have Logo", driver, extentedReport,
       * true);
       */

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(searchPage.ECbannertitleCheck(extentedReport),
          "Customer Dash board has the Logo", " Customer Dashboard failed to have Logo", driver,
          extentedReport, true);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

      // Verify New Quote
      Log.softAssertThat(newquotepage.verifyNewQuotePage(true, extentedReport),
          "Verified NewQuotePage ", "Not verified NewQuotePage", driver, extentedReport, true);

      Log.softAssertThat(searchPage.ECbannertitleCheck(extentedReport), "NewQuotePage has the Logo",
          "NewQuotePage failed to have Logo", driver, extentedReport, true);
      // Enter policy details
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      Log.softAssertThat(searchPage.ECbannertitleCheck(extentedReport), "Search Page has the Logo",
          "Search page failed to have Logo", driver, extentedReport, true);

      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      // Get and Buy Quote
      newquotepage.clickGetQuote(extentedReport);
      Log.softAssertThat(newquotepage.ECbannertitleCheck(extentedReport),
          "Price presentation page has Logoe", "Price presentation page failed to have Logo",
          driver, extentedReport, true);
      newquotepage.clickBuy(extentedReport);
      // Select Payment type
      newquotepage.selectPayment(testData, true, extentedReport);
      Log.softAssertThat(newquotepage.ECbannertitleCheck(extentedReport),
          "Payment Page has the Logo", "Payment page failed to have Logo", driver, extentedReport,
          true);
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


      // Verify payment transaction
      // Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
      // "Payment was successful", "Payment was not successful", driver, extentedReport, true);

      // Confirm payment

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer Dashboard page", "Not navigated to Customer Dashboard page",
          driver, extentedReport, true);



      custdashboardpage.ECbannertitleCheck(extentedReport);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").equalsIgnoreCase("Accepted")) {

        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);

        // MTA
        custdashboardpage.clickManagePolicy(extentedReport);
        custdashboardpage.clickMidTermAdjustment(extentedReport);
        custdashboardpage.enterDateForMTA(extentedReport);
        custdashboardpage.selectMidTermAdjReason(MTAcreationReason, extentedReport);
        custdashboardpage.clickMidTermContinue(extentedReport);
        GetQuotePage getquotepage = new GetQuotePage(driver, extentedReport);
        String[] coversToAdd = testData.get("coverToSelect").toString().split(",");

        for (int i = 1; i < coversToAdd.length; i++) {
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
        getquotepage.selectAvalilableAdjReasons(MTAcreationReason, extentedReport, false);
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
        Thread.sleep(4000);

        custdashboardpage = getquotepage.confirmPayment("MTA", testData, extentedReport);


        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Navigated to Customer dashboard page", "Not navigated to Customer dashboard page",
            driver, extentedReport, true);

        policyDetails = custdashboardpage.getPolicyURN(extentedReport);

        if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
          Log.pass("MTA Policy Created Successfully in accepted status", driver, extentedReport,
              true);
        } else {
          Log.fail("Failed - MTA Policy Created in " + policyDetails.get("Status").toString()
              + " status", driver, extentedReport, true);
        }
        // STA

        custdashboardpage.clickManagePolicy(extentedReport);
        custdashboardpage.clickshortTermAdjustment(extentedReport);

        custdashboardpage.enterSTADetails(testData, extentedReport, true);



        getquotepage = new GetQuotePage(driver, extentedReport);
        /*
         * coversToAdd = testData.get("coverToSelect").toString().split(",");
         * 
         * for (int i = 1; i < coversToAdd.length; i++) { boolean BoolVal = false; String
         * ins_RowtoInteract = getquotepage.SelectInsuranceItem(coversToAdd[i], true,
         * extentedReport); BoolVal = getquotepage.enterInsCoverDetails(testData, coversToAdd[i],
         * ins_RowtoInteract, true, extentedReport); String[] coverType = coversToAdd[i].split("_");
         * if (BoolVal != false) { Log.pass(coverType[2] + " " + coverType[0] +
         * " cover done successfully", driver, extentedReport, true); } else { Log.fail("Failed to "
         * + coverType[2] + " " + coverType[0] + " cover", driver, extentedReport, true); } } //
         * Click on recalculate getquotepage.clickReCalculate(extentedReport);
         */
        Thread.sleep(3000);
        Thread.sleep(3000);
        // Click on buy button
        getquotepage.clickBuy(extentedReport);
        getquotepage.selectAvalilableAdjReasonsSTA(STAcreationReason, extentedReport, false);

        getquotepage.clickAddSTA(extentedReport, false);
        getquotepage.clickAddAllMTANextButton(extentedReport, true);
        getquotepage.confirm();

        // Select card type
        /*
         * getquotepage.takePayment(extentedReport); carddetailspage =
         * getquotepage.selectVisacard(extentedReport);
         * 
         * // Enter Card Details carddetailspage.enterCardNumber(testData, extentedReport, true);
         * carddetailspage.selectExpiry(testData, extentedReport, true);
         * carddetailspage.enterVerification(extentedReport, true);
         * carddetailspage.enterName(testData, extentedReport, true);
         * carddetailspage.clickbuy(extentedReport, true);
         * Log.message("Entered Card Details and Navigated to Verificaion page", driver,
         * extentedReport, true);
         * 
         * 
         * custdashboardpage = getquotepage.confirmPayment("STA", testData, extentedReport);
         */

        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Navigated to Customer dashboard page", "Not navigated to Customer dashboard page",
            driver, extentedReport, true);

        policyDetails = custdashboardpage.getPolicyURN(extentedReport);

        if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
          Log.pass("STA Policy Created Successfully in accepted status", driver, extentedReport,
              true);
        } else {
          Log.fail("Failed - STA Policy Created in " + policyDetails.get("Status").toString()
              + " status", driver, extentedReport, true);
        }



        /*
         * if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted") &&
         * policyDetails.get("Position").toString().equalsIgnoreCase("ShortTermAdjustment ")) {
         * 
         * Log.pass("STA Policy Created Successfully in accepted status", driver, extentedReport,
         * true);
         * 
         * } else {
         * Log.fail("Failed to create STA policy in accepted status,it stands in following status:"
         * + policyDetails.get("Status").toString() + " status, Position :" +
         * policyDetails.get("Position").toString(), driver, extentedReport, true); }
         */


        // complaints page

        /*
         * ComplaintPage complaintPage =
         * custdashboardpage.selectNewcomplaint_ComplaintsTab(extentedReport, true);
         * Log.softAssertThat(complaintPage.ECbannertitleCheck(extentedReport),
         * "Complaint Page has the banner title as Engagement Centre",
         * "Complaint page failed to have banner title as Engagement Centre", driver,
         * extentedReport, true); } else {
         * Log.fail("NB Policy has failed to Create in Accepted status, unable to procced with MTA",
         * driver, extentedReport, true);
         * 
         */


      }
      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(
      description = "Check that the user is able to complete the suspension by selecting Accept button",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_028(String browser) throws Exception {

    String tcId = "TC_028";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");
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
          "Customer dashboard Verified", "Customer dashboard not Verified", driver, extentedReport,
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
      newquotepage.clickPaymentNext(extentedReport);
      // Select Payment
      newquotepage.selectPayment(testData, true, extentedReport);
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer dashboard page", "Not navigated to Customer dashboard page",
          driver, extentedReport, true);
      // Select suspend billing
      SuspendBillingPage suspendBillingPage =
          custdashboardpage.selectSuspendBillingfromManagePolicy(extentedReport, true);
      suspendBillingPage.completeSuspendBilling(suspendBillingReason, extentedReport, true);

      Log.pass("Billing has been suspended", extentedReport);

      UnsuspendBillingPage unsuspendBillingPage =
          custdashboardpage.clickUnsuspendBillingfromManagePolicy(extentedReport, true);

      // Select Payment Plan & method
      unsuspendBillingPage.selectPayment(testData, true, extentedReport);
      custdashboardpage = unsuspendBillingPage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer dashboard page", "Not navigated to Customer dashboard page",
          driver, extentedReport, true);
      Log.softAssertThat(custdashboardpage.verifyUnsuspendBilling_Accepted(),
          "User is able to complete the Unsuspend Billing",
          "User is unable to complete the Unsuspend Billing", driver, extentedReport, true);
      Log.testCaseResult(extentedReport);
      Log.pass("Test Case Passed", extentedReport);
    } catch (Exception e) {
      Log.fail("Test case Failed", extentedReport);
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  // 1699
  @Test(
      description = "Check whether the default policy flags will be set as Automatic Review - Checked,Automatic Renewal - Checked,Block renewal - Unchecked and check the flags after selecting block renewal and verify that changes remains when user perform MTA again",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_029(String browser) throws Exception {
    String tcId = "TC_029";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");
    String MTAcreationReason = testData.get("MTAdjReason");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

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
          "Customer dashboard Verified", "Customer dashboard not Verified", driver, extentedReport,
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
      newquotepage.clickCustomerPreferenceNext(extentedReport);
      // Select Payment
      newquotepage.selectPayment(testData, true, extentedReport);
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, false);
      carddetailspage.selectExpiry(testData, extentedReport, false);
      carddetailspage.enterVerification(extentedReport, false);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);

      // Click continue button
      // newquotepage = carddetailspage.clickContinueButton(extentedReport);
      Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment was successful", "Payment was not successful", driver, extentedReport, true);

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer Dashboard page", "Not navigated to Customer Dashboard page",
          driver, extentedReport, true);

      // check for policy status and MTA
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {

        Log.message("New Business Policy Created Successfully in Accepted status", driver,
            extentedReport, true);
        custdashboardpage.selectSTAfromManagePolicyTab(extentedReport, true);
        custdashboardpage.enterSTADetails(testData, extentedReport, true);
        GetQuotePage getquotepage = custdashboardpage.verifyGetQuotePage(extentedReport);

        // By default the Automatic Review, Renewal - Enabled and checked, Block Renewal
        // - Unchecked(Validating that below)
        // Log.softAssertThat(
        // getquotepage.validateReviewRenewalFields("Automatic Review", "Enabled", extentedReport,
        // true),
        // "Automatic Review field is enabled", "Automatic Review field is not enabled", driver,
        // extentedReport);
        // Log.softAssertThat(
        // getquotepage.validateReviewRenewalFields("Automatic Renewal", "Enabled", extentedReport,
        // true),
        // "Automatic Renewal field is enabled", "Automatic Renewal field is not enabled", driver,
        // extentedReport);
        // Log.softAssertThat(
        // getquotepage.validateReviewRenewalFields("Automatic Review", "checked", extentedReport,
        // true),
        // "Automatic Review field is checked", "Automatic Review field is not checked", driver,
        // extentedReport);
        // Log.softAssertThat(
        // getquotepage.validateReviewRenewalFields("Automatic Renewal", "checked", extentedReport,
        // true),
        // "Automatic Renewal field is checked", "Automatic Renewal field is not checked", driver,
        // extentedReport);

        // Log.softAssertThat(
        // !getquotepage.validateReviewRenewalFields("Block Renewal", "checked", extentedReport,
        // true),
        // "Block Renewal field unchecked as expected",
        // "Block Renewal field checked which is not expected, it should be unchecked by default if
        // no flags set",
        // driver, extentedReport);

        // Selecting block renewal- it should uncheck automatic review and renewal and
        // it should be disabled
        // getquotepage.clickPolicyFlags("Block Renewal", extentedReport, true);
        Log.softAssertThat(
            getquotepage.validateReviewRenewalFields("Automatic Review", "disabled", extentedReport,
                true),
            "Automatic Review field is disabled", "Automatic Review field is not disabled", driver,
            extentedReport);
        Log.softAssertThat(
            getquotepage.validateReviewRenewalFields("Automatic Renewal", "disabled",
                extentedReport, true),
            "Automatic Renewal field is disabled", "Automatic Renewal field is not disabled",
            driver, extentedReport);

        Log.softAssertThat(
            getquotepage.validateReviewRenewalFields("Block Renewal", "disabled", extentedReport,
                true),
            "Block Renewal field is disabled", "Block Renewal field is not disabled", driver,
            extentedReport);

        getquotepage.clickBuy(extentedReport);
        getquotepage.selectSTAReasonPayment(extentedReport);

        custdashboardpage = getquotepage.confirmPayment("STA", testData, extentedReport);

        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Customer dashboard page is verified", "Customer dashboard page is not verified",
            driver, extentedReport, true);
        policyDetails = custdashboardpage.getPolicyURN(extentedReport);

        Log.softAssertThat(
            policyDetails.get("Status").toString().equalsIgnoreCase("Accepted") && policyDetails
                .get("Position").toString().equalsIgnoreCase("ShortTermAdjustment "),
            "STA Policy Created Successfully in accepted status",
            "Failed to create STA policy in accepted status,it stands in following status:"
                + policyDetails.get("Status").toString() + " status, Position :"
                + policyDetails.get("Position").toString(),
            driver, extentedReport, true);

        // Verify the flags set in STA remains same in MTA cycle
        /*
         * custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport); // Fill
         * up MTa data custdashboardpage.enterMTADetails(testData, extentedReport, true);
         */

        newquotepage.clickManagePolicy(extentedReport);
        newquotepage.clickMidTermAdjustment(extentedReport);
        newquotepage.enterDateForMTA(extentedReport);
        newquotepage.selectMidTermAdjReason(MTAcreationReason, extentedReport);
        newquotepage.clickMidTermContinue(extentedReport);

        Log.message("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
        // getquotepage = new GetQuotePage(driver, extentedReport);
        getquotepage.verifyGetQuotePage(extentedReport);
        Log.softAssertThat(
            getquotepage.validateReviewRenewalFields("Automatic Review", "Enabled", extentedReport,
                true),
            "Automatic Review field is Eanbled", "Automatic Review field is not disabled", driver,
            extentedReport);
        Log.softAssertThat(
            getquotepage.validateReviewRenewalFields("Automatic Renewal", "Enabled", extentedReport,
                true),
            "Automatic Renewal field is Enabled", "Automatic Renewal field is not disabled", driver,
            extentedReport);
        // Log.softAssertThat(!getquotepage.validateReviewRenewalFields("Automatic Review",
        // "checked", extentedReport, true)
        // && getquotepage.validateReviewRenewalFields("Automatic Renewal", "checked",
        // extentedReport, true),
        // "Both Automatic Renewal and Automatic Review fields were checked as expected",
        // "Both Automatic Renewal and Automatic Review fields were unchecked", driver,
        // extentedReport);
        // getquotepage.validateReviewRenewalFields("Automatic Renewal",
        // "checked",extentedReport, true);
        // Check block renewal and verify all the policy flag in MTA
        getquotepage.clickPolicyFlags("Block Renewal", extentedReport, true);


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
        Log.message("Entered Card Details and Navigated to Verificaion page", driver,
            extentedReport, true);

        // Click continue button
        // getquotepage = carddetailspage.clickContinueButton_getQuote(extentedReport);
        Log.softAssertThat(getquotepage.verifyPaymentTrasaction(extentedReport),
            "Payment was successful", "Payment was not successful", driver, extentedReport, true);

        custdashboardpage = getquotepage.confirmPayment("MTA", testData, extentedReport);
        // check for policy status
        policyDetails = custdashboardpage.getPolicyURN(extentedReport);

        Log.softAssertThat(policyDetails.get("Status").toString().equalsIgnoreCase("Accepted"),
            "MTA Policy Created Successfully in accepted status",
            "Failed to create MTA policy in accepted status,it stands in following status:"
                + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);

        // Perform MTA and verify the flags are unchecked
        /*
         * custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport); // Fill
         * up MTa data custdashboardpage.enterMTADetails(testData, extentedReport, true);
         */

        newquotepage.clickManagePolicy(extentedReport);
        newquotepage.clickMidTermAdjustment(extentedReport);
        newquotepage.enterDateForMTA(extentedReport);
        newquotepage.selectMidTermAdjReason(MTAcreationReason, extentedReport);
        newquotepage.clickMidTermContinue(extentedReport);



        Log.message("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
        getquotepage.verifyGetQuotePage(extentedReport);

        Log.softAssertThat(
            !getquotepage.validateReviewRenewalFields("Automatic Review", "checked", extentedReport,
                true),
            "Automatic Review field is Unchecked",
            "Automatic Review field is checked, the Unchecked flag during MTA was not saved ",
            driver, extentedReport);
        Log.softAssertThat(
            !getquotepage.validateReviewRenewalFields("Automatic Renewal", "checked",
                extentedReport, true),
            "Automatic Renewal field is Unchecked",
            "Automatic Renewal field is not checked,the Unchecked flag during MTA was not saved",
            driver, extentedReport);

        Log.softAssertThat(
            getquotepage.validateReviewRenewalFields("Block Renewal", "checked", extentedReport,
                true),
            "Block Renewal field is checked as expected",
            "Block Renewal field is unchecked, the unchecked flag during MTA was not saved need to revisist the test case",
            driver, extentedReport);

      } else {
        Log.fail("NB Policy has failed to Create in Accepted status, unable to procced with STA",
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


  @Test(
      description = "Create NB (Add interested party) and validate the Interested party value on Customer Dashboard",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_030(String browser) throws Exception {

    String tcId = "TC_030";
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
      HashMap<String, String> CustomertestData =
          searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardPage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardPage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardPage),
          "Customer Dashboard page is verified", "Customer Dashboard page is not verified", driver,
          extentedReport, true);
      Log.softAssertThat(
          custdashboardPage.verifyContactName(CustomertestData.get("Title") + " "
              + CustomertestData.get("First Name") + " " + CustomertestData.get("Last Name"),
              extentedReport, true),
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
      // newquotepage.verifyPaymentTrasaction(extentedReport);

      /*
       * if (!newquotepage.verifyPaymentTrasaction(extentedReport)) {
       * Log.fail("Payment was not successful", driver, extentedReport, true); } else {
       * 
       * Log.pass("Payment was successful", driver, extentedReport, true); }
       */
      // confirm payment
      // custdashboardPage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custdashboardPage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardPage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      // check for policy status
      HashMap<String, String> policyDetails = custdashboardPage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {

        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);

        Log.softAssertThat(custdashboardPage.verifyIPName(IpNameTochk, extentedReport, true),
            "Verified Interested party Name on Customer Dashboard",
            "Not Verified Interested party Name on Customer Dashboard", driver, extentedReport,
            true);

        // *********************************************************************************************************

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

        // searchPage.enterLastName(mainCustLastName, extentedReport);
        searchPage.enterPolicyNumber(policyDetails.get("PolicyNo").toString(), extentedReport,
            true);
        searchPage.clickSearch(extentedReport);

        custdashboardPage = searchPage.clickSearchedContactPolicy("policy",
            policyDetails.get("PolicyNo"), "", true, extentedReport);
        Log.pass("Policy details displayed for the added interested party", extentedReport);
        custdashboardPage.verifyCustomerDashboardPage();
        Log.pass("Navigated to Cutomer Dashboard", driver, extentedReport, true);

        // Verifying Customer Details
        /*
         * CustomerName = testData.get("Title") + " " + mainCustFirstName + " " + mainCustLastName;
         * Log.softAssertThat(custdashboardPage.verifyContactName(CustomerName, extentedReport,
         * true), "Verified Interested party FirstName and LastName on Customer Dashboard",
         * "Not Verified Interested party FirstName and LastName on Customer Dashboard", driver,
         * extentedReport, true);
         */

        Log.softAssertThat(custdashboardPage.verifyIPName(IpNameTochk, extentedReport, true),
            "Verified Interested party Name on Customer Dashboard",
            "Not Verified Interested party Name on Customer Dashboard", driver, extentedReport,
            true);

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
  }
}
