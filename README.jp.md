EasyBuggy
=

EasyBuggyは、メモリリーク、デッドロック、JVMクラッシュ、SQLインジェクションなど、バグや脆弱性の動作を理解するためのバグだらけのWebアプリケーションです。

クイックスタート
-

    $ mvn clean install exec:exec

( または ``` java -jar easybuggy.jar ``` または 任意のサーブレットコンテナに ROOT.war をデプロイ )

```bash
$ java -Xmx256m -XX:MaxPermSize=64m -XX:MaxDirectMemorySize=90m -XX:+UseSerialGC -Xloggc:logs/gc.log -XX:+PrintHeapAtGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=10M -XX:GCTimeLimit=15 -XX:GCHeapFreeLimit=50 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=logs/ -XX:ErrorFile=logs/hs_err_pid%p.log -XX:NativeMemoryTracking=summary -agentlib:jdwp=transport=dt_socket,server=y,address=9009,suspend=n -Dderby.stream.error.file=logs/derby.log -Dderby.infolog.append=true -Dderby.language.logStatementText=true -Dderby.locks.deadlockTrace=true -Dderby.locks.monitor=true -Dderby.storage.rowLocking=true -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=7900 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -jar easybuggy.jar 
```

以下にアクセス:

    http://localhost:8989/main


停止するには:

  <kbd>CTRL</kbd>+<kbd>C</kbd>をクリック
  

開発するには
-
   
[wikiページ](https://github.com/k-tamura/easybuggy/wiki#to-develop-on-eclipse)を参照下さい。
