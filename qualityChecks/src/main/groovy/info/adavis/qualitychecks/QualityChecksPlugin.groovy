package info.adavis.qualitychecks

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.plugins.quality.CheckstylePlugin
import org.gradle.api.plugins.quality.FindBugs
import org.gradle.api.plugins.quality.FindBugsPlugin
import org.gradle.api.plugins.quality.Pmd
import org.gradle.api.plugins.quality.PmdPlugin

/**
 * @author adavis on 12/15/15.
 */
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

    @SuppressWarnings("GroovyAssignabilityCheck")
    @Override
    void apply(Project project) {
        this.project = project

        project.plugins.apply PmdPlugin
        project.plugins.apply FindBugsPlugin
        project.plugins.apply CheckstylePlugin

        project.extensions.create('qualityChecks', QualityChecksExtension)

        pmdConfigFile = createConfigFile(PMD_FILE_NAME)
        checkStyleConfigFile = createConfigFile(CHECKSTYLE_FILE_NAME)
        findBugsExclusionFile = createConfigFile(FINDBUGS_FILE_NAME)

        project.task(WRITE_PMD_CONFIG_FILE_TASK, type: WriteConfigFileTask) {
            group VERIFICATION_GROUP

            configFile = pmdConfigFile
            fileName = PMD_FILE_NAME
        }

        project.task(WRITE_CHECK_STYLE_CONFIG_FILE_TASK, type: WriteConfigFileTask) {
            group VERIFICATION_GROUP

            configFile = checkStyleConfigFile
            fileName = CHECKSTYLE_FILE_NAME
        }

        project.task(WRITE_FIND_BUGS_EXCLUSION_FILE_TASK, type: WriteConfigFileTask) {
            group VERIFICATION_GROUP

            configFile = findBugsExclusionFile
            fileName = FINDBUGS_FILE_NAME
        }

        project.afterEvaluate {

            project.plugins.withType(PmdPlugin) {
                project.tasks.create('pmd', Pmd) {
                    dependsOn(WRITE_PMD_CONFIG_FILE_TASK)

                    description 'Run PMD'
                    group VERIFICATION_GROUP

                    ruleSetFiles = project.files(project?.qualityChecks?.pmdConfigFile)
                    ruleSets = []

                    source 'src'
                    include '**/*.java'
                    exclude '**/gen/**'

                    reports {
                        xml.enabled = true
                        html.enabled = false
                    }

                    ignoreFailures = true
                }
            }

            project.plugins.withType(FindBugsPlugin) {
                project.tasks.create('findbugs', FindBugs) {
                    dependsOn(WRITE_FIND_BUGS_EXCLUSION_FILE_TASK)

                    description 'Run FindBugs'
                    group VERIFICATION_GROUP

                    classes = project.files("$project.buildDir/intermediates/classes")
                    source 'src'
                    classpath = project.files()

                    effort 'max'
                    excludeFilter project.file(project?.qualityChecks?.findBugsExclusionFile)

                    reports {
                        xml.enabled = true
                        html.enabled = false
                    }

                    ignoreFailures = true
                }
            }

            project.plugins.withType(CheckstylePlugin) {
                project.tasks.create('checkstyle', Checkstyle) {
                    dependsOn(WRITE_CHECK_STYLE_CONFIG_FILE_TASK)

                    description 'Run Checkstyle'
                    group VERIFICATION_GROUP

                    configFile project.file(project?.qualityChecks?.checkstyleConfigFile)
                    source 'src'
                    include '**/*.java'
                    exclude '**/gen/**'

                    classpath = project.files()

                    ignoreFailures = false
                }
            }
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