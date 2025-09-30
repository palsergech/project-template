import { useUser } from "@/providers/UserProvider"
import { signOut } from "next-auth/react"
import { cn } from "@/lib/utils"

export function UserInfo() {
    const { user } = useUser()

    return (
        <div className="p-4 border-b">
            <div className="flex items-center justify-between">
                <div className="flex flex-col">
                    <span className="text-sm font-medium text-gray-900">{user.name}</span>
                    <span className="text-xs text-gray-500">{user.email}</span>
                </div>
                <button
                    onClick={() => signOut()}
                    className={cn(
                        "text-xs text-gray-500 hover:text-gray-700 transition-colors",
                        "focus:outline-none focus:underline"
                    )}
                >
                    Sign Out
                </button>
            </div>
        </div>
    )
} 