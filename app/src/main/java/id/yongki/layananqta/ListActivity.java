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
import android.view.View;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import id.yongki.layananqta.Adapter.RecyclerAdapter;
import id.yongki.layananqta.Model.UsersModel;

public class ListActivity extends AppCompatActivity implements RecyclerAdapter.OnItemListener {
    public static final String EXTRA_MESSAGE = "id.yongki.layananqta.MESSAGE";
    String getid="";
    FirebaseAuth firebaseAuth;
    ArrayList<UsersModel> usersList = new ArrayList<>();
    RecyclerAdapter recyclerAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        firebaseAuth = FirebaseAuth.getInstance();
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), usersList, this);
        RecyclerView recyclerView = findViewById(R.id.myRecyclerview);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        readData();
        String uid = mUser.getUid();
        final DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
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
                        getid = usersModel.docid;

                    }
                } else {
                    // Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

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
        }else if(item.getItemId()==R.id.action_changepassword){
            startActivity(new Intent(getApplicationContext(),ChangePasswordActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {

        //todo fungsi klik cardview masih belum berfungsi
        String id = getid;
        usersList.get(position);
        Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
        intent.putExtra(EXTRA_MESSAGE,id);
        startActivity(intent);
    }// todo nambah detail user
}