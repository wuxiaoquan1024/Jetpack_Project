package com.study.jetpack.viewmodel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

class SelfViewModelFragment : Fragment() {

    private val model: CountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_viewmodel, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val text = view.findViewById<TextView>(R.id.textView)
        model.addObserver(this) {
            text.text = it.toString()
        }
        view.findViewById<Button>(R.id.button).setOnClickListener {
            model.countLiveData.value = model.countLiveData.value?.plus(1)
        }
    }

}