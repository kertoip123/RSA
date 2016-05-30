import com.sun.deploy.panel.ExceptionListDialog;
import com.sun.deploy.trace.LoggerTraceListener;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;

import java.util.Arrays;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created by piotrek on 03.05.2016.
 */
public class MainWindowController {

    private CommunicationManager mCommunicationManager;
    private AsymmetricCipher mCipher;

    private Logger logger;

    @FXML private Button startBtn;
    @FXML private Button readNextMsgBtn;
    @FXML private Button encodeSendBtn;

    @FXML private TextField keyLenTextField;

    @FXML private TextArea receivedMsgTextArea;
    @FXML private TextArea decodedMsgTextArea;
    @FXML private TextArea plainTextArea;
    @FXML private TextArea encodedMsgTextArea;
    @FXML private TextArea logTextArea;



    @FXML protected void handleStartAction(ActionEvent event) {

        RSACipher.setLogger(logger);

        int keyLen = Integer.parseInt(keyLenTextField.getText());
        mCipher = new RSACipher(keyLen);

        Key myPublicKey = mCipher.getPublicKey();

        try {
            mCommunicationManager.send(myPublicKey.toString().getBytes());
        }catch (Exception e){
            logger.fine(e.getMessage());
        }

        startBtn.setDisable(true);
        keyLenTextField.setDisable(true);

         waitForPeerKey();

    }

    @FXML protected void handleReadNextAction(ActionEvent event) {

        byte [] msg = mCommunicationManager.getMessage();

        if(msg == null) {
            logger.fine("No message received");
            return;
        }

        byte [] decoded = mCipher.messageDecode(msg);
        receivedMsgTextArea.setText(Arrays.toString(msg));
        try {
            decodedMsgTextArea.setText(new String(decoded, "UTF-8"));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    @FXML protected void handleEncodeSendAction(ActionEvent event) {

        try {
            String msg = plainTextArea.getText();
            byte [] encodedMsg = mCipher.messageEncode(msg.getBytes("UTF-8"));

            mCommunicationManager.send(encodedMsg);
            encodedMsgTextArea.setText(Arrays.toString(encodedMsg));

        } catch (Exception e){
            logger.fine(e.getMessage());
        }
    }


    public TextArea getLogTextArea(){
        return logTextArea;
    }

    public void setCommunicationManager(CommunicationManager communicationManager){
        this.mCommunicationManager = communicationManager;
    }

    public void closeConnection(){
        try {
            mCommunicationManager.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void initLogger(){
        logger = Logger.getLogger(getClass().getName());
        logger.setLevel(Level.FINE);
        logger.addHandler(new TextAreaHandler(logTextArea));
    }

    private void waitForPeerKey() {

        Task task = new Task <Void> (){
            @Override protected Void call() {

                logger.fine("Waiting for peer public key");

                while (true) {

                    byte[] msg = mCommunicationManager.getMessage();

                    if (msg == null) {
                        try {
                            Thread.sleep(200);
                        } catch (Exception e) {
                            logger.fine(e.getMessage());
                        }
                        continue;
                    }

                    Key receivedKey = new Key(new String(msg));
                    logger.fine("Public key received:\n" + receivedKey.readAsPublicKey());

                    mCipher.setPeerPublicKey(receivedKey);

                    Platform.runLater(() -> {
                        readNextMsgBtn.setDisable(false);
                        encodeSendBtn.setDisable(false);
                    });


                    break;
                }

                return null;
            }
        };
        new Thread(task).start();
    }

}
