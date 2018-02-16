package Uno;

import java.util.ArrayList;

public class Deck {
	ArrayList<Card> cards;
	private String[] colors = {"red", "blue", "green", "yellow"};
	private String[] actions = {"d2", "skip", "rev"};
	private String[] wildCards = {"wild", "d4"};
	private int numWild;
	
	public Deck() {
		numWild = 4;
		cards = new ArrayList<Card>();
		for(int x=0; x<10; x++) {
			for(int i=0; i<colors.length; i++) {
				cards.add(new Card(colors[i], Integer.toString(x)));
				if(x != 0)
					cards.add(new Card(colors[i], Integer.toString(x)));
			}
		}
		for(int x=0; x<actions.length; x++) {
			for(int i=0; i<colors.length; i++) {
				cards.add(new Card(colors[i], actions[x]));
			}
		}
		for(int x=0; x<wildCards.length; x++) {
			for(int i=0; i<numWild; i++) {
				cards.add(new Card("wild", wildCards[x]));
			}
		}
	}
	
	public void shuffle() {
		ArrayList<Card> newCards = new ArrayList<Card>();
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
