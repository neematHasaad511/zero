
import java.util.Scanner;
import java.util.ArrayList;

public class GameController {
    private ArrayList<State> gameBoards;
    private int currentBoardIndex;

    public GameController() {
        gameBoards = new ArrayList<>();
        currentBoardIndex = 0;
    }

    public void addBoard(State board) {
        gameBoards.add(board);
    }

    public State getCurrentBoard() {
        return gameBoards.get(currentBoardIndex);
    }

    public ArrayList<State> getGameBoards() {
        return gameBoards;
    }

    public void setupAllBoards() {
        gameBoards.get(0).addBlock(2, 2);
        gameBoards.get(0).addMovableCell(1, 1, "Red");
        gameBoards.get(0).addMovableCell(4, 4, "Blue");
        gameBoards.get(0).addTargetCell(0, 0, "Red");
        gameBoards.get(0).addTargetCell(6, 6, "Blue");

        gameBoards.get(1).addBlock(2, 2);
        gameBoards.get(1).addBlock(6, 2);
        gameBoards.get(1).addMovableCell(1, 1, "Red");
        gameBoards.get(1).addMovableCell(3, 3, "Blue");
        gameBoards.get(1).addMovableCell(5, 5, "Yellow");
        gameBoards.get(1).addTargetCell(0, 0, "Red");
        gameBoards.get(1).addTargetCell(2, 4, "Blue");
        gameBoards.get(1).addTargetCell(6, 6, "Yellow");

    }

    public void printAllPossibleMoves() {
        State currentState = getCurrentBoard();
        ArrayList<State> possibleMoves = currentState.getAllPossibleMoves();

        if (possibleMoves.isEmpty()) {
            System.out.println("No possible moves available.");
        } else {
            System.out.println("Possible moves:");
            for (int i = 0; i < possibleMoves.size(); i++) {
                System.out.println("Move " + (i + 1) + ":");
                possibleMoves.get(i).printBoard();
            }
        }

    }


    public void printCurrentBoard() {
        if (currentBoardIndex < gameBoards.size()) {
            System.out.println("Board " + (currentBoardIndex + 1) + ":");
            ArrayList<ArrayList<State.Cell>> board = gameBoards.get(currentBoardIndex).getBoardState();
            for (ArrayList<State.Cell> row : board) {
                for (State.Cell cell : row) {
                    if (cell.type == State.CellType.MOVABLE) {
                        System.out.print(cell.color.charAt(0) + " ");
                    } else if (cell.type == State.CellType.BLOCK) {
                        System.out.print("# ");
                    } else if (cell.type == State.CellType.TARGET) {
                        System.out.print("T" + Character.toLowerCase(cell.color.charAt(0)) + " ");

                    } else {
                        System.out.print(". ");
                    }
                }
                System.out.println();
            }
            System.out.println();
        }
    }





    public void startGame() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printCurrentBoard();
            printAllPossibleMoves();

            if (gameBoards.get(currentBoardIndex).checkIfReachedTarget()) {
                System.out.println("Congratulations! All movable cells have reached their targets.");


                currentBoardIndex++;
                if (currentBoardIndex >= gameBoards.size()) {
                    System.out.println("No more boards left. Game over!");
                    break;
                } else {
                    System.out.println("Moving to the next board...");
                }
                continue;
            }

            System.out.println("Enter direction (W, S, A, D) or 'exit' to quit:");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) break;

            String direction = input.toUpperCase();
            getCurrentBoard().moveAllCells(direction);

            gameBoards.get(currentBoardIndex).moveAllCells(direction);


        }
        scanner.close();
    }

}

