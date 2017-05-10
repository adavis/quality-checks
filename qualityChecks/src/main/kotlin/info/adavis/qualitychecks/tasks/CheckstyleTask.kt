package info.adavis.qualitychecks.tasks

import info.adavis.qualitychecks.QualityChecksExtension
import info.adavis.qualitychecks.QualityChecksPlugin.Companion.VERIFICATION_GROUP
import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.plugins.quality.CheckstylePlugin
import org.gradle.api.tasks.Input
import java.io.File

open class CheckstyleTask : Checkstyle() {

    init {
        project.plugins.apply(CheckstylePlugin::class.java)

        description = "Run Checkstyle"
        group = VERIFICATION_GROUP

        configFile = checkstyleConfigFile
        classpath = project.files()
        ignoreFailures = false
        includes.add("**/*.java")
        excludes.add("**/gen/**")
        source.add("src")
    }

    val checkstyleConfigFile: File
        @Input get() {
            val extension = project?.extensions?.findByType(QualityChecksExtension::class.java)
            return project.file(extension?.checkstyleConfigFile)
        }

}
