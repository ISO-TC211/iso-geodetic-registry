#! /bin/bash

PROFILE=""
BRANCH="master"
S3BUCKET=""

for arg in "$@"
do
    if [ "$arg" == "--help" ] || [ "$arg" == "-h" ]
    then
        echo "Usage: [build.sh] -p {maven_profile_id} -b {git_branch_clone} -s3 {aws_s3_bucket_name}"
        echo "  -p  {maven_profile_id} (default: \"${PROFILE}\") - build with maven profile id"
        echo "  -b  {git_branch_clone} (default: \"${BRANCH}\") - git clone branch name"
        echo "  -s3 {aws_s3_bucket_name} (default: \"${S3BUCKET}\") - upload final_isoregistry.zip to s3-bucket used by aws-lambda-function"
        exit 0
    fi
done

BASEDIR="$(pwd)"
APPDIR="${BASEDIR}/git"
DISTDIR="${BASEDIR}/dist"

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

rm -Rf ${APPDIR}
mkdir -p ${APPDIR}

rm -Rf ${DISTDIR}
mkdir -p ${DISTDIR}

git clone \
    --depth=10 \
    --branch=${BRANCH} \
    --recurse-submodules \
    -j8 \
    https://github.com/ISO-TC211/iso-geodetic-registry.git ${APPDIR}

cd ${APPDIR}
mvn package -DskipTests ${PROFILE}
cd -

find -type f \( -name "*.war" -o -name "*.zip" \) | xargs -i cp -f {} ${DISTDIR}/
rm -f ${DISTDIR}/Titillium_Web.zip ## delete redundant files

if [ ! -z "${S3BUCKET}" ]; then
    echo "upload ${DISTDIR}/isoreg-lambda.zip to ${S3BUCKET}"
fi
