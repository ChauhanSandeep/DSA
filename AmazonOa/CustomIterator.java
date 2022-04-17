package AmazonOa;// Array List Iterator

// Given an array of arrays, implement an iterator class to allow the client to traverse and remove elements in the array list in place.  This _iterator_ should provide three public class member functions

// boolean hasNext()
//     return true if there is another element in the whole structure

// int next()
//     return the value of the next element in the structure

// void remove()
//     remove (from the underlying collection) the last element returned by the iterator.
//     That is, remove the element that the previous next() returned
//     This method can be called only once per call to next(), 
//     otherwise an exception will be thrown.

//     The behavior of an iterator is unspecified if the underlying
//     collection is modified while the iteration
//     is in progress in any way other than by calling this method.


// * *Print elements*

// Given:  [[],[1,2,3],[4,5],[],[],[6],[7,8],[],[9],[10],[]]
// Print:  1 2 3 4 5 6 7 8 9 10

// import static java.util.Arrays.asList;

//   public static ArrayList<ArrayList<Integer>> getInput() {
//     ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
//     list.add(new ArrayList<Integer>());
//     list.add(new ArrayList<Integer>(Arrays.asList(1,2,3)));
//     list.add(new ArrayList<Integer>(Arrays.asList(4,5)));
//     list.add(new ArrayList<Integer>());
//     list.add(new ArrayList<Integer>());
//     list.add(new ArrayList<Integer>(Arrays.asList(6)));
//     list.add(new ArrayList<Integer>(Arrays.asList(7,8)));
//     list.add(new ArrayList<Integer>());
//     list.add(new ArrayList<Integer>(Arrays.asList(9)));
//     list.add(new ArrayList<Integer>(Arrays.asList(10)));
//     list.add(new ArrayList<Integer>());
//     return list;
//   }

//   private static void test1(){
//     Solution it = new Solution(getInput());
//     while (it.hasNext())
//       System.out.println(it.next());
//     // 1 2 3 4 5 6 7 8 9 10
//   }

// *  *Remove even elements*

// Given:  [[],[1,2,3],[4,5],[],[],[6],[7,8],[],[9],[10],[]]
// Should result in:  [[],[1,3],[5],[],[],[],[7],[],[9],[],[]]
// Print:  1 3 5 7 9

//   private static void test3(){
//     ArrayList<ArrayList<Integer>> input = getInput();
//     Solution it = new Solution(input);
//     while (it.hasNext()){
//       if (it.next() % 2 == 0)
//         it.remove();
//     }
//     it = new Iterator(input);
//     while (it.hasNext())
//       System.out.println(it.next());
//     // 1 3 5 7 9
//   }var input = [[],[1,2,3],[4,5],[],[],[6],[7,8],[],[9],[10],[]];


import java.util.*;

public class CustomIterator {
    int lastRow;
    int lastCol;
    int row;
    int col;
    List<List<Integer>> lists;
    
    public static void main(String args[]){
        CustomIterator it = new CustomIterator(getInput());
        while (it.hasNext())
            System.out.println(it.next());
    }
    
    public static List<List<Integer>> getInput() {
        List<List<Integer>> list = new ArrayList<>();
        list.add(new ArrayList<Integer>());
        list.add(new ArrayList<Integer>(Arrays.asList(1,2,3)));
        list.add(new ArrayList<Integer>(Arrays.asList(4,5)));
        list.add(new ArrayList<Integer>());
        list.add(new ArrayList<Integer>());
        list.add(new ArrayList<Integer>(Arrays.asList(6)));
        list.add(new ArrayList<Integer>(Arrays.asList(7,8)));
        list.add(new ArrayList<Integer>());
        list.add(new ArrayList<Integer>(Arrays.asList(9)));
        list.add(new ArrayList<Integer>(Arrays.asList(10)));
        list.add(new ArrayList<Integer>());
        return list;
    }
    
    public CustomIterator(List<List<Integer>> input) {
        /* [[],[1,2,3],[4,5],[],[],[6],[7,8],[],[9],[10],[]]
        
        */
        this.lists = input;
        boolean found = false;
        for(int i=0; i<lists.size(); i++) {
            for(int j=0; j<lists.get(i).size(); i++) {
                if(lists.get(i).get(j) != null) {
                    row = i;
                    col = j;
                    found = true;
                    break;
                }
            }
            if(found) break;
        } 
        
        for(int i=0; i<lists.size(); i++) {
            for(int j=0; j<lists.get(i).size(); i++) {
                if(lists.get(i).get(j) != null) {
                    lastRow = i;
                    lastCol = j;
                }
            }
        }
        
        System.out.println(row + " " + col + " " + lastRow + " " + lastCol);
    }
    
    boolean hasNext() {
        boolean result = false;
        if(row <= lastRow) result = true;
        else if(row == lastRow && col <= lastCol) result = true;
        else result = false;
        System.out.println("hasNext " + result);
        return result;
    }

    int next() {
        int val = lists.get(row).get(col);
        
        if(col == lists.get(row).size() - 1) {
            row++;
            col = 0;
        }else{
            col++;
        }
        return val;
    }
}



