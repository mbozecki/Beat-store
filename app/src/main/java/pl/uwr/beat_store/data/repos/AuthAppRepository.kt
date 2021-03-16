package pl.uwr.beat_store.data.repos

import android.app.Application
import android.os.Build
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class AuthAppRepository {

    private lateinit var application: Application;
    private lateinit var firebaseAuth: FirebaseAuth;
    private lateinit var userLiveData: MutableLiveData<FirebaseUser>; //Mutable=can change over time
    private lateinit var loggedOutLiveData: MutableLiveData<Boolean>;

    fun AuthAppRepository(application: Application) {
        this.application = application;
        firebaseAuth = FirebaseAuth.getInstance();
        userLiveData = MutableLiveData();
        loggedOutLiveData = MutableLiveData();
        if (firebaseAuth.getCurrentUser() != null) {
            userLiveData.postValue(firebaseAuth.getCurrentUser());
            loggedOutLiveData.postValue(false);
        }
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser> {
        return userLiveData;
    }

    fun getLoggedOutLiveData(): MutableLiveData<Boolean> {
        return loggedOutLiveData;
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun register(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password) //tries to create user with email+password
            .addOnCompleteListener(application.mainExecutor, {
                @Override
                fun onComplete(@NonNull task: Task<AuthResult>) {
                    if (task.isSuccessful) {
                        userLiveData.postValue(firebaseAuth.currentUser); //updating data if successful
                    } else {
                        Toast.makeText(application.applicationContext, "Registration Failure: ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

}