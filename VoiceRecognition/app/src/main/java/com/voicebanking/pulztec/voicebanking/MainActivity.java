package com.voicebanking.pulztec.voicebanking;
/**
 * Created by Nimantha on 2/9/2017.
 */
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity implements RecognitionListener {

    private TextView returnedText,answersText;
    private ToggleButton toggleButton;
    private SpeechRecognizer speechRecognizer = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "MainActivity";

    private SqliteDB sqliteDB;
    private AudioManager audioManager;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    Cursor cursorResults;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        returnedText = (TextView) findViewById(R.id.textView1);
        answersText=(TextView)findViewById(R.id.txtAnswers);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);

        sqliteDB=new SqliteDB(MainActivity.this);


        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);


        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    speechRecognizer.startListening(recognizerIntent);
                } else {
                    speechRecognizer.stopListening();
                }
            }
        });

        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }else{
                    Log.e("TextToSpeech","Error status="+String.valueOf(status));
                }
            }
        });

        //ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        /*int permissionStatus=ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);
        if(permissionStatus==-1){
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        }*/

        Button btnspeak=(Button)findViewById(R.id.btn_speak);
        btnspeak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    if(SpeechRecognizer.isRecognitionAvailable(MainActivity.this)) {
                        speechRecognizer.startListening(recognizerIntent);
                        return true;
                    }
                }else{
                    speechRecognizer.stopListening();
                }
                /*else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                    return false;
                }*/
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
            Log.i(LOG_TAG, "destroy");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    //region Speech Recongizer Functions
    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {
        toggleButton.setChecked(false);
    }

    @Override
    public void onError(int i) {
        /*String errorMessage = getErrorText(error);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
        returnedText.setText(errorMessage);
        toggleButton.setChecked(false);*/
    }

    @Override
    public void onResults(Bundle bundle) {
        Log.i(LOG_TAG, "onResults");
        returnedText.setText("");

        ArrayList<String> matches = bundle
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";

        for (String result : matches)
        {
            text=result;
            //text += result + "\n";
        }

       /* char [] chars=text.toCharArray();

        for (char c:chars) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            returnedText.append(String.valueOf(c));
        }*/

        returnedText.setText(text);
        aiAnswering(text);
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

    //endregion

    /**
     * handles the answering process
     * @param result take the recognized string
     */
    private void aiAnswering(String result){
        if(!result.equals("")){
            try {
                cursorResults = sqliteDB.getAllFromTable1(result);
                String answer = "";
                if (cursorResults.getCount() > 0) {
                    while (cursorResults.moveToNext()) {
                        answer = cursorResults.getString(2);
                        if (textToSpeech != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                //answersText.setText(answer);
                                char[] chars = answer.toCharArray();
                                answersText.setText("");
                                for (char c : chars) {
                                    answersText.append(String.valueOf(c));
                                }
                                textToSpeech.speak(cursorResults.getString(2), TextToSpeech.QUEUE_FLUSH, null, null);
                            } else {
                                //answersText.setText(answer);
                                char[] chars = answer.toCharArray();
                                answersText.setText("");
                                for (char c : chars) {
                                    answersText.append(String.valueOf(c));
                                }
                                textToSpeech.speak(cursorResults.getString(2), TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }
                    }
                } else {
                    result = "I am sorry I don't know";
                    answersText.setText("");
                    char[] chars = result.toCharArray();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        textToSpeech.speak(result, TextToSpeech.QUEUE_FLUSH, null, null);
                        for (char c : chars) {
                            answersText.append(String.valueOf(c));
                        }
                    } else {
                        answersText.setText("");
                        textToSpeech.speak(result, TextToSpeech.QUEUE_FLUSH, null);
                        for (char c : chars) {
                            answersText.append(String.valueOf(c));
                        }
                    }
                }
            }catch (Exception e){
                Log.e("AnsweringAI",e.getMessage());
            }
        }
    }
}
