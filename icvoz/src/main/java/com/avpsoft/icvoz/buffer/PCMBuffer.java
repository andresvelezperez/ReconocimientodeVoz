/*
 *	Paquete				: buffer
 *	Archivo				: PCMBuffer.java
 *	Version				: 1.0.0		2007-03-22
 *
 *	Todos los derechos reservados.  Usar bajo terminos de la licencia.
 * 
 *	Ver archivo "licencia.txt" para informacion de como usar y
 *	distrubuir este archvio, y para garantizar las responsabilidades 
 *	adquiridas.
 */
package com.avpsoft.icvoz.buffer;

public class PCMBuffer {

    private int capacidad;
    private int leer = 0;
    private int escribir = 0;

    private double pcmBuffer[];

    public PCMBuffer() {
        this.capacidad = 1024;
        this.initBuffer();
    }

    public PCMBuffer(int capacidad) {
        this.capacidad = capacidad;
        this.initBuffer();
    }

    private void initBuffer() {
        this.pcmBuffer = new double[this.capacidad];
        return;
    }

    public synchronized int agregar(double valor) {
        if (this.vacio()) {
            this.pcmBuffer[this.escribir++ % this.capacidad] = valor;
        } else {
            //System.out.println("[PCMBuffer lleno]");
            return -1;
        }

        return 0;
    }

    public synchronized double leer() {
        if (datos()) {
            return this.pcmBuffer[this.leer++ % this.capacidad];
        } else {
            return -1;
        }
    }

    public synchronized boolean vacio() {
        return this.escribir - this.leer < this.capacidad;
    }

    public synchronized boolean datos() {
        if (this.leer >= this.capacidad && this.escribir >= this.capacidad) {
            this.leer = this.leer % this.capacidad;
            this.escribir = this.escribir % this.capacidad;
        }

        return this.escribir > this.leer;
    }

    /*	public static void main(String args[])
	{
		
		PCMBuffer pCMBuffer = new PCMBuffer();
		
		System.out.println(pCMBuffer.vacio());
		System.out.println(pCMBuffer.datos());
		
		int aux=0;
		for(int i=0; i<1030;i++)
		{
			System.out.println(pCMBuffer.vacio());
			pCMBuffer.agregar(5);
		}	
		

		
		for(int i=0; i<1030;i++)
		{
			System.out.println(pCMBuffer.datos());
			pCMBuffer.leer();
		}
		
		for(int i=0; i<1030;i++)
		{
			System.out.println(pCMBuffer.vacio());
			pCMBuffer.agregar(5);
		}	

		
		for(int i=0; i<1030;i++)
		{
			System.out.println(pCMBuffer.datos());
			pCMBuffer.leer();
		}
		
	}*/
}
