package com.numosophy.utility

enum class GenderSelection(val label: String) {
    MALE("Male"),
    FEMALE("Female"),
    NON_BINARY("Non-binary"),
    PREFER_NOT_TO_SAY("Prefer not to say");

    override fun toString(): String = label
}