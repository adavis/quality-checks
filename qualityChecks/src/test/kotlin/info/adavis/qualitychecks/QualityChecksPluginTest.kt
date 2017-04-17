package info.adavis.qualitychecks

import info.adavis.qualitychecks.QualityChecksPlugin.Companion.WRITE_CHECK_STYLE_CONFIG_FILE_TASK
import info.adavis.qualitychecks.QualityChecksPlugin.Companion.WRITE_FIND_BUGS_EXCLUSION_FILE_TASK
import info.adavis.qualitychecks.QualityChecksPlugin.Companion.WRITE_PMD_CONFIG_FILE_TASK
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class QualityChecksPluginTest {

    @Rule @JvmField
    val temporaryFolder = TemporaryFolder()

    lateinit var projectDir: File
    lateinit var project: Project

    @Before
    fun setUp() {
        projectDir = temporaryFolder.root
        projectDir.mkdirs()

        project = ProjectBuilder.builder().withProjectDir(projectDir).build()
    }

    @Test
    fun `plugin should add tasks when applied`() {
        with(project) {
            pluginManager.apply(QualityChecksPlugin::class.java)

            assertNotNull(tasks.findByName(WRITE_CHECK_STYLE_CONFIG_FILE_TASK))
            assertNotNull(tasks.findByName(WRITE_FIND_BUGS_EXCLUSION_FILE_TASK))
            assertNotNull(tasks.findByName(WRITE_PMD_CONFIG_FILE_TASK))
        }
    }

    @Test
    fun `plugin should create config files directory when applied`() {
        with(project) {
            pluginManager.apply(QualityChecksPlugin::class.java)

            assertTrue(File(projectDir, "quality-checks").exists())
        }
    }

}
