package com.example.yazlab

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_game_intro.*
import kotlinx.android.synthetic.main.fragment_update_user.*

class UpdateUserFragment : Fragment() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth=FirebaseAuth.getInstance()

    }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_update_user, container, false)
        }
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val info= auth.currentUser?.email
            updateEditTextEmailAdress.text="${info}"

            updateButtonUpdate.setOnClickListener {
            update(it)
            }
            updateButtonEnter.setOnClickListener {
                auth.signOut()
                val action = UpdateUserFragmentDirections.actionUpdateUserFragment2ToProfileFragment()
                Navigation.findNavController(view).navigate(action)
            }

        }

   fun update(view: View){
       val user = auth.currentUser
           val newPassword=updateEditTextPassword.text.toString()
       if (newPassword.isNotEmpty()){
           user!!.updatePassword(newPassword)
               .addOnCompleteListener { task ->
                   if (task.isSuccessful) {

                       Toast.makeText(requireContext(), "Şifre Başarıyla Güncellendi!", Toast.LENGTH_LONG).show()
                       val action = UpdateUserFragmentDirections.actionUpdateUserFragment2ToGameIntroFragment()
                       Navigation.findNavController(view).navigate(action)

                   }
               }.addOnFailureListener { exception ->
                   Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG).show()
               }
       }else{
           Toast.makeText(requireContext(), "Şifre Alanı Boş Olamaz!", Toast.LENGTH_LONG).show()
       }



   }
}



