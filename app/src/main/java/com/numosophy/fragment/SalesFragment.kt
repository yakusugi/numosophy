package com.numosophy.fragment

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.numosophy.R
import com.numosophy.api.ApiClient
import com.numosophy.entity.Sale
import com.numosophy.model.CountryData
import com.numosophy.model.SaleViewModel
import com.numosophy.model.StateData
import com.numosophy.utility.createSaleFromInputs
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class SalesFragment : Fragment() {

    private val saleViewModel: SaleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sales, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isTablet = resources.getBoolean(R.bool.isTablet)
        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        val titleInput = view.findViewById<TextInputEditText>(R.id.add_title)
        val amountInput = view.findViewById<TextInputEditText>(R.id.add_amount)
        val buyerNameInput = view.findViewById<TextInputEditText>(R.id.add_buyer_name)
        val buyerGenderInput = view.findViewById<TextInputEditText>(R.id.add_buyer_gender)
        val buyerBirthInput = view.findViewById<TextInputEditText>(R.id.add_buyer_age)
        val buyerLocationInput = view.findViewById<TextInputEditText>(R.id.add_buyer_location)
        val notesInput = view.findViewById<TextInputEditText>(R.id.add_notes)
        val dateInput = view.findViewById<TextInputEditText>(R.id.add_date)
        val addBtn = view.findViewById<ImageButton>(R.id.add_btn)

        dateInput?.setOnClickListener { showDatePicker(it as TextInputEditText) }
        buyerBirthInput?.setOnClickListener { showDatePicker(it as TextInputEditText) }

        val inputFields = listOf(titleInput, amountInput, buyerNameInput, dateInput)

        buyerLocationInput?.setOnClickListener {
            if (isTablet && isLandscape) {
                showLocationSidePanel(buyerLocationInput)
            } else {
                showLocationPopupDialog(buyerLocationInput)
            }
        }



        addBtn?.setOnClickListener {
            val allFilled = inputFields.all { it.text?.isNotEmpty() == true }

            if (!allFilled) {
                Toast.makeText(requireContext(), "Please fill in Title, Amount, Buyer, and Date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newSale = createSaleFromInputs(
                title = titleInput.text.toString(),
                amount = amountInput.text.toString().toDoubleOrNull() ?: 0.0,
                buyerName = buyerNameInput.text.toString(),
                buyerGender = buyerGenderInput.text.toString().ifEmpty { null },
                buyerBirthdate = buyerBirthInput.text.toString().ifEmpty { null },
                buyerLocation = buyerLocationInput.text.toString().ifEmpty { null },
                notes = notesInput.text.toString().ifEmpty { null },
                date = dateInput.text.toString(),
                currentUserPublicKey = "mockPublicKey", // TODO: replace with real value
                currentGroupId = "mockGroupId"           // TODO: replace with real value
            )

            saleViewModel.insertSale(newSale)
            Toast.makeText(requireContext(), "✅ Sale saved: ${newSale.title}", Toast.LENGTH_SHORT).show()
        }
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

    private fun showLocationPopup(targetInput: TextInputEditText) {
        val isTablet = resources.getBoolean(R.bool.isTablet)
        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val container = view?.findViewById<FrameLayout>(R.id.side_panel_container)
        container?.visibility = View.VISIBLE

        if (isTablet && isLandscape) {
            showLocationSidePanel(targetInput)
        } else {
            showLocationPopupDialog(targetInput)
        }
    }

    private fun showLocationPopupDialog(targetInput: TextInputEditText) {
        val dialogView = layoutInflater.inflate(R.layout.popup_location_selector, null)

        val countrySpinner = dialogView.findViewById<Spinner>(R.id.spinner_country)
        val stateSpinner = dialogView.findViewById<Spinner>(R.id.spinner_state)
        val okButton = dialogView.findViewById<Button>(R.id.button_ok)

        var countryList = emptyList<CountryData>()
        var stateList = emptyList<StateData>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.locationApiService.getCountriesAndStates()
                countryList = response.data

                withContext(Dispatchers.Main) {
                    val countryNames = countryList.map { it.name }
                    countrySpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, countryNames)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (countryList.isNotEmpty()) {
                    val selectedCountry = countryList[position]
                    stateList = selectedCountry.states

                    val stateNames = stateList.map { it.name }
                    stateSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, stateNames)
                }
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
        val container = view?.findViewById<FrameLayout>(R.id.side_panel_container)
        if (container == null) {
            Toast.makeText(requireContext(), "Side panel container not found", Toast.LENGTH_SHORT).show()
            return
        }

        container.visibility = View.VISIBLE
        container.removeAllViews()

        val sidePanelView = layoutInflater.inflate(R.layout.location_side_panel, container, false)
        container.addView(sidePanelView)

        val countrySpinner = sidePanelView.findViewById<Spinner>(R.id.spinner_country)
        val stateSpinner = sidePanelView.findViewById<Spinner>(R.id.spinner_state)
        val okButton = sidePanelView.findViewById<Button>(R.id.button_ok)

        var countryList = emptyList<CountryData>()
        var stateList = emptyList<StateData>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.locationApiService.getCountriesAndStates()
                countryList = response.data

                withContext(Dispatchers.Main) {
                    val countryNames = countryList.map { it.name }
                    countrySpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, countryNames)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (countryList.isNotEmpty()) {
                    val selectedCountry = countryList[position]
                    stateList = selectedCountry.states

                    val stateNames = stateList.map { it.name }
                    stateSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, stateNames)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        okButton.setOnClickListener {
            val selectedCountry = countrySpinner.selectedItem?.toString() ?: ""
            val selectedState = stateSpinner.selectedItem?.toString() ?: ""

            targetInput.setText("$selectedCountry, $selectedState")

            container.visibility = View.GONE // Hide side panel
        }
    }
}
