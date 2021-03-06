package pl.uwr.beat_store.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import pl.uwr.beat_store.data.repos.AuthAppRepository


class LoginRegisterViewModel(application: Application) : AndroidViewModel(application) {
    private var authAppRepository : AuthAppRepository = AuthAppRepository(application);
    private var userLiveData : MutableLiveData<FirebaseUser>?=null;

    init {
        userLiveData = authAppRepository.getUserLiveData()!!
    }

    fun login(email: String, password: String){
        authAppRepository.login(email, password);
    }

    fun register(email: String, password: String) {
        authAppRepository.register(email, password);
    }

    fun getUserLiveData() : MutableLiveData<FirebaseUser>? {
        return userLiveData;
    }
}