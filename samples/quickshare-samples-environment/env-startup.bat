cd /D %~dp0

java.exe -Djava.ext.dirs=lib;config -jar lib/quickshare-app-starter.jar --server.port=19005 --logging.console.enabled=true

pause