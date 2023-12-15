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
public class Candidate2Fragment extends Fragment {
    private ExoPlayer player;

    @BindView(R.id.point_evaluate_editText2)
    EditText point;
    @BindView(R.id.styledPlayerView22) StyledPlayerView styledPlayerView
            ;
    String vidURL;
    private Unbinder unbinder;

    public Candidate2Fragment() {
        // Required empty public constructor
    }
    public Candidate2Fragment(String url) {
       this.vidURL=url;
    }


    public static Candidate2Fragment newInstance() {
        Candidate2Fragment fragment = new Candidate2Fragment();

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
        View rootView= inflater.inflate(R.layout.fragment_candidate2, container, false);
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
                    if (i>=10){
                        s.replace(0,s.length(),"10");
                    }
                    if (i<0){
                        s.replace(0,s.length(),"0");
                    }
                    point.setText(s);
                    _ignore=false;
                }
            }

        });
        return rootView;
    }
    private void getPlayer() {
        // URL of the video to stream

        if (getContext()!=null){
            player= new ExoPlayer.Builder(getContext()).build();
        }

        styledPlayerView.setPlayer(player);
        MediaItem mediaItem= MediaItem.fromUri(vidURL);
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
    public void setVidURL(String url){
        this.vidURL=url;
    }
    public String getEditText(){
        return point.getText().toString();

    }
}