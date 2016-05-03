import java.math.BigInteger;

public class Key {

    private BigInteger mModulus;
    private BigInteger mValue;

    public Key(BigInteger modulus, BigInteger value){
        mModulus = modulus;
        mValue   = value;
    }

    public BigInteger getModulus(){
        return mModulus;
    }

    public BigInteger getValue(){
        return mValue;
    }

    public int countModulusByteSize(){
        return (int) Math.ceil((mModulus.bitLength()+1)/8.0);
    }

    public String toString(){
        return String.format("(%s, %s)", mModulus.toString(), mValue.toString());
    }
}
