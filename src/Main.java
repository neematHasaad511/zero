
import java.util.ArrayList;
public class Main {
    public static void main(String[] args) {

        GameController controller = new GameController();
        State board1 = new State(5);
//        State board2 = new State(7);
        controller.addBoard(board1);
//        controller.addBoard(board2);
        controller.setupAllBoards();
        controller.startGame();
    }
}
