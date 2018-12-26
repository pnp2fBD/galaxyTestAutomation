package com.ssp.uxp_pages;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.relevantcodes.extentreports.ExtentTest;
import com.ssp.support.Log;
import com.ssp.utils.ElementLayer;
import com.ssp.utils.GenericUtils;
import com.ssp.utils.WaitUtils;

public class BasicMotorDataCapturepage extends LoadableComponent<BasicMotorDataCapturepage> {
  private final WebDriver driver;
  private ExtentTest extentedReport;
  private boolean isPageLoaded;
  public ElementLayer uielement;
  private String radio_button_value = null;

  public static String radAgree =
      "#C2__p4_ComponentPRD_ComponentPHD__ComponentPRD_ComponentPHD_declarationAgreed > div > div > div:nth-child(2) > label";

  @FindBy(css = "[title='DynamicRisk']")
  WebElement titleAcceptance;

  @FindBy(
      css = "#C2__p4_ComponentPRD_ComponentPHD__ComponentPRD_ComponentPHD_declarationAgreed > div > div > div:nth-child(2) > label")
  WebElement declarationAgreed;


  @FindBy(css = "#C2__ComponentPRD_ComponentPHD__ComponentPRD_ComponentPHD_previousInsurance_0")
  WebElement previouslyInsured;
  @FindBy(css = "#C2__ComponentPRD_ComponentPHD__ComponentPRD_ComponentPHD_previousInsurance_0")
  WebElement proposerOccupationlocator;

  @FindBy(
      css = "#C2__ComponentPRD_ComponentPHD__ComponentPRD_ComponentPHD_previousInsuranceInsurerName")
  WebElement insuranceCompanylocator;


  @FindBy(css = "#C2__ComponentPRD_ComponentPHD__ComponentPRD_ComponentPHD_bestQuote")
  WebElement bestQuoteLocator;


  @FindBy(css = "#C2__ComponentPRD_ComponentPHD__ComponentPRD_ComponentPHD_bestQuoteBy")
  WebElement bestQuoteByLocator;


  @FindBy(css = "	#C2__Continue-ComponentPRD_ComponentPHD")
  WebElement btnContinue;



  public BasicMotorDataCapturepage(WebDriver driver, ExtentTest report) {
    this.driver = driver;
    this.extentedReport = report;
    PageFactory.initElements(driver, this);

  }



  @Override
  protected void load() {
    // TODO Auto-generated method stub
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



  /**
   * To select option Declaration Agreed
   * 
   * @param optionToClick - YES ,NO
   * @param extendReport
   * @param Screenshot - Capturing screens
   * @throws Exception
   * 
   */

  public void selectdeclarationAgree(String agree, ExtentTest extentReport) throws Exception {
    try {

      WaitUtils.waitForElement(driver, declarationAgreed);
      selectRadioButton(radAgree, agree);
      Log.message("Selected Agreement Statement :" + agree, extentReport);

    } catch (Exception e) {
      throw new Exception("Unable to select Agreement Statement" + e);
    }
  }

  /**
   * select radio button -generic
   *
   * @throws Exception
   * @param locator : string
   * @param option : string
   * 
   */
  private void selectRadioButton(String locator, String option) throws Exception {
    try {
      List<WebElement> buttonOptions = driver.findElements(By.cssSelector(locator));

      for (int i = 0; i < buttonOptions.size(); i++) {
        radio_button_value = buttonOptions.get(i).getText();
        if (radio_button_value.contains(option)) {

          WaitUtils.waitForElement(driver, buttonOptions.get(i), 1);
          buttonOptions.get(i).click();
          Thread.sleep(1000);
          radio_button_value = null;
          break;
        }
      }
    } catch (Exception e) {
      throw new Exception("Error in Selecting radio button : " + option + e);
    }
  }



  /**
   * Selects the 'Customer agrees...Direct Debit Guarantee' checkbox if it is not selected already.
   * 
   * @param extentedReport
   * @throws Exception
   */
  public void tickPriviouslyInsuredCheckbox(ExtentTest extentedReport) throws Exception {
    try {
      GenericUtils.scrollIntoView(driver, previouslyInsured);
      if (!previouslyInsured.isSelected()) {
        previouslyInsured.click();
        WaitUtils.waitForSpinner(driver);
        Log.message("Checked the 'Customer agrees...Direct Debit Guarantee' checkbox",
            extentedReport);
      } else {
        Log.message("The 'Customer agrees...Direct Debit Guarantee' checkbox is already checked",
            extentedReport);
      }
    } catch (Exception e) {
      throw new Exception(
          "Exception in checking the 'Customer agrees...Direct Debit Guarantee' checkbox." + e);
    }
  }

  /**
   * select Proposer Occupation
   * 
   * @param proposerOccupation : String
   * @param extentedReport
   * @throws Exception
   * 
   */
  public void selectProposerOccupation(String proposerOccupation, ExtentTest extentedReport)
      throws Exception {
    try {
      (new WebDriverWait(driver, 300).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Timed out to find 'Payment Plan' dropdown"))
              .until(ExpectedConditions.elementToBeClickable(proposerOccupationlocator));

      Select paymentdropdown = new Select(proposerOccupationlocator);
      paymentdropdown.selectByVisibleText(proposerOccupation);
      Log.message("Payment Plan Selected : " + proposerOccupation, extentedReport);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Select Payment Plan" + e);
    }
  }



  /**
   * select Proposer Occupation
   * 
   * @param proposerOccupation : String
   * @param extentedReport
   * @throws Exception
   * 
   */
  public void selectInsuranceCompany(String insuranceCompany, ExtentTest extentedReport)
      throws Exception {
    try {
      (new WebDriverWait(driver, 300).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Timed out to find 'Payment Plan' dropdown"))
              .until(ExpectedConditions.elementToBeClickable(insuranceCompanylocator));

      Select paymentdropdown = new Select(insuranceCompanylocator);
      paymentdropdown.selectByVisibleText(insuranceCompany);
      Log.message("Payment Plan Selected : " + insuranceCompany, extentedReport);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Select Payment Plan" + e);
    }
  }

  /**
   * Enter value in Best Quote
   * 
   * @param bestQuote : String
   * @param extentedReport
   * @throws Exception
   * 
   */

  public void entreValueinBestQuote(String bestQuote, ExtentTest extentedRepor) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, bestQuoteLocator,
          " Account number textbox is not found");

      bestQuoteLocator.clear();
      bestQuoteLocator.sendKeys(bestQuote);
      Log.message("Value entered in Best Quote :" + bestQuote, extentedReport);
    }

    catch (Exception e) {
      throw new Exception("Unable to Select Payment Plan" + e);
    }


  }

  /**
   * Enter value in Best Quote By
   * 
   * @param bestQuoteby : String
   * @param extentedReport
   * @throws Exception
   * 
   */

  public void entreValueinBestQuoteBy(String bestQuoteby, ExtentTest extentedRepor)
      throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, bestQuoteByLocator,
          " Best Quote By textbox is not found");

      bestQuoteLocator.clear();
      bestQuoteLocator.sendKeys(bestQuoteby);
      Log.message("Value entered in Best Quote By   :" + bestQuoteby, extentedReport);
    }

    catch (Exception e) {
      throw new Exception("Unable to Select Payment Plan" + e);
    }


  }

  public void clickContinueButton(ExtentTest extentedReport) {
    WaitUtils.waitForElementPresent(driver, btnContinue, "Save button to add account is not found");
    btnContinue.click();
    WaitUtils.waitForSpinner(driver);
    Log.message("Clicked on account save button", extentedReport);
  }



}
