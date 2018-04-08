package Uno;

public class HumanPlayer extends Player {

	public HumanPlayer(Deck d) {
		super(d);
	}

	@Override
	public Card playCard(Card card, String color) {
		return null;
	}

	@Override
	public String chooseColor() {
		return new String[] {"red", "blue", "green", "yellow"}[(int)(Math.random()*4)];
	}


}
