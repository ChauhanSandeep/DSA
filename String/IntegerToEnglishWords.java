package String;

import java.util.*;

/**
 * 273. Integer to English Words
 *
 * Problem: Convert a non-negative integer to its English words representation.
 *
 * Example:
 * Input: num = 123
 * Output: "One Hundred Twenty Three"
 *
 * Input: num = 12345
 * Output: "Twelve Thousand Three Hundred Forty Five"
 *
 * LeetCode: https://leetcode.com/problems/integer-to-english-words
 *
 * Follow-up questions:
 * Q: How to handle different number systems (British vs American)?
 * A: Extend with locale-specific word mappings and rules.
 *
 * Q: Can we support other languages?
 * A: Create pluggable language interfaces with different grammar rules.
 *
 * Q: How to handle ordinal numbers (1st, 2nd, 3rd)?
 * A: Add ordinal conversion methods with appropriate suffix rules.
 */
public class IntegerToEnglishWords {

    // Word mappings for different number ranges
    private static final String[] ONES = {
        "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
        "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
        "Seventeen", "Eighteen", "Nineteen"
    };

    private static final String[] TENS = {
        "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
    };

    private static final String[] THOUSANDS = {
        "", "Thousand", "Million", "Billion"
    };

    public static void main(String args[]) {
        IntegerToEnglishWords obj = new IntegerToEnglishWords();
        System.out.println(obj.numberToWords(1234567891)); // "One Billion Two Hundred Thirty Four Million Five Hundred Sixty Seven Thousand Eight Hundred Ninety One"
    }

    /**
     * Recursive divide-and-conquer approach.
     *
     * Algorithm: Hierarchical decomposition
     * - Handle special case of zero
     * - Recursively process groups of three digits
     * - Combine with appropriate scale words (thousand, million, billion)
     * - Use helper for converting hundreds
     *
     * Time Complexity: O(log n) where n is the input number
     * Space Complexity: O(log n) for recursion stack
     */
    public String numberToWords(int num) {
        if (num == 0) return "Zero";

        return numberToWordsHelper(num).trim();
    }

    private String numberToWordsHelper(int num) {
        if (num == 0) return "";
        else if (num < 20) return ONES[num] + " ";
        else if (num < 100) return TENS[num / 10] + " " + numberToWordsHelper(num % 10);
        else if (num < 1000) return ONES[num / 100] + " Hundred " + numberToWordsHelper(num % 100);

        // Handle thousands, millions, billions
        for (int i = 3; i >= 1; i--) {
            int divisor = (int) Math.pow(1000, i);
            if (num >= divisor) {
                return numberToWordsHelper(num / divisor) + THOUSANDS[i] + " " +
                       numberToWordsHelper(num % divisor);
            }
        }

        return "";
    }

    /**
     * Iterative approach processing chunks.
     * More explicit handling of different magnitude groups.
     */
    public String numberToWordsIterative(int num) {
        if (num == 0) return "Zero";

        List<String> result = new ArrayList<>();
        int chunkIndex = 0;

        while (num > 0) {
            if (num % 1000 != 0) {
                String chunk = convertHundreds(num % 1000);
                if (chunkIndex > 0) {
                    chunk += " " + THOUSANDS[chunkIndex];
                }
                result.add(chunk);
            }

            num /= 1000;
            chunkIndex++;
        }

        // Reverse and join
        Collections.reverse(result);
        return String.join(" ", result);
    }

    // Convert number less than 1000 to words
    private String convertHundreds(int num) {
        StringBuilder sb = new StringBuilder();

        // Hundreds place
        if (num >= 100) {
            sb.append(ONES[num / 100]).append(" Hundred");
            num %= 100;
            if (num > 0) sb.append(" ");
        }

        // Tens and ones place
        if (num >= 20) {
            sb.append(TENS[num / 10]);
            num %= 10;
            if (num > 0) sb.append(" ");
        }

        if (num > 0) {
            sb.append(ONES[num]);
        }

        return sb.toString();
    }

    /**
     * Object-oriented approach with NumberConverter class.
     * More maintainable and extensible design.
     */
    public static class NumberConverter {
        private final String[] ones;
        private final String[] tens;
        private final String[] scales;
        private final String zero;

        public NumberConverter() {
            this(ONES, TENS, THOUSANDS, "Zero");
        }

        public NumberConverter(String[] ones, String[] tens, String[] scales, String zero) {
            this.ones = ones;
            this.tens = tens;
            this.scales = scales;
            this.zero = zero;
        }

        public String convert(int num) {
            if (num == 0) return zero;

            return convertHelper(num).trim();
        }

        private String convertHelper(int num) {
            if (num == 0) return "";
            else if (num < 20) return ones[num] + " ";
            else if (num < 100) return tens[num / 10] + " " + convertHelper(num % 10);
            else if (num < 1000) return ones[num / 100] + " Hundred " + convertHelper(num % 100);

            for (int i = 3; i >= 1; i--) {
                int divisor = (int) Math.pow(1000, i);
                if (num >= divisor) {
                    return convertHelper(num / divisor) + scales[i] + " " +
                           convertHelper(num % divisor);
                }
            }

            return "";
        }
    }

    /**
     * Template-based approach for different locales.
     * Supports multiple languages and number systems.
     */
    public static class LocalizedNumberConverter {

        public static class Locale {
            private final String[] ones;
            private final String[] tens;
            private final String[] scales;
            private final String zero;
            private final String hundred;

            private Locale(String[] ones, String[] tens, String[] scales, String zero, String hundred) {
                this.ones = ones;
                this.tens = tens;
                this.scales = scales;
                this.zero = zero;
                this.hundred = hundred;
            }

            public static final Locale US_ENGLISH = new Locale(
                ONES, TENS, THOUSANDS, "Zero", "Hundred"
            );

            public static final Locale UK_ENGLISH = new Locale(
                ONES, TENS, new String[]{"", "Thousand", "Million", "Milliard"}, "Zero", "Hundred"
            );
        }

        private final Locale locale;

        public LocalizedNumberConverter(Locale locale) {
            this.locale = locale;
        }

        public String convert(int num) {
            // Implementation uses locale-specific rules
            NumberConverter converter = new NumberConverter(
                locale.ones, locale.tens, locale.scales, locale.zero
            );
            return converter.convert(num);
        }
    }

    /**
     * Optimized approach using StringBuilder and minimal string operations.
     * Reduces memory allocations for better performance.
     */
    public String numberToWordsOptimized(int num) {
        if (num == 0) return "Zero";

        StringBuilder result = new StringBuilder();
        convertOptimized(num, result);
        return result.toString().trim();
    }

    private void convertOptimized(int num, StringBuilder sb) {
        if (num == 0) return;

        if (num < 20) {
            sb.append(ONES[num]).append(" ");
        } else if (num < 100) {
            sb.append(TENS[num / 10]).append(" ");
            convertOptimized(num % 10, sb);
        } else if (num < 1000) {
            sb.append(ONES[num / 100]).append(" Hundred ");
            convertOptimized(num % 100, sb);
        } else {
            for (int i = 3; i >= 1; i--) {
                int divisor = (int) Math.pow(1000, i);
                if (num >= divisor) {
                    convertOptimized(num / divisor, sb);
                    sb.append(THOUSANDS[i]).append(" ");
                    convertOptimized(num % divisor, sb);
                    break;
                }
            }
        }
    }

    /**
     * Table-driven approach using precomputed mappings.
     * Fastest for frequently called conversions.
     */
    public static class TableDrivenConverter {
        private static final Map<Integer, String> CACHE = new HashMap<>();

        static {
            // Precompute common numbers
            for (int i = 0; i <= 100; i++) {
                CACHE.put(i, computeWords(i));
            }

            // Precompute powers of 10
            CACHE.put(1000, "One Thousand");
            CACHE.put(1000000, "One Million");
            CACHE.put(1000000000, "One Billion");
        }

        public String convert(int num) {
            String cached = CACHE.get(num);
            if (cached != null) return cached;

            // Fall back to computation
            return new NumberConverter().convert(num);
        }

        private static String computeWords(int num) {
            return new NumberConverter().convert(num);
        }
    }

    /**
     * Extended converter supporting ordinal numbers.
     * Converts to "first", "second", "third", etc.
     */
    public String numberToOrdinalWords(int num) {
        if (num <= 0) throw new IllegalArgumentException("Ordinal numbers must be positive");

        String cardinal = numberToWords(num);
        return convertToOrdinal(cardinal, num);
    }

    private String convertToOrdinal(String cardinal, int num) {
        String[] parts = cardinal.split(" ");
        String lastWord = parts[parts.length - 1];

        // Special cases for ordinal conversion
        String ordinalLastWord = convertWordToOrdinal(lastWord, num % 100);
        parts[parts.length - 1] = ordinalLastWord;

        return String.join(" ", parts);
    }

    private String convertWordToOrdinal(String word, int lastTwoDigits) {
        // Handle teens specially (11th, 12th, 13th, not 11st, 12nd, 13rd)
        if (lastTwoDigits >= 11 && lastTwoDigits <= 13) {
            return word + "th";
        }

        switch (lastTwoDigits % 10) {
            case 1: return word.replace("One", "First");
            case 2: return word.replace("Two", "Second");
            case 3: return word.replace("Three", "Third");
            default: return word + "th";
        }
    }

    /**
     * Converter supporting custom formatting options.
     * Allows various output formats and styles.
     */
    public static class FormattingConverter {

        public enum Style {
            TITLE_CASE,    // "One Hundred Twenty Three"
            LOWER_CASE,    // "one hundred twenty three"
            UPPER_CASE,    // "ONE HUNDRED TWENTY THREE"
            SENTENCE_CASE  // "One hundred twenty three"
        }

        public String convert(int num, Style style) {
            String result = new NumberConverter().convert(num);

            switch (style) {
                case LOWER_CASE:
                    return result.toLowerCase();
                case UPPER_CASE:
                    return result.toUpperCase();
                case SENTENCE_CASE:
                    return result.substring(0, 1).toUpperCase() +
                           result.substring(1).toLowerCase();
                default:
                    return result; // TITLE_CASE is default
            }
        }

        public String convertWithSeparator(int num, String separator) {
            String result = new NumberConverter().convert(num);
            return result.replace(" ", separator);
        }

        public String convertAbbreviated(int num) {
            if (num >= 1000000000) {
                return String.format("%.1fB", num / 1000000000.0);
            } else if (num >= 1000000) {
                return String.format("%.1fM", num / 1000000.0);
            } else if (num >= 1000) {
                return String.format("%.1fK", num / 1000.0);
            } else {
                return String.valueOf(num);
            }
        }
    }

    /**
     * Batch converter for processing multiple numbers efficiently.
     * Optimized for bulk conversions.
     */
    public static class BatchConverter {
        private final NumberConverter converter;
        private final Map<Integer, String> cache;

        public BatchConverter() {
            this.converter = new NumberConverter();
            this.cache = new HashMap<>();
        }

        public List<String> convertBatch(int[] numbers) {
            List<String> results = new ArrayList<>();

            for (int num : numbers) {
                String result = cache.computeIfAbsent(num, converter::convert);
                results.add(result);
            }

            return results;
        }

        public Map<Integer, String> convertToMap(int[] numbers) {
            Map<Integer, String> results = new HashMap<>();

            for (int num : numbers) {
                results.put(num, cache.computeIfAbsent(num, converter::convert));
            }

            return results;
        }

        public void clearCache() {
            cache.clear();
        }

        public int getCacheSize() {
            return cache.size();
        }
    }

    /**
     * Validation and utility methods.
     * Helper methods for testing and validation.
     */
    public static class NumberWordsUtils {

        public static boolean isValidEnglishNumber(String words) {
            // Simplified validation - in practice, would need full parser
            if (words == null || words.trim().isEmpty()) return false;

            String[] parts = words.trim().split("\\s+");
            Set<String> validWords = new HashSet<>();

            validWords.addAll(Arrays.asList(ONES));
            validWords.addAll(Arrays.asList(TENS));
            validWords.addAll(Arrays.asList(THOUSANDS));
            validWords.add("Hundred");
            validWords.add("Zero");

            for (String part : parts) {
                if (!validWords.contains(part)) {
                    return false;
                }
            }

            return true;
        }

        public static int parseEnglishNumber(String words) {
            // Simplified parser - full implementation would be much more complex
            throw new UnsupportedOperationException("English to number parsing not implemented");
        }

        public static String normalizeSpacing(String words) {
            return words.trim().replaceAll("\\s+", " ");
        }

        public static boolean areEquivalent(String words1, String words2) {
            return normalizeSpacing(words1.toLowerCase())
                    .equals(normalizeSpacing(words2.toLowerCase()));
        }
    }
}