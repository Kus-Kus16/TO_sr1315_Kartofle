import {createContext} from "react";

export default interface RefreshContextType {
    refresh: () => void;
}

export const RefreshContext = createContext<RefreshContextType>(null!)
