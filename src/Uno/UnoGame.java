package Uno;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class UnoGame extends Application{
	
	private Deck deck;
	private ArrayList<Card> pile;
	private int gameMode;
	private Player[] players;
	private String color;
	private boolean clockwise;
	private int currentPlayer, winner;
	private Scanner input;
	private final int WIDTH = 500;
	private final int HEIGHT = 500;
	
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
		clockwise = true;
		currentPlayer = 0;
		winner = -1;
		input = new Scanner(System.in);
	}
	
	private void initGame() {
		pile.add(deck.drawCard());
		color = topCard().getColor();
		while(color.equals("wild")) {
			pile.add(deck.drawCard());
			color = topCard().getColor();
		}
		currentPlayer = 0;
		clockwise = true;
	}
	
	public void turn() {
		System.out.println("It's now player " + currentPlayer + "'s turn");
		refillDeck();
			
		while(!players[currentPlayer].hasLegalMove(topCard(), color)) {
			players[currentPlayer].draw(deck);
			refillDeck();
		}
		System.out.println("Player has " + players[currentPlayer]);
		pile.add(players[currentPlayer].playCard());
		color = topCard().getColor();
		performAction();
		if(players[currentPlayer].cardsInHand() == 0)
			winner = currentPlayer;
		nextPlayer();
		
		System.out.println(topCard());

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
		if(top.isWild()) {
			color = new String[] {"red", "green", "blue", "yellow"}[(int)(Math.random() * 4)];
		}
	}
	
	private void refillDeck() {
		if(deck.isEmpty()) {
			Card top = pile.remove(pile.size()-1);
			deck.refillDeck(pile);
			pile.clear();
			pile.add(top);
		}
	}
	
	private void nextPlayer() {
		if(clockwise)
			currentPlayer = (currentPlayer < players.length-1) ? currentPlayer+1:0;
		else
			currentPlayer = (currentPlayer > 0) ? currentPlayer-1:players.length-1;
	}

	private Card topCard() { 
		return pile.get(pile.size()-1);
	}

	public void begin() {
		Application.launch();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		initGame();
		primaryStage.setTitle("Uno");
		
		Button playButton = new Button("Play");
		CardPane pane = new CardPane(topCard());
		HandPane hand1 = new HandPane(players[0]); 
		playButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println(pile);
				turn();
				hand1.addCards(players[0]);
				pane.changeCard(topCard());
				if(winner > -1) {
					System.exit(0);
				}
			}
			
		});
		
		playButton.setLayoutX(WIDTH/2);
		playButton.setLayoutY(50);
		
		
		
		pane.changeCard(topCard());
		pane.setBottom(hand1);
		hand1.relocate(0, 300);
		pane.setCenter(playButton);
		
		Scene scene = new Scene(pane, WIDTH, HEIGHT);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	class CardPane extends BorderPane {
		public CardPane(Card card) {
			changeCard(card);
		}
		
		public void changeCard(Card card) {
			Image img = new Image("images/" + card.getColor() + card.getValue() + ".png");
			
			BackgroundImage bgImg = new BackgroundImage(img, 
				    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				    BackgroundPosition.CENTER, 
				    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));
			setBackground(new Background(bgImg));
		}
	}
	
	class HandPane extends FlowPane {
		public HandPane(Player player) {
			setVgap(8);
		    setHgap(5);
		    setPrefWrapLength(WIDTH);
		    addCards(player);
		}
		
		public void addCards(Player player) {
			getChildren().clear();
			for(int i=0; i<player.cardsInHand(); i++) {
				Card card = player.viewCard(i);
				ImageView iv = new ImageView();
				iv.setImage(new Image("images/" + card.getColor() + card.getValue() + ".png"));
				iv.setFitWidth(WIDTH/7.0);
				iv.setFitHeight(150);
				iv.setPreserveRatio(true);
				getChildren().add(iv);
			}
		}
	}
	
}
