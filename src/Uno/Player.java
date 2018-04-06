package Uno;
import java.util.ArrayList;

public abstract class Player {

	private ArrayList<Card> hand;
	private ArrayList<Integer> legalMoves;


	public Player(Deck d) {
		hand = new ArrayList<Card>();
		for(int i=0; i<5; i++) {
			draw(d);
		}
		legalMoves = new ArrayList<Integer>();
	}

	public void draw(Deck d) {
		hand.add(d.drawCard());
	}

	public void draw(Deck d, int numCards) {
		for(int i=0; i<numCards; i++)
			hand.add(d.drawCard());
	}

	public int cardsInHand() {
		return hand.size();
	}

	private void getLegalMoves(Card topCard, String color) {
		legalMoves.clear();
		for(int i=0; i<hand.size(); i++) {
			if(hand.get(i).canPlay(topCard, color))
				legalMoves.add(i);
		}
	}

	public boolean isLegalMove(Card topCard, String color, int position) {
		getLegalMoves(topCard, color);
		return legalMoves.contains(position);
	}

	public boolean hasLegalMove(Card topCard, String color) {
		getLegalMoves(topCard, color);
		return legalMoves.size() > 0;
	}

	public abstract Card playCard(Card card, String color);

	public Card playCard(int handPosition) {
		return hand.remove(handPosition);
	}

	public Card viewCard(int position) {
		return hand.get(position);
	}

	public abstract String chooseColor();

	public String toString() {
		return hand.toString();
	}

}
