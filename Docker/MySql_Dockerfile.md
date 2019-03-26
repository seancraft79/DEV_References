# MySql Docker file

```
FROM mysql

ENV MYSQL_DATABASE=seandb \
    MYSQL_ROOT_HOST=% \
    MYSQL_ROOT_PASSWORD=tough2983 \
	COMPOSE_PROJECT_NAME=seanmysql

ADD ./sql-scripts/createtable.sql /docker-entrypoint-initdb.d

EXPOSE 3306
```