
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class History extends DataBaseHandling {

    public static History obj = null;
    ArrayList<HistorySeq> al = new ArrayList();
    public String CardNo;
    public Thread timer = null;
    public int t = 0;

    public History() {
        starttimer();
    }

    public void Con() {
        super.SetConnection();
    }

    void starttimer() {
        timer = new Thread() {

            @Override
            public void run() {
                try {
                    t = 0;
                    while (true) {
                        Thread.sleep(1000);
                        t = t + 1;
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(History.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };
        timer.start();
    }

    public void stoptime() {
        timer.stop();
    }

    public void createSequence(String type, String desc) {
        HistorySeq obj1 = new HistorySeq();
        obj1.setSequenceOp(desc);
        obj1.setType(type);
        al.add(obj1);
    }

    public String[] getSequence(String accno) {
        String[] seq = new String[al.size()];

        for (int i = 0; i < al.size(); i++) {
            seq[i] = chkHistory(al.get(i).getSequenceOp(), al.get(i).getType(), accno);
            System.out.println(seq[i] + " " + al.get(i).getSequenceOp() + " " + al.get(i).getType());
        }
        return seq;
    }

    public String chkHistory(String desc, String type, String accno) {
        String sql = "";
        String data = "FROUD";
        try {
            sql = "select * from HistoryTran where  Ac_number=" + accno + " and type='" + type + "'";
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                if (type.equals("time")) {
                    int time = Integer.parseInt(rs.getString("SequenceOp"));
                    if (Integer.parseInt(desc) >= time - 10 && Integer.parseInt(desc) <= time + 10) {
                        data = "OK";
                    }
                } else {
                    String data1 = rs.getString("SequenceOp").trim();
                    if (data1.equals(desc.trim())) {
                        data = "OK";
                    }
                }

            }
        } catch (Exception e) {
            System.out.println("Error in CardNumber " + e.getMessage());
        }
        //    System.out.println("acno=" + data);
        return data;
    }

    public String saveSequence(String accno) {
        String sql = "";
        String data = "";
        try {

            String Seq[] = getSequence(accno);
            for (int i = 0; i < Seq.length; i++) {
                if (Seq[i].equals("FROUD")) {
                    sql = "INSERT INTO HistoryTran (SequenceOp ,Type ,Ac_number)  VALUES  ( '" + al.get(i).getSequenceOp() + "', '" + al.get(i).getType() + "' ," + accno + ")";
                    Statement stat = con.createStatement();
                    stat.executeUpdate(sql);
                }

                System.out.print(Seq[i] + " " + al.get(i).getSequenceOp() + " " + al.get(i).getType());
            }

        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }
        System.out.println("acno=" + data);
        return "FROUD";
    }

    public boolean froudDetect(int acc_no) {
        try {
            System.out.println("Froud Detect :");
            double dd = CreditCard.mainLogic(getSequence(String.valueOf(acc_no)));
            if (dd < 0.5) {
                SecurityCode S = new SecurityCode();
                S.Con();
                String mno = S.GetMobileNo(acc_no);
                String SC = "";// S.GetSecurityCode(Integer.parseInt(accono));
                SC = S.ChangeSecurityCode(acc_no, mno);
                SendSMS.MAIN(mno, "Your Security Code :" + SC);
                return true;
            } else {
                return false;
            }
        } catch (Exception err) {
            System.out.println("Froud Detection Error :" + err.toString());
        }
        return false;
    }
}
