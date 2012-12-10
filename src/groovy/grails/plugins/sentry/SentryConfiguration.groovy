package grails.plugins.sentry

import java.net.URL

class SentryConfiguration {

    String dsn
    String host
    String protocol
    String publicKey
    String secretKey
    String path
    String projectId
    String clientVersion
    Integer port
    boolean active
    String serverName
    String platform = 'groovy'

    SentryConfiguration(Map options = [:]) throws SentryException {
        options.each { k,v -> if (this.hasProperty(k)) { this."$k" = v} }

        URL url = new URL(dsn)
        this.protocol = url.protocol
        this.host = url.host

        String userInfo = url.userInfo
        String[] auth = userInfo.split(':')
        this.secretKey = auth[1]
        this.publicKey = auth[0]

        this.port = url.port

        String[] path = url.path.split('/')
        this.projectId = path[-1]
        this.path = path[0..-2].join('/')
    }

    String getSentryURL() {
        String portStr = ([0, 80, -1].contains(this.port) ? '' : this.port.toString())

        return "$protocol://$host$portStr$path/api/store/"
    }

    URL getEndpoint() {
        return new URL(getSentryURL())
    }

    String getServerName() {
        if (serverName)
            return serverName

        String hostname = InetAddress.localHost?.canonicalHostName
        return hostname?: 'undefined'
    }
}
