/*
 *	Paquete				: audio
 *	Archivo				: Wav.java
 *	Version				: 1.0.0		2007-02-27
 *
 *	Todos los derechos reservados.  Usar bajo terminos de la licencia.
 * 
 *	Ver archivo "licencia.txt" para informacion de como usar y
 *	distrubuir este archvio, y para garantizar las responsabilidades 
 *	adquiridas.
 */
package com.avpsoft.icvoz.audio;

import java.io.*;
import javax.sound.sampled.*;

public class Wav {

    private static DataOutputStream dataOutputStream;
    private static String prefijoRuta = "icvoz" + File.separator + "default" + File.separator + "wav" + File.separator;

    private Wav() {
    }

    public static void grabar(String perfil, String fileName, byte[] dat) {
        Wav.prefijoRuta = "icvoz" + File.separator + perfil + File.separator + "wav" + File.separator;
        Wav.grabar(fileName, dat);
    }

    private static void grabar(String fileName, byte[] dat) {
        System.out.println("Guardando archivo :" + prefijoRuta + fileName + ".wav");
        try {

            dataOutputStream = new DataOutputStream(new FileOutputStream(prefijoRuta + fileName + ".wav"));
        } catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }

        for (int i = 0; i < dat.length; i++) {
            dat[i] += 127;
        }

        cabeceraWAV();
        write(dat, 0, dat.length);
        close();
        System.out.println("Se ha guardado archivo :" + prefijoRuta + fileName + ".wav");
    }

    private static void close() {
        try {
            dataOutputStream.close();
            dataOutputStream = null;
        } catch (Exception e) {
            System.out.println("Error GrabarAudio.close " + e);
        }
    }

    private static void flush() {
        try {
            dataOutputStream.flush();
        } catch (Exception e) {
            System.out.println("Error GrabarAudio.flush " + e);
        }
    }

    private static void write(byte[] b, int off, int len) {
        try {
            dataOutputStream.write(b, off, len);
        } catch (Exception e) {
            System.out.println("Error GrabarAudio.write " + e);
        }
        flush();
    }

    private static void cabeceraWAV() {
        byte Wav[] = new byte[44];
        try {
            /* Campo 1: bytes 0 .. 3. Contiene la palabra "RIFF" en c�digo ASCII. */
            Wav[0] = 'R';
            Wav[1] = 'I';
            Wav[2] = 'F';
            Wav[3] = 'F';

            dataOutputStream.write(Wav, 0, 4);
            //System.out.println("Campo 1 ["+dataOutputStream.size()+"]");

            /*	Campo 2: bytes 4 .. 7. Tama�o total del archivo en bytes menos 8 (no incluye los dos primeros campos). */
            Wav[4] = '#';
            Wav[5] = 0;
            Wav[6] = 0;
            Wav[7] = 0;

            dataOutputStream.write(Wav, 4, 4);
            //System.out.println("Campo 2 ["+dataOutputStream.size()+"]");

            /*	Campo 3: bytes 8 .. 15. Contiene la palabra "WAVEfmt " en c�digo ASCII  */
            Wav[8] = 'W';
            Wav[9] = 'A';
            Wav[10] = 'V';
            Wav[11] = 'E';
            Wav[12] = 'f';
            Wav[13] = 'm';
            Wav[14] = 't';
            Wav[15] = ' ';

            dataOutputStream.write(Wav, 8, 8);
            //System.out.println("Campo 3 ["+dataOutputStream.size()+"]");

            /*	Campo 4: bytes 16 .. 19. Formato: para PCM vale 16. */
            Wav[16] = 16;
            Wav[17] = 0;
            Wav[18] = 0;
            Wav[19] = 0;

            dataOutputStream.write(Wav, 16, 4);
            //System.out.println("Campo 4 ["+dataOutputStream.size()+"]");

            /*	Campo 5: bytes 20 .. 21. Formato: para PCM vale 1. */
            Wav[20] = 1;
            Wav[21] = 0;

            dataOutputStream.write(Wav, 20, 2);
            //System.out.println("Campo 5 ["+dataOutputStream.size()+"]");

            /*	Campo 6: bytes 22 .. 23. Se indica si es mono (1) o est�reo (2). */
            Wav[22] = 1;
            Wav[23] = 0;

            dataOutputStream.write(Wav, 22, 2);
            //System.out.println("Campo 6 ["+dataOutputStream.size()+"]");

            /*	Campo 7: bytes 24 .. 27. Frecuencia de muestreo, puede valer: 11.025,22.050 o 44.100.  */
            //Wav[24]= 64 ;Wav[25]= 31 ;Wav[26]= 0 ;Wav[27]= 0 ;
            Wav[24] = 17;
            Wav[25] = 43;
            Wav[26] = 0;
            Wav[27] = 0;

            dataOutputStream.write(Wav, 24, 4);
            //System.out.println("Campo 7 ["+dataOutputStream.size()+"]");

            /*	Campo 8: bytes 28 .. 31. Indica el n�mero de bytes por segundo que se debe 
				Intercambiar con la tarjeta de sonido para una grabaci�n o reproducci�n.*/
            Wav[28] = 17;
            Wav[29] = 43;
            Wav[30] = 0;
            Wav[31] = 0;
            //Wav[28]= 64 ;Wav[29]= 31 ;Wav[30]= 0 ;Wav[31]= 0 ;

            dataOutputStream.write(Wav, 28, 4);
            //System.out.println("Campo 8 ["+dataOutputStream.size()+"]");

            /*	Campo 9: bytes 32 .. 33. N�mero de bytes por captura, pueden ser 1, 2 o 4.  */
            Wav[32] = 1;
            Wav[33] = 0;

            dataOutputStream.write(Wav, 32, 2);
            //System.out.println("Campo 9 ["+dataOutputStream.size()+"]");

            /*	Campo 10: bytes 34 .. 35. N�mero de bits por muestra, pueden ser 8 � 16. */
            Wav[34] = 8;
            Wav[35] = 0;

            dataOutputStream.write(Wav, 34, 2);
            //System.out.println("Campo 10 ["+dataOutputStream.size()+"]");

            /*	Campo 11: bytes 36 .. 39. Contienen la palabra "data" en c�digo ASCII. */
            Wav[36] = 'd';
            Wav[37] = 'a';
            Wav[38] = 't';
            Wav[39] = 'a';

            dataOutputStream.write(Wav, 36, 4);
            //System.out.println("Campo 11 ["+dataOutputStream.size()+"]");

            /*	Campo 12: bytes 40 . .43: N�mero total de bytes que ocupan las muestras. */
            Wav[40] = -1;
            Wav[41] = -1;
            Wav[42] = -1;
            Wav[43] = -1;

            dataOutputStream.write(Wav, 40, 4);
            //System.out.println("Campo 12 ["+dataOutputStream.size()+"]");

        } catch (Exception e) {
            System.out.println("Error GrabarAudio.cabeceraWAV " + e);
        }
        return;
    }

    public static ByteArrayOutputStream leerArchivo(String fileName) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(prefijoRuta + fileName + ".wav"));
            byte aux[] = new byte[1];

            byte WAV[] = new byte[44];
            try {
                dataInputStream.read(WAV, 0, WAV.length);
                while (true) {
                    aux[0] = dataInputStream.readByte();
                    aux[0] -= 127;
                    byteArrayOutputStream.write(aux, 0, 1);
                }
            } catch (EOFException e) {
                System.out.println("Fin del archivo");
            } catch (Exception e) {
                System.out.println("Error: Wav.leerArchivo " + e);
            }
        } catch (Exception e) {
            System.out.println("Error: Wav.leerArchivo " + e);
        }
        return byteArrayOutputStream;
    }

    private static final int EXTERNAL_BUFFER_SIZE = 128000;

    public static void reproducir(String fileName) {
        String args[] = new String[1];
        args[0] = prefijoRuta + fileName + ".wav";

        String strFilename = args[0];
        File soundFile = new File(strFilename);

        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e) {

            e.printStackTrace();
            System.exit(1);
        }
        AudioFormat audioFormat = audioInputStream.getFormat();

        SourceDataLine line = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class,
                audioFormat);
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        line.start();

        int nBytesRead = 0;
        byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                int nBytesWritten = line.write(abData, 0, nBytesRead);
            }
        }
        line.drain();
        line.close();
    }

    public static void reproducir(byte datos[]) {
        SourceDataLine line = null;
        DataLine.Info info = new DataLine.Info(Audio.getSourceDataLine(), Audio.getAudioFormat());
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);

            line.open(Audio.getAudioFormat());

            FloatControl volumen = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
            volumen.setValue(volumen.getMaximum());
            //System.out.println("MAx "+volumen.getMaximum() );

        } catch (LineUnavailableException e) {
            e.printStackTrace();
            //System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        line.start();

        for (int i = 0; i < datos.length; i++) {
            datos[i] *= 10;
        }

        line.write(datos, 0, datos.length);

        line.drain();
        line.close();
    }

    private static void out(String strMessage) {
        System.out.println(strMessage);
    }
}
