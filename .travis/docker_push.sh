#!/bin/bash

echo "registry= ${REGISTRY}"

echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin registry.gitlab.com

docker push registry.gitlab.com/tc211/geodetic-registry-docker/web:${TRAVIS_BUILD_NUMBER}