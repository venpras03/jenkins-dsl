package helpers

class JobHelper {
    
    def plugin_git_version;
    def plugin_gerrit_trigger_version;

    JobHelper () {
        plugin_git_version = 'git@2.2.12'
        plugin_gerrit_trigger_version = 'gerrit-trigger@2.22.0'
    }

    static Closure gerritConfigurations(String gerritrepo) {
        return {
            it / 'scm' (class:'hudson.plugins.git.GitSCM', plugin:plugin_git_version) << {
                'configVersion' ('2')
                'userRemoteConfigs' {
                    'hudson.plugins.git.UserRemoteConfig' {
                        'refspec' ('$GERRIT_REFSPEC')
                        'url' ('ssh://idondemandhudson@git.dev.identv.com:29418/$gerritrepo')
                        'credentialsId' ('b4b11ae3-8b97-4ea4-955e-478d2b93d478')
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
                        'buildChooser' (class:"com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.GerritTriggerBuildChooser", \
                                                                                    plugin:plugin_gerrit_trigger_version){                  
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
            it / 'properties' << 'hudson.model.ParametersDefinitionProperty' {
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
            it / 'triggers' <<'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.GerritTrigger' \
                                                                        (plugin:plugin_gerrit_trigger_version) {     
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

    static Closure artifactArchiver(String artifactsvalue) {
        return {
            it / 'publishers' << 'hudson.tasks.ArtifactArchiver' {
                'artifacts' (artifactsvalue)
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



    static Closure otherConfigurations(String quietPeriodvalue, String canRoamValue, String assignedNode) {
        return {
            it / {
                'quietPeriod' (quietPeriodvalue)
                'canRoam' (canRoamValue)
                'disabled' ('false')
                'keepDependencies' ('false')
                'concurrentBuild' ('true')
                'assignedNode' (assignedNode)                 
            }
        }
    }    

    static Closure gradleSetup (String tasks) {
        return {
            'steps' {
                'gradle' {
                    'useWrapper' 'true'
                    'makeExecutable' 'false'
                    'fromRootBuildScriptDir' 'false'
                    'tasks' tasks
                }
            }
        }
    }

    // static Closure authorizationMatrix (String personNames) {
    //     // Implement a each loop to do this for a list of names
    //         //     project / 'properties' / 'hudson.security.AuthorizationMatrixProperty' {
    // //         permission('hudson.model.Item.Build:thu')
    // //         permission('hudson.model.Item.Workspace:thu')
    // //         permission('hudson.model.Item.Discover:thu')
    // //         permission('hudson.model.Item.Read:thu')
    // //         permission('hudson.model.Item.Cancel:thu')
    // //     }
    // }
}
