
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


    public void setupAllBoards() {
        gameBoards.get(0).addBlock(0, 2);
       // gameBoards.get(0).addBlock(1, 0);
        gameBoards.get(0).addMovableCell(3, 2, "Red");
        // gameBoards.get(0).addMovableCell(4, 4, "Blue");
        gameBoards.get(0).addTargetCell(0, 0, "Red");
        // gameBoards.get(0).addTargetCell(6, 6, "Blue");


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


            for (int i = 0; i < board.size(); i++) {
                ArrayList<State.Cell> row = board.get(i);


                for (int j = 0; j < row.size(); j++) {
                    State.Cell cell = row.get(j);

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

        }
    }


    public boolean dfs(State state, int depth, int maxDepth, HashSet<State> visitedStates, ArrayList<State> path) {

        if (state.checkIfReachedTarget()) {

            System.out.println("Solution found at depth " + depth);
            System.out.println("Solution path:");
            for (int i = 0; i < path.size(); i++) {
                path.get(i).printBoard();

            }
            State emptyState = new State(getCurrentBoard().getBoardState().size());
            emptyState.printBoard();
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





    public boolean bfs() {
        Queue<State> queue = new LinkedList<>();
        HashSet<State> visitedStates = new HashSet<>();
        HashMap<State, State> parentMap = new HashMap<>();


        State initialState = getCurrentBoard();
        queue.add(initialState);
        visitedStates.add(initialState);

        while (!queue.isEmpty()) {
            State currentState = queue.poll();

            if (currentState.checkIfReachedTarget()) {
                System.out.println("Goal reached!");
                List<State> path = new ArrayList<>();
                State traceBackState = currentState;

                while (traceBackState != null) {
                    path.add(traceBackState);
                    traceBackState = parentMap.get(traceBackState);
                }
                Collections.reverse(path);

                System.out.println("Path to the goal:");
                for (State state : path) {
                    state.printBoard();
                }

                System.out.println("Number of visited states: " + visitedStates.size());
                return true;
            }
            ArrayList<State> possibleMoves = currentState.getAllPossibleMoves();
            for (int i = 0; i < possibleMoves.size(); i++) {
                State nextState = possibleMoves.get(i);

                if (!visitedStates.contains(nextState)) {
                    visitedStates.add(nextState);
                    queue.add(nextState);
                    parentMap.put(nextState, currentState);
                }
            }
        }

        System.out.println("No solution found using BFS.");
        return false;
    }



    public void startGame() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printCurrentBoard();
            //printAllPossibleMoves();

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
            } else if (input.equalsIgnoreCase("bfs")) {
                System.out.println("Starting BFS...");
                if (!bfs()) {
                    System.out.println("No solution found using BFS.");
                }
            } else if (input.equalsIgnoreCase("exit")) {
                break;
            }
               // printAllPossibleMoves();

                getCurrentBoard().moveAllCells(input.toUpperCase());
                gameBoards.get(currentBoardIndex).moveAllCells(input.toUpperCase());

            if (getCurrentBoard().checkIfReachedTarget()) {
                System.out.println("Congratulations! You've reached the target!");

                State emptyState = new State(getCurrentBoard().getBoardState().size());
                emptyState.printBoard();

                System.out.println("Game Over. The game has ended!");
                break;}
        }
        scanner.close();
    }


}

