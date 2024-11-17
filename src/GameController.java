
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Collections;
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
        // gameBoards.get(0).addMovableCell(4, 4, "Blue");
        gameBoards.get(0).addTargetCell(0, 0, "Red");
        // gameBoards.get(0).addTargetCell(6, 6, "Blue");

//        gameBoards.get(1).addBlock(2, 2);
//        gameBoards.get(1).addBlock(6, 2);
//        gameBoards.get(1).addMovableCell(1, 1, "Red");
//        gameBoards.get(1).addMovableCell(3, 3, "Blue");
//        gameBoards.get(1).addMovableCell(5, 5, "Yellow");
//        gameBoards.get(1).addTargetCell(0, 0, "Red");
//        gameBoards.get(1).addTargetCell(2, 4, "Blue");
//        gameBoards.get(1).addTargetCell(6, 6, "Yellow");

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


    public boolean dfs(State state, int depth, int maxDepth, HashSet<State> visitedStates, ArrayList<State> path) {
        if (state.checkIfReachedTarget()) {

            System.out.println("Solution found at depth " + depth);
            System.out.println("Solution path:");
            for (int i = 0; i < path.size(); i++) {
                path.get(i).printBoard();

            }

            System.out.println("Number of visited states: " + visitedStates.size());
            return true;
        }

        if (depth >= maxDepth) {
            return false;
        }

        if (visitedStates.contains(state)) {
            return false;
        }

        visitedStates.add(state);
        path.add(state);

        ArrayList<State> possibleMoves = state.getAllPossibleMoves();

        for (int i = 0; i < possibleMoves.size(); i++) {
            State nextState = possibleMoves.get(i);
            if (dfs(nextState, depth + 1, maxDepth, visitedStates, path)) {
                return true;
            }
        }

        path.remove(path.size() - 1);

        return false;
    }








    public void startGame() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printCurrentBoard();
            printAllPossibleMoves();

            System.out.println("Enter direction (W, S, A, D) or 'dfs' or 'bfs' or 'exit' to quit:");
            String input = scanner.nextLine();


            if (input.equalsIgnoreCase("dfs")) {

                int maxDepth = 3;
                System.out.println("Starting DFS...");
                HashSet<State> visitedStates = new HashSet<>();
                ArrayList<State> path = new ArrayList<>();
                if (!dfs(getCurrentBoard(), 0, maxDepth, visitedStates, path)) {
                    System.out.println("No solution found within depth " + maxDepth);
                }

            } else if (input.equalsIgnoreCase("exit")) {
                break;
            } else {
                printAllPossibleMoves();

                getCurrentBoard().moveAllCells(input.toUpperCase());
                gameBoards.get(currentBoardIndex).moveAllCells(input.toUpperCase());

            }
        }
        scanner.close();
    }


}

