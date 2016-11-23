job('idod-adapter') {
    description 'Build and test the app.'
    scm {
        git('ssh://idondemandhudson@dev.idondemand.com:29418/idod/extras/adapter')
    }
    
    configure { project ->
        
        project / logRotator << 'hudson.tasks.LogRotator' {
            daysToKeep(14)
            numToKeep(40)
            artifactDaysToKeep(-1)
            artifactNumToKeep(-1)
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
        