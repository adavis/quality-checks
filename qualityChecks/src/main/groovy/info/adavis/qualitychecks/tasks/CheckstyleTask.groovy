package info.adavis.qualitychecks.tasks

import info.adavis.qualitychecks.QualityChecksPlugin
import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.plugins.quality.CheckstylePlugin

/**
 * @author Annyce Davis
 */
class CheckstyleTask extends Checkstyle {

    @SuppressWarnings("GroovyAssignabilityCheck")
    CheckstyleTask() {
        project.plugins.apply CheckstylePlugin

        dependsOn(QualityChecksPlugin.WRITE_CHECK_STYLE_CONFIG_FILE_TASK)

        description 'Run Checkstyle'
        group QualityChecksPlugin.VERIFICATION_GROUP

        configFile project.file(project?.qualityChecks?.checkstyleConfigFile)
        source 'src'
        include '**/*.java'
        exclude '**/gen/**'

        classpath = project.files()

        ignoreFailures = false
    }

}
