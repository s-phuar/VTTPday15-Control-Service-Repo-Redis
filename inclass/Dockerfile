#FROM library/node:22-slim
FROM node:22-slim

LABEL maintainer=sphuar
WORKDIR /app

COPY *.js .
COPY *.json .
COPY public public
COPY views views
# COPY main.js .
# COPY metrics.js .
# COPY package.json .
# COPY package-lock.json .
# COPY . .

#install the packages, --omit-dev optional
RUN npm ci --omit-dev

#run(build) image
ENV PORT=3000 METRICS_PORT=3100
EXPOSE ${PORT} ${METRICS_PORT}

# CMD node main
ENTRYPOINT node main.js

#docker image build -t sphuar/ssf-day14inclass:v1.0.0 .
## open app with 12345, container ported from 3000 into 12345
#docker run -d -p 12345:3000 sphuar/ssf-day14inclass:v1.0.0

