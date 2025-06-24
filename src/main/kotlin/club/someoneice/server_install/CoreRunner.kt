package club.someoneice.server_install

import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import kotlin.system.exitProcess

object CoreRunner {
    val serverFile = File("./Server.jar" )
    val config: Config = Config()

    init {
        if (config.shouldBuildBasicConfig) {
            initInstallerLanguage()

            do {
                var userInput = ""
                initBasicMemory()
                if (this.config.memoryMin > this.config.memoryMax) {
                    println(LangHelper.transformText("lang.check.mem.fail"))
                    continue
                }

                println(LangHelper.transformText("lang.check.mem").format(this.config.memoryMin, this.config.memoryMax))
                print("> ")
                userInput = readlnOrNull()?.lowercase() ?: "y"
            } while (userInput.isNotEmpty() && userInput != "y" && userInput != "yes")

            config.writeToConfig()
        } else {
            LangHelper.loadLanguage(this.config.language + ".json")
        }

        checkJAR()
        checkEULA()
        outputProperties()
        createCmd()

        println(LangHelper.transformText("lang.start.jar"))
        print("> ")
        var userInput = readlnOrNull()
        if (userInput.isNullOrEmpty()) {
            userInput = "y"
        }

        if (userInput != "y" && userInput != "yes") {
            exitProcess(0)
        }
    }

    private fun initInstallerLanguage() {
        println("[0] English [Unfinish]")
        println("[1] 简体中文")
        println("[2] 繁體中文")

        var userLang: Int
        while (true) {
            try {
                print("> ")
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

        LangHelper.loadLanguage(lang)
        this.config.language = lang.replace(".json", "")
    }

    private fun initBasicMemory() {
        println(LangHelper.transformText("lang.docs.mem.min"))
        println(LangHelper.transformText("lang.check.mem.min"))

        var userLang: String?

        while (true) {
            try {
                print("> ")
                userLang = readlnOrNull()
                if (!userLang.isNullOrEmpty()) {
                    this.config.memoryMin = userLang.toInt()
                }
                break
            } catch (_: NumberFormatException) {
                println("Please key the number!")
            }
        }

        println(LangHelper.transformText("lang.docs.mem.max"))
        println(LangHelper.transformText("lang.check.mem.max"))

        while (true) {
            try {
                print("> ")
                userLang = readlnOrNull()
                if (!userLang.isNullOrEmpty()) {
                    this.config.memoryMax = userLang.toInt()
                }
                break
            } catch (_: NumberFormatException) {
                println("Please key the number!")
            }
        }
    }

    private fun checkJAR() {
        println(LangHelper.transformText("lang.check.jar"))
        if (serverFile.exists()) {
            println(LangHelper.transformText("lang.check.jar.success"))
            return
        }

        println(LangHelper.transformText("lang.check.jar.fail"))
        print("> ")
        var userInput = readlnOrNull()
        if (userInput.isNullOrEmpty()) {
            userInput = "y"
        }

        if (userInput != "y" && userInput != "yes") {
            exitProcess(0)
        }

        downloadJAR()
    }

    private fun downloadJAR() {
        val head = "https://gh-proxy.net/"
        val serverCoreUrl = "https://github.com/CyberdyneCC/Thermos/releases/download/58/Thermos-1.7.10-1614-server.jar"
        val serverLibraryUrl = "https://github.com/CyberdyneCC/Thermos/releases/download/58/libraries.zip"

        downLoadFromUrl(URL(head + serverCoreUrl), serverFile)
        downLoadFromUrl(URL(head + serverLibraryUrl), File("./libraries.zip"))

        val flag = !ZIPUtil.zipUtil("ThermosOfficial")

        println(LangHelper.transformText(if (flag) "lang.download.fail" else "lang.download.success"))
        if (flag) {
            readlnOrNull()
            exitProcess(0)
        }
    }

/*
    private fun downloadJAR() {
        println(LangHelper.transformText("lang.downloadJar"))
        println("[0] Forge Server")
        println("[1] Thermos Server")
        println("[2] No thanks.")

        var choseJar: Int
        while (true) {
            try {
                choseJar = readln().toInt()
                if(choseJar > -1 && choseJar < 3) break
                else println("Please key the number in 0 - 2 !")
            } catch (_: NumberFormatException) {
                println("Please key the number!")
            }
        }

        when (choseJar) {
            0 -> {
                println(LangHelper.transformText("lang.server.downloadWay"))
                println("[0] ${LangHelper.transformText("lang.forgeJar.official")}")
                println("[1] ${LangHelper.transformText("lang.forgeJar.other")}")
                var chose: String?
                while (true) {
                    chose = readLine()
                    if (chose == "0" || chose == "1") break
                }
                if (chose == "0") {
                    println(LangHelper.transformText("lang.forgeJar"))
                    isForgeServer = true

                    downLoadFromUrl(URL("https://piston-data.mojang.com/v1/objects/952438ac4e01b4d115c5fc38f891710c4941df29/server.jar"), File(System.getProperty("user.dir"), "ValliServer.jar"))
                    downLoadFromUrl(URL("https://maven.minecraftforge.net/net/minecraftforge/forge/1.7.10-10.13.4.1614-1.7.10/forge-1.7.10-10.13.4.1614-1.7.10-installer.jar"), File(System.getProperty("user.dir"), "ForgeInstall.jar"))
                } else {
                    downLoadFromUrl(URL("https://gitcode.net/qq_36258771/serverinstall/-/raw/master/ForgeServer.zip?inline=false"), File(System.getProperty("user.dir"), "ForgeServer.zip"))
                    if (!ZIPUtil.zipUtil("Forge")) {
                        println("Success!")
                    } else {
                        println(LangHelper.transformText("lang.forgeJar.fail"))
                    }
                }

            }
            1 -> {
                println(LangHelper.transformText("lang.server.downloadWay"))
                println("[0] ${LangHelper.transformText("lang.thermos.official")}")
                println("[1] ${LangHelper.transformText("lang.thermos.other")}")

                var chose: String?
                while (true) {
                    chose = readLine()
                    if (chose == "0" || chose == "1") break
                }

                if (chose == "0") {
                    downLoadFromUrl(URL("https://github.com/CyberdyneCC/Thermos/releases/download/58/Thermos-1.7.10-1614-server.jar"), serverFile)
                    downLoadFromUrl(URL("https://github.com/CyberdyneCC/Thermos/releases/download/58/libraries.zip"), File(System.getProperty("user.dir"), "libraries.zip"))
                    if (!ZIPUtil.zipUtil("ThermosOfficial")) println(LangHelper.transformText("lang.thermos.fail"))
                    else println("Success!")
                } else {
                    downLoadFromUrl(URL("https://gitcode.net/qq_36258771/serverinstall/-/raw/master/ThermosServer.zip"), File(System.getProperty("user.dir"), "ThermosServer.zip"))
                    if (!ZIPUtil.zipUtil("Thermos")) println(LangHelper.transformText("lang.thermos.fail"))
                    else println("Success!")
                }
            }

            2 -> return
        }


    }
*/

    private fun downLoadFromUrl(url: URL, file: File) {
        try {
            val connect = url.openConnection() as HttpURLConnection
            connect.connectTimeout = 3 * 1000
            connect.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)")

            val input = connect.inputStream
            val getData = readInputStream(input)

            val fos = FileOutputStream(file)
            fos.write(getData)

            fos.close()
            input.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun readInputStream(inputStream: InputStream?): ByteArray {
        val buffer = ByteArray(1024)
        var len: Int
        val bos = ByteArrayOutputStream()
        if (inputStream != null) {
            while (inputStream.read(buffer).also { len = it } != -1) {
                bos.write(buffer, 0, len)
            }
        }
        bos.close()
        return bos.toByteArray()
    }

    private fun checkEULA() {
        val file = File(System.getProperty("user.dir"), "eula.txt")
        if (file.exists() && file.isFile) {
            return
        }

        println(LangHelper.transformText("lang.check.eula"))
        val allowEULA = readlnOrNull()?.lowercase() ?: "y"

        if (allowEULA == "n" || allowEULA == "no") {
            exitProcess(1)
        }

        file.createNewFile()
        file.bufferedWriter().use {
            it.write("eula=true")
        }
    }

    private fun outputProperties() {
        val file = File("./server.properties")
        if (file.exists() && file.isFile) {
            return
        }

        file.createNewFile()
        try {
            this.javaClass.getResourceAsStream("/assets/file/server.properties")!!.copyTo(file.outputStream())
            println(LangHelper.transformText("lang.check.properties.success"))
        } catch (_: IOException) {
            println(LangHelper.transformText("lang.check.properties.fail"))
        }
    }

    private fun createCmd() {
        val cmd = File("./StartServer.cmd")
        if (cmd.exists() && cmd.isFile) {
            return
        }

        cmd.createNewFile()
        val command = "java -server -Xincgc -Xms${config.memoryMin}M -Xmx${config.memoryMax}M " +
                "-Xss512K -XX:+AggressiveOpts -XX:+UseCompressedOops -XX:+UseCMSCompactAtFullCollection " +
                "-XX:+UseFastAccessorMethods -XX:ParallelGCThreads=4 -XX:+UseConcMarkSweepGC " +
                "-XX:CMSFullGCsBeforeCompaction=2 -XX:CMSInitiatingOccupancyFraction=70 -XX:-DisableExplicitGC " +
                "-XX:TargetSurvivorRatio=90 -jar ${this.serverFile.path} -nogui"
        cmd.writeText(command)
    }
}