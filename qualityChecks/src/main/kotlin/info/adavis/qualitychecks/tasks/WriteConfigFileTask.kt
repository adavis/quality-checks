package info.adavis.qualitychecks.tasks

import info.adavis.qualitychecks.QualityChecksPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

open class WriteConfigFileTask : DefaultTask() {

    @Input
    @Optional
    var configFile: File? = null

    @Input
    @Optional
    var fileName: String? = null

    init {
        group = QualityChecksPlugin.VERIFICATION_GROUP

        onlyIf {
            configFile != null
        }
    }

    @TaskAction
    fun writeConfigFile() {
        description = "Write config file for quality checks task"

        configFile?.let {
            project.logger.info("Copying the file contents from $fileName")
            copyConfigFile(fileName, configFile)
        }
    }

    private fun copyConfigFile(fileName: String?, configFile: File?) {
        this::class.java.classLoader.getResourceAsStream(fileName).use { inputStream ->
            configFile?.outputStream()?.use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }
}
