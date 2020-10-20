package com.adplay.ads

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ads.AdPlay

class MainActivity : AppCompatActivity() {

    private lateinit var adplay: AdPlay
    var tag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tag = getString(R.string.tag)
        adplay = findViewById(R.id.adsId)
        adplay.addTag(this,tag).load()
    }
}