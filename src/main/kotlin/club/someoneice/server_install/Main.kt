package club.someoneice.server_install

fun main(args: Array<String>) {
    LangHelper.loadLanguage("zh_CN.json")
    CoreRunner

    val command = "java -server -Xincgc -Xms${CoreRunner.config.memoryMin}M -Xmx${CoreRunner.config.memoryMax}M -Xss512K -XX:+AggressiveOpts -XX:+UseCompressedOops -XX:+UseCMSCompactAtFullCollection -XX:+UseFastAccessorMethods -XX:ParallelGCThreads=4 -XX:+UseConcMarkSweepGC -XX:CMSFullGCsBeforeCompaction=2 -XX:CMSInitiatingOccupancyFraction=70 -XX:-DisableExplicitGC -XX:TargetSurvivorRatio=90 -jar ${CoreRunner.serverFile.path} -nogui"
    Runtime.getRuntime().exec("cmd.exe /k start $command")
}