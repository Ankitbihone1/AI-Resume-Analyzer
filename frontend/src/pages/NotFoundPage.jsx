import React from 'react';
import { Link } from 'react-router-dom';
import { FileQuestion, ArrowLeft, Home } from 'lucide-react';

export default function NotFoundPage() {
  return (
    <div className="min-h-screen bg-slate-50 flex flex-col items-center justify-center p-4 text-center font-sans">
      <div className="w-16 h-16 rounded-2xl bg-brand-50 text-brand-600 flex items-center justify-center mb-4">
        <FileQuestion className="w-8 h-8" />
      </div>
      <span className="text-sm font-bold text-brand-600 uppercase tracking-wider mb-1">Error 404</span>
      <h1 className="text-3xl font-extrabold text-slate-900 mb-2">Page Not Found</h1>
      <p className="text-sm text-slate-500 max-w-sm mb-6">
        The resume report or page you are looking for does not exist or may have been removed.
      </p>
      <div className="flex items-center space-x-3">
        <Link to="/" className="btn-secondary text-xs space-x-1.5">
          <ArrowLeft className="w-4 h-4" />
          <span>Home</span>
        </Link>
        <Link to="/dashboard" className="btn-primary text-xs space-x-1.5">
          <Home className="w-4 h-4" />
          <span>Go to Dashboard</span>
        </Link>
      </div>
    </div>
  );
}
