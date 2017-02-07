import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by ArtistQiu on 2017/1/26.
 */

public class Game {
    private ArrayList players = new ArrayList();
    private int[] places = new int[6]; //玩家位置
    private int[] purses = new int[6]; //金币数量
    private boolean[] inPenaltyBox = new boolean[6]; //是否在禁闭室

    private LinkedList popQuestions = new LinkedList();
    private LinkedList scienceQuestions = new LinkedList();
    private LinkedList sportsQuestions = new LinkedList();
    private LinkedList rockQuestions = new LinkedList();

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
            popQuestions.addLast("Pop Question " + i);
            scienceQuestions.addLast("Science Question " + i);
            sportsQuestions.addLast("Sports Question " + i);
            rockQuestions.addLast(" Rock Question " + i);
        }
    }

    //TODO-later: The return value of method Game.add() is not used.
    //对于服务端公共接口的改动放到最后去改
    public boolean add(String playerName) {
        players.add(playerName);
        places[howManyPlayers()] = 0;
        purses[howManyPlayers()] = 0;
        inPenaltyBox[howManyPlayers()] = false;
        //TODO-later: Replace System.out.println() with a log method of a logger.
        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
        return true;
    }

    private int howManyPlayers() {
        return players.size();
    }

    public void roll(int rollingNumber) {
        System.out.println(players.get(currentPlayer) + " is the current player");
        System.out.println("They have rolled a " + rollingNumber);

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
            System.out.println(popQuestions.removeFirst());
        }
        if (currentCategory() == "Science") {
            System.out.println(scienceQuestions.removeFirst());
        }
        if (currentCategory() == "Sports") {
            System.out.println(sportsQuestions.removeFirst());
        }
        if (currentCategory() == "Rock") {
            System.out.println(rockQuestions.removeFirst());
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