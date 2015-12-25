package info.adavis.qualitychecks.tasks

import info.adavis.qualitychecks.QualityChecksExtension
import info.adavis.qualitychecks.QualityChecksPlugin
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.*

class CheckstyleTaskTest {

    @Test
    void createCheckstyleTask() {
        def project = ProjectBuilder.builder().build()
        project.extensions.create('qualityChecks', QualityChecksExtension)
        project.qualityChecks.checkstyleConfigFile = File.createTempFile('temp', '.xml').path

        def checkstyleTask = project.tasks.create('checkstyle', CheckstyleTask)

        assertNotNull(checkstyleTask)
        assertEquals(QualityChecksPlugin.VERIFICATION_GROUP, checkstyleTask.group)
        assertTrue(checkstyleTask.configFile.name.startsWith('temp'))
    }
}