#!/bin/bash
set -e

cd /home/ubuntu/app

echo "Stopping existing containers..."
docker-compose down || true

echo "Pulling latest images..."
docker-compose pull

echo "Starting containers..."
docker-compose up -d
