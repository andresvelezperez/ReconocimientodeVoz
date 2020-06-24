/*
 *	Paquete				: rna
 *	Archivo				: RedNeuronal.java
 *	Version				: 1.0.0		2007-04-24
 *
 *	Todos los derechos reservados.  Usar bajo terminos de la licencia.
 * 
 *	Ver archivo "licencia.txt" para informacion de como usar y
 *	distrubuir este archvio, y para garantizar las responsabilidades 
 *	adquiridas.
 */
package com.avpsoft.icvoz.rna;

import com.avpsoft.icvoz.archivo.ArchivoPSS;
import com.avpsoft.icvoz.archivo.ArchivoRNA;
import java.text.DecimalFormat;
import javax.swing.JProgressBar;

public class RedNeuronal {

    private static final int patternSize = 169;//128
    private Network network;
    private String perfil;
    private DecimalFormat df = new DecimalFormat("0.00");
    private int[] mapNeurons = null;

    public static int getPatternSize() {
        return RedNeuronal.patternSize;
    }

    public RedNeuronal(String perfil) {
        this.perfil = perfil;
        this.network = new Network(RedNeuronal.getPatternSize(), 73, 20, 0.1, 0.1);
        try {
            ArchivoPSS archivoPSS = new ArchivoPSS(this.perfil);
            network.reset(archivoPSS.getPesos(), archivoPSS.getUmbral());
            System.out.println("Archivo pss.xml ha sido cargado");
        } catch (Exception e) {
            System.out.println("Error: RedNeuronal:Constructor \n");
            e.printStackTrace();
        }
    }

    public int redNeuronal(double entrada[]) {
        System.out.println("[RedNeuronal iniciada]");
        int salida = 0;
        double vectorSalida[];

        if (entrada != null) {
            /**
             * Red Neuronal
             */
            vectorSalida = network.computeOutputs(entrada);

            double aux = vectorSalida[0];
            for (int i = 1; i < vectorSalida.length; i++) {
                if (vectorSalida[i] > aux) {
                    aux = vectorSalida[i];
                    salida = i;
                }
            }
            for (int i = 0; i < vectorSalida.length; i++) {
                if (i == salida) {
                    System.out.println("Ganador => [" + i + "] " + df.format(vectorSalida[i]));
                } else {
                    System.out.println("Orden   => [" + i + "] " + df.format(vectorSalida[i]));
                }
            }
        }
        System.out.println("[RedNeuronal Finalizado]");
        return salida;
    }

    public void entrenamiento(JProgressBar progreso) {
        System.out.println("Entrenando");
        ArchivoRNA archivoRNA;
        ArchivoPSS archivoPSS;
        double entrada[][];
        double salida[][];
        try {
            archivoRNA = new ArchivoRNA(this.perfil);

            entrada = archivoRNA.getEntrada();
            salida = archivoRNA.getSalida();
            network.reset();
            System.out.println("Entrada : " + entrada.length);
            System.out.println("Salida : " + entrada.length);

            int max = 10000;
            int update = 0;
            double error;
            for (int i = 0; i < max; i++) {
                network.training(entrada, salida);
                error = network.getError(entrada.length);

                progreso.setValue(i / 100);
                update++;
                if (update == 1000) {
                    progreso.setValue(i / 100);
                    System.out.println("Cycles Left:" + (max - i) + ",Error:" + error);
                    update = 0;
                }
            }
            progreso.setValue(99);
            archivoPSS = new ArchivoPSS(this.perfil);
            archivoPSS.setPesos(network.getMatrix());
            archivoPSS.setUmbral(network.getThresholds());
            archivoPSS.guardar();
            progreso.setValue(100);
        } catch (Exception e) {
            System.out.println("Error: RedNeuronal:entrenamiento");
            e.printStackTrace();
        }
        System.out.println("Entrenamiento realizado");
    }
}
