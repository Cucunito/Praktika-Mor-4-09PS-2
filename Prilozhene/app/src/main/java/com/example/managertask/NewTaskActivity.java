package com.example.managertask;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NewTaskActivity extends AppCompatActivity {

    // Поля ввода
    private EditText taskTitleInput;
    private EditText taskDescriptionInput;
    private Spinner projectSpinner;
    private Spinner assigneeSpinner;
    private RadioGroup priorityGroup;
    private RadioButton priorityHigh;
    private RadioButton priorityMedium;
    private RadioButton priorityLow;
    private Button deadlineButton;
    private TextView selectedDateText;

    // Чекбоксы для тегов
    private CheckBox tagBug;
    private CheckBox tagFeature;
    private CheckBox tagDocs;
    private CheckBox tagUrgent;
    private CheckBox tagReview;
    private CheckBox tagTest;

    // Кнопки
    private MaterialButton cancelButton;
    private MaterialButton createButton;
    private Toolbar toolbar;

    // Данные
    private Calendar selectedDate;
    private SimpleDateFormat dateFormat;

    // Списки для Spinner
    private List<String> projects;
    private List<String> assignees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        selectedDate = Calendar.getInstance();

        initializeViews();
        setupToolbar();
        loadSpinnerData();
        setupSpinners();
        setupListeners();
    }

    private void initializeViews() {
        taskTitleInput = findViewById(R.id.taskTitleInput);
        taskDescriptionInput = findViewById(R.id.taskDescriptionInput);
        projectSpinner = findViewById(R.id.projectSpinner);
        assigneeSpinner = findViewById(R.id.assigneeSpinner);
        priorityGroup = findViewById(R.id.priorityGroup);
        priorityHigh = findViewById(R.id.priorityHigh);
        priorityMedium = findViewById(R.id.priorityMedium);
        priorityLow = findViewById(R.id.priorityLow);
        deadlineButton = findViewById(R.id.deadlineButton);
        selectedDateText = findViewById(R.id.selectedDateText);

        tagBug = findViewById(R.id.tagBug);
        tagFeature = findViewById(R.id.tagFeature);
        tagDocs = findViewById(R.id.tagDocs);
        tagUrgent = findViewById(R.id.tagUrgent);
        tagReview = findViewById(R.id.tagReview);
        tagTest = findViewById(R.id.tagTest);

        cancelButton = findViewById(R.id.cancelButton);
        createButton = findViewById(R.id.createButton);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    private void loadSpinnerData() {
        projects = new ArrayList<>();
        projects.add("TaskFlow");
        projects.add("Мобильное приложение");
        projects.add("Веб-сайт");
        projects.add("CRM система");

        assignees = new ArrayList<>();
        assignees.add("Текущий пользователь"); // Это "Меня"
        assignees.add("Иван Петров");
        assignees.add("Мария Сидорова");
        assignees.add("Алексей Иванов");
        assignees.add("Елена Смирнова");
    }

    private void setupSpinners() {
        ArrayAdapter<String> projectAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                projects
        );
        projectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projectSpinner.setAdapter(projectAdapter);

        ArrayAdapter<String> assigneeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                assignees
        );
        assigneeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assigneeSpinner.setAdapter(assigneeAdapter);
    }

    private void setupListeners() {
        deadlineButton.setOnClickListener(v -> showDatePickerDialog());

        cancelButton.setOnClickListener(v -> {
            showCancelConfirmationDialog();
        });

        createButton.setOnClickListener(v -> {
            createNewTask();
        });

        updateDateText();
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate.set(Calendar.YEAR, year);
                    selectedDate.set(Calendar.MONTH, month);
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateText();
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void updateDateText() {
        selectedDateText.setText(dateFormat.format(selectedDate.getTime()));
        selectedDateText.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void showCancelConfirmationDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Отмена создания")
                .setMessage("Вы уверены, что хотите отменить создание задачи?")
                .setPositiveButton("Да", (dialog, which) -> finish())
                .setNegativeButton("Нет", null)
                .show();
    }

    private void createNewTask() {
        String title = taskTitleInput.getText().toString().trim();
        if (title.isEmpty()) {
            taskTitleInput.setError("Введите название задачи");
            taskTitleInput.requestFocus();
            return;
        }

        String description = taskDescriptionInput.getText().toString().trim();
        String project = projects.get(projectSpinner.getSelectedItemPosition());
        String assignee = assignees.get(assigneeSpinner.getSelectedItemPosition());

        String priority = "medium";
        if (priorityHigh.isChecked()) priority = "high";
        else if (priorityLow.isChecked()) priority = "low";

        List<String> selectedTags = new ArrayList<>();
        if (tagBug.isChecked()) selectedTags.add("Баг");
        if (tagFeature.isChecked()) selectedTags.add("Фича");
        if (tagDocs.isChecked()) selectedTags.add("Доки");
        if (tagUrgent.isChecked()) selectedTags.add("Срочно");
        if (tagReview.isChecked()) selectedTags.add("Ревью");
        if (tagTest.isChecked()) selectedTags.add("Тест");

        String tags = selectedTags.isEmpty() ? "нет" : String.join(", ", selectedTags);
        String deadline = dateFormat.format(selectedDate.getTime());

        // Создаем Intent с данными задачи
        Intent resultIntent = new Intent();
        resultIntent.putExtra("task_title", title);
        resultIntent.putExtra("task_description", description);
        resultIntent.putExtra("task_project", project);
        resultIntent.putExtra("task_assignee", assignee);
        resultIntent.putExtra("task_priority", priority);
        resultIntent.putExtra("task_deadline", deadline);
        resultIntent.putExtra("task_tags", tags);

        setResult(RESULT_OK, resultIntent);

        // Показываем сообщение
        String message = "✅ Задача '" + title + "' создана";
        if (assignee.equals("Текущий пользователь")) {
            message += " и назначена на вас";
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            showCancelConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}