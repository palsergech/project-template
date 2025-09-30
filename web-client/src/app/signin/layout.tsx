import React, { Suspense } from "react";

export default function SignInLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <Suspense fallback={<div>Loading...</div>}>
      {children}
    </Suspense>
  );
}
