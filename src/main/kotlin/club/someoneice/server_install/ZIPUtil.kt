package club.someoneice.server_install

import java.io.*
import java.nio.charset.Charset
import java.util.zip.ZipInputStream

object ZIPUtil {
    fun zipUtil(command: String): Boolean {
        val zipPath: File = when (command) {
            "Thermos" -> File(System.getProperty("user.dir"), "ThermosServer.zip")
            "ThermosOfficial" -> File(System.getProperty("user.dir"), "libraries.zip")
            "Forge" -> File(System.getProperty("user.dir"), "ForgeServer.zip")
            else -> throw IOException()
        }

        if (!zipPath.exists()) return false

        val zipInputStream = ZipInputStream(FileInputStream(zipPath), Charset.forName("GBK"))

        while (true) {
            val zipEntry = zipInputStream.nextEntry ?: return true
            val unzipFilePath = "${System.getProperty("user.dir")}\\${zipEntry.name}"
            if (zipEntry.isDirectory) {
                File(unzipFilePath).parentFile.mkdirs()
            } else {
                val file = File(unzipFilePath)
                file.parentFile.mkdirs()
                val bufferedOutputStream = BufferedOutputStream(FileOutputStream(unzipFilePath))
                val bytes = ByteArray(1024)
                var readLen: Int
                while (zipInputStream.read(bytes).also { readLen = it } > 0) {
                    bufferedOutputStream.write(bytes, 0, readLen)
                }
                bufferedOutputStream.close()
            }
            zipInputStream.closeEntry()
        }
    }
}