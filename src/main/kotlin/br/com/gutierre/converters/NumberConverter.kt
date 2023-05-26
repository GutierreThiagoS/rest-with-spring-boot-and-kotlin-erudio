package br.com.gutierre.converters

import kotlin.math.pow

object NumberConverter {


    fun convertToDouble(strNumber: String?): Double {
        if (strNumber.isNullOrBlank()) return 0.0
        val number = strNumber.replace(",".toRegex(), ".")
        return if(isNumeric(number)) number.toDouble() else 0.0
    }

    fun isNumeric(strNumber: String?): Boolean {
        if (strNumber.isNullOrBlank()) return false
        val number = strNumber.replace(",".toRegex(), ".")

        return number.matches("""[-+]?[0-9]*\.?[0-9]+""".toRegex())
    }

    fun calculate(numberOne: Double, numberTwo: Double, sinal: String): Double {
        return when(sinal) {
            "+" -> numberOne + numberTwo
            "-" -> numberOne - numberTwo
            "*" -> numberOne * numberTwo
            "^" -> numberOne.pow(numberTwo)
            "**" -> numberOne.pow(numberTwo)
            "/" -> if (numberTwo > 0) numberOne/numberTwo else 0.0
            "%" -> if (numberTwo > 0) numberOne%numberTwo else 0.0
            "%p" -> numberOne * (numberTwo/100)
            "%+" -> numberOne + numberOne * (numberTwo/100)
            "%-" -> numberOne - numberOne * (numberTwo/100)
            else -> numberOne + numberTwo
        }
    }
}