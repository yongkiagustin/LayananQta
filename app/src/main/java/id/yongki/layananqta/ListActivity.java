package id.yongki.layananqta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;

import java.util.ArrayList;
import java.util.Objects;

import id.yongki.layananqta.Adapter.RecyclerAdapter;
import id.yongki.layananqta.Model.UsersModel;

public class ListActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    ArrayList<UsersModel> usersList = new ArrayList<>();
    RecyclerAdapter recyclerAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        firebaseAuth = FirebaseAuth.getInstance();
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), usersList);
        RecyclerView recyclerView = findViewById(R.id.myRecyclerview);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        readData();



    }
    private void readData(){
        db.collection("users")
                .whereEqualTo("status","Active")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                               usersList.add(new UsersModel(
                                       (String) document.get("nama"),
                                       (String) document.get("nohp"),
                                       (String) document.get("kota"),
                                       (String) document.get("alamat"),
                                       (String) document.get("email"),
                                       (String)document.get("profesi"),
                                       (String)document.get("lamakerja"),
                                       (String)document.get("deskripsi"),
                                       (String)document.get("status"),
                                       (String)document.get("profilePic"),
                                       (String)document.getId()

                               ));
                                recyclerAdapter.notifyDataSetChanged();


                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());

                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_signout) {
            firebaseAuth.signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
            return true;
        }else if(item.getItemId()==R.id.action_account){
            startActivity(new Intent(getApplicationContext(),AccountActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}