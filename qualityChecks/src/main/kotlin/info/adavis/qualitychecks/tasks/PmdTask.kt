package info.adavis.qualitychecks.tasks

import info.adavis.qualitychecks.QualityChecksExtension
import info.adavis.qualitychecks.QualityChecksPlugin.Companion.VERIFICATION_GROUP
import org.gradle.api.file.FileCollection
import org.gradle.api.plugins.quality.Pmd
import org.gradle.api.plugins.quality.PmdPlugin
import org.gradle.api.tasks.Input

open class PmdTask : Pmd() {

    init {
        project.plugins.apply(PmdPlugin::class.java)

        description = "Run Pmd"
        group = VERIFICATION_GROUP

        ruleSetFiles = pmdConfigFiles
        ruleSets = emptyList()
        ignoreFailures = true

        reports.xml.isEnabled = true
        reports.html.isEnabled = true

        includes.add("**/*.java")
        excludes.add("**/gen/**")
        source.add("src")
    }

    val pmdConfigFiles: FileCollection
        @Input get() {
            val extension = project?.extensions?.findByType(QualityChecksExtension::class.java)
            return project.files(extension?.pmdConfigFile)
        }

}
