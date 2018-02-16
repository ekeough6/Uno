package Uno;
import java.util.ArrayList;

public class HumanPlayer  {
	
	private ArrayList<Card> hand;
	
	public HumanPlayer(Deck d) {
		hand = new ArrayList<Card>();
		for(int i=0; i<5; i++) {
			draw(d);
		}
	}
	
	public void draw(Deck d) {
		// TODO Auto-generated method stub
		hand.add(d.drawCard());
	}

	public int cardsInHand() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean hasLegalMove(Card topCard) {
		// TODO Auto-generated method stub
		return false;
	}

	public Card playCard(int handPosition) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		return hand.toString();
	}

}
