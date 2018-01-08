

import java.sql.*;

public class DataBaseHandling {

    public Connection con = null;
    String UserName = "hmm";
    String Password = "hmm";

    public void SetConnection() {
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521:XE";
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, UserName, Password);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
