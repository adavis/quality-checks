package info.adavis.qualitychecks

import info.adavis.qualitychecks.tasks.CheckstyleTask
import info.adavis.qualitychecks.tasks.FindBugsTask
import info.adavis.qualitychecks.tasks.PmdTask
import info.adavis.qualitychecks.tasks.WriteConfigFileTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
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

    lateinit var project: Project

    var pmdConfigFile: File? = null
    var checkStyleConfigFile: File? = null
    var findBugsExclusionFile: File? = null

    override fun apply(target: Project?) {
        target?.let {
            project = target
            if (project.plugins?.withType(ApplicationPlugin::class.java)?.isNotEmpty()!!) {
                project.extensions?.create(PLUGIN_EXTENSION_NAME, QualityChecksExtension::class.java)

                createConfigFilesIfNeeded()
                createConfigFileTasks()
                createQualityChecksTasks()
            }
            else {
                throw GradleException("You must apply the Android plugin before using this plugin.")
            }
        }
    }

    private fun createConfigFilesIfNeeded() {
        val qualityChecksDir = File(project.buildFile?.parentFile, "quality-checks")
        if (!qualityChecksDir.exists()) {
            qualityChecksDir.mkdirs()

            project.logger.info("created the `quality-checks` directory")
        }

        pmdConfigFile = createConfigFile(qualityChecksDir, PMD_FILE_NAME)
        checkStyleConfigFile = createConfigFile(qualityChecksDir, CHECKSTYLE_FILE_NAME)
        findBugsExclusionFile = createConfigFile(qualityChecksDir, FINDBUGS_FILE_NAME)
    }

    private fun createConfigFile(qualityChecksDir:File, fileName: String): File? {
        return File(qualityChecksDir, fileName).apply {
            createNewFile()
            WriteConfigFileTask.copyConfigFile(fileName, this)

            project.logger.info("created the $fileName file with default content")
        }
    }

    private fun createConfigFileTasks() {
        project.tasks?.let {
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
        project.tasks?.let {
            it.create("pmd", PmdTask::class.java)
            it.create("findbugs", FindBugsTask::class.java)
            it.create("checkstyle", CheckstyleTask::class.java)

            project.logger.info("created the static code analysis tasks")
        }
    }

}