package com.example.tunergitarowy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.content.pm.PackageManager;

import com.example.tunergitarowy.recording.AudioDataReceivedListener;
import com.example.tunergitarowy.R;
import com.example.tunergitarowy.recording.RecordingThread;

public class TunerActivity extends AppCompatActivity {

    private RecordingThread recordingThread;
    private TunerView tunerView;
    private int pitchIndex;

    private static final int REQUEST_RECORD_AUDIO = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuner);

        tunerView = (TunerView) findViewById(R.id.tunerView);

        pitchIndex = getIntent().getIntExtra(MainActivity.EXTRA_INT, 0);
        tunerView.setPitchIndex(pitchIndex);

        recordingThread = new RecordingThread(new AudioDataReceivedListener() {
            @Override
            public void onAudioDataReceived(short[] data) {
                tunerView.transferSamples(data);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        startAudioRecordingSafe();
    }

    @Override
    protected void onStop(){
        super.onStop();
        this.recordingThread.stopRecording();
    }

    private void startAudioRecordingSafe() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            recordingThread.startRecording();
        } else {
            requestMicrophonePermission();
        }
    }

    private void requestMicrophonePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.RECORD_AUDIO)) {
            ActivityCompat.requestPermissions(TunerActivity.this, new String[]{
                    android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
        } else {
            ActivityCompat.requestPermissions(TunerActivity.this, new String[]{
                    android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            recordingThread.stopRecording();
        }
    }

}
