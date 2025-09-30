'use client'

import { createContext, useContext, ReactNode } from 'react'
import { useSession } from 'next-auth/react'

type User = {
  email: string | null
  accessToken: string
  name: string | null
}

type UserContextType = {
  user: User
  isLoading: boolean
}

const UserContext = createContext<UserContextType | undefined>(undefined)

export function UserProvider({ children }: { children: ReactNode }) {
  const { data: session, status } = useSession()

  const user: User = {
    email: session?.user?.email ?? null,
    name: session?.user?.name ?? null,
    // @ts-expect-error accessToken is required
    accessToken: session?.accessToken
  }

  return (
    <UserContext.Provider value={{ user, isLoading: status === 'loading' }}>
      {children}
    </UserContext.Provider>
  )
}

export function useUser() {
  const context = useContext(UserContext)
  if (context === undefined) {
    throw new Error('useUser must be used within a UserProvider')
  }
  return context
} 