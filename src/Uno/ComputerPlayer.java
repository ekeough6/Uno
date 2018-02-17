package Uno;
import java.util.ArrayList;

public class ComputerPlayer extends Player {
	
	private ArrayList<Card> hand;
	private ArrayList<Integer> legalMoves;
	
	public ComputerPlayer(Deck d) {
		super(d);
	}

	public Card playCard() {
		return hand.remove((int)legalMoves.get((int)(Math.random() * legalMoves.size())));
	}
	
	

}
