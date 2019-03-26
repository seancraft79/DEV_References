# DotNet Core Docker-compose file

```
version: '3.4'

services:
  dotnetdockertest:
    image: ${DOCKER_REGISTRY}dotnetdockertest
    build:
      context: .
      dockerfile: DotNetDockerTest/Dockerfile

  mysql:
    container_name: seanmysql
    build: ./Docker-Mysql/.
    command: --default-authentication-plugin=mysql_native_password
    restart: always
    ports:
      - '3306:3306'
```