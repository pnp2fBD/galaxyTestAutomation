package com.ssp.uxp_pages;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
import com.ssp.utils.GenericUtils;
import com.ssp.utils.WaitUtils;

public class ManageContactDetailsPage extends LoadableComponent<ManageContactDetailsPage> {

  private final WebDriver driver;
  private ExtentTest extentedReport;
  private boolean isPageLoaded;
  public ElementLayer uielement;

  // Message to be verified
  public final String POSTCODE_ERROR_MSG = "Postcode is Required.";

  // Elements Declaration
  @FindBy(css = "a[data-target='#C2__C1__address']")
  WebElement manageCustDetailsAddrs;

  @FindBy(css = "a[data-target='#C2__C1__phone-email']")
  WebElement manageCustDetailsphone;

  @FindBy(css = "button[class*='Edit']")
  WebElement editBtn;

  @FindBy(css = "table[summary='Contact Addresses'] tbody td:nth-child(1)")
  WebElement txtFullAddress;

  @FindBy(css = "button[title='Add New Address']")
  WebElement btnAddNewAddress;

  @FindBy(css = "select[name*='ADDADDRESS[1].ADDRESSTYPE']")
  WebElement lstAddAddressType;

  @FindBy(css = "select[name*='ADDADDRESS[1].ADDRESSTYPE'] option")
  List<WebElement> drpoptAddAddressTypeOptions;

  @FindBy(css = "input[name*='ADDADDRESS[1].HOUSENUMBER']")
  WebElement fldAddAddressHouseName;

  @FindBy(css = ".checkbox input[name*='ADDADDRESS[1].MAILINGADDRESS']")
  WebElement chkAddMailingAddress;

  @FindBy(css = "#C2__C1__QUE_753975C04A0241C1630594[name*='ADDADDRESS[1].POSTCODE']")
  WebElement fldAddAddressPostCode;

  @FindBy(css = "#C2__C1__BUT_753975C04A0241C1630598[title='Find Address']")
  WebElement btnAddFindAddress;

  @FindBy(css = "#C2__C1__QUE_753975C04A0241C1630604 .list-group-item")
  WebElement lstAddAddressPostCodeDetail;

  @FindBy(css = "#C2__C1__BUT_52BE0008F501490E9216894[value='Save']")
  WebElement btnAddAddressSave;

  @FindBy(css = "#C2__C1__BUT_753975C04A0241C1633500[title='Close']")
  WebElement btnAddAddressClose;

  @FindBy(css = "#C2__C1__p4_QUE_753975C04A0241C1630474")
  WebElement fldMailingAddressType;

  @FindBy(css = "#C2__C1__QUE_753975C04A0241C1630478_0[value='Mailing Address']")
  WebElement chkboxMailingAddress;

  @FindBy(css = "input[name*='ADDRESS[1].ADDRESSLINE1']:not([disabled='disabled'])")
  WebElement fldMailingAddressLine1;

  @FindBy(css = "input[name*='ADDRESS[1].ADDRESSLINE2']:not([disabled='disabled'])")
  WebElement fldMailingAddressLine2;

  @FindBy(css = "input[name*='ADDRESS[1].ADDRESSLINE3']:not([disabled='disabled'])")
  WebElement fldMailingAddressLine3;

  @FindBy(css = "input[name*='ADDRESS[1].POSTCODE'][disabled='']")
  WebElement fldMailingAddressPostCode;

  @FindBy(id = "select[name*='ADDRESS[1].COUNTRY_READONLY']")
  WebElement fldMailingAddressCountry;

  @FindBy(
      css = "#C2__C1__BUT_4E3BCB6B5201A369249686[title='Enter Address Manually']:not(.page-spinner)")
  WebElement lnkEnterManyually;

  @FindBy(css = "#C2__C1__FMT_753975C04A0241C1630467.manualAddress")
  WebElement sectionManualAddress;

  @FindBy(css = "#C2__C1__p1_GRP_1DAA4701266759AC346275[style*='relative'] ")
  WebElement sectionReplaceAddress;

  @FindBy(id = "C2__C1__QUE_1DAA4701266759AC347049")
  WebElement fldReplaceAddressType;

  @FindBy(css = "input[name*='REPLACEADDRESS[1].HOUSENUMBER']:not([disabled='disabled'])")
  WebElement fldReplaceAddressHouseName;

  @FindBy(css = "input[name*='REPLACEADDRESS[1].ADDRESSLINE1']:not([disabled='disabled'])")
  WebElement fldReplaceAddressLine1;

  @FindBy(css = "input[name*='REPLACEADDRESS[1].ADDRESSLINE2']:not([disabled='disabled'])")
  WebElement fldReplaceAddressLine2;

  @FindBy(css = "input[name*='REPLACEADDRESS[1].ADDRESSLINE3']:not([disabled='disabled'])")
  WebElement fldReplaceAddressLine3;

  @FindBy(css = "input[name*='REPLACEADDRESS[1].ADDRESSLINE4']:not([disabled='disabled'])")
  WebElement fldReplaceAddressLine4;

  @FindBy(css = "select[name*='REPLACEADDRESS[1].COUNTRY']:not([disabled='disabled'])")
  WebElement fldReplaceCountry;

  @FindBy(css = "input[name*='REPLACEADDRESS[1].POSTCODE']:not([disabled='disabled'])")
  WebElement fldReplaceAddressPostCode;

  @FindBy(css = "#C2__C1__BUT_753975C04A0241C1630491[title='Find Address']")
  WebElement btnFindAddress;

  @FindBy(
      css = "#C2__C1__QUE_753975C04A0241C1630497[name*='ADDRESS[1].ADDRESSLIST']:not([disabled='disabled']) .list-group-item")
  WebElement lstPostCodeDetail;

  @FindBy(css = "#C2__C1__BUT_753975C04A0241C1630559[title='Cancel']")
  WebElement btnCancel;

  @FindBy(css = "#C2__C1__BUT_753975C04A0241C1630556[title='Save']")
  WebElement btnSave;

  @FindBy(id = "C2__C1__QUE_1DAA4701266759AC347158_ERRORMESSAGE")
  WebElement errmsgPostCode;

  /**
   * 
   * Constructor class for Page Here we initializing the driver for page factory objects. For ajax
   * element waiting time has added while initialization
   * 
   * @param driver : Webdriver
   */
  public ManageContactDetailsPage(WebDriver driver, ExtentTest report) {
    this.driver = driver;
    this.extentedReport = report;
    PageFactory.initElements(driver, this);
  }

  @Override
  protected void load() {
    isPageLoaded = true;
    WaitUtils.waitForPageLoad(driver, 60);

  }// load

  @Override
  protected void isLoaded() {

    if (!isPageLoaded) {
      Assert.fail();
    }

    if (isPageLoaded && !WaitUtils.waitForElement(driver, manageCustDetailsAddrs)) {
      Log.message("User is not navigated to Customer Dashboard Page", driver, extentedReport);
    }
    uielement = new ElementLayer(driver);
  }// isLoaded

  /**
   * Verify Manage contact details fields
   * 
   * @return boolean
   * @throws Exception
   */
  public boolean verifyManageContactDetailsFields() throws Exception {
    boolean status = false;
    try {
      WaitUtils.waitForelementToBeClickable(driver, manageCustDetailsAddrs,
          "Unable to locate Manage Custometer Detail address element");
      if (WaitUtils.waitForElement(driver, manageCustDetailsAddrs)
          && WaitUtils.waitForElement(driver, manageCustDetailsphone)) {
        status = true;
      }
      return status;
    } catch (Exception e) {
      throw new Exception("Unable to find Manage Customer Details address/phone/email tabs" + e);
    }

  }

  /**
   * Click Edit Button
   * 
   * @param extentedReport
   * @param Screenshot
   * @throws Exception
   */
  public void clickEditButton(ExtentTest extentedReport, boolean Screenshot) throws Exception {
    try {
      WaitUtils.waitForelementToBeClickable(driver, editBtn, "Unable to find the Edit button");
      editBtn.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Edit Button", driver, extentedReport, Screenshot);
    } catch (Exception e) {
      throw new Exception("Unable to click Edit button " + e);
    }

  }

  /**
   * Verify edit button is present in an address row
   * 
   * @param extentedReport
   * @param Screenshot
   * @throws Exception
   */
  public void checkRowEditBtn(ExtentTest extentedReport, boolean Screenshot) throws Exception {
    try {
      WebElement AddressActual =
          driver.findElement(By.cssSelector("tr#C2__C1__p0_TBL_753975C04A0241C1630453_R1>td"));
      if (AddressActual.isDisplayed()) {
        if (editBtn.isDisplayed())
          Log.pass("Edit Btn is presented", driver, extentedReport, Screenshot);
        else
          Log.fail("Edit Btn is not presented", driver, extentedReport, Screenshot);

      }
    } catch (Exception e) {
      throw new Exception("Unable to click Edit Btn " + e);
    }

  }

  /**
   * 
   * Verify Mailing address check box is disabled and selected
   * 
   * @throws Exception
   */

  public boolean verifyMailingAddressChkBoxDisabled() throws Exception {
    boolean status = false;
    try {
      if (!chkboxMailingAddress.isEnabled()
          && chkboxMailingAddress.getAttribute("disabled").contains("true")
          && chkboxMailingAddress.getAttribute("checked").contains("true")) {
        status = true;
      }
    } catch (Exception e) {
      throw new Exception("Unable to locate Mailing address checkbox" + e);
    }
    return status;

  }

  /**
   * 
   * Verify Mailing address fields are disabled (Address
   * Type,AddressLine1,AddressLine2,AddressLine3,PostCode,Country)
   * 
   * @throws Exception
   */

  public boolean verifyMailingAddressFieldsDisabled() throws Exception {
    boolean status = false;
    try {
      if (!fldMailingAddressLine1.isEnabled() && !fldMailingAddressLine2.isEnabled()
          && !fldMailingAddressLine3.isEnabled() && !fldMailingAddressPostCode.isEnabled()) {
        status = true;
      }
    } catch (Exception e) {
      throw new Exception("Unable to locate Mailing address fields" + e);
    }
    return status;

  }

  /**
   * 
   * Click Enter Manually button
   * 
   * @param extentedReport
   * @param screenshot
   * @throws Exception
   */

  public void clickEnterManuallyLink(ExtentTest extentedReport, boolean screenshot)
      throws Exception {
    try {
      if (WaitUtils.waitForElement(driver, lnkEnterManyually)) {
        lnkEnterManyually.click();
        WaitUtils.waitForElement(driver, fldReplaceAddressLine1);
        Log.message("Clicked on Enter Manually button", driver, extentedReport, screenshot);
      }
    } catch (Exception e) {
      throw new Exception("Unable to click Enter Manually button" + e);
    }

  }

  /**
   * 
   * Enter Replace Address details
   * 
   * @param fieldName - HouseName/PostCode
   * @param fieldValue - value to be entered
   * @param extentedReport
   * @param screenshot
   * @throws Exception
   */

  public void enterReplaceAddressDetails(String fieldName, String fieldValue,
      ExtentTest extentedReport, boolean screenshot) throws Exception {
    Log.event("Entering Address Detail " + fieldName + " :" + fieldValue);
    (new WebDriverWait(driver, 60).pollingEvery(200, TimeUnit.MILLISECONDS)
        .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
        .withMessage("Unable to find field text box"))
            .until(ExpectedConditions.visibilityOf(fldReplaceAddressPostCode));
    switch (fieldName) {
      case "HouseName":
        fldReplaceAddressHouseName.clear();
        fldReplaceAddressHouseName.sendKeys(fieldValue);
        break;
      case "PostCode":
        fldReplaceAddressPostCode.clear();
        fldReplaceAddressPostCode.sendKeys(fieldValue);
        break;
      default:
        throw new Exception("Address " + fieldName + " is not entered");
    }
    Log.message("Entered Address field " + fieldName + " : '" + fieldValue + "'", driver,
        extentedReport, screenshot);
  }

  /**
   * 
   * Click Find Address button
   * 
   * @param extentedReport
   * @param screenshot
   * @throws Exception
   */

  public void clickFindAddressButton(ExtentTest extentedReport, boolean screenshot)
      throws Exception {
    try {
      if (WaitUtils.waitForElement(driver, btnFindAddress)) {
        btnFindAddress.click();
        WaitUtils.waitForSpinner(driver);
        WaitUtils.waitForElement(driver, lstPostCodeDetail);
        Log.message("Clicked on Find Address button", driver, extentedReport, screenshot);
      }
    } catch (Exception e) {
      throw new Exception("Unable to click Find Address button" + e);
    }
  }

  /**
   * 
   * Click Find Address button
   * 
   * @param extentedReport
   * @param screenshot
   * @throws Exception
   */

  public void clickFindAddressDetail(ExtentTest extentedReport, boolean screenshot)
      throws Exception {
    try {
      if (WaitUtils.waitForElement(driver, lstPostCodeDetail)) {
        lstPostCodeDetail.click();
        Thread.sleep(2000);
        Log.message("Clicked on Find Address Detail", driver, extentedReport, screenshot);
      }
    } catch (Exception e) {
      throw new Exception("Unable to click Find Address Detail list" + e);
    }

  }

  /**
   * To verify PostCode Error Message
   * 
   * @param msg message to be verified
   * @return true if success message displayed as expected else false
   */
  public boolean verifyPostCodeErrorMsg(String msg) {
    boolean status = false;
    if (WaitUtils.waitForElement(driver, errmsgPostCode)) {
      if (GenericUtils.verifyWebElementTextEquals(errmsgPostCode, msg)) {
        status = true;
      }
    }
    return status;
  }

  /**
   * To return Post code address from the list
   * 
   * @return address
   */
  public String getPostCodeAddressFromList() {
    return GenericUtils.getTextOfWebElement(lstPostCodeDetail, driver);
  }

  /**
   * Click Cancel Button
   * 
   * @param extentedReport
   * @param Screenshot
   * @throws Exception
   */
  public void clickCancelButton(ExtentTest extentedReport, boolean Screenshot) throws Exception {
    try {
      WaitUtils.waitForelementToBeClickable(driver, btnCancel, "Unable to find the Cancel button");
      btnCancel.click();
      Thread.sleep(2000);
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Cancel Button", driver, extentedReport, Screenshot);
    } catch (Exception e) {
      throw new Exception("Unable to click Cancel button " + e);
    }
  }

  /**
   * Click Save Button
   * 
   * @param extentedReport
   * @param Screenshot
   * @throws Exception
   */
  public void clickSaveButton(ExtentTest extentedReport, boolean Screenshot) throws Exception {
    try {
      WaitUtils.waitForelementToBeClickable(driver, btnSave, "Unable to find the Save button");
      btnSave.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Save Button", driver, extentedReport, Screenshot);
    } catch (Exception e) {
      throw new Exception("Unable to click Save button " + e);
    }
  }

  /**
   * To verify Full Address Post code
   * 
   * @param msg message to be verified
   * @return true if Post code displayed as expected else false
   * @throws Exception
   */
  public boolean verifyFullAddressPostCode(String msg) throws Exception {
    boolean status = false;
    if (GenericUtils.verifyWebElementTextContains(txtFullAddress, msg)) {
      status = true;
    }
    return status;
  }

  /**
   * Click Add New Address Button
   * 
   * @param extentedReport
   * @param Screenshot
   * @throws Exception
   */
  public void clickAddNewAddressButton(ExtentTest extentedReport, boolean Screenshot)
      throws Exception {
    try {
      WaitUtils.waitForelementToBeClickable(driver, btnAddNewAddress,
          "Unable to find the AddNewAddress button");
      btnAddNewAddress.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Add New Address Button", driver, extentedReport, Screenshot);
    } catch (Exception e) {
      throw new Exception("Unable to click Add New Address button " + e);
    }
  }

  /**
   * 
   * Verify Replace Address House Name is disabled
   * 
   * @param extentedReport
   * @param screenshot
   * @throws Exception
   */

  public boolean verifyReplaceAddressTypeAndHouseNameDisabled() throws Exception {
    boolean status = false;
    try {
      if (WaitUtils.waitForElement(driver, fldReplaceAddressHouseName)) {
        status = true;
      }
    } catch (Exception e) {
      throw new Exception("Able to locate House Name field" + e);
    }
    return status;

  }

  /**
   * Enter New Address Details
   * 
   * @param extentedReport
   * @param Screenshot
   * @throws Exception
   */
  public void enterNewAddressDetails(String postcode, String type, ExtentTest extentedReport,
      boolean Screenshot) throws Exception {
    try {
      enterAddress(postcode, type);
      // WaitUtils.waitForElement(driver, lstAddAddressPostCodeDetail);
      // lstAddAddressPostCodeDetail.click();
      Thread.sleep(2000);
      Log.message("Entered New Address Details", driver, extentedReport, Screenshot);
      btnAddAddressSave.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Save button", extentedReport);
      btnAddAddressClose.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Close button", driver, extentedReport, Screenshot);
    } catch (Exception e) {
      throw new Exception("Unable to click Add New Address details " + e);
    }
  }

  /**
   * Enter New Address Details
   * 
   * @param postCode
   * @param type
   * 
   */
  public void enterAddress(String postCode, String type) throws Exception {
    try {

      lstAddAddressType.click();
      Thread.sleep(2000);
      GenericUtils.getMatchingTextElementFromList(drpoptAddAddressTypeOptions, type).click();
      chkAddMailingAddress.click();
      Thread.sleep(1000);
      ((JavascriptExecutor) driver)
          .executeScript("document.getElementById('C2__C1__QUE_753975C04A0241C1630594').value='"
              + postCode + "';");
      Thread.sleep(2000);
      btnAddFindAddress.click();
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to enter New Address details " + e);
    }
  }

}
