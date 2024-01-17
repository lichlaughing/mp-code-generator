package cn.lichenghao;

public class WordToCamelCaseUtil {
    private static String convertWordToCamelCase(String word) {
        if (word == null || word.isEmpty()) {
            return word;
        }
        String firstChar = word.substring(0, 1).toUpperCase();
        String remainingChars = word.substring(1).toLowerCase();
        return firstChar + remainingChars;
    }

    public static String convertWordsToCamelCase(String words) {
        if (words == null || words.isEmpty()) {
            return words;
        }
        String[] wordsArray = words.split("_");
        for (int i = 0; i < wordsArray.length; i++) {
            wordsArray[i] = convertWordToCamelCase(wordsArray[i]);
        }
        return String.join("", wordsArray);
    }
}
