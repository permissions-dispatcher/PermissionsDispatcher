#!/usr/bin/env bash

./gradlew clean build
./gradlew :processor:bintrayUpload
./gradlew :library:bintrayUpload
./gradlew :annotation:bintrayUpload
./gradlew :ktx:bintrayUpload