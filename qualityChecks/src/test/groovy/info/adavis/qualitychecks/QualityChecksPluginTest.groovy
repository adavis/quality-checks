package info.adavis.qualitychecks

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.assertNotNull

/**
 * @author Annyce Davis on 1/2/16.
 */
public class QualityChecksPluginTest {

    @Test
    public void pluginShouldBeApplied() {
        def project = ProjectBuilder.builder().build()

        project.apply(plugin: QualityChecksPlugin)

        assertNotNull(project.tasks.findByName(QualityChecksPlugin.WRITE_CHECK_STYLE_CONFIG_FILE_TASK))
        assertNotNull(project.tasks.findByName(QualityChecksPlugin.WRITE_FIND_BUGS_EXCLUSION_FILE_TASK))
        assertNotNull(project.tasks.findByName(QualityChecksPlugin.WRITE_PMD_CONFIG_FILE_TASK))
    }

}
