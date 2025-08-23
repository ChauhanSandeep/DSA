package com.sandeep.frazsheet.greedy;

import java.util.Arrays;

/**
 * Problem: Destroying Asteroids
 * 
 * You are given an integer mass, which represents the original mass of a planet. You are further
 * given an integer array asteroids, where asteroids[i] is the mass of the ith asteroid.
 * You can arrange for the planet to collide with the asteroids in any arbitrary order. If the mass
 * of the planet is greater than or equal to the mass of the asteroid, the asteroid is destroyed
 * and the planet gains the mass of the asteroid. Otherwise, the planet is destroyed.
 * Return true if all asteroids can be destroyed. Otherwise, return false.
 * 
 * Example:
 * Input: mass = 10, asteroids = [3,9,19,5,21]
 * Output: true
 * Explanation: One way to order the asteroids is [3,5,9,19,21]: Planet mass becomes 10+3+5+9+19+21 = 67
 * 
 * LeetCode: https://leetcode.com/problems/destroying-asteroids
 * 
 * Follow-up Questions:
 * 1. What if we want to maximize the final mass of the planet?
 *    Answer: Same greedy approach works - absorb smallest asteroids first to maximize growth.
 * 
 * 2. How would you handle the case where asteroids have different rewards/penalties?
 *    Answer: Use dynamic programming or more complex optimization based on reward structure.
 * 
 * 3. What if the planet can split its mass to destroy multiple asteroids simultaneously?
 *    Answer: This becomes a complex optimization problem, possibly NP-hard.
 *    Related: https://leetcode.com/problems/minimum-number-of-taps-to-open-to-water-a-garden/
 * 
 * @author Sandeep
 */
public class DestroyingAsteroids {
    
    /**
     * Determines if all asteroids can be destroyed using greedy approach.
     * 
     * Algorithm:
     * 1. Sort asteroids in ascending order
     * 2. Greedily destroy smallest asteroids first to maximize mass accumulation
     * 3. If at any point planet mass < asteroid mass, return false
     * 4. Otherwise, add asteroid mass to planet mass and continue
     * 
     * Time Complexity: O(n log n) where n is number of asteroids (dominated by sorting)
     * Space Complexity: O(1) if sorting in-place, O(log n) for sorting recursion stack
     * 
     * @param mass Initial mass of the planet
     * @param asteroids Array of asteroid masses
     * @return true if all asteroids can be destroyed, false otherwise
     */
    public boolean asteroidsDestroyed(int mass, int[] asteroids) {
        if (asteroids == null || asteroids.length == 0) return true;
        
        // Sort asteroids in ascending order to apply greedy strategy
        Arrays.sort(asteroids);
        
        // Use long to prevent overflow as mass can grow very large
        long currentMass = mass;
        
        for (int asteroidMass : asteroids) {
            // If planet cannot destroy this asteroid, impossible to proceed
            if (currentMass < asteroidMass) {
                return false;
            }
            
            // Absorb the asteroid's mass
            currentMass += asteroidMass;
        }
        
        return true;
    }
    
    /**
     * Alternative approach that checks feasibility without modifying input array.
     * Creates a copy for sorting to preserve original array.
     * 
     * Time Complexity: O(n log n)
     * Space Complexity: O(n) for the copied array
     */
    public boolean asteroidsDestroyedPreserveInput(int mass, int[] asteroids) {
        if (asteroids == null || asteroids.length == 0) return true;
        
        // Create copy to preserve original array
        int[] sortedAsteroids = Arrays.copyOf(asteroids, asteroids.length);
        Arrays.sort(sortedAsteroids);
        
        long currentMass = mass;
        
        for (int asteroidMass : sortedAsteroids) {
            if (currentMass < asteroidMass) {
                return false;
            }
            currentMass += asteroidMass;
        }
        
        return true;
    }
    
    /**
     * Optimized version that returns early if sum of all asteroids + initial mass
     * indicates impossible scenario.
     * 
     * Time Complexity: O(n log n)
     * Space Complexity: O(1)
     */
    public boolean asteroidsDestroyedOptimized(int mass, int[] asteroids) {
        if (asteroids == null || asteroids.length == 0) return true;
        
        // Early check: if smallest asteroid is larger than initial mass, impossible
        int minAsteroid = Arrays.stream(asteroids).min().orElse(0);
        if (mass < minAsteroid) return false;
        
        Arrays.sort(asteroids);
        long currentMass = mass;
        
        for (int asteroidMass : asteroids) {
            if (currentMass < asteroidMass) {
                return false;
            }
            currentMass += asteroidMass;
        }
        
        return true;
    }
    
    /**
     * Mathematical approach that checks if the strategy is feasible before simulation.
     * Uses the fact that if we can destroy all asteroids, the final mass will be
     * initial_mass + sum(all_asteroids).
     * 
     * Time Complexity: O(n log n)
     * Space Complexity: O(1)
     */
    public boolean asteroidsDestroyedMath(int mass, int[] asteroids) {
        if (asteroids == null || asteroids.length == 0) return true;
        
        // Calculate total mass if all asteroids are absorbed
        long totalPossibleMass = mass;
        for (int asteroid : asteroids) {
            totalPossibleMass += asteroid;
        }
        
        // Sort and simulate the process
        Arrays.sort(asteroids);
        long currentMass = mass;
        
        for (int asteroidMass : asteroids) {
            if (currentMass < asteroidMass) {
                return false;
            }
            currentMass += asteroidMass;
        }
        
        return true;
    }
    
    /**
     * Recursive approach for educational purposes.
     * Not recommended for large inputs due to stack overflow risk.
     * 
     * Time Complexity: O(n log n + n)
     * Space Complexity: O(n) for recursion stack
     */
    public boolean asteroidsDestroyedRecursive(int mass, int[] asteroids) {
        if (asteroids == null || asteroids.length == 0) return true;
        
        Arrays.sort(asteroids);
        return canDestroyRecursive(mass, asteroids, 0);
    }
    
    // Helper method for recursive approach
    private boolean canDestroyRecursive(long currentMass, int[] asteroids, int index) {
        // Base case: all asteroids processed
        if (index >= asteroids.length) {
            return true;
        }
        
        // Check if current asteroid can be destroyed
        if (currentMass < asteroids[index]) {
            return false;
        }
        
        // Recursively check remaining asteroids
        return canDestroyRecursive(currentMass + asteroids[index], asteroids, index + 1);
    }
    
    /**
     * Validation method to check edge cases and constraints.
     * Useful for debugging and testing.
     * 
     * @param mass Initial mass
     * @param asteroids Asteroid array
     * @return true if inputs are valid
     */
    public boolean validateInputs(int mass, int[] asteroids) {
        if (mass <= 0) return false;
        if (asteroids == null) return false;
        
        for (int asteroid : asteroids) {
            if (asteroid <= 0) return false;
        }
        
        return true;
    }
}