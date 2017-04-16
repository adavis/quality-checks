package info.adavis.qualitychecks.tasks

import info.adavis.qualitychecks.QualityChecksExtension
import info.adavis.qualitychecks.QualityChecksPlugin
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.*
import org.junit.Test

import java.io.File

class PmdTaskTest
{
    @Test
    fun createPmdTask()
    {
        val project = ProjectBuilder.builder().build()

        with(project)
        {
            extensions.create("qualityChecks", QualityChecksExtension::class.java)
            extensions.findByType(QualityChecksExtension::class.java).pmdConfigFile = File.createTempFile("temp", ".xml").path

            val pmdTask = tasks.create("pmd", PmdTask::class.java)

            assertNotNull(pmdTask)
            assertEquals(QualityChecksPlugin.VERIFICATION_GROUP, pmdTask.group)
            assertTrue(pmdTask.ruleSetFiles.first().name.startsWith("temp"))
        }
    }
}