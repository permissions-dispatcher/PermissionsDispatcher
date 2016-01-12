#!/usr/bin/env bash

# You have to set $bintrayUser and $bintrayKey before run this script.
set -eu

../gradlew clean build generatePomFileForMavenPublication bintrayUpload -PbintrayUser=$bintrayUser -PbintrayKey=$bintrayKey -PdryRun=true

../gradlew clean build generatePomFileForMavenPublication bintrayUpload -PbintrayUser=$bintrayUser -PbintrayKey=$bintrayKey