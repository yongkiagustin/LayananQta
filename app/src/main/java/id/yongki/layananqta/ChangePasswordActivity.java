package id.yongki.layananqta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.ResolutionDimension;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    boolean isPlay = false;
    EditText etpassword, etrepassword;
    Button button;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ImageView icEye = findViewById(R.id.cp_ic_eye);
        etpassword = findViewById(R.id.cp_etpassword);
        etrepassword = findViewById(R.id.cp_etrepassword);
        button = findViewById(R.id.cp_simpanbtn);

        icEye.setBackgroundResource(R.drawable.ic_eye_hide);
        icEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlay) {
                    v.setBackgroundResource(R.drawable.ic_eye_hide);
                    etpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    v.setBackgroundResource(R.drawable.ic_outline_eye_24);
                    etpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                isPlay = !isPlay;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = etpassword.getText().toString();
                String repassword = etrepassword.getText().toString();

                if (password.isEmpty()) {
                    etpassword.setError("Password tidak boleh kosong");
                } else if (repassword.isEmpty()) {
                    etrepassword.setError("Password tidak boleh kosong");
                } else if (!password.equals(repassword)) {
                    Toast.makeText(getApplicationContext(), "Password Tidak Cocok", Toast.LENGTH_LONG).show();
                } else {
                    mUser.updatePassword(password)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });

    }
}

