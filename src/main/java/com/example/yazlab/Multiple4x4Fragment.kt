package com.example.yazlab

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues
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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_multiple4x4.*
import kotlinx.android.synthetic.main.fragment_multiple4x4.*
import kotlinx.android.synthetic.main.fragment_multiple4x4.*
import kotlinx.android.synthetic.main.fragment_multiple4x4.*
import kotlinx.android.synthetic.main.fragment_multiple4x4.*
import java.util.*
import kotlin.collections.ArrayList


class Multiple4x4Fragment : Fragment() {
    var caunt:Int=0
    var database= FirebaseFirestore.getInstance().collection("cards")
    val bp: ArrayList<Bitmap> = ArrayList<Bitmap>()
    val skor: ArrayList<Double> = ArrayList<Double>()
    val home: ArrayList<String> = ArrayList<String>()
    val homeSkor: ArrayList<Double> = ArrayList<Double>()
    val random: ArrayList<Int> = ArrayList<Int>()
    var game:Int=0

    val id: ArrayList<Int> = ArrayList<Int>()
    private lateinit var buttons: List<ImageButton>
    private lateinit var cards: List<MemoryCard>
    private var indexOfSingleSelectedCard: Int? = null
    var numara=0
    var runnable=Runnable{}
    var handler= Handler(Looper.myLooper()!!)
    var mediaPlayer: MediaPlayer?=null
    var puanGaming1:Double=0.0
    var puanGaming2:Double=0.0

    var images = mutableListOf<Int>()
    var say: Int = 0
    var sayTime:Int=0
    var mediaPlayer2: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rand = java.util.Random()

        while (caunt<8){
            if(caunt<2){
                val number1 = rand.nextInt(11)
                if(random.contains(number1) || number1==0){

                }else{
                    random.add(number1)
                    caunt++
                }
            }else if(caunt<4 && caunt>1){
                val number1 =12+ rand.nextInt(11)
                if(random.contains(number1) || number1==0){

                }else{
                    random.add(number1)
                    caunt++
                }
            }else if(caunt<6 && caunt>3){
                val number1 =23+ rand.nextInt(11)
                if(random.contains(number1) || number1==0){

                }else{
                    random.add(number1)
                    caunt++
                }
            }else if(caunt<8 && caunt>5){
                val number1 =34+ rand.nextInt(11)
                if(random.contains(number1) || number1==0){

                }else{
                    random.add(number1)
                    caunt++
                }
            }

    }
        println("random : $random")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_multiple4x4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaPlayer?.reset()

        stopwatch()
        playSound("prologue")

        println(random)
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

                buttons = listOf(
                    multiple4x4ImageViewImage1,
                    multiple4x4ImageViewImage2,
                    multiple4x4ImageViewImage3,
                    multiple4x4ImageViewImage4,
                    multiple4x4ImageViewImage5,
                    multiple4x4ImageViewImage6,
                    multiple4x4ImageViewImage7,
                    multiple4x4ImageViewImage8,
                    multiple4x4ImageViewImage9,
                    multiple4x4ImageViewImage10,
                    multiple4x4ImageViewImage11,
                    multiple4x4ImageViewImage12,
                    multiple4x4ImageViewImage13,
                    multiple4x4ImageViewImage14,
                    multiple4x4ImageViewImage15,
                    multiple4x4ImageViewImage16
                )


                cards = buttons.indices.map { index ->
                    MemoryCard(images[index])
                }
                Collections.shuffle(cards)
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
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
        multiple4x4TextViewPause.setOnClickListener {
            sayTime=1
            pauseSound("prologue")
            multiple4x4ImageViewSound.setImageResource(R.drawable.quitsound)
        }


        multiple4x4TextViewPlay.setOnClickListener {
            sayTime=0
            playSound("prologue")
            multiple4x4ImageViewSound.setImageResource(R.drawable.sound)
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
        numara = 60

        runnable = object : Runnable {
            override fun run() {
                numara = numara - 1
                if (numara != -1) {
                    if (say != 8) {
                        multiple4x4TextViewRemainingTime.text = "Kalan Süre:${numara}"
                        handler.postDelayed(runnable, 1000)
                    } else {
                        numara = 0
                        stopSound("prologue")
                        playSound("congratulations")
                        SystemClock.sleep(7000)
                        stopSound("congratulations")
                        Multiple4x4Fragment.PurchaseConfirmationDialogFragmentFinish(puanGaming1,puanGaming2).show(
                            childFragmentManager, this@Multiple4x4Fragment.tag
                        )


                    }

                } else {
                    stopSound("prologue")
                    playSound("sad")
                    SystemClock.sleep(1500)
                    stopSound("sad")

                    Multiple4x4Fragment.PurchaseConfirmationDialogFragment(puanGaming1,puanGaming2).show(
                        childFragmentManager, this@Multiple4x4Fragment.tag
                    )

                }

            }


        }
        handler.post(runnable)
    }

    class PurchaseConfirmationDialogFragment(gaming1: Double,gaming2: Double) : DialogFragment() {
        val gaming1=gaming1
        val gaming2=gaming2
        @SuppressLint("SuspiciousIndentation")
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            return AlertDialog.Builder(requireContext())
                .setTitle("Süreniz Bitti!!")
                .setMessage("1. Oyuncunun Puanı: ${gaming1}\n2. Oyuncunun Puanı: ${gaming2}")

                .setPositiveButton("Tekrar Dene",DialogInterface.OnClickListener{dialogInterface, i ->
                    val action=Multiple4x4FragmentDirections.actionMultiple4x4FragmentSelf()
                    findNavController().navigate(action)
                })
                .setNegativeButton("Çıkış",DialogInterface.OnClickListener{dialogInterface, i ->

                    val action=Multiple4x4FragmentDirections.actionMultiple4x4FragmentToGameIntroFragment()
                    findNavController().navigate(action)


                })
                .create()
        }


    }
    class PurchaseConfirmationDialogFragmentFinish(gaming1: Double,gaming2: Double) : DialogFragment() {
        val gaming1=gaming1
        val gaming2=gaming2

        @SuppressLint("SuspiciousIndentation")
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(requireContext())
                .setTitle("Tebrikler!!")
                .setMessage("1. Oyuncunun Puanı: ${gaming1}\n2. Oyuncunun Puanı: ${gaming2}")
                .setPositiveButton("Tekrar Dene",
                    DialogInterface.OnClickListener { dialogInterface, i ->


                        val action = Multiple4x4FragmentDirections.actionMultiple4x4FragmentSelf()
                        findNavController().navigate(action)

                    })
                .setNegativeButton(
                    "Çıkış",
                    DialogInterface.OnClickListener { dialogInterface, i ->

                        val action =
                            Multiple4x4FragmentDirections.actionMultiple4x4FragmentToGameIntroFragment()
                        findNavController().navigate(action)

                    })
                .create()


    }

    private fun updateViews() {
        cards.forEachIndexed { index, card ->
            val button = buttons[index]
            if (card.isMatched) {
                button.alpha = 0.1f


            }
            if (card.isFaceUp) {
                button.setImageBitmap(bp[images.indexOf(card.identifier)])
            } else {
                button.setImageResource(R.drawable.black)

            }
        }
    }

    private fun updateModels(position: Int) {
        val card = cards[position]

        if (card.isFaceUp) {
            Toast.makeText(requireContext(), "Invalid move!", Toast.LENGTH_SHORT).show()
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
        if (game%2==0){
            if (cards[position1].identifier == cards[position2].identifier) {
                say += 1
                cards[position1].isMatched = true
                cards[position2].isMatched = true
                pauseSound("prologue")
                Toast.makeText(requireContext(), "Match found!!", Toast.LENGTH_SHORT).show()
                val cardSkor=skor[images.indexOf(cards[position1].identifier)]
                val homeSk=homeSkor[images.indexOf(cards[position1].identifier)]
                puanGaming1 += (2.0 * cardSkor * homeSk)
                multiple4x4TextViewSkorGaming1.text = "1. Oyuncunun Puanı: ${puanGaming1.toInt()}"
                SystemClock.sleep(50)

                if(cards[position1].identifier==35){
                    playSound("nirvanaa")
                    SystemClock.sleep(7000)
                    stopSound("nirvanaa")
                    SystemClock.sleep(150)
                }else{
                    playSound("happy")
                    SystemClock.sleep(3000)
                    stopSound("happy")
                    SystemClock.sleep(150)
                }
                if(sayTime%2==0){
                    playSound("prologue")
                }
                restoreCards()
            }
            else{
                game=1
                val home1=home[images.indexOf(cards[position1].identifier)]
                val home2=home[images.indexOf(cards[position2].identifier)]


                if(home1.equals(home2)){
                    val cardSkor1=skor[images.indexOf(cards[position1].identifier)]
                    val cardSkor2=skor[images.indexOf(cards[position2].identifier)]
                    val homeSk=homeSkor[images.indexOf(cards[position1].identifier)]
                    puanGaming1 -= ((cardSkor1+cardSkor2)/homeSk)
                    multiple4x4TextViewSkorGaming1.text = "1. Oyuncunun Puanı: ${puanGaming1.toInt()}"

                }else{
                    val cardSkor1=skor[images.indexOf(cards[position1].identifier)]
                    val cardSkor2=skor[images.indexOf(cards[position2].identifier)]
                    val homeSk1=homeSkor[images.indexOf(cards[position1].identifier)]
                    val homeSk2=homeSkor[images.indexOf(cards[position2].identifier)]

                    puanGaming1 -= (((cardSkor1+cardSkor2)/2)*homeSk1*homeSk2)
                    multiple4x4TextViewSkorGaming1.text = "1. Oyuncunun Puanı: ${puanGaming1.toInt()}"
                }
                restoreCards()
            }
        }else{
            if (cards[position1].identifier == cards[position2].identifier) {
                say += 1
                cards[position1].isMatched = true
                cards[position2].isMatched = true
                pauseSound("prologue")
                SystemClock.sleep(50)
                Toast.makeText(requireContext(), "Match found!!", Toast.LENGTH_SHORT).show()
                val cardSkor=skor[images.indexOf(cards[position1].identifier)]
                val homeSk=homeSkor[images.indexOf(cards[position1].identifier)]
                puanGaming2 += (2.0 * cardSkor * homeSk)
                multiple4x4TextViewSkorGaming2.text = "2. Oyuncunun Puanını: ${puanGaming2.toInt()}"

                if(cards[position1].identifier==35){
                    playSound("nirvanaa")
                    SystemClock.sleep(7000)
                    stopSound("nirvanaa")
                    SystemClock.sleep(150)
                }else{
                    playSound("happy")
                    SystemClock.sleep(3000)
                    stopSound("happy")
                    SystemClock.sleep(150)
                }
                SystemClock.sleep(150)
                if(sayTime%2==0){
                    playSound("prologue")
                }
                restoreCards()
            }
            else{
                game=0
                val home1=home[images.indexOf(cards[position1].identifier)]
                val home2=home[images.indexOf(cards[position2].identifier)]


                if(home1.equals(home2)){
                    val cardSkor1=skor[images.indexOf(cards[position1].identifier)]
                    val cardSkor2=skor[images.indexOf(cards[position2].identifier)]
                    val homeSk=homeSkor[images.indexOf(cards[position1].identifier)]

                    puanGaming2 -= ((cardSkor1+cardSkor2)/homeSk)
                    multiple4x4TextViewSkorGaming2.text = "2. Oyuncunun Puanını: ${puanGaming2.toInt()}"

                }else{
                    val cardSkor1=skor[images.indexOf(cards[position1].identifier)]
                    val cardSkor2=skor[images.indexOf(cards[position2].identifier)]
                    val homeSk1=homeSkor[images.indexOf(cards[position1].identifier)]
                    val homeSk2=homeSkor[images.indexOf(cards[position2].identifier)]

                    puanGaming2 -= (((cardSkor1+cardSkor2)/2)*homeSk1*homeSk2)
                    multiple4x4TextViewSkorGaming2.text = "2. Oyuncunun Puanını: ${puanGaming2.toInt()}"
                }
                restoreCards()
            }
        }
    }
}