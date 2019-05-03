package com.clean.asteroids

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.clean.domain.GetAsteroidOfTheDay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var getAsteroidOfTheDay: GetAsteroidOfTheDay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as ActivityInjector).inject(this)

        getAsteroidOfTheDay.execute()
            .map { AsteroidMapper().map(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    Text.text = it.name
                    it?.let {
                        Glide.with(this).load(it.imageUrl).into(Image);
                    }
                },
                onError = {
                    Log.e("qwer", "error", it)
                }
            )
    }
}
