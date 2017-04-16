package info.adavis.qualitychecks.tasks

import info.adavis.qualitychecks.QualityChecksExtension
import info.adavis.qualitychecks.QualityChecksPlugin.Companion.PLUGIN_EXTENSION_NAME
import info.adavis.qualitychecks.QualityChecksPlugin.Companion.VERIFICATION_GROUP
import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.plugins.quality.CheckstylePlugin
import java.io.File

open class CheckstyleTask : Checkstyle()
{
    init
    {
        project.plugins.apply(CheckstylePlugin::class.java)

        description = "Run Checkstyle"
        group = VERIFICATION_GROUP

        configFile = getCheckstyleConfigFile()
        classpath = project.files()
        ignoreFailures = false
        includes.add("**/*.java")
        excludes.add("**/gen/**")
        source.add("src")
    }

    private fun getCheckstyleConfigFile() : File
    {
        val extension = project?.extensions?.findByType(QualityChecksExtension::class.java)
        return project.file(extension?.checkstyleConfigFile)
    }

}
