package lib

def myClosure =
    {nodaysToKeep, nonumToKeep, noartifactDaysToKeep, noartifactNumToKeep ->
        logRotator {
        daysToKeep(nodaysToKeep)
        numToKeep(nonumToKeep)
        artifactDaysToKeep(noartifactDaysToKeep)
        artifactNumToKeep(noartifactNumToKeep)
    }
}

