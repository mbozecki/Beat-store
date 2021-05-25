package pl.uwr.beat_store.data.repos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class CartRepository {
    private val user: FirebaseUser = Firebase.auth.currentUser;
    private val email: String = user.email;

    var cartList= ArrayList<String>();
    private var cartLiveData = MutableLiveData<ArrayList<String>>();
    var firestore = Firebase.firestore;
    init {
        cartLiveData.value= cartList;

    }

    suspend fun getCartData() : ArrayList<String> {
        return try {
            var f= firestore
                    .collection("users").document(email)
                    .get()
                    .await();
            //f.data?.get("cart");
             //          Log.e("28abc.url", f.data?.get("cart").toString());
            cartList= f.data?.get("cart") as ArrayList<String>;
            cartList;
        }
        catch (e: Exception) {
            Log.e(" CartRepository", e.toString());
            cartList;
        }

    }
}