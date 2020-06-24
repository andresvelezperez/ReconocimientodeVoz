/*
 *	Paquete				: espectro
 *	Archivo				: FFTCPP.java
 *	Version                         : 1.0.0		2007-03-31
 *	Autor				: Dominic Mazzoni (Audacity (audacity-src-1.2.4b))
 *	URL					: http://www.intersrv.com/~dcross/fft.html (disponible en: http://audacity.sourceforge.net/)
 *	Copyright			: Audacity, GNU public license (GPL).
 *	Version				: audacity-src-1.2.4b
 *
 *	Este codigo esta basado en una implementacion libre de una
 *	FFT realizado por Audacity.
 *
 *	Todos los derechos reservados.  Usar bajo terminos de la licencia.
 * 
 *	Ver archivo "licencia.txt"(terceros) para informacion de como usar
 *	y distrubuir este archvio, y para garantizar las responsabilidades 
 *	adquiridas.
 */
package com.avpsoft.icvoz.espectro;

/**
 * ********************************************************************
 * FFT.cpp
 *
 * Dominic Mazzoni
 *
 * September 2000
 *
 * This file contains a few FFT routines, including a real-FFT routine that is
 * almost twice as fast as a normal complex FFT, and a power spectrum routine
 * when you know you don't care about phase information.
 *
 * Some of this code was based on a free implementation of an FFT by Don Cross,
 * available on the web at:
 *
 * http://www.intersrv.com/~dcross/fft.html
 *
 * The basic algorithm for his code was based on Numerican Recipes in Fortran. I
 * optimized his code further by reducing array accesses, caching the bit
 * reversal table, and eliminating double-to-double conversions, and I added the
 * routines to calculate a real FFT and a real power spectrum.
 *
 *********************************************************************
 */
import static java.lang.Math.*;

public class FFT {

    private int[][] gFFTBitTable;// = NULL;
    private final int MaxFastBits = 16;

    private boolean isPowerOfTwo(int x) {
        if (x < 2) {
            return false;
        }
        if (x == (x - 1)) /* Thanks to 'byang' for this cute trick! */ {
            return false;
        }
        return true;
    }

    private int numberOfBitsNeeded(int PowerOfTwo) {
        int i;

        if (PowerOfTwo < 2) {
            System.out.printf("Error: FFT called with size %d\n", PowerOfTwo);
            System.exit(1);
        }

        for (i = 0;; i++) {
            if (PowerOfTwo == (1 << i)) {
                return i;
            }
        }
    }

    private int reverseBits(int index, int NumBits) {
        int i, rev;

        for (i = rev = 0; i < NumBits; i++) {
            rev = (rev << 1) | (index & 1);
            index >>= 1;
        }

        return rev;
    }

    private void initFFT() {
        gFFTBitTable = new int[MaxFastBits][];
        int len = 2;
        for (int b = 1; b <= MaxFastBits; b++) {
            gFFTBitTable[b - 1] = new int[len];
            for (int i = 0; i < len; i++) {
                gFFTBitTable[b - 1][i] = reverseBits(i, b);
            }

            len <<= 1;
        }
    }

    private int fastReverseBits(int i, int NumBits) {
        if (NumBits <= MaxFastBits) {
            return gFFTBitTable[NumBits - 1][i];
        } else {
            return reverseBits(i, NumBits);
        }
    }

    /*
	 * Complex Fast Fourier Transform
     */
    private void FFT(int NumSamples, boolean InverseTransform,
            double[] RealIn, double[] ImagIn, double[] RealOut, double[] ImagOut) {
        int NumBits;
        /* Number of bits needed to store indices */
        int i, j, k, n;
        int BlockSize, BlockEnd;

        double angle_numerator = 2.0 * PI;
        double tr, ti;
        /* temp real, temp imaginary */

        if (!isPowerOfTwo(NumSamples)) {
            System.out.printf("%d is not a power of two\n", NumSamples);
            System.exit(1);
        }

        if (gFFTBitTable == null) {
            initFFT();
        }

        if (InverseTransform) {
            angle_numerator = -angle_numerator;
        }

        NumBits = numberOfBitsNeeded(NumSamples);

        /*
	    **   Do simultaneous data copy and bit-reversal ordering into outputs...
         */
        for (i = 0; i < NumSamples; i++) {
            j = fastReverseBits(i, NumBits);
            RealOut[j] = RealIn[i];
            ImagOut[j] = (ImagIn == null) ? 0.0 : ImagIn[i];
        }

        /*
	    **   Do the FFT itself...
         */
        BlockEnd = 1;
        for (BlockSize = 2; BlockSize <= NumSamples; BlockSize <<= 1) {

            double delta_angle = angle_numerator / (double) BlockSize;

            double sm2 = sin(-2 * delta_angle);
            double sm1 = sin(-delta_angle);
            double cm2 = cos(-2 * delta_angle);
            double cm1 = cos(-delta_angle);
            double w = 2 * cm1;
            double ar0, ar1, ar2, ai0, ai1, ai2;

            for (i = 0; i < NumSamples; i += BlockSize) {
                ar2 = cm2;
                ar1 = cm1;

                ai2 = sm2;
                ai1 = sm1;

                for (j = i, n = 0; n < BlockEnd; j++, n++) {
                    ar0 = w * ar1 - ar2;
                    ar2 = ar1;
                    ar1 = ar0;

                    ai0 = w * ai1 - ai2;
                    ai2 = ai1;
                    ai1 = ai0;

                    k = j + BlockEnd;
                    tr = ar0 * RealOut[k] - ai0 * ImagOut[k];
                    ti = ar0 * ImagOut[k] + ai0 * RealOut[k];

                    RealOut[k] = RealOut[j] - tr;
                    ImagOut[k] = ImagOut[j] - ti;

                    RealOut[j] += tr;
                    ImagOut[j] += ti;
                }
            }

            BlockEnd = BlockSize;
        }

        /*
	      **   Need to normalize if inverse transform...
         */
        if (InverseTransform) {
            double denom = (double) NumSamples;

            for (i = 0; i < NumSamples; i++) {
                RealOut[i] /= denom;
                ImagOut[i] /= denom;
            }
        }
    }

    /*
	 * Real Fast Fourier Transform
	 *
	 * This function was based on the code in Numerical Recipes in C.
	 * In Num. Rec., the inner loop is based on a single 1-based array
	 * of interleaved real and imaginary numbers.  Because we have two
	 * separate zero-based arrays, our indices are quite different.
	 * Here is the correspondence between Num. Rec. indices and our indices:
	 *
	 * i1  <->  real[i]
	 * i2  <->  imag[i]
	 * i3  <->  real[n/2-i]
	 * i4  <->  imag[n/2-i]
     */
    private void realFFT(int NumSamples, double[] RealIn, double[] RealOut, double[] ImagOut) {
        int Half = NumSamples / 2;
        int i;

        double theta = PI / Half;

        double[] tmpReal = new double[Half];
        double[] tmpImag = new double[Half];

        for (i = 0; i < Half; i++) {
            tmpReal[i] = RealIn[2 * i];
            tmpImag[i] = RealIn[2 * i + 1];
        }

        FFT(Half, false, tmpReal, tmpImag, RealOut, ImagOut);

        double wtemp = (double) (sin(0.5 * theta));

        double wpr = -2.0 * wtemp * wtemp;
        double wpi = (double) (sin(theta));
        double wr = 1.0 + wpr;
        double wi = wpi;

        int i3;

        double h1r, h1i, h2r, h2i;

        for (i = 1; i < Half / 2; i++) {

            i3 = Half - i;

            h1r = 0.5 * (RealOut[i] + RealOut[i3]);
            h1i = 0.5 * (ImagOut[i] - ImagOut[i3]);
            h2r = 0.5 * (ImagOut[i] + ImagOut[i3]);
            h2i = -0.5 * (RealOut[i] - RealOut[i3]);

            RealOut[i] = h1r + wr * h2r - wi * h2i;
            ImagOut[i] = h1i + wr * h2i + wi * h2r;
            RealOut[i3] = h1r - wr * h2r + wi * h2i;
            ImagOut[i3] = -h1i + wr * h2i + wi * h2r;

            wr = (wtemp = wr) * wpr - wi * wpi + wr;
            wi = wi * wpr + wtemp * wpi + wi;
        }

        RealOut[0] = (h1r = RealOut[0]) + ImagOut[0];
        ImagOut[0] = h1r - ImagOut[0];
    }

    /*
	 * PowerSpectrum
	 *
	 * This function computes the same as RealFFT, above, but
	 * adds the squares of the real and imaginary part of each
	 * coefficient, extracting the power and throwing away the
	 * phase.
	 *
	 * For speed, it does not call RealFFT, but duplicates some
	 * of its code.
     */
    private void powerSpectrum(int NumSamples, double[] In, double[] Out) {
        int Half = NumSamples / 2;
        int i;

        double theta = PI / Half;

        double[] tmpReal = new double[Half];
        double[] tmpImag = new double[Half];
        double[] RealOut = new double[Half];
        double[] ImagOut = new double[Half];

        for (i = 0; i < Half; i++) {
            tmpReal[i] = In[2 * i];
            tmpImag[i] = In[2 * i + 1];
        }

        FFT(Half, false, tmpReal, tmpImag, RealOut, ImagOut);

        double wtemp = (double) (sin(0.5 * theta));

        double wpr = -2.0 * wtemp * wtemp;
        double wpi = (double) (sin(theta));
        double wr = 1.0 + wpr;
        double wi = wpi;

        int i3;

        double h1r, h1i, h2r, h2i, rt, it;

        for (i = 1; i < Half / 2; i++) {

            i3 = Half - i;

            h1r = 0.5 * (RealOut[i] + RealOut[i3]);
            h1i = 0.5 * (ImagOut[i] - ImagOut[i3]);
            h2r = 0.5 * (ImagOut[i] + ImagOut[i3]);
            h2i = -0.5 * (RealOut[i] - RealOut[i3]);

            rt = h1r + wr * h2r - wi * h2i;
            it = h1i + wr * h2i + wi * h2r;

            Out[i] = (float) (rt * rt + it * it);

            rt = h1r - wr * h2r + wi * h2i;
            it = -h1i + wr * h2i + wi * h2r;

            Out[i3] = (float) (rt * rt + it * it);

            wr = (wtemp = wr) * wpr - wi * wpi + wr;
            wi = wi * wpr + wtemp * wpi + wi;
        }

        rt = (h1r = RealOut[0]) + ImagOut[0];
        it = h1r - ImagOut[0];
        Out[0] = (float) (rt * rt + it * it);

        rt = RealOut[Half / 2];
        it = ImagOut[Half / 2];
        Out[Half / 2] = (float) (rt * rt + it * it);

        tmpReal = null;
        tmpImag = null;
        RealOut = null;
        ImagOut = null;
    }

    /*
	 * Windowing Functions
     */
    public static int numWindowFuncs() {
        return 4;
    }

    public static final String windowFuncName(int whichFunction) {
        switch (whichFunction) {
            default:
            case 0:
                return "Rectangular";
            case 1:
                return "Bartlett";
            case 2:
                return "Hamming";
            case 3:
                return "Hanning";
        }
    }

    public static void windowFunc(int whichFunction, int NumSamples, double[] in) {
        int i;

        if (whichFunction == 1) {
            // Bartlett (triangular) window
            for (i = 0; i < NumSamples / 2; i++) {
                in[i] *= (i / (double) (NumSamples / 2));
                in[i + (NumSamples / 2)]
                        *= (1.0 - (i / (double) (NumSamples / 2)));
            }
        }

        if (whichFunction == 2) {
            // Hamming
            for (i = 0; i < NumSamples; i++) {
                in[i] *= 0.54 - 0.46 * cos(2 * PI * i / (NumSamples - 1));
            }
        }

        if (whichFunction == 3) {
            // Hanning
            for (i = 0; i < NumSamples; i++) {
                in[i] *= 0.50 - 0.50 * cos(2 * PI * i / (NumSamples - 1));
            }
        }
    }

    public double[] process(double[] sn) {
        int numSamples = sn.length;
        boolean it = false;

        double realIn[] = new double[numSamples]; //sn;
        System.arraycopy(sn, 0, realIn, 0, sn.length);
        double imagIn[] = new double[numSamples];

        double realOut[] = new double[numSamples];
        double imagOut[] = new double[numSamples];

        this.FFT(numSamples, it, realIn, imagIn, realOut, imagOut);

        double result[] = new double[numSamples / 2];

        for (int i = 0; i < result.length; i++) {
            result[i] = hypot(realOut[i], imagOut[i]);
        }
        return result;
    }
}
