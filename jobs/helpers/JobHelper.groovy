package helpers

class JobHelper {
    static Closure gerritConfigurations(String value) {
        return {
            it / 'scm' (class:'hudson.plugins.git.GitSCM', plugin:'git@2.2.12') << {
                'configVersion' ('2')
                'userRemoteConfigs' {
                    'hudson.plugins.git.UserRemoteConfig' {
                        'refspec' ('$GERRIT_REFSPEC')
                        'url' ('ssh://idondemandhudson@dev.idondemand.com:29418/idod/extras/adapter')
                        'credentialsId' (value)
                    }
                }

                'branches' {
                    'hudson.plugins.git.BranchSpec' {
                        'name' ('master')
                    }
                }
                'doGenerateSubmoduleConfigurations' ('false')
                'submoduleCfg' (class:"list")
                'extensions' {
                    'hudson.plugins.git.extensions.impl.BuildChooserSetting' {
                        'buildChooser' (class:"com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.GerritTriggerBuildChooser", plugin:"gerrit-trigger@2.11.0"){                  
                            'separator' ('#')
                        }
                    }
                'hudson.plugins.git.extensions.impl.CleanCheckout' {}
                }                
            }
        }
    }

    static Closure gerritParameters(String value) {
        return {
            it / 'hudson.model.ParametersDefinitionProperty' {
                'parameterDefinitions' {
                    'hudson.model.StringParameterDefinition' {
                        'name' ('GERRIT_REFSPEC')
                        'defaultValue' (value)
                    }
                }
            }
        }
    }

    static Closure gerritTrigger (String gerritrepo) {
        return {
            it / 'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.GerritTrigger' (plugin:"gerrit-trigger@2.22.0") {     
                'spec' ''
                'gerritProjects' {
                    'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data.GerritProject' {
                        'compareType' ('PLAIN')
                        'pattern'(gerritrepo)
                            'branches' {
                            'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data.Branch' {
                                'compareType' ('PLAIN')
                                'pattern' ('master')
                            }
                        }
                    }
                }
                'skipVote' {
                    'onSuccessful'('false')
                    'onFailed'('false')
                    'onUnstable'('false')
                    'onNotBuilt'('false')
                }
                'silentMode'('false')
                'escapeQuotes'('true')
                'noNameAndEmailParameters'('true')
                'buildStartMessage'()
                'buildFailureMessage'()
                'buildSuccessfulMessage'()
                'buildUnstableMessage'()
                'buildNotBuiltMessage'()
                'buildUnsuccessfulFilepath'()
                'customUrl'()
                'serverName'('git.dev.identiv.com')
                    'triggerOnEvents' {
                    'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.events.PluginPatchsetCreatedEvent' {}
                }
                'allowTriggeringUnreviewedPatches'('false')
                'dynamicTriggerConfiguration'('false')
                'triggerConfigURL'()
                'triggerInformationAction'()
            }
        }

    }

    static Closure artifactArchiver(String quietPeriodvalue, String canRoamValue) {
        return {
            it / 'publishers' << 'hudson.tasks.ArtifactArchiver' {
                'artifacts' ('build/libs/*,build/distributions/*,**/build/libs/*,**/build/distributions/*')
                'latestOnly'('false')
                'allowEmptyArchive'('false')
            }

        }
    }

    static Closure artifactFingerprinter () {
        return {
            it / 'publishers' << 'hudson.tasks.Fingerprinter' {
                'targets' {
                    'recordBuildArtifacts'('true')
                }
            }
        }
    }



    static Closure otherConfigurations(String quietPeriodvalue, String canRoamValue) {
        return {
            it / {
                'quietPeriod' (quietPeriodvalue)
                'canRoam' (canRoamValue)
                'disabled' ('false')
            }
        }
    }    

    static Closure gradleSetup () {
        return {
            it / {        
                'steps' {
                    'gradle' {
                        'useWrapper' 'true'
                        'makeExecutable' 'false'
                        'fromRootBuildScriptDir' 'false'
                        'tasks' 'build'
                    }
                }
            }
        }
    }

    static Closure authorizationMatrix (String personNames) {
        // Implement a each loop to do this for a list of names
            //     project / 'properties' / 'hudson.security.AuthorizationMatrixProperty' {
    //         permission('hudson.model.Item.Build:thu')
    //         permission('hudson.model.Item.Workspace:thu')
    //         permission('hudson.model.Item.Discover:thu')
    //         permission('hudson.model.Item.Read:thu')
    //         permission('hudson.model.Item.Cancel:thu')
    //     }
    }
}
