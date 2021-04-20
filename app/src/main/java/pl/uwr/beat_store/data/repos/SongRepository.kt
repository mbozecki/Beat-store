package pl.uwr.beat_store.data.repos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import pl.uwr.beat_store.data.models.Song
import pl.uwr.beat_store.data.models.Song.Companion.toSong

class SongRepository {
    var songList= ArrayList<Song>();
    private var songLiveData = MutableLiveData<ArrayList<Song>>();
    var firestore = Firebase.firestore;
    init {
        songLiveData.value= songList;
        println("KURWA MAc")

       /* firestore
                .collection("producers")
                .get()
                .addOnSuccessListener { users ->
                    for (user in users)
                    {
                        Log.e("a_url", user.id.toString());
                        firestore.collection("producers").document(user.id).collection("beats").get().addOnSuccessListener { beats ->
                            for (beat in beats)
                            {
                                var image= beat.data?.get("image").toString();
                                var name = beat.data?.get("name").toString();
                                var url = beat.data?.get("url").toString();
                                //beat.toSong()?.let { songList.add(it) };
                                songList.add(beat.toSong()!!);
                                println("TUTA");
                                println(beat.toSong());
                            }
                        }
                    }

                }.addOnFailureListener {
                    e -> Log.e("E", "Error writing getting song repo", e)
                }
        songLiveData?.setValue(songList);

        */
        //println("j"+songList);
    }

    fun getSongDData() : MutableLiveData<ArrayList<Song>>? {
        println("getSongDData")
        println(songLiveData);
        return songLiveData;
    }

     suspend fun getSongData() : MutableLiveData<ArrayList<Song>> {


        println("songList");
        return try {
            firestore
                .collection("producers")
                .get()
                .await().documents.mapNotNull {
                        Log.e("abc.url", "getSongData");
                        firestore
                                .collection("producers")
                                .document(it.id)
                                .collection("beats")
                                .get().await().mapNotNull { its ->
                                Log.e("2ab.url", its.data.toString());

                                    songList.add(its.toSong()!!);
                        }

                    }
            //println(songList);
            songLiveData.postValue(songList);
            println(songLiveData);
            songLiveData;
        }
        catch (e: Exception) {
            Log.e(" SongRepository", e.toString());
            songLiveData

        }

    }
}