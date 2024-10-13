package ip.ivanov.danil;

//f(x) = exp(x), [0,5], h = 10E-6

public class Integration {
    private static double f(double x) {
        return Math.exp(x);
    }
    public static void main(String[] args) {
        double a = 0.0;
        double b = 5.0;
        double h = 1e-6;

        for (int n = 1; n <= 20; n++) {
            long startTime = System.currentTimeMillis();
            double result = integrate(a, b, h, n);
            long endTime = System.currentTimeMillis();
            System.out.println(n + " - " + (endTime - startTime) + " мс.");
        }
    }
    public static double integrate(double a, double b, double h, int threads) {
        double totalSum = 0.0;
        double stepSize = (b - a) / threads;
        Thread[] threadArray = new Thread[threads];
        double[] results = new double[threads];

        for (int i = 0; i < threads; i++) {
            final int index = i;
            threadArray[i] = new Thread(() -> {
                double localA = a + index * stepSize;
                double localB = localA + stepSize;
                double localSum = 0.0;

                for (double x = localA; x < localB; x += h) {
                    localSum += 0.5 * (f(x) + f(x + h)) * h;
                }
                results[index] = localSum;
            });
            threadArray[i].start();
        }
        for (int i = 0; i < threads; i++) {
            try {
                threadArray[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (double sum : results) {
            totalSum += sum;
        }
        return totalSum;
    }
}
