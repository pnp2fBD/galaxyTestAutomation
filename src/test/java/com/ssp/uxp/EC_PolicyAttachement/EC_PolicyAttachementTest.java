package com.ssp.uxp.EC_PolicyAttachement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.commons.io.FilenameUtils;
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
import com.ssp.utils.GenericUtils;
import com.ssp.uxp_pages.CardDetailsPage;
import com.ssp.uxp_pages.CustDashboardPage;
import com.ssp.uxp_pages.CustomerDashboardDocuments;
import com.ssp.uxp_pages.HomePage;
import com.ssp.uxp_pages.LoginPage;
import com.ssp.uxp_pages.NewQuotePage;
import com.ssp.uxp_pages.SearchPage;

@Listeners(EmailReport.class)
public class EC_PolicyAttachementTest extends BaseTest {

  private String webSite;
  public String monthlywebSite;

  @BeforeMethod
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
    String className = "UXP_EC_FT_Attachement_" + env;
    return DataUtils.testDatabyID(testcaseId, className);
  }

  @Test(
      description = "Choose a file with these file types for e.g. jpg, xls, bmp, ppt, xls, docx. It should allow these file types to be chosen for upload after mandatory fields are filled up.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_001(String browser) throws Exception {
    // Get the web driver instance
    String tcId = "TC_001";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");
    String uploadDocuments = testData.get("Upload Documents");
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
      CustDashboardPage custDashboardPage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custDashboardPage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

      // Create New Quote
      custDashboardPage.clickNewQuote(true, extentedReport);
      custDashboardPage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custDashboardPage.clickContinueQuote(true, extentedReport);

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
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, false);
      carddetailspage.selectExpiry(testData, extentedReport, false);
      carddetailspage.enterVerification(extentedReport, false);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      custDashboardPage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custDashboardPage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custDashboardPage),
          "Navigated to Customer Dashboard page", "Not navigated to Customer Dashboard page",
          driver, extentedReport, true);

      // Check for the customerdashboard page tabs
      custDashboardPage.verify_ExpandCollapse_Custdashboard(extentedReport, true);

      // check for policy status
      HashMap<String, String> policyDetails = custDashboardPage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted"))
        Log.message("New Business Policy Created Successfully in Accepted status", driver,
            extentedReport, true);
      else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      CustomerDashboardDocuments custDashboardDocuments =
          custDashboardPage.clickOnDocumentsTab(true, extentedReport);
      int rowDataBefore = custDashboardDocuments.getRowCountofDocuments();
      int expectedRowDataAfter = rowDataBefore;
      ArrayList<String> listOfDocuments =
          GenericUtils.getDocumentsListDelimiter(uploadDocuments, ";");
      if (listOfDocuments.size() < 1)
        throw new Exception("No document file names are in the input data");
      for (int i = 0; i < listOfDocuments.size(); i++) {
        String document = listOfDocuments.get(i);
        document = GenericUtils.getAttachementsDirectory().concat(document);
        File file = new File(document);
        if (!file.exists())
          throw new Exception(
              "File does not exist in your system " + document + " . Please check your input");
        String fileExtension = FilenameUtils.getExtension(document);
        if (!GenericUtils.validateFileExtension(fileExtension,
            CustomerDashboardDocuments.fileExtensionTypes))
          throw new Exception(
              "This file is not supported by EC. Files supported only are xls,xlsx,ppt,pptx,bmp,jpg,jpeg,png,pdf,docx,html,txt,msg,pdf,csv,tif");
        String fileDescription = file.getName().replaceAll("." + fileExtension, "");
        custDashboardDocuments.clickUploadDocumentButton(true, extentedReport);
        custDashboardDocuments.enterDescriptionUploadDocument(fileDescription, true,
            extentedReport);
        custDashboardDocuments.chooseFileUploadDocument(file.getAbsolutePath(), true,
            extentedReport);
        custDashboardDocuments.clickSaveButtonUploadDocument(true, extentedReport);
        expectedRowDataAfter = expectedRowDataAfter + 1;
      }
      int rowDataAfter = custDashboardDocuments.getRowCountofDocuments();
      Log.assertThat(expectedRowDataAfter == rowDataAfter, "Data is added successfully",
          "Data is not added successfully in documents table");
      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(
      description = "Add a file attachment (photo) to an existing quote/policy. Ensure it can be uploaded successfully. View the quote/policy to ensure that the content of the file can be viewed.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_002(String browser) throws Exception {
    // Get the web driver instance
    String tcId = "TC_002";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String uploadDocuments = testData.get("Upload Documents");
    String Corecover = testData.get("Cover");
    String title = testData.get("Title");
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
      CustDashboardPage custDashboardPage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custDashboardPage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

      // Create New Quote
      custDashboardPage.clickNewQuote(true, extentedReport);
      custDashboardPage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custDashboardPage.clickContinueQuote(true, extentedReport);

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
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, false);
      carddetailspage.selectExpiry(testData, extentedReport, false);
      carddetailspage.enterVerification(extentedReport, false);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      custDashboardPage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custDashboardPage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custDashboardPage),
          "Navigated to Customer Dashboard page", "Not navigated to Customer Dashboard page",
          driver, extentedReport, true);

      // Check for the customerdashboard page tabs
      custDashboardPage.verify_ExpandCollapse_Custdashboard(extentedReport, true);

      // check for policy status
      HashMap<String, String> policyDetails = custDashboardPage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted"))
        Log.message("New Business Policy Created Successfully in Accepted status", driver,
            extentedReport, true);
      else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      custDashboardPage.clickComplete(extentedReport);
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
      Log.message("Navigated to Cutomer Dashboard after selecting policy from search page", driver,
          extentedReport, true);
      // Verifying Customer Details
      custdashboardPage.clickPassVerification(extentedReport, true);
      custdashboardPage.verifyCustomerDashboardPage();
      Log.softAssertThat(
          custdashboardPage.verifyContactName(
              title + " " + custDetails.get("First Name") + " " + custDetails.get("Last Name"),
              extentedReport, true),
          "Verified FirstName and LastName on Customer Dashboard : " + custDetails.get("First Name")
              + " " + custDetails.get("Last Name"),
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);
      CustomerDashboardDocuments custDashboardDocuments =
          custDashboardPage.clickOnDocumentsTab(true, extentedReport);
      int rowDataBefore = custDashboardDocuments.getRowCountofDocuments();
      int expectedRowDataAfter = rowDataBefore;
      ArrayList<String> listOfImages = GenericUtils.getDocumentsListDelimiter(uploadDocuments, ";");
      if (listOfImages.size() < 1)
        throw new Exception("No image file names are in the input data");
      for (int i = 0; i < listOfImages.size(); i++) {
        String image = listOfImages.get(i);
        image = GenericUtils.getAttachementsDirectory().concat(image);
        File file = new File(image);
        if (!file.exists())
          throw new Exception(
              "File does not exist in your system " + image + " . Please check your input");
        String fileExtension = FilenameUtils.getExtension(image);
        if (!GenericUtils.validateFileExtension(fileExtension,
            CustomerDashboardDocuments.imageExtensionTypes))
          throw new Exception(
              "This file is not supported by EC. Files supported only are bmp,jpg,jpeg,png,tif");
        String fileDescription = file.getName().replaceAll("." + fileExtension, "");
        custDashboardDocuments.clickUploadDocumentButton(true, extentedReport);
        custDashboardDocuments.enterDescriptionUploadDocument(fileDescription, true,
            extentedReport);
        custDashboardDocuments.chooseFileUploadDocument(file.getAbsolutePath(), true,
            extentedReport);
        custDashboardDocuments.clickSaveButtonUploadDocument(true, extentedReport);
        expectedRowDataAfter = expectedRowDataAfter + 1;
      }
      int rowDataAfter = custDashboardDocuments.getRowCountofDocuments();
      Log.assertThat(expectedRowDataAfter == rowDataAfter, "Data is added successfully",
          "Data is not added successfully in documents table");
      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(
      description = "Attempt to upload a document greater than the configurable file size. It should flag an error message ‘File is greater than configurable file size’ and should not allow the document to be uploaded.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_003(String browser) throws Exception {
    // Get the web driver instance
    String tcId = "TC_003";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String uploadDocuments = testData.get("Upload Documents");
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
      CustDashboardPage custDashboardPage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custDashboardPage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

      // Create New Quote
      custDashboardPage.clickNewQuote(true, extentedReport);
      custDashboardPage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custDashboardPage.clickContinueQuote(true, extentedReport);

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
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, false);
      carddetailspage.selectExpiry(testData, extentedReport, false);
      carddetailspage.enterVerification(extentedReport, false);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      custDashboardPage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custDashboardPage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custDashboardPage),
          "Navigated to Customer Dashboard page", "Not navigated to Customer Dashboard page",
          driver, extentedReport, true);

      // Check for the customerdashboard page tabs
      custDashboardPage.verify_ExpandCollapse_Custdashboard(extentedReport, true);

      // check for policy status
      HashMap<String, String> policyDetails = custDashboardPage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted"))
        Log.message("New Business Policy Created Successfully in Accepted status", driver,
            extentedReport, true);
      else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      CustomerDashboardDocuments custDashboardDocuments =
          custDashboardPage.clickOnDocumentsTab(true, extentedReport);
      ArrayList<String> listOfDocuments =
          GenericUtils.getDocumentsListDelimiter(uploadDocuments, ";");
      if (listOfDocuments.size() < 1)
        throw new Exception("No document file names are in the input data");
      for (int i = 0; i < listOfDocuments.size(); i++) {
        String document = listOfDocuments.get(i);
        document = GenericUtils.getAttachementsDirectory().concat(document);
        File file = new File(document);
        if (!file.exists())
          throw new Exception(
              "File does not exist in your system " + document + " . Please check your input");
        String fileExtension = FilenameUtils.getExtension(document);
        if (!GenericUtils.validateFileExtension(fileExtension,
            CustomerDashboardDocuments.fileExtensionTypes))
          throw new Exception(
              "This file is not supported by EC. Files supported only are xls,xlsx,ppt,pptx,bmp,jpg,jpeg,png,pdf,docx,html,txt,msg,pdf,csv,tif");
        String fileDescription = file.getName().replaceAll("." + fileExtension, "");
        custDashboardDocuments.clickUploadDocumentButton(true, extentedReport);
        custDashboardDocuments.enterDescriptionUploadDocument(fileDescription, true,
            extentedReport);
        custDashboardDocuments.chooseFileUploadDocument(file.getAbsolutePath(), true,
            extentedReport);
        if (custDashboardDocuments.validateSaveButtonEnabled())
          custDashboardDocuments.clickSaveButtonUploadDocument(true, extentedReport);
        else
          break;
      }
      Log.assertThat(custDashboardDocuments.validateErrorMessageOnUploadDocument(),
          "Error message found on the page as expected",
          "Error message does not found on the page. This is not expected.");
      Log.assertThat(
          custDashboardDocuments.getErrorMessageText()
              .equalsIgnoreCase("The maximum file size allowed is 4000k"),
          "Error message matches with The maximum file size allowed is 4000k",
          "This message is not expected. On website it is "
              + custDashboardDocuments.getErrorMessageText());
      Log.assertThat(!custDashboardDocuments.validateSaveButtonEnabled(),
          "Save button is disabled as expected", "Save button is enabled which is not expected");
      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(
      description = "Do not input any mandatory fields when uploading document. Check that the mandatory fields are highlighted and ‘Save’ button disabled.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_004(String browser) throws Exception {
    // Get the web driver instance
    String tcId = "TC_004";
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
      CustDashboardPage custDashboardPage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custDashboardPage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

      // Create New Quote
      custDashboardPage.clickNewQuote(true, extentedReport);
      custDashboardPage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custDashboardPage.clickContinueQuote(true, extentedReport);

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
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, false);
      carddetailspage.selectExpiry(testData, extentedReport, false);
      carddetailspage.enterVerification(extentedReport, false);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      custDashboardPage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custDashboardPage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custDashboardPage),
          "Navigated to Customer Dashboard page", "Not navigated to Customer Dashboard page",
          driver, extentedReport, true);

      // Check for the customerdashboard page tabs
      custDashboardPage.verify_ExpandCollapse_Custdashboard(extentedReport, true);

      // check for policy status
      HashMap<String, String> policyDetails = custDashboardPage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted"))
        Log.message("New Business Policy Created Successfully in Accepted status", driver,
            extentedReport, true);
      else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      CustomerDashboardDocuments custDashboardDocuments =
          custDashboardPage.clickOnDocumentsTab(true, extentedReport);
      custDashboardDocuments.clickUploadDocumentButton(true, extentedReport);
      Log.assertThat(custDashboardDocuments.validateUploadADocumentPopUp(true, extentedReport),
          "Upload a document pop up is as expected",
          "Upload a document pop up fields are not expected");
      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(
      description = "Attempt to upload a document which is renamed from .exe to .pdf. File should be uploaded as no file check capability is included.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_005(String browser) throws Exception {
    // Get the web driver instance
    String tcId = "TC_005";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String uploadDocuments = testData.get("Upload Documents");
    String coreCover = testData.get("Cover");
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
      CustDashboardPage custDashboardPage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custDashboardPage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

      // Create New Quote
      custDashboardPage.clickNewQuote(true, extentedReport);
      custDashboardPage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custDashboardPage.clickContinueQuote(true, extentedReport);

      // Create New Quote
      Log.softAssertThat(newquotepage.verifyNewQuotePage(true, extentedReport),
          "Verified NewQuotePage ", "Not verified NewQuotePage", driver, extentedReport, true);
      newquotepage.enterPolicyDetails(testData, true, extentedReport);
      newquotepage.clickNextOne(extentedReport);
      newquotepage.enterCustomerDetails(testData, true, coreCover, extentedReport);
      newquotepage.clickNextTwo(extentedReport);
      newquotepage.clickView(extentedReport);
      newquotepage.clickAgree(extentedReport);
      // Get and Buy Quote
      newquotepage.clickGetQuote(extentedReport);
      newquotepage.clickBuy(extentedReport);
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
      custDashboardPage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custDashboardPage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custDashboardPage),
          "Navigated to Customer Dashboard page", "Not navigated to Customer Dashboard page",
          driver, extentedReport, true);

      // Check for the customerdashboard page tabs
      custDashboardPage.verify_ExpandCollapse_Custdashboard(extentedReport, true);

      // check for policy status
      HashMap<String, String> policyDetails = custDashboardPage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted"))
        Log.message("New Business Policy Created Successfully in Accepted status", driver,
            extentedReport, true);
      else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      CustomerDashboardDocuments custDashboardDocuments =
          custDashboardPage.clickOnDocumentsTab(true, extentedReport);
      int rowDataBefore = custDashboardDocuments.getRowCountofDocuments();
      int expectedRowDataAfter = rowDataBefore;
      custDashboardDocuments.clickUploadDocumentButton(true, extentedReport);
      ArrayList<String> listOfDocuments =
          GenericUtils.getDocumentsListDelimiter(uploadDocuments, ";");
      if (listOfDocuments.size() < 1)
        throw new Exception("No document file names are in the input data");
      for (int i = 0; i < listOfDocuments.size(); i++) {
        String document = listOfDocuments.get(i);
        document = GenericUtils.getAttachementsDirectory().concat(document);
        String newFileDocument = document;
        File file = new File(document);
        if (!file.exists())
          throw new Exception(
              "File does not exist in your system " + document + " . Please check your input");
        String fileExtension = FilenameUtils.getExtension(document);
        if (!fileExtension.equalsIgnoreCase("exe"))
          throw new Exception("This test case required file with .exe extension only");
        String newDocument = document.replaceAll("." + fileExtension, ".pdf");
        File file1 = new File(newDocument);
        file.renameTo(file1);
        if (GenericUtils.createFile(newFileDocument))
          Log.message("File is created successfully");
        else
          Log.message("File is not created");
        String fileDescription = file.getName().replaceAll("." + fileExtension, "");
        custDashboardDocuments.enterDescriptionUploadDocument(fileDescription, true,
            extentedReport);
        custDashboardDocuments.chooseFileUploadDocument(file1.getAbsolutePath(), true,
            extentedReport);
        custDashboardDocuments.clickSaveButtonUploadDocument(true, extentedReport);
        expectedRowDataAfter = expectedRowDataAfter + 1;
        if (GenericUtils.deleteFile(newDocument))
          Log.message("File is deleted successfully");
        else
          Log.message("File is not deleted");
      }
      int rowDataAfter = custDashboardDocuments.getRowCountofDocuments();
      Log.assertThat(expectedRowDataAfter == rowDataAfter, "Data is added successfully",
          "Data is not added successfully in documents table");
      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(
      description = "Upload a docx file to an existing quote which already contains file attachment(s). This is to ensure that both existing and new files appear in the list when selecting ‘Document’ tab.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_006(String browser) throws Exception {
    // Get the web driver instance
    String tcId = "TC_006";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String Corecover = testData.get("Cover");
    String uploadDocuments = testData.get("Upload Documents");
    String title = testData.get("Title");
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
      CustDashboardPage custDashboardPage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custDashboardPage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

      // Create New Quote
      custDashboardPage.clickNewQuote(true, extentedReport);
      custDashboardPage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custDashboardPage.clickContinueQuote(true, extentedReport);

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
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, false);
      carddetailspage.selectExpiry(testData, extentedReport, false);
      carddetailspage.enterVerification(extentedReport, false);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      custDashboardPage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custDashboardPage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custDashboardPage),
          "Navigated to Customer Dashboard page", "Not navigated to Customer Dashboard page",
          driver, extentedReport, true);

      // Check for the customerdashboard page tabs
      custDashboardPage.verify_ExpandCollapse_Custdashboard(extentedReport, true);

      // check for policy status
      HashMap<String, String> policyDetails = custDashboardPage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted"))
        Log.message("New Business Policy Created Successfully in Accepted status", driver,
            extentedReport, true);
      else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      CustomerDashboardDocuments custDashboardDocuments =
          custDashboardPage.clickOnDocumentsTab(true, extentedReport);
      int rowDataBefore = custDashboardDocuments.getRowCountofDocuments();
      ArrayList<String> listOfDocuments =
          GenericUtils.getDocumentsListDelimiter(uploadDocuments, ";");
      if (listOfDocuments.size() < 2)
        throw new Exception("Minimum 2 document file names are in the input data");
      String document = listOfDocuments.get(0);
      document = GenericUtils.getAttachementsDirectory().concat(document);
      File file = new File(document);
      if (!file.exists())
        throw new Exception(
            "File does not exist in your system " + document + " . Please check your input");
      String fileExtension = FilenameUtils.getExtension(document);
      if (!GenericUtils.validateFileExtension(fileExtension,
          CustomerDashboardDocuments.fileExtensionTypes))
        throw new Exception(
            "This file is not supported by EC. Files supported only are xls,xlsx,ppt,pptx,bmp,jpg,jpeg,png,pdf,docx,html,txt,msg,pdf,csv,tif");
      String fileDescription = file.getName().replaceAll("." + fileExtension, "");
      custDashboardDocuments.clickUploadDocumentButton(true, extentedReport);
      custDashboardDocuments.enterDescriptionUploadDocument(fileDescription, true, extentedReport);
      custDashboardDocuments.chooseFileUploadDocument(file.getAbsolutePath(), true, extentedReport);
      custDashboardDocuments.clickSaveButtonUploadDocument(true, extentedReport);
      int rowDataAfter = custDashboardDocuments.getRowCountofDocuments();
      Log.softAssertThat(rowDataBefore < rowDataAfter, "Data is added successfully",
          "Data is not added successfully in documents table");
      custDashboardPage.clickComplete(extentedReport);
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
      Log.message("Navigated to Cutomer Dashboard after selecting policy from search page", driver,
          extentedReport, true);
      // Verifying Customer Details
      custdashboardPage.clickPassVerification(extentedReport, true);
      custdashboardPage.verifyCustomerDashboardPage();
      Log.softAssertThat(
          custdashboardPage.verifyContactName(
              title + " " + custDetails.get("First Name") + " " + custDetails.get("Last Name"),
              extentedReport, true),
          "Verified FirstName and LastName on Customer Dashboard : " + custDetails.get("First Name")
              + " " + custDetails.get("Last Name"),
          "Not Verified Customer Name on Dashboard", driver, extentedReport, true);
      custDashboardDocuments = custDashboardPage.clickOnDocumentsTab(true, extentedReport);
      rowDataBefore = custDashboardDocuments.getRowCountofDocuments();
      Log.assertThat(rowDataAfter == rowDataBefore, "First row Data is matching as expected",
          "First row Data is not matching as expected");
      int expectedRowDataAfter = rowDataBefore;
      listOfDocuments = GenericUtils.getDocumentsListDelimiter(uploadDocuments, ";");
      if (listOfDocuments.size() < 2)
        throw new Exception("Minimum 2 document file names are in the input data");
      for (int i = 1; i < listOfDocuments.size(); i++) {
        document = listOfDocuments.get(i);
        document = GenericUtils.getAttachementsDirectory().concat(document);
        file = new File(document);
        if (!file.exists())
          throw new Exception(
              "File does not exist in your system " + document + " . Please check your input");
        fileExtension = FilenameUtils.getExtension(document);
        if (!GenericUtils.validateFileExtension(fileExtension,
            CustomerDashboardDocuments.fileExtensionTypes))
          throw new Exception(
              "This file is not supported by EC. Files supported only are xls,xlsx,ppt,pptx,bmp,jpg,jpeg,png,pdf,docx,html,txt,msg,pdf,csv,tif");
        fileDescription = file.getName().replaceAll("." + fileExtension, "");
        custDashboardDocuments.clickUploadDocumentButton(true, extentedReport);
        custDashboardDocuments.enterDescriptionUploadDocument(fileDescription, true,
            extentedReport);
        custDashboardDocuments.chooseFileUploadDocument(file.getAbsolutePath(), true,
            extentedReport);
        custDashboardDocuments.clickSaveButtonUploadDocument(true, extentedReport);
        expectedRowDataAfter = expectedRowDataAfter + 1;
      }
      rowDataAfter = custDashboardDocuments.getRowCountofDocuments();
      Log.assertThat(expectedRowDataAfter == rowDataAfter, "Data is added successfully",
          "Data is not added successfully in documents table");
      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(
      description = "Attempt to upload a document greater than the configurable file size. It should flag an error message ‘File is greater than configurable file size’ and should not allow the document to be uploaded.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_007(String browser) throws Exception {
    // Get the web driver instance
    String tcId = "TC_007";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String uploadDocuments = testData.get("Upload Documents");
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
      CustDashboardPage custDashboardPage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custDashboardPage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

      // Create New Quote
      custDashboardPage.clickNewQuote(true, extentedReport);
      custDashboardPage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custDashboardPage.clickContinueQuote(true, extentedReport);

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
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, false);
      carddetailspage.selectExpiry(testData, extentedReport, false);
      carddetailspage.enterVerification(extentedReport, false);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      custDashboardPage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custDashboardPage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custDashboardPage),
          "Navigated to Customer Dashboard page", "Not navigated to Customer Dashboard page",
          driver, extentedReport, true);

      // Check for the customerdashboard page tabs
      custDashboardPage.verify_ExpandCollapse_Custdashboard(extentedReport, true);

      // check for policy status
      HashMap<String, String> policyDetails = custDashboardPage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted"))
        Log.message("New Business Policy Created Successfully in Accepted status", driver,
            extentedReport, true);
      else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      CustomerDashboardDocuments custDashboardDocuments =
          custDashboardPage.clickOnDocumentsTab(true, extentedReport);
      ArrayList<String> listOfDocuments =
          GenericUtils.getDocumentsListDelimiter(uploadDocuments, ";");
      if (listOfDocuments.size() < 1)
        throw new Exception("No document file names are in the input data");
      for (int i = 0; i < listOfDocuments.size(); i++) {
        String document = listOfDocuments.get(i);
        document = GenericUtils.getAttachementsDirectory().concat(document);
        File file = new File(document);
        if (!file.exists())
          throw new Exception(
              "File does not exist in your system " + document + " . Please check your input");
        String fileExtension = FilenameUtils.getExtension(document);
        String fileDescription = file.getName().replaceAll("." + fileExtension, "");
        custDashboardDocuments.clickUploadDocumentButton(true, extentedReport);
        custDashboardDocuments.enterDescriptionUploadDocument(fileDescription, true,
            extentedReport);
        custDashboardDocuments.chooseFileUploadDocument(file.getAbsolutePath(), true,
            extentedReport);
        if (custDashboardDocuments.validateSaveButtonEnabled())
          custDashboardDocuments.clickSaveButtonUploadDocument(true, extentedReport);
        else
          break;
      }
      Log.assertThat(custDashboardDocuments.validateErrorMessageOnUploadDocument(),
          "Error message found on the page as expected",
          "Error message does not found on the page. This is not expected.");
      Log.assertThat(
          custDashboardDocuments.getErrorMessageText()
              .equalsIgnoreCase("It is not possible to upload this file type."),
          "Error message matches with It is not possible to upload this file type.",
          "This message is not expected. On website it is "
              + custDashboardDocuments.getErrorMessageText());
      Log.assertThat(!custDashboardDocuments.validateSaveButtonEnabled(),
          "Save button is disabled as expected", "Save button is enabled which is not expected");
      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(description = "To validate delete button is deleting the document.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_008(String browser) throws Exception {
    // Get the web driver instance
    String tcId = "TC_008";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String uploadDocuments = testData.get("Upload Documents");
    String deleteDocuments = testData.get("Delete Documents");
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
      CustDashboardPage custDashboardPage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custDashboardPage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

      // Create New Quote
      custDashboardPage.clickNewQuote(true, extentedReport);
      custDashboardPage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custDashboardPage.clickContinueQuote(true, extentedReport);

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
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, false);
      carddetailspage.selectExpiry(testData, extentedReport, false);
      carddetailspage.enterVerification(extentedReport, false);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      custDashboardPage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custDashboardPage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custDashboardPage),
          "Navigated to Customer Dashboard page", "Not navigated to Customer Dashboard page",
          driver, extentedReport, true);

      // Check for the customerdashboard page tabs
      custDashboardPage.verify_ExpandCollapse_Custdashboard(extentedReport, true);

      // check for policy status
      HashMap<String, String> policyDetails = custDashboardPage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted"))
        Log.message("New Business Policy Created Successfully in Accepted status", driver,
            extentedReport, true);
      else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      CustomerDashboardDocuments custDashboardDocuments =
          custDashboardPage.clickOnDocumentsTab(true, extentedReport);
      int rowDataBefore = custDashboardDocuments.getRowCountofDocuments();
      int expectedRowDataAfter = rowDataBefore;
      ArrayList<String> listOfUploadDocuments =
          GenericUtils.getDocumentsListDelimiter(uploadDocuments, ";");
      ArrayList<String> listOfDeleteDocuments =
          GenericUtils.getDocumentsListDelimiter(deleteDocuments, ";");
      if (listOfUploadDocuments.size() < 1)
        throw new Exception("No upload document file names are in the input data");
      if (listOfDeleteDocuments.size() < 1)
        throw new Exception("No delete document file names are in the input data");
      if (!listOfUploadDocuments.containsAll(listOfDeleteDocuments))
        throw new Exception("Please delete only those documents which are in upload list");
      for (int i = 0; i < listOfUploadDocuments.size(); i++) {
        String document = listOfUploadDocuments.get(i);
        document = GenericUtils.getAttachementsDirectory().concat(document);
        File file = new File(document);
        if (!file.exists())
          throw new Exception(
              "File does not exist in your system " + document + " . Please check your input");
        String fileExtension = FilenameUtils.getExtension(document);
        if (!GenericUtils.validateFileExtension(fileExtension,
            CustomerDashboardDocuments.fileExtensionTypes))
          throw new Exception(
              "This file is not supported by EC. Files supported only are xls,xlsx,ppt,pptx,bmp,jpg,jpeg,png,pdf,docx,html,txt,msg,pdf,csv,tif");
        String fileDescription = file.getName().replaceAll("." + fileExtension, "");
        custDashboardDocuments.clickUploadDocumentButton(true, extentedReport);
        custDashboardDocuments.enterDescriptionUploadDocument(fileDescription, true,
            extentedReport);
        custDashboardDocuments.chooseFileUploadDocument(file.getAbsolutePath(), true,
            extentedReport);
        custDashboardDocuments.clickSaveButtonUploadDocument(true, extentedReport);
        expectedRowDataAfter = expectedRowDataAfter + 1;
      }
      int rowDataAfter = custDashboardDocuments.getRowCountofDocuments();
      Log.softAssertThat(expectedRowDataAfter == rowDataAfter, "Data is added successfully",
          "Data is not added successfully in documents table");
      if (expectedRowDataAfter != rowDataAfter)
        throw new Exception("Data is not added successfully in documents table");
      rowDataBefore = custDashboardDocuments.getRowCountofDocuments();
      expectedRowDataAfter = rowDataBefore;
      for (int i = 0; i < listOfDeleteDocuments.size(); i++) {
        String document = listOfDeleteDocuments.get(i);
        document = GenericUtils.getAttachementsDirectory().concat(document);
        String documentName = document.substring(document.lastIndexOf("\\") + 1);
        String fileExtension = FilenameUtils.getExtension(documentName);
        String deleteDescription = documentName.replaceAll("." + fileExtension, "");
        custDashboardDocuments.clickDeleteButton(i + 1, true, extentedReport);
        custDashboardDocuments.enterDescriptionDeleteDocument(deleteDescription, true,
            extentedReport);
        custDashboardDocuments.clickYesButtonDeleteDocument(true, extentedReport);
        expectedRowDataAfter = expectedRowDataAfter - 1;
      }
      rowDataAfter = custDashboardDocuments.getRowCountofDocuments();
      Log.assertThat(expectedRowDataAfter == rowDataAfter, "Data is removed successfully",
          "Data is not removed successfully in documents table");
      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(
      description = "To validate No button on attachement delete pop up does not delete the file from documents.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_009(String browser) throws Exception {
    // Get the web driver instance
    String tcId = "TC_009";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String uploadDocuments = testData.get("Upload Documents");
    String deleteDocuments = testData.get("Delete Documents");
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
      CustDashboardPage custDashboardPage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custDashboardPage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

      // Create New Quote
      custDashboardPage.clickNewQuote(true, extentedReport);
      custDashboardPage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custDashboardPage.clickContinueQuote(true, extentedReport);

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
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, false);
      carddetailspage.selectExpiry(testData, extentedReport, false);
      carddetailspage.enterVerification(extentedReport, false);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      custDashboardPage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custDashboardPage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custDashboardPage),
          "Navigated to Customer Dashboard page", "Not navigated to Customer Dashboard page",
          driver, extentedReport, true);

      // Check for the customerdashboard page tabs
      custDashboardPage.verify_ExpandCollapse_Custdashboard(extentedReport, true);

      // check for policy status
      HashMap<String, String> policyDetails = custDashboardPage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted"))
        Log.message("New Business Policy Created Successfully in Accepted status", driver,
            extentedReport, true);
      else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      CustomerDashboardDocuments custDashboardDocuments =
          custDashboardPage.clickOnDocumentsTab(true, extentedReport);
      int rowDataBefore = custDashboardDocuments.getRowCountofDocuments();
      int expectedRowDataAfter = rowDataBefore;
      ArrayList<String> listOfUploadDocuments =
          GenericUtils.getDocumentsListDelimiter(uploadDocuments, ";");
      ArrayList<String> listOfDeleteDocuments =
          GenericUtils.getDocumentsListDelimiter(deleteDocuments, ";");
      if (listOfUploadDocuments.size() < 1)
        throw new Exception("No upload document file names are in the input data");
      if (listOfDeleteDocuments.size() < 1)
        throw new Exception("No delete document file names are in the input data");
      if (!listOfUploadDocuments.containsAll(listOfDeleteDocuments))
        throw new Exception("Please delete only those documents which are in upload list");
      for (int i = 0; i < listOfUploadDocuments.size(); i++) {
        String document = listOfUploadDocuments.get(i);
        document = GenericUtils.getAttachementsDirectory().concat(document);
        File file = new File(document);
        if (!file.exists())
          throw new Exception(
              "File does not exist in your system " + document + " . Please check your input");
        String fileExtension = FilenameUtils.getExtension(document);
        if (!GenericUtils.validateFileExtension(fileExtension,
            CustomerDashboardDocuments.fileExtensionTypes))
          throw new Exception(
              "This file is not supported by EC. Files supported only are xls,xlsx,ppt,pptx,bmp,jpg,jpeg,png,pdf,docx,html,txt,msg,pdf,csv,tif");
        String fileDescription = file.getName().replaceAll("." + fileExtension, "");
        custDashboardDocuments.clickUploadDocumentButton(true, extentedReport);
        custDashboardDocuments.enterDescriptionUploadDocument(fileDescription, true,
            extentedReport);
        custDashboardDocuments.chooseFileUploadDocument(file.getAbsolutePath(), true,
            extentedReport);
        custDashboardDocuments.clickSaveButtonUploadDocument(true, extentedReport);
        expectedRowDataAfter = expectedRowDataAfter + 1;
      }
      int rowDataAfter = custDashboardDocuments.getRowCountofDocuments();
      Log.softAssertThat(expectedRowDataAfter == rowDataAfter, "Data is added successfully",
          "Data is not added successfully in documents table");
      if (expectedRowDataAfter != rowDataAfter)
        throw new Exception("Data is not added successfully in documents table");
      rowDataBefore = custDashboardDocuments.getRowCountofDocuments();
      for (int i = 0; i < listOfDeleteDocuments.size(); i++) {
        String document = listOfDeleteDocuments.get(i);
        document = GenericUtils.getAttachementsDirectory().concat(document);
        String documentName = document.substring(document.lastIndexOf("\\") + 1);
        String fileExtension = FilenameUtils.getExtension(documentName);
        String deleteDescription = documentName.replaceAll("." + fileExtension, "");
        custDashboardDocuments.clickDeleteButton(i + 1, true, extentedReport);
        custDashboardDocuments.enterDescriptionDeleteDocument(deleteDescription, true,
            extentedReport);
        custDashboardDocuments.clickNoButtonDeleteDocument(true, extentedReport);
      }
      rowDataAfter = custDashboardDocuments.getRowCountofDocuments();
      Log.assertThat(rowDataBefore == rowDataAfter, "Data is not removed successfully",
          "Data is removed successfully in documents table which is not expected");
      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(description = "Upload 2 files and delete one file. To validate only one file is deleted.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_010(String browser) throws Exception {
    // Get the web driver instance
    String tcId = "TC_010";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String uploadDocuments = testData.get("Upload Documents");
    String deleteDocuments = testData.get("Delete Documents");
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
      CustDashboardPage custDashboardPage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custDashboardPage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

      // Create New Quote
      custDashboardPage.clickNewQuote(true, extentedReport);
      custDashboardPage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custDashboardPage.clickContinueQuote(true, extentedReport);

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
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, false);
      carddetailspage.selectExpiry(testData, extentedReport, false);
      carddetailspage.enterVerification(extentedReport, false);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      custDashboardPage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custDashboardPage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custDashboardPage),
          "Navigated to Customer Dashboard page", "Not navigated to Customer Dashboard page",
          driver, extentedReport, true);

      // Check for the customerdashboard page tabs
      custDashboardPage.verify_ExpandCollapse_Custdashboard(extentedReport, true);

      // check for policy status
      HashMap<String, String> policyDetails = custDashboardPage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted"))
        Log.message("New Business Policy Created Successfully in Accepted status", driver,
            extentedReport, true);
      else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      CustomerDashboardDocuments custDashboardDocuments =
          custDashboardPage.clickOnDocumentsTab(true, extentedReport);
      int rowDataBefore = custDashboardDocuments.getRowCountofDocuments();
      int expectedRowDataAfter = rowDataBefore;
      ArrayList<String> listOfUploadDocuments =
          GenericUtils.getDocumentsListDelimiter(uploadDocuments, ";");
      ArrayList<String> listOfDeleteDocuments =
          GenericUtils.getDocumentsListDelimiter(deleteDocuments, ";");
      if (listOfUploadDocuments.size() < 1)
        throw new Exception("No upload document file names are in the input data");
      if (listOfDeleteDocuments.size() < 1)
        throw new Exception("No delete document file names are in the input data");
      if (!listOfUploadDocuments.containsAll(listOfDeleteDocuments))
        throw new Exception("Please delete only those documents which are in upload list");
      for (int i = 0; i < listOfUploadDocuments.size(); i++) {
        String document = listOfUploadDocuments.get(i);
        document = GenericUtils.getAttachementsDirectory().concat(document);
        File file = new File(document);
        if (!file.exists())
          throw new Exception(
              "File does not exist in your system " + document + " . Please check your input");
        String fileExtension = FilenameUtils.getExtension(document);
        if (!GenericUtils.validateFileExtension(fileExtension,
            CustomerDashboardDocuments.fileExtensionTypes))
          throw new Exception(
              "This file is not supported by EC. Files supported only are xls,xlsx,ppt,pptx,bmp,jpg,jpeg,png,pdf,docx,html,txt,msg,pdf,csv,tif");
        String fileDescription = file.getName().replaceAll("." + fileExtension, "");
        custDashboardDocuments.clickUploadDocumentButton(true, extentedReport);
        custDashboardDocuments.enterDescriptionUploadDocument(fileDescription, true,
            extentedReport);
        custDashboardDocuments.chooseFileUploadDocument(file.getAbsolutePath(), true,
            extentedReport);
        custDashboardDocuments.clickSaveButtonUploadDocument(true, extentedReport);
        expectedRowDataAfter = expectedRowDataAfter + 1;
      }
      int rowDataAfter = custDashboardDocuments.getRowCountofDocuments();
      Log.softAssertThat(expectedRowDataAfter == rowDataAfter, "Data is added successfully",
          "Data is not added successfully in documents table");
      if (expectedRowDataAfter != rowDataAfter)
        throw new Exception("Data is not added successfully in documents table");
      rowDataBefore = custDashboardDocuments.getRowCountofDocuments();
      expectedRowDataAfter = rowDataBefore;
      for (int i = 0; i < listOfDeleteDocuments.size(); i++) {
        String document = listOfDeleteDocuments.get(i);
        document = GenericUtils.getAttachementsDirectory().concat(document);
        String documentName = document.substring(document.lastIndexOf("\\") + 1);
        String fileExtension = FilenameUtils.getExtension(documentName);
        String deleteDescription = documentName.replaceAll("." + fileExtension, "");
        custDashboardDocuments.clickDeleteButton(i + 1, true, extentedReport);
        custDashboardDocuments.enterDescriptionDeleteDocument(deleteDescription, true,
            extentedReport);
        custDashboardDocuments.clickYesButtonDeleteDocument(true, extentedReport);
        expectedRowDataAfter = expectedRowDataAfter - 1;
      }
      rowDataAfter = custDashboardDocuments.getRowCountofDocuments();
      Log.assertThat(expectedRowDataAfter == rowDataAfter, "Data is removed successfully",
          "Data is not removed successfully in documents table");
      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(
      description = "Do not input any mandatory fields when deleting document. Check that the mandatory fields are highlighted and 'Yes' button disabled.",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_011(String browser) throws Exception {
    // Get the web driver instance
    String tcId = "TC_011";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String brandname = testData.get("Brand Name");
    String uploadDocuments = testData.get("Upload Documents");
    String deleteDocuments = testData.get("Delete Documents");
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
      CustDashboardPage custDashboardPage = searchPage.confirmCreateCustomer(true, extentedReport);
      Log.softAssertThat(
          custDashboardPage.verifyContactName(testData.get("Title") + " "
              + custDetails.get("First Name") + " " + custDetails.get("Last Name"), extentedReport,
              true),
          "Verified FirstName and LastName on Customer Dashboard page",
          "Not Verified Customer Name on Dashboard page", driver, extentedReport, true);

      // Create New Quote
      custDashboardPage.clickNewQuote(true, extentedReport);
      custDashboardPage.enterQuoteDetails(testData, true, extentedReport);
      NewQuotePage newquotepage = custDashboardPage.clickContinueQuote(true, extentedReport);

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
      CardDetailsPage carddetailspage = newquotepage.selectVisacard(extentedReport);

      // Enter Card Details
      carddetailspage.enterCardNumber(testData, extentedReport, false);
      carddetailspage.selectExpiry(testData, extentedReport, false);
      carddetailspage.enterVerification(extentedReport, false);
      carddetailspage.enterName(testData, extentedReport, true);
      carddetailspage.clickbuy(extentedReport, true);
      Log.message("Entered Card Details and Navigated to Verificaion page", driver, extentedReport,
          true);
      custDashboardPage = newquotepage.confirmPayment(testData, extentedReport);
      Log.softAssertThat(
          custDashboardPage.uielement.verifyPageElements(Arrays.asList("lnkEditDetails"),
              custDashboardPage),
          "Navigated to Customer Dashboard page", "Not navigated to Customer Dashboard page",
          driver, extentedReport, true);

      // Check for the customerdashboard page tabs
      custDashboardPage.verify_ExpandCollapse_Custdashboard(extentedReport, true);

      // check for policy status
      HashMap<String, String> policyDetails = custDashboardPage.getPolicyURN(extentedReport);
      if (policyDetails.get("Status").toString().equalsIgnoreCase("Accepted"))
        Log.message("New Business Policy Created Successfully in Accepted status", driver,
            extentedReport, true);
      else {
        Log.fail(
            "Failed - NB Policy Created in " + policyDetails.get("Status").toString() + " status",
            driver, extentedReport, true);
      }
      CustomerDashboardDocuments custDashboardDocuments =
          custDashboardPage.clickOnDocumentsTab(true, extentedReport);
      int rowDataBefore = custDashboardDocuments.getRowCountofDocuments();
      int expectedRowDataAfter = rowDataBefore;
      ArrayList<String> listOfUploadDocuments =
          GenericUtils.getDocumentsListDelimiter(uploadDocuments, ";");
      ArrayList<String> listOfDeleteDocuments =
          GenericUtils.getDocumentsListDelimiter(deleteDocuments, ";");
      if (listOfUploadDocuments.size() < 1)
        throw new Exception("No upload document file names are in the input data");
      if (listOfDeleteDocuments.size() < 1)
        throw new Exception("No delete document file names are in the input data");
      if (!listOfUploadDocuments.containsAll(listOfDeleteDocuments))
        throw new Exception("Please delete only those documents which are in upload list");
      for (int i = 0; i < listOfUploadDocuments.size(); i++) {
        String document = listOfUploadDocuments.get(i);
        document = GenericUtils.getAttachementsDirectory().concat(document);
        File file = new File(document);
        if (!file.exists())
          throw new Exception(
              "File does not exist in your system " + document + " . Please check your input");
        String fileExtension = FilenameUtils.getExtension(document);
        if (!GenericUtils.validateFileExtension(fileExtension,
            CustomerDashboardDocuments.fileExtensionTypes))
          throw new Exception(
              "This file is not supported by EC. Files supported only are xls,xlsx,ppt,pptx,bmp,jpg,jpeg,png,pdf,docx,html,txt,msg,pdf,csv,tif");
        String fileDescription = file.getName().replaceAll("." + fileExtension, "");
        custDashboardDocuments.clickUploadDocumentButton(true, extentedReport);
        custDashboardDocuments.enterDescriptionUploadDocument(fileDescription, true,
            extentedReport);
        custDashboardDocuments.chooseFileUploadDocument(file.getAbsolutePath(), true,
            extentedReport);
        custDashboardDocuments.clickSaveButtonUploadDocument(true, extentedReport);
        expectedRowDataAfter = expectedRowDataAfter + 1;
      }
      int rowDataAfter = custDashboardDocuments.getRowCountofDocuments();
      Log.assertThat(expectedRowDataAfter == rowDataAfter, "Data is added successfully",
          "Data is not added successfully in documents table");
      if (expectedRowDataAfter != rowDataAfter)
        throw new Exception("Data is not added successfully in documents table");
      custDashboardDocuments.clickDeleteButton(1, true, extentedReport);
      Log.assertThat(custDashboardDocuments.validateDeleteDocumentPopUp(true, extentedReport),
          "Delete a document pop up is as expected",
          "Delete a document pop up fields are not expected");
      Log.testCaseResult(extentedReport);
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }
}
