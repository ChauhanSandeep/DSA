package strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


/**
 * Problem: CSV Parser
 *
 * Parse CSV lines into fields while handling quoted fields, escaped quotes,
 * commas inside quotes, and empty fields. This is a common tokenizer design
 * interview problem rather than a Leetcode problem.
 *
 * Leetcode: Not a Leetcode problem (Interview)
 * Pattern:  String | Tokenization | Quote-state parsing
 *
 * Example:
 *   Input:  line = "1,\"San Francisco, CA\",\"Alex \"\"A\"\"\""
 *   Output: ["1", "San Francisco, CA", "Alex \"A\""]
 *   Why:    quoted commas stay inside the field and doubled quotes decode to one quote.
 *
 * Follow-ups:
 *   1. CSV serializer? Quote fields that need escaping and double embedded quotes.
 *   2. Multiline fields? Carry quote state across physical lines.
 *   3. Custom delimiter? Parameterize delimiter and quote characters.
 */
public class CsvParser {

  public static void main(String[] args) {
    String[] inputs = {"1,2,,4,\"5\"", "\"Alexandra \"\"Alex\"\"\",Menendez,\"San Francisco, CA\""};
    List<List<String>> expected = new ArrayList<>();
    expected.add(Arrays.asList("1", "2", "", "4", "5"));
    expected.add(Arrays.asList("Alexandra \"Alex\"", "Menendez", "San Francisco, CA"));
    for (int i = 0; i < inputs.length; i++) {
      List<String> got = parseSingleLine(inputs[i]);
      System.out.printf("line=%s -> %s  expected=%s%n", inputs[i], got, expected.get(i));
    }
  }


    /**
   * Intuition: when quoted fields do not span lines, each physical line is an
   * independent CSV record. Parse each line with the single-line parser and keep
   * the rows in input order.
   *
   * Algorithm:
   *   1. Create the parsed row list.
   *   2. Parse each input line with parseSingleLine.
   *   3. Add every parsed row to the result.
   *   4. Return all rows.
   *
   * Time:  O(n) - every input character is parsed once.
   * Space: O(f) - returned rows store all parsed fields.
   *
   * @param csvLines CSV records to parse
   * @return parsed rows of fields
   */
  public static List<List<String>> parseCSVLines(String[] csvLines) {
    List<List<String>> parsedRows = new ArrayList<>();
    for (String line : csvLines) {
      parsedRows.add(parseSingleLine(line));
    }
    return parsedRows;
  }

    /**
   * Intuition: a comma ends a field only outside quotes. Quotes toggle quoted
   * mode, except doubled quotes inside quoted mode represent one literal quote.
   * A builder accumulates the current field until an unquoted comma closes it.
   *
   * Algorithm:
   *   1. Scan the line while tracking insideQuotes.
   *   2. Decode doubled quotes inside quoted fields.
   *   3. On an unquoted comma, store the current field and reset the builder.
   *   4. Store the final field after the loop.
   *
   * Time:  O(l) - one scan of the line.
   * Space: O(f) - stores parsed fields for the line.
   *
   * @param line single CSV record
   * @return parsed fields for that record
   */
  public static List<String> parseSingleLine(String line) {
    List<String> fields = new ArrayList<>();
    StringBuilder fieldBuilder = new StringBuilder();
    boolean insideQuotes = false;

    for (int i = 0; i < line.length(); i++) {
      char currentChar = line.charAt(i);

      if (currentChar == '"') {
        if (insideQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
          // Handle escaped quote: two consecutive quotes inside a quoted field
          fieldBuilder.append('"'); // Escaped quote
          i++; // Skip the next quote
        } else {
          insideQuotes = !insideQuotes; // Toggle quoting state
        }
      } else if (currentChar == ',' && !insideQuotes) {
        // Unquoted comma indicates end of field
        fields.add(trimQuotes(fieldBuilder.toString()));
        fieldBuilder.setLength(0); // Reset buffer for next field
      } else {
        // Accumulate character in the current field
        fieldBuilder.append(currentChar);
      }
    }

    // Add the last field (even if empty)
    fields.add(trimQuotes(fieldBuilder.toString()));

    return fields;
  }

    /** Removes one pair of wrapping quotes from a parsed field when present. */
  private static String trimQuotes(String rawField) {
    rawField = rawField.trim();
    if (rawField.startsWith("\"") && rawField.endsWith("\"") && rawField.length() >= 2) {
      return rawField.substring(1, rawField.length() - 1);
    }
    return rawField;
  }
}
