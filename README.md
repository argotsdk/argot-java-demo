Argot Websocket Demo
============================

* Java / Tomcat / Argot on the server side with JSR356 web sockets
* Javascript / Argot on the client side with websockets

The Java server encodes data and sends it through to connected clients that can decode and display.

## Usage

Java builds are managed through Gradle, and the web dependencies are managed through bower.

To pull in the web dependencies you need npm and bower,

```shell
npm install bower
node_modules/bower/bin/bower install
```

This will copy some javascripts into the src/main/webapp folder. And then, run through gradle,

```shell
# export GRADLE_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
gradle tomcatRunWar
```

And finally, browse to `http://localhost:8080/argot-java-demo`.


