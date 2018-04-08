package Uno;

public class MeanComputerPlayer extends Player {
	

	public MeanComputerPlayer(Deck d) {
		super(d);
	}

	@Override
	public Card playCard(Card card, String color) {
		for(int i=0; i<cardsInHand(); i++) {
			if(isLegalMove(card, color, i) && viewCard(i).getValue().equals("d4"))
				return playCard(i);
		}
		for(int i=0; i<cardsInHand(); i++) {
			if(isLegalMove(card, color, i) && viewCard(i).getValue().equals("d2"))
				return playCard(i);
		}
		for(int i=0; i<cardsInHand(); i++) {
			if(isLegalMove(card, color, i))
				return playCard(i);
		}
		return null;
	}

	@Override
	public String chooseColor() {
		for(int i=0; i<cardsInHand(); i++) {
			if(!viewCard(i).isWild())
				return viewCard(i).getColor();
		}
		return new String[] {"red", "blue", "green", "yellow"}[(int)(Math.random()*4)];
	}


}
