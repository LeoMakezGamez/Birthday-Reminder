package com.example.birthdayreminder;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BirthdayAdapter extends RecyclerView.Adapter<BirthdayAdapter.ViewHolder> {
    private Context context;
    private Cursor cursor;
    private DatabaseHelper dbHelper;

    public BirthdayAdapter(Context context, Cursor cursor, DatabaseHelper dbHelper) {
        this.context = context;
        this.cursor = cursor;
        this.dbHelper = dbHelper;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textDate;
        Button btnEdit, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textDate = itemView.findViewById(R.id.textDate);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

        holder.textName.setText(name);
        holder.textDate.setText(date);

        holder.btnEdit.setOnClickListener(v -> {
            if (context instanceof BirthdayListActivity) {
                ((BirthdayListActivity) context).showEditDialog(id, name, date);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            dbHelper.deleteBirthday(id);
            Toast.makeText(context, "Ulang tahun dihapus", Toast.LENGTH_SHORT).show();
            ((BirthdayListActivity) context).refreshList();  // **Pastikan daftar diperbarui**
        });

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
