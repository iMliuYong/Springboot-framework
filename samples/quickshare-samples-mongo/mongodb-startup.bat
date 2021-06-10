cd /D %~dp0

java -Djava.ext.dirs="lib;config;%JAVA_HOME%\jre\lib\ext" -jar lib/quickshare-app-starter.jar --server.port=19006 --logging.console.enabled=true

pause