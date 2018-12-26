package com.ssp.utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.relevantcodes.extentreports.ExtentTest;
import com.ssp.support.EnvironmentPropertiesReader;
import com.ssp.support.Log;
import com.ssp.support.StopWatch;

/**
 * Util class consists wait for page load,page load with user defined max time and is used globally
 * in all classes and methods
 * 
 */
public class GenericUtils {
  public static String MOUSE_HOVER_JS = "var evObj = document.createEvent('MouseEvents');"
      + "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);"
      + "arguments[0].dispatchEvent(evObj);";
  private static EnvironmentPropertiesReader configProperty =
      EnvironmentPropertiesReader.getInstance();

  /**
   * To get the test orientation
   * 
   * <p>
   * if test run on sauce lab device return landscape or portrait or valid message, otherwise check
   * local device execution and return landscape or portrait or valid message
   * 
   * @return dataToBeReturned - portrait or landscape or valid message
   */
  public static String getTestOrientation() {
    String dataToBeReturned = null;
    boolean checkExecutionOnSauce = false;
    boolean checkDeviceExecution = false;
    checkExecutionOnSauce =
        (System.getProperty("SELENIUM_DRIVER") != null || System.getenv("SELENIUM_DRIVER") != null)
            ? true : false;

    if (checkExecutionOnSauce) {
      checkDeviceExecution = ((System.getProperty("runUserAgentDeviceTest") != null)
          && (System.getProperty("runUserAgentDeviceTest").equalsIgnoreCase("true"))) ? true
              : false;
      if (checkDeviceExecution) {
        dataToBeReturned = (System.getProperty("deviceOrientation") != null)
            ? System.getProperty("deviceOrientation")
            : "no sauce run system variable: deviceOrientation ";
      } else {
        dataToBeReturned = "sauce browser test: no orientation";
      }
    } else {
      checkDeviceExecution = (configProperty.hasProperty("runUserAgentDeviceTest")
          && (configProperty.getProperty("runUserAgentDeviceTest").equalsIgnoreCase("true"))) ? true
              : false;
      if (checkDeviceExecution) {
        dataToBeReturned = configProperty.hasProperty("deviceOrientation")
            ? configProperty.getProperty("deviceOrientation")
            : "no local run config variable: deviceOrientation ";
      } else {
        dataToBeReturned = "local browser test: no orientation";
      }
    }
    return dataToBeReturned;
  }

  public static WebDriver switchWindows(WebDriver driver, String windowToSwitch, String opt,
      String closeCurrentDriver, String match) throws Exception {

    WebDriver currentWebDriver = driver;
    WebDriver assingedWebDriver = driver;
    boolean windowFound = false;
    ArrayList<String> multipleWindows = new ArrayList<String>(assingedWebDriver.getWindowHandles());
    System.out.println(multipleWindows.size());
    System.out.println(multipleWindows);
    for (int i = 1; i < multipleWindows.size(); i++) {
      System.out.println(assingedWebDriver.getTitle());
      assingedWebDriver.switchTo().window(multipleWindows.get(i));

      /*
       * if (opt.equals("title")) { if(match.equals("contain")){ if
       * (assingedWebDriver.getTitle().contains(windowToSwitch)) { windowFound = true; break; } }
       * if(match.equals("equal")){ if (assingedWebDriver.getTitle().equals(windowToSwitch)) {
       * windowFound = true; break; } }
       * 
       * } else if (opt.equals("url")) { if
       * (assingedWebDriver.getCurrentUrl().contains(windowToSwitch)) { windowFound = true; break; }
       * } else if (opt.equals("class")){ if (assingedWebDriver.getClass().equals(windowToSwitch)){
       * windowFound = true; break; } }
       */
      // if

      if (opt.equals("url")) {
        if (match.equals("contain")) {
          if (assingedWebDriver.getTitle().contains(windowToSwitch)) {
            windowFound = true;
            break;
          }
        }
        if (match.equals("equal")) {
          if (assingedWebDriver.getTitle().contains(windowToSwitch)) {
            windowFound = true;
            break;
          }
        }
      }
      if (opt.equals("title")) {
        if (match.equals("contain")) {
          if (assingedWebDriver.getTitle().contains(windowToSwitch)) {
            windowFound = true;
            break;
          }
        }
        if (match.equals("equal")) {
          if (assingedWebDriver.getTitle().contains(windowToSwitch)) {
            windowFound = true;
            break;
          }
        }

      } // for

      if (!windowFound)
        throw new Exception("Window: " + windowToSwitch + ", not found!!");
      else {
        if (closeCurrentDriver.equals("true"))
          currentWebDriver.close();
      }
    }
    return assingedWebDriver;

  }// switchWindows

  /**
   * To compare two array list values,then print unique list value and print missed list value
   * 
   * @param expectedElements - expected element list
   * @param actualElements - actual element list
   * @return statusToBeReturned - returns true if both the lists are equal, else returns false
   */
  public static boolean compareTwoList(List<String> expectedElements, List<String> actualElements) {
    boolean statusToBeReturned = false;
    List<String> uniqueList = new ArrayList<String>();
    List<String> missedList = new ArrayList<String>();
    for (String item : expectedElements) {
      if (actualElements.contains(item)) {
        uniqueList.add(item);
      } else {
        missedList.add(item);
      }
    }
    Collections.sort(expectedElements);
    Collections.sort(actualElements);
    if (expectedElements.equals(actualElements)) {
      Log.event("All elements checked on this page:: " + uniqueList);
      statusToBeReturned = true;
    } else {
      Log.event("Missing element on this page:: " + missedList);
      statusToBeReturned = false;
    }
    return statusToBeReturned;
  }

  /**
   * To wait for the specific element which is in disabled state on the page
   * 
   * @param driver - current driver object
   * @param element - disabled webelement
   * @param maxWait - duration of wait in seconds
   * @return boolean - return true if disabled element is present else return false
   */
  public static boolean waitForDisabledElement(WebDriver driver, WebElement element, int maxWait) {
    boolean statusOfElementToBeReturned = false;
    long startTime = StopWatch.startTime();
    WebDriverWait wait = new WebDriverWait(driver, maxWait);
    try {
      WebElement waitElement = wait.until(ExpectedConditions.visibilityOf(element));
      if (!waitElement.isEnabled()) {
        statusOfElementToBeReturned = true;
        Log.event("Element is displayed and disabled:: " + element.toString());
      }
    } catch (Exception ex) {
      statusOfElementToBeReturned = false;
      Log.event("Unable to find disabled element after " + StopWatch.elapsedTime(startTime)
          + " sec ==> " + element.toString());
    }
    return statusOfElementToBeReturned;
  }

  /**
   * To get the text of a WebElement
   * 
   * @param element - the input field you need the value/text of
   * @param driver -
   * @return text of the input's value
   */
  public static String getTextOfWebElement(WebElement element, WebDriver driver) {
    String sDataToBeReturned = null;
    if (WaitUtils.waitForElement(driver, element)) {
      sDataToBeReturned = element.getText().trim().replaceAll("\\s+", " ");
    }
    return sDataToBeReturned;
  }

  /**
   * Verify contents of a WebElement equals a passed in string variable
   * 
   * @param textToVerify - expected text
   * @param elementToVerify - element to verify the text of
   * @return true if text on screen matches passed variable contents
   */
  public static boolean verifyWebElementTextEquals(WebElement elementToVerify,
      String textToVerify) {
    boolean status = false;
    if (elementToVerify.getText().trim().replaceAll("\\s+", " ").equals(textToVerify)) {
      status = true;
    }
    return status;
  }

  /**
   * Verify contents of a WebElement equals ignoring case a passed in string variable
   * 
   * @param textToVerify - expected text
   * @param elementToVerify - element to verify the text of
   * @return true if text on screen equals ignoring case passed variable contents
   */
  public static boolean verifyWebElementTextEqualsIgnoreCase(WebElement elementToVerify,
      String textToVerify) {
    boolean status = false;
    if (elementToVerify.getText().trim().replaceAll("\\s+", " ")
        .equalsIgnoreCase(textToVerify.trim())) {
      status = true;
    }
    return status;
  }

  /**
   * Verify any text is equals a passed in string variable
   * 
   * @param textToVerify - expected text
   * @param elementToVerify - text that got from an element
   * @return true if text on screen matches passed variable contents
   */
  public static boolean verifyTextEquals(String elementTextToVerify, String textToVerify) {
    boolean status = false;
    if (elementTextToVerify.equals(textToVerify)) {
      status = true;
    }
    return status;
  }

  /**
   * Verify contents of a WebElement contains a passed in string variable
   * 
   * @param textToVerify - expected text
   * @param elementToVerify - element to verify the text of
   * @return true if text on screen matches passed variable contents
   */
  public static boolean verifyWebElementTextContains(WebElement elementToVerify,
      String textToVerify) {
    boolean status = false;
    if (elementToVerify.getText().trim().replaceAll("\\s+", " ").contains(textToVerify)) {
      status = true;
    }
    return status;
  }

  /**
   * To get matching text element from List of web elements
   * 
   * @param elements -
   * @param contenttext - text to match
   * @return elementToBeReturned as WebElement
   * @throws Exception -
   */
  public static WebElement getMatchingTextElementFromList(List<WebElement> elements,
      String contenttext) throws Exception {
    WebElement elementToBeReturned = null;
    boolean found = false;

    if (elements.size() > 0) {
      for (WebElement element : elements) {
        if (element.getText().trim().replaceAll("\\s+", " ").equalsIgnoreCase(contenttext)) {
          elementToBeReturned = element;
          found = true;
          break;
        }
      }
      if (!found) {
        throw new Exception("Didn't find the correct text(" + contenttext + ")..! on the page");
      }
    } else {
      throw new Exception("Unable to find list element...!");
    }
    return elementToBeReturned;
  }

  /**
   * To verify matching text element from List of web elements
   * 
   * @param elements -
   * @param contenttext - text to match
   * @return elementToBeReturned as WebElement
   * @throws Exception -
   */
  public static boolean verifyMatchingTextContainsElementFromList(List<WebElement> elements,
      String contenttext) throws Exception {

    boolean found = false;
    if (elements.size() > 0) {
      for (WebElement element : elements) {
        if (element.getText().trim().contains(contenttext)) {
          found = true;
          break;
        }
      }
      if (!found) {
        Log.failsoft("Didn't find the correct text(" + contenttext + ")..! on the page");
      }
    } else {
      throw new Exception("Unable to find list element...!");
    }

    return found;
  }



  /**
   * To scroll into particular element
   * 
   * @param driver -
   * @param element - the element to scroll to
   */
  public static void scrollIntoView(final WebDriver driver, WebElement element) {
    try {
      String scrollElementIntoMiddle =
          "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
              + "var elementTop = arguments[0].getBoundingClientRect().top;"
              + "window.scrollBy(0, elementTop-(viewPortHeight/2));";
      ((JavascriptExecutor) driver).executeScript(scrollElementIntoMiddle, element);
      (new WebDriverWait(driver, 20).pollingEvery(500, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Page spinners/page not loading")).until(WaitUtils.documentLoad);
    } catch (Exception ex) {
      Log.event("Moved to element");
    }
  }

  /**
   * Switching between tabs or windows in a browser
   * 
   * @param driver -
   */
  public static void switchToNewWindow(WebDriver driver) {
    String winHandle = driver.getWindowHandle();
    for (String index : driver.getWindowHandles()) {
      if (!index.equals(winHandle)) {
        driver.switchTo().window(index);
        break;
      }
    }
    if (!((RemoteWebDriver) driver).getCapabilities().getBrowserName().matches(".*safari.*")) {
      ((JavascriptExecutor) driver).executeScript(
          "if(window.screen){window.moveTo(0, 0); window.resizeTo(window.screen.availWidth, window.screen.availHeight);};");
    }
  }

  /**
   * To perform mouse hover on an element using javascript
   * 
   * @param driver
   * @param element
   */
  public static void moveToElementJS(WebDriver driver, WebElement element) {
    ((JavascriptExecutor) driver).executeScript(MOUSE_HOVER_JS, element);
  }

  /**
   * To compare two HashMap values,then print unique list value and print missed list value
   * 
   * @param expectedList - expected element list
   * @param actualList - actual element list
   * @return statusToBeReturned - returns true if both the lists are equal, else returns false
   */
  public static boolean compareTwoHashMap(HashMap<String, String> expectedList,
      HashMap<String, String> actualList) {
    List<String> missedkey = new ArrayList<String>();
    HashMap<String, String> missedvalue = new HashMap<String, String>();
    try {
      for (String k : expectedList.keySet()) {
        if (!(actualList.get(k).equals(expectedList.get(k)))) {
          missedvalue.put(k, actualList.get(k));
          Log.event("Missed Values:: " + missedvalue);
          return false;
        }
      }
      for (String y : actualList.keySet()) {
        if (!expectedList.containsKey(y)) {
          missedkey.add(y);
          Log.event("Missed keys:: " + missedkey);
          return false;
        }
      }
    } catch (NullPointerException np) {
      return false;
    }
    return true;
  }

  /**
   * To verify font of the webelement
   * 
   * 
   * @param locator - css selector of webelement
   * @param font - font type to verify
   * @return boolean
   * 
   */
  public static boolean verifyFont(WebDriver driver, String locator, String font) throws Exception {
    try {
      JavascriptExecutor js = (JavascriptExecutor) driver;
      WebElement element = driver.findElement(By.cssSelector(locator));
      String fontWeight = (String) js.executeScript(
          "return getComputedStyle(arguments[0]).getPropertyValue('font-Weight');", element);
      if (fontWeight.trim().equals(font)) {
        return true;
      } else {
        return false;
      }
    } catch (Exception ex) {
      throw new Exception("Error while veriying whether the string is in bold" + ex);

    }
  }

  /**
   * To verify given string is displayed first
   * 
   * 
   * @param locator - css selector of webelement
   * @param font - font type to verify
   * @return boolean
   * 
   */
  public static boolean verifyWebElementStartWith(WebDriver driver, WebElement element,
      String substringToVerify) throws Exception {
    try {
      boolean firstString = false;
      String displayedPolicyHolderName = getTextOfWebElement(element, driver);
      if (displayedPolicyHolderName.startsWith(substringToVerify)) {
        firstString = true;
      }
      return firstString;

    } catch (Exception ex) {
      throw new Exception("Error while veriying whether the string is in bold" + ex);

    }
  }

  /**
   * To verify a list is in alphabetical order
   * 
   * @param listToCheck - what to check alpha order of
   * @return boolean
   */
  public static boolean verifyListInAlphabeticalOrder(List<WebElement> listToCheck) {
    boolean status = false;
    List<String> ActualList = new ArrayList<String>();
    List<String> Sortedlist = new ArrayList<String>();
    for (WebElement element : listToCheck) {
      ActualList.add(element.getText());
      Sortedlist.add(element.getText());
    }
    Collections.sort(Sortedlist);
    if (ActualList.equals(Sortedlist)) {
      status = true;
      Log.message("List is in alphabetical order: " + Sortedlist);
    } else {
      status = false;
    }
    return status;
  }

  /**
   * To generate Random characters
   * 
   * @param type
   * @param length
   * 
   */
  public static String getRandomCharacters(String type, int length) {
    String SALTCHARS = null;
    if ("ALPHANUMERIC".equalsIgnoreCase(type))
      SALTCHARS = "abcdefghijklmnopqrstuvwxyz0123456789";
    else if ("ALPHA".equalsIgnoreCase(type))
      SALTCHARS = "abcdefghijklmnopqrstuvwxyz";
    else if ("NUMERIC".equalsIgnoreCase(type))
      SALTCHARS = "0123456789";

    StringBuilder salt = new StringBuilder();
    Random rnd = new Random();
    while (salt.length() < length) {
      int index = (int) (rnd.nextFloat() * SALTCHARS.length());
      salt.append(SALTCHARS.charAt(index));
    }

    String saltStr = salt.toString();
    return saltStr;
  }

  /**
   * To set past or future date
   * 
   * @param days - future/past/current
   * @param dateFieldLocator - locator of the date field to set the date
   * @param nofOfDays - No of Days to add or Subtract
   * @param noOfYear - No of Years to add or subtract
   * @throws ParseException
   * 
   */
  public static String setDate(String days, WebElement dateFieldLocator, int nofOfDays,
      int noOfYear) throws ParseException {

    String pastAndFutureDate = null;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Date date = new Date();
    String stringDate = sdf.format(date);
    Date d = sdf.parse(stringDate);
    Calendar cal = Calendar.getInstance();
    cal.setTime(d);
    if (days.equalsIgnoreCase("future")) {
      cal.add(Calendar.DATE, +nofOfDays); // number of days to add
      cal.add(Calendar.YEAR, +noOfYear);
      pastAndFutureDate = sdf.format(cal.getTime());
      dateFieldLocator.sendKeys(pastAndFutureDate);
    } else if (days.equalsIgnoreCase("past")) {
      cal.add(Calendar.DATE, -nofOfDays); // number of days to minus
      cal.add(Calendar.YEAR, -noOfYear);
      pastAndFutureDate = sdf.format(cal.getTime());
      dateFieldLocator.sendKeys(pastAndFutureDate);
    } else if (days.equalsIgnoreCase("current")) {
      dateFieldLocator.sendKeys(stringDate);
      pastAndFutureDate = stringDate;
    }
    return pastAndFutureDate;
  }

  /**
   * Returns a random integer number in given range.
   * 
   * @param min
   * @param max
   * @return the random integer number in given range
   */
  public static int getRandomNumberBetween(int min, int max) {
    return new Random().nextInt(max) + min;
  }

  /**
   * To verify String Present in Array
   * 
   * @param arr
   * @param targetValue
   * 
   */
  public static boolean verifyStringPresentInArray(String[] arr, String targetValue) {
    for (String s : arr) {
      if (s.equals(targetValue))
        return true;
    }
    return false;
  }

  /**
   * To check background color of given element
   * 
   * @param elementToCheck - WebElement that we are checking
   * @param desiredColor - hex value of a color
   * @return true if the desired color matches actual color
   * @throws Exception -
   */
  public static boolean checkBackgroundColor(WebElement elementToCheck, String desiredColor)
      throws Exception {
    boolean flag = false;
    try {
      String color = elementToCheck.getCssValue("background-color");
      String actualColor = convertColorFromRgbaToHex(color);

      flag = actualColor.equalsIgnoreCase(desiredColor);
    } catch (NoSuchElementException ex) {
      Log.exception(ex);
    }
    return flag;
  }

  /**
   * To check color of given element
   * 
   * @param elementToCheck - WebElement that we are checking
   * @param desiredColor - hex value of a color
   * @return true if the desired color matches actual color
   * @throws Exception -
   */
  public static boolean checkColor(WebElement elementToCheck, String desiredColor)
      throws Exception {
    boolean flag = false;
    try {
      String color = elementToCheck.getCssValue("background-color");
      String actualColor = convertColorFromRgbaToHex(color);
      flag = actualColor.equalsIgnoreCase(desiredColor);
    } catch (NoSuchElementException ex) {
      Log.exception(ex);
    }
    return flag;
  }

  /**
   * To convert color of an element from rgba to hex
   * 
   * @param color -
   * @return String of hex value
   */
  public static String convertColorFromRgbaToHex(String color) {
    String[] hexValue = color.replaceAll("[^,0-9]", "").split(",");

    int hexValue1 = Integer.parseInt(hexValue[0]);
    hexValue[1] = hexValue[1].trim();
    int hexValue2 = Integer.parseInt(hexValue[1]);
    hexValue[2] = hexValue[2].trim();
    int hexValue3 = Integer.parseInt(hexValue[2]);

    String actualColor = String.format("#%02x%02x%02x", hexValue1, hexValue2, hexValue3);

    return actualColor;
  }

  /**
   * This method used to select the value from the drop down from any select from
   * 
   * @author purendra.agrawal
   * @param driver
   * @param element
   * @param selectFrom - Should be Index, value and visibleText only
   * @param valueToSelect
   * @throws Exception
   */
  public static void selectFromDropDown(WebDriver driver, WebElement element, String selectFrom,
      String valueToSelect) throws Exception {
    Select dropDownSelect = new Select(element);
    try {
      if (selectFrom.equalsIgnoreCase("index")) {
        int index = Integer.parseInt(valueToSelect);
        dropDownSelect.selectByIndex(index);
      } else if (selectFrom.equalsIgnoreCase("value")) {
        dropDownSelect.selectByValue(valueToSelect);
      } else if (selectFrom.equalsIgnoreCase("visibleText")) {
        dropDownSelect.selectByVisibleText(valueToSelect);
      } else {
        throw new Exception("selectFrom value should be index/value/visibleText");
      }
    } catch (Exception e) {
      throw new Exception("There is an issue while selecting the values from drop down" + e);
    }
  }

  /**
   * This method used to select the value from the radio buttons
   * 
   * @author purendra.agrawal
   * @param driver
   * @param elementBy
   * @param selectedValue
   */
  public static void selectFromRadioButtons(WebDriver driver, By elementBy, String selectedValue) {
    List<WebElement> radioElements = driver.findElements(elementBy);
    for (WebElement element : radioElements) {
      if (element.getText().equalsIgnoreCase(selectedValue)) {
        element.click();
        break;
      }
    }
  }

  /**
   * This method used to select from the drop down and the HTML drop down starts from "span" instead
   * of "select". Here you have to select from the value only not from index.
   * 
   * @author purendra.agrawal
   * @param driver
   * @param cssOfDropDownMenu
   * @param selectedValue
   */
  public static void selectFromSpanDropDown(WebDriver driver, String cssOfDropDownMenu,
      String selectedValue) {
    String cssOfDropDownValues = cssOfDropDownMenu.split("-")[0].concat("-menu").concat(">li");
    By dropDownMenuBy = By.cssSelector(cssOfDropDownMenu);
    WaitUtils.waitForElementPresent(driver, dropDownMenuBy,
        "DropDown does not found of" + cssOfDropDownMenu);
    By dropDownValuesBy = By.cssSelector(cssOfDropDownValues);
    driver.findElement(dropDownMenuBy).click();
    WaitUtils.waitForElementPresent(driver, dropDownValuesBy,
        "DropDown does not found of" + cssOfDropDownValues);
    List<WebElement> dropDownValuesList = driver.findElements(dropDownValuesBy);
    for (WebElement dropDownValuesElement : dropDownValuesList) {
      if (dropDownValuesElement.getText().equalsIgnoreCase(selectedValue)) {
        dropDownValuesElement.click();
        break;
      }
    }
  }

  /**
   * This method used to select from the drop down and the HTML drop down starts from "span" instead
   * of "select". Here you have to select from the index only not from value.
   * 
   * @author purendra.agrawal
   * @param driver
   * @param cssOfDropDownMenu
   * @param index
   */
  public static void selectFromSpanDropDown(WebDriver driver, String cssOfDropDownMenu, int index) {
    By dropDownMenuBy = By.cssSelector(cssOfDropDownMenu);
    WaitUtils.waitForElementPresent(driver, dropDownMenuBy,
        "DropDown does not found of" + cssOfDropDownMenu);
    driver.findElement(dropDownMenuBy).click();
    String indexInString = Integer.toString(index);
    String cssOfDropDownValues = cssOfDropDownMenu.split("-")[0].concat("-menu")
        .concat(">li:nth-child(").concat(indexInString).concat(")");
    driver.findElement(By.cssSelector(cssOfDropDownValues)).click();
  }

  /**
   * It would create a random name
   * 
   * @author purendra.agrawal
   * @return
   */
  public static String generateRandomName() {
    int leftLimit = 97; // letter 'a'
    int rightLimit = 122; // letter 'z'
    int targetStringLength = 10;
    Random random = new Random();
    StringBuilder buffer = new StringBuilder(targetStringLength);
    for (int i = 0; i < targetStringLength; i++) {
      int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
      buffer.append((char) randomLimitedInt);
    }
    String generatedString = buffer.toString();
    return generatedString;
  }

  /**
   * This method creates a unique email with the timestamp.
   * 
   * @author purendra.agrawal
   * @return
   */
  public static String getEmail() {
    Calendar cal = new GregorianCalendar();
    int month = cal.get(Calendar.MONTH); // 4
    month = month + 1;
    int year = cal.get(Calendar.YEAR); // 2013
    int sec = cal.get(Calendar.SECOND);
    int min = cal.get(Calendar.MINUTE);
    int date = cal.get(Calendar.DATE);
    int day = cal.get(Calendar.HOUR_OF_DAY);
    StringBuffer strbuf = new StringBuffer();
    strbuf.append("test").append(year).append(date).append(month).append(day).append(min)
        .append(sec).append("@ssp-uk.com");
    String emailId = strbuf.toString();
    return emailId;
  }

  /**
   * This method convert the String to pound
   * 
   * @author purendra.agrawal
   * @param money
   * @return
   */
  public static String convertIntoPound(String money) {
    if (money == null || money.trim().equalsIgnoreCase(""))
      return "";
    money = "£" + money;
    return money;
  }

  /**
   * This method upload the files in window upload
   * 
   * @author purendra.agrawal
   * @param fileName
   * @throws AWTException
   * @throws InterruptedException
   */
  public static void uploadFilesInWindow(String fileName)
      throws AWTException, InterruptedException {
    StringSelection ss = new StringSelection(fileName);
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
    Robot robot = new Robot();
    Thread.sleep(4000);
    robot.keyPress(KeyEvent.VK_ENTER);
    robot.keyRelease(KeyEvent.VK_ENTER);
    robot.keyPress(KeyEvent.VK_CONTROL);
    robot.keyPress(KeyEvent.VK_V);
    robot.keyRelease(KeyEvent.VK_V);
    robot.keyRelease(KeyEvent.VK_CONTROL);
    robot.keyPress(KeyEvent.VK_ENTER);
    robot.keyRelease(KeyEvent.VK_ENTER);
  }

  /**
   * This method validates the values with the actual value and the element is disabled.
   * 
   * @param element
   * @param actualText
   * @return
   */
  public static boolean isDisabledAndValidateValue(WebElement element, String actualText) {
    if (!element.isEnabled())
      if (element.getAttribute("value").equalsIgnoreCase(actualText))
        return true;
    return false;
  }

  public static ArrayList<String> getDocumentsListDelimiter(String documents, String delimiter) {
    String[] documentsArray = documents.split(delimiter);
    ArrayList<String> documentsList = new ArrayList<String>(Arrays.asList(documentsArray));
    return documentsList;
  }

  public static boolean validateFileExtension(String extension, String[] fileExtensionTypes) {
    HashSet<String> expectedFilesExtensionSet = new HashSet<>(Arrays.asList(fileExtensionTypes));
    if (expectedFilesExtensionSet.contains(extension.toLowerCase()))
      return true;
    return false;
  }

  public static boolean validateFieldIsMandatory(WebElement element) {
    if (element.getText().equals("*"))
      return true;
    else
      return false;
  }

  // Addition and subtraction of dates.
  public static String getNewDateFromNumberOfDays(String format, int numberOfDays)
      throws Exception {
    try {
      LocalDate date = LocalDate.now().plusDays(numberOfDays);
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
      String newDate = date.format(formatter);
      return newDate;
    } catch (DateTimeException e) {
      throw new Exception(
          "Illegal format. Please check the formats at: https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html "
              + e);
    }
  }

  public static String getNewDateFromNumberOfDays(String format, String numberOfDaysInString)
      throws Exception {
    try {
      int numberOfDays = Integer.parseInt(numberOfDaysInString);
      LocalDate date = LocalDate.now().plusDays(numberOfDays);
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
      String newDate = date.format(formatter);
      return newDate;
    } catch (DateTimeException e) {
      throw new Exception(
          "Illegal format. Please check the formats at: https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html "
              + e);
    }
  }

  // • View/Validate text - Text values should be sent from external excel file/global variables.
  // These would be compared with run time values on UI.
  public static boolean validateTextWithExpectedValue(WebElement element, String expectedText,
      ExtentTest extentReport) {
    String actualText = element.getText();
    Log.message("Actual Text fetched from the element is " + actualText, extentReport);
    if (actualText.equals(expectedText)) {
      Log.message("Actual text matched with the expected text", extentReport);
      return true;
    }
    Log.message("Actual text did not matches with the expected text", extentReport);
    return false;
  }

  public static boolean validateTextWithExpectedValue(WebDriver driver, By locator,
      String expectedText, ExtentTest extentReport) {
    WebElement element = driver.findElement(locator);
    String actualText = element.getText();
    Log.message("Actual Text fetched from the element is " + actualText, extentReport);
    if (actualText.equals(expectedText)) {
      Log.message("Actual text matched with the expected text", extentReport);
      return true;
    }
    Log.message("Actual text did not matches with the expected text", extentReport);
    return false;
  }

  /**
   * Returns the directory where the attachments are provided. This method is only used in add
   * attachment or delete attachment feature.
   * 
   * @author purendra.agrawal
   */
  public static String getAttachementsDirectory() {
    String workingDirectory = System.getProperty("user.dir");
    String attachementDirectory = workingDirectory
        .concat("\\src\\main\\resources\\testdata\\data\\featureAttachementDeleteData\\");
    return attachementDirectory;
  }

  /**
   * Create the file with the file extension user have provided
   * 
   * @author purendra.agrawal
   * @param pathWithFileName - Enter the absolute path of the file name
   * @return
   * @throws IOException
   */
  public static boolean createFile(String pathWithFileName) throws IOException {
    File file = new File(pathWithFileName);
    boolean flag = file.createNewFile();
    FileWriter writer = new FileWriter(pathWithFileName, true);
    writer.write("This file is created using automation");
    writer.write("\r\n"); // write new line
    writer.write("Good Bye!");
    writer.close();
    return flag;
  }

  /**
   * Deletes the file from the user system
   * 
   * @author purendra.agrawal
   * @param pathWithFileName - Enter the absolute path of the file
   * @return
   * @throws IOException
   */
  public static boolean deleteFile(String pathWithFileName) throws IOException {
    File file = new File(pathWithFileName);
    return file.delete();
  }

  

}
