public interface AsymmetricCipher {

    /* Sets Public Key of peer application. Used for encoding messages for peer application. */
    void setPeerPublicKey(Key publicKey);

    /* Returns its own Public Key or null if its not yet generated (called before generateKeys()).  */
    Key getPublicKey();

    /* Decodes message with generated Private Key. Returns null if called before generateKeys() */
    byte [] messageDecode(byte [] encodedMessage);

    /* Encodes message with Public Key of peer application. Returns null if called before setPeerPublicKey() */
    byte [] messageEncode( byte [] message);

}
