#! /bin/bash

for arg in "$@"
do
    if [ "$arg" == "--help" ] || [ "$arg" == "-h" ]
    then
        echo "Usage: [build.sh] -p {maven_profile_id} -b {git_branch_clone}"
        echo "default:"
        echo "  -p: ''"
        echo "  -b: 'master'"
        exit 0
    fi
done

APPDIR="$(pwd)/app"
DISTDIR="$(pwd)/dist"

PROFILE=""
BRANCH="master"

while getopts p:b: option; do
case "${option}"
in
p) PROFILE=${OPTARG};;
b) BRANCH=${OPTARG};;
esac
done

if [ ! -z "${PROFILE}" ]; then
    PROFILE="-P${PROFILE}"
fi

rm -Rf ${APPDIR} && mkdir -p ${APPDIR}
rm -Rf ${DISTDIR} && mkdir -p ${DISTDIR}

git clone \
    --depth=10 \
    --branch=${BRANCH} \
    --recurse-submodules \
    -j8 \
    https://github.com/ISO-TC211/iso-geodetic-registry.git ${APPDIR}

cd app && mvn package -DskipTests ${PROFILE} && cd -
find -type f \( -name "*.war" -o -name "*.zip" \) | xargs -i cp -f {} ${DISDIR}/
rm -f ${DISDIR}/Titillium_Web.zip ## delete redundant files
