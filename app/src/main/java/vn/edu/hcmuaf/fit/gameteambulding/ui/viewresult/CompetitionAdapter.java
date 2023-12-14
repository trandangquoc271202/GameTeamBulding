package vn.edu.hcmuaf.fit.gameteambulding.ui.viewresult;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.hcmuaf.fit.gameteambulding.Model.Competition;
import vn.edu.hcmuaf.fit.gameteambulding.R;
import vn.edu.hcmuaf.fit.gameteambulding.ui.viewresult.my_interface.IClickItemUserListener;

public class CompetitionAdapter extends RecyclerView.Adapter<CompetitionAdapter.CompetitionViewHolder> {
    List<Competition> competitions;
    private IClickItemUserListener iClickItemUserListener;

    public CompetitionAdapter(List<Competition> competitions, IClickItemUserListener iClickItemUserListener) {
        this.competitions = competitions;
        this.iClickItemUserListener = iClickItemUserListener;
    }

    @NonNull
    @Override
    public CompetitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_competition, parent, false);
        return new CompetitionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompetitionViewHolder holder, int position) {
        Competition competition = competitions.get(position);
        if (competition == null) {
            return;
        }
        holder.bind(competition);


//        holder.tv_name.setText("Cuộc thi: " + competition.getTitle());
//        holder.tv_code.setText("Mã cuộc thi: " + competition.getCode());

//
    }





    @Override
    public int getItemCount() {
        if (competitions != null) {
            return competitions.size();
        }
        return 0;
    }

    public class CompetitionViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private TextView tv_code;
        private TextView btn;


        public CompetitionViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name1);
            tv_code = itemView.findViewById(R.id.tv_code);
            btn = itemView.findViewById(R.id.button_view);

        }

        public void bind(Competition competition) {
            // Set other data to your TextViews
            tv_name.setText("Cuộc thi: " + competition.getTitle());
            tv_code.setText("Mã cuộc thi: " + competition.getCode());

            // Set the document ID as a tag
            btn.setOnClickListener(view -> {
                // Handle click event and pass necessary data to the new activity
                btn.setOnClickListener(view1 -> {
              iClickItemUserListener.onClickItemUser(competition);
//                    onItemClick(competition);
                });
            });

        }
    }
}
