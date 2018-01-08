package SerialCommunication;

import GUI.NewUser;
import javax.comm.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Enumeration;

public class UPSInterface extends Frame implements ActionListener {

   
    private String messageAreaOut;
    private String messageAreaIn;
    private ConfigurationPanel configurationPanel;
    private SerialParameters parameters;
    private SerialConnection connection;
    private Properties props = null;
    private NewUser rfc0;

    public void go() {
        try {

            connection.openConnection();
            System.out.println("aFTER poRT");
            portOpened();

        } catch (SerialConnectionException e2) {
            System.out.println("Port Open Error :"+e2.toString());
            return;
        }
    }

    public UPSInterface(NewUser rf0) {
        rfc0 = rf0;
        parameters = new SerialParameters();
        parseArgs();
        connection = new SerialConnection(this, parameters,
                messageAreaOut, messageAreaIn, rfc0);
    }

    public void setConfigurationPanel() {
        configurationPanel.setConfigurationPanel();
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        // Loads a configuration file.
        if (cmd.equals("Load")) {
            if (connection.isOpen()) {
                System.out.println("Port Load :Configuration may not be loaded while a port is open.");
                
            } else {
                FileDialog fd = new FileDialog(this,
                        "Load Port Configuration",
                        FileDialog.LOAD);
                fd.setVisible(true);
                String file = fd.getFile();
                if (file != null) {
                    String dir = fd.getDirectory();
                    File f = new File(dir + file);
                    try {
                        FileInputStream fis = new FileInputStream(f);
                        props = new Properties();
                        props.load(fis);
                        fis.close();
                    } catch (FileNotFoundException e1) {
                        System.err.println(e1);
                    } catch (IOException e2) {
                        System.err.println(e2);
                    }
                    loadParams();
                }
            }
        }

        // Saves a configuration file.
        if (cmd.equals("Save")) {
            configurationPanel.setParameters();
            FileDialog fd = new FileDialog(this, "Save Port Configuration",
                    FileDialog.SAVE);
            fd.setFile("UPSInterface.properties");
            fd.setVisible(true);
            String fileName = fd.getFile();
            String directory = fd.getDirectory();
            if ((fileName != null) && (directory != null)) {
                writeFile(directory + fileName);
            }
        }

        // Calls shutdown, which exits the program.
        if (cmd.equals("Exit")) {
            shutdown();
        }

        // Opens a port.
        if (cmd.equals("Open Port")) {
            Cursor previousCursor = getCursor();
            setNewCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            configurationPanel.setParameters();
            try {
                connection.openConnection();
            } catch (SerialConnectionException e2) {
                System.out.println("Port Open :"+e2.toString());
                setNewCursor(previousCursor);
                return;
            }
            portOpened();
            setNewCursor(previousCursor);
        }

        // Closes a port.
        if (cmd.equals("Close Port")) {
            portClosed();
        }

        // Sends a break signal to the port.
        if (cmd.equals("Send Break")) {
            connection.sendBreak();
        }
    }

    public void portOpened() {
    }

    public void portClosed() {
        connection.closeConnection();      
    }

    private void setNewCursor(Cursor c) {
        setCursor(c);

    }

    private void writeFile(String path) {

        Properties newProps;
        FileOutputStream fileOut = null;

        newProps = new Properties();

        newProps.put("portName", parameters.getPortName());
        newProps.put("baudRate", parameters.getBaudRateString());
        newProps.put("flowControlIn", parameters.getFlowControlInString());
        newProps.put("flowControlOut", parameters.getFlowControlOutString());
        newProps.put("parity", parameters.getParityString());
        newProps.put("databits", parameters.getDatabitsString());
        newProps.put("stopbits", parameters.getStopbitsString());

        try {
            fileOut = new FileOutputStream(path);
        } catch (IOException e) {
            System.out.println("Could not open file for writiing");
        }

        newProps.save(fileOut, "Serial Demo poperties");

        try {
            fileOut.close();
        } catch (IOException e) {
            System.out.println("Could not close file for writiing");
        }
    }

    public void shutdown() {
        connection.closeConnection();

    }

    private void parseArgs() {


        File f = new File("HWDet");

        if (f.exists()) {
            try {

                FileInputStream fis = new FileInputStream(f);
                props = new Properties();
                props.load(fis);
                fis.close();
                loadParams();
                System.out.println("Reading File 2");
            } catch (IOException e) {
                System.out.println("Error in parseArgs" + e);
            }
        }
    }

    private void loadParams() {
        parameters.setPortName(props.getProperty("portName"));
        parameters.setBaudRate(props.getProperty("baudRate"));
        parameters.setFlowControlIn(props.getProperty("flowControlIn"));
        parameters.setFlowControlOut(props.getProperty("flowControlOut"));
        parameters.setParity(props.getProperty("parity"));
        parameters.setDatabits(props.getProperty("databits"));
        parameters.setStopbits(props.getProperty("stopbits"));
        //setConfigurationPanel();
    }

    class ConfigurationPanel extends Panel implements ItemListener {

        private Frame parent;
        private Label portNameLabel;
        private Choice portChoice;
        private Label baudLabel;
        private Choice baudChoice;
        private Label flowControlInLabel;
        private Choice flowChoiceIn;
        private Label flowControlOutLabel;
        private Choice flowChoiceOut;
        private Label databitsLabel;
        private Choice databitsChoice;
        private Label stopbitsLabel;
        private Choice stopbitsChoice;
        private Label parityLabel;
        private Choice parityChoice;

        public ConfigurationPanel(Frame parent) {
            this.parent = parent;

            setLayout(new GridLayout(4, 4));

            portNameLabel = new Label("Port Name:", Label.LEFT);
            add(portNameLabel);

            portChoice = new Choice();
            portChoice.addItemListener(this);
            add(portChoice);
            listPortChoices();
            portChoice.select(parameters.getPortName());

            baudLabel = new Label("Baud Rate:", Label.LEFT);
            add(baudLabel);

            baudChoice = new Choice();
            baudChoice.addItem("300");
            baudChoice.addItem("1200");
            baudChoice.addItem("2400");
            baudChoice.addItem("9600");
            baudChoice.addItem("14400");
            baudChoice.addItem("28800");
            baudChoice.addItem("38400");
            baudChoice.addItem("57600");
            baudChoice.addItem("152000");
            baudChoice.select(Integer.toString(parameters.getBaudRate()));
            baudChoice.addItemListener(this);
            add(baudChoice);

            flowControlInLabel = new Label("Flow Control In:", Label.LEFT);
            add(flowControlInLabel);

            flowChoiceIn = new Choice();
            flowChoiceIn.addItem("None");
            flowChoiceIn.addItem("Xon/Xoff In");
            flowChoiceIn.addItem("RTS/CTS In");
            flowChoiceIn.select(parameters.getFlowControlInString());
            flowChoiceIn.addItemListener(this);
            add(flowChoiceIn);

            flowControlOutLabel = new Label("Flow Control Out:", Label.LEFT);
            add(flowControlOutLabel);

            flowChoiceOut = new Choice();
            flowChoiceOut.addItem("None");
            flowChoiceOut.addItem("Xon/Xoff Out");
            flowChoiceOut.addItem("RTS/CTS Out");
            flowChoiceOut.select(parameters.getFlowControlOutString());
            flowChoiceOut.addItemListener(this);
            add(flowChoiceOut);

            databitsLabel = new Label("Data Bits:", Label.LEFT);
            add(databitsLabel);

            databitsChoice = new Choice();
            databitsChoice.addItem("5");
            databitsChoice.addItem("6");
            databitsChoice.addItem("7");
            databitsChoice.addItem("8");
            databitsChoice.select(parameters.getDatabitsString());
            databitsChoice.addItemListener(this);
            add(databitsChoice);

            stopbitsLabel = new Label("Stop Bits:", Label.LEFT);
            add(stopbitsLabel);

            stopbitsChoice = new Choice();
            stopbitsChoice.addItem("1");
            stopbitsChoice.addItem("1.5");
            stopbitsChoice.addItem("2");
            stopbitsChoice.select(parameters.getStopbitsString());
            stopbitsChoice.addItemListener(this);
            add(stopbitsChoice);

            parityLabel = new Label("Parity:", Label.LEFT);
            add(parityLabel);

            parityChoice = new Choice();
            parityChoice.addItem("None");
            parityChoice.addItem("Even");
            parityChoice.addItem("Odd");
            parityChoice.select("None");
            parityChoice.select(parameters.getParityString());
            parityChoice.addItemListener(this);
            add(parityChoice);
        }

        public void setConfigurationPanel() {
            portChoice.select(parameters.getPortName());
            baudChoice.select(parameters.getBaudRateString());
            flowChoiceIn.select(parameters.getFlowControlInString());
            flowChoiceOut.select(parameters.getFlowControlOutString());
            databitsChoice.select(parameters.getDatabitsString());
            stopbitsChoice.select(parameters.getStopbitsString());
            parityChoice.select(parameters.getParityString());
        }

        public void setParameters() {
            parameters.setPortName(portChoice.getSelectedItem());
            parameters.setBaudRate(baudChoice.getSelectedItem());
            parameters.setFlowControlIn(flowChoiceIn.getSelectedItem());
            parameters.setFlowControlOut(flowChoiceOut.getSelectedItem());
            parameters.setDatabits(databitsChoice.getSelectedItem());
            parameters.setStopbits(stopbitsChoice.getSelectedItem());
            parameters.setParity(parityChoice.getSelectedItem());
        }

        void listPortChoices() {
            CommPortIdentifier portId;

            Enumeration en = CommPortIdentifier.getPortIdentifiers();

            // iterate through the ports.
            while (en.hasMoreElements()) {
                portId = (CommPortIdentifier) en.nextElement();
                if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    portChoice.addItem(portId.getName());
                }
            }
            portChoice.select(parameters.getPortName());
        }

        public void itemStateChanged(ItemEvent e) {
            // Check if port is open.
            if (connection.isOpen()) {
                // If port is open do not allow port to change.
                if (e.getItemSelectable() == portChoice) {
                    System.out.println("Port can not,"
                            + "be changed"
                            + "while a port is open.");                    
                    setConfigurationPanel();
                    return;
                }
                // Set the parameters from the choice panel.
                setParameters();
                try {
                    // Attempt to change the settings on an open port.
                    connection.setConnectionParameters();
                } catch (SerialConnectionException ex) {
                    System.out.println("Error :" + ex.toString());
                    setConfigurationPanel();
                }
            } else {
                setParameters();
            }
        }
    }

    class CloseHandler extends WindowAdapter {

        UPSInterface sd;

        public CloseHandler(UPSInterface sd) {
            this.sd = sd;
        }

        public void windowClosing(WindowEvent e) {
            sd.shutdown();
        }
    }
}
