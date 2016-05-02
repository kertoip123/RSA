import java.math.BigInteger;
import java.util.*;

/**
 * Created by piotrek on 02.05.2016.
 */
public class RSACipher implements AsymmetricCipher {

    private int keyLength = 128;
    private int singleBlockLen = 32;

    private BigInteger d = null;
    private BigInteger e = null;
    private BigInteger n = null;

    public void setKeyLength(int keyLength){
        this.keyLength = keyLength;
    }

    public void setPublicKey(BigInteger publicKey){
        this.e = publicKey;
    }


    public BigInteger getPublicKey(){
        return e;
    }


    public BigInteger getModulus(){
        return n;
    }

    public void setModulus(BigInteger modulus){
        this.n = modulus;
    }


    public void generateKeys(){

        BigInteger p = BigInteger.probablePrime(keyLength/2, new Random());
        BigInteger q;
        while(true) {
            q = BigInteger.probablePrime(keyLength/2, new Random());
            if(!q.equals(p))
                break;
        }

        // n = p * q
        n = p.multiply(q);
        //System.out.println(n.bitLength());

        // totient = (p-1) * (q - 1)
        BigInteger totient = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        //d = generatePrimeLowerThan(totient);
        d = generateLowerCoprimeTo(totient);
        e = d.modInverse(totient);

        //keyLength = n.bitLength();
        //singleBlockLen = keyLength;
    }


    public byte[] messageEncode(byte [] message){

        if(n == null)
            return null;

        List<byte[]> dividedMessage = divideMessage(message, singleBlockLen/8);
        List<byte[]> encodedBytes = new LinkedList<>();

        BigInteger m, c;

        for(byte [] bytes : dividedMessage){

            m = new BigInteger(bytes);
            c = m.modPow(e, n);
            encodedBytes.add(convertNumberToBytes(c, (int)Math.ceil(n.bitLength()/8.0)+1));

        }

        return aggregateArrays(encodedBytes);

    }

    public byte [] messageDecode(byte [] encodedBytes){

        if(n == null)
            return null;

        List<byte []> dividedEncodedBytes = divideMessage(encodedBytes, (int)Math.ceil(n.bitLength()/8.0)+1);
        List<byte []> decodedBytes = new LinkedList<>();

        BigInteger m, c;

        for(byte [] bytes : dividedEncodedBytes){

            c = new BigInteger(bytes);
            m = c.modPow(d, n);
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
        BigInteger a = null;
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
}
