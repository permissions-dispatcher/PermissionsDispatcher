#!/usr/bin/env bash

./gradlew clean bintrayUpload -PbintrayUser=$bintrayUser -PbintrayKey=$bintrayKey -PdryRun=false
