package uno;
import java.util.ArrayList;

/**
 * This class creates a Card object which can be compared to other cards and can be checked to see
 * if it fits in certain types.
 */
public class Card implements Comparable {
    private String color;
    private String value;

    /**
     * Creates a new card of a given color and value.
     *
     * @param color Color for the card.
     * @param value Value for the card.
     */
    public Card(String color, String value) {
        this.color = color;
        this.value = value;
    }

    /**
     * Returns the color of the card.
     * @return The color of the card.
     */
    public String getColor() {
        return color;
    }

    /**
     * Returns the value of the card.
     * @return The value of the card.
     */
    public String getValue() {
        return value;
    }

    /**
     * Checks to see if the value of the card is a number.
     * @return true if the value of the card is a number; otherwise, false.
     */
    private boolean isNumber() {
        return !isAction() && !color.equals("wild");
    }

    /**
     * Checks to see if the value of the card is an action.
     * @return true if the value of the card is an action; otherwise, false.
     */
    public boolean isAction() {
        return value.equals("d2") || value.equals("d4") || value.equals("rev") || value.equals("skip");
    }

    /**
     * Checks to see if the card could be played on top of the given card with the current active color.
     * @param c Card to check value against.
     * @param col Active color to check card's color against.
     * @return true if this card can be played on top of the given card with the current active color; otherwise, false.
     */
    public boolean canPlay(Card c, String col) {
        return col.equals(color) || c.getValue().equals(value) || isWild();
    }

    /**
     * Checks to see if the color or the value of the card is wild.
     * @return true if the value or color of the card is wild.
     */
    public boolean isWild() {
        return color.equals("wild") || value.equals("d4");
    }

    /**
     * Compares this card to another card based on their values
     * @param o Object to be compared to.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(Object o) {
        ArrayList<String> order = new ArrayList<>();
        order.add("d4");
        order.add("d2");
        order.add("wild");
        order.add("rev");
        order.add("skip");
        if(isAction() || getValue().equals("wild")){
            if(((Card)o).isNumber() || order.indexOf(getValue()) >= order.indexOf(((Card)o).getValue()))
                return 1;
            else
                return -1;
        } else {
            if(((Card)o).isNumber() && isNumber() && Integer.parseInt(((Card)o).getValue()) > Integer.parseInt(getValue()))
                return 1;
            else
                return -1;
        }
    }
}
