package ru.oktemsec.app3

import com.google.gson.annotations.SerializedName

class Repos(@SerializedName("name") val name: String) {
    override fun toString(): String {
        return name
    }
}
