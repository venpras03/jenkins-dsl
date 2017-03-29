
import static helpers.JobHelper.*

job('idod-adapter') {
    def gerritrepo = 'idod/extras/adapter'
    def artifacts = 'build/libs/*,build/distributions/*,**/build/libs/*,**/build/distributions/*'
    String[] logConfigs = ['14', '40', '7', '20'] // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'true', 'windows'] // quietPeriod, canRoam, machine
    String[] gradleConfigs = ['build'] // tasks

    
    jdk ('jdk8')
    configure logRotation (logConfigs)   
    configure gerritParameters ('refs/head/master')
    configure gerritConfigurations(gerritrepo)
    configure gerritTrigger (gerritrepo)
    configure artifactArchiver (artifacts)
    configure artifactFingerprinter ()
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs)
}

//------------------------------------------------- API-TOOL-PUBLISH ----------------------------------------//

job('api-tool-publish') {
    def gerritrepo = 'its/contrib/softcert-tool'
    def artifacts = 'build/exe/*,build/installer/*,build/distributions/*'
    String[] logConfigs = ['-1', '10', '7', '20'] // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'false', 'windows'] // quietPeriod, canRoam, machine
    String[] gradleConfigs = ['build'] // tasks

    jdk ('default')
    configure logRotation (logConfigs)
    configure gerritParameters ('refs/head/master')
    configure gerritConfigurations(gerritrepo)
    configure gerritTrigger (gerritrepo)
    configure artifactArchiver (artifacts)
    configure artifactFingerprinter ()
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs)
    configure windowsComponent ()
}

//------------------------------------------------- API-TOOL-VERIFY ----------------------------------------//

job('api-tool-verify') {
    def gerritrepo = 'its/contrib/softcert-tool'
    def artifacts = 'build/exe/*,build/installer/*'
    String[] logConfigs = ['-1', '10', '7', '20'] // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'false', 'windows'] // quietPeriod, canRoam, machine
    String[] gradleConfigs = ['build'] // tasks

    jdk ('default')
    configure logRotation (logConfigs)
    configure gerritParameters ('refs/head/master')
    configure gerritConfigurations(gerritrepo)
    configure gerritTrigger (gerritrepo)
    configure artifactArchiver (artifacts)
    configure artifactFingerprinter ()
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs)
    configure windowsComponent ()
    
}

//------------------------------------------- IDOD-ADAPTER-PUBLISH ---------------------------------------------------//

job('idod-adapter-publish') {
    def gerritrepo = 'idod/extras/adapter'
    def artifacts = 'build/libs/*,build/distributions/*,**/build/libs/*,**/build/distributions/*'
    String[] logConfigs = ['-1', '10', '7', '20'] // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'false', 'windows'] // quietPeriod, canRoam, machine
    String[] gradleConfigs = ['build'] // tasks

    jdk ('jdk8')
    configure logRotation (logConfigs)
    configure gerritParameters ('refs/head/master')
    configure gerritConfigurations(gerritrepo)
    configure gerritTrigger (gerritrepo)
    configure artifactArchiver (artifacts)
    configure artifactFingerprinter ()
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs)
}

//------------------------------------------- IDOD-UTIL-VERIFY ---------------------------------------------------//

job('idod-util-verify') {
    def gerritrepo = 'idod/java/util'
    def artifacts = 'build/libs/*,build/distributions/*,**/build/libs/*,**/build/distributions/*'
    String[] logConfigs = ['-1', '10', '7', '20'] // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'false', 'linux'] // quietPeriod, canRoam, machine
    String[] gradleConfigs = ['build'] // tasks

    jdk ('linux-jdk8')
    configure logRotation (logConfigs)
    configure gerritParameters ('refs/head/master')
    configure gerritConfigurations(gerritrepo)
    configure gerritTrigger (gerritrepo)
    configure artifactArchiver (artifacts)
    configure artifactFingerprinter ()
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs)  
}

//------------------------------------------- IDONDEMAND-CORE-RELEASE-JDK8---------------------------------------------------//

job('idondemand-core-release-jdk8') {
    def gerritrepo = 'idod/core'
    def artifacts = '**/build/libs/*,**/build/distributions/*'
    String[] logConfigs = ['-1', '10', '7', '20'] // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'false', 'linux'] // quietPeriod, canRoam, machine
    String[] gradleConfigs = ['build'] // tasks

    jdk ('linux-jdk8')
    configure logRotation (logConfigs)
    configure gerritParameters ('refs/head/master')
    configure gerritConfigurations(gerritrepo)
    configure gerritTrigger (gerritrepo)
    configure artifactArchiver (artifacts)
    configure artifactFingerprinter ()
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs) 
}

//------------------------------------------ INTC-DEPLOY ----------------------------------------------------------------//

job('app-intc-deploy') {
    def gerritrepo = '  idod/chef/instances/int'
    def artifacts = '**/build/libs/*,**/build/distributions/*'
    String[] logConfigs = ['-1', '10', '7', '20'] // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'false', 'linux'] // quietPeriod, canRoam, machine
    String[] gradleConfigs = ['build'] // tasks

    jdk ('linux-jdk8')
    configure logRotation (logConfigs)
    configure gerritParameters ('refs/head/intc')
    configure gerritConfigurations(gerritrepo)
    configure gerritTrigger (gerritrepo)
    configure artifactArchiver (artifacts)
    configure artifactFingerprinter ()
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs) 
}


