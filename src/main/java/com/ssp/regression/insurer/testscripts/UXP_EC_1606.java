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
import com.ssp.uxp_pages.GetQuotePage;
import com.ssp.uxp_pages.HomePage;
import com.ssp.uxp_pages.LoginPage;
import com.ssp.uxp_pages.NewQuotePage;
import com.ssp.uxp_pages.SearchPage;
import com.ssp.uxp_pages.ViewTransactionPage;

public class UXP_EC_1606 extends BaseTest {

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
        testCaseId + " - " + testDesc + " [" + browserwithos + "]", test, "UXP_NFR Team");
  }

  public HashMap<String, String> getTestData(String testcaseId) {
    String env =
        Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("env");
    String className = "UXP_EC_1606_" + env;
    return DataUtils.testDatabyID(testcaseId, className);
  }


  @Test(
      description = "To check that the Add/Remove policy holder section is displayed at the bottom of the Customer Dashboard page",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_166(String browser) throws Exception {

    String tcId = "TC_166";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

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
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

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

      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("policyholdersection"),
              newquotepage),
          "Add/Remove Policy holder section is displayed",
          "Add/Remove Policy holder section is not displayed", driver, extentedReport, true);
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
      description = "To check that the Policyholders grid will contain four columns: Name, Main (containing checkboxes), Blank column (containing bin icon where applicable)",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_167(String browser) throws Exception {

    String tcId = "TC_167";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

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
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

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

      Log.softAssertThat(newquotepage.verifyContactHeader(extentedReport),
          "Contact header is present in policy holder table",
          "Contact header is not present in policy holder table", driver, extentedReport, true);

      Log.softAssertThat(newquotepage.verifyMainHeader(extentedReport),
          "Main header is present in policy holder table",
          "Main header is not present in policy holder table", driver, extentedReport, false);

      Log.softAssertThat(newquotepage.verifyBinIconHeader(extentedReport),
          "BinIcon is present in policy holder table",
          "BinIcon is not present in policy holder table", driver, extentedReport, false);
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
      description = "To check that the main Contact will always be displayed first in the Policyholders grid",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_168(String browser) throws Exception {

    String tcId = "TC_168";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");

    String NameToVerify = firstName + " " + lastName;

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

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
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

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

      Log.softAssertThat(newquotepage.verifyMainContact(NameToVerify, true, extentedReport),
          "Main Contact is displayed first in policy header grid",
          "Main Contact is not displayed first in policy header grid", driver, extentedReport,
          true);
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
      description = "To check that the additional policyholders is displayed below the Main contact and are in alphabetical order by surname",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_169(String browser) throws Exception {

    String tcId = "TC_169";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);
    HashMap<String, String> testData3 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String jointPolicyfirstName2 = "B" + testData.get("First Name");
    String jointPolicylastName2 = "A" + testData.get("Last Name");

    String jointPolicyfirstName3 = "A" + testData.get("First Name");
    String jointPolicylastName3 = "B" + testData.get("Last Name");

    testData2.put("First Name", jointPolicyfirstName2);
    testData2.put("Last Name", jointPolicylastName2);

    testData3.put("First Name", jointPolicyfirstName3);
    testData3.put("Last Name", jointPolicylastName3);

    String jointPolicyName2 = jointPolicyfirstName2 + " " + jointPolicylastName2;
    String jointPolicyName3 = jointPolicyfirstName3 + " " + jointPolicylastName3;

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

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Customer2 Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Customer3 Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData3, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

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

      // Adding joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.addJointPolicyHolder(testData2, extentedReport);

      Log.softAssertThat(
          newquotepage.verifyPolicyHolderContactName(jointPolicyName2, true, extentedReport),
          jointPolicyName2
              + " - Contact Name is added as additional policy holder below main contact",
          jointPolicyName2 + " - Contact Name is not  added as additional policy holder", driver,
          extentedReport, true);

      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.addJointPolicyHolder(testData3, extentedReport);

      Log.softAssertThat(
          newquotepage.verifyPolicyHolderContactName(jointPolicyName3, true, extentedReport),
          jointPolicyName3
              + " - Contact Name is added as additional policy holder below main contact",
          jointPolicyName3
              + " - Contact Name is not added as additional policy holder below main contact",
          driver, extentedReport, true);

      Log.softAssertThat(newquotepage.verify_ContactSurName_Alphabeticalorder(extentedReport),
          "Additional policyholders is displayed below the Main contact are in alphabetical order by surname",
          "Additional policyholders is not displayed below the Main contact are in alphabetical order by surname",
          driver, extentedReport, true);
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
      description = "To check that the Contact Name contains the Title, First Name and Last Name informations",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_170(String browser) throws Exception {

    String tcId = "TC_170";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");
    String title = testData.get("Title");
    String NameToVerify = title + " " + firstName + " " + lastName;

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

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
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

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

      Log.softAssertThat(
          newquotepage.verifyPolicyHolderContactName(NameToVerify, true, extentedReport),
          "Contact Name contains the Title, First Name and Last Name informations",
          "Contact Name does not contains the Title, First Name and Last Name informations", driver,
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
      description = "To check that the Main Policyholder is identified by the checkbox being checked",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_172(String browser) throws Exception {

    String tcId = "TC_172";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");
    String NameToVerify = firstName + " " + lastName;

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

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
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

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

      Log.softAssertThat(newquotepage.verifyMainContact(NameToVerify, true, extentedReport),
          "Main Policyholder is identified by the checkbox being checked" + NameToVerify,
          "Main Policyholder is not identified by the checkbox being checked" + NameToVerify,
          driver, extentedReport, true);
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
      description = "To check that the Bin icon is displayed against Contacts (except Main policy holder) who can be removed from the Policyholder",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_173(String browser) throws Exception {

    String tcId = "TC_173";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String jointPolicyfirstName = testData.get("Last Name");
    String jointPolicylastName = testData.get("First Name");

    testData2.put("First Name", jointPolicyfirstName);
    testData2.put("Last Name", jointPolicylastName);

    String jointPolicyName = jointPolicyfirstName + " " + jointPolicylastName;

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

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

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

      // Adding joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.addJointPolicyHolder(testData2, extentedReport);

      Log.softAssertThat(!newquotepage.verifyMaincheckbox(jointPolicyName, false, extentedReport),
          "Main checkbox is not selected for the contact name - " + jointPolicyName,
          "Main checkbox is selected for the contact name - " + jointPolicyName, driver,
          extentedReport, true);


      Log.softAssertThat(newquotepage.verifyBinIcon(jointPolicyName, false, extentedReport),
          "Bin Icon is displyed against Contacts (except Main policy holder) who can be removed from the Policyholder",
          "Bin Icon is not displyed against Contacts (except Main policy holder) who can be removed from the Policyholder",
          driver, extentedReport, true);
      Log.testCaseResult(extentedReport);


    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(description = "To check that the Add Policyholder button is displayed beneath the grid",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_174(String browser) throws Exception {

    String tcId = "TC_174";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

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
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

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

      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("btnAddPolicyHolder"),
              newquotepage),
          "Add Policy holder button is displayed", "Add Policy holder button is not displayed",
          driver, extentedReport, true);

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
      description = "To check that the contact search model/window is dispalyed when click on Add Policyholder button",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_175(String browser) throws Exception {

    String tcId = "TC_175";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

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
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

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

      newquotepage.clickAddPolicyHolderButton(extentedReport);

      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("wndJointPolicyHolder"),
              newquotepage),
          "The Joint policy contact search window  is displayed",
          "The Joint policy contact search window  is not displayed", driver, extentedReport, true);
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
      description = "To check that the user is able to access Add/Remove Joint Policyholders feature, when performs a New Business Quote creation",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_176(String browser) throws Exception {

    String tcId = "TC_176";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String jointPolicyfirstName = testData.get("Last Name");
    String jointPolicylastName = testData.get("First Name");
    String jointPolicyName = jointPolicyfirstName + " " + jointPolicylastName;

    testData2.put("First Name", jointPolicyfirstName);
    testData2.put("Last Name", jointPolicylastName);

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

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

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

      // Verify button AddPolicyHolder is accessable
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("btnAddPolicyHolder"),
              newquotepage),
          "Add joint Policyholder feature(Button) is accessable for the user",
          "Add joint Policyholder feature(Button) is not accessable for the user", driver,
          extentedReport, true);

      // Add joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.addJointPolicyHolder(testData2, extentedReport);

      Log.softAssertThat(
          newquotepage.verifyPolicyHolderContactName(jointPolicyName, false, extentedReport),
          jointPolicyName + " - is added successfully using add Joint Policyholders feature",
          jointPolicyName + " - is not added using add Joint Policyholders feature", driver,
          extentedReport, true);

      // Verify bin icon is accessable
      Log.softAssertThat(newquotepage.verifyBinIcon(jointPolicyName, false, extentedReport),
          "Remove joint Policyholder feature is accessable for the user (Bin icon is present)",
          "Remove joint Policyholder feature is not accessable for the user (Bin icon is not present)",
          driver, extentedReport, true);
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
      description = "To check that the user is able to access Add/Remove Joint Policyholders feature, when performs Edit/Buying a Quote",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_177(String browser) throws Exception {

    String tcId = "TC_177";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String jointPolicyfirstName = testData.get("Last Name");
    String jointPolicylastName = testData.get("First Name");
    String jointPolicyName = jointPolicyfirstName + " " + jointPolicylastName;

    testData2.put("First Name", jointPolicyfirstName);
    testData2.put("Last Name", jointPolicylastName);

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

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

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
      newquotepage.clickbacktoDashboard(extentedReport);

      GetQuotePage getQuotePage =
          custdashboardpage.clickEditfromManagePolicyDropdown(extentedReport, true);

      // Verify button AddPolicyHolder is accessable
      Log.softAssertThat(
          getQuotePage.uielement.verifyPageElements(Arrays.asList("btnAddPolicyHolder"),
              getQuotePage),
          "Add joint Policyholder feature(Button) is accessable for the user",
          "Add joint Policyholder feature(Button) is not accessable for the user", driver,
          extentedReport, true);

      // Add joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.addJointPolicyHolder(testData2, extentedReport);

      Log.softAssertThat(
          newquotepage.verifyPolicyHolderContactName(jointPolicyName, false, extentedReport),
          jointPolicyName + " - is added successfully using add Joint Policyholders feature",
          jointPolicyName + " - is not added using add Joint Policyholders feature", driver,
          extentedReport, true);


      // Verify bin icon is accessable
      Log.softAssertThat(getQuotePage.verifyBinIcon(jointPolicyName, false, extentedReport),
          "Remove joint Policyholder feature is accessable for the user (Bin icon is present)",
          "Remove joint Policyholder feature is not accessable for the user (Bin icon is not present)",
          driver, extentedReport, true);
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
      description = "To check that the user is able to access Add/Remove Joint Policyholders feature, when performs Mid-Term Adjustment",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_178(String browser) throws Exception {

    String tcId = "TC_178";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String jointPolicyfirstName = testData.get("Last Name");
    String jointPolicylastName = testData.get("First Name");
    String jointPolicyName = jointPolicyfirstName + " " + jointPolicylastName;

    testData2.put("First Name", jointPolicyfirstName);
    testData2.put("Last Name", jointPolicylastName);

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

      // Enter Customer1 Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);

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

      // Add joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.addJointPolicyHolder(testData2, extentedReport);

      Log.softAssertThat(
          newquotepage.verifyPolicyHolderContactName(jointPolicyName, true, extentedReport),
          jointPolicyName + " - additional policy holder is added",
          jointPolicyName + " - additional policy holder is not added", driver, extentedReport,
          true);

      newquotepage.clickBuy(extentedReport);
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
      // newquotepage.verifyPaymentTrasaction(extentedReport);

      // Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport), "Payment was
      // successful", "Payment was not successful", driver, extentedReport,true);
      custdashboardpage = newquotepage.confirmPayment(extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);
      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);

      // Select MTA from drop down
      custdashboardpage.clickPassVerification(extentedReport, false);
      custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport);

      // Fill up MTa data
      GetQuotePage getquotepage = custdashboardpage.enterMTADetails(testData, extentedReport, true);
      getquotepage.verifyGetQuotePage(extentedReport);

      // Verify button AddPolicyHolder is accessable
      Log.softAssertThat(
          getquotepage.uielement.verifyPageElements(Arrays.asList("btnAddPolicyHolder"),
              getquotepage),
          "Add joint Policyholder feature(Button) is accessable for the user",
          "Add joint Policyholder feature(Button) is not accessable for the user", driver,
          extentedReport, true);

      // Verify bin icon is accessable
      Log.softAssertThat(getquotepage.verifyBinIcon(jointPolicyName, false, extentedReport),
          "Remove joint Policyholder feature is accessable for the user (Bin icon is present)",
          "Remove joint Policyholder feature is not accessable for the user (Bin icon is not present)",
          driver, extentedReport, true);
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
      description = "To check that the user is able to access Add/Remove Joint Policyholders feature, when performs Edit Transaction",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_179(String browser) throws Exception {

    String tcId = "TC_179";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String jointPolicyfirstName = testData.get("Last Name");
    String jointPolicylastName = testData.get("First Name");
    String jointPolicyName = jointPolicyfirstName + " " + jointPolicylastName;

    testData2.put("First Name", jointPolicyfirstName);
    testData2.put("Last Name", jointPolicylastName);

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

      // Enter Customer1 Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);

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

      // Add joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.addJointPolicyHolder(testData2, extentedReport);

      Log.softAssertThat(
          newquotepage.verifyPolicyHolderContactName(jointPolicyName, true, extentedReport),
          jointPolicyName + " - additional policy holder is added",
          jointPolicyName + " - additional policy holder is not added", driver, extentedReport,
          true);

      newquotepage.clickBuy(extentedReport);
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
      // newquotepage.verifyPaymentTrasaction(extentedReport);

      // Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport), "Payment was
      // successful", "Payment was not successful", driver, extentedReport,true);
      custdashboardpage = newquotepage.confirmPayment(extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);

      // Select MTA from drop down
      custdashboardpage.clickPassVerification(extentedReport, false);
      custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport);

      // Fill up MTa data
      GetQuotePage getquotepage = custdashboardpage.enterMTADetails(testData, extentedReport, true);
      getquotepage.verifyGetQuotePage(extentedReport);

      getquotepage.clickBackToDashboardButton(extentedReport);

      // Select Row
      custdashboardpage.selectTransactionRow(extentedReport);
      custdashboardpage.clickEditTransactionButton(extentedReport, true);

      // Verify button AddPolicyHolder is accessable
      Log.softAssertThat(
          getquotepage.uielement.verifyPageElements(Arrays.asList("btnAddPolicyHolder"),
              getquotepage),
          "Add joint Policyholder feature(Button) is accessable for the user",
          "Add joint Policyholder feature(Button) is not accessable for the user", driver,
          extentedReport, true);

      // Verify bin icon is accessable
      Log.softAssertThat(getquotepage.verifyBinIcon(jointPolicyName, false, extentedReport),
          "Remove joint Policyholder feature is accessable for the user (Bin icon is present)",
          "Remove joint Policyholder feature is not accessable for the user (Bin icon is not present)",
          driver, extentedReport, true);

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
      description = "To check that the user is able to see the Joint Policyholders details, when performs View Transaction (Read Only)",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_180(String browser) throws Exception {

    String tcId = "TC_180";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String jointPolicyfirstName = testData.get("Last Name");
    String jointPolicylastName = testData.get("First Name");
    String jointPolicyName = jointPolicyfirstName + " " + jointPolicylastName;

    testData2.put("First Name", jointPolicyfirstName);
    testData2.put("Last Name", jointPolicylastName);

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

      // Enter Customer1 Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);

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

      // Add joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.addJointPolicyHolder(testData2, extentedReport);

      Log.softAssertThat(
          newquotepage.verifyPolicyHolderContactName(jointPolicyName, true, extentedReport),
          jointPolicyName + " - additional policy holder is added",
          jointPolicyName + " - additional policy holder is not added", driver, extentedReport,
          true);

      newquotepage.clickBuy(extentedReport);
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
      // newquotepage.verifyPaymentTrasaction(extentedReport);

      // Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport), "Payment was
      // successful", "Payment was not successful", driver, extentedReport,true);
      custdashboardpage = newquotepage.confirmPayment(extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);

      // Select MTA from drop down
      custdashboardpage.clickPassVerification(extentedReport, false);
      custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport);

      // Fill up MTa data
      GetQuotePage getquotepage = custdashboardpage.enterMTADetails(testData, extentedReport, true);
      getquotepage.verifyGetQuotePage(extentedReport);

      getquotepage.clickBackToDashboardButton(extentedReport);

      // Cilck view transaction link
      ViewTransactionPage viewtransaction =
          custdashboardpage.clickViewTransaction(extentedReport, true);

      viewtransaction.selectTab("Price Presentation", extentedReport, true);

      Log.softAssertThat(!viewtransaction.isViewBtnEnabled(),
          "Joint Policyholders details is in Read only mode, when performs View Transaction",
          "Joint Policyholders details is in not Read only mode, when performs View Transaction",
          driver, extentedReport, true);

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
      description = "To check that the user is able to access Add/Remove Joint Policyholders feature, when performs Quote variation",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_181(String browser) throws Exception {

    String tcId = "TC_181";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String jointPolicyfirstName = testData.get("Last Name");
    String jointPolicylastName = testData.get("First Name");
    String jointPolicyName = jointPolicyfirstName + " " + jointPolicylastName;

    testData2.put("First Name", jointPolicyfirstName);
    testData2.put("Last Name", jointPolicylastName);

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

      // Enter Customer1 Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);

      // Create New Quote
      custdashboardpage.clickNewQuote(true, extentedReport);
      custdashboardpage.enterNewQuotePastDate(5, 1, extentedReport, true); // for renewal quote
      custdashboardpage.selectNewQuoteDetails(testData, extentedReport, true);
      /*
       * //To enter start date SimpleDateFormat formDate = new SimpleDateFormat("dd/MM/yyyy");
       * Calendar cal = Calendar.getInstance(); cal.add(Calendar.YEAR, -1); cal.add(Calendar.DATE,
       * +14);
       * 
       * String strDate = formDate.format(cal.getTime()); System.out.println("Date : " + strDate);
       */
      // custdashboardpage.enterQuoteDetails(testData, true, extentedReport, strDate);
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

      // Add joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.addJointPolicyHolder(testData2, extentedReport);

      Log.softAssertThat(
          newquotepage.verifyPolicyHolderContactName(jointPolicyName, true, extentedReport),
          jointPolicyName + " - additional policy holder is added",
          jointPolicyName + " - additional policy holder is not added", driver, extentedReport,
          true);

      newquotepage.clickBuy(extentedReport);
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
      // newquotepage.verifyPaymentTrasaction(extentedReport);

      // Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport), "Payment was
      // successful", "Payment was not successful", driver, extentedReport,true);
      custdashboardpage = newquotepage.confirmPayment(extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);

      // Select invite renewal from drop down
      custdashboardpage.clickPassVerification(extentedReport, false);
      custdashboardpage.clickInviteRenewalfromManagePolicyDropdown(extentedReport, true);

      newquotepage.clickNextOne(extentedReport);
      newquotepage.clickNextTwo(extentedReport);

      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);

      newquotepage.clickGetQuote(extentedReport);

      // Verify button AddPolicyHolder is accessable
      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("btnAddPolicyHolder"),
              newquotepage),
          "Add joint Policyholder feature(Button) is accessable for the user",
          "Add joint Policyholder feature(Button) is not accessable for the user", driver,
          extentedReport, true);

      // Verify bin icon is accessable
      Log.softAssertThat(newquotepage.verifyBinIcon(jointPolicyName, false, extentedReport),
          "Remove joint Policyholder feature is accessable for the user (Bin icon is present)",
          "Remove joint Policyholder feature is not accessable for the user (Bin icon is not present)",
          driver, extentedReport, true);


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
      description = "To check that the Bin icon is NOT displayed against the Main Policyholder(the ONLY Policyholder)",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_182(String browser) throws Exception {

    String tcId = "TC_182";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");
    String NameToVerify = firstName + " " + lastName;

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

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
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

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

      Log.softAssertThat(newquotepage.verifyMaincheckbox(NameToVerify, false, extentedReport),
          "Main checkbox is selected for the contact name - " + NameToVerify,
          "Main checkbox is not selected for the contact name - " + NameToVerify, driver,
          extentedReport, true);

      Log.softAssertThat(!newquotepage.verifyBinIcon(NameToVerify, false, extentedReport),
          "Bin Icon is not displayed against the Main Policyholder(the ONLY Policyholder)",
          "Bin Icon is displayed against the Main Policyholder(the ONLY Policyholder)", driver,
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
      description = "To check that the configurable error message 'There must ALWAYS be a Main Policyholder' is displayed, If the CCA attempts to remove the check from the checkbox",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_183(String browser) throws Exception {

    String tcId = "TC_183";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");
    String NameToVerify = firstName + " " + lastName;

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

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
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

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

      Log.softAssertThat(newquotepage.verifyMaincheckbox(NameToVerify, false, extentedReport),
          "Main checkbox is selected for the contact name - " + NameToVerify,
          "Main checkbox is not selected for the contact name - " + NameToVerify, driver,
          extentedReport, false);

      Log.softAssertThat(!newquotepage.isMainCheckboxEnabled(),
          "Main check box is not enabled to click", "Main check box is enabled to click", driver,
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
      description = "To check that the confirmation for removal of policy holder popup window is prompted when click on Bin icon against a Policyholder who is NOT the Main Policyholder",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_184(String browser) throws Exception {

    String tcId = "TC_184";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String jointPolicyfirstName = testData.get("Last Name");
    String jointPolicylastName = testData.get("First Name");

    testData2.put("First Name", jointPolicyfirstName);
    testData2.put("Last Name", jointPolicylastName);
    String jointPolicyName = jointPolicyfirstName + " " + jointPolicylastName;

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

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

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

      // Adding joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.addJointPolicyHolder(testData2, extentedReport);

      // To click bin icon
      newquotepage.clickBinIcon(jointPolicyName, true, extentedReport);

      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("wndRemovePolicyPopup"),
              newquotepage),
          "confirmation for removal of policy holder popup window is prompted",
          "confirmation for removal of policy holder popup window is not prompted", driver,
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
      description = "To check that the selected policy holder is permanently removed from the list when click on Yes in the confirmation popup window",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_185(String browser) throws Exception {

    String tcId = "TC_185";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String jointPolicyfirstName = testData.get("Last Name");
    String jointPolicylastName = testData.get("First Name");

    testData2.put("First Name", jointPolicyfirstName);
    testData2.put("Last Name", jointPolicylastName);
    String jointPolicyName = jointPolicyfirstName + " " + jointPolicylastName;

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

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

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

      // Adding joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.addJointPolicyHolder(testData2, extentedReport);

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
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(
      description = "To check that the selected policy holder is not removed from the list when click on No in the confirmation popup window",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_186(String browser) throws Exception {

    String tcId = "TC_186";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String jointPolicyfirstName = testData.get("Last Name");
    String jointPolicylastName = testData.get("First Name");

    testData2.put("First Name", jointPolicyfirstName);
    testData2.put("Last Name", jointPolicylastName);
    String jointPolicyName = jointPolicyfirstName + " " + jointPolicylastName;

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

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

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

      // Adding joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.addJointPolicyHolder(testData2, extentedReport);

      // To click No to remove policy
      newquotepage.clickNoToRemovePolicy(jointPolicyName, true, extentedReport);

      Log.softAssertThat(
          newquotepage.verifyPolicyHolderContactName(jointPolicyName, true, extentedReport),
          jointPolicyName + " - is not removed from policy holder grid",
          jointPolicyName + " - is removed from policy holder grid", driver, extentedReport, true);

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
      description = "To check that the removal of the Policyholder is not effected, if the MTA is not accepted",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_187(String browser) throws Exception {

    String tcId = "TC_187";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String jointPolicyfirstName = testData.get("Last Name");
    String jointPolicylastName = testData.get("First Name");
    String jointPolicyName = jointPolicyfirstName + " " + jointPolicylastName;

    testData2.put("First Name", jointPolicyfirstName);
    testData2.put("Last Name", jointPolicylastName);

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

      // Enter Customer1 Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);

      /*
       * // Search with Valid user names searchPage.enterLastName(lastName, extentedReport);
       * searchPage.enterFirstName(firstName, extentedReport);
       * searchPage.clickSearch(extentedReport);
       * 
       * // Selecting the contact in search page CustDashboardPage custdashboardpage =
       * searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
       * Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
       * "Customer Dashboard Page is loaded properly",
       * "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);
       */

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

      // Add joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.addJointPolicyHolder(testData2, extentedReport);

      Log.softAssertThat(
          newquotepage.verifyPolicyHolderContactName(jointPolicyName, true, extentedReport),
          jointPolicyName + " - additional policy holder is added",
          jointPolicyName + " - additional policy holder is not added", driver, extentedReport,
          true);

      newquotepage.clickBuy(extentedReport);
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
      // newquotepage.verifyPaymentTrasaction(extentedReport);

      // Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport), "Payment was
      // successful", "Payment was not successful", driver, extentedReport,true);
      custdashboardpage = newquotepage.confirmPayment(extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport);

      // Fill up MTa data
      GetQuotePage getquotepage = custdashboardpage.enterMTADetails(testData, extentedReport, true);
      getquotepage.verifyGetQuotePage(extentedReport);

      // To click yes to remove policy
      getquotepage.clickYesToRemovePolicy(jointPolicyName, true, extentedReport);

      Log.softAssertThat(
          !getquotepage.verifyPolicyHolderContactName(jointPolicyName, true, extentedReport),
          jointPolicyName + " - is removed from policy holder grid",
          jointPolicyName + " - is not removed from policy holder grid", driver, extentedReport,
          true);

      getquotepage.clickBackToDashboardButton(extentedReport);

      Log.softAssertThat(custdashboardpage.verifyPolicyholderName(jointPolicyName),
          jointPolicyName
              + " - is not removed from policy holder seciotn, if the MTA is not saved/accepted",
          jointPolicyName
              + " - is removed from policy holder seciotn, if the MTA is not saved/accepted",
          driver, extentedReport, true);

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
      description = "To check that the removal of the Policyholder is effected, if the MTA is accepted",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_188(String browser) throws Exception {

    String tcId = "TC_188";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String jointPolicyfirstName = testData.get("Last Name");
    String jointPolicylastName = testData.get("First Name");
    String jointPolicyName = jointPolicyfirstName + " " + jointPolicylastName;

    testData2.put("First Name", jointPolicyfirstName);
    testData2.put("Last Name", jointPolicylastName);

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

      // Enter Customer1 Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);

      /*
       * // Search with Valid user names searchPage.enterLastName(lastName, extentedReport);
       * searchPage.enterFirstName(firstName, extentedReport);
       * searchPage.clickSearch(extentedReport);
       * 
       * // Selecting the contact in search page CustDashboardPage custdashboardpage =
       * searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
       * Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
       * "Customer Dashboard Page is loaded properly",
       * "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);
       */

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

      // Add joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.addJointPolicyHolder(testData2, extentedReport);

      Log.softAssertThat(
          newquotepage.verifyPolicyHolderContactName(jointPolicyName, true, extentedReport),
          jointPolicyName + " - additional policy holder is added",
          jointPolicyName + " - additional policy holder is not added", driver, extentedReport,
          true);

      newquotepage.clickBuy(extentedReport);
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
      // newquotepage.verifyPaymentTrasaction(extentedReport);

      // Log.softAssertThat(newquotepage.verifyPaymentTrasaction(extentedReport), "Payment was
      // successful", "Payment was not successful", driver, extentedReport,true);
      custdashboardpage = newquotepage.confirmPayment(extentedReport);

      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      custdashboardpage.clickfromManagePolicyDropdown(testData, true, extentedReport);

      // Fill up MTa data
      GetQuotePage getquotepage = custdashboardpage.enterMTADetails(testData, extentedReport, true);
      getquotepage.verifyGetQuotePage(extentedReport);

      // To click yes to remove policy
      getquotepage.clickYesToRemovePolicy(jointPolicyName, true, extentedReport);


      Log.softAssertThat(
          !getquotepage.verifyPolicyHolderContactName(jointPolicyName, true, extentedReport),
          jointPolicyName + " - is removed from policy holder seciotn",
          jointPolicyName + " - is not removed from policy holder seciotn", driver, extentedReport,
          true);

      getquotepage.clickSavedButton(extentedReport);

      Log.softAssertThat(!custdashboardpage.verifyPolicyholderName(jointPolicyName),
          jointPolicyName + " - is removed from policy holder seciotn, after the MTA is saved",
          jointPolicyName + " - is not removed from policy holder seciotn, after the MTA is saved",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);


    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }


  @Test(description = "To check that the user allowed to add again the removed policy holder",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_189(String browser) throws Exception {

    String tcId = "TC_189";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String jointPolicyfirstName = testData.get("Last Name");
    String jointPolicylastName = testData.get("First Name");
    String jointPolicyName = jointPolicyfirstName + " " + jointPolicylastName;

    testData2.put("First Name", jointPolicyfirstName);
    testData2.put("Last Name", jointPolicylastName);

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

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

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

      // Adding joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.addJointPolicyHolder(testData2, extentedReport);

      // To click yes to remove policy
      newquotepage.clickYesToRemovePolicy(jointPolicyName, true, extentedReport);

      Log.softAssertThat(
          !newquotepage.verifyPolicyHolderContactName(jointPolicyName, true, extentedReport),
          jointPolicyName + " - is removed from policy holder grid",
          jointPolicyName + " - is not removed from policy holder grid", driver, extentedReport,
          true);

      // Adding joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.addJointPolicyHolder(testData2, extentedReport);

      Log.softAssertThat(
          newquotepage.verifyPolicyHolderContactName(jointPolicyName, true, extentedReport),
          jointPolicyName + " - is added again after it was removed from policy holder grid",
          jointPolicyName + " - is not added again after it was removed from policy holder grid",
          driver, extentedReport, true);

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
      description = "To check that the user is allowed to change the main policy holder by choosing the checkbox against the joint policy holder",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_190(String browser) throws Exception {

    String tcId = "TC_190";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String jointPolicyfirstName = testData.get("Last Name");
    String jointPolicylastName = testData.get("First Name");
    String jointPolicyName = jointPolicyfirstName + " " + jointPolicylastName;

    testData2.put("First Name", jointPolicyfirstName);
    testData2.put("Last Name", jointPolicylastName);

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

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);

      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

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

      // Adding joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.addJointPolicyHolder(testData2, extentedReport);

      Log.softAssertThat(!newquotepage.verifyMaincheckbox(jointPolicyName, true, extentedReport),
          "Main Checkbox is not clicked for the policy holder - " + jointPolicyName,
          "Main Checkbox is clicked for the policy holder - " + jointPolicyName, driver,
          extentedReport, true);

      newquotepage.clickMainCheckbox(jointPolicyName, true, extentedReport);
      newquotepage.switchMainPolicyholder(true, true, extentedReport);

      Log.softAssertThat(newquotepage.verifyMaincheckbox(jointPolicyName, true, extentedReport),
          "Main Checkbox is clicked for the policy holder (" + jointPolicyName
              + "), The user is allowed to change the main policy holder",
          "Main Checkbox is not clicked for the policy holder (" + jointPolicyName
              + "), The user is not allowed to change the main policy holder",
          driver, extentedReport, true);
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
      description = "To check that the previous selected checkbox is unchecked automatically, when user choose the other contact as a main policy holder by click on any of the checkbox against the contact",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_191(String browser) throws Exception {

    String tcId = "TC_191";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String mainPolicyName = firstName + " " + lastName;

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String jointPolicyfirstName = testData.get("Last Name");
    String jointPolicylastName = testData.get("First Name");
    String jointPolicyName = jointPolicyfirstName + " " + jointPolicylastName;

    testData2.put("First Name", jointPolicyfirstName);
    testData2.put("Last Name", jointPolicylastName);

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

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);
      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);

      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

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

      // Adding joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.addJointPolicyHolder(testData2, extentedReport);

      Log.softAssertThat(newquotepage.verifyMaincheckbox(mainPolicyName, true, extentedReport),
          mainPolicyName + " - Contact name is the main contact name",
          mainPolicyName + " - Contact name is not the main contact name", driver, extentedReport,
          true);

      newquotepage.clickMainCheckbox(jointPolicyName, true, extentedReport);
      newquotepage.switchMainPolicyholder(true, true, extentedReport);

      Log.softAssertThat(newquotepage.verifyMaincheckbox(jointPolicyName, true, extentedReport),
          "The user is allowed to change the main policy holder",
          "The user is allowed to change the main policy holder", driver, extentedReport, true);

      Log.softAssertThat(!newquotepage.verifyMaincheckbox(mainPolicyName, true, extentedReport),
          "previous selected checkbox is unchecked automatically",
          "previous selected checkbox is not unchecked automatically", driver, extentedReport,
          true);
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
      description = "To check that the existing personal contacts are displayed in the search result, when Contact Type = Personal",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_192(String browser) throws Exception {

    String tcId = "TC_192";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String dob = testData.get("Date of Birth");
    String postCode = testData.get("Post Code");
    String contactType = testData.get("Contact_Type");
    String nameToVerify = firstName + " " + lastName;

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

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

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);

      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

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

      // Adding joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);

      newquotepage.selectJointPolicyHolderContactType(contactType, extentedReport);
      newquotepage.enterJointPolicy_LastName(lastName, extentedReport);
      newquotepage.enterJointPolicy_FirstName(firstName, extentedReport);
      newquotepage.enterJointPolicy_DateOfBirth(dob, extentedReport);
      newquotepage.enterJointPolicy_PostCode(postCode, extentedReport);
      newquotepage.clickPolicyHolderSearchButton(extentedReport);

      Log.softAssertThat(
          newquotepage.verifyJointPolicySearchResult(nameToVerify, postCode, dob, extentedReport),
          "Existing personal contacts is displayed in the search result",
          "Existing personal contacts is not displayed in the search result", driver,
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
      description = "To check that the existing corporate contacts are displayed in the search result, when Contact Type = Corporate",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_193(String browser) throws Exception {

    String tcId = "TC_193";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String postCode = testData.get("PostCode");
    String contactType = testData.get("Contact_Type");
    String dob = testData.get("Date of Birth");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String corporatefirstName = testData.get("First Name") + "Corp";
    String corporatelastName = testData.get("First Name") + "Corp";

    testData2.put("First Name", corporatefirstName);
    testData2.put("Last Name", corporatelastName);

    String nameToVerify = firstName + " " + lastName;


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

      // Enter corporate Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.createCorporateContact(testData2, extentedReport, true);
      searchPage.confirmCreateCustomer_CC(false, extentedReport);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);

      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

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

      // Adding joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);

      newquotepage.selectJointPolicyHolderContactType(contactType, extentedReport);
      newquotepage.enterJointPolicy_LastName(lastName, extentedReport);
      newquotepage.enterJointPolicy_FirstName(firstName, extentedReport);
      // newquotepage.enterJointPolicy_DateOfBirth(dob, extentedReport);
      newquotepage.enterJointPolicy_PostCode(postCode, extentedReport);
      newquotepage.clickPolicyHolderSearchButton(extentedReport);

      Log.softAssertThat(
          newquotepage.verifyJointPolicySearchResult(nameToVerify, postCode, dob, extentedReport),
          "Existing corporate contacts is displayed in the search result",
          "Existing corporate contacts is not displayed in the search result", driver,
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
      description = "To check that the search filter options are available for searching the contacts",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_194(String browser) throws Exception {

    String tcId = "TC_194";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");
    String contactType = testData.get("Contact_Type");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

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

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);

      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

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

      // Adding joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);

      newquotepage.selectJointPolicyHolderContactType(contactType, extentedReport);

      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("txtJointPolicyHolderLastName"),
              newquotepage),
          "Last Name search filter is displayed in search page",
          "Last Name search filter is not displayed in search page", driver, extentedReport, true);

      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("txtJointPolicyHolderFirstName"),
              newquotepage),
          "First Name search filter is displayed in search page",
          "First Name search filter is not displayed in search page", driver, extentedReport, true);

      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("calJointPolicyHolderDob"),
              newquotepage),
          "DOB search filter is displayed in search page",
          "DOB search filter is not displayed in search page", driver, extentedReport, true);

      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("txtJointPolicyHolderPostCode"),
              newquotepage),
          "Postcode search filter is displayed in search page",
          "Postcode search filter is not displayed in search page", driver, extentedReport, true);
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
      description = "To check that the all search filter fields are mandatory for searching the contacts",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_195(String browser) throws Exception {

    String tcId = "TC_195";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");
    String contactType = testData.get("Contact_Type");
    String errorMessage = testData.get("ErrorMessage");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

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

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);

      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

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

      // Adding joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);

      // Search Joint Policy
      newquotepage.selectJointPolicyHolderContactType(contactType, extentedReport);
      newquotepage.clickPolicyHolderSearchButton(extentedReport);

      Log.softAssertThat(newquotepage.verifyErrorMessage(errorMessage),
          errorMessage + " - Error is displayed to notify the mandatory field",
          errorMessage + " - Error is not displayed for last name to notify the mandatory field",
          driver, extentedReport, true);

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
      description = "To check that the prompted error message is removed when user filled the value in the search filter fields",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_196(String browser) throws Exception {

    String tcId = "TC_196";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");
    String contactType = testData.get("Contact_Type");
    String errorMessage = testData.get("ErrorMessage");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String nameToVerify = firstName + " " + lastName;
    String dob = testData.get("Date of Birth");
    String postCode = testData.get("Post Code");

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

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);

      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

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

      // Adding joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);

      // Search Joint Policy
      newquotepage.selectJointPolicyHolderContactType(contactType, extentedReport);
      newquotepage.clickPolicyHolderSearchButton(extentedReport);

      Log.softAssertThat(newquotepage.verifyErrorMessage(errorMessage),
          errorMessage + " - Error is displayed for mandatory fields ",
          errorMessage + " - Error is not displayed for for mandatory fields", driver,
          extentedReport, true);

      newquotepage.searchJointPolicyHolder(testData, extentedReport);

      Log.softAssertThat(
          newquotepage.verifyJointPolicySearchResult(nameToVerify, postCode, dob, extentedReport),
          nameToVerify + " - contact is displayed for contact search",
          nameToVerify + " - contact is not displayed for contact search", driver, extentedReport,
          true);


      Log.softAssertThat(!newquotepage.verifyErrorMessage(errorMessage),
          errorMessage + " - Error is removed after entering mandatory fields",
          errorMessage + " - Error is not removed after entering mandatory fields", driver,
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
      description = "To check that the Create New Joint Policyholder button is ONLY available after the CCA has searched for the first time",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_197(String browser) throws Exception {

    String tcId = "TC_197";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

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

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);

      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

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

      // Adding joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);

      Log.softAssertThat(
          !newquotepage.uielement.verifyPageElements(Arrays.asList("btnCreateNewJointPolicyholder"),
              newquotepage),
          "Create New Joint Customer button is not displayed",
          "Create New Joint Customer button is displayed", driver, extentedReport, true);

      newquotepage.searchJointPolicyHolder(testData, extentedReport);

      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("btnCreateNewJointPolicyholder"),
              newquotepage),
          "Create New Joint Customer button is displayed after the CCA has searched for the first time",
          "Create New Joint Customer button is not displayed after the CCA has searched for the first time",
          driver, extentedReport, true);
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
      description = "To check that the Create New Joint Policyholder modal/window is displayed, when click on Create New Joint Policyholder button",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_198(String browser) throws Exception {

    String tcId = "TC_198";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

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

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);

      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

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

      // Adding joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);

      Log.softAssertThat(
          !newquotepage.uielement.verifyPageElements(Arrays.asList("btnCreateNewJointPolicyholder"),
              newquotepage),
          "Create New JointPolicyholder button is not displayed",
          "Create New JJointPolicyholder button is displayed", driver, extentedReport, true);

      newquotepage.searchJointPolicyHolder(testData, extentedReport);

      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("btnCreateNewJointPolicyholder"),
              newquotepage),
          "Create New Jointpolicyholder button is displayed after contact search",
          "Create New Jointpolicyholder button is not displayed after contact search", driver,
          extentedReport, true);

      newquotepage.clickCreateNewJointPolicyHolder(extentedReport, true);

      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("wndCreateNewJointPolicyHolder"),
              newquotepage),
          "Create New Jointpolicyholder window is displayed after clicking CreateNewJointPolicyHolder button",
          "Create New Jointpolicyholder window is not displayed after clicking CreateNewJointPolicyHolder button",
          driver, extentedReport, true);

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
      description = "To check that the Policyholder’s address is pre-populated in the Create New Joint Policyholder modal/window",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_199(String browser) throws Exception {

    String tcId = "TC_199";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");
    String postCode = testData.get("Post Code");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

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

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);

      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

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

      // Adding joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);

      Log.softAssertThat(
          !newquotepage.uielement.verifyPageElements(Arrays.asList("btnCreateNewJointPolicyholder"),
              newquotepage),
          "Create New JointPolicyholder button is not displayed",
          "Create New JJointPolicyholder button is displayed", driver, extentedReport, true);

      newquotepage.searchJointPolicyHolder(testData, extentedReport);

      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("btnCreateNewJointPolicyholder"),
              newquotepage),
          "Create New Jointpolicyholder button is displayed after contact search",
          "Create New Jointpolicyholder button is not displayed after contact search", driver,
          extentedReport, true);

      newquotepage.clickCreateNewJointPolicyHolder(extentedReport, true);

      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("wndCreateNewJointPolicyHolder"),
              newquotepage),
          "Create New Jointpolicyholder window is displayed after clicking CreateNewJointPolicyHolder button",
          "Create New Jointpolicyholder window is not displayed after clicking CreateNewJointPolicyHolder button",
          driver, extentedReport, true);

      Log.softAssertThat(newquotepage.verifyPostCodeTxtBoxValue(postCode),
          postCode
              + " - Policyholder’s address is pre-populated in the Create New Joint Policyholder modal/window",
          postCode
              + " - Policyholder’s address is not pre-populated in the Create New Joint Policyholder modal/window",
          driver, extentedReport, true);
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
      description = "To check that the user is able to create new contact for adding as joint policyholder using the Create New Joint Policyholder button",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_200(String browser) throws Exception {

    String tcId = "TC_200";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String jointPolicyfirstName = testData.get("Last Name");
    String jointPolicylastName = testData.get("First Name");
    String jointPolicyName = jointPolicyfirstName + " " + jointPolicylastName;
    String dob = testData.get("Date of Birth");
    String postCode = testData.get("Post Code");

    testData2.put("First Name", jointPolicyfirstName);
    testData2.put("Last Name", jointPolicylastName);

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

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

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

      // Adding joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);

      Log.softAssertThat(
          !newquotepage.uielement.verifyPageElements(Arrays.asList("btnCreateNewJointPolicyholder"),
              newquotepage),
          "Create New JointPolicyholder button is not displayed",
          "Create New JJointPolicyholder button is displayed", driver, extentedReport, true);

      newquotepage.searchJointPolicyHolder(testData, extentedReport);

      Log.softAssertThat(
          newquotepage.uielement.verifyPageElements(Arrays.asList("btnCreateNewJointPolicyholder"),
              newquotepage),
          "Create New Jointpolicyholder button is displayed after contact search",
          "Create New Jointpolicyholder button is not displayed after contact search", driver,
          extentedReport, true);

      // Creating new Joint policyholder
      newquotepage.clickCreateNewJointPolicyHolder(extentedReport, true);
      newquotepage.enterJointPolicyHolderDetails(testData2, true, extentedReport);
      newquotepage.confirmCreateCustomer(true, extentedReport);

      // to search newly created joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.searchJointPolicyHolder(testData2, extentedReport);


      Log.softAssertThat(
          newquotepage.verifyJointPolicySearchResult(jointPolicyName, postCode, dob,
              extentedReport),
          "User is able to create new contact for adding as joint policyholder",
          "User is not able to create new contact for adding as joint policyholder", driver,
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
      description = "To check that the user is able to select/add the newly created contact as Joint policy holder",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_202(String browser) throws Exception {

    String tcId = "TC_202";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String jointPolicyfirstName = testData.get("Last Name");
    String jointPolicylastName = testData.get("First Name");
    String jointPolicyName = jointPolicyfirstName + " " + jointPolicylastName;

    testData2.put("First Name", jointPolicyfirstName);
    testData2.put("Last Name", jointPolicylastName);

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

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

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

      // Search joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.searchJointPolicyHolder(testData, extentedReport);

      // Creating new Joint policy holder
      newquotepage.clickCreateNewJointPolicyHolder(extentedReport, true);
      newquotepage.enterJointPolicyHolderDetails(testData2, true, extentedReport);
      newquotepage.confirmCreateCustomer(true, extentedReport);

      // to search newly created joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.searchJointPolicyHolder(testData2, extentedReport);

      // newquotepage.addJointPolicyHolder(testData2, extentedReport);

      Log.softAssertThat(
          newquotepage.verifyJointPolicySelectOption(jointPolicyName, true, extentedReport),
          "User is able to select/add the newly created contact as Joint policy holder",
          "User is not able to select/add the newly created contact as Joint policy holder", driver,
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
      description = "To check that the selected policy holder is displayed in the Policy Contacts section available at the customer dashboard",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_203(String browser) throws Exception {

    String tcId = "TC_203";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String jointPolicyfirstName = testData.get("Last Name");
    String jointPolicylastName = testData.get("First Name");
    String jointPolicyName = jointPolicyfirstName + " " + jointPolicylastName;

    testData2.put("First Name", jointPolicyfirstName);
    testData2.put("Last Name", jointPolicylastName);

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

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

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

      // Adding joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.addJointPolicyHolder(testData2, extentedReport);

      Log.softAssertThat(
          newquotepage.verifyPolicyHolderContactName(jointPolicyName, true, extentedReport),
          jointPolicyName + " - Contact Name is added in policy holder grid",
          jointPolicyName + " - Contact Name is notadded in policy holder grid", driver,
          extentedReport, true);

      newquotepage.clickbacktoDashboard(extentedReport);

      Log.softAssertThat(custdashboardpage.verifyPolicyholderName(jointPolicyName),
          jointPolicyName
              + " - Added Contact Name is displayed in the Policy Contacts section available at the customer dashboard",
          jointPolicyName
              + " - Added Contact Name is not displayed in the Policy Contacts section available at the customer dashboard",
          driver, extentedReport, true);
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
      description = "To check that the policy holders are separated by a comma, if more than one policy holders are selected/added",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_204(String browser) throws Exception {

    String tcId = "TC_204";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String jointPolicyfirstName = testData.get("Last Name");
    String jointPolicylastName = testData.get("First Name");

    testData2.put("First Name", jointPolicyfirstName);
    testData2.put("Last Name", jointPolicylastName);

    String jointPolicyName = jointPolicyfirstName + " " + jointPolicylastName;

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

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

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

      // Adding joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.addJointPolicyHolder(testData2, extentedReport);

      Log.softAssertThat(
          newquotepage.verifyPolicyHolderContactName(jointPolicyName, true, extentedReport),
          jointPolicyName
              + " - Contact Name is added as additional policy holder below main contact",
          jointPolicyName + " - Contact Name is not  added as additional policy holder", driver,
          extentedReport, true);

      newquotepage.clickbacktoDashboard(extentedReport);

      Log.softAssertThat(custdashboardpage.verifyPolicyholderSeparatedByComma(),
          "Policyholder Names is displayed with comma separated",
          "Policyholder is not displayed with comma separated", driver, extentedReport, true);

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
      description = "To check that the Main Policyholder is ALWAYS be the first name displayed in the Policy Contacts section available at the customer dashboard",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_205(String browser) throws Exception {

    String tcId = "TC_205";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String title = testData.get("Title");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String jointPolicyfirstName = testData.get("Last Name");
    String jointPolicylastName = testData.get("First Name");

    testData2.put("First Name", jointPolicyfirstName);
    testData2.put("Last Name", jointPolicylastName);

    String jointPolicyName = jointPolicyfirstName + " " + jointPolicylastName;
    String mainPolicyName = title + " " + firstName + " " + lastName;

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

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

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

      // Adding joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.addJointPolicyHolder(testData2, extentedReport);

      Log.softAssertThat(
          newquotepage.verifyPolicyHolderContactName(jointPolicyName, true, extentedReport),
          jointPolicyName
              + " - Contact Name is added as additional policy holder below main contact",
          jointPolicyName + " - Contact Name is not  added as additional policy holder", driver,
          extentedReport, true);

      newquotepage.clickbacktoDashboard(extentedReport);

      Log.softAssertThat(custdashboardpage.verifyMainPolicyholderDisplayedFirst(mainPolicyName),
          mainPolicyName
              + " - Main Policyholder is always the first name displayed in the Policy Contacts section available at the customer dashboard",
          mainPolicyName
              + " - Main Policyholder is not the first name displayed in the Policy Contacts section available at the customer dashboard",
          driver, extentedReport, true);
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
      description = "To check that the Main Policyholder is ALWAYS be shown as bold in the Policy Contacts section available at the customer dashboard",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_206(String browser) throws Exception {

    String tcId = "TC_206";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);
    HashMap<String, String> testData2 = new HashMap<String, String>(testData);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

    String jointPolicyfirstName = testData.get("Last Name");
    String jointPolicylastName = testData.get("First Name");

    testData2.put("First Name", jointPolicyfirstName);
    testData2.put("Last Name", jointPolicylastName);

    String jointPolicyName = jointPolicyfirstName + " " + jointPolicylastName;

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
      searchPage.enterCustomerDetails(testData2, true, extentedReport);

      // Confirm customer details and create customer
      CustDashboardPage custdashboardpage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custdashboardpage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custdashboardpage),
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);

      // Enter Main Customer Details
      searchPage.clickCreateCustomer(true, extentedReport);
      searchPage.enterCustomerDetails(testData, true, extentedReport);

      // Confirm customer details and create customer
      searchPage.confirmCreateCustomer(true, extentedReport);
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

      // Adding joint policy holder
      newquotepage.clickAddPolicyHolderButton(extentedReport);
      newquotepage.addJointPolicyHolder(testData2, extentedReport);

      Log.softAssertThat(
          newquotepage.verifyPolicyHolderContactName(jointPolicyName, true, extentedReport),
          jointPolicyName
              + " - Contact Name is added as additional policy holder below main contact",
          jointPolicyName + " - Contact Name is not  added as additional policy holder", driver,
          extentedReport, true);

      newquotepage.clickbacktoDashboard(extentedReport);

      searchPage.clickComplete(extentedReport);

      homePage.clickTakeCall(extentedReport);
      homePage.clickMyBrands(brandname, extentedReport);


      // Search with Valid user names
      searchPage.enterLastName(lastName, extentedReport);
      searchPage.enterFirstName(firstName, extentedReport);
      searchPage.clickSearch(extentedReport);

      // Selecting the contact in search page `
      searchPage.selectContactFromSearchResult(lastName, true, extentedReport);
      Log.softAssertThat(custdashboardpage.verifyCustomerDashboardPage(),
          "Customer Dashboard Page is loaded properly",
          "Customer Dashboard Page is not loaded properly", driver, extentedReport, true);

      custdashboardpage.clickPassVerification(extentedReport, false);

      Log.softAssertThat(custdashboardpage.verifyMainPolicyholderBoldFont(),
          "Main Policyholder is ALWAYS be shown as bold in the Policy Contacts section available at the customer dashboard",
          "Main Policyholder is not be shown as bold in the Policy Contacts section available at the customer dashboard",
          driver, extentedReport, false);
      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }


  @Test(description = "Check to view the policy holder detail using View button",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_619(String browser) throws Exception {

    String tcId = "TC_619";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String firstName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("First Name");
    String lastName = GenericUtils.getRandomCharacters("alpha", 5) + testData.get("Last Name");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");

    String PolicyName = firstName + " " + lastName;

    testData.put("First Name", firstName);
    testData.put("Last Name", lastName);

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
          "custdashboardpage Verified", "custdashboardpage not Verified", driver, extentedReport,
          true);

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

      newquotepage.clickViewButton(PolicyName, true, extentedReport);

      Log.softAssertThat(newquotepage.verifyJointPolicyDetail(testData, true, extentedReport),
          "Joint Policyholder detail is displayed in createjointPolicyholder window after clicking view button",
          "Joint Policyholder detail is not displayed in createjointPolicyholder window after clicking view button",
          driver, extentedReport, true);

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

}
