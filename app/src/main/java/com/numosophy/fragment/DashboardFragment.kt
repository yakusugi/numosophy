package com.numosophy.fragment

import android.os.Bundle
import android.view.View
import com.numosophy.R

class DashboardFragment : BaseFragment(R.layout.fragment_dashboard) { // ✨ extend BaseFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup your dashboard here
        setupDashboard()
    }

    private fun setupDashboard() {
        val salesCard = view?.findViewById<androidx.cardview.widget.CardView>(R.id.sales_card)

        salesCard?.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, SalesAmountFragment())
                .addToBackStack(null)
                .commit()
        }
    }

}
