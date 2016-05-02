import java.math.BigInteger;

/**
 * Created by piotrek on 01.05.2016.
 */
public class Main {

    public static void main(String [] args){

        // simple test
        String text = "abcd qwerty 123456789 zxcvbnmlkjhgfdsa//-=[];/.,";

        AsymmetricCipher bob = new RSACipher();
        AsymmetricCipher alice = new RSACipher();

        // Bob generates public and private key
        bob.generateKeys();

        // Bob sends public key to Alice
        alice.setPublicKey(bob.getPublicKey());
        alice.setModulus(bob.getModulus());

        // Alice encodes message
        byte[] encodedText = alice.messageEncode(text.getBytes());

        // Bob decodes message
        byte[] decodedText = bob.messageDecode(encodedText);

        System.out.println("plain text: " + text);
        //System.out.println("encoded text: " + encodedText);
        System.out.println("decoded text: " + new String(decodedText));


    }
}
