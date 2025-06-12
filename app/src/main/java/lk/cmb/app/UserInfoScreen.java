package lk.cmb.app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

public class UserInfoScreen extends AppCompatActivity {

    private TextView usernameTextView, emailTextView;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info_screen);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Get current user ID
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        }

        // Initialize views
        usernameTextView = findViewById(R.id.username);
        emailTextView = findViewById(R.id.email);

        // Fetch user data from Firebase
        fetchUserDataFromFirebase();

        // Set up Edit Info button
        findViewById(R.id.edit_info_btn).setOnClickListener(v -> showEditDialog());

        // Set up Sign Out button
        findViewById(R.id.sign_out_btn).setOnClickListener(v -> showSignOutDialog());

        // Handle window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_user_info_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void fetchUserDataFromFirebase() {
        if (currentUserId == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("username").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);

                    if (username != null && email != null) {
                        usernameTextView.setText("Username : " + username);
                        emailTextView.setText("Email: " + email);
                    } else {
                        // Handle case where data is incomplete
                        Toast.makeText(UserInfoScreen.this, "Incomplete user data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UserInfoScreen.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserInfoScreen.this, "Failed to load user data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_user_info, null);

        EditText editUsername = dialogView.findViewById(R.id.edit_username);
        EditText editEmail = dialogView.findViewById(R.id.edit_email);

        // Pre-fill current values
        editUsername.setText(getCurrentUsername());
        editEmail.setText(getCurrentEmail());

        AlertDialog dialog = new AlertDialog.Builder(this, R.style.CustomDialog)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        dialogView.findViewById(R.id.ok_btn).setOnClickListener(v -> {
            String newUsername = editUsername.getText().toString().trim();
            String newEmail = editEmail.getText().toString().trim();

            if (validateInput(newUsername, newEmail)) {
                updateUserInfoInFirebase(newUsername, newEmail);
                dialog.dismiss();
            }
        });

        dialogView.findViewById(R.id.cancel_btn).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private boolean validateInput(String username, String email) {
        if (username.isEmpty()) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updateUserInfoInFirebase(String newUsername, String newEmail) {
        if (currentUserId == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("username", newUsername);
        updates.put("email", newEmail);

        databaseReference.child(currentUserId).updateChildren(updates)
                .addOnSuccessListener(aVoid -> {
                    // Update UI only after successful database update
                    usernameTextView.setText("Username : " + newUsername);
                    emailTextView.setText("Email: " + newEmail);
                    Toast.makeText(UserInfoScreen.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UserInfoScreen.this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
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

    private String getCurrentUsername() {
        return usernameTextView.getText().toString().replace("Username : ", "");
    }

    private String getCurrentEmail() {
        return emailTextView.getText().toString().replace("Email: ", "");
    }
}
