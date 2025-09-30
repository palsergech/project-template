import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import React, { Suspense } from "react";
import NextAuthSessionProvider from "@/providers/NextAuthSessionProvider";
import { UserProvider } from "@/providers/UserProvider";
import {ApplicationProvider} from "@/providers/ApplicationProvider";
import {Loader} from "@/components/common/loader";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Template",
  description: "Productivity Solution",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <NextAuthSessionProvider>
          <ApplicationProvider>
            <UserProvider>
              <Suspense fallback={<Loader />}>
                {children}
              </Suspense>
            </UserProvider>
          </ApplicationProvider>
        </NextAuthSessionProvider>
      </body>
    </html>
  );
}
