package pl.uwr.beat_store.data.repos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import pl.uwr.beat_store.data.models.Song
import pl.uwr.beat_store.data.models.Song.Companion.toSong

class SongRepository {
    var songList = ArrayList<Song>();
    private var songLiveData = MutableLiveData<ArrayList<Song>>();
    var firestore = Firebase.firestore;

    init {
        songLiveData.value = songList;

    }

    fun getSongDData(): MutableLiveData<ArrayList<Song>>? {
        println("getSongDData")
        println(songLiveData);
        return songLiveData;
    }

    suspend fun getSongData(): ArrayList<Song> {

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

            songList;
        } catch (e: Exception) {
            Log.e(" SongRepository", e.toString());
            songList;
        }

    }
}