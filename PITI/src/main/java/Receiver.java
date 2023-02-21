import jssc.SerialPort;
import jssc.SerialPortException;

import static jssc.SerialPort.*;

public class Receiver {
    public static final String PORT = "COM3";

    public static void main(String[] args) {
        try {
            System.out.println("Welcome to the chat!");

            // Initialize the serial port
            final var serialPort = new SerialPort(PORT);
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
        } catch (SerialPortException e) {
            throw new RuntimeException(e);
        }
    }
}