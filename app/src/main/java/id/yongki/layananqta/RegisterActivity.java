package id.yongki.layananqta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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

        regisBtn = findViewById(R.id.register_regisbtn);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

<<<<<<< HEAD
        regisBtn = findViewById(R.id.register_regisbtn);
=======

        //inisiasi view
        namaLengkap = findViewById(R.id.register_namaLengkap);
>>>>>>> 22fbf5689d715cbb5ac1e763aa6695d37d8edd74
        email = findViewById(R.id.register_etemail);
        password = findViewById(R.id.register_etpassword);
        repassword = findViewById(R.id.register_etrepassword);



        regisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gemail = email.getText().toString();
                String gpassword = password.getText().toString();
                String grepassword = repassword.getText().toString();

<<<<<<< HEAD

                if (gemail.isEmpty()) {
                    email.setError("Email tidak boleh kosong");
=======
                if (gnama.isEmpty()) {
                    String nama = getString(R.string.nama_kosong);
                    namaLengkap.setError(nama);
                } else if (gemail.isEmpty()) {
                    String emaile = getString(R.string.email_kosong);
                    email.setError(emaile);
                } else if (gnohp.isEmpty()) {
                    String nohpe = getString(R.string.nohp_kosong);
                    nohp.setError(nohpe);
                } else if (gpassword.isEmpty()) {
                    String passse = getString(R.string.pass_kosong);
                    password.setError(passse);
                } else if (!gpassword.equals(grepassword)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Password tidak cocok!", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    try {
                        epass = AESCrypt.encrypt(gpassword);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //koneksi ke firebase
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // membuat data user baru ke table users
                    Map<String, Object> user = new HashMap<>();
                    user.put("nama", gnama);
                    user.put("email", gemail);
                    user.put("Nohp", gnohp);
                    user.put("password", epass);
                    user.put("deskripsi", "");
                    // Add a new document with a generated ID
                    db.collection("users")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("tes", "DocumentSnapshot added with ID: " + documentReference.getId());
>>>>>>> 22fbf5689d715cbb5ac1e763aa6695d37d8edd74

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
<<<<<<< HEAD
=======

                    showDialog();

>>>>>>> 22fbf5689d715cbb5ac1e763aa6695d37d8edd74

                }
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
                .setMessage("Anda sudah terdaftar, Silahkan untuk melakukan login!")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

}