## Common Mistakes & Fixes

### Avoid using a[0] - b[0] in comparators
- Using subtraction in a comparator like (a, b) -> a[0] - b[0] can cause integer overflow, especially with large inputs.
- Use safe built-in comparison instead:
```
Arrays.sort(arr, (a, b) -> Integer.compare(a[0], b[0]));
Arrays.sort(arr, (a, b) -> Long.compare(a[0], b[0]));  // for long[]
```
### Safely convert List to Array
•	For primitive arrays:

```
List<int[]> list = new ArrayList<>();
int[][] arr = list.toArray(new int[0][]); // 0 is used for dynamic sizing
```
•	For objects like String or Integer, use:

```
List<String> list = new ArrayList<>();
String[] arr = list.toArray(new String[0]);
```
**Important Note** : Avoid using list.toArray() without arguments. It returns Object[], which can cause ClassCastException.

### Use Arrays.asList() to wrap an array as a List
```
int[][] arrTwoDimension = {{1, 2}, {3, 4}};
List<int[]> listOfArray = Arrays.asList(arrTwoDimension);

int[] arrSingleDimension = {1, 2, 3};
List<int[]> listOfSingleArray = Arrays.asList(arrSingleDimension);
```
**Important Note:** The returned list is fixed-size and backed by the array. You can’t add/remove elements — only replace.

- To create a mutable list, wrap the array in a new ArrayList:
```
List<int[]> mutableList = new ArrayList<>(Arrays.asList(arr));

List<String> mutableStringList = new ArrayList<>(Arrays.asList("a", "b", "c"));
```

### Monotonically increasing stack is used to find the next and previous smaller elements

## DuplicateZeroes
Missed the intuition of going from the end of the array toward starting. THe micropattern is that whenever we need to 
shift the elements to the right, we should start from the end of the array to avoid overwriting elements that are yet to be processed.

## MatrixBlockSum
Whenever we have to calculate the sum of elements in a sub-matrix, we can use prefix sum matrix to optimize the calculation.

## MinimumDominoRotationsForEqualRow
I was thinking that I would need four variables. So basically I have two elements. Both are the first element of each array. 
And if I take that how many rotations would be required to make any one of them as the result, then I was having four variables.
And in each variable I would keep how many rotations are required to keep the first element of the first array top, first element of the first array bottom, 
first element of the second array top, first element of the second array bottom. So this is how I was thinking. 
But it seems like there is a much better approach where I can iterate over the array two times. And that way I'll just have the two variables instead of four, 
which was seemingly more complex.

