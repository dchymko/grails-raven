# Raven-Grails

[![Build Status](https://secure.travis-ci.org/agorapulse/grails-raven.png?branch=master)](https://travis-ci.org/agorapulse/grails-raven)

Raven is a Grails client for integrating apps with [Sentry](http://www.getsentry.com). [Sentry](http://www.getsentry.com) is an event logging platform primarily focused on capturing and aggregating exceptions.

## Installation

Add the following to your `BuildConfig.groovy`:

```groovy
plugins {
    compile ":raven:0.5.8"
}
```

## Configuration

You need to provide your Sentry DSN in `Config.groovy` file. The plugin will sent notifications to Sentry by default, if you want to disable notifications for an specific environment set the active option as false.

```groovy
grails.plugins.raven.dsn = "https://{PUBLIC_KEY}:{SECRET_KEY}@app.getsentry.com/{PATH}{PROJECT_ID}"

environments {
    test {
        grails.plugins.raven.active = false
    }
    development {
        grails.plugins.raven.active = false
    }
    production {
    }
}
```

To set the current user data to be included in the logged messages sent to Sentry use the method `ravenClient.setUserData` passing a Map containing the user data. The only key that is mandatory is is\_authenticated. If you are using SpringSecurity in your application, you can get the current user using the method `springSecurityService.currentUser`. Below an example of Grails filter to set the user data:

```groovy
import grails.plugins.raven.RavenClient
import grails.util.Holders

class SentryFilters {
    def ravenClient
    def springSecurityService

    def filters = {
        all(uri: '/**') {
            before = {
                if (springSecurityService.isLoggedIn()) {
                    def user = springSecurityService.currentUser
                    def userData = [id: user.id, is_authenticated: true, email: user.email, username: user.username, appVersion: Holders.grailsApplication.metadata['app.version']]
                    ravenClient.setUserData(userData)
                } else {
                    ravenClient.setUserData([is_authenticated:false])
                }
            }
        }
    }
}
```

You can also set the server name, but it is recommended to don't set this configuration and let the plugin to resolve it.

```groovy
grails.plugins.raven.serverName = 'dev.server.com'
```

## Usage

### Log4j Appender

The Log4j Appender is automatically configured by plugin, you have just to set enabled environments in `Config.groovy` file as shown in Configuration section. All application exceptions will be logged on sentry by the appender. The appender is configured to log just the ERROR, WARN and FATAL levels. To log manually just use the `log.error` method.

### ravenClient

You also can use `ravenClient` to sent info messages to Sentry:

```groovy
import grails.plugins.raven.RavenClient

def ravenClient

ravenClient.captureMessage(String message)
ravenClient.captureMessage(String message, String loggerClass, String logLevel)
ravenClient.captureException(Throwable exception)
ravenClient.captureException(Throwable exception, String loggerClass, String logLevel, HttpServletRequest request)
```

# Latest releases

* 2014-03-08 **V0.5.8** : PR by jglapa for improved user data handling + bug fix
* 2014-03-05 **V0.5.7** : PR by Logicopolis for async execution
* 2014-01-03 **V0.5.4** : PR by benorama to remove commons codec Base64 dependency when building message body (for Grails 2.3 compatibility)
* 2012-12-12 **V0.5** : refactoring
* 2012-12-10 **V0.4** : sentryClient Spring Bean
* 2012-11-23 **V0.2** : user interface + bug fixes
* 2012-10-29 **V0.1** : initial release

# Bugs

To report any bug, please use the project [Issues](http://github.com/agorapulse/grails-raven/issues) section on GitHub.