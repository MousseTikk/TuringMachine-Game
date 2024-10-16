package g56583.esi.atl;

import g56583.esi.atl.controller.console.GameController;
import g56583.esi.atl.model.GameFacade;
import g56583.esi.atl.view.console.ConsoleView;

public class Main {
    public static void main(String[] args) {
        String problemsFilePath = "src/main/resources/known_problems.csv";
        GameFacade game = new GameFacade(problemsFilePath);
        ConsoleView view = new ConsoleView();
        GameController controller = new GameController(game, view);
        controller.startGame();
    }
}
