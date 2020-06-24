/*
 *	Paquete				: gui
 *	Archivo				: Acerca.java
 *	Version				: 1.0.0		2007-07-12
 *
 *	Todos los derechos reservados.  Usar bajo terminos de la licencia.
 * 
 *	Ver archivo "licencia.txt" para informacion de como usar y
 *	distrubuir este archvio, y para garantizar las responsabilidades 
 *	adquiridas.
 */
package com.avpsoft.icvoz.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Acerca extends JPanel {

    public Acerca() {

        super(new GridLayout(1, 1));

        JTabbedPane tabbedPane = new JTabbedPane();

        JComponent panel1 = autores();
        tabbedPane.addTab("Autores", panel1);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JComponent panel2 = licencia();
        tabbedPane.addTab("Copyright", panel2);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        /*JComponent panel3 = makeTextPanel("Ninguno");
        tabbedPane.addTab("Colaboradores",  panel3);
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);*/
        JComponent panel4 = systema();
        tabbedPane.addTab("Sistema", panel4);
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

        /*JComponent panel4 = makeTextPanel("Panel #4 (has a preferred size of 410 x 50).");
        panel4.setPreferredSize(new Dimension(410, 50));
        tabbedPane.addTab("Tab 4", panel4);
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);*/
        //Add the tabbed pane to this panel.
        add(tabbedPane);

        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        //     tabbedPane.setBackground(new Color(0xdb,0xba,0x75));
//        tabbedPane.
        tabbedPane.setPreferredSize(new Dimension(400, 200));

    }

    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }

    protected JComponent autores() {
        JScrollPane panel = new JScrollPane();
        JLabel filler = new JLabel("<html>"
                + "<head>"
                + "<title>Documento sin t&iacute;tulo</title>"
                + "</head>"
                + "<body>"
                + "<p>&nbsp;</p>"
                + "<hr>"
                + "Andres Velez <br>"
                + "<p></p>"
                + "<hr>"
                + "<p>&nbsp;</p>"
                + "</body>"
                + "</html>");
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.getViewport().add(filler);
        return panel;
    }

    protected JComponent licencia() {
        JScrollPane panel = new JScrollPane();
        JLabel filler = new JLabel(
                "<html>"
                + "<head>"
                + "<title>Documento sin t&iacute;tulo</title>"
                + "</head>"
                + "<body>"
                + "<p>Tesis:</p>"
                + "<p>Sistema de Reconocimiento de Comandos de Voz en Español</p>"
                + "<p>Aplicación</p>"
                + "<p>Interpretador de Comandos de Voz (Icvoz)</p>"
                + "<p>Copyrigth &copy; 2006 - 2020</p>"
                + "<p>Andres Velez </p>"
                + "<p>&nbsp;</p>"
                + "<p>&nbsp;</p>"
                + "</body>"
                + "</html>");
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.getViewport().add(filler);
        return panel;
    }

    protected JComponent systema() {
        JScrollPane panel = new JScrollPane();
        JLabel filler = new JLabel(
                "<html>"
                + "<head>"
                + "<title>Documento sin t&iacute;tulo</title>"
                + "</head>"
                + "<body>"
                + "JRE Vendor Name : " + System.getProperty("java.vendor") + "<br>"
                + "Java Version : " + System.getProperty("java.version") + "<br>"
                + "Java Home : " + System.getProperty("java.home") + "<br>"
                + "Operating system name : " + System.getProperty("os.name") + "<br>"
                + "Operating system version : " + System.getProperty("os.version") + "<br>"
                + "Operating system architecture : " + System.getProperty("os.arch") + "<br>"
                + "IcVoz Version : " + System.getProperty("icvoz.version") + "<br>"
                + "IcVoz Audio Format : " + System.getProperty("icvoz.audio.format")
                + "</body>"
                + "</html>");
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.getViewport().add(filler);
        return panel;
    }
}
