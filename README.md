Android Quality Checks Plugin
======

This plugin allows you to include Checkstyle, FindBugs, and PMD static code analysis tools to your Android Gradle build. 
It copies basic xml configuration files to a new directory `quality-checks` which is in the same directory of the `build.gradle` file.

Installation
------------

Add the following to your `build.gradle`:

```gradle
buildscript {
    repositories {
        maven { url 'https://plugins.gradle.org/m2/' }
    }
    dependencies {
        classpath 'gradle.plugin.info.adavis:qualityChecks:0.2.+'
    }
}

apply plugin: 'info.adavis.qualitychecks'
```

Usage
-----

You can optionally define the location of the Checkstyle, FindBugs and PMD configuration files you would prefer to use:

```gradle
qualityChecks {
    pmdConfigFile = '<some other location>/pmd-ruleset.xml'
    checkstyleConfigFile = '<some other location>/checkstyle.xml'
    findBugsExclusionFile = '<some other location>/findbugs-exclude.xml'
}
```

Then you can use the following commands to run the checks:

```gradle
gradle pmd
gradle findbugs
gradle checkstyle
```