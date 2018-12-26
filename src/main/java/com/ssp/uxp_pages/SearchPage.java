package com.ssp.uxp_pages;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.relevantcodes.extentreports.ExtentTest;
import com.ssp.support.DateTimeUtility;
import com.ssp.support.Log;
import com.ssp.utils.ElementLayer;
import com.ssp.utils.GenericUtils;
import com.ssp.utils.WaitUtils;

/**
 * Search page of the application is implemented in the way to search contacts/policies.<br/>
 * <br/>
 * This SearchPage class holds the web elements of the page and provides the methods to access them.
 * 
 */
public class SearchPage extends LoadableComponent<SearchPage> {
  private final WebDriver driver;
  private ExtentTest extentedReport;
  private boolean isPageLoaded;
  private String btnVerify = "a.btn";
  private String radio_button_value = null;
  public ElementLayer uielement;

  public String radGender = "fieldset[id*='FS_QUE_B5F37DDFDCE76BB21217328'] .radio";
  public String radEmail = "fieldset[id*='FS_QUE_B5F37DDFDCE76BB21217334'] .radio";
  public String radPreferences = "fieldset#C2__C1__FS_QUE_B5F37DDFDCE76BB21217334>div.radio";
  public String enga_Centre_tabs = "ul.nav.nav-tabs>li>a";
  public String Cnt_Table = "div[id*='C2__p4_QUE_9C3BD3DB194DC120259777']";
  public String policyquote = "div>div>div>div.panel-collapse.collapse.in";
  public String policy_details = "table[summary='PoliciesAndQuotes']>tbody>tr";
  public String specific_Cnt = "div[id*='C2__TXT_7849F86F9C90F02E350936']";
  public String brandname_srch = "div[id*='C2__TXT_FFA636BEB6594D41528089']";
  public String specific_policy_Cnt =
      "table.table.table-hover.table-striped[summary='PoliciesAndQuotes'] tbody";
  public String contact_searchRows = "span[id*='C2__QUE_9C3BD3DB194DC120259777_R";
  public String lstofpolicies = "tr[id*='C2__p0_TBL_9C3BD3DB194DC120259818_R']";
  public String lstofpolStatus = "#C2__p0_TBL_9C3BD3DB194DC120259818_R";

  public final String SEARCH_NOPOLICY_MSG =
      "There are no Quotes or Policies associated with this Contact.";

  @FindBy(css = "table[summary='PoliciesAndQuotes'] tbody tr")
  List<WebElement> lstContactTableRows;

  @FindBy(css = "a[data-target*='personal-details']")
  WebElement enga_Centre_tabs_Personal;

  @FindBy(css = "input[placeholder='Policy Number']")
  WebElement txtPolicyNumber;

  @FindBy(css = "#C2__TXT_1A3613CD19FD282C509020>div")
  WebElement erPolicyNotExist;

  @FindBy(css = ".panel-title>a>div")
  WebElement lblSearchResultTitles;

  @FindBy(css = ".panel-title>a>div:nth-child(1)")
  WebElement lblSearchResultTitleContactName;

  @FindBy(css = ".panel-title>a>div:nth-child(2)")
  WebElement lblSearchResultTitleDOB;

  @FindBy(css = ".panel-title>a>div:nth-child(3)")
  WebElement lblSearchResultTitleAddress;

  @FindBy(css = ".panel-title>a>div:nth-child(4)")
  WebElement lblSearchResultTitleBrand;

  @FindBy(css = ".alert-warning .col-md-11>p:not(.errorClass)")
  WebElement er_lnameNotFound;

  @FindBy(css = ".alert-warning .col-md-11>p:not(.errorClass)")
  WebElement er_fnameNotFound;

  @FindBy(css = ".alert-warning p:not(.errorClass):nth-child(1)")
  WebElement er_wrongSearch;

  @FindBy(css = "input[placeholder='External Quote Number']")
  WebElement txtExtQuote;

  @FindBy(css = "button[id*='57179A1244E6618013765']")
  WebElement btnSearch;

  @FindBy(css = "button[title='Complete']")
  WebElement btnComplete;

  @FindBy(css = "input[placeholder='Last Name']")
  WebElement sp_txtLastName;

  @FindBy(css = "input[placeholder='First Name']")
  WebElement sp_txtfirstName;

  @FindBy(css = "input[placeholder='Postcode']")
  WebElement sp_txtPostCode;

  @FindBy(css = "input[class='date-picker']")
  WebElement sp_txtDatePicker;

  @FindBy(css = "tr[id*='TBL_9C3BD3DB194DC120259818_R1']")
  WebElement selectPolicyFromSearchedContact;

  @FindBy(css = "button[title='Clear Search']")
  WebElement btnClearSearch;

  @FindBy(css = "button[id*='57179A1244E6618059413']")
  WebElement btn1CreateCustomer;

  @FindBy(css = "button[id='C2__BUT_075AC4DA2E63D42D841547']")
  WebElement btn2CreateCustomer;

  @FindBy(css = "div[class='search-results']")
  WebElement searchResults;

  @FindBy(css = "div[id*='8E8C16BFDF38773D121961'].panel-heading.open-section")
  WebElement lstContactDetails;

  @FindBy(css = "div[id*='C22B79E730147F6156240']>div>h4.panel-title")
  WebElement lstSearchResults;

  @FindBy(css = "div.panel-collapse")
  WebElement panelCollapse;

  @FindBy(css = "select[name*='PERSONAL'][name*='TITLE']")
  WebElement custTitle;

  @FindBy(css = "input[name*='PERSONAL'][name*='FIRSTNAME']")
  WebElement cn_firstName;

  @FindBy(css = "input[name*='PERSONAL'][name*='LASTNAME']")
  WebElement cn_lastName;

  @FindBy(css = "input[id*='B5F37DDFDCE76BB21217326']")
  WebElement cn_dateofbirth;

  @FindBy(css = "#C2__C1__QUE_B5F37DDFDCE76BB21217316")
  WebElement selecttitle;

  @FindBy(css = "input[name*='PERSONAL'][name*='POSTCODE']")
  WebElement cn_postCode;

  @FindBy(css = "button[id*='B5F37DDFDCE76BB21217366'][title='Find Address']")
  WebElement cn_findAddress;

  @FindBy(css = "select[name*='PERSONAL'][name*='ADDRESSLIST']")
  WebElement cn_AddressList;

  @FindBy(css = "input[name*='PERSONAL'][name*='ADDRESSLINE1']")
  WebElement cn_AddressLine;

  @FindBy(css = "input[name*='PERSONAL'][name*='HOMENUMBER']")
  WebElement cn_HomePhone;

  @FindBy(css = "input[name*='PERSONAL'][name*='EMAILADDRESS']")
  WebElement cn_Email;

  @FindBy(css = "button[id*='SaveBtn_Create_New_Personal_Contact']")
  WebElement cn_SaveCustomer;

  @FindBy(css = "div[id*='C2__p4_QUE_9C3BD3DB194DC120259777']")
  WebElement contact_Table;

  @FindBy(css = "#C2__QUE_9C3BD3DB194DC120259880_R1")
  WebElement PolicyIDtoClick;

  @FindBy(css = "#p1_HDR_TBL_9C3BD3DB194DC120259818_R1")
  WebElement lstPolicyDetails;

  @FindBy(css = "div.main-brand")
  WebElement app_BrandName;

  @FindBy(css = "ul.nav.nav-tabs>li>a")
  WebElement Ele_engagement_Centre_tabs;

  @FindBy(css = "input[id*='57179A1244E6618019347'][placeholder='Date of Birth']")
  WebElement txt_DataofBirth_search;

  @FindBy(css = "input[placeholder='Postcode']")
  WebElement txtPostCode;

  @FindBy(css = "input[placeholder='Business/TradeName']")
  WebElement txtBusiName;

  @FindBy(css = "button[title='Proceed']")
  WebElement but_Proceed;

  @FindBy(css = "#C2__FMT_C47EBF5E7217CB164249167_R1")
  WebElement firstRow;

  @FindBy(css = "div#C2__FMT_1A3613CD19FD282C373423")
  WebElement table_policysearchsingleResult;

  @FindBy(
      css = "table.table.table-hover.table-striped[summary='PoliciesAndQuotes'] tbody td:nth-child(1) span")
  WebElement txt_PolicyNo;

  @FindBy(css = "h2.page-title")
  WebElement txtContactName;

  @FindBy(css = "#C2__TXT_1A3613CD19FD282C509020>div")
  WebElement txt_searchresHeading;

  //@FindBy(css = "#C2__TXT_1A3613CD19FD282C509020>div")
  @FindBy(css = "#C2__p1_HEAD_7C37BC9733C00CA7665728>div")
  WebElement txt_withnopolicies;

  @FindBy(css = "button#C2__BUT_9C3BD3DB194DC120259906[value='Proceed']")
  WebElement btn_proceed;

  @FindBy(css = ".caret-xs [title='Click to Collapse']")
  WebElement btn_clkTocollapse;

  @FindBy(css = "#C2__TXT_7849F86F9C90F02E350936>div")
  WebElement txt_selectedcontName;

  @FindBy(css = "input[name*='CORPORATE[1].BUSINESSNAME']")
  WebElement txtTradeName;

  @FindBy(css = "input[name*='CORPORATE[1].TRADINGAS']")
  WebElement txtTradeAs;

  @FindBy(css = "select[name*='CORPORATE[1].BUSINESSDETAILS[1].BUSINESSADDRESS[1].TYPE")
  WebElement selectType;

  @FindBy(css = "input[name*='CORPORATE[1].BUSINESSDETAILS[1].BUSINESSADDRESS[1].BUILDINGNUMBER']")
  WebElement txtBuldNumber;

  @FindBy(css = "select[name*='CORPORATE[1].BUSINESSDETAILS[1].BUSINESSADDRESS[1].COUNTRY']")
  WebElement selectCountry;

  @FindBy(css = "input[name*='CORPORATE[1].BUSINESSDETAILS[1].BUSINESSADDRESS[1].POSTCODE']")
  WebElement txtPostcode;

  @FindBy(css = "button[id*='BUT_B5F37DDFDCE76BB21217422'][title='Find Address']")
  WebElement btnfindAddress;

  @FindBy(css = "select[name*='CORPORATE[1].BUSINESSDETAILS[1].BUSINESSADDRESS[1].ADDRESSLIST")
  WebElement AddressList;

  @FindBy(css = "input[name*='CORPORATE[1].BUSINESSDETAILS[1].BUSINESTELEPHONE[1].BUSINESS']")
  WebElement txtBTBiz;

  @FindBy(css = "input[name*='CORPORATE[1].BUSINESSDETAILS[1].BUSINESTELEPHONE[1].HOME']")
  WebElement txtBTHome;

  @FindBy(css = "input[name*='CORPORATE[1].BUSINESSDETAILS[1].BUSINESTELEPHONE[1].MOBILE']")
  WebElement txtBTMob;

  @FindBy(css = "input[name*='CORPORATE[1].BUSINESSDETAILS[1].BUSINESSEMAIL[1].EMAIL']")
  WebElement txtBEEmail;

  @FindBy(css = "input[name*='CORPORATE[1].BUSINESSDETAILS[1].BUSINESSEMAIL[1].WEBSITEADDRESS']")
  WebElement txtBEWbsite;

  @FindBy(
      css = "input[name*='CORPORATE[1].AUTHORISEDCONTACTDETAILS[1].AUTHORISEDTELEPHONENUMBERS[1].HOME']")
  WebElement txtAuthphoneHome;

  @FindBy(
      css = "input[name*='CORPORATE[1].AUTHORISEDCONTACTDETAILS[1].AUTHORISEDTELEPHONENUMBERS[1].WORK']")
  WebElement txtAuthphonework;

  @FindBy(
      css = "input[name*='.CORPORATE[1].AUTHORISEDCONTACTDETAILS[1].AUTHORISEDTELEPHONENUMBERS[1].MOBILE']")
  WebElement txtAuthphoneMob;

  @FindBy(
      css = "input[name*='CORPORATE[1].AUTHORISEDCONTACTDETAILS[1].AUTHORISEDTELEPHONENUMBERS[1].EMAIL']")
  WebElement txtAuthphoneEmail;

  @FindBy(css = "select[name*='CORPORATE[1].AUTHORISEDCONTACTDETAILS[1].TITLE']")
  WebElement selectTitle;

  @FindBy(css = "input[name*='CORPORATE[1].AUTHORISEDCONTACTDETAILS[1].FIRSTNAME']")
  WebElement txtFirstName;

  @FindBy(css = "input[name*='CORPORATE[1].AUTHORISEDCONTACTDETAILS[1].LASTNAME']")
  WebElement txtLastName;

  @FindBy(css = "input[name*='CORPORATE[1].AUTHORISEDCONTACTDETAILS[1].MIDDLENAME']")
  WebElement txtMiddleName;

  @FindBy(css = "select[name*='CORPORATE[1].AUTHORISEDCONTACTDETAILS[1].SUFFIX']")
  WebElement selectSuffix;

  @FindBy(css = "label[for='C2__C1__QUE_B5F37DDFDCE76BB21217496_0']")
  WebElement OptMarketing;

  @FindBy(css = "button[id*='SaveBtn_Create_New_Corporate_Contact']")
  WebElement btnSaveCustomer;

  @FindBy(css = "button[id*='SaveBtn_Create_New_Corporate_Contact']")
  WebElement cn_SaveCustomer_Corp;

  @FindBy(css = "a[data-target='#C2__C1__corporate-details']")
  WebElement enga_Centre_tabs_Corporate;

  @FindBy(css = "#C2__BUT_F449F8C15BC15FF9784873")
  WebElement btn_srchcomplete;

  @FindBy(css = "div.main-brand")
  WebElement txtBannerName;

  @FindBy(css = "#C2__TXT_7849F86F9C90F02E350936>div")
  WebElement txtcontactName;

  @FindBy(css = "#C2__C1__QUE_B5F37DDFDCE76BB21217370")
  WebElement txtfldAddress1;

  @FindBy(css = "#C2__C1__QUE_B5F37DDFDCE76BB21217372")
  WebElement txtfldAddress2;

  @FindBy(css = "#C2__C1__QUE_B5F37DDFDCE76BB21217374")
  WebElement txtfldAddress3;

  @FindBy(css = ".panel-title>a>div")
  List<WebElement> lstSearchTitle;

  @FindBy(css = "div[class='search-results']>p")
  WebElement txtSearchResultCount;

  @FindBy(css = "div[id*='Brands']>div >div[class='modal-content']")
  WebElement modalMyBrands;

  @FindBy(css = ".warning-alert.clearfix")
  WebElement txtWarningMsg;

  @FindBy(css = "button[id='C2__C2__BUT_874EFCFEFA062832841943']")
  WebElement mdlBrndCancelBtn;

  @FindBy(css = "span[class=\"status_icon need_attention\"]")
  WebElement iconReview;

  /**
   * Constructs a Search page object.
   * 
   * @param driver
   * @param extentedReport
   */
  public SearchPage(WebDriver driver, ExtentTest extentedReport) {
    this.driver = driver;
    this.extentedReport = extentedReport;
    ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, WaitUtils.maxElementWait);
    PageFactory.initElements(finder, this);
  }

  @Override
  protected void load() {
    isPageLoaded = true;
    WaitUtils.waitForPageLoad(driver);
  }

  @Override
  protected void isLoaded() throws Error {
    if (!isPageLoaded) {
      Assert.fail();
    }

    if (isPageLoaded && !WaitUtils.waitForElement(driver, txtPolicyNumber)) {
      Log.message("Search Page did not open up.", driver, extentedReport);
    }
    uielement = new ElementLayer(driver);
  }

  /**
   * Verifies that the current page is Search page or not.
   * 
   * @return true if it confirms that the current page is Search page; false otherwise.
   * @throws Exception
   */
  public boolean verifySearchPage() throws Exception {
    boolean status = false;
    if (!WaitUtils.waitForElement(driver, txtPolicyNumber, 20)) {
      status = true;
      throw new Exception("Search Page is not Loaded. Timed out finding the element in page.");
    }
    return status;
  }

  /**
   * Verifies that the error message in Search page is as expected or not.
   * 
   * @param textToVerify
   * @return true if the actual error message matches with the expected; false otherwise.
   * @throws Exception
   */
  public boolean verifySearchError(String textToVerify) throws Exception {
    WaitUtils.waitForElement(driver, txtPolicyNumber, 20);
    if (!GenericUtils.verifyWebElementTextContains(er_wrongSearch, textToVerify)) {
      return false;
    }
    return true;
  }

  /**
   * Verifies that the policy error message is as expected or not.
   * 
   * @param textToVerify
   * @return true if the actual policy error message matches with the expected; false otherwise.
   * @throws Exception
   */
  public boolean verifyErPolicyNotFound(String textToVerify) throws Exception {
    WaitUtils.waitForElement(driver, lblSearchResultTitles, 20);
    if (!GenericUtils.verifyWebElementTextContains(erPolicyNotExist, textToVerify)) {
      return false;
    }
    return true;
  }

  /**
   * Verifies that the last name error message is as expected or not.
   * 
   * @param textToVerify
   * @return true if the actual last name error message matches with the expected; false otherwise.
   * @throws Exception
   */
  public boolean verifyLnameError(String textToVerify) throws Exception {
    WaitUtils.waitForElement(driver, txtPolicyNumber, 20);
    if (!GenericUtils.verifyWebElementTextContains(er_lnameNotFound, textToVerify)) {
      return false;
    }
    return true;
  }

  /**
   * Writes the given date in the 'Date of Birth' input field.
   * 
   * @param cn_dateOfBirth
   * @param extentedReport
   * @throws Exception
   */
  public void enterDateOfBirth(String cn_dateOfBirth, ExtentTest extentedReport) throws Exception {
    try {
      txt_DataofBirth_search.sendKeys(cn_dateOfBirth);
      txt_DataofBirth_search.click();
      Log.message("Entered the Date of Birth: " + cn_dateOfBirth, extentedReport);
    } catch (Exception e) {
      throw new Exception("Date of Birth is not entered. " + e);
    }
  }

  /**
   * Writes the given postcode in the 'Postcode' input field.
   * 
   * @param postCode
   * @param extendReport
   */
  public void enterPostCode(String postCode, ExtentTest extendReport) {
    txtPostCode.clear();
    txtPostCode.sendKeys(postCode);
    Log.message("Entered the Policy Number: " + postCode, extendReport);
  }

  /**
   * Clears the search through 'Clear Search' button.
   * 
   * @param extentedReport
   */
  public void clearSearch(ExtentTest extentedReport) {
    JavascriptExecutor executor = (JavascriptExecutor) driver;
    executor.executeScript("arguments[0].click();", btnClearSearch);
    Log.message("Clicked on Clear button", extentedReport);
  }

  /**
   * Writes the business name in the 'Business/Trade Name' input field.
   * 
   * @param busiName
   * @param extendReport
   */
  public void enterBusinessName(String busiName, ExtentTest extendReport) {
    txtBusiName.clear();
    txtBusiName.sendKeys(busiName);
    Log.message("Entered the Policy Number: " + busiName, extendReport);
  }

  /**
   * Verifies that the last name error message is as expected or not.
   * 
   * @param textToVerify
   * @return true if the actual first name error message matches with the expected; false otherwise.
   * @throws Exception
   */
  public boolean verifyFnameError(String textToVerify) throws Exception {
    WaitUtils.waitForElement(driver, txtPolicyNumber, 20);
    if (!GenericUtils.verifyWebElementTextContains(er_fnameNotFound, textToVerify)) {
      return false;
    }
    return true;
  }

  /**
   * Returns the value of 'Last Name' field.
   * 
   * @param extentedReport
   * @return the string representation of the last name value, or null if no string representation
   *         is applicable.
   * @throws Exception
   */
  public String getValueFromLname(ExtentTest extentedReport) throws Exception {
    WaitUtils.waitForElement(driver, sp_txtLastName, 20);

    String textValue = sp_txtLastName.getAttribute("value");
    Log.message("Getting value from LastName textbox", driver, extentedReport);
    return textValue;
  }

  /**
   * Returns the value of 'First Name' field.
   * 
   * @param extentedReport
   * @return the string representation of the last name value, or null if no string representation
   *         is applicable.
   * @throws Exception
   */
  public String getValueFromFname(ExtentTest extentedReport) throws Exception {
    WaitUtils.waitForElement(driver, sp_txtfirstName, 20);
    String textValue = sp_txtfirstName.getAttribute("value");
    Log.message("Getting value from FirstName textbox", driver, extentedReport);
    return textValue;
  }

  /**
   * Writes the policy number in the 'Policy Number' input field.
   * 
   * @param policynumber
   * @param extentedReport
   * @param screenshot
   * @throws Exception
   */
  public void enterPolicyNumber(String policynumber, ExtentTest extentedReport, boolean screenshot)
      throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, 180, txtPolicyNumber,
          "Timed out to find policy number text box.");
      txtPolicyNumber.clear();
      txtPolicyNumber.sendKeys(policynumber);
      WaitUtils.waitForSpinner(driver);
      Log.message("Entered Policy Number: " + policynumber.toString(), driver, extentedReport,
          screenshot);
    } catch (Exception e) {
      throw new Exception("Policy Number field is not displayed. " + e);
    }
  }

  /**
   * Writes the external quote in the 'External Quote Number' input field.
   * 
   * @param externalQuote
   * @param extendReport
   * 
   */
  public void enterExternalQuote(String externalQuote, ExtentTest extendReport) {
    txtExtQuote.clear();
    txtExtQuote.sendKeys(externalQuote);
    Log.message("Entered the Policy Number: " + externalQuote, extendReport);
  }

  /**
   * Performs the click action on Search button.
   * 
   * @param extentedReport
   */
  public void clickSearch(ExtentTest extentedReport) {
    WaitUtils.waitForElementPresent(driver, 180, txtPolicyNumber,
        "Timed out to find search button on search page.");
    btnSearch.click();
    WaitUtils.waitForSpinner(driver);
    Log.message("Clicked on Search button", driver, extentedReport, true);
  }

  /**
   * Performs the click action on Complete button.
   * 
   * @param extentedReport
   * @throws Exception
   */
  public void clickComplete(ExtentTest extentedReport) throws Exception {
    try {
      if (WaitUtils.waitForElement(driver, btnComplete)) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", btnComplete);
        WaitUtils.waitForSpinner(driver);
        Log.message("Clicked on Complete button", driver, extentedReport);
      } else {
        throw new Exception("Complete button is not found");
      }
    } catch (Exception e) {
      throw new Exception("Exception while clicking the complete button" + e);
    }
  }

  /**
   * Performs a search operation with the valid policy number.
   * 
   * @param policynumber
   * @param screenShot
   * @param extentedReport
   * @throws Exception
   */
  public void searchValidPolicy(String policynumber, boolean screenShot, ExtentTest extentedReport)
      throws Exception {
    Log.message("Search for a Valid policy Number", extentedReport);
    enterPolicyNumber(policynumber, extentedReport, screenShot);
    clickSearch(extentedReport);
    WaitUtils.waitForPageLoad(driver);
    WaitUtils.waitForSpinner(driver);
  }

  /**
   * Performs a search operation with the valid policy number
   * 
   * @param policynumber
   * @param screenShot
   * @param extentedReport
   * @throws Exception
   */
  public void searchValidLastname(String lastname, boolean screenShot, ExtentTest extentedReport)
      throws Exception {
    Log.message("Search for a Valid policy Number", extentedReport);
    enterLastName(lastname, extentedReport);
    clickSearch(extentedReport);
    WaitUtils.waitForPageLoad(driver);
    WaitUtils.waitForSpinner(driver);

  }

  /**
   * Writes the last name in the 'Last Name' input field.
   * 
   * @param sp_lastname
   * @param extendReport
   * @throws Exception
   */
  public void enterLastName(String sp_lastname, ExtentTest extendReport) throws Exception {
    try {
      sp_txtLastName.clear();
      sp_txtLastName.sendKeys(sp_lastname);
      Log.message("Entered the Last Name : " + sp_lastname, driver, extendReport);
    } catch (Exception e) {
      throw new Exception("Last Name is not entered. " + e);
    }
  }
  
  

  /**
   * Writes the first name in the 'First Name' input field.
   * 
   * @param sp_lastname
   * @param extendReport
   * @throws Exception
   */
  public void enterFirstName(String sp_lastname, ExtentTest extendReport) throws Exception {
    try {
      sp_txtfirstName.clear();
      sp_txtfirstName.sendKeys(sp_lastname);
      Log.message("Entered the First Name : " + sp_lastname, driver, extendReport);
    } catch (Exception e) {
      throw new Exception("First Name is not entered. " + e);
    }
  }

  /**
   * Selects the expected contact if it is available in search results.
   * 
   * @param nameToSelect
   * @param screenShot
   * @param extentedReport
   * @return the <code>CustDashboardPage</code> object, or null if no <code>CustDashboardPage</code>
   *         representation is applicable
   * @throws Exception
   */
  public CustDashboardPage selectValidContact(String nameToSelect, boolean screenShot,
      ExtentTest extentedReport) throws Exception {
    CustDashboardPage objCustDashboardPage = null;
    Log.message("Selecting valid Contact", extentedReport);
    try {
      WaitUtils.waitForElementPresent(driver, 180, contact_Table,
          "Timed out to get search results");
      WebElement RowVal = driver.findElement((By.cssSelector(specific_Cnt)));
      boolean isFound = RowVal.getText().toString().contains(nameToSelect);
      if (isFound) {
        Log.message("Search results have the contact with name '" + nameToSelect + "'.",
            extentedReport);
        RowVal.click();
        WaitUtils.waitForSpinner(driver);
        if (driver.findElement(By.cssSelector(policyquote)).isDisplayed()) {
          String strText_Policy = driver.findElement(By.cssSelector(policy_details)).getText();
          driver.findElement(By.cssSelector(policy_details)).click();
          Log.message("Selected Policy Details : " + strText_Policy, extentedReport);
          WaitUtils.waitForSpinner(driver);
          Log.message("Sucessfully searched policy through quick search", extentedReport);
          objCustDashboardPage = new CustDashboardPage(driver, extentedReport).get();
        } else {
          throw new Exception("Search results does not have policy");
        }
      } else {
        throw new Exception(
            "Search results does not have the contact with name '" + nameToSelect + "'.");
      }
    } catch (Exception e) {
      throw new Exception("Unable to Select Valid Contact. " + e);
    }
    return objCustDashboardPage;
  }

  /**
   * To verify whether the Search Title is Present or not
   * 
   * @param extentedReport
   * @return boolean
   * @throws Exception
   * 
   */

  public boolean isSearchTitleAvaialble(ExtentTest extentedReport) {
    boolean result = false;
    result = (lblSearchResultTitles.isDisplayed()) ? true : false;
    if (result) {
      if (lblSearchResultTitleContactName.getText().contains("Contact Name")
          && lblSearchResultTitleDOB.getText().contains("Date of Birth")
          && lblSearchResultTitleAddress.getText().contains("Address")
          && lblSearchResultTitleBrand.getText().contains("Brand")) {
        Log.message("Title available : Contact Name, Date of Birth, Address, Brand",
            extentedReport);
        result = true;
      } else {
        Log.message(
            "One or more of title not available : Contact Name, Date of Birth, Address, Brand",
            extentedReport);
        result = false;
      }
    } else {
      result = false;
      Log.message("Search Result Title not found", extentedReport);
    }
    return result;
  }

  /**
   * Selects the contact and move to customer Dash board page.
   * 
   * @param nameToSelect
   * @param screenShot
   * @param extentedReport
   * @return the <code>CustDashboardPage</code> object, or null if no <code>CustDashboardPage</code>
   *         representation is applicable
   * @throws Exception
   * 
   */
  public CustDashboardPage selectContactAndMoveToCustDashboard(String NameToSelect,
      boolean screenShot, ExtentTest extentedReport) throws Exception {

    Log.message("Selecting contact");
    try {
      boolean boolVal = false;
      WebElement eleToClk = null;
      (new WebDriverWait(driver, 40).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to get search results"))
              .until(ExpectedConditions.visibilityOf(contact_Table));
      if (contact_Table.isDisplayed()) {
        List<WebElement> ContactRows = driver.findElements(By.cssSelector(Cnt_Table));
        for (int i = 0; i < ContactRows.size(); i++) {

          String specific_Cnt = "div#C2__p4_QUE_9C3BD3DB194DC120259777_R" + (i + 1) + ">div>span";
          WebElement RowVal = driver.findElement((By.cssSelector(specific_Cnt)));
          if (i == 0)
            eleToClk = RowVal;
          boolVal = RowVal.getText().toString().contains(NameToSelect);

        }
        if (boolVal) {
          Log.message("Search page has the expected search result", extentedReport);
          eleToClk.click();
          WaitUtils.waitForSpinner(driver);
          Log.message("Selected an existing contact", extentedReport);
          if (selectPolicyFromSearchedContact.isDisplayed()) {
            selectPolicyFromSearchedContact.click();
            WaitUtils.waitForSpinner(driver);
            Log.message("Select a policy of existing contact", driver, extentedReport, true);
          } else if (but_Proceed.isDisplayed()) {
            but_Proceed.sendKeys(Keys.ENTER);

            WaitUtils.waitForSpinner(driver);
            Log.message("Clicked on Proceed button", driver, extentedReport, true);
          } else {
            throw new Exception("Not able to select/create policy");
          }
        }
      }
      return new CustDashboardPage(driver, extentedReport).get();
    } catch (Exception e) {
      throw new Exception("Unable to Select Valid Contact/Policy" + e);
    }

  }

  /**
   * Selects the Policy and move to customer dash board page.
   * 
   * @param nameToSelect
   * @param screenShot
   * @param extentedReport
   * @return the <code>CustDashboardPage</code> object, or null if no <code>CustDashboardPage</code>
   *         representation is applicable
   * @throws Exception
   */

  public CustDashboardPage selectPolicyAndMoveToCustDashboard(String NameToSelect,
      boolean screenShot, ExtentTest extentedReport) throws Exception {
    Log.message("Selecting contact");
    try {
      WaitUtils.waitForSpinner(driver);
      (new WebDriverWait(driver, 40).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to get search results"))
              .until(ExpectedConditions.visibilityOf(selectPolicyFromSearchedContact));
      if (selectPolicyFromSearchedContact.isDisplayed()) {
        System.out.println("Policy Available");
        selectPolicyFromSearchedContact.click();
        WaitUtils.waitForSpinner(driver);
        WaitUtils.waitForPageLoad(driver);
        Log.message("Select a policy of existing contact", driver, extentedReport, true);
      } else if (but_Proceed.isDisplayed()) {
        System.out.println("Proceed button available");
        but_Proceed.sendKeys(Keys.ENTER);

        WaitUtils.waitForSpinner(driver);
        WaitUtils.waitForPageLoad(driver);
        Log.message("Clicked on Proceed button", driver, extentedReport, true);
      } else {
        Log.message("Given Policy not identified", driver, extentedReport, true);
      }
      return new CustDashboardPage(driver, extentedReport).get();
    } catch (Exception e) {
      throw new Exception("Unable to Select Valid Contact/Policy" + e);
    }

  }

  /**
   * Click create customer in customer dash board page.
   * 
   * @param screenShot
   * @param extentedReport
   * @throws Exception
   */

  public void clickCreateCustomer(boolean screenShot, ExtentTest extendReport) throws Exception {
    boolean status = false;
    try {
      btn2CreateCustomer.click();
      status = true;
    } catch (Exception e) {
      btn1CreateCustomer.click();
      status = true;
    }
    if (status) {
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Create Customer Button from Search page", extendReport);
    } else {
      throw new Exception("Not able to click Create Customer Button");
    }

  }

  /**
   * Writes the given customer details in the 'Create New Customer' form.<br/>
   * <br/>
   * The vararg '<i>shouldForceRandomNameCreation</i>' of the method is used to tell whether the
   * random name (both first name and last name) should be entered in form despite the name value
   * given in <i>testData<i/>.
   * 
   * @param testData
   * @param screenShot
   * @param extendReport
   * @param shouldForceRandomNameCreation
   * @return the <code>HashMap</code> representation of testData, or null if no <code>HashMap</code>
   *         representation is applicable
   * @throws Exception
   */
  public HashMap<String, String> enterCustomerDetails(HashMap<String, String> testData,
      boolean screenShot, ExtentTest extendReport, boolean... shouldForceRandomNameCreation)
      throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, Ele_engagement_Centre_tabs,
          "Unable to find Perosnal/corporate contact tabs");

      if (app_BrandName.getText().equalsIgnoreCase("Engagement Centre")) {
        WaitUtils.waitForElementPresent(driver, enga_Centre_tabs_Personal,
            "Unable to find Personal contact tab");
        enga_Centre_tabs_Personal.click();
        Thread.sleep(2000);
      }

      boolean forceRandomValueFlag =
          shouldForceRandomNameCreation.length > 0 ? shouldForceRandomNameCreation[0] : false;
      String title = testData.get("Title").toString();
      String firstName = testData.get("First Name").toString();
      String lastName = testData.get("Last Name").toString();
      String dob = testData.get("Date of Birth").toString();
      String gender = testData.get("Gender").toString();
      String postcode = testData.get("Post Code").toString();
      String homephone = testData.get("Home Phone").toString();
      String email = testData.get("Email").toString();
      String cor_pref_email = testData.get("Cor_Pref_email").toString();

      Log.message("Entering Customer details");

      // selectTitle(title);
      selectTitle(title);
      WaitUtils.waitForSpinner(driver);
      Log.message("Title : " + title);

      if (forceRandomValueFlag || firstName.equals(""))
        firstName = GenericUtils.getRandomCharacters("alpha", 5);

      entercn_firstName(firstName);
      testData.put("First Name", firstName);
      Log.message("First Name : " + firstName, extendReport);

      if (forceRandomValueFlag || lastName.equals(""))
        lastName = GenericUtils.getRandomCharacters("alpha", 4);

      entercn_LastName(lastName);
      testData.put("Last Name", lastName);
      Log.message("Last Name : " + lastName, extendReport);

      entercn_DateOfBirth(dob);
      Log.message("Date of Birth : " + dob, extendReport);
      
      selectGender(gender);
      Log.message("Gender : " + gender, extendReport);
      
   selectPreferenceEmail(cor_pref_email);
   WaitUtils.waitForSpinner(driver);
      Log.message("Corrospondence preference : " + cor_pref_email, extendReport);

      entercn_PostCode(postcode);
      Log.message("Post Code : " + postcode, extendReport);


      selectAddress(extendReport); // this feature of selecting the
                                   // address is changed
      // To handle an AJAX request which is processed after the selection
      // of 'Find
      // Address'

      testData.put("Address1", getAddress1());
      testData.put("Address2", getAddress2());
      testData.put("Address3", getAddress3());
      testData.put("Postcode", postcode);

      entercn_HomePhone(homephone);
      Log.message("Home Phone : " + homephone, extendReport);

      if (!email.contains("@"))
        email = testData.get("Email") + DateTimeUtility.getCurrentDateAndTime() + "EC@test.com";

      cn_Email.clear();
      cn_Email.sendKeys(email);

      Log.message("Email : " + email, extendReport);
      testData.put("Email", email);

      // selectRadioButton(radPreferences, "Email");

      Log.message("Sucessfully entered the Customer details", driver, extendReport, screenShot);
      

    } catch (Exception e) {
      throw new Exception("Exception entering the customer details. " + e);
    }
    return testData;
  }



  /**
   * Writes the given customer details in the 'Create New Customer' form.<br/>
   * <br/>
   * The vararg '<i>shouldForceRandomNameCreation</i>' of the method is used to tell whether the
   * random name (both first name and last name) should be entered in form despite the name value
   * given in <i>testData<i/>.
   * 
   * @param testData
   * @param screenShot
   * @param extendReport
   * @param shouldForceRandomNameCreation
   * @return the <code>HashMap</code> representation of testData, or null if no <code>HashMap</code>
   *         representation is applicable
   * @throws Exception
   */
  /*
   * public HashMap<String, String> enterJointCustomerDetails(HashMap<String, String> testData,
   * boolean screenShot, ExtentTest extendReport, boolean... shouldForceRandomNameCreation) throws
   * Exception { try { WaitUtils.waitForElementPresent(driver, Ele_engagement_Centre_tabs,
   * "Unable to find Perosnal/corporate contact tabs");
   * 
   * if (app_BrandName.getText().equalsIgnoreCase("Engagement Centre")) {
   * WaitUtils.waitForElementPresent(driver, enga_Centre_tabs_Personal,
   * "Unable to find Personal contact tab"); enga_Centre_tabs_Personal.click(); Thread.sleep(2000);
   * }
   * 
   * boolean forceRandomValueFlag = shouldForceRandomNameCreation.length > 0 ?
   * shouldForceRandomNameCreation[0] : false; String title1 = testData.get("Title").toString();
   * String firstName1 = testData.get("First Name").toString(); String lastName1 =
   * testData.get("Last Name").toString(); String dob1 = testData.get("Date of Birth").toString();
   * String gender1 = testData.get("Gender").toString(); String postcode1 =
   * testData.get("Post Code").toString(); String homephone1 =
   * testData.get("Home Phone").toString(); String email1 = testData.get("Email").toString();
   * 
   * Log.message("Entering Customer details"); selectTitle(title1); Log.message("Title : " +
   * title1);
   * 
   * if (forceRandomValueFlag || firstName1.equals("")) firstName1 =
   * GenericUtils.getRandomCharacters("alpha", 5);
   * 
   * entercn_firstName(firstName1); testData.put("First Name", firstName1);
   * Log.message("First Name : " + firstName1, extendReport);
   * 
   * if (forceRandomValueFlag || lastName1.equals("")) lastName1 =
   * GenericUtils.getRandomCharacters("alpha", 4);
   * 
   * entercn_LastName(lastName1); testData.put("Last Name", lastName1); Log.message("Last Name : " +
   * lastName1, extendReport);
   * 
   * entercn_DateOfBirth(dob1); Log.message("Date of Birth : " + dob1, extendReport);
   * 
   * selectGender(gender1); Log.message("Gender : " + gender1, extendReport);
   * 
   * entercn_PostCode(postcode1); Log.message("Post Code : " + postcode1, extendReport);
   * 
   * selectAddress(extendReport); // this feature of selectingf the address is changed // To handle
   * an AJAX request which is processed after the selection of 'Find // Address'
   * 
   * testData.put("Address1", getAddress1()); testData.put("Address2", getAddress2());
   * testData.put("Address3", getAddress3()); testData.put("Postcode", postcode1);
   * 
   * entercn_HomePhone(homephone1); Log.message("Home Phone : " + homephone1, extendReport);
   * 
   * if (!email1.contains("@")) email1 = testData.get("Email") +
   * DateTimeUtility.getCurrentDateAndTime() + "SIAAS@ectest.com";
   * 
   * cn_Email.clear(); cn_Email.sendKeys(email1);
   * 
   * Log.message("Email : " + email1, extendReport); testData.put("Email", email1);
   * 
   * selectRadioButton(radPreferences, "Email");
   * 
   * Log.message("Sucessfully entered the Customer details", driver, extendReport, screenShot);
   * 
   * } catch (Exception e) { throw new Exception("Exception entering the customer details. " + e); }
   * return testData; }
   */

  public ArrayList<String> enterJointCustomerDetails(HashMap<String, String> testData,
      boolean screenShot, ExtentTest extendReport, boolean... shouldForceRandomNameCreation)
      throws Exception {
    ArrayList<String> names = new ArrayList<String>();
    try {
      WaitUtils.waitForElementPresent(driver, Ele_engagement_Centre_tabs,
          "Unable to find Perosnal/corporate contact tabs");

      if (app_BrandName.getText().equalsIgnoreCase("Engagement Centre")) {
        WaitUtils.waitForElementPresent(driver, enga_Centre_tabs_Personal,
            "Unable to find Personal contact tab");
        enga_Centre_tabs_Personal.click();
        Thread.sleep(2000);
      }

      boolean forceRandomValueFlag =
          shouldForceRandomNameCreation.length > 0 ? shouldForceRandomNameCreation[0] : false;
      String title = testData.get("Title").toString();
      String firstName = testData.get("First Name").toString();
      String lastName = testData.get("Last Name").toString();
      String dob = testData.get("Date of Birth").toString();
      String gender = testData.get("Gender").toString();
      String postcode = testData.get("Post Code").toString();
      String homephone = testData.get("Home Phone").toString();
      String email = testData.get("Email").toString();

      Log.message("Entering Customer details");
      selectTitle(title);
      Log.message("Title : " + title, extendReport);

      if (forceRandomValueFlag || firstName.equals(""))
        firstName = GenericUtils.getRandomCharacters("alpha", 5);

      entercn_firstName(firstName);
      testData.put("First Name", firstName);
      Log.message("First Name : " + firstName, extendReport);
      names.add(firstName);

      if (forceRandomValueFlag || lastName.equals(""))
        lastName = GenericUtils.getRandomCharacters("alpha", 4);

      entercn_LastName(lastName);
      testData.put("Last Name", lastName);
      Log.message("Last Name : " + lastName, extendReport);
      names.add(lastName);

      entercn_DateOfBirth(dob);
      Log.message("Date of Birth : " + dob, extendReport);

      selectGender(gender);
      Log.message("Gender : " + gender, extendReport);

      entercn_PostCode(postcode);
      Log.message("Post Code : " + postcode, extendReport);

      selectAddress(extendReport); // this feature of selectingf the address is changed
      // To handle an AJAX request which is processed after the selection of 'Find
      // Address'

      testData.put("Address1", getAddress1());
      testData.put("Address2", getAddress2());
      testData.put("Address3", getAddress3());
      testData.put("Postcode", postcode);

      entercn_HomePhone(homephone);
      Log.message("Home Phone : " + homephone, extendReport);

      if (!email.contains("@"))
        email =
            testData.get("Email") + DateTimeUtility.getCurrentDateAndTime() + "SIAAS@ectest.com";

      cn_Email.clear();
      cn_Email.sendKeys(email);

      Log.message("Email : " + email, extendReport);
      testData.put("Email", email);

      selectRadioButton(radPreferences, "Email");

      Log.message("Sucessfully entered the Customer details", driver, extendReport, screenShot);

    } catch (Exception e) {
      throw new Exception("Exception entering the customer details. " + e);
    }
    return names;
  }


  /**
   * Selects the Title.
   * 
   * @param title
   * @throws Exception
   * 
   */

  public void selectTitle(String title) throws Exception {
    try {
      WaitUtils.waitForelementToBeClickable(driver, selecttitle, "Title field is not present");
      /*
       * JavascriptExecutor js = (JavascriptExecutor) driver;
       * js.executeScript("arguments[0].click();", selecttitle);
       */
      Select titledrpDown = new Select(selecttitle);
      titledrpDown.selectByVisibleText(title);
      Log.message("Title Selected: " + title);
      WaitUtils.waitForSpinner(driver);
    }

    catch (Exception e) {
      throw new Exception("Title is not selected" + e);
    }
  }

  /**
   * To enter customer First Name.
   * 
   * @param cn_firstname
   * @throws Exception
   * 
   */

  public void entercn_firstName(String cn_firstname) throws Exception {
    try {
      cn_firstName.sendKeys(cn_firstname);
      Log.message("Entered the First Name: " + cn_firstname);
    } catch (Exception e) {
      throw new Exception("First Name is not entered" + e);
    }

  }

  /**
   * To enter Customer Last Name.
   * 
   * @param cn_lastname
   * @throws Exception
   */

  public void entercn_LastName(String cn_lastname) throws Exception {
    try {
      cn_lastName.clear();
      cn_lastName.sendKeys(cn_lastname);
      Log.message("Entered the Last Name: " + cn_lastname);
    } catch (Exception e) {
      throw new Exception("Last Name is not entered" + e);
    }
  }

  /**
   * To enter customer Date Of Birth.
   * 
   * @param cn_dateOfBirth
   * @throws Exception
   */

  public void entercn_DateOfBirth(String cn_dateOfBirth) throws Exception {
    try {
      cn_dateofbirth.clear();
      cn_dateofbirth.sendKeys(cn_dateOfBirth);
      cn_dateofbirth.click();
      Log.message("Entered the Date of Birth: " + cn_dateOfBirth);
    }

    catch (Exception e) {
      throw new Exception("Date of Birth is not entered" + e);
    }
  }

  /**
   * Selects the customer gender.
   * 
   * @param gender
   * @throws Exception
   * 
   */

  public void selectGender(String gender) throws Exception {

    try {
      selectRadioButton(radGender, gender);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Log.message("Selected Gender :" + gender);
  }
  
  /**
   * Selects the customer corrospondance Email
   * 
   * @param email
   * @throws Exception
   * 
   */

  public void selectPreferenceEmail(String email) throws Exception {

    try {
      selectRadioButton(radEmail, email);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Log.message("Selected Preference :" + email);
  }

  /**
   * Selects the Customer Post Code.
   * 
   * @param cn_postcode
   * @throws Exception
   */

  public void entercn_PostCode(String cn_postcode) throws Exception {
    try {
      cn_postCode.clear();
      WaitUtils.waitForElement(driver, cn_postCode);
      cn_postCode.click();
      cn_postCode.sendKeys(cn_postcode);
      Log.message("Entered the PostCode: " + cn_postcode);
      WaitUtils.waitForSpinner(driver);
      cn_findAddress.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Find Address");
    } catch (Exception e) {
      throw new Exception("Post Code is not entered" + e);
    }
  }

  /**
   * Selects the Customer Address.
   *
   * @throws Exception
   * 
   */

  public void selectAddress(ExtentTest extentedReport) throws Exception {
    try {
      if (WaitUtils.waitForElement(driver, cn_AddressList, 2)) {
        (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
            .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
            .withMessage("Address not Listed"))
                .until(ExpectedConditions.visibilityOf(cn_AddressList));
        Select cn_Address = new Select(cn_AddressList);
        cn_Address.selectByIndex(0);
        Log.message("Selected the Address", driver, extentedReport);
      }
    }

    catch (Exception e) {
      throw new Exception("Unable to Select Address " + e);
    }
  }

  /**
   * To enter the customer Home phone field.
   * 
   * @param cn_homephone
   * @throws Exception
   */

  public void entercn_HomePhone(String cn_homephone) throws Exception {
    try {
      cn_HomePhone.clear();
      cn_HomePhone.sendKeys(cn_homephone);
      Log.message("Phone Number is entered " + cn_homephone);
    }

    catch (Exception e) {
      throw new Exception("Phone number is not entered" + e);
    }
  }

  /**
   * To enter the customer Email ID.
   * 
   * @param cn_email
   * @throws Exception
   * 
   */

  public void entercn_Email(String cn_email) throws Exception {
    try {
      cn_Email.clear();
      SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
      Date date = new Date();
      String time = sdf.format(date);
      cn_email = cn_email + time + "@ecTest.com";
      WaitUtils.waitForElement(driver, cn_Email);
      cn_Email.sendKeys(cn_email);
      Log.message("Entered Email ID: " + cn_email);
    }

    catch (Exception e) {
      throw new Exception("Email ID is not entered" + e);
    }
  }

  /**
   * Selects the radio button for verifying customer gender.
   * 
   * @param locator
   * @param option
   * @throws Exception
   * 
   */

  private void selectRadioButton(String locator, String option) throws Exception {
    try {
      List<WebElement> buttonOptions = driver.findElements(By.cssSelector(locator));

      for (int i = 0; i < buttonOptions.size(); i++) {
        radio_button_value = buttonOptions.get(i).getText();
        if (radio_button_value.equals(option)) {
          buttonOptions.get(i).findElement(By.cssSelector("input")).click();
          radio_button_value = null;
          break;
        }
      }
    } catch (Exception e) {
      throw new Exception("Error in Selecting Gender" + e);
    }
  }

  /**
   * To verify Search Results.
   * 
   * @throws Exception
   * @return CustDashboardPage
   * 
   */
  public CustDashboardPage verifySearchResults(ExtentTest extentedReport) throws Exception {
    WaitUtils.waitForSpinner(driver);
    try {
      List<WebElement> buttonOptions = driver.findElements(By.cssSelector(btnVerify));
      for (int i = 0; i < buttonOptions.size(); i++) {
        String button_value = buttonOptions.get(i).getText();
        if (button_value.equals("Verify")) {
          buttonOptions.get(i).click();

          button_value = null;
          break;
        }
      }
      return new CustDashboardPage(driver, extentedReport).get();
    } catch (Exception e) {
      throw new Exception("Error while navigating to Customer Dashboard Page" + e);
    }

  }

  /**
   * To confirm the create customer.
   * 
   * @return CustDashboardPage
   * @param screenShot
   * @param extentedReport
   * @throws Exception
   * 
   */

  public CustDashboardPage confirmCreateCustomer(boolean screenShot, ExtentTest extentedReport)
      throws Exception {
    WaitUtils.waitForSpinner(driver);
    try {
      cn_SaveCustomer.click();
      Log.message("Clicked on Save customer", driver, extentedReport, screenShot);
      WaitUtils.waitForSpinner(driver);
      return new CustDashboardPage(driver, extentedReport).get();
    } catch (Exception e) {
      try {
        if (driver.findElement(By.cssSelector("span.qlrError.ErrorColor")).isDisplayed()) {
          throw new Exception("Mandatory Details are not Entered");
        }
      } catch (Exception f) {
        throw new Exception("Error occured while creating customer" + f);
      }

      throw new Exception("Not Navigated to Customer Dashboard Page" + e);
    }

  }

  /**
   * To Select Policy.
   * 
   * @param screenShot
   * @param extentedReport
   * @throws Exception
   * 
   */

  public void selectPolicy(ExtentTest extentedReport, boolean screenShot) throws Exception {

    try {
      WaitUtils.waitForSpinner(driver);
      (new WebDriverWait(driver, 20).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to get search results"))
              .until(ExpectedConditions.visibilityOf(lstSearchResults));
      if (lstSearchResults.isDisplayed() && lstPolicyDetails.isDisplayed()) {
        PolicyIDtoClick.click();
        WaitUtils.waitForSpinner(driver);
        Log.message("Selected Valid Contact with First Name :" + lstContactDetails.getText());
        Log.message("Selected Policy is :", driver, extentedReport, screenShot);
        WaitUtils.waitForSpinner(driver);
      }
    }

    catch (Exception e) {
      throw new Exception("Unable to Select Policy" + e);
    }
  }

  /**
   * To search the customer by brand name.
   * 
   * @param NameToSelect
   * @param screenShot
   * @param extentedReport
   * @throws Exception
   * 
   */

  public void searchbybrandName(String NameToSelect, boolean screenShot, ExtentTest extentedReport)
      throws Exception {

    try {

      WaitUtils.waitForSpinner(driver);
      int count = 0;
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to get search results"))
              .until(ExpectedConditions.visibilityOf(contact_Table));
      if (contact_Table.isDisplayed()) {
        List<WebElement> brandnameRows = driver.findElements(By.cssSelector(brandname_srch));
        if (!NameToSelect.equals("AllBrands")) {
          for (int i = 0; i < brandnameRows.size(); i++) {

            if (brandnameRows.get(i).getText().equals(NameToSelect)) {
              Log.message(NameToSelect + " contact");
              count++;
            }
          }
          if (count == brandnameRows.size()) {

            Log.message("Sucessfully searched " + NameToSelect + " contacts ", driver,
                extentedReport, screenShot);

          } else {
            Log.fail("Not relevant search", driver, extentedReport, true);
          }

        } else {

          Log.message("Sucessfully searched " + NameToSelect + " contacts ", driver, extentedReport,
              screenShot);
        }

      }
    } catch (Exception e) {
      throw new Exception("Unable to Select Valid Contact" + e);
    }

  }

  /**
   * 
   * Description : After Search, select the policy to open it's detail page
   * 
   * @param policyNumber - Policy Number
   * @param extentedReport
   * @param screenShot
   * 
   * @throws Exception as custom Exception Message
   * 
   */
  public void selectPolicyId(String policyNumber, ExtentTest extentedReport, boolean screenShot)
      throws Exception {
    try {
      // (new WebDriverWait(driver, 20).pollingEvery(200, TimeUnit.MILLISECONDS)
      // .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
      // .withMessage("Unable to get search results"))
      // .until(ExpectedConditions.visibilityOf(lstSearchResults));

      if (WaitUtils.waitForElement(driver, PolicyIDtoClick)) {
        if (PolicyIDtoClick.getText().equalsIgnoreCase(policyNumber)) {
          PolicyIDtoClick.click();
          Log.message("Selected Valid Contact with First Name :" + lstContactDetails.getText());
          Log.message("Selected Policy ID : " + policyNumber, driver, extentedReport, screenShot);
          WaitUtils.waitForSpinner(driver);
        } else {
          Log.message("Policy ID searching is not available", extentedReport);
        }
      } else {
        Log.message("No policies are displayed", extentedReport);
      }
    }

    catch (Exception e) {
      throw new Exception("Unable to Select Policy" + e);
    }
  }

  /**
   * 
   * Description : Search any policy number in search page, and Select the policy to open policy
   * details page
   * 
   * 
   * @param policyNumber - Policy number, wants to search
   * @param extentedReport
   * @param screenShot
   * @throws Exception as custom Exception Message
   * 
   */
  public void searchAndSelectPolicyId(String policyNumber, ExtentTest extentedReport,
      boolean screenShot) throws Exception {
    try {
      enterPolicyNumber(policyNumber, extentedReport, screenShot);
      clickSearch(extentedReport);
      selectPolicyId(policyNumber, extentedReport, true);
    } catch (Exception e) {
      throw new Exception("Failed to Search and Select Policy ID" + e);
    }
  }

  /**
   * To search the customer in Search Page.
   * 
   * @return CustDashboardPage
   * @param screenShot
   * @param extentedReport
   * @throws Exception
   * 
   */

  public CustDashboardPage selectPolicy_from_SearchPage(boolean screenShot,
      ExtentTest extentedReport) throws Exception {

    Log.message("Selecting Valid Contact");
    try {

      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to get search results in search page"))
              .until(ExpectedConditions.visibilityOf(table_policysearchsingleResult));
      WebElement RowVal = driver.findElement((By.cssSelector(specific_policy_Cnt)));
      Log.message(
          "Sucessfully searched policy : " + txt_PolicyNo.getText() + " through quick search",
          extentedReport);
      RowVal.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on newly created policy in search policy", driver, extentedReport,
          screenShot);

      return new CustDashboardPage(driver, extentedReport).get();

    } catch (Exception e) {
      throw new Exception("Unable to Select Valid Contact :" + e);
    }

  }

  /**
   * 
   * To verify contact name
   * 
   * @return boolean
   * @throws Exception
   * @param name as string
   * @param Screenshot as boolean
   * 
   */
  public boolean verifyContactName(String name, boolean Screenshot) throws Exception {
    boolean status = false;

    String ContactName[] = txtContactName.getText().split("Date of Birth");
    if (ContactName[0].trim().equalsIgnoreCase(name)) {
      Log.message("Searched Contact Equals the created contact");
      status = true;
    }
    Log.message("Verified the name of the Searched Contact");

    return status;
  }

  /**
   * 
   * Description : After Search, verify search list heading when policy/quote associate to a contact
   * 
   * Return : boolean
   * 
   * @param screenShot
   * @throws Exception
   * 
   */

  public boolean verifyHeading(boolean Screenshot) throws Exception {
    return GenericUtils.verifyWebElementTextEquals(txt_searchresHeading,
        "Select a policy/quote to verify");

  }

  /**
   * 
   * Description : After Search, verify string when no search details displayed
   * 
   * Return : boolean
   * 
   * @param extentedReport
   * @param screenShot
   * 
   * @throws Exception as custom Exception Message
   * 
   */

  public boolean verify_stringonSearchPage(ExtentTest extentedReport, boolean Screenshot)
      throws Exception {

    WaitUtils.waitForElementPresent(driver, 180, txt_withnopolicies,
        "Unable to find message 'There are no Quotes or Policies associated with this Contact' on search page");
    try {
      return GenericUtils.verifyWebElementTextContains(txt_withnopolicies, SEARCH_NOPOLICY_MSG);
    } catch (Exception e) {
      throw new Exception(
          "Unable to find message 'There are no Quotes or Policies associated with this Contact' on search page. Exception occured"
              + e);
    }
  }

  /**
   * 
   * Description : After search, click proceed button
   * 
   * Return : customer dashboard page
   * 
   * @param extentedReport
   * @param screenShot
   * 
   * @throws Exception as custom Exception Message
   * 
   */
  public CustDashboardPage click_proceedButton(ExtentTest extentedReport, boolean Screenshot)
      throws Exception {
    WaitUtils.waitForElementPresent(driver, 180, btn_proceed,
        "Unable to find proceed button on search page");
    try {
      btn_proceed.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on proceed button successfully", driver, extentedReport, true);
      return new CustDashboardPage(driver, extentedReport).get();
    } catch (Exception e) {
      throw new Exception(
          "Failed to locate proceed button on search page. Exception occured : " + e);

    }
  }

  /**
   * 
   * Description : After Search, verify the presence of collapse button on search result
   * 
   * Return : boolean
   * 
   * 
   */

  public boolean verifysearch_CollapseBtn() {

    return WaitUtils.waitForElement(driver, btn_clkTocollapse, 1);

  }

  /**
   * 
   * Description : After Search, verify the search result by clicking each row , validate whether
   * the result moves to top
   * 
   * Return : boolean
   * 
   * @param extentedReport
   * @param screenShot
   * 
   * @throws Exception as custom Exception Message
   * 
   */

  public boolean verifysearch_Results(ExtentTest extentedReport, boolean screenShot)
      throws Exception {
    boolean srchResult = false;
    try {
      List<WebElement> ContactRows = driver.findElements(By.cssSelector(contact_searchRows));

      for (int i = 1; i <= ContactRows.size(); i++) {
        WebElement ele_contactname =
            driver.findElement(By.cssSelector(contact_searchRows + i + "']"));
        String ContactName = ele_contactname.getText();
        ele_contactname.click();

        WaitUtils.waitForSpinner(driver);
        srchResult = ContactName.equalsIgnoreCase(txt_selectedcontName.getText());
      }
      Log.message("Search results are clickable and moves to top of search list when clicked",
          driver, extentedReport, true);
      return srchResult;
    }

    catch (Exception e) {
      throw new Exception(
          "Failed to load the search results, when searched using last and first name. Exception occured : "
              + e);
    }
  }

  /**
   * 
   * Description : After Search, verify the search result are in alphabetical order
   * 
   * @param extentedReport Return : boolean
   * 
   * @throws Exception as custom Exception Message
   * 
   */

  public boolean verify_results_Alphabeticalorder(ExtentTest extentedReport) throws Exception {
    try {
      List<String> myList = new ArrayList<String>();
      List<WebElement> ContactRows = driver.findElements(By.cssSelector(contact_searchRows));
      Log.message("Search result has " + ContactRows.size() + " contacts", extentedReport);
      for (int i = 1; i <= ContactRows.size(); i++) {
        WebElement ele_contactname =
            driver.findElement(By.cssSelector(contact_searchRows + i + "']"));
        String ContactName = ele_contactname.getText();
        myList.add(ContactName);
      }

      if (!myList.isEmpty()) {
        Iterator<String> it = myList.iterator();
        String prev = it.next();
        while (it.hasNext()) {
          String next = it.next();
          if (prev.compareTo(next) > 0) {
            return false;
          }
          prev = next;
        }
      }
      return true;
    } catch (Exception e) {
      throw new Exception(
          "Failed to verify the contact lists in alphabetical order, exception occured: " + e);
    }
  }

  /**
   * 
   * Description : Select the contact from the available list
   * 
   * @param NameToSelect
   * @param extentedReport
   * @param screenShot
   * @throws Exception
   * 
   */

  public void selectcontactFromList(String NameToSelect, ExtentTest extentedReport,
      boolean screenShot) throws Exception {

    Log.message("Selecting contact");
    try {
      boolean boolVal = false;
      WaitUtils.waitForSpinner(driver);
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to get search results"))
              .until(ExpectedConditions.visibilityOf(contact_Table));
      List<WebElement> ContactRows = driver.findElements(By.cssSelector(Cnt_Table));

      for (int i = 0; i < ContactRows.size(); i++) {
        boolVal = ContactRows.get(i).getText().contains(NameToSelect);
        if (boolVal) {
          break;
        }
      }
      if (boolVal) {
        ContactRows.get(ContactRows.size() - 1).click();
        WaitUtils.waitForSpinner(driver);
        Log.message("Selecting the contact " + NameToSelect + " from search result", driver,
            extentedReport, true);
      }

    }

    catch (Exception f) {
      throw new Exception("Error occured while searching customer" + f);
    }
  }

  /**
   * Description :To create a corporate contact
   * 
   * @param extentReport
   * @param testdata
   * @param Screenshot as boolean
   * 
   * @throws Exception : Custom Exception Message
   */

  public void createCorporateContact(HashMap<String, String> testdata, ExtentTest extentReport,
      boolean Screenshot) throws Exception {

    try {
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElementPresent(driver, Ele_engagement_Centre_tabs,
          "Unable to find Perosnal/corporate contact tabs");
      enga_Centre_tabs_Corporate.click();
      Log.message("Clicked on corporate contact tab", extentReport);
      Thread.sleep(2000);

      Log.message("Entering Customer details");

      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to Switch to Windows"))
              .until(ExpectedConditions.visibilityOf(txtTradeName));
      Log.message("Entering Trade Details", extentReport);

      enterTradeDetails(testdata, extentReport);
      enterBizDetails(testdata, extentReport);
      enterBTDetails(testdata, extentReport);
      enterAuthContactDetails(testdata, extentReport);
      enterAuthContactphoneDetails(testdata, extentReport);
      Log.message("Entered Corporate Contact details", extentReport);

    } catch (Exception e) {
      throw new Exception("Error while entering corporate customer details" + e);
    }
  }

  /**
   * Description :To Enter trade details
   * 
   * @param extentReport
   * 
   * @param testdata as hasmap
   * 
   * @throws Exception : Custom Exception Message
   */
  public void enterTradeDetails(HashMap<String, String> testdata, ExtentTest extentReport)
      throws Exception {
    try {
      System.out.println("Inside Trade");

      enterTradeName(testdata, extentReport);

    } catch (Exception e) {
      throw new Exception("Trade name field not entered :" + e);
    }
  }

  /**
   * To enterBizDetails
   * 
   * @throws Exception
   * @param extentReport
   * @param testdata :hashMap
   * 
   */

  public void enterBizDetails(HashMap<?, ?> testdata, ExtentTest extentReport) throws Exception {
    try {

      selectType_CC(testdata.get("Type").toString(), extentReport);
      selectCountry_CC(testdata.get("Country").toString(), extentReport);
      Thread.sleep(2000);
      enterPostCode_cc(testdata.get("Post Code").toString(), extentReport);
      Thread.sleep(2000);
      clickfindAddress(extentReport, true);
      Thread.sleep(2000);
      selectAddressList_CC(extentReport);
      Log.message("Entered the Bisuness Details");
    } catch (Exception e) {
      throw new Exception("Trade name field is not displayed" + e);
    }
  }

  /**
   * To enterBTDetails
   * 
   * @throws Exception
   * @param extentReport
   * @param testdata :hashMap
   * 
   */
  public void enterBTDetails(HashMap<?, ?> testdata, ExtentTest extentReport) throws Exception {
    try {

      enterBTBiz(testdata.get("Phone Number").toString(), extentReport);
      enterBTHome(testdata.get("Phone Number").toString(), extentReport);
      enterBTMob(testdata.get("Phone Number").toString(), extentReport);
      enterBEEmail(testdata.get("Email").toString(), extentReport);
      Log.message("Entered the Business Telephone Details");
    } catch (Exception e) {
      throw new Exception("Trade name field is not displayed" + e);
    }
  }

  /**
   * To enterAuthContactDetails
   * 
   * @throws Exception
   * @param extentReport
   * @param testdata :hashMap
   */
  public void enterAuthContactDetails(HashMap<?, ?> testdata, ExtentTest extentReport)
      throws Exception {
    try {

      selectTitle_CC(testdata.get("Title").toString(), extentReport);
      enterFirstName_cc(testdata.get("First Name").toString() + "Corp", extentReport);
      enterLastName_cc(testdata.get("Last Name").toString() + "Corp", extentReport);
      Log.message("Entered the Authorized Contact Details");
    } catch (Exception e) {
      throw new Exception("Trade name field is not displayed" + e);
    }
  }

  /**
   * To enterAuthContactphoneDetails
   * 
   * @throws Exception
   * @param extentReport
   * @param testdata :hashMap
   */
  public void enterAuthContactphoneDetails(HashMap<?, ?> testdata, ExtentTest extentReport)
      throws Exception {
    try {

      enterAuthphoneHome(testdata.get("Phone Number").toString(), extentReport);
      enterAuthphonework(testdata.get("Phone Number").toString(), extentReport);
      enterAuthphoneMob(testdata.get("Phone Number").toString(), extentReport);
      enterAuthphoneEmail(testdata.get("Email").toString(), extentReport);
      Log.message("Entered the Authorized Contact Telephone Details");
    } catch (Exception e) {
      throw new Exception("Trade name field is not displayed" + e);
    }
  }

  /**
   * To enterTradeName
   * 
   * @throws Exception
   * @param extentReport
   * @param testdata :String
   */
  public void enterTradeName(HashMap<String, String> testdata, ExtentTest extentReport)
      throws Exception {
    try {
      String tradename_str;

      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Trade name text box"))
              .until(ExpectedConditions.visibilityOf(txtTradeName));
      txtTradeName.clear();
      if (testdata.get("Trade Name").toString().equals("")) {
        tradename_str = GenericUtils.getRandomCharacters("alpha", 6);
        testdata.put("Trade Name", tradename_str);
      } else
        tradename_str = testdata.get("Trade Name").toString();

      txtTradeName.sendKeys(tradename_str);
      testdata.put("Trade Name", tradename_str);
      Log.message("Entered the TradeName: " + tradename_str, extentReport);
    } catch (Exception e) {
      throw new Exception("Trade name field is not displayed " + e);
    }
  }

  /**
   * To enter TradeAs option
   * 
   * @throws Exception
   * @param extentReport
   * @param TradeAs :String
   */
  public void enterTradeAs(String TradeAs, ExtentTest extentReport) throws Exception {
    try {
      System.out.println("Entering Trade As");
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Trade As text box"))
              .until(ExpectedConditions.visibilityOf(txtTradeAs));
      txtTradeAs.clear();
      txtTradeAs.sendKeys(TradeAs);
      Log.message("Entered the TradeAs: " + TradeAs);
    } catch (Exception e) {
      throw new Exception("TradeAs field is not displayed" + e);
    }
  }

  /**
   * To enterBuidlingnum
   * 
   * @throws Exception
   * @param extentReport
   * @param BuldingNum :String
   */
  public void enterBuidlingnum(String BuldingNum, ExtentTest extentReport) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find BuldingNum text box"))
              .until(ExpectedConditions.visibilityOf(txtBuldNumber));
      txtBuldNumber.clear();
      txtBuldNumber.sendKeys(BuldingNum);
      Log.message("Entered the TradeAs: " + BuldingNum);
    } catch (Exception e) {
      throw new Exception("BuldingNum field is not displayed" + e);
    }
  }

  /**
   * To enterPostCode
   * 
   * @throws Exception
   * @param extentReport
   * @param PostCode :String
   */
  public void enterPostCode_cc(String PostCode, ExtentTest extentReport) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find PostCode text box"))
              .until(ExpectedConditions.visibilityOf(txtPostcode));
      txtPostcode.clear();
      txtPostcode.sendKeys(PostCode);
      Log.message("Entered the PostCode: " + PostCode, extentReport);
    } catch (Exception e) {
      throw new Exception("PostCode field is not displayed" + e);
    }
  }

  /**
   * To enterBTBiz
   * 
   * @throws Exception
   * @param extentReport
   * @param BTBiz :String
   */
  public void enterBTBiz(String BTBiz, ExtentTest extentReport) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find BTBiz text box"))
              .until(ExpectedConditions.visibilityOf(txtBTBiz));
      txtBTBiz.clear();
      txtBTBiz.sendKeys(BTBiz);
      Log.message("Entered the BTBiz: " + BTBiz, extentReport);
    } catch (Exception e) {
      throw new Exception("BTBiz field is not displayed" + e);
    }
  }

  /**
   * To enterBTHome
   * 
   * @throws Exception
   * @param extentReport
   * @param BTHome :String
   */
  public void enterBTHome(String BTHome, ExtentTest extentReport) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find BTHome text box"))
              .until(ExpectedConditions.visibilityOf(txtBTHome));
      txtBTHome.clear();
      txtBTHome.sendKeys(BTHome);
      Log.message("Entered the BTHome: " + BTHome, extentReport);
    } catch (Exception e) {
      throw new Exception("BTHome field is not displayed" + e);
    }
  }

  /**
   * To enterBTMob
   * 
   * @throws Exception
   * @param extentReport
   * @param BTMob :String
   */
  public void enterBTMob(String BTMob, ExtentTest extentReport) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find BTMob text box"))
              .until(ExpectedConditions.visibilityOf(txtBTMob));
      txtBTMob.clear();
      txtBTMob.sendKeys(BTMob);
      Log.message("Entered the BTMob: " + BTMob, extentReport);
    } catch (Exception e) {
      throw new Exception("BTMob field is not displayed" + e);
    }
  }

  /**
   * To enterBEEmail
   * 
   * @throws Exception
   * @param extentReport
   * @param BEEmail :String
   */
  public void enterBEEmail(String BEEmail, ExtentTest extentReport) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find BEEmail text box"))
              .until(ExpectedConditions.visibilityOf(txtBEEmail));
      txtBEEmail.clear();
      SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
      Date date = new Date();
      String time = sdf.format(date);

      BEEmail = "Tester" + time + "@ecTest.com";
      Thread.sleep(2000);
      txtBEEmail.sendKeys(BEEmail);
      Thread.sleep(3000);
      Log.message("Entered the BEEmail: " + BEEmail, extentReport);
    } catch (Exception e) {
      throw new Exception("BEEmail field is not displayed" + e);
    }
  }

  /**
   * To enterBEWbsite
   * 
   * @throws Exception
   * @param extentReport
   * @param BEWbsite :String
   */
  public void enterBEWbsite(String BEWbsite, ExtentTest extentReport) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find BEWbsit text box"))
              .until(ExpectedConditions.visibilityOf(txtBEWbsite));
      txtBEWbsite.clear();
      txtBEWbsite.sendKeys(BEWbsite);
      Log.message("Entered the BEWbsite: " + BEWbsite);
    } catch (Exception e) {
      throw new Exception("BEWbsite field is not displayed" + e);
    }
  }

  /**
   * To enterAuthphoneHome
   * 
   * @throws Exception
   * @param extentReport
   * @param AuthphoneHome :String
   */
  public void enterAuthphoneHome(String AuthphoneHome, ExtentTest extentReport) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find AuthphoneHome text box"))
              .until(ExpectedConditions.visibilityOf(txtAuthphoneHome));
      txtAuthphoneHome.clear();
      txtAuthphoneHome.sendKeys(AuthphoneHome);
      Log.message("Entered the AuthphoneHome: " + AuthphoneHome, extentReport);
    } catch (Exception e) {
      throw new Exception("AuthphoneHome field is not displayed" + e);
    }
  }

  /**
   * To enterAuthphonework
   * 
   * @throws Exception
   * @param extentReport
   * @param Authphonework :String
   */
  public void enterAuthphonework(String Authphonework, ExtentTest extentReport) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Authphonework text box"))
              .until(ExpectedConditions.visibilityOf(txtAuthphonework));
      txtAuthphonework.clear();
      txtAuthphonework.sendKeys(Authphonework);
      Log.message("Entered the Authphonework: " + Authphonework, extentReport);
    } catch (Exception e) {
      throw new Exception("Authphonework field is not displayed" + e);
    }
  }

  /**
   * To enterAuthphoneMob
   * 
   * @throws Exception
   * @param extentReport
   * @param AuthphoneMob :String
   */
  public void enterAuthphoneMob(String AuthphoneMob, ExtentTest extentReport) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find AuthphoneMob text box"))
              .until(ExpectedConditions.visibilityOf(txtAuthphoneMob));
      txtAuthphoneMob.clear();
      txtAuthphoneMob.sendKeys(AuthphoneMob);
      Log.message("Entered the AuthphoneMob: " + AuthphoneMob, extentReport);
    } catch (Exception e) {
      throw new Exception("AuthphoneMob field is not displayed" + e);
    }
  }

  /**
   * To enterAuthphoneEmail
   * 
   * @throws Exception
   * @param extentReport
   * @param AuthphoneEmail :String
   */
  public void enterAuthphoneEmail(String AuthphoneEmail, ExtentTest extentReport) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find AuthphoneEmail text box"))
              .until(ExpectedConditions.visibilityOf(txtAuthphoneEmail));
      txtAuthphoneEmail.clear();
      SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
      Date date = new Date();
      String time = sdf.format(date);
      AuthphoneEmail = "Tester" + time + "@ecTest.com";
      Thread.sleep(1000);
      txtAuthphoneEmail.sendKeys(AuthphoneEmail);
      Log.message("Entered the AuthphoneEmail: " + AuthphoneEmail, extentReport);
    } catch (Exception e) {
      throw new Exception("AuthphoneEmail field is not displayed" + e);
    }
  }

  /**
   * To enterFirstName
   * 
   * @throws Exception
   * @param extentReport
   * @param FirstName :String
   */
  public void enterFirstName_cc(String FirstName, ExtentTest extentReport) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find FirstName text box"))
              .until(ExpectedConditions.visibilityOf(txtFirstName));
      txtFirstName.clear();
      txtFirstName.sendKeys(FirstName);
      Log.message("Entered the FirstName: " + FirstName, extentReport);
    } catch (Exception e) {
      throw new Exception("FirstName field is not displayed" + e);
    }
  }

  /**
   * To enterLastName_cc
   * 
   * @throws Exception
   * @param extentReport
   * @param LastName :String
   */
  public void enterLastName_cc(String LastName, ExtentTest extentReport) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find LastName text box"))
              .until(ExpectedConditions.visibilityOf(txtLastName));
      txtLastName.clear();
      txtLastName.sendKeys(LastName);
      Log.message("Entered the FirstName: " + LastName, extentReport);
    } catch (Exception e) {
      throw new Exception("LastName field is not displayed" + e);
    }
  }

  /**
   * To enterMiddleName
   * 
   * @throws Exception
   * @param extentReport
   * @param MiddleName :String
   */
  public void enterMiddleName(String MiddleName, ExtentTest extentReport) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find MiddleName text box"))
              .until(ExpectedConditions.visibilityOf(txtMiddleName));
      txtMiddleName.clear();
      txtMiddleName.sendKeys(MiddleName);
      Log.message("Entered the MiddleName: " + MiddleName);
    } catch (Exception e) {
      throw new Exception("MiddleName field is not displayed" + e);
    }
  }

  /**
   * To selectType_CC
   * 
   * @throws Exception
   * @param extentReport
   * @param type_cc :String
   */
  public void selectType_CC(String type_cc, ExtentTest extentReport) throws Exception {
    try {
      System.out.println("Selecting Business Type");
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Type Selection field"))
              .until(ExpectedConditions.visibilityOf(selectType));
      Select typeCC = new Select(selectType);
      typeCC.selectByVisibleText(type_cc);
      Log.message("Selected type_cc: " + type_cc, extentReport);
    } catch (Exception e) {
      throw new Exception("Error while selecting type_cc" + e);
    }
  }

  /**
   * To selectCountry_CC
   * 
   * @throws Exception
   * @param extentReport
   * @param country_cc :String
   */
  public void selectCountry_CC(String country_cc, ExtentTest extentReport) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Country Selection field"))
              .until(ExpectedConditions.visibilityOf(selectCountry));
      Select countryCC = new Select(selectCountry);
      countryCC.selectByVisibleText(country_cc);
      Log.message("Selected country_cc: " + country_cc, extentReport);
    } catch (Exception e) {
      throw new Exception("Error while selecting country_cc" + e);
    }
  }

  /**
   * To selectTitle_CC
   * 
   * @throws Exception
   * @param extentReport
   * @param selectTitle_CC :String
   */
  public void selectTitle_CC(String Title_cc, ExtentTest extentReport) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find selectSuffix Selection field"))
              .until(ExpectedConditions.visibilityOf(selectTitle));
      Select TitleCC = new Select(selectTitle);
      TitleCC.selectByVisibleText(Title_cc);
      Log.message("Selected Title_cc: " + Title_cc, extentReport);
    } catch (Exception e) {
      throw new Exception("Error while selecting Title_cc" + e);
    }
  }

  /**
   * To selectAddressList_CC
   * 
   * @throws Exception
   * @param extentReport
   * 
   */
  public void selectAddressList_CC(ExtentTest extentReport) throws Exception {
    try {

      if (WaitUtils.waitForElement(driver, AddressList, 1)) {
        (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
            .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
            .withMessage("Unable to find selectSuffix Selection field"))
                .until(ExpectedConditions.visibilityOf(AddressList));
        Select AddressListCC = new Select(AddressList);
        AddressListCC.selectByIndex(0);
        Log.message("Selected AddressList_cc", extentReport);
      }
    } catch (Exception e) {
      throw new Exception("Error while selecting AddressList_cc" + e);
    }
  }

  /**
   * To selectSuffix_CC
   * 
   * @param Suffix_cc :String
   * @throws Exception
   * 
   */
  public void selectSuffix_CC(String Suffix_cc) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find selectSuffix Selection field"))
              .until(ExpectedConditions.visibilityOf(selectSuffix));
      Select SuffixCC = new Select(selectSuffix);
      SuffixCC.selectByVisibleText(Suffix_cc);
      Log.message("Selected Suffix_cc: " + Suffix_cc);
    } catch (Exception e) {
      throw new Exception("Error while selecting Suffix_cc" + e);
    }
  }

  /**
   * To SelectOptMarketing
   * 
   * @throws Exception
   * 
   */
  public void SelectOptMarketing() throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find policy number text box"))
              .until(ExpectedConditions.invisibilityOfElementLocated(
                  By.cssSelector("div.spinning-on-load-bg-table-active")));
      selectRadioButton("fieldset#FS_QUE_05DBD1E701916C229357553>div.radio", "No");
      Log.message("Selected marketing preferences");
    } catch (Exception e) {
      throw new Exception("Policy Number field is not displayed" + e);
    }
  }

  /**
   * To SelectCommPreference_CCe
   * 
   * @throws Exception
   * 
   */
  public void SelectCommPreference_CC() throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find policy number text box"))
              .until(ExpectedConditions.invisibilityOfElementLocated(
                  By.cssSelector("div.spinning-on-load-bg-table-active")));
      selectRadioButton("fieldset#FS_QUE_05DBD1E701916C229357567>div.radio",
          "Correspondence by email");
      Log.message("Selected marketing preferences");
    } catch (Exception e) {
      throw new Exception("Policy Number field is not displayed" + e);
    }
  }

  /**
   * To clickfindAddress
   * 
   * @throws Exception
   * @param extentReport
   * @param screenShot :boolean
   */
  public void clickfindAddress(ExtentTest extentReport, boolean screenShot) throws Exception {
    (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
        .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
        .withMessage("Unable to find PostCode text box"))
            .until(ExpectedConditions.visibilityOf(btnfindAddress));
    try {
      btnfindAddress.click();
      Log.message("Clicked on find address button", extentReport);

    } catch (Exception e) {
      if (!btnfindAddress.isDisplayed()) {
        throw new Exception("Find address Button is not visible" + e);
      }
    }
  }

  /**
   * To click confirm button in corporate contact window Returns - customerdashboard apge
   * 
   * @return custDashboardPage
   * @throws Exception
   * @param extentedReport
   * @param screenShot :boolean
   */
  public CustDashboardPage confirmCreateCustomer_CC(boolean screenShot, ExtentTest extentedReport)
      throws Exception {
    WaitUtils.waitForSpinner(driver);
    try {

      cn_SaveCustomer_Corp.click();
      Log.message("Clicked on Save corporate customer", driver, extentedReport, true);
      WaitUtils.waitForSpinner(driver);
      return new CustDashboardPage(driver, extentedReport).get();
    } catch (Exception e) {
      try {
        if (driver.findElement(By.cssSelector("span.qlrError.ErrorColor")).isDisplayed()) {
          throw new Exception("Mandatory Details are not Entered");
        }
      } catch (Exception f) {
        throw new Exception("Error occured while creating corporate customer" + f);
      }

      throw new Exception("Not Navigated to Customer Dashboard Page" + e);
    }
  }

  /**
   * To select contact from Search Result
   * 
   * @return custDashboardPage
   * @throws Exception
   * @param NameToSelect
   * @param extentedReport
   * @param screenShot :boolean
   */

  public CustDashboardPage selectContactFromSearchResult(String NameToSelect, boolean screenShot,
      ExtentTest extentedReport) throws Exception {
    try {
      boolean boolVal = false;

      if (WaitUtils.waitForElement(driver, firstRow)) {
        firstRow.click();
        Thread.sleep(2000);
      }

      if (WaitUtils.waitForElement(driver, but_Proceed)) {
        boolVal = true;
        but_Proceed.sendKeys(Keys.ENTER);
        WaitUtils.waitForSpinner(driver);
      } else {
        Integer totalRowSize = lstContactTableRows.size();
        String cssRow = "#C2__QUE_753975C04A0241C1924590_R";
        for (int loopcount = 0; loopcount < totalRowSize; loopcount++) {
          WebElement RowVal = lstContactTableRows.get(loopcount)
              .findElement((By.cssSelector(cssRow + (loopcount + 1))));
          String displayedContactName = RowVal.getText();
          if (displayedContactName.contains(NameToSelect)) {
            Log.message(NameToSelect + " - Contact name is available in Contact table after search",
                driver, extentedReport, screenShot);
            boolVal = true;
            RowVal.click();
            WaitUtils.waitForSpinner(driver);
            break;
          }

        }
      }

      if (!boolVal) {
        throw new Exception(NameToSelect + "is not found in the contact tabel after search");
      }
      return new CustDashboardPage(driver, extentedReport).get();

    } catch (Exception e) {
      throw new Exception("Unable to Select Valid Contact" + e);
    }

  }

  /**
   * Verify banner title as Engagement centre
   * 
   * @param extentedReport
   * @return boolean
   * @throws Exception : Custom Exception Message
   */

  public boolean ECbannertitleCheck(ExtentTest extentedReport) throws Exception {

    try {
      return GenericUtils.verifyWebElementTextEquals(txtBannerName, "Engagement Centre");
    }

    catch (Exception e) {
      throw new Exception("Exception while getting banner title" + e);
    }

  }

  /**
   * 
   * Description : Search any policy number in search page, and Select the policy to open policy
   * details page
   * 
   * @param policyNumber - Policy number, wants to search
   * @param extentedReport
   * 
   * @throws Exception as custom Exception Message
   * 
   */
  public void searchAndSelectPolicyId(String policyNumber, ExtentTest extentedReport)
      throws Exception {
    try {
      enterPolicyNumber(policyNumber);
      clickSearch(extentedReport);
      selectPolicyId(policyNumber, extentedReport, true);
    } catch (Exception e) {
      throw new Exception("Failed to Search and Select Policy ID" + e);
    }
  }

  /**
   * 
   * Description : Enter any policy number in search page, and Select the policy to open policy
   * details page
   * 
   * @param policyNumber - Policy number, wants to search
   * 
   * @throws Exception as custom Exception Message
   * 
   */

  public void enterPolicyNumber(String policynumber) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find policy number text box"))
              .until(ExpectedConditions.visibilityOf(txtPolicyNumber));
      txtPolicyNumber.clear();
      txtPolicyNumber.sendKeys(policynumber);
      Thread.sleep(2000);
      Log.message("Entered Policy Number: " + policynumber.toString());
    } catch (Exception e) {
      throw new Exception("Policy Number field is not displayed" + e);
    }
  }

  /**
   * Description :To click Searched Contact Policy
   * 
   * @param searchcriteria - Policy / contact as String
   * @param policyNo - Provide the policy no, if policy need to be searched
   * @param cntctName - contact name to be verified after search
   * @param screenShot boolean
   * @param extentedReport
   * @param policyStatus[Optional] to search for policy status and click
   * @return CustDashboardPage
   * 
   * @throws Exception : Custom Exception Message
   */

  public CustDashboardPage clickSearchedContactPolicy(String searchCriteria, String policyNo,
      String cntctName, boolean screenShot, ExtentTest extentedReport, String... policyStatus)
      throws Exception {
    Log.message("Selecting Valid Contact");
    try {

      switch (searchCriteria.toLowerCase()) {

        case "policy":
          WaitUtils.waitForElementPresent(driver, table_policysearchsingleResult,
              "Searched policy results were not displayed in searched page");
          WebElement RowVal = driver.findElement((By.cssSelector(specific_policy_Cnt)));
          if (GenericUtils.verifyWebElementTextContains(txt_PolicyNo, policyNo))
            Log.message("Searched policy : " + txt_PolicyNo.getText(), extentedReport);
          else
            throw new Exception("Policy No:" + policyNo
                + " not displayed in search results,Please check the policy number");

          RowVal.click();
          WaitUtils.waitForSpinner(driver);
          Log.message("Clicked on policy successfully", driver, extentedReport, screenShot);
          break;

        case "contact":
          // if contact displays single row as searched result
          if (WaitUtils.waitForElement(driver, table_policysearchsingleResult, 1) && !GenericUtils
              .verifyWebElementTextContains(txt_withnopolicies, SEARCH_NOPOLICY_MSG)) {
            boolean boolstatus = false;
            if (GenericUtils.verifyWebElementTextContains(txtcontactName, cntctName))
              Log.message("Contact search using first and last name was successful",
                  extentedReport);
            else
              throw new Exception(cntctName
                  + " contact was not listed in the search results, Please check the name to be searched");

            List<WebElement> lstOfpolicy = driver.findElements(By.cssSelector(lstofpolicies));
            if (lstOfpolicy.size() > 1 && policyStatus.length > 0) {
              String reqStatus = policyStatus[0];
              for (int i = 1; i <= lstOfpolicy.size(); i++) {
                WebElement ele_polStatus =
                    driver.findElement(By.cssSelector(lstofpolStatus + i + " td:nth-child(5)"));
                String displayedpolStatus = ele_polStatus.getText();
                if (displayedpolStatus.equalsIgnoreCase(reqStatus)) {
                  Log.message(
                      cntctName
                          + " - Required policy status is available in Contact table after search",
                      driver, extentedReport, screenShot);
                  // lstOfpolicy.get(i).click();
                  ele_polStatus.click();
                  boolstatus = true;
                  WaitUtils.waitForSpinner(driver);
                  break;

                }
              }
              if (!boolstatus && lstOfpolicy.size() > 1)
                throw new Exception(
                    "Required policy status not available in the search result, please refine your search");

            }

            else
              lstOfpolicy.get(0).click();
            Log.message("Clicked on policy associated with the searched contact", extentedReport);
          }

          // If search result has proceed button
          else if (WaitUtils.waitForElement(driver, but_Proceed, 1) && GenericUtils
              .verifyWebElementTextContains(txt_withnopolicies, SEARCH_NOPOLICY_MSG)) {
            but_Proceed.sendKeys(Keys.ENTER);
            Log.message("Clicked on proceed button in searched result", extentedReport);
            WaitUtils.waitForSpinner(driver);
          }
          // if search result has too many results then exception will
          // quits the test case execution
          else {
            List<WebElement> ContactRows = driver.findElements(By.cssSelector(contact_searchRows));
            if (ContactRows.size() > 1)
              Log.message("Search results displayed too many results with same last and first name",
                  driver, extentedReport, true);
            throw new Exception("Unable to select a contact,Please refine your search");
          }
      }

      return new CustDashboardPage(driver, extentedReport).get();

    } catch (Exception e) {
      throw new Exception("Unable to Select Valid Contact,throws exception : " + e);
    }

  }

  /**
   * To get address1
   * 
   * @throws Exception
   * 
   */
  public String getAddress1() throws Exception {
    try {
      String address1 = GenericUtils.getTextOfWebElement(txtfldAddress1, driver);
      Log.message("Address 1 " + address1);
      return address1;

    } catch (Exception e) {
      throw new Exception("Error while getting Address1 : " + e);
    }
  }

  /**
   * To get address2
   * 
   * @throws Exception
   * 
   */
  public String getAddress2() throws Exception {
    try {
      return GenericUtils.getTextOfWebElement(txtfldAddress2, driver);

    } catch (Exception e) {
      throw new Exception("Error while getting Address2 : " + e);
    }
  }

  /**
   * To get address3
   * 
   * @throws Exception
   * 
   */
  public String getAddress3() throws Exception {
    try {
      return GenericUtils.getTextOfWebElement(txtfldAddress3, driver);

    } catch (Exception e) {
      throw new Exception("Error while getting Address3 : " + e);
    }
  }

  /**
   * To verify PolicyTable in SearchResult
   * 
   * @param extentedReport
   * @param Screenshot
   * @throws Exception
   * @return boolean
   * 
   */
  public boolean verifyPolicyTableInSearchResult(ExtentTest extentedReport, boolean screenShot)
      throws Exception {

    boolean status = false;
    try {

      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to get search results in search page"))
              .until(ExpectedConditions.visibilityOf(table_policysearchsingleResult));
      if (WaitUtils.waitForElement(driver, table_policysearchsingleResult)) {
        Log.message("Verified the policy/quote table", extentedReport);
        status = true;
      }
      return status;
    } catch (Exception e) {
      throw new Exception("Unable to Select Valid Contact :" + e);
    }

  }

  /**
   * To verify Search Result Count
   * 
   * @param count
   * @param extentedReport
   * @param Screenshot
   * @return boolean
   * 
   */
  public boolean verifySearchResultCount(String count, ExtentTest extentedReport,
      boolean screenShot) {
    boolean status = false;
    WaitUtils.waitForElement(driver, searchResults, 20);
    Log.message("Actual count: " + txtSearchResultCount.getText());
    if (txtSearchResultCount.getText().contains(count)) {
      status = true;
    }
    return status;
  }

  /**
   * To verify Warning message For Brand
   * 
   * @param actualMessage
   * @param extentedReport
   * @param Screenshot
   * @return boolean
   * 
   */
  public boolean verifyWarningMsgForBrand(String actualMessage, ExtentTest extentedReport,
      boolean screenShot) {
    boolean status = false;
    Log.message(actualMessage);
    String expectedMessage = txtWarningMsg.getText().trim().replaceAll("^\\s+", "");
    Log.message(expectedMessage);
    if (expectedMessage.equals(actualMessage.trim())) {
      status = true;
    }
    return status;
  }

  /**
   * Click Brand popup Cancel button
   * 
   * @param extentedReport
   * @param Screenshot
   * @throws Exception
   * 
   */
  public void clickBrandPopupCancelBtn(ExtentTest extentedReport, boolean Screenshot)
      throws Exception {
    if (WaitUtils.waitForElement(driver, mdlBrndCancelBtn)) {
      mdlBrndCancelBtn.click();
      Thread.sleep(1000);
      Log.message("Clicked on my brand popup cancel button", driver, extentedReport, Screenshot);
    } else {
      throw new Exception("Cancel button is not visible!");
    }
  }

  /**
   * To verify BrandName For All searched Contacts
   * 
   * @param actualBrandName
   * @param extentedReport
   * @param Screenshot
   * @throws Exception
   * @return boolean
   * 
   */
  public boolean verifyBrandNameForAllContacts(String actualBrandName, ExtentTest extentedReport,
      boolean screenShot) throws Exception {
    boolean status = false;
    try {
      WaitUtils.waitForSpinner(driver);
      int count = 0;
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to get search results"))
              .until(ExpectedConditions.visibilityOf(contact_Table));
      List<WebElement> brandnameRows = driver.findElements(By.cssSelector(brandname_srch));
      for (int i = 0; i < brandnameRows.size(); i++) {
        if (brandnameRows.get(i).getText().equals(actualBrandName)) {
          Log.message(actualBrandName + " contact");
          count++;
        }
      }

      if (count == brandnameRows.size()) {
        status = true;
      }
      return status;
    } catch (Exception e) {
      throw new Exception("Unable to Select Valid Contact" + e);
    }

  }

  /**
   * To verify multiple Search Result
   * 
   * @param extentedReport
   * @param Screenshot
   * @return boolean
   * 
   */
  public boolean verifyMultipleSearchResultCount(ExtentTest extentedReport, boolean screenShot) {
    boolean status = false;
    List<WebElement> ContactRows = driver.findElements(By.cssSelector(Cnt_Table));
    if (ContactRows.size() > 1) {
      status = true;
    }
    return status;
  }

  /**
   * To verify reviewStatus
   * 
   * @param expectedStatus -Review Required | Reviewed
   * @param extentedReport
   * @param Screenshot
   * @return boolean
   * 
   */
  public boolean verify_reviewStatus(String expectedStatus, ExtentTest extentedReport,
      boolean screenshot) throws Exception {
    try {
      return (iconReview.getAttribute("Title").contains(expectedStatus));

    } catch (Exception e) {
      throw new Exception("Unable to get the renewal notification status :" + e);

    }
  }

  /**
   * To get policy number
   * 
   * @param extentedReport
   * @param Screenshot
   * @return String - PolicyNumber
   * 
   */
  public String getPolicyNo(ExtentTest extentedReport, boolean screenShot) throws Exception {
    String policyNo = "";
    try {
      policyNo = txt_PolicyNo.getText();
      Log.message("Sucessfully getting policy no: " + txt_PolicyNo.getText(), extentedReport);
      return policyNo;
    } catch (Exception e) {
      throw new Exception("Unable to get policy no :" + e);
    }

  }

}// SearchPage
