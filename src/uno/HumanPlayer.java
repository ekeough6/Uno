package uno;

/**
 * Class that extends the Player class to provide full functionality for a human player
 */
public class HumanPlayer extends Player {
	public HumanPlayer(Deck d) {
		super(d);
	}

	/**
	 * Not a method that should be called
	 *
	 * @param card  Card that should be matched to
	 * @param color Color that should be matched to
	 * @return null
	 */
	@Override
	public Card playCard(Card card, String color) {
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
