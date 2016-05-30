import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;

public class Key {

    private BigInteger mModulus;
    private BigInteger mValue;

    public Key(BigInteger modulus, BigInteger value){
        mModulus = modulus;
        mValue   = value;
    }

    public Key(String str){
        int end = str.indexOf(',');
        String modulusStr = str.substring(0, end);
        String valueStr = str.substring(end+1);

        mModulus = new BigInteger(modulusStr, 10);
        mValue = new BigInteger(valueStr, 10);
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
        return String.format("%s,%s", mModulus.toString(), mValue.toString());
    }


    public String readAsPublicKey(){

        StringWriter text = new StringWriter();
        PrintWriter out = new PrintWriter(text);
        out.println("n = " + mModulus.toString(16));
        out.print("e = " + mValue.toString(16));

        return text.toString();
    }

}
