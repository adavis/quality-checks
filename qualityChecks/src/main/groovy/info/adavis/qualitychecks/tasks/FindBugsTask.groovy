package info.adavis.qualitychecks.tasks

import info.adavis.qualitychecks.QualityChecksPlugin
import org.gradle.api.plugins.quality.FindBugs
import org.gradle.api.plugins.quality.FindBugsPlugin

/**
 * @author Annyce Davis
 */
class FindBugsTask extends FindBugs {

    @SuppressWarnings("GroovyAssignabilityCheck")
    FindBugsTask() {
        project.plugins.apply FindBugsPlugin

        dependsOn(QualityChecksPlugin.WRITE_FIND_BUGS_EXCLUSION_FILE_TASK)

        description 'Run FindBugs'
        group QualityChecksPlugin.VERIFICATION_GROUP

        classes = project.files("$project.buildDir/intermediates/classes")
        source 'src'
        classpath = project.files()

        effort 'max'
        excludeFilter project.file(project?.qualityChecks?.findBugsExclusionFile)

        reports {
            xml.enabled = true
            html.enabled = false
        }

        ignoreFailures = true
    }

}
