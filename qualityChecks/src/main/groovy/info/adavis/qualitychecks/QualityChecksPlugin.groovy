package info.adavis.qualitychecks

import info.adavis.qualitychecks.tasks.CheckstyleTask
import info.adavis.qualitychecks.tasks.FindBugsTask
import info.adavis.qualitychecks.tasks.PmdTask
import info.adavis.qualitychecks.tasks.WriteConfigFileTask
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author adavis on 12/15/15.
 */
@SuppressWarnings("GroovyAssignabilityCheck")
class QualityChecksPlugin implements Plugin<Project> {

    public static final String PMD_FILE_NAME = 'pmd-ruleset.xml'
    public static final String CHECKSTYLE_FILE_NAME = 'checkstyle.xml'
    public static final String FINDBUGS_FILE_NAME = 'findbugs-exclude.xml'
    public static final String WRITE_PMD_CONFIG_FILE_TASK = 'writePmdConfigFile'
    public static final String WRITE_CHECK_STYLE_CONFIG_FILE_TASK = 'writeCheckStyleConfigFile'
    public static final String WRITE_FIND_BUGS_EXCLUSION_FILE_TASK = 'writeFindBugsExclusionFile'
    public static final String VERIFICATION_GROUP = 'verification'

    Project project
    File pmdConfigFile
    File checkStyleConfigFile
    File findBugsExclusionFile

    @Override
    void apply(Project project) {
        this.project = project

        project.extensions.create('qualityChecks', QualityChecksExtension)

        createConfigFilesIfNeeded()
        createConfigFileTasks()
        createQualityChecksTasks()
    }

    private void createQualityChecksTasks() {
        project.afterEvaluate {
            project.task('pmd', type: PmdTask)
            project.task('findbugs', type: FindBugsTask)
            project.task('checkstyle', type: CheckstyleTask)
        }
    }

    private void createConfigFilesIfNeeded() {
        pmdConfigFile = createConfigFile(PMD_FILE_NAME)
        checkStyleConfigFile = createConfigFile(CHECKSTYLE_FILE_NAME)
        findBugsExclusionFile = createConfigFile(FINDBUGS_FILE_NAME)
    }

    private void createConfigFileTasks() {
        project.task(WRITE_PMD_CONFIG_FILE_TASK, type: WriteConfigFileTask) {
            configFile = pmdConfigFile
            fileName = PMD_FILE_NAME
        }

        project.task(WRITE_CHECK_STYLE_CONFIG_FILE_TASK, type: WriteConfigFileTask) {
            configFile = checkStyleConfigFile
            fileName = CHECKSTYLE_FILE_NAME
        }

        project.task(WRITE_FIND_BUGS_EXCLUSION_FILE_TASK, type: WriteConfigFileTask) {
            configFile = findBugsExclusionFile
            fileName = FINDBUGS_FILE_NAME
        }
    }

    File createConfigFile(String fileName) {
        File qualityChecksDir = new File(project.buildFile.parentFile, 'quality-checks')
        if (!qualityChecksDir.exists()) {
            qualityChecksDir.mkdirs()
        }

        File to = new File(qualityChecksDir, fileName)
        if (!to.exists()) {
            return to
        }
        return null
    }

}