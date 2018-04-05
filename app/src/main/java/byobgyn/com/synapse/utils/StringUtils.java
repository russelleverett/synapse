package byobgyn.com.synapse.utils;

public class StringUtils {
    static public String convert(String amount) {
        String rvalue = "";
        int length = amount.replace("-", "").length();
        for(int i = amount.length() - 1; i >= 0; i--) {
            int currLength = rvalue.replace(",", "").replace("-", "").length();
            if(rvalue.length() > 0 && currLength != length && (currLength % 3) == 0)
                rvalue = "," + rvalue;
            rvalue = amount.charAt(i) + rvalue;
        }
        return rvalue;
    }
}
