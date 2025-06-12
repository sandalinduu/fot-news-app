package lk.cmb.app; // Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DevInfoScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_info_screen); // Replace with your current layout XML name

        Button exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start DevInfoActivity
                Intent intent = new Intent(DevInfoScreen.this, NewsScreen.class);
                startActivity(intent);
                // Optional: finish current activity if you don't want to keep it in back stack
                // finish();
            }
        });
    }
}
