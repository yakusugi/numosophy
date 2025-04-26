package com.numosophy.utility

import com.numosophy.entity.Sale

fun createSaleFromInputs(
    title: String,
    amount: Double,
    buyerName: String,
    buyerGender: String?,
    buyerBirthdate: String?,
    buyerLocation: String?,
    notes: String?,
    date: String,
    currentUserPublicKey: String,
    currentGroupId: String
): Sale {
    return Sale(
        title = title,
        amount = amount,
        buyerName = buyerName,
        buyerGender = buyerGender,
        buyerBirthdate = buyerBirthdate,
        buyerLocation = buyerLocation,
        notes = notes,
        date = date,
        createdBy = currentUserPublicKey,
        groupId = currentGroupId
    )
}
