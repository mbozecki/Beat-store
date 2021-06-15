package pl.uwr.beat_store.data.repos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FavoritesRepository {
    private val user: FirebaseUser = Firebase.auth.currentUser;
    private val email: String = user.email;

    var favoritesList = ArrayList<String>();
    private var favoritesLiveData = MutableLiveData<ArrayList<String>>();
    var firestore = Firebase.firestore;
    private lateinit var f: DocumentSnapshot;

    init {
        favoritesLiveData.value = favoritesList;
    }

    suspend fun getFavoritesData(): ArrayList<String> {
        return try {
            f = firestore
                    .collection("users").document(email)
                    .get()
                    .await();
            favoritesList = f.data?.get("favorites") as ArrayList<String>;
            favoritesList;
        } catch (e: Exception) {
            Log.e(" favoritesRepository", e.toString());
            favoritesList;
        }

    }

    fun deleteFavoritesData(name: String) {
        val docRef = firestore.collection("users").document(email)
        docRef.update("favorites", FieldValue.arrayRemove(name))
        favoritesLiveData.value = (favoritesList);
    }
}