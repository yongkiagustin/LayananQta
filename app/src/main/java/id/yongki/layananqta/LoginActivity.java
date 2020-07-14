package id.yongki.layananqta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private EditText etemail, etPassword;
    boolean isPlay = false;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.login_progressbar);
        Button loginBtn = findViewById(R.id.login_loginbtn);
        etemail = findViewById(R.id.login_etemail);
        etPassword = findViewById(R.id.login_etpassword);
        TextView daftar = findViewById(R.id.login_tvregister);
        ImageView icEye = findViewById(R.id.login_ic_eye);
        TextView lupaPassword = findViewById(R.id.login_labelforgotPassword);


        icEye.setBackgroundResource(R.drawable.ic_eye_hide);

        icEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlay) {
                    v.setBackgroundResource(R.drawable.ic_eye_hide);
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    v.setBackgroundResource(R.drawable.ic_outline_eye_24);
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                isPlay = !isPlay;
            }
        });
        lupaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ResetPasswordActivity.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email = etemail.getText().toString();
                String password = etPassword.getText().toString();
                String semail = getString(R.string.email_kosong);
                String spassword = getString(R.string.pass_kosong);

                if (email.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    etemail.setError(semail);
                } else if (password.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    etPassword.setError(spassword);
                } else {

                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    db.collection("users").document(firebaseAuth.getCurrentUser().getUid()).get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if (documentSnapshot.exists()) {
                                                        startActivity(new Intent(getApplicationContext(), ListActivity.class));
                                                    } else {
                                                        startActivity(new Intent(getApplicationContext(), FormBiodataActivity.class));
                                                    }
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();

            }
        });
    }

}
