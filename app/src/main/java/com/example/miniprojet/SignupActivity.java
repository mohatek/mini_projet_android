package com.example.miniprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    private EditText mEmailEditText;
    private EditText mPasswordEditText;

    private EditText mUsernameEditText;

    private EditText mplayer_height;

    private EditText mplayer_post;
    private Button mSignUpButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public SignupActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        mEmailEditText = findViewById(R.id.email_edit_text);
        mPasswordEditText = findViewById(R.id.password_edit_text);
        mUsernameEditText = findViewById(R.id.username_edit_text);
        mSignUpButton = findViewById(R.id.sign_up_button);
        mplayer_height = findViewById(R.id.Player_height);
        mplayer_post = findViewById(R.id.player_post);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();
                String username = mUsernameEditText.getText().toString().trim();
                String height = mplayer_height.getText().toString().trim();
                String post = mplayer_post.getText().toString().trim();

                if (TextUtils.isEmpty(post)) {
                    mplayer_post.setError("post is required");
                    return;
                }
                if (TextUtils.isEmpty(height)) {
                    mplayer_height.setError("height is required");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    mEmailEditText.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPasswordEditText.setError("Password is required");
                    return;
                }

                if (password.length() < 6) {
                    mPasswordEditText.setError("Password must be at least 6 characters");
                    return;
                }

                if (TextUtils.isEmpty(username)) {
                    mUsernameEditText.setError("Username is required");
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    String uid = currentUser.getUid();

                                    // Save user information in Firebase Realtime Database
                                    HashMap<String, String> userMap = new HashMap<>();
                                    userMap.put("username", username);
                                    userMap.put("email", email);

                                    mDatabase.child(uid).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SignupActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                                Intent i= new Intent(SignupActivity.this, LoginActivity.class);
                                                startActivity(i);
                                            } else {
                                                Toast.makeText(SignupActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(SignupActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}