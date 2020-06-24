/*
 *	Paquete				: espectro
 *	Archivo				: Espectro.java
 *	Version				: 1.0.0		2007-03-30
 *
 *	Todos los derechos reservados.  Usar bajo terminos de la licencia.
 * 
 *	Ver archivo "licencia.txt" para informacion de como usar y
 *	distrubuir este archvio, y para garantizar las responsabilidades 
 *	adquiridas.
 */
package com.avpsoft.icvoz.espectro;

import com.avpsoft.icvoz.audio.Audio;
import com.avpsoft.icvoz.gui.Texto;
import com.avpsoft.icvoz.main.Main;
import com.avpsoft.icvoz.rna.RedNeuronal;
import com.avpsoft.icvoz.util.Util;
import java.text.DecimalFormat;

public class Espectro {

    private FFT Fft;
    private Texto texto;

    private MelFrequencyFilterBank mel;
    private DiscreteCosineTransform dct;

    private static float minFreq = 15.0F;// 15 Hz
    private static float maxFreq = 4000.0F; // 4 KHz
    private static int numberMelFilters = 20;
    private static int cepstrumSize = 13;
    private static int frameLength = 13;

    public Espectro() {
        Fft = new FFT();
        mel = new MelFrequencyFilterBank(minFreq, maxFreq, numberMelFilters, Audio.getSampleRate());
        dct = new DiscreteCosineTransform(numberMelFilters, cepstrumSize);
        if (Main.DEBUG) {
            texto = new Texto("TxtPalabra");
        }
    }

    public double[] espectro(double matrixEntrada[][]) {
        System.out.println("[Espectro iniciada]");
        System.out.println("Cantida de ventanas: " + matrixEntrada.length);
        DecimalFormat df = new DecimalFormat("0.00");

        double fft[][] = new double[matrixEntrada.length][];// FFT
        double mffb[][] = new double[frameLength][numberMelFilters];//banco de filtros de mel
        double mfcc[][] = new double[frameLength][cepstrumSize];//coeficientes cepstrales de mel
        double vectorSalida[] = new double[RedNeuronal.getPatternSize()];

        for (int i = 0; i < matrixEntrada.length; i++) {
            fft[i] = Fft.process(matrixEntrada[i]);
        }
        if (Main.DEBUG) {
            texto.println("");
        }
        /* Promediar ventanas de n a 13*/
        fft = reducirVentanas((double[][]) fft.clone(), frameLength);
        /* Obtener los MFCC's de la FFT*/
        for (int i = 0; i < fft.length; i++) {
            mffb[i] = mel.process(fft[i]);
            mfcc[i] = dct.process(mffb[i]);
        }
        /*Impresion de MFCC's*/
        if (Main.DEBUG) {
            for (int i = 0; i < mfcc.length; i++) {
                for (int j = 0; j < mfcc[i].length; j++) {
                    texto.print("[" + df.format(mfcc[i][j]) + "] \t");
                }
                texto.println("");
            }
        }
        /* Covertir la matrix en un vector */
        vectorSalida = Util.matrixToVector(mfcc, RedNeuronal.getPatternSize());

        System.out.println("[Espectro Finalizado]");
        return vectorSalida;
    }

    private double[][] reducirVentanas(final double cadena[][], int nuevo) {
        if (cadena.length <= nuevo) {
            return (double[][]) cadena.clone();
        }
        int anchoViejo = cadena.length;
        int anchoNuevo = nuevo;
        int M = anchoViejo / anchoNuevo;
        int k = 0;
        double cadenaNueva[][] = new double[anchoNuevo][cadena[0].length];

        for (int i = 0; i < anchoNuevo; i++) {
            for (int j = 0; j < cadena[i].length; j++) {
                for (k = i * M; k < (i + 1) * M; k++) {
                    cadenaNueva[i][j] += cadena[k][j];
                }
                cadenaNueva[i][j] /= (double) M;
            }
        }
        return cadenaNueva;
    }
}
