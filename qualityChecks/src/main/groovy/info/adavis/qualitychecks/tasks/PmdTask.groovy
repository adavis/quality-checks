package info.adavis.qualitychecks.tasks

import info.adavis.qualitychecks.QualityChecksPlugin
import org.gradle.api.file.FileCollection
import org.gradle.api.plugins.quality.Pmd
import org.gradle.api.plugins.quality.PmdPlugin

/**
 * @author Annyce Davis
 */
class PmdTask extends Pmd {

    FileCollection getPmdConfigFiles() {
        project.files(project?.qualityChecks?.pmdConfigFile)
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    PmdTask() {
        project.plugins.apply PmdPlugin

        dependsOn(QualityChecksPlugin.WRITE_PMD_CONFIG_FILE_TASK)

        description 'Run Pmd'
        group QualityChecksPlugin.VERIFICATION_GROUP

        ruleSetFiles = getPmdConfigFiles()
        ruleSets = []

        source 'src'
        include '**/*.java'
        exclude '**/gen/**'

        reports {
            xml.enabled = true
            html.enabled = false
        }

        ignoreFailures = true
    }

}
