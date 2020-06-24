/*
 *	Paquete				: archivo
 *	Archivo				: ArchivoPSS.java
 *	Version				: 1.0.0		2007-07-04
 *
 *	Todos los derechos reservados.  Usar bajo terminos de la licencia.
 * 
 *	Ver archivo "licencia.txt" para informacion de como usar y
 *	distrubuir este archvio, y para garantizar las responsabilidades 
 *	adquiridas.
 */
package com.avpsoft.icvoz.archivo;

import java.io.*;
import java.text.*;

import org.jdom.*;

public class ArchivoPSS extends Archivo {

    public ArchivoPSS(String perfil) throws FileNotFoundException, JDOMException, IOException {
        super(perfil, Archivo.pss);
    }

    public double[] getPesos() {
        Document doc = getDocument();
        Element raiz = doc.getRootElement();
        Element pesos = raiz.getChild("pesos");

        String dat[] = pesos.getText().split("\t");

        double pss[] = new double[dat.length];

        for (int i = 0; i < pss.length; i++) {
            pss[i] = Double.valueOf(dat[i].replace(',', '.')).doubleValue();
        }

        return pss;
    }

    public double[] getUmbral() {
        Document doc = getDocument();
        Element raiz = doc.getRootElement();
        Element pesos = raiz.getChild("umbral");

        String dat[] = pesos.getText().split("\t");

        double pss[] = new double[dat.length];

        for (int i = 0; i < pss.length; i++) {
            pss[i] = Double.valueOf(dat[i].replace(',', '.')).doubleValue();
        }

        return pss;
    }

    public void setPesos(double pss[]) throws FileNotFoundException, JDOMException, IOException {
        Document doc = getDocument();
        Element raiz = doc.getRootElement();
        Element pesos = raiz.getChild("pesos");
        DecimalFormat df = new DecimalFormat("0.000000");
        String dat = "";

        for (int i = 0; i < pss.length; i++) {
            dat += df.format(pss[i]) + "\t";
        }

        pesos.setText(dat);
    }

    public void setUmbral(double pss[]) throws FileNotFoundException, JDOMException, IOException {
        Document doc = getDocument();
        Element raiz = doc.getRootElement();
        Element pesos = raiz.getChild("umbral");
        DecimalFormat df = new DecimalFormat("0.000000");
        String dat = "";

        for (int i = 0; i < pss.length; i++) {
            dat += df.format(pss[i]) + "\t";
        }

        pesos.setText(dat);
    }

}
