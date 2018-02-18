package Uno;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class UnoGame extends Application{
	
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
		clockwise = true;
		currentPlayer = 0;
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
		if(pile.get(pile.size()-1).getColor().equals("wild")) {
			do {
				color = input.next();
			} while(!color.equals("red") && !color.equals("blue") && !color.equals("yellow") && !color.equals("green"));
		}
		else
			color = topCard().getColor();
		performAction();
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
		int width = 500;
		int height = 500;
		primaryStage.setTitle("Uno");
		
		Button playButton = new Button("Play");
		StackPane pane = new StackPane();
		Image img = new Image("images/" + topCard().getColor() + topCard().getValue() + ".png");
		
		BackgroundImage bgImg = new BackgroundImage(img, 
			    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
			    BackgroundPosition.CENTER, 
			    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));
		
		playButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println(pile);
				turn();
				Image img = new Image("images/" + topCard().getColor() + topCard().getValue() + ".png");
				
				BackgroundImage bgImg = new BackgroundImage(img, 
					    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
					    BackgroundPosition.CENTER, 
					    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));
				pane.setBackground(new Background(bgImg));
			}
			
		});
		
		playButton.setLayoutX(width/2);
		playButton.setLayoutY(50);
		
		pane.setBackground(new Background(bgImg));
		pane.getChildren().add(playButton);
		Scene scene = new Scene(pane, 500, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
