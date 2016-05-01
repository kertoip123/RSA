import java.math.BigInteger;
import java.util.Random;

/**
 * Created by piotrek on 01.05.2016.
 */

public class MathTools {

    public static boolean isPrime(int n){
        for(int i = 2; i*i < n; ++i){
            if(n%i == 0)
                return false;
        }
        return true;
    }

    public static int gcd(long a, long b){
        BigInteger b1 = BigInteger.valueOf(a);
        BigInteger b2 = BigInteger.valueOf(b);
        BigInteger gcd = b1.gcd(b2);
        return gcd.intValue();
    }

    public static int generatePrimeNumber(int min, int max){

        Random rand = new Random();
        int randNum;

        while(true){
            randNum = rand.nextInt(max-min+1) + min;
            if(isPrime(randNum))
                break;
        }

        return randNum;
    }

}
