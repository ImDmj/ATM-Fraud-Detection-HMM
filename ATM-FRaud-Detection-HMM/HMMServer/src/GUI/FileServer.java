
package GUI;


import java.io.IOException;
import java.net.ServerSocket;

public class FileServer implements Runnable {

    ServerSocket servsock;
    static int port = 9001;

    public static void main(String[] args) throws IOException {
       
    }

    @Override
    public void run() {
        FileFtpServer.FileServerDaemon.main(new String[]{"ServerConfig.cfg", String.valueOf(port)});
    }


  
   
}
