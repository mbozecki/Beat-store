package pl.uwr.beat_store.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseUser
import pl.uwr.beat_store.viewmodels.LoggedInViewModel
import pl.uwr.beat_store.R


class LoggedInFragment : Fragment() {
    private lateinit var loggedInUserTextView: TextView;
    private lateinit var logOutButton: Button;
    private lateinit var loggedInViewModel: LoggedInViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        loggedInViewModel= ViewModelProviders.of(this).get(LoggedInViewModel::class.java);
        loggedInViewModel.getUserLiveData().observe(this, Observer<FirebaseUser>() {

            fun onChanged(firebaseUser: FirebaseUser) {
                if (firebaseUser != null) {
                    loggedInUserTextView.setText("Logged In User: " + firebaseUser.getEmail());
                    logOutButton.setEnabled(true);
                } else {
                    logOutButton.setEnabled(false);
                }
            }
        })

        loggedInViewModel.getLoggedOutLiveData().observe(this,
            { loggedOut ->
                if (loggedOut) {
                    Toast.makeText(context, "User Logged Out", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_loggedInFragment_to_loginRegisterFragment)
                }
            })

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        var view : View =inflater.inflate(pl.uwr.beat_store.R.layout.fragment_loggedin, container, false);
        loggedInUserTextView = view.findViewById(R.id.fragment_loggedin_loggedInUser)
        logOutButton = view.findViewById(R.id.fragment_loggedin_logOut)
        logOutButton.setOnClickListener(View.OnClickListener {
            fun onClick(view: View?) {
                loggedInViewModel.logOut()
            }
        })
        return view
    }
}