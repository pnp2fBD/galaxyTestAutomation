/**
 * 
 */
package com.ssp.smoke.testscripts;

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
import com.ssp.uxp_SSPages.CustomerSelfService;
import com.ssp.uxp_SSPages.CustomerSigninPage;
import com.ssp.uxp_SSPages.SelfServiceCustomerDashboard;
import com.ssp.uxp_SSPages.SummaryPage;
import com.ssp.uxp_SSPages.YourQuoteSelfService;

/**
 * @author jheelum.dutta
 *
 */

@Listeners(EmailReport.class)
public class SelfServiceLogin extends BaseTest {


  private String webSite;

  private HashMap<String, String> customerDetails2 = new HashMap<String, String>();

  @BeforeMethod
  public void init(ITestContext context) throws IOException {
    webSite = System.getProperty("webSite") != null ? System.getProperty("webSite")
        : context.getCurrentXmlTest().getParameter("webSite");

    System.out.println("Website Name-: " + context.getCurrentXmlTest().getParameter("webSite"));


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
    System.out.println("Env is------------ " + env);
    // SelfServiceLogin
    String className = "SelfServiceLogin_" + env;
    System.out.println("className is------------ " + className);

    return DataUtils.testDatabyID(testcaseId, className);
  }



  @Test(description = "Login to SelfService", dataProviderClass = DataProviderUtils.class,
      dataProvider = "ssBVTDataProvider")
  public void TC_SS_001(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_SS_001";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userEmail = testData.get("UserName");
    System.out.println(userEmail);
    String password = testData.get("Password");
    String description = testData.get("Description");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Open CustomerSelfservice page and click on Signin button

      CustomerSelfService cusSelfservice = new CustomerSelfService(driver, webSite, extentedReport);
      cusSelfservice.get();
      Log.pass("Customer Self Service page is displayed : " + webSite, driver, extentedReport,
          true);
      cusSelfservice.clickBtnSignIn(extentedReport, driver);

      Log.pass("Sign in button displayed and  " + webSite, driver, extentedReport, true);

      CustomerSigninPage loginPage = new CustomerSigninPage(driver);
      SelfServiceCustomerDashboard custDashBoardPage =
          loginPage.loginToSSPCustomerSelfService(userEmail, password, false, extentedReport);

      Log.pass("CustomerDashboard Pageis displayed  : " + webSite, driver, extentedReport, true);

      Log.softAssertThat(true, "Successfully logged into SelfService", "Login failed", driver,
          extentedReport, true);
      Log.message("Logged in User id:" + userEmail + "& Password:" + password, driver,
          extentedReport);
      Log.endTestCase(extentedReport);
      driver.quit();

    }



    catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }

  }

  // sgmlxtujbarber@1545.com
  @Test(description = "Login to SelfService and continue with Quote ",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_SS_002(String browser) throws Exception {

    // Get the web driver instance
    String tcId = "TC_SS_002";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userEmail = testData.get("UserName");
    System.out.println(userEmail);
    String password = testData.get("Password");
    String description = testData.get("Description");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Open CustomerSelfservice page and click on Signin button

      CustomerSelfService cusSelfservice = new CustomerSelfService(driver, webSite, extentedReport);
      cusSelfservice.get();
      Log.pass("Customer Self Service page is displayed : " + webSite, driver, extentedReport,
          true);
      cusSelfservice.clickBtnSignIn(extentedReport, driver);

      Log.pass("Sign in button displayed and  " + webSite, driver, extentedReport, true);

      CustomerSigninPage loginPage = new CustomerSigninPage(driver);
      SelfServiceCustomerDashboard CustDashboard =
          loginPage.loginToSSPCustomerSelfService(userEmail, password, false, extentedReport);

      Log.pass("CustomerSigninPage is displayed  : " + webSite, driver, extentedReport, true);

      Log.softAssertThat(true, "Successfully logged into SelfService", "Login failed", driver,
          extentedReport, true);
      Log.message("Logged in User id:" + userEmail + "& Password:" + password, driver,
          extentedReport);

      CustDashboard.clickonContinueQuotebtn(extentedReport);
      YourQuoteSelfService yourquote = new YourQuoteSelfService(driver, extentedReport);

      yourquote.clickOnSummarybtn(extentedReport);
      SummaryPage summarypg = new SummaryPage(driver, extentedReport);
      summarypg.tickCustomerAgreesCheckbox(extentedReport);
      summarypg.clickOnPaymentbtn(extentedReport);

      System.out.println("click in");

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    }

  }
}

