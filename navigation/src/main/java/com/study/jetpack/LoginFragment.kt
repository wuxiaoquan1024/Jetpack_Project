package com.study.jetpack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.login_btn).setOnClickListener {
            // 将welcome_fragment 及以上的 fragment 全部 从堆栈中弹出
//            findNavController().popBackStack(R.id.welcome_fragment, true)
            findNavController().navigate(R.id.activity_home)
//            requireActivity().finish()
        }

        view.findViewById<Button>(R.id.back).setOnClickListener {
            findNavController().popBackStack()
        }
    }

}