package id.yongki.layananqta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AccountActivity extends AppCompatActivity {
 Button changeProfile,cancel,saveChange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        changeProfile = findViewById(R.id.account_changeprofile);
        cancel = findViewById(R.id.account_cancel);
        saveChange = findViewById(R.id.account_save);

        changeProfile.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.GONE);
        saveChange.setVisibility(View.GONE);

        //TODO buat ediitext jadi disable dan enable dan fungsi mengambil data dan foto
        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeProfile.setVisibility(View.GONE);
                cancel.setVisibility(View.VISIBLE);
                saveChange.setVisibility(View.VISIBLE);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeProfile.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.GONE);
                saveChange.setVisibility(View.GONE);
            }
        });
        saveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeProfile.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.GONE);
                saveChange.setVisibility(View.GONE);
            }
        });
    }

}