import { NextAuthOptions, Session } from "next-auth";
import KeycloakProvider from "next-auth/providers/keycloak";
import { config } from "@/config/config";
import { JWT } from "next-auth/jwt";
import {jwtDecode} from "jwt-decode";

export const authOptions: NextAuthOptions = {
  providers: [
    KeycloakProvider({
      issuer: config.keycloak.publicUrl,
      wellKnown: undefined,
      clientId: config.keycloak.clientId,
      clientSecret: config.keycloak.clientSecret,
      authorization: `${config.keycloak.publicUrl}/protocol/openid-connect/auth`,
      token: `${config.keycloak.internalUrl}/protocol/openid-connect/token`,
      userinfo: `${config.keycloak.internalUrl}/protocol/openid-connect/userinfo`,
      jwks_endpoint: `${config.keycloak.internalUrl}/protocol/openid-connect/certs`,
      profile(profile) {
        return {
          id: profile.sub,
          name: profile.name || profile.preferred_username,
          email: profile.email,
          image: null,
        };
      },
    })
  ],
  callbacks: {
    async jwt({ token, account }) {
      // Initial sign in
      if (account) {
        token.at = account.access_token;
        token.it = account.id_token;
        token.rt = account.refresh_token;
        token.expires = jwtDecode(account.access_token!).exp;
        return token;
      }

      const now = Date.now()
      // Return previous token if the access token has not expired yet
      if (now < (token.expires as number) * 1000) {
        console.log("token expires in ", (token.expires as number) * 1000 - now)
        return token;
      }

      // Access token has expired, try to refresh it
      try {
        const response = await fetch(`${config.keycloak.publicUrl}/protocol/openid-connect/token`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
          body: new URLSearchParams({
            client_id: config.keycloak.clientId,
            client_secret: config.keycloak.clientSecret,
            grant_type: 'refresh_token',
            refresh_token: token.rt as string,
          }),
        });

        const tokens = await response.json();

        if (!response.ok) {
          throw tokens;
        }
        return {
          ...token,
          at: tokens.access_token,
          rt: tokens.refresh_token!,
          expires: jwtDecode(tokens.access_token!).exp,
        };
      } catch (error) {
        console.error('Error refreshing access token', error);
        throw error
      }
    },
    async session({ session, token }: {token: JWT, session: Session}) {
      if (token) {
        session.accessToken = token.at!;
        session.error = token.e;
      }
      return session;
    },
    async redirect({ url, baseUrl }) {
      // Allows relative callback URLs
      if (url.startsWith("/")) return `${baseUrl}${url}`;
      // Allows callback URLs on the same origin
      else if (new URL(url).origin === baseUrl) return url;
      return baseUrl;
    },
  },
  events: {
    async signOut({ token }: {token: JWT}) {
      const issuerUrl = config.keycloak.publicUrl
      const logOutUrl = new URL(`${issuerUrl}/protocol/openid-connect/logout`)
      logOutUrl.searchParams.set("id_token_hint", token.it!)
      const res = await fetch(logOutUrl);
      if (res.status !== 200) {
        console.log("error signout")
        throw Error(`failed to logout: ${res.status}`)
      } else {
        console.log("success signout")
      }
    },
  },
  pages: {
    error: '/auth/error',
  },
  session: {
    strategy: "jwt",
    maxAge: 30 * 24 * 60 * 60, // 30 days
  },
  secret: config.nextAuth.secret,
  debug: config.debug
}; 