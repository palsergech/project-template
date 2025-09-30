'use client'
import { signIn, useSession } from "next-auth/react";
import { useRouter, useSearchParams } from "next/navigation";
import { useEffect } from "react";

export default function SignInPage() {
  const { status } = useSession();
  const params = useSearchParams()
  const router = useRouter()

  useEffect(() => {
    if (status === "unauthenticated") {
        signIn("keycloak");
    } else if (status == "authenticated") {
        const callbackUrl = params.get("callbackUrl")
        if (!!callbackUrl) {
            router.push(callbackUrl)
        }
    }
  }, [status, params, router]);

  return <div></div>;
}