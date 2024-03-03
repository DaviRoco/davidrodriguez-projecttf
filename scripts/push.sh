#!/usr/bin/env bash
docker build --build-arg JAR_FILE=build/libs/davidrodriguez-projecttf-0.0.1-SNAPSHOT.jar -t davidrodriguez-projecttf .
docker tag davidrodriguez-projecttf:latest 087113996847.dkr.ecr.us-east-1.amazonaws.com/davidrodriguez-projecttf:latest
docker push 087113996847.dkr.ecr.us-east-1.amazonaws.com/davidrodriguez-projecttf:latest