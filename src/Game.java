import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by ArtistQiu on 2017/1/26.
 */

public class Game {
    //todo-: Move playerName into class Player
    private ArrayList<Player> players = new ArrayList<Player>(); //玩家的名字
    //todo: Move places into class Player
    private int[] places = new int[6]; //玩家位置
    //todo: Move purses into class Player
    private int[] purses = new int[6]; //金币数量
    //todo: Move inPenaltyBox into class Player
    private boolean[] inPenaltyBox = new boolean[6]; //是否在禁闭室

    //todo: Move question lists to a new class QuestionMaker
    private final QuestionMaker questionMaker = new QuestionMaker();

    private int currentPlayer = 0;
    private boolean isGettingOutOfPenaltyBox;

    private static Logger logger = Logger.getLogger("ArtistQiu.trivia.Game");
    private static FileHandler fileHandler = null;

    public Game() {
        try {
            fileHandler = new FileHandler("%h/Game-logging.log", 10000000, 1, true);
            //路径是这个：C:\Users\ArtistQiu
            fileHandler.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addHandler(fileHandler);

        for (int i = 0; i < 50; i++) {
            questionMaker.addPopQuestion("Pop Question " + i);
            questionMaker.addScienceQuestion("Science Question " + i);
            questionMaker.addSportsQuestion("Sports Question " + i);
            questionMaker.addRockQuestion(" Rock Question " + i);
        }
    }

    public void add(String playerName) {
        //todo-working-on: Move playerName into class Player
        players.add(new Player(playerName));
        logger.info(playerName + " was added");
        logger.info("The total amount of players is " + players.size());
    }

    private int howManyPlayers() {
        return players.size();
    }

    public void roll(int rollingNumber) {
        logger.info(players.get(currentPlayer) + " is the current player");
        logger.info("They have rolled a " + rollingNumber);

        if (inPenaltyBox[currentPlayer]) {
            if (rollingNumber % 2 != 0) {
                isGettingOutOfPenaltyBox = true;

                System.out.println(players.get(currentPlayer) + " is getting out of the penalty box");
                currentPlayerMovesToNewPlaceAndAnswersAQuestion(rollingNumber);
            } else {
                System.out.println(players.get(currentPlayer) + "is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
            }
        } else {
            currentPlayerMovesToNewPlaceAndAnswersAQuestion(rollingNumber);
        }
    }

    private void currentPlayerMovesToNewPlaceAndAnswersAQuestion(int rollingNumber) {
        places[currentPlayer] = places[currentPlayer] + rollingNumber;
        if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;
        System.out.println(players.get(currentPlayer) + "'s new location is " + places[currentPlayer]);
        System.out.println("The category is " + currentCategory());
        askQuestion();
    }

    private void askQuestion() {
        if (currentCategory() == "Pop") {
            System.out.println(questionMaker.removeFirstPopQuestion());
        }
        if (currentCategory() == "Science") {
            System.out.println(questionMaker.removeFirstScienceQuestion());
        }
        if (currentCategory() == "Sports") {
            System.out.println(questionMaker.removeFirstSportsQuestion());
        }
        if (currentCategory() == "Rock") {
            System.out.println(questionMaker.removeFirstRockQuestion());
        }
    }

    private String currentCategory() {
        if (places[currentPlayer] == 0) {
            return "Pop";
        }
        if (places[currentPlayer] == 4) {
            return "Pop";
        }
        if (places[currentPlayer] == 8) {
            return "Pop";
        }
        if (places[currentPlayer] == 1) {
            return "Science";
        }
        if (places[currentPlayer] == 5) {
            return "Science";
        }
        if (places[currentPlayer] == 9) {
            return "Science";
        }
        if (places[currentPlayer] == 2) {
            return "Sports";
        }
        if (places[currentPlayer] == 6) {
            return "Sports";
        }
        if (places[currentPlayer] == 10) {
            return "Sports";
        }
        return "Rock";
    }

    public boolean wasCorrectlyAnswered() {
        if (inPenaltyBox[currentPlayer]) {
            if (isGettingOutOfPenaltyBox) {
                return currentPlayerGetsAGoldCoinAndSelectNextPlayer();
            } else {
                nextPlayer();
                return true;
            }
        } else {
            return currentPlayerGetsAGoldCoinAndSelectNextPlayer();
        }
    }

    private boolean currentPlayerGetsAGoldCoinAndSelectNextPlayer() {
        System.out.println("Answer was correct!!!!");
        purses[currentPlayer]++;
        System.out.println(players.get(currentPlayer) + " now has " + purses[currentPlayer]
                + " Gold Coins.");

        boolean isGameStillInProgress = isGameStillInProgress();
        nextPlayer();

        return isGameStillInProgress;
    }

    private void nextPlayer() {
        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
    }

    public boolean wrongAnswer() {
        System.out.println("Question was incorrectly answered");
        System.out.println(players.get(currentPlayer) + " was sent to the penalty box");
        inPenaltyBox[currentPlayer] = true;

        nextPlayer();
        //TODO-later: The return value of method Game.wrongAnswer() is unnecessary and should be eliminated
        return true;
    }

    private boolean isGameStillInProgress() {
        return !(purses[currentPlayer] == 6);
    }
}