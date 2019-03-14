package com.clean.project

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.clean.domain.NasaRepository
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var nasaRepository: NasaRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as NasaApp).appComponent.inject(this)

        nasaRepository.getAsteroidOfTheDay()
            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    //                    Text.text = it.title
//                    Glide.with(this).load(it.url).into(Image);
                },
                onError = {
                    Log.e("qwer", "error", it)
                }
            )
    }
}
