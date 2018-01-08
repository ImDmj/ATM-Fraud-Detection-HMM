package DBHandling;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PinCode extends DataBaseHandling {

    public void Con() {
        super.SetConnection();
    }

    public boolean change_pin_code(int ac_no, String old_pin, String new_pin) {
        String pin;
        String sql = "";
        boolean flag = false;
        try {
            sql = "select pin_code from Account where ac_number=" + ac_no;
            System.out.println(sql);
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            if (rs.next()) {
                pin = rs.getString("pin_code");
                if (pin.equals(old_pin)) {
                    sql = "update account set pin_code = '" + new_pin + "' where ac_number = " + ac_no;
                    System.out.println(sql);
                    stat.executeUpdate(sql);
                    flag = true;
                } else {
                    flag = false;
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return flag;
    }
    public void CloseConnection()
    {
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(PinCode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
