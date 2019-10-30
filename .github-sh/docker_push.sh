#!/bin/bash

echo "DOCKER_TAG= ${DOCKER_TAG}"

echo ${DOCKER_LOGIN_PASSWORD} | docker login -u ${DOCKER_LOGIN_USERNAME} --password-stdin ${CI_REGISTRY}

docker push ${CI_REGISTRY_IMAGE}