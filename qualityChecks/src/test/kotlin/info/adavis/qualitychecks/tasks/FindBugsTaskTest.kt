package info.adavis.qualitychecks.tasks

import info.adavis.qualitychecks.QualityChecksExtension
import info.adavis.qualitychecks.QualityChecksPlugin
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class FindBugsTaskTest {

    @Test
    fun `should create findBugs task`() {
        val project = ProjectBuilder.builder().build()

        with(project) {
            extensions.create("qualityChecks", QualityChecksExtension::class.java)
            extensions.findByType(QualityChecksExtension::class.java).findBugsExclusionFile = File.createTempFile("temp", ".xml").path

            val findbugsTask = tasks.create("findbugs", FindBugsTask::class.java)

            assertNotNull(findbugsTask)
            assertEquals(QualityChecksPlugin.VERIFICATION_GROUP, findbugsTask.group)
            assertTrue(findbugsTask.excludeFilter.name.startsWith("temp"))
        }
    }
}