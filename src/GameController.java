import java.util.*;

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

    // Recursion Dfs
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

    public boolean ucs() {
        PriorityQueue<StateCostPair> queue = new PriorityQueue<>(Comparator.comparingInt(StateCostPair::getCost));
        HashSet<State> visitedStates = new HashSet<>();
        HashMap<State, State> parentMap = new HashMap<>();
        HashMap<State, Integer> costMap = new HashMap<>();

        State initialState = getCurrentBoard();
        queue.add(new StateCostPair(initialState, 0));
        costMap.put(initialState, 0);

        while (!queue.isEmpty()) {
            StateCostPair currentPair = queue.poll();
            State currentState = currentPair.getState();
            int currentCost = currentPair.getCost();

            if (visitedStates.contains(currentState)) {
                continue;
            }

            visitedStates.add(currentState);

            if (currentState.checkIfReachedTarget()) {
                System.out.println("Goal reached with cost: " + currentCost);
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
                int newCost = currentCost + 1;

                if (!visitedStates.contains(nextState) &&
                        (!costMap.containsKey(nextState) || newCost < costMap.get(nextState))) {
                    costMap.put(nextState, newCost);
                    queue.add(new StateCostPair(nextState, newCost));
                    parentMap.put(nextState, currentState);
                }
            }
        }

        System.out.println("No solution found using UCS.");
        return false;
    }

    class StateCostPair {
        private State state;
        private int cost;

        public StateCostPair(State state, int cost) {
            this.state = state;
            this.cost = cost;
        }

        public State getState() {
            return state;
        }

        public int getCost() {
            return cost;
        }
    }
    public boolean dfs_stack(int maxDepth) {
        Stack<State> stack = new Stack<>();
        HashMap<State, Integer> depthMap = new HashMap<>(); // لتتبع العمق
        HashSet<State> visitedStates = new HashSet<>();
        HashMap<State, State> parentMap = new HashMap<>(); // لتتبع المسار
        State initialState = getCurrentBoard();

        stack.push(initialState);
        depthMap.put(initialState, 0);
        visitedStates.add(initialState);



        while (!stack.isEmpty()) {
            State currentState = stack.pop();
            int currentDepth = depthMap.get(currentState);
            if (currentState.checkIfReachedTarget()) {
                System.out.println("Goal reached at depth " + currentDepth + "!");
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

            if (currentDepth >= maxDepth) {
                continue;
            }

            ArrayList<State> possibleMoves = currentState.getAllPossibleMoves();
            for (int i = 0; i < possibleMoves.size(); i++) {
                State nextState = possibleMoves.get(i);
                if (!visitedStates.contains(nextState)) {
                    visitedStates.add(nextState);
                    stack.push(nextState);
                    parentMap.put(nextState, currentState);
                    depthMap.put(nextState, currentDepth + 1);
                }
            }
        }


        System.out.println("No solution found.");
        return false;
    }
    public void startGame() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printCurrentBoard();

            System.out.println("Enter direction (W, S, A, D) or 'dfs' or 'bfs' or 'ucs' or 'dfss' or  'exit' to quit:");
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
            } else if (input.equalsIgnoreCase("ucs")) {
                System.out.println("Starting UCS...");
                if (!ucs()) {
                    System.out.println("No solution found using UCS.");
                }
            }
            else if (input.equalsIgnoreCase("dfss")) {
                int maxDepth = 3;
                System.out.println("Starting DFS...");
                if (!dfs_stack(maxDepth)) {
                    System.out.println("No solution found using DFS.");
                }
            }else if (input.equalsIgnoreCase("exit")) {
                break;
            }

            getCurrentBoard().moveAllCells(input.toUpperCase());
            gameBoards.get(currentBoardIndex).moveAllCells(input.toUpperCase());

            if (getCurrentBoard().checkIfReachedTarget()) {
                System.out.println("Congratulations! You've reached the target!");
                System.out.println("Game Over. The game has ended!");
                break;
            }
        }
        scanner.close();
    }
}
