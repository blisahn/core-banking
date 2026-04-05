export default function LoginPage() {
  return (
    <div className="min-h-screen flex flex-col justify-center items-center p-4 bg-[radial-gradient(ellipse_at_top_right,_var(--tw-gradient-stops))] from-slate-900 via-slate-950 to-slate-950">
      <div className="absolute top-0 left-0 w-full h-full overflow-hidden -z-10">
        <div className="absolute top-[-10%] left-[-10%] w-[40%] h-[40%] rounded-full bg-primary-900/20 blur-[120px]" />
        <div className="absolute bottom-[-10%] right-[-10%] w-[40%] h-[40%] rounded-full bg-emerald-900/20 blur-[120px]" />
      </div>
      
      {/* We use dynamic import for the form if needed, or just import it. In Next.js App Router, page can be server component, but we will import the client component */}
      <div className="w-full flex justify-center animate-in">
        <LoginForm />
      </div>
    </div>
  );
}

import LoginForm from '@/components/auth/LoginForm';
