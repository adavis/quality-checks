package info.adavis.qualitychecks

import info.adavis.qualitychecks.tasks.CheckstyleTask
import info.adavis.qualitychecks.tasks.FindBugsTask
import info.adavis.qualitychecks.tasks.PmdTask
import info.adavis.qualitychecks.tasks.WriteConfigFileTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import java.io.File

class QualityChecksPlugin : Plugin<Project> {

    companion object {
        const val PMD_FILE_NAME = "pmd-ruleset.xml"
        const val CHECKSTYLE_FILE_NAME = "checkstyle.xml"
        const val FINDBUGS_FILE_NAME = "findbugs-exclude.xml"
        const val WRITE_PMD_CONFIG_FILE_TASK = "writePmdConfigFile"
        const val WRITE_CHECK_STYLE_CONFIG_FILE_TASK = "writeCheckStyleConfigFile"
        const val WRITE_FIND_BUGS_EXCLUSION_FILE_TASK = "writeFindBugsExclusionFile"
        const val VERIFICATION_GROUP = "verification"
        const val PLUGIN_EXTENSION_NAME = "qualityChecks"
    }

    var project: Project? = null
    var pmdConfigFile: File? = null
    var checkStyleConfigFile: File? = null
    var findBugsExclusionFile: File? = null

    override fun apply(target: Project?) {
        project = target
        project?.extensions?.create(PLUGIN_EXTENSION_NAME, QualityChecksExtension::class.java)

        createConfigFilesIfNeeded()
        createConfigFileTasks()
        createQualityChecksTasks()
    }

    private fun createConfigFilesIfNeeded() {
        pmdConfigFile = createConfigFile(PMD_FILE_NAME)
        checkStyleConfigFile = createConfigFile(CHECKSTYLE_FILE_NAME)
        findBugsExclusionFile = createConfigFile(FINDBUGS_FILE_NAME)
    }

    private fun createConfigFile(fileName: String): File? {
        val qualityChecksDir = File(project?.buildFile?.parentFile, "quality-checks")
        if (!qualityChecksDir.exists()) {
            qualityChecksDir.mkdirs()
        }

        return File(qualityChecksDir, fileName).apply { createNewFile() }
    }

    private fun createConfigFileTasks() {
        project?.logger?.log(LogLevel.INFO, "creating the write config files tasks")

        project?.tasks?.let {
            it.create(WRITE_PMD_CONFIG_FILE_TASK, WriteConfigFileTask::class.java).apply {
                configFile = pmdConfigFile
                fileName = PMD_FILE_NAME
            }

            it.create(WRITE_CHECK_STYLE_CONFIG_FILE_TASK, WriteConfigFileTask::class.java).apply {
                configFile = checkStyleConfigFile
                fileName = CHECKSTYLE_FILE_NAME
            }

            it.create(WRITE_FIND_BUGS_EXCLUSION_FILE_TASK, WriteConfigFileTask::class.java).apply {
                configFile = findBugsExclusionFile
                fileName = FINDBUGS_FILE_NAME
            }
        }
    }

    private fun createQualityChecksTasks() {
        project?.logger?.log(LogLevel.INFO, "creating the quality checks tasks")

        project?.tasks?.let {
            it.create("pmd", PmdTask::class.java)
            it.create("findbugs", FindBugsTask::class.java)
            it.create("checkstyle", CheckstyleTask::class.java)
        }
    }

}