import java.math.BigInteger;

public interface AsymetricCipher {

    /* Sets Public Key of peer application. Used for encoding messages for peer application. */
    void setPublicKey(BigInteger publicKey);

    /* Returns its own Public Key or null if its not yet generated (called before generateKeys()).  */
    BigInteger getPublicKey(void);

    /* Generate Public and Private Key. */
    void generateKeys();

    /* Decodes message with generated Private Key. Returns null if called before generateKeys() */
    String messageDecode(String encodedMessage);

    /* Encodes message with Public Key of peer application. Returns null if called before setPublicKey() */
    String messageEncode(String message);
}
