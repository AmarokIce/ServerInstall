package club.someoneice.server_install

import club.someoneice.json.JSON
import club.someoneice.json.node.MapNode

object LangHelper {
    var LANG_LIST = MapNode()

    fun transformText(str: String): String {
        return if (LANG_LIST.has(str)) LANG_LIST[str]!!.toString() else str
    }

    fun loadLanguage(lang: String) {
        val str = StringBuffer()
        val langFile = this.javaClass.getResourceAsStream("/assets/lang/${lang}")!!
        langFile.bufferedReader().use {
            str.append(it.readText())
        }
        LANG_LIST = JSON.json.parse(str.toString()).asMapNodeOrEmpty()
    }
}