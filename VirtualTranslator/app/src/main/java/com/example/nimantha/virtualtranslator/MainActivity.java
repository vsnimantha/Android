package com.example.nimantha.virtualtranslator;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nimantha.virtualtranslator.TextOverlay.CameraView;

public class MainActivity extends AppCompatActivity {
    Button aug,userman,voice;
    TextView status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        aug=(Button)findViewById(R.id.btnaug);
        userman=(Button)findViewById(R.id.btnusrman);
        voice=(Button)findViewById(R.id.btnvoice);

        status=(TextView)findViewById(R.id.txtviewstatus);

        if(isNetworkConnected()==true){
            status.setText("Online");
        }else{

        }

        aug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(MainActivity.this,CameraView.class);
//                startActivity(intent);
//
                Log.i("MMMM","LKJ");
                //Toast.makeText(MainActivity.this, "aasadasa", Toast.LENGTH_SHORT).show();
            }
        });
        userman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,CaptureActivity.class);
                startActivity(intent);
            }
        });
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,VoiceTranslator.class);
                startActivity(intent);
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
