package pl.uwr.beat_store.data.repos

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class AuthAppRepository(application: Application) {

    private var application: Application = application;
    private var firebaseAuth: FirebaseAuth? = null;
    private var userLiveData: MutableLiveData<FirebaseUser>? = null; //Mutable=can change over time
    private var loggedOutLiveData: MutableLiveData<Boolean>? = null;

    init { //on creating class
        firebaseAuth = FirebaseAuth.getInstance();
        userLiveData = MutableLiveData();
        loggedOutLiveData = MutableLiveData();
        if (firebaseAuth!!.currentUser != null) {
            userLiveData!!.postValue(firebaseAuth!!.currentUser);
            loggedOutLiveData!!.postValue(false);
        }
    }

    fun register(email: String, password: String) {
        firebaseAuth?.createUserWithEmailAndPassword(email, password) //tries to create user with email+password
                ?.addOnCompleteListener(application.mainExecutor, { task ->
                    if (task.isSuccessful) {
                        userLiveData?.postValue(firebaseAuth?.currentUser); //updating data if successful
                    } else {
                        Toast.makeText(application.applicationContext, "Registration Failure: ", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    fun login(email: String, password: String) {
        firebaseAuth?.signInWithEmailAndPassword(email, password) //tries to login user with email+password
                ?.addOnCompleteListener(application.mainExecutor, { task ->
                    if (task.isSuccessful) {
                        userLiveData?.postValue(firebaseAuth!!.currentUser);
                    } else {
                        Toast.makeText(application.applicationContext, "Login Failure: ", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    fun logOut() {
        firebaseAuth?.signOut();
        loggedOutLiveData?.postValue(true);
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser>? {
        return userLiveData;
    }

    fun getLoggedOutLiveData(): MutableLiveData<Boolean>? {
        return loggedOutLiveData;
    }

}