import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import axiosClient from '../api/axiosClient';
import {
  FileText,
  Upload,
  TrendingUp,
  Award,
  Clock,
  ArrowRight,
  Eye,
  Trash2,
  AlertCircle,
  PlusCircle,
  Sparkles,
  CheckCircle2,
  BarChart3,
} from 'lucide-react';

export default function DashboardPage() {
  const { user } = useAuth();
  const navigate = useNavigate();

  const [analyses, setAnalyses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [deletingId, setDeletingId] = useState(null);

  // Default demo record for instant UI evaluation if backend is not seeded yet
  const defaultDemoAnalyses = [
    {
      id: 1,
      originalFilename: 'Alex_Morgan_Resume_2026.pdf',
      targetRole: 'Junior Java Full Stack Developer',
      overallScore: 82,
      qualityAssessment: 'Strong technical foundation in Spring Boot and React. Lacks quantified metrics and CI/CD demonstration.',
      createdAt: new Date().toISOString(),
      matchingSkillsCount: 7,
      missingSkillsCount: 2,
    },
  ];

  // Fetch user analyses
  useEffect(() => {
    let isMounted = true;

    async function fetchAnalyses() {
      try {
        setLoading(true);
        const response = await axiosClient.get('/analyses');
        if (isMounted) {
          if (Array.isArray(response.data) && response.data.length > 0) {
            setAnalyses(response.data);
          } else {
            // If user has no records in DB yet, check if demo user
            if (user?.email === 'demo.developer@example.com') {
              setAnalyses(defaultDemoAnalyses);
            } else {
              setAnalyses([]);
            }
          }
        }
      } catch (err) {
        console.warn('Backend /analyses not reached yet, using default view:', err);
        if (isMounted) {
          if (user?.email === 'demo.developer@example.com') {
            setAnalyses(defaultDemoAnalyses);
          } else {
            setAnalyses([]);
          }
        }
      } finally {
        if (isMounted) setLoading(false);
      }
    }

    fetchAnalyses();

    return () => {
      isMounted = false;
    };
  }, [user]);

  // Handle delete report
  const handleDelete = async (id, e) => {
    e.stopPropagation();
    if (!window.confirm('Are you sure you want to delete this resume evaluation report?')) {
      return;
    }

    try {
      setDeletingId(id);
      await axiosClient.delete(`/analyses/${id}`);
      setAnalyses(analyses.filter((a) => a.id !== id));
    } catch (err) {
      console.warn('Could not delete on backend, removing locally:', err);
      setAnalyses(analyses.filter((a) => a.id !== id));
    } finally {
      setDeletingId(null);
    }
  };

  // Compute summary stats
  const totalAnalyses = analyses.length;
  const latestScore = analyses.length > 0 ? analyses[0].overallScore : 0;
  const averageScore =
    analyses.length > 0
      ? Math.round(analyses.reduce((acc, curr) => acc + (curr.overallScore || 0), 0) / analyses.length)
      : 0;

  const getScoreBadge = (score) => {
    if (score >= 80) return <span className="badge-success">Strong Match ({score}%)</span>;
    if (score >= 60) return <span className="badge-warning">Moderate Match ({score}%)</span>;
    return <span className="badge-danger">Needs Optimization ({score}%)</span>;
  };

  return (
    <div className="space-y-8">
      {/* Welcome & Quick Action Banner */}
      <div className="bg-gradient-to-r from-slate-900 via-navy-800 to-brand-950 rounded-2xl p-6 sm:p-8 text-white shadow-xl relative overflow-hidden">
        <div className="relative z-10 flex flex-col md:flex-row md:items-center justify-between gap-6">
          <div className="max-w-2xl">
            <div className="inline-flex items-center space-x-1.5 px-3 py-1 rounded-full bg-brand-500/20 border border-brand-400/30 text-brand-300 text-xs font-semibold uppercase tracking-wider mb-3">
              <Sparkles className="w-3.5 h-3.5 text-brand-400" />
              <span>Resume Intelligence Center</span>
            </div>
            <h2 className="text-2xl sm:text-3xl font-extrabold tracking-tight text-white mb-2">
              Welcome back, {user?.fullName || 'Candidate'}!
            </h2>
            <p className="text-slate-300 text-sm sm:text-base leading-relaxed">
              Target Career Role: <span className="text-white font-semibold">{user?.targetRole || 'Software Engineer'}</span>. 
              Upload a new resume against any job description to calculate your ATS match score and discover critical keyword gaps.
            </p>
          </div>

          <div className="shrink-0">
            <Link
              to="/upload"
              className="btn-primary bg-brand-500 hover:bg-brand-600 text-white font-semibold px-6 py-3 space-x-2 shadow-lg shadow-brand-500/30"
            >
              <PlusCircle className="w-5 h-5" />
              <span>Analyze New Resume</span>
            </Link>
          </div>
        </div>
      </div>

      {/* Key Metric Summary Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
        {/* Metric 1 */}
        <div className="card hover:shadow-card-hover transition-all">
          <div className="flex items-center justify-between mb-3">
            <span className="text-xs font-semibold uppercase tracking-wider text-slate-500">
              Total Resumes
            </span>
            <div className="w-9 h-9 rounded-lg bg-blue-50 text-blue-600 flex items-center justify-center">
              <FileText className="w-5 h-5" />
            </div>
          </div>
          <div className="text-3xl font-extrabold text-slate-900">{totalAnalyses}</div>
          <p className="text-xs text-slate-500 mt-1">Evaluations saved in database</p>
        </div>

        {/* Metric 2 */}
        <div className="card hover:shadow-card-hover transition-all">
          <div className="flex items-center justify-between mb-3">
            <span className="text-xs font-semibold uppercase tracking-wider text-slate-500">
              Latest Match Score
            </span>
            <div className="w-9 h-9 rounded-lg bg-emerald-50 text-emerald-600 flex items-center justify-center">
              <Award className="w-5 h-5" />
            </div>
          </div>
          <div className="text-3xl font-extrabold text-slate-900">
            {latestScore > 0 ? `${latestScore}%` : 'N/A'}
          </div>
          <div className="mt-1">
            {latestScore > 0 ? getScoreBadge(latestScore) : <span className="text-xs text-slate-400">No tests yet</span>}
          </div>
        </div>

        {/* Metric 3 */}
        <div className="card hover:shadow-card-hover transition-all">
          <div className="flex items-center justify-between mb-3">
            <span className="text-xs font-semibold uppercase tracking-wider text-slate-500">
              Average Score
            </span>
            <div className="w-9 h-9 rounded-lg bg-brand-50 text-brand-600 flex items-center justify-center">
              <TrendingUp className="w-5 h-5" />
            </div>
          </div>
          <div className="text-3xl font-extrabold text-slate-900">
            {averageScore > 0 ? `${averageScore}%` : 'N/A'}
          </div>
          <p className="text-xs text-slate-500 mt-1">Across all historical analyses</p>
        </div>

        {/* Metric 4 */}
        <div className="card hover:shadow-card-hover transition-all">
          <div className="flex items-center justify-between mb-3">
            <span className="text-xs font-semibold uppercase tracking-wider text-slate-500">
              Scoring Model
            </span>
            <div className="w-9 h-9 rounded-lg bg-purple-50 text-purple-600 flex items-center justify-center">
              <BarChart3 className="w-5 h-5" />
            </div>
          </div>
          <div className="text-sm font-bold text-slate-900">AI ATS Algorithmic</div>
          <p className="text-xs text-slate-500 mt-1">Multi-factor semantic &amp; keyword matching</p>
        </div>
      </div>

      {/* Recent Analysis History Table */}
      <div className="card">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between pb-5 border-b border-slate-200 gap-4">
          <div>
            <h3 className="text-lg font-bold text-slate-900">Recent Resume Analyses</h3>
            <p className="text-xs text-slate-500">
              Detailed evaluations comparing your PDF resumes against specific job descriptions
            </p>
          </div>
          <Link
            to="/history"
            className="text-xs font-semibold text-brand-600 hover:text-brand-700 inline-flex items-center space-x-1"
          >
            <span>View All History</span>
            <ArrowRight className="w-3.5 h-3.5" />
          </Link>
        </div>

        {loading ? (
          <div className="py-12 text-center text-slate-500">
            <div className="w-8 h-8 border-2 border-brand-600 border-t-transparent rounded-full animate-spin mx-auto mb-2" />
            <p className="text-xs font-medium">Loading analysis records...</p>
          </div>
        ) : analyses.length === 0 ? (
          <div className="py-14 text-center max-w-md mx-auto">
            <div className="w-12 h-12 rounded-2xl bg-slate-100 text-slate-400 flex items-center justify-center mx-auto mb-3">
              <Upload className="w-6 h-6" />
            </div>
            <h4 className="text-base font-bold text-slate-900 mb-1">No Resumes Analyzed Yet</h4>
            <p className="text-xs text-slate-500 mb-5 leading-relaxed">
              Upload your PDF resume and paste a target job description to generate your first ATS match evaluation report.
            </p>
            <Link to="/upload" className="btn-primary text-xs space-x-1.5">
              <PlusCircle className="w-4 h-4" />
              <span>Run First Evaluation</span>
            </Link>
          </div>
        ) : (
          <div className="overflow-x-auto mt-4">
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="border-b border-slate-200 text-[11px] font-semibold uppercase tracking-wider text-slate-400 bg-slate-50/50">
                  <th className="py-3 px-4">Resume File</th>
                  <th className="py-3 px-4">Target Job Role</th>
                  <th className="py-3 px-4">Match Score</th>
                  <th className="py-3 px-4">Evaluation Date</th>
                  <th className="py-3 px-4 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 text-sm">
                {analyses.map((item) => (
                  <tr
                    key={item.id}
                    onClick={() => navigate(`/results/${item.id}`)}
                    className="hover:bg-slate-50/80 cursor-pointer transition-colors group"
                  >
                    <td className="py-3.5 px-4 font-medium text-slate-900 flex items-center space-x-2.5">
                      <FileText className="w-4 h-4 text-brand-600 shrink-0" />
                      <span className="truncate max-w-[200px]">{item.originalFilename}</span>
                    </td>
                    <td className="py-3.5 px-4 text-slate-700 font-medium">
                      {item.targetRole}
                    </td>
                    <td className="py-3.5 px-4">
                      {getScoreBadge(item.overallScore)}
                    </td>
                    <td className="py-3.5 px-4 text-xs text-slate-500">
                      {new Date(item.createdAt).toLocaleDateString(undefined, {
                        year: 'numeric',
                        month: 'short',
                        day: 'numeric',
                      })}
                    </td>
                    <td className="py-3.5 px-4 text-right">
                      <div className="inline-flex items-center space-x-2">
                        <Link
                          to={`/results/${item.id}`}
                          onClick={(e) => e.stopPropagation()}
                          className="p-1.5 rounded-lg text-slate-500 hover:text-brand-600 hover:bg-brand-50 transition"
                          title="View Full Report"
                        >
                          <Eye className="w-4 h-4" />
                        </Link>
                        <button
                          onClick={(e) => handleDelete(item.id, e)}
                          disabled={deletingId === item.id}
                          className="p-1.5 rounded-lg text-slate-400 hover:text-rose-600 hover:bg-rose-50 transition"
                          title="Delete Report"
                        >
                          <Trash2 className="w-4 h-4" />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}
