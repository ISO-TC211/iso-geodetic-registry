#!/usr/bin/env bash

set -e

mvn -B -U \
    -Dmaven.test.skip=true \
    -Dproject.build.sourceEncoding=UTF-8 \
    "-Dregistry.schema.xsd=src/iso-registry-api/src/main/resources/schema/service.xsd" \
    "-Dregistry.schema.bindingFile=src/iso-registry-api/src/main/resources/schema/bindings.xjb" \
    clean compile package

mkdir -p dist
cp -Rf src/iso-registry-client/target/*.war dist
