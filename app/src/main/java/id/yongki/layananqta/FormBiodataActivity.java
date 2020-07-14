package id.yongki.layananqta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FormBiodataActivity extends AppCompatActivity {

    EditText etnama, etnohp,etkota, etalamat, etprofesi, etlamakerja, etdeskripsi;
    CheckBox checkBox;
    LinearLayout technician;
    TextView labelChangePhoto;
    ImageView photo;
    ProgressBar uploadProgress;
    String imageUrl;
    String status = "";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    final int GALLERY_REQUEST_CODE = 111;
    final int REQUEST_GALLERY = 222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_biodata);

        etnama = findViewById(R.id.fb_etnama);
        etnohp = findViewById(R.id.fb_etnohp);
        etkota = findViewById(R.id.fb_etkota);
        etalamat = findViewById(R.id.fb_etalamat);
        photo = findViewById(R.id.biodata_imgProfile);
        labelChangePhoto = findViewById(R.id.biodata_labelChangePhoto);
        uploadProgress = findViewById(R.id.fb_progressbar);
        Button nextBtn = findViewById(R.id.fb_nextbtn);
        technician = findViewById(R.id.fb_technician);
        checkBox = findViewById(R.id.fb_checkbox);
        etprofesi = findViewById(R.id.fb_etprofesi);
        etdeskripsi = findViewById(R.id.fb_etdeskripsi);
        etlamakerja = findViewById(R.id.fb_etlamabekerja);
        technician.setVisibility(View.GONE);
        checkBox.isChecked();

        requestGalleryPermission();

        status = "nonactive";

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    technician.setVisibility(View.VISIBLE);
                    status = "pending";
                } else {
                    technician.setVisibility(View.GONE);
                    status = "nonactive";
                }
            }
        });


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProgress.setVisibility(View.VISIBLE);
                String nama = etnama.getText().toString();
                String nohp = etnohp.getText().toString();
                String kota = etkota.getText().toString();
                String alamat = etalamat.getText().toString();
                String profesi = etprofesi.getText().toString();
                String lamakerja = etlamakerja.getText().toString();
                String deskripsi = etdeskripsi.getText().toString();
                String email = mUser.getEmail();

                if (nama.isEmpty()) {
                    etnama.setError("Nama Tidak Boleh Kosong!");
                    uploadProgress.setVisibility(View.GONE);

                } else {

                    //membuat data user ke firestore
                    Map<String, Object> user = new HashMap<>();
                    user.put("nama", nama);
                    user.put("email", email);
                    user.put("nohp", nohp);
                    user.put("kota", kota);
                    user.put("alamat", alamat);
                    user.put("profesi",profesi);
                    user.put("lamakerja",lamakerja);
                    user.put("deskripsi",deskripsi);
                    user.put("status",status);
                    user.put("profilePic", imageUrl);
                    db.collection("users").document(mUser.getUid()).set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    uploadProgress.setVisibility(View.GONE);
                                    Intent intent = new Intent(FormBiodataActivity.this, ListActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    uploadProgress.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }


    void setOnClickButton() {
        labelChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                String[] mimeTypes = {"image/jpg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            }
        });
    }

    private void requestGalleryPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            setOnClickButton();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GALLERY);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setOnClickButton();
        } else {
            Toast.makeText(getApplicationContext(), "Please Accept Permission", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    assert data != null;

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    assert selectedImage != null;
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();
                    int columndIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgDecodeableString = cursor.getString(columndIndex);
                    cursor.close();
                    photo.setImageBitmap(BitmapFactory.decodeFile(imgDecodeableString));

                    uploadProgress.setVisibility(View.VISIBLE);
                    storage.getReference("profile/" + mUser.getUid() + ".jpg").putFile(Objects.requireNonNull(data.getData()))
                            .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful())
                                        throw Objects.requireNonNull(task.getException());

                                    uploadProgress.setVisibility(View.GONE);
                                    return storage.getReference("profile/" + mUser.getUid() + ".jpg").getDownloadUrl();
                                }
                            })
                            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    uploadProgress.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        assert downloadUri != null;
                                        imageUrl = downloadUri.toString();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Upload Error", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    break;
                default:
                    break;
            }
        }
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
        }
        return super.onOptionsItemSelected(item);
    }

}