/**
 * Repository entry point for a quick command-line smoke check.
 *
 * Prints a short welcome banner that orients readers to the major topic areas
 * and shows the Gradle command for running a specific algorithm demo.
 */
public class Main {

    /**
     * Prints the repository welcome message.
     *
     * @param args command-line arguments, not used
     */
    public static void main(String[] args) {
        System.out.printf("input=%s -> %s  expected=%s%n", "no args", "welcome banner", "welcome banner");
        System.out.println("Welcome to tesseract-core - Your Java Algorithm Repository!");
        System.out.println("This repository contains 650+ algorithm implementations organized by topics.");
        System.out.println("");
        System.out.println("Topics covered:");
        System.out.println("   - Arrays & Strings");
        System.out.println("   - Trees & Graphs");
        System.out.println("   - Dynamic Programming");
        System.out.println("   - System Design");
        System.out.println("   - And many more...");
        System.out.println("");
        System.out.println("Ready for your Google/Meta/Amazon interviews!");
        System.out.println("");
        System.out.println("Run specific algorithms with:");
        System.out.println("./gradlew runAlgorithm -PmainClass=Array.ThreeSum");
    }
}
