package com.example.tunergitarowy.algorithms;


import android.util.Log;

import com.example.tunergitarowy.recording.RecordingThread;

public final class HarmonicProductSpectrum {
    private static final String LOG_TAG = RecordingThread.class.getSimpleName();
    private static final int window_size = 32768;
    private int maxIndex;
    private FFT fft;

    public HarmonicProductSpectrum(){
        this.fft = new FFT(window_size);
    }

    public static void HanningWindow(short[] signal_in, short[] signal_out, int pos, int size)
    {
        for (int i = pos; i < pos + size; i++)
        {
            int j = i - pos; // j = index into Hann window function
            signal_out[i] = (short) (signal_in[i] * 0.5 * (1.0 - Math.cos(2.0 * Math.PI * j / size)));
        }
    }

    public static double[] copyFromShortArray(short[] source) {
        double[] dest = new double[source.length];
        for(int i=0; i<source.length; i++) {
            dest[i] = source[i];
        }
        return dest;
    }

    public static void calcHarmonicProductSpectrum(double[] mag, double[] hps, int order) {
        if(mag.length != hps.length) {
            Log.e(LOG_TAG, "calcHarmonicProductSpectrum: mag[] and hps[] have to be of the same length!");
            throw new IllegalArgumentException("mag[] and hps[] have to be of the same length");
        }

        // initialize the hps array
        int hpsLength = mag.length / (order+1);
        for (int i = 0; i < hps.length; i++) {
            if(i < hpsLength)
                hps[i] = mag[i];
            else
                hps[i] = Float.NEGATIVE_INFINITY;
        }

        // do every harmonic in a big loop:
        for (int harmonic = 1; harmonic <= order; harmonic++) {
            int downsamplingFactor = harmonic + 1;
            for (int index = 0; index < hpsLength; index++) {
                // Calculate the average (downsampling):
                float avg = 0;
                for (int i = 0; i < downsamplingFactor; i++) {
                    avg += mag[index*downsamplingFactor + i];
                }
                hps[index] += avg / downsamplingFactor;
            }
        }
    }

    public double CalculateHPS(short[] data) {
        short[] samples;
        samples = data;
        short[] signal_out = new short[window_size];
        double[] spectrum = new double[window_size];
        double[] hps = new double[window_size];
        HanningWindow(samples, signal_out, 0, window_size);
        double[] doubles = copyFromShortArray(signal_out);
        fft.fft(doubles, spectrum);

        for (int i = 0; i < spectrum.length; i++) {
            doubles[i] = Math.abs(spectrum[i]);
        }
        calcHarmonicProductSpectrum(doubles, hps, 5);
        int maxIndex = 0;
        for (int i = 1; i < hps.length; i++) {
            if (hps[maxIndex] < hps[i])
                maxIndex = i;
        }

        double freq = ((double) maxIndex / (double) samples.length) * 44100;


        return freq;
    }
}
