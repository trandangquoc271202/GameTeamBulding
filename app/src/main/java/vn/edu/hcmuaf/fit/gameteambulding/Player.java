package vn.edu.hcmuaf.fit.gameteambulding;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;

import butterknife.Unbinder;

public class Player extends AppCompatActivity {
ExoPlayer player;
    private Unbinder unbinder;
String url="https://firebasestorage.googleapis.com/v0/b/gameteambulding.appspot.com/o/videos%2F05a88dbf-e5a9-4b8f-9fc6-3ecdb9aa1366?alt=media&token=40913e06-0769-4a85-a9cd-61dee4b19999";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        StyledPlayerView styledPlayerView=findViewById(R.id.viewplayer);
        player= new ExoPlayer.Builder(Player.this).build();
        styledPlayerView.setPlayer(player);
        MediaItem mediaItem= MediaItem.fromUri(url);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);

    }
    @Override
    public void onStop(){
        super.onStop();
        player.setPlayWhenReady(false);
        player.release();
        player=null;
    }
}