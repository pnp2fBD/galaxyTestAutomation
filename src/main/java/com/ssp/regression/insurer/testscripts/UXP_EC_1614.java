package com.ssp.regression.insurer.testscripts;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.ssp.uxp_pages.*;
import com.ssp.support.*;
import com.ssp.utils.*;

@Listeners(EmailReport.class)
public class UXP_EC_1614 extends BaseTest {

  private String webSite;
  public String monthlywebSite;
  String policyNumber = "";
  String firstName = "";
  String lastName_TC001 = "";
  String contactName = "";
  String quoteDescription1 = "TestDescription 1";
  String quoteDescription2 = "TestDescription 2";
  String quoteDescription3 = "TestDescription 3";
  String renewQuoteDesc = "Renewal Quote1";

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
    String className = "UXP_EC_1614_" + env;
    return DataUtils.testDatabyID(testcaseId, className);
  }

  @Test(
      description = "Check that the Quote description popup window is prompted for quote description (Save/Cancel button), user lands in the the Acceptance page, user is allowed to return to Data Capture and verify the status of Renewed Quote and user should return to datacapture when cancel button clicked in description pop up",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 0)
  public void TC_831_832_833_836_838_835(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_831_832_833_836_838_835";
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

        // To verify that acceptance tab selected by default or not
        Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
            "Acceptance tab was selected by default when clicked on quote variations",
            "Acceptance tab was not selected as default tab when clicked on quote variations",
            driver, extentedReport, true);

        buyQuotePage.checkQuotePage_traverse(testData.get("tabToClick"), extentedReport, true);
        Log.pass("Naviagated to " + testData.get("tabToClick") + " page successfully", driver,
            extentedReport, true);

        Log.softAssertThat(buyQuotePage.clickDataCaptureandVerify(extentedReport, true),
            "Successfully navigated to data catpure page",
            "Failed to navigate to data capture from price presentation page", driver,
            extentedReport, true);

        // Click Save in data capture page and verify
        buyQuotePage.clickNextOne(extentedReport);
        // newquotepage.enterCustomerDetails(testData, true, Corecover,
        // extentedReport);
        buyQuotePage.clickNextTwo(extentedReport);
        buyQuotePage.clickView(extentedReport);
        buyQuotePage.clickAgree(extentedReport);
        buyQuotePage.clickSaveDataCapture(extentedReport, true);
        Log.softAssertThat(buyQuotePage.enterQuoteDescVerifybuttons("cancel", extentedReport, true),
            "After clicking cancel button in variation description dialog, user returned to data capture page",
            "After clicking cancel button in variation description dialog, user was not taken to data capture page",
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
      driver.quit();
    } // finally

  }

  @Test(description = "Check that the user is allowed to Review/Amend T’s and C’s details",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 3)
  public void TC_842(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_842";
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
        // Edit T& C
        // Log.softAssertThat(buyQuotePage.verifyEditTermAndCondition(extentedReport),
        // "Verified Edit Terms and Conditions", "Not able to verify
        // Edit Terms and Conditions", driver,
        // extentedReport, true);
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
      driver.quit();
    } // finally

  }

  @Test(
      description = "Verify that user able to update Payment Plan / Method details in policy renewal,Check that the Manage Renewals and Renewal Quote Variation tabs are no longer be available on the Customer Dashboard, when the quote variation is bought and verify the policy status changes to Renewed",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 4)
  public void TC_843_845_853(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_843_845_853";
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
        buyQuotePage.selectPaymentPlan("Monthly", extentedReport);
        buyQuotePage.selectPaymentMethod("Direct Debit", extentedReport, true);
        buyQuotePage.addAccountDetail(testData, extentedReport, true);
        buyQuotePage.clickCutomerAgreeButton(extentedReport);
        buyQuotePage.clickAcceptBtn(extentedReport);
        custdashboardpage.verifyCustomerDashboardPage();

        policyDetails = custdashboardpage.getPolicyURN(extentedReport);
        Log.softAssertThat(
            policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")
                && policyDetails.get("Position").toString().equalsIgnoreCase("Renewal"),
            "User able to change the payment plan and method in renewal quote variation ",
            "Policy was not created in accepted status when payment method/plan updated in renew quote variation, it stands in :"
                + policyDetails.get("Status"));

        // verify renewal tab not visible

        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElementsDoNotExist(
                Arrays.asList("drpRenewQotVariations", "drpManageRenewals"), custdashboardpage),
            "Manage Renewal and Renewal Quote Varation tabs were removed from customer dashboard page after completing payment",
            "Manage Renewal and Renewal Quote Varation tabs were not removed from customer dashboard page after completing payment",
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
      driver.quit();
    } // finally

  }

  @Test(
      description = "Verify the user is able to update the Change payor details, Check that the warning message is prompted when user performs lapse from the customer dashboard and user should return to the customer dashboard when click cancel in the warning message and verify that quotes are lapsed when clicked continue button",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 5)
  public void TC_844_870_871_872(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_844_870_871_872";
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
        buyQuotePage.clickCheckboxinPayment(extentedReport, true);
        // buyQuotePage.addAccountDetail(testData, extentedReport,
        // true);
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
        newquotepage = carddetailspage.clickContinueButton(extentedReport);
        Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
            "Payment was successful", "Payment was not successful", driver, extentedReport, true);

        custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Customer Dashboard page is verified", "Customer Dashboard page not verified", driver,
            extentedReport, true);

        // Click and Verify Quote Lapse option in customer dashboard
        // page
        custdashboardpage.performRenewals_LapseRenewal("Lapse Quote", extentedReport, true);
        Log.softAssertThat(custdashboardpage.VerifyLapseRenewalModal(extentedReport, true),
            "Lapse Quote button was clicked and verified the warning message / continue / Cancel button",
            "Warning message / continue / Cancel button verification during Lapse Quote was not successful",
            driver, extentedReport, true);

        custdashboardpage.clickLapseCancelContinue("cancel", extentedReport, true);

        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "User returned to Customer Dashboard page on clicking cancel button in Lapse quote warning modal",
            "User not returned to Customer Dashboard page on clicking cancel button in Lapse quote warning modal",
            driver, extentedReport, true);

        // Perform Lapse quote from customer dashboard page
        custdashboardpage.performRenewals_LapseRenewal("Lapse Quote", extentedReport, true);
        Log.softAssertThat(custdashboardpage.VerifyLapseRenewalModal(extentedReport, true),
            "Lapse Quote button was clicked and verified the warning message / continue / Cancel button",
            "Warning message / continue / Cancel button verification during Lapse Quote was not successful",
            driver, extentedReport, true);

        custdashboardpage.clickLapseCancelContinue("continue", extentedReport, true);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
            true);
        // Add steps to verify all the4 quotes are lapsed
        HashMap<String, String> hmQuotedetails =
            custdashboardpage.getQuoteVariations(extentedReport, true);
        if (hmQuotedetails.get("quoteStatus_0").toString().contentEquals("L")
            && hmQuotedetails.get("quoteStatus_1").toString().contentEquals("L"))
          Log.pass(
              "Quote variations in customer dashboard page are listed as review required and lapsed",
              extentedReport);

        else
          Log.fail(
              "Quote variations in customer dashboard were not listed listed as review required and lapsed",
              extentedReport);

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
      description = "Verify the quote variation status when save at price presentation page and Renewal Quote variation tab has Review required and lapse quotes in drop down option and check that user is returned to Price presentation, when clicked on 'Cancel' or 'X' options in the popup window",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 1)
  public void TC_839_840(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_839_840";
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
        newquotepage.clickQuoteSave("Renew", "Quote1", extentedReport);

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

        // To verify that acceptance tab selected by default or not
        Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
            "Acceptance tab was selected by default when clicked on quote variations",
            "Acceptance tab was not selected as default tab when clicked on quote variations",
            driver, extentedReport, true);

        buyQuotePage.checkQuotePage_traverse(testData.get("tabToClick"), extentedReport, true);
        Log.pass("Naviagated to " + testData.get("tabToClick") + " page successfully", driver,
            extentedReport, true);

        // Log.softAssertThat(buyQuotePage.clickDataCaptureandVerify(extentedReport,true),
        // "Successfully navigated to data catpure page", "Failed to
        // navigate to data capture from price presentation page",
        // driver, extentedReport, true);
        //
        // Add few elements to the quote and validate the price amount
        // and quote status in customer dashboard page

        buyQuotePage.clickUpgradeNow(extentedReport);
        buyQuotePage.verifyUpgradePolicy(extentedReport);
        buyQuotePage.clickReCalculate(extentedReport);

        Log.softAssertThat(
            buyQuotePage.clickCancelandClosebuttonppPageandVerify("cancel", extentedReport, true),
            "Quote descritpion modal closed after clicking Cancel button and user remains in price presentation page",
            "Quote descritpion modal not closed after clicking Cancel button", driver,
            extentedReport, true);
        Log.softAssertThat(
            buyQuotePage.clickCancelandClosebuttonppPageandVerify("close", extentedReport, true),
            "Quote descritpion modal closed after clicking Close button and user remains in price presentation page",
            "Quote descritpion modal not closed after clicking Close button", driver,
            extentedReport, true);

        buyQuotePage.clickSavebuttonForRenewalQuoteinPPpage("Quote2", extentedReport);

        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
            true);

        HashMap<String, String> hmQuotedetails =
            custdashboardpage.getQuoteVariations(extentedReport, true);
        if (hmQuotedetails.get("quoteStatus_0").toString().contentEquals("R")
            && hmQuotedetails.get("quoteStatus_1").toString().contentEquals("L"))
          Log.pass(
              "Quote variations in customer dashboard page are listed as review required and lapsed",
              extentedReport);
        else
          Log.fail(
              "Quote variations in customer dashboard were not listed listed as review required and lapsed",
              extentedReport);

        // Verify Acceptance page is landed

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
      description = "Check the warning message when lapse the renewal quote variation from Acceptance page and check that renewal quote variation is set as a lapse",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 8)
  public void TC_854_855(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_854_855";
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

        // To verify that acceptance tab selected by default or not
        Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
            "Acceptance tab was selected by default when clicked on quote variations",
            "Acceptance tab was not selected as default tab when clicked on quote variations",
            driver, extentedReport, true);
        // Click lapse button in acceptance page
        buyQuotePage.clicksave_LapsebuttoninAcceptancepage("", "", "Lapse", extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Navigated to custdashboardpage successfully after quote lapse",
            "Failed to Navigate to custdashboardpage after quote lapse", driver, extentedReport,
            true);
        // Verify the lapsed status of all the quotes
        HashMap<String, String> hmQuotedetails =
            custdashboardpage.getQuoteVariations(extentedReport, true);
        if (hmQuotedetails.get("quoteStatus_0").toString().contentEquals("L")
            && hmQuotedetails.get("quoteStatus_1").toString().contentEquals("L"))
          Log.pass(
              "Quote variations in customer dashboard page are listed as Lapsed after clicking lapse from acceptance page",
              extentedReport);
        else
          Log.fail(
              "Quote variations in customer dashboard were not listed as Lapsed after clicking lapse from acceptance page",
              extentedReport);

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
      description = "Check that the renewal quote is saved as a new variation with a status of Reviewed, when it is saved from Acceptance page",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 7)
  public void TC_852(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_852";
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
      // Past date for renewal
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

        // To verify that acceptance tab selected by default or not
        Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
            "Acceptance tab was selected by default when clicked on quote variations",
            "Acceptance tab was not selected as default tab when clicked on quote variations",
            driver, extentedReport, true);
        // Click save button in acceptance page
        buyQuotePage.clicksave_LapsebuttoninAcceptancepage("Review Required", "quoteDescrption",
            "Save", extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Navigated to custdashboardpage successfully after quote lapse",
            "Failed to Navigate to custdashboardpage after quote lapse", driver, extentedReport,
            true);
        // Verify the lapsed status of all the quotes
        HashMap<String, String> hmQuotedetails =
            custdashboardpage.getQuoteVariations(extentedReport, true);
        if (hmQuotedetails.get("quoteStatus_0").toString().contentEquals("R")
            && hmQuotedetails.get("quoteStatus_1").toString().contentEquals("L"))
          Log.pass(
              "Renew Quote variations in customer dashboard page are listed as review required and lapsed",
              extentedReport);
        else
          Log.fail(
              "Renew Quote variations in customer dashboard were not listed listed as review required and lapsed",
              extentedReport);

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

  @Test(description = "Check that the user is allowed to update the User Preferences details",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 2)
  public void TC_841(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_841";
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
        newquotepage.clickQuoteSave("Renew", "Quote1", extentedReport);

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

        // To verify that acceptance tab selected by default or not
        Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
            "Acceptance tab was selected by default when clicked on quote variations",
            "Acceptance tab was not selected as default tab when clicked on quote variations",
            driver, extentedReport, true);

        buyQuotePage.checkQuotePage_traverse(testData.get("tabToClick"), extentedReport, true);
        Log.pass("Naviagated to " + testData.get("tabToClick") + " page successfully", driver,
            extentedReport, true);

        buyQuotePage.clickBuy(extentedReport);

        Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
            "User was navigated to acceptance on clicking buy button in price presentation page",
            "User was not navigated to acceptance on clicking buy button in price presentation page",
            driver, extentedReport, true);
        buyQuotePage.clickEditPreferences(extentedReport, true);
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
        newquotepage = carddetailspage.clickContinueButton(extentedReport);
        Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
            "Payment was successful", "Payment was not successful", driver, extentedReport, true);

        custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Customer Dashboard page is verified", "Customer Dashboard page not verified", driver,
            extentedReport, true);

        // Verify the edited preferences on customer dashboard page

        Log.softAssertThat(custdashboardpage.verifyPreferences("Post", extentedReport, true),
            "Customer dashboard page has the selected preferences during quote renewal",
            "Customer dashboard page failed to haveselected preferences during quote renewal");

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
      description = "Check that the descriptin popup window is prompted and Check that the 'continue' button is enabled when description entered and check user is returned to Acceptance page, when click on 'cancel' in the popup window and check the reviewed quote status",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 6)
  public void TC_848_849_850_851(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_848_849_850_851";
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

        // To verify that acceptance tab selected by default or not
        Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
            "Acceptance tab was selected by default when clicked on quote variations",
            "Acceptance tab was not selected as default tab when clicked on quote variations",
            driver, extentedReport, true);
        // Click lapse button in acceptance page

        Log.softAssertThat(
            buyQuotePage.clicksave_LapsebuttoninAcceptancepage("", "QuoteVariationCancel", "Cancel",
                extentedReport),
            "Quote description pop up getting closed when cancel button clicked",
            "Quote description pop up did not close when cancel button clicked", driver,
            extentedReport, true);

        // buyQuotePage.clicksave_LapsebuttoninAcceptancepage("quoteDescription",
        // "Save", extentedReport);
        buyQuotePage.clicksave_LapsebuttoninAcceptancepage("Review Required", "quoteDescrption",
            "Save", extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Navigated to custdashboardpage successfully after quote Save",
            "Failed to Navigate to custdashboardpage after quote Save", driver, extentedReport,
            true);
        // Verify the lapsed status of all the quotes
        HashMap<String, String> hmQuotedetails =
            custdashboardpage.getQuoteVariations(extentedReport, true);
        if (hmQuotedetails.get("quoteStatus_0").toString().contentEquals("R")
            && hmQuotedetails.get("quoteStatus_1").toString().contentEquals("L"))
          Log.pass(
              "Quote variations in customer dashboard page are listed as Lapsed after clicking lapse from acceptance page",
              extentedReport);
        else
          Log.fail(
              "Quote variations in customer dashboard were not listed as Lapsed after clicking lapse from acceptance page",
              extentedReport);

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
      description = "Verify that user able to complete payment for a lapsed quote",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider",
      priority = 13)
  public void BS_014(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_014";
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
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
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

        custdashboardpage.performRenewals_LapseRenewal("Lapse", extentedReport, true);

        custdashboardpage.clickLapseCancelContinue("continue", extentedReport, true);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
            true);
        // Add steps to verify all the quotes are lapsed
        HashMap<String, String> hmQuotedetails =
            custdashboardpage.getQuoteVariations(extentedReport, true);
        if (hmQuotedetails.get("quoteStatus_0").toString().contentEquals("L")) {
          Log.pass("Quote variations in customer dashboard page are listed as lapsed",
              extentedReport);

          // click quote variations
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

          // buyQuotePage.clickCheckboxinPayment(extentedReport,
          // true);
          // buyQuotePage.addAccountDetail(testData,
          // extentedReport,true);
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
              "Customer Dashboard page verified and user able to complete payment transaction for lapsed renewal quote",
              "Customer Dashboard page not verified", driver, extentedReport, true);
        } else
          Log.fail("Quote variations in customer dashboard were not listed listed lapsed",
              extentedReport);

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
      description = "Verify that user able to complete payment for a lapsed quote",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider",
      priority = 14)
  public void BS_015_025(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_015_025";
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
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
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

        custdashboardpage.performRenewals_LapseRenewal("Lapse", extentedReport, true);

        custdashboardpage.clickLapseCancelContinue("continue", extentedReport, true);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
            true);
        // Add steps to verify all the quotes are lapsed
        HashMap<String, String> hmQuotedetails =
            custdashboardpage.getQuoteVariations(extentedReport, true);
        if (hmQuotedetails.get("quoteStatus_0").toString().contentEquals("L")) {
          Log.pass("Quote variations in customer dashboard page are listed as lapsed",
              extentedReport);

          // click quote variations
          BuyQuotePage buyQuotePage =
              custdashboardpage.performRenewals_QuoteVariation(extentedReport, true);

          // Verify Acceptance page is landed
          Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
              "Acceptance tab was selected by default when clicked on quote variations",
              "Acceptance tab was not selected as default tab when clicked on quote variations",
              driver, extentedReport, true);
          // Save the lapsed quote and again click the saved quote and
          // complete payment
          buyQuotePage.clicksave_LapsebuttoninAcceptancepage("Review Required", "quoteDescription",
              "Save", extentedReport);
          Log.softAssertThat(
              custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                  custdashboardpage),
              "Navigated to custdashboardpage successfully after quote lapse",
              "Failed to Navigate to custdashboardpage after quote lapse", driver, extentedReport,
              true);
          // Verify the lapsed status of all the quotes

          hmQuotedetails = custdashboardpage.getQuoteVariations(extentedReport, true);
          if (hmQuotedetails.get("quoteStatus_0").toString().contentEquals("R"))
            Log.pass(
                "Renew Quote variations in customer dashboard page lists as review required after editing the lapsed quote",
                extentedReport);
          else
            Log.fail(
                "Renew Quote variations in customer dashboard page failed to list as review required after editing the lapsed quote",
                extentedReport);

          custdashboardpage.performRenewals_QuoteVariation(extentedReport, true);

          // Verify Acceptance page is landed
          Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
              "Acceptance tab was selected by default when clicked on quote variations",
              "Acceptance tab was not selected as default tab when clicked on quote variations",
              driver, extentedReport, true);

          // update payment method
          buyQuotePage.selectPaymentPlan("Annual", extentedReport);
          buyQuotePage.selectPaymentMethod("Card", extentedReport, true);

          // buyQuotePage.clickCheckboxinPayment(extentedReport,
          // true);
          // buyQuotePage.addAccountDetail(testData,
          // extentedReport,true);
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
              "Customer Dashboard page verified and user able to complete payment transaction for lapsed renewal quote",
              "Customer Dashboard page not verified", driver, extentedReport, true);

          // Canceling a Policy
          custdashboardpage.clickCancelPolicy(testData, true, extentedReport);
          custdashboardpage.enterReason(testData.get("Cancellation Reason"), extentedReport);
          custdashboardpage.enterPremiumType(testData, extentedReport);

          custdashboardpage.clickCalculate(true, extentedReport);
          custdashboardpage.clickConfirm(true, extentedReport);

          // Verifying that policy is cancelled
          Log.softAssertThat(custdashboardpage.verifyCancellation(true, extentedReport),
              "Policy cancelled successfully", "Policy is not cancelled", driver, extentedReport,
              true);

          // Reinstating a Policy
          custdashboardpage.clickReinstatePolicy(true, extentedReport);
          custdashboardpage.selectReinstateReason(testData, true, extentedReport);
          custdashboardpage.clickCalculateForReinstate(true, extentedReport);
          custdashboardpage.clickConfirmForReinstate(true, extentedReport);

          // Verifying that Policy is reinstated
          Log.softAssertThat(custdashboardpage.verifyReinstation(true, extentedReport),
              "Policy Reinstated successfully", "Policy is not reinstated", driver, extentedReport,
              true);

        } else
          Log.fail("Quote variations in customer dashboard were not listed listed lapsed",
              extentedReport);

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
      description = "Verify that more renewal variations can be viewed in the quote variations dropdown and review the icons displayed and perform renewal for the most recently created quote variation and the payment type as annual/card",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider",
      priority = 16)
  public void BS_020_026_017_022(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_020_026_017_022";
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
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
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
        newquotepage.clickBuy(extentedReport);
        // save from acceptance page as Reviewed
        BuyQuotePage buyQuotePage = new BuyQuotePage(driver, extentedReport);
        buyQuotePage.clicksave_LapsebuttoninAcceptancepage("Reviewed", renewQuoteDesc, "Save",
            extentedReport);
        // buyQuotePage.clicksave_LapsebuttoninAcceptancepage(renewQuoteDesc,"Save",
        // extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Navigated to custdashboardpage successfully after quote save",
            "Failed to Navigate to custdashboardpage after quote save", driver, extentedReport,
            true);

        createQuoteVariations(driver, quoteDescription1, custdashboardpage, extentedReport);
        createQuoteVariations(driver, quoteDescription2, custdashboardpage, extentedReport);
        createQuoteVariations(driver, quoteDescription3, custdashboardpage, extentedReport);

        // Verify the lapsed status of all the quotes
        HashMap<String, String> hmQuotedetails =
            custdashboardpage.getQuoteVariations(extentedReport, true);
        if (hmQuotedetails.get("quoteStatus_0").toString().contentEquals("R")
            && hmQuotedetails.get("quoteStatus_1").toString().contentEquals("L")
            && hmQuotedetails.get("quoteStatus_2").toString().contentEquals("L")
            && hmQuotedetails.get("quoteStatus_3").toString().contentEquals("L"))
          Log.pass(
              "Quote variations in customer dashboard page are listed as Review required and Lapsed",
              extentedReport);
        else
          Log.fail(
              "Quote variations in customer dashboard were not listed as Review required and lapsed",
              extentedReport);

        // click the recent quote variation and perform transaction
        buyQuotePage =
            custdashboardpage.clickQuoteVariations(quoteDescription3, extentedReport, true);

        // Verify Acceptance page is landed
        Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
            "Acceptance tab was selected by default when clicked on quote variations",
            "Acceptance tab was not selected as default tab when clicked on quote variations",
            driver, extentedReport, true);
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
            "Customer Dashboard page is verified", "Customer Dashboard page not verified", driver,
            extentedReport, true);

        // Get the policy URN and check the policy statsus
        policyDetails = custdashboardpage.getPolicyURN(extentedReport);
        Log.softAssertThat(
            policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")
                && policyDetails.get("Position").toString().equalsIgnoreCase("Renewal"),
            "Renewal Policy Created Successfully in accepted status",
            "Failed to create Renewal Policy, it stands in following Status and position stands :"
                + policyDetails.get("Status").toString() + policyDetails.get("Position").toString(),
            driver, extentedReport, true);
        Log.testCaseResult(extentedReport);

      } else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
            driver, extentedReport, true);
      }

      Log.testCaseResult(extentedReport);
    }

    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      // driver.quit();
    } // finally

  }

  public void createQuoteVariations(WebDriver driver, String quotDesc,
      CustDashboardPage custdashboardpage, ExtentTest extentedReport) throws Exception {
    // click quote variations
    BuyQuotePage buyQuotePage =
        custdashboardpage.performRenewals_QuoteVariation(extentedReport, true);
    // To verify that acceptance tab selected by default or not
    Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
        "Acceptance tab was selected by default when clicked on quote variations",
        "Acceptance tab was not selected as default tab when clicked on quote variations", driver,
        extentedReport, true);
    // Click lapse button in acceptance page

    buyQuotePage.clicksave_LapsebuttoninAcceptancepage("Review Required", quotDesc, "Save",
        extentedReport);
    Log.softAssertThat(
        custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
            custdashboardpage),
        "Navigated to custdashboardpage successfully after quote save from acceptance page",
        "Failed to Navigate to custdashboardpage after quote lapse", driver, extentedReport, true);
  }

  @Test(groups = "Business_Scenario",
      description = "Verify that more renewal variations can be viewed in the quote variations dropdown and review the icons displayed and perform renewal for the original created quote variation(a lapsed quote)",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider",
      priority = 15)
  public void BS_018_019_021(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_018_019_021";
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
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password,
          driver, extentedReport, true);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
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
        BuyQuotePage buyQuotePage = new BuyQuotePage(driver, extentedReport);
        // buyQuotePage.clicksave_LapsebuttoninAcceptancepage(renewQuoteDesc,"Save",
        // extentedReport);
        buyQuotePage.clicksave_LapsebuttoninAcceptancepage("Reviewed", renewQuoteDesc, "Save",
            extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Navigated to custdashboardpage successfully after quote save",
            "Failed to Navigate to custdashboardpage after quote save", driver, extentedReport,
            true);

        // to validate Reviewed status under view more quotes and
        // policies tab
        policyDetails = custdashboardpage.getPolicyURN(extentedReport);
        Log.softAssertThat(policyDetails.get("notification_plcy").toString().equalsIgnoreCase("R"),
            "Policy position displaying as Renewal", "Latest position is not displaying");

        Log.softAssertThat(custdashboardpage.verify_reviewStatus("Reviewed", extentedReport, true),
            "Reviewed status was displayed after saving a renewal quote",
            "Reviewed status was not displayed after saving a renewal quote");

        createQuoteVariations(driver, "Quote Description1", custdashboardpage, extentedReport);
        createQuoteVariations(driver, "Quote Description2", custdashboardpage, extentedReport);

        // Verify the lapsed status of all the quotes
        HashMap<String, String> hmQuotedetails =
            custdashboardpage.getQuoteVariations(extentedReport, true);
        if (hmQuotedetails.get("quoteStatus_0").toString().contentEquals("R")
            && hmQuotedetails.get("quoteStatus_1").toString().contentEquals("L")
            && hmQuotedetails.get("quoteStatus_2").toString().contentEquals("L"))
          Log.pass(
              "Quote variations in customer dashboard page are listed as Review required and Lapsed",
              extentedReport);
        else
          Log.fail(
              "Quote variations in customer dashboard were not listed as Review required and lapsed",
              extentedReport);

        // click the quote variation based on description and perform
        // transaction
        buyQuotePage = custdashboardpage.clickQuoteVariations(renewQuoteDesc, extentedReport, true);

        // Verify Acceptance page is landed
        Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
            "Acceptance tab was selected by default when clicked on quote variations",
            "Acceptance tab was not selected as default tab when clicked on quote variations",
            driver, extentedReport, true);
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
            "Customer Dashboard page is verified", "Customer Dashboard page not verified", driver,
            extentedReport, true);

        // Get the policy URN and check the policy statsus
        policyDetails = custdashboardpage.getPolicyURN(extentedReport);
        Log.softAssertThat(
            policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")
                && policyDetails.get("Position").toString().equalsIgnoreCase("Renewal"),
            "Renewal Policy Created Successfully in accepted status",
            "Failed to create Renewal Policy, it stands in following Status and position stands :"
                + policyDetails.get("Status").toString() + policyDetails.get("Position").toString(),
            driver, extentedReport, true);

      } else
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString()
                + " status, unable to proceed with renewal transaction",
            driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    }

    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      // driver.quit();
    } // finally

  }

  @Test(groups = "Business_Scenario",
      description = "To check user able view the renewal quote in a contact search and if multiple quotes, all are visible and  view the status of the renewal quote in a contact search",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 9)
  public void BS_001_002(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_001_002";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");
    String searchKeyWord = "Renewal";
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
      Log.message("<b>Setup for TC_001_002</b>", extentedReport);
      searchPage.clickCreateCustomer(true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      firstName = custDetails.get("First Name");
      lastName_TC001 = custDetails.get("Last Name");
      contactName = firstName + " " + lastName_TC001;

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

        custdashboardpage.clickComplete(extentedReport);
        Log.softAssertThat(homePage.verifyHomePage(), "Successfully navigated to UXP Home Page",
            "Login failed", driver, extentedReport, true);
        homePage.clickTakeCall(extentedReport);
        homePage.clickMyBrands(brandname, extentedReport);
        searchPage.enterLastName(searchKeyWord, extentedReport);
        searchPage.clickSearch(extentedReport);

        Log.message("<b>Verification for TC_001_002</b>", extentedReport);
        Log.softAssertThat(searchPage.verifyMultipleSearchResultCount(extentedReport, true),
            "Multiple Renewal quotes are displayed in search page",
            "Multiple renewal quotes are not displayed", driver, extentedReport);
        searchPage.clearSearch(extentedReport);
        searchPage.enterLastName(lastName_TC001, extentedReport);
        searchPage.clickSearch(extentedReport);
        policyNumber = searchPage.getPolicyNo(extentedReport, true);
        Log.softAssertThat(searchPage.verify_reviewStatus("Review Required", extentedReport, true),
            "Review Required status was displayed after saving a renewal quote",
            "Review Required status was not displayed after saving a renewal quote", driver,
            extentedReport);
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
      description = "To check user Accessing a policy within the renewal period will have auto-quote review and auto-quote may be lapsed in manage renewals",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider",
      dependsOnMethods = "BS_001_002", priority = 10)
  public void BS_003_004(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_003_004";
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
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      searchPage.enterLastName(lastName_TC001, extentedReport);
      searchPage.clickSearch(extentedReport);
      searchPage.selectPolicyId(policyNumber, extentedReport, true);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport).get();
      custdashboardpage.clickPassVerification(extentedReport, true);
      Log.message("<b>Verififcation for TC_003</b>", extentedReport);
      custdashboardpage.clickRenewalQuoteVariations(extentedReport, true);
      Log.softAssertThat(
          custdashboardpage.verifyOptionInRenewalQuoteVariations(quoteDescription1, extentedReport,
              true),
          "Auto-quote is displayed in quote variations",
          "Auto-quote is not displayed in quote variations", driver, extentedReport);

      Log.message("<b>Verififcation for TC_004</b>", extentedReport);
      custdashboardpage.clickManageRenewal(extentedReport, true);
      Log.softAssertThat(
          custdashboardpage.verifyOptionInManageRenewals("Lapse Quote", extentedReport, true),
          "Lapse Quote is displayed in Manage Renewal",
          "Lapse Quote is not displayed in Manage Renewal", driver, extentedReport);
      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }

  }

  @Test(groups = "Business_Scenario",
      description = "To check user able to select an existing quote and create a quote variation and second is lapsed and user able to view the active and lapsed quote",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider",
      dependsOnMethods = "BS_003_004", priority = 11)
  public void BS_005_006_007_008_013(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_005_006_007_008_013";
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
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      searchPage.enterLastName(lastName_TC001, extentedReport);
      searchPage.clickSearch(extentedReport);
      searchPage.selectPolicyId(policyNumber, extentedReport, true);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport).get();
      custdashboardpage.clickPassVerification(extentedReport, true);

      Log.message("<b>Verififcation for TC_005_006_007_008_013</b>", extentedReport);
      custdashboardpage.clickRenewalQuoteVariations(extentedReport, true);
      custdashboardpage.ClickOptionInRenewalQuoteVariations(quoteDescription1, extentedReport,
          true);
      Log.message("Selected existing quote from quote variation", extentedReport);
      BuyQuotePage buyQuotePage = new BuyQuotePage(driver, extentedReport).get();

      Log.softAssertThat(
          buyQuotePage.uielement.verifyPageElements(Arrays.asList("txtTitle"), buyQuotePage),
          "Able to view the active quote variation", "Not able to view the active quote variation",
          driver, extentedReport, true);

      buyQuotePage.clicksavebuttoninAcceptancepage(quoteDescription2, extentedReport);
      custdashboardpage.clickRenewalQuoteVariations(extentedReport, false);
      Log.softAssertThat(
          custdashboardpage.verifyOptionInRenewalQuoteVariations(quoteDescription2, extentedReport,
              true),
          "Quote variation is created", "Quote variation is not created", driver, extentedReport);

      Log.softAssertThat(
          custdashboardpage.verifyStatusForQuoteVariations(quoteDescription1, "L", extentedReport,
              true),
          "Previous Quote " + quoteDescription1 + " is lapsed",
          "Previous Quote " + quoteDescription1 + " is not lapsed", driver, extentedReport);
      custdashboardpage.ClickOptionInRenewalQuoteVariations(quoteDescription2, extentedReport,
          true);

      Log.softAssertThat(
          buyQuotePage.uielement.verifyPageElements(Arrays.asList("txtTitle"), buyQuotePage),
          "Able to view the lapsed quote variation", "Not able to view the lapsed quote variation",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }

  }

  @Test(groups = "Business_Scenario",
      description = "To check user create unlimited variations with all previous variations lapsing , Each quote variation includes the: date, variation number, variation description, premium and status notification icon and  all variations  sorted by date or renewal quote with oldest at the bottom and complered renewal quote",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider",
      dependsOnMethods = "BS_005_006_007_008_013", priority = 12)
  public void BS_009_010_011_012(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_009_010_011_012";
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
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      searchPage.enterLastName(lastName_TC001, extentedReport);
      searchPage.clickSearch(extentedReport);
      searchPage.selectPolicyId(policyNumber, extentedReport, true);
      CustDashboardPage custdashboardpage = new CustDashboardPage(driver, extentedReport).get();
      custdashboardpage.clickPassVerification(extentedReport, true);

      Log.message("<b>Verififcation for TC_009_010_011</b>", extentedReport);
      custdashboardpage.clickRenewalQuoteVariations(extentedReport, true);
      custdashboardpage.ClickOptionInRenewalQuoteVariations(quoteDescription2, extentedReport,
          true);
      BuyQuotePage buyQuotePage = new BuyQuotePage(driver, extentedReport).get();

      buyQuotePage.clicksavebuttoninAcceptancepage(quoteDescription3, extentedReport);
      custdashboardpage.clickRenewalQuoteVariations(extentedReport, true);
      Log.softAssertThat(
          custdashboardpage.verifyOptionInRenewalQuoteVariations(quoteDescription3, extentedReport,
              true),
          "Quote variation is created", "Quote variation is not created", driver, extentedReport);

      Log.softAssertThat(
          (custdashboardpage.verifyStatusForQuoteVariations(quoteDescription3, "R", extentedReport,
              true)
              && custdashboardpage.verifyStatusForQuoteVariations(quoteDescription2, "L",
                  extentedReport, true)
              && custdashboardpage.verifyStatusForQuoteVariations(quoteDescription1, "L",
                  extentedReport, true)),
          "Previous Quotes " + quoteDescription1 + ", " + quoteDescription2 + " are lapsed",
          "Previous Quotes " + quoteDescription1 + ", " + quoteDescription2 + " are not lapsed",
          driver, extentedReport);

      HashMap<String, String> QuoteVariationsetails =
          custdashboardpage.getQuoteVariations(extentedReport, true);
      int hashmapSize = QuoteVariationsetails.size();
      Log.message("Size: " + QuoteVariationsetails.size());
      Log.softAssertThat(hashmapSize > 0,
          "All quotes are having date, variation number, variation description, premium and status notification icon",
          "All quotes are not having date, variation number, variation description, premium and status notification icon",
          driver, extentedReport);
      Log.softAssertThat(
          custdashboardpage.verifyQuoteVariationSortingByDate("desc", extentedReport, true),
          "All variations in the variations dropdown are sorted by date and oldest at the bottom",
          "All variations in the variations dropdown are not sorted by date and oldest at the bottom",
          driver, extentedReport);

      Log.message("Verififcation for TC_012", extentedReport);
      custdashboardpage.clickComplete(extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "User able to complete the quote variations and navigated to Home page. ",
          "User is not able to complete the quote variations and not navigated to Home page. ",
          driver, extentedReport, true);
      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }

  }

  @Test(groups = "Business_Scenario",
      description = "Check that quote variation is not able to save without discription",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider",
      priority = 18)
  public void BS_027(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_027";
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
      // Past date for renewal
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
        buyQuotePage.clickSaveOrLapse("Save", extentedReport, true);

        // Verify that save button is present without variation
        Log.softAssertThat(
            !(buyQuotePage.uielement
                .verifyPageElements(Arrays.asList("btnsaveQuoteDesc_acceptance"), buyQuotePage)),
            "Quote varaition is not saved without description",
            "Quote varaition is saved without description", driver, extentedReport, true);

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
      description = "Verify that Reviewed quote variation have green R in quote variation, Verify that Review Required quote variation have red R in quote variation, Verify that Lapsed quote variation have blue L in quote variation",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider",
      priority = 24)
  public void BS_037_039_040(String browser) throws Exception {
    // Get the web driver instance
    String tcId = "BS_037_039_040";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");
    // String[] review = testData.get("Review").split("\\|");

    ExtentTest extentedReport = addTestInfo(tcId, description);
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

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
          "Not Verified Customer Name on Dashboard", driver, extentedReport, false);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      // Past date for renewal
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

        // To verify that acceptance tab selected by default or not
        Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
            "Acceptance tab was selected by default when clicked on quote variations",
            "Acceptance tab was not selected as default tab when clicked on quote variations",
            driver, extentedReport, true);
        // Click save button in acceptance page
        buyQuotePage.clicksave_ReviewedbuttoninAcceptancepage("Save", extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Navigated to custdashboardpage successfully after quote reviewd",
            "Failed to Navigate to custdashboardpage after quote reviewd", driver, extentedReport,
            true);
        // Verify the reviewed and lapsed status
        HashMap<String, String> hmQuotedetails =
            custdashboardpage.getQuoteVariations(extentedReport, true);

        if (hmQuotedetails.get("quoteStatus_0").equals("R")
            && hmQuotedetails.get("quoteStatus_1").equals("L")
            && hmQuotedetails.get("quoteStatusCommand_0").equals("status_icon invited")) {
          Log.pass(
              "Renew Quote variations in customer dashboard page are listed as Reviewed and lapsed",
              extentedReport);
        } else {
          Log.fail(
              "Renew Quote variations in customer dashboard page are not listed as Reviewed and lapsed",
              extentedReport);
        }

        buyQuotePage = custdashboardpage.performRenewals_QuoteVariation(extentedReport, true);
        buyQuotePage.clicksave_LapsebuttoninAcceptancepage("Review Required",
            "Review required Quote variation", "Save", extentedReport);
        // buyQuotePage.clicksave_LapsebuttoninAcceptancepage("Review
        // required Quote variation", "Save",extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Navigated to custdashboardpage successfully after quote reviewd",
            "Failed to Navigate to custdashboardpage after quote reviewd", driver, extentedReport,
            true);
        // Verify the review required status
        hmQuotedetails = custdashboardpage.getQuoteVariations(extentedReport, true);

        if (hmQuotedetails.get("quoteStatus_0").equals("R")
            && hmQuotedetails.get("quoteStatus_1").equals("L")
            && hmQuotedetails.get("quoteStatusCommand_0").equals("status_icon need_attention")) {
          Log.pass(
              "Renew Quote variations in customer dashboard page are listed as Review Required and lapsed",
              extentedReport);
        } else {
          Log.fail(
              "Renew Quote variations in customer dashboard page are not listed as Review Required and lapsed",
              extentedReport);
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
    }

  }

  @Test(groups = "Business_Scenario",
      description = "Check that lapsed quote variation is not able to delete, Verify that alert message should present when lapse all the variation",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider",
      priority = 20)
  public void BS_029_030(String browser) throws Exception {
    // Get the web driver instance
    String tcId = "BS_029_030";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");
    // String[] review = testData.get("Review").split("\\|");
    String warningMsg =
        "You are about to lapse this renewal quote variation leaving no active variations against this policy. Do you wish to continue?";
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
      // Past date for renewal
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

        // To verify that acceptance tab selected by default or not
        Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
            "Acceptance tab was selected by default when clicked on quote variations",
            "Acceptance tab was not selected as default tab when clicked on quote variations",
            driver, extentedReport, true);
        // Click save button in acceptance page

        buyQuotePage.clicksave_LapsebuttoninAcceptancepage("", "", "lapse", extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Navigated to custdashboardpage successfully after quote lapse",
            "Failed to Navigate to custdashboardpage after quote lapse", driver, extentedReport,
            true);
        buyQuotePage = custdashboardpage.performRenewals_QuoteVariation(extentedReport, true);

        // Verify the delete option is available for lapse quote
        List<WebElement> buttons = driver.findElements(By.cssSelector("button"));
        Log.softAssertThat(
            !(GenericUtils.verifyMatchingTextContainsElementFromList(buttons, "delete")),
            "Delete option is not present for lapse quote ",
            "Delete option is present for lapse quote ", driver, extentedReport, true);
        buyQuotePage.clicksave_LapsebuttoninAcceptancepage("Review Required",
            "Review required Quote variation", "Save", extentedReport);
        // buyQuotePage.clicksave_LapsebuttoninAcceptancepage("Review
        // required Quote variation", "Save",extentedReport);
        HashMap<String, String> hmQuotedetails =
            custdashboardpage.getQuoteVariations(extentedReport, true);

        if (hmQuotedetails.get("quoteStatus_0").equals("R")) {
          buyQuotePage = custdashboardpage.performRenewals_QuoteVariation(extentedReport, true);
          buyQuotePage.clickSaveOrLapse("Lapse", extentedReport, true);
          Log.softAssertThat(buyQuotePage.verifyWarningMsg(extentedReport, warningMsg),
              "Alert message is present after clicked the lapse button",
              "Alert message is not present after clicked the lapse button", driver, extentedReport,
              true);
        } else {
          Log.fail("Reviewed quote variation is not present in quote variations", driver,
              extentedReport, true);
        }
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
      description = " To check Reinstate a cancelled policy, quote then quote variation. Cycle through quote variations until completing. For DD (at renewal and mid-term) and card",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider",
      priority = 17)
  public void BS_024(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_024";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String Corecover = testData.get("Cover");
    String brandname = testData.get("Brand Name");
    String lastName = "";

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
      contactName = firstName + " " + lastName;

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
      Log.message("<b> Verification for TC_024</b>", extentedReport);
      custdashboardpage.clickCancelPolicy(testData, true, extentedReport);
      custdashboardpage.enterEffectiveDate();
      custdashboardpage.enterReason("Better Quote", extentedReport);
      custdashboardpage.enterPremiumType(testData, extentedReport);
      custdashboardpage.clickCalculate(true, extentedReport);
      custdashboardpage.clickConfirm(true, extentedReport);
      custdashboardpage.clickReinstatePolicy(true, extentedReport);
      custdashboardpage.selectReinstateReason(testData, true, extentedReport);
      custdashboardpage.clickCalculateForReinstate(true, extentedReport);
      custdashboardpage.clickConfirmForReinstate(true, extentedReport);

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
        newquotepage.clickBuy(extentedReport);
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
        custdashboardpage.clickComplete(extentedReport);
        Log.softAssertThat(homePage.verifyHomePage(),
            "User able to complete the quote variation and navigated to Home page.",
            "User not able to complete the quote variation and navigated to Home page.", driver,
            extentedReport, true);

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
      description = "Check that user can able to complete lapsed varition by reinstate it",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider",
      priority = 21)
  public void BS_031(String browser) throws Exception {
    // Get the web driver instance
    String tcId = "BS_031";
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
      // Past date for renewal
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

        // To verify that acceptance tab selected by default or not
        Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
            "Acceptance tab was selected by default when clicked on quote variations",
            "Acceptance tab was not selected as default tab when clicked on quote variations",
            driver, extentedReport, true);
        // Click save button in acceptance page
        buyQuotePage.clicksave_ReviewedbuttoninAcceptancepage("save", extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Navigated to custdashboardpage successfully after quote reviewd",
            "Failed to Navigate to custdashboardpage after quote reviewd", driver, extentedReport,
            true);

        buyQuotePage = custdashboardpage.performRenewals_QuoteVariation(extentedReport, true);
        buyQuotePage.clicksave_LapsebuttoninAcceptancepage("Review Required",
            "Review required Quote variation", "Save", extentedReport);
        buyQuotePage = custdashboardpage.performRenewals_QuoteVariation(extentedReport, true);
        buyQuotePage.clicksave_LapsebuttoninAcceptancepage("", "", "lapse", extentedReport);
        HashMap<String, String> hmQuotedetails =
            custdashboardpage.getQuoteVariations(extentedReport, true);

        if (hmQuotedetails.get("quoteStatus_0").equals("L")
            && hmQuotedetails.get("quoteStatus_1").equals("L")
            && hmQuotedetails.get("quoteStatus_2").equals("L")) {
          // Reinstate the lapsed variation
          buyQuotePage = custdashboardpage.performRenewals_QuoteVariation(extentedReport, true);
          buyQuotePage.clicksave_LapsebuttoninAcceptancepage("Review Required",
              "Review required Quote variation", "save", extentedReport);
        } else {
          Log.fail("All quote variation are not lapsed", driver, extentedReport, true);
        }
        buyQuotePage = custdashboardpage.performRenewals_QuoteVariation(extentedReport, true);
        // update payment method
        buyQuotePage.selectPaymentPlan("Annual", extentedReport);
        buyQuotePage.selectPaymentMethod("Card", extentedReport, true);
        buyQuotePage.takePayment(extentedReport);
        carddetailspage = buyQuotePage.selectVisacard(extentedReport);
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
        Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport),
            "Payment was successful", "Payment was not successful", driver, extentedReport, true);

        buyQuotePage.clickAcceptbtn(extentedReport);
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
      driver.quit();
    }
  }

  @Test(groups = "Business_Scenario",
      description = "check that user can able to use the variations dropdown to lapse specific variations",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider",
      priority = 23)
  public void BS_036(String browser) throws Exception {
    // Get the web driver instance
    String tcId = "BS_036";
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
      // Past date for renewal
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

        // To verify that acceptance tab selected by default or not
        Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
            "Acceptance tab was selected by default when clicked on quote variations",
            "Acceptance tab was not selected as default tab when clicked on quote variations",
            driver, extentedReport, true);
        // Click save button in acceptance page
        buyQuotePage.clicksave_LapsebuttoninAcceptancepage("", "", "lapse", extentedReport);
        HashMap<String, String> hmQuotedetails =
            custdashboardpage.getQuoteVariations(extentedReport, true);

        if (hmQuotedetails.get("quoteStatus_0").equals("L")) {
          Log.pass("Able to use the variations dropdown to lapse specific variations", driver,
              extentedReport, true);
        } else {
          Log.fail("Cannot able to use the variations dropdown to lapse specific variations",
              driver, extentedReport, true);
        }
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
      description = "Verify that two contacts produce exactly the same quote with same conditions and cover",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider",
      priority = 19)
  public void BS_028(String browser) throws Exception {
    // Get the web driver instance
    String tcId = "BS_028";
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
      Log.softAssertThat(
          homePage.verifyHomePage(), "Successfully logged into UXP Home Page with User id:"
              + userName + "& Password:" + password + "",
          "Login failed", driver, extentedReport, true);
      // Click on Take Call link
      Log.message("Creation of first customer", extentedReport);
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
      testData.put("Email", "");

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
      // Past date for renewal
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

        // To verify that acceptance tab selected by default or not
        Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
            "Acceptance tab was selected by default when clicked on quote variations",
            "Acceptance tab was not selected as default tab when clicked on quote variations",
            driver, extentedReport, true);
        String amountForFirstContact = buyQuotePage.getPolicyPremiumAmount(extentedReport);
        buyQuotePage.clicksave_LapsebuttoninAcceptancepage("Review Required",
            "Review required Quote variation", "Save", extentedReport);
        homePage = custdashboardpage.clickCompleteButton();
        Log.message("Creation of first customer", extentedReport);
        homePage.clickTakeCall(extentedReport);
        homePage.clickMyBrands(brandname, extentedReport);
        searchPage = new SearchPage(driver, extentedReport).get();
        Log.softAssertThat(
            searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
            "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

        // Enter Customer Details
        searchPage.clickCreateCustomer(true, extentedReport);
        custDetails = searchPage.enterCustomerDetails(testData, true, extentedReport);

        // Confirm customer details and create customer
        custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Custdashboardpage Verified", "Custdashboardpage not Verified", driver, extentedReport,
            true);
        Log.softAssertThat(
            custdashboardpage.verifyContactName(testData.get("Title") + " "
                + custDetails.get("First Name") + " " + custDetails.get("Last Name"),
                extentedReport, true),
            "Verified FirstName and LastName on Customer Dashboard",
            "Not Verified Customer Name on Dashboard", driver, extentedReport, true);

        // Create New Quote
        custdashboardpage.clickNewQuote(true, extentedReport);
        // Past date for renewal
        custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true);
        custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
        newquotepage = custdashboardpage.clickContinueQuote(true, extentedReport);

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
        carddetailspage = newquotepage.selectVisacard(extentedReport);

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
        policyDetails = custdashboardpage.getPolicyURN(extentedReport);
        if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted")) {
          Log.pass("NB Policy Created Successfully in accepted status", driver, extentedReport,
              true);
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
              "Custdashboardpage Verified", "Custdashboardpage not Verified", driver,
              extentedReport, true);

          buyQuotePage = custdashboardpage.performRenewals_QuoteVariation(extentedReport, true);

          // To verify that acceptance tab selected by default or not
          Log.softAssertThat(buyQuotePage.verifyDefaultTab("Acceptance", extentedReport, true),
              "Acceptance tab was selected by default when clicked on quote variations",
              "Acceptance tab was not selected as default tab when clicked on quote variations",
              driver, extentedReport, true);
          String amountForSecondContact = buyQuotePage.getPolicyPremiumAmount(extentedReport);
          buyQuotePage.clicksave_LapsebuttoninAcceptancepage("Review Required",
              "Review required Quote variation", "Save", extentedReport);
          if (amountForSecondContact.equals(amountForFirstContact)) {
            Log.pass("Payment amount is same for two persons", extentedReport);
          } else {
            Log.fail("Payment amount is not same for two persons, FirstPerson payment "
                + amountForFirstContact + " and Second person payment is " + amountForSecondContact
                + "", extentedReport);
          }
        }
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
      description = "Verify that user not able to complete quote variation without confirm the gurarantee when using Direct debit",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider",
      priority = 22)
  public void BS_034(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "BS_034";
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
            (buyQuotePage.uielement.verifyPageElementsDisabled(Arrays.asList("btnAcceptPayment"),
                buyQuotePage)),
            "Without confim the guarantee not able to complete the quote variation ",
            "Without confim the guarantee able to complete the quote variation ", driver,
            extentedReport, true);

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
}
