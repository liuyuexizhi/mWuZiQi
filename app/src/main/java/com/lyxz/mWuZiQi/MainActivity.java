package com.lyxz.mWuZiQi;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button clearPanel;
    private WuziqiPanel panel;
    private Button clear_Restart;
    private MediaPlayer mediaPlayer;
    private Button bt_stopMusic;
    private boolean flag = true;

    private Button bt_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_stopMusic = (Button) findViewById(R.id.id_music);
        bt_exit = (Button) findViewById(R.id.id_exit);

        bt_stopMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!flag) {
                    mediaPlayer.release();
                    flag = true;
                } else {
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.alarm);
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                    flag = false;
                }
            }
        });

        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });

        clear_Restart = (Button) findViewById(R.id.clear_restart);
        panel = (WuziqiPanel) findViewById(R.id.id_panel);

        clear_Restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                panel.showDialog("清盘重开");
            }
        });

    }
}