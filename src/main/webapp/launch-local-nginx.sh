#!/bin/bash

docker rm -f currency-exchange-frontend-nginx
docker run -d --name currency-exchange-frontend-nginx -p 80:80 -v $(pwd)/currency-exchange-frontend:/usr/share/nginx/html nginx
