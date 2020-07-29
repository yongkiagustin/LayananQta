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
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import id.yongki.layananqta.adapter.RecyclerAdapter;
import id.yongki.layananqta.model.UsersModel;

public class ListActivity extends AppCompatActivity implements RecyclerAdapter.OnItemListener {
    public static final String EXTRA_MESSAGE = "id.yongki.layananqta.MESSAGE";
    FirebaseAuth firebaseAuth;
    TextView label;
    ArrayList<UsersModel> usersList = new ArrayList<>();
    RecyclerAdapter recyclerAdapter;
    LinearLayoutManager linearLayoutManager;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentSnapshot lastVisible;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    boolean isScrolling;
    boolean isLastItemReached;
    private static final int PAGE_SIZE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        linearLayoutManager = new LinearLayoutManager(this);
        label = findViewById(R.id.list_label);
        label.setVisibility(View.GONE);
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), usersList, this);
        recyclerView = findViewById(R.id.myRecyclerview);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        progressBar = findViewById(R.id.list_progressbar);
        progressBar.setVisibility(View.VISIBLE);

        readData();


    }

    private void readData() {
        db.collection("users")
                .whereEqualTo("status", "Active").orderBy("kota", Query.Direction.ASCENDING).limit(PAGE_SIZE)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() < 1) {
                                label.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);

                            } else {
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                    usersList.add(new UsersModel(
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

                                    ));

                                }
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setAdapter(recyclerAdapter);
                                lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                                RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                        super.onScrollStateChanged(recyclerView, newState);
                                        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                            isScrolling = true;
                                            progressBar.setVisibility(View.VISIBLE);


                                        }
                                    }

                                    @Override
                                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                        super.onScrolled(recyclerView, dx, dy);
                                        int firstVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                                        int visibleItemCount = linearLayoutManager.getChildCount();
                                        int totalItemCount = linearLayoutManager.getItemCount();
                                        if (isScrolling && (firstVisibleItem + visibleItemCount == totalItemCount) && !isLastItemReached) {
                                            isScrolling = false;
                                            db.collection("users")
                                                    .whereEqualTo("status", "Active").orderBy("kota", Query.Direction.ASCENDING).startAfter(lastVisible).limit(10)
                                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                                        usersList.add(new UsersModel(
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

                                                        ));
                                                    }
                                                    progressBar.setVisibility(View.GONE);
                                                    recyclerAdapter.notifyDataSetChanged();

                                                    if (task.getResult().size() < PAGE_SIZE) {
                                                        isLastItemReached = true;
                                                    }
                                                    if (!isLastItemReached) {
                                                        lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                                                        Toast.makeText(getApplicationContext(), "page 2", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                };

                                recyclerView.addOnScrollListener(onScrollListener);
                            }
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
        } else if (item.getItemId() == R.id.action_account) {
            startActivity(new Intent(getApplicationContext(), AccountActivity.class));
        } else if (item.getItemId() == R.id.action_changepassword) {
            startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getApplicationContext(), DetailFreelancerActivity.class);
        intent.putExtra(EXTRA_MESSAGE, usersList.get(position).docid);
        startActivity(intent);
    }
}