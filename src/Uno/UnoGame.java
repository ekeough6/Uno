package Uno;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Duration;

public class UnoGame extends Application {

    private Deck deck;
    private ArrayList<Card> pile;
    private Player[] players;
    private String color;
    private boolean clockwise;
    private int currentPlayer, winner;
    private Button playButton;
    private CardPane pane;
    private HandPane hand1;
    private BorderPane title, winScreen, container;
    private Label player1, player2, player3, colorText, winText;
    private VBox holder1, holder2, holder3, winnerHolder;
    private boolean canPlay;
    private Timeline autoTurns;


    private final int WIDTH = 1000;
    private final int HEIGHT = 800;


    public UnoGame() {
        newGame();
    }

    private void newGame() {
        deck = new Deck();
        deck.shuffle();
        pile = new ArrayList<>();
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
        while (color.equals("wild")) {
            pile.add(deck.drawCard());
            color = topCard().getColor();
        }
        currentPlayer = 0;
        clockwise = true;
    }


    public void startGame() {
        newGame();
        initGame();
        container.setCenter(pane);
        updateText();
        hand1.addCards(players[0]);
        pane.changeCard(topCard());
    }

    public void begin() {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) {
        //TODO Auto-generated method stub
        initGame();
        primaryStage.setTitle("Uno");

        //initialize all of the panes containing any information
        pane = new CardPane(topCard());
        hand1 = new HandPane(players[0]);
        container = new BorderPane();
        player1 = new Label(Integer.toString(players[1].cardsInHand()));
        player1.setFont(Font.font("arial", 35));
        player2 = new Label(Integer.toString(players[2].cardsInHand()));
        player2.setFont(Font.font("arial", 35));
        player3 = new Label(Integer.toString(players[3].cardsInHand()));
        player3.setFont(Font.font("arial", 35));
        colorText = new Label(color);
        colorText.setFont(Font.font("arial", 35));
        colorText.setStyle("-fx-stroke: black; -fx-stroke-width: 1;");
        updateText();
        holder1 = new VBox();
        holder2 = new VBox();
        holder3 = new VBox();

        //initialize panes to hold labels
        holder1.setAlignment(Pos.CENTER);
        holder2.setAlignment(Pos.CENTER);
        holder3.setAlignment(Pos.CENTER);
        holder1.getChildren().add(player1);
        holder2.getChildren().addAll(colorText, player2);
        holder3.getChildren().add(player3);

        //position panes for the game screen
        pane.changeCard(topCard());
        pane.setBottom(hand1);
        hand1.relocate(0, 300);
        pane.setLeft(holder1);
        pane.setTop(holder2);
        pane.setRight(holder3);
        pane.setBackground(new Background(new BackgroundFill(Color.color(.73, .38, .90, .96), CornerRadii.EMPTY, Insets.EMPTY)));

        //initialize title screen
        title = new BorderPane();
        title.setBackground(new Background(new BackgroundFill(Color.color(1, 1, 1, 1), CornerRadii.EMPTY, Insets.EMPTY)));
        Label unoLabel = new Label("UNO");
        unoLabel.setFont(Font.font("arial", 145));
        title.setCenter(unoLabel);
        title.setOnMouseClicked(e -> container.setCenter(pane));

        //replay button
        playButton = new Button("Play Again");
        playButton.setOnAction(arg0 -> startGame());
        playButton.setAlignment(Pos.CENTER);

        winText = new Label();
        winText.setFont(Font.font("arial", 100));
        winText.setAlignment(Pos.CENTER);
        winnerHolder = new VBox();
        winnerHolder.getChildren().add(playButton);
        winnerHolder.setAlignment(Pos.CENTER);
        winScreen = new BorderPane();
        winScreen.setBackground(new Background(new BackgroundFill(Color.color(1, 1, 1, 1), CornerRadii.EMPTY, Insets.EMPTY)));
        winScreen.setCenter(winText);
        winScreen.setBottom(winnerHolder);


        container.setCenter(title);
        Scene scene = new Scene(container, WIDTH, HEIGHT);

        //creates timer to make the computers play on their own
        autoTurns = new Timeline(new KeyFrame(Duration.seconds(2), e -> takeTurn()));
        autoTurns.setCycleCount(Timeline.INDEFINITE);
        autoTurns.play();
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    //draws a card from the deck into the current player's hand
    private void draw() {
        refillDeck();
        players[currentPlayer].draw(deck);
    }

    //draws cards until the player has a card they can play
    private void drawToPlay() {
        while (!players[currentPlayer].hasLegalMove(topCard(), color)) {
            draw();
        }

    }

    //draws a single card into the human player's hand
    public void drawFromDeck() {
        if (!players[currentPlayer].hasLegalMove(topCard(), color)) {
            draw();
            hand1.addCards(players[0]);
        }
    }
    
    private void turn() {
        canPlay = false;
        System.out.println("It's now player " + currentPlayer + "'s turn");
        refillDeck();
        drawToPlay();
        System.out.println("Player has " + players[currentPlayer]);
        pile.add(players[currentPlayer].playCard(topCard(), color));
        color = topCard().getColor();
        performAction();
        canPlay = true;
        if (players[currentPlayer].cardsInHand() == 0)
            winner = currentPlayer;
        nextPlayer();
        System.out.println(topCard());
        hand1.addCards(players[0]);
        updateText();
        if(currentPlayer == 0) {
            autoTurns.stop();
        }
    }

    //only used for human player
    public void turn(Card card) {
        System.out.println("It's now player " + currentPlayer + "'s turn");
        refillDeck();

        System.out.println("Player has " + players[currentPlayer]);
        pile.add(card);
        color = topCard().getColor();
        canPlay = true;
        performAction();
        if (players[currentPlayer].cardsInHand() == 0)
            winner = currentPlayer;
        nextPlayer();

        System.out.println(topCard());
        hand1.addCards(players[0]);
        updateText();
        autoTurns.playFromStart();
    }

    private void updateText() {
        colorText.setText(color);
        switch (color) {
            case "red":
                colorText.setTextFill(Color.color(1, 0, 0));
                break;
            case "blue":
                colorText.setTextFill(Color.color(0, 0, 1));
                break;
            case "green":
                colorText.setTextFill(Color.color(0, 1, 0));
                break;
            case "yellow":
                colorText.setTextFill(Color.color(1, 1, 0));
                break;
            case "wild":
                colorText.setTextFill(Color.color(0, 0, 0));
                break;
        }
        player1.setText(Integer.toString(players[1].cardsInHand()));
        player2.setText(Integer.toString(players[2].cardsInHand()));
        player3.setText(Integer.toString(players[3].cardsInHand()));

        switch (currentPlayer) {
            case(1):
                player1.setTextFill(Color.color(1, 1, 0));
                player2.setTextFill(Color.color(0, 0, 0));
                player3.setTextFill(Color.color(0, 0, 0));
                break;
            case(2):
                player2.setTextFill(Color.color(1, 1, 0));
                player1.setTextFill(Color.color(0, 0, 0));
                player3.setTextFill(Color.color(0, 0, 0));
                break;
            case(3):
                player3.setTextFill(Color.color(1, 1, 0));
                player2.setTextFill(Color.color(0, 0, 0));
                player1.setTextFill(Color.color(0, 0, 0));
                break;
            default:
                player1.setTextFill(Color.color(0, 0, 0));
                player2.setTextFill(Color.color(0, 0, 0));
                player3.setTextFill(Color.color(0, 0, 0));
        }

    }

    private void performAction() {
        Card top = topCard();

        if (top.isWild()) {

            if (currentPlayer == 0) {
                pane.pickColor();
            }
            else {
                color = players[currentPlayer].chooseColor();
            }
        }

        if (top.isAction()) {
            switch (top.getValue()) {
                case "rev":
                    clockwise = !clockwise;
                    break;
                case "d2":
                    nextPlayer();
                    for (int i = 0; i < 2; i++) {
                        refillDeck();
                        players[currentPlayer].draw(deck);
                    }
                    break;
                case "d4":
                    nextPlayer();
                    for (int i = 0; i < 4; i++) {
                        refillDeck();
                        players[currentPlayer].draw(deck);
                    }
                    break;
                case "skip":
                    nextPlayer();
                    break;
            }
        }

    }

    private void refillDeck() {
        if (deck.isEmpty()) {
            Card top = pile.remove(pile.size() - 1);
            deck.refillDeck(pile);
            pile.clear();
            pile.add(top);
        }
    }

    private void nextPlayer() {
        if (clockwise)
            currentPlayer = (currentPlayer < players.length - 1) ? currentPlayer + 1 : 0;
        else
            currentPlayer = (currentPlayer > 0) ? currentPlayer - 1 : players.length - 1;
    }

    private Card topCard() {
        return pile.get(pile.size() - 1);
    }


    private void takeTurn() {
        System.out.println(canPlay);
        if (currentPlayer != 0 && canPlay) {
            turn();
            pane.changeCard(topCard());
            canPlay = true;
            checkWinCondition();
        }

    }

    private void checkWinCondition() {
        if (winner > -1) {
            container.setCenter(winScreen);
            switch (winner){
                case 0:
                    winText.setText("You win!!");
                    break;
                case 1:
                    winText.setText("The winner is \nBachmair");
                    break;
                case 2:
                    winText.setText("The winner is Bender");
                    break;
                case 3:
                    winText.setText("The winner is Fodor");
                    break;
            }
        }
    }

    class CardPane extends BorderPane {
        private ImageView pileView, deckView;
        private HBox views, colorPicker;
        private Pane red, blue, green, yellow;

        public CardPane(Card card) {
            pileView = new ImageView();
            deckView = new ImageView(new Image("images/back.png"));

            deckView.setFitWidth(WIDTH / 4);
            deckView.setFitHeight(HEIGHT / 2);
            deckView.setPreserveRatio(true);
            deckView.setOnMouseClicked(event -> drawFromDeck());

            changeCard(card);

            red = new Pane();
            DoubleProperty paneWidth = new SimpleDoubleProperty(WIDTH / 5);
            red.setMinWidth(paneWidth.doubleValue());
            red.setBackground(new Background(new BackgroundFill(Color.color(1, 0, 0), CornerRadii.EMPTY, Insets.EMPTY)));
            red.setOnMouseClicked(e -> {
                color = "red";
                setCenter(views);
                colorText.setText(color);
                canPlay = true;
            });
            blue = new Pane();
            blue.setMinWidth(paneWidth.doubleValue());
            blue.setBackground(new Background(new BackgroundFill(Color.color(0, 0, 1), CornerRadii.EMPTY, Insets.EMPTY)));
            blue.setOnMouseClicked(e -> {
                color = "blue";
                setCenter(views);
                colorText.setText(color);
                canPlay = true;
            });
            green = new Pane();
            green.setMinWidth(paneWidth.doubleValue());
            green.setBackground(new Background(new BackgroundFill(Color.color(0, 1, 0), CornerRadii.EMPTY, Insets.EMPTY)));
            green.setOnMouseClicked(e -> {
                color = "green";
                setCenter(views);
                colorText.setText(color);
                canPlay = true;
            });
            yellow = new Pane();
            yellow.setMinWidth(paneWidth.doubleValue());
            yellow.setBackground(new Background(new BackgroundFill(Color.color(1, 1, 0), CornerRadii.EMPTY, Insets.EMPTY)));
            yellow.setOnMouseClicked(e -> {
                color = "yellow";
                setCenter(views);
                colorText.setText(color);
                canPlay = true;
            });

            colorPicker = new HBox();
            colorPicker.setAlignment(Pos.CENTER);
            colorPicker.getChildren().addAll(red, blue, green, yellow);

            views = new HBox();
            views.setSpacing(WIDTH / 10);
            views.setAlignment(Pos.CENTER_RIGHT);
            views.getChildren().addAll(pileView, deckView);
            setCenter(views);

        }

        public void changeCard(Card card) {
            Image img = new Image("images/" + card.getColor() + card.getValue() + ".png");
            pileView.setImage(img);
            pileView.setFitWidth(WIDTH / 4);
            pileView.setFitHeight(HEIGHT / 2);
            pileView.setPreserveRatio(true);
        }

        public void pickColor() {
            setCenter(colorPicker);
            updateText();
            canPlay = false;
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
            for (int i = 0; i < player.cardsInHand(); i++) {
                Card card = player.viewCard(i);
                ImageView iv = new ImageView();
                iv.setImage(new Image("images/" + card.getColor() + card.getValue() + ".png"));
                iv.setFitWidth(WIDTH / 15.0);
                iv.setFitHeight(150);
                iv.setPreserveRatio(true);
                int loc = i;
                iv.setOnMouseClicked(arg0 -> {
                    // TODO Auto-generated method stub
                    if (currentPlayer == 0) {
                        if (players[currentPlayer].isLegalMove(topCard(), color, loc)) {
                            turn(players[currentPlayer].playCard(loc));
                            hand1.addCards(players[0]);
                            pane.changeCard(topCard());
                            checkWinCondition();
                        }

                    }

                });
                getChildren().add(iv);
            }
        }
    }

}
