package pl.uwr.beat_store.ui.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DiscoverViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is DISCOVER Fragment"
    }
    val text: LiveData<String> = _text
}