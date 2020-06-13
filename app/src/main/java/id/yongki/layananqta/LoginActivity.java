package id.yongki.layananqta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private EditText etemail, etPassword;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();

        loginBtn = findViewById(R.id.login_loginbtn);
        etemail = findViewById(R.id.login_etemail);
        etPassword = findViewById(R.id.login_etpassword);

        final String email = etemail.getText().toString();
        String password = etPassword.getText().toString();
        String epass = null;
        try {
            epass = AESCrypt.encrypt(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String finalEpass = epass;
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO HARUS MEMBUAT FUNGSI LOGIN
//                firebaseAuth.signInWithEmailAndPassword(email, finalEpass)
//                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    // Sign in success, update UI with the signed-in user's information
//                                    Intent intent = new Intent(LoginActivity.this, ListActivity.class);
//                                    startActivity(intent);
//                                } else {
//                                    // If sign in fails, display a message to the user.
//                                    Log.w("cek login failed", "signInWithEmail:failure", task.getException());
//                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                            Toast.LENGTH_SHORT).show();
//
//                                }
//
//                                // ...
//                            }
//                        });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }
}
