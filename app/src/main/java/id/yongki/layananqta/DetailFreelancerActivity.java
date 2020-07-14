package id.yongki.layananqta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static id.yongki.layananqta.ListActivity.EXTRA_MESSAGE;

public class DetailFreelancerActivity extends AppCompatActivity {
EditText etnama, etkota, etalamat, etprofesi, etdeskripsi;
Button button;
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
        etdeskripsi = findViewById(R.id.detail_etdeskripsi);
        button = findViewById(R.id.detail_contactbtn);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //todo tinggal menambah text ke whatsapp
                String number = "088971684145";
                Uri uri = Uri.parse("smsto:" + number);
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.setPackage("com.whatsapp");
                String text = "Halo, Saya mendapatkan informasi anda dari aplikasi LayananQta, saya ingin menggunakan jasa anda. Bisa minta waktunya sebentar?";
                intent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(intent, ""));
            }
        });
    }
}