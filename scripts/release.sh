#!/usr/bin/env bash

./gradlew clean build generatePomFileForMavenPublication :processor:bintrayUpload -Puser=$bintrayUser -Pkey=$bintrayKey -PdryRun=false
./gradlew :library:bintrayUpload -Puser=$bintrayUser -Pkey=$bintrayKey -PdryRun=false
./gradlew :annotation:bintrayUpload -Puser=$bintrayUser -Pkey=$bintrayKey -PdryRun=false