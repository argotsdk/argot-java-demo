Argot Websocket Demo
============================

* Java / Tomcat / Argot on the server side with JSR356 web sockets
* Javascript / Argot on the client side with socket.io

The Java server encodes data and sends it through to connected clients that can decode and display.

## Usage

Java builds are managed through Gradle, get it if you haven't already.

```shell
# export GRADLE_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
gradle tomcatRunWar
```

And then browse to `http://localhost:8080/argot-java-demo/index.html`.


