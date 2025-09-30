'use client'
import { createContext, ReactNode, useContext } from "react";
import { ClientConfig } from "./config";

const ClientConfigContext = createContext<ClientConfig | undefined>(undefined)

export function ClientConfigProvider({config, children}: {config: ClientConfig, children: ReactNode}) {
    return <ClientConfigContext.Provider value={config}>
        {children}
    </ClientConfigContext.Provider>
} 

export function useClientConfig() {
    const context = useContext(ClientConfigContext)
    if (!context) {
        throw Error("use useClientConfig inside ClientConfigProvider only")
    }
    return context
}