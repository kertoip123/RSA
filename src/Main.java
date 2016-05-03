public class Main {

    public static void main(String [] args){

        // simple test
        String text = "abcd qwerty 123456789 zxcvbnmlkjhgfdsa//-=[];/.,";

        AsymmetricCipher bob   = new RSACipher();
        AsymmetricCipher alice = new RSACipher();

        // Bob sends public key to Alice
        alice.setPeerPublicKey(bob.getPublicKey());

        // Alice encodes message
        byte[] encodedText = alice.messageEncode(text.getBytes());

        // Bob decodes message
        byte[] decodedText = bob.messageDecode(encodedText);

        System.out.println("plain text: " + text);
        //System.out.println("encoded text: " + encodedText);
        System.out.println("decoded text: " + new String(decodedText));

        System.out.println(String.format("Bob's Public Key: %s", bob.getPublicKey()));
        System.out.println(String.format("Alice's Public Key: %s", alice.getPublicKey()));
    }
}
