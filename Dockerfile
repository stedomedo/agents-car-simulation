# JADE Car Simulation - Release Deployment
# Downloads and runs the latest GitHub release JAR

FROM ubuntu:22.04

# Install dependencies (non-interactive)
ENV DEBIAN_FRONTEND=noninteractive
ENV TZ=UTC
ENV DISPLAY=:1
RUN apt-get update && apt-get install -y \
    openjdk-21-jdk \
    openjdk-21-jre \
    python3 \
    python3-pip \
    xvfb \
    x11vnc \
    x11-utils \
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
set -e\n\
\n\
# Clean up any existing X server files\n\
rm -f /tmp/.X1-lock /tmp/.X11-unix/X1\n\
\n\
# Set display\n\
export DISPLAY=:1\n\
\n\
# Start Xvfb (virtual framebuffer)\n\
echo "Starting Xvfb..."\n\
Xvfb :1 -screen 0 1024x768x16 -ac +extension GLX +render -noreset &\n\
XVFB_PID=$!\n\
\n\
# Wait for X server to be ready\n\
echo "Waiting for X server to start..."\n\
for i in {1..30}; do\n\
    if xdpyinfo -display :1 >/dev/null 2>&1; then\n\
        echo "X server is ready"\n\
        break\n\
    fi\n\
    if [ $i -eq 30 ]; then\n\
        echo "X server failed to start"\n\
        exit 1\n\
    fi\n\
    sleep 1\n\
done\n\
\n\
# Start window manager\n\
echo "Starting fluxbox..."\n\
fluxbox &\n\
sleep 2\n\
\n\
# Start VNC server\n\
echo "Starting x11vnc..."\n\
x11vnc -display :1 -nopw -listen localhost -xkb -ncache 10 -ncache_cr -forever -bg\n\
sleep 2\n\
\n\
# Start websockify\n\
echo "Starting websockify..."\n\
websockify --web=/opt/novnc 6080 localhost:5900 &\n\
sleep 3\n\
\n\
# Start the Java application\n\
echo "Starting JADE Car Simulation..."\n\
echo "DISPLAY variable: $DISPLAY"\n\
echo "Testing X connection..."\n\
xdpyinfo -display :1 | head -5\n\
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