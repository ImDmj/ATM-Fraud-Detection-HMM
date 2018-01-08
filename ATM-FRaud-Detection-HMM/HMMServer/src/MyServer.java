import java.net.*;
import java.io.*;
import java.util.*;

public class MyServer implements Runnable {

    private Thread th;
    private int port = 6564;
    private Hashtable idcon = new Hashtable();
    private int id = 0;
    static final String CRLF = "\r\n";
    Vector badwords = null;
    boolean shutdown = false;
    private ServerSocket acceptSocket;
    MainFrame serverPanel;

    synchronized void addConnection(Socket s) {
        ClientConnection con = new ClientConnection(this, s, id, serverPanel);
        set(String.valueOf(id), con);
        id++;
        
    }

    public MyServer(MainFrame serverPanel) {
        System.out.println("Inside MyServer constructor");
        this.serverPanel = serverPanel;
    }

    public void startServer() {
        shutdown = false;        
        th = new Thread(this);
        th.start();
    }

    public void shutDown() {
        shutdown = true;
        th.stop();
        th = null;
        try {
            acceptSocket.close();
        } catch (IOException e) {
            System.out.println("Inside MyServer.shutDown close : " + e);
        }
        System.out.println("Inside MyServer.shutDown");
    }

    synchronized void set(String the_id, ClientConnection con) {
        idcon.remove(the_id);
        System.out.println("Inside set id : " + the_id);
        Vector newuser = new Vector(2);
        newuser.insertElementAt(con, 0);
        newuser.insertElementAt("", 1);
        idcon.put(the_id, newuser);
        System.out.println("End of MyServer.set ");
    }

 
    synchronized void send(String id, String body) {
        Vector v = (Vector) idcon.get(id);
        if (v != null) {
            ClientConnection con = (ClientConnection) v.elementAt(0);
            if (con != null) {
                con.write(body + CRLF);               
            }
        }
    }

   

    public void run() {
        try {
            acceptSocket = new ServerSocket(port);
            System.out.println("Server listening on port " + port);
            while (!shutdown) {
                Socket s = acceptSocket.accept();
                addConnection(s);
            }
            acceptSocket = null;
        } catch (IOException e) {
            System.out.println("accept loop IOException 	 :  " + e);
        }
    }
}

