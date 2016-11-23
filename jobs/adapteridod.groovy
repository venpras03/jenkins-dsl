job('idod-adapter') {
    description 'Build and test the app.'
    
    configure { project ->
        
        project / 'logRotator' << {
            daysToKeep(14)
            numToKeep(40)
            artifactDaysToKeep(-1)
            artifactNumToKeep(-1)
        }

        def scmparam = project / 'scm' (class:'hudson.plugins.git.GitSCM', plugin:'git@2.2.12') << {
            configVersion ('2')
            def paramdef = 'userRemoteConfigs' {
                def strDefinitions = 'hudson.plugins.git.UserRemoteConfig' {
                    refspec ('$GERRIT_REFSPEC')
                    url ('ssh://idondemandhudson@dev.idondemand.com:29418/idod/extras/adapter')
                    credentialsId ('b4b11ae3-8b97-4ea4-955e-478d2b93d478')
                }
            }           
        }

        project << {
            quietPeriod ('6')
            canRoam ('true')
            disabled ('false')
            blockBuildWhenDownstreamBuilding ('false')
            blockBuildWhenUpstreamBuilding ('false')
        }

        def gerritparam = project / 'properties' / 'hudson.plugins.disk__usage.DiskUsageProperty' {
            def paramdef = 'slaveWorkspacesUsage' {
                def strDefinitions = 'entry' {
                    string ('slave-ITS Linux 3')
                    def paramdef = 'concurrent-hash-map' {
                        def strDefinitions = 'entry' {
                            string ('/home/ubuntu/workspace/idod-adapter')
                        }
                    }
                }
            }
            diskUsageWithoutBuilds ('0')
        }

        
        def matrix = project / 'properties' / 'hudson.security.AuthorizationMatrixProperty' {
            permission('hudson.model.Item.Build:thu')
            permission('hudson.model.Item.Workspace:thu')
            permission('hudson.model.Item.Discover:thu')
            permission('hudson.model.Item.Read:thu')
            permission('hudson.model.Item.Cancel:thu')
        }
        
        def param = project / 'properties' / 'hudson.model.ParametersDefinitionProperty' {
            def paramdef = 'parameterDefinitions' {
                def strDefinitions = 'hudson.model.StringParameterDefinition' {
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
        