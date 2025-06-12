package lk.cmb.app;



import android.app.AlertDialog;

import android.content.DialogInterface;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import android.widget.ImageButton;

import android.widget.ImageView;

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

            startActivity(new Intent(this, NewsScreen.class));

            finish();

        });



// Go to My Profile Screen (you must create MyProfileScreen.java and its XML)

        myProfileOption.setOnClickListener(v -> {

            startActivity(new Intent(this, MainActivity.class));

        });



// Go to Developer Info Screen (you must create DevInfoScreen.java and its XML)

        devInfoOption.setOnClickListener(v -> {

            startActivity(new Intent(this, DevInfoScreen.class));

        });



// Show sign out confirmation dialog

        signOutOption.setOnClickListener(v -> showSignOutDialog());

    }



    private void showSignOutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Sign Out")
                .setMessage("Do you really want to sign out?")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Sign out and close the app completely
                    finishAffinity(); // Closes all activities
                    System.exit(0);   // Ensures process is killed
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


}