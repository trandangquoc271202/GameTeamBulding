package vn.edu.hcmuaf.fit.gameteambulding.ui.viewresult;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.hcmuaf.fit.gameteambulding.R;

public class ViewResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_exam);
    }

    public void onTextViewClick(View view) {
        // Chuyển đến màn hình mới
        Intent intent = new Intent(this, ViewResultDetail.class);
        startActivity(intent);
    }
    
}
