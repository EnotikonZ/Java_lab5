// Импортируем классы/интерфейсы для работы с вводом-выводом (IOException и др.)
import java.io.*;

// Импортируем константы кодировок (например UTF-8)
import java.nio.charset.StandardCharsets;

// Импортируем классы для работы с путями и файлами (Path, Files)
import java.nio.file.*;

// Импортируем коллекции (List, Map, Set, Queue, Deque, ArrayList, HashMap, etc.)
import java.util.*;

// Импортируем Stream API (Stream, Collectors и т.п.)
import java.util.stream.*;


public class Lab5Var9 {

    // ===================== [1] Fraction + cache =====================

    // Интерфейс с базовыми операциями над дробью
    public interface FractionOps {
        double toDouble(); // преобразовать дробь в double
        void setNumerator(int numerator); // установить числитель
        void setDenominator(int denominator); // установить знаменатель
    }

    // Класс "обычная дробь" (числитель/знаменатель)
    public static class Fraction implements FractionOps {
        private int numerator; // числитель
        private int denominator; // знаменатель (всегда > 0)

        // Конструктор: задаём числитель и знаменатель
        public Fraction(int numerator, int denominator) {
            setDenominator(denominator); // сначала задаём знаменатель (проверка на 0 и нормализация знака)
            setNumerator(numerator); // затем задаём числитель
            normalizeSign(); // финальная нормализация знака (на случай изменений)
        }

        public int getNumerator() { return numerator; } // вернуть числитель
        public int getDenominator() { return denominator; } // вернуть знаменатель

        // Перевести дробь в double
        public double toDouble() {
            return (double) numerator / (double) denominator; // делим числитель на знаменатель в double
        }

        // Установить числитель
        public void setNumerator(int numerator) {
            this.numerator = numerator; // сохраняем числитель
            normalizeSign(); // следим, чтобы знаменатель оставался положительным
        }

        // Установить знаменатель
        public void setDenominator(int denominator) {
            if (denominator == 0) { // проверка на деление на 0
                throw new IllegalArgumentException("Знаменатель не может быть 0"); // выбрасываем исключение
            }
            this.denominator = denominator; // сохраняем знаменатель
            normalizeSign(); // нормализуем знак (переносим минус в числитель)
        }

        // Нормализация знака: знаменатель должен быть положительным
        protected void normalizeSign() {
            if (denominator < 0) { // если знаменатель отрицательный
                denominator = -denominator; // делаем знаменатель положительным
                numerator = -numerator; // меняем знак числителя
            }
        }

        // Строковое представление дроби
        public String toString() {
            return numerator + "/" + denominator; // формат "a/b"
        }

        // Сравнение дробей (в этой реализации — по полям, без сокращения)
        public boolean equals(Object o) {
            if (this == o) return true; // если это тот же объект
            if (!(o instanceof Fraction)) return false; // если объект не Fraction
            Fraction fraction = (Fraction) o; // приводим тип
            return numerator == fraction.numerator && denominator == fraction.denominator; // сравниваем поля
        }

        // Хэш-код для корректной работы в HashMap/HashSet
        public int hashCode() {
            return Objects.hash(numerator, denominator); // стандартный hash по двум полям
        }
    }

    // Дробь с кэшированием результата toDouble()
    public static class CachedFraction extends Fraction {
        private boolean cacheValid; // флаг валидности кэша
        private double cacheValue; // закэшированное значение double

        // Конструктор кэшируемой дроби
        public CachedFraction(int numerator, int denominator) {
            super(numerator, denominator); // вызываем конструктор базовой дроби
            this.cacheValid = false; // кэш пока не вычислен
        }

        // Переопределённый toDouble() с кэшированием
        public double toDouble() {
            if (!cacheValid) { // если кэш невалиден
                cacheValue = super.toDouble(); // считаем значение через родителя
                cacheValid = true; // помечаем кэш валидным
            }
            return cacheValue; // возвращаем кэш
        }

        // При смене числителя кэш нужно сбросить
        public void setNumerator(int numerator) {
            super.setNumerator(numerator); // меняем числитель у родителя
            cacheValid = false; // инвалидируем кэш
        }

        // При смене знаменателя кэш нужно сбросить
        public void setDenominator(int denominator) {
            super.setDenominator(denominator); // меняем знаменатель у родителя
            cacheValid = false; // инвалидируем кэш
        }

        // Строковое представление с выводом текущего double-значения
        public String toString() {
            return "CachedFraction{" + super.toString() + ", value=" + toDouble() + "}"; // включаем и дробь, и значение
        }
    }

    // ===================== [2] Meow counter =====================

    // Интерфейс "умеет мяукать"
    public interface Meowable {
        void meow(); // действие "мяу"
    }

    // Класс кота, который печатает "имя: мяу!"
    public static class Cat implements Meowable {
        private String name; // имя кота

        public Cat(String name) {
            this.name = name; // сохраняем имя
        }

        public void meow() {
            System.out.println(name + ": мяу!"); // выводим мяу в консоль
        }

        public String toString() {
            return "кот: " + name; // строковое представление кота
        }
    }

    // Декоратор, считающий количество мяуканий у другого Meowable
    public static class MeowCounter implements Meowable {
        private final Meowable target; // объект, которому делегируем meow()
        private int count; // счётчик мяуканий

        public MeowCounter(Meowable target) {
            this.target = target; // сохраняем целевой объект
            this.count = 0; // начинаем с 0
        }

        public int getCount() { return count; } // получить текущее значение счётчика

        public void meow() {
            count++; // увеличиваем счётчик
            target.meow(); // вызываем реальное мяу
        }

        public String toString() {
            return "MeowCounter{count=" + count + ", target=" + target + "}"; // выводим состояние декоратора
        }
    }

    // Класс-организатор "многократного мяуканья"
    public static class Meows {
        private String title; // название/заголовок

        public Meows(String title) {
            this.title = title; // сохраняем заголовок
        }

        // Мяукаем times раз для каждого переданного meowable
        public void meowsCare(int times, Meowable... meowables) {
            for (int i = 0; i < times; i++) { // внешний цикл по количеству повторов
                for (Meowable m : meowables) { // внутренний цикл по объектам
                    m.meow(); // вызываем meow()
                }
            }
        }

        public String toString() {
            return "Meows{" + title + "}"; // строковое представление
        }
    }

    // ===================== [3] List #8 =====================

    // Задачи на списки
    public static class ListTasks {
        private String name; // имя набора задач/объекта

        public ListTasks(String name) {
            this.name = name; // сохраняем имя
        }

        // Вернуть элементы, которые есть в l1, но отсутствуют в l2 (без повторов, порядок как в l1)
        public <T> List<T> onlyInFirst(List<T> l1, List<T> l2) {
            LinkedHashSet<T> result = new LinkedHashSet<>(); // set сохраняет порядок добавления и убирает дубликаты
            for (T x : l1) { // перебор элементов первого списка
                if (!l2.contains(x)) result.add(x); // если элемента нет во втором — добавляем
            }
            return new ArrayList<>(result); // возвращаем как список
        }

        public String toString() {
            return "ListTasks{" + name + "}"; // строковое представление
        }
    }

    // ===================== [4] Map #9 (file) =====================

    // Задачи на отображения (Map), данные берутся из файла
    public static class MapTasks {
        private String name; // имя набора задач/объекта

        public MapTasks(String name) {
            this.name = name; // сохраняем имя
        }

        // Одна строка результата экзамена
        public static class ExamRow {
            private String surname; // фамилия
            private String firstname; // имя
            private int school; // номер школы
            private int score; // балл

            public ExamRow(String surname, String firstname, int school, int score) {
                this.surname = surname; // сохраняем фамилию
                this.firstname = firstname; // сохраняем имя
                this.school = school; // сохраняем школу
                this.score = score; // сохраняем балл
            }

            public int getSchool() { return school; } // получить номер школы
            public int getScore() { return score; } // получить балл

            public String toString() {
                return surname + " " + firstname + " " + school + " " + score; // строковое представление строки
            }
        }

        // Возвращает список школ, средний балл которых выше среднего по району
        public List<Integer> schoolsAboveDistrictAverage(Path file) throws IOException {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8).stream() // читаем строки файла в UTF-8
                    .map(String::trim) // убираем пробелы по краям
                    .filter(s -> !s.isEmpty()) // выкидываем пустые строки
                    .toList(); // собираем в список

            int n = Integer.parseInt(lines.get(0)); // первая строка: количество записей
            if (n <= 0) return Collections.emptyList(); // если записей нет — возвращаем пустой список
            if (lines.size() < n + 1) { // проверяем, что реально есть n строк данных + первая строка
                throw new IllegalArgumentException("Недостаточно строк в файле (ожидалось " + n + ")"); // ошибка формата
            }

            Map<Integer, int[]> bySchool = new HashMap<>(); // school -> [sum, count] (сумма баллов и количество)
            long totalSum = 0; // сумма по району
            long totalCount = 0; // количество по району

            for (int i = 1; i <= n; i++) { // читаем ровно n строк с данными
                String[] p = lines.get(i).split("\\s+"); // делим строку по пробелам/табам
                if (p.length != 4) { // ожидаем 4 поля: фамилия имя школа балл
                    throw new IllegalArgumentException("Неверный формат строки: " + lines.get(i)); // ошибка формата
                }

                ExamRow row = new ExamRow(p[0], p[1], Integer.parseInt(p[2]), Integer.parseInt(p[3])); // создаём запись

                int[] agg = bySchool.computeIfAbsent(row.getSchool(), k -> new int[]{0, 0}); // получаем или создаём агрегат
                agg[0] += row.getScore(); // увеличиваем сумму баллов школы
                agg[1] += 1; // увеличиваем количество учеников школы

                totalSum += row.getScore(); // обновляем общую сумму
                totalCount += 1; // обновляем общее количество
            }

            double districtAvg = totalCount == 0 ? 0.0 : (double) totalSum / totalCount; // средний по району

            List<Integer> result = new ArrayList<>(); // список школ выше среднего
            for (Map.Entry<Integer, int[]> e : bySchool.entrySet()) { // перебираем школы
                int school = e.getKey(); // номер школы
                int sum = e.getValue()[0]; // сумма баллов
                int cnt = e.getValue()[1]; // количество
                double avg = (double) sum / cnt; // средний балл школы
                if (avg > districtAvg) result.add(school); // если выше среднего по району — добавляем
            }

            Collections.sort(result); // сортируем школы по возрастанию
            return result; // возвращаем результат
        }

        public String toString() {
            return "MapTasks{" + name + "}"; // строковое представление
        }
    }

    // ===================== [5] Set #10 (file) =====================

    // Задачи на множества (Set), данные берутся из файла
    public static class SetTasks {
        private String name; // имя набора задач/объекта

        public SetTasks(String name) {
            this.name = name; // сохраняем имя
        }

        // Возвращает символы, которые встречаются ровно в одном слове текста
        public List<Character> charsInExactlyOneWord(Path file) throws IOException {
            String text = Files.readString(file, StandardCharsets.UTF_8); // читаем весь файл в строку (UTF-8)

            List<String> words = new ArrayList<>(); // список слов
            StringBuilder cur = new StringBuilder(); // текущий собираемый токен
            for (int i = 0; i < text.length(); i++) { // идём по всем символам текста
                char ch = text.charAt(i); // текущий символ
                if (Character.isLetter(ch) || Character.isDigit(ch)) { // если буква или цифра — часть слова
                    cur.append(ch); // добавляем в текущее слово
                } else { // иначе разделитель
                    if (cur.length() > 0) { // если слово накопилось
                        words.add(cur.toString()); // добавляем его в список
                        cur.setLength(0); // очищаем буфер
                    }
                }
            }
            if (cur.length() > 0) words.add(cur.toString()); // добавляем последнее слово, если оно было

            Map<Character, Integer> wordCountByChar = new HashMap<>(); // символ -> сколько разных слов его содержат
            for (String w : words) { // перебираем слова
                Set<Character> inWord = new HashSet<>(); // множество уникальных символов в одном слове
                for (int i = 0; i < w.length(); i++) inWord.add(w.charAt(i)); // собираем уникальные символы слова
                for (char ch : inWord) { // увеличиваем счётчик "сколько слов содержит символ"
                    wordCountByChar.put(ch, wordCountByChar.getOrDefault(ch, 0) + 1); // +1 слово для этого символа
                }
            }

            List<Character> result = new ArrayList<>(); // итоговый список символов
            for (Map.Entry<Character, Integer> e : wordCountByChar.entrySet()) { // перебор (символ -> число слов)
                if (e.getValue() == 1) result.add(e.getKey()); // берём только те, что встретились ровно в 1 слове
            }
            result.sort(Comparator.naturalOrder()); // сортировка символов по возрастанию
            return result; // возвращаем результат
        }

        public String toString() {
            return "SetTasks{" + name + "}"; // строковое представление
        }
    }

    // ===================== [6] Queue #5 =====================

    // Задачи на очереди (Queue)
    public static class QueueTasks {
        private String name; // имя набора задач/объекта

        public QueueTasks(String name) {
            this.name = name; // сохраняем имя
        }

        // Разворачивает очередь l1 в новую очередь l2 (при этом l1 опустошается)
        public <T> Queue<T> reverseIntoNewQueue(Queue<T> l1) {
            Deque<T> stack = new ArrayDeque<>(); // используем дек как стек
            while (!l1.isEmpty()) stack.push(l1.remove()); // снимаем из очереди и кладём в стек
            Queue<T> l2 = new ArrayDeque<>(); // новая очередь для результата
            while (!stack.isEmpty()) l2.add(stack.pop()); // вынимаем из стека и добавляем в очередь (получается реверс)
            return l2; // возвращаем новую очередь
        }

        public String toString() {
            return "QueueTasks{" + name + "}"; // строковое представление
        }
    }

    // ===================== [7] Stream #1 and #2 =====================

    // Точка на плоскости
    public static class Point {
        private double x; // координата X
        private double y; // координата Y

        public Point(double x, double y) {
            this.x = x; // сохраняем X
            this.y = y; // сохраняем Y
        }

        public double getX() { return x; } // получить X
        public double getY() { return y; } // получить Y

        public String toString() {
            return "{" + x + ";" + y + "}"; // строковое представление точки
        }

        public boolean equals(Object o) {
            if (this == o) return true; // тот же объект
            if (!(o instanceof Point)) return false; // не точка
            Point p = (Point) o; // приводим тип
            return Double.compare(p.x, x) == 0 && Double.compare(p.y, y) == 0; // сравниваем координаты
        }

        public int hashCode() {
            return Objects.hash(x, y); // хэш по координатам
        }
    }

    // Ломаная линия (полилиния), заданная списком точек
    public static class Polyline {
        private List<Point> points; // точки линии

        public Polyline(List<Point> points) {
            this.points = new ArrayList<>(points); // копируем список, чтобы защититься от внешних изменений
        }

        public List<Point> getPoints() {
            return new ArrayList<>(points); // отдаём копию списка точек
        }

        public String toString() {
            return "Линия " + points; // строковое представление полилинии
        }
    }

    // Задачи на Stream API
    public static class StreamTasks {
        private String name; // имя набора задач/объекта

        public StreamTasks(String name) {
            this.name = name; // сохраняем имя
        }

        // Преобразовать список точек в полилинию:
        // 1) убрать дубликаты, 2) отсортировать по X, 3) сделать Y по модулю
        public Polyline pointsToPolyline(List<Point> points) {
            List<Point> processed = points.stream() // создаём поток по точкам
                    .distinct() // убираем дубликаты (по equals/hashCode)
                    .sorted(Comparator.comparingDouble(Point::getX)) // сортируем по X
                    .map(p -> new Point(p.getX(), Math.abs(p.getY()))) // делаем Y неотрицательным
                    .toList(); // собираем в список
            return new Polyline(processed); // создаём полилинию по обработанным точкам
        }

        // Строка вида "имя:число" (или имя без числа)
        public static class PersonLine {
            private String name; // имя
            private Integer number; // число (может быть null)

            public PersonLine(String name, Integer number) {
                this.name = name; // сохраняем имя
                this.number = number; // сохраняем число
            }

            public String getName() { return name; } // получить имя
            public Integer getNumber() { return number; } // получить число

            public String toString() {
                return "PersonLine{name='" + name + "', number=" + number + "}"; // строковое представление
            }
        }

        // Нормализация имени: trim, lowerCase, первая буква заглавная
        public String normalizeName(String raw) {
            String s = raw.trim(); // убираем пробелы по краям
            if (s.isEmpty()) return s; // если пусто — возвращаем пустую строку
            s = s.toLowerCase(Locale.ROOT); // приводим к нижнему регистру (стабильно по Locale)
            return Character.toUpperCase(s.charAt(0)) + s.substring(1); // делаем первую букву заглавной
        }

        // Сгруппировать имена по числу из файла:
        // строки могут быть "name:number" или просто "name", пустые строки игнорируются
        public Map<Integer, List<String>> groupNamesByNumber(Path file) throws IOException {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8); // читаем все строки файла

            List<PersonLine> persons = lines.stream() // поток по строкам
                    .map(String::trim) // обрезаем пробелы
                    .filter(s -> !s.isEmpty()) // выкидываем пустые строки
                    .map(line -> { // парсим строку в PersonLine
                        String name; // имя слева
                        Integer num = null; // число справа (по умолчанию null)

                        if (line.contains(":")) { // если есть разделитель ":"
                            String[] parts = line.split(":", 2); // делим на 2 части максимум
                            name = parts[0].trim(); // имя — левая часть
                            String right = parts.length > 1 ? parts[1].trim() : ""; // правая часть (число)
                            if (!right.isEmpty() && right.matches("\\d+")) { // если справа только цифры
                                num = Integer.parseInt(right); // парсим число
                            }
                        } else { // если ":" нет
                            name = line.trim(); // всё — это имя
                        }

                        return new PersonLine(name, num); // создаём объект
                    })
                    .toList(); // собираем список PersonLine

            Map<Integer, List<String>> res = new LinkedHashMap<>(); // итог: число -> список имён (сохранение порядка вставки)
            for (PersonLine p : persons) { // перебираем распарсенных людей
                if (p.getNumber() == null) continue; // пропускаем строки без числа
                String nm = normalizeName(p.getName()); // нормализуем имя
                res.computeIfAbsent(p.getNumber(), k -> new ArrayList<>()).add(nm); // добавляем имя по ключу-числу
            }
            return res; // возвращаем карту группировок
        }

        public String toString() {
            return "StreamTasks{" + name + "}"; // строковое представление
        }
    }

    // Универсальная функция склейки коллекции в строку через разделитель
    public static <T> String joinCollection(Collection<T> c, String sep) {
        StringBuilder sb = new StringBuilder(); // накапливаем строку
        boolean first = true; // флаг "первый элемент"
        for (T x : c) { // перебираем элементы коллекции
            if (!first) sb.append(sep); // если не первый — добавляем разделитель
            sb.append(x); // добавляем элемент (через toString)
            first = false; // дальше уже не первый
        }
        return sb.toString(); // возвращаем склеенную строку
    }
}
