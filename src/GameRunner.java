import java.util.Random;

/**
 * Created by ArtistQiu on 2017/1/26.
 */
public class GameRunner {
    private static boolean notAWinner;

    public static void main(String[] args){
        Game aGame = new Game();
        /*
        添加玩家
         */
        aGame.add("Chet");
        aGame.add("Pat");
        aGame.add("Sue");

        Random rand = new Random();

        do{
            aGame.roll(rand.nextInt(5)+1);
            if(rand.nextInt(9) == 7){
                notAWinner = aGame.wrongAnswer();
            }
        }while(notAWinner);
    }
}
