package com.yahoo.maklumi.mathmudah

import android.app.Fragment
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import java.util.*

/**
 * Created by HomePC on 27/1/2017.
 */
class MainActivityFragment : Fragment() {

    val pilihan = arrayListOf<Int>()
    var lokasiJawapan = 0
    var skor = 0
    var bilanganSoalan = 0
    var shouldPlayMusic: Boolean = false

    val had = 10 //had nombor untuk aktiviti. contoh 9 + 9
    var mplayer: MediaPlayer? = null
    var timer: CountDownTimer? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_main, container, false)


        if (savedInstanceState == null) {
            view.apply {
                butangMula.efek(R.animator.sekilas)
                butangMula.setOnClickListener { mulakan() }
                butangMainLagi.setOnClickListener {
                    mainLagi()
                }
                butang0.setOnClickListener { view -> periksaPilihanJawapan(view) }
                butang1.setOnClickListener { view -> periksaPilihanJawapan(view) }
                butang2.setOnClickListener { view -> periksaPilihanJawapan(view) }
                butang3.setOnClickListener { view -> periksaPilihanJawapan(view) }
            }
        }

        cekPreference()

        return view
    }


    fun cekPreference() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)

        shouldPlayMusic = sharedPref.getBoolean(SettingsFragment.KEY_MAIN_MUZIK_LATAR, false)
        if (mplayer == null) {
            mplayer = MediaPlayer.create(activity, R.raw.mvrasseli_play_the_game)
        }

        if (shouldPlayMusic) {
            mplayer?.setVolume(0.1f, 0.1f)
            mplayer?.isLooping = true
            mplayer?.start()
        } else {
            mplayer?.pause()

        }


    }

    fun mulakan() {
        butangMula.animation?.cancel()
        butangMula.visibility = View.INVISIBLE
        relativeLayoutPermainan.visibility = RelativeLayout.VISIBLE
        mainLagi()

    }

    fun mainLagi() {
        skor = 0
        bilanganSoalan = 0

        textViewPembilang.text = "30s"
        textViewSkor.text = "0/0"
        butangMainLagi.visibility = View.INVISIBLE
        enable(gridLayout, true)

        buatSoalan()

        timer = object : CountDownTimer(10100, 1000) {
            override fun onFinish() {
                butangMainLagi.visibility = View.VISIBLE
                textViewPembilang.text = "0s"
                val keputusan = "Markah : $skor/$bilanganSoalan"
                textViewKeputusan.visibility = View.VISIBLE

                enable(gridLayout, false)
                textViewKeputusan.animate()?.cancel()
                textViewKeputusan.text = keputusan

            }

            override fun onTick(milisaatLagi: Long) {
                val bilang = "${milisaatLagi / 1000}s"
                textViewPembilang.text = bilang
            }
        }
        timer?.start()
    }

    fun buatSoalan() {
        val rawak = Random()
        val a = rawak.nextInt(had)
        val b = rawak.nextInt(had)
        val soalan = "$a + $b ="

        textViewKiraKira.text = soalan

        pilihan.clear()

        lokasiJawapan = rawak.nextInt(4)

        val jawapanBetul = a + b

        for (lokasi in 0..3) {
            if (lokasi == lokasiJawapan) {
                pilihan.add(jawapanBetul)
            } else {
                val jawapanSalah = pilihanSalah(jawapanBetul, rawak)
                //  while (jawapanSalah == jawapanBetul) jawapanSalah = rawak.nextInt(had * 2 - 1)
                pilihan.add(jawapanSalah)
            }
        }

        butang0.text = pilihan[0].toString()
        butang1.text = pilihan[1].toString()
        butang2.text = pilihan[2].toString()
        butang3.text = pilihan[3].toString()
    }

    fun periksaPilihanJawapan(view: View) {
        val bunyiBetul = MediaPlayer.create(activity, R.raw.gmae)
        val bunyiSalah = MediaPlayer.create(activity, R.raw.sd_0)
        textViewKeputusan.visibility = View.VISIBLE
        if (view.tag == lokasiJawapan.toString()) {
            skor++
            textViewKeputusan.text = "Betul!"
            bunyiBetul.start()

        } else {
            textViewKeputusan.text = "Salah!"
            bunyiSalah.start()
        }
        textViewKeputusan.efek(R.animator.sekilas, 1000)
        bilanganSoalan++
        textViewSkor.text = "$skor / $bilanganSoalan"
        buatSoalan()
    }

    // enable and disable the layout with all its children
    fun enable(layout: ViewGroup, boolean: Boolean) {
        layout.isEnabled = boolean
        for (i in 0..layout.childCount - 1) {
            val child = layout.getChildAt(i)
            if (child is ViewGroup) {
                enable(child, boolean)
            } else {
                child.isEnabled = boolean
            }
        }
    }

    // tapisan
    fun pilihanSalah(betul: Int, random: Random): Int {
        var salah = random.nextInt(had * 2 - 1)
        // pastikan tak sama dengan pilihan betul
        // pilihan yang salah belum diberi lagi
        while (betul == salah || pilihan.contains(salah)) {
            salah = random.nextInt(had * 2 - 1)
        }
        return salah
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
        mplayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        cekPreference()
    }


    // extension
    fun View.efek(kesan: Int, tempoh: Int = 0) {
        val animasi = AnimationUtils.loadAnimation(activity,
                kesan)

        if (tempoh != 0) {
            this.postDelayed({ this.visibility = android.view.View.GONE }, tempoh.toLong())
        }

        this.startAnimation(animasi)
    }



    companion object {
        val TAG = MainActivityFragment::class.java.simpleName
    }

}