package com.example.yazlab

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_game_intro.*
import kotlinx.android.synthetic.main.fragment_profile.*

class GameIntroFragment : Fragment() {
    private lateinit var auth:FirebaseAuth


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth=FirebaseAuth.getInstance()









    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_intro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val info= auth.currentUser?.email
        IntroTextViewEposta.text = "E-posta Adresiniz: ${info}"

        IntroEnter.setOnClickListener {

            if(IntroCheckBoxOnly.isChecked){
                if(IntroCheckBox2x2.isChecked){
                    val action = GameIntroFragmentDirections.actionGameIntroFragmentToOnly2x2Fragment()
                    Navigation.findNavController(it).navigate(action)

                }
                else if (IntroCheckBox4x4.isChecked){

                            val action = GameIntroFragmentDirections.actionGameIntroFragmentToOnly4x4Fragment()
                            Navigation.findNavController(it).navigate(action)


                }else if (IntroCheckBox6x6.isChecked){
                    val action = GameIntroFragmentDirections.actionGameIntroFragmentToOnly6x6Fragment()
                    Navigation.findNavController(it).navigate(action)
                }


            }else if (IntroCheckBoxMultiple.isChecked){
                if(IntroCheckBox2x2.isChecked){
                    val action = GameIntroFragmentDirections.actionGameIntroFragmentToMultiple2x2Fragment()
                    Navigation.findNavController(it).navigate(action)

                }else if (IntroCheckBox4x4.isChecked){
                    val action = GameIntroFragmentDirections.actionGameIntroFragmentToMultiple4x4Fragment()
                    Navigation.findNavController(it).navigate(action)
                }else if (IntroCheckBox6x6.isChecked){
                    val action = GameIntroFragmentDirections.actionGameIntroFragmentToMultiple6x6Fragment()
                    Navigation.findNavController(it).navigate(action)
                }
            }

        }

        IntroUpdate.setOnClickListener {

                val action = GameIntroFragmentDirections.actionGameIntroFragmentToUpdateUserFragment()
                Navigation.findNavController(it).navigate(action)


        }

        IntroQuit.setOnClickListener {
            auth.signOut()
            val action = GameIntroFragmentDirections.actionGameIntroFragmentToProfileFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }
}