package pl.uwr.beat_store.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import pl.uwr.beat_store.data.repos.AuthAppRepository


class LoggedInViewModel(application: Application) : AndroidViewModel(application){
    private var authAppRepository: AuthAppRepository = AuthAppRepository(application);
    private var userLiveData: MutableLiveData<FirebaseUser>?=null;
    private var loggedOutLiveData: MutableLiveData<Boolean>?=null;

    init {
        //super(application)
        userLiveData = authAppRepository.getUserLiveData()!!
        loggedOutLiveData = authAppRepository.getLoggedOutLiveData()!!
    }

    fun logOut() {
        authAppRepository.logOut()
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser>? {
        return userLiveData
    }

    fun getLoggedOutLiveData(): MutableLiveData<Boolean>? {
        return loggedOutLiveData
    }
}