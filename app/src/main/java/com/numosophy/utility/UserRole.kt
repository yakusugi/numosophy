package com.numosophy.utility

enum class UserRole(val label: String) {
    ADMIN("Admin"),
    MANAGER("Manager"),
    CONTRIBUTOR("Contributor");

    override fun toString(): String = label
}