package info.adavis.qualitychecks.tasks

import info.adavis.qualitychecks.QualityChecksPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

/**
 * @author Annyce Davis on 12/20/15.
 */
@SuppressWarnings("GroovyAssignabilityCheck")
class WriteConfigFileTask extends DefaultTask {

    final Logger log = Logging.getLogger WriteConfigFileTask

    @Input
    @Optional
    File configFile

    @Input
    @Optional
    String fileName

    WriteConfigFileTask() {
        group QualityChecksPlugin.VERIFICATION_GROUP

        onlyIf {
            configFile != null
        }
    }

    @SuppressWarnings("GroovyUnusedDeclaration")
    @TaskAction
    def writeConfigFile() {
        description 'Write config file for quality checks task'

        if (configFile != null) {
            log.info "Copying the file contents from $fileName"
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
