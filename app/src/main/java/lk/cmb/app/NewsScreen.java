package lk.cmb.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NewsScreen extends AppCompatActivity {

    private static final String TAG = "NewsScreen";

    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private List<SignUpScreen.NewsItem> newsList;
    private BottomNavigationView bottomNavigationView;
    private ImageButton menuIcon;
    private TextView toolbarTitle;

    private DatabaseReference newsDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Faculty News");

        menuIcon = findViewById(R.id.menu_icon);
        newsRecyclerView = findViewById(R.id.news_recycler_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(newsList);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsRecyclerView.setAdapter(newsAdapter);

        newsDatabaseRef = FirebaseDatabase.getInstance().getReference("news");

        // 🔁 When the user selects a category
        bottomNavigationView.setOnItemSelectedListener(item -> {
            String category;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_sport) {
                category = "sport";
            } else if (itemId == R.id.nav_academic) {
                category = "academic";
            } else if (itemId == R.id.nav_events) {
                category = "events";
            } else {
                category = "academic";
            }

            fetchNewsByCategory(category);
            return true;
        });

        // Top-left menu icon action
        menuIcon.setOnClickListener(v -> {
            Intent intent = new Intent(NewsScreen.this, OptionMenuScreen.class);
            startActivity(intent);
            Toast.makeText(NewsScreen.this, "Opening Menu...", Toast.LENGTH_SHORT).show();
        });

        // ✅ Load ALL news from all categories at start
        fetchAllNews();
    }

    // ✅ New method to fetch ALL news
    private void fetchAllNews() {
        newsList.clear();
        newsAdapter.notifyDataSetChanged();

        String[] categories = {"sport", "academic", "events"};

        for (String category : categories) {
            DatabaseReference categoryRef = newsDatabaseRef.child(category);

            categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        SignUpScreen.NewsItem item = data.getValue(SignUpScreen.NewsItem.class);
                        if (item != null) {
                            newsList.add(item);
                        }
                    }
                    newsAdapter.updateNewsList(newsList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(NewsScreen.this, "Error loading news", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // ✅ Existing method to fetch a specific category
    private void fetchNewsByCategory(String category) {
        newsList.clear();
        newsAdapter.notifyDataSetChanged();

        DatabaseReference categoryRef = newsDatabaseRef.child(category);

        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SignUpScreen.NewsItem newsItem = snapshot.getValue(SignUpScreen.NewsItem.class);
                    if (newsItem != null) {
                        newsList.add(newsItem);
                    }
                }
                newsAdapter.updateNewsList(newsList);

                if (newsList.isEmpty()) {
                    Toast.makeText(NewsScreen.this, "No news in " + category, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NewsScreen.this, "Error loading " + category + ": " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
