/*
 *	Paquete				: captura
 *	Archivo				: CapturaMicrofono.java
 *	Version				: 1.0.0		2007-02-22
 *
 *	Todos los derechos reservados.  Usar bajo terminos de la licencia.
 * 
 *	Ver archivo "licencia.txt" para informacion de como usar y
 *	distrubuir este archvio, y para garantizar las responsabilidades 
 *	adquiridas.
 */
package com.avpsoft.icvoz.captura;

import com.avpsoft.icvoz.audio.Audio;
import com.avpsoft.icvoz.buffer.PCMBuffer;
import com.avpsoft.icvoz.gui.Grafica;
import com.avpsoft.icvoz.main.Main;
import java.io.ByteArrayOutputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;

public class CapturaMicrofono implements Runnable {

    /**
     * Linea de entrada *
     */
    private DataLine.Info info;
    private TargetDataLine targetDataLine = null;

    /**
     * Otras Variables *
     */
    private Grafica grafica_PCM = null;
    private PCMBuffer pCMBuffer;
    private ByteArrayOutputStream byteArrayOutputStream;
    private Thread thread;

    public CapturaMicrofono() {
        /**
         * Linea del Microfono *
         */
        info = new DataLine.Info(Audio.getTargetDataLine(), Audio.getAudioFormat());

        try {
            /*Grabar*/
            targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            targetDataLine.open(Audio.getAudioFormat());
        } catch (LineUnavailableException e) {
            System.out.println("Error: CapturaMicrofono: Constructor");
            e.printStackTrace();
            System.exit(1);
        }

        if (Main.DEBUG) {
            grafica_PCM = new Grafica("PCM", 10);
            grafica_PCM.ejeX("medio");
        }
    }

    public CapturaMicrofono(PCMBuffer pCMBuffer) {
        this();
        this.pCMBuffer = pCMBuffer;
        this.byteArrayOutputStream = null;
    }

    public CapturaMicrofono(ByteArrayOutputStream byteArrayOutputStream) {
        this();
        this.byteArrayOutputStream = byteArrayOutputStream;
    }

    public void start() {
        thread = new Thread(this);
        thread.setName("CapturaMicrofono");
        if (byteArrayOutputStream != null) {
            byteArrayOutputStream.reset();
        }
        if (grafica_PCM != null) {
            grafica_PCM.start();
        }
        targetDataLine.drain();
        targetDataLine.start();
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    public void run() {
        System.out.println("[Captura de audio iniciada]");
        int off = 0;
        int len = Audio.getFrameSize();
        byte[] b = new byte[len];

        targetDataLine.flush();

        if (byteArrayOutputStream != null) {
            while (thread != null)//(!thread.interrupted())
            {
                targetDataLine.read(b, off, len);
                if (grafica_PCM != null) {
                    for (int i = off; i < len; i++) {
                        grafica_PCM.graficar((double) b[i]);
                    }
                }
                byteArrayOutputStream.write(b, off, len);
            }
        } else {
            while (thread != null)//(!thread.interrupted())
            {
                targetDataLine.read(b, off, len);
                for (int i = off; i < len; i++) {
                    while (!pCMBuffer.vacio()) {
                        thread.yield();
                    }
                    pCMBuffer.agregar((double) b[i]);
                    if (grafica_PCM != null) {
                        grafica_PCM.graficar((double) b[i]);
                    }
                }
            }
        }
        System.out.println("[Captura de audio finalizada]");
    }

    public ByteArrayOutputStream getAudio() {
        return byteArrayOutputStream;
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
        targetDataLine.stop();
        if (grafica_PCM != null) {
            grafica_PCM.stop();
        }
    }

    public synchronized void close() {
        this.stop();
        targetDataLine.close();
    }

    public void setGrafica(Grafica grafica) {
        this.grafica_PCM = grafica;
    }
}
