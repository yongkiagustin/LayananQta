package id.yongki.layananqta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        private FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final EditText email, password, repassword;
        Button regisBtn;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regisBtn = findViewById(R.id.register_regisbtn);
        email = findViewById(R.id.register_etemail);
        password = findViewById(R.id.register_etpassword);
        repassword = findViewById(R.id.register_etrepassword);


        regisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gemail = email.getText().toString();
                String gpassword = password.getText().toString();
                String grepassword = repassword.getText().toString();


                if (gemail.isEmpty()) {
                    email.setError("Email tidak boleh kosong");

                } else if (gpassword.isEmpty()) {
                    password.setError("Password tidak boleh kosong");
                } else if (!gpassword.equals(grepassword)) {
                    password.setError("Password Tidak Cocok");
                    repassword.setError("Password Tidak Cocok");
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("create user :", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("create user :", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                    
                                }
                            });

                }
            }
        });
    }
}