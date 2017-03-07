
import static helpers.JobHelper.*

job('idod-adapter') {
    description 'Build and test the app.'

    jdk ('jdk8')

    configure gerritParameters ('refs/head/master')
    configure gerritConfigurations('b4b11ae3-8b97-4ea4-955e-478d2b93d478')
    configure gerritTrigger ('idod/extras/adapter')
}