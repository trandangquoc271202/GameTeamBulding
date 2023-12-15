package vn.edu.hcmuaf.fit.gameteambulding;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;

//Bundle key: competitionPath = pass in the id of competition
public class UploadView extends AppCompatActivity {
    StorageReference storageReference;
    String competitionPath;
    FirebaseAuth auth= FirebaseAuth.getInstance();
    String competitionID;
    FirebaseFirestore  db= FirebaseFirestore.getInstance();
    String userID;
    String userPath;
    //fake data
    String fakeUserID="User00011";
    String fakeCompID="competition000111";
    //fake end
    LinearProgressIndicator indicator;

    Uri video;
    MediaController mc;
    MaterialButton selectVideo, uploadVideo;
    VideoView videoView;
    TextView name;
    TextView compID;
    ActivityResultLauncher<Intent> activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {

            if(o.getResultCode()== RESULT_OK){
                if(o.getData()!=null){
                    uploadVideo.setEnabled(true);
                    videoView.setVisibility(View.VISIBLE);
                    video=o.getData().getData();
                    Log.d("path",video.toString());
                   videoView.setVideoURI(video);
                   videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                       @Override
                       public void onPrepared(MediaPlayer mp) {
                           mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                               @Override
                               public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                                   /*
                                    * add media controller
                                    */
                                   mc = new MediaController(UploadView.this);
                                   videoView.setMediaController(mc);
                                   /*
                                    * and set its position on screen
                                    */
                                   mc.setAnchorView(videoView);
                               }
                           });
                       }
                   });
                    videoView.start();
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


        videoView= findViewById(R.id.video_Upload_View);
        if(getIntent().getExtras()!=null){
            if(getIntent().getExtras().getString("competitionPath")!=null){
                competitionPath =getIntent().getExtras().getString("competitionPath");
            }
            else{
                competitionPath ="F1KOzoe56XhzzevycpRC";}
            }
        else{
        competitionPath ="F1KOzoe56XhzzevycpRC";}

        if(auth.getCurrentUser()!=null) {
            userID=auth.getCurrentUser().getUid();

        }else {
            userID = "xIwX5LPaEs1N0sehdDWg";
        }
        indicator=findViewById(R.id.process);

        selectVideo=findViewById(R.id.selectVideo);
        uploadVideo=findViewById(R.id.uploadVideo);
        name=findViewById(R.id.competition_name);
        compID=findViewById(R.id.competition_id);
        mc = new MediaController(this);

        DocumentReference docRef = db.collection("COMPETITION2").document(competitionPath);
        docRef.addSnapshotListener( new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                name.setText(value.getString("title"));
                compID.setText(docRef.getPath().substring(docRef.getPath().indexOf("/")+1,docRef.getPath().length()));
                competitionID=docRef.getPath().substring(docRef.getPath().indexOf("/")+1,docRef.getPath().length());

            }
        });


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



                        String entryID=competitionID+userID;
                        String timeStamp = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss").format(Calendar.getInstance().getTime());
                        Entry e= new Entry(entryID,uri.toString(),userID,competitionID,timeStamp);
                        //e.setComp_id(competitionID);
                        Map<String, Object> entry = e.getNewMap();


                        db.collection("ENTRY_LIST").document(UUID.randomUUID().toString())
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