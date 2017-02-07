/**
 * Created by ArtistQiu on 2017/1/26.
 */
public class Player {
    private String playerName;

    public Player(String playerName){
        this.playerName = playerName;
    }

    @Override
    public String toString() {
        return this.playerName;
    }
}
