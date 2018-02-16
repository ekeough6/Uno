package Uno;
import java.util.ArrayList;
import java.util.Scanner;

public class UnoGame {
	
	private Deck deck;
	private ArrayList<Card> pile;
	private int gameMode;
	private Player[] players;
	private String color;
	private boolean clockwise;
	private int currentPlayer;
	Scanner input;
	
	//Selection for different types of rules
	public static final int NORMAL_RULES = 0;
	
	public UnoGame() {
		deck = new Deck();
		deck.shuffle();
		pile = new ArrayList<Card>();
		gameMode = NORMAL_RULES;
		players = new Player[4];
		for(int i=0; i<players.length; i++)
			players[i] = new Player(deck);
		pile.add(deck.drawCard());
		color = topCard().getColor();
		while(color.equals("wild")) {
			pile.add(deck.drawCard());
			color = topCard().getColor();
		}
		clockwise = true;
		currentPlayer = 0;
		input = new Scanner(System.in);
	}
	
	public void turn() {
		System.out.println(color);
		if(deck.isEmpty()) {
			Card top = pile.remove(pile.size()-1);
			deck.refillDeck(pile);
			pile.clear();
			pile.add(top);
		}
			
		while(!players[currentPlayer].hasLegalMove(topCard(), color)) {
			players[currentPlayer].draw(deck);
		}
		pile.add(players[currentPlayer].playCard());
		if(pile.get(pile.size()-1).getColor().equals("wild")) {
			do {
				color = input.next();
			} while(!color.equals("red") && !color.equals("blue") && !color.equals("yellow") && !color.equals("green"));
		}
		performAction();
		nextPlayer();
		System.out.println(pile);
	}
	
	private void performAction() {
		Card top = topCard();
		if(top.isAction()) {
			if(top.getValue().equals("rev")) {
				clockwise = !clockwise;
			}
			else if(top.getValue().equals("d2")) {
				nextPlayer();
				players[currentPlayer].draw(deck, 2);
			}
			else if(top.getValue().equals("d4")) {
				nextPlayer();
				players[currentPlayer].draw(deck, 4);
			}
			else if(top.getValue().equals("skip")) {
				nextPlayer();
			}
		}
	}
	
	private void nextPlayer() {
		if(clockwise)
			currentPlayer = (currentPlayer < players.length-1) ? currentPlayer+1:0;
		else
			currentPlayer = (currentPlayer > 0) ? 0:players.length-1;
	}

	private Card topCard() { 
		return pile.get(pile.size()-1);
	}
}
