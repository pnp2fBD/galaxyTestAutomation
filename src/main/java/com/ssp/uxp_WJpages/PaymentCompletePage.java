package com.ssp.uxp_WJpages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.relevantcodes.extentreports.ExtentTest;
import com.ssp.support.Log;
import com.ssp.utils.ElementLayer;
import com.ssp.utils.WaitUtils;

public class PaymentCompletePage {
  private final WebDriver driver;
  private ExtentTest extentedReport;
  public ElementLayer uielement;
  public String emailAddress;

  public PaymentCompletePage(WebDriver driver, ExtentTest report) {
    this.driver = driver;
    this.extentedReport = report;
    PageFactory.initElements(driver, this);
    WaitUtils.waitForSpinner(driver);
  }

  @FindBy(css = "span.quote-number>span")
  WebElement quoteNumberLocator;

  @FindBy(css = "#HEAD_398053B6E2189E741873")
  WebElement thankyouLocator;

  public void get_QuoteNumber() throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, quoteNumberLocator,
          "Quote number is not present on the page");
      String quoteNumber = quoteNumberLocator.getText().split("-")[2];
      Log.message("Quote number is " + quoteNumber, extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the fetching of quote number" + e);
    }
  }

  public void validateQuoteSuccessfull() throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, thankyouLocator,
          "Thank you page is not showing. Might be there is some issue in WJ");
    } catch (Exception e) {
      throw new Exception("Issue with the Thank you page" + e);
    }
  }

  public void runFlowOfPaymentCompletePage() throws Exception {
    WaitUtils.waitForSpinner(driver);
    this.validateQuoteSuccessfull();
    this.get_QuoteNumber();
  }


}
