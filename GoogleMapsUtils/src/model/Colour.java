package model;

public enum Colour {

	BLU, CYAN, RED, MAGENTA, GREEN, YELLOW;

	public static String getColor(int i) {
		switch (i) {
		case 1:
			return BLU.toString();
		case 2:
			return CYAN.toString();
		case 3:
			return GREEN.toString();
		case 4:
			return MAGENTA.toString();
		case 5:
			return RED.toString();
		default:
			return YELLOW.toString();
		}
	}

}
