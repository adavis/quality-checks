import org.gradle.api.tasks.testing.Test
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension
import com.gradle.publish.PluginBundleExtension

buildscript {
    repositories {
        jcenter()
        gradleScriptKotlin()
        maven { setUrl("https://plugins.gradle.org/m2/") }
    }
    dependencies {
        classpath(kotlinModule("gradle-plugin"))
        classpath("com.gradle.publish:plugin-publish-plugin:0.9.7")
    }
}

apply {
    plugin("kotlin")
    plugin("org.gradle.java-gradle-plugin")
    plugin("com.gradle.plugin-publish")
}

repositories {
    jcenter()
}

dependencies {
    compile(kotlinModule("stdlib", "1.1.2-2"))
    compile(gradleScriptKotlinApi())
    testCompile("junit:junit:4.12")
}

val sourceSets = the<JavaPluginConvention>().sourceSets

sourceSets {
    "functionalTest" {
        compileClasspath += sourceSets["main"].output + configurations.testRuntime
        runtimeClasspath += output + compileClasspath
    }
}

task<Test>("functionalTest") {
    group = "verification"
    testClassesDir = sourceSets["functionalTest"].output.classesDir
    classpath = sourceSets["functionalTest"].runtimeClasspath
}

tasks {
    withType(Test::class.java) {
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    "check" {
        dependsOn("functionalTest")
    }
}

version = "0.2.4"
group = "info.adavis"

val gradlePlugin = configure<GradlePluginDevelopmentExtension> {
    testSourceSets(sourceSets["functionalTest"])

    (plugins) {
        "qualityChecksPlugin" {
            id = "info.adavis.qualitychecks"
            implementationClass = "info.adavis.qualitychecks.QualityChecksPlugin"
        }
    }
}

configure<PluginBundleExtension> {
    website = "https://github.com/adavis/quality-checks"
    vcsUrl = "https://github.com/adavis/quality-checks.git"
    description = "Simple Gradle Plugin for including Checkstyle, FindBugs, and PMD in your Android project."
    tags = listOf("Checkstyle", "FindBugs", "PMD", "Android")

    this.plugins {
        "qualityChecksPlugin" {
            id = "info.adavis.qualitychecks"
            displayName = "Quality Checks Android Plugin"
        }
    }
}
