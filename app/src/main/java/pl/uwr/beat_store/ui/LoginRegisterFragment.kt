package pl.uwr.beat_store.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseUser
import pl.uwr.beat_store.R
import pl.uwr.beat_store.viewmodels.LoginRegisterViewModel


class LoginRegisterFragment : Fragment() {
    private lateinit var emailEditText : EditText;
    private lateinit var passwordEditText : EditText;
    private lateinit var loginButton : Button;
    private lateinit var registerButton : Button;
    private lateinit var loginRegisterViewModel : LoginRegisterViewModel;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        loginRegisterViewModel= ViewModelProviders.of(this).get(LoginRegisterViewModel::class.java);
        loginRegisterViewModel.getUserLiveData().observe(this, Observer<FirebaseUser>() {
            fun onChanged(firebaseUser: FirebaseUser) {
                if (firebaseUser != null) {
                    findNavController(requireView())
                        .navigate(R.id.action_loginRegisterFragment_to_loggedInFragment)
                }
            }
        });
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view : View =inflater.inflate(R.layout.fragment_loginregister, container, false);

        emailEditText = view.findViewById(R.id.fragment_loginregister_email);
        passwordEditText = view.findViewById(R.id.fragment_loginregister_password);
        loginButton = view.findViewById(R.id.fragment_loginregister_login);
        registerButton = view.findViewById(R.id.fragment_loginregister_register);

        loginButton.setOnClickListener(View.OnClickListener {
            fun OnClick(view: View) {
                var email: String = emailEditText.getText().toString();
                var password: String = passwordEditText.getText().toString();
                if (email.length > 0 && password.length > 0) {
                    loginRegisterViewModel.login(email, password);
                } else {
                    Toast.makeText(
                        context,
                        "Email and password must be entered",
                        Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });

        registerButton.setOnClickListener(View.OnClickListener {
            fun onClick(view: View) {
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                if (email.length > 0 && password.length > 0) {
                    loginRegisterViewModel.register(email, password)
                } else {
                    Toast.makeText(
                        context,
                        "Email and password must be entered",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        });


        return view;
    }
}