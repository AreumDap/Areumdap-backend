#!/bin/bash

CONTAINER_NAME=areumdap-backend

if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
  docker stop $CONTAINER_NAME || true
  docker rm $CONTAINER_NAME || true
fi

exit 0
