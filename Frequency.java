import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class Frequency {
        static class FrequencySearchTask implements Callable<int[]> {
                private int x, start, end;
                private int[] A;
		
		FrequencySearchTask(int x, int[] A, int start, int end) {
			this.x = x;
			this.A = A;
			this.start = start;
			this.end = end;
		}
		
		@Override
		public int[] call() {
			int[] chunk = new int[end - start];
			int count = 0;
			for (int j = start; j < end; j++) {
				if (A[j] == x) {
					chunk[count++] = A[j];
				}
			}
			return java.util.Arrays.copyOf(chunk, count);
		}
	}
        public static int parallelFreq(int x, int[] A, int numThreads) throws Exception {
                ExecutorService executor = Executors.newFixedThreadPool(numThreads);
                List<Future<int[]>> futures = new ArrayList<>();
                int partSize = A.length / numThreads;

                for (int i = 0; i < numThreads; i++) {
                        final int start = i * partSize;
                        final int end;
                        if (i == numThreads - 1) {
                                end = A.length;
                        } else {
                                end = start + partSize;
                        }

                        futures.add(executor.submit(new FrequencySearchTask(x, A, start, end)));
                }
                int total = 0;
                for (int i = 0; i < futures.size(); i++) {
                                Future<int[]> future = futures.get(i);
                                total += future.get().length;
                }
                executor.shutdown();
                return total;
        }

        public static void main(String[] args) throws Exception {
                System.out.println("JVM is installed and working!");
        }
}
