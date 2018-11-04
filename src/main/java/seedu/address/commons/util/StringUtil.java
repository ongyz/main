package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Helper functions for handling strings.
 */
public class StringUtil {

    /**
     * Returns true if the {@code sentence} contains the {@code word}.
     *   Ignores case, but a full word match is required.
     *   <br>examples:<pre>
     *       containsWordIgnoreCase("ABc def", "abc") == true
     *       containsWordIgnoreCase("ABc def", "DEF") == true
     *       containsWordIgnoreCase("ABc def", "AB") == false //not a full word match
     *       </pre>
     * @param sentence cannot be null
     * @param word cannot be null, cannot be empty, must be a single word
     */
    public static boolean containsWordIgnoreCase(String sentence, String word) {
        requireNonNull(sentence);
        requireNonNull(word);

        String preppedWord = word.trim();
        checkArgument(!preppedWord.isEmpty(), "Word parameter cannot be empty");
        checkArgument(preppedWord.split("\\s+").length == 1,
                "Word parameter should be a single word");

        String preppedSentence = sentence;
        String[] wordsInPreppedSentence = preppedSentence.split("\\s+");

        return Arrays.stream(wordsInPreppedSentence)
                .anyMatch(preppedWord::equalsIgnoreCase);
    }

    /**
     * Returns true if the {@code word} contains the substring {@code toSearch}.
     * Match is only counted from the beginning of {@code word}
     * {@code toSearch} must be of minimum length three before it is considered to
     * avoid one letter match.
     *   Ignores case, only a partial word match is required.
     *   <br>examples:<pre>
     *       containsSubWordIgnoreCase("ABcDef", "cd") == false
     *       containsSubWordIgnoreCase("ABcdEf", "abc") == true
     *       containsSubWordIgnoreCase("Abd", "AB") == false // length less than three
     *       </pre>
     * @param word cannot be null
     * @param toSearch cannot be null, cannot be empty, must be a single word
     */
    public static boolean isSubstringMatchFromIndexZero(String word, String toSearch) {
        requireNonNull(word);
        requireNonNull(toSearch);
        String preppedToSearch = toSearch.trim();
        checkArgument(!preppedToSearch.isEmpty(), "Word parameter cannot be empty");
        checkArgument(preppedToSearch.split("\\s+").length == 1,
                "Word parameter should be a single word");
        // Length of keyword must exceed 2
        if (preppedToSearch.length() <= 2) {
            return false;
        }
        for (int i = 0; i < preppedToSearch.length(); i++) {
            if (Character.toLowerCase(word.charAt(i)) != Character.toLowerCase(preppedToSearch.charAt(i))) {
                return false;
            }
        }

        return true;
    }
    /**
     * Returns a detailed message of the t, including the stack trace.
     */
    public static String getDetails(Throwable t) {
        requireNonNull(t);
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return t.getMessage() + "\n" + sw.toString();
    }

    /**
     * Returns true if {@code s} represents a non-zero unsigned integer
     * e.g. 1, 2, 3, ..., {@code Integer.MAX_VALUE} <br>
     * Will return false for any other non-null string input
     * e.g. empty string, "-1", "0", "+1", and " 2 " (untrimmed), "3 0" (contains whitespace), "1 a" (contains letters)
     * @throws NullPointerException if {@code s} is null.
     */
    public static boolean isNonZeroUnsignedInteger(String s) {
        requireNonNull(s);

        try {
            int value = Integer.parseInt(s);
            return value > 0 && !s.startsWith("+"); // "+1" is successfully parsed by Integer#parseInt(String)
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
