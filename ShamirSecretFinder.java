import java.io.FileReader;
import java.math.BigInteger;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ShamirSecretFinder {

    public static void main(String[] args) throws Exception {
        System.out.println("Secret for Testcase 1:");
        findSecret("testcase1.json");

        System.out.println("\nSecret for Testcase 2:");
        findSecret("testcase2.json");
    }

    static void findSecret(String filename) throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject data = (JSONObject) parser.parse(new FileReader(filename));

        JSONObject keys = (JSONObject) data.get("keys");
        int n = Integer.parseInt(keys.get("n").toString());
        int k = Integer.parseInt(keys.get("k").toString());

        List<Integer> xList = new ArrayList<>();
        List<BigInteger> yList = new ArrayList<>();

        
        for (int i = 1; i <= k; i++) {
            String key = Integer.toString(i);
            JSONObject point = (JSONObject) data.get(key);

            int base = Integer.parseInt(point.get("base").toString());
            String value = point.get("value").toString();
            BigInteger y = new BigInteger(value, base);

            xList.add(i); 
            yList.add(y);
        }

        BigInteger secret = lagrangeInterpolation(xList, yList);
        System.out.println("Secret (constant term c) = " + secret);

    }

    static BigInteger lagrangeInterpolation(List<Integer> x, List<BigInteger> y) {
    int k = x.size();
    BigInteger result = BigInteger.ZERO;

    for (int j = 0; j < k; j++) {
        BigInteger numerator = BigInteger.ONE;
        BigInteger denominator = BigInteger.ONE;

        for (int m = 0; m < k; m++) {
            if (m != j) {
                numerator = numerator.multiply(BigInteger.valueOf(-x.get(m)));
                denominator = denominator.multiply(BigInteger.valueOf(x.get(j) - x.get(m)));
            }
        }

        BigInteger term = y.get(j)
                .multiply(numerator)
                .divide(denominator);
        result = result.add(term);
    }

    return result;
   }

}