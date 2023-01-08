package com.example.yazlab

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore

import java.security.spec.PSSParameterSpec.*
import java.text.*
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.android.synthetic.main.fragment_only2x2.*

@Suppress("IMPLICIT_CAST_TO_ANY")
class Only2x2Fragment : Fragment() {
    var caunt:Int=0
    var database= FirebaseFirestore.getInstance().collection("cards")
    val bp: ArrayList<Bitmap> = ArrayList<Bitmap>()
    val skor: ArrayList<Double> = ArrayList<Double>()
    val home: ArrayList<String> = ArrayList<String>()
    val homeSkor: ArrayList<Double> = ArrayList<Double>()
    val random: ArrayList<Int> = ArrayList<Int>()


    val id: ArrayList<Int> = ArrayList<Int>()
    private lateinit var buttons: List<ImageButton>
    private lateinit var cards: List<MemoryCard>
    private var indexOfSingleSelectedCard: Int? = null
    var numara=0
    var runnable=Runnable{}
    var handler= Handler(Looper.myLooper()!!)
    var mediaPlayer:MediaPlayer?=null
    var puan:Double=0.0
    var images = mutableListOf<Int>()
    var say: Int = 0
    var sayTime:Int=0
    var mediaPlayer2: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rand = java.util.Random()
        while (caunt<2){
            val number1 = rand.nextInt(44)
            if(random.contains(number1) || number1==0){
            }else{
                random.add(number1)
                caunt++
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_only2x2, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaPlayer?.reset()
        stopwatch()
        playSound("prologue")


        database
            .get()
            .addOnSuccessListener { result ->
                for (number1 in random){


                    for (document in result) {
                        if(number1.equals(document.id.toInt())){
                            val byteDizisi = document.getData().get("image").toString()

                            val bytes: ByteArray = Base64.decode(byteDizisi, Base64.DEFAULT)

                            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                            images.add(number1)
                            home.add(document.getData().get("home").toString())
                            skor.add(document.getData().get("skor").toString().toDouble())
                            homeSkor.add(document.getData().get("homeSkor").toString().toDouble())
                            bp.add(bitmap)
                            break
                        }



                    }
                    caunt++
                }



                home.addAll(home)
                homeSkor.addAll(homeSkor)
                skor.addAll(skor)
                bp.addAll(bp)
                images.addAll(images)

                buttons = listOf(only2x2ImageViewImage1, only2x2ImageViewImage2, only2x2ImageViewImage3, only2x2ImageViewImage4)

                cards = buttons.indices.map { index ->
                    MemoryCard(images[index])
                }
                cards.shuffled()
                for((i, card) in cards.withIndex()){
                    print(i)
                    print("->")
                    println(card.identifier)
                }

                buttons.forEachIndexed { index, button ->
                    button.setOnClickListener {

                        updateModels(index)
                        updateViews()


                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        only2x2TextViewPause.setOnClickListener {

            sayTime=1
            pauseSound("prologue")
            only2x2ImageViewSound.setImageResource(R.drawable.quitsound)
        }


        only2x2TextViewPlay.setOnClickListener {
            sayTime=0
            playSound("prologue")
            only2x2ImageViewSound.setImageResource(R.drawable.sound)
        }


    }

    private fun updateViews() {
        cards.forEachIndexed { index, card ->
            val button = buttons[index]
            if (card.isMatched) {
                button.alpha = 0.1f


            }

            if(card.isFaceUp) {

                button.setImageBitmap(bp[index])

            }
            else {
                button.setImageResource(R.drawable.black)

            }
        }
    }

    private fun updateModels(position: Int) {
        val card = cards[position]

        if (card.isFaceUp) {
            Toast.makeText(requireContext(), "Geçersiz hareket!", Toast.LENGTH_SHORT).show()
            return
        }

        if (indexOfSingleSelectedCard == null) {

            restoreCards()

            indexOfSingleSelectedCard = position

        } else {

            checkForMatch(indexOfSingleSelectedCard!!, position)

            indexOfSingleSelectedCard = null


        }
        card.isFaceUp = !card.isFaceUp
    }

    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }


    private fun checkForMatch(position1: Int, position2: Int) {
        if (cards[position1].identifier == cards[position2].identifier) {
            say += 1
            cards[position1].isMatched = true
            cards[position2].isMatched = true
            pauseSound("prologue")
            SystemClock.sleep(50)
            Toast.makeText(requireContext(), "Match found!!", Toast.LENGTH_SHORT).show()
            val cardSkor=skor[images.indexOf(cards[position1].identifier)]
            val homeSk=homeSkor[images.indexOf(cards[position1].identifier)]
            val sure=numara
            puan += (2.0 * cardSkor * homeSk) * (sure.toDouble() / 10)
            only2x2TextViewSkor.text = "Puanınız: ${puan.toInt()}"

            if(cards[position1].identifier==35){
                playSound("nirvanaa")
                SystemClock.sleep(7000)
                stopSound("nirvanaa")

            }else{
                playSound("happy")
                SystemClock.sleep(3000)
                stopSound("happy")

            }


            if(sayTime%2==0){
                SystemClock.sleep(150)

                playSound("prologue")
            }
            restoreCards()
        }
        else{
            val home1=home[images.indexOf(cards[position1].identifier)]
            val home2=home[images.indexOf(cards[position2].identifier)]


            if(home1.equals(home2)){
                val cardSkor1=skor[images.indexOf(cards[position1].identifier)]
                val cardSkor2=skor[images.indexOf(cards[position2].identifier)]
                val homeSk=homeSkor[images.indexOf(cards[position1].identifier)]
                val sure=numara
                puan -= ((cardSkor1+cardSkor2)/homeSk) * (sure.toDouble() / 10)
                only2x2TextViewSkor.text = "Puanınız: ${puan.toInt()}"

            }else{
                val cardSkor1=skor[images.indexOf(cards[position1].identifier)]
                val cardSkor2=skor[images.indexOf(cards[position2].identifier)]
                val homeSk1=homeSkor[images.indexOf(cards[position1].identifier)]
                val homeSk2=homeSkor[images.indexOf(cards[position2].identifier)]
                val sure=numara
                puan -= (((cardSkor1+cardSkor2)/2)*homeSk1*homeSk2) * (sure.toDouble() / 10)
                only2x2TextViewSkor.text = "Puanınız: ${puan.toInt()}"
            }
            restoreCards()
        }
    }
    fun playSound(name:String) {
        if(name.equals("prologue")){
            if (mediaPlayer2 == null) {
                mediaPlayer2 = MediaPlayer.create(requireContext(),R.raw.prologue)
                mediaPlayer2!!.isLooping = true
                mediaPlayer2!!.start()
            } else {
                mediaPlayer2!!.start()

            }

        }else if(name.equals("happy")){
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(requireContext(),R.raw.happy)
                mediaPlayer!!.isLooping = true
                mediaPlayer!!.start()
            } else {
                mediaPlayer!!.start()

            }

        }else if(name.equals("sad")){
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(requireContext(),R.raw.sad)
                mediaPlayer!!.isLooping = true
                mediaPlayer!!.start()
            } else {
                mediaPlayer!!.start()

            }

        }else if(name.equals("congratulations")){
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(requireContext(),R.raw.congratulations)
                mediaPlayer!!.isLooping = true
                mediaPlayer!!.start()
            } else {
                mediaPlayer!!.start()

            }

        }else if(name.equals("nirvanaa")){
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(requireContext(),R.raw.nirvanaa)
                mediaPlayer!!.isLooping = true
                mediaPlayer!!.start()
            } else {
                mediaPlayer!!.start()

            }

        }


    }

    fun pauseSound(name: String) {
        if(name.equals("happy") || name.equals("sad") || name.equals("congratulations") || name.equals("nirvanaa")){
            if (mediaPlayer?.isPlaying == true) mediaPlayer?.pause()

        }else{
            if (mediaPlayer2?.isPlaying == true) mediaPlayer2?.pause()

        }


    }

    fun stopSound(name: String) {

        if(name.equals("happy") || name.equals("sad") || name.equals("congratulations")|| name.equals("nirvanaa")){
            if (mediaPlayer != null) {
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
                mediaPlayer = null

            }

        }else{
            if (mediaPlayer2 != null) {
                mediaPlayer2!!.stop()
                mediaPlayer2!!.release()
                mediaPlayer2 = null

            }

        }
    }

    override fun onStop() {
        handler.removeCallbacks(runnable)

        super.onStop()
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer = null
        }
        if (mediaPlayer2 != null) {
            mediaPlayer2!!.release()
            mediaPlayer2 = null
        }

    }

    fun stopwatch() {
        numara = 45

        runnable = object : Runnable {
            override fun run() {
                numara = numara - 1
                if (numara != -1) {
                    if (say != 2) {
                        only2x2TextViewRemainingTime.text = "Kalan Süre:${numara}"
                        handler.postDelayed(runnable, 1000)
                    } else {
                        numara = 0
                        stopSound("prologue")
                        playSound("congratulations")
                        SystemClock.sleep(7000)
                        stopSound("congratulations")
                        PurchaseConfirmationDialogFragmentFinish(puan).show(
                            childFragmentManager, this@Only2x2Fragment.tag
                        )


                    }

                } else {
                    stopSound("prologue")
                    playSound("sad")
                    SystemClock.sleep(1500)
                    stopSound("sad")

                    PurchaseConfirmationDialogFragment(puan).show(
                        childFragmentManager, this@Only2x2Fragment.tag
                    )

                }

            }


        }
        handler.post(runnable)
    }
    class PurchaseConfirmationDialogFragment(skor: Double) : DialogFragment() {
        val skor=skor
        @SuppressLint("SuspiciousIndentation")
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            return AlertDialog.Builder(requireContext())
                .setTitle("Süreniz Bitti!!")
                .setMessage("Puanınız: ${skor}")
                .setPositiveButton("Tekrar Dene",DialogInterface.OnClickListener{dialogInterface, i ->
                    val action=Only2x2FragmentDirections.actionOnly2x2FragmentSelf()
                    findNavController().navigate(action)
                })
                .setNegativeButton("Çıkış",DialogInterface.OnClickListener{dialogInterface, i ->

                    val action=Only2x2FragmentDirections.actionOnly2x2FragmentToGameIntroFragment()
                    findNavController().navigate(action)


                })
                .create()
        }


    }
    class PurchaseConfirmationDialogFragmentFinish(skor: Double) : DialogFragment() {
        val skor=skor
        @SuppressLint("SuspiciousIndentation")
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(requireContext())
                .setTitle("Tebrikler!!")
                .setMessage("Puanınız: ${skor}")
                .setPositiveButton("Tekrar Dene",
                    DialogInterface.OnClickListener { dialogInterface, i ->


                        val action = Only2x2FragmentDirections.actionOnly2x2FragmentSelf()
                        findNavController().navigate(action)

                    })
                .setNegativeButton(
                    "Çıkış",
                    DialogInterface.OnClickListener { dialogInterface, i ->

                        val action =
                            Only2x2FragmentDirections.actionOnly2x2FragmentToGameIntroFragment()
                        findNavController().navigate(action)

                    })
                .create()


    }
}


