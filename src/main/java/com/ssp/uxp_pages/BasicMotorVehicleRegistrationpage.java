/**
 * 
 */
package com.ssp.uxp_pages;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.relevantcodes.extentreports.ExtentTest;
import com.ssp.support.Log;
import com.ssp.utils.ElementLayer;
import com.ssp.utils.WaitUtils;

/**
 * @author jheelum.dutta
 *
 */
public class BasicMotorVehicleRegistrationpage
    extends LoadableComponent<BasicMotorVehicleRegistrationpage> {
  private final WebDriver driver;
  private ExtentTest extentedReport;
  private boolean isPageLoaded;
  public ElementLayer uielement;


  @FindBy(css = "[title='DynamicRisk']")
  WebElement titleAcceptance;

  @Override
  protected void load() {

    // is page loaded
    if (!isPageLoaded) {
      Assert.fail();
    }

    if (isPageLoaded && !driver.getTitle().contains("DynamicRisk")) {
      Log.fail("SSP DynamicRisk Page did not open up. Site might be down.", driver, extentedReport);
    }
    uielement = new ElementLayer(driver);
  }

  @Override
  protected void isLoaded() throws Error {
    // TODO Auto-generated method stub

    isPageLoaded = true;
    WaitUtils.waitForPageLoad(driver);

    (new WebDriverWait(driver, 20).pollingEvery(200, TimeUnit.MILLISECONDS)
        .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
        .withMessage("Buyquote page not loaded properly"))
            .until(ExpectedConditions.visibilityOf(titleAcceptance));

  }

  public BasicMotorVehicleRegistrationpage(WebDriver driver, ExtentTest report) {
    this.driver = driver;
    this.extentedReport = report;
    PageFactory.initElements(driver, this);

  }
}
