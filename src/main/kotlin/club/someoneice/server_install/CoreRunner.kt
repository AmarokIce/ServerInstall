package club.someoneice.server_install

import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class CoreRunner {
    private val ServerFile = File(System.getProperty("user.dir"), "Server.jar" )
    private var isForgeServer: Boolean = false
    init {
        // Read Lang.
        LangHelper()

        // Start.
        checkJAR()
        outputBAT()
        outputProperties()
        if (isForgeServer) println(LangHelper.transformText("lang.forgeJar.installFinish"))
    }

    private fun checkJAR() {
        println(LangHelper.transformText("lang.checkJar"))
        if (ServerFile.exists()) {
            println(LangHelper.transformText("lang.checkJar.success"))
            return
        }

        println(LangHelper.transformText("lang.checkJar.install"))

        try {
            downloadJAR()
        } catch (_: Exception) {
            println(LangHelper.transformText("lang.checkJar.fail"))
        }
    }

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
                    downLoadFromUrl(URL("https://github.com/CyberdyneCC/Thermos/releases/download/58/Thermos-1.7.10-1614-server.jar"), ServerFile)
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

    private fun outputBAT() {
        println(LangHelper.transformText("lang.allowEULA"))
        val allowEULA = readLine()?.lowercase()

        if (allowEULA == "n" || allowEULA == "no") {
            return
        }

        val fileEULA = File(System.getProperty("user.dir"), "eula.txt")
        fileEULA.bufferedWriter().use {
            it.write("eula=true")
        }

        val fileStartServerBAT = File(System.getProperty("user.dir"), "StartServer.bat")
        fileStartServerBAT.bufferedWriter().use {
            it.write("java -server -Xmx 2G -jar Server.jar nogui")
        }

        // TODO - Rename helper

    }

    private fun outputProperties() {
        try {
            this.javaClass.getResourceAsStream("/assets/file/server.properties")!!.copyTo(File("${System.getProperty("user.dir")}/server.properties").outputStream())
            println(LangHelper.transformText("lang.cropProperties.success"))
        } catch (_: IOException) {
            println(LangHelper.transformText("lang.cropProperties.fail"))
        }
    }
}