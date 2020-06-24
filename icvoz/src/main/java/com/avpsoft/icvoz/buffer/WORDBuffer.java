/*
 *	Paquete				: buffer
 *	Archivo				: WORDBuffer.java
 *	Version				: 1.0.0		2007-03-22
 *
 *	Todos los derechos reservados.  Usar bajo terminos de la licencia.
 * 
 *	Ver archivo "licencia.txt" para informacion de como usar y
 *	distrubuir este archvio, y para garantizar las responsabilidades 
 *	adquiridas.
 */
package com.avpsoft.icvoz.buffer;

import java.util.Vector;

public class WORDBuffer {

    private Vector<Trama> buffer;

    public WORDBuffer() {
        buffer = new Vector<Trama>(0, 1);
    }

    public boolean vacio() {
        return !(buffer.size() > 0);
    }

    public synchronized int agregar(double[][] datos) {
        return buffer.add(new Trama(datos)) ? 0 : 1;
    }

    public synchronized int agregar(double[] datos) {
        return buffer.add(new Trama(datos)) ? 0 : 1;
    }

    public synchronized double[][] leerMatrix() {
        if (vacio()) {
            return new double[0][0];
        } else {
            return buffer.remove(0).getMatrix();
        }
    }

    public synchronized double[] leerVector() {
        if (vacio()) {
            return new double[0];
        } else {
            return buffer.remove(0).getVector();
        }
    }

    private class Trama {

        private double matrix[][];
        private double vector[];

        public Trama(double matrix[][]) {
            this.matrix = matrix;
        }

        public Trama(double vector[]) {
            this.vector = vector;
        }

        public double[][] getMatrix() {
            return matrix;
        }

        public double[] getVector() {
            return vector;
        }
    }
}
