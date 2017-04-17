package info.adavis.qualitychecks.tasks

import info.adavis.qualitychecks.QualityChecksExtension
import info.adavis.qualitychecks.QualityChecksPlugin
import info.adavis.qualitychecks.QualityChecksPlugin.Companion.PLUGIN_EXTENSION_NAME
import info.adavis.qualitychecks.QualityChecksPlugin.Companion.VERIFICATION_GROUP
import org.gradle.api.file.FileCollection
import org.gradle.api.plugins.quality.Pmd
import org.gradle.api.plugins.quality.PmdPlugin

open class PmdTask : Pmd() {

    override fun getDependsOn(): MutableSet<Any> = mutableSetOf(QualityChecksPlugin.WRITE_PMD_CONFIG_FILE_TASK)

    init {
        project.plugins.apply(PmdPlugin::class.java)

        description = "Run Pmd"
        group = VERIFICATION_GROUP

        ruleSetFiles = getPmdConfigFiles()
        ruleSets = emptyList()
        ignoreFailures = true

        reports.xml.isEnabled = true
        reports.html.isEnabled = true

        includes.add("**/*.java")
        excludes.add("**/gen/**")
        source.add("src")
    }

    private fun getPmdConfigFiles(): FileCollection {
        val extension = project?.extensions?.findByType(QualityChecksExtension::class.java)
        return project.files(extension?.pmdConfigFile)
    }

}
