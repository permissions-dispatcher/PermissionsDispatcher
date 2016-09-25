#!/usr/bin/env bash

./gradlew clean build generatePomFileForMavenPublication :processor:bintrayUpload -PbintrayUser=$bintrayUser -PbintrayKey=$bintrayKey
./gradlew :library:bintrayUpload -PbintrayUser=$bintrayUser -PbintrayKey=$bintrayKey
