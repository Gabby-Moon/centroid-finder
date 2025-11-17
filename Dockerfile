### Maven distro to build the project ###
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Set build directory
WORKDIR /build/processor
COPY processor/ /build/processor/

RUN mvn -q -DskipTests package

### Production Build ###
# supports java 21
FROM eclipse-temurin:21-jre-jammy

# Install Node.js 22
RUN apt-get update && apt-get install -y --no-install-recommends \
      curl gnupg2 ca-certificates \
    && curl -fsSL https://deb.nodesource.com/setup_22.x | bash - \
    && apt-get install -y --no-install-recommends nodejs \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy everything
COPY ./server/ /app/server/

# Copy the built JAR from the Maven builder stage
COPY --from=builder /build/processor/target/videoprocessor.jar /app/processor/videoprocessor.jar

# directory for thumbnails, and fallback directories in case no volumes are mounted
RUN mkdir -p /videos /thumbnails /results

# install Node dependencies
WORKDIR /app/server
RUN npm install --omit=dev

# Environment variables
ENV PORT=3000
ENV VIDEO_DIRECTORY=/videos
ENV THUMBNAIL_DIRECTORY=/thumbnails
ENV RESULTS_DIRECTORY=/results
ENV JAR_PATH=/app/processor/videoprocessor.jar
ENV MAVEN_PROJECT_DIR=/app/processor

# show port on container
EXPOSE 3000

# run node
CMD ["node", "src/index.js"]