/*
 *	Paquete				: captura
 *	Archivo				: DetectarBordes.java
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
import com.avpsoft.icvoz.buffer.WORDBuffer;
import com.avpsoft.icvoz.gui.Grafica;
import com.avpsoft.icvoz.main.Main;
import com.avpsoft.icvoz.util.Calculo;
import com.avpsoft.icvoz.util.Util;
import java.io.ByteArrayOutputStream;

public class DetectarBordes implements Runnable {

    private double ITU;
    private double ITL;
    private double IZCT;
    private final double LI_IZCT = 25; // Valor minimo de comparacion con el IZCT
    private int limitePuntoFinal = 55;//12

    private ByteArrayOutputStream byteArrayOutputStream; // Entrada
    private PCMBuffer pCMBuffer;// Entrada
    private WORDBuffer wORDBuffer; // Salidad

    private final int INCREMENTO_VENTANA = Audio.getWindowIncreaseSize();
    private final int SOLAPAMIENTO_VENTANA = Audio.getWindowOverlapSize();
    private final int VENTANA = Audio.getWindowSize();

    private Grafica grafica_M;
    private Grafica grafica_C;

    private Thread thread;

    public DetectarBordes(ByteArrayOutputStream byteArrayOutputStream) {
        this.byteArrayOutputStream = byteArrayOutputStream;
        this.wORDBuffer = new WORDBuffer();
        init();
    }

    public DetectarBordes(PCMBuffer pCMBuffer, WORDBuffer wORDBuffer) {
        this.byteArrayOutputStream = new ByteArrayOutputStream();
        this.pCMBuffer = pCMBuffer;
        this.wORDBuffer = wORDBuffer;
        init();
    }

    public void init() {
        if (Main.DEBUG) {
            grafica_M = new Grafica("Magnitud", INCREMENTO_VENTANA);
            grafica_C = new Grafica("Cero", INCREMENTO_VENTANA);

            grafica_M.ejeX("bajo");
            grafica_C.ejeX("bajo");
        }
    }

    public void start() {
        thread = new Thread(this, "DetectarBordes");
        thread.setPriority(Thread.MAX_PRIORITY - 1);
        thread.start();
        if (Main.DEBUG) {
            grafica_M.start();
            grafica_C.start();
        }
    }

    public void detector() {
        double datos[][];
        if (Main.DEBUG) {
            grafica_M.start();
            grafica_C.start();
            grafica_M.reset();
        }

        System.out.println("[Detector de bordes iniciado]");
        System.out.println("[INCREMENTO_VENTANA] " + INCREMENTO_VENTANA);
        System.out.println("[SOLAPAMIENTO_VENTANA] " + SOLAPAMIENTO_VENTANA);
        System.out.println("[VENTANA] " + VENTANA);

        //int dat[] = util.Util.bytesToIntArray(byteArrayOutputStream.toByteArray());
        byte dat[] = byteArrayOutputStream.toByteArray();
        if (!(dat.length > 0)) {
            stop();
        }

        double tem[] = new double[dat.length];
        for (int i = 900; i < tem.length; i++) {
            tem[i] = (double) dat[i];
        }
        tem = Calculo.preenfasis(tem, 0.92);

        datos = hacerVentanas(tem);
        detector(datos);
    }

    private void detector(double datos[][]) {
        double aux_datos[][];

        double magnitud[] = new double[datos.length];
        double cruces[] = new double[datos.length];

        for (int i = 0; i < datos.length; i++) {
            magnitud[i] = Calculo.magnitud(datos[i]);
            cruces[i] = Calculo.crucesPorCero_sgn(datos[i]);
        }

        estadisticaSonido(magnitud);
        estadisticaCrucesPorCero(cruces);
        if (Main.DEBUG) {
            grafica_M.limite(ITL, ITU);
        }
        boolean aux_i = false;
        boolean aux_f = false;
        boolean aux_f_f = false;

        int pos_i = 0;
        int pos_f = 0;
        int pos_f_f = datos.length;

        // Buscar punto inicial
        for (int i = 0; i < datos.length && !aux_i; i++) {
            aux_i = busquedaPuntoInicial(magnitud[i]);
            pos_i = i;
        }

        if (pos_i > 10) {
            pos_i -= 10;
        } else {
            pos_i = 0;
        }

        // Buscar punto final como inicial desde la ultima posicion
        for (int i = datos.length - 1; i > 1 && !aux_f_f; i--) {
            aux_f_f = busquedaPuntoInicial(magnitud[i]);
            pos_f_f = i;
        }

        int puntoFinal = 0;

        // Buscar punto final a partir del punto inicial
        int conta_aux = 0;
        do {
            for (int i = pos_i; i < datos.length &&/*!aux_f*/ puntoFinal < limitePuntoFinal; i++) {
                aux_f = busquedaPuntoFinal(magnitud[i]);
                if (aux_f) {
                    puntoFinal++;
                } else {
                    puntoFinal = 0;
                }

                pos_f = i;//(i - limitePuntoFinal) + 3;
            }
            if ((pos_f + 1) == datos.length) {
                ITU *= 1.5;
            }
            ITL *= 1.5;
            System.out.println("Pos_f :" + (pos_f + 1) + "\t length:" + datos.length + "\t" + pos_f_f);
            if (Main.DEBUG) {
                grafica_M.limite(ITL, ITU);
            }
            conta_aux++;
        } while ((pos_f + 1) == datos.length && conta_aux < 3);

        if (pos_f > datos.length) {
            pos_f = datos.length - 1;
        }
        if (pos_f < pos_i) {
            pos_f = 0;
        }

        if (Math.abs(pos_f - pos_i) < 130 && datos.length > 130) {
            pos_f = datos.length - 10;
        }
        // Grafica la informacion mostrando donde es el punto inicial y final
        if (Main.DEBUG) {
            for (int i = 0; i < datos.length; i++) {
                if (pos_i == i) {
                    grafica_M.graficar(-10.0);
                } else if (((pos_f + pos_f_f) / 2) == i) {
                    grafica_M.graficar(-10.0);
                } else {
                    grafica_M.graficar(magnitud[i]);
                }
            }
        }
        pos_f = ((pos_f + pos_f_f) / 2);
        /*for(int i=0; i<datos.length; i++)
		{
			if(pos_i == i){
				grafica_C.graficar(-10);
			}else if(pos_f == i){
				grafica_C.graficar(-10);
			}else{
				grafica_C.graficar(cruces[i]);
			}
		}*/

        aux_datos = new double[pos_f - pos_i][VENTANA];

        for (int i = pos_i; i < pos_f; i++) {
            aux_datos[i - pos_i] = datos[i];
        }
        wORDBuffer.agregar(aux_datos);
    }

    public WORDBuffer getDatos() {
        return this.wORDBuffer;
    }

    public void run() {
        System.out.println("[Detector de bordes iniciado]");

        double vector[] = new double[VENTANA];// datos sin modificar
        double trama[] = new double[VENTANA];// datos modificados
        double vectorSolapamiento[] = new double[SOLAPAMIENTO_VENTANA];
        double matrixSalida[][] = new double[0][];
        int contadorSeñal = 0;

        double senal = 0.0, senalAnterior = 0.0;

        boolean busquedaPuntoInicial = true;
        boolean aux_i = false;
        boolean aux_f = false;
        double ZCR;
        double M;
        int contadorPuntoFinal = 0;

        do {
            if (pCMBuffer.vacio()) {
                /**
                 * Normalizar
                 */
                senal = pCMBuffer.leer();
                /**
                 * Pre enfasis
                 */
                senal = senal - (0.92 * senalAnterior);
                senalAnterior = senal;
                /**
                 * Make Ventana
                 */
                vector[contadorSeñal] = senal;
                senal = 0.0;
                contadorSeñal++;
            } else {
                System.out.println("No hay datos");
                thread.yield();
            }
        } while (contadorSeñal < vector.length);

        System.arraycopy(vector, 0, trama, 0, vector.length);

        aplicarHamming(trama);
        M = Calculo.magnitud(trama);
        ZCR = Calculo.crucesPorCero_sgn(trama);

        for (int i = 0; i < umbralAutomatico.length; i++) {
            setUmbralAutomatico(M);
        }

        double tempZCR[] = new double[10];
        for (int i = 0; i < tempZCR.length; i++) {
            tempZCR[i] = ZCR;
        }
        estadisticaCrucesPorCero(tempZCR);

        /* copiar solapamiento de ventana n */
        System.arraycopy(vector, INCREMENTO_VENTANA, vectorSolapamiento, 0, SOLAPAMIENTO_VENTANA);

        /*
		 *	Ciclo infinito o hasta que el usuario "stop"
		 *	detector de se�al en el medio
		 *
         */
        while (!thread.isInterrupted()) {
            vector = null;
            trama = null;
            vector = new double[VENTANA];
            trama = new double[VENTANA];
            senal = 0.0;

            /* pegar solapamiento de ventana n-1 */
            System.arraycopy(vectorSolapamiento, 0, vector, 0, SOLAPAMIENTO_VENTANA);
            for (contadorSeñal = SOLAPAMIENTO_VENTANA; contadorSeñal < VENTANA;) {
                if (pCMBuffer.datos()) {
                    /**
                     * Normalizar
                     */
                    senal = pCMBuffer.leer();
                    /**
                     * Pre enfasis
                     */
                    senal = senal - 0.92 * senalAnterior;
                    senalAnterior = senal;
                    /**
                     * Make Ventana
                     */
                    vector[contadorSeñal] = senal;
                    contadorSeñal++;
                } else {
                    if (!thread.isInterrupted()) {
                        thread.yield();
                    } else {
                        break;
                    }
                }
            }
            if (thread.isInterrupted()) {
                break;
            }
            /* copiar solapamiento de ventana n */
            System.arraycopy(vector, INCREMENTO_VENTANA, vectorSolapamiento, 0, SOLAPAMIENTO_VENTANA);

            System.arraycopy(vector, 0, trama, 0, vector.length);

            aplicarHamming(trama);

            M = Calculo.magnitud(trama);
            ZCR = Calculo.crucesPorCero_sgn(trama);

            if (busquedaPuntoInicial) {
                setUmbralAutomatico((M + 0.1) * 1.9D);
            }

            aux_i = busquedaPuntoInicial(M);
            aux_f = busquedaPuntoFinal(M);
            if (Main.DEBUG) {
                grafica_M.graficar(M);
                grafica_M.limite(ITL, ITU);
                grafica_C.graficar(ZCR);
            }
            if (aux_i && busquedaPuntoInicial) {
                busquedaPuntoInicial = false;
            }

            if (aux_f && !busquedaPuntoInicial) {
                contadorPuntoFinal++;
                if (contadorPuntoFinal >= limitePuntoFinal) {
                    busquedaPuntoInicial = true;
                    detector((double[][]) matrixSalida.clone());
                    //wORDBuffer.agregar((double[][])matrixSalida.clone());
                    matrixSalida = null;
                    matrixSalida = new double[0][0];
                    System.gc();
                }
            }
            if (!aux_f && !busquedaPuntoInicial) {
                contadorPuntoFinal = 0;
            }

            if (!busquedaPuntoInicial) {
                matrixSalida = Util.copiar(matrixSalida, trama);
                if (matrixSalida.length > 600)//500//450//380//100
                {
                    busquedaPuntoInicial = true;
                    matrixSalida = null;
                    matrixSalida = new double[0][0];
                    System.out.println("Error: Palabra demasiado Larga");
                    Main.mensageError("( ( Voz ) )", "Palabra pronunciada demasiado larga");
                    setUmbralAutomatico(ITU * 10.D);
                    System.gc();
                } else {
                    if (Main.DEBUG) {
                        grafica_M.setResetFactor(M);
                    }
                }
            } else {
                matrixSalida = Util.matrixTemp(matrixSalida, trama, 20);
            }
        }//FIN WHILE
        System.out.println("[Decteccion de bordes finalizada]");
    }

    public boolean busquedaPuntoInicial(final double valor) {
        return valor > ITL ? valor < ITU ? false : valor > ITU ? true : false : false;
    }

    public boolean busquedaPuntoFinal(final double valor) {
        return valor > ITL ? valor > ITU ? false : valor < ITU ? false : false : true;
    }

    private int contadorUmbralAutomatico = 0;
    private double umbralAutomatico[] = new double[350];

    private void setUmbralAutomatico(double valor) {
        if (contadorUmbralAutomatico < umbralAutomatico.length) {
            umbralAutomatico[contadorUmbralAutomatico] = valor;
            contadorUmbralAutomatico++;
        } else {
            contadorUmbralAutomatico = 0;
            umbralAutomatico[contadorUmbralAutomatico] = valor;
        }
        estadisticaSonido(umbralAutomatico);
    }

    public void estadisticaCrucesPorCero(final double v[]) {
        double promCrucesPorCero = Calculo.promedioCrucesporCero(v);
        double desCrucesPorCero = Calculo.desviacionEstandar(v, promCrucesPorCero);

        IZCT = Math.min(LI_IZCT, promCrucesPorCero + 2 * promCrucesPorCero);

        System.out.println("[Estadisticas promCrucesPorCero ] " + promCrucesPorCero);
        System.out.println("[Estadisticas desCrucesPorCero ] " + desCrucesPorCero);
        System.out.println("[Estadisticas IZCT ] " + IZCT);
    }

    public void estadisticaSonido(final double v[]) {
        double IMX = Calculo.max(v);
        double IMN = Calculo.min(v);
        if (IMX == IMN) {
            IMN = 0.0D;
        }
        if (IMN == 0.0D) {
            IMN = Math.log10(IMX) / 2.0D;
        }
        double I1 = Math.abs((0.03 * (IMX - IMN)) + IMN);
        double I2 = Math.abs(4.0 * IMN);
        ITL = Math.min(I1, I2);
        ITU = 4.0 * ITL;

        /*System.out.println("[Estadisticas IMX ] "+IMX);
		System.out.println("[Estadisticas IMN ] "+IMN);
		System.out.println("[Estadisticas I1 ] "+I1);
		System.out.println("[Estadisticas I2 ] "+I2);*/
        //System.out.println("[Estadisticas ITL ] "+ITL);
        //System.out.println("[Estadisticas ITU ] "+ITU);
    }

    public synchronized void stop() {
        if (Main.DEBUG) {
            grafica_M.remove();
            grafica_C.remove();
        }
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

    private void aplicarHamming(double[] dat) {
        for (int j = 0; j < dat.length; j++) {
            dat[j] *= Calculo.Hamming(j);
        }
    }

    public double[][] hacerVentanas(double[] dat) {
        return hacerVentanas(dat, Audio.getWindowSize(), Audio.getWindowIncreaseSize());
    }

    private double[][] hacerVentanas(double[] cadena, int tamV, int avance) {
        int numV = 1;
        for (int i = tamV; i < cadena.length - tamV; i += avance) {
            numV++;
        }
        double[][] ventana = new double[numV][tamV];
        for (int i = 0; i < numV; i++) {
            for (int j = 0; j < tamV; j++) {
                ventana[i][j] = cadena[i * avance + j];
            }
        }
        return ventana;
    }

    private double[][] aplicarVentana(double[][] datos) {
        double aux_datos[][] = new double[datos.length][datos[0].length];
        for (int i = 0; i < aux_datos.length; i++) {
            for (int j = 0; j < aux_datos[i].length; j++) {
                aux_datos[i][j] = datos[i][j] * Calculo.Hamming(j);
            }
        }
        return aux_datos;
    }
}
