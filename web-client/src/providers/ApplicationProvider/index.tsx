'use client'

import React, { createContext, useContext, useState, useCallback } from "react";

// Toast types
type ToastType = "info" | "error";

// Toast state
type ToastState = {
  open: boolean;
  message: string;
  type: ToastType;
};

// Context value
type ApplicationContextType = {
  showToast: (message: string, type?: ToastType) => void;
};

const ApplicationContext = createContext<ApplicationContextType | undefined>(undefined);

export const ApplicationProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [toast, setToast] = useState<ToastState>({ open: false, message: "", type: "info" });

  const showToast = useCallback((message: string, type: ToastType = "info") => {
    setToast({ open: true, message, type });
    setTimeout(() => setToast(t => ({ ...t, open: false })), 3500);
  }, []);

  return (
    <ApplicationContext.Provider value={{ showToast }}>
      {children}
      <Toast open={toast.open} message={toast.message} type={toast.type} />
    </ApplicationContext.Provider>
  );
};

export function useApplication() {
  const ctx = useContext(ApplicationContext);
  if (!ctx) throw new Error("useApplication must be used within ApplicationProvider");
  return ctx;
}

// Simple Toast component
const Toast: React.FC<{ open: boolean; message: string; type: ToastType }> = ({ open, message, type }) => {
  if (!open) return null;
  return (
    <div
      className={`fixed top-6 left-1/2 -translate-x-1/2 z-50 px-6 py-3 rounded shadow-lg text-white transition-all
        ${type === "error" ? "bg-red-400" : "bg-gray-800"}`}
      role="alert"
    >
      {message}
    </div>
  );
}; 