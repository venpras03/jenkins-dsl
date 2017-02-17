job('idod-util-publish') {
    description ''

    configure { project ->

        project / 'logRotator' (class:"hudson.tasks.LogRotator") <<  {
            daysToKeep(7)
            numToKeep(20)
            artifactDaysToKeep(7)
            artifactNumToKeep(20)
        }

        project / 'properties' / 'hudson.security.AuthorizationMatrixProperty' {
            permission('hudson.model.Item.Build:thu')
            permission('hudson.model.Item.Workspace:thu')
            permission('hudson.model.Item.Discover:thu')
            permission('hudson.model.Item.Read:thu')
            permission('hudson.model.Item.Cancel:thu')
        }

        // Throttle property
        project / 'properties' / 'hudson.plugins.throttleconcurrents.ThrottleJobProperty' (plugin:'throttle-concurrents@1.9.0') {
            maxConcurrentPerNode ('0')
            maxConcurrentTotal ('0')
            throttleEnabled ('false')
            throttleOption ('project')
        }        
              
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
                    url ('ssh://idondemandhudson@git.dev.identiv.com:29418/idod/java/util')
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
                    pattern('idod/java/util')
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
            serverName('git.dev.identiv.com')
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
            canRoam ('false')
            disabled ('false')
            keepDependencies ('false')
            concurrentBuild ('true')
            assignedNode ('linux')     
        }

        project / builders << 'hudson.tasks.Shell' {
            command ('git clean -dfx &amp;&amp; git reset --hard HEAD')
        }        

        project / builders << 'hudson.plugins.gradle.Gradle' (plugin:"gradle@1.26") {
            switches ('--refresh-dependencies')
            tasks ('clean build upload')
            rootBuildScriptDir''
            buildFile''
            useWrapper ('true')
            makeExecutable ('true')
            fromRootBuildScriptDir ('false')
            useWorkspaceAsHome ('false')
        }

        project / publishers << 'hudson.tasks.ArtifactArchiver' {
            artifacts ('**/build/libs/*')
            latestOnly(false)
            allowEmptyArchive(false)
        }

        project / publishers << 'hudson.tasks.BuildTrigger' {
            childProjects ('its-bouncer-publish')
            'threshold' {
                name ('SUCCESS')
                ordinal ('0')
                color ('BLUE')
                completeBuild ('true')
            }
        } 

        project / publishers << 'hudson.tasks.junit.JUnitResultArchiver' {
            testResults ('**/build/test-results/*.xml')
            keepLongStdio ('false')
            testDataPublishers ()
        }

        project / publishers << 'hudson.tasks.Fingerprinter' {
            targets {
                recordBuildArtifacts(true)
            }
        }
    }
}