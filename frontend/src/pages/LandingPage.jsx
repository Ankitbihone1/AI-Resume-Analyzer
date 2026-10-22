import React, { useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import Navbar from '../components/common/Navbar';
import Footer from '../components/common/Footer';
import {
  FileText,
  Sparkles,
  CheckCircle2,
  AlertCircle,
  ArrowRight,
  ShieldCheck,
  TrendingUp,
  Cpu,
  Download,
  Target,
  Zap,
} from 'lucide-react';

export default function LandingPage() {
  const location = useLocation();

  useEffect(() => {
    if (location.hash) {
      const id = location.hash.replace('#', '');
      const element = document.getElementById(id);
      if (element) {
        setTimeout(() => {
          element.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }, 80);
      }
    }
  }, [location]);
  const features = [
    {
      icon: Target,
      title: 'Precision ATS Match Score',
      desc: 'Get an estimated 0–100 match score comparing your resume against actual job descriptions using our algorithmic evaluation methodology.',
      color: 'text-brand-600 bg-brand-50',
    },
    {
      icon: CheckCircle2,
      title: 'Skill Gap & Keyword Detection',
      desc: 'Instantly view matching technical competencies, missing critical keywords, and under-represented qualifications.',
      color: 'text-emerald-600 bg-emerald-50',
    },
    {
      icon: Sparkles,
      title: 'Bullet Point Enhancer',
      desc: 'Generate quantifiable, impactful draft bullet points tailored to the job description without fabricating experiences.',
      color: 'text-amber-600 bg-amber-50',
    },
    {
      icon: ShieldCheck,
      title: 'Private & Secure Processing',
      desc: 'All resumes are scoped strictly to your authenticated session. Your data is never exposed publicly or shared with third parties.',
      color: 'text-indigo-600 bg-indigo-50',
    },
    {
      icon: TrendingUp,
      title: 'Historical Analytics',
      desc: 'Track your match score improvements over time across different job titles and companies right from your dashboard.',
      color: 'text-blue-600 bg-blue-50',
    },
    {
      icon: Download,
      title: 'Downloadable PDF Reports',
      desc: 'Export structured, polished evaluation summaries with charts and action items to review offline.',
      color: 'text-purple-600 bg-purple-50',
    },
  ];

  const steps = [
    {
      step: '01',
      title: 'Upload Your Resume PDF',
      desc: 'Drag & drop your resume in PDF format. Our Apache PDFBox parser safely validates and extracts readable text.',
    },
    {
      step: '02',
      title: 'Paste Job Description',
      desc: 'Provide the target job description or specific role requirements you are applying for.',
    },
    {
      step: '03',
      title: 'Review Actionable Report',
      desc: 'Inspect your match score gauge, skill distribution charts, missing keywords, and verify suggested enhancements.',
    },
  ];

  return (
    <div className="min-h-screen bg-slate-50 flex flex-col font-sans selection:bg-brand-500 selection:text-white">
      <Navbar />

      {/* Hero Section */}
      <section className="relative pt-12 pb-20 md:pt-20 md:pb-28 overflow-hidden">
        {/* Subtle decorative background blur */}
        <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[600px] h-[350px] bg-gradient-to-tr from-brand-200/40 to-blue-200/30 rounded-full blur-3xl -z-10 pointer-events-none" />

        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-3xl mx-auto">
            {/* Pill Badge */}
            <div className="inline-flex items-center space-x-2 px-3.5 py-1.5 rounded-full bg-brand-50 border border-brand-200 text-brand-700 text-xs font-semibold uppercase tracking-wider mb-6 shadow-xs">
              <Sparkles className="w-3.5 h-3.5 text-brand-600" />
              <span>Full-Stack AI Resume Evaluation Platform</span>
            </div>

            {/* Main Headline */}
            <h1 className="text-4xl sm:text-5xl lg:text-6xl font-extrabold text-slate-900 tracking-tight leading-[1.15] mb-6">
              Land More Interviews With{' '}
              <span className="bg-gradient-to-r from-brand-600 to-indigo-600 bg-clip-text text-transparent">
                AI Resume Intelligence
              </span>
            </h1>

            {/* Subtitle */}
            <p className="text-lg sm:text-xl text-slate-600 leading-relaxed mb-8 max-w-2xl mx-auto">
              Match your resume against any job description. Uncover hidden keyword gaps, calculate your estimated ATS match score, and enhance bullet points with verified AI suggestions.
            </p>

            {/* CTA Buttons */}
            <div className="flex flex-col sm:flex-row items-center justify-center gap-4">
              <Link
                to="/register"
                className="btn-primary w-full sm:w-auto text-base px-6 py-3.5 space-x-2 shadow-lg shadow-brand-500/25"
              >
                <span>Analyze Your Resume Free</span>
                <ArrowRight className="w-5 h-5" />
              </Link>
              <Link
                to="/login"
                className="btn-secondary w-full sm:w-auto text-base px-6 py-3.5"
              >
                Sign In to Dashboard
              </Link>
            </div>

            {/* Micro assurances */}
            <div className="mt-8 flex flex-wrap items-center justify-center gap-6 text-xs font-medium text-slate-500">
              <span className="flex items-center">
                <CheckCircle2 className="w-4 h-4 text-emerald-500 mr-1.5" />
                No credit card required
              </span>
              <span className="flex items-center">
                <CheckCircle2 className="w-4 h-4 text-emerald-500 mr-1.5" />
                Instant PDFBox text extraction
              </span>
              <span className="flex items-center">
                <CheckCircle2 className="w-4 h-4 text-emerald-500 mr-1.5" />
                Private &amp; user-scoped
              </span>
            </div>
          </div>

          {/* Interactive UI Preview Showcase */}
          <div className="mt-14 max-w-4xl mx-auto">
            <div className="bg-white rounded-2xl border border-slate-200/90 shadow-2xl p-6 md:p-8 relative">
              <div className="flex flex-col md:flex-row items-center justify-between pb-6 border-b border-slate-100 gap-4">
                <div>
                  <span className="badge-neutral text-xs font-semibold mb-1">Live Evaluation Preview</span>
                  <h3 className="text-xl font-bold text-slate-900">
                    Junior Java Full Stack Developer
                  </h3>
                  <p className="text-xs text-slate-500">Alex_Morgan_Resume.pdf • Evaluated in 1.4s</p>
                </div>
                <div className="flex items-center space-x-3">
                  <div className="text-right">
                    <span className="text-3xl font-extrabold text-brand-600">82%</span>
                    <span className="block text-[11px] font-semibold text-emerald-600 uppercase tracking-wider">
                      Strong Match
                    </span>
                  </div>
                  <div className="w-12 h-12 rounded-full border-4 border-brand-500 border-t-brand-200 flex items-center justify-center font-bold text-brand-700 text-sm">
                    82
                  </div>
                </div>
              </div>

              {/* Sample Metrics Grid */}
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4 pt-6">
                <div className="bg-emerald-50/60 border border-emerald-200/80 rounded-xl p-4">
                  <span className="text-xs font-semibold text-emerald-800 uppercase tracking-wider block mb-2">
                    Matching Skills (7 Found)
                  </span>
                  <div className="flex flex-wrap gap-1.5">
                    {['Java 17', 'Spring Boot', 'React.js', 'REST APIs', 'MySQL', 'Docker'].map((s) => (
                      <span key={s} className="px-2 py-0.5 bg-white text-emerald-700 font-medium text-xs rounded border border-emerald-200">
                        ✓ {s}
                      </span>
                    ))}
                  </div>
                </div>

                <div className="bg-amber-50/60 border border-amber-200/80 rounded-xl p-4">
                  <span className="text-xs font-semibold text-amber-800 uppercase tracking-wider block mb-2">
                    Skill Gaps (2 Missing)
                  </span>
                  <div className="flex flex-wrap gap-1.5">
                    {['JUnit 5 / Mockito', 'CI/CD Pipelines'].map((s) => (
                      <span key={s} className="px-2 py-0.5 bg-white text-amber-700 font-medium text-xs rounded border border-amber-200">
                        ! {s}
                      </span>
                    ))}
                  </div>
                </div>

                <div className="bg-brand-50/60 border border-brand-200/80 rounded-xl p-4">
                  <span className="text-xs font-semibold text-brand-800 uppercase tracking-wider block mb-2">
                    Suggested Bullet Enhancement
                  </span>
                  <p className="text-xs text-slate-700 leading-snug line-clamp-3">
                    "Designed &amp; deployed 8+ RESTful APIs with Spring Boot, optimizing indexing to cut database query latency by 25%."
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Features Grid Section */}
      <section id="features" className="scroll-mt-20 py-20 bg-white border-y border-slate-200/80">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-2xl mx-auto mb-16">
            <h2 className="text-3xl font-extrabold text-slate-900 tracking-tight mb-3">
              Built For Serious Tech Job Seekers
            </h2>
            <p className="text-base text-slate-600">
              Modern ATS systems filter out qualified candidates because of formatting or missing keywords. Our system gives you the clarity you need before hitting submit.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {features.map((item, idx) => {
              const Icon = item.icon;
              return (
                <div
                  key={idx}
                  className="card hover:shadow-card-hover hover:border-brand-200 transition-all group"
                >
                  <div className={`w-12 h-12 rounded-xl flex items-center justify-center mb-5 ${item.color}`}>
                    <Icon className="w-6 h-6" />
                  </div>
                  <h3 className="text-lg font-bold text-slate-900 mb-2 group-hover:text-brand-600 transition-colors">
                    {item.title}
                  </h3>
                  <p className="text-sm text-slate-600 leading-relaxed">
                    {item.desc}
                  </p>
                </div>
              );
            })}
          </div>
        </div>
      </section>

      {/* How It Works Section */}
      <section id="how-it-works" className="scroll-mt-20 py-20 bg-slate-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-2xl mx-auto mb-16">
            <span className="text-xs font-semibold uppercase tracking-wider text-brand-600 mb-2 block">
              Effortless Workflow
            </span>
            <h2 className="text-3xl font-extrabold text-slate-900 tracking-tight mb-3">
              From PDF To Optimized In 3 Simple Steps
            </h2>
            <p className="text-base text-slate-600">
              No complicated configuration. Upload, paste requirements, and view tailored recommendations.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {steps.map((item, idx) => (
              <div key={idx} className="bg-white rounded-2xl p-8 border border-slate-200 relative shadow-sm">
                <span className="text-4xl font-black text-brand-200/80 mb-4 block">
                  {item.step}
                </span>
                <h3 className="text-xl font-bold text-slate-900 mb-2">{item.title}</h3>
                <p className="text-sm text-slate-600 leading-relaxed">{item.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Tech Stack Banner for Portfolio Reviewers */}
      <section id="tech-stack" className="scroll-mt-20 py-16 bg-navy-900 text-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex flex-col md:flex-row items-center justify-between gap-8">
            <div>
              <span className="text-xs font-semibold text-brand-400 uppercase tracking-wider block mb-1">
                Developer Portfolio Architecture
              </span>
              <h2 className="text-2xl sm:text-3xl font-bold text-white tracking-tight">
                Crafted With Production-Grade Java Full Stack Architecture
              </h2>
              <p className="text-sm text-slate-300 mt-2 max-w-2xl">
                Demonstrating Java 17, Spring Boot 3, Spring Security JWT, Spring Data JPA, Apache PDFBox, and React 18 SPA.
              </p>
            </div>
            <Link
              to="/register"
              className="btn-primary bg-brand-500 hover:bg-brand-600 text-white font-semibold whitespace-nowrap px-6 py-3"
            >
              Get Started Now
            </Link>
          </div>
        </div>
      </section>

      <Footer />
    </div>
  );
}
