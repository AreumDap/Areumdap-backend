#!/bin/bash
set -e

echo "Docker cleanup start"

docker image prune -af
docker container prune -f
docker network prune -f

echo "Docker cleanup done"
