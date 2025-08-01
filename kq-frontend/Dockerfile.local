FROM node:lts-alpine AS development
ENV NODE_ENV development
WORKDIR /app
COPY package.json .
RUN npm i
COPY . .
EXPOSE 3000
CMD [ "npm", "start" ]