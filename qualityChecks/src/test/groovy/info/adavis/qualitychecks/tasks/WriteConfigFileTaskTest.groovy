package info.adavis.qualitychecks.tasks

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertNull

/**
 * @author Annyce Davis on 12/20/15.
 */
@SuppressWarnings("GroovyAssignabilityCheck")
class WriteConfigFileTaskTest {

    def project
    def task

    @Before
    void setUp() {
        project = ProjectBuilder.builder().build()
        task = project.tasks.create('writeConfigFile', WriteConfigFileTask)
    }

    @Test
    void shouldBeAbleToCreateTask() {
        assertTrue(task instanceof WriteConfigFileTask)
    }

    @Test
    void shouldNotWriteFile() {
        task.setConfigFile(File.createTempFile('temp', '.xml'))
        task.setFileName('pmd-ruleset.xml')

        task.writeConfigFile()

        assertTrue(task.configFile.name.startsWith('temp'))
    }

    @Test
    void shouldNotWriteFileIfNull() {
        task.writeConfigFile()

        assertNull(task.getConfigFile())
    }
}
