package tech.dalapenko.shapeablebug

import org.junit.runner.Description
import java.io.File

object FileProvider {

    private val testMap = mutableMapOf<String, Int>()

    fun get(
        description: Description,
        directory: File,
        fileExtension: String
    ): File {
        val clazzWithoutPackage = description.className.substringAfterLast(".")
        val rootDir = File(directory.absolutePath, clazzWithoutPackage).apply(File::mkdirs)

        return File(rootDir, "${generateCountableOutputNameWithDescription(description.methodName)}.$fileExtension")
    }

    private fun generateCountableOutputNameWithDescription(methodName: String): String {
        return countableOutputName(methodName)
    }

    private fun countableOutputName(testName: String): String {
        val count = testMap.getOrPut(testName) { 1 }
        testMap[testName] = count + 1
        return if (count == 1) {
            testName
        } else {
            testName + "_$count"
        }
    }
}