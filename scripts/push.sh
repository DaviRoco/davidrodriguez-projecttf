#!/usr/bin/env bash
docker build --build-arg JAR_FILE=build/libs/demo-0.0.1-SNAPSHOT.jar -t main-project .
docker tag main-project:latest 087113996847.dkr.ecr.us-east-1.amazonaws.com/main-project:latest
docker push 087113996847.dkr.ecr.us-east-1.amazonaws.com/main-project:latest