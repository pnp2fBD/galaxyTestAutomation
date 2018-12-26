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
import com.ssp.uxp_pages.CardDetailsPage;
import com.ssp.uxp_pages.CustDashboardPage;
import com.ssp.uxp_pages.HomePage;
import com.ssp.uxp_pages.LoginPage;
import com.ssp.uxp_pages.NewQuotePage;
import com.ssp.uxp_pages.SearchPage;

public class UXP_EC_1754 extends BaseTest {
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
    String className = "UXP_EC_1754_" + env;
    return DataUtils.testDatabyID(testcaseId, className);
  }

  @Test(description = "Verify Change Password screen", dataProviderClass = DataProviderUtils.class,
      dataProvider = "ssBVTDataProvider")
  public void TC_459(String browser) throws Exception {

    String tcId = "TC_459";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page",
          "Not logged in to Home page", driver, extentedReport, true);

      homePage.clickChangePassword(extentedReport);
      Log.softAssertThat(
          homePage.uielement.verifyPageElements(Arrays.asList("txtExistDetail", "fldChPwdOldPwd",
              "fldChPwdNewPwd", "fldChPwdReEnterPwd"), homePage),
          "Change Password elements 'Please type exist data' text, 'Old Password', 'New Password', 'ReEnter Password' are Verified",
          "Change Password elements are not present", driver, extentedReport, true);
      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(description = "Verify User able to change Password from change Password screen",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_460(String browser) throws Exception {

    String tcId = "TC_460";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String newPassword = testData.get("NewPassword");
    String description = testData.get("Description");

    HomePage homePage = null;
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass(" SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page",
          "Not logged in to Home page", driver, extentedReport, true);

      homePage.clickChangePassword(extentedReport);
      Log.softAssertThat(
          homePage.uielement.verifyPageElements(Arrays.asList("fldChPwdReEnterPwd"), homePage),
          "Change Password page is loaded", "Change Password page is not loaded", driver,
          extentedReport, true);

      homePage.changePassword(password, newPassword, extentedReport);

      homePage.doLogout(extentedReport);

      homePage = loginPage.loginToSSP(userName, newPassword, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page",
          "Not logged in to Home page", driver, extentedReport, true);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.message("<u> Reverting to Old Password </u>");
      homePage.clickChangePassword(extentedReport);
      homePage.changePassword(newPassword, password, extentedReport);
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(
      description = "Verify My Dashboard screen list of options after successful logging into Engagement Center application",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_461(String browser) throws Exception {

    String tcId = "TC_461";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page",
          "Login failed", driver, extentedReport, true);

      Log.softAssertThat(
          homePage.uielement.verifyPageElements(
              Arrays.asList("lnkTakeCall", "lnkMakeCall", "lnkAdminTask"), homePage),
          "All the links 'Take Call','Make Call','Admin Task' in home page are displayed",
          "Some of the links in home page are not displayed", driver, extentedReport, true);
      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(
      description = "Verify User redirect to Search after click in the Take Call/Make Call/Admin Task/Direct Contact ",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_462(String browser) throws Exception {

    String tcId = "TC_462";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String brandName = testData.get("BrandName");
    String description = testData.get("Description");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page",
          "Login failed", driver, extentedReport, true);

      homePage.clickMakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Thread.sleep(2000);
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page is Verified", "Search Page is not Verified", driver, extentedReport, true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      Thread.sleep(2000);
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      searchPage.clickComplete(extentedReport);
      homePage.clickAdminTask(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      Thread.sleep(2000);
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Search Page Verified", "Search Page not Verified", driver, extentedReport, true);

      searchPage.clickComplete(extentedReport);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(description = "Verify List of search option available in the Search screen",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_463(String browser) throws Exception {

    String tcId = "TC_463";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String brandName = testData.get("BrandName");
    String description = testData.get("Description");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page",
          "Login failed", driver, extentedReport, true);

      homePage.clickMakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(
              Arrays.asList("btnSearch", "btnClearSearch", "btnCreateCustomer", "btnComplete"),
              searchPage),
          "Search Page has BUTTONS like 'Search','Complete','CreateCustomer','ClearSearch' displayed",
          "Search Page is not having some buttons", driver, extentedReport, true);

      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(
              Arrays.asList("txtPolicyNumber", "txt_DataofBirth_search", "txtExtQuote",
                  "sp_txtLastName", "sp_txtfirstName", "sp_txtPostCode", "txtBusiName"),
              searchPage),
          "Search Page has TEST BOXES like 'PolicyNumber','DateOfBirth','ExternalQuote','LastName','FirstName','PostCode','BusinessName' displayed",
          "Search Page is not having some text fields", driver, extentedReport, true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(
              Arrays.asList("btnSearch", "btnClearSearch", "btnCreateCustomer", "btnComplete"),
              searchPage),
          "Search Page has BUTTONS like 'Search','Complete','CreateCustomer','ClearSearch' displayed",
          "Search Page is not having some buttons", driver, extentedReport, true);

      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(
              Arrays.asList("txtPolicyNumber", "txt_DataofBirth_search", "txtExtQuote",
                  "sp_txtLastName", "sp_txtfirstName", "sp_txtPostCode", "txtBusiName"),
              searchPage),
          "Search Page has TEST BOXES like 'PolicyNumber','DateOfBirth','ExternalQuote','LastName','FirstName','PostCode','BusinessName' displayed",
          "Search Page is not having some text fields", driver, extentedReport, true);

      searchPage.clickComplete(extentedReport);

      homePage.clickAdminTask(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(
              Arrays.asList("btnSearch", "btnClearSearch", "btnCreateCustomer", "btnComplete"),
              searchPage),
          "Search Page has BUTTONS like 'Search','Complete','CreateCustomer','ClearSearch' displayed",
          "Search Page is not having some buttons", driver, extentedReport, true);

      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(
              Arrays.asList("txtPolicyNumber", "txt_DataofBirth_search", "txtExtQuote",
                  "sp_txtLastName", "sp_txtfirstName", "sp_txtPostCode", "txtBusiName"),
              searchPage),
          "Search Page has TEST BOXES like 'PolicyNumber','DateOfBirth','ExternalQuote','LastName','FirstName','PostCode','BusinessName' displayed",
          "Search Page is not having some text fields", driver, extentedReport, true);

      searchPage.clickComplete(extentedReport);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(
      description = "Verify User able to search Contact  with options (Policy Number, External Quote Number, External Client Reference) which has available in Quick Search section",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_464(String browser) throws Exception {

    String tcId = "TC_464";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandName = testData.get("BrandName");
    String Corecover = testData.get("Cover");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page",
          "Login failed", driver, extentedReport, true);

      homePage.clickMakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
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
      custDashboardPage.clickComplete(extentedReport);
      homePage.clickMakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      searchPage.searchValidPolicy(policyDetails.get("policyNo").toString(), true, extentedReport);

      Log.softAssertThat(searchPage.isSearchTitleAvaialble(extentedReport),
          "The user searched is displayed with 'Contact', 'Address', 'DateOfBirth' details and 'Policy' in table format",
          "Searching policy number is not available", driver, extentedReport, true);
      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      searchPage.searchValidPolicy(policyDetails.get("policyNo").toString(), true, extentedReport);
      Log.softAssertThat(searchPage.isSearchTitleAvaialble(extentedReport),
          "The user searched is displayed with 'Contact', 'Address', 'DateOfBirth' details and 'Policy' in table format",
          "Searching policy number is not available", driver, extentedReport, true);

      searchPage.clickComplete(extentedReport);

      homePage.clickAdminTask(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      searchPage.searchValidPolicy(policyDetails.get("policyNo").toString(), true, extentedReport);
      Log.softAssertThat(searchPage.isSearchTitleAvaialble(extentedReport),
          "The user searched is displayed with 'Contact', 'Address', 'DateOfBirth' details and 'Policy' in table format",
          "Searching policy number is not available", driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(description = "Verify User not able to retrieve any information from non existence Policy ",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_465(String browser) throws Exception {

    String tcId = "TC_465";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String brandName = testData.get("BrandName");
    String description = testData.get("Description");
    String policynumber = testData.get("PolicyNumber");
    String textToVerify = testData.get("ErrorSearchText");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page",
          "Login failed", driver, extentedReport, true);

      homePage.clickMakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      searchPage.searchValidPolicy(policynumber, true, extentedReport);

      Log.softAssertThat(searchPage.verifySearchError(textToVerify), "The 'Policy' is not present",
          "The 'Policy' is present", driver, extentedReport, true);
      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      searchPage.searchValidPolicy(policynumber, true, extentedReport);
      Log.softAssertThat(searchPage.verifySearchError(textToVerify), "The 'Policy' is not present",
          "The 'Policy' is present", driver, extentedReport, true);
      searchPage.clickComplete(extentedReport);

      homePage.clickAdminTask(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      searchPage.searchValidPolicy(policynumber, true, extentedReport);
      Log.softAssertThat(searchPage.verifySearchError(textToVerify), "The 'Policy' is not present",
          "The 'Policy' is present", driver, extentedReport, true);
      searchPage.clickComplete(extentedReport);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(
      description = "Verify User able to search Contact with options (LastName,FirstName) which has available in Advanced Search section",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_466(String browser) throws Exception {

    String tcId = "TC_466";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String brandName = testData.get("BrandName");
    String description = testData.get("Description");
    String lastName = testData.get("Last Name");
    String firstName = testData.get("First Name");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page",
          "Login failed", driver, extentedReport, true);

      homePage.clickMakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);
      Log.softAssertThat(searchPage.isSearchTitleAvaialble(extentedReport),
          "The user searched is displayed with 'Contact', 'Address', 'DateOfBirth' details and 'Policy' in table format",
          "Searching policy number is not available", driver, extentedReport, true);
      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);
      Log.softAssertThat(searchPage.isSearchTitleAvaialble(extentedReport),
          "The user searched is displayed with 'Contact', 'Address', 'DateOfBirth' details and 'Policy' in table format",
          "Searching policy number is not available", driver, extentedReport, true);

      searchPage.clickComplete(extentedReport);

      homePage.clickAdminTask(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);
      Log.softAssertThat(searchPage.isSearchTitleAvaialble(extentedReport),
          "The user searched is displayed with 'Contact', 'Address', 'DateOfBirth' details and 'Policy' in table format",
          "Searching policy number is not available", driver, extentedReport, true);

      searchPage.clickComplete(extentedReport);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(description = "Verify error message while searching user with only first name",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_467(String browser) throws Exception {

    String tcId = "TC_467";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String brandName = testData.get("BrandName");
    String description = testData.get("Description");
    String firstName = testData.get("First Name");
    String textToVerify = testData.get("ErrorSearchText");

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

      homePage.clickMakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      Log.softAssertThat(searchPage.verifyLnameError(textToVerify),
          "Message appeared as : " + textToVerify, "Error message not displayed", driver,
          extentedReport, true);
      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      Log.softAssertThat(searchPage.verifyLnameError(textToVerify),
          "Message appeared as : " + textToVerify, "Error message not displayed", driver,
          extentedReport, true);
      searchPage.clickComplete(extentedReport);

      homePage.clickAdminTask(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      Log.softAssertThat(searchPage.verifyLnameError(textToVerify),
          "Message appeared as : " + textToVerify, "Error message not displayed", driver,
          extentedReport, true);
      searchPage.clickComplete(extentedReport);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  // Disabled this test as this is 'Pure' test case functionality and the same
  // is not yet implemented in 'Insurer' (error msg). (Based on the discussion
  // with Venkatesh N. on May 24, 2017)
  @Test(enabled = false,
      description = "Verify error message while searching user with only last name",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_468(String browser) throws Exception {

    String tcId = "TC_468";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String brandName = testData.get("BrandName");
    String description = testData.get("Description");
    String lastName = testData.get("Last Name");
    String textToVerify = testData.get("ErrorSearchText");

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

      homePage.clickMakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      searchPage.enterLastName(lastName, extentedReport);
      searchPage.clickSearch(extentedReport);

      Log.softAssertThat(searchPage.verifyLnameError(textToVerify),
          "Message appeared as : " + textToVerify, "Error message not displayed", driver,
          extentedReport, true);
      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.clickSearch(extentedReport);

      Log.softAssertThat(searchPage.verifyLnameError(textToVerify),
          "Message appeared as : " + textToVerify, "Error message not displayed", driver,
          extentedReport, true);
      searchPage.clickComplete(extentedReport);

      homePage.clickAdminTask(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.clickSearch(extentedReport);

      Log.softAssertThat(searchPage.verifyLnameError(textToVerify),
          "Message appeared as : " + textToVerify, "Error message not displayed", driver,
          extentedReport, true);
      searchPage.clickComplete(extentedReport);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  // Disabled this test as this is 'Pure' test case functionality and the same
  // is not yet implemented in 'Insurer' (element edit-locked). (Based on the
  // discussion with Venkatesh N. on May 24, 2017)
  @Test(enabled = false,
      description = "Verify User able to search Contact with options (Business/Trade Name/Postcode/Date of Birth) which has available in Advanced Search section",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_469(String browser) throws Exception {

    String tcId = "TC_469";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String brandName = testData.get("BrandName");
    String description = testData.get("Description");
    String postCode = testData.get("Post Code");
    String businessName = testData.get("Business Name");
    String dateOfBirth = testData.get("Date of Birth");

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

      homePage.clickMakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // This combination does not work. If DOB entered, Business Name is
      // edit-locked, vice versa. Disabled test due to this.
      searchPage.enterDateOfBirth(dateOfBirth, extentedReport);
      searchPage.enterBusinessName(businessName, extentedReport);
      searchPage.enterPostCode(postCode, extentedReport);

      searchPage.clickSearch(extentedReport);

      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("tablePolicyItems",
              "tab_headerContact", "tab_headerDateOfBirth", "tab_headerAddress"), searchPage),
          "The user searched is displayed with 'Contact', 'Address', 'DateOfBirth' details and 'Policy' in table format",
          "Searching policy number is not available", driver, extentedReport, true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      searchPage.enterDateOfBirth(dateOfBirth, extentedReport);
      searchPage.enterBusinessName(businessName, extentedReport);
      searchPage.enterPostCode(postCode, extentedReport);

      searchPage.clickSearch(extentedReport);

      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("tablePolicyItems",
              "tab_headerContact", "tab_headerDateOfBirth", "tab_headerAddress"), searchPage),
          "The user searched is displayed with 'Contact', 'Address', 'DateOfBirth' details and 'Policy' in table format",
          "Searching policy number is not available", driver, extentedReport, true);
      searchPage.clickComplete(extentedReport);

      homePage.clickAdminTask(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      searchPage.enterDateOfBirth(dateOfBirth, extentedReport);
      searchPage.enterBusinessName(businessName, extentedReport);
      searchPage.enterPostCode(postCode, extentedReport);

      searchPage.clickSearch(extentedReport);

      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("tablePolicyItems",
              "tab_headerContact", "tab_headerDateOfBirth", "tab_headerAddress"), searchPage),
          "The user searched is displayed with 'Contact', 'Address', 'DateOfBirth' details and 'Policy' in table format",
          "Searching policy number is not available", driver, extentedReport, true);
      searchPage.clickComplete(extentedReport);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  // Disabled this test as this is 'Pure' test case functionality and the same
  // is not yet implemented in 'Insurer' (error msg). (Based on the discussion
  // with Venkatesh N. on May 24, 2017)
  @Test(enabled = false,
      description = "Verify User not able to search contact with only Business/Trade Name",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_470(String browser) throws Exception {

    String tcId = "TC_470";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String brandName = testData.get("BrandName");
    String description = testData.get("Description");
    String businessName = testData.get("Business Name");
    String textToVerify = testData.get("ErrorSearchText");

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

      homePage.clickMakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      searchPage.enterBusinessName(businessName, extentedReport);

      searchPage.clickSearch(extentedReport);

      Log.softAssertThat(searchPage.verifyLnameError(textToVerify),
          "Message appeared as : " + textToVerify, "Error message not displayed", driver,
          extentedReport, true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);

      searchPage.enterBusinessName(businessName, extentedReport);

      searchPage.clickSearch(extentedReport);

      Log.softAssertThat(searchPage.verifyLnameError(textToVerify),
          "Message appeared as : " + textToVerify, "Error message not displayed", driver,
          extentedReport, true);
      searchPage.clickComplete(extentedReport);

      homePage.clickAdminTask(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);

      searchPage.enterBusinessName(businessName, extentedReport);

      searchPage.clickSearch(extentedReport);

      Log.softAssertThat(searchPage.verifyLnameError(textToVerify),
          "Message appeared as : " + textToVerify, "Error message not displayed", driver,
          extentedReport, true);
      searchPage.clickComplete(extentedReport);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(description = "Verify User not able to search contact with only Postcode",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_471(String browser) throws Exception {

    String tcId = "TC_471";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String brandName = testData.get("BrandName");
    String description = testData.get("Description");
    String postCode = testData.get("Post Code");
    String textToVerify = testData.get("ErrorSearchText");

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

      homePage.clickMakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      searchPage.enterPostCode(postCode, extentedReport);

      searchPage.clickSearch(extentedReport);

      Log.softAssertThat(searchPage.verifyLnameError(textToVerify),
          "Message appeared as : " + textToVerify, "Error message not displayed", driver,
          extentedReport, true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);

      searchPage.enterPostCode(postCode, extentedReport);

      searchPage.clickSearch(extentedReport);

      Log.softAssertThat(searchPage.verifyLnameError(textToVerify),
          "Message appeared as : " + textToVerify, "Error message not displayed", driver,
          extentedReport, true);
      searchPage.clickComplete(extentedReport);

      homePage.clickAdminTask(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);

      searchPage.enterPostCode(postCode, extentedReport);

      searchPage.clickSearch(extentedReport);

      Log.softAssertThat(searchPage.verifyLnameError(textToVerify),
          "Message appeared as : " + textToVerify, "Error message not displayed", driver,
          extentedReport, true);
      searchPage.clickComplete(extentedReport);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(description = "Verify User not able to search contact with only Date Of Birth",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_472(String browser) throws Exception {

    String tcId = "TC_472";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String brandName = testData.get("BrandName");
    String description = testData.get("Description");
    String dateOfBirth = testData.get("Date of Birth");
    String textToVerify = testData.get("ErrorSearchText");

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

      homePage.clickMakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      searchPage.enterDateOfBirth(dateOfBirth, extentedReport);

      searchPage.clickSearch(extentedReport);

      Log.softAssertThat(searchPage.verifyLnameError(textToVerify),
          "Message appeared as : " + textToVerify, "Error message not displayed", driver,
          extentedReport, true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);

      searchPage.enterDateOfBirth(dateOfBirth, extentedReport);

      searchPage.clickSearch(extentedReport);

      Log.softAssertThat(searchPage.verifyLnameError(textToVerify),
          "Message appeared as : " + textToVerify, "Error message not displayed", driver,
          extentedReport, true);
      searchPage.clickComplete(extentedReport);

      homePage.clickAdminTask(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);

      searchPage.enterDateOfBirth(dateOfBirth, extentedReport);

      searchPage.clickSearch(extentedReport);

      Log.softAssertThat(searchPage.verifyLnameError(textToVerify),
          "Message appeared as : " + textToVerify, "Error message not displayed", driver,
          extentedReport, true);
      searchPage.clickComplete(extentedReport);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  // Disabled this test as this is 'Pure' test case functionality and the same
  // is not yet implemented in 'Insurer' (error msg). (Based on the discussion
  // with Venkatesh N. on May 24, 2017)
  @Test(enabled = false,
      description = "Verify System will throw warning message for  contact search which does not have quote/Policy",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_473(String browser) throws Exception {

    String tcId = "TC_473";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String brandName = testData.get("BrandName");
    String description = testData.get("Description");
    String lastName = testData.get("Last Name");
    String firstName = testData.get("First Name");
    String textToVerify = testData.get("ErrorSearchText");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page",
          "Login failed", driver, extentedReport, true);

      homePage.clickMakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
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
      custdashboardpage.clickComplete(extentedReport);
      homePage.clickMakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      Log.softAssertThat(searchPage.verifyErPolicyNotFound(textToVerify),
          "Message appeared as : " + textToVerify, "Error message not displayed", driver,
          extentedReport, true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);
      Log.softAssertThat(searchPage.verifyErPolicyNotFound(textToVerify),
          "Message appeared as : " + textToVerify, "Error message not displayed", driver,
          extentedReport, true);

      searchPage.clickComplete(extentedReport);

      homePage.clickAdminTask(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);
      Log.softAssertThat(searchPage.verifyErPolicyNotFound(textToVerify),
          "Message appeared as : " + textToVerify, "Error message not displayed", driver,
          extentedReport, true);

      searchPage.clickComplete(extentedReport);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(description = "Verify Clear Search button behavior in the My Dashboard screen",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_474(String browser) throws Exception {

    String tcId = "TC_474";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String brandName = testData.get("BrandName");
    String description = testData.get("Description");
    String lastName = testData.get("Last Name");
    String firstName = testData.get("First Name");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page",
          "Login failed", driver, extentedReport, true);

      homePage.clickMakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clearSearch(extentedReport);
      Thread.sleep(10000);
      String strFnameValue = searchPage.getValueFromFname(extentedReport);
      String strLnameValue = searchPage.getValueFromLname(extentedReport);
      Log.softAssertThat(GenericUtils.verifyTextEquals(strFnameValue, ""),
          "The Firstname field become empty", "The first name field is not empty", driver,
          extentedReport, true);
      Log.softAssertThat(GenericUtils.verifyTextEquals(strLnameValue, ""),
          "The Lastname field become empty", "The last name field is not empty", driver,
          extentedReport, true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clearSearch(extentedReport);
      Thread.sleep(10000);
      strFnameValue = searchPage.getValueFromFname(extentedReport);
      strLnameValue = searchPage.getValueFromLname(extentedReport);
      Log.softAssertThat(GenericUtils.verifyTextEquals(strFnameValue, ""),
          "The Firstname field become empty", "The first name field is not empty", driver,
          extentedReport, true);
      Log.softAssertThat(GenericUtils.verifyTextEquals(strLnameValue, ""),
          "The Lastname field become empty", "The last name field is not empty", driver,
          extentedReport, true);
      searchPage.clickComplete(extentedReport);

      homePage.clickAdminTask(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clearSearch(extentedReport);
      Thread.sleep(10000);
      strFnameValue = searchPage.getValueFromFname(extentedReport);
      strLnameValue = searchPage.getValueFromLname(extentedReport);
      Log.softAssertThat(GenericUtils.verifyTextEquals(strFnameValue, ""),
          "The Firstname field become empty", "The first name field is not empty", driver,
          extentedReport, true);
      Log.softAssertThat(GenericUtils.verifyTextEquals(strLnameValue, ""),
          "The Lastname field become empty", "The last name field is not empty", driver,
          extentedReport, true);
      searchPage.clickComplete(extentedReport);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(description = "Verify Complete button behavior in the My Dashboard screen",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_475(String browser) throws Exception {

    String tcId = "TC_475";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String brandName = testData.get("BrandName");
    String description = testData.get("Description");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page",
          "Login failed", driver, extentedReport, true);

      homePage.clickMakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      searchPage.clickComplete(extentedReport);

      homePage.clickAdminTask(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      searchPage.clickComplete(extentedReport);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(description = "Verify that the version details for Pure is displayed in the 'About' pop up",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_477(String browser) throws Exception {

    String tcId = "TC_477";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String strVersionToVerify = testData.get("Version");
    String strTitleToVerify = testData.get("OtherTitle");
    String strLangToVerify = testData.get("Language");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page",
          "Login failed", driver, extentedReport, true);
      homePage.clickAbout(extentedReport);

      String strLanguage = homePage.getAppLangInAbtPopUp();
      Log.message(
          "Actual Language: '" + strLanguage + "'; Expected Language: '" + strLangToVerify + "'",
          extentedReport);
      String strVersion = homePage.getAppVersionInAbtPopUp();
      Log.message(
          "Actual Version: '" + strVersion + "'; Expected Version: '" + strVersionToVerify + "'",
          extentedReport);
      String strTitle = homePage.getAppTitleInAbtPopUp();
      Log.message("Actual Title: '" + strTitle + "'; Expected Title: '" + strTitleToVerify + "'",
          extentedReport);

      Log.softAssertThat(GenericUtils.verifyTextEquals(strVersion, strVersionToVerify),
          "The version is '" + strVersion + "' as expected",
          "The version is not correct : " + strVersion, driver, extentedReport, true);

      Log.softAssertThat(GenericUtils.verifyTextEquals(strTitle, strTitleToVerify),
          "The title is '" + strTitle + "' as expected", "The title is not correct : " + strTitle,
          driver, extentedReport, true);

      Log.softAssertThat(GenericUtils.verifyTextEquals(strLanguage, strLangToVerify),
          "The language is '" + strLanguage + "' as expected",
          "The language is not correct : " + strLanguage, driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(
      description = "Verify that the Insurer User’s actual name is displayed in the Agent dashboard",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_478(String browser) throws Exception {

    String tcId = "TC_478";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String loginName = testData.get("UserName");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into SIAAS Home Page",
          "Login failed", driver, extentedReport, true);
      String strUserLogin = homePage.getLoginUserName();
      Log.softAssertThat(GenericUtils.verifyTextEquals(strUserLogin, "Welcome " + loginName),
          "The login username is '" + loginName + "' as expected",
          "The login username is incorrect : " + strUserLogin, driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

}
