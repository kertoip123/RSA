import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.*;
import java.util.logging.Logger;


public class RSACipher implements AsymmetricCipher {

    private static Logger logger;

    private int mKeyLength = 128;

    private Key mPublicKey;
    private Key mPrivateKey;
    private Key mPeerPublicKey = null;

    public void setPeerPublicKey(Key publicKey){ this.mPeerPublicKey = publicKey; }

    public Key getPublicKey(){
        return mPublicKey;
    }

    public RSACipher(int keyLength){
        mKeyLength = keyLength;
        generateKeys();
    }

    private void generateKeys(){

        int lenDiff = new Random().nextInt(5);

        BigInteger p = BigInteger.probablePrime(mKeyLength/2 + lenDiff, new Random());
        BigInteger q;
        while(true) {
            q = BigInteger.probablePrime(mKeyLength/2 - lenDiff, new Random());
            if(!q.equals(p))
                break;
        }

        // n = p * q
        BigInteger n = p.multiply(q);
        //System.out.println(n.bitLength());

        // totient = (p-1) * (q - 1)
        BigInteger totient = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        //d = generatePrimeLowerThan(totient);
        BigInteger e = generateLowerCoprimeTo(totient);
        BigInteger d = e.modInverse(totient);

        mPublicKey  = new Key(n, e);
        mPrivateKey = new Key(n, d);


        StringWriter text = new StringWriter();
        PrintWriter out = new PrintWriter(text);
        out.println("Generated Keys:");
        out.println("n = " + n.toString(16));
        out.println("e = " + e.toString(16));
        out.print("d = " + d.toString(16));
        logger.fine(text.toString());

        //mKeyLength = n.bitLength();
        //singleBlockLen = mKeyLength;
    }


    public byte[] messageEncode(byte [] message){

        if(mPeerPublicKey == null)
            return null;

        List<byte[]> dividedMessage = divideMessage(message, randomSingleBlockLength());
        List<byte[]> encodedBytes = new LinkedList<>();

        BigInteger m, c;

        for(byte [] bytes : dividedMessage){

            m = new BigInteger(1, bytes);
            c = m.modPow(mPeerPublicKey.getValue(), mPeerPublicKey.getModulus());
            encodedBytes.add(convertNumberToBytes(c, mPeerPublicKey.countModulusByteSize()));

        }

        return aggregateArrays(encodedBytes);
    }

    public byte [] messageDecode(byte [] encodedBytes){

        List<byte []> dividedEncodedBytes = divideMessage(encodedBytes, mPrivateKey.countModulusByteSize());
        List<byte []> decodedBytes = new LinkedList<>();

        BigInteger m, c;
        for(byte [] bytes : dividedEncodedBytes){

            c = new BigInteger(bytes);
            m = c.modPow(mPrivateKey.getValue(), mPrivateKey.getModulus());
            decodedBytes.add(m.toByteArray());
  

        }

        return aggregateArrays(decodedBytes);

    }


    private BigInteger generatePrimeLowerThan(BigInteger n){
        int bitLen = n.bitLength();
        int cmp = 0;
        Random rand = new Random();
        BigInteger bigInteger = null;
        while(cmp < 1){
            bigInteger = BigInteger.probablePrime(bitLen, rand);
            cmp = n.compareTo(bigInteger);
        }
        return bigInteger;
    }

    private BigInteger generateLowerCoprimeTo(BigInteger n){

        int bitLen = n.bitLength();
        BigInteger a;
        Random rand = new Random();

        while(true){
            a = new BigInteger(bitLen, rand);
            if(n.compareTo(a) > 0 && a.gcd(n).compareTo(BigInteger.ONE) == 0)
                break;
        }
        return a;
    }

    private byte[] aggregateArrays(List<byte[]> input){

        int totalLen = 0;
        for(byte [] bytes: input)
            totalLen += bytes.length;

        byte [] result = new byte[totalLen];
        int index = 0;
        for(byte [] bytes: input){
            for(byte b : bytes){
                result[index++] = b;
            }
        }

        return result;
    }

    private List<byte[]> divideMessage(byte [] bytes, int blockLen){

        List<byte[]> dividedArray = new LinkedList<>();

        int start = 0;
        while (start < bytes.length) {
            int end = Math.min(bytes.length, start + blockLen);
            dividedArray.add(Arrays.copyOfRange(bytes, start, end));
            start += blockLen;
        }

        return dividedArray;
    }


    private byte[] convertNumberToBytes(BigInteger a, int numOfBytes){

        byte[] array = a.toByteArray();
        byte [] ret = new byte[numOfBytes];
        Arrays.fill(ret, (byte)0);

        int start = numOfBytes - array.length;
        start = (start > 0) ? start : 0;
        for(int i = start; i < numOfBytes; ++i){
            ret[i] = array[i-start];
        }
        return ret;
    }

    private int randomSingleBlockLength(){
        return (new Random().nextInt(mKeyLength/2)+8)/8;
    }

    public static void setLogger(Logger logger){
        RSACipher.logger = logger;
    }

}
