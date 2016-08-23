package info.adavis.qualitychecks.tasks

import info.adavis.qualitychecks.QualityChecksExtension
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class CheckstyleTaskSpec extends Specification {

    def createCheckstyleTask() {
        given: "we have a project"
        def project = ProjectBuilder.builder().build()

        and: "we apply the extension"
        project.extensions.create('qualityChecks', QualityChecksExtension)

        and: "we supply an existing checkstyle config file"
        project.qualityChecks.checkstyleConfigFile = File.createTempFile('temp', '.xml').path

        when: "we create a checkstyle task"
        def checkstyleTask = project.tasks.create('checkstyle', CheckstyleTask)

        then: "it should not replace our previous file"
        checkstyleTask
        checkstyleTask.configFile.name.startsWith('temp')
    }
}