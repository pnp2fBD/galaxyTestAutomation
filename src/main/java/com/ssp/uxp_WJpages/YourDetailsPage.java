/**
 * 
 */
package com.ssp.uxp_WJpages;

import java.util.ArrayList;
import java.util.HashMap;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.testng.Assert;
import com.relevantcodes.extentreports.ExtentTest;
import com.ssp.support.Log;
import com.ssp.utils.DataUtils;
import com.ssp.utils.ElementLayer;
import com.ssp.utils.GenericUtils;
import com.ssp.utils.WaitUtils;


/**
 * @author purendra.agrawal
 *
 */
public class YourDetailsPage extends LoadableComponent<YourDetailsPage> {

  private final WebDriver driver;
  private ExtentTest extentedReport;
  private boolean isPageLoaded;
  public ElementLayer uielement;

  String cssOfDropDownNumberOfBedrooms = "#QUE_9547744E5D4FFB161993-button";
  String cssOfDropDownTypeOfProperty = "#QUE_72C3D2CAFB89E17B2628-button";
  String cssOfDropDownYourTitle = "#QUE_72C3D2CAFB89E17B2812-button";
  String cssOfDropDownDay = "#QUE_D37785741BC2F0CB1464333\\2e DAY-button";
  String cssOfDropDownMonth = "#QUE_D37785741BC2F0CB1464333\\2e MONTH-button";
  String cssOfDropDownYear = "#QUE_D37785741BC2F0CB1464333\\2e YEAR-button";
  String cssOfDropDownHeldBusinessInsurance = "#QUE_72C3D2CAFB89E17B2822-button";
  String cssOfDropDownHeldContentsInsurance = "#QUE_72C3D2CAFB89E17B2824-button";
  String cssOfDropDownSelectTheAddress = "#QUE_043622D0E87BDFF266879-button";
  String cssOfDropDownJointPolicyHolderTitle = "#QUE_72C3D2CAFB89E17B2876-button";
  String cssOfDropDownJointPolicyHolderDay = "#QUE_72C3D2CAFB89E17B2882\\2e DAY-button";
  String cssOfDropDownJointPolicyHolderMonth = "#QUE_72C3D2CAFB89E17B2882\\2e MONTH-button";
  String cssOfDropDownJointPolicyHolderYear = "#QUE_72C3D2CAFB89E17B2882\\2e YEAR-button";
  String cssOfDropDownNumberOfClaims = "#QUE_B01DF5D2992DC57D7599-button";
  String cssOfDropDownNumberOfPersonalItems = "#QUE_B01DF5D2992DC57D7499-button";
  String cssOfDropDownNumberOfBicycles = "#QUE_7A508E6F65FCBA3D4648-button";
  String cssOfDropDownNumberOfHighRisk = "#QUE_B01DF5D2992DC57D7147-button";

  String dynamicCssOfDropDownClaimsDay = "#QUE_F5EF53E173A9139A20543\\2e DAY_R";
  String dynamicCssOfDropDownClaimsMonth = "#QUE_F5EF53E173A9139A20543\\2e MONTH_R";
  String dynamicCssOfDropDownClaimsYear = "#QUE_F5EF53E173A9139A20543\\2e YEAR_R";
  String dynamicCSSOfValueOfClaim = "#QUE_F5EF53E173A9139A20545_R";
  String dynamicCSSOfPolicySection = "#QUE_F5EF53E173A9139A20547_R";
  String dynamicCSSOfTypeOfClaim = "#QUE_F5EF53E173A9139A20549_R";

  String dynamicCssOfDropDownPersonalItemsCategory = "#QUE_F0C2BAC516DAABB925835_R";
  String dynamicCssOfPersonalItemsDescription = "#QUE_F0C2BAC516DAABB925836_R";
  String dynamicCssOfPersonalItemsValue = "#QUE_F0C2BAC516DAABB925837_R";

  String dynamicCssOfBicycleItemDescription = "#QUE_ACD332F0F8EFB64F6651_R";
  String dynamicCssOfBicycleItemValue = "#QUE_ACD332F0F8EFB64F6652_R";

  String dynamicCssOfHighRiskCategory = "#QUE_F0C2BAC516DAABB926236_R";
  String dynamicCssOfHighRiskItemDescription = "#QUE_F0C2BAC516DAABB926237_R";
  String dynamicCssOfHighRiskItemValue = "#QUE_F0C2BAC516DAABB926238_R";

  @FindBy(css = "div#p1_STP_9547744E5D4FFB161237")
  WebElement titleAcceptance;

  @FindBy(css = "#QUE_9547744E5D4FFB161738")
  WebElement houseNumberLocator;

  @FindBy(css = "#QUE_9547744E5D4FFB161740")
  WebElement postcodeLocator;

  @FindBy(css = "button#BUT_9547744E5D4FFB161793")
  WebElement btnFindAddressLocator;

  @FindBy(css = "input#QUE_9547744E5D4FFB161555")
  WebElement txtYearOfBuild;

  @FindBy(css = "input#QUE_72C3D2CAFB89E17B2814")
  WebElement txtFirstName;

  @FindBy(css = "input#QUE_72C3D2CAFB89E17B2816")
  WebElement txtSurName;

  @FindBy(css = "select#QUE_72C3D2CAFB89E17B2822")
  WebElement drpDownYearsHeldBusinessInsurance;

  @FindBy(css = "select#QUE_72C3D2CAFB89E17B2824")
  WebElement drpDownYearsHeldContentsInsurance;

  @FindBy(css = "a#BUT_72C3D2CAFB89E17B3290")
  WebElement btnGetYourQuote;

  @FindBy(css = "input#QUE_72C3D2CAFB89E17B2878")
  WebElement txtJointPolicyHolderFirstName;

  @FindBy(css = "input#QUE_72C3D2CAFB89E17B2880")
  WebElement txtJointPolicyHolderSurName;


  By radioBtnTypeOfCoverWouldYouLikeBy =
      By.cssSelector("label[for*=QUE_9547744E5D4FFB161866]>span:nth-child(2)");
  By radioBtnLikeToAddJointPolicyHolderBy =
      By.cssSelector("label[for*=QUE_72C3D2CAFB89E17B2820]>span:nth-child(2)");
  By radioBtnMadeAnyClaimsMadeIn4Years =
      By.cssSelector("label[for*=QUE_72C3D2CAFB89E17B2826]>span:nth-child(2)");
  By radioBtnAnyPersonalItemsMoreThan1500 =
      By.cssSelector("label[for*=QUE_72C3D2CAFB89E17B2828]>span:nth-child(2)");
  By radioBtnAnyBicycleMoreThan1500 =
      By.cssSelector("label[for*=QUE_72C3D2CAFB89E17B2830]>span:nth-child(2)");
  By radioBtnAnyHighRiskItemsMoreThan1500 =
      By.cssSelector("label[for*=QUE_72C3D2CAFB89E17B2832]>span:nth-child(2)");
  By radioBtnPromotionCode =
      By.cssSelector("label[for*=QUE_FE03B8B4E69C4F465444]>span:nth-child(2)");
  private String URL;
  JavascriptExecutor jse;


  /**
   * 
   * Constructor class for Card Details Page Here we initializing the driver for page factory
   * objects. For ajax element waiting time has added while initialization
   * 
   * @param driver : Webdriver
   */
  public YourDetailsPage(WebDriver driver, String URL, ExtentTest report) {
    this.URL = URL;
    this.driver = driver;
    this.extentedReport = report;
    jse = (JavascriptExecutor) driver;
    PageFactory.initElements(driver, this);
    driver.get(URL);
  }

  @Override
  protected void isLoaded() {

    WaitUtils.waitForPageLoad(driver);

    if (!isPageLoaded) {
      Assert.fail();
    }

    if (isPageLoaded && !driver.getTitle().contains("Login")) {
      Log.fail("SSP Login Page did not open up. Site might be down.", driver, extentedReport);
    }
  }

  @Override
  protected void load() {
    isPageLoaded = true;
    driver.get(URL);
    WaitUtils.waitForPageLoad(driver);
    WaitUtils.waitForElement(driver, titleAcceptance);
  }

  public void enter_House_number(String houseNumbertxt) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, houseNumberLocator,
          " House Number textbox is not found");
      houseNumberLocator.clear();
      houseNumberLocator.sendKeys(houseNumbertxt);
      Log.message("Value entered in Best Quote :" + houseNumbertxt, extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to Select House number" + e);
    }
  }

  public void enter_PostCode(String postCodetxt) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, postcodeLocator, "Post code textbox is not found");
      postcodeLocator.clear();
      postcodeLocator.sendKeys(postCodetxt);
      Log.message("Value entered in Best Quote :" + postCodetxt, extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to Select Post code number" + e);
    }
  }

  public void click_FindAddress() throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, btnFindAddressLocator,
          "Find Address button is not found on the page");
      btnFindAddressLocator.click();
      Log.message("Clicked on the find address button", extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to click on Find Address buttonr" + e);
    }
  }

  public void click_SelectAddress() throws Exception {
    try {
      if ((driver.findElements(By.cssSelector(cssOfDropDownSelectTheAddress)).size() > 0)
          && driver.findElement(By.cssSelector(cssOfDropDownSelectTheAddress)).isDisplayed()) {
        GenericUtils.selectFromSpanDropDown(driver, cssOfDropDownSelectTheAddress, 2);
        Log.message("Select the address from the drop down after entering the post code",
            extentedReport);
      }
    } catch (Exception e) {
      throw new Exception("Issue with the select address drop down" + e);
    }
  }

  public void click_RadioTypeOfCover(String valueToClick) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, radioBtnTypeOfCoverWouldYouLikeBy,
          "Type of cover would you like radio buttons are not present on the page");
      GenericUtils.selectFromRadioButtons(driver, radioBtnTypeOfCoverWouldYouLikeBy, valueToClick);
      Log.message("Select the radio button " + valueToClick + " in type of cover", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the radio button of What type of cover would you like? " + e);
    }
  }

  public void click_SelectNumberOfBedRooms(String valueToSelect) throws Exception {
    try {
      GenericUtils.selectFromSpanDropDown(driver, cssOfDropDownNumberOfBedrooms, valueToSelect);
      Log.message("Select the Number of bedrooms from the drop down", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the select of Number of bedrooms" + e);
    }
  }

  public void enter_YearOfBuild(String yearOfBuild) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, txtYearOfBuild, "Year of Build textbox is not found");
      txtYearOfBuild.clear();
      txtYearOfBuild.sendKeys(yearOfBuild);
      Log.message("Value entered in Year of Build :" + yearOfBuild, extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to enter values in Year of Build textbox" + e);
    }
  }

  public void click_SelectTypeOfProperty(String valueToSelect) throws Exception {
    try {
      GenericUtils.selectFromSpanDropDown(driver, cssOfDropDownTypeOfProperty, valueToSelect);
      Log.message("Select the Type of property from the drop down", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the select of Type of property" + e);
    }
  }

  public void click_SelectYourTitle(String valueToSelect) throws Exception {
    try {
      GenericUtils.selectFromSpanDropDown(driver, cssOfDropDownYourTitle, valueToSelect);
      Log.message("Select the What's your title? from the drop down", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the select of What's your title?" + e);
    }
  }

  public void enter_FirstName(String firstNametxt) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, txtFirstName, "First Name textbox is not found");
      txtFirstName.clear();
      if (firstNametxt.trim().equals(""))
        firstNametxt = GenericUtils.generateRandomName();
      txtFirstName.sendKeys(firstNametxt);
      Log.message("Value entered in First Name :" + firstNametxt, extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to enter value in First Name" + e);
    }
  }

  public void enter_SurName(String surNametxt) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, txtSurName, "Last name textbox is not found");
      txtSurName.clear();
      if (surNametxt.trim().equals("")) {
        surNametxt = GenericUtils.generateRandomName();
      }
      txtSurName.sendKeys(surNametxt);
      Log.message("Value entered in Last Name :" + surNametxt, extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to enter value in Last Name" + e);
    }
  }

  public void click_SelectDayOfDOB(String valueToSelect) throws Exception {
    try {
      GenericUtils.selectFromSpanDropDown(driver, cssOfDropDownDay, valueToSelect);
      Log.message("Select the Day of date of birth", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the select - Day - Date of Birth" + e);
    }
  }

  public void click_SelectMonthOfDOB(String valueToSelect) throws Exception {
    try {
      GenericUtils.selectFromSpanDropDown(driver, cssOfDropDownMonth, valueToSelect);
      Log.message("Select the Month of date of birth", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the select - Month - Date of Birth" + e);
    }
  }

  public void click_SelectYearOfDOB(String valueToSelect) throws Exception {
    try {
      GenericUtils.selectFromSpanDropDown(driver, cssOfDropDownYear, valueToSelect);
      Log.message("Select the year of date of birth", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the select - year - Date of Birth" + e);
    }
  }

  public void click_RadioLikeToAddJointPolicyHolder(String valueToClick) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, radioBtnLikeToAddJointPolicyHolderBy,
          "Would you like to add a joint policy holder? are not present on the page");
      GenericUtils.selectFromRadioButtons(driver, radioBtnLikeToAddJointPolicyHolderBy,
          valueToClick);
      Log.message("Select the radio button " + valueToClick
          + "in Would you like to add a joint policy holder?", extentedReport);
    } catch (Exception e) {
      throw new Exception(
          "Issue with the radio button of Would you like to add a joint policy holder? " + e);
    }
  }

  public void click_SelectYourJointPolicyHolderTitle(String valueToSelect) throws Exception {
    try {
      GenericUtils.selectFromSpanDropDown(driver, cssOfDropDownJointPolicyHolderTitle,
          valueToSelect);
      Log.message("Select the title of Joint PolicyHolder from the drop down", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the title of Joint PolicyHolder" + e);
    }
  }

  public void enter_JointPolicyHolderFirstName(String jpFirstNametxt) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, txtJointPolicyHolderFirstName,
          "Joint PolicyHolder First Name textbox is not found");
      txtJointPolicyHolderFirstName.clear();
      if (jpFirstNametxt.trim().equals(""))
        jpFirstNametxt = GenericUtils.generateRandomName();
      txtJointPolicyHolderFirstName.sendKeys(jpFirstNametxt);
      Log.message("Value entered in Joint PolicyHolder First Name :" + jpFirstNametxt,
          extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to enter value in Joint PolicyHolder First Name" + e);
    }
  }

  public void enter_JointPolicyHolderSurName(String jpSurNametxt) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, txtJointPolicyHolderSurName,
          "Joint PolicyHolder last name textbox is not found");
      txtJointPolicyHolderSurName.clear();
      if (jpSurNametxt.trim().equals("")) {
        jpSurNametxt = GenericUtils.generateRandomName();
      }
      txtJointPolicyHolderSurName.sendKeys(jpSurNametxt);
      Log.message("Value entered in Joint PolicyHolder Last Name :" + jpSurNametxt, extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to enter value in Joint PolicyHolder Last Name" + e);
    }
  }

  public void click_SelectJointPolicyHolderDayOfDOB(String valueToSelect) throws Exception {
    try {
      GenericUtils.selectFromSpanDropDown(driver, cssOfDropDownJointPolicyHolderDay, valueToSelect);
      Log.message("Select the Joint PolicyHolder Day of date of birth", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the select - Day - Joint PolicyHolder Date of Birth" + e);
    }
  }

  public void click_SelectJointPolicyHolderMonthOfDOB(String valueToSelect) throws Exception {
    try {
      GenericUtils.selectFromSpanDropDown(driver, cssOfDropDownJointPolicyHolderMonth,
          valueToSelect);
      Log.message("Select the Joint PolicyHolder Month of date of birth", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the select - Month - Joint PolicyHolder Date of Birth" + e);
    }
  }

  public void click_SelectJointPolicyHolderYearOfDOB(String valueToSelect) throws Exception {
    try {
      GenericUtils.selectFromSpanDropDown(driver, cssOfDropDownJointPolicyHolderYear,
          valueToSelect);
      Log.message("Select the Joint PolicyHolder year of date of birth", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the select - year - Joint PolicyHolder Date of Birth" + e);
    }
  }


  public void click_SelectYearsHeldBusinessInsurance(String valueToSelect) throws Exception {
    try {
      GenericUtils.selectFromSpanDropDown(driver, cssOfDropDownHeldBusinessInsurance,
          valueToSelect);
      Log.message("Select the How many years have you held Buildings insurance for?",
          extentedReport);
    } catch (Exception e) {
      throw new Exception(
          "Issue with the select How many years have you held Buildings insurance for?" + e);
    }
  }

  public void click_SelectYearsHeldContentsInsurance(String valueToSelect) throws Exception {
    try {
      GenericUtils.selectFromSpanDropDown(driver, cssOfDropDownHeldContentsInsurance,
          valueToSelect);
      Log.message("Select the How many years have you held contents insurance for?",
          extentedReport);
    } catch (Exception e) {
      throw new Exception(
          "Issue with the select How many years have you held contents insurance for?" + e);
    }
  }

  public void click_RadioClaimsMadeIn4Years(String valueToClick) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, radioBtnMadeAnyClaimsMadeIn4Years,
          "Have you or anyone living with you, made any claims or suffered any losses, at or away from the home in the last 4 years? is not present on the page");
      GenericUtils.selectFromRadioButtons(driver, radioBtnMadeAnyClaimsMadeIn4Years, valueToClick);
      Log.message(
          "Select the radio button " + valueToClick
              + "in Have you or anyone living with you, made any claims or suffered any losses, at or away from the home in the last 4 years?",
          extentedReport);
    } catch (Exception e) {
      throw new Exception(
          "Issue with the radio button of Have you or anyone living with you, made any claims or suffered any losses, at or away from the home in the last 4 years? "
              + e);
    }
  }

  public void click_SelectNumberOfClaims(String valueToSelect) throws Exception {
    try {
      GenericUtils.selectFromSpanDropDown(driver, cssOfDropDownNumberOfClaims, valueToSelect);
      Log.message("Select the " + valueToSelect + " number of claims", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue while selecting the number of claims" + e);
    }
  }

  public void click_SelectClaimsDayOfDOB(String valueToSelect, int i) throws Exception {
    try {
      String cssOfDropDownClaimsDay = dynamicCssOfDropDownClaimsDay + i + "-button";
      GenericUtils.selectFromSpanDropDown(driver, cssOfDropDownClaimsDay, valueToSelect);
      Log.message("Select the day of claim of claim " + i, extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue while selecting the date of claim of claim " + i + " " + e);
    }
  }

  public void click_SelectClaimsMonthOfDOB(String valueToSelect, int i) throws Exception {
    try {
      String cssOfDropDownClaimsMonth = dynamicCssOfDropDownClaimsMonth + i + "-button";
      GenericUtils.selectFromSpanDropDown(driver, cssOfDropDownClaimsMonth, valueToSelect);
      Log.message("Select the month of claim of claim " + i, extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue while selecting the month of claim of claim " + i + " " + e);
    }
  }

  public void click_SelectClaimsYearOfDOB(String valueToSelect, int i) throws Exception {
    try {
      String cssOfDropDownClaimsYear = dynamicCssOfDropDownClaimsYear + i + "-button";
      GenericUtils.selectFromSpanDropDown(driver, cssOfDropDownClaimsYear, valueToSelect);
      Log.message("Select the year of claim of claim " + i, extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue while selecting the year of claim of claim " + i + " " + e);
    }
  }

  public void enter_ValueOfClaim(String txtValueOfClaim, int i) throws Exception {
    try {
      String cssOfValueOfClaim = dynamicCSSOfValueOfClaim + i;
      By dynamicCSSOfValueOfClaimBy = By.cssSelector(cssOfValueOfClaim);
      WaitUtils.waitForElementPresent(driver, dynamicCSSOfValueOfClaimBy,
          "Value of claim " + i + " does not found on the page");
      WebElement dynamicCSSOfValueOfClaimElement = driver.findElement(dynamicCSSOfValueOfClaimBy);
      dynamicCSSOfValueOfClaimElement.clear();
      dynamicCSSOfValueOfClaimElement.sendKeys(txtValueOfClaim);
      Log.message("Value entered in value of claim " + i + " is " + txtValueOfClaim,
          extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to enter value of claim " + i + " " + e);
    }
  }

  public void click_SelectPolicySection(String valueToSelect, int i) throws Exception {
    try {
      String cssOfPolicySection = dynamicCSSOfPolicySection + i + "-button";
      GenericUtils.selectFromSpanDropDown(driver, cssOfPolicySection, valueToSelect);
      Log.message("Select the policy section of claim " + i, extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue while selecting the policy section of claim " + i + " " + e);
    }
  }

  public void click_SelectTypeOfClaim(String valueToSelect, int i) throws Exception {
    try {
      String cssOfTypeOfClaim = dynamicCSSOfTypeOfClaim + i + "-button";
      GenericUtils.selectFromSpanDropDown(driver, cssOfTypeOfClaim, valueToSelect);
      Log.message("Select the type of claim of claim " + i, extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue while selecting the type of claim of claim " + i + " " + e);
    }
  }

  public void click_RadioPersonalItemsMoreThan1500(String valueToClick) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, radioBtnAnyPersonalItemsMoreThan1500,
          "Do you have any personal items worth more than £1,500 each? is not present on the page");
      GenericUtils.selectFromRadioButtons(driver, radioBtnAnyPersonalItemsMoreThan1500,
          valueToClick);
      Log.message("Select the radio button " + valueToClick
          + "in Do you have any personal items worth more than £1,500 each?", extentedReport);
    } catch (Exception e) {
      throw new Exception(
          "Issue with the radio button of Do you have any personal items worth more than £1,500 each? "
              + e);
    }
  }

  public void click_SelectNumberOfPersonalItems(String valueToSelect) throws Exception {
    try {
      GenericUtils.selectFromSpanDropDown(driver, cssOfDropDownNumberOfPersonalItems,
          valueToSelect);
      Log.message("Select the " + valueToSelect + " number of personal items", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue while selecting the number of personal items" + e);
    }
  }

  public void click_SelectPersonalItemsCategory(String valueToSelect, int i) throws Exception {
    try {
      String cssOfDropDownPersonalItemsCategory =
          dynamicCssOfDropDownPersonalItemsCategory + i + "-button";
      GenericUtils.selectFromSpanDropDown(driver, cssOfDropDownPersonalItemsCategory,
          valueToSelect);
      Log.message("Select the type of category for personal item " + i, extentedReport);
    } catch (Exception e) {
      throw new Exception(
          "Issue while selecting the type of category while selecting personal items " + i + " "
              + e);
    }
  }

  public void enter_ValueOfPersonalItemsDescription(String txtItemDescription, int i)
      throws Exception {
    try {
      String cssOfPersonalItemsDescription = dynamicCssOfPersonalItemsDescription + i;
      By cssOfPersonalItemsDescriptionBy = By.cssSelector(cssOfPersonalItemsDescription);
      WaitUtils.waitForElementPresent(driver, cssOfPersonalItemsDescriptionBy,
          "Items description " + i + " does not found on the page");
      WebElement cssOfPersonalItemsDescriptionElement =
          driver.findElement(cssOfPersonalItemsDescriptionBy);
      cssOfPersonalItemsDescriptionElement.clear();
      cssOfPersonalItemsDescriptionElement.sendKeys(txtItemDescription);
      Log.message("Description entered in Personal Items " + i + " is " + txtItemDescription,
          extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to enter personal items description " + i + " " + e);
    }
  }

  public void enter_ValueOfPersonalItemsValue(String txtPersonalItemValue, int i) throws Exception {
    try {
      String cssOfPersonalItemsValue = dynamicCssOfPersonalItemsValue + i;
      By cssOfPersonalItemsValueBy = By.cssSelector(cssOfPersonalItemsValue);
      WaitUtils.waitForElementPresent(driver, cssOfPersonalItemsValueBy,
          "Items value " + i + " does not found on the page");
      WebElement cssOfPersonalItemsValueElement = driver.findElement(cssOfPersonalItemsValueBy);
      cssOfPersonalItemsValueElement.clear();
      cssOfPersonalItemsValueElement.sendKeys(txtPersonalItemValue);
      Log.message("Value entered in Personal Items " + i + " is " + txtPersonalItemValue,
          extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to enter personal items value " + i + " " + e);
    }
  }

  public void click_RadioBicyclesMoreThan1500(String valueToClick) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, radioBtnAnyBicycleMoreThan1500,
          "Do you have any Bicycles worth more than £1,500 each? is not present on the page");
      GenericUtils.selectFromRadioButtons(driver, radioBtnAnyBicycleMoreThan1500, valueToClick);
      Log.message("Select the radio button " + valueToClick
          + "in Do you have any Bicycles worth more than £1,500 each?", extentedReport);
    } catch (Exception e) {
      throw new Exception(
          "Issue with the radio button of Do you have any Bicycles worth more than £1,500 each? "
              + e);
    }
  }

  public void click_SelectNumberOfBicycles(String valueToSelect) throws Exception {
    try {
      GenericUtils.selectFromSpanDropDown(driver, cssOfDropDownNumberOfBicycles, valueToSelect);
      Log.message("Select the " + valueToSelect + " number of Bicycles", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue while selecting the number of Bicycles" + e);
    }
  }

  public void enter_ValueOfBicycleItemDescription(String txtBicycleItemDescription, int i)
      throws Exception {
    try {
      String cssOfBicycleItemDescription = dynamicCssOfBicycleItemDescription + i;
      By cssOfBicycleItemDescriptionBy = By.cssSelector(cssOfBicycleItemDescription);
      WaitUtils.waitForElementPresent(driver, cssOfBicycleItemDescriptionBy,
          "Items Description " + i + " does not found on the page");
      WebElement cssOfBicyclesDescriptionElement =
          driver.findElement(cssOfBicycleItemDescriptionBy);
      cssOfBicyclesDescriptionElement.clear();
      cssOfBicyclesDescriptionElement.sendKeys(txtBicycleItemDescription);
      Log.message(
          "Value entered in Bicycle Item Description " + i + " is " + txtBicycleItemDescription,
          extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to enter value in Bicycle Item description " + i + " " + e);
    }
  }

  public void enter_ValueOfBicycleItemValue(String txtBicycleItemValue, int i) throws Exception {
    try {
      String cssOfBicycleItemValue = dynamicCssOfBicycleItemValue + i;
      By cssOfBicycleItemValueBy = By.cssSelector(cssOfBicycleItemValue);
      WaitUtils.waitForElementPresent(driver, cssOfBicycleItemValueBy,
          "Items value " + i + " does not found on the page");
      WebElement cssOfBicycleItemValueElement = driver.findElement(cssOfBicycleItemValueBy);
      cssOfBicycleItemValueElement.clear();
      cssOfBicycleItemValueElement.sendKeys(txtBicycleItemValue);
      Log.message("Value entered in Bicycle Item value " + i + " is " + txtBicycleItemValue,
          extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to enter value in Bicycle Item value " + i + " " + e);
    }
  }

  public void click_RadioHighRiskItemsMoreThan1500(String valueToClick) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, radioBtnAnyHighRiskItemsMoreThan1500,
          "Do you have any High Risk Items worth over £1,500? is not present on the page");
      GenericUtils.selectFromRadioButtons(driver, radioBtnAnyHighRiskItemsMoreThan1500,
          valueToClick);
      Log.message("Select the radio button " + valueToClick
          + "in Do you have any High Risk Items worth over £1,500?", extentedReport);
    } catch (Exception e) {
      throw new Exception(
          "Issue with the radio button of Do you have any High Risk Items worth over £1,500? " + e);
    }
  }

  public void click_SelectNumberOfHighRiskItems(String valueToSelect) throws Exception {
    try {
      GenericUtils.selectFromSpanDropDown(driver, cssOfDropDownNumberOfHighRisk, valueToSelect);
      Log.message("Select the " + valueToSelect + " number of High risk items", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue while selecting the high risk items" + e);
    }
  }

  public void click_SelectHighRiskItemsCategory(String valueToSelect, int i) throws Exception {
    try {
      String cssOfHighRiskCategory = dynamicCssOfHighRiskCategory + i + "-button";
      GenericUtils.selectFromSpanDropDown(driver, cssOfHighRiskCategory, valueToSelect);
      Log.message("Select the type of category for high risk " + i, extentedReport);
    } catch (Exception e) {
      throw new Exception(
          "Issue while selecting the type of category while selecting high risk " + i + " " + e);
    }
  }

  public void enter_ValueOfHighRiskItemsDescription(String txtHighRiskItems, int i)
      throws Exception {
    try {
      String cssOfHighRiskItemDescription = dynamicCssOfHighRiskItemDescription + i;
      By cssOfHighRiskItemDescriptionBy = By.cssSelector(cssOfHighRiskItemDescription);
      WaitUtils.waitForElementPresent(driver, cssOfHighRiskItemDescriptionBy,
          "High risk Items description " + i + " does not found on the page");
      WebElement cssOfHighRiskItemDescriptionElement =
          driver.findElement(cssOfHighRiskItemDescriptionBy);
      cssOfHighRiskItemDescriptionElement.clear();
      cssOfHighRiskItemDescriptionElement.sendKeys(txtHighRiskItems);
      Log.message("Description entered in High risk " + i + " is " + txtHighRiskItems,
          extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to enter high risk items description " + i + " " + e);
    }
  }

  public void enter_ValueOfHighRiskItemsValue(String txtHighRiskItemValue, int i) throws Exception {
    try {
      String cssOfHighRiskItemValue = dynamicCssOfHighRiskItemValue + i;
      By cssOfHighRiskItemValueBy = By.cssSelector(cssOfHighRiskItemValue);
      WaitUtils.waitForElementPresent(driver, cssOfHighRiskItemValueBy,
          "Items value " + i + " does not found on the page");
      WebElement cssOfHighRiskItemValueElement = driver.findElement(cssOfHighRiskItemValueBy);
      cssOfHighRiskItemValueElement.clear();
      cssOfHighRiskItemValueElement.sendKeys(txtHighRiskItemValue);
      Log.message("Value entered in High Risk Items " + i + " is " + txtHighRiskItemValue,
          extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to enter high risk items value " + i + " " + e);
    }
  }

  public void click_RadioPromotionCode(String valueToClick) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, radioBtnPromotionCode,
          "Do you have a promotional code? is not present on the page");
      GenericUtils.selectFromRadioButtons(driver, radioBtnPromotionCode, valueToClick);
      Log.message("Select the radio button " + valueToClick + "in Do you have a promotional code?",
          extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the radio button of Do you have a promotional code? " + e);
    }
  }

  public void click_GetYourQuoteBtn() throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, btnGetYourQuote,
          "Get your quote button is not present on the page");
      btnGetYourQuote.click();
      Log.message("Click on Get your quote button", extentedReport);
    } catch (Exception e) {
      throw new Exception("Issue with the button og Get quote " + e);
    }
  }

  public void enterDOB(String dateofBirth) throws Exception {
    String dateformat[] = dateofBirth.split("-");
    String dayOfDOBValueToSelect = dateformat[0];
    String monthOfDOBValueToSelect = dateformat[1];
    String yearOfDOBValueToSelect = dateformat[2];
    this.click_SelectDayOfDOB(dayOfDOBValueToSelect);
    this.click_SelectMonthOfDOB(monthOfDOBValueToSelect);
    this.click_SelectYearOfDOB(yearOfDOBValueToSelect);
  }

  public void enterJointPolicyHolderDOB(String jpDateOfBirth) throws Exception {
    String dateformat[] = jpDateOfBirth.split("-");
    String dayOfDOBValueToSelect = dateformat[0];
    String monthOfDOBValueToSelect = dateformat[1];
    String yearOfDOBValueToSelect = dateformat[2];
    this.click_SelectJointPolicyHolderDayOfDOB(dayOfDOBValueToSelect);
    this.click_SelectJointPolicyHolderMonthOfDOB(monthOfDOBValueToSelect);
    this.click_SelectJointPolicyHolderYearOfDOB(yearOfDOBValueToSelect);
  }

  public void enterClaimsDate(String dateOfClaim, int i) throws Exception {
    String dateformat[] = dateOfClaim.split("-");
    String dayOfDOBValueToSelect = dateformat[0];
    String monthOfDOBValueToSelect = dateformat[1];
    String yearOfDOBValueToSelect = dateformat[2];
    this.click_SelectClaimsDayOfDOB(dayOfDOBValueToSelect, i);
    Thread.sleep(1000);
    this.click_SelectClaimsMonthOfDOB(monthOfDOBValueToSelect, i);
    Thread.sleep(1000);
    this.click_SelectClaimsYearOfDOB(yearOfDOBValueToSelect, i);
  }

  public void enterJointPolicyHolderDetailsFlow(String jointPolicyHolderDetailsData)
      throws Exception {
    ArrayList<HashMap<String, String>> jpTestDataList =
        DataUtils.getListDataValues(jointPolicyHolderDetailsData);
    if (jpTestDataList.size() == 1) {
      HashMap<String, String> jpTestDataValues = jpTestDataList.get(0);
      this.click_SelectYourJointPolicyHolderTitle(jpTestDataValues.get("Title"));
      this.enter_JointPolicyHolderFirstName(jpTestDataValues.get("FirstName"));
      this.enter_JointPolicyHolderSurName(jpTestDataValues.get("Surname"));
      this.enterJointPolicyHolderDOB(jpTestDataValues.get("DOB"));
    } else
      throw new Exception("Please check your input data for the Joint Policy Holder");
  }

  public void enterClaimsFlow(String claimsData) throws Exception {
    ArrayList<HashMap<String, String>> claimsTestDataList = DataUtils.getListDataValues(claimsData);
    int numberOfClaims = claimsTestDataList.size();
    if (numberOfClaims > 0) {
      this.click_SelectNumberOfClaims(Integer.toString(numberOfClaims));
      for (int i = 0; i < claimsTestDataList.size(); i++) {
        HashMap<String, String> claimTestDataValues = claimsTestDataList.get(i);
        this.enterClaimsDate(claimTestDataValues.get("Date of claim"), i + 1);
        this.enter_ValueOfClaim(claimTestDataValues.get("Value of claim"), i + 1);
        this.click_SelectPolicySection(claimTestDataValues.get("Policy Section"), i + 1);
        this.click_SelectTypeOfClaim(claimTestDataValues.get("Type of claim"), i + 1);
      }
    } else
      throw new Exception("Please check your input data for the claims");
  }

  public void enterPersonalItemsFlow(String personalItemsData) throws Exception {
    ArrayList<HashMap<String, String>> personalItemsTestDataList =
        DataUtils.getListDataValues(personalItemsData);
    int numberOfPersonalItems = personalItemsTestDataList.size();
    if (numberOfPersonalItems > 0) {
      this.click_SelectNumberOfPersonalItems(Integer.toString(numberOfPersonalItems));
      for (int i = 0; i < personalItemsTestDataList.size(); i++) {
        HashMap<String, String> personalItemsTestDataValues = personalItemsTestDataList.get(i);
        this.click_SelectPersonalItemsCategory(personalItemsTestDataValues.get("Category"), i + 1);
        this.enter_ValueOfPersonalItemsDescription(personalItemsTestDataValues.get("Description"),
            i + 1);
        this.enter_ValueOfPersonalItemsValue(personalItemsTestDataValues.get("Value"), i + 1);
      }
    } else
      throw new Exception("Please check your input data for the personal items");
  }

  public void enterBicycleFlow(String bicycleTestData) throws Exception {
    ArrayList<HashMap<String, String>> bicyclesTestDataList =
        DataUtils.getListDataValues(bicycleTestData);
    int numberOfBicycles = bicyclesTestDataList.size();
    if (numberOfBicycles > 0) {
      this.click_SelectNumberOfBicycles(Integer.toString(numberOfBicycles));
      for (int i = 0; i < bicyclesTestDataList.size(); i++) {
        HashMap<String, String> bicyclesTestDataValues = bicyclesTestDataList.get(i);
        this.enter_ValueOfBicycleItemDescription(bicyclesTestDataValues.get("Description"), i + 1);
        this.enter_ValueOfBicycleItemValue(bicyclesTestDataValues.get("Value"), i + 1);
      }
    } else
      throw new Exception("Please check your input data for the Bicycle");
  }

  public void enterHighRiskItemsFlow(String highRiskItemsData) throws Exception {
    ArrayList<HashMap<String, String>> highRiskItemsTestDataList =
        DataUtils.getListDataValues(highRiskItemsData);
    int numberOfHighRisk = highRiskItemsTestDataList.size();
    if (numberOfHighRisk > 0) {
      this.click_SelectNumberOfHighRiskItems(Integer.toString(numberOfHighRisk));
      for (int i = 0; i < highRiskItemsTestDataList.size(); i++) {
        HashMap<String, String> highRiskTestDataValues = highRiskItemsTestDataList.get(i);
        this.click_SelectHighRiskItemsCategory(highRiskTestDataValues.get("Category"), i + 1);
        this.enter_ValueOfHighRiskItemsDescription(highRiskTestDataValues.get("Description"),
            i + 1);
        this.enter_ValueOfHighRiskItemsValue(highRiskTestDataValues.get("Value"), i + 1);
      }
    } else
      throw new Exception("Please check your input data for the High risk items");
  }

  public YourQuotePage runFlowOfYourDetailsPage(HashMap<String, String> testData) throws Exception {
    String postCodetxt = testData.get("PostCode");
    String typeOfCoverValueToClick = testData.get("Cover Type");
    String numberOfBedroomsValueToSelect = testData.get("No of Bedroom");
    String yearOfBuild = testData.get("Year of Build");
    String typeOfPropertyValueToSelect = testData.get("Type of Property");
    String titleValueToSelect = testData.get("Title");
    String firstNametxt = testData.get("FirstName");
    String surNametxt = testData.get("LastName");
    String addJointPolicyHolderValueToSelect = testData.get("Joint Policy");
    String dateofBirth = testData.get("DOB(dd/mm/yyyy)");
    String yearsHeldBusinessInsuranceValueToSelect = testData.get("Building Insurance Held");
    String yearsHeldContentsInsuranceValueToSelect = testData.get("Contents Insurance Held");
    String claimsMadeIn4YearsValueToSelect = testData.get("Claims Made");
    String personalItemsMoreThan1500ValueToSelect = testData.get("Personal Items");
    String bicyclesMoreThan1500ValueToClick = testData.get("Bicycle");
    String highRiskItemsMoreThan1500ValueToClick = testData.get("High Risk");
    String promotionCodeValueToClick = testData.get("Have Promo Code");
    String jointPolicyHolderDetails = testData.get("Joint Policy holder details");
    String claimsData = testData.get("Claim Details");
    String personalItemsTestData = testData.get("Specified Personal Items");
    String bicyclesTestData = testData.get("Specified Bicycle");
    String highRiskItemsData = testData.get("High Risk Items");
    this.enter_PostCode(postCodetxt);
    this.click_FindAddress();
    WaitUtils.waitForSpinner(driver);
    this.click_SelectAddress();
    WaitUtils.waitForSpinner(driver);
    this.click_RadioTypeOfCover(typeOfCoverValueToClick);
    this.click_SelectNumberOfBedRooms(numberOfBedroomsValueToSelect);
    this.enter_YearOfBuild(yearOfBuild);
    this.click_SelectTypeOfProperty(typeOfPropertyValueToSelect);
    this.click_SelectYourTitle(titleValueToSelect);
    this.enter_FirstName(firstNametxt);
    this.enter_SurName(surNametxt);
    this.enterDOB(dateofBirth);
    this.click_RadioLikeToAddJointPolicyHolder(addJointPolicyHolderValueToSelect);
    if (addJointPolicyHolderValueToSelect.equalsIgnoreCase("Yes")) {
      this.enterJointPolicyHolderDetailsFlow(jointPolicyHolderDetails);
    }
    this.click_SelectYearsHeldBusinessInsurance(yearsHeldBusinessInsuranceValueToSelect);
    this.click_SelectYearsHeldContentsInsurance(yearsHeldContentsInsuranceValueToSelect);
    this.click_RadioClaimsMadeIn4Years(claimsMadeIn4YearsValueToSelect);
    if (claimsMadeIn4YearsValueToSelect.equalsIgnoreCase("Yes"))
      this.enterClaimsFlow(claimsData);
    this.click_RadioPersonalItemsMoreThan1500(personalItemsMoreThan1500ValueToSelect);
    if (personalItemsMoreThan1500ValueToSelect.equalsIgnoreCase("yes"))
      this.enterPersonalItemsFlow(personalItemsTestData);
    this.click_RadioBicyclesMoreThan1500(bicyclesMoreThan1500ValueToClick);
    if (bicyclesMoreThan1500ValueToClick.equalsIgnoreCase("Yes"))
      this.enterBicycleFlow(bicyclesTestData);
    this.click_RadioHighRiskItemsMoreThan1500(highRiskItemsMoreThan1500ValueToClick);
    if (highRiskItemsMoreThan1500ValueToClick.equalsIgnoreCase("Yes"))
      this.enterHighRiskItemsFlow(highRiskItemsData);
    this.click_RadioPromotionCode(promotionCodeValueToClick);
    this.click_GetYourQuoteBtn();
    return new YourQuotePage(driver, extentedReport);
  }
}
