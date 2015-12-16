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

        pmdConfigFile = createConfigFile('pmd-ruleset.xml')
        checkStyleConfigFile = createConfigFile('checkstyle.xml')
        findBugsExclusionFile = createConfigFile('findbugs-exclude.xml')

        def writePmdConfigFile = project.task('writePmdConfigFile') {
            description 'Write PMD config file for PMD task'

            onlyIf {
                pmdConfigFile != null
            }

            doFirst {
                if (pmdConfigFile != null) {
                    copyConfigFile('pmd-ruleset.xml', pmdConfigFile)
                }
            }
        }

        def writeCheckstyleConfigFile = project.task('writeCheckStyleConfigFile') {
            description 'Write Checkstyle config file for Checkstyle task'

            onlyIf {
                checkStyleConfigFile != null
            }

            doFirst {
                if (checkStyleConfigFile != null) {
                    copyConfigFile('checkstyle.xml', checkStyleConfigFile)
                }
            }
        }

        def writeFindBugsExclusionFile = project.task('writeFindBugsExclusionFile') {
            description 'Write FindBugs exclusion file for FindBugs task'

            onlyIf {
                findBugsExclusionFile != null
            }

            doFirst {
                if (findBugsExclusionFile != null) {
                    copyConfigFile('findbugs-exclude.xml', findBugsExclusionFile)
                }
            }
        }

        project.afterEvaluate {

            project.plugins.withType(PmdPlugin) {
                project.tasks.create('pmd', Pmd) {
                    dependsOn(writePmdConfigFile)

                    description 'Run PMD'
                    group 'verification'

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
                    dependsOn(writeFindBugsExclusionFile)

                    description 'Run FindBugs'
                    group 'verification'

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
                    dependsOn(writeCheckstyleConfigFile)

                    description 'Run Checkstyle'
                    group 'verification'

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

    def copyConfigFile(String fileName, File configFile) {
        this.class.classLoader.getResourceAsStream(fileName).withStream { input ->
            configFile.withOutputStream { out ->
                out << input
            }
        }
    }
}