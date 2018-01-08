package DBHandling;

import java.sql.*;

public class Transaction extends DataBaseHandling {

    int balance = 0;

    public void Con() {
        super.SetConnection();
    }

    public boolean check(String ac_no, String amt) {
        boolean flag = false;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from transaction1 where ac_number=" + ac_no + " and amount=" + amt);
            if (rs.next()) {
                flag = true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return flag;
    }

    public boolean withdraw_transaction(int ac_no, int withdrawamt) {
        boolean flag = false;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select balance from Account where ac_number=" + ac_no);
            rs.next();
            balance = rs.getInt("balance");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (withdrawamt > balance) {
            flag = false;
        } else {
            try {
                String sql = null;
                char ttype = 'W';
                int tid = 1;
                Statement st = con.createStatement();
                balance = balance - withdrawamt;
                st.executeUpdate("update Account set balance = " + balance + " where ac_number=" + ac_no);
                ResultSet rs = st.executeQuery("select max(tr_number) from transaction1");
                if (rs.next()) {
                    tid = rs.getInt(1) + 1;
                } else {
                    tid = 1;
                }
                sql = "insert into transaction1 values(" + tid + "," + ac_no + ",'" + new java.util.Date() + "','W'," + withdrawamt + ")";
                st.executeUpdate(sql);
                flag = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return flag;
    }

    public int check_balance(int ac_no) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select balance from Account where ac_number=" + ac_no);
            rs.next();
            balance = rs.getInt("balance");
            return balance;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public String GetMiniStatement(int ac_no) {
        String data = "";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from transaction1 where ac_number=" + ac_no);
            while (rs.next()) {
                data = data + rs.getInt("Amount") + ";" + rs.getString("Date") + ";" + rs.getString("Tr_type") + ";";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return data;
    }
}
