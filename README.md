EasyBuggy
=

EasyBuggy is a broken web application in order to understand behavior of bugs and vulnerabilities, for example, memory leak, dead lock, JVM crash, SQL injection and so on.

Quick Start:
-

    $ mvn clean install exec:exec

or

```bash
java -Xmx256m -XX:MaxPermSize=64m -XX:MaxDirectMemorySize=90m -XX:+UseSerialGC -Xloggc:logs/gc.log -XX:+PrintHeapAtGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=10M -XX:GCTimeLimit=15 -XX:GCHeapFreeLimit=50 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=logs/ -XX:ErrorFile=logs/hs_err_pid%p.log -XX:NativeMemoryTracking=summary -agentlib:jdwp=transport=dt_socket,server=y,address=9009,suspend=n -Dderby.stream.error.file=logs/derby.log -Dderby.infolog.append=true -Dderby.language.logStatementText=true -Dderby.locks.deadlockTrace=true -Dderby.locks.monitor=true -Dderby.storage.rowLocking=true -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=7900 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -jar easybuggy.jar 
```

Access to:

    http://localhost:8989/main

## To stop:

  use <kbd>CTRL</kbd>+<kbd>C</kbd>

    
To develop
-
   
See [the wiki page](https://github.com/k-tamura/easybuggy/wiki#to-develop-on-eclipse).

