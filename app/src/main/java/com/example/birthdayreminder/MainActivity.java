package com.example.birthdayreminder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnViewBirthdays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Log.d("MainActivity", "Memulai MainActivity");
            setContentView(R.layout.activity_main);

            btnViewBirthdays = findViewById(R.id.btnViewBirthdays);
            btnViewBirthdays.setOnClickListener(v -> {
                Log.d("MainActivity", "Tombol ditekan, membuka BirthdayListActivity");
                Intent intent = new Intent(MainActivity.this, BirthdayListActivity.class);
                startActivity(intent);
            });

            Log.d("MainActivity", "MainActivity berhasil dimuat");
        } catch (Exception e) {
            Log.e("MainActivity", "Error saat memuat MainActivity", e);
        }
    }
}
