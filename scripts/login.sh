#!/usr/bin/env bash
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 087113996847.dkr.ecr.us-east-1.amazonaws.com