#!/bin/bash

# JADE Car Simulation - Quick Deploy Script
# Usage: ./deploy.sh [local|build|up|down|logs]

set -e

case "$1" in
    "local")
        echo "🏗️  Building JAR locally..."
        ./gradlew build
        echo "✅ JAR built: $(ls -lh build/libs/*.jar)"
        echo "🚀 To run locally: java -jar build/libs/project-1.0.0.jar"
        ;;
    
    "build")
        echo "🐳 Building Docker image..."
        docker build -t jade-car-simulation .
        echo "✅ Docker image built successfully!"
        ;;
    
    "up")
        echo "🚀 Starting JADE Car Simulation..."
        docker-compose up --build -d
        echo "✅ Application started!"
        echo "🌐 Access at: http://localhost:6080"
        echo "📊 View logs: ./deploy.sh logs"
        ;;
    
    "down")
        echo "🛑 Stopping application..."
        docker-compose down
        echo "✅ Application stopped!"
        ;;
    
    "logs")
        echo "📊 Viewing application logs..."
        docker-compose logs -f jade-simulation
        ;;
    
    *)
        echo "🎯 JADE Car Simulation - Deploy Script"
        echo ""
        echo "Usage: $0 [command]"
        echo ""
        echo "Commands:"
        echo "  local  - Build JAR for local testing"
        echo "  build  - Build Docker image"
        echo "  up     - Start application with Docker"
        echo "  down   - Stop application"
        echo "  logs   - View application logs"
        echo ""
        echo "Quick start:"
        echo "  $0 up    # Start the application"
        echo "  $0 logs  # View logs"
        echo "  $0 down  # Stop when done"
        echo ""
        echo "🌐 Once running, visit: http://localhost:6080"
        ;;
esac