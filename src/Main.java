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
}