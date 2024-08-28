#!/bin/bash

docker rm -f currency-exchange-frontend-nginx
docker container run -d --name currency-exchange-frontend-nginx -p 80:80 -v $(pwd):/usr/share/nginx/html nginx