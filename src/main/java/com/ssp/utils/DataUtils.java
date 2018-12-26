package com.ssp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.regex.Pattern;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Utils consists finding Row and Column count,Reading excel headers, Retrieving testdata id
 * and Fetching testdata values
 */
public class DataUtils {
  private static final Logger logger = LoggerFactory.getLogger(DataUtils.class);

  /**
   * FindRowColumnCount method to get total no of row and column count in a excel work sheet
   * 
   * @param sheet name
   * @param rowColumnCount as Hashtable
   * @return Hashtable (returns row count and column count)
   */

  public static Hashtable<String, Integer> findRowColumnCount(HSSFSheet sheet,
      Hashtable<String, Integer> rowColumnCount) {

    HSSFRow row = null;
    int rows;
    rows = sheet.getPhysicalNumberOfRows();
    int cols = 0;
    int tmp = 0;
    int counter = 0;
    String temp = null;

    for (int i = 0; i < 10 || i < rows; i++) {
      row = sheet.getRow(i);
      if (row != null) {
        temp = convertHSSFCellToString(row.getCell(0));
        if (!temp.isEmpty()) {
          counter++;
        }
        tmp = sheet.getRow(i).getPhysicalNumberOfCells();
        if (tmp > cols) {
          cols = tmp;
        }
      }
    }

    rowColumnCount.put("RowCount", counter);
    rowColumnCount.put("ColumnCount", cols);

    return rowColumnCount;
  }

  /**
   * ReadExcelHeaders method read the excel headers column wise sheet
   * 
   * @param sheet name
   * @param excelHeaders (Hashtable)
   * @param rowColumnCount (Hashtable)
   * @return excelHeaders (returns Header column values)
   */
  public static Hashtable<String, Integer> readExcelHeaders(HSSFSheet sheet,
      Hashtable<String, Integer> excelHeaders, Hashtable<String, Integer> rowColumnCount) {

    HSSFRow row = null;
    HSSFCell cell = null;
    for (int r = 0; r < rowColumnCount.get("RowCount"); r++) {
      row = sheet.getRow(r);

      if (row != null) {
        for (int c = 0; c < rowColumnCount.get("ColumnCount"); c++) {
          cell = row.getCell(c);
          if (cell != null) {
            excelHeaders.put(cell.toString(), c);
          }
        }
        break;
      }
    }
    return excelHeaders;
  }

  /**
   * ConvertHSSFCellToString method to convert the HSSFCell value to its equivalent string value
   * 
   * @param cell value
   * @return String cellValue
   */
  public static String convertHSSFCellToString(HSSFCell cell) {
    String cellValue = null;
    if (cell != null) {
      cellValue = cell.toString();
      cellValue = cellValue.trim();
    } else {
      cellValue = "";
    }
    return cellValue;
  }

  /**
   * To overriding the config sheet name to get test data
   * 
   * @param testCaseId from test case
   * @param testClassName test name
   * @return test data values for specified testCaseId
   */
  public static HashMap<String, String> testDatabyID(String testCaseId, String testClassName) {
    String configSheetName = "Config";
    System.out.println("testCaseId is ---------> " + testCaseId + "\ntestClassName-----------> "
        + testClassName + "\nconfigSheetName -> " + configSheetName);
    return testDatabyID(testCaseId, testClassName, configSheetName);
  }

  /**
   * Map to the test data sheet based on Config declaration
   * 
   * @param testCaseId - actual test case id
   * @param testClassName - test case class name
   * @param configSheetName - config sheet name
   * @return test data values for specified testCaseId
   */
  public static HashMap<String, String> testDatabyID(String testCaseId, String testClassName,
      String configSheetName) {
    String filePath = "";
    String sheetName = "";
    String fileName = "";
    HSSFRow row = null;
    HSSFCell cell = null;
    HashMap<String, String> data = new HashMap<String, String>();
    Hashtable<String, Integer> excelHeaders = new Hashtable<String, Integer>();

    try {

      String basePath = new File(".").getCanonicalPath() + File.separator + "src" + File.separator
          + "main" + File.separator + "resources" + File.separator + "testdata" + File.separator;
      logger.info("data utils base path: " + basePath);
      String configFilePath = basePath + "Config-TD.xls";
      System.out.println("Config-------" + configFilePath);
      POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(configFilePath));
      HSSFWorkbook wb = new HSSFWorkbook(fs);
      HSSFSheet sheet = wb.getSheet(configSheetName);
      System.out.println(sheet);

      // Function call to find excel header fields
      Hashtable<String, Integer> excelrRowColumnCount = new Hashtable<String, Integer>();
      excelrRowColumnCount = findRowColumnCount(sheet, excelrRowColumnCount);
      excelHeaders = readExcelHeaders(sheet, excelHeaders, excelrRowColumnCount);

      // Get test data set
      for (int r = 1; r < excelrRowColumnCount.get("RowCount"); r++) {
        row = sheet.getRow(r);
        if (row != null) {
          HSSFCell tempCell = sheet.getRow(r).getCell(0);
          if (tempCell != null) {
            String testClass = convertHSSFCellToString(row.getCell(0));

            if (testClass.equalsIgnoreCase(testClassName)) {
              cell = sheet.getRow(r).getCell(1);
              if (cell != null) {
                fileName = convertHSSFCellToString(row.getCell(1));
              }
              cell = sheet.getRow(r).getCell(2);
              if (cell != null) {
                sheetName = convertHSSFCellToString(row.getCell(2));
              }
              break;
            }
          }
        }
      }
      filePath = basePath + fileName;
      System.out.println("\nfile path----------> " + filePath);
      data = getTestData(filePath, fileName, sheetName, testCaseId);
      System.out.println("\ndata----------> " + data);
      // return data;
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
    return data;
    // throw new
    // RuntimeException("error: DataUtils couldn't load the data");
  }

  /**
   * Fetch the test data for a test case based on test case ID
   * 
   * @param filePath test data xl location
   * @param workBook name
   * @param sheetName name
   * @param testCaseId test id
   * @return testData data
   * @throws IOException - java IO exception
   */
  public static HashMap<String, String> getTestData(String filePath, String workBook,
      String sheetName, String testCaseId) throws IOException {
    HSSFRow row = null;
    HSSFCell cell = null;
    System.out.println("filePath-------" + filePath + "  " + "workBook------" + workBook + " "
        + "sheetName----" + sheetName + "testCaseId------------" + testCaseId);
    // Establish connection to work sheet
    POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filePath));
    HSSFWorkbook wb = new HSSFWorkbook(fs);
    HSSFSheet sheet = wb.getSheet(sheetName);
    Hashtable<String, Integer> excelrRowColumnCount = new Hashtable<String, Integer>();
    excelrRowColumnCount = findRowColumnCount(sheet, excelrRowColumnCount);

    // function call to find excel header fields
    Hashtable<String, Integer> excelHeaders = new Hashtable<String, Integer>();
    excelHeaders = readExcelHeaders(sheet, excelHeaders, excelrRowColumnCount);
    HashMap<String, String> data = null;
    ArrayList<String> header = new ArrayList<String>();
    ArrayList<String> matcher = null;
    HashMap<String, String> matcherList = new HashMap<String, String>();

    // Get all header
    row = sheet.getRow(0);
    if (row != null) {
      for (int c = 0; c < excelrRowColumnCount.get("ColumnCount"); c++) {
        cell = sheet.getRow(0).getCell(c);
        if (cell != null) {
          String temp = convertHSSFCellToString(row.getCell(c));
          header.add(temp);
        }
      }
    }

    // Get test data set
    for (int r = 1; r < excelrRowColumnCount.get("RowCount"); r++) {
      row = sheet.getRow(r);
      if (row != null) {
        HSSFCell tempCell = sheet.getRow(r).getCell(0);
        if (tempCell != null) {
          String tcID = convertHSSFCellToString(row.getCell(0));
          if (tcID.equalsIgnoreCase(testCaseId)) {
            data = new HashMap<String, String>();
            matcher = new ArrayList<String>();
            matcher.add(tcID);
            for (int c = 1; c < excelrRowColumnCount.get("ColumnCount"); c++) {
              cell = sheet.getRow(r).getCell(c);
              String temp = cellToString(cell);
              matcher.add(temp);
            }
            // Add all the test data to a Map
            for (int i = 0; i < matcher.size(); i++) {
              data.put(header.get(i), matcher.get(i));
            }
            matcherList.putAll(data);
          }
        }
      }
    }

    return matcherList;
  }

  /**
   * Fetch the test data form a cell and convert it to string
   * 
   * @param cell cellValue
   */
  public static String cellToString(HSSFCell cell) {
    Object result;
    switch (cell.getCellType()) {

      case 0: // numeric value in Excel
        result = NumberToTextConverter.toText(cell.getNumericCellValue());
        break;
      case 1: // String Value in Excel
        result = cell.getStringCellValue();
        break;
      case 3: // String Value in Excel
        result = cell.toString();
        break;
      default:
        throw new RuntimeException("There is no support for this type of cell");
    }
    return result.toString();
  }

  public static ArrayList<HashMap<String, String>> getListDataValues(String data) throws Exception {
    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    try {
      int itemidx = 0;
      String[] rows = data.split("\n");

      while (itemidx < rows.length) {
        HashMap<String, String> row = new HashMap<String, String>();
        String[] fields = rows[itemidx].split(Pattern.quote("|"));
        int fieldidx = 0;

        while (fieldidx < fields.length) {
          String[] field = fields[fieldidx].split(":");
          row.put(field[0], field[1]);
          fieldidx++;
        }
        list.add(row);
        itemidx++;
      }

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    } // catch
    return list;
  }

}
