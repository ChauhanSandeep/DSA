package graphs.topologicalsort;

import java.util.*;

/**
 * Problem: Find All Possible Recipes From Given Supplies
 *
 * Given recipes, their ingredient lists, and initial supplies, return every
 * recipe that can be created. A completed recipe becomes an available ingredient
 * for later recipes, while cyclic or missing dependencies stay unavailable.
 *
 * Leetcode: https://leetcode.com/problems/find-all-possible-recipes-from-given-supplies/ (Medium)
 * Rating:   1679 (zerotrac Elo)
 * Pattern:  Graph | Topological sort | Dependency in-degree
 *
 * Example:
 *   Input:  recipes = ["bread","sandwich"], ingredients = [["yeast","flour"],["bread","meat"]], supplies = ["yeast","flour","meat"]
 *   Output: ["bread", "sandwich"]
 *   Why:    bread is made from supplies first, then bread becomes an ingredient for sandwich.
 *
 * Follow-ups:
 *   1. Return recipes in creation order?
 *      The BFS topological order already gives a valid earliest-available order.
 *   2. Ingredients have limited quantities?
 *      Track counts and consume quantities when a recipe is committed.
 *   3. Circular recipe dependencies?
 *      Recipes in cycles never reach in-degree zero unless an external supply breaks the cycle.
 *
 * Related: Course Schedule (207), Course Schedule II (210), Parallel Courses (1136).
 */
public class FindAllPossibleRecipes {

    public static void main(String[] args) {
        FindAllPossibleRecipes solver = new FindAllPossibleRecipes();
        String[] recipes1 = {"bread", "sandwich"};
        List<List<String>> ingredients1 = Arrays.asList(
            Arrays.asList("yeast", "flour"), Arrays.asList("bread", "meat"));
        String[] supplies1 = {"yeast", "flour", "meat"};
        String[] recipes2 = {"bread"};
        List<List<String>> ingredients2 = Arrays.asList(Arrays.asList("yeast", "flour"));
        String[] supplies2 = {"yeast"};

        System.out.printf("recipes=%s ingredients=%s supplies=%s -> bfs=%s dfs=%s  expected=[bread, sandwich]%n",
            Arrays.toString(recipes1), ingredients1, Arrays.toString(supplies1),
            solver.findAllRecipes(recipes1, ingredients1, supplies1),
            solver.findAllRecipesDFS(recipes1, ingredients1, supplies1));
        System.out.printf("recipes=%s ingredients=%s supplies=%s -> bfs=%s dfs=%s  expected=[]%n",
            Arrays.toString(recipes2), ingredients2, Arrays.toString(supplies2),
            solver.findAllRecipes(recipes2, ingredients2, supplies2),
            solver.findAllRecipesDFS(recipes2, ingredients2, supplies2));
    }

        /**
     * Intuition: a recipe is ready when all of its ingredients have become
     * available. Treat each ingredient as pointing to the recipes that need it,
     * and use inDegree as the count of still-missing ingredients per recipe.
     *
     * Algorithm:
     *   1. Build ingredient -> recipes dependency lists and recipe in-degree counts.
     *   2. Queue every initial supply.
     *   3. Pop available items; for every recipe needing that item, decrement its in-degree.
     *   4. When a recipe reaches zero, add it to the result and queue it as a new ingredient.
     *
     * Time:  O(R + I) - recipes and ingredient occurrences are processed through the graph once.
     * Space: O(R + I) - dependency graph, in-degree map, queue, and result.
     *
     * @param recipes recipe names
     * @param ingredients ingredients.get(i) lists what recipes[i] needs
     * @param supplies initially available ingredients
     * @return all recipes that can be created
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