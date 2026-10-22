import React, { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { FileText, Lock, Mail, Eye, EyeOff, Loader2, AlertCircle, ArrowLeft, CheckCircle } from 'lucide-react';

export default function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  // Check if session was expired
  const queryParams = new URLSearchParams(location.search);
  const isExpired = queryParams.get('expired') === 'true';

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!email.trim() || !password) {
      setError('Please provide both your email address and password.');
      return;
    }

    setLoading(true);
    const result = await login(email, password);
    setLoading(false);

    if (result.success) {
      const origin = location.state?.from?.pathname || '/dashboard';
      navigate(origin, { replace: true });
    } else {
      setError(result.error);
    }
  };

  // One-click demo login for portfolio reviewers
  const handleQuickDemoLogin = async () => {
    setEmail('demo.developer@example.com');
    setPassword('Password123!');
    setLoading(true);
    const result = await login('demo.developer@example.com', 'Password123!');
    setLoading(false);
    if (result.success) {
      navigate('/dashboard');
    } else {
      setError(result.error);
    }
  };

  return (
    <div className="min-h-screen bg-slate-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8 font-sans">
      <div className="sm:mx-auto sm:w-full sm:max-w-md">
        {/* Back to Home Link */}
        <Link
          to="/"
          className="inline-flex items-center text-xs font-semibold text-slate-500 hover:text-brand-600 mb-6 transition"
        >
          <ArrowLeft className="w-4 h-4 mr-1.5" />
          Back to Homepage
        </Link>

        {/* Brand Icon */}
        <div className="flex justify-center">
          <div className="w-12 h-12 rounded-2xl bg-gradient-to-tr from-brand-700 via-brand-600 to-indigo-500 flex items-center justify-center text-white shadow-lg">
            <FileText className="w-6 h-6 text-white" />
          </div>
        </div>

        <h2 className="mt-4 text-center text-2xl font-extrabold text-slate-900 tracking-tight">
          Sign In to AI Resume Analyzer
        </h2>
        <p className="mt-1 text-center text-sm text-slate-500">
          Access your personal resume evaluations and match analytics
        </p>
      </div>

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md px-4 sm:px-0">
        <div className="bg-white py-8 px-6 sm:px-10 rounded-2xl border border-slate-200/80 shadow-card">
          {/* Expired Session Alert */}
          {isExpired && (
            <div className="mb-5 p-3 rounded-lg bg-amber-50 border border-amber-200 flex items-start space-x-2.5 text-xs text-amber-800">
              <AlertCircle className="w-4 h-4 text-amber-600 mt-0.5 shrink-0" />
              <span>Your session has expired. Please sign in again to continue.</span>
            </div>
          )}

          {/* Error Message */}
          {error && (
            <div className="mb-5 p-3.5 rounded-lg bg-rose-50 border border-rose-200 flex items-start space-x-2.5 text-xs text-rose-800">
              <AlertCircle className="w-4 h-4 text-rose-600 mt-0.5 shrink-0" />
              <span>{error}</span>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            {/* Email Field */}
            <div>
              <label className="block text-xs font-semibold text-slate-700 mb-1.5">
                Email Address
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                  <Mail className="w-4 h-4" />
                </div>
                <input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="name@company.com"
                  className="input-field pl-10"
                  required
                  autoComplete="email"
                />
              </div>
            </div>

            {/* Password Field */}
            <div>
              <div className="flex items-center justify-between mb-1.5">
                <label className="block text-xs font-semibold text-slate-700">
                  Password
                </label>
              </div>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                  <Lock className="w-4 h-4" />
                </div>
                <input
                  type={showPassword ? 'text' : 'password'}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="••••••••"
                  className="input-field pl-10 pr-10"
                  required
                  autoComplete="current-password"
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute inset-y-0 right-0 pr-3.5 flex items-center text-slate-400 hover:text-slate-600"
                >
                  {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                </button>
              </div>
            </div>

            {/* Submit Button */}
            <button
              type="submit"
              disabled={loading}
              className="btn-primary w-full py-2.5 mt-2"
            >
              {loading ? (
                <>
                  <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                  <span>Signing In...</span>
                </>
              ) : (
                <span>Sign In</span>
              )}
            </button>
          </form>

          {/* Quick Demo Login Option for Reviewers */}
          <div className="mt-6 pt-6 border-t border-slate-100">
            <div className="bg-slate-50 p-3 rounded-xl border border-slate-200/80 mb-3 text-center">
              <span className="block text-xs font-semibold text-slate-700 mb-1">
                Portfolio Quick Access
              </span>
              <p className="text-[11px] text-slate-500 mb-2">
                Click below to instantly sign in as the pre-configured demo candidate.
              </p>
              <button
                type="button"
                onClick={handleQuickDemoLogin}
                disabled={loading}
                className="w-full inline-flex items-center justify-center px-3 py-1.5 rounded-lg text-xs font-semibold text-brand-700 bg-brand-50 hover:bg-brand-100 border border-brand-200 transition"
              >
                <CheckCircle className="w-3.5 h-3.5 mr-1.5 text-brand-600" />
                <span>Demo Sign In (Alex Morgan)</span>
              </button>
            </div>

            <div className="text-center text-xs text-slate-500">
              Don't have an account?{' '}
              <Link to="/register" className="font-semibold text-brand-600 hover:text-brand-700">
                Create Account
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
