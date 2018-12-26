package com.ssp.regression.insurer.testscripts;

import java.io.IOException;
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
import com.ssp.uxp_pages.HomePage;
import com.ssp.uxp_pages.LoginPage;

public class UXP_EC_1753 extends BaseTest {
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
    String className = "UXP_EC_1753_" + env;
    return DataUtils.testDatabyID(testcaseId, className);
  }

  @Test(description = "Verify User able to login with valid UserName and Password ",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_440(String browser) throws Exception {

    String tcId = "TC_440";
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
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username : " + userName + " & Password : "
              + password,
          "Login failed into Home Page with Username :  " + userName + " & Password : " + password,
          driver, extentedReport, true);

      // To check whether My Dashboard is displayed
      Log.softAssertThat(homePage.verifyMyDashboardTitle(),
          "My dashboard title is displayed in homepage",
          "My dashboard title is not displayed in homepage", driver, extentedReport, true);

      // To check whether Makecall is displayed
      Log.softAssertThat(homePage.verifyMakeCall(),
          "Agent Dashboard is displayed with Makecall button",
          "Agent Dashboard is not displayed with Makecall button", driver, extentedReport, false);

      // To check whether Takecall
      Log.softAssertThat(homePage.verifyTakeCall(),
          "Agent Dashboard is displayed with Takecall button",
          "Agent Dashboard is not displayed with Takecall button", driver, extentedReport, false);

      // To check whether AdminTask
      Log.softAssertThat(homePage.verifyAdminTask(),
          "Agent Dashboard is displayed with AdminTask button",
          "Agent Dashboard is not displayed with AdminTask button", driver, extentedReport, false);

      Log.testCaseResult(extentedReport);


    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  @Test(description = "Verify User not able to login with Invalid UserName and Password ",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_441(String browser) throws Exception {

    String tcId = "TC_441";
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
      try {
        loginPage.loginToSSP(userName, password, false, extentedReport);
      } catch (Exception e) {

        Log.softAssertThat(e.getMessage().contains("Invalid Login Details"),
            "User could not login with invalid Username : " + userName + " & valid Password : "
                + password,
            "User could login with invalid Username : " + userName + " & valid Password : "
                + password,
            driver, extentedReport, true);

      }

      Log.softAssertThat(loginPage.verifyErroMessage(),
          loginPage.ERROR_MSG_LOGIN + " - Error message is displayed ",
          loginPage.ERROR_MSG_LOGIN + "  Error message is not displayed", driver, extentedReport,
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

  @Test(description = "Verify User not able to login with valid UserName and Invalid Password ",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_442(String browser) throws Exception {

    String tcId = "TC_442";
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
      try {
        loginPage.loginToSSP(userName, password, false, extentedReport);
      } catch (Exception e) {
        Log.softAssertThat(e.getMessage().contains("Invalid Login Details"),
            "User could not login with valid Username : " + userName + " & invalid Password : "
                + password,
            "User could login with valid Username : " + userName + " & invalid Password : "
                + password,
            driver, extentedReport, true);
      }

      // To check the error messaage
      Log.softAssertThat(loginPage.verifyErroMessage(),
          loginPage.ERROR_MSG_LOGIN + " - Error message is displayed ",
          loginPage.ERROR_MSG_LOGIN + "  Error message is not displayed", driver, extentedReport,
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

  @Test(description = "Verify User not able to login with Invalid UserName and valid Password",
      dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_443(String browser) throws Exception {


    String tcId = "TC_443";
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
      try {
        loginPage.loginToSSP(userName, password, false, extentedReport);
      } catch (Exception e) {
        Log.softAssertThat(e.getMessage().contains("Invalid Login Details"),
            "User could not login with invalid Username : " + userName + " & invalid Password : "
                + password,
            "User could login with invalid Username : " + userName + " & invalid Password : "
                + password,
            driver, extentedReport, true);
      }

      // To check the error messaage
      Log.softAssertThat(loginPage.verifyErroMessage(),
          loginPage.ERROR_MSG_LOGIN + " - Error message is displayed ",
          loginPage.ERROR_MSG_LOGIN + "  Error message is not displayed", driver, extentedReport,
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


  // @Test(description = "To check whether the user account is locked, when 3 unsuccessful attempts
  // is made by the user", dataProviderClass = DataProviderUtils.class, dataProvider =
  // "ssBVTDataProvider")
  public void TC_444(String browser) throws Exception {


    String tcId = "TC_444";
    final WebDriver driver = WebDriverFactory.get(browser);
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    String errorMessage = testData.get("ErrorMessage");
    String secondErrorMessage = testData.get("SecondErrorMessage");
    // String errorMessage = "We do not recognise your details. Please re-enter your credentials";

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page
      LoginPage loginPage = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite, driver, extentedReport, true);

      // Login to the application-1st attempt
      for (int loopCount = 0; loopCount < 3; loopCount++) {
        try {
          loginPage.loginToSSP(userName, password, false, extentedReport);
        } catch (Exception e) {
          Log.softAssertThat(e.getMessage().contains("Invalid Login Details"),
              "User could not login with invalid Username : " + userName + " & invalid Password : "
                  + password,
              "User could login with invalid Username : " + userName + " & invalid Password : "
                  + password,
              driver, extentedReport, true);
        }

        if (loopCount < 2) {
          // To check the error messaage
          Log.softAssertThat(loginPage.verifyErroMessage(),
              errorMessage + " - Error message is displayed for " + (loopCount + 1)
                  + " Attempt to login ",
              errorMessage + " - Error message is not displayed " + (loopCount + 1)
                  + " Attempt to login",
              driver, extentedReport, true);
        } else {
          // To check the error messaage
          Log.softAssertThat(loginPage.verifyErroMessage(),
              secondErrorMessage + " - Error message is displayed for " + (loopCount + 1)
                  + " Attempt to login ",
              secondErrorMessage + " - Error message is not displayed " + (loopCount + 1)
                  + " Attempt to login",
              driver, extentedReport, true);
        }

        /*
         * //To check the error messaage
         * Log.softAssertThat(loginPage.verifyErroMessage(errorMessage), errorMessage +
         * " - Error message is displayed for " + loopCount + 1 + "Attempt to login ", errorMessage
         * + " - Error message is not displayed " + loopCount + " Attempt to login", driver,
         * extentedReport, true);
         */
      }

      Log.testCaseResult(extentedReport);

    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  // @Test(description = "To check that the user is not allowed to login to application once the
  // user account is locked", dataProviderClass = DataProviderUtils.class, dataProvider =
  // "ssBVTDataProvider"/*, dependsOnMethods = "TC_444"*/)
  public void TC_445(String browser) throws Exception {


    String tcId = "TC_445";
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
      try {
        HomePage homePage = loginPage.loginToSSP(userName, password, false, extentedReport);

        Log.softAssertThat(!homePage.verifyHomePage(),
            "Login failed into Home Page with Username :  " + userName + " & Password :" + password
                + " after account is locked",
            "Successfully logged into Home Page with Username :  " + userName + " & Password :"
                + password + " after account is locked",
            driver, extentedReport, true);

      } catch (Exception e) {
        Log.softAssertThat(e.getMessage().contains("Invalid Login Details"),
            "User could not login with valid Username : " + userName + " & valid Password : "
                + password + " after account is locked",
            "User could login with valid Username : " + userName + " & valid Password : " + password
                + " after account is locked",
            driver, extentedReport, true);
      }

      Log.testCaseResult(extentedReport);



    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
    } // finally
  }

  // @Test(description = "To check that the following message is prompted, If a user is already
  // logged into a WebUI (first session) and then tries to log into second time (second session)",
  // dataProviderClass = DataProviderUtils.class, dataProvider = "ssBVTDataProvider")
  public void TC_456(String browser) throws Exception {


    String tcId = "TC_456";
    final WebDriver driver = WebDriverFactory.get(browser);
    WebDriver driver2 = null;
    HashMap<String, String> testData = getTestData(tcId);

    String userName = testData.get("UserName");
    String password = testData.get("Password");
    String description = testData.get("Description");
    // String errorMessage = testData.get("ErrorMessage");

    Log.testCaseInfo(description + "<small><b><i>[" + browser + "]</b></i></small>");

    ExtentTest extentedReport = addTestInfo(tcId, description);

    try {

      // Navigate to Login Page - First session
      LoginPage loginPage1 = new LoginPage(driver, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite + " in the first browser ", driver, extentedReport,
          true);

      // Login to the application
      HomePage homePage = loginPage1.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(homePage.verifyHomePage(),
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password + " in the first browser",
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password
              + " in the first browser",
          driver, extentedReport, true);

      driver2 = WebDriverFactory.get(browser);
      // Navigate to Login Page - Second session
      LoginPage loginPage2 = new LoginPage(driver2, webSite, extentedReport).get();
      Log.pass("SIAAS Landing Page : " + webSite + " in the second browser", driver2,
          extentedReport, true);

      // Login to the application
      HomePage homePage2 = loginPage2.loginToSSP(userName, password, false, extentedReport);
      Log.softAssertThat(!homePage2.verifyHomePage(),
          "Login failed into Home Page with Username :  " + userName + " & Password :" + password
              + " in the First browser",
          "Successfully logged into Home Page with Username :  " + userName + " & Password :"
              + password + " in the Second browser",
          driver2, extentedReport, true);

      Log.testCaseResult(extentedReport);


    } catch (Exception e) {
      Log.exception(e, driver, extentedReport);
    } // catch
    finally {
      Log.endTestCase(extentedReport);
      driver.quit();
      if (driver2 != null)
        driver2.quit();
    } // finally
  }


}
