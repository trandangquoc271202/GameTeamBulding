package vn.edu.hcmuaf.fit.gameteambulding.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import vn.edu.hcmuaf.fit.gameteambulding.CreateCompetitionActivity;
import vn.edu.hcmuaf.fit.gameteambulding.HomeActivity;
import vn.edu.hcmuaf.fit.gameteambulding.ListContestActivity;
import vn.edu.hcmuaf.fit.gameteambulding.R;
import vn.edu.hcmuaf.fit.gameteambulding.databinding.FragmentHomeBinding;
import vn.edu.hcmuaf.fit.gameteambulding.ui.viewresult.ViewCompetition;
import vn.edu.hcmuaf.fit.gameteambulding.ui.viewresult.ViewResult;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private CardView createCompetition, listContest, viewCompetition;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        viewCompetition();
        createCompetition();
        listContest();
        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void createCompetition() {
        createCompetition = (CardView) binding.createCompetition;
        createCompetition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateCompetitionActivity.class);
                startActivity(intent);
            }
        });
    }
    public void listContest() {
        listContest = (CardView) binding.listContest;
        listContest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ListContestActivity.class);
                startActivity(intent);
            }
        });
    }
    public void viewCompetition(){
        Log.d("ViewCompetition", "onclick: ");
        viewCompetition = (CardView) binding.viewCompetition;
        viewCompetition.setOnClickListener(view -> {
            Log.d("ViewResult", "vo: ");
            Intent intent = new Intent(getActivity(), ViewCompetition.class);
            startActivity(intent);
        });
    }
}