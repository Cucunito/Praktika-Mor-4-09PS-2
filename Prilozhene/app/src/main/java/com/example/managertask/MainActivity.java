package com.example.managertask;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_NEW_TASK = 100;

    // Элементы UI
    private CardView projectCard;
    private MaterialButton newTaskButton;
    private MaterialButton exportButton;
    private TextView emptyTasksText;
    private RecyclerView myTasksRecyclerView;
    private EditText searchEditText;

    // Вертикальные кнопки
    private Button tasksChip;
    private Button projectsChip;
    private Button teamChip;

    // Данные - ТОЛЬКО 2 ЗАДАЧИ
    private List<Task> myTasks;
    private List<Task> filteredTasks;
    private MyTasksAdapter myTasksAdapter;
    private String currentUser = "Текущий пользователь";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupInitialData();
        setupListeners();
        setupSearch();
    }

    private void initializeViews() {
        projectCard = findViewById(R.id.projectCard);
        newTaskButton = findViewById(R.id.newTaskButton);
        exportButton = findViewById(R.id.exportButton);
        emptyTasksText = findViewById(R.id.emptyTasksText);
        myTasksRecyclerView = findViewById(R.id.myTasksRecyclerView);
        searchEditText = findViewById(R.id.searchEditText);

        tasksChip = findViewById(R.id.tasksChip);
        projectsChip = findViewById(R.id.projectsChip);
        teamChip = findViewById(R.id.teamChip);

        myTasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupInitialData() {
        myTasks = new ArrayList<>();

        // ТОЛЬКО 2 ТЕСТОВЫЕ ЗАДАЧИ
        myTasks.add(new Task(
                1,
                "Протестировать модуль авторизации",
                "Проверить вход через email и Google",
                currentUser,
                "16 фев 2026",
                "high",
                false,
                "TaskFlow",
                "Тест"
        ));

        myTasks.add(new Task(
                2,
                "Исправить баг входа",
                "Ошибка 500 при входе через Google",
                currentUser,
                "17 фев 2026",
                "high",
                false,
                "TaskFlow",
                "Баг"
        ));

        filteredTasks = new ArrayList<>(myTasks);
        updateTasksDisplay();
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTasks(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                return true;
            }
        });
    }

    private void filterTasks(String query) {
        filteredTasks.clear();

        if (query.isEmpty()) {
            filteredTasks.addAll(myTasks);
        } else {
            String lowerCaseQuery = query.toLowerCase(Locale.getDefault());

            for (Task task : myTasks) {
                if (task.getTitle().toLowerCase(Locale.getDefault()).contains(lowerCaseQuery) ||
                        task.getDescription().toLowerCase(Locale.getDefault()).contains(lowerCaseQuery) ||
                        task.getProject().toLowerCase(Locale.getDefault()).contains(lowerCaseQuery) ||
                        task.getTags().toLowerCase(Locale.getDefault()).contains(lowerCaseQuery)) {

                    filteredTasks.add(task);
                }
            }
        }

        updateFilteredDisplay();
    }

    private void updateFilteredDisplay() {
        List<Task> userFilteredTasks = new ArrayList<>();
        for (Task task : filteredTasks) {
            if (task.getAssignee().equals(currentUser) && !task.isCompleted()) {
                userFilteredTasks.add(task);
            }
        }

        if (userFilteredTasks.isEmpty()) {
            if (filteredTasks.isEmpty() && !searchEditText.getText().toString().isEmpty()) {
                emptyTasksText.setText("🔍 Ничего не найдено\nПопробуйте изменить запрос");
            } else {
                emptyTasksText.setText("✨ У вас пока нет задач\nСоздайте новую задачу");
            }
            emptyTasksText.setVisibility(View.VISIBLE);
            myTasksRecyclerView.setVisibility(View.GONE);
        } else {
            emptyTasksText.setVisibility(View.GONE);
            myTasksRecyclerView.setVisibility(View.VISIBLE);
            myTasksAdapter = new MyTasksAdapter(userFilteredTasks);
            myTasksRecyclerView.setAdapter(myTasksAdapter);
        }
    }

    private void setupListeners() {
        if (tasksChip != null) {
            tasksChip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, TasksActivity.class);
                    startActivity(intent);
                }
            });
        }

        if (projectsChip != null) {
            projectsChip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ProjectsActivity.class);
                    startActivity(intent);
                }
            });
        }

        if (teamChip != null) {
            teamChip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, TeamActivity.class);
                    startActivity(intent);
                }
            });
        }

        if (projectCard != null) {
            projectCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ProjectDetailActivity.class);
                    intent.putExtra("project_name", "TaskFlow");
                    intent.putExtra("project_progress", 65);
                    startActivity(intent);
                }
            });
        }

        if (newTaskButton != null) {
            newTaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, NewTaskActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_NEW_TASK);
                }
            });
        }

        if (exportButton != null) {
            exportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showExportDialog();
                }
            });
        }
    }

    private void showExportDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("📤 Экспорт данных")
                .setMessage("Выберите формат экспорта:")
                .setPositiveButton("PDF", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Экспорт в PDF начат", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("CSV", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Экспорт в CSV начат", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("Отмена", null)
                .show();
    }

    private void updateTasksDisplay() {
        List<Task> userTasks = new ArrayList<>();
        for (Task task : myTasks) {
            if (task.getAssignee().equals(currentUser) && !task.isCompleted()) {
                userTasks.add(task);
            }
        }

        if (userTasks.isEmpty()) {
            emptyTasksText.setVisibility(View.VISIBLE);
            myTasksRecyclerView.setVisibility(View.GONE);
        } else {
            emptyTasksText.setVisibility(View.GONE);
            myTasksRecyclerView.setVisibility(View.VISIBLE);
            myTasksAdapter = new MyTasksAdapter(userTasks);
            myTasksRecyclerView.setAdapter(myTasksAdapter);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_NEW_TASK && resultCode == RESULT_OK) {
            if (data != null) {
                String title = data.getStringExtra("task_title");
                String description = data.getStringExtra("task_description");
                String project = data.getStringExtra("task_project");
                String assignee = data.getStringExtra("task_assignee");
                String priority = data.getStringExtra("task_priority");
                String deadline = data.getStringExtra("task_deadline");
                String tags = data.getStringExtra("task_tags");

                Task newTask = new Task(
                        myTasks.size() + 1,
                        title,
                        description,
                        assignee,
                        deadline,
                        priority,
                        false,
                        project,
                        tags
                );

                myTasks.add(newTask);
                filteredTasks.add(newTask);
                searchEditText.setText("");
                updateTasksDisplay();

                if (assignee.equals(currentUser)) {
                    Toast.makeText(this, "📋 У вас новая задача: " + title, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "✅ Задача '" + title + "' создана", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Адаптер для задач
    private class MyTasksAdapter extends RecyclerView.Adapter<MyTasksAdapter.TaskViewHolder> {
        private List<Task> tasks;

        public MyTasksAdapter(List<Task> tasks) {
            this.tasks = tasks;
        }

        @Override
        public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_my_task, parent, false);
            return new TaskViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TaskViewHolder holder, int position) {
            Task task = tasks.get(position);
            holder.bind(task);
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }

        class TaskViewHolder extends RecyclerView.ViewHolder {
            private CheckBox taskCheckBox;
            private TextView taskTitleText;
            private TextView taskProjectText;
            private TextView taskDeadlineText;
            private View priorityIndicator;

            public TaskViewHolder(View itemView) {
                super(itemView);
                taskCheckBox = itemView.findViewById(R.id.taskCheckBox);
                taskTitleText = itemView.findViewById(R.id.taskTitleText);
                taskProjectText = itemView.findViewById(R.id.taskProjectText);
                taskDeadlineText = itemView.findViewById(R.id.taskDeadlineText);
                priorityIndicator = itemView.findViewById(R.id.priorityIndicator);
            }

            public void bind(Task task) {
                taskTitleText.setText(task.getTitle());
                taskProjectText.setText("📁 " + task.getProject());
                taskDeadlineText.setText("📅 " + task.getDeadline());
                taskCheckBox.setChecked(task.isCompleted());

                switch (task.getPriority()) {
                    case "high":
                        priorityIndicator.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                        break;
                    case "medium":
                        priorityIndicator.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                        break;
                    case "low":
                        priorityIndicator.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                        break;
                }

                taskCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        task.setCompleted(isChecked);
                        if (isChecked) {
                            Toast.makeText(MainActivity.this, "✅ Задача выполнена: " + task.getTitle(), Toast.LENGTH_SHORT).show();
                            myTasks.remove(task);
                            filteredTasks.remove(task);
                            updateTasksDisplay();

                            if (!searchEditText.getText().toString().isEmpty()) {
                                filterTasks(searchEditText.getText().toString());
                            }
                        }
                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTaskDetails(task);
                    }
                });
            }
        }
    }

    private void showTaskDetails(Task task) {
        String status = task.isCompleted() ? "✅ Завершена" : "⏳ В работе";

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle(task.getTitle())
                .setMessage("📝 " + task.getDescription() + "\n\n" +
                        "📁 Проект: " + task.getProject() + "\n" +
                        "👤 Исполнитель: " + task.getAssignee() + "\n" +
                        "📅 Дедлайн: " + task.getDeadline() + "\n" +
                        "⚡ Приоритет: " + getPriorityText(task.getPriority()) + "\n" +
                        "🏷️ Теги: " + task.getTags() + "\n" +
                        "📊 Статус: " + status)
                .setPositiveButton("Закрыть", null)
                .show();
    }

    private String getPriorityText(String priority) {
        switch (priority) {
            case "high": return "Высокий";
            case "medium": return "Средний";
            case "low": return "Низкий";
            default: return priority;
        }
    }
}