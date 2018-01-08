import java.net.*;
import java.util.*;
import java.io.*;

class ClientConnection implements Runnable {

    private Socket sock;
    private BufferedReader in;
    private OutputStream out;
    private MyServer server;
    private MainFrame serverPanel;
    private static final String CRLF = "\r\n";
    private String id;

    public ClientConnection(MyServer srv, Socket s, int i, MainFrame serverPanel) {
        try {
            System.out.println("Inside ClientConnection.constructor");
            this.serverPanel = serverPanel;
            server = srv;
            sock = s;
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = s.getOutputStream();
            id = "" + i;
            write("id " + id + CRLF);
            new Thread(this).start();
        } catch (IOException e) {
            System.out.println("failed clientconnection " + e);
        }
    }

    public String getId() {
        return id;
    }

    public void close() {
        try {
            sock.close();
        } catch (IOException e) {
        }
    }

    public void write(String s) {
        byte buf[];
        buf = s.getBytes();
        try {
            out.write(buf, 0, buf.length);
        } catch (IOException e) {
            close();
        }
    }

    public String readline() {
        try {
            return in.readLine();
        } catch (IOException e) {
            return null;
        }
    }
    private static final int ID = 1;
    private static final int RFID = 2;
    private static final int BALANCE = 3;
    private static final int CHECKPIN = 4;
    private static final int CHECKSECURITYCODE = 5;
    private static final int WITHDRAW = 6;
    private static final int PRINT = 7;
    private static final int CHANGEPIN = 8;
    private static final int CHECKFRAUD = 9;
    private static final int EMAIL=10;
    static private Hashtable keys = new Hashtable();
    static private String keysStrings[] = {"", "id", "rfid", "balance", "checkpin", "checksecuritycode", "withdraw", "print", "changepin", "checkfraud","email"};

    static {
        for (int i = 0; i < keysStrings.length; i++) {
            keys.put(keysStrings[i], new Integer(i));
        }
    }

    private int lookup(String s) {
        Integer i = (Integer) keys.get(s);
        return i == null ? -1 : i.intValue();
    }

    public void run() {
        String s;
        StringTokenizer st;
        while ((s = readline()) != null) {
            System.out.println("command : " + s);
            if (!s.equals("")) {
                st = new StringTokenizer(s, " ");
                String keyword = st.nextToken();

                switch (lookup(keyword)) {
                    default:
                        System.out.println("Invalid keywords:   " + keyword + "\r");
                        break;
                    case ID:
                        id = st.nextToken();
                        serverPanel.jTextArea1.append("New Client Connect....\n");
                        break;
                    case RFID: {
                        String cid = st.nextToken();
                        String RFIDNo = st.nextToken();
                        serverPanel.jTextArea1.append("Get Account Number Request of :" + RFIDNo + "\n");
                        UserAuthetication l = new UserAuthetication();
                        l.Con();
                        String accno = l.GetAccNo(RFIDNo);
                        l.CloseConnection();
                        //l.Con();
                        //byte[] data = l.GetThumbData(accno);
                        //l.CloseConnection();
                        String body = "rfid " + accno;
                        server.send(cid, body);
                    }
                    break;
                    case CHECKPIN: {
                        String cid = st.nextToken();
                        String accno = st.nextToken();
                        String pinno = st.nextToken();
                        serverPanel.jTextArea1.append("Check Pin Request of :" + accno + "\n");
                        UserAuthetication l = new UserAuthetication();
                        l.Con();
                        boolean f = l.lcheck(Integer.parseInt(accno), pinno);
                        l.CloseConnection();
                        String body;
                        if (f) {
                            body = "checkpin ok";
                        } else {
                            body = "checkpin error";
                        }
                        server.send(cid, body);
                    }
                    break;

                    case BALANCE: {
                        String cid = st.nextToken();
                        String accno = st.nextToken();
                        serverPanel.jTextArea1.append("Check Balance Request of :" + accno + "\n");
                        Transaction tr = new Transaction();
                        tr.Con();
                        int bal = tr.check_balance(Integer.parseInt(accno));
                        String body = "balance " + bal;
                        server.send(cid, body);
                    }
                    break;
                    case CHANGEPIN: {
                        String cid = st.nextToken();
                        String accno = st.nextToken();
                        String pinno = st.nextToken();
                        String newpinno = st.nextToken();
                        serverPanel.jTextArea1.append("Change Pin Request of :" + accno + "\n");
                        PinCode pin = new PinCode();
                        pin.Con();
                        boolean f = pin.change_pin_code(Integer.parseInt(accno), pinno, newpinno);
                        pin.CloseConnection();
                        String body;
                        if (f) {
                            body = "changepin ok";
                        } else {
                            body = "changepin error";
                        }
                        server.send(cid, body);

                    }
                    break;

                    case CHECKSECURITYCODE: {
                        String body;
                        String cid = st.nextToken();
                        String accno = st.nextToken();
                        String ScCode = st.nextToken();
                        String Amount = st.nextToken();
                        serverPanel.jTextArea1.append("Check Flash Code Request of :" + accno + "\n");
                        SecurityCode sc = new SecurityCode();
                        sc.Con();
                        String oldsc = sc.GetSecurityCode(Integer.parseInt(accno));
                        sc.CloseConnection();
                        if (ScCode.equals(oldsc)) {
                            Transaction tr = new Transaction();
                            tr.Con();
                            boolean f = tr.withdraw_transaction(Integer.parseInt(accno), Integer.parseInt(Amount));
                            if (f) {
                                body = "withdraw ok";
                            } else {
                                body = "withdraw error";
                            }
                        } else {
                            body = "checksecuritycode no";
                        }
                        server.send(cid, body);
                    }

                    break;
                    case EMAIL:
                    {
                        st.nextToken();
                        String rfid=st.nextToken();
                        UserAuthetication l = new UserAuthetication();
                        l.Con();
                        String email = l.GetEmailID(rfid);
                        l.CloseConnection();
                        String body = "email " + email;
                        server.send(id, body);
                        System.out.println("send email " + email);
                    }break;
                    case WITHDRAW: {
                        String body;
                        String cid = st.nextToken();
                        String accno = st.nextToken();
                        String Amount = st.nextToken();
                        serverPanel.jTextArea1.append("Withdraw Amount Request of :" + accno + "\n");
                        Transaction tr = new Transaction();
                        tr.Con();
                        boolean f = tr.withdraw_transaction(Integer.parseInt(accno), Integer.parseInt(Amount));
                        if (f) {
                            body = "withdraw ok";
                        } else {
                            body = "withdraw error";
                        }
                        server.send(cid, body);
                    }

                    break;

                    case PRINT: {
                        String body;
                        String cid = st.nextToken();
                        String accno = st.nextToken();
                        serverPanel.jTextArea1.append("Mini Statement Request of :" + accno + "\n");
                        Transaction tr = new Transaction();
                        tr.Con();
                        body = "print " + tr.GetMiniStatement(Integer.parseInt(accno));
                        server.send(cid, body);
                    }

                    break;
                    case CHECKFRAUD: {
                        String body;
                        String cid = st.nextToken();
                        String accno = st.nextToken();
                        String Amount = st.nextToken();
                        String CardNo = st.nextToken();
                        History.obj = new History();
                        History.obj.CardNo = CardNo;
                        History.obj.Con();
                        History.obj.createSequence("withdraw", Amount);
                        boolean froud = History.obj.froudDetect(Integer.parseInt(accno));
                        if (froud) {
                            History.obj.createSequence("time", String.valueOf(History.obj.t));
                            History.obj.createSequence("SEQ", "withdraw:time:");
                            History.obj.saveSequence(accno);
                            History.obj.stoptime();
                            History.obj = null;
                            body = "checkfraud yes";
                        } else {
                            Transaction tr = new Transaction();
                            tr.Con();
                            boolean f = tr.withdraw_transaction(Integer.parseInt(accno), Integer.parseInt(Amount));
                            if (f) {
                                body = "withdraw ok";
                            } else {
                                body = "withdraw error";
                            }
                        }
                        server.send(cid, body);
                    }
                    break;
                }

            }
        }
        close();
    }
}
