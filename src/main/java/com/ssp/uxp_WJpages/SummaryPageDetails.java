package com.ssp.uxp_WJpages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.relevantcodes.extentreports.ExtentTest;
import com.ssp.support.Log;
import com.ssp.utils.ElementLayer;
import com.ssp.utils.WaitUtils;

public class SummaryPageDetails {
  private final WebDriver driver;
  private ExtentTest extentedReport;
  public ElementLayer uielement;

  @FindBy(css = "label[for='SingleSelectCheckbox_imp_0']")
  WebElement importantStatementsCheckBox;

  @FindBy(css = "a#BUT_72C3D2CAFB89E17B3478")
  WebElement btnPayment;

  public SummaryPageDetails(WebDriver driver, ExtentTest report) {
    this.driver = driver;
    this.extentedReport = report;
    PageFactory.initElements(driver, this);
    WaitUtils.waitForSpinner(driver);
  }

  public void click_ImportantCheckBox() throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, importantStatementsCheckBox,
          "I agree with above statements checkbox is not present");
      importantStatementsCheckBox.click();
      Log.message("Click on I agree with above statements check box", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the check box of I agree with above statements " + e);
    }
  }

  public void click_PaymentBtn() throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, btnPayment,
          "Payment button is not present on the page");
      btnPayment.click();
      Log.message("Click on Payment button", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the button of Payment " + e);
    }
  }

  public PaymentOptionsPage runFlowOfSummaryPage() throws Exception {
    this.click_ImportantCheckBox();
    this.click_PaymentBtn();
    return new PaymentOptionsPage(driver, extentedReport);
  }

}
