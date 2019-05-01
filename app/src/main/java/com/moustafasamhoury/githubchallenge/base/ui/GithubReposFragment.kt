package com.moustafasamhoury.githubchallenge.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * This is the base fragment for the whole application, all other fragments shall extend it.
 *
 *
 * @author moustafasamhoury
 * created on Wednesday, 01 May, 2019
 */

abstract class GithubReposFragment : Fragment() {
    protected abstract val layout: Int


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
    }


    /**
     * This function is called at the appropriate time to initialise the views
     * i.e. set listeners...
     *
     * @param rootView – The fragment's root view
     */
    protected abstract fun setupViews(rootView: View)


    /**
     * Override this method when you want to clean up resources
     */
    protected abstract fun cleanupResources()


}