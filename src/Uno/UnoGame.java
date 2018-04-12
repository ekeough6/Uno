package Uno;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class UnoGame extends Application {

    private final int WIDTH = 1000;
    private final int HEIGHT = 800;
    private Deck deck;
    private ArrayList<Card> pile;
    private Player[] players;
    private String color;
    private boolean clockwise;
    private int currentPlayer, winner;
    private CardPilePane pane;
    private HandPane hand1;
    private BorderPane title, container;
    private FireworkPane winScreen;
    private Label player1, player2, player3, colorText, winText, bachmair, bender, fodor;
    private VBox holder1, holder2, holder3, winnerHolder;
    private boolean canPlay;
    private Timeline autoTurns, celebrate;


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
        autoTurns.play();
        celebrate.stop();
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
        pane = new CardPilePane(topCard());
        container = new BorderPane();
        hand1 = new HandPane(players[0]);
        player1 = new Label(Integer.toString(players[1].cardsInHand()));
        player1.setFont(Font.font("arial", 35));
        player2 = new Label(Integer.toString(players[2].cardsInHand()));
        player2.setFont(Font.font("arial", 35));
        player3 = new Label(Integer.toString(players[3].cardsInHand()));
        player3.setFont(Font.font("arial", 35));
        bender = new Label("Bender:");
        bender.setFont(Font.font("arial", 35));
        bachmair = new Label("Bachmair:");
        bachmair.setFont(Font.font("arial", 35));
        fodor = new Label("Fodor:");
        fodor.setFont(Font.font("arial", 35));
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
        holder1.getChildren().addAll(bachmair, player1);
        holder2.getChildren().addAll(colorText, bender, player2);
        holder3.getChildren().addAll(fodor, player3);

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
        title.setOnMouseClicked(event -> container.setCenter(pane));

        winText = new Label();
        winText.setFont(Font.font("arial", 100));
        winText.setAlignment(Pos.CENTER);
        winnerHolder = new VBox();
        winnerHolder.getChildren().add(new Label("Click to play again"));
        winnerHolder.setAlignment(Pos.CENTER);
        winScreen = new FireworkPane();
        winScreen.setBackground(new Background(new BackgroundFill(Color.color(1, 1, 1, 1), CornerRadii.EMPTY, Insets.EMPTY)));
        winScreen.setCenter(winText);
        winScreen.setBottom(winnerHolder);
        winScreen.setOnMouseClicked(event -> startGame());


        container.setCenter(title);
        Scene scene = new Scene(container, WIDTH, HEIGHT);

        //creates timer to make the computers play on their own
        autoTurns = new Timeline(new KeyFrame(Duration.seconds(2), event -> takeTurn()));
        autoTurns.setCycleCount(Timeline.INDEFINITE);
        autoTurns.play();

        celebrate = new Timeline((new KeyFrame(Duration.seconds(3), event -> winScreen.start())));
        celebrate.setCycleCount(Timeline.INDEFINITE);

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
        refillDeck();
        drawToPlay();
        pile.add(players[currentPlayer].playCard(topCard(), color));
        color = topCard().getColor();
        performAction();
        canPlay = true;
        if (players[currentPlayer].cardsInHand() == 0)
            winner = currentPlayer;
        nextPlayer();
        hand1.addCards(players[0]);
        updateText();
        if (currentPlayer == 0) {
            autoTurns.stop();
        }
    }

    //only used for human player
    private void turn(Card card) {
        refillDeck();
        pile.add(card);
        color = topCard().getColor();
        canPlay = true;
        performAction();
        if (players[currentPlayer].cardsInHand() == 0)
            winner = currentPlayer;
        nextPlayer();
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
            case (1):
                player1.setTextFill(Color.color(1, 1, 0));
                player2.setTextFill(Color.color(0, 0, 0));
                player3.setTextFill(Color.color(0, 0, 0));
                bachmair.setTextFill(Color.color(1, 1, 0));
                bender.setTextFill(Color.color(0, 0, 0));
                fodor.setTextFill(Color.color(0, 0, 0));
                break;
            case (2):
                player2.setTextFill(Color.color(1, 1, 0));
                player1.setTextFill(Color.color(0, 0, 0));
                player3.setTextFill(Color.color(0, 0, 0));
                bender.setTextFill(Color.color(1, 1, 0));
                bachmair.setTextFill(Color.color(0, 0, 0));
                fodor.setTextFill(Color.color(0, 0, 0));
                break;
            case (3):
                player3.setTextFill(Color.color(1, 1, 0));
                player2.setTextFill(Color.color(0, 0, 0));
                player1.setTextFill(Color.color(0, 0, 0));
                fodor.setTextFill(Color.color(1, 1, 0));
                bender.setTextFill(Color.color(0, 0, 0));
                bachmair.setTextFill(Color.color(0, 0, 0));
                break;
            default:
                player1.setTextFill(Color.color(0, 0, 0));
                player2.setTextFill(Color.color(0, 0, 0));
                player3.setTextFill(Color.color(0, 0, 0));
                bachmair.setTextFill(Color.color(0, 0, 0));
                bender.setTextFill(Color.color(0, 0, 0));
                fodor.setTextFill(Color.color(0, 0, 0));
        }

    }

    private void performAction() {
        Card top = topCard();

        if (top.isWild()) {

            if (currentPlayer == 0) {
                pane.pickColor();
            } else {
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
        if (currentPlayer != 0 && canPlay) {
            turn();
            pane.changeCard(topCard());
            canPlay = true;
            checkWinCondition();
        }

    }

    private void checkWinCondition() {
        //winner = 1;
        if (winner > -1) {
            container.setCenter(winScreen);
            switch (winner) {
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
            autoTurns.stop();
            celebrate.playFrom(Duration.seconds(2.5));
        }
    }

    class CardPilePane extends BorderPane {
        private ImageView pileView, deckView;
        private HBox views, colorPicker;
        private Pane red, blue, green, yellow;

        public CardPilePane(Card card) {
            pileView = new ImageView();
            deckView = new ImageView(new Image("images/back.png"));

            deckView.fitHeightProperty().bind(heightProperty().divide(2));
            deckView.setPreserveRatio(true);
            deckView.setOnMouseClicked(event -> drawFromDeck());
            deckView.setOnMouseEntered(event -> {
                setCursor(Cursor.HAND);
            });

            changeCard(card);

            colorPicker = new HBox();
            colorPicker.setAlignment(Pos.CENTER);

            red = new Pane();
            DoubleProperty paneWidth = new SimpleDoubleProperty(WIDTH / 6);
            red.setMinWidth(paneWidth.doubleValue());
            red.prefWidthProperty().bind(widthProperty().divide(6));
            red.setBackground(new Background(new BackgroundFill(Color.color(1, 0, 0), CornerRadii.EMPTY, Insets.EMPTY)));
            red.setOnMouseClicked(event -> {
                color = "red";
                setCenter(views);
                updateText();
                canPlay = true;
            });
            blue = new Pane();
            blue.setMinWidth(paneWidth.doubleValue());
            blue.prefWidthProperty().bind(widthProperty().divide(6));
            blue.setBackground(new Background(new BackgroundFill(Color.color(0, 0, 1), CornerRadii.EMPTY, Insets.EMPTY)));
            blue.setOnMouseClicked(event -> {
                color = "blue";
                setCenter(views);
                updateText();
                canPlay = true;
            });
            green = new Pane();
            green.setMinWidth(paneWidth.doubleValue());
            green.prefWidthProperty().bind(widthProperty().divide(6));
            green.setBackground(new Background(new BackgroundFill(Color.color(0, 1, 0), CornerRadii.EMPTY, Insets.EMPTY)));
            green.setOnMouseClicked(event -> {
                color = "green";
                setCenter(views);
                updateText();
                canPlay = true;
            });
            yellow = new Pane();
            yellow.setMinWidth(paneWidth.doubleValue());
            yellow.prefWidthProperty().bind(widthProperty().divide(6));
            yellow.setBackground(new Background(new BackgroundFill(Color.color(1, 1, 0), CornerRadii.EMPTY, Insets.EMPTY)));
            yellow.setOnMouseClicked(event -> {
                color = "yellow";
                setCenter(views);
                updateText();
                canPlay = true;
            });


            colorPicker.getChildren().addAll(red, blue, green, yellow);

            views = new HBox();
            SimpleDoubleProperty space = new SimpleDoubleProperty(getWidth() / 10);
            views.setSpacing(space.doubleValue());
            views.setAlignment(Pos.CENTER);
            views.getChildren().addAll(pileView, deckView);
            setCenter(views);

        }

        public void changeCard(Card card) {
            Image img = new Image("images/" + card.getColor() + card.getValue() + ".png");
            pileView.setImage(img);
            pileView.fitHeightProperty().bind(heightProperty().divide(2));
            pileView.setPreserveRatio(true);
        }

        public void pickColor() {
            setCenter(colorPicker);
            updateText();
            canPlay = false;
        }

        public boolean onCard(double x, double y) {
            Bounds boundsInScene = pileView.localToScene(pileView.getBoundsInLocal());
            return y > boundsInScene.getMinY() && y < boundsInScene.getMaxY() && x > boundsInScene.getMinX() && x < boundsInScene.getMaxX();
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
                CardView iv = new CardView(new Image("images/" + card.getColor() + card.getValue() + ".png"), i);
                getChildren().add(iv);
            }
        }
    }

    class CardView extends ImageView {
        private double dragDeltaX, dragDeltaY, lastX, lastY;
        private ImageView iv;

        public CardView(Image img, int loc) {
            super(img);
            iv = new ImageView(getImage());
            fitHeightProperty().bind(container.heightProperty().divide(7));
            setPreserveRatio(true);
            iv.fitHeightProperty().bind(container.heightProperty().divide(7));
            iv.setPreserveRatio(true);

            setOnMousePressed(event -> {
                // record a delta distance for the drag and drop operation.
                if (currentPlayer == 0) {
                    iv.setLayoutY(event.getSceneY() - iv.getFitHeight() / 2);
                    iv.setLayoutX(event.getSceneX() - iv.getFitHeight() / 3);
                    dragDeltaX = iv.getLayoutX() - event.getSceneX();
                    dragDeltaY = iv.getLayoutY() - event.getSceneY();
                    setVisible(false);
                    container.getChildren().add(iv);
                    setCursor(Cursor.MOVE);
                }
            });
            setOnMouseReleased(event -> {
                setVisible(true);
                setCursor(Cursor.HAND);

                if (currentPlayer == 0) {
                    if (players[currentPlayer].isLegalMove(topCard(), color, loc) && pane.onCard(lastX, lastY)) {
                        turn(players[currentPlayer].playCard(loc));
                        hand1.addCards(players[0]);
                        pane.changeCard(topCard());
                        checkWinCondition();
                    }
                }
                container.getChildren().remove(iv);


            });
            setOnMouseDragged(event -> {
                lastX = event.getSceneX();
                lastY = event.getSceneY();
                iv.setLayoutX(event.getSceneX() + dragDeltaX);
                iv.setLayoutY(event.getSceneY() + dragDeltaY);
            });
            setOnMouseEntered(event -> {
                setCursor(Cursor.HAND);
            });
        }

    }

    class FireworkPane extends BorderPane {
        private final int MAX_PARTICLES = 50;
        private Circle upFirework;
        private PathTransition up;
        private Circle[] particles;

        public FireworkPane() {
            upFirework = new Circle(20, 20, 20);
            upFirework.radiusProperty().bind(widthProperty().divide(40));
            upFirework.setFill(Color.LIGHTBLUE);
            upFirework.setOpacity(0);
            getChildren().add(upFirework);
        }

        public void start() {
            int x = (int) (Math.random() * getWidth());
            int y = (int) (Math.random() * getHeight() / 1.5);
            particles = new Circle[MAX_PARTICLES];
            PathTransition[] particleArcs = new PathTransition[particles.length];

            SequentialTransition[] particleTransitions = new SequentialTransition[particles.length];
            for (int i = 0; i < particles.length; i++) {
                particles[i] = new Circle(x, y, 10, Color.color(Math.random(), Math.random(), Math.random()));
                particles[i].setOpacity(0);
                getChildren().add(particles[i]);
                particleArcs[i] = new PathTransition(Duration.millis(2000), new Line(x, y, x + (Math.random() * getWidth() / 2 - getWidth() / 4), y + (Math.random() * getHeight() / 2 - getHeight() / 4)), particles[i]);
                particleArcs[i].setCycleCount(1);
                particleArcs[i].setInterpolator(Interpolator.LINEAR);
                FadeTransition particleFadesIn = new FadeTransition(Duration.millis(20), particles[i]);
                particleFadesIn.setFromValue(0);
                particleFadesIn.setToValue(1);
                particleFadesIn.setCycleCount(1);
                FadeTransition particleFadesOut = new FadeTransition(Duration.millis(1000), particles[i]);
                particleFadesOut.setFromValue(1);
                particleFadesOut.setToValue(0);
                particleFadesOut.setCycleCount(1);
                ParallelTransition fall = new ParallelTransition(particleArcs[i], particleFadesOut);
                particleTransitions[i] = new SequentialTransition(particleFadesIn, fall);
            }
            FadeTransition fade = new FadeTransition(Duration.millis(50), upFirework);
            fade.setFromValue(1);
            fade.setToValue(0);
            fade.setCycleCount(1);
            up = new PathTransition(Duration.millis(1000), new Line(x, getHeight(), x, y), upFirework);
            up.setCycleCount(1);

            ParallelTransition falling = new ParallelTransition();
            for (SequentialTransition p : particleTransitions) {
                falling.getChildren().add(p);
            }

            SequentialTransition upwards = new SequentialTransition(up, fade);
            SequentialTransition fireworks = new SequentialTransition(upwards, falling);
            fireworks.play();
            System.out.println(getChildren());

        }
    }


}
