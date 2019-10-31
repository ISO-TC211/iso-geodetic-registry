#!/bin/bash

CI_REGISTRY_IMAGE="${CI_REGISTRY_IMAGE,,}"

docker build -t "${CI_REGISTRY_IMAGE}" .