#!/usr/bin/env bash
docker build --build-arg JAR_FILE=build/libs/demo-0.0.1-SNAPSHOT.jar -t main-project .