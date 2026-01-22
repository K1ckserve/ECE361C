
class Frequency {
    public static int parallelFreq(int x, int[] A, int numThreads) {
        int[] counts = new int[numThreads];
        int chunkSize = A.length / numThreads;
        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
                final int threadId = i;
                final int start = i * chunkSize;
                final int end = (i == numThreads - 1) ? A.length : start + chunkSize;
                
                threads[i] = new Thread(() -> {
                        for (int j = start; j < end; j++) {
                                if (A[j] == x) counts[threadId]++;
                        }
                });
                threads[i].start();
        }

        for (Thread t : threads) {
                try {
                        t.join();
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
        }

        int total = 0;
        for (int count : counts) {
                total += count;
        }
        return total;
        // your implementation goes here
    }

    public static void main(String[] args) {
        Frequency freq = new Frequency();
        System.out.println("JVM is installed and working!");
    }
}
