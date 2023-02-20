import jssc.SerialPort;
import jssc.SerialPortException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static jssc.SerialPort.*;

public class Emitter {
    public static final String PORT = "COM1";

    public static void main(String[] args) {
        try (final var userInput = // The user's input comes from the keyboard
                     new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Welcome! Please introduce a message.");

            // Initialize the serial port
            final var serialPort = new SerialPort(Emitter.PORT);
            serialPort.openPort();
            serialPort.setParams(BAUDRATE_9600, DATABITS_8, STOPBITS_1, PARITY_NONE);

            String auxiliary = ""; // Auxiliary string
            while (!auxiliary.equals("exit")) {
                System.out.println("Message: ");
                auxiliary = userInput.readLine();

                serialPort.writeBytes(auxiliary.getBytes()); // Send the message
            }
            serialPort.closePort(); // Connection closed
        } catch (IOException | SerialPortException e) {
            throw new RuntimeException(e);
        }
    }
}