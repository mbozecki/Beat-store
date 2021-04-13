package pl.uwr.beat_store.ui

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import pl.uwr.beat_store.R
import java.io.IOException
import kotlin.math.ceil

class MusicPlayerFragment : Fragment() {

    private lateinit var beatnameText: TextView;
    private lateinit var producerText: TextView;
    private lateinit var playButton : Button;
    private lateinit var pauseButton : Button;
    private lateinit var seekBar :SeekBar;
    private lateinit var mediaPlayer: MediaPlayer;
    private lateinit var firebaseDatabase: FirebaseDatabase;
    private lateinit var audioUrl : String;
    private var startTime=0;
    private var finalTime=0;
    private var pausedTime=0;
    var job: Job? =null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
       // audioUrl = "https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_700KB.mp3"; //temp
        firebaseDatabase= FirebaseDatabase.getInstance();
        val firestore = Firebase.firestore;

        firestore.firestoreSettings= FirebaseFirestoreSettings.Builder().build();
        firestore
                .collection("urls").document("links")
                .get()
                .addOnSuccessListener {
                    audioUrl= it.data?.get("link").toString();
                    Log.e("a_url", audioUrl);
                }.addOnFailureListener {
                    e -> Log.e("E", "Error writing document", e)
                }

    }

    @InternalCoroutinesApi
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_musicplayer, container, false);

        beatnameText= view.findViewById(R.id.beatname);
        producerText= view.findViewById(R.id.producername);
        playButton = view.findViewById(R.id.start);
        pauseButton= view.findViewById(R.id.pause);
        seekBar= view.findViewById(R.id.seekBar);

        playButton.setOnClickListener {
            playAudio(audioUrl, pausedTime);
        }

        pauseButton.setOnClickListener{
            if (mediaPlayer.isPlaying)
            {
                pausedTime = mediaPlayer.currentPosition;
                job?.cancel();
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                Toast.makeText(this.context, "audio paused", Toast.LENGTH_SHORT).show();
            }
            else {
                //job?.cancel();
                Toast.makeText(this.context, "Audio aint workin", Toast.LENGTH_SHORT).show();
            }
        }

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) { //using seekbar to change song position
                if (seekBar != null ) {
                    mediaPlayer.seekTo(mediaPlayer.duration*seekBar.progress/100);
                };
            }
        })
        return view;
    }

    @InternalCoroutinesApi
    private fun playAudio(audioUrl: String, pausedOn: Int) {
        mediaPlayer= MediaPlayer(); //initialization
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); //setting audio stream type

        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            finalTime= mediaPlayer.duration;
            startTime= mediaPlayer.currentPosition;
            println(startTime);
            seekBar.progress = startTime
            job= updateTime();
            mediaPlayer.start();
            if (pausedOn !=0)
                mediaPlayer.seekTo(pausedOn);
            Toast.makeText(this.context, "Audio is now playing", Toast.LENGTH_SHORT).show();
        } catch (e: IOException) {
            Toast.makeText(this.context, "error in playing: " + e, Toast.LENGTH_SHORT).show();
        }
    }


    @InternalCoroutinesApi
    private fun updateTime() : Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while (NonCancellable.isActive) {
                startTime = mediaPlayer.currentPosition
                println((startTime.toFloat() / finalTime.toFloat()) * 100);
                seekBar.progress = ((startTime.toFloat() / finalTime.toFloat())*100).toInt();
                delay(100)
            }
        }
    }
}

