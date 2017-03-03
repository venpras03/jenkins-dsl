package lib

class JobHelper {
    static Closure switchOnOrOff(String value) {
        return {
            it / 'properties' / 'com.example.Test' {
                'switch'(value)
            }
        }
    }
}

