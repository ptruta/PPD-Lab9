import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    private static List<String> list = new ArrayList<>(Arrays.asList("1", "9", "1", "49", "40", "50"));
    private static List<String> reversedList = new ArrayList<>();
    private static Queue<Character> queue = new LinkedList<>();
    private static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        //put the root as the middle of the list.
        Node root = new Node(0, list.size());
        for (String s : list) {
            //we reverse the list in the way we can pop it from the queue in the right order.
            reversedList.add(new StringBuilder(s).reverse().toString());
        }
        //calculate the sum of the n numbers digit by digit.
        up(root, queue);
        char elem;
        StringBuilder res = new StringBuilder();
        while (queue.size() != 0) {
            elem = queue.remove();
            //put the result in the reverse order
            res.append(elem);
        }
        //print it in the right order.
        System.out.println(res.reverse());
    }

    private static void up(Node node, Queue<Character> tempQueue) {
        int low = node.getStartIndex();
        int high = node.getEndIndex();
        ExecutorService executorService1 = Executors.newSingleThreadExecutor();
        ExecutorService executorService2 = Executors.newSingleThreadExecutor();
        if (high - low != 1) {
            int mid = (low + high) / 2;
            Node left = new Node(low, mid);
            Node right = new Node(mid, high);
            node.setLeft(left);
            node.setRight(right);
            Queue<Character> queueLeft = new LinkedList<>();
            Queue<Character> queueRight = new LinkedList<>();
            executorService1.execute(() -> up(left, queueLeft));
            executorService2.execute(() -> up(right, queueRight));

            executorService1.shutdown();
            executorService2.shutdown();
            while (!executorService1.isTerminated()) {
            }
            while (!executorService2.isTerminated()) {
            }

            int res;
            int carry = 0;
            while (true) {
                res = carry;
                carry = 0;
                //we compute the sum from the left side from the tempQueue digit by digit
                if (queueLeft.size() != 0) {
                    //we still have elements in left queue
                    char elemLeft = queueLeft.remove();
                    res += Character.getNumericValue(elemLeft);
                }

                //we compute the sum from the right side from the tempQueue digit by digit
                if (queueRight.size() != 0) {
                    //we still have elements in right queue
                    char elemRight = queueRight.remove();
                    res += Character.getNumericValue(elemRight);
                }

                //if the queues are empty then we check if the result number
                //which we want to put in the tempQueue is greater then 9,
                //if it greater than 9 we need to add the carry 1 to the result
                //in the tempQueue.
                if (queueLeft.size() == 0 && queueRight.size() == 0) {
                    tempQueue.add(String.valueOf(res % 10).charAt(0));
                    if (res > 9) {
                        tempQueue.add('1');
                    }
                    break;
                }
                //same as the above when the queues are still not empty.
                if (res > 9) {
                    res = res % 10;
                    carry = 1;
                }
                tempQueue.add(String.valueOf(res).charAt(0));
                //System.out.println(res);
            }

        } else {
            //leaf
            for (int idx = 0; idx < reversedList.get(low).length(); idx++) {
                lock.lock();
                //we put the elements from the reversedList to the queue to can compute the sum above
                tempQueue.add(reversedList.get(low).charAt(idx));
                lock.unlock();
            }
            //System.out.println("Leaf:"+tempQueue);
        }
    }
}