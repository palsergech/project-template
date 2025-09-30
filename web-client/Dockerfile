ARG NODE_VERSION=22.14.0-slim
ARG PNPM_VERSION=9.15.1

FROM node:${NODE_VERSION} AS base
WORKDIR /app
RUN npm install -g pnpm@${PNPM_VERSION}
COPY --link package.json pnpm-lock.yaml tsconfig.json ./
ENV NODE_ENV=production
RUN pnpm install --prod

FROM base AS builder
COPY --link . .
COPY --link .env.example .env

# yes, it's silly, but it's reliable. For some reason styles are not workins expected wat after first build run
RUN pnpm run build && pnpm run build

FROM node:${NODE_VERSION} AS runner
USER node
ENV PORT=3000
ENV NEXT_TELEMETRY_DISABLE=1
WORKDIR /app

# Copy only necessary files from the builder stage to keep the image minimal
COPY --link --from=builder /app/.next/standalone ./
COPY --link --from=builder /app/.next/static ./.next/static
COPY --link --from=builder /app/public ./public

EXPOSE 3000

ENTRYPOINT ["node", "server.js"]