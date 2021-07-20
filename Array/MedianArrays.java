package Array;

public class MedianArrays {

    public static void main(String[] args) {
//        int[] arr1 = {900};
//        int[] arr2 = {5, 8, 10, 20};
//        int median = getMedian(arr1, arr2, arr1.length, arr2.length);
//        System.out.println(median);
        String str = "abc";
        System.out.println(str.substring(0, 1));
    }

    static int getMedian(int arr1[], int arr2[], int len1, int len2) {

        int i = 0;
        int j = 0;
        int count;
        int m1 = -1, m2 = -1;

        if ((len2 + len1) % 2 == 1) {
            for (count = 0;count <= (len1 + len2) / 2;count++) {
                if (i != len1 && j != len2) {
                    m1 = (arr1[i] > arr2[j]) ?arr2[j++] : arr1[i++];
                } else if (i < len1) {
                    m1 = arr1[i++];
                } else {
                    m1 = arr2[j++];
                }
            }
            return m1;
        } else {
            for (count = 0;count <= (len1 + len2) / 2;count++) {
                m2 = m1;
                if (i != len1 && j != len2) {
                    m1 = (arr1[i] > arr2[j]) ?arr2[j++] : arr1[i++];
                } else if (i < len1) {
                    m1 = arr1[i++];
                } else {
                    m1 = arr2[j++];
                }
            }
            return (m1 + m2) / 2;
        }
    }
}
