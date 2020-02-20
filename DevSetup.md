# Development Setup Instructions

## Dependencies

- JDK 7
- Set Maven Ops
  - export MAVEN_OPTS="-Xmx512m -XX:MaxPermSize=350m"
- Postgres 9.4
  - newer versions (i.e. 12) of Postgres do not work, issues with UUID fields and enviornment variables
- Add db to host file
  - db must point to 127.0.0.1
