package com.ssp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * FileUtils consists copy/move a file from source to destination location
 */
public class FileUtils {
  private static String reportFolderPath;

  /**
   * Copy a file from one location to another
   * 
   * @param f1 - Source file
   * @param f2 - Destination File
   * @throws IOException - java IO exception
   */
  public static void copyFile(File f1, File f2) throws IOException {
    InputStream in = new FileInputStream(f1);

    // For Overwrite the file.
    OutputStream out = new FileOutputStream(f2);

    byte[] buf = new byte[1024];
    int len;
    while ((len = in.read(buf)) > 0) {
      out.write(buf, 0, len);
    }
    in.close();
    out.close();
  }

  public static File createReportFolder(File reportFolder, String env) throws IOException {
    Date now = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyhhmmss");
    String time = dateFormat.format(now);

    reportFolderPath = reportFolder.getParent() + File.separator + env + File.separator
        + "AutomationTestResults_" + time + File.separator;
    reportFolder = new File(reportFolderPath);

    if (!reportFolder.exists()) {
      reportFolder.mkdir();
    }
    return reportFolder;
  }

  public static void copyFolder(File src, File dest) throws IOException {

    if (src.isDirectory()) {

      // if directory not exists, create it
      if (!dest.exists()) {
        dest.mkdir();
        System.out.println("Directory copied from " + src + "  to " + dest);
      }

      // list all the directory contents
      String files[] = src.list();

      for (String file : files) {
        // construct the src and dest file structure
        File srcFile = new File(src, file);
        File destFile = new File(dest, file);
        // recursive copy
        copyFolder(srcFile, destFile);
      }

    } else {
      // if file, then copy it
      // Use bytes stream to support all file types
      InputStream in = new FileInputStream(src);
      OutputStream out = new FileOutputStream(dest);

      byte[] buffer = new byte[1024];

      int length;
      // copy the file content in bytes
      while ((length = in.read(buffer)) > 0) {
        out.write(buffer, 0, length);
      }

      in.close();
      out.close();
      System.out.println("File copied from " + src + " to " + dest);
    }
  }

  /***
   * Method move the file from source location, rename it and place it in the destination location
   * and deletes the old file from the destination location if any
   * 
   * @param oldFile - source file name
   * @param newFile - destination file name
   * @throws IOException - java IO exception
   */
  public static void moveFile(String oldFile, String newFile) throws IOException {
    File oldfile = new File(oldFile);
    File newfile = new File(newFile);
    copyFile(oldfile, newfile);
    oldfile.delete();
  }
}
