package pl.uwr.beat_store.data.repos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import pl.uwr.beat_store.data.models.Song

class CartRepository {
    private val user: FirebaseUser = Firebase.auth.currentUser;
    private val email: String = user.email;

    var cartList = ArrayList<String>();
    var purchaseList = ArrayList<String>();
    private var purchaseLiveData = MutableLiveData<ArrayList<String>>()

    private var cartLiveData = MutableLiveData<ArrayList<String>>();
    var firestore = Firebase.firestore;

    init {
        cartLiveData.value = cartList;

    }

    suspend fun getCartData(): ArrayList<String> {
        return try {
            var f = firestore
                    .collection("users").document(email)
                    .get()
                    .await();
            cartList = f.data?.get("cart") as ArrayList<String>;
            cartList;
        } catch (e: Exception) {
            Log.e(" CartRepository", e.toString());
            cartList;
        }

    }

    fun deleteCartData(name: String) {
        val docRef = firestore.collection("users").document(email)
        docRef.update("cart", FieldValue.arrayRemove(name))
        cartLiveData.value = (cartList);
    }

    suspend fun getPurchaseData(): ArrayList<String> {
        return try {
            var fs = firestore
                    .collection("users").document(email)
                    .get()
                    .await()
            purchaseList = fs.data?.get("purchased") as ArrayList<String>;
            purchaseList;
        } catch (e: Exception) {
            Log.e(" PrchaseRepository", e.toString());
            purchaseList;
        }

    }

    fun addToPurchase(songList: ArrayList<Song>) {
        val docRef = firestore.collection("users").document(email)
        for (x in songList) {
            docRef.update("purchased", FieldValue.arrayUnion(x.name))
        }

    }
}