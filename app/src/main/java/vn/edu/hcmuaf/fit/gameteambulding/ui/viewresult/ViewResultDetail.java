package vn.edu.hcmuaf.fit.gameteambulding.ui.viewresult;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import vn.edu.hcmuaf.fit.gameteambulding.R;

public class ViewResultDetail extends AppCompatActivity{
    private View back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_result);
        back = (View) findViewById(R.id.back);
        back();
        // Các thao tác khác nếu cần
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
