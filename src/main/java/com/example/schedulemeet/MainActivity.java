package com.example.schedulemeet;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // UI components
    private EditText editTextTitle;
    private EditText editTextDate;
    private EditText editTextTime;
    private Button buttonAdd;
    private Button buttonDelete;
    private ListView listViewMeetings;

    // Data
    private ArrayList<String> meetingsList;
    private ArrayAdapter<String> meetingsAdapter;
    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonDelete = findViewById(R.id.buttonDelete);
        listViewMeetings = findViewById(R.id.listViewMeetings);

        // Initialize data
        meetingsList = new ArrayList<>();
        meetingsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, meetingsList);
        listViewMeetings.setAdapter(meetingsAdapter);

        // Disable manual input for date and time fields
        editTextDate.setKeyListener(null);
        editTextTime.setKeyListener(null);

        // Set up click listeners for date and time pickers
        editTextDate.setOnClickListener(v -> showDatePickerDialog());
        editTextTime.setOnClickListener(v -> showTimePickerDialog());

        // Set up button listeners
        buttonAdd.setOnClickListener(v -> addMeeting());
        buttonDelete.setOnClickListener(v -> deleteMeeting());

        // Set up item selection in ListView
        listViewMeetings.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            Toast.makeText(MainActivity.this, "Meeting selected", Toast.LENGTH_SHORT).show();
        });
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (DatePicker view, int year1, int month1, int dayOfMonth) -> {
                    // month1 is zero-based
                    String date = String.format("%02d/%02d/%04d", month1 + 1, dayOfMonth, year1);
                    editTextDate.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (TimePicker view, int hourOfDay, int minute1) -> {
                    String time = String.format("%02d:%02d", hourOfDay, minute1);
                    editTextTime.setText(time);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void addMeeting() {
        String title = editTextTitle.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String time = editTextTime.getText().toString().trim();

        if (title.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Format meeting entry
        String meetingEntry = title + " - " + date + " at " + time;
        meetingsList.add(meetingEntry);
        meetingsAdapter.notifyDataSetChanged();

        // Clear form fields
        editTextTitle.setText("");
        editTextDate.setText("");
        editTextTime.setText("");

        Toast.makeText(this, "Meeting added", Toast.LENGTH_SHORT).show();
    }

    private void deleteMeeting() {
        if (selectedPosition != -1 && selectedPosition < meetingsList.size()) {
            meetingsList.remove(selectedPosition);
            meetingsAdapter.notifyDataSetChanged();
            selectedPosition = -1; // Reset selection
            Toast.makeText(this, "Meeting deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please select a meeting to delete", Toast.LENGTH_SHORT).show();
        }
    }
}
