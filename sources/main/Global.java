package main;

import agents.CarAgent;

/*
 * Created on 27.12.2004
 */

/**
 * Diese Klasse kapselt globale Bezeichner und Methoden.
 * 
 * @author Denis Stein, Philipp B&ouml;nisch 
 */
public class Global {

	/**
	 * Conversation ID, um eine Preisanfrage zu stellen
	 */
	public final static String ID_PRICE_REQ = "Preisanfrage";
	
	/**
	 * Conversation ID f&uuml;r ein Preisangebot
	 */
	public static final String ID_PRICE_ANSW = "Preisangebot";
	
	/**
	 * Conversation ID f&uuml;r die Anfrage zum Tanken
	 */
	public static final String ID_FUEL_REQ = "Tankanfrage";
	
	/**
	 * Conversation ID um eine Tankanfrage zu beantworten
	 */
	public static final String ID_FUEL_ANSW = "TankOK";
	
	/**
	 * Namen des angebotenen Dienstes der Tankstellen
	 */
	public static final String SERVICE = "PetrolStation";
	
	/**
	 * Wandelt int-Koordinate in String um.
	 * 
	 * @param x x-Koordinate
	 * @param y y-Koordinate
	 * @return Koordinate als String der Form x-y
	 */
	public static String coordinatesToString(int x, int y) {
		return Integer.toString(x) + "-" + Integer.toString(y);
	}
	
	/**
	 * Berechnet Distanz zwischen übergebener Koordinate als String und 
	 * aktueller Position des Autos.
	 * 
	 * @param s Koordinaten
	 * @return Distanz
	 */
	public static int distance(String s, CarAgent myAgent) {
		int diffX = ((CarAgent) myAgent).getActualXPosition() - 
			xCoordinateFromString(s);
		if (diffX < 0) diffX = -diffX;
		int diffY = ((CarAgent) myAgent).getActualYPosition() - 
			yCoordinateFromString(s);
		if (diffY < 0) diffY = -diffY;		
		return diffX + diffY;
	}
	
	/**
	 * Liefert x-Koordinate aus String der Form x-y
	 * @param s String der Form x-y
	 * @return x-Koordinate
	 */
	public static int xCoordinateFromString(String s) {
		int middle = s.indexOf('-');
		return Integer.parseInt(s.substring(0, middle));		
	}
	
	/**
	 * Liefert y-Koordinate aus String der Form x-y
	 * @param s String der Form x-y
	 * @return y-Koordinate
	 */
	public static int yCoordinateFromString(String s) {
		int middle = s.indexOf('-');
		return Integer.parseInt(s.substring(middle + 1, s.length()));		
	}
	
	/**
	 * Wandelt den &uuml;bergebenen Wert in einen String mit 2 Nachkommastellen und mit deutscher
	 * Kommaschreibweise um.
	 * 
	 * @param arg Wert, der in einen String gewandelt werden soll
	 * @return String mit 2 Nachkommastellen und deutscher Kommaschreibweise
	 */
	public static final String doubleToString(double arg)
	{
		String ret = String.valueOf(Global.doubleToDouble(arg));
		
		int pos = ret.indexOf(".");
		
		String after = ret.substring(pos+1, ret.length());
		
		if (after.length() < 2)
			after += "0";
		
		return ret.substring(0, pos) + "," + after; 
	}
	
	/**
	 * Wandelt den Wert in einen Wert mit maximal 2 Nachkommastellen um.
	 * 
	 * @param arg Wert, der gewandelt werden soll
	 * @return Wert mit maximal 2 Nachkommastellen
	 */
	public static final double doubleToDouble(double arg)
	{
		String help = String.valueOf(arg);
		
		int pos = help.indexOf(".");
		
		String after = help.substring(pos+1, help.length());
		
		if (after.length() > 2)
			after = after.substring(0, 2);
		
		return Double.parseDouble(help.substring(0, pos) + "." + after);
	}
}