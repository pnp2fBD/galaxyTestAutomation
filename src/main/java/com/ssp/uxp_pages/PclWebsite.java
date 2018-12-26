package com.ssp.uxp_pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.testng.Assert;
import com.relevantcodes.extentreports.ExtentTest;
import com.ssp.support.Log;
import com.ssp.utils.WaitUtils;

public class PclWebsite extends LoadableComponent<LoginPage> {
  private final WebDriver driver;
  public String url;
  private ExtentTest extentedReport;
  private boolean isPageLoaded;

  /**********************************************************************************************
   ********************************* WebElements of Login Page **********************************
   **********************************************************************************************/

  @FindBy(css = ".heading1")
  WebElement Pclheading;

  @FindBy(css = "#content>span>ul>li>a")
  WebElement XMLs;

  //
  /**********************************************************************************************
   ********************************* WebElements of Login Page - Ends ***************************
   **********************************************************************************************/

  /**
   * 
   * Constructor class for pcl page Here we initializing the driver for page factory objects. For
   * ajax element waiting time has added while initialization
   * 
   * @param driver : Webdriver
   * @param url : Loading pcl Page URL
   * @param extentedReport : ExtentTest
   */
  public PclWebsite(WebDriver driver, String url, ExtentTest extentedReport) {

    this.driver = driver;
    this.url = url;
    this.extentedReport = extentedReport;
    PageFactory.initElements(driver, this);
  }


  @Override
  protected void isLoaded() {

    WaitUtils.waitForPageLoad(driver);

    if (!isPageLoaded) {
      Assert.fail();
    }

    try {
      if (isPageLoaded && !driver.getTitle().contains("XMLTransferEngine")) {
        Log.fail("Pcl page is not loaded. Site might be down", driver, extentedReport);
      }
    } catch (Exception e) {

      e.printStackTrace();
    }
  }

  @Override
  protected void load() {

    isPageLoaded = true;
    driver.get(url);
    WaitUtils.waitForPageLoad(driver);
    if (!isPageLoaded) {
      Assert.fail();
    }

    try {
      if (isPageLoaded && !driver.getTitle().contains("XMLTransferEngine")) {
        throw new Exception("Pcl page is not loaded. Site might be down");
      }
    } catch (Exception e) {

      e.printStackTrace();
    }
    WaitUtils.waitForElement(driver, Pclheading);

  }



  /**
   * Verify the page loading
   * 
   * @return boolean
   * @throws Exception
   * 
   */



  public boolean verifyPageloading() throws Exception {
    try {
      driver.get(url);
      WaitUtils.waitForPageLoad(driver);
      if (WaitUtils.waitForElement(driver, Pclheading)) {
        return true;
      } else
        return false;
    } catch (Exception e) {

      throw new Exception("Unable to load url,Pcl down:(:(" + e);
    }

  }

  /**
   * Verify the heading
   * 
   * @throws Exception
   * 
   */

  public void verifyheading() throws Exception {
    try {
      WaitUtils.waitForElement(driver, Pclheading);
      if (Pclheading.equals("XMLTransferEngine")) {
        Log.event("Launched PCL XMLTransferEngine::" + url);
        Log.message("Launched PCL XMLTransferEngine Successfully", driver, true);
      }
    }

    catch (Exception e) {
      throw new Exception("Unable to load PCL XMLTransferEngine Page:(:(" + e);
    }
  }

  /**
   * Verify All XMLs
   * 
   * @throws Exception
   * 
   */

  public void VerifyAllXmls() throws Exception {
    try {

      int definedsize = 28;
      List<WebElement> XMLnames = driver.findElements(By.cssSelector("#content>span>ul>li>a"));
      if (XMLnames.size() == definedsize) {
        Log.event("All XMLs Presented in this Page ");
        Log.message("All XMLs Presented in this Page", driver, true);
      } else
        Log.fail("All XMLs are not Presented in this Page,some Xmls are missing");

    } catch (Exception e) {
      throw new Exception("Unable to load PCL XMLTransferEngine Page:(:(" + e);
    }

  }

  /**
   * Verify User Name is displayed
   * 
   * @return boolean
   * 
   */

  public boolean failmessage() {

    return false;

  }


}
