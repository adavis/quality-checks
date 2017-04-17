package info.adavis.qualitychecks.tasks

import info.adavis.qualitychecks.QualityChecksExtension
import info.adavis.qualitychecks.QualityChecksPlugin
import info.adavis.qualitychecks.QualityChecksPlugin.Companion.PLUGIN_EXTENSION_NAME
import info.adavis.qualitychecks.QualityChecksPlugin.Companion.VERIFICATION_GROUP
import org.gradle.api.plugins.quality.FindBugs
import org.gradle.api.plugins.quality.FindBugsPlugin
import java.io.File

open class FindBugsTask : FindBugs() {

    override fun getDependsOn(): MutableSet<Any> = mutableSetOf(QualityChecksPlugin.WRITE_FIND_BUGS_EXCLUSION_FILE_TASK)

    init {
        project.plugins.apply(FindBugsPlugin::class.java)

        description = "Run FindBugs"
        group = VERIFICATION_GROUP

        classes = project.files("$project.buildDir/intermediates/classes")
        classpath = project.files()
        effort = "max"
        excludeFilter = getFindBugsExclusionFile()
        ignoreFailures = true

        reports.xml.isEnabled = true
        reports.html.isEnabled = false

        source.add("src")
    }

    private fun getFindBugsExclusionFile(): File {
        val extension = project?.extensions?.findByType(QualityChecksExtension::class.java)
        return project.file(extension?.findBugsExclusionFile)
    }

}
