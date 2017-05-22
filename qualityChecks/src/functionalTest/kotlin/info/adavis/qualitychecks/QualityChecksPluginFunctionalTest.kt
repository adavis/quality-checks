package info.adavis.qualitychecks

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.UnexpectedBuildFailure
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class QualityChecksPluginFunctionalTest {

    @get:Rule val testProjectDir: TemporaryFolder = TemporaryFolder()

    lateinit var buildFile: File

    @Before
    fun setup() {
        buildFile = testProjectDir.newFile("build.gradle")
    }

    @Test(expected = UnexpectedBuildFailure::class)
    fun `when application plugin not available should throw exception`() {
        buildFile.appendText("""
            plugins {
                id 'info.adavis.qualitychecks'
            }
        """)

        GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withPluginClasspath()
                .build()
    }

    @Test
    fun `when plugin applied build should succeed and create files`() {
        buildFile.appendText("""
            plugins {
                id 'application'
                id 'info.adavis.qualitychecks'
            }
        """)

        val result: BuildResult = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withPluginClasspath()
                .build()

        assertTrue(result.output.contains("BUILD SUCCESSFUL"))

        val qualityChecksDir = File(buildFile.parentFile, "quality-checks")
        assertTrue(qualityChecksDir.exists())
        assertTrue(File(qualityChecksDir, QualityChecksPlugin.PMD_FILE_NAME).exists())
        assertTrue(File(qualityChecksDir, QualityChecksPlugin.CHECKSTYLE_FILE_NAME).exists())
        assertTrue(File(qualityChecksDir, QualityChecksPlugin.FINDBUGS_FILE_NAME).exists())
    }

    @Test
    fun `when executed checkstyle should succeed`() {
        buildFile.appendText("""
            plugins {
                id 'application'
                id 'info.adavis.qualitychecks'
            }
        """)

        val result: BuildResult = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments("checkstyle", "--info")
                .withPluginClasspath()
                .build()

        println(result.output)
        assertTrue(result.output.contains("BUILD SUCCESSFUL"))
    }
}