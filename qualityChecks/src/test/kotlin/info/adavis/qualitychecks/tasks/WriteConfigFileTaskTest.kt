package info.adavis.qualitychecks.tasks

import info.adavis.qualitychecks.QualityChecksPlugin
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class WriteConfigFileTaskTest
{
    @Rule @JvmField
    val temporaryFolder = TemporaryFolder()

    lateinit var projectDir: File
    lateinit var project: Project
    lateinit var task: WriteConfigFileTask

    @Before
    fun setUp()
    {
        projectDir = temporaryFolder.root
        projectDir.mkdirs()

        project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        task = project.tasks.create("writeConfigFile", WriteConfigFileTask::class.java)
    }

    @Test
    fun shouldBeAbleToCreateTask()
    {
        assertTrue(task is WriteConfigFileTask)
    }

    @Test
    fun shouldNotWriteFile()
    {
        with(task)
        {
            configFile = File.createTempFile("temp", ".xml")
            fileName = "pmd-ruleset.xml"

            writeConfigFile()

            configFile?.name?.startsWith("temp")?.let(::assertTrue)
        }
    }

    @Test
    fun shouldNotWriteFileIfNull()
    {
        with(task)
        {
            writeConfigFile()

            assertNull(configFile)
        }
    }

    @Test
    fun shouldWriteFileIfFileExists()
    {
        with(task)
        {
            configFile = temporaryFolder.newFile("pmd-ruleset.xml")
            fileName = QualityChecksPlugin.PMD_FILE_NAME

            writeConfigFile()

            val file = File(projectDir, "pmd-ruleset.xml")

            assertTrue(file.exists())
            assertEquals(QualityChecksPlugin.PMD_FILE_NAME, task.configFile?.name)
            assertTrue(file.readText().contains("<description>Custom ruleset for Android application</description>"))
        }
    }

}
