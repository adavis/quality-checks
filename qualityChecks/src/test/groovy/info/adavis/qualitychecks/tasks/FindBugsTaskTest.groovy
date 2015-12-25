package info.adavis.qualitychecks.tasks

import info.adavis.qualitychecks.QualityChecksExtension
import info.adavis.qualitychecks.QualityChecksPlugin
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.*

class FindBugsTaskTest {

    @Test
    void createFindBugsTask() {
        def project = ProjectBuilder.builder().build()
        project.extensions.create('qualityChecks', QualityChecksExtension)
        project.qualityChecks.findBugsExclusionFile = File.createTempFile('temp', '.xml').path

        def findbugsTask = project.tasks.create('findbugs', FindBugsTask)

        assertNotNull(findbugsTask)
        assertEquals(QualityChecksPlugin.VERIFICATION_GROUP, findbugsTask.group)
        assertTrue(findbugsTask.excludeFilter.name.startsWith('temp'))
    }
}