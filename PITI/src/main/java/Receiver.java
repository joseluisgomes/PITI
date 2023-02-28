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

            SerialPort port = new SerialPort(PORT);
            port.openPort();
            port.setParams(BAUDRATE_9600,  DATABITS_8, STOPBITS_1, PARITY_NONE);
            // port.setParams(9600, 8, 1, 0); // alternate technique
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;
            port.setEventsMask(mask);
            port.addEventListener(new MyPortListener(port) /* defined below */);


            // Initialize the serial port
       /*     final var serialPort = new SerialPort(PORT);
            serialPort.openPort();
            serialPort.setParams(BAUDRATE_9600,  DATABITS_8, STOPBITS_1, PARITY_NONE);
            final int mask = MASK_RXCHAR + MASK_CTS + MASK_DSR;
            serialPort.setEventsMask(mask);

            String auxiliary = ""; // Auxiliary string
            while (!auxiliary.equals("exit")) {
                var portListener = new MyPortListener(serialPort);
                serialPort.addEventListener(portListener);

                auxiliary = portListener.getMessageReceived();

                String messageToPrint = String
                        .format("Received: %s ( %d bytes)", auxiliary, auxiliary.length());
                System.out.println(messageToPrint);
            }
            serialPort.closePort(); // Connection closed
            */
        } catch (SerialPortException e) {
            throw new RuntimeException(e);
        }
    }
}

class MyPortListener implements SerialPortEventListener {
    SerialPort port;

    public MyPortListener(SerialPort port) {
        this.port = port;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if(serialPortEvent.isRXCHAR()){ // data is available
            // read data, if 10 bytes available
            if(serialPortEvent.getEventValue() > 0){
                try {
                    byte[] buffer = port.readBytes(serialPortEvent.getEventValue());
                    String messageReceived = new String(buffer);

                    System.out.println("Received: " + messageReceived);
                } catch (SerialPortException ex) {
                    System.out.println(ex);
                }
            }
        } else if(serialPortEvent.isCTS()){ // CTS line has changed state
            if(serialPortEvent.getEventValue() == 1){ // line is ON
                System.out.println("CTS - ON");
            } else {
                System.out.println("CTS - OFF");
            }
        } else if(serialPortEvent.isDSR()){ // DSR line has changed state
            if(serialPortEvent.getEventValue() == 1){ // line is ON
                System.out.println("DSR - ON");
            } else {
                System.out.println("DSR - OFF");
            }
        }
    }
}