package com.numosophy.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.numosophy.R
import com.numosophy.entity.Sale
import com.google.android.material.textfield.TextInputEditText
import com.numosophy.model.SaleViewModel
import com.numosophy.utility.createSaleFromInputs
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

        val titleInput = view.findViewById<AutoCompleteTextView>(R.id.add_title)
        val amountInput = view.findViewById<TextInputEditText>(R.id.add_amount)
        val buyerNameInput = view.findViewById<TextInputEditText>(R.id.add_buyer_name)
        val buyerGenderInput = view.findViewById<TextInputEditText>(R.id.add_buyer_gender)
        val buyerBirthInput = view.findViewById<TextInputEditText>(R.id.add_buyer_age)
        val buyerLocationInput = view.findViewById<TextInputEditText>(R.id.add_buyer_location)
        val notesInput = view.findViewById<TextInputEditText>(R.id.add_notes)
        val dateInput = view.findViewById<TextInputEditText>(R.id.add_date)
        val addBtn = view.findViewById<ImageButton>(R.id.add_btn)

        dateInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, y, m, d ->
                // Format date: you can customize this
                val selectedDate = String.format("%04d-%02d-%02d", y, m + 1, d)
                dateInput.setText(selectedDate)
            }, year, month, day)

            datePicker.show()
        }

        buyerBirthInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, y, m, d ->
                val birthdate = String.format("%04d-%02d-%02d", y, m + 1, d)
                buyerBirthInput.setText(birthdate)
            }, year, month, day)

            datePicker.show()
        }

        val inputFields = listOf(
            titleInput,
            amountInput,
            buyerNameInput,
            dateInput  // ✅ These are REQUIRED
        )

        addBtn.setOnClickListener {
            val allFilled = inputFields.all { it.text?.isNotEmpty() == true }

            if (!allFilled) {
                Toast.makeText(requireContext(), "Please fill in Title, Amount, Buyer, and Date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 🎯 Collect all fields
            val title = titleInput.text.toString()
            val amount = amountInput.text.toString().toDoubleOrNull() ?: 0.0
            val buyerName = buyerNameInput.text.toString()
            val buyerGender = buyerGenderInput.text.toString().ifEmpty { null }
            val buyerBirthdate = buyerBirthInput.text.toString().ifEmpty { null }
            val buyerLocation = buyerLocationInput.text.toString().ifEmpty { null }
            val notes = notesInput.text.toString().ifEmpty { null }
            val date = dateInput.text.toString()

            // 🎯 Replace these with real session values in your app
            val currentUserPublicKey = "mockPublicKey"
            val currentGroupId = "mockGroupId"

            val newSale = createSaleFromInputs(
                title = title,
                amount = amount,
                buyerName = buyerName,
                buyerGender = buyerGender,
                buyerBirthdate = buyerBirthdate,
                buyerLocation = buyerLocation,
                notes = notes,
                date = date,
                currentUserPublicKey = currentUserPublicKey,
                currentGroupId = currentGroupId
            )

            saleViewModel.insertSale(newSale)
            Toast.makeText(requireContext(), "✅ Sale saved: ${newSale.title}", Toast.LENGTH_SHORT).show()
        }
    }
}
