
public class Main {

    public static void main(String [] args){

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

        ServerManager serverManager = null;
        ClientManager clientManager = null;
        try{
            int port = 53705;
            serverManager = new ServerManager(port);
            clientManager = new ClientManager("localhost", port);
            for(int i=1; i < 5; i++){
                clientManager.send("Starting loop iteration " + i);
                System.out.println("Main: Thread sleeping in iteration: " + i);
                Thread.sleep(1000);
                serverManager.send("Finishing loop iteration " + i);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        System.out.println("Client message queue: ");
        String message;
        do{
            message = clientManager.getMessage();
            System.out.println(message);
        } while(message != null);

        System.out.println("Server message queue: ");
        do{
            message = serverManager.getMessage();
            System.out.println(message);
        } while(message != null);

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
