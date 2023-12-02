package vn.edu.hcmuaf.fit.gameteambulding;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UploadView extends AppCompatActivity {
    StorageReference storageReference;

    LinearProgressIndicator indicator;
    Uri video;
    MaterialButton selectVideo, uploadVideo;
    VideoView videoView;
    ActivityResultLauncher<Intent> activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if(o.getResultCode()== RESULT_OK){
                if(o.getData()!=null){
                    uploadVideo.setEnabled(true);
                    video=o.getData().getData();
//                    StorageReference reference=storageReference.child("video/"+ UUID.randomUUID().toString());
//                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//
//                            //videoView.findViewById(R.id.video_Upload_View);
//
//                        }
//                    });
                    //  Glide.with(UploadView.this).load(video).into(videoView);
                }
            }
            else {
                Toast.makeText(UploadView.this,  "Select video", Toast.LENGTH_SHORT).show();
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_view);
        FirebaseApp.initializeApp(this);
        storageReference = FirebaseStorage.getInstance().getReference();
        MaterialToolbar toolbar=findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        videoView= findViewById(R.id.video_Upload_View);
        indicator=findViewById(R.id.process);
        selectVideo=findViewById(R.id.selectVideo);
        uploadVideo=findViewById(R.id.uploadVideo);

        selectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("select");
                Intent intent= new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                activityResultLauncher.launch(intent);
            }
        });
        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadVideo(video);
            }
        });
    }
    private void uploadVideo(Uri uri){
        StorageReference reference=storageReference.child("videos/"+ UUID.randomUUID().toString());
        FirebaseFirestore  db= FirebaseFirestore.getInstance();

        reference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Map<String, Object> entry = new HashMap<>();
                        entry.put("ENTRY_LINK", uri.toString());


                        db.collection("ENTRY").document(UUID.randomUUID().toString())
                                .set(entry)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                        Toast.makeText(UploadView.this,"Upload success",Toast.LENGTH_SHORT).show();

                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(UploadView.this,"Upload failed",Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                indicator.setVisibility(View.VISIBLE);
                indicator.setMax(Math.toIntExact(snapshot.getTotalByteCount()));
                indicator.setProgress(Math.toIntExact(snapshot.getBytesTransferred()));
            }
        });
    }
}