package vn.edu.hcmuaf.fit.gameteambulding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class EmailContestAdapter extends ArrayAdapter<String> {
    Context context;
    TextView tv_Email;
    ArrayList<String> list;
    DetailContestActivity main;
    public EmailContestAdapter(@NonNull Context context, DetailContestActivity activity, ArrayList<String> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
        this.main = activity;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.criteria, null);
        }
        final String item = list.get(position);
        if (v != null){
            tv_Email = v.findViewById(R.id.textView);
            tv_Email.setText(item);
        }

        return v;
    }
}
