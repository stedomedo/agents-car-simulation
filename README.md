# JADE Car Simulation - Multi-Agent System

A Java-based car simulation using the JADE (Java Agent DEvelopment Framework) platform, developed in 2004-2005. This project demonstrates a multi-agent system where car agents navigate a city environment and interact with petrol station agents.

## Overview

This simulation features:
- **Car Agents**: Autonomous vehicles that navigate the city and require fuel
- **Petrol Station Agents**: Service stations that provide fuel to cars
- **Real-time GUI**: Visual representation of the city map with moving agents
- **Agent Communication**: JADE-based message passing between agents
- **Behavioral Patterns**: Complex agent interactions and decision-making

## Prerequisites

### Java Development Kit (JDK)
- **Required**: Java 8 or higher
- **Recommended**: OpenJDK 11, 17, or 21
- **Tested with**: OpenJDK 21

### JADE Framework
- The project includes `jade.jar` (JADE framework library)
- No additional JADE installation required

### Display Requirements
- **GUI Mode**: Requires X11 display server (Linux/macOS) or Windows display
- **Headless Mode**: Can run with virtual display (Xvfb on Linux)

## Installation & Setup

### 1. Clone or Download the Project
```bash
# If using git
git clone <repository-url>
cd <project-directory>

# Or extract from archive
unzip project.zip
cd project/
```

### 2. Verify Java Installation
```bash
java -version
javac -version
```

### 3. Project Structure
```
project/
├── sources/                # Source code directory
│   ├── agents/             # Agent implementations
│   ├── behaviours/         # Agent behaviors
│   ├── gui/               # GUI components
│   └── main/              # Main application class
├── pics/                   # Image resources
├── jade.jar               # JADE framework library
└── README.md              # This file
```

## Compilation

### 1. Create Build Directory
```bash
mkdir -p build
```

### 2. Copy Resources
```bash
# Copy image resources to build directory
cp -r pics build/
mkdir -p build/gui
cp -r pics build/gui/
```

### 3. Compile Java Sources
```bash
# Compile all Java files with JADE in classpath
find sources -name "*.java" -exec javac -cp jade.jar -d build {} +
```

**Note**: You may see deprecation warnings about `Integer(int)` constructor usage. These are harmless and don't affect functionality.

## Running the Application

### Standard GUI Mode (with display)
```bash
java -cp jade.jar:build main.Main
```

### Headless Mode (Linux/Unix)
If running on a server without display:
```bash
# Install virtual display (Ubuntu/Debian)
sudo apt-get install xvfb

# Start virtual display
Xvfb :99 -screen 0 1024x768x24 &

# Run application with virtual display
DISPLAY=:99 java -cp jade.jar:build main.Main
```

### Windows
```cmd
java -cp jade.jar;build main.Main
```

## Application Interface

Once running, you'll see:

### Main Window Components:
- **Stadtplan (City Map)**: Visual representation of the simulation environment
- **Programm Info**: Information panel showing system status
- **Tankstellen (Petrol Stations)**: List of fuel stations with status indicators
  - 🟢 Green: High fuel level
  - 🟡 Yellow: Medium fuel level  
  - 🔴 Red: Low fuel level
- **Nachrichten (Messages)**: System messages and agent communications

### Visual Elements:
- **Cars**: Moving vehicle agents (different orientations)
- **Petrol Stations**: Fuel service locations
- **Trees**: Environmental decorations
- **Roads**: Navigation paths for vehicles

## Troubleshooting

### Common Issues:

#### 1. "Could not find or load main class"
```bash
# Ensure classpath includes both jade.jar and build directory
java -cp jade.jar:build main.Main
```

#### 2. Image Loading Errors
```bash
# Verify pics directory is copied to build/gui/
ls -la build/gui/pics/
```

#### 3. Display Issues (Linux)
```bash
# For headless systems, ensure Xvfb is running
ps aux | grep Xvfb
export DISPLAY=:99
```

#### 4. Java Version Compatibility
- If using Java 9+, you may need to add JVM flags:
```bash
java --add-opens java.base/java.lang=ALL-UNNAMED -cp jade.jar:build main.Main
```

### Memory Issues
For large simulations, increase heap size:
```bash
java -Xmx512m -cp jade.jar:build main.Main
```

## Development Notes

### Code Structure:
- **main.Main**: Application entry point
- **agents/**: Car and petrol station agent implementations
- **behaviours/**: Agent behavior definitions (movement, communication, etc.)
- **gui/**: Swing-based user interface components

### JADE Platform:
- Automatically starts JADE container and platform
- Agents register with JADE Directory Facilitator (DF)
- Uses JADE Agent Communication Language (ACL) for messaging

### Historical Context:
This project was developed in 2004-2005 using:
- Java 1.4/1.5 features
- JADE 3.x framework
- Swing GUI components
- Early multi-agent system concepts

## License

This project was developed as part of university coursework in 2004-2005. Please respect any applicable academic or institutional licenses.

## Support

For issues related to:
- **JADE Framework**: Visit [JADE Official Site](http://jade.tilab.com/)
- **Java Compatibility**: Check Oracle Java documentation
- **Project-specific Issues**: Review source code comments (in German)

---

*Developed during university studies, 2004-2005*
*Multi-Agent Systems and Artificial Intelligence*