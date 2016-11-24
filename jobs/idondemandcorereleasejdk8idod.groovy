job('idondemand-core-release-jdk8') {
    description ''
    displayName 'idOnDemand Core [JDK8/Release]'

    configure { project ->
        
        project / 'logRotator' (class:"hudson.tasks.LogRotator") <<  {
            daysToKeep(-1)
            numToKeep(20)
            artifactDaysToKeep(7)
            artifactNumToKeep(20)
        }
        project / 'properties' / 'hudson.model.ParametersDefinitionProperty' {
            'parameterDefinitions' {
                'hudson.model.StringParameterDefinition' {
                    name ('database')
                    defaultValue ('postgres') 
                }
                'hudson.model.StringParameterDefinition' {
                    name ('GERRIT_REFSPEC')
                    defaultValue ('refs/heads/master') 
                }
                'hudson.model.StringParameterDefinition' {
                    name ('GIT_BRANCH')
                    defaultValue ('master')
                }
            }
        }
               

        project / 'scm' (class:'hudson.plugins.git.GitSCM', plugin:'git@2.2.12') << {
            configVersion ('2')
            'userRemoteConfigs' {
                'hudson.plugins.git.UserRemoteConfig' {
                    refspec ('$GERRIT_REFSPEC')
                    url ('ssh://idondemandhudson@git.dev.identiv.com:29418/idod/core')
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

        project / triggers << {
            'jenkins.triggers.ReverseBuildTrigger'{
                upstreamProjects'Project name'
                'threshold'{
                    spec''
                    name ('SUCCESS')
                    ordinal ('0')
                    color ('BLUE')
                    completeBuild ('true')
                }    
            }

        }

        project << {
            quietPeriod ('74')
            canRoam ('true')
            disabled ('false')
            blockBuildWhenDownstreamBuilding ('false')
            blockBuildWhenUpstreamBuilding ('false')
            keepDependencies ('false')
            assignedNode('windows')
            concurrentBuild ('true')
        }

        project / builders << 'hudson.tasks.Shell' {
            command ('git clean -fdx && git reset --hard HEAD')
            command ('java -version')
            command ('git show HEAD')
        }
        
        project / builders << 'org.jvnet.hudson.plugins.exclusion.CriticalBlockStart' (plugin:"Exclusion@0.8") {
            'hudson.plugins.gradle.Gradle' (plugin:"gradle@1.23") {
                switches ('-Pidod.gwt.all -P$database --refresh-dependencies -Pidod.release --no-daemon')
                tasks ('clean idondemandpackage:buildRpms')
                rootBuildScriptDir''
                buildFile''
                useWrapper ('true')
                makeExecutable ('true')
                fromRootBuildScriptDir ('false')
                useWorkspaceAsHome ('false')
            }
            'org.jvnet.hudson.plugins.exclusion.CriticalBlockEnd' (plugin:"Exclusion@0.8") ''
        }
        
        project / publishers << 'hudson.tasks.ArtifactArchiver' {
            artifacts ('**/build/libs/*,**/build/distributions/*')
            latestOnly(false)
            allowEmptyArchive(false)
        }
        project / publishers << 'hudson.tasks.Fingerprinter' {
            targets ('**/build/libs/*,**/build/distributions/*')
            recordBuildArtifacts ('false')
        }     

        project / publishers << 'hudson.tasks.BuildTrigger' {
            childProjects ('its-yum-publish')
            'threshold' {
                name ('SUCCESS')
                ordinal ('0')
                color ('BLUE')
                completeBuild ('true')
            }
        }
        project / publishers << 'hudson.tasks.Mailer' (plugin:"mailer@1.5") {
            recipients ('idondemand-svn@googlegroups.com')
            dontNotifyEveryUnstableBuild ('false')
            sendToIndividuals ('true')
        }
        project / publishers << 'com.chikli.hudson.plugin.naginator.NaginatorPublisher' (plugin:"naginator@1.9") {
            regexpForRerun ''
            rerunIfUnstable ('false')
            checkRegexp ('false')
            'delay' (class:"com.chikli.hudson.plugin.naginator.ProgressiveDelay") {
                increment ('60')
                max ('0')
            }
            maxSchedule ('6')
        }        
    }
}
        