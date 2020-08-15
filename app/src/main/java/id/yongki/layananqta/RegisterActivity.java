package id.yongki.layananqta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

public class RegisterActivity extends AppCompatActivity {

    public EditText etemail, etpassword, etrepassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    boolean isPlay = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        TextView login = findViewById(R.id.register_tvlogin);
        progressBar = findViewById(R.id.register_progressbar);
        Button regisBtn = findViewById(R.id.register_regisbtn);
        etemail = findViewById(R.id.register_etemail);
        etpassword = findViewById(R.id.register_etpassword);
        etrepassword = findViewById(R.id.register_etrepassword);
        ImageView icEye = findViewById(R.id.register_ic_eye);


        icEye.setBackgroundResource(R.drawable.ic_eye_hide);

        icEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlay) {
                    v.setBackgroundResource(R.drawable.ic_eye_hide);
                    etpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                } else {
                    v.setBackgroundResource(R.drawable.ic_outline_eye_24);
                    etpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                isPlay = !isPlay;
            }
        });


        regisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String gemail = etemail.getText().toString();
                String gpassword = etpassword.getText().toString();
                String grepassword = etrepassword.getText().toString();

                final String semail = getString(R.string.email_kosong);
                final String spassword = getString(R.string.pass_kosong);
                final String spassTidakCocok = getString(R.string.pass_tidak_cocok);

                progressBar.setVisibility(View.VISIBLE);

                if (gemail.isEmpty()) {
                    etemail.setError(semail);
                    progressBar.setVisibility(View.GONE);


                } else if (gpassword.isEmpty()) {
                    etpassword.setError(spassword);
                    progressBar.setVisibility(View.GONE);
                } else if (!gpassword.equals(grepassword)) {
                    progressBar.setVisibility(View.GONE);
                    etrepassword.setError(spassTidakCocok);
                } else {

                    mAuth.createUserWithEmailAndPassword(gemail, gpassword)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    showDialog();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });


                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();

            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Selamat!");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Anda berhasil mendaftar, klik Ok untuk melakukan login!")
                //.setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }
}