package com.moustafasamhoury.githubchallenge.base.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * This is the base activity for the whole application, all other activities shall extend it.
 *
 *
 * @author moustafasamhoury
 * created on Wednesday, 01 May, 2019
 */
abstract class GithubReposActivity : AppCompatActivity() {

    protected abstract val layout: Int


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
    }


    override fun onStart() {
        super.onStart()
        setupViews()
    }


    override fun onDestroy() {
        cleanupResources()
        super.onDestroy()
    }


    /**
     * This function is called at the appropriate time to initialise the views
     * i.e. set listeners...
     */
    protected abstract fun setupViews()


    /**
     * Override this method when you want to clean up resources
     */
    protected abstract fun cleanupResources()

}