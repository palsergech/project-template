'use client'
import { createContext, ReactNode, useContext } from "react";

const TokenContext = createContext<string | undefined>(undefined)

export function TokenProvider({token, children}: {token: string, children: ReactNode}) {
    return <TokenContext.Provider value={token}>
        {children}
    </TokenContext.Provider>
} 

export function useToken() {
    const context = useContext(TokenContext)
    if (!context) {
        throw Error("use useToken inside TokenProvider only")
    }
    return context
}