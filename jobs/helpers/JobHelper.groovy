package helpers

class JobHelper {
    def plugin_ms_build_version;
    def plugin_git_version;
    def plugin_gerrit_trigger_version;

    JobHelper () {
        plugin_git_version = 'git@2.2.12'
        plugin_gerrit_trigger_version = 'gerrit-trigger@2.22.0'
        plugin_ms_build_version = 'msbuild@1.16'
    }

    static Closure logRotation (String[] configValue) {
        return {        
            it / 'logRotator' (class:"hudson.tasks.LogRotator") <<  {
                'daysToKeep' (configValue[0])
                'numToKeep' (configValue[1])
                'artifactDaysToKeep' (configValue[2])
                'artifactNumToKeep' (configValue[3])
            }   
        }     
    }

    static Closure gerritConfigurations(String gerritrepo) {
        return {
            it / 'scm' (class:'hudson.plugins.git.GitSCM', plugin:plugin_git_version) << {
                'configVersion' ('2')
                'userRemoteConfigs' {
                    'hudson.plugins.git.UserRemoteConfig' {
                        'refspec' ('$GERRIT_REFSPEC')
                        'url' ('ssh://idondemandhudson@git.dev.identiv.com:29418/${gerritrepo}')
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
                        if (eventType == "changemerged")
                        {
                            'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.events.PluginChangeMergedEvent' {}
                        } else {
                            'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.events.PluginPatchsetCreatedEvent' {}
                        }
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



    static Closure otherConfigurations(String[] configValue) {
        return {
            it / 'properties' << {
                'quietPeriod' (configValue[0])
                'canRoam' (configValue[1])
                'disabled' ('false')
                'keepDependencies' ('false')
                'concurrentBuild' ('true')
                'assignedNode' (configValue[2])          
            }
        }
    }    

    static Closure gradleSetup (String tasks) {
        return {
            it /  'builders' << 'hudson.plugins.gradle.Gradle' {
                'tasks' tasks
                'makeExecutable' 'false'
                'fromRootBuildScriptDir' 'false'            
                'useWrapper' 'true'
            }
        }
    }

    static Closure windowsComponent ()
    {
        return {
            it / 'builders' << 'hudson.plugins.msbuild.MsBuildBuilder' (plugin:plugin_ms_build_version) {
            'msBuildName' ('MSBuild35')
            'msBuildFile' ('idondemandSoftCertTool.sln')
            'cmdLineArgs' ('/p:Configuration=Release /t:Clean;Rebuild')
            'buildVariablesAsProperties' ('true')
            'continueOnBuildFailure' ('false')
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
