package lib

class JobHelper {
    static Closure gerritconfig(String value) {
        return {
            it / 'scm' (class:'hudson.plugins.git.GitSCM', plugin:'git@2.2.12') << {
                'configVersion' ('2')
                'userRemoteConfigs' {
                    'hudson.plugins.git.UserRemoteConfig' {
                        'refspec' ('$GERRIT_REFSPEC')
                        'url' ('ssh://idondemandhudson@dev.idondemand.com:29418/idod/extras/adapter')
                        'credentialsId' (value)
                    }
                }
            }
        }
    }
}
