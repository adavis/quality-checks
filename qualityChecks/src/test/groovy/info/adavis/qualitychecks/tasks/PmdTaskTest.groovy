package info.adavis.qualitychecks.tasks

import info.adavis.qualitychecks.QualityChecksExtension
import info.adavis.qualitychecks.QualityChecksPlugin
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.*

class PmdTaskTest {

    @Test
    void createPmdTask() {
        def project = ProjectBuilder.builder().build()
        project.extensions.create('qualityChecks', QualityChecksExtension)
        project.qualityChecks.pmdConfigFile = File.createTempFile('temp', '.xml').path

        def pmdTask = project.tasks.create('pmd', PmdTask)

        assertNotNull(pmdTask)
        assertEquals(QualityChecksPlugin.VERIFICATION_GROUP, pmdTask.group)
        assertTrue(pmdTask.ruleSetFiles[0].name.startsWith('temp'))
    }
}