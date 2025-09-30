import { z } from 'zod';

const envSchema = z.object({
  KEYCLOAK_CLIENT_ID: z.string().min(1),
  KEYCLOAK_CLIENT_SECRET: z.string().min(1),
  KEYCLOAK_PUBLIC_URL: z.string().url(),
  KEYCLOAK_INTERNAL_URL: z.string().url(),
  NEXTAUTH_URL: z.string().url(),
  NEXTAUTH_SECRET: z.string().min(1),
  BACKEND_URL: z.string().url(),
  DEBUG: z.string()
});

const env = envSchema.parse(process.env);

export const config = {
  keycloak: {
    clientId: env.KEYCLOAK_CLIENT_ID,
    clientSecret: env.KEYCLOAK_CLIENT_SECRET,
    publicUrl: env.KEYCLOAK_PUBLIC_URL,
    internalUrl: env.KEYCLOAK_INTERNAL_URL,
  },
  nextAuth: {
    url: env.NEXTAUTH_URL,
    secret: env.NEXTAUTH_SECRET,
  },
  backend: {
    url: env.BACKEND_URL
  },
  debug: Boolean(env.DEBUG)
} as const; 

export type ClientConfig = {
  backend: {
    url: string,
  }
}
