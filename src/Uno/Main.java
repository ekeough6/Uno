package Uno;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Deck deck = new Deck();
		Player p = new Player(deck);
		//deck.shuffle();
		while(!deck.isEmpty()) {
			System.out.println(deck.drawCard());
		}
		
		System.out.println(p);
		
		UnoGame uno = new UnoGame();
		uno.begin();
		for(int i=0; i<10; i++)
			uno.turn();
	}
	
	
}
