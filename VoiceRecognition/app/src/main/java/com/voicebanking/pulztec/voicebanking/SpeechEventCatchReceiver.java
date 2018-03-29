package com.voicebanking.pulztec.voicebanking;
/**
 * Created by Nimantha on 2/9/2017.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;

public class SpeechEventCatchReceiver extends BroadcastReceiver {
    public SpeechEventCatchReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        RecognizerIntent recognizerIntentInfo=intent.getParcelableExtra(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        if(recognizerIntentInfo!=null){
            //TODO: fire speech recongnition action
        }
    }
}
