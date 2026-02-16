package com.example.managertask;

import android.content.Intent;
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

public class ProjectsActivity extends AppCompatActivity {

    private RecyclerView projectsRecyclerView;
    private TabLayout tabLayout;
    private TextView emptyProjectsText;
    private Toolbar toolbar;

    private List<Project> allProjects;
    private ProjectsAdapter projectsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        initializeViews();
        setupToolbar();
        setupTabs();
        loadProjects();
    }

    private void initializeViews() {
        projectsRecyclerView = findViewById(R.id.projectsRecyclerView);
        tabLayout = findViewById(R.id.tabLayout);
        emptyProjectsText = findViewById(R.id.emptyProjectsText);
        toolbar = findViewById(R.id.toolbar);

        projectsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Проекты");
        }
    }

    private void setupTabs() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterProjects(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadProjects() {
        allProjects = new ArrayList<>();

        // Тестовые проекты
        allProjects.add(new Project(
                1,
                "TaskFlow",
                "Разработка системы управления задачами",
                65,
                15,
                23,
                "16 фев 2026",
                "high",
                "@drawable/ic_project_blue"
        ));

        allProjects.add(new Project(
                2,
                "Мобильное приложение",
                "Разработка мобильного приложения для клиента",
                30,
                6,
                20,
                "28 фев 2026",
                "medium",
                "@drawable/ic_project_green"
        ));

        allProjects.add(new Project(
                3,
                "CRM система",
                "Внедрение CRM для отдела продаж",
                80,
                16,
                20,
                "10 мар 2026",
                "high",
                "@drawable/ic_project_orange"
        ));

        allProjects.add(new Project(
                4,
                "Корпоративный сайт",
                "Разработка сайта компании",
                45,
                5,
                11,
                "05 мар 2026",
                "low",
                "@drawable/ic_project_purple"
        ));

        allProjects.add(new Project(
                5,
                "API интеграция",
                "Интеграция с платежной системой",
                90,
                9,
                10,
                "20 фев 2026",
                "high",
                "@drawable/ic_project_red"
        ));

        filterProjects(0); // Показываем все проекты
    }

    private void filterProjects(int tabPosition) {
        List<Project> filteredProjects = new ArrayList<>();

        for (Project project : allProjects) {
            switch (tabPosition) {
                case 0: // Все проекты
                    filteredProjects.add(project);
                    break;
                case 1: // Активные
                    if (project.getProgress() < 100) {
                        filteredProjects.add(project);
                    }
                    break;
                case 2: // Завершенные
                    if (project.getProgress() >= 100) {
                        filteredProjects.add(project);
                    }
                    break;
                case 3: // По приоритету
                    if (project.getPriority().equals("high")) {
                        filteredProjects.add(project);
                    }
                    break;
            }
        }

        if (filteredProjects.isEmpty()) {
            emptyProjectsText.setVisibility(View.VISIBLE);
            projectsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyProjectsText.setVisibility(View.GONE);
            projectsRecyclerView.setVisibility(View.VISIBLE);
            projectsAdapter = new ProjectsAdapter(filteredProjects);
            projectsRecyclerView.setAdapter(projectsAdapter);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // Адаптер для проектов
    private class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder> {
        private List<Project> projects;

        public ProjectsAdapter(List<Project> projects) {
            this.projects = projects;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_project_card, parent, false);
            return new ProjectViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            Project project = projects.get(position);
            holder.bind(project);
        }

        @Override
        public int getItemCount() {
            return projects.size();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder {
            private TextView tvProjectName;
            private TextView tvProjectDescription;
            private TextView tvProjectProgress;
            private TextView tvProjectTasks;
            private TextView tvProjectDeadline;
            private ProgressBar progressBar;
            private View priorityIndicator;
            private ImageView ivProjectIcon;
            private CardView projectCard;

            public ProjectViewHolder(View itemView) {
                super(itemView);
                tvProjectName = itemView.findViewById(R.id.tvProjectName);
                tvProjectDescription = itemView.findViewById(R.id.tvProjectDescription);
                tvProjectProgress = itemView.findViewById(R.id.tvProjectProgress);
                tvProjectTasks = itemView.findViewById(R.id.tvProjectTasks);
                tvProjectDeadline = itemView.findViewById(R.id.tvProjectDeadline);
                progressBar = itemView.findViewById(R.id.progressBar);
                priorityIndicator = itemView.findViewById(R.id.priorityIndicator);
                ivProjectIcon = itemView.findViewById(R.id.ivProjectIcon);
                projectCard = itemView.findViewById(R.id.projectCard);
            }

            public void bind(Project project) {
                tvProjectName.setText(project.getName());
                tvProjectDescription.setText(project.getDescription());
                tvProjectProgress.setText(project.getProgress() + "%");
                tvProjectTasks.setText(project.getCompletedTasks() + "/" + project.getTotalTasks() + " задач");
                tvProjectDeadline.setText("📅 " + project.getDeadline());

                progressBar.setProgress(project.getProgress());

                // Цвет индикатора приоритета
                switch (project.getPriority()) {
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

                // Иконка проекта (разные цвета)
                switch (project.getId() % 4) {
                    case 0:
                        ivProjectIcon.setImageResource(android.R.drawable.ic_dialog_info);
                        ivProjectIcon.setColorFilter(getResources().getColor(android.R.color.holo_blue_dark));
                        break;
                    case 1:
                        ivProjectIcon.setImageResource(android.R.drawable.ic_dialog_info);
                        ivProjectIcon.setColorFilter(getResources().getColor(android.R.color.holo_green_dark));
                        break;
                    case 2:
                        ivProjectIcon.setImageResource(android.R.drawable.ic_dialog_info);
                        ivProjectIcon.setColorFilter(getResources().getColor(android.R.color.holo_orange_dark));
                        break;
                    case 3:
                        ivProjectIcon.setImageResource(android.R.drawable.ic_dialog_info);
                        ivProjectIcon.setColorFilter(getResources().getColor(android.R.color.holo_purple));
                        break;
                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProjectsActivity.this, ProjectDetailActivity.class);
                        intent.putExtra("project_name", project.getName());
                        intent.putExtra("project_progress", project.getProgress());
                        startActivity(intent);
                    }
                });
            }
        }
    }

    // Класс Project
    private class Project {
        private int id;
        private String name;
        private String description;
        private int progress;
        private int completedTasks;
        private int totalTasks;
        private String deadline;
        private String priority;
        private String iconRes;

        public Project(int id, String name, String description, int progress,
                       int completedTasks, int totalTasks, String deadline,
                       String priority, String iconRes) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.progress = progress;
            this.completedTasks = completedTasks;
            this.totalTasks = totalTasks;
            this.deadline = deadline;
            this.priority = priority;
            this.iconRes = iconRes;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public int getProgress() { return progress; }
        public int getCompletedTasks() { return completedTasks; }
        public int getTotalTasks() { return totalTasks; }
        public String getDeadline() { return deadline; }
        public String getPriority() { return priority; }
    }
}