FROM maven:3-jdk-8-alpine

ENV PARAMS "-p aws -b git_branch"

RUN apk update && \
    # install awscli
    apk add --no-cache --virtual .build-deps && \
    apk add bash && \
    apk add make && \
    apk add curl && \
    apk add openssh && \
    apk -Uuv add groff less python py-pip && \
    pip install awscli && \
    apk --purge -v del py-pip && \
    # install bash+git
    apk upgrade && \
    apk add bash git

# Set timezone to UTC by default
RUN ln -sf /usr/share/zoneinfo/Etc/UTC /etc/localtime
RUN rm /var/cache/apk/*

WORKDIR /app

COPY ./build.sh build.sh
RUN chmod +x build.sh

CMD bash build.sh ${PARAMS}

#RUN aws s3 cp /app/build.sh s3://my-bucket/


#aws s3 mb s3://bucket-name
#aws s3 cp file.txt s3://my-bucket/
