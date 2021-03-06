package pl.uwr.beat_store.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import pl.uwr.beat_store.R
import pl.uwr.beat_store.viewmodels.LoggedInViewModel


class LoggedInFragment : Fragment() {
    private var loggedInUserTextView: TextView? = null;
    private lateinit var logOutButton: Button;
    private var loggedInViewModel: LoggedInViewModel? = null;
    private lateinit var bottomNavigationMenu: BottomNavigationView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        loggedInViewModel = ViewModelProviders.of(this).get(LoggedInViewModel::class.java);
        loggedInViewModel!!.getUserLiveData()?.observe(this, { firebaseUser ->
            if (FirebaseAuth.getInstance().currentUser != null) {
                loggedInUserTextView?.setText("Logged In User: " + firebaseUser.getEmail());
                logOutButton.isEnabled = true;
            } else {
                logOutButton.isEnabled = false;
            }
        }
        );

        loggedInViewModel!!.getLoggedOutLiveData()?.observe(this,
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

        var view: View = inflater.inflate(R.layout.fragment_loggedin, container, false);
        loggedInUserTextView = view.findViewById(R.id.fragment_loggedin_loggedInUser)
        logOutButton = view.findViewById(R.id.fragment_loggedin_logOut)
        bottomNavigationMenu = activity?.findViewById(R.id.nav_view)!!; //Hiding navbar. It is not needed there
        bottomNavigationMenu.visibility = View.GONE;
        logOutButton.setOnClickListener {
            loggedInViewModel?.logOut()
            //activity?.supportFragmentManager?.popBackStack(); TODO wylaczenie mozliwosci powrodu do fragmentu po kliknieciu back buttona

        };

        return view
    }
}