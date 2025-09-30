'use server'

import {ClientConfig, config} from "./config"

export async function getClientConfig(): Promise<ClientConfig> {
    return {
        backend: {
            url: config.backend.url,
        }
    }
}