package com.bigelbrus.racegame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {
    private static AtomicBoolean isFirst = new AtomicBoolean(true);
    private RaceParticipant first;
    private RaceParticipant second;
    private static FileWriter writer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFile();
        initThreads();
    }

    public void startRace(View v) {
        first.start();
        second.start();
        v.setEnabled(false);
        findViewById(R.id.ready).setVisibility(View.VISIBLE);
    }

    private void initThreads() {
        first = new RaceParticipant("Поток А");
        second = new RaceParticipant("Поток Б");
    }

    private void initFile() {
        File file = new File(MainActivity.this.getFilesDir(), "text");
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            File writeTo = new File(file, "results.txt");
            writer = new FileWriter(writeTo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static class RaceParticipant extends Thread {
        private String threadName;

        RaceParticipant(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run() {
            try {
                for (int i = 1; i < 101; i++) {
                    write(String.valueOf(i));
                }
                if (isFirst.get()) {
                    write("WIN");
                    isFirst.set(false);
                } else {
                    write("LOSE");
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void write(String s) throws IOException {
                writer.write(threadName + " " + s + "\n");
        }
    }
}
