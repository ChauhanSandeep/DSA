package graphs.topologicalsort;

import java.util.*;

/**
 * Problem Statement:
 * You have information about n different recipes. You are given:
 * - recipes[i]: name of the ith recipe
 * - ingredients[i]: list of ingredients needed for recipes[i]
 * - supplies: list of ingredients you initially have (infinite supply)
 *
 * A recipe can be created if you have all its ingredients. Some ingredients might be other recipes.
 * Return a list of all recipes you can create (in any order).
 *
 * Example:
 * Input: recipes = ["bread","sandwich","burger"], 
 *        ingredients = [["yeast","flour"],["bread","meat"],["sandwich","meat","bread"]], 
 *        supplies = ["yeast","flour","meat"]
 * Output: ["bread","sandwich","burger"]
 * Explanation:
 * - bread: can make with yeast + flour (both in supplies)
 * - sandwich: can make with bread (created) + meat (in supplies)
 * - burger: can make with sandwich (created) + meat + bread (both available)
 *
 * LeetCode link: https://leetcode.com/problems/find-all-possible-recipes-from-given-supplies/
 *
 * Follow-up Questions FAANG Interviews Might Ask:
 *  - What if there are circular dependencies between recipes?
 *    → Topological sort handles this naturally - recipes in cycles will never reach in-degree 0.
 *  - How would you return recipes in order of creation (earliest to latest)?
 *    → The BFS queue processing order already gives this - it's inherent in topological sort.
 *  - What if ingredients have limited quantities (not infinite)?
 *    → Track quantity consumed/available, check feasibility before marking recipe as makeable.
 *  - How would you optimize if supplies list is very large?
 *    → Use HashSet for O(1) lookup instead of List when checking ingredient availability.
 *
 * Relevant Follow-up Problems:
 *  - LeetCode 207 (Course Schedule): https://leetcode.com/problems/course-schedule/
 *  - LeetCode 210 (Course Schedule II): https://leetcode.com/problems/course-schedule-ii/
 *  - LeetCode 1136 (Parallel Courses): https://leetcode.com/problems/parallel-courses/
 */
public class FindAllPossibleRecipes {

    /**
     * Main method: Finds all makeable recipes using BFS-based Topological Sort.
     * Step-by-step:
     *  1. Build dependency graph:
     *     - Each ingredient points to recipes that need it
     *     - Track in-degree for each recipe (number of missing ingredients)
     *  2. Initialize queue with all available supplies (base ingredients)
     *  3. Process queue (BFS):
     *     a. Remove ingredient from queue
     *     b. For each recipe that needs this ingredient:
     *        - Decrease its in-degree (one ingredient now available)
     *        - If in-degree becomes 0: all ingredients available, recipe can be made
     *        - Add recipe to queue (it becomes available as ingredient for other recipes)
     *        - Add recipe to result
     *  4. Return all makeable recipes
     *
     * Key Insight:
     * This is topological sort applied to recipes/ingredients dependency graph.
     * Recipes with in-degree 0 have all prerequisites met and can be created.
     * Once created, they become available ingredients for other recipes.
     *
     * Algorithm: BFS-based Topological Sort (Kahn's Algorithm variant).
     * Time Complexity: O(R + I), where R is total recipes and I is total ingredient occurrences.
     *                  Each recipe and ingredient processed at most once.
     * Space Complexity: O(R + I) for graph, in-degree map, and queue.
     */
    public List<String> findAllRecipes(String[] recipes, List<List<String>> ingredients, String[] supplies) {
        // Build dependency graph: ingredient -> list of recipes needing it
        Map<String, List<String>> graph = new HashMap<>();
        // Track in-degree: recipe -> count of ingredients still needed
        Map<String, Integer> inDegree = new HashMap<>();
        
        // Create graph and in-degrees
        initializeRecipeDependencies(recipes, ingredients, graph, inDegree);
        
        // Initialize queue with all available supplies
        Queue<String> queue = new LinkedList<>();
        for (String supply : supplies) {
            queue.offer(supply);
        }
        
        List<String> result = new ArrayList<>();
        
        // Process available ingredients/recipes (BFS)
        while (!queue.isEmpty()) {
            String currentIngredient = queue.poll();
            
            // Check which recipes can now be made
            if (graph.containsKey(currentIngredient)) {
                for (String recipe : graph.get(currentIngredient)) {
                    // Decrease in-degree (one ingredient now available)
                    inDegree.computeIfPresent(recipe , (k, v) -> v - 1);
                    
                    // If all ingredients available, recipe can be made
                    if (inDegree.get(recipe) == 0) {
                        result.add(recipe);
                        queue.offer(recipe); // Recipe becomes available ingredient
                    }
                }
            }
        }
        
        return result;
    }

    /** Initializes the dependency graph and in-degree map for recipes. */
    private void initializeRecipeDependencies(String[] recipes, List<List<String>> ingredients, Map<String, List<String>> graph,
            Map<String, Integer> inDegree) {
        // Build graph and calculate in-degrees
        for (int i = 0; i < recipes.length; i++) {
            String recipe = recipes[i];
            List<String> recipeIngredients = ingredients.get(i);
            
            // Initialize in-degree
            inDegree.put(recipe, recipeIngredients.size());
            
            // For each ingredient, map it to the recipes that need it
            for (String ingredient : recipeIngredients) {
                graph.computeIfAbsent(ingredient, k -> new ArrayList<>()).add(recipe);
            }
        }
    }

    /**
     * Alternative method: Using DFS with memoization for dependency checking.
     * Step-by-step:
     *  1. Convert supplies to HashSet for O(1) lookup
     *  2. Build map: recipe name -> its ingredients list
     *  3. For each recipe, use DFS to check if it can be made:
     *     a. If ingredient is in supplies: available
     *     b. If ingredient is a recipe: recursively check if that recipe can be made
     *     c. Use memoization to avoid recomputation
     *  4. Use three states: UNVISITED, PROCESSING (for cycle detection), MAKEABLE/UNMAKEABLE
     *
     * Key Insight:
     * DFS approach checks each recipe's makeability by recursively verifying all dependencies.
     * Memoization ensures each recipe is evaluated only once.
     *
     * Algorithm: DFS with memoization and cycle detection.
     * Time Complexity: O(R * I), where R is recipes and I is average ingredients per recipe.
     * Space Complexity: O(R + I) for recipe map, memo cache, and recursion stack.
     */
    public List<String> findAllRecipesDFS(String[] recipes, List<List<String>> ingredients, String[] supplies) {
        // Convert supplies to set for O(1) lookup
        Set<String> availableIngredients = new HashSet<>(Arrays.asList(supplies));
        
        // Build recipe -> ingredients map
        Map<String, List<String>> recipeMap = new HashMap<>();
        for (int i = 0; i < recipes.length; i++) {
            recipeMap.put(recipes[i], ingredients.get(i));
        }
        
        // Memoization: recipe -> can be made (null = processing, for cycle detection)
        Map<String, Boolean> memo = new HashMap<>();
        
        List<String> result = new ArrayList<>();
        
        // Check each recipe
        for (String recipe : recipes) {
            if (canMakeRecipe(recipe, recipeMap, availableIngredients, memo)) {
                result.add(recipe);
            }
        }
        
        return result;
    }

    /**
     * Helper: DFS to check if recipe can be made with available ingredients.
     */
    private boolean canMakeRecipe(String recipe, Map<String, List<String>> recipeMap,
                                  Set<String> availableIngredients, Map<String, Boolean> memo) {
        // Check if already computed
        if (memo.containsKey(recipe)) {
            Boolean canMake = memo.get(recipe);
            // null means currently processing (cycle detected)
            return canMake != null && canMake;
        }
        
        // Not a recipe, check if it's in supplies
        if (!recipeMap.containsKey(recipe)) {
            return availableIngredients.contains(recipe);
        }
        
        // Mark as processing (cycle detection)
        memo.put(recipe, null);
        
        // Check if all ingredients are available
        for (String ingredient : recipeMap.get(recipe)) {
            if (availableIngredients.contains(ingredient)) {
                // Ingredient directly available
                continue;
            }
            
            // Ingredient might be another recipe - check recursively
            if (!canMakeRecipe(ingredient, recipeMap, availableIngredients, memo)) {
                memo.put(recipe, false);
                return false;
            }
        }
        
        // All ingredients available - recipe can be made
        memo.put(recipe, true);
        return true;
    }
}