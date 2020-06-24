/*
 *	Paquete				: gui
 *	Archivo				: Grafica.java
 *	Version				: 1.0.0		2007-03-23
 *
 *	Todos los derechos reservados.  Usar bajo terminos de la licencia.
 * 
 *	Ver archivo "licencia.txt" para informacion de como usar y
 *	distrubuir este archvio, y para garantizar las responsabilidades 
 *	adquiridas.
 */
package com.avpsoft.icvoz.gui;

import java.awt.*;
import javax.swing.*;
import java.util.*;

public class Grafica extends JPanel implements Runnable {

    private Vector<Double> datos = new Vector<Double>(0, 1);
    private Vector<Double> limites = new Vector<Double>(0, 1);

    private int tiempoActualizacion = 100;

    private double LI = 0.0;
    private double LS = 0.0;

    private final String ALTO = "alto";
    private final String MEDIO = "medio";
    private final String BAJO = "bajo";

    private int ancho = 1000;//500
    private int alto = 450;

    private int EJE_X = alto / 2;

    private Thread thread;
    private String t;
    private String pos = MEDIO;

    private double maxDibujo = 200.0D;
    private double max = 1.0D;
    private double factor = maxDibujo / max;

    public Grafica(final String t, int ventana) {
        this();
        this.t = t;
        this.tiempoActualizacion = ventana / 10;
        GUI.addGrafica("Grafica " + t, this);
    }

    public Grafica() {
        super(true);
        while (ancho > datos.size()) {
            datos.add(new Double(0.0D));
        }
    }

    public void start() {
        thread = new Thread(this, "Grafica " + t);
        thread.start();
    }

    public synchronized void limite(double LI, double LS) {
        addLimite(LI);
        addLimite(LS);
    }

    public synchronized void addLimite(double l) {
        if (limites.size() >= 2) {
            limites.remove(0);
        }
        max = Math.max(l, max);
        factor = maxDibujo / max;
        limites.add(new Double(l));
    }

    public void ejeX(String pos) {
        int i = 0;
        this.pos = pos;
        if (pos == ALTO) {
            i = 1;
        } else if (pos == MEDIO) {
            i = 2;
        } else if (pos == BAJO) {
            i = 3;
        }

        switch (i) {
            case 1:
                EJE_X = alto / 4;
                break;
            case 2:
                EJE_X = alto / 2;
                break;
            case 3:
                EJE_X = alto / 4 + alto / 2;
                break;
            default:
                EJE_X = alto / 2;
        }
    }

    public synchronized void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, ancho, alto);

        g.setColor(Color.white);
        for (int i = 0; i < datos.size(); i++) {
            //g.fillOval(i,EJE_X - datos.elementAt(i).intValue(), 1,1);
            //g.drawLine(i-1, EJE_X - datos.elementAt(i-1).intValue(), i, EJE_X - datos.elementAt(i).intValue());
            g.drawLine(i, EJE_X, i, EJE_X - (int) (datos.elementAt(i).doubleValue() * factor));
        }
        g.setColor(Color.blue);
        g.drawLine(0, EJE_X, ancho, EJE_X);
        g.setColor(Color.red);
        //g.drawLine(0, (int) (EJE_X -  LI*factor), ancho, (int) (EJE_X -  LI*factor));
        //g.drawLine(0, (int) (EJE_X -  LS*factor), ancho, (int) (EJE_X -  LS*factor));
        for (int i = 0; i < limites.size(); i++) {
            //g.fillOval(i,EJE_X - datos.elementAt(i).intValue(), 1,1);
            //g.drawLine(i-1, EJE_X - datos.elementAt(i-1).intValue(), i, EJE_X - datos.elementAt(i).intValue());
            g.drawLine(0, EJE_X - (int) (limites.elementAt(i).doubleValue() * factor), ancho, EJE_X - (int) (limites.elementAt(i).doubleValue() * factor));
        }
    }

    public synchronized void graficar(double n) {
        if (datos.size() >= ancho) {
            datos.remove(0);
        }
        max = Math.max(n, max);
        factor = maxDibujo / max;
        datos.add(new Double(n));
        //datos.add(new Integer((int)n));
        //repaint();
    }

    public synchronized void reset() {
        datos.removeAllElements();
        while (ancho > datos.size()) {
            datos.add(new Double(0.0D));
        }
    }

    public synchronized void setResetFactor(double l) {
        max = l;
        factor = maxDibujo / max;
    }

    public void run() {
        while (!thread.interrupted()) {
            this.repaint();
            try {
                thread.sleep(tiempoActualizacion);
            } catch (Exception e) {

            }
        }
    }

    public synchronized void stop() {
        if (thread != null) {
            thread.interrupt();
            /*try{
            	thread.join();
            }catch(InterruptedException e){
            	System.out.println("Error: stop");
            }*/
        }
        thread = null;
    }

    public synchronized void remove() {
        GUI.removeGrafica(this);
        stop();
    }

    public void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
        ancho = (int) preferredSize.getWidth();
        alto = (int) preferredSize.getHeight();
        ejeX(pos);
    }

}
