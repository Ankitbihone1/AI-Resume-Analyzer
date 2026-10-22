import React from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { FileText, Shield, Sparkles, Code2, Database } from 'lucide-react';

export default function Footer() {
  const location = useLocation();
  const navigate = useNavigate();

  const scrollToSection = (e, sectionId) => {
    e.preventDefault();
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
    <footer className="bg-white border-t border-slate-200 text-slate-600 text-sm">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          {/* Brand & Mission */}
          <div className="space-y-4 md:col-span-1">
            <div className="flex items-center space-x-2.5">
              <div className="w-8 h-8 rounded-lg bg-gradient-to-tr from-brand-700 to-indigo-500 flex items-center justify-center text-white">
                <FileText className="w-4 h-4" />
              </div>
              <span className="font-bold text-slate-900 text-base">AI Resume Analyzer</span>
            </div>
            <p className="text-xs text-slate-500 leading-relaxed">
              Enterprise-grade AI resume evaluation and ATS matching platform built for software engineers, freshers, and tech professionals.
            </p>
            <div className="flex items-center space-x-2 text-xs text-emerald-700 font-medium">
              <Shield className="w-3.5 h-3.5 text-emerald-600" />
              <span>Zero Public Storage • Private &amp; Scoped</span>
            </div>
          </div>

          {/* Quick Navigation */}
          <div>
            <h4 className="text-xs font-semibold uppercase tracking-wider text-slate-900 mb-3">
              Application
            </h4>
            <ul className="space-y-2 text-xs text-slate-600">
              <li>
                <a
                  href="#features"
                  onClick={(e) => scrollToSection(e, 'features')}
                  className="hover:text-brand-600 transition cursor-pointer"
                >
                  Platform Features
                </a>
              </li>
              <li>
                <a
                  href="#how-it-works"
                  onClick={(e) => scrollToSection(e, 'how-it-works')}
                  className="hover:text-brand-600 transition cursor-pointer"
                >
                  How ATS Scoring Works
                </a>
              </li>
              <li>
                <Link to="/login" className="hover:text-brand-600 transition">
                  Sign In
                </Link>
              </li>
              <li>
                <Link to="/register" className="hover:text-brand-600 transition">
                  Create Account
                </Link>
              </li>
            </ul>
          </div>

          {/* Tech Stack Portfolio */}
          <div>
            <h4 className="text-xs font-semibold uppercase tracking-wider text-slate-900 mb-3">
              Technology Architecture
            </h4>
            <ul className="space-y-2 text-xs text-slate-600">
              <li className="flex items-center space-x-1.5">
                <Code2 className="w-3.5 h-3.5 text-brand-600" />
                <span>Spring Boot 3 + Spring Security JWT</span>
              </li>
              <li className="flex items-center space-x-1.5">
                <Sparkles className="w-3.5 h-3.5 text-brand-600" />
                <span>React 18 + Vite + Tailwind CSS</span>
              </li>
              <li className="flex items-center space-x-1.5">
                <Database className="w-3.5 h-3.5 text-brand-600" />
                <span>MySQL 8.0 Relational Storage</span>
              </li>
              <li className="flex items-center space-x-1.5">
                <FileText className="w-3.5 h-3.5 text-brand-600" />
                <span>Apache PDFBox Text Extraction</span>
              </li>
            </ul>
          </div>

          {/* Scoring Methodology Disclaimer */}
          <div>
            <h4 className="text-xs font-semibold uppercase tracking-wider text-slate-900 mb-3">
              Scoring Methodology
            </h4>
            <p className="text-xs text-slate-500 leading-relaxed">
              Match scores (0–100) are estimates calculated by our proprietary AI matching methodology comparing keywords, experiences, and technical skill distributions. They do not constitute an official hiring guarantee.
            </p>
          </div>
        </div>

        {/* Bottom bar */}
        <div className="mt-8 pt-8 border-t border-slate-100 flex flex-col sm:flex-row items-center justify-between text-xs text-slate-400">
          <p>© 2026 AI Resume Analyzer System. Java Full Stack Developer Portfolio Project.</p>
          <p className="mt-2 sm:mt-0 font-medium text-slate-500">
            Engineered with Java 17, Spring Boot, MySQL, and React
          </p>
        </div>
      </div>
    </footer>
  );
}
