package DBHandling;

import java.sql.*;

public class DataBaseHandling {

    public Connection con = null;

    public void SetConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("Driver Initialized Successfully");
            String connectionUrl = "jdbc:sqlserver://localhost;database=HMM;";
            con = DriverManager.getConnection(connectionUrl, "sa", "sa123");
        } catch (SQLException se) {
            System.err.println("Unable to Connect" + se.toString());
        } catch (Exception e) {
            System.err.println("Exception" + e.toString());
        }

    }
}
