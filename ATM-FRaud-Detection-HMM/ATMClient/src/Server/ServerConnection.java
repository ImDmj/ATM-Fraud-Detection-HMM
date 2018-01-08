package Server;

import GUI.GeneratePDF;
import GUI.MainFrame;
import java.net.*;
import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerConnection implements Runnable {

    private static final int port = 6564;
    private BufferedReader in;
    private PrintWriter out;
    private String id = null;

    public ServerConnection(String site) throws IOException {
        Socket server = new Socket(site, port);
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        out = new PrintWriter(server.getOutputStream(), true);
    }

    public String getId() {
        return id;
    }

    private String readline() {
        try {
            return in.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    public void SendCommand(String cmd) {
        out.println(cmd);
    }
    private Thread t;

    public void start() {
        t = new Thread(this);
        t.start();
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
    private static final int EMAIL = 10;
    private static Hashtable keys = new Hashtable();
    private static String keystrings[] = {"", "id", "rfid", "balance", "checkpin", "checksecuritycode", "withdraw", "print", "changepin", "checkfraud","email"};

    static {
        for (int i = 0; i < keystrings.length; i++) {
            keys.put(keystrings[i], new Integer(i));
        }
    }

    private int lookup(String s) {
        Integer i = (Integer) keys.get(s);
        System.out.println("i=" + i);
        return i == null ? -1 : i.intValue();
    }

    public void run() {
        String s;
        StringTokenizer st;
        while ((s = readline()) != null) {
            System.out.println(s);
            st = new StringTokenizer(s);
            String keyword = st.nextToken();
            switch (lookup(keyword)) {
                default:
                    System.out.println("Invalid keywords:   " + keyword + "\r");
                    break;
                case ID:
                    id = st.nextToken();
                    break;
                case RFID: {
                    if (st.hasMoreTokens()) {
                        try {
                            MainFrame.Acc_no = st.nextToken();
                            FileClient.FtpConnect cn;
                            FileClient.Ftp cl;
                            cn = FileClient.FtpConnect.newConnect("ftp://" + MainFrame.ServerIP
                                    + ":21");
                            cn.setUserName("Admin");
                            cn.setPassWord("Admin");
                            cl = new FileClient.Ftp();
                            if (cl.connect(cn)) {
                                FileClient.CoFile file = new FileClient.FtpFile(MainFrame.mainobj.RFIDNumber + ".fpt", cl);
                                FileClient.CoFile to = new FileClient.LocalFile("C:\\Client\\" + MainFrame.mainobj.RFIDNumber + ".fpt");
                                FileClient.CoLoad.copy(to, file);
                            }
                            //byte[] bb=(st.nextToken().trim()).getBytes();                        
                            //MainFrame.data = bb;
                            MainFrame.mainobj.ChangeMsg(0);
                        } catch (IOException ex) {
                            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                break;
                case CHECKPIN: {
                    String msg = st.nextToken();
                    if (msg.toLowerCase().equals("ok")) {
                        MainFrame.mainobj.ChangeMsg(3);
                    } else {
                        MainFrame.mainobj.ChangeMsg(2);
                    }
                }

                break;
                case EMAIL:
                {
                    String emailid=st.nextToken();
                    System.out.println("Received EmailID " + emailid);
                }
            break;
                case BALANCE: {
                    String bal = st.nextToken();
                    MainFrame.Bal = bal;
                    MainFrame.mainobj.ChangeMsg(4);
                }
                break;

                case CHANGEPIN: {
                    String msg = st.nextToken();
                    if (msg.toLowerCase().equals("ok")) {
                        MainFrame.mainobj.ChangeMsg(8);
                    }
                }
                break;

                case CHECKSECURITYCODE: {
                    String msg = st.nextToken();
                    if (msg.toLowerCase().equals("no")) {
                        MainFrame.mainobj.ChangeMsg(7);
                    }
                }
                break;

                case WITHDRAW: {
                    String msg = st.nextToken();
                    if (msg.toLowerCase().equals("ok")) {
                        MainFrame.mainobj.ChangeMsg(6);
                    }
                }
                break;

                case PRINT: {
                    String data = s;
                    GeneratePDF pdf = new GeneratePDF(data);
                    String cmd = "cmd /start /c print.pdf";
                    try {
                        Runtime.getRuntime().exec(cmd);
                    } catch (IOException ex) {
                        Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    MainFrame.mainobj.Reload();
                }

                break;
                case CHECKFRAUD: {
                    String msg = st.nextToken();
                    if (msg.toLowerCase().equals("yes")) {
                        MainFrame.mainobj.ChangeMsg(5);
                    }
                }

                break;
            }
        }
    }
}