# FROM node:alpine3.11 as build
FROM node:lts-alpine

# install simple http server for serving static content
RUN npm install -g http-server

# Create an application directory
RUN mkdir -p /app

# make the 'app' folder the current working directory
WORKDIR /app

# copy both 'package.json' and 'package-lock.json' (if available)
COPY frontend/GreenC/package*.json ./

# install project dependencies
RUN npm install 
RUN npm install @vue/cli -g
RUN npm install vue bootstrap-vue bootstrap
RUN npm install --save axios
RUN npm install vue-chartjs chart.js --save

#Copy frontend stuff
COPY frontend/GreenC/ .

# build app for production with minification
RUN npm run build 

# production environment
EXPOSE 8080
CMD ["http-server", "dist"]
