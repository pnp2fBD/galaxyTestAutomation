package com.ssp.support;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
// import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

// import java.util.LinkedHashMap;

public class ConnectDB {

  public static Connection connection = null;
  public static Statement statement = null;
  String connSatus = "Initialized";

  // public String connectionString="jdbc:db2://guivmgdb01:50099/HDEXSIT";
  // public String userName="hdrdusr";
  // public String password="R35dtp23";

  public ConnectDB(String connectionString, String userName, String password) throws SQLException {

    // logi.logInfo("Method into db trying to connect")
    boolean connectionClosed = false;
    if (connection != null)
      if (connection.isClosed() == true)
        connectionClosed = true;

    if (connection == null || connectionClosed) {
      statement = null;
      try {
        Class.forName("com.ibm.db2.jcc.DB2Driver");
      } catch (ClassNotFoundException e) {
        throw new SQLException("Database Connection Failed!! " + e.getMessage());
      }

      try {

        connection = DriverManager.getConnection(connectionString, userName, password);

      } catch (SQLException ex) {
        connSatus = "Database Connection Failed!!";
        throw new SQLException(connSatus + ex.getMessage());
        // return;
      }

      if (connection != null) {
        Log.event("DB CONNECTION ESTABLISHED!!!");
      } else {
        Log.event("Failed to make connection!");
      }
    }
  }

  public ResultSet executeQueryP2(String query) throws SQLException {
    try {
      ResultSet rs;
      boolean connectionStatementClosed;
      connectionStatementClosed = false;
      if (statement == null || connectionStatementClosed)
        statement = connection.createStatement();
      rs = statement.executeQuery(query);
      return rs;
    }

    catch (SQLException ex) {
      throw new SQLException("Failed to execute query in DB, exception occured" + ex.getMessage());
    }

  }

}
