# JADE Car Simulation - Multi-Agent System

A Java-based car simulation using the JADE (Java Agent DEvelopment Framework) platform, developed in 2004-2005. This project demonstrates a multi-agent system where car agents navigate a city environment and interact with petrol station agents.

## Overview

This simulation features:
- **Car Agents**: Autonomous vehicles that navigate the city and require fuel
- **Petrol Station Agents**: Service stations that provide fuel to cars
- **Real-time GUI**: Visual representation of the city map with moving agents
- **Agent Communication**: JADE-based message passing between agents
- **Behavioral Patterns**: Complex agent interactions and decision-making

## Simulation in Action

![Car Simulation Demo](agent_animated.gif)

*The animation shows car agents autonomously navigating the city environment, visiting petrol stations when fuel is needed, and demonstrating the multi-agent coordination system in real-time.*

## Modern Build System

This project has been modernized with Gradle for easier dependency management and building. The project follows standard Gradle conventions with the following structure:

```
project/
├── build.gradle           # Gradle build configuration
├── gradle/               # Gradle wrapper files
├── libs/
│   └── jade.jar          # Local JADE dependency
├── src/
│   └── main/
│       ├── java/         # Source code
│       └── resources/    # Resources (images, etc.)
└── build/                # Build output (generated)
```

### Building and Running

```bash
# Compile the project
gradle build

# Run the application
gradle run

# Run with explicit GUI support
gradle runGui

# Create executable JAR
gradle jar

# Clean build artifacts
gradle clean

# Show available tasks
gradle tasks
```

### Requirements

- **Java**: 8 or higher (tested with OpenJDK 21)
- **Gradle**: 4.4 or higher
- **JADE Framework**: Included as local JAR dependency in `libs/`

### Advantages of the Gradle Build System

- **Standardized Structure**: Follows Maven/Gradle conventions for easy IDE integration
- **Dependency Management**: JADE framework included as local dependency
- **Cross-Platform**: Works on Windows, macOS, and Linux
- **IDE Support**: Compatible with IntelliJ IDEA, Eclipse, and VS Code
- **Simplified Commands**: Single commands for build, run, and package operations

## Prerequisites

### Java Development Kit (JDK)
- **Required**: Java 8 or higher
- **Recommended**: OpenJDK 11, 17, or 21
- **Tested with**: OpenJDK 21

### Gradle
- **Installation**: Gradle 8.0 or higher (for Java 21 support)
- **Included**: Gradle wrapper files are included in the project (automatically downloads Gradle 8.5)

### JADE Framework
- **Included**: JADE JAR is included in `libs/jade.jar`
- **Version**: JADE 4.6.0
- **No additional installation required**

### Display Requirements
- **GUI Mode**: Requires X11 display server (Linux/macOS) or Windows display
- **Headless Mode**: Can run with virtual display (Xvfb on Linux)

## Quick Start

### Prerequisites
- **Java**: JDK 8 or higher (recommended: OpenJDK 11, 17, or 21)
- **Gradle**: 8.0 or higher for Java 21 support (or use included Gradle wrapper)
- **Display**: GUI requires X11 (Linux/macOS) or Windows display

### 1. Clone the Project
```bash
git clone <repository-url>
cd <project-directory>
```

### 2. Build and Run
```bash
# Build the project
gradle build

# Run the application
gradle run
```

### 3. Alternative: Generate and Use Gradle Wrapper
```bash
# Generate Gradle wrapper (one-time setup)
gradle wrapper

# Then use wrapper instead of system Gradle
# On Linux/macOS
./gradlew build
./gradlew run

# On Windows
gradlew.bat build
gradlew.bat run
```

That's it! The Gradle build system handles all compilation, dependency management, and resource copying automatically.

### Expected Output
When running successfully, you should see:
- JADE platform initialization messages
- GUI window with city map, petrol stations, and car agents
- Real-time simulation of cars moving and refueling

**Note**: In headless environments, you may see resource loading errors - this is expected and requires virtual display setup (see Platform-Specific Installation section).

## Detailed Usage

### Building the Project
```bash
# Clean and build
gradle clean build

# Build only (without tests)
gradle assemble

# Show build information
gradle build --info
```

### Running the Application

#### Standard GUI Mode
```bash
gradle run
```

#### With GUI Support (explicit)
```bash
gradle runGui
```

#### Headless Mode (Linux/Unix)
If running on a server without display:
```bash
# Install virtual display (Ubuntu/Debian)
sudo apt-get install xvfb

# Start virtual display
Xvfb :1 -screen 0 1024x768x24 &

# Run with virtual display
DISPLAY=:1 gradle run
```

#### Platform-Specific Notes

**macOS:**
- Native display support - no virtual display needed
- If Java is not installed: `brew install openjdk`
- May need to allow Java in System Preferences > Security & Privacy

**Windows:**
- Use Command Prompt or PowerShell
- Gradle handles classpath automatically

### Creating Executable JAR
```bash
# Create JAR file
gradle jar

# JAR will be created in build/libs/
# Run the JAR directly (requires JADE in classpath):
java -cp "libs/jade.jar:build/libs/project-1.0.0.jar" main.Main

# Or create a fat JAR with all dependencies (add to build.gradle):
gradle shadowJar  # if shadow plugin is configured
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

#### 1. Build Failures
```bash
# Clean and rebuild
gradle clean build

# Check Gradle version
gradle --version

# Use Gradle wrapper if available
./gradlew build
```

#### 2. Runtime Issues
```bash
# Run with debug information
gradle run --debug

# Check if JADE dependency is properly loaded
gradle dependencies
```

#### 3. Display Issues (Linux)
```bash
# For headless systems, ensure Xvfb is running
ps aux | grep Xvfb
export DISPLAY=:1
gradle run
```

#### 4. macOS Security Permissions
If you get security warnings on macOS:
```bash
# Allow Java in System Preferences > Security & Privacy
# Check Java installation:
java -version
```

#### 5. Java Version Compatibility
```bash
# Check Java version
java -version

# For Java 9+, Gradle handles module compatibility automatically
# If issues persist, try with Java 8 or 11
```

#### 6. JAR Execution Issues
```bash
# If running JAR directly fails, ensure JADE is in classpath
java -cp "libs/jade.jar:build/libs/project-1.0.0.jar" main.Main

# Check JAR contents
jar -tf build/libs/project-1.0.0.jar | head -10

# Verify main class
java -cp "libs/jade.jar:build/libs/project-1.0.0.jar" -verbose:class main.Main
```

### Memory Issues
For large simulations, configure JVM options in `build.gradle`:
```gradle
run {
    jvmArgs = ['-Xmx512m']
}
```

### IDE Integration
- **IntelliJ IDEA**: Import as Gradle project
- **Eclipse**: Use Gradle plugin or import as existing Gradle project
- **VS Code**: Install Gradle extension

## Platform-Specific Installation

### Linux (Ubuntu/Debian)

#### Prerequisites Installation
```bash
# Update package list
sudo apt update

# Install OpenJDK
sudo apt install openjdk-11-jdk

# Install Gradle
sudo apt install gradle

# For GUI applications, ensure X11 is available
sudo apt install xorg

# For headless servers, install virtual display
sudo apt install xvfb
```

#### Running the Application
```bash
# Clone and build
git clone <repository-url>
cd <project-directory>
gradle build

# Run with GUI (if display available)
gradle run

# Run headless with virtual display
Xvfb :1 -screen 0 1024x768x24 &
DISPLAY=:1 gradle run
```

### macOS

#### Prerequisites Installation
```bash
# Install Homebrew (if not already installed)
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install OpenJDK
brew install openjdk@11

# Install Gradle
brew install gradle

# Link Java (if needed)
sudo ln -sfn /opt/homebrew/opt/openjdk@11/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-11.jdk
```

#### Running the Application
```bash
# Clone and build
git clone <repository-url>
cd <project-directory>
gradle build

# Run the application
gradle run
```

#### macOS-Specific Notes
- Native display support - no virtual display needed
- May need to allow Java in **System Preferences > Security & Privacy**
- If you get "cannot be opened because it is from an unidentified developer":
  ```bash
  # Allow the application temporarily
  sudo spctl --master-disable
  # Run your application
  gradle run
  # Re-enable security (recommended)
  sudo spctl --master-enable
  ```

### Windows

#### Prerequisites Installation

**Option 1: Using Chocolatey (Recommended)**
```powershell
# Install Chocolatey (run as Administrator)
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# Install OpenJDK and Gradle
choco install openjdk11 gradle
```

**Option 2: Manual Installation**
1. **Java**: Download OpenJDK from [Adoptium](https://adoptium.net/) or [Oracle](https://www.oracle.com/java/technologies/downloads/)
2. **Gradle**: Download from [Gradle.org](https://gradle.org/install/) and add to PATH

#### Running the Application
```cmd
# Clone and build
git clone <repository-url>
cd <project-directory>
gradle build

# Run the application
gradle run
```

#### Windows-Specific Notes
- Use **Command Prompt** or **PowerShell**
- Gradle handles classpath automatically (no manual classpath configuration needed)
- Windows Defender may require permission for Java applications

### Docker Alternative (All Platforms)

For a consistent environment across all platforms:

```dockerfile
# Create Dockerfile
FROM openjdk:11-jdk

# Install Gradle
RUN apt-get update && apt-get install -y gradle xvfb

# Copy project
COPY . /app
WORKDIR /app

# Build project
RUN gradle build

# Run with virtual display
CMD ["sh", "-c", "Xvfb :1 -screen 0 1024x768x24 & DISPLAY=:1 gradle run"]
```

```bash
# Build and run Docker container
docker build -t jade-car-simulation .
docker run -it jade-car-simulation
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