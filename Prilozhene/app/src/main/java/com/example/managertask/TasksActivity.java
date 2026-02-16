package com.example.managertask;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class TasksActivity extends AppCompatActivity {

    private RecyclerView tasksRecyclerView;
    private TabLayout tabLayout;
    private TextView emptyTasksText;
    private Toolbar toolbar;

    private List<Task> allTasks;
    private TasksAdapter tasksAdapter;
    private String currentUser = "Текущий пользователь";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        initializeViews();
        setupToolbar();
        setupTabs();
        loadTasks();
    }

    private void initializeViews() {
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tabLayout = findViewById(R.id.tabLayout);
        emptyTasksText = findViewById(R.id.emptyTasksText);
        toolbar = findViewById(R.id.toolbar);

        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Задачи");
        }
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

    private void loadTasks() {
        allTasks = new ArrayList<>();

        // Мои задачи
        allTasks.add(new Task(1, "Протестировать авторизацию", "Провести тестирование модуля",
                currentUser, "16 фев 2026", "high", false, "TaskFlow", "Тест"));
        allTasks.add(new Task(2, "Написать отчет", "Подготовить отчет за неделю",
                currentUser, "18 фев 2026", "medium", false, "TaskFlow", "Документы"));

        // Задачи других
        allTasks.add(new Task(3, "Исправить баг #234", "Ошибка при входе в систему",
                "Иван Петров", "17 фев 2026", "high", true, "TaskFlow", "Баг"));
        allTasks.add(new Task(4, "Обновить документацию", "Добавить новые разделы",
                "Мария Сидорова", "20 фев 2026", "low", false, "TaskFlow", "Доки"));
        allTasks.add(new Task(5, "Провести код-ревью", "Проверить PR #123",
                "Алексей Иванов", "19 фев 2026", "medium", false, "TaskFlow", "Ревью"));
        allTasks.add(new Task(6, "Настроить CI/CD", "Обновить конфигурацию",
                "Дмитрий Козлов", "22 фев 2026", "high", true, "DevOps", "Инфра"));
        allTasks.add(new Task(7, "Создать макет", "Дизайн главной страницы",
                "Елена Смирнова", "21 фев 2026", "medium", false, "Design", "Дизайн"));

        filterTasks(0); // Показываем все задачи
    }

    private void filterTasks(int tabPosition) {
        List<Task> filteredTasks = new ArrayList<>();

        for (Task task : allTasks) {
            switch (tabPosition) {
                case 0: // Все задачи
                    filteredTasks.add(task);
                    break;
                case 1: // Мои задачи
                    if (task.getAssignee().equals(currentUser)) {
                        filteredTasks.add(task);
                    }
                    break;
                case 2: // Активные
                    if (!task.isCompleted()) {
                        filteredTasks.add(task);
                    }
                    break;
                case 3: // Завершенные
                    if (task.isCompleted()) {
                        filteredTasks.add(task);
                    }
                    break;
            }
        }

        if (filteredTasks.isEmpty()) {
            emptyTasksText.setVisibility(View.VISIBLE);
            tasksRecyclerView.setVisibility(View.GONE);
        } else {
            emptyTasksText.setVisibility(View.GONE);
            tasksRecyclerView.setVisibility(View.VISIBLE);
            tasksAdapter = new TasksAdapter(filteredTasks);
            tasksRecyclerView.setAdapter(tasksAdapter);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // Адаптер для задач
    private class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {
        private List<Task> tasks;

        public TasksAdapter(List<Task> tasks) {
            this.tasks = tasks;
        }

        @Override
        public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_task_card, parent, false);
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
            private TextView tvTaskTitle;
            private TextView tvTaskDescription;
            private TextView tvTaskProject;
            private TextView tvTaskAssignee;
            private TextView tvTaskDeadline;
            private TextView tvTaskStatus;
            private View priorityIndicator;
            private ImageView ivAssigneeIcon;
            private CardView taskCard;

            public TaskViewHolder(View itemView) {
                super(itemView);
                tvTaskTitle = itemView.findViewById(R.id.tvTaskTitle);
                tvTaskDescription = itemView.findViewById(R.id.tvTaskDescription);
                tvTaskProject = itemView.findViewById(R.id.tvTaskProject);
                tvTaskAssignee = itemView.findViewById(R.id.tvTaskAssignee);
                tvTaskDeadline = itemView.findViewById(R.id.tvTaskDeadline);
                tvTaskStatus = itemView.findViewById(R.id.tvTaskStatus);
                priorityIndicator = itemView.findViewById(R.id.priorityIndicator);
                ivAssigneeIcon = itemView.findViewById(R.id.ivAssigneeIcon);
                taskCard = itemView.findViewById(R.id.taskCard);
            }

            public void bind(Task task) {
                tvTaskTitle.setText(task.getTitle());
                tvTaskDescription.setText(task.getDescription());
                tvTaskProject.setText("📁 " + task.getProject());
                tvTaskAssignee.setText("👤 " + task.getAssignee());
                tvTaskDeadline.setText("📅 " + task.getDeadline());

                // Статус
                if (task.isCompleted()) {
                    tvTaskStatus.setText("✅ Завершена");
                    tvTaskStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                } else {
                    tvTaskStatus.setText("⏳ В работе");
                    tvTaskStatus.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                }

                // Приоритет
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

                // Цвет фона для моих задач
                if (task.getAssignee().equals(currentUser)) {
                    taskCard.setCardBackgroundColor(getResources().getColor(android.R.color.white));
                    tvTaskAssignee.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                } else {
                    taskCard.setCardBackgroundColor(getResources().getColor(android.R.color.white));
                    tvTaskAssignee.setTextColor(getResources().getColor(android.R.color.darker_gray));
                }

                itemView.setOnClickListener(v -> showTaskDetails(task));
            }
        }
    }

    private void showTaskDetails(Task task) {
        String status = task.isCompleted() ? "✅ Завершена" : "⏳ В работе";
        String myTask = task.getAssignee().equals(currentUser) ? "⭐ Это ваша задача" : "";

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(task.getTitle())
                .setMessage(
                        "📝 " + task.getDescription() + "\n\n" +
                                "📁 Проект: " + task.getProject() + "\n" +
                                "👤 Исполнитель: " + task.getAssignee() + " " + myTask + "\n" +
                                "📅 Дедлайн: " + task.getDeadline() + "\n" +
                                "⚡ Приоритет: " + getPriorityText(task.getPriority()) + "\n" +
                                "🏷️ Теги: " + task.getTags() + "\n" +
                                "📊 Статус: " + status
                )
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