package com.ssp.uxp_WJpages;

import java.util.HashMap;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.relevantcodes.extentreports.ExtentTest;
import com.ssp.support.Log;
import com.ssp.utils.ElementLayer;
import com.ssp.utils.GenericUtils;
import com.ssp.utils.WaitUtils;

public class YourQuotePage {
  private final WebDriver driver;
  private ExtentTest extentedReport;
  public ElementLayer uielement;

  @FindBy(css = "div#p1_STP_9547744E5D4FFB161241")
  WebElement titleAcceptance;

  @FindBy(css = "a#BUT_72C3D2CAFB89E17B3478")
  WebElement btnSummary;

  @FindBy(css = "label[for='BuildingsAD_0']")
  WebElement buildingsADCover;

  @FindBy(css = "label[for='ContentsAD_0']")
  WebElement contentsADCover;

  @FindBy(css = "label[for='HomeEmergency_0']")
  WebElement homeEmergencyCover;

  @FindBy(css = "label[for='LegalExpenses_0']")
  WebElement legalExpensesCover;

  @FindBy(css = "#BUT_7BAE44A1ECDF5A912766893")
  WebElement btnUpgradeNow;

  String dropDownOfGardenCover = "#QUE_2EA329519BBC4DCC315262-button";
  String dropDownOfBicycleCover = "#QUE_2EA329519BBC4DCC315266-button";
  String dropDownOfTechnologyEntertainmentCover = "#QUE_F15FCE5A4E4BF8F4251043-button";
  String dropDownOfPersonalItems = "#QUE_5058C59F4FB97188244329-button";
  String dropDownOfBuildingsExcessCover = "#QUE_D3AA9217F0DF1FFA4379724testg-button";
  String dropDownOfContentsExcessCover = "#QUE_D3AA9217F0DF1FFA4379729teste-button";


  public YourQuotePage(WebDriver driver, ExtentTest report) {
    this.driver = driver;
    this.extentedReport = report;
    PageFactory.initElements(driver, this);
    WaitUtils.waitForSpinner(driver);
  }

  public void click_SummaryBtn() throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, btnSummary,
          "Get your summary button is not present on the page");
      btnSummary.click();
      Log.message("Click on summary button", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the button of Summary " + e);
    }
  }

  public void addBuildingsADCover() throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, buildingsADCover,
          "Buildings AD cover button is not present on the page");
      buildingsADCover.click();
      Log.message("Click on buildings AD cover button", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the buildings AD cover button " + e);
    }
  }

  public void addContentsADCover() throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, contentsADCover,
          "Contents AD cover button is not present on the page");
      WaitUtils.waitForelementToBeClickable(driver, contentsADCover,
          "Contents AD cover button is not clickable on the page");
      contentsADCover.click();
      Log.message("Click on contents AD cover button", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the contents AD cover button " + e);
    }
  }

  public void addHomeEmergency() throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, homeEmergencyCover,
          "home Emergency Cover button is not present on the page");
      homeEmergencyCover.click();
      Log.message("Click on home Emergency Cover cover button", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the home Emergency Cover cover button " + e);
    }
  }

  public void addLegalExpenses() throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, legalExpensesCover,
          "Legal Expenses Cover button is not present on the page");
      legalExpensesCover.click();
      Log.message("Click on legal expenses cover button", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the legal expenses Cover cover button " + e);
    }
  }

  public void click_SelectGardenCover(String valueToSelect) throws Exception {
    try {
      GenericUtils.selectFromSpanDropDown(driver, dropDownOfGardenCover, valueToSelect);
      Log.message("Select the " + valueToSelect + " from the garden cover", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the garden cover drop down" + e);
    }
  }

  public void click_SelectBicycleCover(String valueToSelect) throws Exception {
    try {
      GenericUtils.selectFromSpanDropDown(driver, dropDownOfBicycleCover, valueToSelect);
      Log.message("Select the " + valueToSelect + " from the Bicycle cover", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the bicycle cover drop down" + e);
    }
  }

  public void click_SelectTechnologyEntertainment(String valueToSelect) throws Exception {
    try {
      GenericUtils.selectFromSpanDropDown(driver, dropDownOfTechnologyEntertainmentCover,
          valueToSelect);
      Log.message("Select the " + valueToSelect + " from the Technology & Entertainment",
          extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the Technology & Entertainment  drop down" + e);
    }
  }

  public void click_SelectBuildingsExcess(String valueToSelect) throws Exception {
    try {
      GenericUtils.selectFromSpanDropDown(driver, dropDownOfBuildingsExcessCover, valueToSelect);
      Log.message("Select the " + valueToSelect + " from the  Buildings excess", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the Buildings excess cover drop down" + e);
    }
  }

  public void click_SelectContentsExcess(String valueToSelect) throws Exception {
    try {
      GenericUtils.selectFromSpanDropDown(driver, dropDownOfContentsExcessCover, valueToSelect);
      Log.message("Select the " + valueToSelect + " from the  Contents excess", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the Contents excess cover drop down" + e);
    }
  }

  public void click_SelectPersonalItems(String valueToSelect) throws Exception {
    try {
      GenericUtils.selectFromSpanDropDown(driver, dropDownOfPersonalItems, valueToSelect);
      Log.message("Select the " + valueToSelect + " from the Personal Items", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the Personal Items cover drop down" + e);
    }
  }

  public void click_UpgradeToSeniorProduct() throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, btnUpgradeNow,
          "Upgrade now button is not present on the page");
      JavascriptExecutor jse = (JavascriptExecutor) driver;
      jse.executeScript("arguments[0].click()", btnUpgradeNow);
      Log.message("Click on upgrade now button", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the updgrade now button " + e);
    }
  }

  public SummaryPageDetails runFlowOfYourQuotePage(HashMap<String, String> testData)
      throws Exception {
    String gardenCoverValue = GenericUtils.convertIntoPound(testData.get("Garden"));
    String bicycleCoverValue = testData.get("Bicycles");
    String techAndEntertainmentCoverValue = GenericUtils.convertIntoPound(testData.get("Tech"));
    String peronalItemCoverValue =
        GenericUtils.convertIntoPound(testData.get("Unspecified PersonalItems"));
    String buildingsADCover = testData.get("BuildingsAD");
    String contentsADCover = testData.get("ContentsAD");
    String heCover = testData.get("Hecover");
    String leCover = testData.get("Lecover");
    String buildingExcessCoverValue =
        GenericUtils.convertIntoPound(testData.get("BuildingsExcess"));
    String policyType = testData.get("PolicyType");
    String contentsExcessCoverValue = GenericUtils.convertIntoPound(testData.get("ContentsExcess"));
    if (buildingExcessCoverValue.trim() != "") {
      this.click_SelectBuildingsExcess(buildingExcessCoverValue);
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForOverlay(driver);
    }
    if (contentsExcessCoverValue.trim() != "") {
      this.click_SelectContentsExcess(contentsExcessCoverValue);
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForOverlay(driver);
    }
    if (buildingsADCover.equalsIgnoreCase("yes")) {
      this.addBuildingsADCover();
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForOverlay(driver);
    }
    if (contentsADCover.equalsIgnoreCase("yes")) {
      this.addContentsADCover();
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForOverlay(driver);
    }
    if (heCover.equalsIgnoreCase("Yes")) {
      this.addHomeEmergency();
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForOverlay(driver);
    }
    if (leCover.equalsIgnoreCase("Yes")) {
      this.addLegalExpenses();
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForOverlay(driver);
    }
    if (gardenCoverValue.trim() != "") {
      this.click_SelectGardenCover(gardenCoverValue);
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForOverlay(driver);
    }
    if (bicycleCoverValue != "") {
      this.click_SelectBicycleCover(bicycleCoverValue);
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForOverlay(driver);
    }
    if (techAndEntertainmentCoverValue != "") {
      this.click_SelectTechnologyEntertainment(techAndEntertainmentCoverValue);
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForOverlay(driver);
    }
    if (peronalItemCoverValue.trim() != "") {
      this.click_SelectPersonalItems(peronalItemCoverValue);
      WaitUtils.waitForSpinner(driver);
    }
    if (policyType.equalsIgnoreCase("5star")) {
      this.click_UpgradeToSeniorProduct();
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForOverlay(driver);
    }
    WaitUtils.waitForOverlay(driver);
    this.click_SummaryBtn();
    return new SummaryPageDetails(driver, extentedReport);
  }

}
