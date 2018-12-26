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
import com.ssp.uxp_pages.CardDetailsPage;
import com.ssp.uxp_pages.CustDashboardPage;
import com.ssp.uxp_pages.GetQuotePage;
import com.ssp.uxp_pages.HomePage;
import com.ssp.uxp_pages.LoginPage;
import com.ssp.uxp_pages.ManageContactDetailsPage;
import com.ssp.uxp_pages.NewQuotePage;
import com.ssp.uxp_pages.SearchPage;

public class UXP_EC_1598 extends BaseTest {
  private String webSite;
  public String PCLwebSite;

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
        "EC_ " + testCaseId + " - " + testDesc + " [" + browserwithos + "]", test, "Regression");
  }

  public HashMap<String, String> getTestData(String testcaseId) {
    String env =
        Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("env");
    String className = "UXP_EC_1598_" + env;
    return DataUtils.testDatabyID(testcaseId, className);
  }

  @Test(
      description = "To check Mailing Address checkbox is checked for the address which has marked Yes for mailing address",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_033(String browser) throws Exception {

    String tcId = "TC_033";
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
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);
      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      ManageContactDetailsPage manageContactDetailsPg =
          custdashboardpage.verifyndClickManageCntDetails(extentedReport, false);
      Log.softAssertThat(manageContactDetailsPg.verifyManageContactDetailsFields(),
          "Manage Contact Details screen is displayed",
          "Manage Contact Details screen is not displayed", driver, extentedReport, true);
      manageContactDetailsPg.clickEditButton(extentedReport, true);
      Log.softAssertThat(manageContactDetailsPg.verifyMailingAddressChkBoxDisabled(),
          "'Mailing Address' checkbox is checked for address which has marked as mailing address 'Yes'",
          "'Mailing Address' checkbox is not checked for address which has marked as mailing address 'Yes'",
          driver, extentedReport);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally

  }

  @Test(
      description = "Check that the Address Detail section is displayed In the Manage Contact Details modal, select edit against a MANUAL address to reveal the address details",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_034(String browser) throws Exception {

    String tcId = "TC_034";
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
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);
      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      ManageContactDetailsPage manageContactDetailsPg =
          custdashboardpage.verifyndClickManageCntDetails(extentedReport, false);
      Log.softAssertThat(manageContactDetailsPg.verifyManageContactDetailsFields(),
          "Manage Contact Details screen is displayed",
          "Manage Contact Details screen is not displayed", driver, extentedReport, true);
      manageContactDetailsPg.clickEditButton(extentedReport, true);
      manageContactDetailsPg.clickEnterManuallyLink(extentedReport, false);
      Log.softAssertThat(manageContactDetailsPg.uielement.verifyPageElements(
          Arrays.asList("sectionManualAddress", "sectionReplaceAddress"), manageContactDetailsPg),
          "System displays Replace Address Section & Address Details Section",
          "System did not displays Replace Address Section & Address Details Section", driver,
          extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally

  }

  @Test(
      description = "To check fields are getting disable after click in the  Mailing address Edit button",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_035(String browser) throws Exception {

    String tcId = "TC_035";
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
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);
      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      ManageContactDetailsPage manageContactDetailsPg =
          custdashboardpage.verifyndClickManageCntDetails(extentedReport, false);
      Log.softAssertThat(manageContactDetailsPg.verifyManageContactDetailsFields(),
          "Manage Contact Details screen is displayed",
          "Manage Contact Details screen is not displayed", driver, extentedReport, true);
      manageContactDetailsPg.clickEditButton(extentedReport, true);
      Log.softAssertThat(manageContactDetailsPg.verifyMailingAddressFieldsDisabled(),
          "Mailing Address fields Type,AddressLine1,AddressLine2,AddressLine3,PostCode & Country are disabled",
          "Mailing Address fields Type,AddressLine1,AddressLine2,AddressLine3,PostCode & Country are not disabled",
          driver, extentedReport);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "To check user will be prompted to search for a new address after click in the Edit button",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_038(String browser) throws Exception {

    String tcId = "TC_038";
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
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);
      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      ManageContactDetailsPage manageContactDetailsPg =
          custdashboardpage.verifyndClickManageCntDetails(extentedReport, false);
      Log.softAssertThat(manageContactDetailsPg.verifyManageContactDetailsFields(),
          "Manage Contact Details screen is displayed",
          "Manage Contact Details screen is not displayed", driver, extentedReport, true);
      manageContactDetailsPg.clickEditButton(extentedReport, true);
      Log.softAssertThat(
          manageContactDetailsPg.uielement.verifyPageElements(
              Arrays.asList("sectionReplaceAddress", "lnkEnterManyually"), manageContactDetailsPg),
          "System prompted to search for a new address",
          "System not prompted to search for a new address", driver, extentedReport);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(description = "To check user can not search address only by entering Housename or number",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_039(String browser) throws Exception {

    String tcId = "TC_039";
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
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);
      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      ManageContactDetailsPage manageContactDetailsPg =
          custdashboardpage.verifyndClickManageCntDetails(extentedReport, false);
      Log.softAssertThat(manageContactDetailsPg.verifyManageContactDetailsFields(),
          "Manage Contact Details screen is displayed",
          "Manage Contact Details screen is not displayed", driver, extentedReport, true);
      manageContactDetailsPg.clickEditButton(extentedReport, true);
      manageContactDetailsPg.enterReplaceAddressDetails("HouseName", "2", extentedReport, true);
      manageContactDetailsPg.clickFindAddressButton(extentedReport, false);
      Log.softAssertThat(
          manageContactDetailsPg.verifyPostCodeErrorMsg(manageContactDetailsPg.POSTCODE_ERROR_MSG),
          "System throw error message : " + manageContactDetailsPg.POSTCODE_ERROR_MSG,
          "System did not throw error message : " + manageContactDetailsPg.POSTCODE_ERROR_MSG,
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

  }// TC_039

  @Test(
      description = "To check user can enter search criteria into the Postcode for triggering  quick address search",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_040(String browser) throws Exception {

    String tcId = "TC_040";
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
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);
      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      ManageContactDetailsPage manageContactDetailsPg =
          custdashboardpage.verifyndClickManageCntDetails(extentedReport, false);
      Log.softAssertThat(manageContactDetailsPg.verifyManageContactDetailsFields(),
          "Manage Contact Details screen is displayed",
          "Manage Contact Details screen is not displayed", driver, extentedReport, true);
      manageContactDetailsPg.clickEditButton(extentedReport, true);
      manageContactDetailsPg.enterReplaceAddressDetails("PostCode", "AB101AH", extentedReport,
          true);
      manageContactDetailsPg.clickFindAddressButton(extentedReport, false);
      Log.softAssertThat(
          manageContactDetailsPg.uielement.verifyPageElements(Arrays.asList("btnSave"),
              manageContactDetailsPg),
          "System displayed relevant address in the screen",
          // + manageContactDetailsPg.getPostCodeAddressFromList(),
          "System did not displayed relevant address in the screen",
          // + manageContactDetailsPg.getPostCodeAddressFromList(),
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(description = "To check user enter housename/number & Postcode to find exact address",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_041(String browser) throws Exception {

    String tcId = "TC_041";
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
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);
      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      ManageContactDetailsPage manageContactDetailsPg =
          custdashboardpage.verifyndClickManageCntDetails(extentedReport, false);
      Log.softAssertThat(manageContactDetailsPg.verifyManageContactDetailsFields(),
          "Manage Contact Details screen is displayed",
          "Manage Contact Details screen is not displayed", driver, extentedReport, true);
      manageContactDetailsPg.clickEditButton(extentedReport, true);
      manageContactDetailsPg.enterReplaceAddressDetails("HouseName", "Aberdeen City Council",
          extentedReport, true);
      manageContactDetailsPg.enterReplaceAddressDetails("PostCode", "AB10 1AH", extentedReport,
          true);
      manageContactDetailsPg.clickFindAddressButton(extentedReport, true);
      Log.softAssertThat(
          manageContactDetailsPg.uielement.verifyPageElements(Arrays.asList("btnSave"),
              manageContactDetailsPg),
          "System displayed relevant address in the screen",
          "System did not displayed relevant address in the screen", driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(
      description = "To Check whether the Address details pane is closed when clicking Cancel button while editing address",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_042(String browser) throws Exception {

    String tcId = "TC_042";
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
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);
      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      ManageContactDetailsPage manageContactDetailsPg =
          custdashboardpage.verifyndClickManageCntDetails(extentedReport, false);
      Log.softAssertThat(manageContactDetailsPg.verifyManageContactDetailsFields(),
          "Manage Contact Details screen is displayed",
          "Manage Contact Details screen is not displayed", driver, extentedReport, true);
      manageContactDetailsPg.clickEditButton(extentedReport, true);
      manageContactDetailsPg.clickEnterManuallyLink(extentedReport, false);
      manageContactDetailsPg.enterReplaceAddressDetails("PostCode", "AB10 1AH", extentedReport,
          true);
      manageContactDetailsPg.clickCancelButton(extentedReport, false);
      Log.softAssertThat(
          manageContactDetailsPg.uielement.verifyPageElementsDoNotExist(Arrays.asList("btnCancel"),
              manageContactDetailsPg),
          "User stayed in the Manage contact Details modal window. But the Address Details pane is closed",
          "User stayed in the Manage contact Details modal window. But the Address Details pane is not closed",
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
      description = "To check user can able to enter address manually by selecting or Enter Manually link to change the address",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_044(String browser) throws Exception {

    String tcId = "TC_044";
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
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);
      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      ManageContactDetailsPage manageContactDetailsPg =
          custdashboardpage.verifyndClickManageCntDetails(extentedReport, false);
      Log.softAssertThat(manageContactDetailsPg.verifyManageContactDetailsFields(),
          "Manage Contact Details screen is displayed",
          "Manage Contact Details screen is not displayed", driver, extentedReport, true);
      manageContactDetailsPg.clickEditButton(extentedReport, true);
      manageContactDetailsPg.enterReplaceAddressDetails("PostCode", "AB10 1AH", extentedReport,
          true);
      manageContactDetailsPg.clickFindAddressButton(extentedReport, false);
      manageContactDetailsPg.clickFindAddressDetail(extentedReport, true);
      manageContactDetailsPg.clickSaveButton(extentedReport, false);
      Log.softAssertThat(manageContactDetailsPg.verifyFullAddressPostCode("AB10 1AH"),
          "User is able to enter address manually by selecting or Enter Manually link to change the address",
          "User is not able to enter address manually by selecting or Enter Manually link to change the address",
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
      description = "To check  User able to replace address in the Manage Contact Detail screen by using Quick Search  and 'or Enter Manually' link",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_045(String browser) throws Exception {

    String tcId = "TC_045";
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
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);
      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      ManageContactDetailsPage manageContactDetailsPg =
          custdashboardpage.verifyndClickManageCntDetails(extentedReport, false);
      Log.softAssertThat(manageContactDetailsPg.verifyManageContactDetailsFields(),
          "Manage Contact Details screen is displayed",
          "Manage Contact Details screen is not displayed", driver, extentedReport, true);
      manageContactDetailsPg.clickEditButton(extentedReport, true);
      manageContactDetailsPg.clickEnterManuallyLink(extentedReport, false);
      Log.softAssertThat(manageContactDetailsPg.verifyReplaceAddressTypeAndHouseNameDisabled(),
          "System not displayed Housename or number field",
          "System displayed Housename or number field", driver, extentedReport, true);

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
      description = "To check all fields are getting display after selecting 'or Enter Manually' link in Replace Address Section",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_046(String browser) throws Exception {

    String tcId = "TC_046";
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
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);
      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      ManageContactDetailsPage manageContactDetailsPg =
          custdashboardpage.verifyndClickManageCntDetails(extentedReport, false);
      Log.softAssertThat(manageContactDetailsPg.verifyManageContactDetailsFields(),
          "Manage Contact Details screen is displayed",
          "Manage Contact Details screen is not displayed", driver, extentedReport, true);
      manageContactDetailsPg.clickEditButton(extentedReport, true);
      manageContactDetailsPg.clickEnterManuallyLink(extentedReport, false);
      Log.softAssertThat(
          manageContactDetailsPg.uielement
              .verifyPageElements(Arrays.asList("fldReplaceAddressLine1", "fldReplaceAddressLine2",
                  "fldReplaceAddressLine3", "fldReplaceCountry", "fldReplaceAddressPostCode",
                  "btnCancel", "btnSave"), manageContactDetailsPg),
          "System displays list of fields after selecting 'or Enter Manually' link 'AddressLine1','AddressLine2','AddressLine3','PostCode','Country',Save & Cancel' buttons",
          "System not displays list of fields after selecting 'or Enter Manually' link 'AddressLine1','AddressLine2','AddressLine3','PostCode','Country',Save & Cancel' buttons",
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
      description = "To check that if the address you changed is the mailing address, that when you return to the customer dashboard the new address is displayed in the Contact Details section",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_046a(String browser) throws Exception {

    String tcId = "TC_046a";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String address = testData.get("ContactAddress");

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

  @Test(groups = "Business_Scenario",
      description = "Verify that corprate contact is able to replace",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_002(String browser) throws Exception {

    String tcId = "BS_002";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    // String address = testData.get("ContactAddress");

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
      searchPage.createCorporateContact(testData, extentedReport, true);
      CustDashboardPage custdashboardpage =
          searchPage.confirmCreateCustomer_CC(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);
      custdashboardpage.verifyAndClickManageBusinesDeatils(extentedReport, false);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("sectionReplaceAddress"),
              custdashboardpage),
          "System displays Replace Address Section",
          "System did not displays Replace Address Section", driver, extentedReport, true);
      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }

  }

  @Test(groups = "Business_Scenario",
      description = "Check that able to add multiple mailing address",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_004(String browser) throws Exception {

    String tcId = "BS_004";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String address[] = {"AB10 1AH", "GU11 1PZ"};

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
      ManageContactDetailsPage manageContactDetailsPg =
          custdashboardpage.verifyndClickManageCntDetails(extentedReport, false);
      Log.softAssertThat(manageContactDetailsPg.verifyManageContactDetailsFields(),
          "Manage Contact Details screen is displayed",
          "Manage Contact Details screen is not displayed", driver, extentedReport, true);
      manageContactDetailsPg.clickAddNewAddressButton(extentedReport, true);
      manageContactDetailsPg.enterNewAddressDetails("AB10 1AH", "Business", extentedReport, true);
      manageContactDetailsPg =
          custdashboardpage.verifyndClickManageCntDetails(extentedReport, false);
      for (String postCode : address) {
        Log.softAssertThat(manageContactDetailsPg.verifyFullAddressPostCode(postCode),
            "User is able to add multiple address", "User is not able to add multiple address",
            driver, extentedReport, true);
      }

      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(groups = "Business_Scenario",
      description = "Change the Mailing Adrress and Start MTA process and check whether correct address is being reflected",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_011(String browser) throws Exception {

    String tcId = "BS_011";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("BrandName");
    String address = testData.get("ContactAddress");
    String MTAcreationReason = testData.get("MTAdjReason");
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
      getquotepage.clickConfirmMTA(extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "Navigated to Customer dashboard page", "Not navigated to Customer dashboard page",
          driver, extentedReport, true);

      Log.softAssertThat(custdashboardpage.verifyContactAddressDetails(address),
          "System displayed newly mailing address under Contact Detail section : " + address,
          "System not displayed newly mailing address under Contact Detail section : " + address,
          driver, extentedReport, true);
      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }

  @Test(groups = "Business_Scenario",
      description = "Verify that address lookup option should be available after user has selected address but not yet saved and also User should have option to delete the address for starting fresh search",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void BS_008(String browser) throws Exception {

    String tcId = "BS_008";
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
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);
      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(false, extentedReport);
      ManageContactDetailsPage manageContactDetailsPg =
          custdashboardpage.verifyndClickManageCntDetails(extentedReport, false);
      Log.softAssertThat(manageContactDetailsPg.verifyManageContactDetailsFields(),
          "Manage Contact Details screen is displayed",
          "Manage Contact Details screen is not displayed", driver, extentedReport, true);
      manageContactDetailsPg.clickAddNewAddressButton(extentedReport, true);
      manageContactDetailsPg.enterAddress("AB10 1AH", "Business");
      Log.softAssertThat(
          manageContactDetailsPg.uielement.verifyPageElements(
              Arrays.asList("lstAddAddressPostCodeDetail"), manageContactDetailsPg),
          "Address Look Up option is available after find the address by postcode",
          "Address Look Up option is not available in manage contact details page", driver,
          extentedReport, true);
      Log.testCaseResult(extentedReport);

    } // try
    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.close();
    }
  }
}
