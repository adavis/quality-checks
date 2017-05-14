package info.adavis.qualitychecks

import org.gradle.api.GradleException
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertNotNull
import org.junit.Test

class QualityChecksPluginTest {

    @Test(expected = GradleException::class)
    fun `plugin should throw exception when app plugin unavailable`() {
        val project = ProjectBuilder.builder().build()

        project.pluginManager.apply(QualityChecksPlugin::class.java)
    }

    @Test
    fun `plugin should add tasks when applied`() {
        val project = ProjectBuilder.builder().build()

        with(project) {
            pluginManager.apply(ApplicationPlugin::class.java)
            pluginManager.apply(QualityChecksPlugin::class.java)

            assertNotNull(tasks.findByName(QualityChecksPlugin.WRITE_CHECK_STYLE_CONFIG_FILE_TASK))
            assertNotNull(tasks.findByName(QualityChecksPlugin.WRITE_FIND_BUGS_EXCLUSION_FILE_TASK))
            assertNotNull(tasks.findByName(QualityChecksPlugin.WRITE_PMD_CONFIG_FILE_TASK))
        }
    }

}
