# Multi-stage build for JADE Car Simulation with Web VNC
FROM gradle:8.5-jdk21 AS builder

# Copy source code and build
WORKDIR /app
COPY . .
RUN ./gradlew build --no-daemon

# Runtime stage with GUI support
FROM ubuntu:22.04

# Install Java 21, VNC, and GUI dependencies
RUN apt-get update && apt-get install -y \
    openjdk-21-jre-headless \
    x11vnc \
    xvfb \
    fluxbox \
    wget \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Install noVNC for web-based VNC access
RUN wget -qO- https://github.com/novnc/noVNC/archive/v1.4.0.tar.gz | tar xz -C /opt \
    && mv /opt/noVNC-1.4.0 /opt/novnc \
    && wget -qO- https://github.com/novnc/websockify/archive/v0.11.0.tar.gz | tar xz -C /opt \
    && mv /opt/websockify-0.11.0 /opt/websockify

# Create app user
RUN useradd -m -s /bin/bash app
USER app
WORKDIR /home/app

# Copy built JAR from builder stage
COPY --from=builder /app/build/libs/*.jar ./jade-simulation.jar

# Create startup script
RUN echo '#!/bin/bash\n\
export DISPLAY=:1\n\
Xvfb :1 -screen 0 1024x768x16 &\n\
fluxbox &\n\
x11vnc -display :1 -nopw -listen localhost -xkb -ncache 10 -ncache_cr -forever &\n\
cd /opt/websockify && python3 websockify.py --web=/opt/novnc 6080 localhost:5900 &\n\
sleep 3\n\
java -jar jade-simulation.jar\n\
' > start.sh && chmod +x start.sh

# Expose VNC web interface port
EXPOSE 6080

# Start the application
CMD ["./start.sh"]