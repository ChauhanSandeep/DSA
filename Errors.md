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
