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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import id.yongki.layananqta.Model.UsersModel;

public class AccountActivity extends AppCompatActivity {
    Button changeProfile, cancel, saveChange;
    EditText etnama, etnohp, etalamat, etemail, etprofesi, etlamaKerja, etdeskripsi;
    TextView labelstatus, labelpending, labelChangePhoto;
    String status = "";
    String imageUrl;
    RadioGroup radioGroup;
    ImageView photo;
    CheckBox checkBox;
    ProgressBar uploadProgress;
    LinearLayout llcheckbox;
    RadioButton rbactive, rbnonactive;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    final int GALLERY_REQUEST_CODE = 111;
    final int REQUEST_GALLERY = 222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        etnama = findViewById(R.id.account_etnama);
        etnohp = findViewById(R.id.account_etnohp);
        etalamat = findViewById(R.id.account_etalamat);
        etemail = findViewById(R.id.account_etemail);
        etprofesi = findViewById(R.id.account_etprofesi);
        etlamaKerja = findViewById(R.id.account_etlamabekerja);
        etdeskripsi = findViewById(R.id.account_etdeskripsi);
        radioGroup = findViewById(R.id.account_radioGroup);
        checkBox = findViewById(R.id.account_checkbox);
        llcheckbox = findViewById(R.id.account_layout_checkbox);
        labelstatus = findViewById(R.id.account_label_status);
        labelpending = findViewById(R.id.account_label_status_pending);
        labelChangePhoto = findViewById(R.id.account_label_change_photo);
        photo = findViewById(R.id.account_imgprofile);
        uploadProgress = findViewById(R.id.account_progressbar);

        changeProfile = findViewById(R.id.account_changeprofile);
        cancel = findViewById(R.id.account_cancel);
        saveChange = findViewById(R.id.account_save);

        requestGalleryPermission();

        changeProfile.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.GONE);
        saveChange.setVisibility(View.GONE);


        etnama.setEnabled(false);
        etnohp.setEnabled(false);
        etalamat.setEnabled(false);
        etemail.setEnabled(false);
        etprofesi.setEnabled(false);
        etlamaKerja.setEnabled(false);
        etdeskripsi.setEnabled(false);
        String uid = mUser.getUid();

        radioGroup.setVisibility(View.GONE);
        checkBox.setVisibility(View.GONE);
        labelpending.setVisibility(View.GONE);

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
                        etnohp.setText(usersModel.nohp);
                        etalamat.setText(usersModel.alamat);
                        etemail.setText(usersModel.email);
                        etprofesi.setText(usersModel.profesi);
                        etlamaKerja.setText(usersModel.lamaKerja);
                        etdeskripsi.setText(usersModel.deskripsi);
                        status = usersModel.status; //active, nonactive, pending
                        labelstatus.setText(status);
                        Glide.with(AccountActivity.this).load(usersModel.profilePic).into(photo);

                        //TODO jika status=nonaktif, checkbox visible; jika status=pending, label STATUS:MENUNGGU KONFIRMASI ADMIN;
                        //TODO jika status = active, radio box active nonactive muncul
                        //kondisi status
                        if(status.equalsIgnoreCase("active")){
                            radioGroup.setVisibility(View.VISIBLE);
                            checkBox.setVisibility(View.GONE);
                            labelpending.setVisibility(View.GONE);
                        }else if(status.equalsIgnoreCase("pending")){
                            radioGroup.setVisibility(View.GONE);
                            checkBox.setVisibility(View.GONE);
                            labelpending.setVisibility(View.VISIBLE);
                        }else {
                            radioGroup.setVisibility(View.GONE);
                            checkBox.setVisibility(View.VISIBLE);
                            labelpending.setVisibility(View.GONE);


                        }

                    } else {
                        // Log.d(TAG, "No such document");
                    }
                } else {
                    // Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });




        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              BUTTON CHANGE PROFILE


                etnama.setEnabled(true);
                etnohp.setEnabled(true);
                etalamat.setEnabled(true);
                etprofesi.setEnabled(true);
                etlamaKerja.setEnabled(true);
                etdeskripsi.setEnabled(true);
                etemail.setEnabled(true);


                changeProfile.setVisibility(View.GONE);
                cancel.setVisibility(View.VISIBLE);
                saveChange.setVisibility(View.VISIBLE);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                BUTTON CANCEL

                etnama.setEnabled(false);
                etnohp.setEnabled(false);
                etalamat.setEnabled(false);
                etprofesi.setEnabled(false);
                etlamaKerja.setEnabled(false);
                etdeskripsi.setEnabled(false);
                etemail.setEnabled(false);

                changeProfile.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.GONE);
                saveChange.setVisibility(View.GONE);
            }
        });
        saveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                BUTTON SAVE

                etnama.setEnabled(false);
                etnohp.setEnabled(false);
                etalamat.setEnabled(false);
                etprofesi.setEnabled(false);
                etlamaKerja.setEnabled(false);
                etdeskripsi.setEnabled(false);
                etemail.setEnabled(false);

                changeProfile.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.GONE);
                saveChange.setVisibility(View.GONE);

                String nama = etnama.getText().toString();
                String email = etemail.getText().toString();
                String nohp = etnohp.getText().toString();
                String alamat = etalamat.getText().toString();
                String profesi = etprofesi.getText().toString();
                String lamakerja = etlamaKerja.getText().toString();
                String deskripsi = etdeskripsi.getText().toString();


                //membuat data user ke firestore
                Map<String, Object> user = new HashMap<>();
                user.put("nama", nama);
                user.put("email", email);
                user.put("nohp", nohp);
                user.put("alamat", alamat);
                user.put("profesi", profesi);
                user.put("lamakerja", lamakerja);
                user.put("deskripsi", deskripsi);
                user.put("status",status);
                user.put("profilePic", imageUrl);
                db.collection("users").document(mUser.getUid()).set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Data Berhasil Disimpan", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(AccountActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("", "Error adding document", e);
                            }
                        });
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

}