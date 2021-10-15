package com.example.wanandroidclient.ui.fragment.publicNumber

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wanandroidclient.R


/**
 * A simple [Fragment] subclass.
 * Use the [PublicNumberFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PublicNumberFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_public_number, container, false)
    }


}