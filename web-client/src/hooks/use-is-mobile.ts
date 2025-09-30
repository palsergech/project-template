import {useMediaQuery} from "@react-hookz/web";

export function useIsMobile() {
    return useMediaQuery('(max-width: 1024px)')
}