package Uno;
import java.util.ArrayList;
import java.util.HashMap;

public class SmartComputerPlayer extends Player {
    public SmartComputerPlayer(Deck d) {
        super(d);
    }

    @Override
    public Card playCard(Card card, String color) {
        ArrayList<Integer> cards = new ArrayList<Integer>();
        for(int i=0; i<cardsInHand(); i++) {
            if(isLegalMove(card, color, i))
                cards.add(i);
        }
        cards.sort((o1, o2) -> viewCard(o1).compareTo(viewCard(o2)));
        return playCard(cards.get(0));
    }

    @Override
    public String chooseColor() {
        HashMap<String, Integer> colors = new HashMap<>();
        for(int i=0; i<cardsInHand(); i++) {
            if(!viewCard(i).isWild()) {
                if(colors.containsKey(viewCard(i).getColor())) {
                    colors.put(viewCard(i).getColor(), colors.get(viewCard(i).getColor())+1);
                }
                else {
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
