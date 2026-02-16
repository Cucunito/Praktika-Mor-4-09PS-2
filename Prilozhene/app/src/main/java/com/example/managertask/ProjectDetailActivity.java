package com.example.managertask;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class ProjectDetailActivity extends AppCompatActivity {

    private TextView projectNameText;
    private TextView projectDescriptionText;
    private ProgressBar progressBar;
    private TextView progressText;
    private TextView tasksCountText;
    private TextView deadlineText;
    private TextView priorityText;
    private RecyclerView tasksRecyclerView;
    private MaterialButton addTaskButton;
    private ImageButton shareButton;
    private ImageButton settingsButton;
    private TabLayout tabLayout;
    private Toolbar toolbar;

    private String projectName;
    private int projectProgress;
    private List<Task> projectTasks;
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        // Получаем данные
        Intent intent = getIntent();
        projectName = intent.getStringExtra("project_name");
        projectProgress = intent.getIntExtra("project_progress", 0);
        if (projectName == null) projectName = "TaskFlow";

        // Инициализация View
        projectNameText = findViewById(R.id.projectNameText);
        projectDescriptionText = findViewById(R.id.projectDescriptionText);
        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);
        tasksCountText = findViewById(R.id.tasksCountText);
        deadlineText = findViewById(R.id.deadlineText);
        priorityText = findViewById(R.id.priorityText);
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        addTaskButton = findViewById(R.id.addTaskButton);
        shareButton = findViewById(R.id.shareButton);
        settingsButton = findViewById(R.id.settingsButton);
        tabLayout = findViewById(R.id.tabLayout);
        toolbar = findViewById(R.id.toolbar);

        // Настройка toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        // Загрузка данных
        loadProjectData();

        // Настройка табов
        setupTabs();

        // Настройка RecyclerView
        setupRecyclerView();

        // Настройка кнопок
        setupButtons();
    }

    private void loadProjectData() {
        projectNameText.setText(projectName);
        projectDescriptionText.setText("Разработка системы управления задачами");
        progressBar.setProgress(projectProgress);
        progressText.setText(projectProgress + "%");
        tasksCountText.setText("15/23 задач");
        deadlineText.setText("28 фев 2026");
        priorityText.setText("Высокий");
        priorityText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

        // Создаем тестовые задачи
        projectTasks = new ArrayList<>();
        projectTasks.add(new Task(1, "Разработать макет", "Создать дизайн", "Иван", "2026-02-20", "high", true));
        projectTasks.add(new Task(2, "Написать код", "Реализовать логику", "Мария", "2026-02-22", "medium", false));
        projectTasks.add(new Task(3, "Тестирование", "Провести тесты", "Алексей", "2026-02-25", "low", false));
    }

    private void setupTabs() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterTasks(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void filterTasks(int position) {
        List<Task> filtered = new ArrayList<>();
        for (Task task : projectTasks) {
            if (position == 0) filtered.add(task);
            else if (position == 1 && !task.completed) filtered.add(task);
            else if (position == 2 && task.completed) filtered.add(task);
        }
        taskAdapter.updateTasks(filtered);
    }

    private void setupRecyclerView() {
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(projectTasks);
        tasksRecyclerView.setAdapter(taskAdapter);
    }

    private void setupButtons() {
        addTaskButton.setOnClickListener(v -> {
            Toast.makeText(this, "Создание новой задачи", Toast.LENGTH_SHORT).show();
        });

        shareButton.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Проект: " + projectName);
            startActivity(Intent.createChooser(shareIntent, "Поделиться"));
        });

        settingsButton.setOnClickListener(v -> {
            Toast.makeText(this, "Настройки проекта", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Класс Task
    private static class Task {
        int id;
        String title;
        String description;
        String assignee;
        String deadline;
        String priority;
        boolean completed;

        Task(int id, String title, String description, String assignee,
             String deadline, String priority, boolean completed) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.assignee = assignee;
            this.deadline = deadline;
            this.priority = priority;
            this.completed = completed;
        }
    }

    // Адаптер
    private class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
        private List<Task> tasks;

        TaskAdapter(List<Task> tasks) {
            this.tasks = tasks;
        }

        void updateTasks(List<Task> newTasks) {
            this.tasks = newTasks;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Task task = tasks.get(position);
            holder.text1.setText(task.title);
            holder.text2.setText(task.description + " - " + task.assignee);
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView text1, text2;
            ViewHolder(View itemView) {
                super(itemView);
                text1 = itemView.findViewById(android.R.id.text1);
                text2 = itemView.findViewById(android.R.id.text2);
            }
        }
    }
}