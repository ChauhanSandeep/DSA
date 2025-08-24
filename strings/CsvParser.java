package strings;

import java.util.ArrayList;
import java.util.List;


/**
 * CSV Parser
 *
 * Problem Statement:
 * Given multiple lines of CSV-formatted input strings, parse each line and extract fields correctly,
 * considering CSV-specific edge cases such as:
 * - Quoted fields: e.g., `"San Francisco, CA"` should be parsed as a single field.
 * - Escaped quotes within quoted fields: e.g., `"Alexandra ""Alex"""` should parse as `Alexandra "Alex"`.
 * - Empty fields: consecutive commas like `1,,2` imply an empty field between `1` and `2`.
 *
 * Not a Leetcode problem, but frequently asked in FAANG-style rounds as a string parsing or tokenizer design question.
 *
 * Example:
 * Input:
 * [
 *   "\"Alexandra \"\"Alex\"\"\",Menendez,alex.menendez@gmail.com,Miami,1",
 *   "John,Smith,john.smith@gmail.com,Los Angeles,10",
 *   "Jane,Roberts,janer@msn.com,\"San Francisco, CA\",0",
 *   "1,2,,4,\"5\""
 * ]
 *
 * Output:
 * [
 *   ["Alexandra \"Alex\"", "Menendez", "alex.menendez@gmail.com", "Miami", "1"],
 *   ["John", "Smith", "john.smith@gmail.com", "Los Angeles", "10"],
 *   ["Jane", "Roberts", "janer@msn.com", "San Francisco, CA", "0"],
 *   ["1", "2", "", "4", "5"]
 * ]
 *
 * 🔍 Follow-up Questions:
 * - Can you write a CSV serializer that converts structured data back into CSV?
 * - How would you handle multiline quoted fields in streaming input? (Hint: maintain state across lines)
 * - What changes are needed to support configurable delimiters (e.g., semicolon `;` instead of comma)?
 *
 */
public class CsvParser {

  public static void main(String[] args) {
    String[] input = {"\"Alexandra \"\"Alex\"\"\",Menendez,alex.menendez@gmail.com,Miami,1",
        "John,Smith,john.smith@gmail.com,Los Angeles,10", "Jane,Roberts,janer@msn.com,\"San Francisco, CA\",0",
        "1,2,,4,\"5\""};

    List<List<String>> parsedData = parseCSVLines(input);

    for (List<String> row : parsedData) {
      System.out.println(row);
    }
  }

  /**
   * Parses an array of CSV lines into structured rows of fields.
   *
   * @param csvLines An array of strings, each representing a CSV line.
   * @return A list of rows, where each row is a list of parsed field values.
   *
   * 🔹 Steps:
   * - Iterate over each line.
   * - Use parseSingleLine() to parse each line independently.
   *
   * Time Complexity: O(N), where N = total characters in all input lines.
   * Space Complexity: O(M), where M = total number of parsed fields.
   */
  public static List<List<String>> parseCSVLines(String[] csvLines) {
    List<List<String>> parsedRows = new ArrayList<>();
    for (String line : csvLines) {
      parsedRows.add(parseSingleLine(line));
    }
    return parsedRows;
  }

  /**
   * Parses a single line of CSV input, accounting for quotes, escaped quotes, and commas inside quotes.
   *
   * @param line A single line of CSV data.
   * @return A list of parsed fields for the line.
   *
   * 🔹 Steps:
   * - Traverse character-by-character.
   * - Maintain a flag `insideQuotes` to track quoted sections.
   * - Accumulate characters in `fieldBuilder` until a field delimiter (unquoted comma) is found.
   * - Handle escaped quotes (i.e., double double-quotes).
   *
   * Time Complexity: O(L), where L = number of characters in the line.
   * Space Complexity: O(F), where F = number of fields in the line.
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

  /**
   * Removes wrapping quotes from a field and trims whitespace if applicable.
   *
   * @param rawField The raw field string as accumulated from CSV.
   * @return Cleaned and unquoted field value.
   *
   * 🔹 Example: "\"Alex\"" -> Alex ; " San Francisco " -> " San Francisco "
   */
  private static String trimQuotes(String rawField) {
    rawField = rawField.trim();
    if (rawField.startsWith("\"") && rawField.endsWith("\"") && rawField.length() >= 2) {
      return rawField.substring(1, rawField.length() - 1);
    }
    return rawField;
  }
}
