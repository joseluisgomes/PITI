package ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import ui.App;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

import static jssc.SerialPort.*;

public class ReceiverViewController implements Initializable {

    @FXML
    private TextArea ReceiverTextArea;

    @FXML
    private Label receiverTextBR;

    @FXML
    private Label receiverTextPort;

    private String messagesReceived = "";

    static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ReceiverTextArea.setEditable(false);

        receiverTextPort.setText("Port: " + MainViewController.getPortFromApp());
        receiverTextBR.setText("Baudrate: " + MainViewController.getBaudrateFromApp());

        try {
            // Initialize the SERIAL PORT
            final var serialPort = new SerialPort(App.port);
            serialPort.openPort();
            serialPort.setParams(Integer.parseInt(App.baudrate), DATABITS_8, STOPBITS_1, PARITY_NONE);

            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;
            serialPort.setEventsMask(mask);
            serialPort.addEventListener(new MyPortListener(serialPort));
            //serialPort.closePort();
        } catch (SerialPortException e) {
            throw new RuntimeException(e);
        }

        //addText("FIRST MESSAGE");
    }

    @FXML
    private void handleReturnButtonClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/MainView.fxml")));
        Scene newScene = new Scene(root);
        newScene.setFill(Color.TRANSPARENT);

        Stage newStage = App.getStage();
        newStage.setScene(newScene);

        event.consume();
    }

    public void addText(String msg) {
        LocalDateTime ldt = LocalDateTime.now();
        String time = ldt.format(format);

        if (Objects.equals(ReceiverTextArea.getText(), "")) {
            ReceiverTextArea.setText(time + " " + msg);
        } else {
            ReceiverTextArea.appendText("\n" + time + " " + msg);
        }
    }

    class MyPortListener implements SerialPortEventListener {
        private final SerialPort port;

        public MyPortListener(SerialPort port) {
            this.port = port;
        }

        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
            int eventValue = serialPortEvent.getEventValue();

            if (serialPortEvent.isRXCHAR()) { // data is available
                if (eventValue > 0) { // read data, if there are bytes available
                    try {
                        byte[] buffer = port.readBytes(eventValue); // Decode the received message
                        String messageReceived = new String(buffer);

                        messagesReceived += messageReceived;
                        if (messagesReceived.contains("\n")) {
                            var messages = messagesReceived.split("\n");
                            for (String message : messages)
                                addText(message);
                            messagesReceived = "";
                        }

                        System.out.println(messageReceived);
                    } catch (SerialPortException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } else if (serialPortEvent.isCTS()) { // CTS line has changed state
                if (eventValue == 1) { // line is ON
                    System.out.println("CTS - ON");
                } else {
                    System.out.println("CTS - OFF");
                }
            } else if (serialPortEvent.isDSR()) { // DSR line has changed state
                if (eventValue == 1) { // line is ON
                    System.out.println("DSR - ON");
                } else {
                    System.out.println("DSR - OFF");
                }
            }
        }
    }
}
