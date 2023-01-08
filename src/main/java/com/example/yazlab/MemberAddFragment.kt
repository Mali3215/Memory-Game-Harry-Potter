package com.example.yazlab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.yazlab.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_member_add.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.Objects


class MemberAddFragment : Fragment() {


    val arrayList: ArrayList<String> = ArrayList<String>()
    private lateinit var auth:FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth=FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_member_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AddButtonEnter.setOnClickListener {

            val action = MemberAddFragmentDirections.actionMemberAddFragmentToProfileFragment()
            Navigation.findNavController(it).navigate(action)

        }
        AddButtonNewUser.setOnClickListener {
            register(it)
        }
    }
    fun register(view: View){


        val email=AddEditTextEmailAdress.text.toString()

        val password=AddEditTextPassword.text.toString()
        if(email.isNotEmpty() || password.isNotEmpty()){
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ task->
                if(task.isSuccessful){
                    val action = MemberAddFragmentDirections.actionMemberAddFragmentToProfileFragment()
                    Navigation.findNavController(view).navigate(action)

                }

            }.addOnFailureListener { exception ->
                Toast.makeText(this.context,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this.context,"Bilgileriniz Boş Olamaz",Toast.LENGTH_LONG).show()
        }




    }



}

