package nearsoft.academy.bigdata.recommendation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by jonathan on 31/03/17.
 */
public class Finder {
    public static final String PRODUCT_REGEX = "(?<=productId:\\s)\\w+";
    public static final String USER_REGEX = "(?<=userId:\\s)\\w+";

    public static String findInText(String regex, String text){

        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(text);

        if (matcher.find()){
            return matcher.group().trim();
        }

        return "";
    }
}
