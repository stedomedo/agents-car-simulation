# JADE Multi-Agent System - Petrol Station Simulation

## Overview

A multi-agent system simulation built with JADE (Java Agent DEvelopment Framework) that models interactions between autonomous car agents and petrol stations. Originally developed in 2004-2005, this project demonstrates agent-based modeling with real-time visualization.

![Agent Animation](agent_animated.gif)

A release on Github can be used directly with **Docker + Web VNC** to run the application locally in your browser without installation.
```bash
# Start the application (uses GitHub release JAR)
./deploy.sh up
```


## Quick Start

### Prerequisites
- **Java**: JDK 8+ (recommended: OpenJDK 11, 17, or 21)
- **Display**: GUI requires X11 (Linux/macOS) or Windows display

Example for MacOS setup:
```bash
# Install Java via Homebrew
brew install openjdk@11

# Add to PATH (add to ~/.zshrc)
export PATH="/opt/homebrew/opt/openjdk@11/bin:$PATH"
```


### Build and Run
```bash
# Using included Gradle wrapper (recommended)
./gradlew build
./gradlew run

# Or with system Gradle
gradle build
gradle run
```

For more options and a browser-based application see [Deployment](./DEPLOYMENT.md).


### Expected Output
- JADE platform initialization messages
- Agent creation confirmations (in German)
- GUI window with simulation map showing cars and petrol stations

## Project Structure

- **`agents/`**: Car and PetrolStation agent implementations
- **`behaviours/`**: Agent behavior definitions (searching, driving, fueling)
- **`gui/`**: Swing-based graphical interface
- **`libs/jade.jar`**: JADE 4.6.0 framework (included)

## Build System

Modern Gradle build system with:
- **Automated Dependencies**: JADE classpath handled automatically
- **Cross-Platform**: Works on Linux, macOS, Windows
- **IDE Integration**: IntelliJ IDEA, Eclipse, VS Code support

### Additional Commands
```bash
# Create executable JAR
./gradlew jar
java -cp "libs/jade.jar:build/libs/project-1.0.0.jar" main.Main

# Clean build
./gradlew clean build

# Headless mode (servers)
./gradlew run -Djava.awt.headless=true
```


## Development Notes

- **Framework**: JADE 4.6.0 for multi-agent systems
- **Communication**: ACL (Agent Communication Language)
- **GUI**: Java Swing with real-time agent visualization
- **Historical**: Early 2000s agent-based modeling example

