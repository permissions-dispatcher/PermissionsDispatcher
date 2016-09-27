#!/usr/bin/env bash

./gradlew clean build generatePomFileForMavenPublication :processor:bintrayUpload -PbintrayUser=$bintrayUser -PbintrayKey=$bintrayKey -PdryRun=false
./gradlew :library:bintrayUpload -PbintrayUser=$bintrayUser -PbintrayKey=$bintrayKey -PdryRun=false
