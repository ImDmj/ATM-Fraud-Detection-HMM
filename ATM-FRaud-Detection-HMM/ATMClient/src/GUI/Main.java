package GUI;


import NFCLib.CardFactory;
import NFCLib.Conversions;
import NFCLib.ICard;
import java.io.IOException;
import java.util.List;
import javax.smartcardio.*;

public class Main {

    //Handler to card terminal
    private static CardTerminal terminal = null;
    private static CardChannel channel = null;
    public static final byte[] POLL_THE_TAG = {(byte) 0xFF, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0xD4, (byte) 0x4A, (byte) 0x01, (byte) 0x00};

  /**
    * @param args the command line arguments
  */

  /**
    * Enumerate all card readers in the system and
    * choose one which will be used.
    *
    * @throws IOException
  */
    public static void initReader() throws IOException {
        try {
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminalsList = factory.terminals().list();
            System.out.println("[INFO] Avaliable card readers:");
            for (int i = 0; i < terminalsList.size(); i++) {
                System.out.println("\t" + i + "..." + terminalsList.get(i));
            }

            //Ask user for card terminal to use
            System.out.print("\n\tUse terminal #: ");
            //InputStreamReader isr = new InputStreamReader(System.in);
            //BufferedReader br = new BufferedReader(isr);
            //String selection = br.readLine();

            try {
                int selectionNumber = Integer.parseInt("0");
                if (selectionNumber < terminalsList.size()) {
                    System.out.println("[INFO] Using: " + terminalsList.get(selectionNumber));
                    terminal = terminalsList.get(selectionNumber);
                } else {
                    System.out.println("\n[ERROR] Your number is to big? Any complex?");
                    System.exit(-1);
                }

            } catch (NumberFormatException nfex) {
                System.out.println("\n[ERROR] Is it too hard to enter the number?!?!");
                System.exit(-1);
            }


        } catch (CardException ex) {
            System.out.println("[ERROR] Check if any terminal is connected!");
            System.exit(-1);
        }
    }

    /**
     *
     */
    public static String waitForCard() {
        String TagID="";
        try {
            Card card = terminal.connect("*");
            channel = card.getBasicChannel();

            //Reset the card
            card.getATR();

            System.out.println("[INFO] Waiting for card...");
            CommandAPDU cmd = new CommandAPDU(POLL_THE_TAG);
            ResponseAPDU transmit = channel.transmit(cmd);

            ICard icard = CardFactory.getInstance().getCard(transmit.getBytes());
            String tid = Conversions.byte2String(icard.getUID());
            System.out.println("[INFO] Card TagID: " + tid);
            System.out.println("[INFO] Card type: " + icard.getTagType());
            TagID=tid;

            /*  //If card is not MIFARE_1K or MIFARE_4K - exit
            TagType type = icard.getTagType();
            if (type != TagType.MIFARE_1K && type != TagType.MIFARE_4K) {
            System.exit(0);
            }
            
            if (bruteforce) {
            while (BruteForce.hasNextKey()) {
            byte[] key = BruteForce.getKey();
            System.out.print("[INFO] Key " + BruteForce.getKeyAsString());
            crackKeys(icard, key);
            System.out.println();
            BruteForce.getNextKey();
            }
            } else if (bruteXforce) {
            while (BruteXForce.hasNextKey()) {
            byte[] key = BruteXForce.getKey();
            System.out.print("[INFO] Key " + BruteXForce.getKeyAsString());
            crackKeys(icard, key);
            System.out.println();
            BruteXForce.getNextKey();
            }
            } else if (customKey == null) {
            try {
            File keysFile = new File(fileName);
            BufferedReader br = new BufferedReader(new FileReader(keysFile));
            
            String line = "";
            while ((line = br.readLine()) != null) {
            
            System.out.print("[INFO] Key " + line.trim() + " ");
            String[] bytes = line.split(" ");
            byte[] key = new byte[bytes.length];
            
            for (int i = 0; i < key.length; i++) {
            key[i] = (byte) (int) Integer.decode(bytes[i]);
            }
            
            crackKeys(icard, key);
            System.out.println();
            }
            
            } catch (IOException ex) {
            System.out.println("[ERROR] Sth wrong with keys file!");
            }
            } else if (customKey != null) {
            if (customKey.length() == 12) {
            
            byte[] key = new byte[6];
            System.out.print("[INFO] Key ");
            
            for (int i = 0; i < 6; i++) {
            String sub = customKey.substring(i * 2, i * 2 + 2);
            System.out.print("0x" + sub + " ");
            key[i] = (byte) (int) Integer.decode("0x" + sub);
            }
            
            crackKeys(icard, key);
            
            } else {
            System.out.println("[ERROR] Key '" + customKey + "' is invalid!");
            System.exit(-1);
            }
            }
             */
        } catch (CardException ex) {
            System.out.println("[ERROR] Hmm...exception occured when connecting to the card.");
            //System.exit(-1);
        }
        return TagID;
        
    }
}