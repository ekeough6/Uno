package uno;

import java.util.ArrayList;

/**
 * Deck is a collection of cards from which cards can be drawn, shuffled,
 * and to which cards can be added
 */
public class Deck {
	private ArrayList<Card> cards;

	/**
	 * Creates a new un-shuffled deck of UNO cards with four of each wild card, and
	 * one card of each action and number in all four colors for a total of 60 cards
	 */
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
		for (String wildCard : wildCards) {
			for (int i = 0; i < numWild; i++) {
				cards.add(new Card("wild", wildCard));
			}
		}
	}

	/**
	 * Randomizes the positions of every card in the deck to shuffle the deck
	 */
	public void shuffle() {
		ArrayList<Card> newCards = new ArrayList<>();
		while(cards.size() > 0) {
			newCards.add(cards.remove((int)(Math.random() * cards.size())));
		}
		cards = newCards;
	}

	/**
	 * Gives the card from the top of the deck
	 * @return The first card in the deck
	 */
	public Card drawCard() {
		return cards.remove(0);
	}

	/**
	 * Checks to see if the deck has 0 cards left
	 * @return true if the deck has 0 cards; otherwise, false
	 */
	public boolean isEmpty() {
		return cards.size() == 0;
	}

	/**
	 * Adds all of the cards from a given set of cards into the deck
	 * @param pile cards ot be added to the deck
	 */
	public void refillDeck(ArrayList<Card> pile) {
		cards.addAll(pile);
		shuffle();
	}


}
