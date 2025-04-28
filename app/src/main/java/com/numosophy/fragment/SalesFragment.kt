package com.numosophy.fragment

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.viewModels
import com.numosophy.R
import com.numosophy.api.ApiClient
import com.numosophy.entity.Sale
import com.numosophy.model.CountryData
import com.numosophy.model.SaleViewModel
import com.numosophy.model.StateData
import com.numosophy.utility.createSaleFromInputs
import com.google.android.material.textfield.TextInputEditText
import com.numosophy.utility.GenderSelection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class SalesFragment : BaseFragment(R.layout.fragment_sales) { // ✨ extend BaseFragment

    private val saleViewModel: SaleViewModel by viewModels()

    private lateinit var titleInput: TextInputEditText
    private lateinit var amountInput: TextInputEditText
    private lateinit var buyerNameInput: TextInputEditText
    private lateinit var buyerGenderSpinner: Spinner
    private lateinit var buyerBirthInput: TextInputEditText
    private lateinit var buyerLocationInput: TextInputEditText
    private lateinit var notesInput: TextInputEditText
    private lateinit var dateInput: TextInputEditText
    private lateinit var addBtn: ImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupGenderSpinner()
        setupListeners()
    }

    private fun initViews(view: View) {
        titleInput = view.findViewById(R.id.add_title)
        amountInput = view.findViewById(R.id.add_amount)
        buyerNameInput = view.findViewById(R.id.add_buyer_name)
        buyerGenderSpinner = view.findViewById(R.id.spinner_add_gender)
        buyerBirthInput = view.findViewById(R.id.add_buyer_age)
        buyerLocationInput = view.findViewById(R.id.add_buyer_location)
        notesInput = view.findViewById(R.id.add_notes)
        dateInput = view.findViewById(R.id.add_date)
        addBtn = view.findViewById(R.id.add_btn)
    }

    private fun setupGenderSpinner() {
        val genders = GenderSelection.values()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genders)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        buyerGenderSpinner.adapter = adapter
    }

    private fun setupListeners() {
        dateInput.setOnClickListener { showDatePicker(dateInput) }
        buyerBirthInput.setOnClickListener { showDatePicker(buyerBirthInput) }

        buyerLocationInput.setOnClickListener {
            if (isTablet() && isLandscape()) {
                showLocationSidePanel(buyerLocationInput)
            } else {
                showLocationPopupDialog(buyerLocationInput)
            }
        }

        addBtn.setOnClickListener { saveSale() }
    }

    private fun saveSale() {
        val inputFields = listOf(titleInput, amountInput, buyerNameInput, dateInput)

        val allFilled = inputFields.all { it.text?.isNotEmpty() == true }
        if (!allFilled) {
            showToast("Please fill in Title, Amount, Buyer, and Date")
            return
        }

        val newSale = createSaleFromInputs(
            title = titleInput.text.toString(),
            amount = amountInput.text.toString().toDoubleOrNull() ?: 0.0,
            buyerName = buyerNameInput.text.toString(),
            buyerGender = (buyerGenderSpinner.selectedItem as? GenderSelection)?.label,
            buyerBirthdate = buyerBirthInput.text.toString().ifEmpty { null },
            buyerLocation = buyerLocationInput.text.toString().ifEmpty { null },
            notes = notesInput.text.toString().ifEmpty { null },
            date = dateInput.text.toString(),
            currentUserPublicKey = "mockPublicKey",
            currentGroupId = "mockGroupId"
        )

        saleViewModel.insertSale(newSale)
        showToast("✅ Sale saved: ${newSale.title}")
    }

    private fun showDatePicker(targetInput: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(requireContext(), { _, y, m, d ->
            val selectedDate = String.format("%04d-%02d-%02d", y, m + 1, d)
            targetInput.setText(selectedDate)
        }, year, month, day)

        datePicker.show()
    }

    private fun isTablet(): Boolean {
        return resources.getBoolean(R.bool.isTablet)
    }

    private fun isLandscape(): Boolean {
        return resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    private fun showLocationPopupDialog(targetInput: TextInputEditText) {
        val dialogView = layoutInflater.inflate(R.layout.popup_location_selector, null)

        val countrySpinner = dialogView.findViewById<Spinner>(R.id.spinner_country)
        val stateSpinner = dialogView.findViewById<Spinner>(R.id.spinner_state)
        val okButton = dialogView.findViewById<Button>(R.id.button_ok)

        var countryList = emptyList<CountryData>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.locationApiService.getCountriesAndStates()
                countryList = response.data

                withContext(Dispatchers.Main) {
                    val countryNames = countryList.map { it.name }
                    countrySpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, countryNames)
                }
            } catch (e: Exception) {
                showError(e)
            }
        }

        countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCountry = countryList[position]
                val stateNames = selectedCountry.states.map { it.name }
                stateSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, stateNames)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        okButton.setOnClickListener {
            val selectedCountry = countrySpinner.selectedItem?.toString() ?: ""
            val selectedState = stateSpinner.selectedItem?.toString() ?: ""

            targetInput.setText("$selectedCountry, $selectedState")
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showLocationSidePanel(targetInput: TextInputEditText) {
        val container = view?.findViewById<FrameLayout>(R.id.side_panel_container) ?: return showToast("Side panel not found").run {}

        container.visibility = View.VISIBLE
        container.removeAllViews()

        val sidePanelView = layoutInflater.inflate(R.layout.location_side_panel, container, false)
        container.addView(sidePanelView)

        val countrySpinner = sidePanelView.findViewById<Spinner>(R.id.spinner_country)
        val stateSpinner = sidePanelView.findViewById<Spinner>(R.id.spinner_state)
        val okButton = sidePanelView.findViewById<Button>(R.id.button_ok)

        var countryList = emptyList<CountryData>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.locationApiService.getCountriesAndStates()
                countryList = response.data

                withContext(Dispatchers.Main) {
                    val countryNames = countryList.map { it.name }
                    countrySpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, countryNames)
                }
            } catch (e: Exception) {
                showError(e)
            }
        }

        countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCountry = countryList[position]
                val stateNames = selectedCountry.states.map { it.name }
                stateSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, stateNames)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        okButton.setOnClickListener {
            val selectedCountry = countrySpinner.selectedItem?.toString() ?: ""
            val selectedState = stateSpinner.selectedItem?.toString() ?: ""

            targetInput.setText("$selectedCountry, $selectedState")
            container.visibility = View.GONE
        }
    }
}
