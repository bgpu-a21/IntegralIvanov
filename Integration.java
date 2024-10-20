package ip.ivanov.danil;

// Программа для численного интегрирования функции f(x) = exp(x) методом трапеций на отрезке [0, 5] с шагом 10E-6 и использованием многопоточности.

public class Integration {
    // Метод вычисления функции f(x) = exp(x)
    private static double f(double x) {
        return Math.exp(x); // Вычисляем экспоненту от x
    }

    public static void main(String[] args) {
        // Пределы интегрирования [a, b]
        double a = 0.0; 
        double b = 5.0; 
        double h = 1e-6; // Шаг интегрирования 10^-6

        // Запуск интегрирования с различным количеством потоков от 1 до 20
        for (int n = 1; n <= 20; n++) {
            long startTime = System.currentTimeMillis(); // Время начала
            double result = integrate(a, b, h, n); // Вызываем метод интегрирования
            long endTime = System.currentTimeMillis(); // Время окончания
            System.out.println(n + " - " + (endTime - startTime) + " мс."); // Выводим количество потоков и время выполнения
        }
    }

    // Метод для интегрирования функции на отрезке [a, b] с шагом h и использованием заданного количества потоков
    public static double integrate(double a, double b, double h, int threads) {
        double totalSum = 0.0; // Итоговая сумма интеграла
        double stepSize = (b - a) / threads; // Определяем размер участка для каждого потока
        Thread[] threadArray = new Thread[threads]; // Массив для потоков
        double[] results = new double[threads]; // Массив для хранения результатов каждого потока

        // Создание и запуск потоков
        for (int i = 0; i < threads; i++) {
            final int index = i;
            threadArray[i] = new Thread(() -> {
                // Локальные границы для каждого потока
                double localA = a + index * stepSize;
                double localB = localA + stepSize;
                double localSum = 0.0;

                // Численное интегрирование методом трапеций
                for (double x = localA; x < localB; x += h) {
                    localSum += 0.5 * (f(x) + f(x + h)) * h; // Вычисляем площадь трапеции
                }
                results[index] = localSum; // Запоминаем результат в массиве
            });
            threadArray[i].start(); // Запуск потока
        }

        // Ожидание завершения всех потоков
        for (int i = 0; i < threads; i++) {
            try {
                threadArray[i].join(); // Ожидаем завершения потока
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Суммируем результаты всех потоков
        for (double sum : results) {
            totalSum += sum;
        }

        return totalSum; // Возвращаем итоговый результат интегрирования
    }
}
