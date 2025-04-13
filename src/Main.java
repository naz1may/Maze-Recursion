import java.util.*;

public class Main {
    private static final char WALL = '0';
    private static final char PATH = '.';  // Изменил '·' на '.' для совместимости с алгоритмом решения
    private static final Random random = new Random();

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Type the size of the maze(within 10 to 20): ");
        int size = scan.nextInt();
        System.out.println();

        if (size < 10 || size > 20) {
            System.out.println("Invalid size! The size is settled as 10.");
            size = 10;
        }

        char[][] maze = generateMaze(size);
        printMaze(maze);

        // Создаем копию лабиринта для решения, чтобы оригинал остался неизменным
        char[][] mazeCopy = new char[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(maze[i], 0, mazeCopy[i], 0, size);
        }

        if (traverse(mazeCopy, 0, 0)) {
            System.out.println("SOLVED maze");
        } else {
            System.out.println("could NOT SOLVE maze");
        }

        printMaze(mazeCopy);
    }

    public static char[][] generateMaze(int size) {
        char[][] maze = new char[size][size];

        // заполняем все клетки стенами (0 нулем)
        for (int i = 0; i < size; i++) {
            Arrays.fill(maze[i], WALL);
        }

        // старт и финиш
        maze[0][0] = PATH;
        maze[size-1][size-1] = PATH;

        // Основной путь
        createMainPath(maze, size);

        // доп. случайные ветки
        addBranches(maze, size);

        return maze;
    }

    private static void createMainPath(char[][] maze, int size) {
        int x = 0, y = 0;

        while (x != size-1 || y != size-1) {
            // Выбираем направление к финишу с приоритетом
            int direction = chooseDirection(x, y, size-1, size-1);

            switch (direction) {
                case 0: // вправо
                    if (y < size-1) {
                        maze[x][y+1] = PATH;
                        y++;
                    }
                    break;
                case 1: // вниз
                    if (x < size-1) {
                        maze[x+1][y] = PATH;
                        x++;
                    }
                    break;
                case 2: // влево
                    if (y > 0 && (x != size-1 || y-1 != size-1)) {
                        maze[x][y-1] = PATH;
                        y--;
                    }
                    break;
                case 3: // вверх
                    if (x > 0 && (x-1 != size-1 || y != size-1)) {
                        maze[x-1][y] = PATH;
                        x--;
                    }
                    break;
            }
        }
    }

    private static int chooseDirection(int x, int y, int targetX, int targetY) {
        // Вероятностный выбор направления с приоритетом движения к цели
        int[] directions = new int[2]; // массив для возможных направлений к цели
        int count = 0; // счетчик возможных направлений

        if (y < targetY) directions[count++] = 0; // право (сначала запись, потом инкремент)
        if (x < targetX) directions[count++] = 1; // низ
        if (y > targetY) directions[count++] = 2; // лево
        if (x > targetX) directions[count++] = 3; // верх

        if (count > 0) {
            // 80% выбираем направление к цели, с рандомайзером, чтобы путь не был прямым
            if (random.nextInt(100) < 80) { // если условие выполняется идем к цели
                return directions[random.nextInt(count)];
            }
        }

        return random.nextInt(4);  // или случайное направление
    }

    private static void addBranches(char[][] maze, int size) {
        // Количество веток зависит от размера лабиринта
        int branches = size * 2;

        for (int i = 0; i < branches; i++) {
            int x, y;
            // Выбираем случайную точку на основном пути
            do {
                x = random.nextInt(size);
                y = random.nextInt(size);
            } while (maze[x][y] != PATH); // проверяем лежит ли точка на основном пути

            // Пытаемся создать ветку
            createBranch(maze, x, y, size);
        }
    }

    private static void createBranch(char[][] maze, int startX, int startY, int size) {
        // Длина ветки от 3 до size/2 клеток
        int length = 3 + random.nextInt(size/2);
        int x = startX, y = startY;

        for (int i = 0; i < length; i++) {
            int direction = random.nextInt(4);  // случайное направление
            int newX = x, newY = y;

            switch (direction) {
                case 0: newY++; break; // право
                case 1: newX++; break; // низ
                case 2: newY--; break; // лево
                case 3: newX--; break; // верх
            }

            // проверяем, не выходит ли ветка за границы лабиринта
            if (newX >= 0 && newX < size && newY >= 0 && newY < size) {
                if (maze[newX][newY] == WALL) {
                    maze[newX][newY] = PATH; // Если клетка — стена, превращаем её в путь
                    x = newX; // Обновляем текущие координаты
                    y = newY;
                }
            }
        }
    }

    public static void printMaze(char[][] maze) {
        System.out.println("-----------------------");
        for (char[] row : maze) {
            System.out.print("| ");
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println("|");
        }
        System.out.println("-----------------------");
    }

    // Методы для решения лабиринта
    public static boolean isValidSpot(char[][] maze, int r, int c) {
        // Проверяем, является ли клетка допустимой для перемещения
        if (r >= 0 && r < maze.length && c >= 0 && c < maze[0].length) {
            return maze[r][c] == PATH;
        }
        return false;
    }

    public static boolean traverse(char[][] maze, int r, int c) {
        if (isValidSpot(maze, r, c)) {
            // Если достигли конечной точки
            if (r == maze.length-1 && c == maze[0].length-1) {
                return true;
            }

            // Помечаем текущую клетку как посещенную
            maze[r][c] = '*';

            // Пробуем все направления (рекурсивно)
            boolean returnValue = traverse(maze, r - 1, c);  // вверх
            if (!returnValue) returnValue = traverse(maze, r, c + 1);  // вправо
            if (!returnValue) returnValue = traverse(maze, r + 1, c);  // вниз
            if (!returnValue) returnValue = traverse(maze, r, c - 1);  // влево

            // Если путь найден, помечаем текущую клетку как часть решения
            if (returnValue) {
                System.out.println(r + ", " + c);  // выводим координаты решения
                maze[r][c] = ' ';  // помечаем как часть конечного пути
            }
            return returnValue;
        }
        return false;
    }
}