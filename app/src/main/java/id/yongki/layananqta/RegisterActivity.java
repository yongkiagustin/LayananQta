package id.yongki.layananqta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;




public class RegisterActivity extends AppCompatActivity {

    private TextView login;
    private EditText email, password, repassword;
    private ProgressBar progressBar;
    private Button regisBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.register_tvlogin);
        progressBar = findViewById(R.id.register_progressbar);
        regisBtn = findViewById(R.id.register_regisbtn);
        email = findViewById(R.id.register_etemail);
        password = findViewById(R.id.register_etpassword);
        repassword = findViewById(R.id.register_etrepassword);



        regisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                String gemail = email.getText().toString();
                String gpassword = password.getText().toString();
                String grepassword = repassword.getText().toString();
                String semail = getString(R.string.email_kosong);
                String spassword = getString(R.string.pass_kosong);
                String spassTidakCocok = getString(R.string.pass_tidak_cocok);

                if (gemail.isEmpty()) {
                    email.setError(semail);
                    progressBar.setVisibility(View.GONE);

                } else if (gpassword.isEmpty()) {
                    password.setError(spassword);
                    progressBar.setVisibility(View.GONE);
                } else if (!gpassword.equals(grepassword)) {
                    progressBar.setVisibility(View.GONE);
                    repassword.setError(spassTidakCocok);
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
                
            }
        });
    }
    private void showDialog(){
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