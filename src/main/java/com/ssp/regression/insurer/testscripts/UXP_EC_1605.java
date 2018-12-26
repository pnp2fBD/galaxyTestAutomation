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
public class UXP_EC_1605 extends BaseTest {

  private String webSite;
  public String PCLwebSite;
  String userName;
  String password;
  String description;
  String firstName;
  String lastName;
  String brandName;
  String title;
  String Corecover;
  String contactName = "";

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

    return Log.testCaseInfo("EC - " + testCaseId + " [" + test + "]",
        "EC - " + testCaseId + " - " + testDesc + " [" + browserwithos + "]", test, "");
  }

  public HashMap<String, String> getTestData(String testcaseId) {
    String env =
        Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("env");
    String className = "UXP_EC_1605_" + env;
    return DataUtils.testDatabyID(testcaseId, className);
  }

  @Test(
      description = "To check  that the CCA is landed to the Engagement Centre search screen, after selecting the brand in the popup window",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 2)
  public void TC_142(String browser) throws Exception {

    String tcId = "TC_142";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    userName = testData.get("UserName");
    password = testData.get("Password");
    description = testData.get("Description");
    brandName = testData.get("Brand Name");

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

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();

      // Search page verification
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Engagement Centre Search Page is verified",
          "Engagement Centre Search Page is not verified", driver, extentedReport, true);
      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(
      description = "To check that the \"All Brands\" option is NOT listed in \"My Brands\" pop up window, when click on \"Take Call/Make Call\" action button",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 3)
  public void TC_144_146(String browser) throws Exception {

    String tcId = "TC_144_146";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    userName = testData.get("UserName");
    password = testData.get("Password");
    description = testData.get("Description");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      Log.message("<b>Verification for TC_144</b>", extentedReport);
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      Log.softAssertThat(
          !homePage.uielement.verifyPageElements(Arrays.asList("btnAllBrands"), homePage),
          "All Brands option is not displayed in My Brands popup",
          "All Brands option is displayed in My Brands popup", driver, extentedReport, true);

      Log.message("<b>Verification for TC_146</b>", extentedReport);
      homePage.clickBrandPopupCancelBtn(extentedReport, true);

      // Click on AdminTask
      homePage.clickAdminTask(extentedReport);
      Log.softAssertThat(
          homePage.uielement.verifyPageElements(Arrays.asList("btnAllBrands"), homePage),
          "All Brands option is displayed in My Brands popup",
          "All Brands option is not displayed in My Brands popup", driver, extentedReport, true);
      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(
      description = "To check selected Brand screen will not save when CCA return back from Customer Dashboard to Agent Dashboard",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 4)
  public void TC_149_150_151(String browser) throws Exception {

    String tcId = "TC_149_150_151";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    userName = testData.get("UserName");
    password = testData.get("Password");
    description = testData.get("Description");
    brandName = testData.get("Brand Name");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      Log.message("<b>Setup for TC_149</b>", extentedReport);
      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(), "Successfully logged into Home Page",
          "Login failed", driver, extentedReport, true);

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);

      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      // searchPage.enterCustomerDetails(testData, true, extentedReport);
      HashMap<String, String> custDetails =
          searchPage.enterCustomerDetails(testData, true, extentedReport);
      firstName = custDetails.get("First Name");
      lastName = custDetails.get("Last Name");

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      custdashboardpage.clickComplete(extentedReport);
      homePage.doLogout(extentedReport);

      Log.message("<b>Verification steps for TC_149</b>", extentedReport);
      loginPage.loginToSSP(userName, password, false, extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);
      Log.message("Searching with the last and first name ", extentedReport);
      searchPage.clickComplete(extentedReport);
      homePage = new HomePage(driver).get();
      Log.softAssertThat(homePage.verifyAgentDashboard(), "Agent Dashboard is verified",
          "Agent Dashboard is not verified", driver, extentedReport, true);

      Log.message("<b>Verification steps for TC_150</b>", extentedReport);
      homePage.clickTakeCall(extentedReport);
      Log.softAssertThat(homePage.verifyBrandsPopup(), "My Brands Popup is opened",
          "My Brands Popup is not opened", driver, extentedReport);

      Log.message("<b>Verification steps for TC_151</b>", extentedReport);
      homePage.clickBrandPopupCancelBtn(extentedReport, true);
      Log.softAssertThat(homePage.verifyAgentDashboard(), "Agent Dashboard is verified",
          "Agent Dashboard is not verified", driver, extentedReport, true);
      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(
      description = "To check for Insurer,brand name is displayed alongside the address of the customer in the customer search result page",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 5)
  public void TC_152_154(String browser) throws Exception {

    String tcId = "TC_152_154";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    userName = testData.get("UserName");
    password = testData.get("Password");
    description = testData.get("Description");
    brandName = testData.get("Brand Name");
    firstName = testData.get("First Name");
    lastName = testData.get("Last Name");
    title = testData.get("Title");
    Corecover = testData.get("Cover");
    String searchKeyWord = "ECTestSearch";
    contactName = firstName + " " + lastName;

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");
    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("Engagement Centre Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application
      HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);
      // Log.message("Searching with the last and first name ",
      // extentedReport);

      if (!searchPage.verifySearchResultCount("Results", extentedReport, true)) {

        Log.message("Searched contact is not displayed in search results", extentedReport);
        Log.message("<b>Setup for TC_152_154</b>", extentedReport);
        // Enter Customer Details
        searchPage.clickCreateCustomer(true, extentedReport);
        searchPage.enterCustomerDetails(testData, true, extentedReport);

        // Confirm customer details and create customer
        CustDashboardPage custdashboardpage =
            searchPage.confirmCreateCustomer(true, extentedReport);

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
        // Get and Buy Quote
        newquotepage.clickGetQuote(extentedReport);
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
        custdashboardpage = newquotepage.confirmPayment(testData, extentedReport);
        Log.softAssertThat(
            custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
                custdashboardpage),
            "Custdashboardpage is Verified", "Custdashboardpage not Verified", driver,
            extentedReport, true);
        custdashboardpage.clickComplete(extentedReport);

        Log.softAssertThat(homePage.verifyHomePage(), "Successfully navigated to UXP Home Page",
            "Login failed", driver, extentedReport, true);
        homePage.clickTakeCall(extentedReport);
        homePage.clickMyBrands(brandName, extentedReport);
        searchPage.enterLastName(searchKeyWord, extentedReport);
        searchPage.enterFirstName(firstName, extentedReport);
        searchPage.clickSearch(extentedReport);

      } else {

        Log.message("Searched contact is displayed in search results", extentedReport);
        Log.message("<b>Setup for TC_152_154</b>", extentedReport);
        searchPage.clearSearch(extentedReport);
        searchPage.enterLastName(searchKeyWord, extentedReport);
        searchPage.enterFirstName(firstName, extentedReport);
        searchPage.clickSearch(extentedReport);
      }
      Log.message("<b>Verification steps for TC_152</b>", extentedReport);
      Log.softAssertThat(searchPage.isSearchTitleAvaialble(extentedReport),
          "Brand column is displayed in search results",
          "Brand column is not displayed in search results", driver, extentedReport);

      Log.message("<b>Verification steps for TC_154</b>", extentedReport);
      searchPage.selectcontactFromList(contactName, extentedReport, true);
      searchPage.verifyPolicyTableInSearchResult(extentedReport, true);
      Log.softAssertThat(searchPage.isSearchTitleAvaialble(extentedReport),
          "Brand column is displayed in search results",
          "Brand column is not displayed in search results", driver, extentedReport);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(
      description = "To check CCA can able to create New Customer after selecting brand from \"My Brand\" list",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 6)
  public void TC_157_158(String browser) throws Exception {

    String tcId = "TC_157_158";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    userName = testData.get("UserName");
    password = testData.get("Password");
    description = testData.get("Description");
    brandName = testData.get("Brand Name");

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

      // Click on Take Call link
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);

      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.message("New customer is created after selecting brand", extentedReport);

      Log.message("<b>Verification for TC_158</b>", extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("imgHeadOfficeInHeader"),
              custdashboardpage),
          "Selected brand is displayed in customer dashboard page",
          "Selected brand is not displayed in customer dashboard page", driver, extentedReport,
          true);
      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(
      description = "To check that the CCA is prompted to select again the brand after clicked \"Admin\" action and create new customer",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 7)
  public void TC_160_161_162(String browser) throws Exception {

    String tcId = "TC_160_161_162";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    userName = testData.get("UserName");
    password = testData.get("Password");
    description = testData.get("Description");
    brandName = testData.get("Brand Name");
    String warningMsg =
        "You must select a brand before you can proceed with creating a new customer";

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

      // Click on Take Call link
      homePage.clickAdminTask(extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);

      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      Log.message("<b>Verification for TC_160</b>", extentedReport);
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("modalMyBrands"), searchPage),
          "System is prompted the My brands popup to select the brand",
          "System is not prompted the My brands popup to select the brand", driver, extentedReport,
          true);
      Log.message("<b>Verification for TC_161</b>", extentedReport);
      Log.softAssertThat(searchPage.verifyWarningMsgForBrand(warningMsg, extentedReport, true),
          "Waring message is displayed in My Brands popup. Message: " + warningMsg,
          "Waring message is not displayed in My Brands popup.Message: " + warningMsg, driver,
          extentedReport, true);
      Log.message("<b>Verification for TC_162</b>", extentedReport);
      searchPage.clickBrandPopupCancelBtn(extentedReport, true);
      Log.softAssertThat(
          searchPage.uielement.verifyPageElements(Arrays.asList("txtPolicyNumber"), searchPage),
          "Engagement Centre Search Page Verified", "Engagement Centre Search Page not Verified",
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
      description = "To check that All Brand option will displayed in the My brand pop up if only two Brand has been configured in the Business Unit Profile",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 1)
  public void TC_139c_142a(String browser) throws Exception {

    String tcId = "TC_139c_142a";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    userName = testData.get("UserName");
    password = testData.get("Password");
    description = testData.get("Description");
    brandName = testData.get("Brand Name");
    String searchKeyword = "ECTestSearch";

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

      // Click on Take Call link
      homePage.clickAdminTask(extentedReport);
      Log.message("<b>Verification for TC_139c</b>", extentedReport);
      Log.softAssertThat(
          homePage.uielement.verifyPageElements(Arrays.asList("btnAllBrands"), homePage),
          "Verified that All Brands option is displayed in My Brands popup",
          "All Brands option is not displayed in My Brands popup", driver, extentedReport, true);

      Log.message("<b>Verification for TC_142a</b>", extentedReport);
      homePage.clickMyBrands(brandName, extentedReport);
      SearchPage searchPage = new SearchPage(driver, extentedReport).get();
      searchPage.enterLastName(searchKeyword, extentedReport);
      searchPage.clickSearch(extentedReport);
      Log.softAssertThat(searchPage.verifyBrandNameForAllContacts(brandName, extentedReport, true),
          "All searched contacts are respect to " + brandName + " brand",
          "All searched contacts are not respect to " + brandName + " brand", driver,
          extentedReport, true);
      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(
      description = "To check that only two brand displayed in the My brand pop up which has configured in the Business Unit Profile",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider", priority = 0)
  public void TC_139b(String browser) throws Exception {

    String tcId = "TC_139b";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    userName = testData.get("UserName");
    password = testData.get("Password");
    description = testData.get("Description");
    brandName = testData.get("Brand Name");
    String[] brandList = brandName.split("\\|");

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

      // Click on Take Call link
      homePage.clickMakeCall(extentedReport);
      Log.softAssertThat(homePage.verifyBrandsInPopup(brandList[0], extentedReport, true),
          brandList[0] + " brand is displayed in My Brands popup",
          brandList[0] + " brand is not displayed in My Brands popup", driver, extentedReport,
          true);
      Log.softAssertThat(homePage.verifyBrandsInPopup(brandList[1], extentedReport, false),
          brandList[1] + " brand is displayed in My Brands popup",
          brandList[1] + " brand is not displayed in My Brands popup", driver, extentedReport);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }
}
