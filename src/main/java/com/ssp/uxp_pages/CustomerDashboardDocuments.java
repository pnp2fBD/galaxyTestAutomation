package com.ssp.uxp_pages;

import java.util.ArrayList;
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
import org.openqa.selenium.support.ui.WebDriverWait;
import com.relevantcodes.extentreports.ExtentTest;
import com.ssp.support.Log;
import com.ssp.utils.ElementLayer;
import com.ssp.utils.GenericUtils;
import com.ssp.utils.WaitUtils;

public class CustomerDashboardDocuments {

  private final WebDriver driver;
  public ExtentTest extentedReport;
  public ElementLayer uielement;

  String dynamicCssOfDeleteBtn = "button#C2__C6__BUT_51FA5B2E2ECDBBE11857219_R";

  @FindBy(css = "button[id*='BUT_7C64D74AEB7EF0C77786148']")
  WebElement uploadDocumentBtn;

  @FindBy(css = "input[id*='QUE_7C64D74AEB7EF0C77777797']")
  WebElement uploadDocumentDescriptionTextBox;

  @FindBy(css = "input[id*='QUE_7C64D74AEB7EF0C77777798']")
  WebElement uploadDocumentChooseFileButton;

  @FindBy(css = "button[id*='BUT_7C64D74AEB7EF0C77777800']")
  WebElement uploadDocumentSaveBtn;

  @FindBy(css = "div#C2__C6__p1_QUE_7C64D74AEB7EF0C77777797 span.red")
  WebElement labelDescriptionMandatory;

  @FindBy(css = "div#C2__C6__p1_QUE_7C64D74AEB7EF0C77777798 span.red")
  WebElement labelFileMandatory;

  @FindBy(css = "input#C2__C6__QUE_7C64D74AEB7EF0C77777799")
  WebElement uploadDocumentDocumentType;

  @FindBy(css = "input[id*='QUE_51FA5B2E2ECDBBE11898213']")
  WebElement deleteDescription;

  @FindBy(css = "button[id*='BUT_51FA5B2E2ECDBBE12054219']")
  WebElement deleteDocumentYesButton;

  @FindBy(css = "button[id*='BUT_51FA5B2E2ECDBBE12054224']")
  WebElement deleteDocumentNoButton;

  @FindBy(css = "div#C2__C6__row_QUE_51FA5B2E2ECDBBE11898213 span.red")
  WebElement deleteDescriptionMandatory;

  By spanErrorMsgBy = By.cssSelector("span[id*='QUE_7C64D74AEB7EF0C77777798_ERRORMESSAGE']");
  By rowInATableBy = By.cssSelector("tbody>tr[id*='TBL_BD388A52554BC5E72766684']");
  By firstRowDataBy =
      By.cssSelector("tbody>tr[id*='TBL_BD388A52554BC5E72766684']:nth-child(1)>td>div>span");

  public static final String[] fileExtensionTypes = {"xls", "xlsx", "ppt", "pptx", "bmp", "jpg",
      "jpeg", "png", "pdf", "docx", "html", "txt", "msg", "csv", "tif"};

  public static final String[] imageExtensionTypes = {"bmp", "jpg", "jpeg", "png", "tif"};

  public CustomerDashboardDocuments(WebDriver driver, ExtentTest report) {
    this.driver = driver;
    this.extentedReport = report;
    PageFactory.initElements(driver, this);
    WaitUtils.waitForSpinner(driver);
  }

  public boolean validateUploadADocumentPopUp(boolean screenshot, ExtentTest extentReport)
      throws Exception {
    if (!GenericUtils.validateFieldIsMandatory(labelDescriptionMandatory))
      throw new Exception("Description label field is not mandatory");
    if (!GenericUtils.validateFieldIsMandatory(labelFileMandatory))
      throw new Exception("Field label field is not mandatory");
    if (!uploadDocumentDescriptionTextBox.isEnabled())
      throw new Exception("Description text box field is not enabled");
    if (!uploadDocumentChooseFileButton.isEnabled())
      throw new Exception("Choose file button is not enabled");
    if (uploadDocumentDocumentType.isEnabled())
      throw new Exception("Document type text box field is enabled");
    if (this.validateSaveButtonEnabled())
      throw new Exception("Save button is enabled");
    Log.message("Upload a document pop-up is validated", driver, extentReport, screenshot);
    return true;
  }

  public void clickUploadDocumentButton(boolean Screenshot, ExtentTest extentReport)
      throws Exception {
    WaitUtils.waitForSpinner(driver);
    try {
      (new WebDriverWait(driver, 10).pollingEvery(20, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find upload document button"))
              .until(ExpectedConditions.elementToBeClickable(uploadDocumentBtn));
      uploadDocumentBtn.click();
      Log.message("Clicked on upload document button", driver, extentReport, Screenshot);
    } catch (Exception e) {
      throw new Exception("Unable to Click on upload document button" + e);
    }
  }

  public boolean validateSaveButtonEnabled() throws Exception {
    WaitUtils.waitForSpinner(driver);
    try {
      (new WebDriverWait(driver, 10).pollingEvery(20, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find save button"))
              .until(ExpectedConditions.visibilityOf(uploadDocumentSaveBtn));
      if (uploadDocumentSaveBtn.isEnabled())
        return true;
      return false;
    } catch (Exception e) {
      throw new Exception("Unable to find save button" + e);
    }
  }

  public void enterDescriptionUploadDocument(String description, boolean screenshot,
      ExtentTest extentReport) throws Exception {
    WaitUtils.waitForSpinner(driver);
    try {
      (new WebDriverWait(driver, 10).pollingEvery(20, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find description textfield"))
              .until(ExpectedConditions.elementToBeClickable(uploadDocumentDescriptionTextBox));
      uploadDocumentDescriptionTextBox.clear();
      uploadDocumentDescriptionTextBox.sendKeys(description);
      Log.message("Entererd the description " + description, driver, extentReport, screenshot);
    } catch (Exception e) {
      throw new Exception("Unable to enter details in description textfield" + e);
    }
  }

  public void chooseFileUploadDocument(String fileName, boolean screenshot, ExtentTest extentReport)
      throws Exception {
    WaitUtils.waitForSpinner(driver);
    try {
      (new WebDriverWait(driver, 10).pollingEvery(20, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find choose file button"))
              .until(ExpectedConditions.elementToBeClickable(uploadDocumentChooseFileButton));
      uploadDocumentChooseFileButton.click();
      GenericUtils.uploadFilesInWindow(fileName);
      Log.message("Click on choose file " + fileName, driver, extentReport, screenshot);
    } catch (Exception e) {
      throw new Exception("Unable to click on choose file button in upload a document window" + e);
    }
  }

  public void clickSaveButtonUploadDocument(boolean screenshot, ExtentTest extentReport)
      throws Exception {
    WaitUtils.waitForSpinner(driver);
    try {
      (new WebDriverWait(driver, 10).pollingEvery(20, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find save button"))
              .until(ExpectedConditions.elementToBeClickable(uploadDocumentSaveBtn));
      if (uploadDocumentSaveBtn.isEnabled())
        uploadDocumentSaveBtn.click();
      else
        throw new Exception("Save button in upload a document is not enabled");
      Log.message("Click on save button in upload a document", driver, extentReport, screenshot);
    } catch (Exception e) {
      throw new Exception("Unable to click on save button in upload a document window" + e);
    }
  }

  public boolean validateErrorMessageOnUploadDocument() throws Exception {
    WaitUtils.waitForSpinner(driver);
    try {
      (new WebDriverWait(driver, 10).pollingEvery(20, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find error message span"))
              .until(ExpectedConditions.presenceOfElementLocated(spanErrorMsgBy));
      WebElement spanErrorMsgElement = driver.findElement(spanErrorMsgBy);
      if (!spanErrorMsgElement.getText().equals(""))
        return true;
      return false;
    } catch (Exception e) {
      throw new Exception("Unable to find error message span" + e);
    }
  }

  public String getErrorMessageText() throws Exception {
    WaitUtils.waitForSpinner(driver);
    try {
      (new WebDriverWait(driver, 10).pollingEvery(20, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find error message"))
              .until(ExpectedConditions.visibilityOfElementLocated(spanErrorMsgBy));
      WebElement spanErrorMsgElement = driver.findElement(spanErrorMsgBy);
      return spanErrorMsgElement.getText();
    } catch (Exception e) {
      throw new Exception("Unable to find error message" + e);
    }
  }

  public void clickDeleteButton(int index, boolean Screenshot, ExtentTest extentReport)
      throws Exception {
    dynamicCssOfDeleteBtn = dynamicCssOfDeleteBtn + Integer.toString(index);
    By deleteBtnLocator = By.cssSelector(dynamicCssOfDeleteBtn);
    WaitUtils.waitForSpinner(driver);
    try {
      (new WebDriverWait(driver, 10).pollingEvery(20, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find delete document button"))
              .until(ExpectedConditions.elementToBeClickable(deleteBtnLocator));
      driver.findElement(deleteBtnLocator).click();
      Log.message("Clicked on delete document button", driver, extentReport, Screenshot);
    } catch (Exception e) {
      throw new Exception("Unable to Click on delete document button" + e);
    }
  }

  public boolean validateDeleteDocumentPopUp(boolean screenshot, ExtentTest extentReport)
      throws Exception {
    if (!GenericUtils.validateFieldIsMandatory(deleteDescriptionMandatory))
      throw new Exception("Description label field is not mandatory");
    if (!deleteDescription.isEnabled())
      throw new Exception("Description text box field is not enabled");
    if (deleteDocumentYesButton.isEnabled())
      throw new Exception("Yes button is enabled without entering any Description");
    if (!deleteDocumentNoButton.isEnabled())
      throw new Exception("No button is not enabled");
    Log.message("Delete a document pop-up is validated", driver, extentReport, screenshot);
    return true;
  }

  public void enterDescriptionDeleteDocument(String description, boolean screenshot,
      ExtentTest extentReport) throws Exception {
    WaitUtils.waitForSpinner(driver);
    try {
      (new WebDriverWait(driver, 10).pollingEvery(20, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find description textfield"))
              .until(ExpectedConditions.elementToBeClickable(deleteDescription));
      deleteDescription.clear();
      deleteDescription.sendKeys("Delete " + description);
      Log.message("Entererd the description " + description, driver, extentReport, screenshot);
    } catch (Exception e) {
      throw new Exception("Unable to enter details in description textfield" + e);
    }
  }

  public boolean validateYesButtonEnabled() throws Exception {
    WaitUtils.waitForSpinner(driver);
    try {
      (new WebDriverWait(driver, 10).pollingEvery(20, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Yes button"))
              .until(ExpectedConditions.visibilityOf(deleteDocumentYesButton));
      if (deleteDocumentYesButton.isEnabled())
        return true;
      return false;
    } catch (Exception e) {
      throw new Exception("Unable to find Yes button" + e);
    }
  }

  public void clickYesButtonDeleteDocument(boolean screenshot, ExtentTest extentReport)
      throws Exception {
    WaitUtils.waitForSpinner(driver);
    try {
      (new WebDriverWait(driver, 10).pollingEvery(20, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find yes button"))
              .until(ExpectedConditions.elementToBeClickable(deleteDocumentYesButton));
      deleteDocumentYesButton.click();
      Log.message("Click on yes button in delete a document", driver, extentReport, screenshot);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to click on yes button in delete a document window" + e);
    }
  }

  public void clickNoButtonDeleteDocument(boolean screenshot, ExtentTest extentReport)
      throws Exception {
    WaitUtils.waitForSpinner(driver);
    try {
      (new WebDriverWait(driver, 10).pollingEvery(20, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find No button"))
              .until(ExpectedConditions.elementToBeClickable(deleteDocumentNoButton));
      deleteDocumentNoButton.click();
      Log.message("Click on No button in delete a document", driver, extentReport, screenshot);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to click on no button in delete a document window" + e);
    }
  }

  public Integer getRowCountofDocuments() {
    List<WebElement> rowInATableElement = driver.findElements(rowInATableBy);
    List<WebElement> dataInFirstRowElement = driver.findElements(firstRowDataBy);
    int tableSize = rowInATableElement.size();
    if (tableSize > 0) {
      if (dataInFirstRowElement.size() > 1) {
        return tableSize;
      } else
        return 0;
    }
    return 0;
  }

  public List<String> getFirstRowData() {
    List<WebElement> dataInFirstRowElement = driver.findElements(firstRowDataBy);
    List<String> firstRowValues = new ArrayList<>();
    for (WebElement element : dataInFirstRowElement) {
      firstRowValues.add(element.getText());
    }
    return firstRowValues;
  }
}
