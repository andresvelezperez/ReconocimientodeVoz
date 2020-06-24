/*
 *	Paquete				: util
 *	Archivo				: Util.java
 *	Version				: 1.0.0		2007-08-01
 *
 *	Todos los derechos reservados.  Usar bajo terminos de la licencia.
 * 
 *	Ver archivo "licencia.txt" para informacion de como usar y
 *	distrubuir este archvio, y para garantizar las responsabilidades 
 *	adquiridas.
 */
package com.avpsoft.icvoz.util;

import com.avpsoft.icvoz.audio.Audio;

public class Util {

    private Util() {
    }

    public static double[] byteArrayDouble(byte a) {
        double bt[] = new double[8];

        for (int i = 0; i < 8; i++) {
            bt[i] = a >> 7 - i & 1;
        }

        return bt;
    }

    public static int bytesToInt(byte[] bytes) {
        if (bytes.length == 1) {
            return (int) (short) bytes[0];
        } else if (Audio.getBigEndian()) {
            return (int) ((short) ((bytes[0] << 8) | (0xff & bytes[1])));
        } else {
            return (int) ((short) ((bytes[1] << 8) | (0xff & bytes[0])));
        }
    }

    static public int toUnsignedShort(byte[] bytes, boolean bigEndian) {
        if (bytes.length == 1) {
            return (int) (0xff & bytes[0]);
        } else if (bigEndian) {
            return (int) (((bytes[0] & 0xff) << 8) | (0xff & bytes[1]));
        } else {
            return (int) (((bytes[1] & 0xff) << 8) | (0xff & bytes[0]));
        }
    }

    public static int[] bytesToIntArray(byte[] bytes) {
        int[] salida;
        if (Audio.getFrameSize() == 2) {
            salida = new int[bytes.length / 2];
            for (int i = 0; i < salida.length; i += 2) {
                salida[i] = Util.bytesToInt(new byte[]{bytes[0], bytes[1]});
            }
        } else {
            salida = new int[bytes.length];
            for (int i = 0; i < salida.length; i += 2) {
                salida[i] = (int) bytes[i];
            }
        }

        return salida;
    }

    public static double[] ArrayDoubleByte(double a[]) {
        double bt[] = new double[8 * a.length];
        int aux;
        int con = 0;
        for (int j = 0; j < a.length; j++) {
            aux = (int) (((int) a[j] > 255) ? 255 : a[j]);
            for (int i = 0; i < 8; i++) {

                bt[con++] = aux >> 7 - i & 1;
            }
        }

        return bt;
    }

    public static double[] VectorAdouble(java.util.Vector<Double> dato) {
        double aux[] = new double[dato.size()];

        for (int i = 0; i < aux.length; i++) {
            aux[i] = dato.elementAt(i).doubleValue();
        }

        return aux;
    }

    public static double[][] copiar(final double matrix[][], final double vector[]) {
        double matrixSalida[][] = matrix;
        double AuxMatrixSalida[][] = new double[matrixSalida.length][];
        System.arraycopy(matrixSalida, 0, AuxMatrixSalida, 0, matrixSalida.length);
        matrixSalida = new double[AuxMatrixSalida.length + 1][];
        System.arraycopy(AuxMatrixSalida, 0, matrixSalida, 0, AuxMatrixSalida.length);
        matrixSalida[matrixSalida.length - 1] = new double[vector.length];
        System.arraycopy(vector, 0, matrixSalida[matrixSalida.length - 1], 0, vector.length);
        return matrixSalida;
    }

    public static double[][] matrixTemp(final double matrix[][], final double vector[], int l) {
        double matrixSalida[][] = matrix;
        double AuxMatrixSalida[][];
        l -= 1;
        if (matrixSalida.length > l) {
            AuxMatrixSalida = new double[l][];
            System.arraycopy(matrixSalida, matrixSalida.length - l, AuxMatrixSalida, 0, l);
        } else {
            AuxMatrixSalida = new double[matrixSalida.length][];
            System.arraycopy(matrixSalida, 0, AuxMatrixSalida, 0, matrixSalida.length);
        }

        matrixSalida = new double[AuxMatrixSalida.length + 1][];
        System.arraycopy(AuxMatrixSalida, 0, matrixSalida, 0, AuxMatrixSalida.length);
        matrixSalida[matrixSalida.length - 1] = new double[vector.length];
        System.arraycopy(vector, 0, matrixSalida[matrixSalida.length - 1], 0, vector.length);
        return matrixSalida;
    }

    public static double[][] matrixCortar(final double matrix[][], final int l) {
        double matrixSalida[][] = matrix;
        double AuxMatrixSalida[][] = new double[l][];
        System.arraycopy(matrixSalida, 0, AuxMatrixSalida, 0, l);
        return AuxMatrixSalida;
    }

    public static double[] matrixToVector(final double matrix[][], final int tamV) {
        double salir[] = new double[tamV];
        int cont = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                salir[cont++] = matrix[i][j];
            }
        }
        return salir;
    }

}
