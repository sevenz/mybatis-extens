#!/usr/bin/env bash

mvn clean package 

cd ./generator-extens/target/

mvn deploy:deploy-file -DgroupId=bob-mybatis-extens -DartifactId=generator-extens -Dversion=0.0.3 -Dpackaging=jar -Dfile=./generator-extens-0.0.3.jar -DrepositoryId=xxx-releases -Durl=http://192.168.17.252:8081/nexus/content/repositories/releases/

#mvn deploy:deploy-file -DgroupId=bob-mybatis-extens -DartifactId=generator-extens -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar -Dfile=./generator-extens-0.0.1.jar -DrepositoryId=xxx-snapshots -Durl=http://192.168.17.252:8081/nexus/content/repositories/snapshots/