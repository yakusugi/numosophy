package com.numosophy.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.numosophy.R
import com.numosophy.model.SaleViewModel
import androidx.fragment.app.viewModels
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class SalesAmountFragment : BaseFragment(R.layout.fragment_sales_amount) {  // ✅ extends BaseFragment

    private val saleViewModel: SaleViewModel by viewModels()
    private lateinit var lineChart: LineChart

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lineChart = view.findViewById(R.id.line_chart_sales)

        saleViewModel.allSales.observe(viewLifecycleOwner) { salesList ->
            if (salesList.isNullOrEmpty()) {
                showToast("No sales data available.")
                return@observe
            }

            val monthlySales = groupSalesByMonth(salesList)
            setupLineChart(monthlySales)
            styleChart()
        }
    }


    private fun groupSalesByMonth(salesList: List<com.numosophy.entity.Sale>): Map<Int, Double> {
        val monthTotals = HashMap<Int, Double>()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        for (sale in salesList) {
            try {
                val date = dateFormat.parse(sale.date)
                val calendar = Calendar.getInstance()
                calendar.time = date!!

                val month = calendar.get(Calendar.MONTH) + 1  // January = 0, so +1

                monthTotals[month] = (monthTotals[month] ?: 0.0) + sale.amount
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return monthTotals
    }

    private fun setupLineChart(monthlySales: Map<Int, Double>) {
        val entries = mutableListOf<Entry>()

        for (month in 1..12) {
            val amount = monthlySales[month] ?: 0.0
            entries.add(Entry(month.toFloat(), amount.toFloat()))
        }

        val dataSet = LineDataSet(entries, "Sales Amount per Month").apply {
            setDrawFilled(true)
            lineWidth = 2f
            circleRadius = 4f
            valueTextSize = 10f
            color = Color.parseColor("#6A0DAD") // purple line
            fillColor = Color.parseColor("#D8BFD8") // light purple fill
        }

        val lineData = LineData(dataSet)
        lineChart.data = lineData
        lineChart.description.isEnabled = false
        lineChart.animateX(1000)

        // ✨ Set month names on X Axis
        val months = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )

        val xAxis = lineChart.xAxis
        xAxis.granularity = 1f
        xAxis.setLabelCount(12, true)
        xAxis.valueFormatter = com.github.mikephil.charting.formatter.IndexAxisValueFormatter(months)

        lineChart.invalidate()
    }


    private fun styleChart() {
        val chart = view?.findViewById<LineChart>(R.id.line_chart_sales) ?: return

        // Set background color
        chart.setBackgroundColor(Color.WHITE)

        // Customize axis lines and labels
        val axisLeft = chart.axisLeft
        val axisRight = chart.axisRight
        val xAxis = chart.xAxis

        axisLeft.textColor = Color.parseColor("#6A0DAD") // Purple text for Y-axis
        axisRight.isEnabled = false  // Hide right Y-axis
        xAxis.textColor = Color.parseColor("#6A0DAD") // Purple text for X-axis

        // Customize grid lines
        axisLeft.gridColor = Color.LTGRAY
        xAxis.gridColor = Color.LTGRAY

        // Chart border
        chart.setDrawBorders(true)
        chart.setBorderColor(Color.LTGRAY)

        // Legend and description
        chart.legend.textColor = Color.parseColor("#6A0DAD")
        chart.description.isEnabled = false
    }


}
