/*
 *	Paquete				: audio
 *	Archivo				: Audio.java
 *	Version				: 1.0.0		2007-08-20
 *
 *	Todos los derechos reservados.  Usar bajo terminos de la licencia.
 * 
 *	Ver archivo "licencia.txt" para informacion de como usar y
 *	distrubuir este archvio, y para garantizar las responsabilidades 
 *	adquiridas.
 */
package com.avpsoft.icvoz.audio;

import javax.sound.sampled.*;

public class Audio {

    private Audio() {
    }

    /**
     * Formato de la entrada de audio *
     */
    private static AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
    /* 	the audio encoding technique		*/
    private static float sampleRate = 11025.0F;
    /*	the number of samples per second	*/
    private static int sampleSizeInBits = 8;
    /*	the number of bits in each sample	*/
    private static int frameSize = 1;
    /*	the number of bytes in each frame	*/
    private static int channels = 1;
    /*	the number of channels (1 for mono, 2 for stereo, and so on)	*/
    private static float frameRate = sampleRate;
    /*	the number of frames per second		*/
    private static boolean signed = true;
    /*	indicates whether the data is signed or unsigned */
    private static boolean bigEndian = true;
    /*	indicates whether the data for a single sample is stored
	 																						 *	in big-endian byte order (false means little-endian)	*/
//	private static AudioFormat audioFormat = new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize, frameRate, bigEndian);
    private static AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    private static Class targetDataLine = TargetDataLine.class;
    private static Class sourceDataLine = SourceDataLine.class;

    private static int windowSize = 128;
    private static int windowOverlapSize = 100;
    private static int windowIncreaseSize = Audio.windowSize - Audio.windowOverlapSize;

    public static int getSampleRate() {
        return (int) Audio.sampleRate;
    }

    public static int getFrameSize() {
        return Audio.frameSize;
    }

    public static boolean getBigEndian() {
        return Audio.bigEndian;
    }

    public static AudioFormat getAudioFormat() {
        return Audio.audioFormat;
    }

    public static Class getTargetDataLine() {
        return Audio.targetDataLine;
    }

    public static Class getSourceDataLine() {
        return Audio.sourceDataLine;
    }

    public static int getWindowSize() {
        return Audio.windowSize;
    }

    public static int getWindowOverlapSize() {
        return Audio.windowOverlapSize;
    }

    public static int getWindowIncreaseSize() {
        return Audio.windowIncreaseSize;
    }

}
