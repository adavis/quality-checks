import org.gradle.api.tasks.wrapper.Wrapper

buildscript {
    repositories {
        jcenter()
        gradleScriptKotlin()
    }
    dependencies {
    }
}

allprojects {
    repositories {
        jcenter()
    }
}

task<Wrapper>("wrapper") {
    gradleVersion = "3.5"
}