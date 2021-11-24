package ru.oktemsec.retrofit_24nov_abramov

class Contribution(private val login:String, private val contributions: Int) {
    override fun toString():String {
        return "$login ($contributions)"
    }
}