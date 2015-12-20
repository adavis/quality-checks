package info.adavis.qualitychecks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

/**
 * @author Annyce Davis on 12/20/15.
 */
class WriteConfigFileTask extends DefaultTask {

    @Input
    @Optional
    File configFile

    @Input
    @Optional
    String fileName

    WriteConfigFileTask() {
        onlyIf {
            configFile != null
        }
    }

    @TaskAction
    def writeConfigFile() {
        description 'Write config file for quality checks task'

        if (configFile != null) {
            println "copying the file contents from $fileName"
            copyConfigFile(fileName, configFile)
        }
    }

    def copyConfigFile(String fileName, File configFile) {
        this.class.classLoader.getResourceAsStream(fileName).withStream { input ->
            configFile.withOutputStream { out ->
                out << input
            }
        }
    }
}
