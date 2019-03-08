package anton.example.com.manager;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

import static java.lang.Math.round;

public class DailyMin extends AppCompatActivity {

    private TextView mTimeView;
    private EditText mEditPages;
    private EditText mEditUpToPage;
    private Button mButtonRes;
    private Button mButtonEnd;
    private TextView mTextView1;
    private TextView mTextView2;
    private TextView mTextView3;
    int last;
    int h;
    int m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_min);

        mTimeView = findViewById(R.id.timeView);
        mEditPages = findViewById(R.id.editPages);
        mEditUpToPage = findViewById(R.id.editUpToPage);
        mButtonRes = findViewById(R.id.buttonRes);
        mButtonEnd = findViewById(R.id.buttonEnd);
        mTextView1 = findViewById(R.id.textView1);
        mTextView2 = findViewById(R.id.textView2);
        mTextView3 = findViewById(R.id.textView3);

        Calendar date = Calendar.getInstance();
        h = date.get(Calendar.HOUR_OF_DAY);
        m = date.get(Calendar.MINUTE);
        String zer = "";
        if (m < 10) zer += "0";
        mTimeView.setText(String.valueOf(h) + ":" + zer + String.valueOf(m));
        mEditPages.setText("0");

        try {
            FileInputStream fileInput = openFileInput("mincircum.txt");
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader buffer = new BufferedReader(reader);
            StringBuffer strBuffer = new StringBuffer();
            String lines = buffer.readLine();
            strBuffer.append(lines);
            fileInput.close();
            last = Integer.parseInt(strBuffer.toString());
            mEditUpToPage.setText(String.valueOf(last));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mButtonRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                last = Integer.parseInt(mEditUpToPage.getText().toString());
                try {
                    FileOutputStream fileOutput = openFileOutput("mincircum.txt", MODE_PRIVATE);
                    fileOutput.write(String.valueOf(last).getBytes());
                    fileOutput.close();
                    getRes();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mButtonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DailyMin.this.finish();
            }
        });
    }

    private void getRes() {
        int curr = Integer.parseInt(mEditPages.getText().toString());

        int k = 1;
        Calendar date = Calendar.getInstance();
        h = date.get(Calendar.HOUR_OF_DAY);
        m = date.get(Calendar.MINUTE);
        String zer = "";
        if (m < 10) zer += "0";
        mTimeView.setText(String.valueOf(h) + ":" + zer + String.valueOf(m));
        int minval = (m >= 30) ? 1 : 0;
        if (h < 22) k = 44 - (h * 2 + minval);
        if (curr < last) {
            String View1 = "До страницы " + String.valueOf(last) + " ещё: " + String.valueOf(last - curr);
            mTextView1.setText(View1);
            String View2 = "В этот промежуток: " + String.valueOf(round((double) (last - curr) / k));
            mTextView2.setText(View2);
            String View3 = "То есть, до страницы: " + String.valueOf(curr + (int) round((double) (last - curr) / k));
            mTextView3.setText(View3);
            mTextView1.setVisibility(View.VISIBLE);
            mTextView2.setVisibility(View.VISIBLE);
            mTextView3.setVisibility(View.VISIBLE);
            mButtonEnd.setVisibility(View.VISIBLE);
        } else {
            String View1 = "Вы прочли все!";
            mTextView1.setText(View1);
            mTextView1.setVisibility(View.VISIBLE);
            mButtonEnd.setVisibility(View.VISIBLE);
        }
    }
}
