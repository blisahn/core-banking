import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import { AuthProvider } from "@/contexts/AuthContext";
import { Toaster } from "react-hot-toast";

const inter = Inter({
  variable: "--font-inter",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "CleanBank - Premium Banking",
  description: "Modern banking experience powered by Clean Architecture",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html
      lang="en"
      className={`${inter.variable} min-h-screen antialiased dark`}
    >
      <body className="min-h-screen flex flex-col bg-slate-950 text-slate-50 font-sans selection:bg-emerald-500/30">
        <AuthProvider>
          <Toaster
            position="top-right"
            toastOptions={{
              style: {
                background: "#1e293b",
                color: "#f1f5f9",
                border: "1px solid rgba(51,65,85,0.5)",
              },
              success: {
                iconTheme: { primary: "#34d399", secondary: "#1e293b" },
              },
              error: {
                iconTheme: { primary: "#fb7185", secondary: "#1e293b" },
              },
            }}
          />
          {children}
        </AuthProvider>
      </body>
    </html>
  );
}
