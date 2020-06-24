/*
 *	Paquete				: archivo
 *	Archivo				: ArchivoRNA.java
 *	Version				: 1.0.0		2007-07-04
 *
 *	Todos los derechos reservados.  Usar bajo terminos de la licencia.
 * 
 *	Ver archivo "licencia.txt" para informacion de como usar y
 *	distrubuir este archvio, y para garantizar las responsabilidades 
 *	adquiridas.
 */
package com.avpsoft.icvoz.archivo;

import java.text.*;
import java.io.*;
import java.util.*;

import org.jdom.*;

public class ArchivoRNA extends Archivo {

    public ArchivoRNA(String perfil) throws FileNotFoundException, JDOMException, IOException {
        super(perfil, Archivo.rna);
    }

    public double[][] getEntrada() {
        Document doc = getDocument();
        Element raiz = doc.getRootElement();
        Element comandos = raiz.getChild("comandos");

        List cmd = comandos.getChildren("comando");

        double entrada[][] = new double[cmd.size()][];

        for (int i = 0; i < cmd.size(); i++) {
            entrada[i] = getDouble(((Element) cmd.get(i)).getChild("entrada").getText());
        }

        return entrada;
    }

    public double[][] getSalida() {
        Document doc = getDocument();
        Element raiz = doc.getRootElement();
        Element comandos = raiz.getChild("comandos");

        List cmd = comandos.getChildren("comando");

        double entrada[][] = new double[cmd.size()][];

        for (int i = 0; i < cmd.size(); i++) {
            entrada[i] = getDouble(((Element) cmd.get(i)).getChild("salida").getText());
        }

        return entrada;
    }

    public void setEntrada(double pss[], String codigo) throws FileNotFoundException, JDOMException, IOException {
        Document doc = getDocument();
        Element raiz = doc.getRootElement();
        Element comandos = raiz.getChild("comandos");

        List cmd = comandos.getChildren("comando");

        String aux = "";

        for (int i = 0; i < cmd.size(); i++) {
            if (((Element) cmd.get(i)).getAttributeValue("codigo").compareTo(codigo) == 0) {
                aux = getString(pss);
                ((Element) cmd.get(i)).getChild("entrada").setText(aux);
                i = cmd.size() + 1;
            }
        }
    }

    public void setSalida(double pss[], String codigo) throws FileNotFoundException, JDOMException, IOException {
        Document doc = getDocument();
        Element raiz = doc.getRootElement();
        Element comandos = raiz.getChild("comandos");

        List cmd = comandos.getChildren("comando");

        String aux = "";

        for (int i = 0; i < cmd.size(); i++) {
            if (((Element) cmd.get(i)).getAttributeValue("codigo").compareTo(codigo) == 0) {
                aux = getString(pss);
                ((Element) cmd.get(i)).getChild("salida").setText(aux);
                i = cmd.size() + 1;
            }
        }
    }

    private double[] getDouble(String cad) {
        String dat[] = cad.split("\t");

        double temp[] = new double[dat.length];

        for (int i = 0; i < temp.length; i++) {
            temp[i] = Double.valueOf(dat[i].replace(',', '.')).doubleValue();
        }

        return temp;
    }

    private String getString(double temp[]) {
        DecimalFormat df = new DecimalFormat("0.00");
        String dat = "";

        for (int i = 0; i < temp.length; i++) {
            dat += df.format(temp[i]) + "\t";
        }
        return dat;
    }
}
