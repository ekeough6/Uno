package uno;
import java.util.ArrayList;

/**
 * Player class from which all other player classes extend.
 * <p>
 * Provides functionality so that moves can be determined from a given hand.
 * and so that cards can be appropriately added to that hand.
 * </p>
 * <p>
 * Note that the chooseColor and playCard methods are abstract and must be implemented.
 * </p>
 */
public abstract class Player {

	private ArrayList<Card> hand;
	private final ArrayList<Integer> legalMoves;

	public Player(Deck d) {
		hand = new ArrayList<>();
		for(int i = 0; i<5; i++) {
			draw(d);
		}
		legalMoves = new ArrayList<>();
	}

	/**
	 * Adds the top card of a deck to the player's hand.
	 * @param d The deck from which the card will be drawn.
	 */
	public void draw(Deck d) {
		hand.add(d.drawCard());
	}

	/**
	 * get the number of cards in this player's hand.
	 * @return number of cards in hand.
	 */
	public int cardsInHand() {
		return hand.size();
	}

	/**
	 * Creates a list of UNO cards that the player has that they can legally play with the given card and color.
	 * @param topCard The card which the player's card must match to play.
	 * @param color The color which the player's card must match to play.
	 */
	private void getLegalMoves(Card topCard, String color) {
		legalMoves.clear();
		for(int i = 0; i<hand.size(); i++) {
			if(hand.get(i).canPlay(topCard, color))
				legalMoves.add(i);
		}
	}

	/**
	 * Checks to see if the card at the given hand position is a legal to play
	 * @param topCard The card which the player's card must match to play.
	 * @param color The color which the player's card must match to play.
	 * @param position The position of the desired card in the player's hand.
	 * @return true if card can be played; otherwise, false.
	 */
	public boolean isLegalMove(Card topCard, String color, int position) {
		getLegalMoves(topCard, color);
		return legalMoves.contains(position);
	}

	/**
	 * Checks to see if the player has any cards they can legally play.
	 * @param topCard The card which the player's card must match to play.
	 * @param color The color which the player's card must match to play.
	 * @return true if there are cards which the player can play; otherwise, false.
	 */
	public boolean hasLegalMove(Card topCard, String color) {
		getLegalMoves(topCard, color);
		return legalMoves.size() > 0;
	}

	public abstract Card playCard(Card card, String color);

	/**
	 * Removes the card at the given position from the player's hand
	 * @param handPosition The position of the desired card in the player's hand
	 * @return The card at the given position in the player's hand
	 */
	public Card playCard(int handPosition) {
		return hand.remove(handPosition);
	}

	/**
	 * Peeks at the card at the given position from the player's hand
	 * @param handPosition The position of the desired card in the player's hand
	 * @return The card at the given position in the player's hand
	 */
	public Card viewCard(int handPosition) {
		return hand.get(handPosition);
	}

	public abstract String chooseColor();

}
