package com.gracodev.postkeeper.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gracodev.postkeeper.ui.layouts.LoadingScreen

abstract class BaseFragment : Fragment() {
    abstract var TAG: String
    lateinit var dialog: LoadingScreen

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = LoadingScreen()
    }
}