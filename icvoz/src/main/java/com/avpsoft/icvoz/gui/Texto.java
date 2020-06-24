package com.avpsoft.icvoz.gui;

import java.awt.*;
import javax.swing.*;

public class Texto {

    private Text txt;
    private int contadorLineas = 0;
    private int maxLineas = 500;

    public Texto(final String t) {

        txt = new Text(t);
    }

    public void println(String a) {
        contadorLineas++;
        txt.print(a + "\n");
        if (contadorLineas > maxLineas) {
            txt.reset();
        }
    }

    public void print(String a) {
        txt.print(a);
    }

    public synchronized void reset() {
        txt.reset();
    }

    private class Text extends JPanel {

        private JTextArea jtxt = new JTextArea();

        public Text(String t) {

            super(new BorderLayout());

            JScrollPane js = new JScrollPane();

            js.getViewport().add(jtxt);

            add(js, BorderLayout.CENTER);

            GUI.addGrafica("Grafica " + t, this);
        }

        public void print(String s) {
            jtxt.append(s);
        }

        public synchronized void reset() {
            jtxt.setText("");
        }

        public void remove() {
            GUI.removeGrafica(this);
        }
    }

    public void remove() {
        txt.remove();
    }
}
