/**
 * 
 */
package com.ssp.uxp_pages;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.ssp.utils.ElementLayer;
import com.ssp.utils.WaitUtils;

/**
 * @author jheelum.dutta
 *
 */
public class SelectHomePage extends LoadableComponent<SelectHomePage> {


  private WebDriver driver;
  private boolean isPageLoaded;
  public ElementLayer uielement;

  @FindBy(css = "#crashed_workitems_fs")
  WebElement resumecrashedworkitemslabel;

  public SelectHomePage(WebDriver driver) {
    this.driver = driver;
    ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, WaitUtils.maxElementWait);
    PageFactory.initElements(finder, this);
  }

  @Override
  protected void isLoaded() {

    if (!isPageLoaded) {
      Assert.fail();
    }

    (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
        .ignoring(NoSuchElementException.class).withMessage("SIAAS Home page did not open up."))
            .until(ExpectedConditions.visibilityOf(resumecrashedworkitemslabel));

    uielement = new ElementLayer(driver);
  }

  @Override
  protected void load() {

    isPageLoaded = true;
    WaitUtils.waitForPageLoad(driver);

  }

  /**
   * Verify HomePage
   * 
   * @throws Exception : Custom Exception Message
   * @return boolean
   * 
   */
  public boolean verifyHomePage() throws Exception {
    WaitUtils.waitForSpinner(driver);
    if (!WaitUtils.waitForElement(driver, resumecrashedworkitemslabel))
      throw new Exception("Home Page is not loaded");
    return true;
  }



}
