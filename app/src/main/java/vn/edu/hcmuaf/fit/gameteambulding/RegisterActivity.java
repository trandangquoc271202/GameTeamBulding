package vn.edu.hcmuaf.fit.gameteambulding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

import vn.edu.hcmuaf.fit.gameteambulding.Firebase.RegisterService;
import vn.edu.hcmuaf.fit.gameteambulding.Model.User;
import vn.edu.hcmuaf.fit.gameteambulding.databinding.ActivityLoginBinding;

public class RegisterActivity extends AppCompatActivity {


    ActivityLoginBinding binding;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    RegisterService registerService = new RegisterService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        handleClickEvent();
    }

    private void handleClickEvent() {
        binding.btnSignup.setOnClickListener(view -> {

            String username = binding.edtUsername.getText().toString().trim();
            String email = binding.editEmail.getText().toString().trim();
            String password = binding.edtPassword.getText().toString().trim();
            String rePassword = binding.editRePassword.getText().toString().trim();

            if (password.length() <= 6) {
                Toast.makeText(this, "Mật khẩu phải có độ dài hơn 6 kí tự", Toast.LENGTH_SHORT).show();

            } else if (!password.equals(rePassword)) {
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            } else {
                User user = new User(username, email, password, rePassword);
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(authResult -> {

                            if (authResult.isSuccessful()) {

                                registerService.createUserFirebaseStore(user);
                                Toast.makeText(this, "Đăng ký thành công tài khoản", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.d( "handleClickEvent: ", "error="+e.getMessage());
                            Toast.makeText(this, "error = " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });

            }
        });

        binding.btnSignin.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            finish();
        });
    }
}