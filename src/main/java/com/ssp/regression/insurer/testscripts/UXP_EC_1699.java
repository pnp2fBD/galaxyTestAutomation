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
import com.ssp.uxp_pages.CardDetailsPage;
import com.ssp.uxp_pages.CustDashboardPage;
import com.ssp.uxp_pages.GetQuotePage;
import com.ssp.uxp_pages.HomePage;
import com.ssp.uxp_pages.LoginPage;
import com.ssp.uxp_pages.NewQuotePage;
import com.ssp.uxp_pages.SearchPage;

@Listeners(EmailReport.class)
public class UXP_EC_1699 extends BaseTest {

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
    String className = "UXP_EC_1699_" + env;
    return DataUtils.testDatabyID(testcaseId, className);
  }


  @Test(
      description = "Check whether the default policy flags will be set as Automatic Review - Checked,Automatic Renewal - Checked,Block renewal - Unchecked and check the flags after selecting block renewal and verify that changes remains when user perform MTA again",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_414_415_416_417_421(String browser) throws Exception {
    String tcId = "TC_414_415_416_417_421";
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

        custdashboardpage.selectSTAfromManagePolicyTab(extentedReport, true);
        custdashboardpage.enterSTADetails(testData, extentedReport, true);
        GetQuotePage getquotepage = custdashboardpage.verifyGetQuotePage(extentedReport);

        // By default the Automatic Review, Renewal - Enabled and checked, Block Renewal -
        // Unchecked(Validating that below)
        getquotepage.validateReviewRenewalFields("Automatic Review", "Enabled", extentedReport,
            true);
        getquotepage.validateReviewRenewalFields("Automatic Renewal", "Enabled", extentedReport,
            true);
        getquotepage.validateReviewRenewalFields("Automatic Review", "checked", extentedReport,
            true);
        getquotepage.validateReviewRenewalFields("Automatic Renewal", "checked", extentedReport,
            true);

        Log.softAssertThat(
            !getquotepage.validateReviewRenewalFields("Block Renewal", "checked", extentedReport,
                true),
            "Block Renewal field unchecked as expected",
            "Block Renewal field checked which is not expected, it should be unchecked by default if no flags set");
        // Selecting block renewal- it should uncheck automatic review and renewal and it should be
        // disabled
        getquotepage.clickPolicyFlags("Block Renewal", extentedReport, true);
        getquotepage.validateReviewRenewalFields("Automatic Review", "disabled", extentedReport,
            true);
        getquotepage.validateReviewRenewalFields("Automatic Renewal", "disabled", extentedReport,
            true);

        getquotepage.clickBuy(extentedReport);
        Log.message("Clicked on Buy Quote", driver, extentedReport);
        getquotepage.selectMTAReasonPayment(extentedReport);
        custdashboardpage = getquotepage.confirmPayment("STA", testData, extentedReport);

        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
            true);
        policyDetails = custdashboardpage.getPolicyURN(extentedReport);

        if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")
            && policyDetails.get("Position").toString().equalsIgnoreCase("ShortTermAdjustment ")) {

          Log.pass("STA Policy Created Successfully in accepted status", driver, extentedReport,
              true);

        } else {
          Log.fail("Failed to create STA policy in accepted status,it stands in following status:"
              + policyDetails.get("Status").toString() + " status, Position :"
              + policyDetails.get("Position").toString(), driver, extentedReport, true);
        }

        // Verify the flags set in STA remains same in MTA cycle
        custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport);
        Log.pass("Clicked on MTA menu successfully", driver, extentedReport, true);
        // Fill up MTa data
        custdashboardpage.enterMTADetails(testData, extentedReport, true);

        Log.pass("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
        // getquotepage = new GetQuotePage(driver);
        getquotepage.verifyGetQuotePage(extentedReport);
        Log.pass("Get quote page loaded successfully", driver, extentedReport, true);

        getquotepage.validateReviewRenewalFields("Automatic Review", "disabled", extentedReport,
            true);
        getquotepage.validateReviewRenewalFields("Automatic Renewal", "disabled", extentedReport,
            true);
        Log.softAssertThat(
            !getquotepage.validateReviewRenewalFields("Automatic Review", "checked", extentedReport,
                true)
                && getquotepage.validateReviewRenewalFields("Automatic Renewal", "checked",
                    extentedReport, true),
            "Both Automatic Renewal and Automatic Review fields were unchecked as expected",
            "Both Automatic Renewal and Automatic Review fields were not unchecked", driver);
        // getquotepage.validateReviewRenewalFields("Automatic Renewal", "checked",extentedReport,
        // true);

        Log.softAssertThat(
            getquotepage.validateReviewRenewalFields("Block Renewal", "checked", extentedReport,
                true),
            "Block Renewal field remains checked as it was in STA",
            "Block Renewal field unchecked which is not expected, it should be checked as it was checked during STA transaction");

        // UnCheck block renewal and verify all the policy flag in MTA
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
            "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
            true);

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

        // Perform MTA and verify the flags are unchecked
        custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport);
        Log.pass("Clicked on MTA menu successfully", driver, extentedReport, true);
        // Fill up MTa data
        custdashboardpage.enterMTADetails(testData, extentedReport, true);

        Log.pass("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
        getquotepage.verifyGetQuotePage(extentedReport);
        Log.pass("Get quote page loaded successfully", driver, extentedReport, true);
        Log.softAssertThat(
            !getquotepage.validateReviewRenewalFields("Automatic Review", "checked", extentedReport,
                true),
            "Automatic Review field displayed as unchecked which is expected",
            "Automatic Review field found to be checked,the unchecked flag during MTA was not saved need to revisist the test case",
            driver);
        Log.softAssertThat(
            !getquotepage.validateReviewRenewalFields("Automatic Renewal", "checked",
                extentedReport, true),
            "Automatic Renewal field displayed as unchecked which is expected",
            "Automatic Renewal field found to be checked,the unchecked flag during MTA was not saved need to revisist the test case",
            driver);
        Log.softAssertThat(
            !getquotepage.validateReviewRenewalFields("Block Renewal", "checked", extentedReport,
                true),
            "Block Renewal field is unchecked as expected",
            "Block Renewal field is checked, the uncheked flag during MTA was not saved need to revisist the test case",
            driver);

      } else {
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


  @Test(
      description = "Check Automatic Review,Automatic Renewal,Block renewal flags are disabled in MTA transaction for the policy entered in Renewal cycle",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_418_419_424(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_418_419_424";
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

        // Perform MTA and verify the flags are unchecked
        custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport);
        Log.pass("Clicked on MTA menu successfully", driver, extentedReport, true);
        // Fill up MTa data
        custdashboardpage.enterMTADetails(testData, extentedReport, true);

        Log.pass("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
        // getquotepage = new GetQuotePage(driver);
        GetQuotePage getquotepage = custdashboardpage.verifyGetQuotePage(extentedReport);
        Log.pass("Get quote page loaded successfully", driver, extentedReport, true);
        Log.softAssertThat(
            getquotepage.validateReviewRenewalFields("Automatic Review", "disabled", extentedReport,
                true),
            "Automatic Review field was disabled",
            "Automatic Review field was enabled, it should be disabled for policy entered in renewal cycle");
        Log.softAssertThat(
            getquotepage.validateReviewRenewalFields("Automatic Renewal", "disabled",
                extentedReport, true),
            "Automatic Renew field was disabled",
            "Automatic Renew field was enabled, it should be disabled for policy entered in renewal cycle");
        Log.softAssertThat(
            getquotepage.validateReviewRenewalFields("Block Renewal", "disabled", extentedReport,
                true),
            "Automatic Renew field was disabled",
            "Automatic Renew field was enabled, it should be disabled for policy entered in renewal cycle");

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


  @Test(
      description = "Check if Block Renewal is checked by the CCA then 'B' with Red mark will display in the Customer Dashboard and verify that blocked renewal icon present in price presentation page when user tries to perform MTA for MTA policy with Blocked renewal status",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_422_423(String browser) throws Exception {
    String tcId = "TC_422_423";
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
        // getquotepage = new GetQuotePage(driver);
        GetQuotePage getquotepage = custdashboardpage.verifyGetQuotePage(extentedReport);
        Log.pass("Get quote page loaded successfully", driver, extentedReport, true);

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
            "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
            true);

        // check for policy status
        policyDetails = null;
        policyDetails = custdashboardpage.getPolicyURN(extentedReport);

        if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {

          Log.pass("MTA Policy Created Successfully in accepted status", driver, extentedReport,
              true);

        } else {
          Log.fail("Failed to create MTA policy in accepted status,it stands in following status:"
              + policyDetails.get("Status").toString(), driver, extentedReport, true);
        }

        Log.softAssertThat(custdashboardpage.verify_blockedRenewalStatus(extentedReport, true),
            "Displays blocked renewal status in customer dashboard page",
            "Failed to display blocked renewal status in customer dashboard page", driver,
            extentedReport, true);

        // Verify that blocked renewal option not removed in MTA page
        // Verify the flags set in STA remains same in MTA cycle
        custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport);
        Log.pass("Clicked on MTA menu successfully", driver, extentedReport, true);
        // Fill up MTa data
        custdashboardpage.enterMTADetails(testData, extentedReport, true);

        Log.pass("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
        getquotepage = custdashboardpage.verifyGetQuotePage(extentedReport);
        Log.pass("Get quote page loaded successfully", driver, extentedReport, true);

        // Check block renewal and verify all the policy flag in MTA
        getquotepage.clickPolicyFlags("Block Renewal", extentedReport, true);

        Log.softAssertThat(
            getquotepage.validateReviewRenewalFields("Block Renewal", "checked", extentedReport,
                true),
            "As expected Block Renewal field appears to be checked",
            "Block Renewal field unchecked which is not expected, it should be in checked state as it was a MTA policy with blocked status");

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

  @Test(
      description = "Check If Automatic Review is unchecked by the CCA then 'R' with Red mark will display in the Customer Dashboard",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_420(String browser) throws Exception {
    String tcId = "TC_420";
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
        // getquotepage = new GetQuotePage(driver);
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
