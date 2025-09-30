import 'next-auth';

declare module 'next-auth' {
  interface Session {
    accessToken: string;
    error?: string;
    user: {
      email?: string | null;
      name?: string | null;
      image?: string | null;
    };
  }
}

declare module 'next-auth/jwt' {
  interface JWT {
    at?: string; // accessToken
    it?: string; // idToken
    expires?: number;
    rt?: string; // refreshToken
    e?: string; // error
  }
} 