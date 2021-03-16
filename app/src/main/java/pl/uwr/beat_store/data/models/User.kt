package pl.uwr.beat_store.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

//Data model for user

data class User(
        var uid: String,
        var name: String,
        var email: String,
) : Serializable {
    var isAuthenticated: Boolean = false;
    var isNew: Boolean = false;
    var isCreated:Boolean = false;
}