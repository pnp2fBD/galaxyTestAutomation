package com.ssp.uxp.WJ_Test;

import java.io.IOException;
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
import com.ssp.uxp_WJpages.PaymentCompletePage;
import com.ssp.uxp_WJpages.PaymentConfirmationPage;
import com.ssp.uxp_WJpages.PaymentOptionsPage;
import com.ssp.uxp_WJpages.SummaryPageDetails;
import com.ssp.uxp_WJpages.YourDetailsPage;
import com.ssp.uxp_WJpages.YourQuotePage;

@Listeners(EmailReport.class)
public class WJTest extends BaseTest {


  public String webSite;
  public String monthlywebSite;

  @BeforeMethod
  public void init(ITestContext context) throws IOException {
    webSite = System.getProperty("webSite") != null ? System.getProperty("webSite")
        : context.getCurrentXmlTest().getParameter("webSite");

    System.out.println("Website Name-: " + context.getCurrentXmlTest().getParameter("webSite"));

    monthlywebSite =
        System.getProperty("monthlywebSite") != null ? System.getProperty("monthlywebSite")
            : context.getCurrentXmlTest().getParameter("monthlywebSite");
  }

  public ExtentTest addTestInfo(String testCaseId, String testDesc) {

    String browserwithos = null;
    String test = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getName();
    System.out.println("test is - " + test);
    String browsername = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
        .getParameter("browser");
    System.out.println("Browser- " + browsername);
    String browserversion = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
        .getParameter("browser_version");
    System.out.println("browserversion- " + browserversion);
    String os = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
        .getParameter("os").substring(0, 1);
    System.out.println("OS Name - " + os);
    String osversion = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
        .getParameter("os_version");
    System.out.println("osversion - " + osversion);

    browserwithos = os + osversion + "_" + browsername + browserversion;
    System.out.println("browserwithos - " + browserwithos);
    return Log.testCaseInfo(testCaseId + " [" + test + "]",
        testCaseId + " - " + testDesc + " [" + browserwithos + "]", test);

  }

  public HashMap<String, String> getTestData(String testcaseId) {
    String env =
        Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("env");
    String className = "WJ" + env;

    return DataUtils.testDatabyID(testcaseId, className);
  }

  @Test(description = "Basic Policy - Annual", dataProviderClass = DataProviderUtils.class,
      dataProvider = "ssBVTDataProvider")
  public void TC_001(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_001";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      YourDetailsPage wjYourDetailsPage =
          new YourDetailsPage(driver, webSite, extentedReport).get();
      YourQuotePage wjYourQuotePage;
      SummaryPageDetails wjSummaryPage;
      PaymentOptionsPage wjPaymentOptionsPage;
      PaymentConfirmationPage wjPaymentConfirmationPage;
      PaymentCompletePage wjPaymentCompletePage;
      wjYourQuotePage = wjYourDetailsPage.runFlowOfYourDetailsPage(testData);
      wjSummaryPage = wjYourQuotePage.runFlowOfYourQuotePage(testData);
      wjPaymentOptionsPage = wjSummaryPage.runFlowOfSummaryPage();
      wjPaymentConfirmationPage = wjPaymentOptionsPage.runFlowOfPaymentOptionsPage(testData);
      if (testData.get("Payment Method").equalsIgnoreCase("Annual"))
        wjPaymentCompletePage =
            wjPaymentConfirmationPage.runFlowOfAnnualPaymentConfirmationPage(testData);
      else
        wjPaymentCompletePage = wjPaymentConfirmationPage.runFlowOfMonthlyPaymentConfirmationPage();
      wjPaymentCompletePage.runFlowOfPaymentCompletePage();
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(description = "Basic WJ Policy - 3 star - Monthly",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_002(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_002";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      YourDetailsPage wjYourDetailsPage =
          new YourDetailsPage(driver, webSite, extentedReport).get();
      YourQuotePage wjYourQuotePage;
      SummaryPageDetails wjSummaryPage;
      PaymentOptionsPage wjPaymentOptionsPage;
      PaymentConfirmationPage wjPaymentConfirmationPage;
      PaymentCompletePage wjPaymentCompletePage;
      wjYourQuotePage = wjYourDetailsPage.runFlowOfYourDetailsPage(testData);
      wjSummaryPage = wjYourQuotePage.runFlowOfYourQuotePage(testData);
      wjPaymentOptionsPage = wjSummaryPage.runFlowOfSummaryPage();
      wjPaymentConfirmationPage = wjPaymentOptionsPage.runFlowOfPaymentOptionsPage(testData);
      if (testData.get("Payment Method").equalsIgnoreCase("Annual"))
        wjPaymentCompletePage =
            wjPaymentConfirmationPage.runFlowOfAnnualPaymentConfirmationPage(testData);
      else
        wjPaymentCompletePage = wjPaymentConfirmationPage.runFlowOfMonthlyPaymentConfirmationPage();
      wjPaymentCompletePage.runFlowOfPaymentCompletePage();
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(description = "Joint Holder Policy - Annual", dataProviderClass = DataProviderUtils.class,
      dataProvider = "ssBVTDataProvider")
  public void TC_003(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_003";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      YourDetailsPage wjYourDetailsPage =
          new YourDetailsPage(driver, webSite, extentedReport).get();
      YourQuotePage wjYourQuotePage;
      SummaryPageDetails wjSummaryPage;
      PaymentOptionsPage wjPaymentOptionsPage;
      PaymentConfirmationPage wjPaymentConfirmationPage;
      PaymentCompletePage wjPaymentCompletePage;
      wjYourQuotePage = wjYourDetailsPage.runFlowOfYourDetailsPage(testData);
      wjSummaryPage = wjYourQuotePage.runFlowOfYourQuotePage(testData);
      wjPaymentOptionsPage = wjSummaryPage.runFlowOfSummaryPage();
      wjPaymentConfirmationPage = wjPaymentOptionsPage.runFlowOfPaymentOptionsPage(testData);
      if (testData.get("Payment Method").equalsIgnoreCase("Annual"))
        wjPaymentCompletePage =
            wjPaymentConfirmationPage.runFlowOfAnnualPaymentConfirmationPage(testData);
      else
        wjPaymentCompletePage = wjPaymentConfirmationPage.runFlowOfMonthlyPaymentConfirmationPage();
      wjPaymentCompletePage.runFlowOfPaymentCompletePage();
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }

  @Test(description = "Joint Holder Policy - Monthly", dataProviderClass = DataProviderUtils.class,
      dataProvider = "ssBVTDataProvider")
  public void TC_004(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_004";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String description = testData.get("Description");
    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {
      YourDetailsPage wjYourDetailsPage =
          new YourDetailsPage(driver, webSite, extentedReport).get();
      YourQuotePage wjYourQuotePage;
      SummaryPageDetails wjSummaryPage;
      PaymentOptionsPage wjPaymentOptionsPage;
      PaymentConfirmationPage wjPaymentConfirmationPage;
      PaymentCompletePage wjPaymentCompletePage;
      wjYourQuotePage = wjYourDetailsPage.runFlowOfYourDetailsPage(testData);
      wjSummaryPage = wjYourQuotePage.runFlowOfYourQuotePage(testData);
      wjPaymentOptionsPage = wjSummaryPage.runFlowOfSummaryPage();
      wjPaymentConfirmationPage = wjPaymentOptionsPage.runFlowOfPaymentOptionsPage(testData);
      if (testData.get("Payment Method").equalsIgnoreCase("Annual"))
        wjPaymentCompletePage =
            wjPaymentConfirmationPage.runFlowOfAnnualPaymentConfirmationPage(testData);
      else
        wjPaymentCompletePage = wjPaymentConfirmationPage.runFlowOfMonthlyPaymentConfirmationPage();
      wjPaymentCompletePage.runFlowOfPaymentCompletePage();
    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }
  }
}
