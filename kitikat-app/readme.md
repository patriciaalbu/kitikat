# Start MySQL in docker container

```shell script
docker run --name mysql-server -p:3306:3306 -e MYSQL_DATABASE=test -e MYSQL_ROOT_PASSWORD=test -e MYSQL_USER=test -e MYSQL_PASSWORD=test -d mysql:5.7
```

If container is already created, start it with:

```shell script
docker start mysql-serrver
```

Investigate the db logs with (Ctrl+C to exit):

```shell script
docker logs -f mysql-serrver
```

To stop the container, use:

```shell script
docker stop mysql-serrver
```

To remove the container, stop it firt, then use:

```shell script
docker rm mysql-serrver
```

