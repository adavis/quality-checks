package info.adavis.qualitychecks

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static junit.framework.TestCase.assertTrue
import static junit.framework.TestCase.assertNull

/**
 * @author Annyce Davis on 12/20/15.
 */
class WriteConfigFileTaskTest {

    @Test
    void shouldBeAbleToCreateTask() {
        def project = ProjectBuilder.builder().build()
        def task = project.tasks.create('writeConfigFile', WriteConfigFileTask)

        assertTrue(task instanceof WriteConfigFileTask)
    }

    @Test
    void shouldNotWriteFile() {
        def project = ProjectBuilder.builder().build()
        def task = project.tasks.create('writeConfigFile', WriteConfigFileTask)

        task.setConfigFile(File.createTempFile('temp', '.xml'))
        task.setFileName('pmd-ruleset.xml')

        task.writeConfigFile()

        assertTrue(task.getConfigFile().getName().startsWith('temp'))
    }

    @Test
    void shouldNotWriteFileIfNull() {
        def project = ProjectBuilder.builder().build()
        def task = project.tasks.create('writeConfigFile', WriteConfigFileTask)

        task.writeConfigFile()

        assertNull(task.getConfigFile())
    }
}
