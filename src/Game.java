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
        logger.info(players.get(currentPlayer) + "`s new location is" + players.get(currentPlayer).getPlace());
        logger.info("They have rolled a " + rollingNumber);

        if (players.get(currentPlayer).isInPenaltyBox()) {
            if (rollingNumber % 2 != 0) {
                isGettingOutOfPenaltyBox = true;
                players.get(currentPlayer).getOutOfPenaltyBox();

                System.out.println(players.get(currentPlayer) + " is getting out of the penalty box");
                currentPlayerMovesToNewPlaceAndAnswersAQuestion(rollingNumber);
            } else {
                System.out.println(players.get(currentPlayer) + "is not getting out of the penalty box");
                players.get(currentPlayer).stayInPenaltyBox();
            }
        } else {
            currentPlayerMovesToNewPlaceAndAnswersAQuestion(rollingNumber);
        }
    }

    private void currentPlayerMovesToNewPlaceAndAnswersAQuestion(int rollingNumber) {
        players.get(currentPlayer).moveForwardSteps(rollingNumber);
        logger.info(players.get(currentPlayer) + "`s new location is " + players.get(currentPlayer).getPlace());
        logger.info("The category is " + players.get(currentPlayer).getCurrentCategory());
        askQuestion();
    }

    private void askQuestion() {
        if (players.get(currentPlayer).getCurrentCategory() == "Pop") {
            System.out.println(questionMaker.removeFirstPopQuestion());
        }
        if (players.get(currentPlayer).getCurrentCategory() == "Science") {
            System.out.println(questionMaker.removeFirstScienceQuestion());
        }
        if (players.get(currentPlayer).getCurrentCategory() == "Sports") {
            System.out.println(questionMaker.removeFirstSportsQuestion());
        }
        if (players.get(currentPlayer).getCurrentCategory() == "Rock") {
            System.out.println(questionMaker.removeFirstRockQuestion());
        }
    }

    //todo: Move method Game.currentCategory() to class QuestionMaker

    public boolean wasCorrectlyAnswered() {
        if (players.get(currentPlayer).isInPenaltyBox()) {
            if (isGettingOutOfPenaltyBox || players.get(currentPlayer).isGettingOutOfPenaltyBox()) {
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
        players.get(currentPlayer).winAGoldCoin();
        logger.info(players.get(currentPlayer)
                + " now has " + players.get(currentPlayer).countGoldCoins()
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
        players.get(currentPlayer).sentToPenaltyBox();

        nextPlayer();
        //TODO-later: The return value of method Game.wrongAnswer() is unnecessary and should be eliminated
        return true;
    }

    private boolean isGameStillInProgress() {
        //todo: The magic number 6
        return !(players.get(currentPlayer).countGoldCoins() == 6);
    }
}