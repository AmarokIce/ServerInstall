package club.someoneice.server_install

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.IOException

class LangHelper {
    companion object {
        var LANG_LIST: Map<String, String> = HashMap()

        fun transformText(str: String): String {
            return if (LANG_LIST.containsKey(str)) LANG_LIST[str]!! else str
        }
    }

    init {
        println("Which lang would you like? (Key the number)")
        println("[0] English")
        println("[1] 简体中文")
        println("[2] 繁體中文")

        var userLang: Int
        while (true) {
            try {
                userLang = readln().toInt()
                if(userLang > -1 && userLang < 3) break
                else println("Please key the number in 0 - 3 !")
            } catch (_: NumberFormatException) {
                println("Please key the number!")
            }
        }

        val lang: String = when (userLang) {
            0 -> "en_US.json"
            1 -> "zh_CN.json"
            2 -> "zh_TW.json"
            else -> throw IOException()
        }

        val str = StringBuffer()
        val gson: Gson = GsonBuilder().create()
        val langFile = this.javaClass.getResourceAsStream("/assets/lang/${lang}")!!
        langFile.bufferedReader().use { out ->
            var text: String?

            while (true) {
                text = out.readLine()
                if (text == null) break
                else str.append(text)
            }

            LANG_LIST = gson.fromJson(str.toString(), object : TypeToken<Map<String, String>>() {}.type)

        }
    }
}