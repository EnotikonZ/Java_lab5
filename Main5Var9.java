import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * Лабораторная работа №5 (Вариант 9)
 * Единственная точка входа (main).
 *
 * Все исходные данные вводятся с клавиатуры + проверка.
 * Для заданий 4/5/7.2 — путь к файлу вводится с клавиатуры.
 */
public class Main5Var9 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== ЛАБОРАТОРНАЯ РАБОТА №5 | Вариант 9 ===");

        demoFraction(sc);
        demoMeowCounter(sc);
        demoListTask(sc);
        demoMapTask(sc);
        demoSetTask(sc);
        demoQueueTask(sc);
        demoStreamPoints(sc);
        demoStreamPeople(sc);

        System.out.println("\n=== ГОТОВО ===");
    }

    // [1] Дробь + кэш
    private static void demoFraction(Scanner sc) {
        System.out.println("\n[1] Дробь + кэширование");

        int num = readInt(sc, "Введите числитель: ");
        int den = readNonZeroInt(sc, "Введите знаменатель (не 0): ");

        Lab5Var9.CachedFraction f = new Lab5Var9.CachedFraction(num, den);

        System.out.println("Дробь: " + f);
        System.out.println("toDouble() #1: " + f.toDouble());
        System.out.println("toDouble() #2 (кэш): " + f.toDouble());

        System.out.println("\nИзменим числитель -> кэш должен сброситься.");
        int newNum = readInt(sc, "Новый числитель: ");
        f.setNumerator(newNum);

        System.out.println("Дробь: " + f);
        System.out.println("toDouble() после изменения: " + f.toDouble());
    }

    // [2] Мяуканья
    private static void demoMeowCounter(Scanner sc) {
        System.out.println("\n[2] Подсчёт мяуканий (кота менять нельзя)");

        String name = readNonEmptyString(sc, "Имя кота: ");
        int times = readIntRange(sc, "Сколько раз мяукать (1..20): ", 1, 20);

        Lab5Var9.Cat cat = new Lab5Var9.Cat(name);
        Lab5Var9.MeowCounter counter = new Lab5Var9.MeowCounter(cat);
        Lab5Var9.Meows meows = new Lab5Var9.Meows("meowsCare");

        meows.meowsCare(times, counter);

        System.out.println("Кот мяукнул: " + counter.getCount() + " раз(а)");
    }

    // [3] Список №8
    private static void demoListTask(Scanner sc) {
        System.out.println("\n[3] Список №8: элементы L1, которых нет в L2 (по одному разу)");

        List<Integer> l1 = readIntList(sc, "Введите L1 (числа через пробел): ");
        List<Integer> l2 = readIntList(sc, "Введите L2 (числа через пробел): ");

        Lab5Var9.ListTasks tasks = new Lab5Var9.ListTasks("onlyInFirst");
        List<Integer> res = tasks.onlyInFirst(l1, l2);

        System.out.println("L1 = " + l1);
        System.out.println("L2 = " + l2);
        System.out.println("Результат = " + res);
    }

    // [4] Map №9 (из файла)
    private static void demoMapTask(Scanner sc) {
        System.out.println("\n[4] Map №9 (из файла): школы выше среднего по району");

        Path file = readExistingFile(sc,
                "Путь к файлу (N + N строк 'Фамилия Имя Школа Балл'): ");

        Lab5Var9.MapTasks mapTasks = new Lab5Var9.MapTasks("schoolsAboveDistrictAverage");
        try {
            List<Integer> schools = mapTasks.schoolsAboveDistrictAverage(file);
            if (schools.isEmpty()) {
                System.out.println("Таких школ нет.");
            } else {
                System.out.println("Школы: " + Lab5Var9.joinCollection(schools, " "));
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Ошибка формата: " + e.getMessage());
        }
    }

    // [5] Set №10 (из файла)
    private static void demoSetTask(Scanner sc) {
        System.out.println("\n[5] Set №10 (из файла): символы ровно в одном слове");

        Path file = readExistingFile(sc, "Путь к текстовому файлу: ");

        Lab5Var9.SetTasks setTasks = new Lab5Var9.SetTasks("charsInExactlyOneWord");
        try {
            List<Character> chars = setTasks.charsInExactlyOneWord(file);
            if (chars.isEmpty()) {
                System.out.println("Таких символов нет.");
            } else {
                System.out.print("Символы: ");
                for (char ch : chars) System.out.print(ch + " ");
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения: " + e.getMessage());
        }
    }

    // [6] Queue №5
    private static void demoQueueTask(Scanner sc) {
        System.out.println("\n[6] Очередь №5: переписать L1 -> L2 в обратном порядке");

        List<Integer> list = readIntList(sc, "Введите L1 (числа через пробел): ");
        Queue<Integer> q1 = new ArrayDeque<>(list);

        Lab5Var9.QueueTasks qt = new Lab5Var9.QueueTasks("reverseIntoNewQueue");
        Queue<Integer> q2 = qt.reverseIntoNewQueue(q1);

        System.out.println("L1 после = " + q1);
        System.out.println("L2 = " + q2);
    }

    // [7.1] Stream №1
    private static void demoStreamPoints(Scanner sc) {
        System.out.println("\n[7.1] Stream №1: Point -> Polyline");

        int n = readIntRange(sc, "Сколько точек? (1..20): ", 1, 20);
        List<Lab5Var9.Point> points = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            System.out.println("Точка #" + i);
            double x = readDouble(sc, "  X: ");
            double y = readDouble(sc, "  Y: ");
            points.add(new Lab5Var9.Point(x, y));
        }

        Lab5Var9.StreamTasks st = new Lab5Var9.StreamTasks("pointsToPolyline");
        Lab5Var9.Polyline pl = st.pointsToPolyline(points);

        System.out.println("Polyline = " + pl);
    }

    // [7.2] Stream №2 (из файла)
    private static void demoStreamPeople(Scanner sc) {
        System.out.println("\n[7.2] Stream №2 (из файла): 'Имя:номер' -> группировка");

        Path file = readExistingFile(sc, "Путь к файлу (строки 'Вася:5', 'Миша:' ...): ");

        Lab5Var9.StreamTasks st = new Lab5Var9.StreamTasks("groupNamesByNumber");
        try {
            Map<Integer, List<String>> grouped = st.groupNamesByNumber(file);
            System.out.println("Результат: " + grouped);
        } catch (IOException e) {
            System.out.println("Ошибка чтения: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Ошибка формата: " + e.getMessage());
        }
    }

    // ---------- Ввод + проверки ----------

    private static String readNonEmptyString(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("Ошибка: пустая строка.");
        }
    }

    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число.");
            }
        }
    }

    private static int readNonZeroInt(Scanner sc, String prompt) {
        while (true) {
            int v = readInt(sc, prompt);
            if (v != 0) return v;
            System.out.println("Ошибка: нельзя 0.");
        }
    }

    private static int readIntRange(Scanner sc, String prompt, int min, int max) {
        while (true) {
            int v = readInt(sc, prompt);
            if (v >= min && v <= max) return v;
            System.out.println("Ошибка: диапазон [" + min + ";" + max + "].");
        }
    }

    private static double readDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim().replace(',', '.');
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число (например 2.5).");
            }
        }
    }

    private static List<Integer> readIntList(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                System.out.println("Ошибка: пустой ввод.");
                continue;
            }
            String[] parts = line.split("\\s+");
            List<Integer> res = new ArrayList<>();
            boolean ok = true;
            for (String p : parts) {
                try {
                    res.add(Integer.parseInt(p));
                } catch (NumberFormatException e) {
                    ok = false;
                    break;
                }
            }
            if (ok) return res;
            System.out.println("Ошибка: вводите только целые числа через пробел.");
        }
    }

    private static Path readExistingFile(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (s.isEmpty()) {
                System.out.println("Ошибка: пустой путь.");
                continue;
            }
            Path p = Paths.get(s);
            if (!Files.exists(p)) {
                System.out.println("Ошибка: файл не найден.");
                continue;
            }
            if (!Files.isRegularFile(p)) {
                System.out.println("Ошибка: это не файл.");
                continue;
            }
            if (!Files.isReadable(p)) {
                System.out.println("Ошибка: нет прав на чтение.");
                continue;
            }
            return p;
        }
    }
}
