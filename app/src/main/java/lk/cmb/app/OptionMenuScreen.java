package lk.cmb.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class OptionMenuScreen extends AppCompatActivity {

    private ImageButton closeButton;
    private LinearLayout homeOption, myProfileOption, devInfoOption, signOutOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_menu_screen);

        // Bind UI elements
        closeButton = findViewById(R.id.closeButton);
        homeOption = findViewById(R.id.homeOption);
        myProfileOption = findViewById(R.id.myProfileOption);
        devInfoOption = findViewById(R.id.devInfoOption);
        signOutOption = findViewById(R.id.signOutOption);

        // Handle close button
        closeButton.setOnClickListener(v -> finish());

        // Go to News Screen (Home)
        homeOption.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        // Go to My Profile Screen
        myProfileOption.setOnClickListener(v -> {
            startActivity(new Intent(this, UserInfoScreen.class));
        });

        // Go to Developer Info Screen
        devInfoOption.setOnClickListener(v -> {
            startActivity(new Intent(this, DevInfoScreen.class));
        });

        // Show sign out confirmation dialog
        signOutOption.setOnClickListener(v -> showSignOutDialog());
    }

    private void showSignOutDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_sign_out, null);

        AlertDialog dialog = new AlertDialog.Builder(this, R.style.CustomDialog)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        dialogView.findViewById(R.id.ok_btn).setOnClickListener(v -> {
            dialog.dismiss();
            finishAffinity(); // Close all activities
            System.exit(0);   // Ensure app process is killed
        });

        dialogView.findViewById(R.id.cancel_btn).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
