import java.util.ArrayList;

public class State {
    public enum CellType {
        EMPTY, BLOCK, MOVABLE, TARGET
    }

    public class Cell {
        CellType type;
        String color;
        int row, col;

        public Cell(CellType type, int row, int col, String color) {
            this.type = type;
            this.row = row;
            this.col = col;
            this.color = color;
        }
    }
    public ArrayList<ArrayList<Cell>> getBoardState() {
        return board;
    }
    private int n;
    private ArrayList<ArrayList<Cell>> board;

    public State(int n) {
        this.n = n;
        board = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            ArrayList<Cell> row = new ArrayList<>(n);
            for (int j = 0; j < n; j++) {
                row.add(new Cell(CellType.EMPTY, i, j, null));
            }
            board.add(row);
        }
    }

    public ArrayList<State> getAllPossibleMoves() {

        String[] directions = {"W", "S", "A", "D"};
        ArrayList<State> possibleStates = new ArrayList<>();


        ArrayList<Cell> movableCells = new ArrayList<>();
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (board.get(row).get(col).type == CellType.MOVABLE) {
                    movableCells.add(board.get(row).get(col));
                }
            }
        }

        for (Cell cell : movableCells) {
            for (String direction : directions) {
                State newState = new State(n);

                for (int row = 0; row < n; row++) {
                    for (int col = 0; col < n; col++) {
                        newState.board.get(row).set(col, new Cell(board.get(row).get(col).type, row, col, board.get(row).get(col).color));
                    }
                }


                newState.moveCell(cell.row, cell.col, direction);


                possibleStates.add(newState);

            }
        }

        return possibleStates;
    }

    public void printBoard() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Cell cell = board.get(i).get(j);
                if (cell.type == CellType.MOVABLE) {
                    System.out.print(cell.color.charAt(0) + " ");
                } else if (cell.type == CellType.BLOCK) {
                    System.out.print("# ");
                } else if (cell.type == CellType.TARGET) {
                    System.out.print("T" + Character.toLowerCase(cell.color.charAt(0)) + " ");

                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }


    public void addBlock(int row, int col) {
        board.get(row).set(col, new Cell(CellType.BLOCK, row, col, null));
    }

    public void addMovableCell(int row, int col, String color) {
        board.get(row).set(col, new Cell(CellType.MOVABLE, row, col, color));
    }

    public void addTargetCell(int row, int col, String color) {
        board.get(row).set(col, new Cell(CellType.TARGET, row, col, color));
    }




    public void moveAllCells(String direction) {
        ArrayList<Cell> cellsToMove = new ArrayList<>();
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (board.get(row).get(col).type == CellType.MOVABLE) {
                    cellsToMove.add(board.get(row).get(col));
                }
            }
        }

        for (Cell cell : cellsToMove) {
            int row = cell.row;
            int col = cell.col;
            moveCell(row, col, direction);
        }
    }

    public boolean moveCell(int row, int col, String direction) {
        Cell cell = board.get(row).get(col);
        int newRow = row;
        int newCol = col;
        String targetColor = cell.color;

        while (true) {
            switch (direction) {
                case "W":
                    newRow--;
                    break;
                case "S":
                    newRow++;
                    break;
                case "A":
                    newCol--;
                    break;
                case "D":
                    newCol++;
                    break;
                default:
                   // System.out.println("Invalid direction!");
                    return false;
            }


            if (newRow < 0 || newRow >= n || newCol < 0 || newCol >= n) {

                return false;
            }

            Cell targetCell = board.get(newRow).get(newCol);

            if (targetCell.type == CellType.TARGET && targetCell.color.equals(targetColor)) {
                System.out.println("Reached target at (" + newRow + ", " + newCol + ")");
                board.get(newRow).set(newCol, new Cell(CellType.EMPTY, newRow, newCol, null));
                board.get(row).set(col, new Cell(CellType.EMPTY, row, col, null));
                return true;
            }


            if (targetCell.type == CellType.BLOCK || (targetCell.type == CellType.MOVABLE && !targetCell.color.equals(targetColor))) {
                return false;
            }


            board.get(row).set(col, new Cell(CellType.EMPTY, row, col, null));
            row = newRow;
            col = newCol;
            board.get(row).set(col, cell);
            cell.row = row;
            cell.col = col;
        }
    }

    public boolean checkIfReachedTarget() {
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(i).size(); j++) {
                Cell cell = board.get(i).get(j);
                if (cell.type == CellType.MOVABLE) {
                    return false;
                }
            }
        }
        return true;
    }




}


