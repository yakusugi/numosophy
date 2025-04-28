package com.numosophy.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

abstract class BaseFragment(layoutId: Int) : Fragment(layoutId) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    // Optional function you can override in child fragments
    open fun setupObservers() {
        // Child fragments can override this if needed
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun showError(e: Exception) {
        e.printStackTrace()
        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
    }
}
