package Uno;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class UnoGame extends Application{

	private Deck deck;
	private ArrayList<Card> pile;
	private int gameMode;
	private Player[] players;
	private String color;
	private boolean clockwise;
	private int currentPlayer, winner;
	private Button playButton;
	private CardPane pane;
	private HandPane hand1;
	private BorderPane title, container;
	private Label player1, player2, player3, colorText;
	private VBox holder1, holder2, holder3;

	private final int WIDTH = 1000;
	private final int HEIGHT = 800;

	//Selection for different types of rules
	public static final int NORMAL_RULES = 0;

	public UnoGame() {
		deck = new Deck();
		deck.shuffle();
		pile = new ArrayList<Card>();
		gameMode = NORMAL_RULES;
		players = new Player[4];
		players[0] = new HumanPlayer(deck);
		players[1] = new MeanComputerPlayer(deck);
		players[2] = new DumbComputerPlayer(deck);
		players[3] = new SmartComputerPlayer(deck);
		clockwise = true;
		currentPlayer = 0;
		winner = -1;
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


	private void drawToPlay() {
		while(!players[currentPlayer].hasLegalMove(topCard(), color)) {
			players[currentPlayer].draw(deck);
			refillDeck();
		}

    }

	public void turn() {
		System.out.println("It's now player " + currentPlayer + "'s turn");
		refillDeck();
		drawToPlay();
		System.out.println("Player has " + players[currentPlayer]);
		pile.add(players[currentPlayer].playCard(topCard(), color));
		color = topCard().getColor();
		performAction();
		if(players[currentPlayer].cardsInHand() == 0)
			winner = currentPlayer;
		nextPlayer();

		System.out.println(topCard());
		hand1.addCards(players[0]);
		if(currentPlayer == 0)
			playButton.setText("Draw");
        colorText.setText(color);
        player1.setText(Integer.toString(players[1].cardsInHand()));
        player2.setText(Integer.toString(players[2].cardsInHand()));
        player3.setText(Integer.toString(players[3].cardsInHand()));
    }

	public void turn(Card card) {
		System.out.println("It's now player " + currentPlayer + "'s turn");
		refillDeck();

		System.out.println("Player has " + players[currentPlayer]);
		pile.add(card);
		color = topCard().getColor();
		performAction();
		if(players[currentPlayer].cardsInHand() == 0)
			winner = currentPlayer;
		nextPlayer();

		System.out.println(topCard());
		playButton.setText("Play");
		hand1.addCards(players[0]);
		colorText.setText(color);
        player1.setText(Integer.toString(players[1].cardsInHand()));
        player2.setText(Integer.toString(players[2].cardsInHand()));
        player3.setText(Integer.toString(players[3].cardsInHand()));
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
			color = players[currentPlayer].chooseColor();
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

		playButton = new Button("Draw");
		pane = new CardPane(topCard());
		hand1 = new HandPane(players[0]);
        container = new BorderPane();
        player1 = new Label(Integer.toString(players[1].cardsInHand()));
        player2 = new Label(Integer.toString(players[2].cardsInHand()));
        player3 = new Label(Integer.toString(players[3].cardsInHand()));
        colorText = new Label(color);
        holder1 = new VBox();
        holder2 = new VBox();
        holder3 = new VBox();


        playButton.setOnAction(arg0 -> action());

		playButton.setLayoutX(WIDTH/2);
		playButton.setLayoutY(50);

		holder1.setAlignment(Pos.CENTER);
        holder2.setAlignment(Pos.CENTER);
        holder3.setAlignment(Pos.CENTER);
        holder1.getChildren().add(player1);
        holder2.getChildren().addAll(colorText, player2);
        holder3.getChildren().add(player3);


		pane.changeCard(topCard());
		pane.setBottom(hand1);
		hand1.relocate(0, 300);
		//pane.setCenter(playButton);
		pane.setLeft(holder1);
		pane.setTop(holder2);
		pane.setRight(holder3);

		title = new BorderPane();
		title.setCenter(new Label("UNO"));
		title.setOnMouseClicked(e-> {
		    container.setCenter(pane);
        });


        container.setCenter(title);
        Scene scene = new Scene(container, WIDTH, HEIGHT);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	class CardPane extends BorderPane {
		public CardPane(Card card) {
			changeCard(card);
		}

		public void changeCard(Card card) {
			Image img = new Image("images/" + card.getColor() + card.getValue() + ".png");

			/*BackgroundImage bgImg = new BackgroundImage(img,
					BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
					BackgroundPosition.CENTER, 
					new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));
			setBackground(new Background(bgImg));*/
            ImageView iv = new ImageView();
            iv.setImage(img);
            iv.setFitWidth(WIDTH/3);
            iv.setFitHeight(HEIGHT/2);
            iv.setPreserveRatio(true);
            iv.setOnMouseClicked(e->action());
            setCenter(iv);
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
				iv.setFitWidth(WIDTH/15.0);
				iv.setFitHeight(150);
				iv.setPreserveRatio(true);
				int loc = i;
				iv.setOnMouseClicked(arg0 -> {
					// TODO Auto-generated method stub
					if(currentPlayer == 0) {
						if(players[currentPlayer].isLegalMove(topCard(), color, loc)) {
							turn(players[currentPlayer].playCard(loc));
							hand1.addCards(players[0]);
							pane.changeCard(topCard());
							if(winner > -1) {
								System.exit(0);
							}
						}

					}

				});
				getChildren().add(iv);
			}
		}
	}

	public void action() {
        System.out.println(pile);
        if(currentPlayer != 0) {
            turn();
            pane.changeCard(topCard());
            if(winner > -1) {
                System.exit(0);
            }
        }

        else {
            drawToPlay();
            hand1.addCards(players[0]);
        }
    }

}
