package info.adavis.qualitychecks

import info.adavis.qualitychecks.QualityChecksPlugin.Companion.CHECKSTYLE_FILE_NAME
import info.adavis.qualitychecks.QualityChecksPlugin.Companion.FINDBUGS_FILE_NAME
import info.adavis.qualitychecks.QualityChecksPlugin.Companion.PMD_FILE_NAME

open class QualityChecksExtension(var pmdConfigFile: String = "quality-checks/$PMD_FILE_NAME",
                                  var checkstyleConfigFile: String = "quality-checks/$CHECKSTYLE_FILE_NAME",
                                  var findBugsExclusionFile: String = "quality-checks/$FINDBUGS_FILE_NAME")
