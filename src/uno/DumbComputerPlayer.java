package uno;

public class DumbComputerPlayer extends Player{


    public DumbComputerPlayer(Deck d) {
        super(d);
    }

    @Override
    public Card playCard(Card card, String color) {
        for(int i=0; i<cardsInHand(); i++) {
            if(isLegalMove(card, color, i))
                return playCard(i);
        }
        return null;
    }

    @Override
    public String chooseColor() {
        return new String[] {"red", "blue", "green", "yellow"}[(int)(Math.random()*4)];
    }
}
