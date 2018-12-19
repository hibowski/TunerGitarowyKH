package com.example.tunergitarowy.algorithms;

import com.example.tunergitarowy.recording.RecordingThread;

public class Utils {
    private static final String LOG_TAG = RecordingThread.class.getSimpleName();
    private static final float CONCERT_PITCH = 440.0f;


    /**
     * converts a frequency (float) into an pitch index ( 0 is A0, 1 is A0#, 2 is B0, 3 is C1, ...).
     * This will round the frequency to the closest pitch index.
     *
     * @param frequency		frequency in Hz
     * @return pitch index
     */
    public static int frequencyToPitchIndex(float frequency) {
        float A1 = CONCERT_PITCH / 8;
        return Math.round((float) (12 * Math.log(frequency / A1) / Math.log(2)));
    }

    /**
     * returns the corresponding frequency (in Hz) for a given pitch index
     * @param index			pitch index ( 0 is A0, 1 is A0#, 2 is B0, 3 is C1, ...)
     * @return frequency in Hz
     */
    public static float pitchIndexToFrequency(int index) {
        float A1 = CONCERT_PITCH / 8;
        return (float) (A1 * Math.pow(2, index/12f));
    }
    public static String pitchLetterFromIndex(int index) {
        String letters;
        int octaveNumber = ((index+9) / 12) + 1;
        switch(index%12) {
            case 0:  letters = "A" + octaveNumber; break;
            case 1:  letters = "A" + octaveNumber + "#"; break;
            case 2:  letters = "H" + octaveNumber; break;
            case 3:  letters = "C" + octaveNumber; break;
            case 4:  letters = "C" + octaveNumber + "#"; break;
            case 5:  letters = "D" + octaveNumber; break;
            case 6:  letters = "D" + octaveNumber + "#"; break;
            case 7:  letters = "E" + octaveNumber; break;
            case 8:  letters = "F" + octaveNumber; break;
            case 9:  letters = "F" + octaveNumber + "#"; break;
            case 10: letters = "G" + octaveNumber; break;
            case 11: letters = "G" + octaveNumber + "#"; break;
            default: letters = "-";
        }
        return letters;
    }

}
