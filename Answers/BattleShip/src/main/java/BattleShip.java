import java.util.Scanner;

/**
 * The BattleShip class manages the gameplay of the Battleship game between two players.
 * It includes methods to manage grids, turns, and check the game status.
 */
public class BattleShip {

    // Grid size for the game
    static final int GRID_SIZE = 10;

    // Player 1's main grid containing their ships
    static char[][] player1Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's main grid containing their ships
    static char[][] player2Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 1's tracking grid to show their hits and misses
    static char[][] player1TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's tracking grid to show their hits and misses
    static char[][] player2TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Scanner object for user input
    static Scanner scanner = new Scanner(System.in);

    /**
     * The main method that runs the game loop.
     * It initializes the grids for both players, places ships randomly, and manages turns.
     * The game continues until one player's ships are completely sunk.
     */
    public static void main(String[] args) {
        // Initialize grids for both players
        initializeGrid(player1Grid);
        initializeGrid(player2Grid);
        initializeGrid(player1TrackingGrid);
        initializeGrid(player2TrackingGrid);

        // Place ships randomly on each player's grid
        placeShips(player1Grid);
        placeShips(player2Grid);

        // Variable to track whose turn it is
        boolean player1Turn = true;

        // Main game loop, runs until one player's ships are all sunk
        while (!isGameOver()) {
            if (player1Turn) {
                System.out.println("Player 1's turn:");
                printGrid(player1TrackingGrid);
                playerTurn(player2Grid, player1TrackingGrid);
            } else {
                System.out.println("Player 2's turn:");
                printGrid(player2TrackingGrid);
                playerTurn(player1Grid, player2TrackingGrid);
            }
            player1Turn = !player1Turn;
        }

        System.out.println("Game Over!");
    }

    /**
     * Initializes the grid by filling it with water ('~').
     *
     * @param grid The grid to initialize.
     */
    static void initializeGrid(char[][] grid) {
        for (char i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = '~';
            }
        }
    }

    /**
     * Places ships randomly on the given grid.
     * This method is called for both players to place their ships on their respective grids.
     *
     * @param grid The grid where ships need to be placed.
     */
    static void placeShips(char[][] grid) {
        int randomI;
        int randomJ;
        boolean horizontal;


        for (int size = 2; size <= 5; ++size) {
            do {
                randomI = (int) (Math.random() * GRID_SIZE);
                randomJ = (int) (Math.random() * GRID_SIZE);
                horizontal = (((int) (Math.random() * 2)) == 0);
            }
            while (!canPlaceShip(grid, randomI, randomJ, size, horizontal));


            if (horizontal) {
                for (int c = 0; c < size; ++c) {
                    grid[randomI + c][randomJ] = '1';
                }
            } else {
                for (int c = 0; c < size; ++c) {
                    grid[randomI][randomJ + c] = '1';
                }
            }
        }
    }


    /**
     * Checks if a ship can be placed at the specified location on the grid.
     * This includes checking the size of the ship, its direction (horizontal or vertical),
     * and if there's enough space to place it.
     *
     * @param grid       The grid where the ship is to be placed.
     * @param row        The starting row for the ship.
     * @param col        The starting column for the ship.
     * @param size       The size of the ship.
     * @param horizontal The direction of the ship (horizontal or vertical).
     * @return true if the ship can be placed at the specified location, false otherwise.
     */
    static boolean canPlaceShip(char[][] grid, int row, int col, int size, boolean horizontal) {
        if (horizontal) {
            if (row + size > GRID_SIZE) return false;
            for (int i = 0; i < size; ++i) {
                if ((grid[row + i][col] == '1')) {
                    return false;
                }
            }

        } else {
            if (col + size > GRID_SIZE) return false;
            for (int i = 0; i < size; ++i) {
                if (grid[row][col + i] == '1') {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Manages a player's turn, allowing them to attack the opponent's grid
     * and updates their tracking grid with hits or misses.
     *
     * @param opponentGrid The opponent's grid to attack.
     * @param trackingGrid The player's tracking grid to update.
     */
    static void playerTurn(char[][] opponentGrid, char[][] trackingGrid) {
        System.out.println("Enter target (for example A5): ");
        String input = scanner.nextLine();
        if (isValidInput(input)) {
            int col = (input.charAt(0) - 'A');
            String numberString = input.substring(1);
            int row = Integer.parseInt(numberString);


            if (opponentGrid[row][col] == '~') {
                trackingGrid[row][col] = '0';
                System.out.println("Miss!");
            } else if (opponentGrid[row][col] == '1') {
                trackingGrid[row][col] = 'X';
                System.out.println("Hit!");
            }
        } else {
            System.out.println("Miss!");
        }
    }

    /**
     * Checks if the game is over by verifying if all ships are sunk.
     *
     * @return true if the game is over (all ships are sunk), false otherwise.
     */
    static boolean isGameOver() {
        if (allShipsSunk(player1TrackingGrid)) {
            System.out.println("Player 2 is winner!");
            return true;
        }
        if (allShipsSunk(player2TrackingGrid)) {
            System.out.println("Player 1 is winner!");
            return true;
        }

        return false;
    }

    /**
     * Checks if all ships have been destroyed on a given grid.
     *
     * @param grid The grid to check for destroyed ships.
     * @return true if all ships are sunk, false otherwise.
     */
    static boolean allShipsSunk(char[][] grid) {
        int destroyed_ships = 0;
        for (int row = 0; row < GRID_SIZE; ++row) {
            for (int col = 0; col < GRID_SIZE; ++col) {
                if (grid[row][col] == 'X') destroyed_ships++;
            }
        }
        if (destroyed_ships < 14) return false;

        return true;
    }

    /**
     * Validates if the user input is in the correct format (e.g., A5).
     *
     * @param input The input string to validate.
     * @return true if the input is in the correct format, false otherwise.
     */
    static boolean isValidInput(String input) {
        if (input.length() != 2) return false;
        if (input.charAt(0) < 'A' || input.charAt(0) > 'J') return false;
        try {
            String numberString = input.substring(1);
            int number = Integer.parseInt(numberString);

            if (number < 1 || number > 10) return false;
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    /**
     * Prints the current state of the player's tracking grid.
     * This method displays the grid, showing hits, misses, and untried locations.
     *
     * @param grid The tracking grid to print.
     */
    static void printGrid(char[][] grid) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (i == 0) {
                System.out.print("  ");
                for (char letter = 'A'; letter <= 'J'; letter++) {
                    System.out.print(letter + " ");

                }
                System.out.print("\n");
            }

            for (int j = 0; j < GRID_SIZE; j++) {
                if (j == 0) {
                    System.out.print(i + " ");
                }
                System.out.print(grid[i][j] + " ");
            }

            System.out.print("\n");
        }
    }
}
