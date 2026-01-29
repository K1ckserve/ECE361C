import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;


public class PIncrement {
    private static final int M = 1_200_000;

    public static int parallelIncrement(int c, int numThreads) {
        Bakery lock = new Bakery(numThreads);
        Thread[] threads = new Thread[numThreads];
        final int[] counter = new int[]{c};

        int base = M / numThreads;

        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                int start = threadId * base;
                int end = (threadId == numThreads - 1) ? M : start + base;
                for (int j = start; j < end; j++) {
                    lock.requestCS(threadId);
                    counter[0]++;
                    lock.releaseCS(threadId);
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return counter[0];
    }
    public static int parallelIncrementCAS(int c, int numThreads) {
        AtomicInteger counter = new AtomicInteger(c);
        Thread[] threads = new Thread[numThreads];
        int base = M / numThreads;
        int rem = M % numThreads;
        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            final int increments = (threadId == numThreads - 1) ? base + rem : base;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < increments; j++) {
                    int oldValue, newValue;
                    do {
                        oldValue = counter.get();
                        newValue = oldValue + 1;
                    } while (!counter.compareAndSet(oldValue, newValue));
                }
            });
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return counter.get();
    }
    public static int parallelIncrementSynchronized(int c, int numThreads) {
        final Object lock = new Object();
        Thread[] threads = new Thread[numThreads];
        final int[] counter = new int[]{c};

        int base = M / numThreads;

        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                int start = threadId * base;
                int end = (threadId == numThreads - 1) ? M : start + base;
                for (int j = start; j < end; j++) {
                    synchronized (lock) {
                        counter[0]++;
                    }
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return counter[0];
    }
    public static int parallelIncrementReentrantLock(int c, int numThreads) {
        ReentrantLock lock = new ReentrantLock();
        Thread[] threads = new Thread[numThreads];
        final int[] counter = new int[]{c};

        int base = M / numThreads;

        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                int start = threadId * base;
                int end = (threadId == numThreads - 1) ? M : start + base;
                for (int j = start; j < end; j++) {
                    lock.lock();
                    try {
                        counter[0]++;
                    } finally {
                        lock.unlock();
                    }
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return counter[0];
    }

    public static void main(String[] args) {
        int c0 = 0;
        int[] ns = {1, 2, 4, 8};

        System.out.println("n,bakery_ms,cas_ms,sync_ms,reentrant_ms,bakery_result,cas_result,sync_result,reentrant_result");

        for (int n : ns) {
            long t0 = System.nanoTime();
            int r1 = parallelIncrement(c0, n);
            long t1 = System.nanoTime();
            double bakeryMs = (t1 - t0) / 1_000_000.0;

            long t2 = System.nanoTime();
            int r2 = parallelIncrementCAS(c0, n);
            long t3 = System.nanoTime();
            double casMs = (t3 - t2) / 1_000_000.0;

            long t4 = System.nanoTime();
            int r3 = parallelIncrementSynchronized(c0, n);
            long t5 = System.nanoTime();
            double syncMs = (t5 - t4) / 1_000_000.0;

            long t6 = System.nanoTime();
            int r4 = parallelIncrementReentrantLock(c0, n);
            long t7 = System.nanoTime();
            double reMs = (t7 - t6) / 1_000_000.0;

            System.out.println(n + "," + bakeryMs + "," + casMs + "," + syncMs + "," + reMs + "," +
                            r1 + "," + r2 + "," + r3 + "," + r4);
        }

    }

}
