#!/bin/bash

echo "DOCKER_TAG= ${DOCKER_TAG}"

echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin ${CI_REGISTRY}

docker push ${CI_REGISTRY_IMAGE}