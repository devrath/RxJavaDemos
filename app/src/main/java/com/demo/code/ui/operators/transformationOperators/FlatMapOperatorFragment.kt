package com.demo.code.ui.operators.transformationOperators

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.demo.code.databinding.FragmentOperatorFlatMapBinding

class FlatMapOperatorFragment : Fragment() {

    private val TAG = FlatMapOperatorFragment::class.java.simpleName
    private var _binding: FragmentOperatorFlatMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOperatorFlatMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onClickListeners() {
        binding.floatingActionButton.setOnClickListener {

        }
    }
}