#!/bin/bash
set -e

APP_DIR=/home/ubuntu/app

echo "Stopping containers with docker-compose..."

cd $APP_DIR

docker-compose down || true

echo "Containers stopped."
