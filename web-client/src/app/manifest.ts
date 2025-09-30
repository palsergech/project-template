import type { MetadataRoute } from 'next'

export default function manifest(): MetadataRoute.Manifest {
    return {
        name: 'Zametki',
        short_name: 'Zametki',
        description: 'Description',
        start_url: '/',
        display: 'standalone',
        background_color: '#ffffff',
        theme_color: '#000000',
        icons: [
            {
                src: '/logo.png',
                sizes: '1024x1024',
                type: 'image/png',
            },
        ],
    }
}