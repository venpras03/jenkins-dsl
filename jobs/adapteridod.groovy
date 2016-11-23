job('idod-adapter') {
    description 'Build and test the app.'
    
    configure { project ->
        
        project / 'logRotator' << {
            daysToKeep(14)
            numToKeep(40)
            artifactDaysToKeep(-1)
            artifactNumToKeep(-1)
        }

        project / 'scm' (class:'hudson.plugins.git.GitSCM', plugin:'git@2.2.12') << {
            configVersion ('2')
            'userRemoteConfigs' {
                'hudson.plugins.git.UserRemoteConfig' {
                    refspec ('$GERRIT_REFSPEC')
                    url ('ssh://idondemandhudson@dev.idondemand.com:29418/idod/extras/adapter')
                    credentialsId ('b4b11ae3-8b97-4ea4-955e-478d2b93d478')
                }
            }      
            'branches' {
                'hudson.plugins.git.BranchSpec' {
                    name ('master')
                }
            }
            doGenerateSubmoduleConfigurations ('false')
            submoduleCfg (class:"list")
            'extensions' {
                'hudson.plugins.git.extensions.impl.BuildChooserSetting' {
                    'buildChooser' (class:"com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.GerritTriggerBuildChooser", plugin:"gerrit-trigger@2.11.0"){                  
                        separator ('#')
                    }
                }
            'hudson.plugins.git.extensions.impl.CleanCheckout' {}
            }   

        }


        project << {
            quietPeriod ('6')
            canRoam ('true')
            disabled ('false')
            blockBuildWhenDownstreamBuilding ('false')
            blockBuildWhenUpstreamBuilding ('false')
        }

        project / 'properties' / 'hudson.plugins.disk__usage.DiskUsageProperty' {
            'slaveWorkspacesUsage' {
                'entry' {
                    string ('slave-ITS Linux 3')
                    'concurrent-hash-map' {
                        'entry' {
                            string ('/home/ubuntu/workspace/idod-adapter')
                        }
                    }
                }
            }
            diskUsageWithoutBuilds ('0')
        }

        
        project / 'properties' / 'hudson.security.AuthorizationMatrixProperty' {
            permission('hudson.model.Item.Build:thu')
            permission('hudson.model.Item.Workspace:thu')
            permission('hudson.model.Item.Discover:thu')
            permission('hudson.model.Item.Read:thu')
            permission('hudson.model.Item.Cancel:thu')
        }
        
        project / 'properties' / 'hudson.model.ParametersDefinitionProperty' {
            'parameterDefinitions' {
                'hudson.model.StringParameterDefinition' {
                    name ('GERRIT_REFSPEC')
                    defaultValue ('refs/heads/master')
                }
            }
        }
        
        project / publishers << 'hudson.tasks.ArtifactArchiver' {
            artifacts ('build/libs/*,build/distributions/*,**/build/libs/*,**/build/distributions/*')
            latestOnly(false)
            allowEmptyArchive(false)
        }
        project / publishers << 'hudson.tasks.Fingerprinter' {
            targets {
                recordBuildArtifacts(true)
            }
        }     

        project / triggers << 'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.GerritTrigger' (plugin:"gerrit-trigger@2.11.0") {     
            spec ''
            'gerritProjects' {
                'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data.GerritProject' {
                    compareType ('PLAIN')
                    pattern('idod/extras/adapter')
                    'branches' {
                        'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data.Branch' {
                            compareType ('PLAIN')
                            pattern ('master')
                        }
                    }
                }
            }
            'skipVote' {
                onSuccessful('false')
                onFailed('false')
                onUnstable('false')
                onNotBuilt('false')
            }
            silentMode('false')
            escapeQuotes('true')
            noNameAndEmailParameters('true')
            buildStartMessage()
            buildFailureMessage()
            buildSuccessfulMessage()
            buildUnstableMessage()
            buildNotBuiltMessage()
            buildUnsuccessfulFilepath()
            customUrl()
            serverName('__ANY__')
            'triggerOnEvents' {
                'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.events.PluginPatchsetCreatedEvent' {}
            }
            allowTriggeringUnreviewedPatches('false')
            dynamicTriggerConfiguration('false')
            triggerConfigURL()
            triggerInformationAction()
        }


    }

    steps {
        gradle {
            useWrapper true
            makeExecutable false
            fromRootBuildScriptDir false
            tasks 'build'
        }
    }    
}
        