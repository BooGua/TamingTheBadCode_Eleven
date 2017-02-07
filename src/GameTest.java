import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by ArtistQiu on 2017/2/4.
 */
public class GameTest {
    //TODO-user-intent: the game should be over if a player rolls the dice and answers each question correctly for 6 times.

    @Test
    public void add_two_pop_and_remove_one(){
        // Arrange
        QuestionMaker questionMaker = new QuestionMaker();

        // Act
        questionMaker.addPopQuestion("Pop Question 1");
        questionMaker.addPopQuestion("Pop Question 2");

        // Assert
        assertEquals("Pop Question 1", questionMaker.removeFirstPopQuestion());
    }

    @Test
    public void add_two_science_and_remove_one(){
        // Arrange
        QuestionMaker questionMaker = new QuestionMaker();

        // Act
        questionMaker.addScienceQuestion("Science Question 1");
        questionMaker.addScienceQuestion("Science Question 2");

        // Assert
        assertEquals("Science Question 1", questionMaker.removeFirstScienceQuestion());
    }

    @Test
    public void add_two_sports_and_remove_one(){
        // Arrange
        QuestionMaker questionMaker = new QuestionMaker();

        // Act
        questionMaker.addSportsQuestion("Sports Question 1");
        questionMaker.addSportsQuestion("Sports Question 2");

        // Assert
        assertEquals("Sports Question 1", questionMaker.removeFirstSportsQuestion());
    }

    @Test
    public void add_two_rock_and_remove_one(){
        // Arrange
        QuestionMaker questionMaker = new QuestionMaker();

        // Act
        questionMaker.addRockQuestion("Rock Question 1");
        questionMaker.addRockQuestion("Rock Question 2");

        // Assert
        assertEquals("Rock Question 1", questionMaker.removeFirstRockQuestion());
    }

    @Test
    public void the_game_should_be_over_if_a_player_rolls_the_dice_and_answers_each_question_correctly_for_6_times() {
        //Arrange
        Game game = new Game();
        game.add("Chet");
        boolean isGameStillInProgress = true;

        //Act
        for (int i = 0; i < 6; i++) {
            game.roll(1);
            isGameStillInProgress = game.wasCorrectlyAnswered();
        }

        //Assert
        assertFalse(isGameStillInProgress);
    }

    //TODO-user-intent: the game should be over if a player rolls the dice for 7 times and answers the  question wrongly
    // for 1 time followed by an odd rolling number but then correctly for 6 times
    @Test
    public void the_game_should_be_over_if_a_player_rolls_the_dice_for_7_times_and_answers_the_question_wrongly_for_1_time_followed_by_an_odd_rolling_number_but_then_correctly_for_6_times() {
        //Arrange
        Game game = new Game();
        game.add("Chet");
        boolean isGameStillInProgress = true;

        //Act
        game.roll(1);
        game.wrongAnswer();
        game.roll(1);
        game.wasCorrectlyAnswered();
        for (int i = 0; i < 5; i++) {
            game.roll(1);
            isGameStillInProgress = game.wasCorrectlyAnswered();
        }

        //Assert
        assertFalse(isGameStillInProgress);
    }

    //TODO-user-intent: the game should be over if a player rolls the dice for 8 times and answers the question wrongly
    // for 1 time followed by an odd rolling number but then correctly for 7 times
    @Test
    public void the_game_should_be_over_if_a_player_rolls_the_dice_for_8_times_and_answers_the_question_wrongly_for_1_time_followed_by_an_odd_rolling_number_but_then_correctly_for_7_times() {
        //Arrange
        Game game = new Game();
        game.add("Chet");
        boolean isGameStillInProgress = true;

        //Act
        game.roll(1);
        game.wrongAnswer();
        game.roll(2);
        game.wasCorrectlyAnswered();
        for (int i = 0; i < 6; i++) {
            game.roll(1);
            isGameStillInProgress = game.wasCorrectlyAnswered();
        }

        //Assert
        assertFalse(isGameStillInProgress);

    }
}
