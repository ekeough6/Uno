package uno;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Class that extends the Player class to provide full functionality for a Computer player who tries to weigh the value of his cards
 * as a form of primitive strategy
 */
public class SmartComputerPlayer extends Player {
    public SmartComputerPlayer(Deck d) {
        super(d);
    }

    /**
     * Plays a card from this players hand that could legally be played on top of the given card with
     * the given active color using a comparison between cards
     *
     * @param card  Card to check value against.
     * @param color Active color to check card's color against.
     * @return Played card if a card can be played; otherwise, null.
     */
    @Override
    public Card playCard(Card card, String color) {
        ArrayList<Integer> cards = new ArrayList<>();
        for(int i = 0; i<cardsInHand(); i++) {
            if(isLegalMove(card, color, i))
                cards.add(i);
        }
        cards.sort(Comparator.comparing(this::viewCard));
        return playCard(cards.get(0));
    }

    /**
     * Picks a color for when a wild card is drawn.
     * @return color that appears most frequently in hand if non wild cards are in the hand; otherwise, random color between red, blue, green, and yellow.
     */
    @Override
    public String chooseColor() {
        HashMap<String, Integer> colors = new HashMap<>();
        for(int i = 0; i<cardsInHand(); i++) {
            if(!viewCard(i).isWild()) {
                if(colors.containsKey(viewCard(i).getColor())) {
                    colors.put(viewCard(i).getColor(), colors.get(viewCard(i).getColor())+1);
                } else {
                    colors.put(viewCard(i).getColor(), 1);
                }
            }
        }
        String max = "";
        if(colors.isEmpty())
            return new String[] {"red", "blue", "green", "yellow"}[(int)(Math.random()*4)];
        for (String key : colors.keySet()) {
            if(max.equals("") || colors.get(key) > colors.get(max))
                max = key;
        }
        return max;
    }
}
