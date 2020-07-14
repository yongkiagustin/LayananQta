package id.yongki.layananqta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import static id.yongki.layananqta.ListActivity.EXTRA_MESSAGE;

public class DetailFreelancerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_freelancer);
        Intent intent = getIntent();
        String id = intent.getStringExtra(EXTRA_MESSAGE);

        TextView textView = findViewById(R.id.detail_textview);
        textView.setText(id);
    }
}