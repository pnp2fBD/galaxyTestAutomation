package com.ssp.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

/**
 * Data Provider to get the test data from xml
 */
public class DataProviderUtils {
  @DataProvider(parallel = true, name = "ssBVTDataProvider")
  public static Iterator<Object[]> DataProvider(ITestContext context) throws IOException {

    List<Object[]> dataToBeReturned = new ArrayList<Object[]>();
    List<String> browsers =
        Arrays.asList(context.getCurrentXmlTest().getParameter("browserName").split(","));
    System.out.println("Arrays of browser"
        + Arrays.asList(context.getCurrentXmlTest().getParameter("browserName").split(",")));
    for (String browser : browsers) {
      dataToBeReturned.add(new Object[] {browser});
    }

    return dataToBeReturned.iterator();
  }
}
