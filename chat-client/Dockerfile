# Stage 1: Build the React application
FROM node:23-alpine AS build

# Install pnpm
RUN corepack enable && corepack prepare pnpm@10 --activate

# Set the working directory
WORKDIR /app

# Copy package.json and pnpm-lock.yaml
COPY package.json pnpm-lock.yaml ./

# Install dependencies using pnpm
RUN pnpm install

# Copy the rest of the application code
COPY . .

# Set environment variables for build
ARG CHAT_SERVER
ENV VITE_CHAT_SERVER=$CHAT_SERVER

RUN echo "REACT_APP_CHAT_SERVER=$CHAT_SERVER"

# Build the React application
RUN pnpm run build

# Stage 2: Serve the React application using Nginx
FROM nginx:alpine

# Copy the Nginx configuration file
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy the built React application from the previous stage
COPY --from=build /app/dist /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
