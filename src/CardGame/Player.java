package CardGame;

import java.util.ArrayList;
import java.util.List;


public class Player {

    private List<String> cardCollection = new ArrayList<>();
    private String _name;
    private String currentCard;

    public String getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(String card) {
        currentCard = card;
    }

    public int getScore() {
        return this.cardCollection.size();
    }

    public Player(String _name) {
        this._name = _name;
    }

    public String getName() {
        return _name;
    }

    public void addToCollection(String card) {
        this.cardCollection.add(card);
    }

}














