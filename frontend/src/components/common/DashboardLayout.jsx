import React, { useState } from 'react';
import { Outlet, Link, useLocation } from 'react-router-dom';
import Sidebar from './Sidebar';
import { useAuth } from '../../context/AuthContext';
import { Menu, PlusCircle, Bell, Search, Sparkles } from 'lucide-react';

export default function DashboardLayout() {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const { user } = useAuth();
  const location = useLocation();

  const getPageTitle = (pathname) => {
    if (pathname.includes('/upload')) return 'New Resume Analysis';
    if (pathname.includes('/results')) return 'Evaluation Report';
    if (pathname.includes('/history')) return 'Analysis History';
    if (pathname.includes('/profile')) return 'Profile & Settings';
    return 'Dashboard Overview';
  };

  return (
    <div className="min-h-screen bg-slate-50 flex flex-col lg:flex-row">
      {/* Responsive Sidebar */}
      <Sidebar isOpen={sidebarOpen} onClose={() => setSidebarOpen(false)} />

      {/* Main Content Area */}
      <div className="flex-1 flex flex-col min-w-0 overflow-hidden">
        {/* Top Bar */}
        <header className="h-16 bg-white border-b border-slate-200/80 px-4 sm:px-6 lg:px-8 flex items-center justify-between sticky top-0 z-20 shadow-xs">
          <div className="flex items-center space-x-3">
            <button
              onClick={() => setSidebarOpen(true)}
              className="lg:hidden p-2 rounded-lg text-slate-600 hover:bg-slate-100 focus:outline-none"
              aria-label="Open sidebar"
            >
              <Menu className="w-5 h-5" />
            </button>
            <h1 className="text-lg font-bold text-slate-900 tracking-tight">
              {getPageTitle(location.pathname)}
            </h1>
          </div>

          <div className="flex items-center space-x-3">
            <Link
              to="/upload"
              className="btn-primary space-x-2 text-xs py-2 px-3 sm:px-4 shadow-sm"
            >
              <PlusCircle className="w-4 h-4" />
              <span className="hidden sm:inline">New Analysis</span>
            </Link>

            <div className="h-6 w-[1px] bg-slate-200 hidden sm:block" />

            <Link
              to="/profile"
              className="flex items-center space-x-2 pl-2 text-slate-700 hover:text-brand-600"
            >
              <div className="w-8 h-8 rounded-full bg-brand-100 text-brand-700 flex items-center justify-center font-bold text-xs border border-brand-200">
                {user?.fullName ? user.fullName.charAt(0).toUpperCase() : 'U'}
              </div>
              <span className="text-xs font-semibold hidden md:block max-w-[130px] truncate">
                {user?.fullName || 'My Account'}
              </span>
            </Link>
          </div>
        </header>

        {/* Dynamic Outlet Body */}
        <main className="flex-1 p-4 sm:p-6 lg:p-8 max-w-7xl w-full mx-auto">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
