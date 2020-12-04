package GUI.UtilityFunctions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringToJLabel {
    public static String correctFormat(String text){
        StringBuilder result = new StringBuilder();
        String REGEX = "\n";
        // create a pattern
        Pattern pattern = Pattern.compile(REGEX);

        // get a matcher object
        Matcher matcher = pattern.matcher(text);

        int previousEnd = 0;
        int currentStart = 0;
        int currentEnd = 0;
        result.append("<html>");
        while(matcher.find()) {
            currentStart = matcher.start();
            currentEnd = matcher.end();
            result.append(text.substring(previousEnd,currentStart));
            result.append("<br/>");
            previousEnd = currentEnd;
        }
        if(previousEnd<text.length()){
            result.append(text.substring(previousEnd));
        }
        result.append("</html>");
        return result.toString();
    }

}
