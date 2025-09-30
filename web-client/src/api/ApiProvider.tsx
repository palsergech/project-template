'use client'

import {createContext, ReactNode, useContext, useEffect, useRef, useState} from "react"
import {Configuration, ErrorContext, GreetingApi, ResponseContext} from "./generated"
import { useToken } from "./TokenProvider"
import { useClientConfig } from "@/config/ConfigProvider"
import {useApplication} from "@/providers/ApplicationProvider";

type ApiError = {
    type: 'SERVER_ERROR' | 'AUTH_ERROR' | 'NOT_FOUND_ERROR' | 'BAD_REQUEST' | 'DOMAIN_ERROR'
    message: string
    details?: {
        objectType?: string
        key?: string
        [key: string]: unknown
    }
}

const createApiConfig = (
    {
        url,
        token,
        onNoResponseError,
        onApiError
    }: {
        url: string,
        token: () => string,
        onNoResponseError: (error: unknown) => void,
        onApiError: (error: unknown) => void
    }) => new Configuration({
        basePath: url,
        middleware: [{
            pre: async (context) => {
                context.init.headers = {
                    ...context.init.headers,
                    Authorization: `Bearer ${token()}`
                }
                return context
            },
            post: async (response: ResponseContext) => {
                if (response.response.status == 401) {
                    onApiError({type: 'AUTH_ERROR', message: 'Authentication error. Please log in again.'})
                    throw new Error('Authentication error. Please log in again.')
                }
                if (response.response.status >= 400) {
                    const error = await response.response.json() as ApiError
                    onApiError(error)
                    throw error
                }
                return response.response
            },
            onError: async (context: ErrorContext) => {
                if (!context.response) {
                    onNoResponseError(context.error)
                }
                return context.response
            }
        }]
    })

type Api = {
    greeting: GreetingApi
}

const ApiContext = createContext<Api | null>(null)

export function ApiProvider({ children }: { children: ReactNode }) {
    const clientConfig = useClientConfig()
    const { showToast } = useApplication()
    const token = useToken()
    const tokenRef = useRef(token)

    useEffect(() => {
        tokenRef.current = token
    }, [token]);

    const [api] = useState({
        greeting: new GreetingApi(createApiConfig({
            url: clientConfig.backend.url,
            token: () => tokenRef.current,
            onNoResponseError: () => showToast('Cannot connect to the server. Please check your internet connection.', 'error'),
            onApiError: (error) => showToast(`API call error: ${JSON.stringify(error)}`, 'error')
        }))
    })

    if (!api) {
        return null
    }

    return <ApiContext.Provider value={api}>
        {children}
    </ApiContext.Provider>
}

export function useApi(): Api {
    const api = useContext(ApiContext)
    if (!api) {
        throw new Error('useApi must be used within ApiProvider')
    }
    return api
}