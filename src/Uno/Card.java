package Uno;

public class Card {
	private String color;
	private String value;
	
	public Card(String color, String value) {
		this.color = color;
		this.value = value;
	}
	
	public String getColor() {
		return color;
	}
	
	public String getValue() {
		return value;
	}
	
	public boolean isNumber() {
		return !isAction();
	}
	
	public boolean isAction() {
		return value.equals("d2") || value.equals("d4") || value.equals("rev") || value.equals("skip");
	}
	
	public boolean canPlay(Card c, String col) {
		return  col.equals(color) || c.getValue().equals(value) || isWild();
	}
	
	public boolean isWild() {
		return color.equals("wild");
	}
	
	public String toString() {
		return color + " " + value;
	}
}
