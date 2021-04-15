package pl.uwr.beat_store.data.repos

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import pl.uwr.beat_store.data.models.Song
import pl.uwr.beat_store.data.models.Song.Companion.toSong

object SongRepository {
    suspend fun getSongData() : List<Song> {
        val firestore = Firebase.firestore;
        val songList = ArrayList<Song>()
        return try {
            firestore
                .collection("producers")
                .get()
                .await().documents.mapNotNull {
                        firestore.collection("producers")
                            .document(it.id).collection("beats").
                            get().await().mapNotNull { its ->
                                //Log.e("a.url", it.data?.get("url").toString());
                                its.toSong()?.let { it1 -> songList.add(it1) };
                        }
                    return songList.toList();
                    }
        }
        catch (e: Exception) {
            Log.e(" SongRepository", e.toString());
            emptyList();
        }
    }
}