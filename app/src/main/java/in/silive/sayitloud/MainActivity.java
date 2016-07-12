package in.silive.sayitloud;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MediaRecorder recorder;
    AudioTrack track;
    AudioRecord record;
    File audiofile = null;
    private static final String TAG = "SoundRecordingActivity";
    private Button startButton;
    private Button stopButton;
    private Button playButton;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(this);
        playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startButton:
                startButton.setEnabled(false);
                stopButton.setEnabled(true);

                File sampleDir = Environment.getExternalStorageDirectory();
                try {
                    audiofile = File.createTempFile("sound", ".3gp", sampleDir);
                } catch (IOException e) {
                    Log.e(TAG, "sdcard access error");
                    return;
                }
                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                recorder.setOutputFile(audiofile.getAbsolutePath());
                try {
                    recorder.prepare();
                } catch (IOException e) {
                    Log.e(TAG, "record  error");
                }

                recorder.start();
                break;
            case R.id.stopButton:
                flag = 2;
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                recorder.stop();
                recorder.release();
                addRecordingToMediaLibrary();
                break;
            case R.id.playButton:
                if (flag != 2) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Dialog_play dialog = new Dialog_play();
                    dialog.show(fragmentManager.beginTransaction(), "Play Dialog");

                } else {
                    startButton.setEnabled(false);
                    stopButton.setEnabled(false);

                    AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    Uri uri = Uri.fromFile(audiofile);
                    intent.setDataAndType(uri, "audio/*");
                    startActivity(Intent.createChooser(intent, "Play Loud Audio "));
                    startButton.setEnabled(true);
                    stopButton.setEnabled(true);
                }
                break;




        }

    }

    protected void addRecordingToMediaLibrary() {
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
        values.put(MediaStore.Audio.Media.TITLE, "audio" + audiofile.getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
        values.put(MediaStore.Audio.Media.DATA, audiofile.getAbsolutePath());
        ContentResolver contentResolver = getContentResolver();

        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(base, values);

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
        Toast.makeText(this, "Added File " + newUri, Toast.LENGTH_LONG).show();
    }
}
