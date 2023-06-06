package ui.controllers;

import jssc.SerialPortList;
import ui.App;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    @FXML
    ComboBox<String> comboBoxBR = new ComboBox<>();

    @FXML
    ComboBox<String> comboBoxCOM = new ComboBox<>();

    ArrayList<String> ports = new ArrayList<>();

    final Alert alert = new Alert(Alert.AlertType.ERROR);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/application.css")).toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");

        ports = getAvailableCOMPorts();

        if (getBaudrateFromApp() != null) {
            comboBoxBR.valueProperty().setValue(getBaudrateFromApp());
        }

        if (getPortFromApp() != null) {
            comboBoxCOM.valueProperty().setValue(getPortFromApp());
        }

        comboBoxBR.getItems().addAll(FXCollections.observableArrayList("2400", "9600", "28800", "57600", "115200"));
        comboBoxCOM.getItems().addAll(FXCollections.observableArrayList(ports));
    }

    @FXML
    private void handleExitButtonClicked(ActionEvent event) {
        Platform.exit();
        event.consume();
    }

    @FXML
    private void handleGitButtonClicked(ActionEvent event) {
        new Application() {
            @Override
            public void start(Stage stage) {
            }
        }.getHostServices().showDocument("https://github.com/joseluisgomes/PITI/");
        event.consume();
    }

    @FXML
    private void handleMinimizeButtonClicked(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void handleRefreshButtonAction() {
        // refresh the available COM ports and add them to the ComboBox
        ports = getAvailableCOMPorts();
        comboBoxCOM.setItems(FXCollections.observableArrayList(ports));
    }

    @FXML
    private void handleEmitterViewAction(ActionEvent event) throws IOException {
        String tempBaudrate = getBaudrateFromCB();
        String tempPort = getCOMPortNameFromCB();

        if (tempPort == null) {
            alertNullPort();
        } else if (tempBaudrate == null) {
            alertNullBaudrate();
        } else {
            setPortOnApp(tempPort);
            setBaudrateOnApp(tempBaudrate);

            System.out.println("\nE: COM port = " + App.port);
            System.out.println("E: Baudrate = " + App.baudrate);

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/EmitterView.fxml")));
            Scene newScene = new Scene(root);
            newScene.setFill(Color.TRANSPARENT);
            Stage newStage = App.getStage();
            newStage.setScene(newScene);
        }
        event.consume();
    }

    @FXML
    private void handleReceiverViewAction(ActionEvent event) throws IOException {
        String tempBaudrate = getBaudrateFromCB();
        String tempPort = getCOMPortNameFromCB();

        if (tempPort == null) {
            alertNullPort();
        } else if (tempBaudrate == null) {
            alertNullBaudrate();
        } else {
            setPortOnApp(tempPort);
            App.baudrate = tempBaudrate;
            setBaudrateOnApp(tempBaudrate);

            System.out.println("\nR: COM port = " + App.port);
            System.out.println("R: Baudrate = " + App.baudrate);

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/ReceiverView.fxml")));
            Scene newScene = new Scene(root);
            newScene.setFill(Color.TRANSPARENT);
            Stage newStage = App.getStage();
            newStage.setScene(newScene);
        }
        event.consume();
    }

    private void alertNullPort() {
        alert.setTitle("ERROR: COM PORT");
        alert.setContentText("No COM port has been chosen");
        alert.setContentText("Choose a COM port to proceed...");
        alert.show();
    }

    private void alertNullBaudrate() {
        alert.setTitle("ERROR: BAUDRATE ERROR");
        alert.setContentText("No baud rate has been chosen");
        alert.setContentText("Choose a baud rate to proceed...");
        alert.show();
    }

    private ArrayList<String> getAvailableCOMPorts() {
        String[] ports = SerialPortList.getPortNames();
        ArrayList<String> temp = new ArrayList<>();
        String name;

        for (String port : ports) {
            name = port;
            temp.add(name);
        }

        return temp;
    }

    public String getCOMPortNameFromCB() { return comboBoxCOM.valueProperty().getValue(); }

    public String getBaudrateFromCB() { return comboBoxBR.valueProperty().getValue(); }

    private static void setPortOnApp(String p) { App.port = p; }

    private static void setBaudrateOnApp(String br) { App.baudrate = br; }

    public static String getPortFromApp() { return App.port; }

    public static String getBaudrateFromApp() { return App.baudrate; }
}
