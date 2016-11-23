job('idod-adapter') {
    description 'Build and test the app.'
    scm {
        git('ssh://idondemandhudson@dev.idondemand.com:29418/idod/extras/adapter')
    }
    
    configure { project ->
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
    }

    configure { project ->
        def trigger = project / 'triggers' / 'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.GerritTrigger' {

    }
    } 

    steps {
        gradle {
            useWrapper true
            tasks 'build'
        }
    }    

    configure { project ->
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

}
        