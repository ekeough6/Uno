package Uno;
import java.util.ArrayList;

public class Player {
	
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

	public boolean hasLegalMove(Card topCard, String color) {
		legalMoves.clear();
		for(int i=0; i<hand.size(); i++) {
			if(hand.get(i).canPlay(topCard, color))
				legalMoves.add(i);
		}
		return legalMoves.size() > 0;
	}

	public Card playCard() {
		return hand.remove((int)legalMoves.get(0));
	}
	
	public Card playCard(int handPosition) {
		return null;
	}
	
	public String toString() {
		return hand.toString();
	}
	
}
