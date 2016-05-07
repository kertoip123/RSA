import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by piotrek on 03.05.2016.
 */
public class ConfigPanelController {

    @FXML private HBox serverModeOptions;
    @FXML private VBox clientModeOptions;
    @FXML private TextField serverModePort;
    @FXML private TextField clientModePort;
    @FXML private TextField clientModeIpAddr;
    @FXML private RadioButton serverMode;
    @FXML private RadioButton clientMode;


    @FXML protected void handleServerModeSelect(ActionEvent event) {
        clientModeOptions.setVisible(false);
        serverModeOptions.setVisible(true);
    }

    @FXML protected void handleClientModeSelect(ActionEvent event) {
        clientModeOptions.setVisible(true);
        serverModeOptions.setVisible(false);
    }

    @FXML protected void handleStartBtnAction(ActionEvent event) {
        try {

            MainWindowController controller = Main.initMainApplication();
            CommunicationManager.addLogHandler(new TextAreaHandler(controller.getLogTextArea()));
            CommunicationManager communicationManager = null;

            if(serverMode.isSelected()){
                int port = Integer.parseInt(serverModePort.getText());
                communicationManager = new ServerManager(port);
            }

            if(clientMode.isSelected()){
                String ipAddr = clientModeIpAddr.getText();
                int port = Integer.parseInt(clientModePort.getText());
                communicationManager = new ClientManager(ipAddr, port);
            }

            controller.setCommunicationManager(communicationManager);

            //close config window
            ((Stage)((Button)event.getSource()).getScene().getWindow()).close();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
