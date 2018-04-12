package uno;
import java.util.ArrayList;

public class Card implements Comparable {
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
	
	private boolean isNumber() {
		return !isAction() && !color.equals("wild");
	}
	
	public boolean isAction() {
		return value.equals("d2") || value.equals("d4") || value.equals("rev") || value.equals("skip");
	}
	
	public boolean canPlay(Card c, String col) {
		return col.equals(color) || c.getValue().equals(value) || isWild();
	}
	
	public boolean isWild() {
		return color.equals("wild") || value.equals("d4");
	}
	
	public String toString() {
		return color + " " + value;
	}

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
		}
		else {
			if(((Card)o).isNumber() && isNumber() && Integer.parseInt(((Card)o).getValue()) > Integer.parseInt(getValue()))
				return 1;
			else
				return -1;
		}
	}
}
