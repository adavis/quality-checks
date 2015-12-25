package info.adavis.qualitychecks.tasks

import info.adavis.qualitychecks.QualityChecksPlugin
import org.gradle.api.plugins.quality.Pmd
import org.gradle.api.plugins.quality.PmdPlugin

/**
 * @author Annyce Davis
 */
class PmdTask extends Pmd {

    @SuppressWarnings("GroovyAssignabilityCheck")
    PmdTask() {
        project.plugins.apply PmdPlugin

        dependsOn(QualityChecksPlugin.WRITE_PMD_CONFIG_FILE_TASK)

        description 'Run Pmd'
        group QualityChecksPlugin.VERIFICATION_GROUP

        ruleSetFiles = project.files(project?.qualityChecks?.pmdConfigFile)
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
