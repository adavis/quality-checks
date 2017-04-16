package info.adavis.qualitychecks

open class QualityChecksExtension(var pmdConfigFile: String = "quality-checks/pmd-ruleset.xml",
                                  var checkstyleConfigFile: String = "quality-checks/checkstyle.xml",
                                  var findBugsExclusionFile: String = "quality-checks/findbugs-exclude.xml")
