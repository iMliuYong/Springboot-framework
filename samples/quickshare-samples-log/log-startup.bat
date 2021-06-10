cd /D %~dp0

java -Djava.ext.dirs=lib;config -jar lib/quickshare-app-starter.jar --server.port=19001

pause