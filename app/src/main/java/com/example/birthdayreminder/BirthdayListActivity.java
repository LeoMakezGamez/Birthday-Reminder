package com.example.birthdayreminder;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BirthdayListActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText etName, etDate;
    private Button btnAdd;
    private ListView listView;

    private RecyclerView recyclerView;
    private BirthdayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday_list);

        dbHelper = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etDate = findViewById(R.id.etDate);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String date = etDate.getText().toString();
            if (!name.isEmpty() && !date.isEmpty()) {
                dbHelper.addBirthday(name, date);
                etName.setText("");
                etDate.setText("");
                refreshList();
                Toast.makeText(BirthdayListActivity.this, "Ulang tahun ditambahkan", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BirthdayListActivity.this, "Harap isi semua bidang", Toast.LENGTH_SHORT).show();
            }
        });

        refreshList();
    }

    public void refreshList() {
        Cursor cursor = dbHelper.getAllBirthdays();
        adapter = new BirthdayAdapter(this, cursor, dbHelper);
        recyclerView.setAdapter(adapter);
    }


    public void showEditDialog(int id, String oldName, String oldDate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Ulang Tahun");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);

        EditText inputName = new EditText(this);
        inputName.setText(oldName);
        layout.addView(inputName);

        EditText inputDate = new EditText(this);
        inputDate.setText(oldDate);
        layout.addView(inputDate);

        builder.setView(layout);

        builder.setPositiveButton("Simpan", (dialog, which) -> {
            String newName = inputName.getText().toString();
            String newDate = inputDate.getText().toString();
            if (!newName.isEmpty() && !newDate.isEmpty()) {
                dbHelper.updateBirthday(id, newName, newDate);
                refreshList();  // **Panggil refreshList() agar daftar diperbarui**
                Toast.makeText(this, "Ulang tahun diperbarui", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Harap isi semua bidang", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Batal", (dialog, which) -> dialog.dismiss());

        builder.show();
    }


    private void scheduleNotification(String name, String date) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("birthday_channel", "Birthday Reminders", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "birthday_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Pengingat Ulang Tahun")
                .setContentText(name + " berulang tahun pada " + date + "!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(0, builder.build());
    }
}
