package com.numosophy.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.numosophy.R
import com.numosophy.adapter.SalesAdapter
import com.numosophy.model.SaleViewModel
import androidx.fragment.app.viewModels

class SalesDataFragment : BaseFragment(R.layout.fragment_sales_data) {

    private val saleViewModel: SaleViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var salesAdapter: SalesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_sales)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        salesAdapter = SalesAdapter(emptyList())
        recyclerView.adapter = salesAdapter

        saleViewModel.allSales.observe(viewLifecycleOwner) { salesList ->
            if (salesList.isNullOrEmpty()) {
                showToast("No sales data available.")
            } else {
                salesAdapter.updateSales(salesList)
            }
        }
    }
}
