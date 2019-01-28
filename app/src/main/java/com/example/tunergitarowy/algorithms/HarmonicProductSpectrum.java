package com.example.tunergitarowy.algorithms;

public final class HarmonicProductSpectrum {

    private static final int window_size = 32768;
    private FFT fft;

    public HarmonicProductSpectrum(){
        this.fft = new FFT(window_size);
    }

    private static void HanningWindow(short[] signal_in, double[] signal_out, int pos, int size)
    {
        for (int i = pos; i < pos + size; i++)
        {
            int j = i - pos; // j = index into Hann window function
            signal_out[i] =   (signal_in[i] * 0.5 * (1.0 - Math.cos(2.0 * Math.PI * j / size)));
        }
    }


    private static void CalcHarmonicProductSpectrum(double[] mag, double[] hps, int order) {

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
        double[] spectrum = new double[window_size];
        double[] hps = new double[window_size/2];
        double[] module = new double[window_size/2];
        double[] doubles =  new double[window_size];
        HanningWindow(samples, doubles, 0, window_size);
        fft.fft(doubles, spectrum);

        for (int i = 0; i < module.length; i++) {
            module[i] = Math.sqrt(doubles[i]*doubles[i] + spectrum[i]*spectrum[i]);
        }
        CalcHarmonicProductSpectrum(module, hps, 5);
        int maxIndex = 0;
        for (int i = 1; i < hps.length; i++) {
            if (hps[maxIndex] < hps[i])
                maxIndex = i;
        }

        return ((double) maxIndex / (double) samples.length) * 8192;

    }
}

