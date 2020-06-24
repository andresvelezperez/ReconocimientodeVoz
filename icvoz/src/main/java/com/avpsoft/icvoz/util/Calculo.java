/*
 *	Paquete				: util
 *	Archivo				: Calculo.java
 *	Version				: 1.0.0		2007-07-10
 *
 *	Todos los derechos reservados.  Usar bajo terminos de la licencia.
 * 
 *	Ver archivo "licencia.txt" para informacion de como usar y
 *	distrubuir este archvio, y para garantizar las responsabilidades 
 *	adquiridas.
 */
package com.avpsoft.icvoz.util;

import com.avpsoft.icvoz.audio.Audio;

public class Calculo {

    /*
	 * Calculo de la energia de una ventana de audio.
	 * Formula obtenida de:
	 *	BERNAL B. JES�S, BOBADILLA S. JES�S, G�MEZ V. PEDRO Reconocimiento
	 *		de Voz  y Fon�tica Ac�stica. ALFAOMEGA Grupo Editor a�o 2000, 332 P�ginas.
	 * Numero de pagina donde se encuentra: pag. 143.
	 *
     */
    public static double energia(final double v[]) {
        double energia = 0;
        for (int i = 0; i < v.length; i++) {
            energia += (double) Math.pow(v[i], 2);
        }
        return energia * (1.0 / (double) v.length);
    }

    /*
	 * Calculo de magnitud de una ventana de audio.
	 * Formula obtenida de:
	 *	BERNAL B. JES�S, BOBADILLA S. JES�S, G�MEZ V. PEDRO Reconocimiento
	 *		de Voz  y Fon�tica Ac�stica. ALFAOMEGA Grupo Editor a�o 2000, 332 P�ginas.
	 * Numero de pagina donde se encuentra: pag. 143.
	 *
     */
    public static double magnitud(final double v[]) {
        double magnitud = 0;
        for (int i = 0; i < v.length; i++) {
            magnitud += Math.abs(v[i]);
        }
        return magnitud * (1.0 / (double) v.length);
    }

    public static double media(final double v[]) {
        double media = 0;
        for (int i = 0; i < v.length; i++) {
            media += v[i];
        }
        return Math.abs(media * (1.0 / (double) v.length));
    }

    public static double desviacionEstandar(final double vect[], final double med) {
        double aux = 0;
        for (int i = 0; i < vect.length; i++) {
            aux += Math.pow(vect[i] - med, 2);
        }
        return Math.sqrt(aux / (double) vect.length);
    }

    public static double max(final double[] v) {
        double max = 0.0;
        for (int i = 0; i < v.length; i++) {
            max = Math.max(max, v[i]);
        }
        return max;
    }

    public static double min(final double[] v) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < v.length; i++) {
            min = Math.min(min, v[i]);
        }
        return min;
    }

    public static double[] normalizar(double[] cadena) {
        cadena[0] = 0.0;
        for (int i = 0; i < cadena.length; i++) {
            cadena[i] = cadena[i] / 127.0;
        }
        return cadena;
    }

    public static double[] preenfasis(double[] cadena, final double a) {
        for (int i = 1; i < cadena.length; i++) {
            cadena[i] = cadena[i] - a * cadena[i - 1];
        }
        return cadena;
    }

    public static double crucesPorCero_sgn(final double v[]) {
        double sumatoria = 0.0;
        for (int i = 1; i < v.length; i++) {
            sumatoria += (Math.abs(sgn(v[i]) - sgn(v[i - 1])) / 2);
        }
        return sumatoria;
    }

    public static int sgn(final double v) {
        return v < 0.0D ? -1 : 1;
    }

    public static int crucesPorCero(final int v_crucesCero[]) {
        double sumatoria = 0;
        for (int i = 1; i < v_crucesCero.length; i++) {
            sumatoria += v_crucesCero[i];
        }
        return (int) sumatoria;
    }

    /*private int[] vector_crucesCero(final double v[])
	{
		int v_crucesCero[] = new int[v.length];
		for(int i=1; i<v.length; i++)
		{
			v_crucesCero[i-1]=(Math.abs(sgn(v[i])-sgn(v[i-1]))/2);	
		}
		
		return v_crucesCero;
	}*/
    public static double promedioCrucesporCero(final double v_crucesCero[]) {
        double sumatoria = 0;
        for (int i = 0; i < v_crucesCero.length; i++) {
            sumatoria += v_crucesCero[i];
        }
        sumatoria *= 1.0 / (double) v_crucesCero.length;
        return sumatoria;
    }

    public static double distancia(final double[] p1, final double[] p2) {
        if (p1.length != p2.length) {
            throw new java.lang.IndexOutOfBoundsException("Distancia: p1 != p2");
        }

        double d = 0;
        for (int i = 0; i < p1.length; i++) {
            d += Math.pow(p1[i] - p2[i], 2);
        }

        return Math.sqrt(d);
    }

    public static double Hamming(final double n) {
        return n > (double) Audio.getWindowSize() ? 0 : 0.54 + 0.56 * Math.cos((2.0 * Math.PI * n) / (((double) Audio.getWindowSize()) - 1.0));
    }
}
