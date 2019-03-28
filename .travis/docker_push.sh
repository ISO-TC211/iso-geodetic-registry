#!/bin/bash

echo "DOCKER_TAG= ${DOCKER_TAG}"

echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin registry.gitlab.com

docker push ${DOCKER_TAG}