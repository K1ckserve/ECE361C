class FrequencyTest {
        public static void main(String[] args) throws Exception {
                testBasicFrequency();
                testNoMatches();
                testAllMatches();
                testSingleElement();
                testLargeArray();
                testNegativeNumbers();
                testMultipleThreads();
        }

        static void testBasicFrequency() throws Exception {
                int[] arr = {1, 2, 3, 2, 4, 2, 5};
                int result = Frequency.parallelFreq(2, arr, 2);
                assert result == 3 : "Expected 3, got " + result;
                System.out.println("✓ testBasicFrequency passed");
        }

        static void testNoMatches() throws Exception {
                int[] arr = {1, 2, 3, 4, 5};
                int result = Frequency.parallelFreq(10, arr, 2);
                assert result == 0 : "Expected 0, got " + result;
                System.out.println("✓ testNoMatches passed");
        }

        static void testAllMatches() throws Exception {
                int[] arr = {5, 5, 5, 5};
                int result = Frequency.parallelFreq(5, arr, 2);
                assert result == 4 : "Expected 4, got " + result;
                System.out.println("✓ testAllMatches passed");
        }

        static void testSingleElement() throws Exception {
                int[] arr = {7};
                int result = Frequency.parallelFreq(7, arr, 1);
                assert result == 1 : "Expected 1, got " + result;
                System.out.println("✓ testSingleElement passed");
        }

        static void testLargeArray() throws Exception {
                int[] arr = new int[10000];
                for (int i = 0; i < arr.length; i++) arr[i] = i % 100;
                int result = Frequency.parallelFreq(42, arr, 4);
                assert result == 100 : "Expected 100, got " + result;
                System.out.println("✓ testLargeArray passed");
        }

        static void testNegativeNumbers() throws Exception {
                int[] arr = {-1, -2, -1, 3, -1};
                int result = Frequency.parallelFreq(-1, arr, 2);
                assert result == 3 : "Expected 3, got " + result;
                System.out.println("✓ testNegativeNumbers passed");
        }

        static void testMultipleThreads() throws Exception {
                int[] arr = {1, 1, 2, 2, 2, 3, 3, 3, 3};
                int result = Frequency.parallelFreq(3, arr, 3);
                assert result == 4 : "Expected 4, got " + result;
                System.out.println("✓ testMultipleThreads passed");
        }
}