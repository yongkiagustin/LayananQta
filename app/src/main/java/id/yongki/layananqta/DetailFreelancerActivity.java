package id.yongki.layananqta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import id.yongki.layananqta.model.UsersModel;

import static id.yongki.layananqta.ListActivity.EXTRA_MESSAGE;

public class DetailFreelancerActivity extends AppCompatActivity {
    EditText etnama, etkota, etalamat, etprofesi, etlamakerja, etdeskripsi;
    ImageView photo;
    String imageUrl, noHp;
    Button button;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_freelancer);
        Intent intent = getIntent();
        String id = intent.getStringExtra(EXTRA_MESSAGE);

        etnama = findViewById(R.id.detail_etnama);
        etkota = findViewById(R.id.detail_etkota);
        etalamat = findViewById(R.id.detail_etalamat);
        etprofesi = findViewById(R.id.detail_etprofesi);
        etlamakerja = findViewById(R.id.detail_etlamabekerja);
        etdeskripsi = findViewById(R.id.detail_etdeskripsi);
        button = findViewById(R.id.detail_contactbtn);
        photo = findViewById(R.id.detail_imgprofile);

        assert id != null;
        final DocumentReference docRef = db.collection("users").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    UsersModel usersModel = new UsersModel(
                            (String) document.get("nama"),
                            (String) document.get("nohp"),
                            (String) document.get("kota"),
                            (String) document.get("alamat"),
                            (String) document.get("email"),
                            (String) document.get("profesi"),
                            (String) document.get("lamakerja"),
                            (String) document.get("deskripsi"),
                            (String) document.get("status"),
                            (String) document.get("profilePic"),
                            (String) document.getId()
                    );
                    etnama.setText(usersModel.nama);
                    etkota.setText(usersModel.kota);
                    etalamat.setText(usersModel.alamat);
                    etprofesi.setText(usersModel.profesi);
                    etlamakerja.setText(usersModel.lamaKerja);
                    etdeskripsi.setText(usersModel.deskripsi);
                    noHp = usersModel.nohp;

                    if ((usersModel.profilePic == null) || (usersModel.profilePic.equals(""))) {
                        photo.setImageResource(R.drawable.img_default_user);
                    }else{
                        Glide.with(DetailFreelancerActivity.this).load(usersModel.profilePic).into(photo);
                    }
                }
            }
        });


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //todo tinggal menambah text ke whatsapp
                Uri uri = Uri.parse("smsto:" + noHp);
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.setPackage("com.whatsapp");
                String text = "Halo, Saya mendapatkan informasi anda dari aplikasi LayananQta, saya ingin menggunakan jasa anda. Bisa minta waktunya sebentar?";
                intent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(intent, ""));
            }
        });
    }
}