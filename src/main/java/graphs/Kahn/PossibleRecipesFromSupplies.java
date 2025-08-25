package graph.Kahn;

import java.util.*;

/**
 * Leetcode 2115 - Find All Possible Recipes from Given Supplies
 * https://leetcode.com/problems/find-all-possible-recipes-from-given-supplies
 *
 * Problem Statement:
 * Given a list of recipes, the ingredients needed for each, and an initial list of supplies,
 * return all the recipes you can make. A recipe is creatable if all its ingredients
 * are either in the supplies or are other recipes that have already been created.
 *
 * Example:
 * Input:
 *   recipes = ["bread", "sandwich"]
 *   ingredients = [["yeast", "flour"], ["bread", "meat"]]
 *   supplies = ["yeast", "flour", "meat"]
 * Output:
 *   ["bread", "sandwich"]
 *
 * Explanation:
 * - You can make "bread" using yeast and flour.
 * - Once "bread" is available, you can make "sandwich".
 *
 * Follow-up:
 * - What if circular dependencies are allowed? (Answer: Detect using visited set)
 * - Can we return the steps in which recipes are built? (Topological ordering)
 */
public class PossibleRecipesFromSupplies {

    /**
     * Returns a list of all recipes that can be created using available supplies.
     *
     * Approach:
     * - Build a dependency graph from ingredient → recipes needing it.
     * - Track indegree (missing ingredient count) per recipe.
     * - Use a BFS (topological sort) to create recipes whose all ingredients are available.
     *
     * Time Complexity: O(N + M), where N = number of recipes, M = total number of ingredients
     * Space Complexity: O(N + M)
     *
     * @param recipes     Array of recipe names
     * @param ingredients List of ingredients for each recipe
     * @param supplies    Array of initially available supplies
     * @return List of creatable recipes
     */
    public List<String> findAllRecipes(String[] recipes, List<List<String>> ingredients, String[] supplies) {
        // Convert supplies into a set for fast lookup
        Set<String> availableItems = new HashSet<>(Arrays.asList(supplies));

        // Map: recipeName → index in recipes[]
        Map<String, Integer> recipeIndexMap = new HashMap<>();
        for (int i = 0; i < recipes.length; i++) {
            recipeIndexMap.put(recipes[i], i);
        }

        // Map: ingredient → list of recipes that depend on it
        Map<String, List<String>> ingredientToRecipes = new HashMap<>();

        // Array to track the number of unavailable ingredients for each recipe
        int[] missingIngredientCount = new int[recipes.length];

        // Step 1: Build the dependency graph and count missing ingredients
        for (int i = 0; i < recipes.length; i++) {
            for (String ingredient : ingredients.get(i)) {
                if (!availableItems.contains(ingredient)) {
                    // Mark dependency: ingredient → recipe
                    ingredientToRecipes
                        .computeIfAbsent(ingredient, key -> new ArrayList<>())
                        .add(recipes[i]);

                    // Increase the missing count
                    missingIngredientCount[i]++;
                }
            }
        }

        // Step 2: Start with recipes whose all ingredients are already available
        Queue<Integer> readyQueue = new LinkedList<>();
        for (int i = 0; i < recipes.length; i++) {
            if (missingIngredientCount[i] == 0) {
                readyQueue.offer(i);
            }
        }

        // Step 3: BFS to simulate recipe creation (topological order)
        List<String> creatableRecipes = new ArrayList<>();
        while (!readyQueue.isEmpty()) {
            int currentIdx = readyQueue.poll();
            String currentRecipe = recipes[currentIdx];
            creatableRecipes.add(currentRecipe);
            availableItems.add(currentRecipe); // Mark as now available

            // No recipe depends on this output
            if (!ingredientToRecipes.containsKey(currentRecipe)) {
                continue;
            }

            // For each recipe that depends on the current recipe
            for (String dependentRecipe : ingredientToRecipes.get(currentRecipe)) {
                int depIdx = recipeIndexMap.get(dependentRecipe);
                if (--missingIngredientCount[depIdx] == 0) {
                    readyQueue.offer(depIdx);
                }
            }
        }

        return creatableRecipes;
    }
}