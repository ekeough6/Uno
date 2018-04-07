package Uno;

import java.util.ArrayList;

public class Deck {
	ArrayList<Card> cards;

	public Deck() {
		int numWild = 4;
		cards = new ArrayList<>();
		String[] colors = {"red", "blue", "green", "yellow"};
		for(int x = 0; x<10; x++) {
			for (String color : colors) {
				cards.add(new Card(color, Integer.toString(x)));
				if (x != 0)
					cards.add(new Card(color, Integer.toString(x)));
			}
		}
		String[] actions = {"d2", "skip", "rev"};
		for (String action : actions) {
			for (String color : colors) {
				cards.add(new Card(color, action));
			}
		}
		String[] wildCards = {"wild", "d4"};
		for(int x = 0; x< wildCards.length; x++) {
			for(int i = 0; i< numWild; i++) {
				cards.add(new Card("wild", wildCards[x]));
			}
		}
	}
	
	public void shuffle() {
		ArrayList<Card> newCards = new ArrayList<>();
		while(cards.size() > 0) {
			newCards.add(cards.remove((int)(Math.random() * cards.size())));
		}
		cards = newCards;
	}
	
	public String toString() {
		return cards.toString();
	}
	
	public Card drawCard() {
		return cards.remove(0);
	}
	
	public boolean isEmpty() {
		return cards.size() == 0;
	}
	
	public void refillDeck(ArrayList<Card> pile) {
		cards.addAll(pile);
		shuffle();
	}
	
	
}
