#!/bin/bash

docker rm -f currency-exchange-frontend-nginx
docker run -d --name currency-exchange-frontend-nginx -p 80:80 -v //c/Users/Vladislav/currency-exchange-frontend:/usr/share/nginx/html nginx