
import static helpers.JobHelper.*

job('idod-adapter') {
    def artifacts = 'build/libs/*,build/distributions/*,**/build/libs/*,**/build/distributions/*'
    String[] gerritparams = ['idod/extras/adapter', 'verify']    
    String[] logConfigs = ['14', '40', '7', '20'] // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'linux'] // quietPeriod, machine
    String[] gradleConfigs = ['clean build', ''] // tasks, switches

    
    jdk ('linux-jdk8')
    configure logRotation (logConfigs)   
    configure gerritParameters ('refs/heads/master')
    configure gerritConfigurations(gerritparams)
    configure gerritTrigger (gerritparams)
    configure artifactArchiver (artifacts)
    configure artifactFingerprinter ('')
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs)
}

//------------------------------------------------- API-TOOL-PUBLISH ----------------------------------------//

job('api-tool-publish') {
    def artifacts = 'build/exe/*,build/installer/*,build/distributions/*'    
    String[] gerritparams = ['its/contrib/softcert-tool', 'publish']
    String[] logConfigs = ['-1', '10', '7', '20'] // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'windows'] // quietPeriod, machine
    String[] gradleConfigs = ['build', ''] // tasks, switches

    jdk ('default')
    configure logRotation (logConfigs)
    configure gerritParameters ('refs/heads/master')
    configure gerritConfigurations(gerritparams)
    configure gerritTrigger (gerritparams)
    configure artifactArchiver (artifacts)
    configure artifactFingerprinter ('')
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs)
    configure windowsComponent ()
}

//------------------------------------------------- API-TOOL-VERIFY ----------------------------------------//

job('api-tool-verify') {
    String[] gerritparams = ['its/contrib/softcert-tool', 'verify']
    def artifacts = 'build/exe/*,build/installer/*'
    String[] logConfigs = ['-1', '10', '7', '20'] // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'windows'] // quietPeriod, machine
    String[] gradleConfigs = ['build', ''] // tasks, switches

    jdk ('default')
    configure logRotation (logConfigs)
    configure gerritParameters ('refs/heads/master')
    configure gerritConfigurations(gerritparams)
    configure gerritTrigger (gerritparams)
    configure artifactArchiver (artifacts)
    configure artifactFingerprinter ('')
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs)
    configure windowsComponent ()
    
}

//------------------------------------------- IDOD-ADAPTER-PUBLISH ---------------------------------------------------//

job('idod-adapter-publish') {
    String[] gerritparams = ['idod/extras/adapter', 'publish']
    def artifacts = 'build/libs/*,build/distributions/*,**/build/libs/*,**/build/distributions/*'
    String[] logConfigs = ['-1', '10', '7', '20'] // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'windows'] // quietPeriod, machine
    String[] gradleConfigs = ['build', ''] // tasks, switches

    jdk ('jdk8')
    configure logRotation (logConfigs)
    configure gerritParameters ('refs/heads/master')
    configure gerritConfigurations(gerritparams)
    configure gerritTrigger (gerritparams)
    configure artifactArchiver (artifacts)
    configure artifactFingerprinter ('')
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs)
}

//------------------------------------------- IDOD-UTIL-VERIFY ---------------------------------------------------//

job('idod-util-verify') {
    String[] gerritparams = ['idod/java/util', 'verify']
    def artifacts = '**/*.jar'
    String[] logConfigs = ['14', '40', '-1', '-1'] // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'linux'] // quietPeriod, machine
    String[] gradleConfigs = ['clean build', '--refresh-dependencies --no-daemon'] // tasks, switches
    String fingerprintFile = "**/*.jar"
    String testReportPath = "**/build/test-results/*.xml"

    jdk ('linux-jdk8')
    configure logRotation (logConfigs)
    configure gerritParameters ('refs/heads/master')
    configure gerritConfigurations(gerritparams)
    configure gerritTrigger (gerritparams)
    configure artifactArchiver (artifacts)
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs)  
    configure testReportJUnit (testReportPath)    
}

//------------------------------------------- IDOD-UTIL-PUBLISH ---------------------------------------------------//

job('idod-util-publish  ') {
    String[] gerritparams = ['idod/java/util', 'publish']
    def artifacts = '**/build/libs/*'
    String[] logConfigs = ['14', '40', '7', '20']// daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'linux'] // quietPeriod, machine
    String[] gradleConfigs = ['clean build upload', '--refresh-dependencies'] // tasks, switches
    String otherprojects = "its-bouncer-publish"
    String testReportPath = "**/build/test-results/*.xml"

    jdk ('linux-jdk8')
    configure logRotation (logConfigs)
    configure gerritParameters ('refs/heads/master')
    configure gerritConfigurations (gerritparams)
    configure gerritTrigger (gerritparams)
    configure artifactArchiver (artifacts)
    configure artifactFingerprinter ('')
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs)  
    configure buildOtherProjects (otherprojects)
    configure testReportJUnit (testReportPath)
}

//------------------------------------------- IDONDEMAND-CORE-VERIFY-JDK8 ---------------------------------------------------//

job('idod-core-verify-jdk8') {
    String[] gerritparams = ['idod/core', 'verify']
    def artifacts = '**/build/test-results/test/*.xml'
    String[] logConfigs = ['14', '40', '-1', '-1'] // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'linux'] // quietPeriod, machine
    String[] gradleConfigs = ['clean build buildRpms', '--refresh-dependencies -Pidod.integrate -Poracle  --stacktrace --no-daemon'] // tasks, switches
    String testReportPath = "**/build/test-results/*.xml"

    jdk ('linux-jdk8')
    displayName ('idOnDemand Core [JDK8/Verify]')
    configure logRotation (logConfigs)
    configure gerritParameters ('refs/heads/master')
    configure gerritConfigurations(gerritparams)
    configure gerritTrigger (gerritparams)
    configure artifactArchiver (artifacts)
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs)  
    configure testReportJUnit (testReportPath)    
}

//------------------------------------------- IDONDEMAND-CORE-RELEASE-JDK8---------------------------------------------------//

job('idondemand-core-release-jdk8') {
    String[] gerritparams = ['idod/core', 'publish']
    def artifacts = '**/build/libs/*,**/build/distributions/*'
    String[] logConfigs = ['14', '40', '7', '20'] // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'linux'] // quietPeriod, machine
    String[] gradleConfigs = ['build', ''] // tasks, switches

    jdk ('linux-jdk8')
    displayName ('idOnDemand Core [JDK8/Release]')
    configure logRotation (logConfigs)
    configure gerritParameters ('refs/heads/master')
    configure gerritConfigurations(gerritparams)
    configure gerritTrigger (gerritparams)
    configure artifactArchiver (artifacts)
    configure artifactFingerprinter ('')
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs) 
}

//------------------------------------------- IDONDEMAND-WEBCONTROLS-CORE-VERIFY---------------------------------------------------//

job('iwc-verify') {
    String[] gerritparams = ['idod/webcontrols/core', 'verify']
    String[] logConfigs = ['14', '40', '-1', '-1'] // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'windows'] // quietPeriod, machine
    String[] gradleConfigs = ['build', '--stacktrace --refresh-dependencies'] // tasks


    jdk ('default')
    displayName ('idOnDemand Webcontrols Core [Verify]')    
    configure logRotation (logConfigs)
    configure gerritParameters ('refs/heads/master')
    configure gerritConfigurations(gerritparams)
    configure gerritTrigger (gerritparams)
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs) 
    //configure executeShell ("""git clean -fdx && git reset --hard HEAD echo WIX_ROOT_DIR: \$WIX_ROOT_DIR""")

    configure {project ->
            project / publishers << 'com.chikli.hudson.plugin.naginator.NaginatorPublisher' (plugin:"naginator@1.9") {
            'regexpForRerun' ''
            'rerunIfUnstable' 'false'
            'checkRegexp' 'false'
            'delay' (class:"com.chikli.hudson.plugin.naginator.FixedDelay") {
                'delay' '300'
            }
            'maxSchedule' '1'
        }
    }   
}

//------------------------------------------- IDONDEMAND-WEBCONTROLS-CORE-PUBLISH ---------------------------------------------------//

job('iwc-publish') {
    String[] gerritparams = ['idod/webcontrols/core', 'publish']
    def artifacts = '**/*.zip,**/*.msi,**/*.jar,**/*.dll,**/*.exe'
    String[] logConfigs = ['14', '40', '7', '20'] // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'windows'] // quietPeriod, machine
    String[] gradleConfigs = ['build upload', '--stacktrace --refresh-dependencies -Denvironment=deployment'] // tasks
    String otherprojects = "iwli-publish, iws-publish, iwue-publish"

    jdk ('jkd6')
    displayName ('idOnDemand Webcontrols Core [Publish]')
    configure logRotation (logConfigs)
    configure gerritParameters ('refs/heads/master')
    configure gerritConfigurations(gerritparams)
    configure gerritTrigger (gerritparams)
    configure artifactArchiver (artifacts)    
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs)
    configure buildOtherProjects (otherprojects) 
    configure artifactFingerprinter ('')    
    //configure executeShell ("""git clean -fdx && git reset --hard HEAD echo WIX_ROOT_DIR: \$WIX_ROOT_DIR""")
 
}

//------------------------------- IDONDEMAND-WEBCONTROLS-LOCAL-ISSUANCE-VERIFY --------------------------------------------------//

job('iwli-verify') {
    String[] gerritparams = ['idod/webcontrols/local-issuance', 'verify']
    String[] logConfigs = ['14', '40', '-1', '-1']// daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'windows'] // quietPeriod, machine
    String[] gradleConfigs = ['clean build', '--stacktrace --refresh-dependencies'] // tasks
    
    jdk ('jdk6')
    displayName ('idOnDemand Webcontrols Local Issuance [Verify]')      
    configure logRotation (logConfigs)
    configure gerritParameters ('refs/heads/master')
    configure gerritConfigurations(gerritparams)
    configure gerritTrigger (gerritparams)
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs)
}

//------------------------------- IDONDEMAND-WEBCONTROLS-LOCAL-ISSUANCE-PUBLISH --------------------------------------------------//

job('iwli-publish') {
    String[] gerritparams = ['idod/webcontrols/local-issuance', 'publish']
    def artifacts = 'idod-printctrl/Release/*.dll,idod-printctrl/target/classes/main/printctrl/*.msi'
    String[] logConfigs = ['14', '40', '7', '20'] // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'windows'] // quietPeriod, machine
    String[] gradleConfigs = ['build upload', '--stacktrace --refresh-dependencies -Denvironment=deployment'] // tasks
    String otherprojects = 'idondemand-core-release-jdk8'

    jdk ('default')
    displayName ('idOnDemand Webcontrols Local Issuance [Publish]')    
    configure logRotation (logConfigs)
    configure gerritParameters ('refs/heads/master')
    configure gerritConfigurations(gerritparams)
    configure gerritTrigger (gerritparams)
    configure artifactArchiver (artifacts)    
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs)
    configure buildOtherProjects (otherprojects)    
    configure artifactFingerprinter ('')    
 
}

//------------------------------- IDONDEMAND-WEBCONTROLS-SMARTCARD-VERIFY--------------------------------------------------//

job('iws-verify') {
    String[] gerritparams = ['idod/webcontrols/smartcard', 'verify']
    def artifacts = '**/*.zip,**/*.msi,**/*.jar,**/*.dll,**/*.exe'
    String[] logConfigs = ['14', '40', '-1', '-1'] // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'windows'] // quietPeriod, machine
    String[] gradleConfigs = ['clean build', '--stacktrace --refresh-dependencies'] // tasks
    String otherprojects = 'iwli-publish'

    jdk ('default')
    displayName ('idOnDemand Webcontrols Smartcard [Verify]')    
    configure logRotation (logConfigs)
    configure gerritParameters ('refs/heads/master')
    configure gerritConfigurations(gerritparams)
    configure gerritTrigger (gerritparams)
    configure artifactArchiver (artifacts)    
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs)
    configure buildOtherProjects (otherprojects)    
    configure artifactFingerprinter ('')    
 
}

//------------------------------- IDONDEMAND-WEBCONTROLS-SMARTCARD-PUBLISH--------------------------------------------------//

job('iws-publish') {
    String[] gerritparams = ['idod/webcontrols/smartcard', 'publish']
    def artifacts = '**/*.zip,**/*.msi,**/*.jar,**/*.dll,**/*.exe'
    String[] logConfigs = ['14', '40', '7', '20'] // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'windows'] // quietPeriod, machine
    String[] gradleConfigs = ['clean build upload', '--stacktrace --refresh-dependencies -Denvironment=deployment'] // tasks
    String otherprojects = 'iwli-publish'

    jdk ('default')
    displayName ('idOnDemand Webcontrols Smartcard [Publish]') 
    configure logRotation (logConfigs)
    configure gerritParameters ('refs/heads/master')
    configure gerritConfigurations(gerritparams)
    configure gerritTrigger (gerritparams)
    configure artifactArchiver (artifacts)    
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs)
    configure buildOtherProjects (otherprojects)    
    configure artifactFingerprinter ('')    
 
}

//------------------------------- IDONDEMAND-WEBCONTROLS-USER-ENROLLMENT-VERIFY--------------------------------------------------//

job('iwue-verify') {
    String[] gerritparams = ['idod/webcontrols/user-enrollment', 'verify']
    def artifacts = 'idod-webcontrols/Release/*.dll,idod-webcontrols/target/classes/main/webcontrols/*.msi'
    String[] logConfigs = ['14', '40', '-1', '-1']// daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'windows'] // quietPeriod, machine
    String[] gradleConfigs = ['clean build', '--stacktrace --refresh-dependencies'] // tasks

    jdk ('jdk6')
    displayName ('idOnDemand Webcontrols User Enrollment [Verify]')    
    configure logRotation (logConfigs)
    configure gerritParameters ('refs/heads/master')
    configure gerritConfigurations(gerritparams)
    configure gerritTrigger (gerritparams)
    configure artifactArchiver (artifacts)    
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs)
}

//------------------------------- IDONDEMAND-WEBCONTROLS-USER-ENROLLMENT-PUBLISH--------------------------------------------------//

job('iwue-publish') {
    String[] gerritparams = ['idod/webcontrols/user-enrollment', 'publish']
    def artifacts = 'idod-webcontrols/Release/*.dll,idod-webcontrols/target/classes/main/webcontrols/*.msi'
    String[] logConfigs = ['14', '40', '7', '20'] // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'windows'] // quietPeriod, machine
    String[] gradleConfigs = ['clean build upload', '--stacktrace --refresh-dependencies -Denvironment=deployment -p idod-webcontrols'] // tasks

    jdk ('default')
    displayName ('idOnDemand Webcontrols User Enrollment [Publish]')
    configure logRotation (logConfigs)
    configure gerritParameters ('refs/heads/master')
    configure gerritConfigurations(gerritparams)
    configure gerritTrigger (gerritparams)
    configure artifactArchiver (artifacts)    
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs)
}

//------------------------------------------ INTC-DEPLOY ----------------------------------------------------------------//

job('app-intc-deploy') {
    String[] gerritparams = ['idod/chef/instances/int', 'verify']
    def artifacts = '**/build/libs/*,**/build/distributions/*'
    String[] logConfigs = ['-1', '10', '7', '20'] // daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep 
    String[] otherConfigs = ['40', 'linux'] // quietPeriod, machine
    String[] gradleConfigs = ['build', ''] // tasks, switches

    jdk ('linux-jdk8')
    configure logRotation (logConfigs)
    configure gerritParameters ('refs/heads/intc')
    configure gerritConfigurations(gerritparams)
    configure gerritTrigger (gerritparams)
    configure artifactArchiver (artifacts)
    configure artifactFingerprinter ('')
    configure otherConfigurations (otherConfigs)
    configure gradleConfigurations (gradleConfigs) 
}


