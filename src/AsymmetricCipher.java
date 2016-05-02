import java.math.BigInteger;

public interface AsymmetricCipher {

    /* Sets the key length. */
    void setKeyLength(int keyLength);

    /* Sets Public Key of peer application. Used for encoding messages for peer application. */
    void setPublicKey(BigInteger publicKey);

    /* Returns its own Public Key or null if its not yet generated (called before generateKeys()).  */
    BigInteger getPublicKey();

    /* Returns modulus value which is used in both the encryption and decryption operations.
       Returns null if called before generateKeys().
    */
    BigInteger getModulus();

    /* Sets modulus value */
    void setModulus(BigInteger modulus);

    /* Generate Public and Private Key. */
    void generateKeys();

    /* Decodes message with generated Private Key. Returns null if called before generateKeys() */
    byte [] messageDecode(byte [] encodedMessage);

    /* Encodes message with Public Key of peer application. Returns null if called before setPublicKey() */
    byte [] messageEncode( byte [] message);

}
