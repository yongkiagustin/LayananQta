package id.yongki.layananqta;import androidx.annotation.NonNull;import androidx.appcompat.app.AppCompatActivity;import android.content.Intent;import android.os.Bundle;import android.text.method.HideReturnsTransformationMethod;import android.text.method.PasswordTransformationMethod;import android.view.View;import android.widget.Button;import android.widget.EditText;import android.widget.ImageView;import android.widget.ProgressBar;import android.widget.TextView;import android.widget.Toast;import com.google.android.gms.tasks.OnFailureListener;import com.google.android.gms.tasks.OnSuccessListener;import com.google.firebase.auth.AuthResult;import com.google.firebase.auth.FirebaseAuth;public class LoginActivity extends AppCompatActivity {    private FirebaseAuth firebaseAuth;    private ProgressBar progressBar;    private EditText etemail, etPassword;    boolean isPlay = false;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_login);        firebaseAuth = FirebaseAuth.getInstance();        progressBar = findViewById(R.id.login_progressbar);        Button loginBtn = findViewById(R.id.login_loginbtn);        etemail = findViewById(R.id.login_etemail);        etPassword = findViewById(R.id.login_etpassword);        TextView daftar = findViewById(R.id.login_tvregister);        ImageView icEye = findViewById(R.id.register_ic_eye);        icEye.setBackgroundResource(R.drawable.ic_eye_hide);        icEye.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                if (isPlay){                    v.setBackgroundResource(R.drawable.ic_eye_hide);                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());                }else{                    v.setBackgroundResource(R.drawable.ic_outline_eye_24);                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());                }                isPlay = !isPlay;            }        });        loginBtn.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                progressBar.setVisibility(View.VISIBLE);                String email = etemail.getText().toString();                String password = etPassword.getText().toString();                String semail = getString(R.string.email_kosong);                String spassword = getString(R.string.pass_kosong);                if (email.isEmpty()) {                    progressBar.setVisibility(View.GONE);                    etemail.setError(semail);                } else if (password.isEmpty()) {                    progressBar.setVisibility(View.GONE);                    etPassword.setError(spassword);                } else {                    firebaseAuth.signInWithEmailAndPassword(email, password)                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {                                @Override                                public void onSuccess(AuthResult authResult) {                                    startActivity(new Intent(getApplicationContext(), ListActivity.class));                                    finish();                                }                            }).addOnFailureListener(new OnFailureListener() {                        @Override                        public void onFailure(@NonNull Exception e) {                            progressBar.setVisibility(View.GONE);                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();                        }                    });                }            }        });        daftar.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));            }        });    }}