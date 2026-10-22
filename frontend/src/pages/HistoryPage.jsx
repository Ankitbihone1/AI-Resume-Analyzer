import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axiosClient from '../api/axiosClient';
import { useAuth } from '../context/AuthContext';
import {
  FileText,
  Search,
  Trash2,
  Eye,
  PlusCircle,
  Filter,
  ArrowRight,
  Sparkles,
} from 'lucide-react';

export default function HistoryPage() {
  const { user } = useAuth();
  const navigate = useNavigate();

  const [analyses, setAnalyses] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [filterScore, setFilterScore] = useState('ALL');
  const [loading, setLoading] = useState(true);
  const [deletingId, setDeletingId] = useState(null);

  const defaultDemoAnalyses = [
    {
      id: 1,
      originalFilename: 'Alex_Morgan_Resume_2026.pdf',
      targetRole: 'Junior Java Full Stack Developer',
      overallScore: 82,
      createdAt: new Date().toISOString(),
    },
  ];

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
            setAnalyses(user?.email === 'demo.developer@example.com' ? defaultDemoAnalyses : []);
          }
        }
      } catch (err) {
        if (isMounted) {
          setAnalyses(user?.email === 'demo.developer@example.com' ? defaultDemoAnalyses : []);
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

  const handleDelete = async (id, e) => {
    e.stopPropagation();
    if (!window.confirm('Are you sure you want to delete this evaluation report?')) {
      return;
    }

    try {
      setDeletingId(id);
      await axiosClient.delete(`/analyses/${id}`);
      setAnalyses(analyses.filter((a) => a.id !== id));
    } catch (err) {
      setAnalyses(analyses.filter((a) => a.id !== id));
    } finally {
      setDeletingId(null);
    }
  };

  // Filter items
  const filteredAnalyses = analyses.filter((item) => {
    const matchesSearch =
      item.targetRole?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      item.originalFilename?.toLowerCase().includes(searchTerm.toLowerCase());

    if (!matchesSearch) return false;

    if (filterScore === 'HIGH') return item.overallScore >= 80;
    if (filterScore === 'MEDIUM') return item.overallScore >= 60 && item.overallScore < 80;
    if (filterScore === 'LOW') return item.overallScore < 60;

    return true;
  });

  return (
    <div className="space-y-6 max-w-5xl mx-auto">
      {/* Header & Quick Action */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-2xl font-bold text-slate-900 tracking-tight">Analysis History</h2>
          <p className="text-xs text-slate-500">
            View, reopen, or remove past resume evaluations scoped to your account
          </p>
        </div>

        <Link to="/upload" className="btn-primary text-xs space-x-1.5 self-start sm:self-auto">
          <PlusCircle className="w-4 h-4" />
          <span>New Analysis</span>
        </Link>
      </div>

      {/* Search & Filter Bar */}
      <div className="card p-4">
        <div className="flex flex-col sm:flex-row items-center gap-3">
          <div className="relative flex-1 w-full">
            <Search className="w-4 h-4 text-slate-400 absolute left-3 top-3" />
            <input
              type="text"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              placeholder="Filter by role title or filename..."
              className="input-field pl-9 py-2 text-xs"
            />
          </div>

          <div className="flex items-center space-x-2 w-full sm:w-auto">
            <Filter className="w-4 h-4 text-slate-400" />
            <select
              value={filterScore}
              onChange={(e) => setFilterScore(e.target.value)}
              className="input-field py-2 text-xs w-full sm:w-44"
            >
              <option value="ALL">All Match Scores</option>
              <option value="HIGH">Strong Match (80%+)</option>
              <option value="MEDIUM">Moderate (60-79%)</option>
              <option value="LOW">Needs Work (&lt;60%)</option>
            </select>
          </div>
        </div>
      </div>

      {/* List or Table */}
      <div className="card p-0 overflow-hidden">
        {loading ? (
          <div className="py-16 text-center text-slate-500">
            <div className="w-8 h-8 border-2 border-brand-600 border-t-transparent rounded-full animate-spin mx-auto mb-2" />
            <p className="text-xs font-semibold">Loading history records...</p>
          </div>
        ) : filteredAnalyses.length === 0 ? (
          <div className="py-16 text-center max-w-sm mx-auto px-4">
            <FileText className="w-10 h-10 text-slate-300 mx-auto mb-3" />
            <h4 className="text-sm font-bold text-slate-800 mb-1">No Evaluation Records Found</h4>
            <p className="text-xs text-slate-500 mb-4">
              {searchTerm || filterScore !== 'ALL'
                ? 'No evaluations match your search filter.'
                : 'You have not evaluated any resumes yet.'}
            </p>
            <Link to="/upload" className="btn-primary text-xs space-x-1">
              <PlusCircle className="w-3.5 h-3.5" />
              <span>Evaluate A Resume</span>
            </Link>
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="border-b border-slate-200 text-[11px] font-semibold uppercase tracking-wider text-slate-400 bg-slate-50/70">
                  <th className="py-3 px-4">Target Job Role</th>
                  <th className="py-3 px-4">Resume File</th>
                  <th className="py-3 px-4">Score</th>
                  <th className="py-3 px-4">Date</th>
                  <th className="py-3 px-4 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 text-sm">
                {filteredAnalyses.map((item) => (
                  <tr
                    key={item.id}
                    onClick={() => navigate(`/results/${item.id}`)}
                    className="hover:bg-slate-50 cursor-pointer transition group"
                  >
                    <td className="py-3.5 px-4 font-semibold text-slate-900">
                      {item.targetRole}
                    </td>
                    <td className="py-3.5 px-4 text-slate-600 text-xs flex items-center space-x-2">
                      <FileText className="w-4 h-4 text-brand-600 shrink-0" />
                      <span className="truncate max-w-[200px]">{item.originalFilename}</span>
                    </td>
                    <td className="py-3.5 px-4">
                      <span
                        className={`inline-flex items-center px-2 py-0.5 rounded text-xs font-bold ${
                          item.overallScore >= 80
                            ? 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                            : item.overallScore >= 60
                            ? 'bg-amber-50 text-amber-700 border border-amber-200'
                            : 'bg-rose-50 text-rose-700 border border-rose-200'
                        }`}
                      >
                        {item.overallScore}%
                      </span>
                    </td>
                    <td className="py-3.5 px-4 text-xs text-slate-500">
                      {new Date(item.createdAt).toLocaleDateString()}
                    </td>
                    <td className="py-3.5 px-4 text-right">
                      <div className="inline-flex items-center space-x-2">
                        <Link
                          to={`/results/${item.id}`}
                          onClick={(e) => e.stopPropagation()}
                          className="p-1.5 rounded-lg text-slate-500 hover:text-brand-600 hover:bg-brand-50 transition"
                          title="View Report"
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
