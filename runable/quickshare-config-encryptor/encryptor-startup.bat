echo off

cd /D %~dp0

java -Djava.ext.dirs=lib;config;%JAVA_HOME%/jre/lib;%JAVA_HOME%/jre/lib/ext;../../jre1.8.0_191/lib;../../jre1.8.0_191/lib/ext -jar lib/quickshare-config-encryptor.jar

pause