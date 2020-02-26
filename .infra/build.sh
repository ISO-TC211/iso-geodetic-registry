#! /bin/bash

APPDIR="$(pwd)/app"
DISTDIR="$(pwd)/dist"

rm -Rf ${APPDIR} && mkdir -p ${APPDIR}
rm -Rf ${DISTDIR} && mkdir -p ${DISTDIR}

git clone \
    --depth=10 \
    --branch=aws-lambda-api \
    --recurse-submodules \
    -j8 \
    https://github.com/ISO-TC211/iso-geodetic-registry.git ${APPDIR}

cd app && mvn package -DskipTests && cd -
find "${APPDIR}/src/" -type f \( -name "*.war" -o -name "*.zip" \) | xargs -i cp {} ${DISDIR}
