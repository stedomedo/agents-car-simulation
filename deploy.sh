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
    
    "release")
        echo "🚀 Starting JADE Car Simulation (Release Version)..."
        docker-compose -f docker-compose.release.yml up --build -d
        echo "✅ Application started using GitHub release JAR!"
        echo "🌐 Access at: http://localhost:6080"
        echo "📊 View logs: ./deploy.sh logs-release"
        ;;
    
    "build")
        echo "🐳 Building Docker image..."
        docker build -t jade-car-simulation .
        echo "✅ Docker image built successfully!"
        ;;
    
    "build-release")
        echo "🐳 Building Docker image (Release Version)..."
        docker build -f Dockerfile.release -t jade-car-simulation-release .
        echo "✅ Docker release image built successfully!"
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
        docker-compose -f docker-compose.release.yml down
        echo "✅ Application stopped!"
        ;;
    
    "logs")
        echo "📊 Viewing application logs..."
        docker-compose logs -f jade-simulation
        ;;
    
    "logs-release")
        echo "📊 Viewing release application logs..."
        docker-compose -f docker-compose.release.yml logs -f jade-simulation
        ;;
    
    *)
        echo "🎯 JADE Car Simulation - Deploy Script"
        echo ""
        echo "Usage: $0 [command]"
        echo ""
        echo "Commands:"
        echo "  local        - Build JAR for local testing"
        echo "  release      - Start using GitHub release JAR (recommended)"
        echo "  build        - Build Docker image from source"
        echo "  build-release- Build Docker image using release JAR"
        echo "  up           - Start application with Docker (from source)"
        echo "  down         - Stop application"
        echo "  logs         - View application logs"
        echo "  logs-release - View release application logs"
        echo ""
        echo "Quick start (using GitHub release):"
        echo "  $0 release      # Start using release JAR"
        echo "  $0 logs-release # View logs"
        echo "  $0 down         # Stop when done"
        echo ""
        echo "🌐 Once running, visit: http://localhost:6080"
        ;;
esac