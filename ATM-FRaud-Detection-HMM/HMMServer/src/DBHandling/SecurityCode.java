package DBHandling;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SecurityCode extends DataBaseHandling {

    public void Con() {
        super.SetConnection();
    }

    public String GetMobileNo(int ac_no) {
        String pno = "";
        System.out.println("acno=" + ac_no);
        try {

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select phone_number from customer where ac_number=" + ac_no);
            if (rs.next()) {
                pno = rs.getString(1);
            }

            System.out.println("mobile No=" + pno);

        }//try
        catch (Exception e) {
            System.out.println("Error GetMobileNo=" + e.getMessage());
        }

        return (pno);
    }

    public String GetSecurityCode(int ac_no) {
        String SC = "";
        try {

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("select SecurityCode from Account where ac_number=" + ac_no);
            if (rs.next()) {
                SC = rs.getString(1);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return SC;
    }

    public String ChangeSecurityCode(int ac_no,String mno) {
        java.util.Random r = new java.util.Random();

        String SC = "";
        int Scd;
        try {
            Scd = r.nextInt();
            System.out.println("New Security Code=" + Scd);
            if (Scd < 0) {
                Scd = Scd * -1;
            }

            SC = String.valueOf(Scd);
            Statement stat = con.createStatement();
            stat.executeUpdate("update Account set SecurityCode='" + SC + "' where ac_number=" + ac_no);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return SC;
    }
    public void CloseConnection()
    {
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(SecurityCode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
