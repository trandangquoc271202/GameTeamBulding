package vn.edu.hcmuaf.fit.gameteambulding.Firebase;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import vn.edu.hcmuaf.fit.gameteambulding.Model.User;

public class RegisterService {

    private FirebaseFirestore db;

    public RegisterService() {
        db = FirebaseFirestore.getInstance();
    }
    public void createUserFirebaseStore(User user){
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", user.getUsername());
        userMap.put("email", user.getEmail());
        userMap.put("password", user.getPassword());
// Add a new document with a generated ID
        db.collection("Users")
                .add(userMap)
                .addOnSuccessListener(documentReference -> Log.d(TAG,
                        "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}
