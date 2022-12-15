package club.someoneice.server_install

import java.io.File

fun main(args: Array<String>) {
    println("Now start for readly to install server jar ......!")
    CoreRunner()

    // Finish Work
    println(LangHelper.transformText("lang.finishInstall"))
    val makeChose = readln().lowercase()
    if (makeChose == "no" || makeChose == "n") File("${System.getProperty("user.dir")}/InstallServer.bat").delete()
}