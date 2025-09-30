'use client'
import { useEffect, useState } from 'react'
import { useUser } from '@/providers/UserProvider'
import { ApiProvider } from "@/api/ApiProvider"
import { Loader } from "@/components/common/loader"
import { TokenProvider } from "@/api/TokenProvider"
import { ClientConfigProvider } from "@/config/ConfigProvider"
import { ClientConfig } from "@/config/config"
import { getClientConfig } from "@/config/getClientConfigAction"
import {useIsMobile} from "@/hooks/use-is-mobile";

export default function ProtectedLayout({
  children,
}: {
  children: React.ReactNode
}) {
  const { isLoading: userLoading, user } = useUser()
  const [config, setConfig] = useState<ClientConfig | null>(null)
  const isMobile = useIsMobile()

  useEffect(() => {
    getClientConfig()
      .then(conf => setConfig(conf))
      .catch(() => alert("failed to get client config"))
  }, [])

  if (userLoading || !config) {
    return (
      <Loader />
    )
  }

  return (
      <ClientConfigProvider config={config}>
        <TokenProvider token={user.accessToken}>
          <ApiProvider>
            {children}
          </ApiProvider>
        </TokenProvider>
      </ClientConfigProvider>
  )
} 
