#!/usr/bin/env bash
docker run \
  --env SPRING_PROFILES_ACTIVE=local \
  --env AWS_ACCESS_KEY_ID=AKIARISDGDIXYEPNWQVM \
  --env AWS_SECRET_ACCESS_KEY=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 \
  --env AWS_REGION=us-east-1 \
  -p 8080:8080 \
  main-project