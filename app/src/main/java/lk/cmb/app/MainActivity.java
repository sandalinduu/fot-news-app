package lk.cmb.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Show default fragment (Sports)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new SportFragment())
                    .commit();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_sport) {
                selectedFragment = new SportFragment();
            } else if (itemId == R.id.nav_academic) {
                selectedFragment = new AcademicFragment();
            } else if (itemId == R.id.nav_events) {
                selectedFragment = new EventsFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });

        // Link menu icon to OptionMenuScreen
        ImageButton menuIcon = findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OptionMenuScreen.class);
            startActivity(intent);
        });
    }
}
