#!/usr/bin/env bash

./gradlew clean build generatePomFiles bintrayUpload -PbintrayUser=$bintrayUser -PbintrayKey=$bintrayKey -PdryRun=false
