package info.adavis.qualitychecks.tasks

import info.adavis.qualitychecks.QualityChecksPlugin
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertNull

/**
 * @author Annyce Davis on 12/20/15.
 */
@SuppressWarnings("GroovyAssignabilityCheck")
class WriteConfigFileTaskTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder()

    def projectDir
    def project
    def task

    @Before
    void setUp() {
        projectDir = temporaryFolder.root
        projectDir.mkdirs()

        project = ProjectBuilder.builder().withProjectDir(projectDir).build()
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

    @Test
    void shouldWriteFileIfFileExists() {
        def task = project.task('testWriteConfigFile', type: WriteConfigFileTask) {
            configFile = temporaryFolder.newFile('pmd-ruleset.xml')
            fileName = QualityChecksPlugin.PMD_FILE_NAME
        }

        task.writeConfigFile()

        def file = new File('pmd-ruleset.xml', projectDir)

        assertTrue(file.exists())
        assertEquals(QualityChecksPlugin.PMD_FILE_NAME, task.configFile.name)
        assertTrue(file.text.contains('<description>Custom ruleset for Android application</description>'))
    }

}
