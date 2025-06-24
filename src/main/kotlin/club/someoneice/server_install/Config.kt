package club.someoneice.server_install

import club.someoneice.json.JSON
import club.someoneice.json.Nodes
import club.someoneice.json.node.JsonNode
import club.someoneice.json.processor.Json5Builder
import java.io.File

class Config {
    var language = "en_US"
    var memoryMin = 512
    var memoryMax = 2048

    var shouldBuildBasicConfig = false

    val file = File("./ServerConfig.json5")

    init {
        val flag = !file.exists() || !file.isFile
        this.shouldBuildBasicConfig = flag
        read()

        val feedback = LangHelper.transformText("lang.check.config.${if (flag) "fail" else "success"}")
        println(feedback)
    }

    private fun read() {
        if (shouldBuildBasicConfig) {
            return
        }

        val node = JSON.json5.parse(file).asMapNodeOrEmpty()
        println(node.obj.toString())
        language = node["Language"].toString()
        memoryMin = node["MemoryMin"].asTypeOrNull(JsonNode.NodeType.Int).obj as Int? ?: 512
        memoryMax = node["MemoryMax"].asTypeOrNull(JsonNode.NodeType.Int).obj as Int? ?: 2048
    }

    fun writeToConfig() {
        this.file.createNewFile()
        val builder = Json5Builder()
        builder.createObjectBean()
            .addNote("语言文件名称。")
            .put("Language", Nodes.`as`(this.language))
            .addNote("最小与最大内存。")
            .put("MemoryMin", Nodes.`as`(this.memoryMin))
            .put("MemoryMax", Nodes.`as`(this.memoryMax))
        val dat = builder.build()
        this.file.writeText(dat)
    }
}