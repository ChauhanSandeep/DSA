/**
 * Main class to run the program.
 * 
 * Purpose:
 * - This is the entry point of the program.
 * - Currently, it just prints a welcome message to the console.
 * 
 * Intuition & Logic:
 * - The main method is the entry point for the program.
 * - In this case, it simply outputs a message to the console using System.out.println.
 * 
 * Algorithm:
 * - The program only executes a single command to print a message, which is O(1) in time complexity.
 * 
 * Time Complexity: O(1)
 * Space Complexity: O(1)
 * 
 * LeetCode problem link (if applicable): N/A (this is just a simple program).
 */
public class Main {

    /**
     * Entry point for the program.
     * 
     * @param args Command-line arguments (not used in this implementation).
     */
    public static void main(String[] args) {
        // Print a welcome message to the console
        System.out.println("Welcome to tesseract-core - Your Java Algorithm Repository!");
        System.out.println("This repository contains 650+ algorithm implementations organized by topics.");
        System.out.println("");
        System.out.println("🎯 Topics covered:");
        System.out.println("   • Arrays & Strings");
        System.out.println("   • Trees & Graphs");
        System.out.println("   • Dynamic Programming");
        System.out.println("   • System Design");
        System.out.println("   • And many more...");
        System.out.println("");
        System.out.println("🚀 Ready for your Google/Meta/Amazon interviews!");
        System.out.println("");
        System.out.println("Run specific algorithms with:");
        System.out.println("./gradlew runAlgorithm -PmainClass=Array.ThreeSum");
    }
}
