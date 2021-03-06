package pl.uwr.beat_store.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import pl.uwr.beat_store.R
import pl.uwr.beat_store.viewmodels.LoginRegisterViewModel


class LoginRegisterFragment : Fragment() {
    private lateinit var emailEditText: EditText;
    private lateinit var passwordEditText: EditText;
    private lateinit var loginButton: Button;
    private lateinit var registerButton: Button;
    private lateinit var loginRegisterViewModel: LoginRegisterViewModel;
    private lateinit var bottomNavigationMenu: BottomNavigationView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);



        loginRegisterViewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel::class.java);
        loginRegisterViewModel.getUserLiveData()?.observe(this, { firebaseUser ->
            if (FirebaseAuth.getInstance().currentUser != null) {
                Log.d(FirebaseAuth.getInstance().currentUser.toString(), "onChanged: ")
                findNavController(requireView())
                        .navigate(R.id.action_loginRegisterFragment_to_navigation_discover)
            }
        });
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        var view: View = inflater.inflate(R.layout.fragment_loginregister, container, false);

        emailEditText = view.findViewById(R.id.fragment_loginregister_email);
        passwordEditText = view.findViewById(R.id.fragment_loginregister_password);
        loginButton = view.findViewById(R.id.fragment_loginregister_login);
        registerButton = view.findViewById(R.id.fragment_loginregister_register);

        bottomNavigationMenu = activity?.findViewById(R.id.nav_view)!!; //Hiding navbar. It is not needed there
        bottomNavigationMenu.visibility = View.GONE;

        loginButton.setOnClickListener { view ->
            var email: String = emailEditText.text.toString();
            var password: String = passwordEditText.text.toString();
            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginRegisterViewModel.login(email, password);
            } else {
                Toast.makeText(
                        context,
                        "Email and password must be entered",
                        Toast.LENGTH_SHORT
                ).show();
            }

        }

        registerButton.setOnClickListener { view ->
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginRegisterViewModel.register(email, password)
            } else {
                Toast.makeText(
                        context,
                        "Email and password must be entered",
                        Toast.LENGTH_SHORT
                ).show()
            }
        }

        return view;
    }
}