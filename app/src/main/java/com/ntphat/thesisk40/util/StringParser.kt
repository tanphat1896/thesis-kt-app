package com.ntphat.thesisk40.util

class StringParser {
    companion object {

        @JvmStatic
        fun removeVietnameseChar(string: String): String {
            return string
                    .replace("([àáạảãâầấậẩẫăằắặẳẵ])".toRegex(), "a")
                    .replace("([èéẹẻẽêềếệểễ])".toRegex(), "e")
                    .replace("([ìíịỉĩ])".toRegex(), "i")
                    .replace("([òóọỏõôồốộổỗơờớợởỡ])".toRegex(), "o")
                    .replace("([ùúụủũưừứựửữ])".toRegex(), "u")
                    .replace("([ỳýỵỷỹ])".toRegex(), "y")
                    .replace("([đ])".toRegex(), "d")
                    .replace("([àáạảãâầấậẩẫăằắặẳẵ])".toUpperCase().toRegex(), "A")
                    .replace("([èéẹẻẽêềếệểễ])".toUpperCase().toRegex(), "E")
                    .replace("([ìíịỉĩ])".toUpperCase().toRegex(), "I")
                    .replace("([òóọỏõôồốộổỗơờớợởỡ])".toUpperCase().toRegex(), "O")
                    .replace("([ùúụủũưừứựửữ])".toUpperCase().toRegex(), "U")
                    .replace("([ỳýỵỷỹ])".toUpperCase().toRegex(), "Y")
                    .replace("([đ])".toUpperCase().toRegex(), "D")
        }
    }
}