package com.example.managertask;

import android.content.DialogInterface;
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

public class TeamActivity extends AppCompatActivity {

    private RecyclerView teamRecyclerView;
    private TabLayout tabLayout;
    private TextView emptyTeamText;
    private Toolbar toolbar;

    private List<TeamMember> allMembers;
    private TeamAdapter teamAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        initializeViews();
        setupToolbar();
        setupTabs();
        loadTeamMembers();
    }

    private void initializeViews() {
        teamRecyclerView = findViewById(R.id.teamRecyclerView);
        tabLayout = findViewById(R.id.tabLayout);
        emptyTeamText = findViewById(R.id.emptyTeamText);
        toolbar = findViewById(R.id.toolbar);

        teamRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Команда");
        }
    }

    private void setupTabs() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterMembers(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadTeamMembers() {
        allMembers = new ArrayList<>();

        // Добавляем всех сотрудников
        allMembers.add(new TeamMember(
                1,
                "Иван Петров",
                "Ведущий разработчик",
                "Разработка",
                "ivan.petrov@company.com",
                "+7 (999) 123-45-67",
                "С 2020",
                15,
                "high"
        ));

        allMembers.add(new TeamMember(
                2,
                "Мария Сидорова",
                "Frontend-разработчик",
                "Разработка",
                "maria.sidorova@company.com",
                "+7 (999) 234-56-78",
                "С 2021",
                12,
                "medium"
        ));

        allMembers.add(new TeamMember(
                3,
                "Алексей Иванов",
                "Backend-разработчик",
                "Разработка",
                "alexey.ivanov@company.com",
                "+7 (999) 345-67-89",
                "С 2019",
                20,
                "high"
        ));

        allMembers.add(new TeamMember(
                4,
                "Елена Смирнова",
                "UI/UX дизайнер",
                "Дизайн",
                "elena.smirnova@company.com",
                "+7 (999) 456-78-90",
                "С 2022",
                8,
                "medium"
        ));

        allMembers.add(new TeamMember(
                5,
                "Дмитрий Козлов",
                "DevOps инженер",
                "Инфраструктура",
                "dmitry.kozlov@company.com",
                "+7 (999) 567-89-01",
                "С 2021",
                10,
                "high"
        ));

        allMembers.add(new TeamMember(
                6,
                "Анна Попова",
                "Тестировщик",
                "QA",
                "anna.popova@company.com",
                "+7 (999) 678-90-12",
                "С 2023",
                5,
                "low"
        ));

        allMembers.add(new TeamMember(
                7,
                "Сергей Соколов",
                "Project Manager",
                "Менеджмент",
                "sergey.sokolov@company.com",
                "+7 (999) 789-01-23",
                "С 2018",
                25,
                "high"
        ));

        allMembers.add(new TeamMember(
                8,
                "Ольга Новикова",
                "Аналитик",
                "Аналитика",
                "olga.novikova@company.com",
                "+7 (999) 890-12-34",
                "С 2022",
                7,
                "medium"
        ));

        filterMembers(0);
    }

    private void filterMembers(int tabPosition) {
        List<TeamMember> filteredMembers = new ArrayList<>();

        for (TeamMember member : allMembers) {
            switch (tabPosition) {
                case 0:
                    filteredMembers.add(member);
                    break;
                case 1:
                    if (member.getDepartment().equals("Разработка") ||
                            member.getDepartment().equals("QA") ||
                            member.getDepartment().equals("Инфраструктура")) {
                        filteredMembers.add(member);
                    }
                    break;
                case 2:
                    if (member.getDepartment().equals("Дизайн")) {
                        filteredMembers.add(member);
                    }
                    break;
                case 3:
                    if (member.getDepartment().equals("Менеджмент") ||
                            member.getPosition().contains("Ведущий")) {
                        filteredMembers.add(member);
                    }
                    break;
            }
        }

        if (filteredMembers.isEmpty()) {
            emptyTeamText.setVisibility(View.VISIBLE);
            teamRecyclerView.setVisibility(View.GONE);
        } else {
            emptyTeamText.setVisibility(View.GONE);
            teamRecyclerView.setVisibility(View.VISIBLE);
            teamAdapter = new TeamAdapter(filteredMembers);
            teamRecyclerView.setAdapter(teamAdapter);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {
        private List<TeamMember> members;

        public TeamAdapter(List<TeamMember> members) {
            this.members = members;
        }

        @Override
        public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_team_member, parent, false);
            return new TeamViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TeamViewHolder holder, int position) {
            TeamMember member = members.get(position);
            holder.bind(member);
        }

        @Override
        public int getItemCount() {
            return members.size();
        }

        class TeamViewHolder extends RecyclerView.ViewHolder {
            private TextView tvMemberName;
            private TextView tvMemberPosition;
            private TextView tvMemberDepartment;
            private TextView tvMemberEmail;
            private TextView tvMemberPhone;
            private TextView tvMemberExperience;
            private TextView tvMemberProjects;
            private View onlineIndicator;
            private ImageView ivMemberAvatar;
            private CardView memberCard;

            public TeamViewHolder(View itemView) {
                super(itemView);
                tvMemberName = itemView.findViewById(R.id.tvMemberName);
                tvMemberPosition = itemView.findViewById(R.id.tvMemberPosition);
                tvMemberDepartment = itemView.findViewById(R.id.tvMemberDepartment);
                tvMemberEmail = itemView.findViewById(R.id.tvMemberEmail);
                tvMemberPhone = itemView.findViewById(R.id.tvMemberPhone);
                tvMemberExperience = itemView.findViewById(R.id.tvMemberExperience);
                tvMemberProjects = itemView.findViewById(R.id.tvMemberProjects);
                onlineIndicator = itemView.findViewById(R.id.onlineIndicator);
                ivMemberAvatar = itemView.findViewById(R.id.ivMemberAvatar);
                memberCard = itemView.findViewById(R.id.memberCard);
            }

            public void bind(TeamMember member) {
                tvMemberName.setText(member.getName());
                tvMemberPosition.setText(member.getPosition());
                tvMemberDepartment.setText("Отдел: " + member.getDepartment());
                tvMemberEmail.setText("✉️ " + member.getEmail());
                tvMemberPhone.setText("📞 " + member.getPhone());
                tvMemberExperience.setText("⭐ " + member.getExperience());
                tvMemberProjects.setText("📊 " + member.getProjectsCount() + " проектов");

                if (member.getId() % 2 == 0) {
                    onlineIndicator.setBackgroundResource(R.drawable.online_indicator);
                } else {
                    onlineIndicator.setBackgroundResource(R.drawable.offline_indicator);
                }

                String initials = member.getName().substring(0, 1);
                ivMemberAvatar.setImageDrawable(null);

                switch (member.getDepartment()) {
                    case "Разработка":
                    case "QA":
                    case "Инфраструктура":
                        ivMemberAvatar.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
                        break;
                    case "Дизайн":
                        ivMemberAvatar.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));
                        break;
                    case "Менеджмент":
                        ivMemberAvatar.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                        break;
                    default:
                        ivMemberAvatar.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMemberDetails(member);
                    }
                });
            }
        }
    }

    private void showMemberDetails(TeamMember member) {
        String onlineStatus = member.getId() % 2 == 0 ? "🟢 В сети" : "⚫ Офлайн";

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle(member.getName())
                .setMessage(
                        "👤 " + member.getPosition() + "\n" +
                                "🏢 Отдел: " + member.getDepartment() + "\n" +
                                "✉️ " + member.getEmail() + "\n" +
                                "📞 " + member.getPhone() + "\n" +
                                "⭐ " + member.getExperience() + "\n" +
                                "📊 Участвует в " + member.getProjectsCount() + " проектах\n" +
                                "📱 Статус: " + onlineStatus)
                .setNegativeButton("Закрыть", null)
                .show();
    }

    private class TeamMember {
        private int id;
        private String name;
        private String position;
        private String department;
        private String email;
        private String phone;
        private String experience;
        private int projectsCount;
        private String priority;

        public TeamMember(int id, String name, String position, String department,
                          String email, String phone, String experience,
                          int projectsCount, String priority) {
            this.id = id;
            this.name = name;
            this.position = position;
            this.department = department;
            this.email = email;
            this.phone = phone;
            this.experience = experience;
            this.projectsCount = projectsCount;
            this.priority = priority;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getPosition() { return position; }
        public String getDepartment() { return department; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getExperience() { return experience; }
        public int getProjectsCount() { return projectsCount; }
        public String getPriority() { return priority; }
    }
}