package com.sandeep.frazsheet.stack;

import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Asteroid Collision
 * 
 * We are given an array asteroids of integers representing asteroids in a row.
 * For each asteroid, the absolute value represents its size, and the sign represents its direction
 * (positive meaning right, negative meaning left). Each asteroid moves at the same speed.
 * Find out the state of the asteroids after all collisions.
 * 
 * Example:
 * Input: asteroids = [5,10,-5]
 * Output: [5,10]
 * Explanation: The 10 and -5 collide resulting in 10. The 5 and 10 never collide.
 * 
 * LeetCode: https://leetcode.com/problems/asteroid-collision
 * 
 * Follow-up Questions:
 * 1. What if asteroids have different speeds?
 *    Answer: Need to track time and position, becomes much more complex simulation problem.
 * 
 * 2. How would you handle 3D asteroid collisions?
 *    Answer: Extend to 3D vectors, check collision conditions for all three dimensions.
 * 
 * 3. What if we need to track the collision events and their order?
 *    Answer: Return list of collision events with timestamps and participating asteroids.
 *    Related: https://leetcode.com/problems/car-fleet/
 * 
 * @author Sandeep
 */
public class AsteroidCollision {
    
    /**
     * Simulates asteroid collisions using stack-based approach.
     * 
     * Algorithm:
     * 1. Use stack to track asteroids moving right (positive values)
     * 2. For each asteroid moving left (negative), check collisions with right-moving ones
     * 3. Handle three collision outcomes: left wins, right wins, or both destroyed
     * 4. Continue until no more collisions possible
     * 
     * Time Complexity: O(n) where n is number of asteroids
     * Space Complexity: O(n) for the stack in worst case
     * 
     * @param asteroids Array representing asteroids with size and direction
     * @return Array representing final state after all collisions
     */
    public int[] asteroidCollision(int[] asteroids) {
        if (asteroids == null || asteroids.length == 0) {
            return new int[0];
        }
        
        Stack<Integer> stack = new Stack<>();
        
        for (int asteroid : asteroids) {
            if (asteroid > 0) {
                // Right-moving asteroid, no collision possible yet
                stack.push(asteroid);
            } else {
                // Left-moving asteroid, check for collisions
                boolean destroyed = false;
                
                while (!stack.isEmpty() && stack.peek() > 0 && !destroyed) {
                    int rightMoving = stack.peek();
                    
                    if (rightMoving < -asteroid) {
                        // Left-moving asteroid is larger, destroys right-moving one
                        stack.pop();
                    } else if (rightMoving == -asteroid) {
                        // Same size, both destroyed
                        stack.pop();
                        destroyed = true;
                    } else {
                        // Right-moving asteroid is larger, left-moving one destroyed
                        destroyed = true;
                    }
                }
                
                // If left-moving asteroid survived, add it to result
                if (!destroyed) {
                    stack.push(asteroid);
                }
            }
        }
        
        // Convert stack to result array
        int[] result = new int[stack.size()];
        for (int i = result.length - 1; i >= 0; i--) {
            result[i] = stack.pop();
        }
        
        return result;
    }
    
    /**
     * Alternative implementation using list for clearer logic flow.
     * 
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public int[] asteroidCollisionList(int[] asteroids) {
        List<Integer> result = new ArrayList<>();
        
        for (int asteroid : asteroids) {
            boolean addAsteroid = true;
            
            while (addAsteroid && asteroid < 0 && !result.isEmpty() && 
                   result.get(result.size() - 1) > 0) {
                
                int last = result.get(result.size() - 1);
                
                if (last < -asteroid) {
                    // Current left-moving asteroid destroys the last right-moving one
                    result.remove(result.size() - 1);
                } else if (last == -asteroid) {
                    // Both asteroids destroy each other
                    result.remove(result.size() - 1);
                    addAsteroid = false;
                } else {
                    // Last right-moving asteroid destroys current left-moving one
                    addAsteroid = false;
                }
            }
            
            if (addAsteroid) {
                result.add(asteroid);
            }
        }
        
        return result.stream().mapToInt(i -> i).toArray();
    }
    
    /**
     * Simulation approach that processes all asteroids iteratively.
     * More intuitive but potentially less efficient.
     */
    public int[] asteroidCollisionSimulation(int[] asteroids) {
        List<Integer> current = new ArrayList<>();
        for (int asteroid : asteroids) {
            current.add(asteroid);
        }
        
        boolean hasCollision = true;
        
        while (hasCollision) {
            hasCollision = false;
            List<Integer> next = new ArrayList<>();
            
            for (int i = 0; i < current.size(); i++) {
                int asteroid = current.get(i);
                
                if (asteroid > 0) {
                    // Check if this right-moving asteroid collides with next left-moving one
                    if (i + 1 < current.size() && current.get(i + 1) < 0) {
                        int nextAsteroid = current.get(i + 1);
                        hasCollision = true;
                        
                        if (asteroid > -nextAsteroid) {
                            // Right-moving wins
                            next.add(asteroid);
                        } else if (asteroid == -nextAsteroid) {
                            // Both destroyed, skip both
                        } else {
                            // Left-moving wins
                            next.add(nextAsteroid);
                        }
                        
                        i++; // Skip the next asteroid as it's already processed
                    } else {
                        next.add(asteroid);
                    }
                } else {
                    next.add(asteroid);
                }
            }
            
            current = next;
        }
        
        return current.stream().mapToInt(i -> i).toArray();
    }
    
    /**
     * Optimized stack implementation with early termination optimizations.
     */
    public int[] asteroidCollisionOptimized(int[] asteroids) {
        Stack<Integer> stack = new Stack<>();
        
        for (int asteroid : asteroids) {
            boolean survived = true;
            
            // Only left-moving asteroids can cause collisions with right-moving ones in stack
            while (survived && asteroid < 0 && !stack.isEmpty() && stack.peek() > 0) {
                int top = stack.peek();
                
                if (top <= -asteroid) {
                    stack.pop(); // Right-moving asteroid destroyed
                    if (top == -asteroid) {
                        survived = false; // Both destroyed
                    }
                } else {
                    survived = false; // Left-moving asteroid destroyed
                }
            }
            
            if (survived) {
                stack.push(asteroid);
            }
        }
        
        // Convert to array
        int[] result = new int[stack.size()];
        for (int i = stack.size() - 1; i >= 0; i--) {
            result[i] = stack.get(i);
        }
        
        return result;
    }
}