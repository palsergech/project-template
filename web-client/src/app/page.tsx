'use client'
 
import { Loader } from '@/components/common/loader'
import { useSession } from 'next-auth/react'
import { useEffect } from 'react'
import {useRouter} from "next/navigation";

export default function HomePage() {
  const {status} = useSession()
  const router = useRouter()

  useEffect(() => {
    if (status === "authenticated") {
      router.push("/greeting")
    }
  }, [status, router])

  return (
    <Loader/>
  )
} 