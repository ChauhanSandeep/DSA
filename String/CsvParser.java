package String;

import java.util.ArrayList;
import java.util.List;

/**
 * CSV Parser
 *
 * This program parses a CSV input while correctly handling:
 * - Quoted values (e.g., `"San Francisco, CA"`)
 * - Escaped double quotes within quotes (e.g., `"Alexandra ""Alex"""`)
 * - Empty fields
 * 
 * Approach:
 * - Use a character-wise traversal to correctly parse fields.
 * - Handle double quotes using a flag to track whether we are inside a quoted field.
 * - Ensure proper escaping of double quotes.
 *
 * Time Complexity: O(N), where N is the total number of characters in the input.
 * Space Complexity: O(M), where M is the number of parsed fields.
 */
public class CsvParser {
    public static void main(String[] args) {
        String[] input = {
                "\"Alexandra \"\"Alex\"\"\",Menendez,alex.menendez@gmail.com,Miami,1",
                "John,Smith,john.smith@gmail.com,Los Angeles,10",
                "Jane,Roberts,janer@msn.com,\"San Francisco, CA\",0",
                "1,2,,4,\"5\""
        };

        List<List<String>> parsedData = parseCSV(input);

        // Print parsed CSV output
        for (List<String> row : parsedData) {
            System.out.println(row);
        }
    }

    /**
     * Parses multiple lines of CSV input.
     *
     * @param csvLines Array of CSV-formatted strings.
     * @return List of rows, where each row is a list of parsed fields.
     */
    public static List<List<String>> parseCSV(String[] csvLines) {
        List<List<String>> parsedRows = new ArrayList<>();
        for (String line : csvLines) {
            parsedRows.add(parseCSVLine(line));
        }
        return parsedRows;
    }

    /**
     * Parses a single line of CSV data, handling quoted fields properly.
     *
     * @param line The CSV line as a string.
     * @return List of parsed fields.
     */
    public static List<String> parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder fieldBuilder = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char currentChar = line.charAt(i);

            if (currentChar == '"') {
                // If we are inside quotes and see another quote, check if it's escaped
                if (insideQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    fieldBuilder.append('"'); // Add escaped quote
                    i++; // Skip next quote
                } else {
                    insideQuotes = !insideQuotes; // Toggle quote state
                }
            } else if (currentChar == ',' && !insideQuotes) {
                // End of field (outside quotes)
                fields.add(cleanField(fieldBuilder.toString()));
                fieldBuilder.setLength(0); // Reset field builder
            } else {
                // Normal character
                fieldBuilder.append(currentChar);
            }
        }

        // Add last field
        fields.add(cleanField(fieldBuilder.toString()));

        return fields;
    }

    /**
     * Cleans up field data by trimming and removing unnecessary surrounding quotes.
     *
     * @param field The raw field string.
     * @return The cleaned field value.
     */
    private static String cleanField(String field) {
        if (field.startsWith("\"") && field.endsWith("\"")) {
            return field.substring(1, field.length() - 1); // Remove surrounding quotes
        }
        return field.trim(); // Trim any leading/trailing whitespace
    }
}
