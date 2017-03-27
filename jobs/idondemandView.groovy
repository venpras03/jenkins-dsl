
import static helpers.JobHelper.*

job('idod-adapter') {
    def gerritrepo = 'idod/extras/adapter'
    def quietPeriod = '40'
    def canRoam = 'false'
    def machine = 'windows'
    description 'Build and test the app.'
    jdk ('jdk8')
    configure gerritParameters ('refs/head/master')
    configure gerritConfigurations(gerritrepo)
    configure gerritTrigger (gerritrepo)
    configure artifactArchiver ('build/libs/*,build/distributions/*,**/build/libs/*,**/build/distributions/*')
    configure artifactFingerprinter ()
    configure otherConfigurations ("40") // quietPeriod, canRoam, machine
    //configure gradleSetup ('build')

    steps {
        gradle {
            useWrapper true
            makeExecutable false
            fromRootBuildScriptDir false
            tasks 'build'
        }
    }
}

//------------------------------------------------- API-TOOL-PUBLISH ----------------------------------------//

job('api-tool-publish') {
    def gerritrepo = 'its/contrib/softcert-tool'
    description 'api-tool-publish'
    jdk ('default')
    //configure logRotation ('-1', '10', '7', '20') // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep
    configure gerritParameters ('refs/head/master')
    configure gerritConfigurations(gerritrepo)
    configure gerritTrigger (gerritrepo)
    configure artifactArchiver ('build/exe/*,build/installer/*,build/distributions/*')
    configure artifactFingerprinter ()
    //configure otherConfigurations ('40', 'false', 'windows') // quietPeriod, canRoam
    //configure gradleSetup ('build')
    configure windowsComponent ()
    steps {
        gradle {
            useWrapper true
            makeExecutable false
            fromRootBuildScriptDir false
            tasks 'build'
        }
    }
}

//------------------------------------------------- API-TOOL-VERIFY ----------------------------------------//

job('api-tool-verify') {
    def gerritrepo = 'its/contrib/softcert-tool'
    description 'api-tool-verify'
    jdk ('default')
    //configure logRotation ('-1', '10', '7', '20') // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep
    configure gerritParameters ('refs/head/master')
    configure gerritConfigurations(gerritrepo)
    configure gerritTrigger (gerritrepo)
    configure artifactArchiver ('build/exe/*,build/installer/*')
    configure artifactFingerprinter ()
    configure windowsComponent ()
    //configure otherConfigurations ('40', 'false', 'windows') // quietPeriod, canRoam
    
}

//------------------------------------------- IDOD-ADAPTER ---------------------------------------------------//

job('idod-adapter') {
    def gerritrepo = 'idod/extras/adapter'
    description 'idod-adapter'
    jdk ('jdk8')
    //configure logRotation ('-1', '10', '7', '20') // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep
    configure gerritParameters ('refs/head/master')
    configure gerritConfigurations(gerritrepo)
    configure gerritTrigger (gerritrepo)
    configure artifactArchiver ('build/libs/*,build/distributions/*,**/build/libs/*,**/build/distributions/*')
    configure artifactFingerprinter ()
    //configure otherConfigurations ('40', 'false', 'windows') // quietPeriod, canRoam

    steps {
        gradle {
            useWrapper true
            makeExecutable false
            fromRootBuildScriptDir false
            tasks 'build'
        }
    }    
    
}

//------------------------------------------- IDOD-ADAPTER-PUBLISH ---------------------------------------------------//

job('idod-adapter-publish') {
    def gerritrepo = 'idod/extras/adapter'
    description 'idod-adapter'
    jdk ('jdk8')
    //configure logRotation ('-1', '10', '7', '20') // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep
    configure gerritParameters ('refs/head/master')
    configure gerritConfigurations(gerritrepo)
    configure gerritTrigger (gerritrepo)
    configure artifactArchiver ('build/libs/*,build/distributions/*,**/build/libs/*,**/build/distributions/*')
    configure artifactFingerprinter ()
    //configure otherConfigurations ('40', 'false', 'windows') // quietPeriod, canRoam

    steps {
        gradle {
            useWrapper true
            makeExecutable false
            fromRootBuildScriptDir false
            tasks 'build'
        }
    }    
    
}

//------------------------------------------- IDOD-UTIL-VERIFY ---------------------------------------------------//

job('idod-util-verify') {
    def gerritrepo = 'idod/java/util'
    description 'idod-util-verify'
    jdk ('linux-jdk8')
    //configure logRotation ('-1', '10', '7', '20') // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep
    configure gerritParameters ('refs/head/master')
    configure gerritConfigurations(gerritrepo)
    configure gerritTrigger (gerritrepo)
    configure artifactArchiver ('build/libs/*,build/distributions/*,**/build/libs/*,**/build/distributions/*')
    configure artifactFingerprinter ()
    //configure otherConfigurations ('40', 'false', 'windows') // quietPeriod, canRoam

    steps {
        gradle {
            useWrapper true
            makeExecutable false
            fromRootBuildScriptDir false
            tasks 'clean build --refresh-dependencies --no-daemon'
        }
    }    
    
}

//------------------------------------------- IDONDEMAND-CORE-RELEASE-JDK8---------------------------------------------------//

job('idondemand-core-release-jdk8') {
    def gerritrepo = 'idod/core'
    description 'idondemand-core-release-jdk8'
    jdk ('linux-jdk8')
    //configure logRotation ('-1', '10', '7', '20') // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep
    configure gerritParameters ('refs/head/master')
    configure gerritConfigurations(gerritrepo)
    configure gerritTrigger (gerritrepo)
    configure artifactArchiver ('**/build/libs/*,**/build/distributions/*')
    configure artifactFingerprinter ()
    //configure otherConfigurations ('40', 'false', 'linux') // quietPeriod, canRoam

    steps {
        gradle {
            useWrapper true
            makeExecutable false
            fromRootBuildScriptDir false
            tasks 'clean idondemandpackage:buildRpms'
        }
    }    
    
}



