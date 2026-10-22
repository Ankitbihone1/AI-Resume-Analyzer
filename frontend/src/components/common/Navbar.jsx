import React, { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { FileText, LogOut, LayoutDashboard, User, Menu, X, ArrowRight } from 'lucide-react';

export default function Navbar() {
  const { user, isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const isAuthPage = location.pathname === '/login' || location.pathname === '/register';

  const scrollToSection = (e, sectionId) => {
    e.preventDefault();
    setMobileMenuOpen(false);
    if (location.pathname === '/') {
      const element = document.getElementById(sectionId);
      if (element) {
        element.scrollIntoView({ behavior: 'smooth', block: 'start' });
      }
    } else {
      navigate(`/#${sectionId}`);
    }
  };

  return (
    <nav className="bg-white/90 backdrop-blur-md border-b border-slate-200/80 sticky top-0 z-40 transition-all">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16 items-center">
          {/* Logo & Brand */}
          <Link to="/" className="flex items-center space-x-3 group">
            <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-brand-700 via-brand-600 to-indigo-500 flex items-center justify-center text-white shadow-md group-hover:scale-105 transition-transform">
              <FileText className="w-5 h-5 text-white" />
            </div>
            <div className="flex flex-col">
              <span className="text-lg font-bold text-slate-900 tracking-tight leading-none group-hover:text-brand-600 transition-colors">
                AI Resume Analyzer
              </span>
              <span className="text-[11px] font-medium text-slate-500 tracking-wide mt-1">
                Intelligent ATS Matching
              </span>
            </div>
          </Link>

          {/* Desktop Navigation Links */}
          {!isAuthPage && (
            <div className="hidden md:flex items-center space-x-8 text-sm font-medium text-slate-600">
              <a
                href="#features"
                onClick={(e) => scrollToSection(e, 'features')}
                className="hover:text-brand-600 transition-colors cursor-pointer"
              >
                Features
              </a>
              <a
                href="#how-it-works"
                onClick={(e) => scrollToSection(e, 'how-it-works')}
                className="hover:text-brand-600 transition-colors cursor-pointer"
              >
                How It Works
              </a>
              <a
                href="#tech-stack"
                onClick={(e) => scrollToSection(e, 'tech-stack')}
                className="hover:text-brand-600 transition-colors cursor-pointer"
              >
                Tech Stack
              </a>
            </div>
          )}

          {/* Right Action / Auth Buttons */}
          <div className="hidden md:flex items-center space-x-4">
            {isAuthenticated ? (
              <div className="flex items-center space-x-3">
                <Link
                  to="/dashboard"
                  className="inline-flex items-center space-x-2 text-sm font-medium text-slate-700 hover:text-brand-600 px-3 py-2 rounded-lg hover:bg-slate-50 transition"
                >
                  <LayoutDashboard className="w-4 h-4 text-brand-600" />
                  <span>Dashboard</span>
                </Link>

                <div className="h-5 w-[1px] bg-slate-200" />

                <Link
                  to="/profile"
                  className="flex items-center space-x-2 text-sm text-slate-700 hover:text-brand-600 px-2 py-1 rounded-lg"
                  title="View Profile"
                >
                  <div className="w-8 h-8 rounded-full bg-brand-100 text-brand-700 flex items-center justify-center font-bold text-xs border border-brand-200">
                    {user?.fullName ? user.fullName.charAt(0).toUpperCase() : 'U'}
                  </div>
                  <span className="font-medium max-w-[120px] truncate">{user?.fullName || 'My Account'}</span>
                </Link>

                <button
                  onClick={handleLogout}
                  className="p-2 text-slate-500 hover:text-rose-600 hover:bg-rose-50 rounded-lg transition"
                  title="Sign Out"
                >
                  <LogOut className="w-4 h-4" />
                </button>
              </div>
            ) : (
              <div className="flex items-center space-x-3">
                <Link
                  to="/login"
                  className="text-sm font-semibold text-slate-700 hover:text-brand-600 px-3.5 py-2 transition"
                >
                  Sign In
                </Link>
                <Link
                  to="/register"
                  className="btn-primary space-x-2 text-xs font-semibold uppercase tracking-wider"
                >
                  <span>Get Started Free</span>
                  <ArrowRight className="w-4 h-4" />
                </Link>
              </div>
            )}
          </div>

          {/* Mobile Menu Hamburger */}
          <div className="flex md:hidden items-center">
            <button
              onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
              className="p-2 rounded-lg text-slate-600 hover:bg-slate-100 focus:outline-none"
            >
              {mobileMenuOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
            </button>
          </div>
        </div>
      </div>

      {/* Mobile Drawer */}
      {mobileMenuOpen && (
        <div className="md:hidden border-b border-slate-200 bg-white px-4 pt-2 pb-6 space-y-3 shadow-lg">
          <a
            href="#features"
            onClick={(e) => scrollToSection(e, 'features')}
            className="block px-3 py-2 rounded-md text-base font-medium text-slate-700 hover:bg-slate-50 cursor-pointer"
          >
            Features
          </a>
          <a
            href="#how-it-works"
            onClick={(e) => scrollToSection(e, 'how-it-works')}
            className="block px-3 py-2 rounded-md text-base font-medium text-slate-700 hover:bg-slate-50 cursor-pointer"
          >
            How It Works
          </a>
          <a
            href="#tech-stack"
            onClick={(e) => scrollToSection(e, 'tech-stack')}
            className="block px-3 py-2 rounded-md text-base font-medium text-slate-700 hover:bg-slate-50 cursor-pointer"
          >
            Tech Stack
          </a>

          <div className="pt-4 border-t border-slate-200">
            {isAuthenticated ? (
              <div className="space-y-2">
                <Link
                  to="/dashboard"
                  onClick={() => setMobileMenuOpen(false)}
                  className="flex items-center space-x-2 px-3 py-2 text-base font-medium text-brand-600 bg-brand-50 rounded-lg"
                >
                  <LayoutDashboard className="w-5 h-5" />
                  <span>Go to Dashboard</span>
                </Link>
                <Link
                  to="/profile"
                  onClick={() => setMobileMenuOpen(false)}
                  className="flex items-center space-x-2 px-3 py-2 text-base font-medium text-slate-700 hover:bg-slate-50 rounded-lg"
                >
                  <User className="w-5 h-5" />
                  <span>Profile Settings</span>
                </Link>
                <button
                  onClick={() => {
                    setMobileMenuOpen(false);
                    handleLogout();
                  }}
                  className="w-full flex items-center space-x-2 px-3 py-2 text-base font-medium text-rose-600 hover:bg-rose-50 rounded-lg"
                >
                  <LogOut className="w-5 h-5" />
                  <span>Sign Out</span>
                </button>
              </div>
            ) : (
              <div className="grid grid-cols-2 gap-3 pt-2">
                <Link
                  to="/login"
                  onClick={() => setMobileMenuOpen(false)}
                  className="btn-secondary text-center"
                >
                  Sign In
                </Link>
                <Link
                  to="/register"
                  onClick={() => setMobileMenuOpen(false)}
                  className="btn-primary text-center"
                >
                  Register
                </Link>
              </div>
            )}
          </div>
        </div>
      )}
    </nav>
  );
}
