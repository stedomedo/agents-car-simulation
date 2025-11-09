package gui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

/*
 * Created on 27.12.2004
 */

/**
 * Diese Klasse erzeugt den Hauptframe der Anwendung
 * 
 * @author Carmen Heger
 */
public class MainFrame extends JFrame implements ActionListener {

    public static MainFrame instance;

    private JSplitPane hSplit;

    private JSplitPane vSplit;
    
   public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
        }
        return instance;
    }

   /**
    * erzeugt neuen MainFrame
    */
   public MainFrame() {
        WindowListener wl = new WindowClosingAdapter(true);
        addWindowListener(wl);
        setSize(600, 600);
        setTitle("Agenten - Hauptframe");
        Image img = getToolkit().getImage(
                ClassLoader.getSystemResource("gui/pics/lupe.gif"));
        setIconImage(img);

        JMenuBar menubar = new JMenuBar();
        menubar.add(createFileMenu());
        menubar.add(createHelpMenu());
        setJMenuBar(menubar);

        JPanel frame1 = InfoPane.getInstance();
        JPanel frame2 = MapPane.getInstance();
        JPanel frame3 = MessagesPane.getInstance();
        frame1.setPreferredSize(new Dimension(200, 400));
        frame2.setPreferredSize(new Dimension(400,400));
        frame3.setPreferredSize(new Dimension(600,200));

        JSplitPane hSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, frame1,
                frame2);
        hSplit.setPreferredSize(new Dimension(600, 400));
        frame1.setPreferredSize(new Dimension(200, 400));
        hSplit.setOneTouchExpandable(true);
        JSplitPane vSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, hSplit,
                frame3);
        vSplit.setOneTouchExpandable(true);
        getContentPane().add(vSplit);

        pack();
        setVisible(true);
    }

   /**
    * erzeugt erstes Menü - Programm
    * @return Menü
    */
    private JMenu createFileMenu() {
        JMenu ret = new JMenu("Programm");
        JMenuItem mi;
        mi = new JMenuItem("Beenden", 'c');
        mi.addActionListener(this);
        ret.add(mi);
        return ret;
    }

    /**
     * erzeugt zweites Menü - Info
     * @return Menü
     */
    private JMenu createHelpMenu() {
        JMenu ret = new JMenu("Info");
        JMenuItem mi;
        mi = new JMenuItem("Info", 'i');
        mi.addActionListener(this);
        ret.add(mi);
        return ret;
    }

    
    /**
     * gibt an, was bei bestimmten Aktionen ausgeführt wird
     */
    public void actionPerformed(ActionEvent event) {
        if (event.getActionCommand().equals("Beenden")) {
            System.exit(0);
        }
        if (event.getActionCommand().equals("Info")) {
            JOptionPane
                    .showConfirmDialog(
                            getInstance(),
                            "Programm entstand im Rahmen der Vorlesung \nAgententechnologie an der TU Dresden,\nFakultät Informatik.\nCopyright 2004, 2005:\nPhilipp Bönisch, Carmen Heger, Denis Stein",
                            "Agenten", JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE);
        }

    }
}