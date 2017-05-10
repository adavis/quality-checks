package info.adavis.qualitychecks.tasks

import info.adavis.qualitychecks.QualityChecksExtension
import info.adavis.qualitychecks.QualityChecksPlugin.Companion.VERIFICATION_GROUP
import org.gradle.api.plugins.quality.FindBugs
import org.gradle.api.plugins.quality.FindBugsPlugin
import org.gradle.api.tasks.Input
import java.io.File

open class FindBugsTask : FindBugs() {

    init {
        project.plugins.apply(FindBugsPlugin::class.java)

        description = "Run FindBugs"
        group = VERIFICATION_GROUP

        classes = project.files("$project.buildDir/intermediates/classes")
        classpath = project.files()
        effort = "max"
        excludeFilter = findBugsExclusionFile
        ignoreFailures = true

        reports.xml.isEnabled = true
        reports.html.isEnabled = false

        source.add("src")
    }

    val findBugsExclusionFile: File
        @Input get() {
            val extension = project?.extensions?.findByType(QualityChecksExtension::class.java)
            return project.file(extension?.findBugsExclusionFile)
        }

}
