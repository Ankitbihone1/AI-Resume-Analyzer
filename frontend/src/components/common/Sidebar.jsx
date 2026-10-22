import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import {
  LayoutDashboard,
  FilePlus,
  History,
  User,
  LogOut,
  FileText,
  Sparkles,
  ChevronRight,
  ShieldCheck,
} from 'lucide-react';

export default function Sidebar({ isOpen, onClose }) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const navLinks = [
    {
      name: 'Dashboard',
      path: '/dashboard',
      icon: LayoutDashboard,
      description: 'Overview & metrics',
    },
    {
      name: 'New Analysis',
      path: '/upload',
      icon: FilePlus,
      description: 'Upload PDF resume',
      badge: 'AI Powered',
    },
    {
      name: 'Analysis History',
      path: '/history',
      icon: History,
      description: 'Past evaluations',
    },
    {
      name: 'Profile & Settings',
      path: '/profile',
      icon: User,
      description: 'Manage account',
    },
  ];

  return (
    <>
      {/* Mobile Backdrop */}
      {isOpen && (
        <div
          className="fixed inset-0 z-40 bg-slate-900/50 backdrop-blur-sm lg:hidden transition-opacity"
          onClick={onClose}
        />
      )}

      {/* Sidebar Container */}
      <aside
        className={`fixed top-0 bottom-0 left-0 z-50 w-72 bg-white border-r border-slate-200/80 flex flex-col transition-transform duration-300 ease-in-out lg:translate-x-0 lg:static lg:z-auto ${
          isOpen ? 'translate-x-0' : '-translate-x-full'
        }`}
      >
        {/* Brand Header */}
        <div className="h-16 px-6 border-b border-slate-200 flex items-center justify-between">
          <NavLink to="/dashboard" className="flex items-center space-x-3 group">
            <div className="w-9 h-9 rounded-xl bg-gradient-to-tr from-brand-700 to-indigo-500 flex items-center justify-center text-white shadow-md">
              <FileText className="w-5 h-5 text-white" />
            </div>
            <div>
              <span className="text-base font-bold text-slate-900 tracking-tight group-hover:text-brand-600 transition-colors">
                AI Resume Analyzer
              </span>
              <p className="text-[10px] uppercase font-semibold text-brand-600 tracking-wider">
                ATS Intelligence
              </p>
            </div>
          </NavLink>
        </div>

        {/* Navigation Items */}
        <div className="flex-1 px-4 py-6 space-y-1.5 overflow-y-auto">
          <div className="px-3 pb-2 text-[11px] font-semibold uppercase tracking-wider text-slate-400">
            Main Menu
          </div>

          {navLinks.map((item) => {
            const Icon = item.icon;
            return (
              <NavLink
                key={item.path}
                to={item.path}
                onClick={onClose}
                className={({ isActive }) =>
                  `group flex items-center justify-between px-3 py-2.5 rounded-xl text-sm font-medium transition-all ${
                    isActive
                      ? 'bg-brand-50 text-brand-700 font-semibold shadow-sm border border-brand-100'
                      : 'text-slate-600 hover:bg-slate-50 hover:text-slate-900'
                  }`
                }
              >
                <div className="flex items-center space-x-3">
                  <div
                    className="p-1.5 rounded-lg text-slate-500 group-hover:text-brand-600 transition-colors"
                  >
                    <Icon className="w-5 h-5" />
                  </div>
                  <div>
                    <span className="block leading-tight">{item.name}</span>
                    <span className="block text-[11px] text-slate-400 font-normal leading-tight mt-0.5">
                      {item.description}
                    </span>
                  </div>
                </div>

                {item.badge && (
                  <span className="inline-flex items-center space-x-1 text-[10px] font-semibold uppercase tracking-wider px-2 py-0.5 rounded-full bg-brand-100 text-brand-700 border border-brand-200">
                    <Sparkles className="w-2.5 h-2.5" />
                    <span>{item.badge}</span>
                  </span>
                )}
              </NavLink>
            );
          })}
        </div>

        {/* Bottom User Card & Logout */}
        <div className="p-4 border-t border-slate-200 bg-slate-50/50">
          <div className="bg-white p-3 rounded-xl border border-slate-200/80 shadow-xs mb-3">
            <div className="flex items-center space-x-3">
              <div className="w-10 h-10 rounded-full bg-brand-100 text-brand-700 flex items-center justify-center font-bold text-sm border border-brand-200">
                {user?.fullName ? user.fullName.charAt(0).toUpperCase() : 'U'}
              </div>
              <div className="flex-1 min-w-0">
                <p className="text-sm font-semibold text-slate-900 truncate">
                  {user?.fullName || 'User Account'}
                </p>
                <p className="text-xs text-slate-500 truncate">{user?.email}</p>
                {user?.targetRole && (
                  <p className="text-[11px] font-medium text-brand-600 truncate mt-0.5">
                    {user.targetRole}
                  </p>
                )}
              </div>
            </div>
          </div>

          <button
            onClick={handleLogout}
            className="w-full flex items-center justify-center space-x-2 px-3 py-2 text-xs font-semibold text-rose-600 hover:text-rose-700 bg-rose-50/60 hover:bg-rose-100/80 rounded-lg transition"
          >
            <LogOut className="w-4 h-4" />
            <span>Sign Out Session</span>
          </button>
        </div>
      </aside>
    </>
  );
}
