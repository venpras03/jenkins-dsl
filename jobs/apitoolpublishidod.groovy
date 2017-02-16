job('api-tool-publish') {
    description ''

    configure { project ->

        project / 'logRotator' (class:"hudson.tasks.LogRotator") <<  {
            daysToKeep(-1)
            numToKeep(10)
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
          
        project / 'properties' / 'hudson.plugins.disk__usage.DiskUsageProperty' (plugin:'disk-usage@0.22') {
            diskUsageWithoutBuilds ('14204379')
            'slaveWorkspacesUsage' {
                'entry' {
                    string ()
                    'concurrent-hash-map' {
                        'entry' {
                            string (/c:\hudson\jobs\api-tool-publish\workspace/)
                            'long' ('14829468')
                        }
                    }
                }
            }
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
                    url ('ssh://idondemandhudson@git.dev.identiv.com:29418/its/contrib/softcert-tool')
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
                    pattern('its/contrib/softcert-tool')
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
            canRoam ('true')
            disabled ('false')
            blockBuildWhenDownstreamBuilding ('false')
            blockBuildWhenUpstreamBuilding ('false')
            keepDependencies ('false')
            concurrentBuild ('true')
            assignedLabel ('windows')
            
        }

        project / builders << 'hudson.plugins.gradle.Gradle' (plugin:"gradle@1.23") {
            description ('Clean workspace')
            switches''
            tasks ('clean')
            rootBuildScriptDir''
            buildFile''
            useWrapper ('true')
            makeExecutable ('false')
            fromRootBuildScriptDir ('true')
            useWorkspaceAsHome ('false')
        }

        project / builders << 'hudson.plugins.msbuild.MsBuildBuilder' (plugin:"msbuild@1.16") {
            msBuildName ('MSBuild35')
            msBuildFile ('idondemandSoftCertTool.sln')
            cmdLineArgs ('/p:Configuration=Release /t:Clean;Rebuild')
            buildVariablesAsProperties ('true')
            continueOnBuildFailure ('false')
        }
        project / builders << 'hudson.plugins.gradle.Gradle' (plugin:"gradle@1.23") {
            description ('Upload artifacts')
            switches''
            tasks ('upload')
            rootBuildScriptDir''
            buildFile''
            useWrapper ('true')
            makeExecutable ('false')
            fromRootBuildScriptDir ('true')
            useWorkspaceAsHome ('false')
        }


        project / publishers << 'hudson.tasks.ArtifactArchiver' {
            artifacts ('build/exe/*,build/installer/*,build/distributions/*')
            latestOnly(false)
            allowEmptyArchive(false)
        }
        project / publishers << 'hudson.tasks.Fingerprinter' {
            targets {
                recordBuildArtifacts(true)
            }
        }
    }
}