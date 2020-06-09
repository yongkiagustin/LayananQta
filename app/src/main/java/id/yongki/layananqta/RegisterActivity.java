package id.yongki.layananqta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final EditText namaLengkap, email, nohp, password, repassword;
        Button regisBtn;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regisBtn = findViewById(R.id.register_regisbtn);
        namaLengkap = findViewById(R.id.register_namaLengkap);
        email = findViewById(R.id.register_etemail);
        nohp = findViewById(R.id.register_etnotlp);
        password = findViewById(R.id.register_etpassword);
        repassword = findViewById(R.id.register_etrepassword);


        regisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gnama = namaLengkap.getText().toString();
                String gemail = email.getText().toString();
                String gnohp = nohp.getText().toString();
                String gpassword = password.getText().toString();
                String grepassword = repassword.getText().toString();
                String epass = " ";

                if (gnama.isEmpty()) {
                    namaLengkap.setError("Nama lengkap tidak boleh kosong");
                } else if (gemail.isEmpty()) {
                    email.setError("Email tidak boleh kosong");
                }else if (gnama.isEmpty()) {
                    nohp.setError("Nomor tidak boleh kosong");
                }else if (gpassword.isEmpty()) {
                    password.setError("Password tidak boleh kosong");
                }
                else if(!gpassword.equals(grepassword)){
                    Toast toast = Toast.makeText(getApplicationContext(), "Password tidak cocok!", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
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

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("tes", "Error adding document", e);

                                }
                            });
                    Toast toast = Toast.makeText(getApplicationContext(), "Selamat, anda sudah terdaftar! Silahkan melakukan login", Toast.LENGTH_LONG);
                    toast.show();


                }
            }
        });
    }
}