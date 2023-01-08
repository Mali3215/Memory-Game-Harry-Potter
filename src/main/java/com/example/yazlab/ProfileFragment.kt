package com.example.yazlab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {



    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth= FirebaseAuth.getInstance()

        val currentUser=auth.currentUser
        if (currentUser!=null){
            Toast.makeText(requireContext(), "Hoşgeldiniz!", Toast.LENGTH_LONG).show()
            val action =
                ProfileFragmentDirections.actionProfileFragmentToGameIntroFragment()
            findNavController().navigate(action)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        btn_enter.setOnClickListener {

    login(it)

        }
        entertExtViewAddUser.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToMemberAddFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }
    fun login(view: View) {
        val email=enterEditTextEmail.text.toString()
        val password=enterEditTextPassword.text.toString()
        if(email.isNotEmpty() || password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Hoşgeldiniz!", Toast.LENGTH_LONG).show()
                    val action =
                        ProfileFragmentDirections.actionProfileFragmentToGameIntroFragment()
                    Navigation.findNavController(view).navigate(action)

                }

            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this.context,"Bilgileriniz Boş Olamaz",Toast.LENGTH_LONG).show()
        }





    }



}