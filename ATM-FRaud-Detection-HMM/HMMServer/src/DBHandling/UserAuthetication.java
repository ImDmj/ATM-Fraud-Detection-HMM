package DBHandling;

import com.digitalpersona.onetouch.verification.DPFPVerificationResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserAuthetication extends DataBaseHandling {

    public void Con() {
        super.SetConnection();
    }

    public String MaxAccNo() {
        String sql = "";
        String data = "";
        try {
            sql = "select max(ac_number) from Account";
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            if (rs.next()) {
                int acc = rs.getInt(1);
                if (acc == 0) {
                    acc = 1001;
                } else {
                    acc = acc + 1;
                }
                data = String.valueOf(acc);
            }
        } catch (Exception e) {
        }
        return data;
    }

    public String GetAccNo(String RFID) {
        String sql = "";
        String data = "";
        try {
            sql = "select ac_number from Account where RFID='" + RFID + "'";
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            if (rs.next()) {
                data = String.valueOf(rs.getInt(1));
            } else {
                System.out.println("No data found");
            }
        } catch (Exception e) {
            System.out.println("Error in GetAccNo " + e.getMessage());
        }
        System.out.println("acno=" + data);
        return data;
    }

    public boolean lcheck(int ac_no, String pin) {
        boolean flag = false;
        try {
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("select * from Account where ac_number=" + ac_no + " and pin_code='" + pin + "'");
            if (rs.next()) {
                flag = true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("flag=" + flag);
        return flag;
    }

    public byte[] GetThumbData(String ac_no) {
        byte[] data = null;
        try {
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("select ThumbData from Customer where ac_number=" + ac_no + "");
            if (rs.next()) {
                data = rs.getBytes(1);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    public boolean AddCustomer(String fullname, String address, String phno, String city, String emailid, String accno, byte[] data) {
        boolean f = false;
        try {
            String sql = "insert into Customer values('" + fullname + "','" + address + "','" + phno + "','" + city + "','" + emailid + "'," + accno + ",?)";
            PreparedStatement stat = con.prepareStatement(sql);
            stat.setBytes(1, data);
            if (stat.executeUpdate() > 0) {
                f = true;
            }
        } catch (Exception err) {
            System.out.println("Customer Add Error :" + err.toString());
        }
        return f;
    }

    public boolean CreateAccount(String accno, String ttype, String bal, String rfid, String Mno) {
        boolean f = false;
        java.util.Random r = new java.util.Random();
        int Scd = r.nextInt();
        System.out.println("New Security Code=" + Scd);
        if (Scd < 0) {
            Scd = Scd * -1;
        }
        String SC = String.valueOf(Scd);
        if (SC.length() > 4) {
            SC = SC.substring(0, 4);
        }
        try {
            Statement stat = con.createStatement();
            String sql = "insert into Account values(" + accno + ",'" + ttype + "','" + SC + "'," + bal + ",'" + rfid + "','0000')";
            if (stat.executeUpdate(sql) > 0) {
                SendSMS.MAIN(Mno, "Your Pin Number is :" + SC);
                f = true;
            }
        } catch (Exception err) {
            System.out.println("Account Add Error :" + err.toString());
        }
        return f;
    }

    public void CloseConnection() {
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(UserAuthetication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
