# JADE Car Simulation - Release Deployment
# Downloads and runs the latest GitHub release JAR

FROM ubuntu:22.04

# Install dependencies (non-interactive)
ENV DEBIAN_FRONTEND=noninteractive
ENV TZ=UTC
RUN apt-get update && apt-get install -y \
    openjdk-21-jre-headless \
    python3 \
    python3-pip \
    xvfb \
    x11vnc \
    fluxbox \
    wget \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Install noVNC and websockify
RUN wget -qO- https://github.com/novnc/noVNC/archive/v1.4.0.tar.gz | tar xz -C /opt \
    && mv /opt/noVNC-1.4.0 /opt/novnc \
    && pip3 install websockify

# Create app directory
WORKDIR /app

# Download the latest release JAR
RUN curl -L -o agents-car-simulation.jar \
    "https://github.com/stedomedo/agents-car-simulation/releases/download/agents-openhands/agents-car-simulation-1.0.0.jar"

# Create startup script
RUN echo '#!/bin/bash\n\
export DISPLAY=:1\n\
Xvfb :1 -screen 0 1024x768x16 &\n\
sleep 2\n\
fluxbox &\n\
x11vnc -display :1 -nopw -listen localhost -xkb -ncache 10 -ncache_cr -forever &\n\
websockify --web=/opt/novnc 6080 localhost:5900 &\n\
sleep 3\n\
cd /app\n\
java -jar agents-car-simulation.jar\n\
' > /app/start.sh && chmod +x /app/start.sh

# Expose VNC web port
EXPOSE 6080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:6080/ || exit 1

# Start the application
CMD ["/app/start.sh"]