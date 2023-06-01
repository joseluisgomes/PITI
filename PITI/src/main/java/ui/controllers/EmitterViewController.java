package ui.controllers;

import javafx.scene.control.Label;
import jssc.SerialPort;
import jssc.SerialPortException;
import ui.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

import static jssc.SerialPort.*;

public class EmitterViewController implements Initializable {
    @FXML
    private TextArea EmitterTextArea;

    @FXML
    private TextArea EmitterHistoryTextArea;

    @FXML
    private Label emitterTextBR;

    @FXML
    private Label emitterTextPort;

    static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final SerialPort serialPort = new SerialPort(App.port);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EmitterHistoryTextArea.setEditable(false);

        emitterTextPort.setText("Port: " + MainViewController.getPortFromApp());
        emitterTextBR.setText("Baudrate: " + MainViewController.getBaudrateFromApp());

        EmitterTextArea.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                EmitterTextArea.setText(EmitterTextArea.getText().replace("\n",""));
                handleSubmitButtonAction(new ActionEvent());
            }
        });

        try {
            serialPort.openPort();
            serialPort.setParams(Integer.parseInt(App.baudrate), DATABITS_8, STOPBITS_1, PARITY_NONE);
        } catch (SerialPortException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
        String textToBeSent = "";

        if(!EmitterTextArea.getText().isEmpty()) { // if TextArea has text
            textToBeSent = EmitterTextArea.getText();
        } else if(EmitterTextArea.getText().isEmpty()  || EmitterTextArea.getText().equals("")) {  // if TextArea doesn't have text
            textToBeSent = createRandomMessage();
            //EmitterTextArea.setPromptText(textToBeSent);
        }

        try {
            addText(textToBeSent);
            textToBeSent += "\n";
            serialPort.writeBytes(textToBeSent.getBytes());
            //serialPort.closePort(); // Connection closed
        } catch (SerialPortException e) {
            throw new RuntimeException(e);
        }

        EmitterTextArea.setText("");
        event.consume();
    }

    @FXML
    private void handleReturnButtonClicked(ActionEvent event) throws IOException {
        try {
            serialPort.closePort();
        } catch (SerialPortException e) {
            System.out.println("PORT FAILED TO CLOSE");
        }

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/MainView.fxml")));

        Scene newScene = new Scene(root);
        newScene.setFill(Color.TRANSPARENT);

        Stage newStage = App.getStage();
        newStage.setScene(newScene);

        event.consume();
    }

    private String createRandomMessage() {
        String temp;

        int minValue = 10, maxValue = 40; // will print a random set of chars of a random size between this two values
        int leftLimit = 65, rightLimit = 90; //ascii values from capital A to capital Z
        int numChars = (int) (Math.random() * maxValue) + minValue;

        Random random = new Random();

        temp = random.ints(leftLimit, rightLimit + 1)
                .limit(numChars)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return temp;
    }

    public void addText(String msg) {
        LocalDateTime ldt = LocalDateTime.now();
        String time = ldt.format(format);

        if (Objects.equals(EmitterHistoryTextArea.getText(), "")) {
            EmitterHistoryTextArea.setText(time + " " + msg);
        } else {
            EmitterHistoryTextArea.appendText("\n" + time + " " + msg);
        }
    }
}