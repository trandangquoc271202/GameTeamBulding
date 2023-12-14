package vn.edu.hcmuaf.fit.gameteambulding;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Candidate1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Candidate1Fragment extends Fragment {
    private ExoPlayer player;
    @BindView(R.id.evaluation_save_btn) Button save;
    @BindView(R.id.point_evaluate_editText)
    EditText point;
    @BindView(R.id.styledPlayerView2) StyledPlayerView styledPlayerView
            ;


    String url="https://firebasestorage.googleapis.com/v0/b/gameteambulding.appspot.com/o/videos%2F05a88dbf-e5a9-4b8f-9fc6-3ecdb9aa1366?alt=media&token=40913e06-0769-4a85-a9cd-61dee4b19999";
    private Unbinder unbinder;

    public Candidate1Fragment() {
        // Required empty public constructor
    }
    public Candidate1Fragment(List<CriterionItem> l) {

    }

    public static Candidate1Fragment newInstance() {
        Candidate1Fragment fragment = new Candidate1Fragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
       View rootView= inflater.inflate(R.layout.fragment_cadidate1, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        getPlayer();
        point.addTextChangedListener(new TextWatcher() {
            boolean _ignore = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s!=null){
                    if (_ignore)
                        return;
                    _ignore = true;
                    Integer i= Integer.parseInt(s.toString());
                    if (i>=0&&i<10){
                        if (s.length()>1){
                            s.replace(0,s.length(),s.subSequence(s.length()-1,s.length()));
                        }
                    }
                    if (i>10){
                        s.replace(0,s.length(),"10");
                    }
                    if (i<0){
                        s.replace(0,s.length(),"0");
                    }
                    point.setText(s);
                    save.setEnabled(true);
                    _ignore=false;
                }
                }

        });
       return rootView;
    }
    private void getPlayer() {
        // URL of the video to stream
        String videoURL = "https://www.youtube.com/watch?v=3tmd-ClpJxA";
        if (getContext()!=null){
            player= new ExoPlayer.Builder(getContext()).build();
        }

        styledPlayerView.setPlayer(player);
        MediaItem mediaItem= MediaItem.fromUri(url);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);
        // Create the player with previously created TrackSelector
    }
    @Override
    public void
    onViewCreated(@NonNull View view,
                  @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);


    }
}