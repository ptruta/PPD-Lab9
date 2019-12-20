import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    //the list numbers for which we need to calculate the prefix sum
    private static List<Integer> prefixSum = new ArrayList<>(Arrays.asList(1, 5, 2, 4, 7, 9, 8, 6));
    //the output list of numbers
    private static List<Integer> output = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0));

    public static void main(String[] args) {
        Node root = new Node(0, 8);
        int nrOfThreads = 3;
        up(root, nrOfThreads);
        //print the prefix sum of the root
        printInOrder(root);
        down(root, nrOfThreads);
        System.out.println();
        System.out.println("The sums are:");
        System.out.println(output);
    }

    private static void printInOrder(Node node) {
        if (node != null) {
            //to print the prefix sum of the left node
            printInOrder(node.getLeft());
            System.out.print(node.getSum() + " ");
            //to print the prefix sum of the right node
            printInOrder(node.getRight());
        }
    }

    //root has the sum of the range(0, size of the array of inputs)
    //if a node has sum of [lo,hi) and hi > lo.
    //left child has the sum of [lo,middle)
    //right child has the sum of [middle,hi)
    //a leaf has sum of [i,i+1) i.e input[i].
    //tree build bottom-up in parallel.
    private static void up(Node node, int nrOfThreads) {
        int low = node.getStartIndex();
        int high = node.getEndIndex();
        ExecutorService executorService1 = Executors.newFixedThreadPool(nrOfThreads);
        ExecutorService executorService2 = Executors.newFixedThreadPool(nrOfThreads);
        if (high - low != 1) {
            int mid = (low + high) / 2;
            Node left = new Node(low, mid);
            Node right = new Node(mid, high);

            node.setLeft(left);
            node.setRight(right);
            executorService1.execute(() -> up(left, nrOfThreads));
            executorService2.execute(() -> up(right, nrOfThreads));

            executorService1.shutdown();
            executorService2.shutdown();
            while (!executorService1.isTerminated()) {
            }
            while (!executorService2.isTerminated()) {
            }
            //System.out.println("Left: " + left.getSum());
            //System.out.println("Right: " + right.getSum());
            //System.out.println(left.getSum()+right.getSum());
            //add sum of the left child[lo,middle) and sum of the right child[middle,hi)
            node.setSum(left.getSum() + right.getSum());
        } else {
            //leaf
            //the input exactly.
            node.setSum(prefixSum.get(low));
        }
    }

    //root given as fromLeft of 0.
    //pass down a value fromLeft.
    //node takes it fromLeft value and
    //-passes its left child the same fromLeft.
    //-passes its right child its fromLeft plus left child's sum.
    private static void down(Node node, int nrOfThreads) {
        ExecutorService executorService1 = Executors.newFixedThreadPool(nrOfThreads);
        ExecutorService executorService2 = Executors.newFixedThreadPool(nrOfThreads);
        if (node.getLeft() != null && node.getRight() != null) {
            Node left = node.getLeft();
            Node right = node.getRight();

            left.setFromLeft(node.getFromLeft());
            right.setFromLeft(node.getFromLeft() + left.getSum());

            executorService1.execute(() -> down(left, nrOfThreads));
            executorService2.execute(() -> down(right, nrOfThreads));

            executorService1.shutdown();
            executorService2.shutdown();
            while (!executorService1.isTerminated()) {
            }
            while (!executorService2.isTerminated()) {
            }
        } else {
            //leaf
            //System.out.println(output);
            //traverse the tree in step one and produce no result.
            //getFromLeft is the sum of elements from the left to the node's range.
            output.set(node.getStartIndex(), node.getFromLeft() + prefixSum.get(node.getStartIndex()));
        }
    }
}