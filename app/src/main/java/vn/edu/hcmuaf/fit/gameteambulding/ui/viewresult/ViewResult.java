package vn.edu.hcmuaf.fit.gameteambulding.ui.viewresult;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import vn.edu.hcmuaf.fit.gameteambulding.R;


public class ViewResult extends AppCompatActivity {
    private View back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //log
        Log.d("ViewResult", "onCreate: ");
        setContentView(R.layout.activity_result_exam);
        back = (View) findViewById(R.id.back);
        back();
    }

    public void onTextViewClick(View view) {
        // Chuyển đến màn hình mới
        Intent intent = new Intent(this, ViewResultDetail.class);
        startActivity(intent);
    }

    public void back() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
