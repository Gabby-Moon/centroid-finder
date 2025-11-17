FROM maven:3.9.11-eclipse-temurin-17

WORKDIR /app

EXPOSE 3000

COPY . .

RUN apt-get update && \
    apt-get install -y curl && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs && \
    apt-get clean

RUN npm i

CMD ["npm", "start"]