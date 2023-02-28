import jssc.SerialPort;
import jssc.SerialPortException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static jssc.SerialPort.*;

public class Emitter {
    public static final String PORT = "COM4";

    public static void main(String[] args) {
        try (final var userInput = // The user's input comes from the keyboard
                     new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Welcome! Please introduce a message.");

            // Initialize the serial port
            final var serialPort = new SerialPort(PORT);
            serialPort.openPort();
            serialPort.setParams(BAUDRATE_9600, DATABITS_8, STOPBITS_1, PARITY_NONE);

            String auxiliary = ""; // Auxiliary string
            while (!auxiliary.equals("exit")) {
                System.out.println("Message: ");
                auxiliary = userInput.readLine(); // Read the user's input

                // Encode the message and send it through the serial port
                serialPort.writeBytes(auxiliary.getBytes());

                String messageToPrint = String
                        .format("Sent: %s ( %d bytes)", auxiliary, auxiliary.length());
                System.out.println(messageToPrint);
            }
            serialPort.closePort(); // Connection closed
        } catch (IOException | SerialPortException e) {
            throw new RuntimeException(e);
        }
    }
}