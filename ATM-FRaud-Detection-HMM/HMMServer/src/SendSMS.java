

public class SendSMS {

    public static void MAIN(String MNO, String Mes) {

        try {
            System.out.println("Msg :" + Mes);
            SMSServer.SMSClient sms = null;
            sms = new SMSServer.SMSClient();
            sms.sendMessage(MNO, Mes);
        } catch (Exception err) {
            System.out.println(err.toString());
        }
    }
    public static void main(String[] args) {
        SendSMS.MAIN("9975965956","test msg");
    }
}
