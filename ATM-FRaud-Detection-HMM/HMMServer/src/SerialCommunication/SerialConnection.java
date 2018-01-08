package SerialCommunication;

import GUI.NewUser;
import javax.comm.*;
import java.io.*;
import java.util.TooManyListenersException;

public class SerialConnection implements SerialPortEventListener,
        CommPortOwnershipListener {

    private UPSInterface parent;
    private SerialParameters parameters;
    private OutputStream os;
    private InputStream is;
    private int flag;
    private CommPortIdentifier portId;
    private SerialPort sPort;
    private boolean open;
    private NewUser rfc0;

    public SerialConnection(UPSInterface parent,
            SerialParameters parameters,
            String messageAreaOut,
            String messageAreaIn, NewUser rfc) {

        this.rfc0 = rfc;
        this.parent = parent;
        this.parameters = parameters;
        flag = 0;
        open = false;
    }

    public void openConnection() throws SerialConnectionException {


        try {
            System.out.println("Inside open Connection" + parameters.getPortName());
            portId = CommPortIdentifier.getPortIdentifier(parameters.getPortName());
            System.out.println("Inside open Connection");


        } catch (NoSuchPortException e) {
            throw new SerialConnectionException(e.getMessage());
        }

        try {
            if (!isOpen()) {
                sPort = (SerialPort) portId.open("UPSInterface", 30000);
            }

        } catch (PortInUseException e) {
            throw new SerialConnectionException(e.getMessage());
        }

        try {
            setConnectionParameters();

        } catch (SerialConnectionException e) {
            sPort.close();
            throw e;
        }

        try {
            os = sPort.getOutputStream();
            is = sPort.getInputStream();
        } catch (IOException e) {
            sPort.close();
            throw new SerialConnectionException("Error opening i/o streams");
        }

        try {
            sPort.addEventListener(this);
        } catch (TooManyListenersException e) {
            sPort.close();
            throw new SerialConnectionException("too many listeners added");
        }

        // Set notifyOnDataAvailable to true to allow event driven input.
        sPort.notifyOnDataAvailable(true);

        // Set notifyOnBreakInterrup to allow event driven break handling.
        sPort.notifyOnBreakInterrupt(true);

        // Set receive timeout to allow breaking out of polling loop during
        // input handling.
        try {
            sPort.enableReceiveTimeout(30);
        } catch (UnsupportedCommOperationException e) {
        }

        // Add ownership listener to allow ownership event handling.
        portId.addPortOwnershipListener(this);

        open = true;
    }

    public void setConnectionParameters() throws SerialConnectionException {

        // Save state of parameters before trying a set.
        int oldBaudRate = sPort.getBaudRate();
        int oldDatabits = sPort.getDataBits();
        int oldStopbits = sPort.getStopBits();
        int oldParity = sPort.getParity();
        // Set connection parameters, if set fails return parameters object
        // to original state.
        try {
            sPort.setSerialPortParams(parameters.getBaudRate(),
                    parameters.getDatabits(),
                    parameters.getStopbits(),
                    parameters.getParity());
        } catch (UnsupportedCommOperationException e) {
            parameters.setBaudRate(oldBaudRate);
            parameters.setDatabits(oldDatabits);
            parameters.setStopbits(oldStopbits);
            parameters.setParity(oldParity);
            throw new SerialConnectionException("Unsupported parameter");
        }

        // Set flow control.
        try {
            sPort.setFlowControlMode(parameters.getFlowControlIn()
                    | parameters.getFlowControlOut());
        } catch (UnsupportedCommOperationException e) {
            throw new SerialConnectionException("Unsupported flow control");
        }
    }

    public void closeConnection() {
        if (!open) {
            return;
        }
        if (sPort != null) {
            try {
                os.close();
                is.close();
            } catch (IOException e) {
                System.err.println(e);
            }

            // Close the port.
            sPort.close();

            // Remove the ownership listener.
            portId.removePortOwnershipListener(this);
        }

        open = false;
    }

    public void sendBreak() {
        sPort.sendBreak(1000);
    }

    public boolean isOpen() {
        return open;
    }

    public void serialEvent(SerialPortEvent e) {
        // Create a StringBuffer and int to receive input data.
        StringBuffer inputBuffer = new StringBuffer();
        int newData = 0;

        // Determine type of event.
        switch (e.getEventType()) {

            // Read data until -1 is returned. If \r is received substitute
            // \n for correct newline handling.
            case SerialPortEvent.DATA_AVAILABLE:
                while (newData != -1) {
                    try {
                        newData = is.read();
                        if (newData == -1) {
                            break;
                        }
                        if ('\r' == (char) newData) {
                            inputBuffer.append('\n');

                            //this.jTextField1.append('\n');
                        } else {
                            inputBuffer.append((char) newData);
                            //	this.jTextField1.append((char)newData);
                        }
                    } catch (IOException ex) {
                        System.err.println(ex);
                        return;
                    }
                }

                if (flag == 0) {
                    rfc0.txtcardno.setText((new String(inputBuffer)).substring(1, 11));
                }
                break;

            case SerialPortEvent.BI:

        }

    }

    public void ownershipChange(int type) {
        if (type == CommPortOwnershipListener.PORT_OWNERSHIP_REQUESTED) {
            
        }
    }
}
