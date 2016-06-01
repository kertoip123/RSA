import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;

public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("config_panel.fxml"));

        primaryStage.setTitle("RSA config panel");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


        //test
        /*
        new Thread(() -> {
            test();

        }).start();
        */
    }

    public static void main(String [] args){ launch(args); }


    public static MainWindowController initMainApplication(Stage parent) throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader();

        Parent root = fxmlLoader.load(Main.class.getResource("main_panel.fxml").openStream());

        Stage stage = new Stage();
        stage.setTitle("RSA Application");
        stage.setScene(new Scene(root));
        
        double x = parent.getX() + parent.getWidth()/2;
        double y = parent.getY() + parent.getHeight()/2;

        stage.show();
        stage.setX(x  - stage.getWidth()/2);
        stage.setY(y  - stage.getHeight()/2);

        MainWindowController controller = fxmlLoader.getController();
        controller.initLogger();

        stage.setOnCloseRequest(we -> {
            controller.closeConnection();
            stage.close();
            System.exit(0);
        });

        return controller;
    }


    private void test(){

        //        // simple test
//        String text = "abcd qwerty 123456789 zxcvbnmlkjhgfdsa//-=[];/.,";
//
//        AsymmetricCipher bob   = new RSACipher();
//        AsymmetricCipher alice = new RSACipher();
//
//        // Bob sends public key to Alice
//        alice.setPeerPublicKey(bob.getPublicKey());
//
//        // Alice encodes message
//        byte[] encodedText = alice.messageEncode(text.getBytes());
//
//        // Bob decodes message
//        byte[] decodedText = bob.messageDecode(encodedText);
//
//        System.out.println("plain text: " + text);
//        //System.out.println("encoded text: " + encodedText);
//        System.out.println("decoded text: " + new String(decodedText));
//
//        System.out.println(String.format("Bob's Public Key: %s", bob.getPublicKey()));
//        System.out.println(String.format("Alice's Public Key: %s", alice.getPublicKey()));

        RSACipher rsa = new RSACipher(128);
        System.out.println(rsa.getPublicKey().toString());
        Key  key = new Key(rsa.getPublicKey().toString());
       // System.out.println(key.toString());

        ServerManager serverManager = null;
        ClientManager clientManager = null;
        try{
            int port = 53705;
            serverManager = new ServerManager(port);
            clientManager = new ClientManager("localhost", port);
            for(int i=1; i < 5; i++){
                clientManager.send(new String("Starting loop iteration " + i).getBytes());
                System.out.println("Main: Thread sleeping in iteration: " + i);
                Thread.sleep(1000);
                serverManager.send(new String("Finishing loop iteration " + i).getBytes());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        System.out.println("Client message queue: ");
        byte [] message;
        while(true){
            message = clientManager.getMessage();
            if(message == null)
                break;
            System.out.println(new String(message));
        }

        System.out.println("Server message queue: ");
        while(true){
            message = serverManager.getMessage();
            if(message == null)
                break;
            System.out.println(new String(message));
        }

        try{
            if(clientManager != null){
                clientManager.close();
            }
            if(serverManager != null){
                serverManager.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
}
