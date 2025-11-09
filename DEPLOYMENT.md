# 🚀 JADE Car Simulation - Public Deployment Guide

This guide shows you how to deploy your JADE multi-agent car simulation publicly so anyone can access it through a web browser.

## 🎯 Deployment Architecture

Your application uses **Docker + Web VNC** to make the Java Swing GUI accessible through any web browser:

```
User Browser → Web VNC Interface → Docker Container → JADE Application
```

## 🐳 Quick Start (Local Testing)

### Option 1: Using GitHub Release (Recommended)
```bash
# Uses the pre-built JAR from your GitHub release
./deploy.sh release
```

### Option 2: Build from Source
```bash
# Builds from source code
docker-compose up --build
```

**Access the application:**
- Open your browser to: `http://localhost:6080`
- Click "Connect" in the noVNC interface
- Your JADE car simulation will appear!

## ☁️ Cloud Deployment Options

### Option 1: Railway (Recommended - Free Tier Available)

1. **Push your deployment files** to GitHub (if not already done)
2. **Connect to Railway:**
   - Go to [railway.app](https://railway.app)
   - Sign up with GitHub
   - Click "Deploy from GitHub repo"
   - Select your repository

3. **Configure the service:**
   - Railway will auto-detect `Dockerfile.release` (uses your GitHub release JAR)
   - Port will be automatically set to `6080`
   - Deploy!

4. **Access your app:**
   - Railway provides a public URL like `https://your-app.railway.app`
   - No build time needed - uses your pre-built release!

### Option 2: Render (Free Tier)

1. **Connect repository:**
   - Go to [render.com](https://render.com)
   - Create new "Web Service"
   - Connect your GitHub repo

2. **Configure:**
   - Build Command: `docker build -t jade-app .`
   - Start Command: `docker run -p 6080:6080 jade-app`
   - Port: `6080`

### Option 3: DigitalOcean App Platform

1. **Create app:**
   - Go to DigitalOcean Apps
   - Create from GitHub repository
   - Select Dockerfile deployment

2. **Configure:**
   - HTTP Port: `6080`
   - Instance size: Basic ($5/month)

### Option 4: AWS/GCP/Azure

Deploy using their container services:
- **AWS:** ECS Fargate or App Runner
- **GCP:** Cloud Run
- **Azure:** Container Instances

## 🔧 Advanced Configuration

### Custom Domain Setup

1. **Add domain to your cloud provider**
2. **Update nginx.conf:**
   ```nginx
   server_name your-domain.com;
   ```
3. **Enable SSL** through your cloud provider's dashboard

### Performance Tuning

For better performance with multiple users:

```yaml
# docker-compose.yml
services:
  jade-simulation:
    deploy:
      replicas: 3  # Run multiple instances
    environment:
      - JAVA_OPTS=-Xmx512m -Xms256m
```

### Scaling for Multiple Users

Each user needs their own container instance. Use:
- **Load balancer** to distribute users
- **Session affinity** to keep users connected to their instance
- **Auto-scaling** based on CPU/memory usage

## 🛠️ Local Development

### Test without Docker:
```bash
# Build the JAR
./gradlew build

# Run locally (requires X11 display)
java -jar build/libs/project-1.0.0.jar
```

### Debug Docker container:
```bash
# Build and run interactively
docker build -t jade-app .
docker run -it -p 6080:6080 jade-app bash

# Check VNC is running
ps aux | grep vnc
```

## 📱 Mobile Support

The web VNC interface works on mobile devices:
- **Touch controls** translate to mouse clicks
- **Pinch to zoom** for better visibility
- **Landscape mode** recommended

## 🔒 Security Considerations

For production deployment:
- Add **authentication** to the VNC interface
- Use **HTTPS** with SSL certificates
- Implement **rate limiting**
- Add **monitoring** and logging

## 💰 Cost Estimates

| Platform | Free Tier | Paid Tier |
|----------|-----------|-----------|
| Railway | 500 hours/month | $5/month |
| Render | 750 hours/month | $7/month |
| DigitalOcean | None | $5/month |
| AWS Fargate | Limited | ~$10/month |

## 🎮 User Experience

Once deployed, users can:
1. Visit your public URL
2. See the noVNC loading screen
3. Click "Connect"
4. Interact with your JADE simulation using mouse/touch
5. Watch car agents navigate and interact with petrol stations

## 🚨 Troubleshooting

**Container won't start:**
- Check logs: `docker logs jade-car-simulation`
- Verify Java version compatibility

**VNC not accessible:**
- Ensure port 6080 is exposed
- Check firewall settings

**GUI appears corrupted:**
- Increase screen resolution in Dockerfile
- Adjust color depth settings

## 📞 Support

If you encounter issues:
1. Check the container logs
2. Verify the JAR builds successfully
3. Test locally first before cloud deployment
4. Ensure your cloud provider supports long-running containers

---

**Ready to deploy?** Start with Railway or Render for the easiest setup!