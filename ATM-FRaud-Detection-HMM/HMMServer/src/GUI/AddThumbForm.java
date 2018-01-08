package GUI;
import com.digitalpersona.onetouch.*;
import com.digitalpersona.onetouch.processing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class AddThumbForm extends CaptureForm {

    private DPFPEnrollment enroller = DPFPGlobal.getEnrollmentFactory().createEnrollment();

    AddThumbForm(Frame owner) {
        super(owner);
    }

    @Override
    protected void init() {
        super.init();
        updateStatus();

    }

    @Override
    protected void process(DPFPSample sample) {
        super.process(sample);
        DPFPFeatureSet features = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT);
        if (features != null) {
            try {
                makeReport("The fingerprint feature set was created.");
                enroller.addFeatures(features);		// Add feature set to template.
            } catch (DPFPImageQualityException ex) {
            } finally {
                FileOutputStream stream = null;
                try {
                    updateStatus();
                    switch (enroller.getTemplateStatus()) {
                        case TEMPLATE_STATUS_READY:
                            // report success and stop capturing
                            stop();
                            // ((RegisterForm) getOwner()).setTemplate(enroller.getTemplate());
                            File file = new File("C:\\Output\\"+NewUser.txtcardno.getText()+".fpt");
                            if (file.exists()) {
                                file.delete();
                            }
                            stream = new FileOutputStream(file);
                            try {
                                stream.write(enroller.getTemplate().serialize());
                                NewUser.jButton2.setEnabled(true);
                            } catch (IOException ex) {
                                Logger.getLogger(AddThumbForm.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                stream.close();
                            } catch (IOException ex) {
                                Logger.getLogger(AddThumbForm.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            setStatus("Ok Finish Sample");
                            setPrompt("Click Close, and then click Fingerprint Verification.");

                            break;
                        case TEMPLATE_STATUS_FAILED:	// report failure and restart capturing
                            enroller.clear();
                            stop();
                            updateStatus();                            
                            JOptionPane.showMessageDialog(AddThumbForm.this, "The fingerprint template is not valid. Repeat fingerprint enrollment.", "Fingerprint Enrollment", JOptionPane.ERROR_MESSAGE);
                            start();
                            break;
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(AddThumbForm.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        //stream.close();
                    } catch (Exception ex) {
                        Logger.getLogger(AddThumbForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    private void updateStatus() {
        // Show number of samples needed.
        setStatus(String.format("Fingerprint samples needed: %1$s", enroller.getFeaturesNeeded()));
    }
}
