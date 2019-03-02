package anton.example.com.manager;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, Quote.class);
        startActivity(intent);
    }

    public void launchDailyMin (View view) {
        Intent intentDM = new Intent(this, DailyMin.class);
        startActivity(intentDM);
    }
}
