package com.avpsoft.icvoz.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame {

    private JPanel control;
    private JButton play, stop;
    public static JTabbedPane tabbedPane = null;
    //private Inicio inicio;

    public GUI() {
        super("Reconocimiento de comandos de Voz");

        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        /*play = new JButton("Play");
		play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                PlayActionPerformed(evt);
            }
        });
        
        stop = new JButton("Stop");
		stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                StopActionPerformed(evt);
            }
        });
        
 
        control = new JPanel(new FlowLayout(FlowLayout.CENTER));
        control.add(play);
        control.add(stop);
        
		getContentPane().add(control,BorderLayout.SOUTH);
         */
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 726) / 2, (screenSize.height - 517) / 2, 726, 517);

        //this.inicio=new Inicio();
    }

    public static void addGrafica(String titulo, JPanel panel) {
        if (tabbedPane != null) {
            tabbedPane.addTab(titulo, panel);
        } else {
            System.out.println("Error: GUI.addGrafica ");
        }

    }

    public static void removeGrafica(JPanel panel) {
        if (tabbedPane != null) {
            tabbedPane.remove(panel);
        } else {
            System.out.println("Error: GUI.removeGrafica ");
        }
    }

    private void PlayActionPerformed(ActionEvent evt) {
        //inicio.start();		
        play.setEnabled(false);
    }

    private void StopActionPerformed(ActionEvent evt) {
        play.setEnabled(true);
        //inicio.stop();	
    }

    public static void makeGUI() {
        GUI gui = new GUI();
        gui.setVisible(true);
    }

}
