package anton.example.com.manager;

import android.content.res.AssetManager;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class Quote extends AppCompatActivity {
    private TextToSpeech mTTS;
    private Button mButtonStart;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        mTextView = findViewById(R.id.textView);
        mButtonStart = findViewById(R.id.buttonStart);

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    Locale locale = new Locale("ru");
                    int result = mTTS.setLanguage(locale);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        speak();
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakDate();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Quote.this.finish();
                    }
                }, 2000);
            }
        });
    }

    private void speak() {
        try {
            AssetManager assetManager = getAssets();
            InputStream fileInput = assetManager.open("text.txt");
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader buffer1 = new BufferedReader(reader);
            LineNumberReader lineNumberReader = new LineNumberReader(buffer1);
            try {
                lineNumberReader.skip(Long.MAX_VALUE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int linesInFile = lineNumberReader.getLineNumber() + 1;  // because line numbers starts from 0
            fileInput.close();

            ArrayList<String> ans = new ArrayList<String>();
            InputStream fileInput1 = assetManager.open("text.txt");
            InputStreamReader reader1 = new InputStreamReader(fileInput1);
            BufferedReader buffer = new BufferedReader(reader1);
            String lines;
            while ((lines = buffer.readLine()) != null) {
                ans.add(lines);
            }

            Random rand = new Random();
            int num = rand.nextInt(linesInFile);
            String quote = ans.get(num);
            mTextView.setText(quote);
            mTTS.speak(quote, TextToSpeech.QUEUE_FLUSH, null, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }

        super.onDestroy();
    }

    private void speakDate() {
        Calendar date = Calendar.getInstance();
        int hour = date.get(Calendar.HOUR_OF_DAY);
        final String greet;
        if (hour <= 3)
            greet = "Доброй ночи, товарищи!";
        else if (hour <= 11)
            greet = "Доброе утро, товарищи!";
        else if (hour <= 17)
            greet = "Добрый день, товарищи!";
        else greet = "Добрый вечер, товарищи!";

        mTTS.speak(greet, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}
