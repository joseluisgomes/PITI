import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import static jssc.SerialPort.*;

public class Receiver {
    public static final String PORT = "COM4";

    public static void main(String[] args) {
        try {
            System.out.println("Welcome to the chat!");

            // Initialize the SERIAL PORT
            final var port = new SerialPort(PORT);
            port.openPort();
            port.setParams(BAUDRATE_9600,  DATABITS_8, STOPBITS_1, PARITY_NONE);

            /*
              The mask is an additive quantity, thus to set a mask on the expectation of the arrival of Event Data (MASK_RXCHAR)
              and change the status lines CTS (MASK_CTS), DSR (MASK_DSR) we just need to combine all three masks.
             */
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;
            port.setEventsMask(mask);
            port.addEventListener(new MyPortListener(port));
        } catch (SerialPortException e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * This class must implement the method serialEvent, through it, we learn about
 * events that happened to our port. But we will not report on all events but only
 * those that we put in the mask. In this case the arrival of the data and change the
 * status lines CTS and DSR
 */
class MyPortListener implements SerialPortEventListener {
    private final SerialPort port;

    public MyPortListener(SerialPort port) {
        this.port = port;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        int eventValue = serialPortEvent.getEventValue();

        if(serialPortEvent.isRXCHAR()){ // data is available
            if(eventValue > 0){ // read data, if there are bytes available
                try {
                    byte[] buffer = port.readBytes(eventValue); // Decode the received message
                    String messageReceived = new String(buffer);

                    var messageToPrint = String
                            .format("Received: %s ( %d bytes)", messageReceived, messageReceived.length());
                    System.out.println(messageToPrint);
                } catch (SerialPortException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else if(serialPortEvent.isCTS()){ // CTS line has changed state
            if(eventValue == 1){ // line is ON
                System.out.println("CTS - ON");
            } else {
                System.out.println("CTS - OFF");
            }
        } else if(serialPortEvent.isDSR()){ // DSR line has changed state
            if(eventValue == 1){ // line is ON
                System.out.println("DSR - ON");
            } else {
                System.out.println("DSR - OFF");
            }
        }
    }
}