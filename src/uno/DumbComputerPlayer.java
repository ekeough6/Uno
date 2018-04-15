package uno;

/**
 * Class that extends the Player class to provide full functionality for a Computer player who plays
 * with no sense of strategy
 */
public class DumbComputerPlayer extends Player{

    public DumbComputerPlayer(Deck d) {
        super(d);
    }

    /**
     * Plays first card from this players hand that could legally be played on top of the given card
     *
     * @param card  Card to check value against.
     * @param color Active color to check card's color against.
     * @return Played card if a card can be played; otherwise, null.
     */
    @Override
    public Card playCard(Card card, String color) {
        for(int i = 0; i<cardsInHand(); i++) {
            if(isLegalMove(card, color, i))
                return playCard(i);
        }
        return null;
    }

    /**
     * Picks a color for when a wild card is drawn
     * @return Random color between red, blue, green, and yellow
     */
    @Override
    public String chooseColor() {
        return new String[] {"red", "blue", "green", "yellow"}[(int)(Math.random()*4)];
    }
}
