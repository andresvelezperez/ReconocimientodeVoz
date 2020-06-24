/*
 *	Paquete				: main
 *	Archivo				: Inicio.java
 *	Version				: 1.0.0		2007-07-12
 *
 *	Todos los derechos reservados.  Usar bajo terminos de la licencia.
 * 
 *	Ver archivo "licencia.txt" para informacion de como usar y
 *	distrubuir este archvio, y para garantizar las responsabilidades 
 *	adquiridas.
 */
 
package com.avpsoft.icvoz.main;

import com.avpsoft.icvoz.buffer.PCMBuffer;
import com.avpsoft.icvoz.buffer.WORDBuffer;
import com.avpsoft.icvoz.captura.CapturaMicrofono;
import com.avpsoft.icvoz.captura.DetectarBordes;
import com.avpsoft.icvoz.proceso.Proceso;



public class Inicio implements Runnable 
{
	//Buffer para capturar el audio.
	private PCMBuffer pCMBuffer_mic;
	//Buffer para almacenar la palabra.
	private WORDBuffer wORDBuffer; 

	//Captura la voz del microfono
	private CapturaMicrofono capturaMicrofono;
	//Detector de bordes
	private DetectarBordes detectarBordes;
	//Hilo que procesa:
	//	- Extracion de caracteristicas expectrales.
	//  - Ejecucion de la red neuronal.
	//  - Ejecucion del comando.
	private Proceso proceso;
	//Perfil del Usuario.
	private String perfil;
	
	//Hilo para inicializar los procesos
	private Thread thread;
	
	public Inicio(String perfil)
	{
		//Inicilizacion del perfil
		this.perfil = perfil;
		//Inicilizacion del buffer
		this.pCMBuffer_mic = new PCMBuffer(8192);
		this.wORDBuffer = new WORDBuffer();
		
		//Inicializacion de los Procesos
		capturaMicrofono = new CapturaMicrofono(this.pCMBuffer_mic);
		detectarBordes = new DetectarBordes(this.pCMBuffer_mic,wORDBuffer);	
		proceso = new Proceso(wORDBuffer,this.perfil);
	}
	
	public void start()
	{
		thread = new Thread(this,"Inicio");
		thread.start();	
	}
	
	public void run()
	{
		System.out.println("[Sistema Iniciado]");
		proceso.start();
		detectarBordes.start();
		capturaMicrofono.start();	
	}
	
	public void stop()
	{
		if (thread != null)
		{
            thread.interrupt();
        }
        thread = null;
		capturaMicrofono.stop();
		detectarBordes.stop();
		proceso.stop();
		System.gc();
		//System.out.println("[Sistema Detenido]");
	}
	public void close()
	{
		this.stop();
		capturaMicrofono = null;
		detectarBordes = null;
		proceso = null;
		pCMBuffer_mic = null;
		wORDBuffer = null;
		System.gc();
		System.out.println("[Sistema Detenido]");
	}
}