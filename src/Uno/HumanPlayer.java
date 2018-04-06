package Uno;
import java.util.ArrayList;

public class HumanPlayer extends Player {

	private ArrayList<Card> hand;

	public HumanPlayer(Deck d) {
		super(d);
	}

	@Override
	public Card playCard(Card card, String color) {
		return null;
	}

	@Override
	public String chooseColor() {
		return  new String[] {"red", "blue", "green", "yellow"}[(int)(Math.random()*4)];
	}


}
