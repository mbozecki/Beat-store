package pl.uwr.beat_store.data.models

import android.content.ContentValues.TAG
import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize

//Data model for Song
@Parcelize
data class Song (
    var url : String,
    var name : String,
    var producer : String,
    var image : String,
    var type : String
) : Parcelable {

    companion object {
        fun DocumentSnapshot.toSong(): Song? {
            return try {
                val url = get("url").toString()
                var name = get("name").toString()
                var image = get("image").toString()
                var type = get("type").toString();
                var producer = get("producer").toString()
                //Log.e("SONG", "song")
                Song(url, name, producer, image,type);
            } catch (e: Exception) {
                Log.e("SONG", "Error converting song profile", e)
                null
            }
        }

    }
}