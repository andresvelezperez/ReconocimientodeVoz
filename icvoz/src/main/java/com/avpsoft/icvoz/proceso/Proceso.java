/*
 *	Paquete				: proceso
 *	Archivo				: Proceso.java
 *	Version				: 1.0.0		2007-07-06
 *
 *	Todos los derechos reservados.  Usar bajo terminos de la licencia.
 * 
 *	Ver archivo "licencia.txt" para informacion de como usar y
 *	distrubuir este archvio, y para garantizar las responsabilidades 
 *	adquiridas.
 */
package com.avpsoft.icvoz.proceso;

import com.avpsoft.icvoz.buffer.WORDBuffer;
import com.avpsoft.icvoz.emulador.Dispositivo;
import com.avpsoft.icvoz.espectro.Espectro;
import com.avpsoft.icvoz.rna.RedNeuronal;

public class Proceso implements Runnable {

    private WORDBuffer entrada;
    private Thread thread;

    private String perfil;

    private Dispositivo dispositivo;
    private Espectro espectro;
    private RedNeuronal redNeuronal;

    public Proceso(WORDBuffer entrada, String perfil) {
        this.entrada = entrada;
        this.perfil = perfil;
        this.dispositivo = new Dispositivo(perfil);
        this.espectro = new Espectro();
        this.redNeuronal = new RedNeuronal(perfil);
    }

    public void start() {
        thread = new Thread(this, "Proceso");
        thread.start();
    }

    public void run() {
        System.out.println("[Proceso Iniciado]");
        double matrixEntrada[][];
        double vectorEntrada[];
        int salida;

        while (!thread.interrupted()) {
            if (!entrada.vacio()) {
                try {
                    matrixEntrada = entrada.leerMatrix();
                    vectorEntrada = espectro.espectro(matrixEntrada);
                    salida = redNeuronal.redNeuronal(vectorEntrada);
                    System.out.println("Salida Red Neuronal : " + salida);
                    dispositivo.eventoTeclado("" + salida);
                } catch (Exception e) {
                    System.out.println("Error: Proceso:run");
                    e.printStackTrace();
                }
            } else {
                thread.yield();
            }
        }
        System.out.println("[Proceso Finalizado]");
    }

    public void stop() {
        if (thread != null) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Error: stop");
            }
        }
        thread = null;
    }
}
