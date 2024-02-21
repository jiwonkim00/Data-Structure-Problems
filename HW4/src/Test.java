import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Test {

    public static void main(String args[]) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int size = 100000;	// 총 갯수
        int[] dataset = new int[size];	// 입력 받을 숫자들의 배열

        Random random = new Random();

//        for (int i = 0; i < size; i++) {
//            int value = random.nextInt(100000000);
//            dataset[i] = value;
//        }
        //Test_2 : Generate random array with a certain collision ratio

//        double collisionRate = 0.99;
//
//        //Create dataset with a particular collision rate
//        int numCollisions = (int) (size * collisionRate);
//        Set<Integer> uniqueValues = new HashSet<>();
//
//        for (int i = 0; i < numCollisions; i++) {
//            int value = random.nextInt(size/100);
//            uniqueValues.add(value);
//        }
//
//        int uniqueCount = uniqueValues.size();
//
//        for (int i = numCollisions; i < size; i++) {
//            int value;
//            do {
//                value = random.nextInt(size);
//            } while (!uniqueValues.add(value));
//
//            dataset[i] = value;
//        }
//
//        for (int i = 0; i < numCollisions; i++) {
//            int value = random.nextInt(uniqueCount);
//            dataset[i] = uniqueValues.toArray(new Integer[0])[value];
//        }
//
//        //actual collision rate
//        Set<Integer> actual_uniqueValues = new HashSet<>();
//        int collisions = 0;
//        for (int value : dataset) {
//            if (!actual_uniqueValues.add(value)) {
//                collisions++;
//            }
//        }
//        double actualCollisionRate = (double) collisions / dataset.length;
//        System.out.println("Actual collision rate: " + actualCollisionRate);

////////////////////////////////////////////////////////////////////////////////////////////////////

        //Test_2 : Created a duplicate array with a certain duplicate ratio
//        double duplicateRatio = 0.999;
//        dataset = generateDuplicatedArray(size, duplicateRatio);

        //Test_3 : Create a partially sorted array with a certain sorted ratio
        double sortedRatio = 0.999;
        dataset = generateSortedArray(size, sortedRatio);

        double[] ratio = getDup_SortedRatio(dataset);
        System.out.println("Actual duplicate rate: " + ratio[0]);
        System.out.println("Acutal sorted rate: " + ratio[1]);




        while (true) {
            int[] newvalue = dataset.clone();    // 원래 값의 보호를 위해 복사본을 생성한다.
            char algo = ' ';
            String command = br.readLine();

            long t = System.currentTimeMillis();
            switch (command.charAt(0)) {
                case 'B':    // Bubble Sort
                    newvalue = DoBubbleSort(newvalue);
                    break;
                case 'I':    // Insertion Sort
                    newvalue = DoInsertionSort(newvalue);
                    break;
                case 'H':    // Heap Sort
                    newvalue = DoHeapSort(newvalue);
                    break;
                case 'M':    // Merge Sort
                    newvalue = DoMergeSort(newvalue);
                    break;
                case 'Q':    // Quick Sort
                    newvalue = DoQuickSort(newvalue);
                    break;
                case 'R':    // Radix Sort
                    newvalue = DoRadixSort(newvalue);
                    break;
                case 'S':    // Search
                    algo = DoSearch(newvalue);
                    System.out.println(algo);
                    break;
                case 'X':
                    return;    // 프로그램을 종료한다.
                default:
                    throw new IOException("잘못된 정렬 방법을 입력했습니다.");
            }
            System.out.println((System.currentTimeMillis() - t) + " ms");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoBubbleSort(int[] value)
    {
        // TODO : Bubble Sort 를 구현하라.
        // value는 정렬안된 숫자들의 배열이며 value.length 는 배열의 크기가 된다.
        // 결과로 정렬된 배열은 리턴해 주어야 하며, 두가지 방법이 있으므로 잘 생각해서 사용할것.
        // 주어진 value 배열에서 안의 값만을 바꾸고 value를 다시 리턴하거나
        // 같은 크기의 새로운 배열을 만들어 그 배열을 리턴할 수도 있다.

        int n = value.length;
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (value[j] > value[j + 1]) {
                    // Swap array[j] and array[j+1]
                    swap(value, j, j+1);
                    swapped = true;
                }
            }
            // If no two elements were swapped in the inner loop, the array is already sorted
            if (!swapped) {
                break;
            }
        }
        return value;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoInsertionSort(int[] value)
    {
        // TODO : Insertion Sort 를 구현하라.
        int n = value.length;

        for (int i = 1; i < n; i++) {
            int key = value[i];
            int j = i - 1;
            while (j >= 0 && value[j] > key) {
                value[j + 1] = value[j];
                j --;
            }
            value[j + 1] = key;
        }
        return value;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoHeapSort(int[] value)
    {
        // TODO : Heap Sort 를 구현하라.
        int n = value.length;

        // Build heap (rearrange array)
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(value, n, i);
        }

        // Extract elements from the heap in descending order
        for (int i = n - 1; i > 0; i--) {
            // Move the current root (maximum value) to the end
            swap(value, 0, i);
            // Call heapify on the reduced heap
            heapify(value, i, 0);
        }
        return (value);
    }
    public static void heapify(int[] array, int n, int i) {
        int largest = i; // Initialize largest as root
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        // If left child is larger than root
        if (left < n && array[left] > array[largest]) {
            largest = left;
        }

        // If right child is larger than the largest so far
        if (right < n && array[right] > array[largest]) {
            largest = right;
        }

        // If largest is not the root
        if (largest != i) {
            // Swap array[i] and array[largest]
            swap(array, i, largest);
            // Recursively heapify the affected subtree
            heapify(array, n, largest);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoMergeSort(int[] value)
    {
        // TODO : Merge Sort 를 구현하라.
        if (value == null || value.length <= 1) {
            return value; // Base case: already sorted or empty
        }

        int n = value.length;
        int mid = n / 2;

        // Create temporary arrays for left and right halves
        int[] leftArray = new int[mid];
        int[] rightArray = new int[n - mid];

        // Copy elements into temporary arrays
        System.arraycopy(value, 0, leftArray, 0, mid);
        System.arraycopy(value, mid, rightArray, 0, n - mid);

        // Recursively sort the left and right halves
        leftArray = DoMergeSort(leftArray);
        rightArray = DoMergeSort(rightArray);

        // Merge the sorted halves
        return merge(leftArray, rightArray);
    }
    public static int[] merge(int[] leftArray, int[] rightArray) {
        int leftLength = leftArray.length;
        int rightLength = rightArray.length;
        int[] mergedArray = new int[leftLength + rightLength];
        int i = 0, j = 0, k = 0;

        // Compare elements from left and right arrays and merge them in ascending order
        while (i < leftLength && j < rightLength) {
            if (leftArray[i] <= rightArray[j]) {
                mergedArray[k++] = leftArray[i++];
            } else {
                mergedArray[k++] = rightArray[j++];
            }
        }

        // Copy remaining elements from leftArray, if any
        while (i < leftLength) {
            mergedArray[k++] = leftArray[i++];
        }

        // Copy remaining elements from rightArray, if any
        while (j < rightLength) {
            mergedArray[k++] = rightArray[j++];
        }

        return mergedArray;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoQuickSort(int[] value)
    {
        // TODO : Quick Sort 를 구현하라.
        if (value == null || value.length <= 1) {
            return value; // Base case: already sorted or empty
        }

        int low = 0;
        int high = value.length - 1;

        // Call the recursive quickSort method
        quickSortRecursive(value, low, high);

        return value;
    }
    private static void quickSortRecursive(int[] value, int low, int high) {
        if (low < high) {
            // Partition the array into two halves
            int pivotIndex = partition(value, low, high);

            // Recursively sort the two halves
            quickSortRecursive(value, low, pivotIndex - 1);
            quickSortRecursive(value, pivotIndex + 1, high);
        }
    }
    private static int partition(int[] value, int low, int high) {
        int pivot = value[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (value[j] < pivot) {
                i++;
                swap(value, i, j);
            }
        }
        // Place the pivot element in its correct position
        swap(value, i + 1, high);
        return i + 1;
    }
    private static void swap(int[] value, int i, int j) {
        int temp = value[i];
        value[i] = value[j];
        value[j] = temp;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoRadixSort(int[] value)
    {
        // TODO : Radix Sort 를 구현하라.
        if (value == null || value.length <= 1) {
            return value; // Base case: already sorted or empty
        }

        // Separate negative and positive numbers
        int[] negativeNums = Arrays.stream(value)
                .filter(num -> num < 0)
                .toArray();

        int[] positiveNums = Arrays.stream(value)
                .filter(num -> num >= 0)
                .toArray();

        // Sort negative numbers in decreasing order
        if (negativeNums.length > 0) {
            radixSortHelper(negativeNums);
            reverseArray(negativeNums);
        }

        // Sort positive numbers in ascending order
        if (positiveNums.length > 0) {
            radixSortHelper(positiveNums);
        }

        // Combine negative and positive numbers
        int[] sortedArray = new int[value.length];
        int negIndex = 0;
        int posIndex = 0;

        for (int i = 0; i < value.length; i++) {
            if (negIndex < negativeNums.length) {
                sortedArray[i] = negativeNums[negIndex];
                negIndex++;
            } else {
                sortedArray[i] = positiveNums[posIndex];
                posIndex++;
            }
        }

        return sortedArray;
    }
    private static void radixSortHelper(int[] value) {
        int max = getMax(value);
        int exp = 1;

        // Perform counting sort for every digit
        while (max / exp > 0) {
            countingSort(value, exp);
            exp *= 10;
        }
    }

    private static int getMax(int[] value) {
        int max = Math.abs(value[0]);
        for (int i = 1; i < value.length; i++) {
            int absValue = Math.abs(value[i]);
            if (absValue > max) {
                max = absValue;
            }
        }
        return max;
    }

    private static void countingSort(int[] value, int exp) {
        int n = value.length;
        int[] output = new int[n];
        int[] count = new int[10];
        Arrays.fill(count, 0);

        // Count occurrences of each digit
        for (int j : value) {
            int digit = (Math.abs(j) / exp) % 10;
            count[digit]++;
        }

        // Calculate cumulative count
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        // Build the output array
        for (int i = n - 1; i >= 0; i--) {
            int digit = (Math.abs(value[i]) / exp) % 10;
            output[count[digit] - 1] = value[i];
            count[digit]--;
        }

        // Copy the output array to the original array
        System.arraycopy(output, 0, value, 0, n);
    }

    private static void reverseArray(int[] array) {
        int start = 0;
        int end = array.length - 1;

        while (start < end) {
            int temp = array[start];
            array[start] = array[end];
            array[end] = temp;
            start++;
            end--;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static char DoSearch(int[] value)
    {
        // TODO : Search 를 구현하라.
        int input_length = value.length;

        double[] ratio = getRatios(value);

        int max = (int)ratio[0];
        double duplicateRatio = ratio[1];
        double sortedRatio = ratio[2];

        //type 1
        int max_length = (int)(Math.log10(max)+1);
        System.out.println("Max length : " + max_length);
        if (input_length >= 50000 && max_length < 10 ) return 'R';
        //type 2
        if (duplicateRatio >= 0.998) return 'M';
        //type 3
        if (sortedRatio >= 0.993) return 'I';

        return ('Q'); // 여러 조건을 설정하고, 각 조건에 따라 B, I, H, M, Q, R 중 하나를 리턴하라.
    }
    public static double[] getRatios(int[] array) {
        int max = Math.abs(array[0]);

        int duplicateCount = 0;
        Set<Integer> uniqueValues = new HashSet<>();

        int numPairs = 0;
        int numInversions = 0;

        for (int i = 0; i < array.length - 1; i++) {
            int absValue = Math.abs(array[i]);
            if (absValue > max) {
                max = absValue;
            }

            if (array[i] <= array[i + 1]) {
                numPairs++;
            } else {
                numInversions++;
                numPairs++;
            }
            if (uniqueValues.contains(array[i])) {
                duplicateCount++;
            } else {
                uniqueValues.add(array[i]);
            }
        }

        //last element
        if (uniqueValues.contains(array[array.length-1])) {
            duplicateCount++;
        }
        else {
            uniqueValues.add(array[array.length-1]);
        }
        if (Math.abs(array[array.length-1]) > max) {
            max = Math.abs(array[array.length-1]);
        }

        return new double[]{(double) max, (double) duplicateCount/array.length , 1.0 - ((double) numInversions / numPairs)};

    }
    public static double getSortedRatio(int[] input) {
        int numPairs = 0;
        int numInversions = 0;

        for (int i = 0; i < input.length - 1; i++) {
            if (input[i] <= input[i + 1]) {
                numPairs++;
            } else {
                numInversions++;
                numPairs++;
            }
        }

        double sortedRatio = 1.0 - ((double) numInversions / numPairs);

        return sortedRatio;
    }
    public static int[] generateSortedArray(int size, double sortedRatio) {
        if (sortedRatio < 0 || sortedRatio > 1) {
            throw new IllegalArgumentException("Sorted ratio should be between 0 and 1");
        }

        int[] array = new int[size];
        Random random = new Random();

        int numPairs = (int) (size * sortedRatio);
        int numInversions = size - numPairs;

        // Generate a sorted section
        for (int i = 0; i < numPairs; i++) {
            array[i] = i+1000000000;
        }

        // Generate remaining elements with random values
        for (int i = numPairs; i < size; i++) {
            array[i] = random.nextInt(size);
        }

        // Shuffle the array to introduce inversions
        for (int i = 0; i < numInversions; i++) {
            int index1 = random.nextInt(size);
            int index2 = random.nextInt(size);
            swap(array, index1, index2);
        }

        return array;
    }
    public static double getDuplicateRatio(int[] array) {
        Set<Integer> uniqueValues = new HashSet<>();
        int duplicateCount = 0;

        for (int num : array) {
            if (uniqueValues.contains(num)) {
                duplicateCount++;
            } else {
                uniqueValues.add(num);
            }
        }

        return (double) duplicateCount / array.length;
    }
    public static int[] generateDuplicatedArray(int size, double duplicateRatio) {
        if (duplicateRatio < 0 || duplicateRatio > 1) {
            throw new IllegalArgumentException("Duplicate ratio should be between 0 and 1");
        }

        int[] array = new int[size];
        Random random = new Random();

        int numDuplicates = (int) (size * duplicateRatio);
        int numUnique = size - numDuplicates;

        // Generate unique values
        for (int i = 0; i < numUnique; i++) {
            array[i] = (i + 1) ;
        }

        // Generate duplicate values
        for (int i = numUnique; i < size; i++) {
            array[i] = random.nextInt(numUnique) + 1;
        }

        // Shuffle the array
        for (int i = size - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            swap(array, i, j);
        }

        return array;
    }
    public static double[] getDup_SortedRatio(int[] array) {
        int duplicateCount = 0;
        Set<Integer> uniqueValues = new HashSet<>();

        int numPairs = 0;
        int numInversions = 0;

        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] <= array[i + 1]) {
                numPairs++;
            } else {
                numInversions++;
                numPairs++;
            }
            if (uniqueValues.contains(array[i])) {
                duplicateCount++;
            } else {
                uniqueValues.add(array[i]);
            }
        }
        double[] dup_SortedRatio = {(double) duplicateCount/array.length , 1.0 - ((double) numInversions / numPairs)};
        return dup_SortedRatio;



    }
}
