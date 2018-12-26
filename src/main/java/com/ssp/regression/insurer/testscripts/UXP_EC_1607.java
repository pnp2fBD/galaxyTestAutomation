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
import com.ssp.utils.WaitUtils;
import com.ssp.uxp_pages.CardDetailsPage;
import com.ssp.uxp_pages.CustDashboardPage;
import com.ssp.uxp_pages.GetQuotePage;
import com.ssp.uxp_pages.HomePage;
import com.ssp.uxp_pages.LoginPage;
import com.ssp.uxp_pages.NewQuotePage;
import com.ssp.uxp_pages.SearchPage;

public class UXP_EC_1607 extends BaseTest {
  private String webSite;
  public HashMap<String, String> policyDetails = new HashMap<String, String>();
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

    return Log.testCaseInfo("EC -" + testCaseId + " [" + test + "]",
        "EC_" + testCaseId + " - " + testDesc + " [" + browserwithos + "]", test, "Regression");
  }

  public HashMap<String, String> getTestData(String testcaseId) {
    String env =
        Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("env");
    String className = "UXP_EC_1607_" + env;
    return DataUtils.testDatabyID(testcaseId, className);
  }

  @Test(
      description = "Check that 'Reverse Transaction' modal is displayed on clicking 'Reverse Transaction' option from the Policy Details section",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 0)
  public void TC_212(String browser) throws Exception {

    String tcId = "TC_212";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
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
        String ins_RowtoInteract =
            newquotepage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
        newquotepage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract, true,
            extentedReport);
      }
      newquotepage.clickUpgradeNow(extentedReport);
      newquotepage.clickReCalculate(extentedReport);
      newquotepage.clickBuy(extentedReport);
      newquotepage.clickCustomerPreferenceNext(extentedReport);
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
      newquotepage.verifyPaymentTrasaction(extentedReport);

      Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment was successful", "Payment was not successful", driver, extentedReport, true);

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      Log.message("Policy Number ====>" + policyDetails.get("PolicyNo"), driver, extentedReport);
      newquotepage.clickManagePolicy(extentedReport);
      newquotepage.clickMidTermAdjustment(extentedReport);
      newquotepage.enterDateForMTA(extentedReport);
      newquotepage.selectMidTermAdjReason(MTAcreationReason, extentedReport);
      newquotepage.clickMidTermContinue(extentedReport);
      newquotepage.clickBuyMTA(extentedReport, false);
      custdashboardpage.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
      custdashboardpage.clickAddMTA(extentedReport, false);
      newquotepage.clickAddAllMTANextButton(extentedReport, true);
      newquotepage.clickConfirmMTA(extentedReport);
      newquotepage.clickManagePolicy(extentedReport);
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

  @Test(
      description = "Check that 'Reverse Transaction' option is available under the 'Manage Policy' sub-section after performing MTA in EC application ",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 1)
  public void TC_216(String browser) throws Exception {

    String tcId = "TC_216";
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
      searchPage.enterPolicyNumber(policyDetails.get("PolicyNo"), extentedReport, true);
      searchPage.clickSearch(extentedReport);
      searchPage.selectPolicy_from_SearchPage(true, extentedReport);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport).get();
      custdashboardpage.clickPassVerification(extentedReport, true);
      custdashboardpage.selectTabDropdown("Manage Policy", extentedReport, false);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkReverseTransaction"),
              custdashboardpage),
          "User able to see the 'Reverse Transaction' option under the 'Manage Policy' sub-section after performing MTA",
          "User not able to see the 'Reverse Transaction' option under the 'Manage Policy' sub-section after performing MTA",
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
      description = "Check that 'Reverse Transaction' is done in chronological order with most recent transaction first",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 2)
  public void TC_220(String browser) throws Exception {

    String tcId = "TC_220";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
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
      searchPage.enterPolicyNumber(policyDetails.get("PolicyNo"), extentedReport, true);
      searchPage.clickSearch(extentedReport);
      searchPage.selectPolicy_from_SearchPage(true, extentedReport);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport).get();
      custdashboardpage.clickPassVerification(extentedReport, true);
      custdashboardpage.clickManagePolicy(extentedReport);
      custdashboardpage.clickMidTermAdjustment(extentedReport);
      custdashboardpage.enterDateForMTA(extentedReport);
      custdashboardpage.selectMidTermAdjReason(MTAcreationReason, extentedReport);
      custdashboardpage.clickMidTermContinue(extentedReport);
      GetQuotePage getQuotePage = new GetQuotePage(driver, extentedReport).get();
      // Add the insurance cover items
      String[] coversToAdd = testData.get("coverToSelect_NB").toString().split(",");
      for (int i = 0; i < coversToAdd.length; i++) {
        String ins_RowtoInteract =
            getQuotePage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
        getQuotePage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract, true,
            extentedReport);
      }
      getQuotePage.clickReCalculate(extentedReport);
      getQuotePage.clickBuy(extentedReport);
      getQuotePage.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
      getQuotePage.clickAddMTA(extentedReport, false);
      getQuotePage.clickAddAllMTANextButton(extentedReport, true);
      getQuotePage.clickSaveQuoteButton(extentedReport, true);
      getQuotePage.clickConfirmMTA(extentedReport);
      custdashboardpage.clickManagePolicy(extentedReport);
      custdashboardpage.clickReverseTransaction(extentedReport);
      custdashboardpage.selectReverseTransType("Client Request", extentedReport, true);
      custdashboardpage.clickConfirmReversalButton(extentedReport);
      Log.softAssertThat(custdashboardpage.verifyMTATransactionStatus(),
          "'Reverse Transaction' are in chronological order with most recent transaction first",
          "'Reverse Transaction' are not in chronological order with most recent transaction first",
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
      description = "Check that 'Reverse Transaction' is done for the latest date when multiple MTAs are performed in EC application",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 3)
  public void TC_221(String browser) throws Exception {

    String tcId = "TC_221";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
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
      searchPage.enterPolicyNumber(policyDetails.get("PolicyNo"), extentedReport, true);
      searchPage.clickSearch(extentedReport);
      searchPage.selectPolicy_from_SearchPage(true, extentedReport);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport).get();
      custdashboardpage.clickPassVerification(extentedReport, true);
      custdashboardpage.clickManagePolicy(extentedReport);
      custdashboardpage.clickMidTermAdjustment(extentedReport);
      custdashboardpage.enterfutureDateForMTA(extentedReport, true);
      custdashboardpage.selectMidTermAdjReason(MTAcreationReason, extentedReport);
      custdashboardpage.clickMidTermContinue(extentedReport);
      GetQuotePage getQuotePage = new GetQuotePage(driver, extentedReport).get();
      // Add the insurance cover items
      String[] coversToAdd = testData.get("coverToSelect_NB").toString().split(",");
      for (int i = 0; i < coversToAdd.length; i++) {
        String ins_RowtoInteract =
            getQuotePage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
        getQuotePage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract, true,
            extentedReport);
      }
      getQuotePage.clickReCalculate(extentedReport);

      getQuotePage.clickBuy(extentedReport);
      getQuotePage.selectAvalilableAdjReasons("Remove Cover", extentedReport, false);
      getQuotePage.clickAddMTA(extentedReport, false);
      getQuotePage.clickAddAllMTANextButton(extentedReport, true);
      getQuotePage.clickConfirmMTA(extentedReport);
      custdashboardpage.clickManagePolicy(extentedReport);
      custdashboardpage.clickReverseTransaction(extentedReport);
      custdashboardpage.selectReverseTransType("Client Request", extentedReport, true);
      custdashboardpage.clickConfirmReversalButton(extentedReport);
      Log.softAssertThat(custdashboardpage.verifyMTATStatusAndDate("Inactive: Reversed"),
          "'Reverse Transaction' is made for the latest date when multiple MTAs are performed in EC application",
          "'Reverse Transaction' is not made for the latest date when multiple MTAs are performed in EC application",
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
      description = "Check that 'Reverse Transaction' option is available under the 'Manage Policy' sub-section after performing Renewal in EC application",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 4)
  public void TC_223(String browser) throws Exception {

    String tcId = "TC_223";
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
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      newquotepage.performRenewals(extentedReport, true);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickRenewalQuoteBuy(extentedReport);
      newquotepage.clickPaymentOptionBar(extentedReport, true);
      newquotepage.selectPCLAccountDetails(extentedReport);
      newquotepage.clickManagePolicy(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkReverseTransaction"),
              custdashboardpage),
          "User able to see the 'Reverse Transaction' option under the 'Manage Policy' sub-section after performing MTA",
          "User not able to see the 'Reverse Transaction' option under the 'Manage Policy' sub-section after performing MTA",
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
      description = "Check that 'Reverse Transaction' option is greyed out and NOT selectable under the 'Manage Policy' sub-section after performing New Business in EC application",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 4)
  public void TC_227(String browser) throws Exception {

    String tcId = "TC_227";
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
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      Log.message("Policy Number ====>" + policyDetails.get("PolicyNo"), driver, extentedReport);
      newquotepage.clickManagePolicy(extentedReport);
      Log.softAssertThat(newquotepage.isReverseTransactionLinkDisabled(),
          "'Reverse Transaction' option is greyed out and NOT selectable under the 'Manage Policy' sub-section after performing New Business in EC application ",
          "'Reverse Transaction' option is not greyed out and selectable under the 'Manage Policy' sub-section after performing New Business in EC application",
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
      description = "Check that 'Reverse Transaction' option is greyed out and NOT selectable under the 'Manage Policy' sub-section after creating a New Quote in EC application(Manage Quote tab should NOT display Reverse Transaction tab",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 4)
  public void TC_229(String browser) throws Exception {

    String tcId = "TC_229";
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
      // customerDetails.get("Email");
      testData.get("Email");
      String email = testData.get("Email");
      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
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
      newquotepage.clickQuoteSave("NB", email, extentedReport);
      custdashboardpage.clickManagePolicy(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElementsDoNotExist(
              Arrays.asList("lnkReverseTransactionDisabled"), custdashboardpage),
          "'Reverse Transaction' option is not displayed under the 'Manage Policy' sub-section ",
          "'Reverse Transaction' option is displayed under the 'Manage Policy' sub-section", driver,
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
      description = "Check that 'Reverse Transaction' option is greyed out and NOT selectable under the 'Manage Policy' sub-section after Cancellation in EC application",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 4)
  public void TC_231(String browser) throws Exception {

    String tcId = "TC_231";
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
      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
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
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      custdashboardpage.clickCancelPolicy(extentedReport);
      custdashboardpage.clickStandardCancelletion(extentedReport);
      custdashboardpage.enterRequestCancellationDetails("No Refund", "Better Quote",
          extentedReport);
      newquotepage.clickManagePolicy(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkReverseTransaction"),
              custdashboardpage),
          "'Reverse Transaction' option is greyed out and NOT selectable under the 'Manage Policy' sub-section after performing Cancellation in EC application",
          "'Reverse Transaction' option is not greyed out and NOT selectable under the 'Manage Policy' sub-section after performing Cancellation in EC application",
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
      description = "Check that 'Reverse Transaction' option is greyed out and NOT selectable under the 'Manage Policy' sub-section after Reinstatement in EC application ",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 4)
  public void TC_233(String browser) throws Exception {

    String tcId = "TC_233";
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
      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
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
      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      custdashboardpage.clickCancelPolicy(extentedReport);
      custdashboardpage.clickStandardCancelletion(extentedReport);
      custdashboardpage.enterRequestCancellationDetails("No Refund", "Better Quote",
          extentedReport);
      custdashboardpage.clickReinstatePolicy(true, extentedReport);
      custdashboardpage.selectReinstateReason(testData, true, extentedReport);
      custdashboardpage.clickCalculateForReinstate(false, extentedReport);
      custdashboardpage.clickConfirmForReinstate(true, extentedReport);
      newquotepage.clickManagePolicy(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkReverseTransaction"),
              custdashboardpage),
          "'Reverse Transaction' option is greyed out and NOT selectable under the 'Manage Policy' sub-section after performing Reinstatement in EC application ",
          "'Reverse Transaction' option is not greyed out and NOT selectable under the 'Manage Policy' sub-section after performing Reinstatement in EC application ",
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
      description = "Check that the status of a policy is displayed as 'Inactive and Reversed' when a Reverse Transaction is performed in EC application",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 5)
  public void TC_235(String browser) throws Exception {

    String tcId = "TC_235";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
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
        String ins_RowtoInteract =
            newquotepage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
        newquotepage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract, true,
            extentedReport);
      }
      newquotepage.clickUpgradeNow(extentedReport);
      newquotepage.clickReCalculate(extentedReport);
      newquotepage.clickBuy(extentedReport);
      newquotepage.clickCustomerPreferenceNext(extentedReport);
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
      newquotepage.verifyPaymentTrasaction(extentedReport);

      Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment was successful", "Payment was not successful", driver, extentedReport, true);

      custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
      policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      Log.message("Policy Number ====>" + policyDetails.get("PolicyNo"), driver, extentedReport);
      newquotepage.clickManagePolicy(extentedReport);
      newquotepage.clickMidTermAdjustment(extentedReport);
      newquotepage.enterDateForMTA(extentedReport);
      newquotepage.selectMidTermAdjReason(MTAcreationReason, extentedReport);
      newquotepage.clickMidTermContinue(extentedReport);
      GetQuotePage getQuotePage = new GetQuotePage(driver, extentedReport).get();
      // Add the insurance cover items
      coversToAdd = testData.get("coverToSelect_MT").toString().split(",");
      for (int i = 0; i < coversToAdd.length; i++) {
        String ins_RowtoInteract =
            getQuotePage.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
        getQuotePage.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract, true,
            extentedReport);
      }

      getQuotePage.clickReCalculate(extentedReport);
      Log.message("Adjustment Amount : " + adjustmentAmount);
      getQuotePage.clickBuy(extentedReport);
      getQuotePage.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
      getQuotePage.clickAddMTA(extentedReport, false);
      getQuotePage.clickAddAllMTANextButton(extentedReport, true);
      getQuotePage.clickConfirmMTA(extentedReport);
      custdashboardpage.clickManagePolicy(extentedReport);
      custdashboardpage.clickReverseTransaction(extentedReport);
      custdashboardpage.selectReverseTransType("Client Request", extentedReport, true);
      custdashboardpage.clickConfirmReversalButton(extentedReport);
      Log.softAssertThat(custdashboardpage.verifyMTATStatusAndDate("Inactive: Reversed"),
          "'Reverse Transaction' is made for the latest date when multiple MTAs are performed in EC application",
          "'Reverse Transaction' is not made for the latest date when multiple MTAs are performed in EC application",
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
      description = "Check that the 'Balance' of policy is also reversed upon successful Reverse Transaction in the EC application",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 6)
  public void TC_237(String browser) throws Exception {

    String tcId = "TC_237";
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
      searchPage.enterPolicyNumber(policyDetails.get("PolicyNo"), extentedReport, true);
      searchPage.clickSearch(extentedReport);
      searchPage.selectPolicy_from_SearchPage(true, extentedReport);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport).get();
      custdashboardpage.clickPassVerification(extentedReport, true);
      Log.softAssertThat(custdashboardpage.verifyMTAPremium("£ 0"),
          "'The 'Balance' of policy is reversed upon successful Reverse Transaction in the EC application",
          "'The 'Balance' of policy is not reversed upon successful Reverse Transaction in the EC application",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(groups = "Business_Scenario",
      description = "To perform New business, MTA, Reverse transaction, cancelling policy, Reinstate, MTA and Reverse Transaction",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_008_009_003(String browser) throws Exception {
    String tcId = "BS_008_009_003";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String title = testData.get("Title");
    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");
    try {
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in with User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);
      searchPage.clickCreateCustomer(true, extentedReport);
      testData = searchPage.enterCustomerDetails(testData, true, extentedReport);
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
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);
      Log.softAssertThat(newquotepage.verifyNewQuotePage(true, extentedReport),
          "Verified NewQuotePage ", "Not verified NewQuotePage", driver, extentedReport, true);
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickBuy(extentedReport);
      newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);
      Log.pass("Selected VISA Card", driver, extentedReport, true);
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
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
        custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport);
        Log.pass(" Clicked on MTA menu successfully", driver, extentedReport, true);
        custdashboardpage.enterMTADetails(testData, extentedReport, true);
        Log.pass("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
        GetQuotePage getquotepage = new GetQuotePage(driver, extentedReport);
        getquotepage.verifyGetQuotePage(extentedReport);
        Log.pass("Get quote page loaded successfully", driver, extentedReport, true);
        String[] coversToAdd = testData.get("coverToSelect_NB").toString().split(",");
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
      } else {
        Log.fail("NB Policy has failed to Create in Accepted status, unable to procced with MTA",
            driver, extentedReport, true);
      }
      GetQuotePage getquotepage = new GetQuotePage(driver, extentedReport);
      getquotepage.clickReCalculate(extentedReport);
      getquotepage.clickBuy(extentedReport);
      getquotepage.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
      getquotepage.clickAddMTA(extentedReport, false);
      getquotepage.clickAddAllMTANextButton(extentedReport, true);
      getquotepage.takePayment(extentedReport);
      carddetailspage = getquotepage.selectVisacard(extentedReport);
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      getquotepage = carddetailspage.clickContinueButton_getQuote(extentedReport);
      Log.softAssertThat(getquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment is successful", "Payment is not successful", driver, extentedReport, true);
      newquotepage = getquotepage.confirmPaymentOnMTA("MTA", testData, extentedReport);
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
            "Failed - MTA Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      custdashboardpage.selectTabDropdown("Manage Policy", extentedReport, false);
      custdashboardpage.clickReverseTransaction(extentedReport);
      custdashboardpage.selectReverseTransType("Client Request", extentedReport, true);
      custdashboardpage.clickConfirmReversalButton(extentedReport);
      HashMap<String, String> policyDetailsrnl = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat(
          policyDetailsrnl.get("Position").toString().equalsIgnoreCase("NewBusiness"),
          "Policy position displaying as NewBusiness", "Latest position is not displaying");
      custdashboardpage.clickCancelPolicy(extentedReport);
      custdashboardpage.clickStandardCancelletion(extentedReport);
      custdashboardpage.enterRequestCancellationDetails("No Refund", "Better Quote",
          extentedReport);
      HashMap<String, String> policyDetailcnl = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat(
          policyDetailcnl.get("Position").toString().equalsIgnoreCase("cancellation"),
          "Policy position displaying as cancellation",
          "Latest position is not displaying as cancellation");
      custdashboardpage.clickReinstatePolicy(true, extentedReport);
      custdashboardpage.selectReinstateReason(testData, true, extentedReport);
      custdashboardpage.clickCalculateForReinstate(false, extentedReport);
      WaitUtils.waitForSpinner(driver);
      custdashboardpage.clickConfirmForReinstate(true, extentedReport);
      HashMap<String, String> policyDetailRIN = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat(policyDetailRIN.get("Position").toString().equalsIgnoreCase("NewBusiness"),
          "Policy position displaying as Reinstatement",
          "Latest position is not displaying as Reinstatement");
      // MTA after performing Reinstatement
      CustDashboardPage custdashboardpage1 = new CustDashboardPage(driver, extentedReport);
      custdashboardpage1.clickfromManagePolicyDropdown(testData, true, extentedReport);
      Log.pass(" Clicked on MTA menu successfully", driver, extentedReport, true);
      custdashboardpage1.enterMTADetails(testData, extentedReport, true);
      Log.pass("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
      GetQuotePage getquotepage1 = new GetQuotePage(driver, extentedReport);
      getquotepage1.verifyGetQuotePage(extentedReport);
      Log.pass("Get quote page loaded successfully", driver, extentedReport, true);
      String[] coversToAdd = testData.get("coverToSelect_NB").toString().split(",");
      for (int i = 0; i < coversToAdd.length; i++) {
        boolean BoolVal = false;
        String ins_RowtoInteract =
            getquotepage1.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
        BoolVal = getquotepage1.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract,
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

      GetQuotePage getquotepage11 = new GetQuotePage(driver, extentedReport);
      getquotepage11.clickReCalculate(extentedReport);
      getquotepage11.clickBuy(extentedReport);
      getquotepage11.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
      getquotepage11.clickAddMTA(extentedReport, false);
      getquotepage11.clickAddAllMTANextButton(extentedReport, true);
      getquotepage11.takePayment(extentedReport);
      carddetailspage = getquotepage11.selectVisacard(extentedReport);
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      getquotepage11 = carddetailspage.clickContinueButton_getQuote(extentedReport);
      Log.softAssertThat(getquotepage11.verifyPaymentTrasaction(extentedReport),
          "Payment is successful", "Payment is not successful", driver, extentedReport, true);
      newquotepage = getquotepage11.confirmPaymentOnMTA("MTA", testData, extentedReport);
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
            "Failed - MTA Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      custdashboardpage.selectTabDropdown("Manage Policy", extentedReport, false);
      custdashboardpage.clickReverseTransaction(extentedReport);
      custdashboardpage.selectReverseTransType("Client Request", extentedReport, true);
      custdashboardpage.clickConfirmReversalButton(extentedReport);
      HashMap<String, String> policyDetailRev = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat(policyDetailRev.get("Position").toString().equalsIgnoreCase("NewBusiness"),
          "Policy position displaying as NewBusiness", "Latest position is not displaying");



      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();

    }
  }

  @Test(groups = "Business_Scenario",
      description = "To perform New business, Cancelling Policy and Reinstate the policy",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_007(String browser) throws Exception {
    String tcId = "BS_007";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String title = testData.get("Title");
    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");
    try {
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in with User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);
      searchPage.clickCreateCustomer(true, extentedReport);
      testData = searchPage.enterCustomerDetails(testData, true, extentedReport);
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
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);
      Log.softAssertThat(newquotepage.verifyNewQuotePage(true, extentedReport),
          "Verified NewQuotePage ", "Not verified NewQuotePage", driver, extentedReport, true);
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickBuy(extentedReport);
      newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);
      Log.pass("Selected VISA Card", driver, extentedReport, true);
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
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
      } else {
        Log.fail("NB Policy has failed to Create in Accepted status, unable to procced with MTA",
            driver, extentedReport, true);
      }
      custdashboardpage.clickCancelPolicy(extentedReport);
      custdashboardpage.clickStandardCancelletion(extentedReport);
      custdashboardpage.enterRequestCancellationDetails("No Refund", "Better Quote",
          extentedReport);
      HashMap<String, String> policyDetailcnl = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat(
          policyDetailcnl.get("Position").toString().equalsIgnoreCase("cancellation"),
          "Policy position displaying as cancellation",
          "Latest position is not displaying as cancellation");
      custdashboardpage.clickReinstatePolicy(true, extentedReport);
      custdashboardpage.selectReinstateReason(testData, true, extentedReport);
      custdashboardpage.clickCalculateForReinstate(false, extentedReport);
      WaitUtils.waitForSpinner(driver);
      custdashboardpage.clickConfirmForReinstate(true, extentedReport);
      HashMap<String, String> policyDetailRIN = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat(
          policyDetailRIN.get("Position").toString().equalsIgnoreCase("Reinstatement"),
          "Policy position displaying as Reinstatement",
          "Latest position is not displaying as Reinstatement");
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();

    }
  }

  @Test(groups = "Business_Scenario", description = "Perform New business, Renewal and MTA",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 4)
  public void BS_006(String browser) throws Exception {

    String tcId = "BS_006";
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
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);

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
      // MTA
      CustDashboardPage custdashboardpage1 = new CustDashboardPage(driver, extentedReport);
      custdashboardpage1.clickfromManagePolicyDropdown(testData, true, extentedReport);
      Log.pass(" Clicked on MTA menu successfully", driver, extentedReport, true);
      custdashboardpage1.enterMTADetails(testData, extentedReport, true);
      Log.pass("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
      GetQuotePage getquotepage1 = new GetQuotePage(driver, extentedReport);
      getquotepage1.verifyGetQuotePage(extentedReport);
      Log.pass("Get quote page loaded successfully", driver, extentedReport, true);
      String[] coversToAdd = testData.get("coverToSelect_NB").toString().split(",");
      for (int i = 0; i < coversToAdd.length; i++) {
        boolean BoolVal = false;
        String ins_RowtoInteract =
            getquotepage1.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
        BoolVal = getquotepage1.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract,
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

      GetQuotePage getquotepage11 = new GetQuotePage(driver, extentedReport);
      getquotepage11.clickReCalculate(extentedReport);
      getquotepage11.clickBuy(extentedReport);
      getquotepage11.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
      getquotepage11.clickAddMTA(extentedReport, false);
      getquotepage11.clickAddAllMTANextButton(extentedReport, true);
      getquotepage11.takePayment(extentedReport);
      carddetailspage = getquotepage11.selectVisacard(extentedReport);
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      getquotepage11 = carddetailspage.clickContinueButton_getQuote(extentedReport);
      Log.softAssertThat(getquotepage11.verifyPaymentTrasaction(extentedReport),
          "Payment is successful", "Payment is not successful", driver, extentedReport, true);
      newquotepage = getquotepage11.confirmPaymentOnMTA("MTA", testData, extentedReport);
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
            "Failed - MTA Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(groups = "Business_Scenario",
      description = "To perform New business, MTA, STA, Reverse Transaction",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_004(String browser) throws Exception {
    String tcId = "BS_004";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String title = testData.get("Title");
    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");
    try {
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in with User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);
      searchPage.clickCreateCustomer(true, extentedReport);
      testData = searchPage.enterCustomerDetails(testData, true, extentedReport);
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
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);
      Log.softAssertThat(newquotepage.verifyNewQuotePage(true, extentedReport),
          "Verified NewQuotePage ", "Not verified NewQuotePage", driver, extentedReport, true);
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickBuy(extentedReport);
      newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);
      Log.pass("Selected VISA Card", driver, extentedReport, true);
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
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
      } else {
        Log.fail("NB Policy has failed to Create in Accepted status, unable to procced with MTA",
            driver, extentedReport, true);
      }
      // MTA
      CustDashboardPage custdashboardpage1 = new CustDashboardPage(driver, extentedReport);
      custdashboardpage1.clickfromManagePolicyDropdown(testData, true, extentedReport);
      Log.pass(" Clicked on MTA menu successfully", driver, extentedReport, true);
      custdashboardpage1.enterMTADetails(testData, extentedReport, true);
      Log.pass("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
      GetQuotePage getquotepage1 = new GetQuotePage(driver, extentedReport);
      getquotepage1.verifyGetQuotePage(extentedReport);
      Log.pass("Get quote page loaded successfully", driver, extentedReport, true);
      String[] coversToAdd = testData.get("coverToSelect_NB").toString().split(",");
      for (int i = 0; i < coversToAdd.length; i++) {
        boolean BoolVal = false;
        String ins_RowtoInteract =
            getquotepage1.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
        BoolVal = getquotepage1.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract,
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

      GetQuotePage getquotepage11 = new GetQuotePage(driver, extentedReport);
      getquotepage11.clickReCalculate(extentedReport);
      getquotepage11.clickBuy(extentedReport);
      getquotepage11.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
      getquotepage11.clickAddMTA(extentedReport, false);
      getquotepage11.clickAddAllMTANextButton(extentedReport, true);
      getquotepage11.takePayment(extentedReport);
      CardDetailsPage carddetailspage1 = new CardDetailsPage(driver, extentedReport);
      carddetailspage1 = getquotepage11.selectVisacard(extentedReport);
      carddetailspage1.enterCardNumber(testData, extentedReport, true);
      carddetailspage1.selectExpiry(testData, extentedReport, true);
      carddetailspage1.enterVerification(extentedReport, true);
      carddetailspage1.enterName(testData, extentedReport, true);
      carddetailspage1.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      getquotepage11 = carddetailspage1.clickContinueButton_getQuote(extentedReport);
      Log.softAssertThat(getquotepage11.verifyPaymentTrasaction(extentedReport),
          "Payment is successful", "Payment is not successful", driver, extentedReport, true);
      newquotepage = getquotepage11.confirmPaymentOnMTA("MTA", testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage1.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage1),
          "Navigated to Customer dashboard page", "Not navigated to Customer dashboard page",
          driver, extentedReport, true);
      policyDetails = custdashboardpage1.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {

        Log.pass("MTA Policy Created Successfully in accepted status", driver, extentedReport,
            true);
      } else {
        Log.fail(
            "Failed - MTA Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
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
      WaitUtils.waitForSpinner(driver);
      getQuotePage.clickConfirmMTA(extentedReport);

      policyDetails = custdashboardpage1.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("NewBusiness")) {

        Log.pass("STA Policy Created Successfully in accepted status", driver, extentedReport,
            true);
      } else {
        Log.fail(
            "Failed - STA Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      // Reverse Transaction
      custdashboardpage.selectTabDropdown("Manage Policy", extentedReport, false);
      custdashboardpage.clickReverseTransaction(extentedReport);
      custdashboardpage.selectReverseTransType("Client Request", extentedReport, true);
      custdashboardpage.clickConfirmReversalButton(extentedReport);
      HashMap<String, String> policyDetailsrnl = custdashboardpage.getPolicyURN(extentedReport);
      Log.softAssertThat(
          policyDetailsrnl.get("Position").toString().equalsIgnoreCase("NewBusiness"),
          "Policy position displaying as NewBusiness", "Latest position is not displaying");
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(groups = "Business_Scenario",
      description = "To perform New business, MTA, MTA1 with Quoted State, MTA! with Completed state",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_005(String browser) throws Exception {
    String tcId = "BS_005";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String title = testData.get("Title");
    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");
    try {
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in with User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);
      searchPage.clickCreateCustomer(true, extentedReport);
      testData = searchPage.enterCustomerDetails(testData, true, extentedReport);
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
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);
      Log.softAssertThat(newquotepage.verifyNewQuotePage(true, extentedReport),
          "Verified NewQuotePage ", "Not verified NewQuotePage", driver, extentedReport, true);
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickBuy(extentedReport);
      newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);
      Log.pass("Selected VISA Card", driver, extentedReport, true);
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
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
      } else {
        Log.fail("NB Policy has failed to Create in Accepted status, unable to procced with MTA",
            driver, extentedReport, true);
      }
      // MTA
      CustDashboardPage custdashboardpage1 = new CustDashboardPage(driver, extentedReport);
      custdashboardpage1.clickfromManagePolicyDropdown(testData, true, extentedReport);
      Log.pass(" Clicked on MTA menu successfully", driver, extentedReport, true);
      custdashboardpage1.enterMTADetails(testData, extentedReport, true);
      Log.pass("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
      GetQuotePage getquotepage1 = new GetQuotePage(driver, extentedReport);
      getquotepage1.verifyGetQuotePage(extentedReport);
      Log.pass("Get quote page loaded successfully", driver, extentedReport, true);
      String[] coversToAdd = testData.get("coverToSelect_NB").toString().split(",");
      for (int i = 0; i < coversToAdd.length; i++) {
        boolean BoolVal = false;
        String ins_RowtoInteract =
            getquotepage1.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
        BoolVal = getquotepage1.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract,
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

      GetQuotePage getquotepage11 = new GetQuotePage(driver, extentedReport);
      getquotepage11.clickReCalculate(extentedReport);
      getquotepage11.clickBuy(extentedReport);
      getquotepage11.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
      getquotepage11.clickAddMTA(extentedReport, false);
      getquotepage11.clickAddAllMTANextButton(extentedReport, true);
      getquotepage11.takePayment(extentedReport);
      CardDetailsPage carddetailspage1 = new CardDetailsPage(driver, extentedReport);
      carddetailspage1 = getquotepage11.selectVisacard(extentedReport);
      carddetailspage1.enterCardNumber(testData, extentedReport, true);
      carddetailspage1.selectExpiry(testData, extentedReport, true);
      carddetailspage1.enterVerification(extentedReport, true);
      carddetailspage1.enterName(testData, extentedReport, true);
      carddetailspage1.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      getquotepage11 = carddetailspage1.clickContinueButton_getQuote(extentedReport);
      Log.softAssertThat(getquotepage11.verifyPaymentTrasaction(extentedReport),
          "Payment is successful", "Payment is not successful", driver, extentedReport, true);
      newquotepage = getquotepage11.confirmPaymentOnMTA("MTA", testData, extentedReport);
      Log.softAssertThat(
          custdashboardpage1.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage1),
          "Navigated to Customer dashboard page", "Not navigated to Customer dashboard page",
          driver, extentedReport, true);
      policyDetails = custdashboardpage1.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {

        Log.pass("MTA Policy Created Successfully in accepted status", driver, extentedReport,
            true);
      } else {
        Log.fail(
            "Failed - MTA Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      // MTA Quote
      CustDashboardPage custdashboardpage11 = new CustDashboardPage(driver, extentedReport);
      custdashboardpage11.clickfromManagePolicyDropdown(testData, true, extentedReport);
      Log.pass(" Clicked on MTA menu successfully", driver, extentedReport, true);
      custdashboardpage11.enterMTADetails(testData, extentedReport, true);
      Log.pass("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
      GetQuotePage getquotepage2 = new GetQuotePage(driver, extentedReport);
      getquotepage2.verifyGetQuotePage(extentedReport);
      Log.pass("Get quote page loaded successfully", driver, extentedReport, true);
      String[] coversToAdd1 = testData.get("coverToSelect_MT").toString().split(",");
      for (int i = 0; i < coversToAdd1.length; i++) {
        boolean BoolVal = false;
        String ins_RowtoInteract =
            getquotepage2.SelectInsuranceItem(coversToAdd1[i], true, extentedReport);
        BoolVal = getquotepage2.enterInsCoverDetails(testData, coversToAdd1[i], ins_RowtoInteract,
            true, extentedReport);
        String[] coverType = coversToAdd1[i].split("_");
        if (BoolVal != false) {
          Log.pass(coverType[2] + " " + coverType[0] + "_" + coverType[1]
              + " cover type done successfully", driver, extentedReport, true);
        } else {
          Log.fail("Failed to " + coverType[2] + " " + coverType[0] + "_" + coverType[1] + " cover",
              driver, extentedReport, true);
        }
      }
      GetQuotePage getquotepage3 = new GetQuotePage(driver, extentedReport);
      getquotepage3.clickReCalculate(extentedReport);
      getquotepage3.clickSavedButton(extentedReport);
      Log.softAssertThat(
          custdashboardpage1.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage1),
          "Navigated to Customer dashboard page", "Not navigated to Customer dashboard page",
          driver, extentedReport, true);
      custdashboardpage.selectTransactionRow(extentedReport);
      custdashboardpage.clickEditTransactionButton(extentedReport, true);

      getquotepage11.clickBuy(extentedReport);
      getquotepage11.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
      getquotepage11.clickAddMTA(extentedReport, false);
      getquotepage11.clickAddAllMTANextButton(extentedReport, true);
      WaitUtils.waitForSpinner(driver);

      getquotepage11.clickConfirmMTA(extentedReport);
      Log.softAssertThat(
          custdashboardpage1.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage1),
          "Navigated to Customer dashboard page", "Not navigated to Customer dashboard page",
          driver, extentedReport, true);
      policyDetails = custdashboardpage1.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {

        Log.pass("MTA Policy Created Successfully in accepted status", driver, extentedReport,
            true);
      } else {
        Log.fail(
            "Failed - MTA Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(groups = "Business_Scenario",
      description = "To perform New business, MTA1 with Add Premium, MTA2 with Remove premium, MTA3 with Zero Premium,Reversed all transactions and MTA with Add Premium",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_001(String browser) throws Exception {
    String tcId = "BS_001";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String title = testData.get("Title");
    ExtentTest extentedReport = addTestInfo(tcId, description);
    String Corecover = testData.get("Cover");
    try {
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into UXP Home Page",
          "Login failed", driver, extentedReport, true);
      Log.message("Logged in with User id:" + userName + "& Password:" + password, driver,
          extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);
      searchPage.clickCreateCustomer(true, extentedReport);
      testData = searchPage.enterCustomerDetails(testData, true, extentedReport);
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
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);
      Log.softAssertThat(newquotepage.verifyNewQuotePage(true, extentedReport),
          "Verified NewQuotePage ", "Not verified NewQuotePage", driver, extentedReport, true);
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, Corecover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickBuy(extentedReport);
      newquotepage.clickCustomerPreferenceNext(extentedReport);
      newquotepage.selectPayment(testData, true, extentedReport);
      newquotepage.takePayment(extentedReport);
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);
      Log.pass("Selected VISA Card", driver, extentedReport, true);
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
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);
      HashMap<String, String> policyDetails = custdashboardpage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
        Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport, true);
        // MTA with Add Premium
        custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport);
        Log.pass(" Clicked on MTA menu successfully", driver, extentedReport, true);
        custdashboardpage.enterMTADetails(testData, extentedReport, true);
        Log.pass("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
        GetQuotePage getquotepage = new GetQuotePage(driver, extentedReport);
        getquotepage.verifyGetQuotePage(extentedReport);
        Log.pass("Get quote page loaded successfully", driver, extentedReport, true);
        String[] coversToAdd = testData.get("coverToSelect_NB").toString().split(",");
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
      } else {
        Log.fail("NB Policy has failed to Create in Accepted status, unable to procced with MTA",
            driver, extentedReport, true);
      }
      GetQuotePage getquotepage = new GetQuotePage(driver, extentedReport);
      getquotepage.clickReCalculate(extentedReport);
      getquotepage.clickBuy(extentedReport);
      getquotepage.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
      getquotepage.clickAddMTA(extentedReport, false);
      getquotepage.clickAddAllMTANextButton(extentedReport, true);
      getquotepage.takePayment(extentedReport);
      carddetailspage = getquotepage.selectVisacard(extentedReport);
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      getquotepage = carddetailspage.clickContinueButton_getQuote(extentedReport);
      Log.softAssertThat(getquotepage.verifyPaymentTrasaction(extentedReport),
          "Payment is successful", "Payment is not successful", driver, extentedReport, true);
      newquotepage = getquotepage.confirmPaymentOnMTA("MTA", testData, extentedReport);
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
            "Failed - MTA Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      // MTA with Remove Premium
      custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport);
      Log.pass(" Clicked on MTA menu successfully", driver, extentedReport, true);
      custdashboardpage.enterMTADetails(testData, extentedReport, true);
      Log.pass("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
      GetQuotePage getquotepage1 = new GetQuotePage(driver, extentedReport);
      getquotepage1.verifyGetQuotePage(extentedReport);
      Log.pass("Get quote page loaded successfully", driver, extentedReport, true);
      String[] coversToAdd = testData.get("coverToSelect_NB").toString().split(",");
      for (int i = 0; i < coversToAdd.length; i++) {
        boolean BoolVal = false;
        String ins_RowtoInteract =
            getquotepage1.SelectInsuranceItem(coversToAdd[i], true, extentedReport);
        BoolVal = getquotepage1.enterInsCoverDetails(testData, coversToAdd[i], ins_RowtoInteract,
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

      GetQuotePage getquotepage2 = new GetQuotePage(driver, extentedReport);
      getquotepage2.clickReCalculate(extentedReport);
      getquotepage2.clickBuy(extentedReport);
      getquotepage2.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
      getquotepage2.clickAddMTA(extentedReport, false);
      getquotepage2.clickAddAllMTANextButton(extentedReport, true);
      getquotepage2.takePayment(extentedReport);
      carddetailspage = getquotepage2.selectVisacard(extentedReport);
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      getquotepage2 = carddetailspage.clickContinueButton_getQuote(extentedReport);
      Log.softAssertThat(getquotepage2.verifyPaymentTrasaction(extentedReport),
          "Payment is successful", "Payment is not successful", driver, extentedReport, true);
      newquotepage = getquotepage2.confirmPaymentOnMTA("MTA", testData, extentedReport);
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
            "Failed - MTA Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      // MTA With Zero Premium
      custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport);
      Log.pass(" Clicked on MTA menu successfully", driver, extentedReport, true);
      custdashboardpage.enterMTADetails(testData, extentedReport, true);
      Log.pass("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
      GetQuotePage getquotepage4 = new GetQuotePage(driver, extentedReport);
      getquotepage4.verifyGetQuotePage(extentedReport);
      Log.pass("Get quote page loaded successfully", driver, extentedReport, true);
      GetQuotePage getquotepage3 = new GetQuotePage(driver, extentedReport);
      getquotepage3.clickReCalculate(extentedReport);
      getquotepage3.clickBuy(extentedReport);
      getquotepage3.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
      getquotepage3.clickAddMTA(extentedReport, false);
      getquotepage3.clickAddAllMTANextButton(extentedReport, true);
      getquotepage3.takePayment(extentedReport);
      carddetailspage = getquotepage3.selectVisacard(extentedReport);
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      getquotepage3 = carddetailspage.clickContinueButton_getQuote(extentedReport);
      Log.softAssertThat(getquotepage3.verifyPaymentTrasaction(extentedReport),
          "Payment is successful", "Payment is not successful", driver, extentedReport, true);
      newquotepage = getquotepage3.confirmPaymentOnMTA("MTA", testData, extentedReport);
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
            "Failed - MTA Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      // Reverse Transaction
      for (int i = 1; i <= 3; i++) {
        custdashboardpage.selectTabDropdown("Manage Policy", extentedReport, false);
        custdashboardpage.clickReverseTransaction(extentedReport);
        custdashboardpage.selectReverseTransType("Client Request", extentedReport, true);
        custdashboardpage.clickConfirmReversalButton(extentedReport);
        HashMap<String, String> policyDetailsrnl = custdashboardpage.getPolicyURN(extentedReport);
        Log.softAssertThat(
            policyDetailsrnl.get("Position").toString().equalsIgnoreCase("NewBusiness"),
            "Policy position displaying as NewBusiness", "Latest position is not displaying");
      }
      // Final MTA with Add Premium
      custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport);
      Log.pass(" Clicked on MTA menu successfully", driver, extentedReport, true);
      custdashboardpage.enterMTADetails(testData, extentedReport, true);
      Log.pass("Entered Quote Details and Clicked on Confirm", driver, extentedReport, true);
      GetQuotePage getquotePage = new GetQuotePage(driver, extentedReport);
      getquotePage.verifyGetQuotePage(extentedReport);
      Log.pass("Get quote page loaded successfully", driver, extentedReport, true);
      String[] coversToAdd1 = testData.get("coverToSelect_NB").toString().split(",");
      for (int i = 0; i < coversToAdd1.length; i++) {
        boolean BoolVal = false;
        String ins_RowtoInteract =
            getquotePage.SelectInsuranceItem(coversToAdd1[i], true, extentedReport);
        BoolVal = getquotePage.enterInsCoverDetails(testData, coversToAdd1[i], ins_RowtoInteract,
            true, extentedReport);
        String[] coverType = coversToAdd1[i].split("_");
        if (BoolVal != false) {
          Log.pass(coverType[2] + " " + coverType[0] + "_" + coverType[1]
              + " cover type done successfully", driver, extentedReport, true);
        } else {
          Log.fail("Failed to " + coverType[2] + " " + coverType[0] + "_" + coverType[1] + " cover",
              driver, extentedReport, true);
        }
      }
      GetQuotePage getquotepage5 = new GetQuotePage(driver, extentedReport);
      getquotepage5.clickReCalculate(extentedReport);
      getquotepage5.clickBuy(extentedReport);
      getquotepage5.selectAvalilableAdjReasons("Add Cover", extentedReport, false);
      getquotepage5.clickAddMTA(extentedReport, false);
      getquotepage5.clickAddAllMTANextButton(extentedReport, true);
      getquotepage5.takePayment(extentedReport);
      carddetailspage = getquotepage5.selectVisacard(extentedReport);
      carddetailspage.enterCardNumber(testData, extentedReport, true);
      carddetailspage.selectExpiry(testData, extentedReport, true);
      carddetailspage.enterVerification(extentedReport, true);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      getquotepage5 = carddetailspage.clickContinueButton_getQuote(extentedReport);
      Log.softAssertThat(getquotepage5.verifyPaymentTrasaction(extentedReport),
          "Payment is successful", "Payment is not successful", driver, extentedReport, true);
      newquotepage = getquotepage5.confirmPaymentOnMTA("MTA", testData, extentedReport);
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
            "Failed - MTA Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }


  }
}

