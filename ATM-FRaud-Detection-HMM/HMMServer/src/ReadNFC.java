
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ReadNFC {

    static String NFCCardTagID = "";
    Timer timer;

    public ReadNFC(int seconds) throws IOException {
        Main.initReader();
        timer = new Timer();
        timer.schedule(new RemindTask(), 0, seconds * 1000);
    }

    class RemindTask extends TimerTask {

        public void run() {
            System.out.println("Waiting for Card");
            NFCCardTagID = Main.waitForCard();
            if (!NFCCardTagID.equals("")) {
                timer.cancel(); //Terminate the timer thread
            }
        }
    }

    public static void main(String args[]) throws IOException {
        new ReadNFC(3);
        System.out.println("Task scheduled.");
    }
}