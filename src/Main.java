import java.util.*;

public class Main {
    private static final char WALL = '0';
    private static final char PATH = '·';
    private static final Random random = new Random();

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Type the size of the maze(withing 10 to 20): ");
        int size = scan.nextInt();
        System.out.println();

        if (size < 10 || size > 20) {
            System.out.println("Invalid size! The size is settled as 10.");
            size=10;
        }
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
        // Выбираем направление к финишу с приоритетом
        int[] directions = new int[2]; // массив для возможных направлений к цели
        int count = 0; // счетчик возможных направлений

        if (y < targetY) directions[count++] = 0; // право (сначала запись, потом ++)
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
}