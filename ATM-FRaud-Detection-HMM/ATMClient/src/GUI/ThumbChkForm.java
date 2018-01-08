package GUI;

import com.digitalpersona.onetouch.*;
import com.digitalpersona.onetouch.verification.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThumbChkForm extends CaptureForm {

    private DPFPVerification verificator = DPFPGlobal.getVerificationFactory().createVerification();
    String ThumbID = "";
    String type = "";

    ThumbChkForm(Frame owner) {
        super(owner);
        setTitle("Capture Thumb");
    }

    @Override
    protected void init() {
        super.init();
        //this.setTitle("Fingerprint Enrollment");
        updateStatus(0);
    }

    @Override
    protected void process(DPFPSample sample) {
        super.process(sample);

        // Process the sample and create a feature set for the enrollment purpose.
        DPFPFeatureSet features = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);

        // Check quality of the sample and start verification if it's good
        if (features != null) {
            FileInputStream stream = null;
            try {
                // Compare the feature set with our template
                DPFPVerificationResult result = null;
                stream = new FileInputStream(new File("C:\\Client\\" + MainFrame.mainobj.RFIDNumber + ".fpt"));
                byte[] data = null;
//                if (MainFrame.data != null) {
//                    data = MainFrame.data;
//                }
                try {
                    data = new byte[stream.available()];
                } catch (IOException ex) {
                    Logger.getLogger(ThumbChkForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    stream.read(data);
                } catch (IOException ex) {
                    Logger.getLogger(ThumbChkForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    stream.close();
                } catch (IOException ex) {
                    Logger.getLogger(ThumbChkForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (data != null) {
                    DPFPTemplate t = DPFPGlobal.getTemplateFactory().createTemplate();
                    t.deserialize(data);
                    result = verificator.verify(features, t);
                    updateStatus(result.getFalseAcceptRate());
                    if (result.isVerified()) {
                        setTitle("Valid User");
                        //makeReport("The fingerprint was VERIFIED."); 
                        MainFrame.jDesktopPane1.setVisible(false);
                        MainFrame.mainobj.ChangeMsg(1);
                        stop();
                        this.dispose();
                    } else {
                        setTitle("Invalid Thumb");
                        //makeReport("The fingerprint was NOT VERIFIED.");
                    }
                } else {
                    setTitle("Sorry RFID Not Exits");
                }
            } catch (Exception ex) {
                Logger.getLogger(ThumbChkForm.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    // stream.close();
                } catch (Exception ex) {
                    Logger.getLogger(ThumbChkForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void updateStatus(int FAR) {
        // Show "False accept rate" value
        setStatus(String.format("False Accept Rate (FAR) = %1$s", FAR));
    }
}
