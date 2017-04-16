package info.adavis.qualitychecks.tasks

import info.adavis.qualitychecks.QualityChecksExtension
import info.adavis.qualitychecks.QualityChecksPlugin
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class CheckstyleTaskTest
{
    @Test
    fun createCheckstyleTask()
    {
        val project = ProjectBuilder.builder().build()

        with(project)
        {
            extensions.create("qualityChecks", QualityChecksExtension::class.java)
            extensions.findByType(QualityChecksExtension::class.java).checkstyleConfigFile = File.createTempFile("temp", ".xml").path

            val checkstyleTask = tasks.create("checkstyle", CheckstyleTask::class.java)

            assertNotNull(checkstyleTask)
            assertEquals(QualityChecksPlugin.VERIFICATION_GROUP, checkstyleTask.group)
            assertTrue(checkstyleTask.configFile.name.startsWith("temp"))
        }
    }
}