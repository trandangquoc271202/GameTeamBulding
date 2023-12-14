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

import vn.edu.hcmuaf.fit.gameteambulding.Model.Contest;

public class ContestAdapter extends ArrayAdapter<Contest> {
    Context context;
    ArrayList<Contest> list;
    ListContestActivity main;
    TextView tv_title;
    public ContestAdapter(@NonNull Context context, ListContestActivity activity, ArrayList<Contest> list) {
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
            v = inflater.inflate(R.layout.item_contest, null);
        }
        final Contest item = list.get(position);
        if (v != null){
            tv_title = v.findViewById(R.id.tv_name);
            tv_title.setText(item.getTitle());
        }

        return v;
    }
}
