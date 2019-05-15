### Most used docker commands

`docker images` - list all images  
`docker ps -a` - list all containers, incluing stopped ones  
`docker start / stop container-name`  
`docker exec -it container-name command` - e.g. `docker exec my-container bash`  
`docker run image-name command` - create a new container and run a command in it. E.g.:
  - `docker run --name clubheal-local-db -p 3306:3306 -v $(pwd)/db_data/:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=adminpwd -d mysql:8`

### Steps to dockerize a node.js service

https://nodejs.org/de/docs/guides/nodejs-docker-webapp/
