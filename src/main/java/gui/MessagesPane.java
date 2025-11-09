/*
 * Created on 10.01.2005
 */
package gui;

import java.awt.Dimension;

import javax.swing.*;
import javax.swing.text.BadLocationException;

/**
 * 
 * Klasse erzeugt eine Art Output-Stream für Meldungen der Auto- bzw. Tankstellenagenten
 * 
 * @author Carmen Heger
 */
public class MessagesPane extends JPanel {
    
    static final JTextArea trace = new JTextArea("");
    
    public static MessagesPane instance = null;

	JScrollPane scrollPane = new JScrollPane();
	
	public static MessagesPane getInstance(){
        if (instance==null) { instance = new MessagesPane(); }
        return instance;
    }

	public static void print(String s){
        trace.append(s);
        try{
		    trace.scrollRectToVisible(trace.modelToView(trace.getDocument().getLength()));
		} catch (BadLocationException be) {}
    }

  	/**
  	 * Methode, die in das Textfeld schreibt
  	 * @param s - Text, der geschrieben werden soll
  	 */
	public static void println(String s){
    	trace.append(s+"\n");
        try{
		    trace.scrollRectToVisible(trace.modelToView(trace.getDocument().getLength()));
		} catch (BadLocationException be) {}
	}

    /**
     * erzeugt neues MessagePane
     *
     */
  	public MessagesPane(){
        
        trace.setCaretPosition(trace.getDocument().getLength());
        
        add(new JLabel("Nachrichten:"));

        scrollPane.setViewportView(trace);
        scrollPane.setPreferredSize(new Dimension(600,160));
        add(scrollPane);
        
    }

}
