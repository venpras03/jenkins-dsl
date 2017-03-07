
import static helpers.JobHelper.gerritconfig

job('idod-adapter') {
    description 'Build and test the app.'

    jdk ('jdk8')

    configure gerritConfigurations('b4b11ae3-8b97-4ea4-955e-478d2b93d478')
}