job('app-beta-deploy') {
    description ''
    
    configure { project ->
        
        project / 'properties' / 'hudson.model.ParametersDefinitionProperty' {
            'parameterDefinitions' {
                'hudson.model.StringParameterDefinition' {
                    name ('GERRIT_REFSPEC')
                    defaultValue ('refs/heads/master')
                }
            }
        }        

        project / 'scm' (class:'hudson.plugins.git.GitSCM', plugin:'git@2.2.12') << {
            configVersion ('2')
            'userRemoteConfigs' {
                'hudson.plugins.git.UserRemoteConfig' {
                    refspec ('$GERRIT_REFSPEC')
                    url ('ssh://idondemandhudson@git.dev.identiv.com:29418/idod/chef/instances/beta')
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

        project / triggers << 'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.GerritTrigger' (plugin:"gerrit-trigger@2.22.0") {     
            spec ''
            'gerritProjects' {
                'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data.GerritProject' {
                    compareType ('PLAIN')
                    pattern('idod/chef/instances/beta')
                    'branches' {
                        'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data.Branch' {
                            compareType ('REG_EXP')
                            pattern ('.*')
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
            buildStartMessage''
            buildFailureMessage''
            buildSuccessfulMessage''
            buildUnstableMessage''
            buildNotBuiltMessage''
            buildUnsuccessfulFilepath''
            customUrl''
            serverName('__ANY__')
            'triggerOnEvents' {
                'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.events.PluginChangeMergedEvent' {}
            }
            allowTriggeringUnreviewedPatches('false')
            dynamicTriggerConfiguration('false')
            triggerConfigURL()
            triggerInformationAction()
        }        


        project << {
            quietPeriod ('5')
            canRoam ('true')
            disabled ('false')
            blockBuildWhenDownstreamBuilding ('false')
            blockBuildWhenUpstreamBuilding ('false')
            keepDependencies ('false')
            assignedNode('slave01')
        }

        project / builders << 'hudson.tasks.Shell' {
            command ('#!/bin/bash -lx # Load RVM into a shell session *as a function* [[ -s "$HOME/.rvm/scripts/rvm" ]] && source "$HOME/.rvm/scripts/rvm" git clean -fd git reset --hard HEAD #rvm install 2.0.0 rvm use 2.0.0 bundle berks ssh -t app-beta.idondemand.com \'sudo yum clean all\' knife solo cook --no-color app-beta.idondemand.com #knife solo cook --no-color app.ficam-test.idondemand.com knife solo cook --no-color sync-beta.idondemand.com')
        }
        
        project / publishers << 'hudson.tasks.Mailer' {
            recipients ('mbutcher@identiv.com, tprasanna@identiv.com')
            dontNotifyEveryUnstableBuild('false')
            sendToIndividuals ('false')
        }

        project / publishers << 'hudson.tasks.BuildTrigger' {
            childProjects ('app-beta-testsuite-release')
            'threshold' {
                name ('SUCCESS')
                ordinal ('0')
                color ('BLUE')
                completeBuild ('true')
            }
        }     

        project / buildWrappers << 'hudson.plugins.ansicolor.AnsiColorBuildWrapper' (plugin:"ansicolor@0.4.1") {
            colorMapName ('gnome-terminal')
        }       
    }
}
        