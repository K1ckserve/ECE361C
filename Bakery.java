import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

class Bakery implements Lock{
    int N;
    AtomicBoolean[] choosing;
    AtomicInteger[] number;

    public Bakery(int numProc) {
        N = numProc;
        choosing = new AtomicBoolean[N];
        number = new AtomicInteger[N];
        for (int i = 0; i < N; i++) {
            choosing[i] = new AtomicBoolean(false);
            number[i] = new AtomicInteger(0);
        }
    }

    public void requestCS(int i) {
        choosing[i].set(true);
        for (int j = 0; j < N; j++) {
            if (number[j].get() > number[i].get()) {
                number[i].set(number[j].get());
            }
        }
        number[i].set(number[i].get() + 1);
        choosing[i].set(false);
        for (int j = 0; j < N; j++) {
            while (choosing[j].get());
            while (number[j].get() != 0 && (number[j].get() < number[i].get() ||
                    (number[j].get() == number[i].get() && j < i)));
        }
    }
    public void releaseCS(int i) {
        number[i].set(0);
    }
}
