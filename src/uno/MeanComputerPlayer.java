package uno;

/**
 * Class that extends the Player class to provide full functionality for a Computer player who prefers to
 * play draw cards whenever possible.
 */
public class MeanComputerPlayer extends Player {

	public MeanComputerPlayer(Deck d) {
		super(d);
	}

	/**
	 * Plays a card from this players hand that could legally be played on top of the given card with
	 * the given active color with a strong preference to playing draw cards.
	 *
	 * @param card  Card to check value against.
	 * @param color Active color to check card's color against.
	 * @return Played card if a card can be played; otherwise, null.
	 */
	@Override
	public Card playCard(Card card, String color) {
		for(int i = 0; i<cardsInHand(); i++) {
			if(isLegalMove(card, color, i) && viewCard(i).getValue().equals("d4"))
				return playCard(i);
		}
		for(int i = 0; i<cardsInHand(); i++) {
			if(isLegalMove(card, color, i) && viewCard(i).getValue().equals("d2"))
				return playCard(i);
		}
		for(int i = 0; i<cardsInHand(); i++) {
			if(isLegalMove(card, color, i))
				return playCard(i);
		}
		return null;
	}

	/**
	 * Picks a color for when a wild card is drawn.
	 * @return First color that shows up in hand if a non wild card is present; otherwise, random color between red, blue, green, and yellow.
	 */
	@Override
	public String chooseColor() {
		for(int i = 0; i<cardsInHand(); i++) {
			if(!viewCard(i).isWild())
				return viewCard(i).getColor();
		}
		return new String[] {"red", "blue", "green", "yellow"}[(int)(Math.random()*4)];
	}
}
