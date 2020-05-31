package Chapter7.Section2;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicInterArrayTest {
    private static int [] value = new int[]{1,2,3};
    private static AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(value);

    public static void main(String[] args) {
        atomicIntegerArray.getAndSet(0,3);
        System.out.println(atomicIntegerArray.get(0));
        System.out.println(value[0]);
    }
}
